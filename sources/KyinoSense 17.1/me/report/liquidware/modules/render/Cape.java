/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Cape", description="KyinoSense cape settings.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2={"Lme/report/liquidware/modules/render/Cape;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "styleValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getStyleValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "getCapeLocation", "Lnet/minecraft/util/ResourceLocation;", "value", "", "CapeStyle", "KyinoClient"})
public final class Cape
extends Module {
    @NotNull
    private final ListValue styleValue = new ListValue("Style", new String[]{"Flux", "Funny", "Keli", "LiquidBounce", "Novoline", "gamesense"}, "Keli");

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
            string = CapeStyle.LIQUIDBOUNCE.getLocation();
        }
        return string;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f\u00a8\u0006\r"}, d2={"Lme/report/liquidware/modules/render/Cape$CapeStyle;", "", "location", "Lnet/minecraft/util/ResourceLocation;", "(Ljava/lang/String;ILnet/minecraft/util/ResourceLocation;)V", "getLocation", "()Lnet/minecraft/util/ResourceLocation;", "FLUX", "FUNNY", "KELI", "LIQUIDBOUNCE", "NOVOLINE", "GAMESENSE", "KyinoClient"})
    public static final class CapeStyle
    extends Enum<CapeStyle> {
        public static final /* enum */ CapeStyle FLUX;
        public static final /* enum */ CapeStyle FUNNY;
        public static final /* enum */ CapeStyle KELI;
        public static final /* enum */ CapeStyle LIQUIDBOUNCE;
        public static final /* enum */ CapeStyle NOVOLINE;
        public static final /* enum */ CapeStyle GAMESENSE;
        private static final /* synthetic */ CapeStyle[] $VALUES;
        @NotNull
        private final ResourceLocation location;

        static {
            CapeStyle[] capeStyleArray = new CapeStyle[6];
            CapeStyle[] capeStyleArray2 = capeStyleArray;
            capeStyleArray[0] = FLUX = new CapeStyle(new ResourceLocation("liquidbounce/capes/Flux.png"));
            capeStyleArray[1] = FUNNY = new CapeStyle(new ResourceLocation("liquidbounce/capes/Funny.png"));
            capeStyleArray[2] = KELI = new CapeStyle(new ResourceLocation("liquidbounce/capes/keli.png"));
            capeStyleArray[3] = LIQUIDBOUNCE = new CapeStyle(new ResourceLocation("liquidbounce/capes/LiquidBounce.png"));
            capeStyleArray[4] = NOVOLINE = new CapeStyle(new ResourceLocation("liquidbounce/capes/Novoline.png"));
            capeStyleArray[5] = GAMESENSE = new CapeStyle(new ResourceLocation("liquidbounce/capes/gamesense.png"));
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

