package today.flux.module.implement.World.phase;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import today.flux.event.PreUpdateEvent;
import today.flux.module.SubModule;

public class Clip extends SubModule {

    public Clip() {
        super("Clip", "Phase");
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent event) {
            mc.thePlayer.setPosition(this.mc.thePlayer.posX,
                    this.mc.thePlayer.posY - 3, this.mc.thePlayer.posZ);

    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return mc.theWorld
                .getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }


}
