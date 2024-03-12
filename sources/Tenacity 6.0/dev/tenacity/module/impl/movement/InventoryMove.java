// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import java.util.Arrays;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import dev.tenacity.event.impl.player.MotionEvent;
import net.minecraft.client.settings.GameSettings;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.client.settings.KeyBinding;
import java.util.List;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class InventoryMove extends Module
{
    private final ModeSetting mode;
    private final TimerUtil delayTimer;
    private boolean wasInContainer;
    private static final List<KeyBinding> keys;
    
    public InventoryMove() {
        super("InventoryMove", "Inventory Move", Category.MOVEMENT, "lets you move in your inventory");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Spoof", "Delay" });
        this.delayTimer = new TimerUtil();
        this.addSettings(this.mode);
    }
    
    public static void updateStates() {
        if (InventoryMove.mc.currentScreen != null) {
            InventoryMove.keys.forEach(k -> KeyBinding.setKeyBindState(k.getKeyCode(), GameSettings.isKeyDown(k)));
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        final boolean inContainer = InventoryMove.mc.currentScreen instanceof GuiContainer;
        if (this.wasInContainer && !inContainer) {
            this.wasInContainer = false;
            updateStates();
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Spoof":
            case "Vanilla": {
                if (inContainer) {
                    this.wasInContainer = true;
                    updateStates();
                    break;
                }
                break;
            }
            case "Delay": {
                if (e.isPre() && inContainer && this.delayTimer.hasTimeElapsed(100L)) {
                    this.wasInContainer = true;
                    updateStates();
                    this.delayTimer.reset();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent e) {
        if (this.mode.is("Spoof") && (e.getPacket() instanceof S2DPacketOpenWindow || e.getPacket() instanceof S2EPacketCloseWindow)) {
            e.cancel();
        }
    }
    
    static {
        keys = Arrays.asList(InventoryMove.mc.gameSettings.keyBindForward, InventoryMove.mc.gameSettings.keyBindBack, InventoryMove.mc.gameSettings.keyBindLeft, InventoryMove.mc.gameSettings.keyBindRight, InventoryMove.mc.gameSettings.keyBindJump);
    }
}
