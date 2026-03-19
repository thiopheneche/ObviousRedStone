package yuxin.obviousredstone.entities;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.EnumSet;
import java.util.List;

public class JiaoEntity extends PathfinderMob {
    
    public JiaoEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);//让它可以拾取东西
    }

    // 这是一个自定义的辅助方法：它可以把游戏里【所有包含食物组件】的物品收集起来
    // 这样只要玩家手里拿的是能吃的东西，它就会被吸引
    private static Ingredient getAllFoodIngredient() {
        // 1. 从注册表中获取所有物品的数据流
        // 2. 筛选出带有 FOOD 组件的物品
        // 3. 将筛选出的物品打包成一个 Item 数组
        Item[] foodItems = BuiltInRegistries.ITEM.stream()
                .filter(item -> item.components().has(DataComponents.FOOD))
                .toArray(Item[]::new);
        
        // 4. 将打包好的数组直接传给 Ingredient
        return Ingredient.of(foodItems);
    }

    @Override
    protected void registerGoals() {
        // === 常规行为 (Goal) ===
        this.goalSelector.addGoal(0, new FloatGoal(this));
        
        // 1. 近战攻击目标 (必须有这个，它产生仇恨后才会真的走过去挥拳)
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        
        // 2. 被任何食物吸引 (调用了我们上面写的 getAllFoodIngredient 方法)
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, getAllFoodIngredient(), false));
        
        // 3. 随机游荡和环顾
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
         // 发现地上的食物就跑过去捡
        this.goalSelector.addGoal(3, new FindFoodGoal());

        // === 仇恨目标 (Target) 决定它要去打谁 ===
        
        // T1. 受到攻击时反击 (这使得它变成中立生物，像僵尸猪灵一样)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        
        // T2. 主动寻找并攻击可以吃的动物（猪、牛、羊、鸡）
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Pig.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Cow.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, true));
        

       

    }

    // === 拦截服务端的事件广播，在客户端本地生成粒子 ===
    @Override
    public void handleEntityEvent(byte id) {
        // 如果收到的暗号是 60，说明该掉食物残渣了
        if (id == 60) {
            // 看看现在手里拿的是什么食物
            ItemStack stack = this.getItemInHand(InteractionHand.MAIN_HAND);
            if (!stack.isEmpty()) {
                // 纯客户端本地生成，绝对不会引发网络崩溃
                for (int i = 0; i < 3; i++) {
                    this.level().addParticle(
                            new net.minecraft.core.particles.ItemParticleOption(net.minecraft.core.particles.ParticleTypes.ITEM, stack),
                            this.getX(),
                            this.getEyeY() - 0.2D, // 在眼睛下面一点(嘴巴位置)
                            this.getZ(),
                            this.random.nextGaussian() * 0.05D, // 增加一点随机向四周飞溅的效果
                            0.05D,
                            this.random.nextGaussian() * 0.05D
                    );
                }
            }
        } else {
            // 如果是其他官方事件（比如受伤发红），交给原版父类处理
            super.handleEntityEvent(id);
        }
    }




    // === 死亡掉落物逻辑 (1.21.2+ 支持火烧判定) ===
    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource damageSource, boolean recentlyHit) {
        // 先调用父类的默认逻辑
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        
        // 1. 计算掉落数量：1 到 2 个
        int dropCount = this.random.nextInt(2) + 1;
        
        // 2. 核心判定：实体死亡时，身上是否处于“着火”状态？
        if (this.isOnFire()) {
            // 如果身上有火（被火烧死、岩浆烫死、火焰附加砍死），掉落熟猪排
            this.spawnAtLocation(level, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.COOKED_PORKCHOP, dropCount));
        } else {
            // 正常死亡（比如普通剑砍死、摔死、淹死），掉落生猪排
            this.spawnAtLocation(level, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.PORKCHOP, dropCount));
        }
    }



   public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                // 玩家标准走路速度通常在 0.25 左右，这里设置为 0.20
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                // 基础攻击力设置为 2.0 (也就是 1 颗心)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                // >>> 新增：给它装上“鼻子”，设置它能在 16 格外闻到玩家手里的食物 >>>
                .add(Attributes.TEMPT_RANGE, 16.0D); 
    }
    
 
    

    // === 核心机制：拦截伤害结算 ===
    // 1.21.2 版本的攻击判定方法，我们在这里做手脚
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        // 如果它当前正在攻击的目标是玩家
        if (target instanceof Player) {
            // 仅仅播放一下挥手的攻击动画
            this.swing(InteractionHand.MAIN_HAND);
            // 直接返回 true (欺骗它的大脑，让它以为自己打完了，但实际上没有任何伤害产生)
            return true; 
        }
        
        // 如果攻击的不是玩家 (比如猪牛羊)，则正常调用父类方法，造成上面设置的 2.0 伤害
        return super.doHurtTarget(level, target);
    }
    // >>> 新增：重写实体的拾取意愿，让它只对食物感兴趣 >>>
    @Override
    public boolean wantsToPickUp(net.minecraft.server.level.ServerLevel level, ItemStack stack) {
        // 直接返回这个物品是否包含 FOOD 组件，包含就是 true，不包含就是 false
        return stack.has(DataComponents.FOOD);
    }



   
    // === 自定义的寻找食物与进食 AI ===
   // === 自定义的寻找食物与沉浸式进食 AI ===
    class FindFoodGoal extends Goal {
        private ItemEntity targetItem;
        private int eatTick = 0; // >>> 新增：吃东西的计时器

        public FindFoodGoal() {
            // 新增 Goal.Flag.LOOK，这样它吃的时候会低头盯着地上的食物
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            List<ItemEntity> list = JiaoEntity.this.level().getEntitiesOfClass(
                    ItemEntity.class,
                    JiaoEntity.this.getBoundingBox().inflate(8.0D, 3.0D, 8.0D),
                    item -> item.getItem().has(DataComponents.FOOD) && !item.hasPickUpDelay()
            );

            if (list.isEmpty()) {
                return false;
            }
            this.targetItem = list.get(0);
            return true;
        }

        @Override
        public void start() {
            JiaoEntity.this.getNavigation().moveTo(this.targetItem, 1.2D);
            this.eatTick = 0; // 刚发现食物，还没开始吃
        }




        @Override
        public void tick() {
            if (this.targetItem != null && this.targetItem.isAlive()) {
                
                // 阶段一：还在跑向食物的路上
                if (this.eatTick == 0) {
                    JiaoEntity.this.getNavigation().moveTo(this.targetItem, 1.2D);
                    JiaoEntity.this.getLookControl().setLookAt(this.targetItem, 30.0F, 30.0F);

                    // 如果靠近了，进入阶段二！
                    if (JiaoEntity.this.distanceTo(this.targetItem) < 1.5D) {
                        this.eatTick = 40; 
                        JiaoEntity.this.getNavigation().stop(); 
                        
                       
                    }
                } 
                // 阶段二：正在疯狂咀嚼中
                else {
                    this.eatTick--;
                    JiaoEntity.this.getLookControl().setLookAt(this.targetItem, 30.0F, 30.0F);

                   // 每 2 个 tick 响一次吧唧嘴的声音，并喷射一次粒子！
                     if (this.eatTick % 2 == 0) {
                         JiaoEntity.this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
                        // >>> 核心修改：不再发送数据包，而是全服广播一个自定义的实体事件 (代号 60)
                        JiaoEntity.this.level().broadcastEntityEvent(JiaoEntity.this, (byte) 60);
                    }

                    // 阶段三：吃完了
                    if (this.eatTick == 0) {
                        
                        JiaoEntity.this.heal(4.0F);
                        
                        ItemStack stack = this.targetItem.getItem();
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            this.targetItem.discard();
                        } else {
                            this.targetItem.setItem(stack);
                        }
                        
                        // >>> 新增：吃完后，把手里的东西清空，抹抹嘴
                        JiaoEntity.this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        this.targetItem = null;
                    }
                }
            }
        }


        @Override
        public boolean canContinueToUse() {
            // 只要食物还在，或者嘴里的还没嚼完(eatTick > 0)，就继续执行
            return (this.targetItem != null && this.targetItem.isAlive()) || this.eatTick > 0;
        }

        @Override
        public void stop() {
            // AI 中断时，把状态清空
            this.targetItem = null;
            this.eatTick = 0;
        }
    }
}