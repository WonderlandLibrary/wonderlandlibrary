// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.boss;

public final class BossStatus
{
    public static float healthScale;
    public static int statusBarTime;
    public static String bossName;
    public static boolean hasColorModifier;
    private static final String __OBFID = "CL_00000941";
    
    public static void setBossStatus(final IBossDisplayData p_82824_0_, final boolean p_82824_1_) {
        BossStatus.healthScale = p_82824_0_.getHealth() / p_82824_0_.getMaxHealth();
        BossStatus.statusBarTime = 100;
        BossStatus.bossName = p_82824_0_.getDisplayName().getFormattedText();
        BossStatus.hasColorModifier = p_82824_1_;
    }
}
