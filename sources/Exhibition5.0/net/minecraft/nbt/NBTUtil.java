// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import java.util.Iterator;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.util.StringUtils;
import com.mojang.authlib.GameProfile;

public final class NBTUtil
{
    private static final String __OBFID = "CL_00001901";
    
    public static GameProfile readGameProfileFromNBT(final NBTTagCompound compound) {
        String var1 = null;
        String var2 = null;
        if (compound.hasKey("Name", 8)) {
            var1 = compound.getString("Name");
        }
        if (compound.hasKey("Id", 8)) {
            var2 = compound.getString("Id");
        }
        if (StringUtils.isNullOrEmpty(var1) && StringUtils.isNullOrEmpty(var2)) {
            return null;
        }
        UUID var3;
        try {
            var3 = UUID.fromString(var2);
        }
        catch (Throwable var12) {
            var3 = null;
        }
        final GameProfile var4 = new GameProfile(var3, var1);
        if (compound.hasKey("Properties", 10)) {
            final NBTTagCompound var5 = compound.getCompoundTag("Properties");
            for (final String var7 : var5.getKeySet()) {
                final NBTTagList var8 = var5.getTagList(var7, 10);
                for (int var9 = 0; var9 < var8.tagCount(); ++var9) {
                    final NBTTagCompound var10 = var8.getCompoundTagAt(var9);
                    final String var11 = var10.getString("Value");
                    if (var10.hasKey("Signature", 8)) {
                        var4.getProperties().put((Object)var7, (Object)new Property(var7, var11, var10.getString("Signature")));
                    }
                    else {
                        var4.getProperties().put((Object)var7, (Object)new Property(var7, var11));
                    }
                }
            }
        }
        return var4;
    }
    
    public static NBTTagCompound writeGameProfile(final NBTTagCompound p_180708_0_, final GameProfile p_180708_1_) {
        if (!StringUtils.isNullOrEmpty(p_180708_1_.getName())) {
            p_180708_0_.setString("Name", p_180708_1_.getName());
        }
        if (p_180708_1_.getId() != null) {
            p_180708_0_.setString("Id", p_180708_1_.getId().toString());
        }
        if (!p_180708_1_.getProperties().isEmpty()) {
            final NBTTagCompound var2 = new NBTTagCompound();
            for (final String var4 : p_180708_1_.getProperties().keySet()) {
                final NBTTagList var5 = new NBTTagList();
                for (final Property var7 : p_180708_1_.getProperties().get((Object)var4)) {
                    final NBTTagCompound var8 = new NBTTagCompound();
                    var8.setString("Value", var7.getValue());
                    if (var7.hasSignature()) {
                        var8.setString("Signature", var7.getSignature());
                    }
                    var5.appendTag(var8);
                }
                var2.setTag(var4, var5);
            }
            p_180708_0_.setTag("Properties", var2);
        }
        return p_180708_0_;
    }
}
