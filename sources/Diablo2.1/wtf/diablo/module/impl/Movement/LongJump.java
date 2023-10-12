package wtf.diablo.module.impl.Movement;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.player.MoveUtil;
import wtf.diablo.utils.world.EntityUtil;

@Getter
@Setter
public class LongJump extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Verus", "Watchdog");
    private double verusBoost;
    public boolean shouldJump;

    public LongJump() {
        super("LongJump", "We longjump XD", Category.MOVEMENT, ServerType.All);
        this.addSettings(mode);
    }

    @Override
    public void onEnable() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        shouldJump = true;
        if (mc.thePlayer.onGround) {
            switch (mode.getMode()) {
                case "Watchdog":
                    for (int i = 0; i < 49; i++) {
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625D, z, false));
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));

                    }
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                    break;
                case "Verus":
                    verusBoost = 3.4;
                    EntityUtil.damageVerus();
                    break;
            }
        } else {
            ChatUtil.log("You must be onground!");
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        this.setSuffix(mode.getMode());
        switch (mode.getMode()) {
            case "Watchdog":
                if (e.getType() == EventType.Pre) {
                    if (MoveUtil.isMoving()) {

                        if (mc.thePlayer.onGround) {
                            if (shouldJump) {
                                mc.thePlayer.motionY = 0.70F;
                                shouldJump = false;
                            } else {
                                mc.timer.timerSpeed = 1f;
                                this.setToggled(false);
                            }
                        }
                    }
                }
                if(e.getType() == EventType.Post){
                    MoveUtil.setMotion(MoveUtil.getBaseMoveSpeed() * 1.14F);
                }
                break;
            case "Verus":
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround && shouldJump) {
                        mc.thePlayer.motionY = 0.42F;
                    }
                    if (mc.thePlayer.hurtTime != 0) {
                        MoveUtil.setMotion(verusBoost);
                        verusBoost -= 0.05;
                    }
                    if (mc.thePlayer.lastTickPosY > mc.thePlayer.posY) {
                        mc.thePlayer.motionY *= 0.83;
                    }
                    if ((mc.thePlayer.onGround && !shouldJump) || mc.thePlayer.isInWater()) {
                        this.setToggled(false);
                    }
                }
                break;
        }
    }
}
