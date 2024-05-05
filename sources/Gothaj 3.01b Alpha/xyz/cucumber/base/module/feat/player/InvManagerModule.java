package xyz.cucumber.base.module.feat.player;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.InventoryUtils;

@ModuleInfo(category = Category.PLAYER, description = "Automatically sorts things in your inv", name = "Inv Manager")
public class InvManagerModule extends Mod{
	public Timer startTimer = new Timer();
    public Timer timer = new Timer();
    
    public ModeSettings mode = new ModeSettings("Mode", new String[] {
    		"Open Inv", "Spoof"
    });
    public BooleanSettings intave = new BooleanSettings("Intave", true);
    public BooleanSettings throwGarbage = new BooleanSettings("Throw Garbage", true);
    public NumberSettings startDelay = new NumberSettings("Start Delay", 150.0D, 0.0D, 1000.0D, 1);
    public NumberSettings speed = new NumberSettings("Speed", 150.0D, 0.0D, 1000.0D, 1);
    public BooleanSettings sword = new BooleanSettings("Sword", true);
    public NumberSettings swordSlot = new NumberSettings("sword Slot", 1, 1, 9, 1);
    public BooleanSettings axe = new BooleanSettings("Axe", true);
    public NumberSettings axeSlot = new NumberSettings("Axe Slot", 2, 1, 9, 1);
    public BooleanSettings pickaxe = new BooleanSettings("Pickaxe", true);
    public NumberSettings pickaxeSlot = new NumberSettings("Pickaxe", 3, 1, 9, 1);
    public BooleanSettings shovel = new BooleanSettings("Shovel", false);
    public NumberSettings shovelSlot = new NumberSettings("Shovel Slot", 4, 1, 9, 1);
    public BooleanSettings bow = new BooleanSettings("Bow", false);
    public NumberSettings bowSlot = new NumberSettings("Bow Slot", 5, 1, 9, 1);
    public BooleanSettings blocks = new BooleanSettings("Blocks", true);
    public NumberSettings blockSlot = new NumberSettings("Block Slot", 6, 1, 9, 1);
    public BooleanSettings projectiles = new BooleanSettings("Projectiles", true);
    public NumberSettings projectileSlot = new NumberSettings("Projectile Slot", 7, 1, 9, 1);
    public BooleanSettings waterBucket = new BooleanSettings("Water Bucket", true);
    public NumberSettings waterBucketSlot = new NumberSettings("Water Bucket Slot", 8, 1, 9, 1);
    
    public KeyBinding[] moveKeys = new KeyBinding[]
    	    {
    	        mc.gameSettings.keyBindForward,
    	        mc.gameSettings.keyBindBack,
    	        mc.gameSettings.keyBindLeft,
    	        mc.gameSettings.keyBindRight,
    	        mc.gameSettings.keyBindJump,
    	        mc.gameSettings.keyBindSneak
    	    };
    
    public InvManagerModule() {
        this.addSettings(mode,
        		intave,
        		throwGarbage,
        		startDelay,
        		speed,
        		sword,
        		swordSlot,
        		axe,
        		axeSlot,
        		pickaxe,
        		pickaxeSlot,
        		shovel,
        		shovelSlot,
        		bow,
        		bowSlot,
        		blocks,
        		blockSlot,
        		projectiles,
        		projectileSlot,
        		waterBucket,
        		waterBucketSlot
        		);
    }
    
    @EventListener
    public void onMoveButton(EventMoveButton e) {
    	if(InventoryUtils.isInventoryOpen && intave.isEnabled()) {

			e.forward = false;
			e.backward = false;
			e.left = false;
			e.right = false;
			e.jump = false;
			e.sneak = false;
		}
    }
    @EventListener
    public void onUpdate(EventUpdate e) {
    	setInfo(mode.getMode());
    	
    	if(mode.getMode().equalsIgnoreCase("Open Inv")) {
        	if(mc.currentScreen == null) {
        		startTimer.reset();
        	}
        	if(!startTimer.hasTimeElapsed(startDelay.getValue(), false)) {
        		return;
        	}
        }

        if (this.mode.getMode().equalsIgnoreCase("Spoof") && mc.currentScreen != null)
        {
            return;
        }

        if (mode.getMode().equalsIgnoreCase("Open Inv") && !(mc.currentScreen instanceof GuiInventory))
        {
            return;
        }
        
        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (InventoryUtils.timer.hasTimeElapsed(speed.value, false))
                {
                    if (swordSlot.value != 0 && is.getItem() instanceof ItemSword && is == InventoryUtils.bestSword() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.bestSword()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + swordSlot.value)).getStack() != is && sword.isEnabled())
                    {
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(swordSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    else if (bowSlot.value != 0 && is.getItem() instanceof ItemBow && is == InventoryUtils.bestBow() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.bestBow()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + bowSlot.value)).getStack() != is && bow.isEnabled())
                    {
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(bowSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    else if (pickaxeSlot.value != 0 && is.getItem() instanceof ItemPickaxe && is == InventoryUtils.bestPick() && is != InventoryUtils.bestWeapon() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.bestPick()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + pickaxeSlot.value)).getStack() != is && pickaxe.isEnabled())
                    {
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(pickaxeSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    else if (axeSlot.value != 0 && is.getItem() instanceof ItemAxe && is == InventoryUtils.bestAxe() && is != InventoryUtils.bestWeapon() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.bestAxe()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + axeSlot.value)).getStack() != is && axe.isEnabled())
                    {
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(axeSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    else if (shovelSlot.value != 0 && is.getItem() instanceof ItemSpade && is == InventoryUtils.bestShovel() && is != InventoryUtils.bestWeapon() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.bestShovel()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + shovelSlot.value)).getStack() != is && shovel.isEnabled())
                    {
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(shovelSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    
                    
                    else if (blockSlot.value != 0 && is.getItem() instanceof ItemBlock && is == InventoryUtils.getBlockSlotInventory() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.getBlockSlotInventory()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + blockSlot.value)).getStack() != is && blocks.isEnabled())
                    {
                    	if(mc.thePlayer.inventoryContainer.getSlot((int)(35 + blockSlot.value)).getStack() != null && mc.thePlayer.inventoryContainer.getSlot((int)(35 + blockSlot.value)).getStack().getItem() instanceof ItemBlock && !InventoryUtils.invalidBlocks.contains(((ItemBlock) mc.thePlayer.inventoryContainer.getSlot((int)(35 + blockSlot.value)).getStack().getItem()).getBlock()))return;
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(blockSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    
                    
                    else if (projectileSlot.value != 0 && is == InventoryUtils.getProjectileSlotInventory() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.getProjectileSlotInventory()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + projectileSlot.value)).getStack() != is && projectiles.isEnabled())
                    {
                    	if(mc.thePlayer.inventoryContainer.getSlot((int)(35 + projectileSlot.value)).getStack() != null && (mc.thePlayer.inventoryContainer.getSlot((int)(35 + projectileSlot.value)).getStack().getItem() instanceof ItemSnowball || mc.thePlayer.inventoryContainer.getSlot((int)(35 + projectileSlot.value)).getStack().getItem() instanceof ItemEgg || mc.thePlayer.inventoryContainer.getSlot((int)(35 + projectileSlot.value)).getStack().getItem() instanceof ItemFishingRod))return;
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(projectileSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    
                    
                    else if (waterBucketSlot.value != 0 && is.getItem() == Items.water_bucket && is == InventoryUtils.getBucketSlotInventory() && mc.thePlayer.inventoryContainer.getInventory().contains(InventoryUtils.getBucketSlotInventory()) && mc.thePlayer.inventoryContainer.getSlot((int)(35 + shovelSlot.value)).getStack() != is && waterBucket.isEnabled())
                    {
                    	if(mc.thePlayer.inventoryContainer.getSlot((int)(35 + waterBucketSlot.value)).getStack() != null && mc.thePlayer.inventoryContainer.getSlot((int)(35 + waterBucketSlot.value)).getStack().getItem() == Items.water_bucket)return;
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, (int)(waterBucketSlot.value - 1), 2, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }
                    else if (InventoryUtils.isBadStack(is, true, true) && throwGarbage.isEnabled())
                    {
                        InventoryUtils.openInv(mode.getMode());
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                        InventoryUtils.timer.reset();

                        if (this.speed.value != 0)break;
                    }else {
                    	if (InventoryUtils.timer.getTime() > 75) {
                            InventoryUtils.closeInv(mode.getMode());
                        }
                    }
                }
            }
        }
    }
}
