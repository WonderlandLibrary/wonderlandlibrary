/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.ClickWindowEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.value.BoolValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;

@Info(name="InvMove", spacedName="Gui Move", description="Allows you to walk while an inventory is opened.", category=Category.MOVEMENT, cnName="\u80cc\u5305\u884c\u8d70")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\b\u0010\u000f\u001a\u00020\fH\u0016J\u0010\u0010\u0010\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0011H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/modules/module/modules/movement/GuiMove;", "Lnet/dev/important/modules/module/Module;", "()V", "aacAdditionProValue", "Lnet/dev/important/value/BoolValue;", "getAacAdditionProValue", "()Lnet/dev/important/value/BoolValue;", "noDetectableValue", "getNoDetectableValue", "noMoveClicksValue", "getNoMoveClicksValue", "onClick", "", "event", "Lnet/dev/important/event/ClickWindowEvent;", "onDisable", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class GuiMove
extends Module {
    @NotNull
    private final BoolValue noDetectableValue = new BoolValue("Ghost", false);
    @NotNull
    private final BoolValue aacAdditionProValue = new BoolValue("AACAdditionPro", false);
    @NotNull
    private final BoolValue noMoveClicksValue = new BoolValue("NoMoveClicks", false);

    @NotNull
    public final BoolValue getNoDetectableValue() {
        return this.noDetectableValue;
    }

    @NotNull
    public final BoolValue getAacAdditionProValue() {
        return this.aacAdditionProValue;
    }

    @NotNull
    public final BoolValue getNoMoveClicksValue() {
        return this.noMoveClicksValue;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Module module2 = Client.INSTANCE.getModuleManager().getModule(Speed.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.movement.Speed");
        }
        Speed speedModule = (Speed)module2;
        if (!(MinecraftInstance.mc.field_71462_r instanceof GuiChat || MinecraftInstance.mc.field_71462_r instanceof GuiIngameMenu || ((Boolean)this.noDetectableValue.get()).booleanValue() && MinecraftInstance.mc.field_71462_r instanceof GuiContainer)) {
            MinecraftInstance.mc.field_71474_y.field_74351_w.field_74513_e = GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74351_w);
            MinecraftInstance.mc.field_71474_y.field_74368_y.field_74513_e = GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74368_y);
            MinecraftInstance.mc.field_71474_y.field_74366_z.field_74513_e = GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74366_z);
            MinecraftInstance.mc.field_71474_y.field_74370_x.field_74513_e = GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74370_x);
            MinecraftInstance.mc.field_71474_y.field_151444_V.field_74513_e = GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_151444_V);
        }
    }

    @EventTarget
    public final void onClick(@NotNull ClickWindowEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.noMoveClicksValue.get()).booleanValue() && MovementUtils.isMoving()) {
            event.cancelEvent();
        }
    }

    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74351_w) || MinecraftInstance.mc.field_71462_r != null) {
            MinecraftInstance.mc.field_71474_y.field_74351_w.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74368_y) || MinecraftInstance.mc.field_71462_r != null) {
            MinecraftInstance.mc.field_71474_y.field_74368_y.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74366_z) || MinecraftInstance.mc.field_71462_r != null) {
            MinecraftInstance.mc.field_71474_y.field_74366_z.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74370_x) || MinecraftInstance.mc.field_71462_r != null) {
            MinecraftInstance.mc.field_71474_y.field_74370_x.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74314_A) || MinecraftInstance.mc.field_71462_r != null) {
            MinecraftInstance.mc.field_71474_y.field_74314_A.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_151444_V) || MinecraftInstance.mc.field_71462_r != null) {
            MinecraftInstance.mc.field_71474_y.field_151444_V.field_74513_e = false;
        }
    }
}

