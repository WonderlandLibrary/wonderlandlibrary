package vestige.module.impl.movement;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.Priority;
import vestige.event.impl.EntityActionEvent;
import vestige.event.impl.TickEvent;
import vestige.event.impl.UpdateEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.ModeSetting;

public class InventoryMove extends Module {

    private final ModeSetting noSprint = new ModeSetting("No sprint", "Disabled", "Disabled", "Enabled", "Spoof");
    private final BooleanSetting blink = new BooleanSetting("Blink", false);

    private boolean hadInventoryOpened;

    private boolean blinking;

    public InventoryMove() {
        super("Inventory Move", Category.MOVEMENT);
        this.addSettings(noSprint, blink);
    }

    @Override
    public void onDisable() {
        if(blinking) {
            Vestige.instance.getPacketBlinkHandler().stopAll();
            blinking = false;
        }
    }

    @Listener(Priority.LOW)
    public void onTick(TickEvent event) {
        if(isInventoryOpened()) {
            allowMove();

            if(noSprint.is("Enabled")) {
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
            }

            if(blink.isEnabled()) {
                Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                blinking = true;
            }
        } else {
            if(blinking) {
                Vestige.instance.getPacketBlinkHandler().stopAll();
                blinking = false;
            }

            if(hadInventoryOpened) {
                allowMove();
                hadInventoryOpened = false;
            }
        }
    }

    @Listener(Priority.LOW)
    public void onUpdate(UpdateEvent event) {
        if(isInventoryOpened()) {
            allowMove();

            if(noSprint.is("Enabled")) {
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
            }
        }
    }

    @Listener(Priority.LOW)
    public void onEntityAction(EntityActionEvent event) {
        if(isInventoryOpened()) {
            allowMove();

            if(noSprint.is("Spoof")) {
                event.setSprinting(false);
            }
        }
    }

    private boolean isInventoryOpened() {
        return mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest;
    }

    private void allowMove() {
        GameSettings settings = mc.gameSettings;
        KeyBinding keys[] = {settings.keyBindForward, settings.keyBindBack, settings.keyBindLeft, settings.keyBindRight, settings.keyBindJump};

        for(KeyBinding key : keys) {
            key.pressed = Keyboard.isKeyDown(key.getKeyCode());
        }

        hadInventoryOpened = true;
    }

}
