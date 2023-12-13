package me.aidanmees.trivia.hook;

import me.aidanmees.trivia.client.modules.Exploits.Phase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class EntityPlayerSPHook
extends EntityPlayerSP
{
public EntityPlayerSPHook(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_)
{
  super(mcIn, worldIn, p_i46278_3_, p_i46278_4_);
}




public void func_175141_a(TileEntitySign p_175141_1_)
{
  this.mc.displayGuiScreen(new GuiEditSignHook(p_175141_1_));
}
}
