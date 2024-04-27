/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="OldHitting", description="faq", category=ModuleCategory.RENDER)
public final class OldHitting
extends Module {
    @JvmField
    public static final IntegerValue SpeedSwing;
    private final BoolValue onlySword;
    @JvmField
    public static FloatValue itemPosZ;
    @JvmField
    public static FloatValue itemPosY;
    @JvmField
    public static FloatValue itemPosX;
    @JvmField
    public static FloatValue Scale;
    @JvmField
    public static final ListValue guiAnimations;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"MineCraft", "Reverse", "Strange", "ETB", "Test", "Jello", "SigmaOld", "Zoom", "Push", "SideDown"}, "MineCraft");
    public static final Companion Companion;

    public OldHitting() {
        this.onlySword = new BoolValue("Only-Sword", true);
    }

    static {
        Companion = new Companion(null);
        SpeedSwing = new IntegerValue("SpeedSwing", 4, 0, 20);
        itemPosX = new FloatValue("itemPosX", 0.0f, -1.0f, 1.0f);
        itemPosY = new FloatValue("itemPosY", 0.0f, -1.0f, 1.0f);
        itemPosZ = new FloatValue("itemPosZ", 0.0f, -1.0f, 1.0f);
        Scale = new FloatValue("Scale", 1.0f, 0.0f, 2.0f);
        guiAnimations = new ListValue("Container-Animation", new String[]{"None", "Zoom", "VSlide", "HSlide", "HVSlide"}, "Zoom");
    }

    public final BoolValue getOnlySword() {
        return this.onlySword;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

