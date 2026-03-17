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


public class FanBlock {

     private FanBlock() {
    }

  public static final Block CUSTOM_BLOCK = registerBlock("fan_block",
            new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK,
                            Identifier.fromNamespaceAndPath("obviousredstone", "fan_block")))));

    public static final Item CUSTOM_BLOCK_ITEM = registerBlockItem("fan_block", CUSTOM_BLOCK);

    private static Block registerBlock(String path, Block block) {
        Identifier id = Identifier.fromNamespaceAndPath("obviousredstone", path);
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static Item registerBlockItem(String path, Block block) {
        Identifier id = Identifier.fromNamespaceAndPath("obviousredstone", path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        return Registry.register(BuiltInRegistries.ITEM, id,
                new BlockItem(block, new Item.Properties().setId(key)));
    }

    public static void initialize() {
    }
}

