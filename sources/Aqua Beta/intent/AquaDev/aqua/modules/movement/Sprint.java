package intent.AquaDev.aqua.modules.movement;

import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import net.minecraft.client.settings.KeyBinding;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", "Sprint", 0, Category.Movement);
        System.out.println("Sprint::init");
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        KeyBinding.setKeyBindState((int)Sprint.mc.gameSettings.keyBindSprint.getKeyCode(), (boolean)false);
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate && !Aqua.moduleManager.getModuleByName("Scaffold").isToggled() && Sprint.mc.gameSettings.keyBindForward.pressed) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
}
