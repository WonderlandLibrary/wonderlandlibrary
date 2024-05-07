package com.masterof13fps.features.modules.impl.combat;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.time.TimeHelper;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;

import com.masterof13fps.features.modules.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "AutoArmor", category = Category.COMBAT, description = "You automatically equip yourself with the best armor available")
public class AutoArmor extends Module {

    public AutoArmor() {
        Client.main().setMgr().addSetting(new Setting("Delay", this, 100, 0, 500, true));
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    TimeHelper time = new TimeHelper();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            double delay = Client.main().setMgr().settingByName("Delay", this).getCurrentValue();
            if (state()) {
                if (mc.thePlayer.capabilities.isCreativeMode
                        || mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory))
                    return;

                int[] bestArmor1 = new int[4];
                for (int i = 0; i < bestArmor1.length; i++)
                    bestArmor1[i] = -1;
                for (int i = 0; i < 36; i++) {
                    ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                        ItemArmor armor = (ItemArmor) itemstack.getItem();
                        if (armor.damageReduceAmount > bestArmor1[3 - armor.armorType])
                            bestArmor1[3 - armor.armorType] = i;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(i);
                    ItemArmor currentArmor;
                    if (itemstack != null && itemstack.getItem() instanceof ItemArmor)
                        currentArmor = (ItemArmor) itemstack.getItem();
                    else
                        currentArmor = null;
                    ItemArmor bestArmor;
                    try {
                        bestArmor = (ItemArmor) mc.thePlayer.inventory.getStackInSlot(bestArmor1[i]).getItem();
                    } catch (Exception e) {
                        bestArmor = null;
                    }
                    if (bestArmor != null
                            && (currentArmor == null || bestArmor.damageReduceAmount > currentArmor.damageReduceAmount))
                        if (mc.thePlayer.inventory.getFirstEmptyStack() != -1 || currentArmor == null) {
                            if (time.hasReached((long) delay)) {
                                mc.playerController.windowClick(0, 8 - i, 0, 1, mc.thePlayer);
                                mc.playerController.windowClick(0,
                                        bestArmor1[i] < 9 ? 36 + bestArmor1[i] : bestArmor1[i], 0, 1,
                                        Minecraft.mc().thePlayer);
                                time.reset();
                            }
                        }
                }
            }
        }
    }
}