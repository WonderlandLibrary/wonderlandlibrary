package me.crimp.claudius.mod.modules.movement;

import me.crimp.claudius.event.events.PacketEvent;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {

    public Setting<Boolean> items = register(new Setting<>("Items", true));
    public Setting<Boolean> inventory = register(new Setting<>("Inventory", true));
    public Setting<Integer> lookSpeed = register(new Setting<>("LookSpeed", 5, 0, 10));
    // anti-cheat
    public Setting<Bypass> bypass = register(new Setting<>("Bypass", Bypass.TwoBeTwoTee));
    public Setting<Boolean> inventoryMoveBypass = register(new Setting<>("InvMoveBypass", false));
    public static NoSlow INSTANCE;
    public static KeyBinding[] KEYS;

    static {
        KEYS = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
    }

    private boolean sneaking = false;

    public NoSlow() {
        super("NoSlow", "Allows you to do stuff without slowing down", Category.Movement,false, false);
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        return bypass.getValue().toString();
    }

    @Override
    public void onDisable() {
        if (!fullNullCheck() && sneaking) {
            sneaking = false;
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @Override
    public void onUpdate() {
        if (sneaking && !mc.player.isHandActive() && !mc.player.isElytraFlying()) {
            sneaking = false;
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (inventory.getValue() && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiScreenBook)) {
            if (Keyboard.isKeyDown(200)) mc.player.rotationPitch -= lookSpeed.getValue();

            if (Keyboard.isKeyDown(208)) mc.player.rotationPitch += lookSpeed.getValue();

            if (Keyboard.isKeyDown(205)) mc.player.rotationYaw += lookSpeed.getValue();

            if (Keyboard.isKeyDown(203)) mc.player.rotationYaw -= lookSpeed.getValue();

            if (mc.player.rotationPitch > 90) mc.player.rotationPitch = 90;

            if (mc.player.rotationPitch < -90) mc.player.rotationPitch = -90;

            for (final KeyBinding keyBinding : KEYS) {
                if (Keyboard.isKeyDown(keyBinding.getKeyCode())) {
                    if (keyBinding.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
                        keyBinding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
                    }
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), true);
                } else {
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityItemUse(LivingEntityUseItemEvent event) {
        if (mc.player.isElytraFlying() || fullNullCheck()) return;
        if (event.getEntity() == mc.player && items.getValue()) {
            if (bypass.getValue() == Bypass.Sneak) {
                if (!sneaking) {
                    sneaking = true;
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
            } else if (bypass.getValue() == Bypass.TwoBeTwoTee && !mc.player.isElytraFlying()) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem)); // lmao nice
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (fullNullCheck()) return;
        if (event.getPacket() instanceof CPacketClickWindow) {
            if (inventoryMoveBypass.getValue()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING)); // nice
            }
        } else if (event.getPacket() instanceof CPacketPlayer) {
            if (bypass.getValue() == Bypass.NCP) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, mc.player.getPosition(), EnumFacing.DOWN));
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }

    public enum Bypass {
        /**
         * Bypasses old NCP versions or shitty ass anti-cheats
         */
        NCP,

        /**
         * Old, sneak method. Only works while strafing.
         */
        Sneak,

        /**
         * The infamous FencingFPlus2+2 NoSlow bypass
         * Skidded into Konas, RusherHack, Future, claudius, and every paid client under the sun
         */
        TwoBeTwoTee
    }
}
