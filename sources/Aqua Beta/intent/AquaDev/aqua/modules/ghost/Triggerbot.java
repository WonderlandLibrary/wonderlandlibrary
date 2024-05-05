package intent.AquaDev.aqua.modules.ghost;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.TimeUtil;
import java.util.Random;
import net.minecraft.util.MathHelper;

public class Triggerbot
extends Module {
    TimeUtil timeUtil = new TimeUtil();

    public Triggerbot() {
        super("Triggerbot", "Triggerbot", 0, Category.Ghost);
    }

    public void setup() {
        Aqua.setmgr.register(new Setting("minCPS", (Module)this, 8.0, 1.0, 20.0, true));
        Aqua.setmgr.register(new Setting("maxCPS", (Module)this, 19.0, 1.0, 20.0, true));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        float maxCPS;
        float minCPS;
        float CPS;
        if (event instanceof EventUpdate && this.timeUtil.hasReached((long)(1000.0f / (CPS = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)(minCPS = (float)Aqua.setmgr.getSetting("TriggerbotminCPS").getCurrentNumber()), (double)(maxCPS = (float)Aqua.setmgr.getSetting("TriggerbotmaxCPS").getCurrentNumber()))))) && Triggerbot.mc.objectMouseOver.entityHit != null) {
            mc.clickMouse();
            this.timeUtil.reset();
        }
    }
}
