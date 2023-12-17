// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;

public class CookedFishIDTypo implements IFixableData
{
    private static final ResourceLocation WRONG;
    
    @Override
    public int getFixVersion() {
        return 502;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if (compound.hasKey("id", 8) && CookedFishIDTypo.WRONG.equals(new ResourceLocation(compound.getString("id")))) {
            compound.setString("id", "minecraft:cooked_fish");
        }
        return compound;
    }
    
    static {
        WRONG = new ResourceLocation("cooked_fished");
    }
}
