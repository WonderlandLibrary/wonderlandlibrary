package me.aidanmees.trivia.client.AntiCrash;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class AntiCrash
{
  public ArrayList<AntiCrashCause> causes = new ArrayList();
  
  public boolean isCrashable(ItemStack item)
  {
    return (item.hasTagCompound()) && (item.getTagCompound().hasKey("crash"));
  }
  
  public void registerCrash(AntiCrashCause cause)
  {
    this.causes.remove(cause);
    this.causes.add(cause);
  }
  
  public boolean hasCrash()
  {
    boolean hasCrash = false;
    for (int i = 0; i < this.causes.size(); i++)
    {
      AntiCrashCause cause = (AntiCrashCause)this.causes.get(i);
      if (System.currentTimeMillis() - cause.time < 3000L) {
        hasCrash = true;
      } else {
        this.causes.remove(i);
      }
    }
    return hasCrash;
  }
  
  public ArrayList<AntiCrashCause> getCauses()
  {
    return this.causes;
  }
}
