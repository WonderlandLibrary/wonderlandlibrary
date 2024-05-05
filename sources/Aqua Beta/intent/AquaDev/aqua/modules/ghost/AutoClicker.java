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
import org.lwjgl.input.Mouse;

public class AutoClicker
extends Module {
    TimeUtil timeUtil = new TimeUtil();
    public boolean attack;

    public AutoClicker() {
        super("AutoClicker", "AutoClicker", 0, Category.Ghost);
        System.out.println("AutoClicker::init");
    }

    public void setup() {
        Aqua.setmgr.register(new Setting("OnlyLeftClick", (Module)this, true));
        Aqua.setmgr.register(new Setting("1.9", (Module)this, false));
        Aqua.setmgr.register(new Setting("minCPS", (Module)this, 8.0, 1.0, 20.0, true));
        Aqua.setmgr.register(new Setting("maxCPS", (Module)this, 19.0, 1.0, 20.0, true));
    }

    public void onEnable() {
        this.attack = false;
        super.onEnable();
    }

    public void onDisable() {
        this.attack = false;
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            float minCPS = (float)Aqua.setmgr.getSetting("AutoClickerminCPS").getCurrentNumber();
            float maxCPS = (float)Aqua.setmgr.getSetting("AutoClickermaxCPS").getCurrentNumber();
            float CPS = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)minCPS, (double)maxCPS);
            AutoClicker.mc.gameSettings.keyBindAttack.pressed = false;
            this.attack = Mouse.isButtonDown((int)0);
            if (!Aqua.setmgr.getSetting("AutoClicker1.9").isState()) {
                if (this.timeUtil.hasReached((long)(1000.0f / CPS))) {
                    if (Aqua.setmgr.getSetting("AutoClickerOnlyLeftClick").isState()) {
                        if (this.attack) {
                            mc.clickMouse();
                        }
                    } else {
                        mc.clickMouse();
                    }
                    this.timeUtil.reset();
                }
            } else if (Aqua.setmgr.getSetting("AutoClicker1.9").isState()) {
                if (Aqua.setmgr.getSetting("AutoClickerOnlyLeftClick").isState()) {
                    if (this.attack && AutoClicker.mc.thePlayer.ticksExisted % 11 == 0) {
                        mc.clickMouse();
                    }
                } else if (AutoClicker.mc.thePlayer.ticksExisted % 11 == 0) {
                    mc.clickMouse();
                }
            }
        }
    }
}
