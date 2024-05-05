/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module;

public final class ModuleCategory
extends Enum<ModuleCategory> {
    public static final /* enum */ ModuleCategory COMBAT;
    public static final /* enum */ ModuleCategory PLAYER;
    public static final /* enum */ ModuleCategory MOVEMENT;
    public static final /* enum */ ModuleCategory RENDER;
    public static final /* enum */ ModuleCategory WORLD;
    public static final /* enum */ ModuleCategory MISC;
    public static final /* enum */ ModuleCategory EXPLOIT;
    public static final /* enum */ ModuleCategory HYT;
    private static final /* synthetic */ ModuleCategory[] $VALUES;
    private final String displayName;

    static {
        ModuleCategory[] moduleCategoryArray = new ModuleCategory[8];
        ModuleCategory[] moduleCategoryArray2 = moduleCategoryArray;
        moduleCategoryArray[0] = COMBAT = new ModuleCategory("Combat");
        moduleCategoryArray[1] = PLAYER = new ModuleCategory("Player");
        moduleCategoryArray[2] = MOVEMENT = new ModuleCategory("Movement");
        moduleCategoryArray[3] = RENDER = new ModuleCategory("Render");
        moduleCategoryArray[4] = WORLD = new ModuleCategory("World");
        moduleCategoryArray[5] = MISC = new ModuleCategory("Misc");
        moduleCategoryArray[6] = EXPLOIT = new ModuleCategory("Exploit");
        moduleCategoryArray[7] = HYT = new ModuleCategory("Hyt");
        $VALUES = moduleCategoryArray;
    }

    public final String getDisplayName() {
        return this.displayName;
    }

    private ModuleCategory(String displayName) {
        this.displayName = displayName;
    }

    public static ModuleCategory[] values() {
        return (ModuleCategory[])$VALUES.clone();
    }

    public static ModuleCategory valueOf(String string) {
        return Enum.valueOf(ModuleCategory.class, string);
    }
}

