package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
   private static final String __OBFID = "CL_00000136";
   private TreeMap theGameRules = new TreeMap();

   public String[] getRules() {
      Set var1 = this.theGameRules.keySet();
      return (String[])var1.toArray(new String[var1.size()]);
   }

   public boolean getBoolean(String var1) {
      GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(var1);
      return var2 != null ? var2.getBoolean() : false;
   }

   public int getInt(String var1) {
      GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(var1);
      return var2 != null ? var2.getInt() : 0;
   }

   public NBTTagCompound writeToNBT() {
      NBTTagCompound var1 = new NBTTagCompound();
      Iterator var3 = this.theGameRules.keySet().iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         GameRules.Value var4 = (GameRules.Value)this.theGameRules.get(var2);
         var1.setString((String)var2, var4.getString());
      }

      return var1;
   }

   public boolean hasRule(String var1) {
      return this.theGameRules.containsKey(var1);
   }

   public void setOrCreateGameRule(String var1, String var2) {
      GameRules.Value var3 = (GameRules.Value)this.theGameRules.get(var1);
      if (var3 != null) {
         var3.setValue(var2);
      } else {
         this.addGameRule(var1, var2, GameRules.ValueType.ANY_VALUE);
      }

   }

   public void readFromNBT(NBTTagCompound var1) {
      Iterator var3 = var1.getKeySet().iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         String var4 = var1.getString(var2);
         this.setOrCreateGameRule(var2, var4);
      }

   }

   public GameRules() {
      this.addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doEntityDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
      this.addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
      this.addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
   }

   public String getString(String var1) {
      GameRules.Value var2 = (GameRules.Value)this.theGameRules.get(var1);
      return var2 != null ? var2.getString() : "";
   }

   public void addGameRule(String var1, String var2, GameRules.ValueType var3) {
      this.theGameRules.put(var1, new GameRules.Value(var2, var3));
   }

   public boolean areSameType(String var1, GameRules.ValueType var2) {
      GameRules.Value var3 = (GameRules.Value)this.theGameRules.get(var1);
      return var3 != null && (var3.getType() == var2 || var2 == GameRules.ValueType.ANY_VALUE);
   }

   public static enum ValueType {
      private static final GameRules.ValueType[] ENUM$VALUES = new GameRules.ValueType[]{ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE};
      NUMERICAL_VALUE("NUMERICAL_VALUE", 2);

      private static final GameRules.ValueType[] $VALUES = new GameRules.ValueType[]{ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE};
      private static final String __OBFID = "CL_00002151";
      BOOLEAN_VALUE("BOOLEAN_VALUE", 1),
      ANY_VALUE("ANY_VALUE", 0);

      private ValueType(String var3, int var4) {
      }
   }

   static class Value {
      private int valueInteger;
      private final GameRules.ValueType type;
      private double valueDouble;
      private static final String __OBFID = "CL_00000137";
      private String valueString;
      private boolean valueBoolean;

      public int getInt() {
         return this.valueInteger;
      }

      public boolean getBoolean() {
         return this.valueBoolean;
      }

      public void setValue(String var1) {
         this.valueString = var1;
         if (var1 != null) {
            if (var1.equals("false")) {
               this.valueBoolean = false;
               return;
            }

            if (var1.equals("true")) {
               this.valueBoolean = true;
               return;
            }
         }

         this.valueBoolean = Boolean.parseBoolean(var1);
         this.valueInteger = this.valueBoolean ? 1 : 0;

         try {
            this.valueInteger = Integer.parseInt(var1);
         } catch (NumberFormatException var4) {
         }

         try {
            this.valueDouble = Double.parseDouble(var1);
         } catch (NumberFormatException var3) {
         }

      }

      public Value(String var1, GameRules.ValueType var2) {
         this.type = var2;
         this.setValue(var1);
      }

      public String getString() {
         return this.valueString;
      }

      public GameRules.ValueType getType() {
         return this.type;
      }
   }
}
