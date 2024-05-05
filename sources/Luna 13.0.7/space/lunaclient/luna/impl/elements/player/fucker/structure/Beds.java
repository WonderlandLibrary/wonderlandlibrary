package space.lunaclient.luna.impl.elements.player.fucker.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;

public class Beds
{
  private int xOffset;
  private int zOffset;
  private int yOffset;
  
  public Beds() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    this.xOffset = -5;
    while (this.xOffset < 6)
    {
      this.zOffset = -5;
      while (this.zOffset < 6)
      {
        this.yOffset = 5;
        while (this.yOffset > -5)
        {
          double x = Minecraft.thePlayer.posX + this.xOffset;
          double y = Minecraft.thePlayer.posY + this.yOffset;
          double z = Minecraft.thePlayer.posZ + this.zOffset;
          int id = Block.getIdFromBlock(Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
          if (id == 26)
          {
            smashBlock(new BlockPos(x, y, z));
            PlayerUtils.faceBlockPacket(new BlockPos(x, y, z));
            break;
          }
          this.yOffset -= 1;
        }
        this.zOffset += 1;
      }
      this.xOffset += 1;
    }
  }
  
  private void smashBlock(BlockPos pos)
  {
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
    Minecraft.thePlayer.swingItem();
  }
}
