package de.theBest.MythicX.modules.player;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import de.theBest.MythicX.utils.TimeUtils;
import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.inventory.ContainerChest;

import java.awt.*;

public class ChestStealer extends Module {

    public ChestStealer() {
        super("Chest Stealer", Type.Player, 0, Category.PLAYER, Color.green, "Automatically grabs out shit from a chest");
    }
    public TimeUtils timer = new TimeUtils();
    private boolean isEmpty;
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) this.mc.thePlayer.openContainer;

                for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                    if (timer.hasTimeElapsed((long) MythicX.setmgr.getSettingByName("Delay").getValDouble(), true)) {
                        if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                        mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                    }
                }
            }


            if (isEmpty) {
                mc.thePlayer.closeScreen();
                mc.displayGuiScreen(null);
            }
        }
    }


    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void setup() {
        MythicX.setmgr.rSetting(new Setting("Delay", this, 20, 1, 600, false));
    }
}