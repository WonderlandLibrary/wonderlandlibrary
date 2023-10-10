/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 */
package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringUtils;

public class TileEntitySkull
extends TileEntity {
    private int skullType;
    private int skullRotation;
    private GameProfile playerProfile = null;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
        compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
        if (this.playerProfile == null) return;
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
        compound.setTag("Owner", nbttagcompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.skullType = compound.getByte("SkullType");
        this.skullRotation = compound.getByte("Rot");
        if (this.skullType != 3) return;
        if (compound.hasKey("Owner", 10)) {
            this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
            return;
        }
        if (!compound.hasKey("ExtraType", 8)) return;
        String s = compound.getString("ExtraType");
        if (StringUtils.isNullOrEmpty(s)) return;
        this.playerProfile = new GameProfile((UUID)null, s);
        this.updatePlayerProfile();
    }

    public GameProfile getPlayerProfile() {
        return this.playerProfile;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 4, nbttagcompound);
    }

    public void setType(int type) {
        this.skullType = type;
        this.playerProfile = null;
    }

    public void setPlayerProfile(GameProfile playerProfile) {
        this.skullType = 3;
        this.playerProfile = playerProfile;
        this.updatePlayerProfile();
    }

    private void updatePlayerProfile() {
        this.playerProfile = TileEntitySkull.updateGameprofile(this.playerProfile);
        this.markDirty();
    }

    public static GameProfile updateGameprofile(GameProfile input) {
        if (input == null) return input;
        if (StringUtils.isNullOrEmpty(input.getName())) return input;
        if (input.isComplete() && input.getProperties().containsKey((Object)"textures")) {
            return input;
        }
        if (MinecraftServer.getServer() == null) {
            return input;
        }
        GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(input.getName());
        if (gameprofile == null) {
            return input;
        }
        Property property = Iterables.getFirst(gameprofile.getProperties().get((Object)"textures"), null);
        if (property != null) return gameprofile;
        return MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(gameprofile, true);
    }

    public int getSkullType() {
        return this.skullType;
    }

    public int getSkullRotation() {
        return this.skullRotation;
    }

    public void setSkullRotation(int rotation) {
        this.skullRotation = rotation;
    }
}

