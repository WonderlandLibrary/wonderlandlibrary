// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.nbt.NBTTagCompound;

public class ServerData
{
    public String serverName;
    public String serverIP;
    public String populationInfo;
    public String serverMOTD;
    public long pingToServer;
    public int version;
    public String gameVersion;
    public boolean field_78841_f;
    public String playerList;
    private ServerResourceMode resourceMode;
    private String serverIcon;
    private boolean lanServer;
    
    public ServerData(final String name, final String ip, final boolean isLan) {
        this.version = 47;
        this.gameVersion = "1.8.9";
        this.resourceMode = ServerResourceMode.PROMPT;
        this.serverName = name;
        this.serverIP = ip;
        this.lanServer = isLan;
    }
    
    public NBTTagCompound getNBTCompound() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("name", this.serverName);
        nbttagcompound.setString("ip", this.serverIP);
        if (this.serverIcon != null) {
            nbttagcompound.setString("icon", this.serverIcon);
        }
        if (this.resourceMode == ServerResourceMode.ENABLED) {
            nbttagcompound.setBoolean("acceptTextures", true);
        }
        else if (this.resourceMode == ServerResourceMode.DISABLED) {
            nbttagcompound.setBoolean("acceptTextures", false);
        }
        return nbttagcompound;
    }
    
    public ServerResourceMode getResourceMode() {
        return this.resourceMode;
    }
    
    public void setResourceMode(final ServerResourceMode mode) {
        this.resourceMode = mode;
    }
    
    public static ServerData getServerDataFromNBTCompound(final NBTTagCompound nbtCompound) {
        final ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
        if (nbtCompound.hasKey("icon", 8)) {
            serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
        }
        if (nbtCompound.hasKey("acceptTextures", 1)) {
            if (nbtCompound.getBoolean("acceptTextures")) {
                serverdata.setResourceMode(ServerResourceMode.ENABLED);
            }
            else {
                serverdata.setResourceMode(ServerResourceMode.DISABLED);
            }
        }
        else {
            serverdata.setResourceMode(ServerResourceMode.PROMPT);
        }
        return serverdata;
    }
    
    public String getBase64EncodedIconData() {
        return this.serverIcon;
    }
    
    public void setBase64EncodedIconData(final String icon) {
        this.serverIcon = icon;
    }
    
    public boolean isOnLAN() {
        return this.lanServer;
    }
    
    public void copyFrom(final ServerData serverDataIn) {
        this.serverIP = serverDataIn.serverIP;
        this.serverName = serverDataIn.serverName;
        this.setResourceMode(serverDataIn.getResourceMode());
        this.serverIcon = serverDataIn.serverIcon;
        this.lanServer = serverDataIn.lanServer;
    }
    
    public enum ServerResourceMode
    {
        ENABLED("enabled"), 
        DISABLED("disabled"), 
        PROMPT("prompt");
        
        private final IChatComponent motd;
        
        private ServerResourceMode(final String name) {
            this.motd = new ChatComponentTranslation("addServer.resourcePack." + name, new Object[0]);
        }
        
        public IChatComponent getMotd() {
            return this.motd;
        }
    }
}
