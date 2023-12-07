package Velo.impl.Modules.player;


import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import Velo.api.Module.Module;
import Velo.api.Util.Other.PlayerUtil;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;



public class ChestStealer extends Module {

	public BooleanSetting titleCheck = new BooleanSetting("Title Check", true);
    public BooleanSetting autoClose = new BooleanSetting("Auto Close", true);
    public BooleanSetting aura = new BooleanSetting("Aura", false);
    public BooleanSetting filter = new BooleanSetting("Filter", true);
    public BooleanSetting silent = new BooleanSetting("Silent", false);
    
    public NumberSetting delay = new NumberSetting("Delay", 4, 1, 10, 1);
    public NumberSetting range = new NumberSetting("Aura Range", 5.0, 1.0, 6.0, 1);
    
    public Timer timer = new Timer();
    
    public Timer auraTimer = new Timer();
    
    public final Set openedChests = new HashSet();

    public ChestStealer() {
        super("ChestStealer", "ChestStealer", 0, Category.PLAYER);
        loadSettings(autoClose, titleCheck, filter, aura, silent, range, delay);
    }
@Override
public void onPostMotionUpdate(EventPostMotion event) {
    int index;
	 if ( mc.currentScreen == null && aura.isEnabled()) {
     	List loadedTileEntityList = mc.theWorld.loadedTileEntityList;
         index = 0;
         
         for(int loadedTileEntityListSize = loadedTileEntityList.size(); index < loadedTileEntityListSize; ++index) {
         	TileEntity tile = (TileEntity)loadedTileEntityList.get(index);
         	BlockPos pos = tile.getPos();
         	if (tile instanceof TileEntityChest && getDistToPos(pos) < range.getValue() && !openedChests.contains(tile) && auraTimer.delay(500L) && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.DOWN, getVec3(tile.getPos()))) {
         		mc.getNetHandler().addToSendQueueSilent(new C0APacketAnimation());
         		set(openedChests, tile);
         		auraTimer.reset2();
         		return;
         	}
         }
     }
	super.onPostMotionUpdate(event);
}


@Override
public void onPreMotionUpdate(EventPreMotion event) {
    final int delay = (int) (this.delay.getValue() * 50);
    final EntityPlayerSP player = mc.thePlayer;
    int index;
    if (mc.currentScreen instanceof GuiChest) {
        final GuiChest chest = (GuiChest) mc.currentScreen;
        boolean titleCheck = this.titleCheck.isEnabled() ? (chest.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Chest")) || chest.getLowerChestInventory().getDisplayName().getUnformattedText().equalsIgnoreCase("LOW") : true;
        if (titleCheck) {
        	if (autoClose.isEnabled()) {
                if (isChestEmpty(chest) || isInventoryFull()) {
                	player.closeScreen();
                    return;
                }
        	}
            for (index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                if (stack != null && timer.delay(delay - ThreadLocalRandom.current().nextInt(0, 250))) {
                	boolean trash = filter.isEnabled() ? !PlayerUtil.isTrash(stack) : true;
                	if (trash) {
                        mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, player);
                        timer.reset2();
                        break;
                	}
                }
            }
        }
    }
	super.onPreMotionUpdate(event);
}
    

    
    public void set(Set set, TileEntity chest) {
    	if (set.size() > 128) {
    		set.clear();
    	}
    	set.add(chest);
    }

	public Vec3 getVec3(BlockPos pos) {
    	return new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }
    
    public double getDistToPos(BlockPos pos) {
    	return mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

	public boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null)
            	
                if (!PlayerUtil.isTrash(stack) )
                    return false;
        }
        return true;
    }

    public boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
    
    public void onEnable() {
    	super.onEnable();
    	auraTimer.reset2();
    	timer.reset2();
    }
    
    public void onDisable() {
    	super.onDisable();
    }
}
