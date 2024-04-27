/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "", "displayName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getDisplayName", "()Ljava/lang/String;", "COMBAT", "PLAYER", "MOVEMENT", "RENDER", "WORLD", "MISC", "EXPLOIT", "HYT", "COLOR", "LiKingSense"})
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
    public static final /* enum */ ModuleCategory COLOR;
    private static final /* synthetic */ ModuleCategory[] $VALUES;
    @NotNull
    private final String displayName;

    static {
        ModuleCategory[] moduleCategoryArray = new ModuleCategory[9];
        ModuleCategory[] moduleCategoryArray2 = moduleCategoryArray;
        moduleCategoryArray[0] = COMBAT = new ModuleCategory("Combat");
        moduleCategoryArray[1] = PLAYER = new ModuleCategory("Player");
        moduleCategoryArray[2] = MOVEMENT = new ModuleCategory("Movement");
        moduleCategoryArray[3] = RENDER = new ModuleCategory("Render");
        moduleCategoryArray[4] = WORLD = new ModuleCategory("World");
        moduleCategoryArray[5] = MISC = new ModuleCategory("Misc");
        moduleCategoryArray[6] = EXPLOIT = new ModuleCategory("Exploit");
        moduleCategoryArray[7] = HYT = new ModuleCategory("Hyt");
        moduleCategoryArray[8] = COLOR = new ModuleCategory("Color");
        $VALUES = moduleCategoryArray;
    }

    @NotNull
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

