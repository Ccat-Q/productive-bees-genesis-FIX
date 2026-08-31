package com.ccatq.pbgconfigsyncfix.mixin;

import net.neoforged.fml.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Keeps PBG's file-based legacy migration out of synced in-memory configs.
 */
@Mixin(targets = "com.ayoshiko.productivebeesgenesis.config.BalanceConfigCompatibility", remap = false)
public abstract class BalanceConfigCompatibilityMixin {
    private static final String PRODUCTIVE_BEES_GENESIS = "productivebeesgenesis";

    @Inject(
            method = "migrateLegacyConfig",
            at = @At("HEAD"),
            cancellable = true,
            require = 1,
            remap = false
    )
    private static void pbgConfigSyncFix$skipLegacyMigrationForSyncedConfig(
            ModConfig config, CallbackInfoReturnable<Boolean> callbackInfo) {
        Object loadedConfig = config == null ? null : config.getLoadedConfig();
        if (config != null
                && PRODUCTIVE_BEES_GENESIS.equals(config.getModId())
                && config.getType() == ModConfig.Type.SERVER
                && loadedConfig instanceof LoadedConfigAccessor pathAccessor
                && pathAccessor.pbgConfigSyncFix$getPath() == null) {
            callbackInfo.setReturnValue(false);
        }
    }
}
