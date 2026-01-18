package com.reclipse.redstoneactivation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> rewardItemId;
    public static final ItemSupplier rewardItem;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        rewardItemId = builder
                .comment("The item to give when a player activates unlit redstone ore.")
                .comment("Use the full item ID (e.g., 'minecraft:redstone', 'minecraft:diamond').")
                .define("rewardItem", "minecraft:redstone");

        SPEC = builder.build();
        rewardItem = new ItemSupplier();
    }

    /**
     * Lazily resolves the configured item ID to an Item instance.
     * Falls back to redstone if the configured item is invalid.
     */
    public static class ItemSupplier {
        public Item get() {
            String itemId = rewardItemId.get();
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemId));
            return item != Items.AIR ? item : Items.REDSTONE;
        }
    }
}
