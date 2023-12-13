// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.movement;

import Lavish.utils.math.MathUtils;
import Lavish.utils.movement.MovementUtil;
import Lavish.event.events.EventUpdate;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import Lavish.event.events.EventPacket;
import Lavish.event.Event;
import Lavish.modules.player.Scaffold;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class Speed extends Module
{
    double speed;
    int ticks;
    Timer timer;
    int posY;
    int posYGround;
    
    public Speed() {
        super("Speed", 0, true, Category.Movement, "Makes you faster");
        this.timer = new Timer();
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Hypixel");
        options.add("Verus Hop");
        options.add("MMC");
        Client.instance.setmgr.rSetting(new Setting("Speed Mode", this, "Hypixel", options));
        Client.instance.setmgr.rSetting(new Setting("Speed Flag Check", this, false));
    }
    
    @Override
    public void onEnable() {
        Scaffold.ypos = (int)Speed.mc.thePlayer.posY;
        Speed.mc.timer.timerSpeed = 1.0f;
        this.ticks = 1;
    }
    
    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket && Client.instance.setmgr.getSettingByName("Speed Flag Check").getValBoolean() && ((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook) {
            this.toggle();
        }
        if (e instanceof EventUpdate) {
            if (Client.instance.setmgr.getSettingByName("Speed Mode").getValString().equalsIgnoreCase("Hypixel")) {
                if (Speed.mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    Speed.mc.thePlayer.jump();
                    this.posYGround = (int)Speed.mc.thePlayer.getPositionVector().yCoord;
                    Speed.mc.timer.timerSpeed = MathUtils.getRandomInRange(1.0f, 1.3f);
                    this.timer.reset();
                }
                else {
                    this.speed = 0.26;
                }
                this.posY = (int)Speed.mc.thePlayer.getPositionVector().yCoord;
                if (this.posY > this.posYGround && this.timer.hasTimeElapsed(200.0, true)) {
                    Speed.mc.thePlayer.motionY = -0.10000000149011612;
                }
                MovementUtil.setSpeed(this.speed);
            }
            if (Client.instance.setmgr.getSettingByName("Speed Mode").getValString().equalsIgnoreCase("MMC")) {
                if (this.ticks == 1) {
                    Speed.mc.timer.timerSpeed = 0.1f;
                }
                if (this.ticks >= 1.1) {
                    Speed.mc.timer.timerSpeed = 2.5f;
                }
                if (this.ticks >= 20) {
                    this.ticks = 0;
                }
                ++this.ticks;
            }
            if (Client.instance.setmgr.getSettingByName("Speed Mode").getValString().equalsIgnoreCase("Verus Hop")) {
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.jump();
                }
                if (!Speed.mc.thePlayer.onGround) {
                    this.speed = 0.3;
                }
                MovementUtil.setSpeed(this.speed);
            }
        }
    }
}
