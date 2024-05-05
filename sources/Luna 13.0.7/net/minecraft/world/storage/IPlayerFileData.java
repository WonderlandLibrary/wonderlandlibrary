package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract interface IPlayerFileData
{
  public abstract void writePlayerData(EntityPlayer paramEntityPlayer);
  
  public abstract NBTTagCompound readPlayerData(EntityPlayer paramEntityPlayer);
  
  public abstract String[] getAvailablePlayerDat();
}
