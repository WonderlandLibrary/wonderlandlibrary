package me.dev.legacy.features.modules.movement;

import me.dev.legacy.event.events.KeyEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module
{
    public Setting<Boolean> guiMove;
    public Setting<Boolean> noSlow;
    public Setting<Boolean> soulSand;
    private static NoSlow INSTANCE;
    private static KeyBinding[] keys;

    public NoSlow() {
        super("NoSlow", "Prevents you from getting slowed down.", Category.MOVEMENT, true, false, false);
        this.guiMove = (Setting<Boolean>)this.register(new Setting("GuiMove", true));
        this.noSlow = (Setting<Boolean>)this.register(new Setting("NoSlow", true));
        this.soulSand = (Setting<Boolean>)this.register(new Setting("SoulSand", true));
        this.setInstance();
    }

    private void setInstance() {
        NoSlow.INSTANCE = this;
    }

    public static NoSlow getInstance() {
        if (NoSlow.INSTANCE == null) {
            NoSlow.INSTANCE = new NoSlow();
        }
        return NoSlow.INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.guiMove.getValue()) {
            if (NoSlow.mc.currentScreen instanceof GuiOptions || NoSlow.mc.currentScreen instanceof GuiVideoSettings || NoSlow.mc.currentScreen instanceof GuiScreenOptionsSounds || NoSlow.mc.currentScreen instanceof GuiContainer || NoSlow.mc.currentScreen instanceof GuiIngameMenu) {
                for (final KeyBinding bind : NoSlow.keys) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
                }
            }
            else if (NoSlow.mc.currentScreen == null) {
                for (final KeyBinding bind : NoSlow.keys) {
                    if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                        KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onInput(final InputUpdateEvent event) {
        if (this.noSlow.getValue() && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onKeyEvent(final KeyEvent event) {
        if (this.guiMove.getValue() && event.getStage() == 0 && !(NoSlow.mc.currentScreen instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }

    static {
        NoSlow.INSTANCE = new NoSlow();
        NoSlow.keys = new KeyBinding[] { NoSlow.mc.gameSettings.keyBindForward, NoSlow.mc.gameSettings.keyBindBack, NoSlow.mc.gameSettings.keyBindLeft, NoSlow.mc.gameSettings.keyBindRight, NoSlow.mc.gameSettings.keyBindJump, NoSlow.mc.gameSettings.keyBindSprint };
    }
}

