package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "Teams", description = "Prevents Killaura from attacking team mates.", category = ModuleCategory.MISC)
public class Teams extends Module {

    public static BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    public static BoolValue colorValue = new BoolValue("Color", true);
    public static BoolValue armorColorValue = new BoolValue("ArmorColor", false);

    public static boolean isInYourTeam(EntityLivingBase entity) {
        if (mc.thePlayer == null) return false;

        if (scoreboardValue.get() && mc.thePlayer.getTeam() != null && entity.getTeam() != null &&
                mc.thePlayer.getTeam().isSameTeam(entity.getTeam()))
            return true;

        if(armorColorValue.get()){
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            if(mc.thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null){
                ItemStack myHead = mc.thePlayer.inventory.armorInventory[3];
                ItemArmor myItemArmor = (ItemArmor)myHead.getItem();

                ItemStack entityHead = entityPlayer.inventory.armorInventory[3];
                ItemArmor entityItemArmor = (ItemArmor)myHead.getItem();

                if(myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead))
                    return true;
            }
        }

        if (colorValue.get() && mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            return targetName.startsWith("§"+clientName.charAt(1));
        }

        return false;
    }
}
