/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.KeyEvent;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.AnimationUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="Hud", description="Toggles visibility of the HUD.", category=Category.RENDER, cnName="\u7528\u6237\u754c\u9762")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\u001cJ\u0010\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u0007J\u0010\u0010/\u001a\u00020,2\u0006\u0010-\u001a\u000200H\u0007J\u0010\u00101\u001a\u00020,2\u0006\u0010-\u001a\u000202H\u0007J\u0012\u00103\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u000104H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0011\u0010\u0015\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u001d\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0006R\u0011\u0010\u001f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0006R\u000e\u0010!\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\"\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0006R\u000e\u0010$\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2={"Lnet/dev/important/modules/module/modules/render/HUD;", "Lnet/dev/important/modules/module/Module;", "()V", "animHotbarValue", "Lnet/dev/important/value/BoolValue;", "getAnimHotbarValue", "()Lnet/dev/important/value/BoolValue;", "blackHotbarValue", "getBlackHotbarValue", "chatAnimationSpeedValue", "Lnet/dev/important/value/FloatValue;", "getChatAnimationSpeedValue", "()Lnet/dev/important/value/FloatValue;", "chatAnimationValue", "getChatAnimationValue", "chatCombineValue", "getChatCombineValue", "chatRectValue", "getChatRectValue", "containerBackground", "getContainerBackground", "fontChatValue", "getFontChatValue", "fontType", "Lnet/dev/important/value/FontValue;", "getFontType", "()Lnet/dev/important/value/FontValue;", "hotBarX", "", "invEffectOffset", "getInvEffectOffset", "inventoryParticle", "getInventoryParticle", "nobobValue", "tabHead", "getTabHead", "toggleMessageValue", "toggleSoundValue", "Lnet/dev/important/value/ListValue;", "toggleVolumeValue", "Lnet/dev/important/value/IntegerValue;", "getAnimPos", "pos", "onKey", "", "event", "Lnet/dev/important/event/KeyEvent;", "onRender2D", "Lnet/dev/important/event/Render2DEvent;", "onTick", "Lnet/dev/important/event/TickEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class HUD
extends Module {
    @NotNull
    private final BoolValue tabHead = new BoolValue("Tab-HeadOverlay", true);
    @NotNull
    private final BoolValue animHotbarValue = new BoolValue("AnimatedHotbar", true);
    @NotNull
    private final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    @NotNull
    private final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    @NotNull
    private final BoolValue fontChatValue = new BoolValue("FontChat", false);
    @NotNull
    private final FontValue fontType;
    @NotNull
    private final BoolValue chatRectValue;
    @NotNull
    private final BoolValue chatCombineValue;
    @NotNull
    private final BoolValue chatAnimationValue;
    @NotNull
    private final FloatValue chatAnimationSpeedValue;
    @NotNull
    private final BoolValue toggleMessageValue;
    @NotNull
    private final ListValue toggleSoundValue;
    @NotNull
    private final IntegerValue toggleVolumeValue;
    @NotNull
    private final BoolValue containerBackground;
    @NotNull
    private final BoolValue invEffectOffset;
    @NotNull
    private final BoolValue nobobValue;
    private float hotBarX;

    public HUD() {
        String[] stringArray = Fonts.font40;
        Intrinsics.checkNotNullExpressionValue(stringArray, "font40");
        this.fontType = new FontValue("Font", (FontRenderer)stringArray, new Function0<Boolean>(this){
            final /* synthetic */ HUD this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getFontChatValue().get();
            }
        });
        this.chatRectValue = new BoolValue("ChatRect", true);
        this.chatCombineValue = new BoolValue("ChatCombine", true);
        this.chatAnimationValue = new BoolValue("ChatAnimation", true);
        this.chatAnimationSpeedValue = new FloatValue("Chat-AnimationSpeed", 0.1f, 0.01f, 0.1f);
        this.toggleMessageValue = new BoolValue("DisplayToggleMessage", true);
        stringArray = new String[]{"None", "Default", "Custom"};
        this.toggleSoundValue = new ListValue("ToggleSound", stringArray, "Default");
        this.toggleVolumeValue = new IntegerValue("ToggleVolume", 100, 0, 100, new Function0<Boolean>(this){
            final /* synthetic */ HUD this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)HUD.access$getToggleSoundValue$p(this.this$0).get(), "custom", true);
            }
        });
        this.containerBackground = new BoolValue("Container-Background", false);
        this.invEffectOffset = new BoolValue("InvEffect-Offset", false);
        this.nobobValue = new BoolValue("NoBob", false);
        this.setState(true);
    }

    @NotNull
    public final BoolValue getTabHead() {
        return this.tabHead;
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
    public final BoolValue getFontChatValue() {
        return this.fontChatValue;
    }

    @NotNull
    public final FontValue getFontType() {
        return this.fontType;
    }

    @NotNull
    public final BoolValue getChatRectValue() {
        return this.chatRectValue;
    }

    @NotNull
    public final BoolValue getChatCombineValue() {
        return this.chatCombineValue;
    }

    @NotNull
    public final BoolValue getChatAnimationValue() {
        return this.chatAnimationValue;
    }

    @NotNull
    public final FloatValue getChatAnimationSpeedValue() {
        return this.chatAnimationSpeedValue;
    }

    @NotNull
    public final BoolValue getContainerBackground() {
        return this.containerBackground;
    }

    @NotNull
    public final BoolValue getInvEffectOffset() {
        return this.invEffectOffset;
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner) {
            return;
        }
        Client.INSTANCE.getHud().render(false);
    }

    @EventTarget(ignoreCondition=true)
    public final void onTick(@NotNull TickEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Client.INSTANCE.getModuleManager().setShouldNotify((Boolean)this.toggleMessageValue.get());
        Client.INSTANCE.getModuleManager().setToggleSoundMode(ArraysKt.indexOf(this.toggleSoundValue.getValues(), this.toggleSoundValue.get()));
        Client.INSTANCE.getModuleManager().setToggleVolume(((Number)this.toggleVolumeValue.get()).intValue());
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        Client.INSTANCE.getHud().update();
        if (((Boolean)this.nobobValue.get()).booleanValue()) {
            MinecraftInstance.mc.field_71439_g.field_70140_Q = 0.0f;
        }
    }

    @EventTarget
    public final void onKey(@NotNull KeyEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Client.INSTANCE.getHud().handleKey('a', event.getKey());
    }

    public final float getAnimPos(float pos) {
        this.hotBarX = this.getState() && (Boolean)this.animHotbarValue.get() != false ? AnimationUtils.animate(pos, this.hotBarX, 0.02f * (float)RenderUtils.deltaTime) : pos;
        return this.hotBarX;
    }

    public static final /* synthetic */ ListValue access$getToggleSoundValue$p(HUD $this) {
        return $this.toggleSoundValue;
    }
}

