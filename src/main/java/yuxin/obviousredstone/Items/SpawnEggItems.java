package yuxin.obviousredstone.Items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import yuxin.obviousredstone.entities.testEntities;

public final class SpawnEggItems {
    private SpawnEggItems() {
    }

    public static final Item JIAO_SPAWN_EGG = registerSpawnEgg("jiao_spawn_egg", testEntities.JIAO_ENTITY);

    private static Item registerSpawnEgg(String path, net.minecraft.world.entity.EntityType<? extends Mob> entityType) {
        Identifier identifier = Identifier.fromNamespaceAndPath("obviousredstone", path);
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        Item.Properties properties = new Item.Properties().setId(resourceKey);

        SpawnEggItem spawnEgg = new TypedSpawnEggItem(entityType, properties);
        return Registry.register(BuiltInRegistries.ITEM, identifier, spawnEgg);
    }

    private static final class TypedSpawnEggItem extends SpawnEggItem {
        private final EntityType<? extends Mob> entityType;

        private TypedSpawnEggItem(EntityType<? extends Mob> entityType, Item.Properties properties) {
            super(properties);
            this.entityType = entityType;
        }

        @Override
        public EntityType<?> getType(ItemStack stack) {
            return this.entityType;
        }

        @Override
        public boolean spawnsEntity(ItemStack stack, EntityType<?> type) {
            return this.entityType == type;
        }
    }

    public static void initialize() {
    }
}
