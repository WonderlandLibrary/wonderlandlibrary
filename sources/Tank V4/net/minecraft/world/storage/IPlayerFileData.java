package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData {
   NBTTagCompound readPlayerData(EntityPlayer var1);

   String[] getAvailablePlayerDat();

   void writePlayerData(EntityPlayer var1);
}
