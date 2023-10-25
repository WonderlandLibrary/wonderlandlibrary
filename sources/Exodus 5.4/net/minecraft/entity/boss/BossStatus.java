/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.boss;

import net.minecraft.entity.boss.IBossDisplayData;

public final class BossStatus {
    public static boolean hasColorModifier;
    public static String bossName;
    public static float healthScale;
    public static int statusBarTime;

    public static void setBossStatus(IBossDisplayData iBossDisplayData, boolean bl) {
        healthScale = iBossDisplayData.getHealth() / iBossDisplayData.getMaxHealth();
        statusBarTime = 100;
        bossName = iBossDisplayData.getDisplayName().getFormattedText();
        hasColorModifier = bl;
    }
}

