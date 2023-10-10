package client.module.impl.player.nofall;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.MoveEvent;
import client.module.impl.player.NoFall;
import client.value.Mode;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;

public class MushNoFall extends Mode<NoFall> {

    private boolean active;

    public MushNoFall(final String name, final NoFall parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (active && mc.thePlayer.fallDistance > 3F) {
            event.setY(event.getY() + 0.0626);
            event.setOnGround(true);
        }
    };

    @EventLink
    public final Listener<MoveEvent> onMove = event -> active = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + event.getX(), mc.thePlayer.posY + event.getY(), mc.thePlayer.posZ + event.getZ())).getBlock().getMaterial() != Material.air;
}
