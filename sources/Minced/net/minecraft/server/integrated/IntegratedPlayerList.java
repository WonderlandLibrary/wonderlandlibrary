// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;

public class IntegratedPlayerList extends PlayerList
{
    private NBTTagCompound hostPlayerData;
    
    public IntegratedPlayerList(final IntegratedServer server) {
        super(server);
        this.setViewDistance(10);
    }
    
    @Override
    protected void writePlayerData(final EntityPlayerMP playerIn) {
        if (playerIn.getName().equals(this.getServerInstance().getServerOwner())) {
            this.hostPlayerData = playerIn.writeToNBT(new NBTTagCompound());
        }
        super.writePlayerData(playerIn);
    }
    
    @Override
    public String allowUserToConnect(final SocketAddress address, final GameProfile profile) {
        return (profile.getName().equalsIgnoreCase(this.getServerInstance().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null) ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }
    
    @Override
    public IntegratedServer getServerInstance() {
        return (IntegratedServer)super.getServerInstance();
    }
    
    @Override
    public NBTTagCompound getHostPlayerData() {
        return this.hostPlayerData;
    }
}
