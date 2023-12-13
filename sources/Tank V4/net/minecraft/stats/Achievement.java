package net.minecraft.stats;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class Achievement extends StatBase {
   public final Achievement parentAchievement;
   public final int displayRow;
   public final ItemStack theItemStack;
   private IStatStringFormat statStringFormatter;
   private boolean isSpecial;
   public final int displayColumn;
   private final String achievementDescription;

   public StatBase initIndependentStat() {
      return this.initIndependentStat();
   }

   public boolean isAchievement() {
      return true;
   }

   public IChatComponent getStatName() {
      IChatComponent var1 = super.getStatName();
      var1.getChatStyle().setColor(this.getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
      return var1;
   }

   public StatBase func_150953_b(Class var1) {
      return this.func_150953_b(var1);
   }

   public boolean getSpecial() {
      return this.isSpecial;
   }

   public Achievement(String var1, String var2, int var3, int var4, ItemStack var5, Achievement var6) {
      super(var1, new ChatComponentTranslation("achievement." + var2, new Object[0]));
      this.theItemStack = var5;
      this.achievementDescription = "achievement." + var2 + ".desc";
      this.displayColumn = var3;
      this.displayRow = var4;
      if (var3 < AchievementList.minDisplayColumn) {
         AchievementList.minDisplayColumn = var3;
      }

      if (var4 < AchievementList.minDisplayRow) {
         AchievementList.minDisplayRow = var4;
      }

      if (var3 > AchievementList.maxDisplayColumn) {
         AchievementList.maxDisplayColumn = var3;
      }

      if (var4 > AchievementList.maxDisplayRow) {
         AchievementList.maxDisplayRow = var4;
      }

      this.parentAchievement = var6;
   }

   public Achievement setStatStringFormatter(IStatStringFormat var1) {
      this.statStringFormatter = var1;
      return this;
   }

   public Achievement func_150953_b(Class var1) {
      return (Achievement)super.func_150953_b(var1);
   }

   public Achievement(String var1, String var2, int var3, int var4, Block var5, Achievement var6) {
      this(var1, var2, var3, var4, new ItemStack(var5), var6);
   }

   public Achievement initIndependentStat() {
      this.isIndependent = true;
      return this;
   }

   public String getDescription() {
      return this.statStringFormatter != null ? this.statStringFormatter.formatString(StatCollector.translateToLocal(this.achievementDescription)) : StatCollector.translateToLocal(this.achievementDescription);
   }

   public Achievement registerStat() {
      super.registerStat();
      AchievementList.achievementList.add(this);
      return this;
   }

   public StatBase registerStat() {
      return this.registerStat();
   }

   public Achievement(String var1, String var2, int var3, int var4, Item var5, Achievement var6) {
      this(var1, var2, var3, var4, new ItemStack(var5), var6);
   }

   public Achievement setSpecial() {
      this.isSpecial = true;
      return this;
   }
}
