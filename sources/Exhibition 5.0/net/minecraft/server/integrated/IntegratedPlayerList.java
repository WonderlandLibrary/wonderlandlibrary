// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.ServerConfigurationManager;

public class IntegratedPlayerList extends ServerConfigurationManager
{
    private NBTTagCompound hostPlayerData;
    private static final String __OBFID = "CL_00001128";
    
    public IntegratedPlayerList(final IntegratedServer p_i1314_1_) {
        super(p_i1314_1_);
        this.setViewDistance(10);
    }
    
    @Override
    protected void writePlayerData(final EntityPlayerMP playerIn) {
        if (playerIn.getName().equals(this.func_180603_b().getServerOwner())) {
            playerIn.writeToNBT(this.hostPlayerData = new NBTTagCompound());
        }
        super.writePlayerData(playerIn);
    }
    
    @Override
    public String allowUserToConnect(final SocketAddress address, final GameProfile profile) {
        return (profile.getName().equalsIgnoreCase(this.func_180603_b().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null) ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }
    
    public IntegratedServer func_180603_b() {
        return (IntegratedServer)super.getServerInstance();
    }
    
    @Override
    public NBTTagCompound getHostPlayerData() {
        return this.hostPlayerData;
    }
    
    @Override
    public MinecraftServer getServerInstance() {
        return this.func_180603_b();
    }
}
