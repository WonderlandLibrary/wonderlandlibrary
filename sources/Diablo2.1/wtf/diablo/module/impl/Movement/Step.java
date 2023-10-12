package wtf.diablo.module.impl.Movement;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import wtf.diablo.events.impl.StepConfirmEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.module.impl.Player.Scaffold;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.utils.packet.PacketUtil;
import wtf.diablo.utils.player.MoveUtil;

@Getter
@Setter
public class Step extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Watchdog");

    public Step() {
        super("Step", "Step up things", Category.MOVEMENT, ServerType.All);
        this.addSettings(mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if(!(ModuleManager.getModule(Scaffold.class).isToggled() || ModuleManager.getModule(Speed.class).isToggled() || ModuleManager.getModule(Fly.class).isToggled())) {
            mc.thePlayer.stepHeight = 1F;
        } else {
            mc.thePlayer.stepHeight = 0.6F;
        }
    }

    @Subscribe
    public void onStep(StepConfirmEvent e) {
        this.setSuffix(mode.getMode());

        double posX = mc.thePlayer.posX;
        double posY = mc.thePlayer.posY;
        double posZ = mc.thePlayer.posZ;

        double yChange = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;

        switch (mode.getMode()) {
            case "Watchdog":
                if (mc.thePlayer.onGround && MoveUtil.isMoving() && yChange > 0.7F && yChange < 1.5F) {
                    for (double o : new double[]{0.42F, 0.75F}) {
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + o, posZ, false));
                    }
                    for (int i = 0; i < 4; i++) {
                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                    }
                    PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                }
                break;
        }
    }
}
