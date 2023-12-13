// 
// Decompiled by Procyon v0.5.30
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
    private static final Logger logger;
    private final Minecraft mc;
    private final List servers;
    private static final String __OBFID = "CL_00000891";
    
    public ServerList(final Minecraft mcIn) {
        this.servers = Lists.newArrayList();
        this.mc = mcIn;
        this.loadServerList();
    }
    
    public void loadServerList() {
        try {
            this.servers.clear();
            final NBTTagCompound var1 = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
            if (var1 == null) {
                return;
            }
            final NBTTagList var2 = var1.getTagList("servers", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                this.servers.add(ServerData.getServerDataFromNBTCompound(var2.getCompoundTagAt(var3)));
            }
        }
        catch (Exception var4) {
            ServerList.logger.error("Couldn't load server list", (Throwable)var4);
        }
    }
    
    public void saveServerList() {
        try {
            final NBTTagList var1 = new NBTTagList();
            for (final ServerData var3 : this.servers) {
                var1.appendTag(var3.getNBTCompound());
            }
            final NBTTagCompound var4 = new NBTTagCompound();
            var4.setTag("servers", var1);
            CompressedStreamTools.safeWrite(var4, new File(this.mc.mcDataDir, "servers.dat"));
        }
        catch (Exception var5) {
            ServerList.logger.error("Couldn't save server list", (Throwable)var5);
        }
    }
    
    public ServerData getServerData(final int p_78850_1_) {
        return this.servers.get(p_78850_1_);
    }
    
    public void removeServerData(final int p_78851_1_) {
        this.servers.remove(p_78851_1_);
    }
    
    public void addServerData(final ServerData p_78849_1_) {
        this.servers.add(p_78849_1_);
    }
    
    public int countServers() {
        return this.servers.size();
    }
    
    public void swapServers(final int p_78857_1_, final int p_78857_2_) {
        final ServerData var3 = this.getServerData(p_78857_1_);
        this.servers.set(p_78857_1_, this.getServerData(p_78857_2_));
        this.servers.set(p_78857_2_, var3);
        this.saveServerList();
    }
    
    public void func_147413_a(final int p_147413_1_, final ServerData p_147413_2_) {
        this.servers.set(p_147413_1_, p_147413_2_);
    }
    
    public static void func_147414_b(final ServerData p_147414_0_) {
        final ServerList var1 = new ServerList(Minecraft.getMinecraft());
        var1.loadServerList();
        for (int var2 = 0; var2 < var1.countServers(); ++var2) {
            final ServerData var3 = var1.getServerData(var2);
            if (var3.serverName.equals(p_147414_0_.serverName) && var3.serverIP.equals(p_147414_0_.serverIP)) {
                var1.func_147413_a(var2, p_147414_0_);
                break;
            }
        }
        var1.saveServerList();
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
