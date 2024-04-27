/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.combat.TeleportAura;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.extensions.PlayerExtensionKt;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.render.BlendUtils;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.EaseUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.render.ShadowUtils;
import net.dev.important.utils.render.Stencil;
import net.dev.important.utils.render.UiUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Target", disableScale=true, retrieveDamage=true)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001:\u0002WXB\u0005\u00a2\u0006\u0002\u0010\u0002J(\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020G2\u0006\u0010I\u001a\u00020G2\u0006\u0010J\u001a\u00020GH\u0002J\b\u0010K\u001a\u00020LH\u0016JP\u0010M\u001a\u00020E2\u0006\u0010N\u001a\u00020>2\u0006\u0010F\u001a\u00020\u001d2\u0006\u0010H\u001a\u00020\u001d2\u0006\u0010O\u001a\u00020\u001d2\u0006\u0010I\u001a\u00020G2\u0006\u0010J\u001a\u00020G2\u0006\u0010P\u001a\u00020\u001d2\u0006\u0010Q\u001a\u00020\u001d2\u0006\u0010R\u001a\u00020\u001dH\u0002J \u0010M\u001a\u00020E2\u0006\u0010N\u001a\u00020>2\u0006\u0010I\u001a\u00020G2\u0006\u0010J\u001a\u00020GH\u0002J0\u0010M\u001a\u00020E2\u0006\u0010N\u001a\u00020>2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020G2\u0006\u0010I\u001a\u00020G2\u0006\u0010J\u001a\u00020GH\u0002J8\u0010M\u001a\u00020E2\u0006\u0010N\u001a\u00020>2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020G2\u0006\u0010I\u001a\u00020G2\u0006\u0010J\u001a\u00020G2\u0006\u0010S\u001a\u00020\u001dH\u0002J\b\u0010T\u001a\u00020LH\u0002J\u0010\u0010U\u001a\u00020E2\u0006\u0010V\u001a\u00020CH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020>X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010B\u001a\u0004\u0018\u00010CX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006Y"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Target;", "Lnet/dev/important/gui/client/hud/element/Element;", "()V", "backgroundColorAlphaValue", "Lnet/dev/important/value/IntegerValue;", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorRedValue", "blueValue", "blurStrength", "Lnet/dev/important/value/FloatValue;", "blurValue", "Lnet/dev/important/value/BoolValue;", "borderColorAlphaValue", "borderColorBlueValue", "borderColorGreenValue", "borderColorRedValue", "brightnessValue", "chillFadingValue", "chillFontSpeed", "chillHealthBarValue", "colorModeValue", "Lnet/dev/important/value/ListValue;", "decimalFormat", "Ljava/text/DecimalFormat;", "decimalFormat2", "decimalFormat3", "distanceValue", "easingHealth", "", "exhiFontValue", "Lnet/dev/important/value/FontValue;", "fadeSpeed", "generateAmountValue", "gotDamaged", "", "gradientAmountValue", "greenValue", "lastTarget", "Lnet/minecraft/entity/Entity;", "maxParticleSize", "minParticleSize", "mixerSecondsValue", "numberRenderer", "Lnet/dev/important/gui/client/hud/element/elements/Target$CharRenderer;", "particleList", "", "Lnet/dev/important/gui/client/hud/element/elements/Target$Particle;", "particleRange", "progress", "progressChill", "redValue", "riseParticle", "riseParticleFade", "riseParticleFadingSpeed", "riseParticleSpeed", "riseShadow", "riseShadowHead", "riseShadowLegacy", "saturationValue", "shadowStrengthValue", "shieldIcon", "Lnet/minecraft/util/ResourceLocation;", "showUrselfWhenChatOpen", "styleValue", "tSlideAnim", "target", "Lnet/minecraft/entity/player/EntityPlayer;", "drawArmorIcon", "", "x", "", "y", "width", "height", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "drawHead", "skin", "scale", "red", "green", "blue", "alpha", "getTBorder", "handleDamage", "ent", "CharRenderer", "Particle", "LiquidBounce"})
public final class Target
extends Element {
    @NotNull
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
    @NotNull
    private final DecimalFormat decimalFormat2 = new DecimalFormat("##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
    @NotNull
    private final DecimalFormat decimalFormat3 = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
    @NotNull
    private final ListValue styleValue;
    @NotNull
    private final FloatValue fadeSpeed;
    @NotNull
    private final FloatValue chillFontSpeed;
    @NotNull
    private final BoolValue chillHealthBarValue;
    @NotNull
    private final BoolValue chillFadingValue;
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final BoolValue tSlideAnim;
    @NotNull
    private final BoolValue showUrselfWhenChatOpen;
    @NotNull
    private final BoolValue riseShadow;
    @NotNull
    private final BoolValue riseShadowLegacy;
    @NotNull
    private final BoolValue riseShadowHead;
    @NotNull
    private final IntegerValue shadowStrengthValue;
    @NotNull
    private final BoolValue riseParticle;
    @NotNull
    private final BoolValue riseParticleFade;
    @NotNull
    private final IntegerValue gradientAmountValue;
    @NotNull
    private final IntegerValue distanceValue;
    @NotNull
    private final FloatValue riseParticleSpeed;
    @NotNull
    private final FloatValue riseParticleFadingSpeed;
    @NotNull
    private final IntegerValue generateAmountValue;
    @NotNull
    private final FloatValue particleRange;
    @NotNull
    private final FloatValue minParticleSize;
    @NotNull
    private final FloatValue maxParticleSize;
    @NotNull
    private final FontValue exhiFontValue;
    @NotNull
    private final ListValue colorModeValue;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue mixerSecondsValue;
    @NotNull
    private final IntegerValue backgroundColorRedValue;
    @NotNull
    private final IntegerValue backgroundColorGreenValue;
    @NotNull
    private final IntegerValue backgroundColorBlueValue;
    @NotNull
    private final IntegerValue backgroundColorAlphaValue;
    @NotNull
    private final IntegerValue borderColorRedValue;
    @NotNull
    private final IntegerValue borderColorGreenValue;
    @NotNull
    private final IntegerValue borderColorBlueValue;
    @NotNull
    private final IntegerValue borderColorAlphaValue;
    @NotNull
    private final ResourceLocation shieldIcon;
    private float easingHealth;
    @Nullable
    private Entity lastTarget;
    @NotNull
    private final List<Particle> particleList;
    private boolean gotDamaged;
    private float progress;
    private float progressChill;
    @Nullable
    private EntityPlayer target;
    @NotNull
    private final CharRenderer numberRenderer;

    public Target() {
        super(0.0, 0.0, 0.0f, null, 15, null);
        String[] stringArray = new String[]{"LiquidBounce", "Flux", "Novoline", "Slowly", "Rise", "Exhibition", "LiquidBounce+", "Chill"};
        this.styleValue = new ListValue("Style", stringArray, "LiquidBounce");
        this.fadeSpeed = new FloatValue("FadeSpeed", 2.0f, 1.0f, 9.0f);
        this.chillFontSpeed = new FloatValue("Chill-FontSpeed", 0.5f, 0.01f, 1.0f, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "chill", true);
            }
        });
        this.chillHealthBarValue = new BoolValue("Chill-Healthbar", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "chill", true);
            }
        });
        this.chillFadingValue = new BoolValue("Chill-FadingAnim", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "chill", true);
            }
        });
        this.blurValue = new BoolValue("Blur", false, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getBlurValue$p(this.this$0).get() != false;
            }
        });
        this.tSlideAnim = new BoolValue("TSlide-Animation", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.showUrselfWhenChatOpen = new BoolValue("DisplayWhenChat", true);
        this.riseShadow = new BoolValue("Rise-Shadow", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.riseShadowLegacy = new BoolValue("Rise-Shadow-Legacy", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.riseShadowHead = new BoolValue("Rise-Shadow-Head", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.shadowStrengthValue = new IntegerValue("Rise-Shadow-Strength", 3, 1, 5, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.riseParticle = new BoolValue("Rise-Particle", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.riseParticleFade = new BoolValue("Rise-Particle-Fade", true, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        });
        this.gradientAmountValue = new IntegerValue("Rise-Gradient-Amount", 4, 1, 40, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.distanceValue = new IntegerValue("Rise-GradientDistance", 50, 1, 200, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true);
            }
        });
        this.riseParticleSpeed = new FloatValue("Rise-ParticleSpeed", 0.05f, 0.01f, 0.2f, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        });
        this.riseParticleFadingSpeed = new FloatValue("ParticleFadingSpeed", 0.05f, 0.01f, 0.2f, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        });
        this.generateAmountValue = new IntegerValue("ParticleGenerateAmount", 10, 1, 40, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        });
        this.particleRange = new FloatValue("Rise-ParticleRange", 50.0f, 0.0f, 50.0f, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        });
        stringArray = new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        };
        this.minParticleSize = new FloatValue(this, stringArray){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super("MinParticleSize", 0.5f, 0.0f, 5.0f, $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)Target.access$getMaxParticleSize$p(this.this$0).get()).floatValue();
                if (v < newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        stringArray = new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "rise", true) && (Boolean)Target.access$getRiseParticle$p(this.this$0).get() != false;
            }
        };
        this.maxParticleSize = new FloatValue(this, stringArray){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super("MaxParticleSize", 2.5f, 0.0f, 5.0f, $super_call_param$1);
            }

            protected void onChanged(float oldValue, float newValue) {
                float v = ((Number)Target.access$getMinParticleSize$p(this.this$0).get()).floatValue();
                if (v > newValue) {
                    this.set(Float.valueOf(v));
                }
            }
        };
        stringArray = Fonts.fontSFUI35;
        Intrinsics.checkNotNullExpressionValue(stringArray, "fontSFUI35");
        this.exhiFontValue = new FontValue("Exhi-Font", (FontRenderer)stringArray, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "exhibition", true);
            }
        });
        stringArray = new String[]{"Custom", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer", "Health"};
        this.colorModeValue = new ListValue("Color", stringArray, "Custom");
        this.redValue = new IntegerValue("Red", 252, 0, 255);
        this.greenValue = new IntegerValue("Green", 96, 0, 255);
        this.blueValue = new IntegerValue("Blue", 66, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 1.0f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.mixerSecondsValue = new IntegerValue("Seconds", 2, 1, 10);
        this.backgroundColorRedValue = new IntegerValue("Background-Red", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-Green", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-Blue", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 160, 0, 255);
        this.borderColorRedValue = new IntegerValue("Liquid-Border-Red", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce", true) || StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce+", true);
            }
        });
        this.borderColorGreenValue = new IntegerValue("Liquid-Border-Green", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce", true) || StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce+", true);
            }
        });
        this.borderColorBlueValue = new IntegerValue("Liquid-Border-Blue", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce", true) || StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce+", true);
            }
        });
        this.borderColorAlphaValue = new IntegerValue("Liquid-Border-Alpha", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ Target this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce", true) || StringsKt.equals((String)Target.access$getStyleValue$p(this.this$0).get(), "liquidbounce+", true);
            }
        });
        this.shieldIcon = new ResourceLocation("liquidplus/shield.png");
        this.particleList = new ArrayList();
        this.numberRenderer = new CharRenderer(false);
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @Override
    @NotNull
    public Border drawElement() {
        block149: {
            block147: {
                v0 = Client.INSTANCE.getModuleManager().get(KillAura.class);
                if (v0 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
                }
                kaTarget = ((KillAura)v0).getTarget();
                v1 = Client.INSTANCE.getModuleManager().get(TeleportAura.class);
                if (v1 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.TeleportAura");
                }
                taTarget = ((TeleportAura)v1).getLastTarget();
                actualTarget = kaTarget != null && kaTarget instanceof EntityPlayer != false ? (EntityPlayer)kaTarget : (taTarget != null && taTarget instanceof EntityPlayer != false ? (EntityPlayer)taTarget : (MinecraftInstance.mc.field_71462_r instanceof GuiChat != false && (Boolean)this.showUrselfWhenChatOpen.get() != false || MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner != false ? (EntityPlayer)MinecraftInstance.mc.field_71439_g : null));
                var5_4 = (String)this.colorModeValue.get();
                tmp = -1;
                switch (var5_4.hashCode()) {
                    case 83201: {
                        if (var5_4.equals("Sky")) {
                            tmp = 1;
                        }
                        break;
                    }
                    case -2137395588: {
                        if (var5_4.equals("Health")) {
                            tmp = 2;
                        }
                        break;
                    }
                    case -1656737386: {
                        if (var5_4.equals("Rainbow")) {
                            tmp = 3;
                        }
                        break;
                    }
                    case 74357737: {
                        if (var5_4.equals("Mixer")) {
                            tmp = 4;
                        }
                        break;
                    }
                    case 2029746065: {
                        if (var5_4.equals("Custom")) {
                            tmp = 5;
                        }
                        break;
                    }
                    case 2181788: {
                        if (var5_4.equals("Fade")) {
                            tmp = 6;
                        }
                        break;
                    }
                }
                switch (tmp) {
                    case 3: {
                        v2 = new Color(RenderUtils.getRainbowOpaque(((Number)this.mixerSecondsValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0));
                        break;
                    }
                    case 5: {
                        v2 = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue());
                        break;
                    }
                    case 1: {
                        v2 = RenderUtils.skyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case 6: {
                        v2 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), 0, 100);
                        break;
                    }
                    case 2: {
                        if (actualTarget != null) {
                            v2 = BlendUtils.getHealthColor(actualTarget.func_110143_aJ(), actualTarget.func_110138_aP());
                            break;
                        }
                        v2 = Color.green;
                        break;
                    }
                    case 4: {
                        v2 = ColorMixer.getMixedColor(0, ((Number)this.mixerSecondsValue.get()).intValue());
                        break;
                    }
                    default: {
                        v3 = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        v2 = v3;
                        Intrinsics.checkNotNull(v3);
                    }
                }
                barColor = v2;
                bgColor = new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                borderColor = new Color(((Number)this.borderColorRedValue.get()).intValue(), ((Number)this.borderColorGreenValue.get()).intValue(), ((Number)this.borderColorBlueValue.get()).intValue(), ((Number)this.borderColorAlphaValue.get()).intValue());
                this.progress += 0.0025f * (float)RenderUtils.deltaTime * (actualTarget != null ? -1.0f : 1.0f);
                this.progressChill += 0.0075f * (float)RenderUtils.deltaTime * (actualTarget != null ? -1.0f : 1.0f);
                if (this.progress < 0.0f) {
                    this.progress = 0.0f;
                } else if (this.progress > 1.0f) {
                    this.progress = 1.0f;
                }
                if (this.progressChill < 0.0f || !((Boolean)this.chillFadingValue.get()).booleanValue()) {
                    this.progressChill = 0.0f;
                } else if (this.progressChill > 1.0f) {
                    this.progressChill = 1.0f;
                }
                if (StringsKt.equals((String)this.styleValue.get(), "chill", true)) {
                    if (actualTarget == null && ((Boolean)this.chillFadingValue.get()).booleanValue()) {
                        if (this.progressChill >= 1.0f && this.target != null) {
                            this.target = null;
                        }
                    } else {
                        this.target = actualTarget;
                    }
                } else if (actualTarget == null && ((Boolean)this.tSlideAnim.get()).booleanValue()) {
                    if (this.progress >= 1.0f && this.target != null) {
                        this.target = null;
                    }
                } else {
                    this.target = actualTarget;
                }
                animProgress = EaseUtils.easeInQuart(this.progress);
                tHeight = this.getTBorder().getY2() - this.getTBorder().getY();
                if (((Boolean)this.tSlideAnim.get()).booleanValue() && !StringsKt.equals((String)this.styleValue.get(), "rise", true) && !StringsKt.equals((String)this.styleValue.get(), "chill", true)) {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)0.0, (double)((-this.getRenderY() - (double)tHeight) * animProgress), (double)0.0);
                }
                if (this.target == null) break block147;
                v4 = this.target;
                Intrinsics.checkNotNull(v4);
                convertedTarget = v4;
                var11_10 = (String)this.styleValue.get();
                tmp = -1;
                switch (var11_10.hashCode()) {
                    case 548855207: {
                        if (var11_10.equals("LiquidBounce+")) {
                            tmp = 1;
                        }
                        break;
                    }
                    case 1712985934: {
                        if (var11_10.equals("Novoline")) {
                            tmp = 2;
                        }
                        break;
                    }
                    case 65078532: {
                        if (var11_10.equals("Chill")) {
                            tmp = 3;
                        }
                        break;
                    }
                    case -1815582866: {
                        if (var11_10.equals("Slowly")) {
                            tmp = 4;
                        }
                        break;
                    }
                    case 2192905: {
                        if (var11_10.equals("Flux")) {
                            tmp = 5;
                        }
                        break;
                    }
                    case -1367768316: {
                        if (var11_10.equals("LiquidBounce")) {
                            tmp = 6;
                        }
                        break;
                    }
                    case 2547433: {
                        if (var11_10.equals("Rise")) {
                            tmp = 7;
                        }
                        break;
                    }
                    case -352259601: {
                        if (var11_10.equals("Exhibition")) {
                            tmp = 8;
                        }
                        break;
                    }
                }
                switch (tmp) {
                    case 6: {
                        if (!Intrinsics.areEqual(convertedTarget, this.lastTarget) || this.easingHealth < 0.0f || this.easingHealth > convertedTarget.func_110138_aP() || (double)Math.abs(this.easingHealth - convertedTarget.func_110143_aJ()) < 0.01) {
                            this.easingHealth = convertedTarget.func_110143_aJ();
                        }
                        var13_11 = convertedTarget.func_70005_c_();
                        Intrinsics.checkNotNullExpressionValue(var13_11, "convertedTarget.name");
                        width = RangesKt.coerceAtLeast(38 + Fonts.font40.func_78256_a(var13_11), 118);
                        RenderUtils.drawBorderedRect(0.0f, 0.0f, width, 36.0f, 3.0f, borderColor.getRGB(), bgColor.getRGB());
                        if (this.easingHealth > convertedTarget.func_110143_aJ()) {
                            RenderUtils.drawRect(0.0f, 34.0f, this.easingHealth / convertedTarget.func_110138_aP() * width, 36.0f, new Color(252, 185, 65).getRGB());
                        }
                        RenderUtils.drawRect(0.0f, 34.0f, convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP() * width, 36.0f, barColor.getRGB());
                        if (this.easingHealth < convertedTarget.func_110143_aJ()) {
                            RenderUtils.drawRect(this.easingHealth / convertedTarget.func_110138_aP() * width, 34.0f, convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP() * width, 36.0f, new Color(44, 201, 144).getRGB());
                        }
                        this.easingHealth += (convertedTarget.func_110143_aJ() - this.easingHealth) / (float)Math.pow(2.0f, 10.0f - ((Number)this.fadeSpeed.get()).floatValue()) * (float)RenderUtils.deltaTime;
                        Fonts.font40.func_78276_b(convertedTarget.func_70005_c_(), 36, 3, 0xFFFFFF);
                        var13_11 = MinecraftInstance.mc.field_71439_g;
                        Intrinsics.checkNotNullExpressionValue(var13_11, "mc.thePlayer");
                        Fonts.font35.func_78276_b(Intrinsics.stringPlus("Distance: ", this.decimalFormat.format(PlayerExtensionKt.getDistanceToEntityBox((Entity)var13_11, (Entity)convertedTarget))), 36, 15, 0xFFFFFF);
                        playerInfo = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au());
                        if (playerInfo != null) {
                            Fonts.font35.func_78276_b(Intrinsics.stringPlus("Ping: ", RangesKt.coerceAtLeast(playerInfo.func_178853_c(), 0)), 36, 24, 0xFFFFFF);
                            locationSkin = playerInfo.func_178837_g();
                            Intrinsics.checkNotNullExpressionValue(locationSkin, "locationSkin");
                            this.drawHead(locationSkin, 30, 30);
                            break;
                        }
                        break block149;
                    }
                    case 5: {
                        playerInfo = convertedTarget.func_70005_c_();
                        Intrinsics.checkNotNullExpressionValue(playerInfo, "convertedTarget.name");
                        width = RangesKt.coerceAtLeast(26.0f + (float)Fonts.fontSFUI40.func_78256_a(playerInfo), 26.0f + (float)Fonts.fontSFUI35.func_78256_a(Intrinsics.stringPlus("Health: ", this.decimalFormat2.format(Float.valueOf(convertedTarget.func_110143_aJ()))))) + 10.0f;
                        RenderUtils.drawRoundedRect(-1.0f, -1.0f, 1.0f + width, 47.0f, 1.0f, new Color(35, 35, 40, 230).getRGB());
                        if (MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()) != null) {
                            playerInfo = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()).func_178837_g();
                            Intrinsics.checkNotNullExpressionValue(playerInfo, "mc.netHandler.getPlayerI\u2026et.uniqueID).locationSkin");
                            this.drawHead((ResourceLocation)playerInfo, 1, 1, 26, 26);
                        }
                        playerInfo = convertedTarget.func_70005_c_();
                        Intrinsics.checkNotNullExpressionValue(playerInfo, "convertedTarget.name");
                        Fonts.fontSFUI40.drawString(playerInfo, 30.0f, 5.0f, 0xFFFFFF);
                        Fonts.fontSFUI35.drawString(Intrinsics.stringPlus("Health: ", this.decimalFormat2.format(Float.valueOf(convertedTarget.func_110143_aJ()))), 30.0f, 17.5f, 0xFFFFFF);
                        Fonts.fontSFUI35.drawString("\u2764", 2.0f, 29.0f, -1);
                        this.drawArmorIcon(2, 38, 7, 7);
                        this.easingHealth += (float)((double)(convertedTarget.func_110143_aJ() - this.easingHealth) / Math.pow(2.0, 7.0)) * (float)RenderUtils.deltaTime;
                        RenderUtils.drawRect(12.0f, 30.0f, 12.0f + width - 15.0f, 33.0f, new Color(20, 20, 20, 255).getRGB());
                        RenderUtils.drawRect(12.0f, 40.0f, 12.0f + width - 15.0f, 43.0f, new Color(20, 20, 20, 255).getRGB());
                        if (this.easingHealth < 0.0f || this.easingHealth > convertedTarget.func_110138_aP()) {
                            this.easingHealth = convertedTarget.func_110143_aJ();
                        }
                        if (this.easingHealth > convertedTarget.func_110143_aJ()) {
                            RenderUtils.drawRect(12.0f, 30.0f, 12.0f + this.easingHealth / convertedTarget.func_110138_aP() * (width - 15.0f), 33.0f, new Color(231, 182, 0, 255).getRGB());
                        }
                        RenderUtils.drawRect(12.0f, 30.0f, 12.0f + convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP() * (width - 15.0f), 33.0f, new Color(0, 224, 84, 255).getRGB());
                        if (convertedTarget.func_70658_aO() != 0) {
                            RenderUtils.drawRect(12.0f, 40.0f, 12.0f + (float)convertedTarget.func_70658_aO() / 20.0f * (width - 15.0f), 43.0f, new Color(77, 128, 255, 255).getRGB());
                            break;
                        }
                        break block149;
                    }
                    case 2: {
                        font = Fonts.minecraftFont;
                        fontHeight = font.field_78288_b;
                        mainColor = barColor;
                        percent = convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP() * 100.0f;
                        nameLength = (float)RangesKt.coerceAtLeast(font.func_78256_a(convertedTarget.func_70005_c_()), font.func_78256_a(Intrinsics.stringPlus(this.decimalFormat2.format(Float.valueOf(percent)), "%"))) + 10.0f;
                        barWidth = RangesKt.coerceIn(convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP(), 0.0f, convertedTarget.func_110138_aP()) * (nameLength - 2.0f);
                        RenderUtils.drawRect(-2.0f, -2.0f, 3.0f + nameLength + 36.0f, 38.0f, new Color(24, 24, 24, 255).getRGB());
                        RenderUtils.drawRect(-1.0f, -1.0f, 2.0f + nameLength + 36.0f, 37.0f, new Color(31, 31, 31, 255).getRGB());
                        if (MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()) != null) {
                            var18_48 = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()).func_178837_g();
                            Intrinsics.checkNotNullExpressionValue(var18_48, "mc.netHandler.getPlayerI\u2026et.uniqueID).locationSkin");
                            this.drawHead(var18_48, 0, 0, 36, 36);
                        }
                        font.func_175063_a(convertedTarget.func_70005_c_(), 39.0f, 2.0f, -1);
                        RenderUtils.drawRect(38.0f, 15.0f, 36.0f + nameLength, 25.0f, new Color(24, 24, 24, 255).getRGB());
                        this.easingHealth += (convertedTarget.func_110143_aJ() - this.easingHealth) / (float)Math.pow(2.0f, 10.0f - ((Number)this.fadeSpeed.get()).floatValue()) * (float)RenderUtils.deltaTime;
                        animateThingy = RangesKt.coerceIn(this.easingHealth, convertedTarget.func_110143_aJ(), convertedTarget.func_110138_aP()) / convertedTarget.func_110138_aP() * (nameLength - 2.0f);
                        if (this.easingHealth > convertedTarget.func_110143_aJ()) {
                            RenderUtils.drawRect(38.0f, 15.0f, 38.0f + animateThingy, 25.0f, mainColor.darker().getRGB());
                        }
                        RenderUtils.drawRect(38.0f, 15.0f, 38.0f + barWidth, 25.0f, mainColor.getRGB());
                        font.func_175063_a(Intrinsics.stringPlus(this.decimalFormat2.format(Float.valueOf(percent)), "%"), 38.0f + (nameLength - 2.0f) / 2.0f - (float)font.func_78256_a(Intrinsics.stringPlus(this.decimalFormat2.format(Float.valueOf(percent)), "%")) / 2.0f, 16.0f, -1);
                        break;
                    }
                    case 4: {
                        font = Fonts.minecraftFont;
                        length = (float)RangesKt.coerceAtLeast(RangesKt.coerceAtLeast(60, font.func_78256_a(convertedTarget.func_70005_c_())), font.func_78256_a(Intrinsics.stringPlus(this.decimalFormat2.format(Float.valueOf(convertedTarget.func_110143_aJ())), " \u2764"))) + 10.0f;
                        RenderUtils.drawRect(0.0f, 0.0f, 32.0f + length, 36.0f, bgColor.getRGB());
                        if (MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()) != null) {
                            mainColor = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()).func_178837_g();
                            Intrinsics.checkNotNullExpressionValue(mainColor, "mc.netHandler.getPlayerI\u2026et.uniqueID).locationSkin");
                            this.drawHead(mainColor, 1, 1, 30, 30);
                        }
                        font.func_175063_a(convertedTarget.func_70005_c_(), 33.0f, 2.0f, -1);
                        font.func_175063_a(Intrinsics.stringPlus(this.decimalFormat2.format(Float.valueOf(convertedTarget.func_110143_aJ())), " \u2764"), length + 32.0f - 1.0f - (float)font.func_78256_a(Intrinsics.stringPlus(this.decimalFormat2.format(Float.valueOf(convertedTarget.func_110143_aJ())), " \u2764")), 22.0f, barColor.getRGB());
                        this.easingHealth += (convertedTarget.func_110143_aJ() - this.easingHealth) / (float)Math.pow(2.0f, 10.0f - ((Number)this.fadeSpeed.get()).floatValue()) * (float)RenderUtils.deltaTime;
                        RenderUtils.drawRect(0.0f, 32.0f, RangesKt.coerceIn(this.easingHealth / convertedTarget.func_110138_aP(), 0.0f, convertedTarget.func_110138_aP()) * (length + 32.0f), 36.0f, barColor.getRGB());
                        break;
                    }
                    case 7: {
                        font = Fonts.fontSFUI40;
                        name = Intrinsics.stringPlus("Name: ", convertedTarget.func_70005_c_());
                        percent = MinecraftInstance.mc.field_71439_g;
                        Intrinsics.checkNotNullExpressionValue(percent, "mc.thePlayer");
                        info = Intrinsics.stringPlus("Distance: ", this.decimalFormat2.format(PlayerExtensionKt.getDistanceToEntityBox((Entity)percent, (Entity)convertedTarget)));
                        this.easingHealth += (convertedTarget.func_110143_aJ() - this.easingHealth) / (float)Math.pow(2.0f, 10.0f - ((Number)this.fadeSpeed.get()).floatValue()) * (float)RenderUtils.deltaTime;
                        healthName = this.decimalFormat2.format(Float.valueOf(this.easingHealth)).toString();
                        length = RangesKt.coerceAtLeast((float)RangesKt.coerceAtLeast(font.func_78256_a(name), font.func_78256_a(info)) + 40.0f, 125.0f);
                        floatX = (float)this.getRenderX();
                        floatY = (float)this.getRenderY();
                        if (((Boolean)this.blurValue.get()).booleanValue()) {
                            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                            GL11.glPushMatrix();
                            if (((Boolean)this.riseShadow.get()).booleanValue() && !((Boolean)this.riseShadowLegacy.get()).booleanValue()) {
                                BlurUtils.blurArea(floatX, floatY, floatX + 10.0f + length, floatY + 55.0f, ((Number)this.blurStrength.get()).floatValue());
                            } else {
                                BlurUtils.blurAreaRounded(floatX, floatY, floatX + 10.0f + length, floatY + 55.0f, 3.0f, ((Number)this.blurStrength.get()).floatValue());
                            }
                            GL11.glPopMatrix();
                            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                        }
                        if (((Boolean)this.riseShadow.get()).booleanValue()) {
                            if (!((Boolean)this.riseShadowLegacy.get()).booleanValue()) {
                                GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                                GL11.glPushMatrix();
                                ShadowUtils.processShadow(true, ((Number)this.shadowStrengthValue.get()).intValue());
                                RenderUtils.drawRoundedRect(floatX, floatY, floatX + 10.0f + length, floatY + 55.0f, 3.0f, bgColor.getRGB());
                                ShadowUtils.processShadow(false, ((Number)this.shadowStrengthValue.get()).intValue());
                                GL11.glPopMatrix();
                                GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                            } else {
                                UiUtils.fastShadowRoundedRect(0.0f, 0.0f, 10.0f + length, 55.0f, 3.0f, ((Number)this.shadowStrengthValue.get()).intValue(), bgColor);
                            }
                        } else {
                            RenderUtils.drawRoundedRect(0.0f, 0.0f, 10.0f + length, 55.0f, 3.0f, bgColor.getRGB());
                        }
                        if (((Boolean)this.riseParticle.get()).booleanValue()) {
                            if (this.gotDamaged) {
                                parSize = RandomUtils.nextFloat(((Number)this.minParticleSize.get()).floatValue(), ((Number)this.maxParticleSize.get()).floatValue());
                                distParticle = ((Number)this.particleRange.get()).floatValue();
                                var21_64 = 0;
                                var22_68 = ((Number)this.generateAmountValue.get()).intValue();
                                if (var21_64 <= var22_68) {
                                    do {
                                        j = var21_64++;
                                        var25_77 /* !! */  = new float[]{0.0f, 1.0f};
                                        v5 = var25_77 /* !! */ ;
                                        var25_77 /* !! */  = (float[])new Color[2];
                                        var26_82 = Color.white;
                                        Intrinsics.checkNotNullExpressionValue(var26_82, "white");
                                        var25_77 /* !! */ [0] = (float)var26_82;
                                        Intrinsics.checkNotNullExpressionValue(barColor, "barColor");
                                        var25_77 /* !! */ [1] = (float)barColor;
                                        var24_74 = BlendUtils.blendColors(v5, (Color[])var25_77 /* !! */ , RandomUtils.nextBoolean() != false ? RandomUtils.nextFloat(0.5f, 1.0f) : 0.0f);
                                        Intrinsics.checkNotNullExpressionValue(var24_74, "blendColors(floatArrayOf\u2026loat(0.5F, 1.0F) else 0F)");
                                        this.particleList.add(new Particle(var24_74, RandomUtils.nextFloat(-distParticle, distParticle), RandomUtils.nextFloat(-distParticle, distParticle), parSize));
                                        parSize = RandomUtils.nextFloat(((Number)this.minParticleSize.get()).floatValue(), ((Number)this.maxParticleSize.get()).floatValue());
                                    } while (j != var22_68);
                                }
                                this.gotDamaged = false;
                            }
                            deleteQueue = new ArrayList<E>();
                            $this$forEach$iv = this.particleList;
                            $i$f$forEach = false;
                            var22_69 = $this$forEach$iv.iterator();
                            while (var22_69.hasNext()) {
                                element$iv = var22_69.next();
                                particle = (Particle)element$iv;
                                $i$a$-forEach-Target$drawElement$1 = false;
                                if (particle.getAlpha() > 0.0f) {
                                    particle.render(20.0f, 20.0f, (Boolean)this.riseParticleFade.get(), ((Number)this.riseParticleSpeed.get()).floatValue(), ((Number)this.riseParticleFadingSpeed.get()).floatValue());
                                    continue;
                                }
                                deleteQueue.add(particle);
                            }
                            for (Particle p : deleteQueue) {
                                this.particleList.remove(p);
                            }
                        }
                        scaleHT = RangesKt.coerceIn((float)convertedTarget.field_70737_aN / (float)RangesKt.coerceAtLeast(convertedTarget.field_70738_aO, 1), 0.0f, 1.0f);
                        if (((Boolean)this.riseShadow.get()).booleanValue() && !((Boolean)this.riseShadowLegacy.get()).booleanValue() && ((Boolean)this.riseShadowHead.get()).booleanValue() && convertedTarget.field_70737_aN > 0) {
                            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                            GL11.glPushMatrix();
                            ShadowUtils.processShadow(true, scaleHT * 9.0f);
                            RenderUtils.newDrawRect(floatX + 5.0f + 15.0f * (scaleHT * 0.2f), floatY + 5.0f + 15.0f * (scaleHT * 0.2f), floatX + 35.0f - 15.0f * (scaleHT * 0.2f), floatY + 35.0f - 15.0f * (scaleHT * 0.2f), new Color(255, 0, 0).getRGB());
                            ShadowUtils.processShadow(false, scaleHT * 9.0f);
                            GL11.glPopMatrix();
                            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                        }
                        if (MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()) != null) {
                            $this$forEach$iv = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au()).func_178837_g();
                            Intrinsics.checkNotNullExpressionValue($this$forEach$iv, "mc.netHandler.getPlayerI\u2026et.uniqueID).locationSkin");
                            this.drawHead((ResourceLocation)$this$forEach$iv, 5.0f + 15.0f * (scaleHT * 0.2f), 5.0f + 15.0f * (scaleHT * 0.2f), 1.0f - scaleHT * 0.2f, 30, 30, 1.0f, 0.4f + (1.0f - scaleHT) * 0.6f, 0.4f + (1.0f - scaleHT) * 0.6f);
                        }
                        maxHealthLength = font.func_78256_a(this.decimalFormat2.format(Float.valueOf(convertedTarget.func_110138_aP())).toString());
                        GlStateManager.func_179117_G();
                        font.drawString(name, 39.0f, 11.0f, -1);
                        font.drawString(info, 39.0f, 23.0f, -1);
                        barWidth = (length - 5.0f - maxHealthLength) * RangesKt.coerceIn(this.easingHealth / convertedTarget.func_110138_aP(), 0.0f, 1.0f);
                        particle = ((String)this.colorModeValue.get()).toLowerCase();
                        Intrinsics.checkNotNullExpressionValue(particle, "this as java.lang.String).toLowerCase()");
                        var22_69 = particle;
                        if (!Intrinsics.areEqual(var22_69, "custom")) ** GOTO lbl352
                        RenderUtils.drawRect(5.0f, 42.0f, 5.0f + barWidth, 48.0f, barColor.getRGB());
                        ** GOTO lbl468
lbl352:
                        // 1 sources

                        if (!Intrinsics.areEqual(var22_69, "health")) ** GOTO lbl355
                        RenderUtils.drawRect(5.0f, 42.0f, 5.0f + barWidth, 48.0f, BlendUtils.getHealthColor(this.easingHealth, convertedTarget.func_110138_aP()).getRGB());
                        ** GOTO lbl468
lbl355:
                        // 1 sources

                        GL11.glPushMatrix();
                        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        RenderUtils.makeScissorBox(5.0f * this.getScale() + (float)this.getRenderX() * this.getScale(), 0.0f, 5.0f * this.getScale() + (float)this.getRenderX() * this.getScale() + barWidth * this.getScale(), 48.0f * this.getScale() + (float)this.getRenderY() * this.getScale());
                        GL11.glEnable((int)3089);
                        element$iv = 0;
                        particle = ((Number)this.gradientAmountValue.get()).intValue() - 1;
                        if (element$iv > particle) ** GOTO lbl466
                        do {
                            i = element$iv++;
                            barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * (double)(length - 5.0f - maxHealthLength);
                            barEnd = (double)(i + 1) / (double)((Number)this.gradientAmountValue.get()).intValue() * (double)(length - 5.0f - maxHealthLength);
                            var30_85 = (String)this.colorModeValue.get();
                            tmp = -1;
                            switch (var30_85.hashCode()) {
                                case 83201: {
                                    if (var30_85.equals("Sky")) {
                                        tmp = 1;
                                    }
                                    break;
                                }
                                case -884013110: {
                                    if (var30_85.equals("LiquidSlowly")) {
                                        tmp = 2;
                                    }
                                    break;
                                }
                                case -1656737386: {
                                    if (var30_85.equals("Rainbow")) {
                                        tmp = 3;
                                    }
                                    break;
                                }
                                case 74357737: {
                                    if (var30_85.equals("Mixer")) {
                                        tmp = 4;
                                    }
                                    break;
                                }
                                case 2181788: {
                                    if (var30_85.equals("Fade")) {
                                        tmp = 5;
                                    }
                                    break;
                                }
                            }
                            switch (tmp) {
                                case 3: {
                                    v6 = RenderUtils.getRainbowOpaque(((Number)this.mixerSecondsValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), i * ((Number)this.distanceValue.get()).intValue());
                                    break;
                                }
                                case 1: {
                                    v6 = RenderUtils.SkyRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                    break;
                                }
                                case 2: {
                                    v7 = ColorUtils.LiquidSlowly(System.nanoTime(), i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                    Intrinsics.checkNotNull(v7);
                                    v6 = v7.getRGB();
                                    break;
                                }
                                case 4: {
                                    v6 = ColorMixer.getMixedColor(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.mixerSecondsValue.get()).intValue()).getRGB();
                                    break;
                                }
                                case 5: {
                                    v6 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                                    break;
                                }
                                default: {
                                    v6 = -1;
                                }
                            }
                            var30_85 = (String)this.colorModeValue.get();
                            tmp = -1;
                            switch (var30_85.hashCode()) {
                                case 83201: {
                                    if (var30_85.equals("Sky")) {
                                        tmp = 1;
                                    }
                                    break;
                                }
                                case -884013110: {
                                    if (var30_85.equals("LiquidSlowly")) {
                                        tmp = 2;
                                    }
                                    break;
                                }
                                case -1656737386: {
                                    if (var30_85.equals("Rainbow")) {
                                        tmp = 3;
                                    }
                                    break;
                                }
                                case 74357737: {
                                    if (var30_85.equals("Mixer")) {
                                        tmp = 4;
                                    }
                                    break;
                                }
                                case 2181788: {
                                    if (var30_85.equals("Fade")) {
                                        tmp = 5;
                                    }
                                    break;
                                }
                            }
                            switch (tmp) {
                                case 3: {
                                    v8 = RenderUtils.getRainbowOpaque(((Number)this.mixerSecondsValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (i + 1) * ((Number)this.distanceValue.get()).intValue());
                                    break;
                                }
                                case 1: {
                                    v8 = RenderUtils.SkyRainbow((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                    break;
                                }
                                case 2: {
                                    v9 = ColorUtils.LiquidSlowly(System.nanoTime(), (i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                                    Intrinsics.checkNotNull(v9);
                                    v8 = v9.getRGB();
                                    break;
                                }
                                case 4: {
                                    v8 = ColorMixer.getMixedColor((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.mixerSecondsValue.get()).intValue()).getRGB();
                                    break;
                                }
                                case 5: {
                                    v8 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (i + 1) * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                                    break;
                                }
                                default: {
                                    v8 = -1;
                                }
                            }
                            RenderUtils.drawGradientSideways(5.0 + barStart, 42.0, 5.0 + barEnd, 48.0, v6, v8);
                        } while (i != particle);
lbl466:
                        // 2 sources

                        GL11.glDisable((int)3089);
                        GL11.glPopMatrix();
lbl468:
                        // 3 sources

                        GlStateManager.func_179117_G();
                        font.drawString(healthName, 10.0f + barWidth, 41.0f, -1);
                        break;
                    }
                    case 8: {
                        font = (FontRenderer)this.exhiFontValue.get();
                        minWidth = RangesKt.coerceAtLeast(140.0f, 45.0f + (float)font.func_78256_a(convertedTarget.func_70005_c_()));
                        RenderUtils.drawExhiRect(0.0f, 0.0f, minWidth, 45.0f);
                        RenderUtils.drawRect(2.5f, 2.5f, 42.5f, 42.5f, new Color(59, 59, 59).getRGB());
                        RenderUtils.drawRect(3.0f, 3.0f, 42.0f, 42.0f, new Color(19, 19, 19).getRGB());
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        RenderUtils.drawEntityOnScreen(22, 40, 15, (EntityLivingBase)convertedTarget);
                        font.func_78276_b(convertedTarget.func_70005_c_(), 46, 4, -1);
                        barLength = 60.0f * RangesKt.coerceIn(convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP(), 0.0f, 1.0f);
                        RenderUtils.drawRect(45.0f, 14.0f, 105.0f, 17.0f, BlendUtils.getHealthColor(convertedTarget.func_110143_aJ(), convertedTarget.func_110138_aP()).darker().darker().darker().getRGB());
                        RenderUtils.drawRect(45.0f, 14.0f, 45.0f + barLength, 17.0f, BlendUtils.getHealthColor(convertedTarget.func_110143_aJ(), convertedTarget.func_110138_aP()).getRGB());
                        healthName = 0;
                        while (healthName < 10) {
                            i = healthName++;
                            RenderUtils.drawBorder(45.0f + (float)i * 6.0f, 14.0f, 45.0f + ((float)i + 1.0f) * 6.0f, 17.0f, 0.25f, Color.black.getRGB());
                        }
                        GL11.glPushMatrix();
                        GL11.glTranslatef((float)46.0f, (float)20.0f, (float)0.0f);
                        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                        v10 = new StringBuilder().append("HP: ").append((int)convertedTarget.func_110143_aJ()).append(" | Dist: ");
                        healthName = MinecraftInstance.mc.field_71439_g;
                        Intrinsics.checkNotNullExpressionValue(healthName, "mc.thePlayer");
                        Fonts.minecraftFont.func_78276_b(v10.append((int)PlayerExtensionKt.getDistanceToEntityBox((Entity)healthName, (Entity)convertedTarget)).toString(), 0, 0, -1);
                        GL11.glPopMatrix();
                        GlStateManager.func_179117_G();
                        GL11.glPushMatrix();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        RenderHelper.func_74520_c();
                        renderItem = MinecraftInstance.mc.func_175599_af();
                        x = 45;
                        y = 26;
                        floatY = 3;
                        do {
                            if (convertedTarget.field_71071_by.field_70460_b[index = floatY--] == null || stack.func_77973_b() == null) continue;
                            renderItem.func_175042_a(stack, x, y);
                            renderItem.func_175030_a(MinecraftInstance.mc.field_71466_p, stack, x, y);
                            x += 18;
                        } while (0 <= floatY);
                        mainStack = convertedTarget.func_70694_bm();
                        if (mainStack != null && mainStack.func_77973_b() != null) {
                            renderItem.func_175042_a(mainStack, x, y);
                            renderItem.func_175030_a(MinecraftInstance.mc.field_71466_p, mainStack, x, y);
                        }
                        RenderHelper.func_74518_a();
                        GlStateManager.func_179141_d();
                        GlStateManager.func_179084_k();
                        GlStateManager.func_179140_f();
                        GlStateManager.func_179129_p();
                        GL11.glPopMatrix();
                        break;
                    }
                    case 1: {
                        if (!Intrinsics.areEqual(convertedTarget, this.lastTarget) || this.easingHealth < 0.0f || this.easingHealth > convertedTarget.func_110138_aP() || (double)Math.abs(this.easingHealth - convertedTarget.func_110143_aJ()) < 0.01) {
                            this.easingHealth = convertedTarget.func_110143_aJ();
                        }
                        minWidth = convertedTarget.func_70005_c_();
                        Intrinsics.checkNotNullExpressionValue(minWidth, "convertedTarget.name");
                        width = RangesKt.coerceAtLeast(38 + Fonts.font40.func_78256_a(minWidth), 120);
                        RenderUtils.drawBorderedRect(0.0f, 0.0f, width, 36.0f, 3.0f, borderColor.getRGB(), bgColor.getRGB());
                        if (this.easingHealth > convertedTarget.func_110143_aJ()) {
                            RenderUtils.drawRect(0.0f, 34.0f, this.easingHealth / convertedTarget.func_110138_aP() * width, 36.0f, new Color(252, 185, 65).getRGB());
                        }
                        RenderUtils.drawRect(0.0f, 34.0f, convertedTarget.func_110143_aJ() / convertedTarget.func_110138_aP() * width, 36.0f, barColor.getRGB());
                        this.easingHealth += (convertedTarget.func_110143_aJ() - this.easingHealth) / (float)Math.pow(2.0f, 10.0f - ((Number)this.fadeSpeed.get()).floatValue()) * (float)RenderUtils.deltaTime;
                        Fonts.font40.func_78276_b(convertedTarget.func_70005_c_(), 36, 3, 0xFFFFFF);
                        minWidth = MinecraftInstance.mc.field_71439_g;
                        Intrinsics.checkNotNullExpressionValue(minWidth, "mc.thePlayer");
                        Fonts.font35.func_78276_b(Intrinsics.stringPlus("Distance: ", this.decimalFormat.format(PlayerExtensionKt.getDistanceToEntityBox((Entity)minWidth, (Entity)convertedTarget))), 36, 15, 0xFFFFFF);
                        playerInfo = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au());
                        if (playerInfo != null) {
                            Fonts.font35.func_78276_b(Intrinsics.stringPlus("Ping: ", RangesKt.coerceAtLeast(playerInfo.func_178853_c(), 0)), 36, 24, 0xFFFFFF);
                            locationSkin = playerInfo.func_178837_g();
                            scaleHT = RangesKt.coerceIn((float)convertedTarget.field_70737_aN / (float)RangesKt.coerceAtLeast(convertedTarget.field_70738_aO, 1), 0.0f, 1.0f);
                            Intrinsics.checkNotNullExpressionValue(locationSkin, "locationSkin");
                            this.drawHead(locationSkin, 2.0f + 15.0f * (scaleHT * 0.2f), 2.0f + 15.0f * (scaleHT * 0.2f), 1.0f - scaleHT * 0.2f, 30, 30, 1.0f, 0.4f + (1.0f - scaleHT) * 0.6f, 0.4f + (1.0f - scaleHT) * 0.6f);
                            break;
                        }
                        break block149;
                    }
                    case 3: {
                        this.easingHealth += (convertedTarget.func_110143_aJ() - this.easingHealth) / (float)Math.pow(2.0f, 10.0f - ((Number)this.fadeSpeed.get()).floatValue()) * (float)RenderUtils.deltaTime;
                        name = convertedTarget.func_70005_c_();
                        health = convertedTarget.func_110143_aJ();
                        Intrinsics.checkNotNullExpressionValue(name, "name");
                        v11 = Fonts.font40.func_78256_a(name);
                        scaleHT = this.decimalFormat.format(Float.valueOf(health));
                        Intrinsics.checkNotNullExpressionValue(scaleHT, "decimalFormat.format(health)");
                        tWidth = RangesKt.coerceAtLeast(45.0f + (float)RangesKt.coerceAtLeast(v11, Fonts.font72.func_78256_a(scaleHT)), (Boolean)this.chillHealthBarValue.get() != false ? 150.0f : 90.0f);
                        playerInfo = MinecraftInstance.mc.func_147114_u().func_175102_a(convertedTarget.func_110124_au());
                        reColorBg = new Color((float)bgColor.getRed() / 255.0f, (float)bgColor.getGreen() / 255.0f, (float)bgColor.getBlue() / 255.0f, (float)bgColor.getAlpha() / 255.0f * (1.0f - this.progressChill));
                        reColorBar = new Color((float)barColor.getRed() / 255.0f, (float)barColor.getGreen() / 255.0f, (float)barColor.getBlue() / 255.0f, (float)barColor.getAlpha() / 255.0f * (1.0f - this.progressChill));
                        reColorText = new Color(1.0f, 1.0f, 1.0f, 1.0f - this.progressChill);
                        floatX = (float)this.getRenderX();
                        floatY = (float)this.getRenderY();
                        calcScaleX = this.progressChill * (4.0f / (tWidth / 2.0f));
                        calcScaleY = (Boolean)this.chillHealthBarValue.get() != false ? this.progressChill * 0.16666667f : this.progressChill * 0.21052632f;
                        calcTranslateX = floatX + tWidth / 2.0f * calcScaleX;
                        calcTranslateY = floatY + ((Boolean)this.chillHealthBarValue.get() != false ? 24.0f * (this.progressChill * 0.16666667f) : 19.0f * (this.progressChill * 0.21052632f));
                        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glPopMatrix();
                        GL11.glPushMatrix();
                        if (((Boolean)this.chillFadingValue.get()).booleanValue()) {
                            GL11.glTranslatef((float)calcTranslateX, (float)calcTranslateY, (float)0.0f);
                            GL11.glScalef((float)(1.0f - calcScaleX), (float)(1.0f - calcScaleY), (float)(1.0f - calcScaleX));
                        } else {
                            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        }
                        RenderUtils.drawRoundedRect(0.0f, 0.0f, tWidth, (Boolean)this.chillHealthBarValue.get() != false ? 48.0f : 38.0f, 7.0f, reColorBg.getRGB());
                        GlStateManager.func_179117_G();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        if (playerInfo != null) {
                            Stencil.write(false);
                            GL11.glDisable((int)3553);
                            GL11.glEnable((int)3042);
                            GL11.glBlendFunc((int)770, (int)771);
                            RenderUtils.fastRoundedRect(4.0f, 4.0f, 34.0f, 34.0f, 8.0f);
                            GL11.glDisable((int)3042);
                            GL11.glEnable((int)3553);
                            Stencil.erase(true);
                            var25_81 = playerInfo.func_178837_g();
                            Intrinsics.checkNotNullExpressionValue(var25_81, "playerInfo.locationSkin");
                            this.drawHead(var25_81, 4, 4, 30, 30, 1.0f - this.progressChill);
                            Stencil.dispose();
                        }
                        GlStateManager.func_179117_G();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        Fonts.font40.func_175065_a(name, 38.0f, 6.0f, reColorText.getRGB(), false);
                        this.numberRenderer.renderChar(health, calcTranslateX, calcTranslateY, 38.0f, 17.0f, calcScaleX, calcScaleY, false, ((Number)this.chillFontSpeed.get()).floatValue(), reColorText.getRGB());
                        GlStateManager.func_179117_G();
                        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        if (((Boolean)this.chillHealthBarValue.get()).booleanValue()) {
                            RenderUtils.drawRoundedRect(4.0f, 38.0f, tWidth - 4.0f, 44.0f, 3.0f, reColorBar.darker().darker().darker().getRGB());
                            Stencil.write(false);
                            GL11.glDisable((int)3553);
                            GL11.glEnable((int)3042);
                            GL11.glBlendFunc((int)770, (int)771);
                            RenderUtils.fastRoundedRect(4.0f, 38.0f, tWidth - 4.0f, 44.0f, 3.0f);
                            GL11.glDisable((int)3042);
                            Stencil.erase(true);
                            RenderUtils.drawRect(4.0f, 38.0f, 4.0f + this.easingHealth / convertedTarget.func_110138_aP() * (tWidth - 8.0f), 44.0f, reColorBar.getRGB());
                            Stencil.dispose();
                        }
                        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                        GL11.glPopMatrix();
                        GL11.glPushMatrix();
                        GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                    }
                }
                break block149;
            }
            if (this.target == null) {
                this.easingHealth = 0.0f;
                this.gotDamaged = false;
                this.particleList.clear();
            }
        }
        if (((Boolean)this.tSlideAnim.get()).booleanValue() && !StringsKt.equals((String)this.styleValue.get(), "rise", true) && !StringsKt.equals((String)this.styleValue.get(), "chill", true)) {
            GL11.glPopMatrix();
        }
        GlStateManager.func_179117_G();
        this.lastTarget = (Entity)this.target;
        return this.getTBorder();
    }

    @Override
    public void handleDamage(@NotNull EntityPlayer ent) {
        Intrinsics.checkNotNullParameter(ent, "ent");
        if (this.target != null && Intrinsics.areEqual(ent, this.target)) {
            this.gotDamaged = true;
        }
    }

    private final Border getTBorder() {
        Border border;
        switch ((String)this.styleValue.get()) {
            case "LiquidBounce": {
                border = new Border(0.0f, 0.0f, 90.0f, 36.0f);
                break;
            }
            case "Flux": {
                border = new Border(0.0f, -1.0f, 90.0f, 47.0f);
                break;
            }
            case "Novoline": {
                border = new Border(-1.0f, -2.0f, 90.0f, 38.0f);
                break;
            }
            case "Slowly": {
                border = new Border(0.0f, 0.0f, 90.0f, 36.0f);
                break;
            }
            case "Rise": {
                border = new Border(0.0f, 0.0f, 90.0f, 55.0f);
                break;
            }
            case "Exhibition": {
                border = new Border(0.0f, 3.0f, 140.0f, 48.0f);
                break;
            }
            case "Chill": {
                border = new Border(0.0f, 0.0f, 110.0f, 46.0f);
                break;
            }
            default: {
                border = new Border(0.0f, 0.0f, 120.0f, 36.0f);
            }
        }
        return border;
    }

    private final void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        MinecraftInstance.mc.func_110434_K().func_110577_a(skin);
        Gui.func_152125_a((int)2, (int)2, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
    }

    private final void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        MinecraftInstance.mc.func_110434_K().func_110577_a(skin);
        Gui.func_152125_a((int)x, (int)y, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
    }

    private final void drawHead(ResourceLocation skin, int x, int y, int width, int height, float alpha2) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha2);
        MinecraftInstance.mc.func_110434_K().func_110577_a(skin);
        Gui.func_152125_a((int)x, (int)y, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
    }

    private final void drawHead(ResourceLocation skin, float x, float y, float scale, int width, int height, float red2, float green2, float blue2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        GL11.glColor4f((float)RangesKt.coerceIn(red2, 0.0f, 1.0f), (float)RangesKt.coerceIn(green2, 0.0f, 1.0f), (float)RangesKt.coerceIn(blue2, 0.0f, 1.0f), (float)1.0f);
        MinecraftInstance.mc.func_110434_K().func_110577_a(skin);
        Gui.func_152125_a((int)0, (int)0, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private final void drawArmorIcon(int x, int y, int width, int height) {
        GlStateManager.func_179118_c();
        RenderUtils.drawImage(this.shieldIcon, x, y, width, height);
        GlStateManager.func_179141_d();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ ListValue access$getStyleValue$p(Target $this) {
        return $this.styleValue;
    }

    public static final /* synthetic */ BoolValue access$getBlurValue$p(Target $this) {
        return $this.blurValue;
    }

    public static final /* synthetic */ BoolValue access$getRiseParticle$p(Target $this) {
        return $this.riseParticle;
    }

    public static final /* synthetic */ FloatValue access$getMaxParticleSize$p(Target $this) {
        return $this.maxParticleSize;
    }

    public static final /* synthetic */ FloatValue access$getMinParticleSize$p(Target $this) {
        return $this.minParticleSize;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0011\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bJ.\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u0005R\u001a\u0010\t\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000b\"\u0004\b\u0015\u0010\rR\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u000b\"\u0004\b\u001d\u0010\r\u00a8\u0006&"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Target$Particle;", "", "color", "Ljava/awt/Color;", "distX", "", "distY", "radius", "(Ljava/awt/Color;FFF)V", "alpha", "getAlpha", "()F", "setAlpha", "(F)V", "getColor", "()Ljava/awt/Color;", "setColor", "(Ljava/awt/Color;)V", "getDistX", "setDistX", "getDistY", "setDistY", "progress", "", "getProgress", "()D", "setProgress", "(D)V", "getRadius", "setRadius", "render", "", "x", "y", "fade", "", "speed", "fadeSpeed", "LiquidBounce"})
    private static final class Particle {
        @NotNull
        private Color color;
        private float distX;
        private float distY;
        private float radius;
        private float alpha;
        private double progress;

        public Particle(@NotNull Color color, float distX, float distY, float radius) {
            Intrinsics.checkNotNullParameter(color, "color");
            this.color = color;
            this.distX = distX;
            this.distY = distY;
            this.radius = radius;
            this.alpha = 1.0f;
        }

        @NotNull
        public final Color getColor() {
            return this.color;
        }

        public final void setColor(@NotNull Color color) {
            Intrinsics.checkNotNullParameter(color, "<set-?>");
            this.color = color;
        }

        public final float getDistX() {
            return this.distX;
        }

        public final void setDistX(float f) {
            this.distX = f;
        }

        public final float getDistY() {
            return this.distY;
        }

        public final void setDistY(float f) {
            this.distY = f;
        }

        public final float getRadius() {
            return this.radius;
        }

        public final void setRadius(float f) {
            this.radius = f;
        }

        public final float getAlpha() {
            return this.alpha;
        }

        public final void setAlpha(float f) {
            this.alpha = f;
        }

        public final double getProgress() {
            return this.progress;
        }

        public final void setProgress(double d) {
            this.progress = d;
        }

        public final void render(float x, float y, boolean fade, float speed, float fadeSpeed) {
            if (this.progress >= 1.0) {
                this.progress = 1.0;
                if (fade) {
                    this.alpha -= fadeSpeed * 0.02f * (float)RenderUtils.deltaTime;
                }
                if (this.alpha < 0.0f) {
                    this.alpha = 0.0f;
                }
            } else {
                this.progress += (double)(speed * 0.025f * (float)RenderUtils.deltaTime);
            }
            if (this.alpha <= 0.0f) {
                return;
            }
            Color reColored = new Color((float)this.color.getRed() / 255.0f, (float)this.color.getGreen() / 255.0f, (float)this.color.getBlue() / 255.0f, this.alpha);
            float easeOut = (float)EaseUtils.easeOutQuart(this.progress);
            RenderUtils.drawFilledCircle(x + this.distX * easeOut, y + this.distY * easeOut, this.radius, reColored);
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010\b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JV\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020!R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\""}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Target$CharRenderer;", "", "small", "", "(Z)V", "deFormat", "Ljava/text/DecimalFormat;", "moveX", "", "getMoveX", "()[F", "setMoveX", "([F)V", "moveY", "getMoveY", "setMoveY", "numberList", "", "", "getSmall", "()Z", "renderChar", "", "number", "orgX", "orgY", "initX", "initY", "scaleX", "scaleY", "shadow", "fontSpeed", "color", "", "LiquidBounce"})
    private static final class CharRenderer {
        private final boolean small;
        @NotNull
        private float[] moveY;
        @NotNull
        private float[] moveX;
        @NotNull
        private final List<String> numberList;
        @NotNull
        private final DecimalFormat deFormat;

        public CharRenderer(boolean small) {
            this.small = small;
            this.moveY = new float[20];
            this.moveX = new float[20];
            String[] stringArray = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};
            this.numberList = CollectionsKt.listOf(stringArray);
            this.deFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
            int n = 0;
            while (n < 20) {
                int i = n++;
                this.moveX[i] = 0.0f;
                this.moveY[i] = 0.0f;
            }
        }

        public final boolean getSmall() {
            return this.small;
        }

        @NotNull
        public final float[] getMoveY() {
            return this.moveY;
        }

        public final void setMoveY(@NotNull float[] fArray) {
            Intrinsics.checkNotNullParameter(fArray, "<set-?>");
            this.moveY = fArray;
        }

        @NotNull
        public final float[] getMoveX() {
            return this.moveX;
        }

        public final void setMoveX(@NotNull float[] fArray) {
            Intrinsics.checkNotNullParameter(fArray, "<set-?>");
            this.moveX = fArray;
        }

        /*
         * WARNING - void declaration
         */
        public final float renderChar(float number, float orgX, float orgY, float initX, float initY, float scaleX, float scaleY, boolean shadow, float fontSpeed, int color) {
            String reFormat = this.deFormat.format(number);
            GameFontRenderer fontRend = this.small ? Fonts.font40 : Fonts.font72;
            int delta = RenderUtils.deltaTime;
            ScaledResolution scaledRes = new ScaledResolution(Target.access$getMc$p$s1046033730());
            int indexX = 0;
            int indexY = 0;
            float animX = 0.0f;
            float cutY = initY + (float)fontRend.field_78288_b * 0.75f;
            GL11.glEnable((int)3089);
            RenderUtils.makeScissorBox(0.0f, orgY + initY - 4.0f * scaleY, scaledRes.func_78326_a(), orgY + cutY - 4.0f * scaleY);
            Intrinsics.checkNotNullExpressionValue(reFormat, "reFormat");
            char[] cArray = reFormat.toCharArray();
            Intrinsics.checkNotNullExpressionValue(cArray, "this as java.lang.String).toCharArray()");
            char[] cArray2 = cArray;
            int n = 0;
            int n2 = cArray2.length;
            while (n < n2) {
                char c = cArray2[n];
                ++n;
                this.moveX[indexX] = AnimationUtils.animate(animX, this.moveX[indexX], fontSpeed * 0.025f * (float)delta);
                animX = this.moveX[indexX];
                int pos = this.numberList.indexOf(String.valueOf(c));
                float expectAnim = ((float)fontRend.field_78288_b + 2.0f) * (float)pos;
                float expectAnimMin = ((float)fontRend.field_78288_b + 2.0f) * (float)(pos - 2);
                float expectAnimMax = ((float)fontRend.field_78288_b + 2.0f) * (float)(pos + 2);
                if (pos >= 0) {
                    this.moveY[indexY] = AnimationUtils.animate(expectAnim, this.moveY[indexY], fontSpeed * 0.02f * (float)delta);
                    GL11.glTranslatef((float)0.0f, (float)(initY - this.moveY[indexY]), (float)0.0f);
                    Iterable $this$forEachIndexed$iv = this.numberList;
                    boolean $i$f$forEachIndexed = false;
                    int index$iv = 0;
                    for (Object item$iv : $this$forEachIndexed$iv) {
                        void num;
                        int n3 = index$iv;
                        index$iv = n3 + 1;
                        if (n3 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        String string = (String)item$iv;
                        int index = n3;
                        boolean bl = false;
                        if (!(((float)fontRend.field_78288_b + 2.0f) * (float)index >= expectAnimMin) || !(((float)fontRend.field_78288_b + 2.0f) * (float)index <= expectAnimMax)) continue;
                        fontRend.func_175065_a((String)num, initX + this.getMoveX()[indexX], ((float)fontRend.field_78288_b + 2.0f) * (float)index, color, shadow);
                    }
                    GL11.glTranslatef((float)0.0f, (float)(-initY + this.moveY[indexY]), (float)0.0f);
                } else {
                    this.moveY[indexY] = 0.0f;
                    fontRend.func_175065_a(String.valueOf(c), initX + this.moveX[indexX], initY, color, shadow);
                }
                animX += (float)fontRend.func_78256_a(String.valueOf(c));
                int n4 = indexX;
                indexX = n4 + 1;
                n4 = indexY;
                indexY = n4 + 1;
            }
            GL11.glDisable((int)3089);
            return animX;
        }
    }
}

