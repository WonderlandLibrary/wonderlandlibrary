// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import java.util.UUID;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;

public class TileEntitySkull extends TileEntity
{
    private int skullType;
    private int skullRotation;
    private GameProfile playerProfile;
    private static final String __OBFID = "CL_00000364";
    
    public TileEntitySkull() {
        this.playerProfile = null;
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
        compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
        if (this.playerProfile != null) {
            final NBTTagCompound var2 = new NBTTagCompound();
            NBTUtil.writeGameProfile(var2, this.playerProfile);
            compound.setTag("Owner", var2);
        }
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
                final String var2 = compound.getString("ExtraType");
                if (!StringUtils.isNullOrEmpty(var2)) {
                    this.playerProfile = new GameProfile((UUID)null, var2);
                    this.func_152109_d();
                }
            }
        }
    }
    
    public GameProfile getPlayerProfile() {
        return this.playerProfile;
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 4, var1);
    }
    
    public void setType(final int type) {
        this.skullType = type;
        this.playerProfile = null;
    }
    
    public void setPlayerProfile(final GameProfile playerProfile) {
        this.skullType = 3;
        this.playerProfile = playerProfile;
        this.func_152109_d();
    }
    
    private void func_152109_d() {
        this.playerProfile = updateGameprofile(this.playerProfile);
        this.markDirty();
    }
    
    public static GameProfile updateGameprofile(final GameProfile input) {
        if (input == null || StringUtils.isNullOrEmpty(input.getName())) {
            return input;
        }
        if (input.isComplete() && input.getProperties().containsKey((Object)"textures")) {
            return input;
        }
        if (MinecraftServer.getServer() == null) {
            return input;
        }
        GameProfile var1 = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(input.getName());
        if (var1 == null) {
            return input;
        }
        final Property var2 = (Property)Iterables.getFirst((Iterable)var1.getProperties().get((Object)"textures"), (Object)null);
        if (var2 == null) {
            var1 = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(var1, true);
        }
        return var1;
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
}
