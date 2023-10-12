package wtf.diablo.module.impl.Movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;

import java.util.Arrays;

public class InventoryMove extends Module {
    public InventoryMove(){
        super("Inventory Move","Move in GUIs", Category.MOVEMENT, ServerType.All);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if (mc.currentScreen != null && e.getType() == EventType.Pre && !(mc.currentScreen instanceof GuiChat)) {
            MovementInput.moveForward = 1;
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) mc.thePlayer.rotationPitch += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) mc.thePlayer.rotationPitch -= 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) mc.thePlayer.rotationYaw += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) mc.thePlayer.rotationYaw -= 2f;
            KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,mc.gameSettings.keyBindJump};
            Arrays.stream(keys).forEach(key -> KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode())));
        }
    }
}
