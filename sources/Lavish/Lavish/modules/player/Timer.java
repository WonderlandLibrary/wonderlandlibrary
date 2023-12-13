// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import org.lwjgl.input.Mouse;
import Lavish.event.events.EventUpdate;
import net.minecraft.client.gui.ScaledResolution;
import Lavish.event.events.EventRenderGUI;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Timer extends Module
{
    Lavish.utils.misc.Timer timer;
    long balance;
    long startTime;
    long endTime;
    long endTime2;
    long time;
    
    public Timer() {
        super("Timer", 0, true, Category.Player, "Speeds up the in game time");
        this.timer = new Lavish.utils.misc.Timer();
        Client.instance.setmgr.rSetting(new Setting("Timer Amount", this, 2.0, 0.1, 10.0, false));
    }
    
    @Override
    public void onUpdate() {
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRenderGUI) {
            final ScaledResolution sr = new ScaledResolution(Timer.mc);
            Timer.mc.fontRendererObj.drawString(new StringBuilder(String.valueOf(this.balance)).toString(), sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, -1);
        }
        if (e instanceof EventUpdate) {
            if (Mouse.isButtonDown(2) && Timer.mc.gameSettings.keyBindSneak.isKeyDown()) {
                this.balance = -(this.startTime - System.currentTimeMillis());
                this.endTime = System.currentTimeMillis() + this.balance;
                Timer.mc.timer.timerSpeed = 0.1f;
            }
            if (Mouse.isButtonDown(2) && !Timer.mc.gameSettings.keyBindSneak.isKeyDown()) {
                this.balance = this.endTime - System.currentTimeMillis();
                if (this.balance >= 0L) {
                    Timer.mc.timer.timerSpeed = (float)Client.instance.setmgr.getSettingByName("Timer Amount").getValDouble();
                }
                else {
                    Timer.mc.timer.timerSpeed = 1.0f;
                }
            }
            else if (!Mouse.isButtonDown(2) && !Timer.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Timer.mc.timer.timerSpeed = 1.0f;
            }
        }
    }
    
    @Override
    public void onDisable() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEnable() {
        this.startTime = System.currentTimeMillis();
        this.endTime = System.currentTimeMillis();
    }
}
