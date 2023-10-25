package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.IFixableData;

public class PaintingDirection implements IFixableData
{
    public int getFixVersion()
    {
        return 111;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        String s = compound.getString("id");
        boolean flag = "Painting".equals(s);
        boolean flag1 = "ItemFrame".equals(s);

        if ((flag || flag1) && !compound.hasKey("Facing", 99))
        {
            EnumFacing enumfacing;

            if (compound.hasKey("Direction", 99))
            {
                enumfacing = EnumFacing.byHorizontalIndex(compound.getByte("Direction"));
                compound.setInteger("TileX", compound.getInteger("TileX") + enumfacing.getXOffset());
                compound.setInteger("TileY", compound.getInteger("TileY") + enumfacing.getYOffset());
                compound.setInteger("TileZ", compound.getInteger("TileZ") + enumfacing.getZOffset());
                compound.removeTag("Direction");

                if (flag1 && compound.hasKey("ItemRotation", 99))
                {
                    compound.setByte("ItemRotation", (byte)(compound.getByte("ItemRotation") * 2));
                }
            }
            else
            {
                enumfacing = EnumFacing.byHorizontalIndex(compound.getByte("Dir"));
                compound.removeTag("Dir");
            }

            compound.setByte("Facing", (byte)enumfacing.getHorizontalIndex());
        }

        return compound;
    }
}
