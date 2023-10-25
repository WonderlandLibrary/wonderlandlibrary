/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ServerData {
    public int version = 47;
    public String serverIP;
    private ServerResourceMode resourceMode = ServerResourceMode.PROMPT;
    private boolean field_181042_l;
    public String serverMOTD;
    public String serverName;
    public String gameVersion = "1.8.8";
    public String populationInfo;
    public String playerList;
    public long pingToServer;
    public boolean field_78841_f;
    private String serverIcon;

    public ServerResourceMode getResourceMode() {
        return this.resourceMode;
    }

    public void copyFrom(ServerData serverData) {
        this.serverIP = serverData.serverIP;
        this.serverName = serverData.serverName;
        this.setResourceMode(serverData.getResourceMode());
        this.serverIcon = serverData.serverIcon;
        this.field_181042_l = serverData.field_181042_l;
    }

    public void setBase64EncodedIconData(String string) {
        this.serverIcon = string;
    }

    public ServerData(String string, String string2, boolean bl) {
        this.serverName = string;
        this.serverIP = string2;
        this.field_181042_l = bl;
    }

    public boolean func_181041_d() {
        return this.field_181042_l;
    }

    public String getBase64EncodedIconData() {
        return this.serverIcon;
    }

    public void setResourceMode(ServerResourceMode serverResourceMode) {
        this.resourceMode = serverResourceMode;
    }

    public static ServerData getServerDataFromNBTCompound(NBTTagCompound nBTTagCompound) {
        ServerData serverData = new ServerData(nBTTagCompound.getString("name"), nBTTagCompound.getString("ip"), false);
        if (nBTTagCompound.hasKey("icon", 8)) {
            serverData.setBase64EncodedIconData(nBTTagCompound.getString("icon"));
        }
        if (nBTTagCompound.hasKey("acceptTextures", 1)) {
            if (nBTTagCompound.getBoolean("acceptTextures")) {
                serverData.setResourceMode(ServerResourceMode.ENABLED);
            } else {
                serverData.setResourceMode(ServerResourceMode.DISABLED);
            }
        } else {
            serverData.setResourceMode(ServerResourceMode.PROMPT);
        }
        return serverData;
    }

    public NBTTagCompound getNBTCompound() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        nBTTagCompound.setString("name", this.serverName);
        nBTTagCompound.setString("ip", this.serverIP);
        if (this.serverIcon != null) {
            nBTTagCompound.setString("icon", this.serverIcon);
        }
        if (this.resourceMode == ServerResourceMode.ENABLED) {
            nBTTagCompound.setBoolean("acceptTextures", true);
        } else if (this.resourceMode == ServerResourceMode.DISABLED) {
            nBTTagCompound.setBoolean("acceptTextures", false);
        }
        return nBTTagCompound;
    }

    public static enum ServerResourceMode {
        ENABLED("enabled"),
        DISABLED("disabled"),
        PROMPT("prompt");

        private final IChatComponent motd;

        public IChatComponent getMotd() {
            return this.motd;
        }

        private ServerResourceMode(String string2) {
            this.motd = new ChatComponentTranslation("addServer.resourcePack." + string2, new Object[0]);
        }
    }
}

