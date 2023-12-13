// 
// Decompiled by Procyon v0.5.30
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
    public static long pingToServer;
    public int version;
    public String gameVersion;
    public boolean field_78841_f;
    public String playerList;
    private ServerResourceMode resourceMode;
    private String serverIcon;
    private static final String __OBFID = "CL_00000890";
    
    public ServerData(final String p_i1193_1_, final String p_i1193_2_) {
        this.version = 47;
        this.gameVersion = "1.8";
        this.resourceMode = ServerResourceMode.PROMPT;
        this.serverName = p_i1193_1_;
        this.serverIP = p_i1193_2_;
    }
    
    public NBTTagCompound getNBTCompound() {
        final NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("name", this.serverName);
        var1.setString("ip", this.serverIP);
        if (this.serverIcon != null) {
            var1.setString("icon", this.serverIcon);
        }
        if (this.resourceMode == ServerResourceMode.ENABLED) {
            var1.setBoolean("acceptTextures", true);
        }
        else if (this.resourceMode == ServerResourceMode.DISABLED) {
            var1.setBoolean("acceptTextures", false);
        }
        return var1;
    }
    
    public ServerResourceMode getResourceMode() {
        return this.resourceMode;
    }
    
    public void setResourceMode(final ServerResourceMode mode) {
        this.resourceMode = mode;
    }
    
    public static ServerData getServerDataFromNBTCompound(final NBTTagCompound nbtCompound) {
        final ServerData var1 = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"));
        if (nbtCompound.hasKey("icon", 8)) {
            var1.setBase64EncodedIconData(nbtCompound.getString("icon"));
        }
        if (nbtCompound.hasKey("acceptTextures", 1)) {
            if (nbtCompound.getBoolean("acceptTextures")) {
                var1.setResourceMode(ServerResourceMode.ENABLED);
            }
            else {
                var1.setResourceMode(ServerResourceMode.DISABLED);
            }
        }
        else {
            var1.setResourceMode(ServerResourceMode.PROMPT);
        }
        return var1;
    }
    
    public String getBase64EncodedIconData() {
        return this.serverIcon;
    }
    
    public void setBase64EncodedIconData(final String icon) {
        this.serverIcon = icon;
    }
    
    public void copyFrom(final ServerData serverDataIn) {
        this.serverIP = serverDataIn.serverIP;
        this.serverName = serverDataIn.serverName;
        this.setResourceMode(serverDataIn.getResourceMode());
        this.serverIcon = serverDataIn.serverIcon;
    }
    
    public enum ServerResourceMode
    {
        ENABLED("ENABLED", 0, "enabled"), 
        DISABLED("DISABLED", 1, "disabled"), 
        PROMPT("PROMPT", 2, "prompt");
        
        private final IChatComponent motd;
        private static final ServerResourceMode[] $VALUES;
        private static final String __OBFID = "CL_00001833";
        
        private ServerResourceMode(final String p_i1053_1_, final int p_i1053_2_, final String p_i1053_3_) {
            this.motd = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_, new Object[0]);
        }
        
        public IChatComponent getMotd() {
            return this.motd;
        }
        
        static {
            $VALUES = new ServerResourceMode[] { ServerResourceMode.ENABLED, ServerResourceMode.DISABLED, ServerResourceMode.PROMPT };
        }
    }
}
