package com.reclipse.redstoneactivation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
                .comment("Use the full item ID, optionally followed by an NBT compound (same syntax as /give).")
                .comment("Examples:")
                .comment("  'minecraft:redstone'")
                .comment("  'minecraft:diamond_sword{Enchantments:[{id:\"minecraft:sharpness\",lvl:5}]}'")
                .comment("  'minecraft:potion{Potion:\"minecraft:strong_healing\"}'")
                .define("rewardItem", "minecraft:redstone");

        SPEC = builder.build();
        rewardItem = new ItemSupplier();
    }

    /**
     * Lazily resolves the configured item ID (and optional NBT) to an ItemStack.
     * Falls back to redstone if the configured item is invalid; drops NBT if it fails to parse.
     */
    public static class ItemSupplier {
        public ItemStack getStack() {
            String raw = rewardItemId.get().trim();
            int braceIdx = raw.indexOf('{');
            String itemId = (braceIdx == -1 ? raw : raw.substring(0, braceIdx)).trim();
            String nbt = braceIdx == -1 ? null : raw.substring(braceIdx);

            Item item;
            try {
                item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemId));
            } catch (ResourceLocationException e) {
                item = Items.REDSTONE;
            }
            if (item == Items.AIR) item = Items.REDSTONE;

            ItemStack stack = new ItemStack(item);
            if (nbt != null) {
                try {
                    CompoundTag tag = TagParser.parseTag(nbt);
                    stack.setTag(tag);
                } catch (CommandSyntaxException ignored) {
                }
            }
            return stack;
        }
    }
}
