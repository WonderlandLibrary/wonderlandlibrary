// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.File;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class ServerList
{
    private static final Logger LOGGER;
    private final Minecraft mc;
    private final List<ServerData> servers;
    
    public ServerList(final Minecraft mcIn) {
        this.servers = (List<ServerData>)Lists.newArrayList();
        this.mc = mcIn;
        this.loadServerList();
    }
    
    public void loadServerList() {
        try {
            this.servers.clear();
            final NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.gameDir, "servers.dat"));
            if (nbttagcompound == null) {
                return;
            }
            final NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
            }
        }
        catch (Exception exception) {
            ServerList.LOGGER.error("Couldn't load server list", (Throwable)exception);
        }
    }
    
    public void saveServerList() {
        try {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final ServerData serverdata : this.servers) {
                nbttaglist.appendTag(serverdata.getNBTCompound());
            }
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.gameDir, "servers.dat"));
        }
        catch (Exception exception) {
            ServerList.LOGGER.error("Couldn't save server list", (Throwable)exception);
        }
    }
    
    public ServerData getServerData(final int index) {
        return this.servers.get(index);
    }
    
    public void removeServerData(final int index) {
        this.servers.remove(index);
    }
    
    public void addServerData(final ServerData server) {
        this.servers.add(server);
    }
    
    public int countServers() {
        return this.servers.size();
    }
    
    public void swapServers(final int pos1, final int pos2) {
        final ServerData serverdata = this.getServerData(pos1);
        this.servers.set(pos1, this.getServerData(pos2));
        this.servers.set(pos2, serverdata);
        this.saveServerList();
    }
    
    public void set(final int index, final ServerData server) {
        this.servers.set(index, server);
    }
    
    public static void saveSingleServer(final ServerData server) {
        final ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();
        for (int i = 0; i < serverlist.countServers(); ++i) {
            final ServerData serverdata = serverlist.getServerData(i);
            if (serverdata.serverName.equals(server.serverName) && serverdata.serverIP.equals(server.serverIP)) {
                serverlist.set(i, server);
                break;
            }
        }
        serverlist.saveServerList();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
