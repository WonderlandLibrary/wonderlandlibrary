package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "Eagle")
public class Eagle extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
	    BlockPos BlockPos;
        BlockPos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
        if (this.mc.theWorld.getBlockState(BlockPos).getBlock() == Blocks.air) {
           this.mc.gameSettings.keyBindSneak.pressed = true;
        } else {
           this.mc.gameSettings.keyBindSneak.pressed = false;
        }
	}
}
