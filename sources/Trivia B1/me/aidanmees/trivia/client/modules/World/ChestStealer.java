package me.aidanmees.trivia.client.modules.World;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.timer;
import me.aidanmees.trivia.module.Module;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer extends Module {

	timer timer2 = new timer();
	

    public static double delay = 1000.0;


	

	
	

	public ChestStealer() {
		super("ChestStealer", Keyboard.KEY_H, Category.PLAYER,
				"Automatically steals items from chests.");
	}

	@Override
	public void onDisable() {
	
		super.onDisable();
	}

	@Override
	public void onEnable() {
	
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (mc.thePlayer.openContainer != null) {
           
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                
                ContainerChest container = (ContainerChest)mc.thePlayer.openContainer;
                if (container.getInventory().isEmpty()) {
               
                    mc.thePlayer.openContainer = null;
                }
                if (!container.getLowerChestInventory().getName().equals("Game Menu") && !container.getLowerChestInventory().getName().contains("Play")) {
                	
                
                int i = 0;
                while (i < container.getLowerChestInventory().getSizeInventory()) {
                    if (container.getLowerChestInventory().getStackInSlot(i) != null && timer2.delay(120)) {
                       
                        mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                        timer2.reset();
                    }
                    ++i;
                }
                if (container.getInventory().isEmpty()) {
                    mc.displayGuiScreen(null);
                }
            }
            }
        }
    }
}

