/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0003HIJB\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nB1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u00a2\u0006\u0002\u0010\u000eJ^\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020\u001d2\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020<2\u0006\u0010>\u001a\u00020\u001d2\u0006\u0010?\u001a\u00020@2\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u00032\u0006\u0010D\u001a\u00020<2\u0006\u0010E\u001a\u00020\u001d2\u0006\u0010F\u001a\u00020\u001d2\u0006\u0010G\u001a\u00020\u001dR\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\u00020\fX\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0010R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0003X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010(\u001a\u00020)X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u001a\u0010.\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0010\"\u0004\b0\u00101R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u00103\u001a\u00020\u001dX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107\u00a8\u0006K"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification;", "", "message", "", "type", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$Type;", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$Type;)V", "(Ljava/lang/String;)V", "displayLength", "", "(Ljava/lang/String;J)V", "time", "", "animeTime", "(Ljava/lang/String;Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$Type;JII)V", "getAnimeTime", "()I", "displayTime", "getDisplayTime", "()J", "setDisplayTime", "(J)V", "fadeState", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$FadeState;", "getFadeState", "()Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$FadeState;", "setFadeState", "(Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$FadeState;)V", "fadeStep", "", "firstY", "height", "getHeight", "imgError", "Lnet/minecraft/util/ResourceLocation;", "imgInfo", "imgSuccess", "imgWarning", "notifyDir", "stay", "stayTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getStayTimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "setStayTimer", "(Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;)V", "textLength", "getTextLength", "setTextLength", "(I)V", "getTime", "x", "getX", "()F", "setX", "(F)V", "drawNotification", "", "animationY", "smooth", "", "newAnim", "animSpeed", "backgroundColor", "Ljava/awt/Color;", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "style", "blur", "strength", "originalX", "originalY", "FadeState", "NotifyType", "Type", "KyinoClient"})
public final class Notification {
    private final String notifyDir;
    private final ResourceLocation imgSuccess;
    private final ResourceLocation imgError;
    private final ResourceLocation imgWarning;
    private final ResourceLocation imgInfo;
    private final int height;
    private float x;
    private int textLength;
    private float stay;
    private float fadeStep;
    @NotNull
    private FadeState fadeState;
    private long displayTime;
    @NotNull
    private MSTimer stayTimer;
    private float firstY;
    private String message;
    private Type type;
    private final int time;
    private final int animeTime;

    public final int getHeight() {
        return this.height;
    }

    public final float getX() {
        return this.x;
    }

    public final void setX(float f) {
        this.x = f;
    }

    public final int getTextLength() {
        return this.textLength;
    }

    public final void setTextLength(int n) {
        this.textLength = n;
    }

    @NotNull
    public final FadeState getFadeState() {
        return this.fadeState;
    }

    public final void setFadeState(@NotNull FadeState fadeState) {
        Intrinsics.checkParameterIsNotNull((Object)fadeState, "<set-?>");
        this.fadeState = fadeState;
    }

    public final long getDisplayTime() {
        return this.displayTime;
    }

    public final void setDisplayTime(long l) {
        this.displayTime = l;
    }

    @NotNull
    public final MSTimer getStayTimer() {
        return this.stayTimer;
    }

    public final void setStayTimer(@NotNull MSTimer mSTimer) {
        Intrinsics.checkParameterIsNotNull(mSTimer, "<set-?>");
        this.stayTimer = mSTimer;
    }

    public final void drawNotification(float animationY, boolean smooth, boolean newAnim, float animSpeed, @NotNull Color backgroundColor, @NotNull Side side, @NotNull String style, boolean blur, float strength, float originalX, float originalY) {
        Intrinsics.checkParameterIsNotNull(backgroundColor, "backgroundColor");
        Intrinsics.checkParameterIsNotNull(side, "side");
        Intrinsics.checkParameterIsNotNull(style, "style");
        int delta = RenderUtils.deltaTime;
        float width = (float)this.textLength + 8.0f;
        long nowTime = System.currentTimeMillis();
        if (smooth) {
            if (this.firstY == 19190.0f) {
                this.firstY = animationY;
            }
            this.firstY += (animationY - this.firstY) * 0.25f;
        } else {
            this.firstY = animationY;
        }
        float y = this.firstY;
        String string = style;
        boolean bl = false;
        String string2 = string.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
        string = string2;
        switch (string.hashCode()) {
            case 570230263: {
                ResourceLocation resourceLocation;
                float f;
                boolean bl2;
                float f2;
                float f3;
                float f4;
                float f5;
                if (!string.equals("intellij")) break;
                float dist = this.x + 1.0f + 26.0f - (this.x - (float)8 - (float)this.textLength);
                float kek = -this.x - 1.0f - 20.0f;
                GlStateManager.func_179117_G();
                if (blur) {
                    GL11.glTranslatef((float)(-originalX), (float)(-originalY), (float)0.0f);
                    GL11.glPushMatrix();
                    GL11.glPopMatrix();
                    GL11.glTranslatef((float)originalX, (float)originalY, (float)0.0f);
                }
                Stencil.write(true);
                if (this.type == Type.ERROR) {
                    RenderUtils.drawRoundedRect(-this.x + (float)9 + (float)this.textLength, -y + 1.0f, kek - 1.0f, -28.0f - y - 1.0f, 0.0f, new Color(115, 69, 75).getRGB());
                    RenderUtils.drawRoundedRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, 0.0f, new Color(89, 61, 65).getRGB());
                    f5 = width - width * ((float)(nowTime - this.displayTime) / ((float)this.animeTime * 1.0f + (float)this.time));
                    f4 = 0.0f;
                    f3 = (float)this.height - 1.0f;
                    f2 = 0.0f;
                    bl2 = false;
                    f = Math.max(f5, f4);
                    RenderUtils.drawRect(f2, f3, f, (float)this.height, 16723770);
                    Fonts.minecraftFont.func_175063_a("IDE Error:", -this.x - (float)4, -25.0f - y, new Color(249, 130, 108).getRGB());
                }
                if (this.type == Type.INFO) {
                    RenderUtils.drawRoundedRect(-this.x + (float)9 + (float)this.textLength, -y + 1.0f, kek - 1.0f, -28.0f - y - 1.0f, 0.0f, new Color(70, 94, 115).getRGB());
                    RenderUtils.drawRoundedRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, 0.0f, new Color(61, 72, 87).getRGB());
                    RenderUtils.drawRoundedRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, 0.0f, new Color(61, 72, 87).getRGB());
                    Fonts.minecraftFont.func_175063_a("IDE Information:", -this.x - (float)4, -25.0f - y, new Color(119, 145, 147).getRGB());
                }
                if (this.type == Type.SUCCESS) {
                    RenderUtils.drawRoundedRect(-this.x + (float)9 + (float)this.textLength, -y + 1.0f, kek - 1.0f, -28.0f - y - 1.0f, 0.0f, new Color(67, 104, 67).getRGB());
                    RenderUtils.drawRoundedRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, 0.0f, new Color(55, 78, 55).getRGB());
                    f5 = width - width * ((float)(nowTime - this.displayTime) / ((float)this.animeTime * 1.0f + (float)this.time));
                    f4 = 0.0f;
                    f3 = (float)this.height - 1.0f;
                    f2 = 0.0f;
                    bl2 = false;
                    f = Math.max(f5, f4);
                    RenderUtils.drawRect(f2, f3, f, (float)this.height, 0x60E066);
                    Fonts.minecraftFont.func_175063_a("IDE Success:", -this.x - (float)4, -25.0f - y, new Color(10, 142, 2).getRGB());
                }
                if (this.type == Type.WARNING) {
                    RenderUtils.drawRoundedRect(-this.x + (float)9 + (float)this.textLength, -y + 1.0f, kek - 1.0f, -28.0f - y - 1.0f, 0.0f, new Color(103, 103, 63).getRGB());
                    RenderUtils.drawRoundedRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, 0.0f, new Color(80, 80, 57).getRGB());
                    f5 = width - width * ((float)(nowTime - this.displayTime) / ((float)this.animeTime * 1.0f + (float)this.time));
                    f4 = 0.0f;
                    f3 = (float)this.height - 1.0f;
                    f2 = 0.0f;
                    bl2 = false;
                    f = Math.max(f5, f4);
                    RenderUtils.drawRect(f2, f3, f, (float)this.height, 16121088);
                    Fonts.minecraftFont.func_175063_a("IDE Warning:", -this.x - (float)4, -25.0f - y, new Color(175, 163, 0).getRGB());
                }
                Stencil.erase(true);
                GlStateManager.func_179117_G();
                Stencil.dispose();
                GL11.glPushMatrix();
                GlStateManager.func_179118_c();
                GlStateManager.func_179117_G();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                switch (Notification$WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                    case 1: {
                        resourceLocation = this.imgSuccess;
                        break;
                    }
                    case 2: {
                        resourceLocation = this.imgError;
                        break;
                    }
                    case 3: {
                        resourceLocation = this.imgWarning;
                        break;
                    }
                    case 4: {
                        resourceLocation = this.imgInfo;
                        break;
                    }
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                }
                RenderUtils.drawImage2(resourceLocation, kek + (float)5, -25.0f - y, 7, 7);
                f5 = width - width * ((float)(nowTime - this.displayTime) / ((float)this.animeTime * 1.0f + (float)this.time));
                f4 = 0.0f;
                f3 = (float)this.height - 1.0f;
                f2 = 0.0f;
                bl2 = false;
                f = Math.max(f5, f4);
                RenderUtils.drawRect(f2, f3, f, (float)this.height, new Color(180, 180, 157).getRGB());
                GlStateManager.func_179141_d();
                GL11.glPopMatrix();
                Fonts.minecraftFont.func_175063_a(this.message, -this.x - (float)4, -13.0f - y, -1);
            }
        }
        switch (Notification$WhenMappings.$EnumSwitchMapping$1[this.fadeState.ordinal()]) {
            case 1: {
                if (this.x < width) {
                    this.x = newAnim ? net.ccbluex.liquidbounce.utils.AnimationUtils.animate(width, this.x, animSpeed * 0.025f * (float)delta) : AnimationUtils.easeOut(this.fadeStep, width) * width;
                    this.fadeStep += (float)delta / 4.0f;
                }
                if (this.x >= width) {
                    this.fadeState = FadeState.STAY;
                    this.x = width;
                    this.fadeStep = width;
                }
                this.stay = 60.0f;
                this.stayTimer.reset();
                break;
            }
            case 2: {
                if (this.stay > 0.0f) {
                    this.stay = 0.0f;
                    this.stayTimer.reset();
                }
                if (!this.stayTimer.hasTimePassed(this.displayTime)) break;
                this.fadeState = FadeState.OUT;
                break;
            }
            case 3: {
                if (this.x > 0.0f) {
                    this.x = newAnim ? net.ccbluex.liquidbounce.utils.AnimationUtils.animate(-width, this.x, animSpeed * 0.025f * (float)delta) : AnimationUtils.easeOut(this.fadeStep, width) * width;
                    this.fadeStep -= (float)delta / 4.0f;
                    break;
                }
                this.fadeState = FadeState.END;
                break;
            }
            case 4: {
                LiquidBounce.INSTANCE.getHud().removeNotification(this);
            }
        }
    }

    public final int getTime() {
        return this.time;
    }

    public final int getAnimeTime() {
        return this.animeTime;
    }

    public Notification(@NotNull String message, @NotNull Type type, long displayLength, int time, int animeTime) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull((Object)type, "type");
        this.time = time;
        this.animeTime = animeTime;
        this.notifyDir = "liquidbounce/noti/intellj/";
        this.imgSuccess = new ResourceLocation(this.notifyDir + "checkmark.png");
        this.imgError = new ResourceLocation(this.notifyDir + "error.png");
        this.imgWarning = new ResourceLocation(this.notifyDir + "warning.png");
        this.imgInfo = new ResourceLocation(this.notifyDir + "info.png");
        this.height = 18;
        this.fadeState = FadeState.IN;
        this.stayTimer = new MSTimer();
        this.message = "";
        this.message = message;
        this.type = type;
        this.displayTime = displayLength;
        this.firstY = 19190.0f;
        this.stayTimer.reset();
        this.textLength = Fonts.minecraftFont.func_78256_a(message);
    }

    public /* synthetic */ Notification(String string, Type type, long l, int n, int n2, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 8) != 0) {
            n = 2000;
        }
        if ((n3 & 0x10) != 0) {
            n2 = 500;
        }
        this(string, type, l, n, n2);
    }

    public Notification(@NotNull String message, @NotNull Type type) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull((Object)type, "type");
        this(message, type, 2000L, 0, 0, 24, null);
    }

    public Notification(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        this(message, Type.INFO, 500L, 0, 0, 24, null);
    }

    public Notification(@NotNull String message, long displayLength) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        this(message, Type.INFO, displayLength, 0, 0, 24, null);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$NotifyType;", "", "renderColor", "Ljava/awt/Color;", "(Ljava/lang/String;ILjava/awt/Color;)V", "getRenderColor", "()Ljava/awt/Color;", "setRenderColor", "(Ljava/awt/Color;)V", "SUCCESS", "ERROR", "WARNING", "INFO", "KyinoClient"})
    public static final class NotifyType
    extends Enum<NotifyType> {
        public static final /* enum */ NotifyType SUCCESS;
        public static final /* enum */ NotifyType ERROR;
        public static final /* enum */ NotifyType WARNING;
        public static final /* enum */ NotifyType INFO;
        private static final /* synthetic */ NotifyType[] $VALUES;
        @NotNull
        private Color renderColor;

        static {
            NotifyType[] notifyTypeArray = new NotifyType[4];
            NotifyType[] notifyTypeArray2 = notifyTypeArray;
            notifyTypeArray[0] = SUCCESS = new NotifyType(new Color(0x60E066));
            notifyTypeArray[1] = ERROR = new NotifyType(new Color(16723770));
            notifyTypeArray[2] = WARNING = new NotifyType(new Color(16121088));
            notifyTypeArray[3] = INFO = new NotifyType(new Color(6590631));
            $VALUES = notifyTypeArray;
        }

        @NotNull
        public final Color getRenderColor() {
            return this.renderColor;
        }

        public final void setRenderColor(@NotNull Color color) {
            Intrinsics.checkParameterIsNotNull(color, "<set-?>");
            this.renderColor = color;
        }

        private NotifyType(Color renderColor) {
            this.renderColor = renderColor;
        }

        public static NotifyType[] values() {
            return (NotifyType[])$VALUES.clone();
        }

        public static NotifyType valueOf(String string) {
            return Enum.valueOf(NotifyType.class, string);
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$Type;", "", "(Ljava/lang/String;I)V", "SUCCESS", "INFO", "WARNING", "ERROR", "KyinoClient"})
    public static final class Type
    extends Enum<Type> {
        public static final /* enum */ Type SUCCESS;
        public static final /* enum */ Type INFO;
        public static final /* enum */ Type WARNING;
        public static final /* enum */ Type ERROR;
        private static final /* synthetic */ Type[] $VALUES;

        static {
            Type[] typeArray = new Type[4];
            Type[] typeArray2 = typeArray;
            typeArray[0] = SUCCESS = new Type();
            typeArray[1] = INFO = new Type();
            typeArray[2] = WARNING = new Type();
            typeArray[3] = ERROR = new Type();
            $VALUES = typeArray;
        }

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        public static Type valueOf(String string) {
            return Enum.valueOf(Type.class, string);
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification$FadeState;", "", "(Ljava/lang/String;I)V", "IN", "STAY", "OUT", "END", "KyinoClient"})
    public static final class FadeState
    extends Enum<FadeState> {
        public static final /* enum */ FadeState IN;
        public static final /* enum */ FadeState STAY;
        public static final /* enum */ FadeState OUT;
        public static final /* enum */ FadeState END;
        private static final /* synthetic */ FadeState[] $VALUES;

        static {
            FadeState[] fadeStateArray = new FadeState[4];
            FadeState[] fadeStateArray2 = fadeStateArray;
            fadeStateArray[0] = IN = new FadeState();
            fadeStateArray[1] = STAY = new FadeState();
            fadeStateArray[2] = OUT = new FadeState();
            fadeStateArray[3] = END = new FadeState();
            $VALUES = fadeStateArray;
        }

        public static FadeState[] values() {
            return (FadeState[])$VALUES.clone();
        }

        public static FadeState valueOf(String string) {
            return Enum.valueOf(FadeState.class, string);
        }
    }
}

