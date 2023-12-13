/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.ghost;

import java.awt.AWTException;
import java.awt.Robot;
import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.NumberSetting;
import net.minecraft.client.Minecraft;

public class AutoClicker
extends Module {
    public NumberSetting Speed = new NumberSetting("CPS", 12.0, 1.0, 24.0, 1.0);
    public long lastMS = System.currentTimeMillis();

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public void setTime(long Time) {
        this.lastMS = Time;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public static void click() throws AWTException {
        Robot bot = new Robot();
        bot.mousePress(1024);
        bot.mouseRelease(1024);
    }

    public AutoClicker() {
        super("AutoClicker", 0, Module.Category.GHOST);
        this.addSettings(this.Speed);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.hasTimeElapsed((long)(1000.0 / this.Speed.getValue()), true) && Minecraft.getMinecraft().currentScreen == null) {
            try {
                AutoClicker.click();
            }
            catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }
}

