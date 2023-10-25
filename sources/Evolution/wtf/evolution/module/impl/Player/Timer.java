package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.SliderSetting;



@ModuleInfo(name = "Timer", type = Category.Player)
public class Timer extends Module {

    public float ticks = 0;
    public boolean active;

    public float animWidth;


    public SliderSetting timer = new SliderSetting("Timer", 1, 0.1f, 15, 0.1f).call(this);
    public BooleanSetting smart = new BooleanSetting("Smart", false).call(this);
    public ColorSetting color = new ColorSetting("Color", -1).setHidden(() -> !smart.get()).call(this);


    @EventTarget
    public void onSend(EventPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof CPacketPlayer.Position || eventSendPacket.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (ticks <= 25 && !active) {
                if (MovementUtil.isMoving()) {
                    ticks+=0.6;
                }
            }
        }
    }
    @EventTarget
    public void onPreUpdate(EventMotion e) {
        if (smart.get()) {
            if (!active)  mc.timer.timerSpeed = timer.get(); else  mc.timer.timerSpeed = 1;
        }
        else {
            mc.timer.timerSpeed = timer.get();
        }
        ticks = MathHelper.clamp(ticks, 0, 100);
    }
    @EventTarget
    public void onRender(EventDisplay e) {

    }
    public void onDisable() {
        super.onDisable();
        active = true;
        this.mc.timer.timerSpeed = 1.0f;
    }
}
