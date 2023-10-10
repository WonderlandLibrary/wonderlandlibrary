// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.legacy;

import com.viaversion.viaversion.api.legacy.bossbar.BossBar;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;

public interface LegacyViaAPI<T>
{
    BossBar createLegacyBossBar(final String p0, final float p1, final BossColor p2, final BossStyle p3);
    
    default BossBar createLegacyBossBar(final String title, final BossColor color, final BossStyle style) {
        return this.createLegacyBossBar(title, 1.0f, color, style);
    }
}
