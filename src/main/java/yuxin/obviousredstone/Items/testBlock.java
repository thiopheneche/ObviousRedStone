package yuxin.obviousredstone.Items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class testBlock {
    private testBlock() {
    }

    public static final Block CUSTOM_BLOCK = register("custom_block",
            new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK,
                            Identifier.fromNamespaceAndPath("obviousredstone", "custom_block")))));

    public static final Block FAN_BLOCK = register("fan_block",
            new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK,
                            Identifier.fromNamespaceAndPath("obviousredstone", "fan_block")))));

        public static final Block JIAO_BLOCK = register("jiao_block",
            new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK,
                            Identifier.fromNamespaceAndPath("obviousredstone", "fan_block")))));

    private static Block register(String path, Block block) {
        Identifier id = Identifier.fromNamespaceAndPath("obviousredstone", path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, id, block);
        Registry.register(BuiltInRegistries.ITEM, id,
                new BlockItem(registeredBlock, new Item.Properties().setId(key)));
        return registeredBlock;
    }

    public static void initialize() {
    }
}
