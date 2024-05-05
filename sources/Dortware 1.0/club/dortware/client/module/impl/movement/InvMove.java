package club.dortware.client.module.impl.movement;

import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.event.impl.MovementEvent;
import club.dortware.client.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author Auth
 */

@ModuleData(name = "Gui Move", description = "Makes it so you can move around while in your inventory", category = ModuleCategory.MOVEMENT, defaultKeyBind = 0)
public class InvMove extends Module {

    @Override
    public void setup() {}

    @Subscribe
    public void onMove(MovementEvent event) {
        if (mc.currentScreen instanceof GuiChat) {
            return;
        }

        if (mc.currentScreen != null) {
            KeyBinding[] keyBindings = new KeyBinding[]{
                    mc.gameSettings.keyBindForward,
                    mc.gameSettings.keyBindRight,
                    mc.gameSettings.keyBindLeft,
                    mc.gameSettings.keyBindBack,
                    mc.gameSettings.keyBindJump,
                    mc.gameSettings.keyBindSprint
            };

            for (KeyBinding keyBinding : keyBindings) {
                keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
            }
        }
    }
}
