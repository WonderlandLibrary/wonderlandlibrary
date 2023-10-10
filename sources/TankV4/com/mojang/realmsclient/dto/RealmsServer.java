package com.mojang.realmsclient.dto;

import com.google.common.collect.ComparisonChain;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.realms.RealmsServerPing;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServer extends ValueObject {
   private static final Logger LOGGER = LogManager.getLogger();
   public long id;
   public String remoteSubscriptionId;
   public String name;
   public String motd;
   public RealmsServer.State state;
   public String owner;
   public String ownerUUID;
   public List players;
   public RealmsOptions options;
   public String ip;
   public boolean expired;
   public int daysLeft;
   public RealmsServer.WorldType worldType;
   public int protocol;
   public String status = "";
   public RealmsServerPing serverPing = new RealmsServerPing();

   public String getMotd() {
      return this.motd;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setMotd(String var1) {
      this.motd = var1;
   }

   public void latestStatFrom(RealmsServer var1) {
      this.status = var1.status;
      this.protocol = var1.protocol;
      this.serverPing.nrOfPlayers = var1.serverPing.nrOfPlayers;
      this.serverPing.lastPingSnapshot = var1.serverPing.lastPingSnapshot;
   }

   public static RealmsServer parse(JsonObject var0) {
      RealmsServer var1 = new RealmsServer();

      try {
         var1.id = JsonUtils.getLongOr("id", var0, -1L);
         var1.remoteSubscriptionId = JsonUtils.getStringOr("remoteSubscriptionId", var0, (String)null);
         var1.name = JsonUtils.getStringOr("name", var0, (String)null);
         var1.motd = JsonUtils.getStringOr("motd", var0, (String)null);
         var1.state = getState(JsonUtils.getStringOr("state", var0, RealmsServer.State.CLOSED.name()));
         var1.owner = JsonUtils.getStringOr("owner", var0, (String)null);
         if (var0.get("players") != null && var0.get("players").isJsonArray()) {
            var1.players = parseInvited(var0.get("players").getAsJsonArray());
            sortInvited(var1);
         } else {
            var1.players = new ArrayList();
         }

         var1.daysLeft = JsonUtils.getIntOr("daysLeft", var0, 0);
         var1.ip = JsonUtils.getStringOr("ip", var0, (String)null);
         var1.expired = JsonUtils.getBooleanOr("expired", var0, false);
         var1.worldType = getWorldType(JsonUtils.getStringOr("worldType", var0, RealmsServer.WorldType.NORMAL.name()));
         var1.ownerUUID = JsonUtils.getStringOr("ownerUUID", var0, "");
         if (var0.get("options") != null && !var0.get("options").isJsonNull()) {
            JsonParser var2 = new JsonParser();
            JsonElement var3 = var2.parse(var0.get("options").getAsString());
            if (var3 == null) {
               var1.options = RealmsOptions.getDefaults();
            } else {
               var1.options = RealmsOptions.parse(var3.getAsJsonObject());
            }
         } else {
            var1.options = RealmsOptions.getDefaults();
         }
      } catch (Exception var4) {
         LOGGER.error("Could not parse McoServer: " + var4.getMessage());
      }

      return var1;
   }

   private static void sortInvited(RealmsServer var0) {
      Collections.sort(var0.players, new Comparator() {
         public int compare(PlayerInfo var1, PlayerInfo var2) {
            return var1.getName().toLowerCase().compareTo(var2.getName().toLowerCase());
         }

         public int compare(Object var1, Object var2) {
            return this.compare((PlayerInfo)var1, (PlayerInfo)var2);
         }
      });
   }

   private static List parseInvited(JsonArray var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         JsonElement var3 = (JsonElement)var2.next();

         try {
            JsonObject var4 = var3.getAsJsonObject();
            PlayerInfo var5 = new PlayerInfo();
            var5.setName(JsonUtils.getStringOr("name", var4, (String)null));
            var5.setUuid(JsonUtils.getStringOr("uuid", var4, (String)null));
            var5.setOperator(JsonUtils.getBooleanOr("operator", var4, false));
            var1.add(var5);
         } catch (Exception var6) {
         }
      }

      return var1;
   }

   public static RealmsServer parse(String var0) {
      RealmsServer var1 = new RealmsServer();

      try {
         JsonParser var2 = new JsonParser();
         JsonObject var3 = var2.parse(var0).getAsJsonObject();
         var1 = parse(var3);
      } catch (Exception var4) {
         LOGGER.error("Could not parse McoServer: " + var4.getMessage());
      }

      return var1;
   }

   private static RealmsServer.State getState(String var0) {
      try {
         return RealmsServer.State.valueOf(var0);
      } catch (Exception var2) {
         return RealmsServer.State.CLOSED;
      }
   }

   private static RealmsServer.WorldType getWorldType(String var0) {
      try {
         return RealmsServer.WorldType.valueOf(var0);
      } catch (Exception var2) {
         return RealmsServer.WorldType.NORMAL;
      }
   }

   public boolean shouldPing(long var1) {
      return var1 - this.serverPing.lastPingSnapshot >= 6000L;
   }

   public int hashCode() {
      return (new HashCodeBuilder(17, 37)).append(this.id).append((Object)this.name).append((Object)this.motd).append((Object)this.state).append((Object)this.owner).append(this.expired).toHashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (var1.getClass() != this.getClass()) {
         return false;
      } else {
         RealmsServer var2 = (RealmsServer)var1;
         return (new EqualsBuilder()).append(this.id, var2.id).append((Object)this.name, (Object)var2.name).append((Object)this.motd, (Object)var2.motd).append((Object)this.state, (Object)var2.state).append((Object)this.owner, (Object)var2.owner).append(this.expired, var2.expired).append((Object)this.worldType, (Object)this.worldType).isEquals();
      }
   }

   public RealmsServer clone() {
      RealmsServer var1 = new RealmsServer();
      var1.id = this.id;
      var1.remoteSubscriptionId = this.remoteSubscriptionId;
      var1.name = this.name;
      var1.motd = this.motd;
      var1.state = this.state;
      var1.owner = this.owner;
      var1.players = this.players;
      var1.options = new RealmsOptions(this.options.pvp, this.options.spawnAnimals, this.options.spawnMonsters, this.options.spawnNPCs, this.options.spawnProtection, this.options.commandBlocks, this.options.difficulty, this.options.gameMode, this.options.forceGameMode);
      var1.ip = this.ip;
      var1.expired = this.expired;
      var1.daysLeft = this.daysLeft;
      var1.protocol = this.protocol;
      var1.status = this.status;
      var1.serverPing = new RealmsServerPing();
      var1.serverPing.nrOfPlayers = this.serverPing.nrOfPlayers;
      var1.serverPing.lastPingSnapshot = this.serverPing.lastPingSnapshot;
      var1.worldType = this.worldType;
      var1.ownerUUID = this.ownerUUID;
      return var1;
   }

   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   public static enum WorldType {
      NORMAL,
      MINIGAME,
      ADVENTUREMAP;

      private static final RealmsServer.WorldType[] $VALUES = new RealmsServer.WorldType[]{NORMAL, MINIGAME, ADVENTUREMAP};
   }

   public static enum State {
      CLOSED,
      OPEN,
      ADMIN_LOCK,
      UNINITIALIZED;

      private static final RealmsServer.State[] $VALUES = new RealmsServer.State[]{CLOSED, OPEN, ADMIN_LOCK, UNINITIALIZED};
   }

   public static class McoServerComparator implements Comparator {
      private final String refOwner;

      public McoServerComparator(String var1) {
         this.refOwner = var1;
      }

      public int compare(RealmsServer var1, RealmsServer var2) {
         return ComparisonChain.start().compareTrueFirst(var1.state.equals(RealmsServer.State.UNINITIALIZED), var2.state.equals(RealmsServer.State.UNINITIALIZED)).compareFalseFirst(var1.expired, var2.expired).compareTrueFirst(var1.owner.equals(this.refOwner), var2.owner.equals(this.refOwner)).compareTrueFirst(var1.state.equals(RealmsServer.State.OPEN), var2.state.equals(RealmsServer.State.OPEN)).compare(var1.id, var2.id).result();
      }

      public int compare(Object var1, Object var2) {
         return this.compare((RealmsServer)var1, (RealmsServer)var2);
      }
   }
}
