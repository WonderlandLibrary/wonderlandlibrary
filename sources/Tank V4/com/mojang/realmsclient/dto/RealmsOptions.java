package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;

public class RealmsOptions {
   public Boolean pvp;
   public Boolean spawnAnimals;
   public Boolean spawnMonsters;
   public Boolean spawnNPCs;
   public Integer spawnProtection;
   public Boolean commandBlocks;
   public Boolean forceGameMode;
   public Integer difficulty;
   public Integer gameMode;
   private static boolean forceGameModeDefault = false;
   private static boolean pvpDefault = true;
   private static boolean spawnAnimalsDefault = true;
   private static boolean spawnMonstersDefault = true;
   private static boolean spawnNPCsDefault = true;
   private static int spawnProtectionDefault = 0;
   private static boolean commandBlocksDefault = false;
   private static int difficultyDefault = 1;
   private static int gameModeDefault = 0;

   public RealmsOptions(Boolean var1, Boolean var2, Boolean var3, Boolean var4, Integer var5, Boolean var6, Integer var7, Integer var8, Boolean var9) {
      this.pvp = var1;
      this.spawnAnimals = var2;
      this.spawnMonsters = var3;
      this.spawnNPCs = var4;
      this.spawnProtection = var5;
      this.commandBlocks = var6;
      this.difficulty = var7;
      this.gameMode = var8;
      this.forceGameMode = var9;
   }

   public static RealmsOptions getDefaults() {
      return new RealmsOptions(pvpDefault, spawnAnimalsDefault, spawnMonstersDefault, spawnNPCsDefault, spawnProtectionDefault, commandBlocksDefault, difficultyDefault, gameModeDefault, forceGameModeDefault);
   }

   public static RealmsOptions parse(JsonObject var0) {
      return new RealmsOptions(JsonUtils.getBooleanOr("pvp", var0, pvpDefault), JsonUtils.getBooleanOr("spawnAnimals", var0, spawnAnimalsDefault), JsonUtils.getBooleanOr("spawnMonsters", var0, spawnMonstersDefault), JsonUtils.getBooleanOr("spawnNPCs", var0, spawnNPCsDefault), JsonUtils.getIntOr("spawnProtection", var0, spawnProtectionDefault), JsonUtils.getBooleanOr("commandBlocks", var0, commandBlocksDefault), JsonUtils.getIntOr("difficulty", var0, difficultyDefault), JsonUtils.getIntOr("gameMode", var0, gameModeDefault), JsonUtils.getBooleanOr("forceGameMode", var0, forceGameModeDefault));
   }

   public String toJson() {
      JsonObject var1 = new JsonObject();
      if (this.pvp != pvpDefault) {
         var1.addProperty("pvp", this.pvp);
      }

      if (this.spawnAnimals != spawnAnimalsDefault) {
         var1.addProperty("spawnAnimals", this.spawnAnimals);
      }

      if (this.spawnMonsters != spawnMonstersDefault) {
         var1.addProperty("spawnMonsters", this.spawnMonsters);
      }

      if (this.spawnNPCs != spawnNPCsDefault) {
         var1.addProperty("spawnNPCs", this.spawnNPCs);
      }

      if (this.spawnProtection != spawnProtectionDefault) {
         var1.addProperty("spawnProtection", (Number)this.spawnProtection);
      }

      if (this.commandBlocks != commandBlocksDefault) {
         var1.addProperty("commandBlocks", this.commandBlocks);
      }

      if (this.difficulty != difficultyDefault) {
         var1.addProperty("difficulty", (Number)this.difficulty);
      }

      if (this.gameMode != gameModeDefault) {
         var1.addProperty("gameMode", (Number)this.gameMode);
      }

      if (this.forceGameMode != forceGameModeDefault) {
         var1.addProperty("forceGameMode", this.forceGameMode);
      }

      return var1.toString();
   }
}
