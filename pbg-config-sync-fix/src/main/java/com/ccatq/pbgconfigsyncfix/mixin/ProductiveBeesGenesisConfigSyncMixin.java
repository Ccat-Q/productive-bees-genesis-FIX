package com.ccatq.pbgconfigsyncfix.mixin;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents Productive Bees Genesis 1.0.5 from treating a synced SERVER config
 * as a file-backed configuration during its Reloading listener.
 */
@Mixin(targets = "com.ayoshiko.productivebeesgenesis.ProductiveBeesGenesis", remap = false)
public abstract class ProductiveBeesGenesisConfigSyncMixin {
    private static final String PRODUCTIVE_BEES_GENESIS = "productivebeesgenesis";

    /**
     * The target is the 1.0.5 bytecode method
     * {@code lambda$registerConfigListeners$1(Reloading)V}. We intentionally
     * select it by name: it has no overload and its synthetic descriptor is
     * not stable input to Mixin's selector parser.
     */
    @Inject(
            method = "lambda$registerConfigListeners$1",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/fml/config/ModConfig;getFullPath()Ljava/nio/file/Path;",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            remap = false,
            require = 1,
            cancellable = true
    )
    private static void pbgConfigSyncFix$skipPathOnlyRetryForSyncedServerConfig(
            ModConfigEvent.Reloading event, CallbackInfo callbackInfo) {
        ModConfig config = event.getConfig();
        Object loadedConfig = config.getLoadedConfig();
        if (PRODUCTIVE_BEES_GENESIS.equals(config.getModId())
                && config.getType() == ModConfig.Type.SERVER
                && loadedConfig instanceof LoadedConfigAccessor pathAccessor
                && pathAccessor.pbgConfigSyncFix$getPath() == null) {
            // All preceding PBG reload work has already run. The remaining
            // instruction only reports a retry for other mods and is the sole
            // consumer of getFullPath() in this callback.
            callbackInfo.cancel();
        }
    }
}
