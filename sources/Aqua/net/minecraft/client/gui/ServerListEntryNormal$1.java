package net.minecraft.client.gui;

import java.net.UnknownHostException;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.util.EnumChatFormatting;

/*
 * Exception performing whole class analysis ignored.
 */
class ServerListEntryNormal.1
implements Runnable {
    ServerListEntryNormal.1() {
    }

    public void run() {
        try {
            ServerListEntryNormal.access$100((ServerListEntryNormal)ServerListEntryNormal.this).getOldServerPinger().ping(ServerListEntryNormal.access$000((ServerListEntryNormal)ServerListEntryNormal.this));
        }
        catch (UnknownHostException var2) {
            ServerListEntryNormal.access$000((ServerListEntryNormal)ServerListEntryNormal.this).pingToServer = -1L;
            ServerListEntryNormal.access$000((ServerListEntryNormal)ServerListEntryNormal.this).serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
        }
        catch (Exception var3) {
            ServerListEntryNormal.access$000((ServerListEntryNormal)ServerListEntryNormal.this).pingToServer = -1L;
            ServerListEntryNormal.access$000((ServerListEntryNormal)ServerListEntryNormal.this).serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
        }
    }
}
