// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData
{
    private static final List<String> MINECART_TYPE_LIST;
    
    @Override
    public int getFixVersion() {
        return 106;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("Minecart".equals(compound.getString("id"))) {
            String s = "MinecartRideable";
            final int i = compound.getInteger("Type");
            if (i > 0 && i < MinecartEntityTypes.MINECART_TYPE_LIST.size()) {
                s = MinecartEntityTypes.MINECART_TYPE_LIST.get(i);
            }
            compound.setString("id", s);
            compound.removeTag("Type");
        }
        return compound;
    }
    
    static {
        MINECART_TYPE_LIST = Lists.newArrayList((Object[])new String[] { "MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock" });
    }
}
