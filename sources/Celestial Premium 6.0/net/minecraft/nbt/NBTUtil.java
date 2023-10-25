/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class NBTUtil {
    private static final Logger field_193591_a = LogManager.getLogger();

    @Nullable
    public static GameProfile readGameProfileFromNBT(NBTTagCompound compound) {
        String s = null;
        String s1 = null;
        if (compound.hasKey("Name", 8)) {
            s = compound.getString("Name");
        }
        if (compound.hasKey("Id", 8)) {
            s1 = compound.getString("Id");
        }
        try {
            UUID uuid;
            try {
                uuid = UUID.fromString(s1);
            }
            catch (Throwable var12) {
                uuid = null;
            }
            GameProfile gameprofile = new GameProfile(uuid, s);
            if (compound.hasKey("Properties", 10)) {
                NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");
                for (String s2 : nbttagcompound.getKeySet()) {
                    NBTTagList nbttaglist = nbttagcompound.getTagList(s2, 10);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        String s3 = nbttagcompound1.getString("Value");
                        if (nbttagcompound1.hasKey("Signature", 8)) {
                            gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound1.getString("Signature")));
                            continue;
                        }
                        gameprofile.getProperties().put(s2, new Property(s2, s3));
                    }
                }
            }
            return gameprofile;
        }
        catch (Throwable var13) {
            return null;
        }
    }

    public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile) {
        if (!StringUtils.isNullOrEmpty(profile.getName())) {
            tagCompound.setString("Name", profile.getName());
        }
        if (profile.getId() != null) {
            tagCompound.setString("Id", profile.getId().toString());
        }
        if (!profile.getProperties().isEmpty()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (String s : profile.getProperties().keySet()) {
                NBTTagList nbttaglist = new NBTTagList();
                for (Property property : profile.getProperties().get(s)) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setString("Value", property.getValue());
                    if (property.hasSignature()) {
                        nbttagcompound1.setString("Signature", property.getSignature());
                    }
                    nbttaglist.appendTag(nbttagcompound1);
                }
                nbttagcompound.setTag(s, nbttaglist);
            }
            tagCompound.setTag("Properties", nbttagcompound);
        }
        return tagCompound;
    }

    @VisibleForTesting
    public static boolean areNBTEquals(NBTBase nbt1, NBTBase nbt2, boolean compareTagList) {
        if (nbt1 == nbt2) {
            return true;
        }
        if (nbt1 == null) {
            return true;
        }
        if (nbt2 == null) {
            return false;
        }
        if (!nbt1.getClass().equals(nbt2.getClass())) {
            return false;
        }
        if (nbt1 instanceof NBTTagCompound) {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbt1;
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbt2;
            for (String s : nbttagcompound.getKeySet()) {
                NBTBase nbtbase1 = nbttagcompound.getTag(s);
                if (NBTUtil.areNBTEquals(nbtbase1, nbttagcompound1.getTag(s), compareTagList)) continue;
                return false;
            }
            return true;
        }
        if (nbt1 instanceof NBTTagList && compareTagList) {
            NBTTagList nbttaglist = (NBTTagList)nbt1;
            NBTTagList nbttaglist1 = (NBTTagList)nbt2;
            if (nbttaglist.hasNoTags()) {
                return nbttaglist1.hasNoTags();
            }
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTBase nbtbase = nbttaglist.get(i);
                boolean flag = false;
                for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
                    if (!NBTUtil.areNBTEquals(nbtbase, nbttaglist1.get(j), compareTagList)) continue;
                    flag = true;
                    break;
                }
                if (flag) continue;
                return false;
            }
            return true;
        }
        return nbt1.equals(nbt2);
    }

    public static NBTTagCompound createUUIDTag(UUID uuid) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setLong("M", uuid.getMostSignificantBits());
        nbttagcompound.setLong("L", uuid.getLeastSignificantBits());
        return nbttagcompound;
    }

    public static UUID getUUIDFromTag(NBTTagCompound tag) {
        return new UUID(tag.getLong("M"), tag.getLong("L"));
    }

    public static BlockPos getPosFromTag(NBTTagCompound tag) {
        return new BlockPos(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"));
    }

    public static NBTTagCompound createPosTag(BlockPos pos) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setInteger("X", pos.getX());
        nbttagcompound.setInteger("Y", pos.getY());
        nbttagcompound.setInteger("Z", pos.getZ());
        return nbttagcompound;
    }

    public static IBlockState readBlockState(NBTTagCompound tag) {
        if (!tag.hasKey("Name", 8)) {
            return Blocks.AIR.getDefaultState();
        }
        Block block = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("Name")));
        IBlockState iblockstate = block.getDefaultState();
        if (tag.hasKey("Properties", 10)) {
            NBTTagCompound nbttagcompound = tag.getCompoundTag("Properties");
            BlockStateContainer blockstatecontainer = block.getBlockState();
            for (String s : nbttagcompound.getKeySet()) {
                IProperty<?> iproperty = blockstatecontainer.getProperty(s);
                if (iproperty == null) continue;
                iblockstate = NBTUtil.func_193590_a(iblockstate, iproperty, s, nbttagcompound, tag);
            }
        }
        return iblockstate;
    }

    private static <T extends Comparable<T>> IBlockState func_193590_a(IBlockState p_193590_0_, IProperty<T> p_193590_1_, String p_193590_2_, NBTTagCompound p_193590_3_, NBTTagCompound p_193590_4_) {
        Optional<T> optional = p_193590_1_.parseValue(p_193590_3_.getString(p_193590_2_));
        if (optional.isPresent()) {
            return p_193590_0_.withProperty(p_193590_1_, (Comparable)optional.get());
        }
        field_193591_a.warn("Unable to read property: {} with value: {} for blockstate: {}", p_193590_2_, p_193590_3_.getString(p_193590_2_), p_193590_4_.toString());
        return p_193590_0_;
    }

    public static NBTTagCompound writeBlockState(NBTTagCompound tag, IBlockState state) {
        tag.setString("Name", Block.REGISTRY.getNameForObject(state.getBlock()).toString());
        if (!state.getProperties().isEmpty()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (Map.Entry entry : state.getProperties().entrySet()) {
                IProperty iproperty = (IProperty)entry.getKey();
                nbttagcompound.setString(iproperty.getName(), NBTUtil.getName(iproperty, (Comparable)entry.getValue()));
            }
            tag.setTag("Properties", nbttagcompound);
        }
        return tag;
    }

    private static <T extends Comparable<T>> String getName(IProperty<T> p_190010_0_, Comparable<?> p_190010_1_) {
        return p_190010_0_.getName(p_190010_1_);
    }
}

