package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u00002\b0\u00000B\b0¢R0¢\b\n\u0000\bj\bj\b\bj\b\tj\b\nj\bj\b\fj\b\rj\bj\bj\bj\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Cape$CapeStyle;", "", "location", "Lnet/minecraft/util/ResourceLocation;", "(Ljava/lang/String;ILnet/minecraft/util/ResourceLocation;)V", "getLocation", "()Lnet/minecraft/util/ResourceLocation;", "DARK", "ASTOLFO", "LIGHT", "SUNNY", "WYY", "POWERX", "AZRAEL", "TARGET", "FLUX", "LIQUIDBOUNCE", "NOVOLINE", "SPECIAL1", "SPECIAL2", "Pride"})
public static final class Cape$CapeStyle
extends Enum<Cape$CapeStyle> {
    public static final Cape$CapeStyle DARK;
    public static final Cape$CapeStyle ASTOLFO;
    public static final Cape$CapeStyle LIGHT;
    public static final Cape$CapeStyle SUNNY;
    public static final Cape$CapeStyle WYY;
    public static final Cape$CapeStyle POWERX;
    public static final Cape$CapeStyle AZRAEL;
    public static final Cape$CapeStyle TARGET;
    public static final Cape$CapeStyle FLUX;
    public static final Cape$CapeStyle LIQUIDBOUNCE;
    public static final Cape$CapeStyle NOVOLINE;
    public static final Cape$CapeStyle SPECIAL1;
    public static final Cape$CapeStyle SPECIAL2;
    private static final Cape$CapeStyle[] $VALUES;
    @NotNull
    private final ResourceLocation location;

    static {
        Cape$CapeStyle[] capeStyleArray = new Cape$CapeStyle[13];
        Cape$CapeStyle[] capeStyleArray2 = capeStyleArray;
        capeStyleArray[0] = DARK = new Cape$CapeStyle(new ResourceLocation("pride/capes/dark.png"));
        capeStyleArray[1] = ASTOLFO = new Cape$CapeStyle(new ResourceLocation("pride/capes/astolfo.png"));
        capeStyleArray[2] = LIGHT = new Cape$CapeStyle(new ResourceLocation("pride/capes/light.png"));
        capeStyleArray[3] = SUNNY = new Cape$CapeStyle(new ResourceLocation("pride/capes/Sunny.png"));
        capeStyleArray[4] = WYY = new Cape$CapeStyle(new ResourceLocation("pride/capes/Wyy.png"));
        capeStyleArray[5] = POWERX = new Cape$CapeStyle(new ResourceLocation("pride/capes/PowerX.png"));
        capeStyleArray[6] = AZRAEL = new Cape$CapeStyle(new ResourceLocation("pride/capes/azrael.png"));
        capeStyleArray[7] = TARGET = new Cape$CapeStyle(new ResourceLocation("pride/capes/Target.png"));
        capeStyleArray[8] = FLUX = new Cape$CapeStyle(new ResourceLocation("pride/capes/Flux.png"));
        capeStyleArray[9] = LIQUIDBOUNCE = new Cape$CapeStyle(new ResourceLocation("pride/capes/LiquidBounce.png"));
        capeStyleArray[10] = NOVOLINE = new Cape$CapeStyle(new ResourceLocation("pride/capes/Novoline.png"));
        capeStyleArray[11] = SPECIAL1 = new Cape$CapeStyle(new ResourceLocation("pride/capes/special1.png"));
        capeStyleArray[12] = SPECIAL2 = new Cape$CapeStyle(new ResourceLocation("pride/capes/special2.png"));
        $VALUES = capeStyleArray;
    }

    @NotNull
    public final ResourceLocation getLocation() {
        return this.location;
    }

    private Cape$CapeStyle(ResourceLocation location) {
        this.location = location;
    }

    public static Cape$CapeStyle[] values() {
        return (Cape$CapeStyle[])$VALUES.clone();
    }

    public static Cape$CapeStyle valueOf(String string) {
        return Enum.valueOf(Cape$CapeStyle.class, string);
    }
}
