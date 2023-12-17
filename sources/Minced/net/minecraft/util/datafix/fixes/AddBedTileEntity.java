// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.datafix.IFixableData;

public class AddBedTileEntity implements IFixableData
{
    private static final Logger LOGGER;
    
    @Override
    public int getFixVersion() {
        return 1125;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        final int i = 416;
        try {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("Level");
            final int j = nbttagcompound.getInteger("xPos");
            final int k = nbttagcompound.getInteger("zPos");
            final NBTTagList nbttaglist = nbttagcompound.getTagList("TileEntities", 10);
            final NBTTagList nbttaglist2 = nbttagcompound.getTagList("Sections", 10);
            for (int l = 0; l < nbttaglist2.tagCount(); ++l) {
                final NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(l);
                final int i2 = nbttagcompound2.getByte("Y");
                final byte[] abyte = nbttagcompound2.getByteArray("Blocks");
                for (int j2 = 0; j2 < abyte.length; ++j2) {
                    if (416 == (abyte[j2] & 0xFF) << 4) {
                        final int k2 = j2 & 0xF;
                        final int l2 = j2 >> 8 & 0xF;
                        final int i3 = j2 >> 4 & 0xF;
                        final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                        nbttagcompound3.setString("id", "bed");
                        nbttagcompound3.setInteger("x", k2 + (j << 4));
                        nbttagcompound3.setInteger("y", l2 + (i2 << 4));
                        nbttagcompound3.setInteger("z", i3 + (k << 4));
                        nbttaglist.appendTag(nbttagcompound3);
                    }
                }
            }
        }
        catch (Exception var17) {
            AddBedTileEntity.LOGGER.warn("Unable to datafix Bed blocks, level format may be missing tags.");
        }
        return compound;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
