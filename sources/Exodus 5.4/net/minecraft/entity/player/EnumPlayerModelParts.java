/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.player;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum EnumPlayerModelParts {
    CAPE(0, "cape"),
    JACKET(1, "jacket"),
    LEFT_SLEEVE(2, "left_sleeve"),
    RIGHT_SLEEVE(3, "right_sleeve"),
    LEFT_PANTS_LEG(4, "left_pants_leg"),
    RIGHT_PANTS_LEG(5, "right_pants_leg"),
    HAT(6, "hat");

    private final IChatComponent field_179339_k;
    private final String partName;
    private final int partMask;
    private final int partId;

    private EnumPlayerModelParts(int n2, String string2) {
        this.partId = n2;
        this.partMask = 1 << n2;
        this.partName = string2;
        this.field_179339_k = new ChatComponentTranslation("options.modelPart." + string2, new Object[0]);
    }

    public int getPartId() {
        return this.partId;
    }

    public IChatComponent func_179326_d() {
        return this.field_179339_k;
    }

    public String getPartName() {
        return this.partName;
    }

    public int getPartMask() {
        return this.partMask;
    }
}

