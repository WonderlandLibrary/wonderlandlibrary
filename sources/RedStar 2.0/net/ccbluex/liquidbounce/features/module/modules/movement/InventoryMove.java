package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name="InvMove", description="Allows you to walk while an inventory is opened.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000P\n\n\n\b\n\n\b\n!\n\n\u0000\n\n\b\t\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\b\u000020B¢J020HJ\b0HJ020HJ020 HJ!020\"HJ\b#0HR0¢\b\n\u0000\bR\b0\t0\bX¢\n\u0000R\f02\n0@BX¢\b\n\u0000\b\rR02\n0@BX¢\b\n\u0000\bR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R08VX¢\b¨$"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/InventoryMove;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacAdditionProValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAacAdditionProValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "blinkPacketList", "", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "<set-?>", "", "invOpen", "getInvOpen", "()Z", "lastInvOpen", "getLastInvOpen", "noDetectableValue", "noMoveClicksValue", "rotateValue", "tag", "", "getTag", "()Ljava/lang/String;", "onClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickWindowEvent;", "onDisable", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onScreen", "Lnet/ccbluex/liquidbounce/event/ScreenEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "updateKeyState", "Pride"})
public final class InventoryMove
extends Module {
    private final BoolValue noDetectableValue = new BoolValue("NoDetectable", false);
    private final BoolValue rotateValue = new BoolValue("Rotate", true);
    @NotNull
    private final BoolValue aacAdditionProValue = new BoolValue("AACAdditionPro", false);
    private final BoolValue noMoveClicksValue = new BoolValue("NoMoveClicks", false);
    private final List<ICPacketPlayer> blinkPacketList;
    private boolean lastInvOpen;
    private boolean invOpen;

    @NotNull
    public final BoolValue getAacAdditionProValue() {
        return this.aacAdditionProValue;
    }

    public final boolean getLastInvOpen() {
        return this.lastInvOpen;
    }

    public final boolean getInvOpen() {
        return this.invOpen;
    }

    private final void updateKeyState() {
        if (!(MinecraftInstance.mc2.currentScreen == null || MinecraftInstance.mc2.currentScreen instanceof GuiChat || ((Boolean)this.noDetectableValue.get()).booleanValue() && MinecraftInstance.mc2.currentScreen instanceof GuiContainer)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindForward()));
            MinecraftInstance.mc.getGameSettings().getKeyBindBack().setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindBack()));
            MinecraftInstance.mc.getGameSettings().getKeyBindRight().setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindRight()));
            MinecraftInstance.mc.getGameSettings().getKeyBindLeft().setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindLeft()));
            MinecraftInstance.mc.getGameSettings().getKeyBindJump().setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindJump()));
            MinecraftInstance.mc.getGameSettings().getKeyBindSprint().setPressed(MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindSprint()));
            if (((Boolean)this.rotateValue.get()).booleanValue()) {
                if (Keyboard.isKeyDown((int)200)) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getRotationPitch() > (float)-90) {
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP2.setRotationPitch(iEntityPlayerSP2.getRotationPitch() - (float)5);
                    }
                }
                if (Keyboard.isKeyDown((int)208)) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getRotationPitch() < (float)90) {
                        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP3.setRotationPitch(iEntityPlayerSP3.getRotationPitch() + (float)5);
                    }
                }
                if (Keyboard.isKeyDown((int)203)) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.setRotationYaw(iEntityPlayerSP.getRotationYaw() - (float)5);
                }
                if (Keyboard.isKeyDown((int)205)) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.setRotationYaw(iEntityPlayerSP.getRotationYaw() + (float)5);
                }
            }
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.updateKeyState();
    }

    @EventTarget
    public final void onScreen(@NotNull ScreenEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.updateKeyState();
    }

    @EventTarget
    public final void onClick(@NotNull ClickWindowEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.noMoveClicksValue.get()).booleanValue() && MovementUtils.isMoving()) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.blinkPacketList.clear();
        this.invOpen = false;
        this.lastInvOpen = false;
    }

    @Override
    public void onDisable() {
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindForward()) || MinecraftInstance.mc.getCurrentScreen() != null) {
            MinecraftInstance.mc.getGameSettings().getKeyBindForward().setPressed(false);
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindBack()) || MinecraftInstance.mc.getCurrentScreen() != null) {
            MinecraftInstance.mc.getGameSettings().getKeyBindBack().setPressed(false);
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindRight()) || MinecraftInstance.mc.getCurrentScreen() != null) {
            MinecraftInstance.mc.getGameSettings().getKeyBindRight().setPressed(false);
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindLeft()) || MinecraftInstance.mc.getCurrentScreen() != null) {
            MinecraftInstance.mc.getGameSettings().getKeyBindLeft().setPressed(false);
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindJump()) || MinecraftInstance.mc.getCurrentScreen() != null) {
            MinecraftInstance.mc.getGameSettings().getKeyBindJump().setPressed(false);
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindSprint()) || MinecraftInstance.mc.getCurrentScreen() != null) {
            MinecraftInstance.mc.getGameSettings().getKeyBindSprint().setPressed(false);
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.aacAdditionProValue.get() != false ? "AACAdditionPro" : "Bypass";
    }

    public InventoryMove() {
        List list;
        InventoryMove inventoryMove = this;
        boolean bl = false;
        inventoryMove.blinkPacketList = list = (List)new ArrayList();
    }
}
