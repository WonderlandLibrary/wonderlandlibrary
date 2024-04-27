/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import ad.novoline.font.Fonts;
import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u00a2\u0006\u0002\u0010\nJ.\u00101\u001a\u0002022\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00106\u001a\u0002042\u0006\u00107\u001a\u00020\b2\u0006\u00108\u001a\u00020\bJ\u0016\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020\b2\u0006\u0010<\u001a\u00020=R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0010\"\u0004\b\u0015\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0010\"\u0004\b\u001a\u0010\u0012R\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u0014\u0010!\u001a\u00020\bX\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\fR\u001a\u0010#\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\f\"\u0004\b%\u0010&R\u001a\u0010'\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0017\"\u0004\b)\u0010*R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0017R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010/\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\f\u00a8\u0006>"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification;", "", "title", "", "content", "type", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/NotifyType;", "time", "", "animeTime", "(Ljava/lang/String;Ljava/lang/String;Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/NotifyType;II)V", "getAnimeTime", "()I", "animeXTime", "", "getAnimeXTime", "()J", "setAnimeXTime", "(J)V", "animeYTime", "getAnimeYTime", "setAnimeYTime", "getContent", "()Ljava/lang/String;", "displayTime", "getDisplayTime", "setDisplayTime", "fadeState", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/FadeState;", "getFadeState", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/FadeState;", "setFadeState", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/FadeState;)V", "height", "getHeight", "nowY", "getNowY", "setNowY", "(I)V", "string", "getString", "setString", "(Ljava/lang/String;)V", "getTime", "getTitle", "getType", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/NotifyType;", "width", "getWidth", "drawCircle", "", "x", "", "y", "radius", "start", "end", "drawNotification", "", "index", "noti", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notifications;", "LiKingSense"})
public final class Notification {
    @NotNull
    private FadeState fadeState;
    private final int height = 30;
    private int nowY;
    @NotNull
    private String string;
    private long displayTime;
    private long animeXTime;
    private long animeYTime;
    private final int width;
    @NotNull
    private final String title;
    @NotNull
    private final String content;
    @NotNull
    private final NotifyType type;
    private final int time;
    private final int animeTime;

    @NotNull
    public final FadeState getFadeState() {
        return this.fadeState;
    }

    public final void setFadeState(@NotNull FadeState fadeState) {
        Intrinsics.checkParameterIsNotNull((Object)((Object)fadeState), (String)"<set-?>");
        this.fadeState = fadeState;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getNowY() {
        return this.nowY;
    }

    public final void setNowY(int n) {
        this.nowY = n;
    }

    @NotNull
    public final String getString() {
        return this.string;
    }

    public final void setString(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull((Object)string, (String)"<set-?>");
        this.string = string;
    }

    public final long getDisplayTime() {
        return this.displayTime;
    }

    public final void setDisplayTime(long l) {
        this.displayTime = l;
    }

    public final long getAnimeXTime() {
        return this.animeXTime;
    }

    public final void setAnimeXTime(long l) {
        this.animeXTime = l;
    }

    public final long getAnimeYTime() {
        return this.animeYTime;
    }

    public final void setAnimeYTime(long l) {
        this.animeYTime = l;
    }

    public final int getWidth() {
        return this.width;
    }

    public final void drawCircle(float x, float y, float radius, int start, int end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            Color color = RenderUtils.getGradientOffset(new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue()), new Color(((Number)hud.getR2().get()).intValue(), ((Number)hud.getG2().get()).intValue(), ((Number)hud.getB2().get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / 360.0 + (double)(i * (float)34 / (float)360 * (float)56 / (float)100)) / (double)10);
            Intrinsics.checkExpressionValueIsNotNull((Object)color, (String)"RenderUtils.getGradientO\u2026/ 360) * 56 / 100) / 10))");
            int c = color.getRGB();
            float f2 = (float)(c >> 24 & 0xFF) / 255.0f;
            float f22 = (float)(c >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
            float f4 = (float)(c & 0xFF) / 255.0f;
            GlStateManager.func_179131_c((float)f22, (float)f3, (float)f4, (float)f2);
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / (double)180) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / (double)180) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    /*
     * Exception decompiling
     */
    public final boolean drawNotification(int index, @NotNull Notifications noti) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl435 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    @NotNull
    public final String getContent() {
        return this.content;
    }

    @NotNull
    public final NotifyType getType() {
        return this.type;
    }

    public final int getTime() {
        return this.time;
    }

    public final int getAnimeTime() {
        return this.animeTime;
    }

    public Notification(@NotNull String title, @NotNull String content, @NotNull NotifyType type, int time, int animeTime) {
        Intrinsics.checkParameterIsNotNull((Object)title, (String)"title");
        Intrinsics.checkParameterIsNotNull((Object)content, (String)"content");
        Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
        this.title = title;
        this.content = content;
        this.type = type;
        this.time = time;
        this.animeTime = animeTime;
        this.fadeState = FadeState.IN;
        this.height = 30;
        this.nowY = -this.height;
        this.string = "";
        this.displayTime = System.currentTimeMillis();
        this.animeXTime = System.currentTimeMillis();
        this.animeYTime = System.currentTimeMillis();
        this.width = Fonts.posterama.posterama16.posterama16.stringWidth(this.content) + 53;
    }

    public /* synthetic */ Notification(String string, String string2, NotifyType notifyType, int n, int n2, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 8) != 0) {
            n = 2000;
        }
        if ((n3 & 0x10) != 0) {
            n2 = 500;
        }
        this(string, string2, notifyType, n, n2);
    }
}

