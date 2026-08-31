package com.ccatq.pbgconfigsyncfix.mixin;

import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Exposes NeoForge's nullable backing path without invoking ModConfig.getFullPath(). */
@Mixin(targets = "net.neoforged.fml.config.LoadedConfig", remap = false)
public interface LoadedConfigAccessor {
    @Accessor("path")
    Path pbgConfigSyncFix$getPath();
}
