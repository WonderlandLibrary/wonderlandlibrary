package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerList {
   private final List servers = Lists.newArrayList();
   private static final Logger logger = LogManager.getLogger();
   private final Minecraft mc;

   public static void func_147414_b(ServerData var0) {
      ServerList var1 = new ServerList(Minecraft.getMinecraft());
      var1.loadServerList();

      for(int var2 = 0; var2 < var1.countServers(); ++var2) {
         ServerData var3 = var1.getServerData(var2);
         if (var3.serverName.equals(var0.serverName) && var3.serverIP.equals(var0.serverIP)) {
            var1.func_147413_a(var2, var0);
            break;
         }
      }

      var1.saveServerList();
   }

   public void func_147413_a(int var1, ServerData var2) {
      this.servers.set(var1, var2);
   }

   public void swapServers(int var1, int var2) {
      ServerData var3 = this.getServerData(var1);
      this.servers.set(var1, this.getServerData(var2));
      this.servers.set(var2, var3);
      this.saveServerList();
   }

   public int countServers() {
      return this.servers.size();
   }

   public void loadServerList() {
      try {
         this.servers.clear();
         NBTTagCompound var1 = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
         if (var1 == null) {
            return;
         }

         NBTTagList var2 = var1.getTagList("servers", 10);

         for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
            this.servers.add(ServerData.getServerDataFromNBTCompound(var2.getCompoundTagAt(var3)));
         }
      } catch (Exception var4) {
         logger.error((String)"Couldn't load server list", (Throwable)var4);
      }

   }

   public void addServerData(ServerData var1) {
      this.servers.add(var1);
   }

   public void saveServerList() {
      try {
         NBTTagList var1 = new NBTTagList();
         Iterator var3 = this.servers.iterator();

         while(var3.hasNext()) {
            ServerData var2 = (ServerData)var3.next();
            var1.appendTag(var2.getNBTCompound());
         }

         NBTTagCompound var5 = new NBTTagCompound();
         var5.setTag("servers", var1);
         CompressedStreamTools.safeWrite(var5, new File(this.mc.mcDataDir, "servers.dat"));
      } catch (Exception var4) {
         logger.error((String)"Couldn't save server list", (Throwable)var4);
      }

   }

   public ServerList(Minecraft var1) {
      this.mc = var1;
      this.loadServerList();
   }

   public ServerData getServerData(int var1) {
      return (ServerData)this.servers.get(var1);
   }

   public void removeServerData(int var1) {
      this.servers.remove(var1);
   }
}
