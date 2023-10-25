package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatFileWriter {
  protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();
  
  public boolean hasAchievementUnlocked(Achievement achievementIn) {
    return (readStat(achievementIn) > 0);
  }
  
  public boolean canUnlockAchievement(Achievement achievementIn) {
    return (achievementIn.parentAchievement == null || hasAchievementUnlocked(achievementIn.parentAchievement));
  }
  
  public int func_150874_c(Achievement p_150874_1_) {
    if (hasAchievementUnlocked(p_150874_1_))
      return 0; 
    int i = 0;
    for (Achievement achievement = p_150874_1_.parentAchievement; achievement != null && !hasAchievementUnlocked(achievement); i++)
      achievement = achievement.parentAchievement; 
    return i;
  }
  
  public void increaseStat(EntityPlayer player, StatBase stat, int amount) {
    if (!stat.isAchievement() || canUnlockAchievement((Achievement)stat))
      unlockAchievement(player, stat, readStat(stat) + amount); 
  }
  
  public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
    TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);
    if (tupleintjsonserializable == null) {
      tupleintjsonserializable = new TupleIntJsonSerializable();
      this.statsData.put(statIn, tupleintjsonserializable);
    } 
    tupleintjsonserializable.setIntegerValue(p_150873_3_);
  }
  
  public int readStat(StatBase stat) {
    TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
    return (tupleintjsonserializable == null) ? 0 : tupleintjsonserializable.getIntegerValue();
  }
  
  public <T extends IJsonSerializable> T func_150870_b(StatBase p_150870_1_) {
    TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150870_1_);
    return (tupleintjsonserializable != null) ? (T)tupleintjsonserializable.getJsonSerializableValue() : null;
  }
  
  public <T extends IJsonSerializable> T func_150872_a(StatBase p_150872_1_, T p_150872_2_) {
    TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150872_1_);
    if (tupleintjsonserializable == null) {
      tupleintjsonserializable = new TupleIntJsonSerializable();
      this.statsData.put(p_150872_1_, tupleintjsonserializable);
    } 
    tupleintjsonserializable.setJsonSerializableValue((IJsonSerializable)p_150872_2_);
    return p_150872_2_;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\stats\StatFileWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */