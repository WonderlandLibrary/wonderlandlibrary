package net.ccbluex.liquidbounce.features.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.utils.render.VisualUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IEntityRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.AnimationUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HUD", description="Toggles visibility of the HUD.", category=ModuleCategory.RENDER, array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000b\n\n\n\b\n\n\b\n\n\b\n\n\b\n\n\n\b\n\n\b\n\n\b\t\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\b\u0000 :20::B¢J,0!2-0!J.0/2001HJ20/2\b003HJ40/2005HJ60/2007HJ80/2\b009HR0¢\b\n\u0000\bR0¢\b\n\u0000\b\bR\t0\n¢\b\n\u0000\b\fR\r0¢\b\n\u0000\bR0¢\b\n\u0000\bR0\n¢\b\n\u0000\b\fR0X¢\n\u0000R0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0\n¢\b\n\u0000\b\fR 0!X¢\n\u0000R\"0¢\b\n\u0000\b#R$0%¢\b\n\u0000\b&'R(0\n¢\b\n\u0000\b)\fR*0X¢\n\u0000R+0%X¢\n\u0000¨;"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/HUD;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "ClientName", "Lnet/ccbluex/liquidbounce/value/TextValue;", "getClientName", "()Lnet/ccbluex/liquidbounce/value/TextValue;", "DevName", "getDevName", "Radius", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getRadius", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "animHotbarValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAnimHotbarValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "blackHotbarValue", "getBlackHotbarValue", "blueValue", "getBlueValue", "blurValue", "containerBackground", "getContainerBackground", "customColor", "Ljava/awt/Color;", "getCustomColor", "()Ljava/awt/Color;", "fontChatValue", "getFontChatValue", "greenValue", "getGreenValue", "hotBarX", "", "inventoryParticle", "getInventoryParticle", "logValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getLogValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "redValue", "getRedValue", "toggleMessageValue", "toggleSoundValue", "getAnimPos", "pos", "onKey", "", "event", "Lnet/ccbluex/liquidbounce/event/KeyEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onScreen", "Lnet/ccbluex/liquidbounce/event/ScreenEvent;", "onTick", "Lnet/ccbluex/liquidbounce/event/TickEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Companion", "Pride"})
public final class HUD
extends Module {
    private final BoolValue toggleMessageValue = new BoolValue("DisplayToggleMessage", true);
    private final ListValue toggleSoundValue = new ListValue("ToggleSound", new String[]{"None", "Default", "Custom"}, "Custom");
    @NotNull
    private final BoolValue containerBackground = new BoolValue("Container-Background", false);
    @NotNull
    private final BoolValue animHotbarValue = new BoolValue("AnimatedHotbar", true);
    @NotNull
    private final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    @NotNull
    private final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    private final BoolValue blurValue = new BoolValue("Blur", false);
    @NotNull
    private final IntegerValue Radius = new IntegerValue("BlurRadius", 10, 1, 50);
    @NotNull
    private final BoolValue fontChatValue = new BoolValue("FontChat", false);
    @NotNull
    private final ListValue logValue = new ListValue("LogMode", new String[]{"idk", "141Sense", "Jello", "PowerX", "None", "Novoline"}, "None");
    @NotNull
    private final TextValue ClientName = new TextValue("ClientName", "RedStar");
    @NotNull
    private final TextValue DevName = new TextValue("DevName", "YiTong");
    @NotNull
    private final IntegerValue redValue = new IntegerValue("NovolineRed", 255, 0, 255);
    @NotNull
    private final IntegerValue greenValue = new IntegerValue("NovolineGreen", 255, 0, 255);
    @NotNull
    private final IntegerValue blueValue = new IntegerValue("NovolineBlue", 255, 0, 255);
    @NotNull
    private final Color customColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue());
    private float hotBarX;
    @JvmField
    @NotNull
    public static final BoolValue Hotbarblur;
    @NotNull
    private static final ListValue shadowValue;
    public static final Companion Companion;

    @NotNull
    public final BoolValue getContainerBackground() {
        return this.containerBackground;
    }

    @NotNull
    public final BoolValue getAnimHotbarValue() {
        return this.animHotbarValue;
    }

    @NotNull
    public final BoolValue getBlackHotbarValue() {
        return this.blackHotbarValue;
    }

    @NotNull
    public final BoolValue getInventoryParticle() {
        return this.inventoryParticle;
    }

    @NotNull
    public final IntegerValue getRadius() {
        return this.Radius;
    }

    @NotNull
    public final BoolValue getFontChatValue() {
        return this.fontChatValue;
    }

    @NotNull
    public final ListValue getLogValue() {
        return this.logValue;
    }

    @NotNull
    public final TextValue getClientName() {
        return this.ClientName;
    }

    @NotNull
    public final TextValue getDevName() {
        return this.DevName;
    }

    @NotNull
    public final IntegerValue getRedValue() {
        return this.redValue;
    }

    @NotNull
    public final IntegerValue getGreenValue() {
        return this.greenValue;
    }

    @NotNull
    public final IntegerValue getBlueValue() {
        return this.blueValue;
    }

    @NotNull
    public final Color getCustomColor() {
        return this.customColor;
    }

    @EventTarget
    public final void onRender2D(@Nullable Render2DEvent event) {
        LiquidBounce.INSTANCE.getHud().render(false);
        String string = (String)this.logValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "novoline": {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Calendar calendar = Calendar.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(calendar, "Calendar.getInstance()");
                String time = simpleDateFormat.format(calendar.getTime());
                Fonts.font35.drawString((String)this.ClientName.get() + ChatFormatting.GRAY + " (" + ChatFormatting.WHITE + time + ChatFormatting.GRAY + ")", 3.0f, 4.0f, this.customColor.getRGB(), true);
                break;
            }
            case "idk": {
                Fonts.fontSFUI35.drawStringWithShadow((String)this.ClientName.get() + "#0810", 7, 10, new Color(255, 255, 255, 245).getRGB());
                break;
            }
            case "powerx": {
                Gui.drawRect((int)2, (int)1, (int)78, (int)18, (int)new Color(10, 10, 10, 180).getRGB());
                Gui.drawRect((int)2, (int)1, (int)4, (int)18, (int)new Color(240, 240, 240, 245).getRGB());
                Fonts.fontSFUI35.drawStringWithShadow((String)this.ClientName.get(), 7, 3, new Color(255, 255, 255, 245).getRGB());
                break;
            }
            case "jello": {
                Fonts.fontSFUI120.drawString((String)this.ClientName.get(), 10.0f, 10.0f, VisualUtils.reAlpha(Colors.WHITE.c, 0.75f));
                break;
            }
            case "141sense": {
                RenderUtils.drawRect(3.0f, 3.0f, 142.0f, 63.0f, new Color(255, 255, 255, 120).getRGB());
                Fonts.font80.drawCenteredString((String)this.ClientName.get(), 71.0f, 7.0f, new Color(0, 0, 0, 180).getRGB());
                Fonts.font35.drawCenteredString("by" + (String)this.DevName.get(), 71.0f, 25.0f, new Color(0, 0, 0, 180).getRGB());
                Fonts.font80.drawString("_______________", 6.0f, 19.0f, new Color(0, 0, 0, 180).getRGB());
                StringBuilder stringBuilder = new StringBuilder().append("UserName:");
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                String string4 = stringBuilder.append(iEntityPlayerSP.getName()).toString();
                StringBuilder stringBuilder2 = new StringBuilder().append("UserName:");
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                Fonts.font35.drawString(string4, 45.0f - (float)Fonts.font35.getStringWidth(stringBuilder2.append(iEntityPlayerSP2.getName()).toString()) / 2.0f + 28.0f, 40.0f, new Color(0, 0, 0, 180).getRGB());
                Fonts.font35.drawString("FPS:" + MinecraftInstance.mc.getDebugFPS(), 45.0f - (float)Fonts.font35.getStringWidth("FPS:" + MinecraftInstance.mc.getDebugFPS()) / 2.0f + 28.0f, 52.0f, new Color(0, 0, 0, 180).getRGB());
                break;
            }
        }
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        LiquidBounce.INSTANCE.getHud().render(false);
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        LiquidBounce.INSTANCE.getHud().update();
    }

    @EventTarget(ignoreCondition=true)
    public final void onTick(@NotNull TickEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        LiquidBounce.INSTANCE.getModuleManager().setShouldNotify((Boolean)this.toggleMessageValue.get());
        LiquidBounce.INSTANCE.getModuleManager().setToggleSoundMode(ArraysKt.indexOf(this.toggleSoundValue.getValues(), this.toggleSoundValue.get()));
    }

    @EventTarget
    public final void onKey(@NotNull KeyEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        LiquidBounce.INSTANCE.getHud().handleKey('a', event.getKey());
    }

    public final float getAnimPos(float pos) {
        this.hotBarX = this.getState() && (Boolean)this.animHotbarValue.get() != false ? AnimationUtils.animate(pos, this.hotBarX, 0.02f * (float)RenderUtils.deltaTime) : pos;
        return this.hotBarX;
    }

    @EventTarget(ignoreCondition=true)
    public final void onScreen(@NotNull ScreenEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MinecraftInstance.mc.getTheWorld() == null || MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (this.getState() && ((Boolean)this.blurValue.get()).booleanValue() && !MinecraftInstance.mc.getEntityRenderer().isShaderActive() && event.getGuiScreen() != null && !MinecraftInstance.classProvider.isGuiChat(event.getGuiScreen()) && !MinecraftInstance.classProvider.isGuiHudDesigner(event.getGuiScreen())) {
            String string = "Pride";
            StringBuilder stringBuilder = new StringBuilder();
            IClassProvider iClassProvider = MinecraftInstance.classProvider;
            IEntityRenderer iEntityRenderer = MinecraftInstance.mc.getEntityRenderer();
            boolean bl = false;
            String string2 = string.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
            String string3 = string2;
            iEntityRenderer.loadShader(iClassProvider.createResourceLocation(stringBuilder.append(string3).append("/blur.json").toString()));
        } else if (MinecraftInstance.mc.getEntityRenderer().getShaderGroup() != null) {
            IShaderGroup iShaderGroup = MinecraftInstance.mc.getEntityRenderer().getShaderGroup();
            if (iShaderGroup == null) {
                Intrinsics.throwNpe();
            }
            if (StringsKt.contains$default((CharSequence)iShaderGroup.getShaderGroupName(), "pride/blur.json", false, 2, null)) {
                MinecraftInstance.mc.getEntityRenderer().stopUseShader();
            }
        }
    }

    public HUD() {
        this.setState(true);
        this.setState(true);
    }

    static {
        Companion = new Companion(null);
        Hotbarblur = new BoolValue("BlurGuiButton", false);
        shadowValue = new ListValue("ShadowMode", new String[]{"Test", "LiquidBounce", "Outline", "Default", "Autumn"}, "LiquidBounce");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\u0000\n\n\b\b\u000020B\b¢R08X¢\n\u0000R0¢\b\n\u0000\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/HUD$Companion;", "", "()V", "Hotbarblur", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "shadowValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getShadowValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "Pride"})
    public static final class Companion {
        @NotNull
        public final ListValue getShadowValue() {
            return shadowValue;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
