// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import vip.Resolute.util.world.RandomUtil;
import net.minecraft.client.Minecraft;
import vip.Resolute.events.impl.EventTick;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class AutoClicker extends Module
{
    public NumberSetting mincps;
    public NumberSetting maxcps;
    public BooleanSetting rightClick;
    TimerUtil timer;
    
    public AutoClicker() {
        super("AutoClicker", 0, "Automatically clicks fo you", Category.COMBAT);
        this.mincps = new NumberSetting("Min CPS", 9.0, 1.0, 20.0, 1.0);
        this.maxcps = new NumberSetting("Max CPS", 12.0, 1.0, 20.0, 1.0);
        this.rightClick = new BooleanSetting("Right Click", false);
        this.timer = new TimerUtil();
        this.addSettings(this.mincps, this.maxcps, this.rightClick);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventTick) {
            if (this.rightClick.isEnabled() && AutoClicker.mc.gameSettings.keyBindRight.isKeyDown()) {
                Minecraft.getMinecraft().rightClickMouse();
            }
            else if (AutoClicker.mc.gameSettings.keyBindAttack.isKeyDown() && !AutoClicker.mc.thePlayer.isUsingItem()) {
                final double cps = RandomUtil.getRandomInRange(this.mincps.getValue(), this.maxcps.getValue());
                if (this.timer.hasElapsed((long)(1000.0 / cps))) {
                    Minecraft.getMinecraft().clickMouse();
                    this.timer.reset();
                }
            }
        }
    }
}
