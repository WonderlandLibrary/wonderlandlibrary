// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.multiplayer;

import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.File;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class ServerList
{
    private static final Logger logger;
    private final Minecraft mc;
    private final List<ServerData> servers;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ServerList(final Minecraft mcIn) {
        this.servers = Lists.newArrayList();
        this.mc = mcIn;
        this.loadServerList();
    }
    
    public void loadServerList() {
        try {
            this.servers.clear();
            final NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
            if (nbttagcompound == null) {
                return;
            }
            final NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
            }
        }
        catch (final Exception exception) {
            ServerList.logger.error("Couldn't load server list", (Throwable)exception);
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
            CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir, "servers.dat"));
        }
        catch (final Exception exception) {
            ServerList.logger.error("Couldn't save server list", (Throwable)exception);
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
    
    public void swapServers(final int p_78857_1_, final int p_78857_2_) {
        final ServerData serverdata = this.getServerData(p_78857_1_);
        this.servers.set(p_78857_1_, this.getServerData(p_78857_2_));
        this.servers.set(p_78857_2_, serverdata);
        this.saveServerList();
    }
    
    public void func_147413_a(final int index, final ServerData server) {
        this.servers.set(index, server);
    }
    
    public static void func_147414_b(final ServerData p_147414_0_) {
        final ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();
        for (int i = 0; i < serverlist.countServers(); ++i) {
            final ServerData serverdata = serverlist.getServerData(i);
            if (serverdata.serverName.equals(p_147414_0_.serverName) && serverdata.serverIP.equals(p_147414_0_.serverIP)) {
                serverlist.func_147413_a(i, p_147414_0_);
                break;
            }
        }
        serverlist.saveServerList();
    }
}
