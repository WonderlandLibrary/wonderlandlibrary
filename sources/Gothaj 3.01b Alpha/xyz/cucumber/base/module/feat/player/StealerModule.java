package xyz.cucumber.base.module.feat.player;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.Display;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.S30PacketWindowItems;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.events.ext.EventWorldChange;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;

@ModuleInfo(category = Category.PLAYER, description = "Automatically steals items from chest", name = "Stealer")
public class StealerModule extends Mod{
	private final Timer timer = new Timer();
    private final Timer startTimer = new Timer();

    private final NumberSettings startDelay = new NumberSettings("Start Delay", 50, 0, 1000, 25);
    private final NumberSettings minDelay = new NumberSettings("Min Delay", 5, 0, 1000, 25);
    private final NumberSettings maxDelay = new NumberSettings("Max Delay", 5, 0, 1000, 25);
    private final BooleanSettings randomize = new BooleanSettings("Randomize", false);
    private final BooleanSettings stealTrashItems = new BooleanSettings("Steal trash items", false);
    private final BooleanSettings autoClose = new BooleanSettings("Auto Close", true);
    private final BooleanSettings chestName = new BooleanSettings("Check chest name", false);
    private final BooleanSettings disableOnWorldChange = new BooleanSettings("Disable on World Change", false);
    
    public StealerModule()
    {
       
        this.addSettings(startDelay,minDelay,maxDelay,randomize,stealTrashItems,autoClose,chestName,disableOnWorldChange);
    }
    private int decidedTimer = 0;

    public static boolean closeAfterContainer;

    private boolean gotItems;
    private int ticksInChest;

    private boolean lastInChest;
    
    public void onDisable()
    {
        closeAfterContainer = false;
        gotItems = false;
    }
    @EventListener
    public void onReceivePacket(EventReceivePacket e) {
    	 if (mc.thePlayer.ticksExisted <= 60)
         {
             return;
         }

         if (((EventReceivePacket)e).getPacket() instanceof S30PacketWindowItems)
         {
             gotItems = true;
         }
    }
    @EventListener
    public void onMotion(EventMotion e) {
    	 if(e.getType() == EventType.PRE) {
    		 if (mc.thePlayer.ticksExisted <= 60)
             {
                 return;
             }

             if (mc.currentScreen instanceof GuiChest && Display.isVisible() && (!chestName.isEnabled() || (((GuiChest) mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText().contains("chest"))))
             {
                 mc.mouseHelper.mouseXYChange();
                 mc.mouseHelper.ungrabMouseCursor();
                 mc.mouseHelper.grabMouseCursor();
             }

             if (mc.currentScreen instanceof GuiChest)
             {
                 ticksInChest++;

                 if (ticksInChest * 50 > 255)
                 {
                     ticksInChest = 10;
                 }
             }
             else
             {
                 ticksInChest--;
                 gotItems = false;

                 if (ticksInChest < 0)
                 {
                     ticksInChest = 0;
                 }
             }
    	 }
    }
    @EventListener
    public void onRenderGui(EventUpdate e) {
    	if (mc.thePlayer.ticksExisted <= 60)
        {
            return;
        }

        if (!lastInChest)
        {
            startTimer.reset();
        }

        lastInChest = mc.currentScreen instanceof GuiChest;

        if (mc.currentScreen instanceof GuiChest)
        {
            if (chestName.isEnabled())
            {
                final String name = ((GuiChest) mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText();

                if (!name.toLowerCase().contains("chest"))
                {
                    return;
                }
            }
            
            if(!startTimer.hasTimeElapsed(startDelay.getValue(), false))return;

            if (decidedTimer == 0)
            {
                final int delayFirst = (int) Math.floor(Math.min(minDelay.value, maxDelay.value));
                final int delaySecond = (int) Math.ceil(Math.max(minDelay.value, maxDelay.value));
                decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
            }

            if (timer.hasTimeElapsed(decidedTimer, false))
            {
                final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                
                if(randomize.isEnabled()) {
                	boolean found = false;
                	for (int i = 0; i < chest.inventorySlots.size(); i++)
                    {
                        final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);

                        if (stack != null && (itemWhitelisted(stack) && !stealTrashItems.isEnabled()))
                        {
                            found = true;
                        }
                    }
                	
                	int i = 0;
                	while(chest.getLowerChestInventory().getStackInSlot(i) == null) {
                		i = RandomUtils.nextInt(1, chest.inventorySlots.size());
                	}
                	
                	final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);

                    if (stack != null && (itemWhitelisted(stack) && !stealTrashItems.isEnabled()))
                    {
                        mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                        timer.reset();
                        final int delayFirst = (int) Math.floor(Math.min(minDelay.value, maxDelay.value));
                        final int delaySecond = (int) Math.ceil(Math.max(minDelay.value, maxDelay.value));
                        decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
                        gotItems = true;
                        return;
                    }
                    
                    if (gotItems && !found && autoClose.isEnabled() && ticksInChest > 3)
                    {
                        mc.thePlayer.closeScreen();
                        return;
                    }
                }else {
                	for (int i = 0; i < chest.inventorySlots.size(); i++)
                    {
                        final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);

                        if (stack != null && (itemWhitelisted(stack) && !stealTrashItems.isEnabled()))
                        {
                            mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                            timer.reset();
                            final int delayFirst = (int) Math.floor(Math.min(minDelay.value, maxDelay.value));
                            final int delaySecond = (int) Math.ceil(Math.max(minDelay.value, maxDelay.value));
                            decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
                            gotItems = true;
                            return;
                        }
                    }

                    if (gotItems && autoClose.isEnabled() && ticksInChest > 3)
                    {
                        mc.thePlayer.closeScreen();
                    }
                }
            }
        }
    }
    @EventListener
    public void onWorld(EventWorldChange e) {
    	 if (disableOnWorldChange.isEnabled())
         {
             this.toggle();
         }
    }
    
    
    private boolean itemWhitelisted(final ItemStack itemStack)
    {
        final ArrayList<Item> whitelistedItems = new ArrayList<Item>()
        {
            {
                add(Items.ender_pearl);
                add(Items.iron_ingot);
                add(Items.snowball);
                add(Items.gold_ingot);
                add(Items.redstone);
                add(Items.diamond);
                add(Items.emerald);
                add(Items.quartz);
                add(Items.bow);
                add(Items.arrow);
                add(Items.fishing_rod);
            }
        };
        final Item item = itemStack.getItem();
        final String itemName = itemStack.getDisplayName();

        if (itemName.contains("Right Click") || itemName.contains("Click to Use") || itemName.contains("Players Finder"))
        {
            return true;
        }

        final ArrayList<Integer> whitelistedPotions = new ArrayList<Integer>()
        {
            {
                add(6);
                add(1);
                add(5);
                add(8);
                add(14);
                add(12);
                add(10);
                add(16);
            }
        };

        if (item instanceof ItemPotion)
        {
            final int potionID = getPotionId(itemStack);
            return whitelistedPotions.contains(potionID);
        }

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
               || itemName.contains("\247")
               || whitelistedItems.contains(item)
               && !item.equals(Items.spider_eye);
    }
    private int getPotionId(final ItemStack potion)
    {
        final Item item = potion.getItem();

        try
        {
            if (item instanceof ItemPotion)
            {
                final ItemPotion p = (ItemPotion) item;
                return p.getEffects(potion.getMetadata()).get(0).getPotionID();
            }
        }
        catch (final NullPointerException ignored)
        {
        }

        return 0;
    }
}
