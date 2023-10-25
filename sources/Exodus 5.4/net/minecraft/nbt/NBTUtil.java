/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 */
package net.minecraft.nbt;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;

public final class NBTUtil {
    public static NBTTagCompound writeGameProfile(NBTTagCompound nBTTagCompound, GameProfile gameProfile) {
        if (!StringUtils.isNullOrEmpty(gameProfile.getName())) {
            nBTTagCompound.setString("Name", gameProfile.getName());
        }
        if (gameProfile.getId() != null) {
            nBTTagCompound.setString("Id", gameProfile.getId().toString());
        }
        if (!gameProfile.getProperties().isEmpty()) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            for (String string : gameProfile.getProperties().keySet()) {
                NBTTagList nBTTagList = new NBTTagList();
                for (Property property : gameProfile.getProperties().get((Object)string)) {
                    NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
                    nBTTagCompound3.setString("Value", property.getValue());
                    if (property.hasSignature()) {
                        nBTTagCompound3.setString("Signature", property.getSignature());
                    }
                    nBTTagList.appendTag(nBTTagCompound3);
                }
                nBTTagCompound2.setTag(string, nBTTagList);
            }
            nBTTagCompound.setTag("Properties", nBTTagCompound2);
        }
        return nBTTagCompound;
    }

    public static GameProfile readGameProfileFromNBT(NBTTagCompound nBTTagCompound) {
        UUID uUID;
        String string = null;
        String string2 = null;
        if (nBTTagCompound.hasKey("Name", 8)) {
            string = nBTTagCompound.getString("Name");
        }
        if (nBTTagCompound.hasKey("Id", 8)) {
            string2 = nBTTagCompound.getString("Id");
        }
        if (StringUtils.isNullOrEmpty(string) && StringUtils.isNullOrEmpty(string2)) {
            return null;
        }
        try {
            uUID = UUID.fromString(string2);
        }
        catch (Throwable throwable) {
            uUID = null;
        }
        GameProfile gameProfile = new GameProfile(uUID, string);
        if (nBTTagCompound.hasKey("Properties", 10)) {
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Properties");
            for (String string3 : nBTTagCompound2.getKeySet()) {
                NBTTagList nBTTagList = nBTTagCompound2.getTagList(string3, 10);
                int n = 0;
                while (n < nBTTagList.tagCount()) {
                    NBTTagCompound nBTTagCompound3 = nBTTagList.getCompoundTagAt(n);
                    String string4 = nBTTagCompound3.getString("Value");
                    if (nBTTagCompound3.hasKey("Signature", 8)) {
                        gameProfile.getProperties().put((Object)string3, (Object)new Property(string3, string4, nBTTagCompound3.getString("Signature")));
                    } else {
                        gameProfile.getProperties().put((Object)string3, (Object)new Property(string3, string4));
                    }
                    ++n;
                }
            }
        }
        return gameProfile;
    }

    public static boolean func_181123_a(NBTBase nBTBase, NBTBase nBTBase2, boolean bl) {
        if (nBTBase == nBTBase2) {
            return true;
        }
        if (nBTBase == null) {
            return true;
        }
        if (nBTBase2 == null) {
            return false;
        }
        if (!nBTBase.getClass().equals(nBTBase2.getClass())) {
            return false;
        }
        if (nBTBase instanceof NBTTagCompound) {
            NBTTagCompound nBTTagCompound = (NBTTagCompound)nBTBase;
            NBTTagCompound nBTTagCompound2 = (NBTTagCompound)nBTBase2;
            for (String string : nBTTagCompound.getKeySet()) {
                NBTBase nBTBase3 = nBTTagCompound.getTag(string);
                if (NBTUtil.func_181123_a(nBTBase3, nBTTagCompound2.getTag(string), bl)) continue;
                return false;
            }
            return true;
        }
        if (nBTBase instanceof NBTTagList && bl) {
            NBTTagList nBTTagList = (NBTTagList)nBTBase;
            NBTTagList nBTTagList2 = (NBTTagList)nBTBase2;
            if (nBTTagList.tagCount() == 0) {
                return nBTTagList2.tagCount() == 0;
            }
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                NBTBase nBTBase4 = nBTTagList.get(n);
                boolean bl2 = false;
                int n2 = 0;
                while (n2 < nBTTagList2.tagCount()) {
                    if (NBTUtil.func_181123_a(nBTBase4, nBTTagList2.get(n2), bl)) {
                        bl2 = true;
                        break;
                    }
                    ++n2;
                }
                if (!bl2) {
                    return false;
                }
                ++n;
            }
            return true;
        }
        return nBTBase.equals(nBTBase2);
    }
}

