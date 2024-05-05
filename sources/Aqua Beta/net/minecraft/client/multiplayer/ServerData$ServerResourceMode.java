package net.minecraft.client.multiplayer;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public static enum ServerData.ServerResourceMode {
    ENABLED("enabled"),
    DISABLED("disabled"),
    PROMPT("prompt");

    private final IChatComponent motd;

    private ServerData.ServerResourceMode(String name) {
        this.motd = new ChatComponentTranslation("addServer.resourcePack." + name, new Object[0]);
    }

    public IChatComponent getMotd() {
        return this.motd;
    }
}
