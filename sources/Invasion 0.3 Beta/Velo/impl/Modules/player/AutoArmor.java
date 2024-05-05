package Velo.impl.Modules.player;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Other.PlayerUtil;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
    private int[] chestplate, leggings, boots, helmet;
    private int delay;
    private boolean best;
    public static NumberSetting delay2 = new NumberSetting("Armor Delay", 200, 1, 1000, 1);
	public static BooleanSetting inven = new BooleanSetting("On Open Inventory", true);
	private Timer timer = new Timer();
    public AutoArmor() {
        super("AutoArmor", "AutoArmor", 0, Category.PLAYER);
		this.loadSettings(delay2, inven);
    }


    
    @Override
    public void onEnable() {
    	   chestplate = new int[] {311, 307, 315, 303, 299};
           leggings = new int[] {312, 308, 316, 304, 300};
           boots = new int[] {313, 309, 317, 305, 301};
           helmet = new int[] {310, 306, 314, 302, 298};
           delay = 0;
           best = true;
    	super.onEnable();
    }
@Override
public void onUpdate(EventUpdate event) {
	if(!(mc.currentScreen instanceof GuiInventory) && this.inven.isEnabled()) {
		return;
	}
    autoArmor();
    betterArmor();
	super.onUpdate(event);
}


    public void autoArmor() {
        if(best)
            return;
        int item = -1;
        if(timer.hasTimedElapsed((long) delay2.getValue(), true)) {
            if(mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                int length = (boots = this.boots).length;
                for(int i =0; i < length; i++) {
                    int id = boots[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if(mc.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings;
                int length = (leggings = this.leggings).length;
                for(int i = 0; i < length; i++) {
                    int id = leggings[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if(mc.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate;
                int length = (chestplate = this.chestplate).length;
                for(int i = 0; i < length; i++) {
                    int id = chestplate[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if(mc.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet;
                int length = (helmet = this.helmet).length;
                for(int i = 0; i < length; i++) {
                    int id = helmet[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if(item != -1) {
                mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
                delay = 0;
            }
        }
    }

    public void betterArmor() {
        if(!best)
            return;
        if(timer.hasTimedElapsed((long) delay2.getValue(), true) && (mc.thePlayer.openContainer == null || mc.thePlayer.openContainer.windowId == 0)) {
            boolean switchArmor = false;
            int item = -1;
            int[] array;
            int i;
            if(mc.thePlayer.inventory.armorInventory[0] == null) {
                int j = (array = this.boots).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if (PlayerUtil.isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            if(mc.thePlayer.inventory.armorInventory[3] == null) {
                int j = (array = this.helmet).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if (PlayerUtil.isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            if(mc.thePlayer.inventory.armorInventory[1] == null) {
                int j = (array = this.leggings).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if (PlayerUtil.isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            if(mc.thePlayer.inventory.armorInventory[2] == null) {
                int j = (array = this.chestplate).length;
                for(i = 0; i < j; i++) {
                    int id = array[i];
                    if(PlayerUtil.getItem(id) != -1) {
                        item = PlayerUtil.getItem(id);
                        break;
                    }
                }
            }
            if (PlayerUtil.isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            ItemStack[] stackArray;
            int k = (stackArray = mc.thePlayer.inventory.mainInventory).length;
            for(int j = 0; j < k; j++) {
                ItemStack stack = stackArray[j];
                if(stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = switchArmor && !b;
            if(item != -1) {
                mc.playerController.windowClick(0, item, 0, switchArmor ? 4 : 1, mc.thePlayer);
                delay = 0;
            }
        }
    }
}