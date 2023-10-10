// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.legacy;

import com.viaversion.viaversion.legacy.bossbar.CommonBoss;
import com.viaversion.viaversion.api.legacy.bossbar.BossBar;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.LegacyViaAPI;

public final class LegacyAPI<T> implements LegacyViaAPI<T>
{
    @Override
    public BossBar createLegacyBossBar(final String title, final float health, final BossColor color, final BossStyle style) {
        return new CommonBoss(title, health, color, style);
    }
}
