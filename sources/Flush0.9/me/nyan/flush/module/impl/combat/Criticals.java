package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.movement.Fly;
import me.nyan.flush.module.impl.movement.LongJump;
import me.nyan.flush.module.impl.movement.Speed;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.BlockPos;

public class Criticals extends Module {
    private final Timer timer = new Timer();
    private int onGroundTicks;
    private boolean noground;

    private final ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet", "NoGround");

    public Criticals() {
        super("Criticals", Category.COMBAT);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.isPost()) {
            return;
        }

        if (mode.is("noground") && noground) {
            e.setGround(false);
            noground = false;
        }

        if (mc.thePlayer.onGround)
            onGroundTicks++;
        else
            onGroundTicks = 0;
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        //double[] offsets = {0.0512, 0.0115, 0.001, 0.001};
        double[] offsets = {0.0625, 0, 0};

        if (e.isOutgoing()) {
            if (e.getPacket() instanceof C02PacketUseEntity) {
                if (((C02PacketUseEntity) e.getPacket()).getAction() != C02PacketUseEntity.Action.ATTACK) {
                    return;
                }
                mc.thePlayer.onCriticalHit(((C02PacketUseEntity) e.getPacket()).getEntityFromWorld(mc.theWorld));
                IBlockState iBlockState = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ));
                if (!(isEnabled(Speed.class) || isEnabled(Fly.class) || isEnabled(LongJump.class)) &&
                        MovementUtils.isOnGround(0.005) && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() &&
                        iBlockState.getBlock() instanceof BlockAir) {
                    if (timer.hasTimeElapsed(250, true) && onGroundTicks > 1 && mode.is("packet")) {
                        for (double offset : offsets) {
                            MovementUtils.packetvClip(offset);
                        }
                    }
                }
                noground = true;
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}