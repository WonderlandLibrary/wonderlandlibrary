package net.minecraft.stats;

import net.minecraft.util.IChatComponent;

public class StatBasic extends StatBase {
  public StatBasic(String statIdIn, IChatComponent statNameIn, IStatType typeIn) {
    super(statIdIn, statNameIn, typeIn);
  }
  
  public StatBasic(String statIdIn, IChatComponent statNameIn) {
    super(statIdIn, statNameIn);
  }
  
  public StatBase registerStat() {
    super.registerStat();
    StatList.generalStats.add(this);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\stats\StatBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */