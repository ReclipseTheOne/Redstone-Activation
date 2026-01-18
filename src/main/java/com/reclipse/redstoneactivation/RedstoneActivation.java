package com.reclipse.redstoneactivation;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(RedstoneActivation.MODID)
public class RedstoneActivation {

    public static final String MODID = "redstoneactivation";

    public RedstoneActivation() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }
}
