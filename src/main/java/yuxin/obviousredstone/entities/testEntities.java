package yuxin.obviousredstone.entities;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class testEntities {
    private testEntities() {
    }

    private static final Identifier TEST_ENTITY_ID = Identifier.fromNamespaceAndPath("obviousredstone", "test_entity");
    private static final ResourceKey<EntityType<?>> TEST_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            TEST_ENTITY_ID);

    public static final EntityType<testEntity> TEST_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            TEST_ENTITY_ID,
            EntityType.Builder.of(testEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F)
                    .build(TEST_ENTITY_KEY));


        private static final Identifier JIAO_ENTITY_ID = Identifier.fromNamespaceAndPath("obviousredstone", "jiao_entity");
    private static final ResourceKey<EntityType<?>> JIAO_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, JIAO_ENTITY_ID);

    public static final EntityType<JiaoEntity> JIAO_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            JIAO_ENTITY_ID,
            EntityType.Builder.of(JiaoEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F) // 宽度0.6，高度1.8（两格）
                    .build(JIAO_ENTITY_KEY));

                    
    public static void initialize() {
    }
}
