package com.ccatq.pbgconfigsyncfix.mixin;

import java.nio.file.Path;

import net.neoforged.fml.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Prevents Productive Bees Genesis 1.0.5 from treating a synced SERVER config
 * as a file-backed configuration during its Reloading listener.
 */
@Mixin(targets = "com.ayoshiko.productivebeesgenesis.ProductiveBeesGenesis", remap = false)
public abstract class ProductiveBeesGenesisConfigSyncMixin {
    private static final String PRODUCTIVE_BEES_GENESIS = "productivebeesgenesis";
    private static final Path SYNCED_SERVER_CONFIG_PLACEHOLDER = Path.of("productivebeesgenesis-server.toml");

    @Redirect(
            method = "lambda$registerConfigListeners$1(Lnet/neoforged/fml/event/config/ModConfigEvent$Reloading;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/fml/config/ModConfig;getFullPath()Ljava/nio/file/Path;"
            ),
            remap = false,
            require = 1
    )
    private static Path pbgConfigSyncFix$getFullPathOrSyncedPlaceholder(ModConfig config) {
        try {
            return config.getFullPath();
        } catch (IllegalStateException exception) {
            if (PRODUCTIVE_BEES_GENESIS.equals(config.getModId()) && config.getType() == ModConfig.Type.SERVER) {
                return SYNCED_SERVER_CONFIG_PLACEHOLDER;
            }
            throw exception;
        }
    }
