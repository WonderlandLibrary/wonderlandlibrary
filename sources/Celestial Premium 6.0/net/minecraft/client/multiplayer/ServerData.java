/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ServerData {
    public String serverName;
    public String serverIP;
    public String populationInfo;
    public String serverMOTD;
    public long pingToServer;
    public int version = 340;
    public String gameVersion = "1.12.2";
    public boolean pinged;
    public String playerList;
    private ServerResourceMode resourceMode = ServerResourceMode.PROMPT;
    private String serverIcon;
    private boolean lanServer;

    public ServerData(String name, String ip, boolean isLan) {
        this.serverName = name;
        this.serverIP = ip;
        this.lanServer = isLan;
    }

    public NBTTagCompound getNBTCompound() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("name", this.serverName);
        nbttagcompound.setString("ip", this.serverIP);
        if (this.serverIcon != null) {
            nbttagcompound.setString("icon", this.serverIcon);
        }
        if (this.resourceMode == ServerResourceMode.ENABLED) {
            nbttagcompound.setBoolean("acceptTextures", true);
        } else if (this.resourceMode == ServerResourceMode.DISABLED) {
            nbttagcompound.setBoolean("acceptTextures", false);
        }
        return nbttagcompound;
    }

    public ServerResourceMode getResourceMode() {
        return this.resourceMode;
    }

    public void setResourceMode(ServerResourceMode mode) {
        this.resourceMode = mode;
    }

    public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound) {
        ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
        if (nbtCompound.hasKey("icon", 8)) {
            serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
        }
        if (nbtCompound.hasKey("acceptTextures", 1)) {
            if (nbtCompound.getBoolean("acceptTextures")) {
                serverdata.setResourceMode(ServerResourceMode.ENABLED);
            } else {
                serverdata.setResourceMode(ServerResourceMode.DISABLED);
            }
        } else {
            serverdata.setResourceMode(ServerResourceMode.PROMPT);
        }
        return serverdata;
    }

    public String getBase64EncodedIconData() {
        return this.serverIcon;
    }

    public void setBase64EncodedIconData(String icon) {
        this.serverIcon = icon;
    }

    public boolean isOnLAN() {
        return this.lanServer;
    }

    public void copyFrom(ServerData serverDataIn) {
        this.serverIP = serverDataIn.serverIP;
        this.serverName = serverDataIn.serverName;
        this.setResourceMode(serverDataIn.getResourceMode());
        this.serverIcon = serverDataIn.serverIcon;
        this.lanServer = serverDataIn.lanServer;
    }

    public static enum ServerResourceMode {
        ENABLED("enabled"),
        DISABLED("disabled"),
        PROMPT("prompt");

        private final ITextComponent motd;

        private ServerResourceMode(String name) {
            this.motd = new TextComponentTranslation("addServer.resourcePack." + name, new Object[0]);
        }

        public ITextComponent getMotd() {
            return this.motd;
        }
    }
}

