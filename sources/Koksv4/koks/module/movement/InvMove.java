package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.DisplayGuiScreenEvent;
import koks.event.GuiHandleEvent;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import koks.module.player.NoPitchLimit;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "InvMove", description = "You can move in the inventory", category = Module.Category.MOVEMENT)
public class InvMove extends Module {

    private final TimeHelper timeHelper = new TimeHelper();

    @Value(name = "Spoof")
    boolean spoof = false;

    @Value(name = "Jump")
    boolean jump = true;

    @Value(name = "Sprint")
    boolean sprint = true;

    @Value(name = "Silent Sprint")
    boolean silentSprint = false;

    @Value(name = "Look")
    boolean look = true;

    @Value(name = "LookWithArrow")
    boolean lookWithArrow = false;

    @Value(name = "RotationSpeed", minimum = 0.5, maximum = 10)
    double rotationSpeed = 5;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Silent Sprint":
                return sprint;
            case "RotationSpeed":
                return lookWithArrow;
        }
        return super.isVisible(value, name);
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packet instanceof final C16PacketClientStatus clientStatus) {
                if (spoof) {
                    if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                        packetEvent.setCanceled(true);
                    }
                }
            }
            if(packet instanceof final C0BPacketEntityAction entityAction) {
                if(sprint && silentSprint && mc.currentScreen != null) {
                    switch (entityAction.getAction()) {
                        case STOP_SPRINTING, START_SPRINTING -> {
                            packetEvent.setCanceled(true);
                        }
                    }
                }
            }
        }
        if(event instanceof final DisplayGuiScreenEvent displayGuiScreenEvent) {
            if(displayGuiScreenEvent.getScreen() == null)
                handleMovement();
        }
        if (event instanceof GuiHandleEvent) {
            if (mc.currentScreen instanceof GuiChat || mc.currentScreen == null) {
                return;
            }
            handleMovement();

            if (lookWithArrow) {
                Keyboard.enableRepeatEvents(true);
                if (isKeyDown(Keyboard.KEY_LEFT))
                    getPlayer().rotationYaw -= rotationSpeed;
                if (isKeyDown(Keyboard.KEY_RIGHT))
                    getPlayer().rotationYaw += rotationSpeed;
                if (isKeyDown(Keyboard.KEY_UP))
                    getPlayer().rotationPitch -= rotationSpeed;
                if (isKeyDown(Keyboard.KEY_DOWN))
                    getPlayer().rotationPitch += rotationSpeed;

                if (!ModuleRegistry.getModule(NoPitchLimit.class).isToggled())
                    getPlayer().rotationPitch = MathHelper.clamp_float(getPlayer().rotationPitch, -90, 90);
            }

            if (isKeyDown(getGameSettings().keyBindPlayerList.getKeyCode()) && look) {
                if (mc.currentScreen != null) {
                    if (timeHelper.hasReached(200)) {
                        mc.inGameHasFocus = !mc.inGameHasFocus;
                        if (mc.inGameHasFocus) {
                            mc.mouseHelper.grabMouseCursor();
                        } else {
                            KeyBinding.unPressAllKeys();
                            mc.mouseHelper.ungrabMouseCursor();
                        }
                        timeHelper.reset();
                    }
                }
            }
        }
    }

    public void handleMovement() {
        getGameSettings().keyBindForward.pressed = isKeyDown(getGameSettings().keyBindForward.getKeyCode());
        getGameSettings().keyBindBack.pressed = isKeyDown(getGameSettings().keyBindBack.getKeyCode());
        getGameSettings().keyBindLeft.pressed = isKeyDown(getGameSettings().keyBindLeft.getKeyCode());
        getGameSettings().keyBindRight.pressed = isKeyDown(getGameSettings().keyBindRight.getKeyCode());
        if (jump)
            getGameSettings().keyBindJump.pressed = isKeyDown(getGameSettings().keyBindJump.getKeyCode());
        if (sprint)
            getGameSettings().keyBindSprint.pressed = isKeyDown(getGameSettings().keyBindSprint.getKeyCode());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
