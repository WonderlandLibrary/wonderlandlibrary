package me.aidanmees.trivia.client.modules.Legit;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.EntityHitEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Timer1;
import me.aidanmees.trivia.module.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoRod extends Module {

	private boolean changeItem = false;
	private boolean stop = false;
	private ItemStack stack = null;
	int saved;
	public boolean out;
	Timer1 timer = new Timer1();
	private int index = -1;

	public AutoRod() {
		super("RodTrick", Keyboard.KEY_NONE, Category.LEGIT, "Hit em with tha rod!");
	}

	@Override
	public void onDisable() {
		out = false;
		if(getRodSpot() > -1)
		mc.thePlayer.inventory.currentItem = saved;
		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		
			if(getRodSpot() > -1){
				if(mc.thePlayer.getHeldItem() == null){
					saved = mc.thePlayer.inventory.currentItem;
				}else
				if(!(mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod)){
					saved = mc.thePlayer.inventory.currentItem;
				}
				mc.thePlayer.inventory.currentItem = getRodSpot();
				if(!out){
				mc.rightClickMouse();
				out = true;
				}
				
				if(timer.hasReached(800)){
					timer.reset();
					setToggled(false, true);
				}
			}else{
//				saved = mc.thePlayer.inventory.currentItem;
				setToggled(false, true);
			}
		
		
		}
		public int getRodSpot(){
			for(int i = 0; i < 9; i++){
				if(isAHotSteamyRod(mc.thePlayer.inventory.getStackInSlot(i))){
					return i;
				}
			}
			return -1;
		}
		private boolean isAHotSteamyRod(ItemStack stack) {
			if (stack == null) {
				return false;
			}
			Item item = stack.getItem();
			if ((stack.getItem() instanceof ItemFishingRod)) {
							return true;
			}
			return false;

	}

	@Override
	public void onLeftClick() {
		if (!this.currentMode.equals("OnClick")) {
			return;
		}
		searchInvetory();
	}

	@Override
	public void onEntityHit(EntityHitEvent entityHitEvent) {
		if (!this.currentMode.equals("OnHit")) {
			return;
		}
		searchInvetory();
		super.onEntityHit(entityHitEvent);
	}

	public void searchInvetory() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			Item item = stack.getItem();
			if (item == null) {
				continue;
			}
			if (item instanceof ItemFishingRod) {
				changeItem = true;
				this.stack = stack;
				index = i;
				timer.reset();
				break;
			}
		}
	}
}

