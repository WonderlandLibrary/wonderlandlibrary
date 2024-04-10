package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.BlockPos;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Parkour extends Module {

    public Parkour() {
        super("Parkour", Category.Player, "Auto jump on outside of block");
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.world.getBlockState(new BlockPos(mc.player.getPositionVector().add(0, -1, 0))).getBlock() instanceof AirBlock){
                if(mc.player.onGround) mc.player.jump();
            }
        }
        super.onUpdateEvent(event);
    }
}
