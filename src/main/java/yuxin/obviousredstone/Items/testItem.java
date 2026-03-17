package yuxin.obviousredstone.Items;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class testItem {
    private testItem() {
    }

    public static final Item CUSTOM_ITEM = register("custom_item", Item::new, new Item.Properties());

    public static Item register(String path, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Identifier identifier = Identifier.fromNamespaceAndPath("obviousredstone",
                path);
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM,
                identifier);
        Item.Properties keyedSettings = settings.setId(resourceKey);
        return Registry.register(BuiltInRegistries.ITEM, identifier,
                factory.apply(keyedSettings));
    }

    public static void initialize() {
    }
}