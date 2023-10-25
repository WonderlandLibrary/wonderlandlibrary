package intent.AquaDev.aqua.modules.movement;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.utils.PlayerUtil;

public class Step
extends Module {
    public Step() {
        super("Step", "Step", 0, Category.Movement);
        Aqua.setmgr.register(new Setting("Boost", (Module)this, 0.2, 0.1, 0.85, false));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            if (Step.mc.gameSettings.keyBindLeft.pressed || Step.mc.gameSettings.keyBindRight.pressed || Step.mc.gameSettings.keyBindBack.pressed || Killaura.target != null) {
                return;
            }
            if (Step.mc.thePlayer.onGround && Step.mc.thePlayer.isCollidedHorizontally) {
                Step.mc.thePlayer.motionY = 0.37f;
            }
            if (Step.mc.thePlayer.isCollidedHorizontally) {
                PlayerUtil.setSpeed((double)(PlayerUtil.getSpeed() + Aqua.setmgr.getSetting("StepBoost").getCurrentNumber()));
            }
        }
    }
}
