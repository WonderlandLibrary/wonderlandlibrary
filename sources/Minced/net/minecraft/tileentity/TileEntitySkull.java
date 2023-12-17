// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSkull;
import net.minecraft.util.Mirror;
import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import javax.annotation.Nullable;
import java.util.UUID;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.server.management.PlayerProfileCache;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.ITickable;

public class TileEntitySkull extends TileEntity implements ITickable
{
    private int skullType;
    private int skullRotation;
    private GameProfile playerProfile;
    private int dragonAnimatedTicks;
    private boolean dragonAnimated;
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;
    
    public static void setProfileCache(final PlayerProfileCache profileCacheIn) {
        TileEntitySkull.profileCache = profileCacheIn;
    }
    
    public static void setSessionService(final MinecraftSessionService sessionServiceIn) {
        TileEntitySkull.sessionService = sessionServiceIn;
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
        compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
        if (this.playerProfile != null) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
            compound.setTag("Owner", nbttagcompound);
        }
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.skullType = compound.getByte("SkullType");
        this.skullRotation = compound.getByte("Rot");
        if (this.skullType == 3) {
            if (compound.hasKey("Owner", 10)) {
                this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
            }
            else if (compound.hasKey("ExtraType", 8)) {
                final String s = compound.getString("ExtraType");
                if (!StringUtils.isNullOrEmpty(s)) {
                    this.playerProfile = new GameProfile((UUID)null, s);
                    this.updatePlayerProfile();
                }
            }
        }
    }
    
    @Override
    public void update() {
        if (this.skullType == 5) {
            if (this.world.isBlockPowered(this.pos)) {
                this.dragonAnimated = true;
                ++this.dragonAnimatedTicks;
            }
            else {
                this.dragonAnimated = false;
            }
        }
    }
    
    public float getAnimationProgress(final float p_184295_1_) {
        return this.dragonAnimated ? (this.dragonAnimatedTicks + p_184295_1_) : ((float)this.dragonAnimatedTicks);
    }
    
    @Nullable
    public GameProfile getPlayerProfile() {
        return this.playerProfile;
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    public void setType(final int type) {
        this.skullType = type;
        this.playerProfile = null;
    }
    
    public void setPlayerProfile(@Nullable final GameProfile playerProfile) {
        this.skullType = 3;
        this.playerProfile = playerProfile;
        this.updatePlayerProfile();
    }
    
    private void updatePlayerProfile() {
        this.playerProfile = updateGameProfile(this.playerProfile);
        this.markDirty();
    }
    
    public static GameProfile updateGameProfile(final GameProfile input) {
        if (input == null || StringUtils.isNullOrEmpty(input.getName())) {
            return input;
        }
        if (input.isComplete() && input.getProperties().containsKey((Object)"textures")) {
            return input;
        }
        if (TileEntitySkull.profileCache == null || TileEntitySkull.sessionService == null) {
            return input;
        }
        GameProfile gameprofile = TileEntitySkull.profileCache.getGameProfileForUsername(input.getName());
        if (gameprofile == null) {
            return input;
        }
        final Property property = (Property)Iterables.getFirst((Iterable)gameprofile.getProperties().get((Object)"textures"), (Object)null);
        if (property == null) {
            gameprofile = TileEntitySkull.sessionService.fillProfileProperties(gameprofile, true);
        }
        return gameprofile;
    }
    
    public int getSkullType() {
        return this.skullType;
    }
    
    public int getSkullRotation() {
        return this.skullRotation;
    }
    
    public void setSkullRotation(final int rotation) {
        this.skullRotation = rotation;
    }
    
    @Override
    public void mirror(final Mirror mirrorIn) {
        if (this.world != null && this.world.getBlockState(this.getPos()).getValue((IProperty<Comparable>)BlockSkull.FACING) == EnumFacing.UP) {
            this.skullRotation = mirrorIn.mirrorRotation(this.skullRotation, 16);
        }
    }
    
    @Override
    public void rotate(final Rotation rotationIn) {
        if (this.world != null && this.world.getBlockState(this.getPos()).getValue((IProperty<Comparable>)BlockSkull.FACING) == EnumFacing.UP) {
            this.skullRotation = rotationIn.rotate(this.skullRotation, 16);
        }
    }
}
