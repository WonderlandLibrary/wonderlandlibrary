package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.MathHelper;

public class MapGenMineshaft extends MapGenStructure {
   private double field_82673_e = 0.004D;

   public String getStructureName() {
      return "Mineshaft";
   }

   public MapGenMineshaft(Map var1) {
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (((String)var2.getKey()).equals("chance")) {
            this.field_82673_e = MathHelper.parseDoubleWithDefault((String)var2.getValue(), this.field_82673_e);
         }
      }

   }

   public MapGenMineshaft() {
   }

   protected StructureStart getStructureStart(int var1, int var2) {
      return new StructureMineshaftStart(this.worldObj, this.rand, var1, var2);
   }

   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(var1), Math.abs(var2));
   }
}
