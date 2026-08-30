package com.ccatq.pbgconfigsyncfix;

import net.neoforged.fml.common.Mod;

/**
 * Client-side compatibility patch for Productive Bees Genesis 1.0.5.
 *
 * <p>The implementation lives in a Mixin so this mod does not register or
 * replace Productive Bees Genesis configuration itself.</p>
 */
@Mod(PbgConfigSyncFix.MOD_ID)
public final class PbgConfigSyncFix {
    public static final String MOD_ID = "pbg_config_sync_fix";

    public PbgConfigSyncFix() {}
}
