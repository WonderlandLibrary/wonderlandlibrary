package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.DamageUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;


public class ExtraTooltips extends Module {
  public Setting<Boolean> TrueDura = register(new Setting<>("TrueDura", true));
  public Setting<Boolean> NonDuraitems = register(new Setting<Boolean>("All Item Dura",true, v -> TrueDura.getValue()));
  public Setting<Boolean> Alwasydura = register(new Setting<Boolean>("Always On",true, v -> TrueDura.getValue()));

  public ExtraTooltips() {
    super("ExtraTooltips", "Adds More ToolTips Options ", Category.Render, false, false);
  }

  @SubscribeEvent
  public void itemToolTip(ItemTooltipEvent event) {
    if (this.TrueDura.getValue()) {
      ItemStack stack = event.getItemStack();
      int max = stack.getMaxDamage();
      List<String> toolTip = event.getToolTip();


      if (this.NonDuraitems.getValue()) {
        int damage;
        damage = stack.getItemDamage();
        long count = (long) max - (long) damage;
        if (max == 0) {
          toolTip.add(TextFormatting.GRAY + "Durability: " + Long.toString(count) + " / " + Long.toString(max));
        }
      }
      if (Alwasydura.getValue()) {
        int damage;
        damage = stack.getItemDamage();
        long count = (long) max - (long) damage;
        if (count == max && max != 0) {
          toolTip.add(TextFormatting.GRAY + "Durability: " + Long.toString(count) + " / " + Long.toString(max));
        }
      }

      if (stack.isEmpty() || max <= 0) return;
      if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("Unbreakable")) {
        toolTip.add("");
        toolTip.add(TextFormatting.GRAY + "Durability: " + TextFormatting.GOLD + "Tag.Unbreakable ");
        return;
      }
      int durabilty = toolTip.indexOf("Durability:");

      int damage;
      NBTTagCompound tag = stack.getTagCompound();
      if (tag != null && tag instanceof DamageUtil.DamageTagUtil) {
        damage = ((DamageUtil.DamageTagUtil) tag).getTrueDamage();
      } else damage = stack.getItemDamage();
      long count = (long) max - (long) damage;
      if (count > max) {
        toolTip.set(durabilty, TextFormatting.GRAY + "Durability: " + TextFormatting.GOLD + Long.toString(count) + TextFormatting.GRAY + " / " + Long.toString(max));
      }
    }
  }
}
