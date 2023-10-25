/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class IntegratedPlayerList
extends ServerConfigurationManager {
    private NBTTagCompound hostPlayerData;

    @Override
    public String allowUserToConnect(SocketAddress socketAddress, GameProfile gameProfile) {
        return gameProfile.getName().equalsIgnoreCase(this.getServerInstance().getServerOwner()) && this.getPlayerByUsername(gameProfile.getName()) != null ? "That name is already taken." : super.allowUserToConnect(socketAddress, gameProfile);
    }

    @Override
    protected void writePlayerData(EntityPlayerMP entityPlayerMP) {
        if (entityPlayerMP.getName().equals(this.getServerInstance().getServerOwner())) {
            this.hostPlayerData = new NBTTagCompound();
            entityPlayerMP.writeToNBT(this.hostPlayerData);
        }
        super.writePlayerData(entityPlayerMP);
    }

    @Override
    public NBTTagCompound getHostPlayerData() {
        return this.hostPlayerData;
    }

    @Override
    public IntegratedServer getServerInstance() {
        return (IntegratedServer)super.getServerInstance();
    }

    public IntegratedPlayerList(IntegratedServer integratedServer) {
        super(integratedServer);
        this.setViewDistance(10);
    }
}

