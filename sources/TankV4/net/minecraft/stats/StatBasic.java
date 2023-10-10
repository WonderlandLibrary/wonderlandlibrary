package net.minecraft.stats;

import net.minecraft.util.IChatComponent;

public class StatBasic extends StatBase {
   public StatBasic(String var1, IChatComponent var2) {
      super(var1, var2);
   }

   public StatBase registerStat() {
      super.registerStat();
      StatList.generalStats.add(this);
      return this;
   }

   public StatBasic(String var1, IChatComponent var2, IStatType var3) {
      super(var1, var2, var3);
   }
}
