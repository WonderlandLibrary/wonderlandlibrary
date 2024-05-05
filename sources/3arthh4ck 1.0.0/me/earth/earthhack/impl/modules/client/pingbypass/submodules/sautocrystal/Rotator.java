package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal;

import me.earth.earthhack.api.event.events.Stage;
import me.earth.earthhack.impl.core.ducks.IEntityPlayerSP;
import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import net.minecraft.init.Items;

/**
 * PingBypass AutoCrystal simulates onUpdateWalkingPlayer
 * by using the packets we send. We always want to produce a
 * rotation packet so PingBypass can spoof it.
 */
public class Rotator extends ModuleListener<ServerAutoCrystal, MotionUpdateEvent>
{
    /** The offset made to yaw and pitch. */
    private float offset = 0.0004f;

    protected Rotator(ServerAutoCrystal module)
    {
        super(module, MotionUpdateEvent.class, Integer.MIN_VALUE);
    }

    @Override
    public void invoke(MotionUpdateEvent event)
    {
        if (event.getStage() == Stage.PRE)
        {
            if (InventoryUtil.isHolding(Items.END_CRYSTAL))
            {
                final float yawDif   = event.getYaw() - ((IEntityPlayerSP) mc.player).getLastReportedYaw();
                final float pitchDif = event.getPitch() - ((IEntityPlayerSP) mc.player).getLastReportedPitch();

                if (yawDif == 0.0f && pitchDif == 0.0f)
                {
                    offset = -offset;
                    event.setYaw(event.getYaw() + offset);
                    event.setPitch(event.getPitch() + offset);
                }
            }
        }
    }

}
