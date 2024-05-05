/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\r\u0010\u0007\u001a\u00020\u0005H\u0007\u00a2\u0006\u0002\b\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/module/ModuleCategory;", "", "displayName", "", "color", "", "(Ljava/lang/String;ILjava/lang/String;I)V", "getColor", "()I", "getDisplayName", "()Ljava/lang/String;", "getColor1", "COMBAT", "PLAYER", "MOVEMENT", "RENDER", "WORLD", "FUN", "KyinoClient"})
public final class ModuleCategory
extends Enum<ModuleCategory> {
    public static final /* enum */ ModuleCategory COMBAT;
    public static final /* enum */ ModuleCategory PLAYER;
    public static final /* enum */ ModuleCategory MOVEMENT;
    public static final /* enum */ ModuleCategory RENDER;
    public static final /* enum */ ModuleCategory WORLD;
    public static final /* enum */ ModuleCategory FUN;
    private static final /* synthetic */ ModuleCategory[] $VALUES;
    @NotNull
    private final String displayName;
    private final int color;

    static {
        ModuleCategory[] moduleCategoryArray = new ModuleCategory[6];
        ModuleCategory[] moduleCategoryArray2 = moduleCategoryArray;
        moduleCategoryArray[0] = COMBAT = new ModuleCategory("Combat", new Color(231, 75, 58, 175).getRGB());
        moduleCategoryArray[1] = PLAYER = new ModuleCategory("Player", new Color(142, 69, 174, 175).getRGB());
        moduleCategoryArray[2] = MOVEMENT = new ModuleCategory("Movement", new Color(46, 205, 111, 175).getRGB());
        moduleCategoryArray[3] = RENDER = new ModuleCategory("Render", new Color(76, 143, 200, 175).getRGB());
        moduleCategoryArray[4] = WORLD = new ModuleCategory("World", new Color(233, 215, 100, 175).getRGB());
        moduleCategoryArray[5] = FUN = new ModuleCategory("Other", new Color(244, 157, 19, 175).getRGB());
        $VALUES = moduleCategoryArray;
    }

    @JvmName(name="getColor1")
    public final int getColor1() {
        return this.color;
    }

    @NotNull
    public final String getDisplayName() {
        return this.displayName;
    }

    public final int getColor() {
        return this.color;
    }

    private ModuleCategory(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public static ModuleCategory[] values() {
        return (ModuleCategory[])$VALUES.clone();
    }

    public static ModuleCategory valueOf(String string) {
        return Enum.valueOf(ModuleCategory.class, string);
    }
}

