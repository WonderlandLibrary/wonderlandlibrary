package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsFile extends StatFileWriter {
   private final MinecraftServer mcServer;
   private final Set field_150888_e = Sets.newHashSet();
   private int field_150885_f = -300;
   private boolean field_150886_g = false;
   private static final Logger logger = LogManager.getLogger();
   private final File statsFile;

   public boolean func_150879_e() {
      return this.field_150886_g;
   }

   public void readStatFile() {
      if (this.statsFile.isFile()) {
         try {
            this.statsData.clear();
            this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
         } catch (IOException var3) {
            logger.error((String)("Couldn't read statistics file " + this.statsFile), (Throwable)var3);
         } catch (JsonParseException var4) {
            logger.error((String)("Couldn't parse statistics file " + this.statsFile), (Throwable)var4);
         }
      }

   }

   public StatisticsFile(MinecraftServer var1, File var2) {
      this.mcServer = var1;
      this.statsFile = var2;
   }

   public void sendAchievements(EntityPlayerMP var1) {
      HashMap var2 = Maps.newHashMap();
      Iterator var4 = AchievementList.achievementList.iterator();

      while(var4.hasNext()) {
         Achievement var3 = (Achievement)var4.next();
         if (this.hasAchievementUnlocked(var3)) {
            var2.put(var3, this.readStat(var3));
            this.field_150888_e.remove(var3);
         }
      }

      var1.playerNetServerHandler.sendPacket(new S37PacketStatistics(var2));
   }

   public static String dumpJson(Map var0) {
      JsonObject var1 = new JsonObject();
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (((TupleIntJsonSerializable)var2.getValue()).getJsonSerializableValue() != null) {
            JsonObject var4 = new JsonObject();
            var4.addProperty("value", (Number)((TupleIntJsonSerializable)var2.getValue()).getIntegerValue());

            try {
               var4.add("progress", ((TupleIntJsonSerializable)var2.getValue()).getJsonSerializableValue().getSerializableElement());
            } catch (Throwable var6) {
               logger.warn("Couldn't save statistic " + ((StatBase)var2.getKey()).getStatName() + ": error serializing progress", var6);
            }

            var1.add(((StatBase)var2.getKey()).statId, var4);
         } else {
            var1.addProperty(((StatBase)var2.getKey()).statId, (Number)((TupleIntJsonSerializable)var2.getValue()).getIntegerValue());
         }
      }

      return var1.toString();
   }

   public void func_150876_a(EntityPlayerMP var1) {
      int var2 = this.mcServer.getTickCounter();
      HashMap var3 = Maps.newHashMap();
      if (this.field_150886_g || var2 - this.field_150885_f > 300) {
         this.field_150885_f = var2;
         Iterator var5 = this.func_150878_c().iterator();

         while(var5.hasNext()) {
            StatBase var4 = (StatBase)var5.next();
            var3.put(var4, this.readStat(var4));
         }
      }

      var1.playerNetServerHandler.sendPacket(new S37PacketStatistics(var3));
   }

   public Set func_150878_c() {
      HashSet var1 = Sets.newHashSet((Iterable)this.field_150888_e);
      this.field_150888_e.clear();
      this.field_150886_g = false;
      return var1;
   }

   public void unlockAchievement(EntityPlayer var1, StatBase var2, int var3) {
      int var4 = var2.isAchievement() ? this.readStat(var2) : 0;
      super.unlockAchievement(var1, var2, var3);
      this.field_150888_e.add(var2);
      if (var2.isAchievement() && var4 == 0 && var3 > 0) {
         this.field_150886_g = true;
         if (this.mcServer.isAnnouncingPlayerAchievements()) {
            this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement", new Object[]{var1.getDisplayName(), var2.func_150955_j()}));
         }
      }

      if (var2.isAchievement() && var4 > 0 && var3 == 0) {
         this.field_150886_g = true;
         if (this.mcServer.isAnnouncingPlayerAchievements()) {
            this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.achievement.taken", new Object[]{var1.getDisplayName(), var2.func_150955_j()}));
         }
      }

   }

   public void saveStatFile() {
      try {
         FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
      } catch (IOException var2) {
         logger.error((String)"Couldn't save stats", (Throwable)var2);
      }

   }

   public Map parseJson(String var1) {
      JsonElement var2 = (new JsonParser()).parse(var1);
      if (!var2.isJsonObject()) {
         return Maps.newHashMap();
      } else {
         JsonObject var3 = var2.getAsJsonObject();
         HashMap var4 = Maps.newHashMap();
         Iterator var6 = var3.entrySet().iterator();

         while(true) {
            while(var6.hasNext()) {
               Entry var5 = (Entry)var6.next();
               StatBase var7 = StatList.getOneShotStat((String)var5.getKey());
               if (var7 != null) {
                  TupleIntJsonSerializable var8 = new TupleIntJsonSerializable();
                  if (((JsonElement)var5.getValue()).isJsonPrimitive() && ((JsonElement)var5.getValue()).getAsJsonPrimitive().isNumber()) {
                     var8.setIntegerValue(((JsonElement)var5.getValue()).getAsInt());
                  } else if (((JsonElement)var5.getValue()).isJsonObject()) {
                     JsonObject var9 = ((JsonElement)var5.getValue()).getAsJsonObject();
                     if (var9.has("value") && var9.get("value").isJsonPrimitive() && var9.get("value").getAsJsonPrimitive().isNumber()) {
                        var8.setIntegerValue(var9.getAsJsonPrimitive("value").getAsInt());
                     }

                     if (var9.has("progress") && var7.func_150954_l() != null) {
                        try {
                           Constructor var10 = var7.func_150954_l().getConstructor();
                           IJsonSerializable var11 = (IJsonSerializable)var10.newInstance();
                           var11.fromJson(var9.get("progress"));
                           var8.setJsonSerializableValue(var11);
                        } catch (Throwable var13) {
                           logger.warn("Invalid statistic progress in " + this.statsFile, var13);
                        }
                     }
                  }

                  var4.put(var7, var8);
               } else {
                  logger.warn("Invalid statistic in " + this.statsFile + ": Don't know what " + (String)var5.getKey() + " is");
               }
            }

            return var4;
         }
      }
   }

   public void func_150877_d() {
      Iterator var2 = this.statsData.keySet().iterator();

      while(var2.hasNext()) {
         StatBase var1 = (StatBase)var2.next();
         this.field_150888_e.add(var1);
      }

   }
}
