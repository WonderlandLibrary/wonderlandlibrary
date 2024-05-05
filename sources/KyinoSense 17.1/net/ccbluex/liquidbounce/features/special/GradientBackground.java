/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.special;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import net.ccbluex.liquidbounce.features.special.GradientBackground$WhenMappings;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002/0B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0002J \u0010'\u001a\u00020$2\u0006\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020$H\u0002J \u0010+\u001a\u00020!2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020-2\u0006\u0010*\u001a\u00020$H\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0019\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010\u00a2\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u00061"}, d2={"Lnet/ccbluex/liquidbounce/features/special/GradientBackground;", "", "()V", "animated", "", "getAnimated", "()Z", "setAnimated", "(Z)V", "gradientSide", "Lnet/ccbluex/liquidbounce/features/special/GradientBackground$GradientSide;", "getGradientSide", "()Lnet/ccbluex/liquidbounce/features/special/GradientBackground$GradientSide;", "setGradientSide", "(Lnet/ccbluex/liquidbounce/features/special/GradientBackground$GradientSide;)V", "gradientSides", "", "getGradientSides", "()[Lnet/ccbluex/liquidbounce/features/special/GradientBackground$GradientSide;", "[Lnet/ccbluex/liquidbounce/features/special/GradientBackground$GradientSide;", "gradients", "", "Lnet/ccbluex/liquidbounce/features/special/GradientBackground$Gradient;", "getGradients", "()Ljava/util/List;", "nowGradient", "getNowGradient", "()Lnet/ccbluex/liquidbounce/features/special/GradientBackground$Gradient;", "setNowGradient", "(Lnet/ccbluex/liquidbounce/features/special/GradientBackground$Gradient;)V", "draw", "", "width", "", "height", "getGradientPercent", "", "time", "", "interpolate", "a", "b", "proportion", "interpolateColor", "col1", "Ljava/awt/Color;", "col2", "Gradient", "GradientSide", "KyinoClient"})
public final class GradientBackground {
    @NotNull
    private static Gradient nowGradient;
    @NotNull
    private static GradientSide gradientSide;
    private static boolean animated;
    @NotNull
    private static final List<Gradient> gradients;
    @NotNull
    private static final GradientSide[] gradientSides;
    public static final GradientBackground INSTANCE;

    @NotNull
    public final Gradient getNowGradient() {
        return nowGradient;
    }

    public final void setNowGradient(@NotNull Gradient gradient) {
        Intrinsics.checkParameterIsNotNull(gradient, "<set-?>");
        nowGradient = gradient;
    }

    @NotNull
    public final GradientSide getGradientSide() {
        return gradientSide;
    }

    public final void setGradientSide(@NotNull GradientSide gradientSide) {
        Intrinsics.checkParameterIsNotNull((Object)gradientSide, "<set-?>");
        GradientBackground.gradientSide = gradientSide;
    }

    public final boolean getAnimated() {
        return animated;
    }

    public final void setAnimated(boolean bl) {
        animated = bl;
    }

    @NotNull
    public final List<Gradient> getGradients() {
        return gradients;
    }

    @NotNull
    public final GradientSide[] getGradientSides() {
        return gradientSides;
    }

    /*
     * WARNING - void declaration
     */
    public final void draw(int width, int height) {
        if (!animated) {
            int col1 = nowGradient.getFrom().getRGB();
            int col2 = nowGradient.getTo().getRGB();
            switch (GradientBackground$WhenMappings.$EnumSwitchMapping$0[gradientSide.ordinal()]) {
                case 1: {
                    RenderUtils.drawGradientSidewaysH(0.0, 0.0, width, height, col1, col2);
                    break;
                }
                case 2: {
                    RenderUtils.drawGradientSidewaysH(0.0, 0.0, width, height, col2, col1);
                    break;
                }
                case 3: {
                    RenderUtils.drawGradientSidewaysV(0.0, 0.0, width, height, col1, col2);
                    break;
                }
                case 4: {
                    RenderUtils.drawGradientSidewaysV(0.0, 0.0, width, height, col2, col1);
                }
            }
            return;
        }
        double posAffect = 0.06666666666666667;
        long time = System.currentTimeMillis();
        int n = 0;
        IntProgression intProgression = RangesKt.step(new IntRange(n, 1500), 100);
        int n2 = intProgression.getFirst();
        int n3 = intProgression.getLast();
        int n4 = intProgression.getStep();
        int n5 = n2;
        int n6 = n3;
        if (n4 >= 0 ? n5 <= n6 : n5 >= n6) {
            while (true) {
                void i;
                float pct1 = this.getGradientPercent(time + (long)i);
                float pct2 = this.getGradientPercent(time + (long)i + (long)100);
                int col1 = this.interpolateColor(nowGradient.getFrom(), nowGradient.getTo(), pct1);
                int col2 = this.interpolateColor(nowGradient.getFrom(), nowGradient.getTo(), pct2);
                double pos = (double)i / 1500.0;
                switch (GradientBackground$WhenMappings.$EnumSwitchMapping$1[gradientSide.ordinal()]) {
                    case 1: {
                        RenderUtils.drawGradientSidewaysH((double)width * pos, 0.0, (double)width * (pos + posAffect), height, col1, col2);
                        break;
                    }
                    case 2: {
                        RenderUtils.drawGradientSidewaysH((double)width * (1.0 - pos), 0.0, (double)width * (1.0 - pos + posAffect), height, col2, col1);
                        break;
                    }
                    case 3: {
                        RenderUtils.drawGradientSidewaysV(0.0, (double)height * pos, width, (double)height * (pos + posAffect), col1, col2);
                        break;
                    }
                    case 4: {
                        RenderUtils.drawGradientSidewaysV(0.0, (double)height * (1.0 - pos), width, (double)height * (1.0 - pos + posAffect), col2, col1);
                    }
                }
                if (i == n3) break;
                i += n4;
            }
        }
    }

    private final float getGradientPercent(long time) {
        long stage = time % (long)3000;
        boolean part = (float)stage / 1500.0f > 1.0f;
        return part ? 1.0f - (float)stage % 1500.0f / 1500.0f : (float)stage % 1500.0f / 1500.0f;
    }

    private final float interpolate(float a, float b, float proportion) {
        return a + (b - a) * proportion;
    }

    /*
     * WARNING - void declaration
     */
    private final int interpolateColor(Color col1, Color col2, float proportion) {
        float[] hsva = Color.RGBtoHSB(col1.getRed(), col1.getGreen(), col1.getBlue(), null);
        float[] hsvb = Color.RGBtoHSB(col2.getRed(), col2.getGreen(), col2.getBlue(), null);
        int n = 0;
        int n2 = 2;
        while (n <= n2) {
            void i;
            hsvb[i] = this.interpolate(hsva[i], hsvb[i], proportion);
            ++i;
        }
        return Color.HSBtoRGB(hsvb[0], hsvb[1], hsvb[2]);
    }

    private GradientBackground() {
    }

    static {
        JsonArray json;
        GradientBackground gradientBackground;
        INSTANCE = gradientBackground = new GradientBackground();
        gradientSide = GradientSide.LEFT;
        animated = true;
        boolean bl = false;
        gradients = new ArrayList();
        gradientSides = GradientSide.values();
        JsonParser jsonParser = new JsonParser();
        InputStream inputStream = GradientBackground.class.getClassLoader().getResourceAsStream("assets/minecraft/fdpclient/misc/gradient.json");
        Intrinsics.checkExpressionValueIsNotNull(inputStream, "GradientBackground::clas\u2026ient/misc/gradient.json\")");
        InputStream inputStream2 = inputStream;
        Charset charset = Charsets.UTF_8;
        JsonParser jsonParser2 = jsonParser;
        boolean bl2 = false;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream2, charset);
        JsonElement jsonElement = jsonParser2.parse((Reader)inputStreamReader);
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser().parse(Gradi\u2026).reader(Charsets.UTF_8))");
        JsonArray jsonArray = json = jsonElement.getAsJsonArray();
        Intrinsics.checkExpressionValueIsNotNull(jsonArray, "json");
        Iterable $this$forEach$iv = (Iterable)jsonArray;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            JsonElement it = (JsonElement)element$iv;
            boolean bl3 = false;
            JsonElement jsonElement2 = it;
            Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "it");
            JsonObject obj = jsonElement2.getAsJsonObject();
            JsonArray colors = obj.getAsJsonArray("colors");
            JsonElement jsonElement3 = obj.get("name");
            Intrinsics.checkExpressionValueIsNotNull(jsonElement3, "obj.get(\"name\")");
            String string = jsonElement3.getAsString();
            Intrinsics.checkExpressionValueIsNotNull(string, "obj.get(\"name\").asString");
            JsonArray jsonArray2 = colors;
            Intrinsics.checkExpressionValueIsNotNull(jsonArray2, "colors");
            Object t = CollectionsKt.first((Iterable)jsonArray2);
            Intrinsics.checkExpressionValueIsNotNull(t, "colors.first()");
            String string2 = ((JsonElement)t).getAsString();
            Intrinsics.checkExpressionValueIsNotNull(string2, "colors.first().asString");
            Object t2 = CollectionsKt.last((Iterable)colors);
            Intrinsics.checkExpressionValueIsNotNull(t2, "colors.last()");
            String string3 = ((JsonElement)t2).getAsString();
            Intrinsics.checkExpressionValueIsNotNull(string3, "colors.last().asString");
            gradients.add(new Gradient(string, string2, string3));
        }
        nowGradient = CollectionsKt.first(gradients);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/features/special/GradientBackground$Gradient;", "", "name", "", "from", "to", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "Ljava/awt/Color;", "(Ljava/lang/String;Ljava/awt/Color;Ljava/awt/Color;)V", "getFrom", "()Ljava/awt/Color;", "getName", "()Ljava/lang/String;", "getTo", "KyinoClient"})
    public static final class Gradient {
        @NotNull
        private final String name;
        @NotNull
        private final Color from;
        @NotNull
        private final Color to;

        @NotNull
        public final String getName() {
            return this.name;
        }

        @NotNull
        public final Color getFrom() {
            return this.from;
        }

        @NotNull
        public final Color getTo() {
            return this.to;
        }

        public Gradient(@NotNull String name, @NotNull Color from, @NotNull Color to) {
            Intrinsics.checkParameterIsNotNull(name, "name");
            Intrinsics.checkParameterIsNotNull(from, "from");
            Intrinsics.checkParameterIsNotNull(to, "to");
            this.name = name;
            this.from = from;
            this.to = to;
        }

        public Gradient(@NotNull String name, @NotNull String from, @NotNull String to) {
            Intrinsics.checkParameterIsNotNull(name, "name");
            Intrinsics.checkParameterIsNotNull(from, "from");
            Intrinsics.checkParameterIsNotNull(to, "to");
            Color color = Color.decode(from);
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.decode(from)");
            Color color2 = Color.decode(to);
            Intrinsics.checkExpressionValueIsNotNull(color2, "Color.decode(to)");
            this(name, color, color2);
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/special/GradientBackground$GradientSide;", "", "(Ljava/lang/String;I)V", "LEFT", "TOP", "RIGHT", "BOTTOM", "KyinoClient"})
    public static final class GradientSide
    extends Enum<GradientSide> {
        public static final /* enum */ GradientSide LEFT;
        public static final /* enum */ GradientSide TOP;
        public static final /* enum */ GradientSide RIGHT;
        public static final /* enum */ GradientSide BOTTOM;
        private static final /* synthetic */ GradientSide[] $VALUES;

        static {
            GradientSide[] gradientSideArray = new GradientSide[4];
            GradientSide[] gradientSideArray2 = gradientSideArray;
            gradientSideArray[0] = LEFT = new GradientSide();
            gradientSideArray[1] = TOP = new GradientSide();
            gradientSideArray[2] = RIGHT = new GradientSide();
            gradientSideArray[3] = BOTTOM = new GradientSide();
            $VALUES = gradientSideArray;
        }

        public static GradientSide[] values() {
            return (GradientSide[])$VALUES.clone();
        }

        public static GradientSide valueOf(String string) {
            return Enum.valueOf(GradientSide.class, string);
        }
    }
}

