package net.ccbluex.liquidbounce.features.module;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u00002\b0\u00000B\b0¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\bj\b\tj\b\nj\bj\b\fj\b\rj\bj\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "", "displayName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getDisplayName", "()Ljava/lang/String;", "namee", "getNamee", "COMBAT", "PLAYER", "MOVEMENT", "RENDER", "WORLD", "MISC", "EXPLOIT", "COLOR", "HYT", "Pride"})
public final class ModuleCategory
extends Enum<ModuleCategory> {
    public static final ModuleCategory COMBAT;
    public static final ModuleCategory PLAYER;
    public static final ModuleCategory MOVEMENT;
    public static final ModuleCategory RENDER;
    public static final ModuleCategory WORLD;
    public static final ModuleCategory MISC;
    public static final ModuleCategory EXPLOIT;
    public static final ModuleCategory COLOR;
    public static final ModuleCategory HYT;
    private static final ModuleCategory[] $VALUES;
    @Nullable
    private final String namee;
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
        moduleCategoryArray[7] = COLOR = new ModuleCategory("Color");
        moduleCategoryArray[8] = HYT = new ModuleCategory("Hyt");
        $VALUES = moduleCategoryArray;
    }

    @Nullable
    public final String getNamee() {
        return this.namee;
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
