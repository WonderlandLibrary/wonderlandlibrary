package vestige.module.impl.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import vestige.event.Listener;
import vestige.event.impl.MotionEvent;
import vestige.event.impl.PostStepEvent;
import vestige.event.impl.PreStepEvent;
import vestige.event.impl.UpdateEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.network.PacketUtil;

public class Step extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
    private final DoubleSetting height = new DoubleSetting("Height", () -> mode.is("Vanilla"), 1, 1, 9, 0.5);

    private final DoubleSetting timer = new DoubleSetting("Timer", 1, 0.1, 1, 0.05);

    private boolean prevOffGround;

    private boolean timerTick;

    public Step() {
        super("Step", Category.MOVEMENT);
        this.addSettings(mode, height, timer);
    }

    @Override
    public void onDisable() {
        prevOffGround = false;

        mc.timer.timerSpeed = 1F;
        mc.thePlayer.stepHeight = 0.6F;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        switch (mode.getMode()) {
            case "Vanilla":
                mc.thePlayer.stepHeight = (float) height.getValue();
                break;
            case "NCP":
                mc.thePlayer.stepHeight = 1F;
                break;
        }

        if(timerTick) {
            mc.timer.timerSpeed = 1F;
            timerTick = false;
        }
    }

    @Listener
    public void onPreStep(PreStepEvent event) {
        if(!mode.is("Vanilla")) {
            if(mc.thePlayer.onGround && prevOffGround) {
                if(event.getHeight() > 0.6) {
                    event.setHeight(0.6F);
                }
            }
        }
    }

    @Listener
    public void onPostStep(PostStepEvent event) {
        if(event.getHeight() > 0.6F) {
            if(timer.getValue() < 1) {
                mc.timer.timerSpeed = (float) timer.getValue();
                timerTick = true;
            }

            switch (mode.getMode()) {
                case "NCP":
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false));
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75, mc.thePlayer.posZ, false));
                    break;
            }
        }
    }

    @Listener
    public void onMotion(MotionEvent event) {
        prevOffGround = !event.isOnGround();
    }

}
