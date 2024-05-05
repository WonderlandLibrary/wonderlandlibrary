package epsilon.modules.player;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.TimeUtil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.WeightedRandom.Item;

public class ChestStealer extends Module{

    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil startTimer = new TimeUtil();

    private final NumberSetting minDelay = new NumberSetting("Min Delay",  5, 0, 1000, 25);
    private final NumberSetting maxDelay = new NumberSetting("Max Delay",  5, 0, 1000, 25);
    private final BooleanSetting stealTrashItems = new BooleanSetting("Steal trash items", false);
    private final BooleanSetting autoClose = new BooleanSetting("Auto Close",  true);
    private final BooleanSetting hideGui = new BooleanSetting("Hide Gui", true);
    private final BooleanSetting mouseInput = new BooleanSetting("Move head in chest",  true);
    private final BooleanSetting chestName = new BooleanSetting("Check chest name",  false);

    private int decidedTimer = 0;

    public static boolean hideChestGui, allowMouseInput, closeAfterContainer;

    private boolean gotItems;
    private int ticksInChest;

    private boolean lastInChest;

	
	public ChestStealer(){
		super("ChestStealer", Keyboard.KEY_P, Category.PLAYER, "Steals the contents of a chest");
		this.addSettings(minDelay, maxDelay, stealTrashItems, autoClose, hideGui, mouseInput, chestName);
	}
	
    public void onDisable() {
        closeAfterContainer = false;
        allowMouseInput = false;
        hideChestGui = false;
        gotItems = false;
        ticksInChest = 0;
    }
    public void onEvent(Event e){
		if(e instanceof EventUpdate){
		        if (mc.thePlayer.ticksExisted <= 60) return;
		        if (!lastInChest) startTimer.reset();
		        lastInChest = mc.currentScreen instanceof GuiChest;
		        ticksInChest++;
		        if (mc.currentScreen instanceof GuiChest) {
		        	
		            if (hideGui.isEnabled()) {
		                hideChestGui = true;
		            }

		            if (mouseInput.isEnabled()) {
		                allowMouseInput = true;
		            }

		            if (chestName.isEnabled()) {
		                final String name = ((GuiChest) mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText();

		                if (!name.toLowerCase().contains("chest")) return;
		            }

		            if (hideGui.isEnabled()) {
		                final ScaledResolution SR = new ScaledResolution(mc, 1, 1);
		                final String t = "Stealing chest... Press " + Keyboard.getKeyName(mc.gameSettings.keyBindInventory.getKeyCode()) + " to close the chest";
		                int o;

		                o = ticksInChest * 50;

		                if (o > 255)
		                    o = 255;
		                Minecraft.getMinecraft().fontRendererObj.drawString(t, o, o, 5);

		                
		            }

		            if (decidedTimer == 0) {
		                final int delayFirst = (int) Math.floor(Math.min(minDelay.getValue(), maxDelay.getValue()));
		                final int delaySecond = (int) Math.ceil(Math.max(minDelay.getValue(), maxDelay.getValue()));
		                decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
		            }

		            if (timer.hasReached(decidedTimer) && ticksInChest>5) {
		                final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

		                for (int i = 0; i < chest.inventorySlots.size(); i++) {
		                    final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);
		                    if (stack != null && (itemWhitelisted(stack) && !stealTrashItems.isEnabled())) {
		                        mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
		                        timer.reset();

		                        final int delayFirst = (int) Math.floor(Math.min(minDelay.getValue(), maxDelay.getValue()));
		                        final int delaySecond = (int) Math.ceil(Math.max(minDelay.getValue(), maxDelay.getValue()));

		                        decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);

		                        gotItems = true;
		                        return;
		                    }
		                }

		                if (autoClose.isEnabled() && (gotItems || ticksInChest > 10)) {
		                    mc.thePlayer.closeScreen();
		                }
		            }
		        } else {
		            hideChestGui = false;
		            allowMouseInput = false;
		        }
		    }
    }
		
	

    private boolean itemWhitelisted(final ItemStack itemStack) {
        

        final net.minecraft.item.Item item = itemStack.getItem();
        final String itemName = itemStack.getDisplayName();

        if (itemName.contains("Right Click") || itemName.contains("Click to Use") || itemName.contains("Players Finder"))
            return true;

        final ArrayList<Integer> whitelistedPotions = new ArrayList<Integer>() {{
            add(6);
            add(1);
            add(5);
            add(8);
            add(14);
            add(12);
            add(10);
            add(16);
        }};


        return (item instanceof ItemBlock
                && !(((ItemBlock) item).getBlock() instanceof BlockTNT)
                && !(((ItemBlock) item).getBlock() instanceof BlockSlime)
                && !(((ItemBlock) item).getBlock() instanceof BlockFalling))
                || item instanceof ItemAnvilBlock
                || item instanceof ItemSword
                || item instanceof ItemArmor
                || item instanceof ItemTool
                || item instanceof ItemFood
                || item instanceof ItemSkull
                || item instanceof ItemPotion
                || !(item instanceof ItemBucket)
                || itemName.contains("\247");
    }

}	