package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.block.BlockSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;

@Module.Mod(displayName = "SlimeJump")
public class SlimeJump extends Module
{   
    @Op(name = "Motion", min = 0.42, max = 10.0, increment = 0.01)
    private double motion;
	Minecraft mc = Minecraft.getMinecraft();
	
	public SlimeJump() {
		this.motion = 1;
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
        final BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockSlime) {
            mc.thePlayer.motionY = motion;
        }
	}
}
