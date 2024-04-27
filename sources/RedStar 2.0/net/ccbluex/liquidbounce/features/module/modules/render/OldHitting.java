package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="OldHitting", description="faq", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\b\u0000 \r20:\rB¢J0J\f0R0X¢\n\u0000R0X¢\n\u0000R0\b8VX¢\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/OldHitting;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onlySword", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "tag", "", "getTag", "()Ljava/lang/String;", "getModeValue", "getOnlySword", "Companion", "Pride"})
public final class OldHitting
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"MineCraft", "Reverse", "Strange", "ETB", "Test", "Jello", "SigmaOld", "Zoom", "Push", "SideDown"}, "MineCraft");
    private final BoolValue onlySword = new BoolValue("Only-Sword", true);
    @JvmField
    @NotNull
    public static final IntegerValue SpeedSwing;
    @JvmField
    @NotNull
    public static FloatValue itemPosX;
    @JvmField
    @NotNull
    public static FloatValue itemPosY;
    @JvmField
    @NotNull
    public static FloatValue itemPosZ;
    @JvmField
    @NotNull
    public static FloatValue Scale;
    @JvmField
    @Nullable
    public static final ListValue guiAnimations;
    public static final Companion Companion;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @NotNull
    public final BoolValue getOnlySword() {
        return this.onlySword;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\u0000\n\b\n\n\u0000\n\n\u0000\n\n\b\b\u000020B\b¢R08@X¢\n\u0000R08X¢\n\u0000R0\b8X¢\n\u0000R\t08@X¢\n\u0000R\n08@X¢\n\u0000R08@X¢\n\u0000¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/OldHitting$Companion;", "", "()V", "Scale", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "SpeedSwing", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "guiAnimations", "Lnet/ccbluex/liquidbounce/value/ListValue;", "itemPosX", "itemPosY", "itemPosZ", "Pride"})
    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
