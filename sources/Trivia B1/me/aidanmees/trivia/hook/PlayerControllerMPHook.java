package me.aidanmees.trivia.hook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class PlayerControllerMPHook
extends PlayerControllerMP
{
public PlayerControllerMPHook(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_)
{
  super(mcIn, p_i45062_2_);
}

public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_)
{
  Minecraft.getMinecraft();return new EntityPlayerSPHook(mc, worldIn, this.netClientHandler, p_178892_2_);
}

}