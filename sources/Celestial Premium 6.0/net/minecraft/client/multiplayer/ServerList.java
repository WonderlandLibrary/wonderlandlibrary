/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerList {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final List<ServerData> servers = Lists.newArrayList();

    public ServerList(Minecraft mcIn) {
        this.mc = mcIn;
        this.loadServerList();
    }

    private void serverAdd() {
        this.addServerData(new ServerDataFeatured("\u041b\u0443\u0447\u0448\u0438\u0439 \u0441\u0435\u0440\u0432\u0435\u0440", "mc.StormHVH.ru"));
    }

    public void loadServerList() {
        try {
            this.servers.clear();
            this.serverAdd();
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.gameDir, "servers.dat"));
            if (nbttagcompound == null) {
                return;
            }
            NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
            }
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't load server list", (Throwable)exception);
        }
    }

    public void saveServerList() {
        try {
            NBTTagList nbttaglist = new NBTTagList();
            for (ServerData serverdata : this.servers) {
                if (serverdata instanceof ServerDataFeatured) continue;
                nbttaglist.appendTag(serverdata.getNBTCompound());
            }
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.gameDir, "servers.dat"));
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't save server list", (Throwable)exception);
        }
    }

    public ServerData getServerData(int index) {
        return this.servers.get(index);
    }

    public void removeServerData(int index) {
        this.servers.remove(index);
    }

    public void addServerData(ServerData server) {
        this.servers.add(server);
    }

    public int countServers() {
        return this.servers.size();
    }

    public void swapServers(int pos1, int pos2) {
        ServerData serverdata = this.getServerData(pos1);
        this.servers.set(pos1, this.getServerData(pos2));
        this.servers.set(pos2, serverdata);
        this.saveServerList();
    }

    public void set(int index, ServerData server) {
        this.servers.set(index, server);
    }

    public static void saveSingleServer(ServerData server) {
        ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();
        for (int i = 0; i < serverlist.countServers(); ++i) {
            ServerData serverdata = serverlist.getServerData(i);
            if (!serverdata.serverName.equals(server.serverName) || !serverdata.serverIP.equals(server.serverIP)) continue;
            serverlist.set(i, server);
            break;
        }
        serverlist.saveServerList();
    }

    public class ServerDataFeatured
    extends ServerData {
        public ServerDataFeatured(String serverName, String serverIP) {
            super(serverName, serverIP, false);
        }
    }
}

