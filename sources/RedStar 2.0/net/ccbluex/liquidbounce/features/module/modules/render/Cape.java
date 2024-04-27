package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Cape", description="LiquidBounce+ capes.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\n\n\b\n\n\b\n\n\b\n\n\b\b\u000020:B¢J0\f2\r0\bR0¢\b\n\u0000\bR0\b8VX¢\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Cape;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "styleValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getStyleValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "getCapeLocation", "Lnet/minecraft/util/ResourceLocation;", "value", "CapeStyle", "Pride"})
public final class Cape
extends Module {
    @NotNull
    private final ListValue styleValue = new ListValue("Style", new String[]{"Dark", "Astolfo", "Sunny", "Target", "Wyy", "PowerX", "Azrael", "Flux", "LiquidBounce", "Light", "Novoline", "Special1", "Special2"}, "Dark");

    @NotNull
    public final ListValue getStyleValue() {
        return this.styleValue;
    }

    @NotNull
    public final ResourceLocation getCapeLocation(@NotNull String value) {
        String string;
        Intrinsics.checkParameterIsNotNull(value, "value");
        try {
            string = value;
            boolean bl = false;
            String string2 = string.toUpperCase();
            Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toUpperCase()");
            string = CapeStyle.valueOf(string2).getLocation();
        }
        catch (IllegalArgumentException e) {
            string = CapeStyle.DARK.getLocation();
        }
        return string;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.styleValue.get();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u00002\b0\u00000B\b0¢R0¢\b\n\u0000\bj\bj\b\bj\b\tj\b\nj\bj\b\fj\b\rj\bj\bj\bj\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Cape$CapeStyle;", "", "location", "Lnet/minecraft/util/ResourceLocation;", "(Ljava/lang/String;ILnet/minecraft/util/ResourceLocation;)V", "getLocation", "()Lnet/minecraft/util/ResourceLocation;", "DARK", "ASTOLFO", "LIGHT", "SUNNY", "WYY", "POWERX", "AZRAEL", "TARGET", "FLUX", "LIQUIDBOUNCE", "NOVOLINE", "SPECIAL1", "SPECIAL2", "Pride"})
    public static final class CapeStyle
    extends Enum<CapeStyle> {
        public static final CapeStyle DARK;
        public static final CapeStyle ASTOLFO;
        public static final CapeStyle LIGHT;
        public static final CapeStyle SUNNY;
        public static final CapeStyle WYY;
        public static final CapeStyle POWERX;
        public static final CapeStyle AZRAEL;
        public static final CapeStyle TARGET;
        public static final CapeStyle FLUX;
        public static final CapeStyle LIQUIDBOUNCE;
        public static final CapeStyle NOVOLINE;
        public static final CapeStyle SPECIAL1;
        public static final CapeStyle SPECIAL2;
        private static final CapeStyle[] $VALUES;
        @NotNull
        private final ResourceLocation location;

        static {
            CapeStyle[] capeStyleArray = new CapeStyle[13];
            CapeStyle[] capeStyleArray2 = capeStyleArray;
            capeStyleArray[0] = DARK = new CapeStyle(new ResourceLocation("pride/capes/dark.png"));
            capeStyleArray[1] = ASTOLFO = new CapeStyle(new ResourceLocation("pride/capes/astolfo.png"));
            capeStyleArray[2] = LIGHT = new CapeStyle(new ResourceLocation("pride/capes/light.png"));
            capeStyleArray[3] = SUNNY = new CapeStyle(new ResourceLocation("pride/capes/Sunny.png"));
            capeStyleArray[4] = WYY = new CapeStyle(new ResourceLocation("pride/capes/Wyy.png"));
            capeStyleArray[5] = POWERX = new CapeStyle(new ResourceLocation("pride/capes/PowerX.png"));
            capeStyleArray[6] = AZRAEL = new CapeStyle(new ResourceLocation("pride/capes/azrael.png"));
            capeStyleArray[7] = TARGET = new CapeStyle(new ResourceLocation("pride/capes/Target.png"));
            capeStyleArray[8] = FLUX = new CapeStyle(new ResourceLocation("pride/capes/Flux.png"));
            capeStyleArray[9] = LIQUIDBOUNCE = new CapeStyle(new ResourceLocation("pride/capes/LiquidBounce.png"));
            capeStyleArray[10] = NOVOLINE = new CapeStyle(new ResourceLocation("pride/capes/Novoline.png"));
            capeStyleArray[11] = SPECIAL1 = new CapeStyle(new ResourceLocation("pride/capes/special1.png"));
            capeStyleArray[12] = SPECIAL2 = new CapeStyle(new ResourceLocation("pride/capes/special2.png"));
            $VALUES = capeStyleArray;
        }

        @NotNull
        public final ResourceLocation getLocation() {
            return this.location;
        }

        private CapeStyle(ResourceLocation location) {
            this.location = location;
        }

        public static CapeStyle[] values() {
            return (CapeStyle[])$VALUES.clone();
        }

        public static CapeStyle valueOf(String string) {
            return Enum.valueOf(CapeStyle.class, string);
        }
    }
}
