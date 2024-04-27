/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.client.hud.element.elements.Notifications;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.render.Stencil;
import net.dev.important.utils.timer.MSTimer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u000278B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\u0016\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u00182\u0006\u00105\u001a\u000206R\u001a\u0010\f\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0003X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010!\u001a\u00020\"X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001a\u0010'\u001a\u00020(X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010-\u001a\u00020\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101\u00a8\u00069"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Notification;", "", "message", "", "type", "Lnet/dev/important/gui/client/hud/element/elements/Notification$Type;", "(Ljava/lang/String;Lnet/dev/important/gui/client/hud/element/elements/Notification$Type;)V", "(Ljava/lang/String;)V", "displayLength", "", "(Ljava/lang/String;J)V", "(Ljava/lang/String;Lnet/dev/important/gui/client/hud/element/elements/Notification$Type;J)V", "displayTime", "getDisplayTime", "()J", "setDisplayTime", "(J)V", "fadeState", "Lnet/dev/important/gui/client/hud/element/elements/Notification$FadeState;", "getFadeState", "()Lnet/dev/important/gui/client/hud/element/elements/Notification$FadeState;", "setFadeState", "(Lnet/dev/important/gui/client/hud/element/elements/Notification$FadeState;)V", "fadeStep", "", "firstY", "imgError", "Lnet/minecraft/util/ResourceLocation;", "imgInfo", "imgSuccess", "imgWarning", "notifyDir", "stay", "stayTimer", "Lnet/dev/important/utils/timer/MSTimer;", "getStayTimer", "()Lnet/dev/important/utils/timer/MSTimer;", "setStayTimer", "(Lnet/dev/important/utils/timer/MSTimer;)V", "textLength", "", "getTextLength", "()I", "setTextLength", "(I)V", "x", "getX", "()F", "setX", "(F)V", "drawNotification", "", "animationY", "parent", "Lnet/dev/important/gui/client/hud/element/elements/Notifications;", "FadeState", "Type", "LiquidBounce"})
public final class Notification {
    @NotNull
    private final String notifyDir;
    @NotNull
    private final ResourceLocation imgSuccess;
    @NotNull
    private final ResourceLocation imgError;
    @NotNull
    private final ResourceLocation imgWarning;
    @NotNull
    private final ResourceLocation imgInfo;
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
    @NotNull
    private String message;
    @NotNull
    private Type type;

    public Notification(@NotNull String message, @NotNull Type type, long displayLength) {
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        this.notifyDir = "liquidplus/notification/";
        this.imgSuccess = new ResourceLocation(Intrinsics.stringPlus(this.notifyDir, "checkmark.png"));
        this.imgError = new ResourceLocation(Intrinsics.stringPlus(this.notifyDir, "error.png"));
        this.imgWarning = new ResourceLocation(Intrinsics.stringPlus(this.notifyDir, "warning.png"));
        this.imgInfo = new ResourceLocation(Intrinsics.stringPlus(this.notifyDir, "info.png"));
        this.fadeState = FadeState.IN;
        this.stayTimer = new MSTimer();
        this.message = "";
        this.message = message;
        this.type = type;
        this.displayTime = displayLength;
        this.firstY = 19190.0f;
        this.stayTimer.reset();
        this.textLength = Fonts.font40.func_78256_a(message);
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
        Intrinsics.checkNotNullParameter((Object)fadeState, "<set-?>");
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
        Intrinsics.checkNotNullParameter(mSTimer, "<set-?>");
        this.stayTimer = mSTimer;
    }

    public Notification(@NotNull String message, @NotNull Type type) {
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        this(message, type, 2000L);
    }

    public Notification(@NotNull String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        this(message, Type.INFO, 500L);
    }

    public Notification(@NotNull String message, long displayLength) {
        Intrinsics.checkNotNullParameter(message, "message");
        this(message, Type.INFO, displayLength);
    }

    public final void drawNotification(float animationY, @NotNull Notifications parent) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        int delta = RenderUtils.deltaTime;
        float width = (float)this.textLength + 8.0f;
        boolean smooth = (Boolean)parent.getSmoothYTransition().get();
        boolean newAnim = (Boolean)parent.getNewAnimValue().get();
        float animSpeed = ((Number)parent.getAnimationSpeed().get()).floatValue();
        Side side = parent.getSide();
        String style = (String)parent.getStyleValue().get();
        boolean blur = (Boolean)parent.getBlurValue().get();
        float strength = ((Number)parent.getBlurStrength().get()).floatValue();
        float originalX = (float)parent.getRenderX();
        float originalY = (float)parent.getRenderY();
        Color backgroundColor = new Color(((Number)parent.getBgRedValue().get()).intValue(), ((Number)parent.getBgGreenValue().get()).intValue(), ((Number)parent.getBgBlueValue().get()).intValue(), ((Number)parent.getBgAlphaValue().get()).intValue());
        if (smooth) {
            if (this.firstY == 19190.0f) {
                this.firstY = animationY;
            }
            this.firstY += (animationY - this.firstY) * 0.25f;
        } else {
            this.firstY = animationY;
        }
        float y = this.firstY;
        String string = style.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "compact": {
                int n;
                GlStateManager.func_179117_G();
                if (blur) {
                    GL11.glTranslatef((float)(-originalX), (float)(-originalY), (float)0.0f);
                    GL11.glPushMatrix();
                    BlurUtils.blurAreaRounded(originalX + -this.x - 5.0f, originalY + -18.0f - y, originalX + -this.x + 8.0f + (float)this.textLength, originalY + -y, 3.0f, strength);
                    GL11.glPopMatrix();
                    GL11.glTranslatef((float)originalX, (float)originalY, (float)0.0f);
                }
                RenderUtils.customRounded(-this.x + 8.0f + (float)this.textLength, -y, -this.x - 2.0f, -18.0f - y, 0.0f, 3.0f, 3.0f, 0.0f, backgroundColor.getRGB());
                float f = -this.x - 2.0f;
                float f2 = -y;
                float f3 = -this.x - 5.0f;
                switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                    case 1: {
                        n = new Color(80, 255, 80).getRGB();
                        break;
                    }
                    case 2: {
                        n = new Color(255, 80, 80).getRGB();
                        break;
                    }
                    case 3: {
                        n = new Color(255, 255, 255).getRGB();
                        break;
                    }
                    case 4: {
                        n = new Color(255, 255, 0).getRGB();
                        break;
                    }
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                }
                RenderUtils.customRounded(f, f2, f3, -18.0f - y, 3.0f, 0.0f, 0.0f, 3.0f, n);
                GlStateManager.func_179117_G();
                Fonts.font40.drawString(this.message, -this.x + (float)3, -13.0f - y, -1);
                break;
            }
            case "full": {
                ResourceLocation resourceLocation;
                float dist = this.x + 1.0f + 26.0f - (this.x - (float)8 - (float)this.textLength);
                float kek = -this.x - 1.0f - 26.0f;
                GlStateManager.func_179117_G();
                if (blur) {
                    GL11.glTranslatef((float)(-originalX), (float)(-originalY), (float)0.0f);
                    GL11.glPushMatrix();
                    BlurUtils.blurArea(originalX + kek, originalY + -28.0f - y, originalX + -this.x + (float)8 + (float)this.textLength, originalY + -y, strength);
                    GL11.glPopMatrix();
                    GL11.glTranslatef((float)originalX, (float)originalY, (float)0.0f);
                }
                RenderUtils.drawRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, backgroundColor.getRGB());
                GL11.glPushMatrix();
                GlStateManager.func_179118_c();
                switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                    case 1: {
                        resourceLocation = this.imgSuccess;
                        break;
                    }
                    case 2: {
                        resourceLocation = this.imgError;
                        break;
                    }
                    case 4: {
                        resourceLocation = this.imgWarning;
                        break;
                    }
                    case 3: {
                        resourceLocation = this.imgInfo;
                        break;
                    }
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                }
                RenderUtils.drawImage2(resourceLocation, kek, -27.0f - y, 26, 26);
                GlStateManager.func_179141_d();
                GL11.glPopMatrix();
                GlStateManager.func_179117_G();
                if (this.fadeState == FadeState.STAY && !this.stayTimer.hasTimePassed(this.displayTime)) {
                    int n;
                    float f = -y;
                    float f4 = kek + dist * (this.stayTimer.hasTimePassed(this.displayTime) ? 0.0f : (float)(this.displayTime - (System.currentTimeMillis() - this.stayTimer.time)) / (float)this.displayTime);
                    switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                        case 1: {
                            n = new Color(80, 255, 80).getRGB();
                            break;
                        }
                        case 2: {
                            n = new Color(255, 80, 80).getRGB();
                            break;
                        }
                        case 3: {
                            n = new Color(255, 255, 255).getRGB();
                            break;
                        }
                        case 4: {
                            n = new Color(255, 255, 0).getRGB();
                            break;
                        }
                        default: {
                            throw new NoWhenBranchMatchedException();
                        }
                    }
                    RenderUtils.drawRect(kek, f, f4, -1.0f - y, n);
                } else if (this.fadeState == FadeState.IN) {
                    int n;
                    float f = -y;
                    switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                        case 1: {
                            n = new Color(80, 255, 80).getRGB();
                            break;
                        }
                        case 2: {
                            n = new Color(255, 80, 80).getRGB();
                            break;
                        }
                        case 3: {
                            n = new Color(255, 255, 255).getRGB();
                            break;
                        }
                        case 4: {
                            n = new Color(255, 255, 0).getRGB();
                            break;
                        }
                        default: {
                            throw new NoWhenBranchMatchedException();
                        }
                    }
                    RenderUtils.drawRect(kek, f, kek + dist, -1.0f - y, n);
                }
                GlStateManager.func_179117_G();
                Fonts.font40.drawString(this.message, -this.x + (float)2, -18.0f - y, -1);
                break;
            }
            case "new": {
                ResourceLocation resourceLocation;
                float dist = this.x + 1.0f + 26.0f - (this.x - (float)8 - (float)this.textLength);
                float kek = -this.x - 1.0f - 26.0f;
                float toolong = dist * (this.stayTimer.hasTimePassed(this.displayTime) ? 0.0f : (float)(this.displayTime - (System.currentTimeMillis() - this.stayTimer.time)) / (float)this.displayTime);
                GlStateManager.func_179117_G();
                if (blur) {
                    GL11.glTranslatef((float)(-originalX), (float)(-originalY), (float)0.0f);
                    GL11.glPushMatrix();
                    BlurUtils.blurAreaRounded(originalX + kek, originalY + -28.0f - y, originalX + -this.x + (float)8 + (float)this.textLength, originalY + -y, 3.0f, strength);
                    GL11.glPopMatrix();
                    GL11.glTranslatef((float)originalX, (float)originalY, (float)0.0f);
                }
                Stencil.write(true);
                RenderUtils.drawRoundedRect(-this.x + (float)8 + (float)this.textLength, -y, kek, -28.0f - y, 3.0f, backgroundColor.getRGB());
                Stencil.erase(true);
                GlStateManager.func_179117_G();
                if (this.fadeState == FadeState.STAY && !this.stayTimer.hasTimePassed(this.displayTime)) {
                    int n;
                    float f = -y;
                    switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                        case 1: {
                            n = new Color(80, 255, 80, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        case 2: {
                            n = new Color(255, 80, 80, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        case 3: {
                            n = new Color(255, 255, 255, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        case 4: {
                            n = new Color(255, 255, 0, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        default: {
                            throw new NoWhenBranchMatchedException();
                        }
                    }
                    RenderUtils.newDrawRect(kek, f, kek + toolong, -28.0f - y, n);
                } else if (this.fadeState == FadeState.IN) {
                    int n;
                    float f = -y;
                    switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                        case 1: {
                            n = new Color(80, 255, 80, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        case 2: {
                            n = new Color(255, 80, 80, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        case 3: {
                            n = new Color(255, 255, 255, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        case 4: {
                            n = new Color(255, 255, 0, backgroundColor.getAlpha() / 2).getRGB();
                            break;
                        }
                        default: {
                            throw new NoWhenBranchMatchedException();
                        }
                    }
                    RenderUtils.newDrawRect(kek, f, kek + dist, -28.0f - y, n);
                }
                Stencil.dispose();
                GL11.glPushMatrix();
                GlStateManager.func_179118_c();
                GlStateManager.func_179117_G();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                switch (WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()]) {
                    case 1: {
                        resourceLocation = this.imgSuccess;
                        break;
                    }
                    case 2: {
                        resourceLocation = this.imgError;
                        break;
                    }
                    case 4: {
                        resourceLocation = this.imgWarning;
                        break;
                    }
                    case 3: {
                        resourceLocation = this.imgInfo;
                        break;
                    }
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                }
                RenderUtils.drawImage2(resourceLocation, kek, -27.0f - y, 26, 26);
                GlStateManager.func_179141_d();
                GL11.glPopMatrix();
                Fonts.font40.drawString(this.message, -this.x + (float)2, -18.0f - y, -1);
            }
        }
        switch (WhenMappings.$EnumSwitchMapping$1[this.fadeState.ordinal()]) {
            case 1: {
                if (this.x < width) {
                    this.x = newAnim ? AnimationUtils.animate(width, this.x, animSpeed * 0.025f * (float)delta) : net.dev.important.utils.render.AnimationUtils.easeOut(this.fadeStep, width) * width;
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
                    this.x = newAnim ? AnimationUtils.animate(-width, this.x, animSpeed * 0.025f * (float)delta) : net.dev.important.utils.render.AnimationUtils.easeOut(this.fadeStep, width) * width;
                    this.fadeStep -= (float)delta / 4.0f;
                    break;
                }
                this.fadeState = FadeState.END;
                break;
            }
            case 4: {
                Client.INSTANCE.getHud().removeNotification(this);
            }
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Notification$Type;", "", "(Ljava/lang/String;I)V", "SUCCESS", "INFO", "WARNING", "ERROR", "LiquidBounce"})
    public static final class Type
    extends Enum<Type> {
        public static final /* enum */ Type SUCCESS = new Type();
        public static final /* enum */ Type INFO = new Type();
        public static final /* enum */ Type WARNING = new Type();
        public static final /* enum */ Type ERROR = new Type();
        private static final /* synthetic */ Type[] $VALUES;

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        public static Type valueOf(String value) {
            return Enum.valueOf(Type.class, value);
        }

        static {
            $VALUES = typeArray = new Type[]{Type.SUCCESS, Type.INFO, Type.WARNING, Type.ERROR};
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Notification$FadeState;", "", "(Ljava/lang/String;I)V", "IN", "STAY", "OUT", "END", "LiquidBounce"})
    public static final class FadeState
    extends Enum<FadeState> {
        public static final /* enum */ FadeState IN = new FadeState();
        public static final /* enum */ FadeState STAY = new FadeState();
        public static final /* enum */ FadeState OUT = new FadeState();
        public static final /* enum */ FadeState END = new FadeState();
        private static final /* synthetic */ FadeState[] $VALUES;

        public static FadeState[] values() {
            return (FadeState[])$VALUES.clone();
        }

        public static FadeState valueOf(String value) {
            return Enum.valueOf(FadeState.class, value);
        }

        static {
            $VALUES = fadeStateArray = new FadeState[]{FadeState.IN, FadeState.STAY, FadeState.OUT, FadeState.END};
        }
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] nArray = new int[Type.values().length];
            nArray[Type.SUCCESS.ordinal()] = 1;
            nArray[Type.ERROR.ordinal()] = 2;
            nArray[Type.INFO.ordinal()] = 3;
            nArray[Type.WARNING.ordinal()] = 4;
            $EnumSwitchMapping$0 = nArray;
            nArray = new int[FadeState.values().length];
            nArray[FadeState.IN.ordinal()] = 1;
            nArray[FadeState.STAY.ordinal()] = 2;
            nArray[FadeState.OUT.ordinal()] = 3;
            nArray[FadeState.END.ordinal()] = 4;
            $EnumSwitchMapping$1 = nArray;
        }
    }
}

