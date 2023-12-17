// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.nbt;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import com.google.common.base.Optional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.StringUtils;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import org.apache.logging.log4j.Logger;

public final class NBTUtil
{
    private static final Logger LOGGER;
    
    @Nullable
    public static GameProfile readGameProfileFromNBT(final NBTTagCompound compound) {
        String s = null;
        String s2 = null;
        if (compound.hasKey("Name", 8)) {
            s = compound.getString("Name");
        }
        Label_0040: {
            if (!compound.hasKey("Id", 8)) {
                break Label_0040;
            }
            s2 = compound.getString("Id");
            try {
                UUID uuid;
                try {
                    uuid = UUID.fromString(s2);
                }
                catch (Throwable var12) {
                    uuid = null;
                }
                final GameProfile gameprofile = new GameProfile(uuid, s);
                if (compound.hasKey("Properties", 10)) {
                    final NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");
                    for (final String s3 : nbttagcompound.getKeySet()) {
                        final NBTTagList nbttaglist = nbttagcompound.getTagList(s3, 10);
                        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                            final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                            final String s4 = nbttagcompound2.getString("Value");
                            if (nbttagcompound2.hasKey("Signature", 8)) {
                                gameprofile.getProperties().put((Object)s3, (Object)new Property(s3, s4, nbttagcompound2.getString("Signature")));
                            }
                            else {
                                gameprofile.getProperties().put((Object)s3, (Object)new Property(s3, s4));
                            }
                        }
                    }
                }
                return gameprofile;
            }
            catch (Throwable var13) {
                return null;
            }
        }
    }
    
    public static NBTTagCompound writeGameProfile(final NBTTagCompound tagCompound, final GameProfile profile) {
        if (!StringUtils.isNullOrEmpty(profile.getName())) {
            tagCompound.setString("Name", profile.getName());
        }
        if (profile.getId() != null) {
            tagCompound.setString("Id", profile.getId().toString());
        }
        if (!profile.getProperties().isEmpty()) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (final String s : profile.getProperties().keySet()) {
                final NBTTagList nbttaglist = new NBTTagList();
                for (final Property property : profile.getProperties().get((Object)s)) {
                    final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                    nbttagcompound2.setString("Value", property.getValue());
                    if (property.hasSignature()) {
                        nbttagcompound2.setString("Signature", property.getSignature());
                    }
                    nbttaglist.appendTag(nbttagcompound2);
                }
                nbttagcompound.setTag(s, nbttaglist);
            }
            tagCompound.setTag("Properties", nbttagcompound);
        }
        return tagCompound;
    }
    
    @VisibleForTesting
    public static boolean areNBTEquals(final NBTBase nbt1, final NBTBase nbt2, final boolean compareTagList) {
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
            final NBTTagCompound nbttagcompound = (NBTTagCompound)nbt1;
            final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbt2;
            for (final String s : nbttagcompound.getKeySet()) {
                final NBTBase nbtbase1 = nbttagcompound.getTag(s);
                if (!areNBTEquals(nbtbase1, nbttagcompound2.getTag(s), compareTagList)) {
                    return false;
                }
            }
            return true;
        }
        if (!(nbt1 instanceof NBTTagList) || !compareTagList) {
            return nbt1.equals(nbt2);
        }
        final NBTTagList nbttaglist = (NBTTagList)nbt1;
        final NBTTagList nbttaglist2 = (NBTTagList)nbt2;
        if (nbttaglist.isEmpty()) {
            return nbttaglist2.isEmpty();
        }
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTBase nbtbase2 = nbttaglist.get(i);
            boolean flag = false;
            for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
                if (areNBTEquals(nbtbase2, nbttaglist2.get(j), compareTagList)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
    
    public static NBTTagCompound createUUIDTag(final UUID uuid) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setLong("M", uuid.getMostSignificantBits());
        nbttagcompound.setLong("L", uuid.getLeastSignificantBits());
        return nbttagcompound;
    }
    
    public static UUID getUUIDFromTag(final NBTTagCompound tag) {
        return new UUID(tag.getLong("M"), tag.getLong("L"));
    }
    
    public static BlockPos getPosFromTag(final NBTTagCompound tag) {
        return new BlockPos(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"));
    }
    
    public static NBTTagCompound createPosTag(final BlockPos pos) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setInteger("X", pos.getX());
        nbttagcompound.setInteger("Y", pos.getY());
        nbttagcompound.setInteger("Z", pos.getZ());
        return nbttagcompound;
    }
    
    public static IBlockState readBlockState(final NBTTagCompound tag) {
        if (!tag.hasKey("Name", 8)) {
            return Blocks.AIR.getDefaultState();
        }
        final Block block = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("Name")));
        IBlockState iblockstate = block.getDefaultState();
        if (tag.hasKey("Properties", 10)) {
            final NBTTagCompound nbttagcompound = tag.getCompoundTag("Properties");
            final BlockStateContainer blockstatecontainer = block.getBlockState();
            for (final String s : nbttagcompound.getKeySet()) {
                final IProperty<?> iproperty = blockstatecontainer.getProperty(s);
                if (iproperty != null) {
                    iblockstate = setValueHelper(iblockstate, iproperty, s, nbttagcompound, tag);
                }
            }
        }
        return iblockstate;
    }
    
    private static <T extends Comparable<T>> IBlockState setValueHelper(final IBlockState p_193590_0_, final IProperty<T> p_193590_1_, final String p_193590_2_, final NBTTagCompound p_193590_3_, final NBTTagCompound p_193590_4_) {
        final Optional<T> optional = p_193590_1_.parseValue(p_193590_3_.getString(p_193590_2_));
        if (optional.isPresent()) {
            return p_193590_0_.withProperty(p_193590_1_, optional.get());
        }
        NBTUtil.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", (Object)p_193590_2_, (Object)p_193590_3_.getString(p_193590_2_), (Object)p_193590_4_.toString());
        return p_193590_0_;
    }
    
    public static NBTTagCompound writeBlockState(final NBTTagCompound tag, final IBlockState state) {
        tag.setString("Name", Block.REGISTRY.getNameForObject(state.getBlock()).toString());
        if (!state.getProperties().isEmpty()) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (final Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
                final IProperty<?> iproperty = entry.getKey();
                nbttagcompound.setString(iproperty.getName(), getName(iproperty, entry.getValue()));
            }
            tag.setTag("Properties", nbttagcompound);
        }
        return tag;
    }
    
    private static <T extends Comparable<T>> String getName(final IProperty<T> p_190010_0_, final Comparable<?> p_190010_1_) {
        return p_190010_0_.getName((T)p_190010_1_);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
