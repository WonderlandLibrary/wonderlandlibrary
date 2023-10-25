package club.cloverhook.cheat.impl.movement;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerSlowdownEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.SendPacketEvent;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;

import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


public class NoSlowdown extends Cheat {
    protected StringsProperty prop_mode = new StringsProperty("Mode", "How this cheat will function.",null, false, false, new String[] {"NCP"}, new Boolean[] {true});
    private boolean blocking;
    Stopwatch timer;
    int counter;//Will be used in Matrix noslow
    public NoSlowdown() {
        super("NoSlow", "Prevents you from slowing down when blocking, eating, etc.", CheatCategory.MOVEMENT);
        registerProperties(prop_mode);
        timer = new Stopwatch();
    }


    @Collect
    public void onSendPacket(SendPacketEvent sendPacketEvent) {
    	if (sendPacketEvent.getPacket() instanceof C07PacketPlayerDigging) {
    		blocking = false;
    	}

    	if (sendPacketEvent.getPacket() instanceof C08PacketPlayerBlockPlacement) {
    		blocking = true;
    	}
    }

    @Collect
    public void onPlayerSlowdown(PlayerSlowdownEvent playerSlowdownEvent) {
        if (this.getState()) {
            playerSlowdownEvent.setCancelled(true);
        }
    }
    
    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
    	setMode(prop_mode.getSelectedStrings().get(0));
    	
    	if (e.isPre()) {

    		if (prop_mode.getValue().get("NCP")) {
    			if (holdingSword() && getPlayer().isBlocking() && blocking && getPlayer().isMoving()) {
    				getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") ? new BlockPos(-.8, -.8, -.8)  : BlockPos.ORIGIN, EnumFacing.DOWN));
    				blocking = false;
    			}
    		}
    	} else {
    		if (prop_mode.getValue().get("NCP")) {
    			if (holdingSword() && getPlayer().isBlocking() && !blocking) {
    				getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-.8, -.8, -.8), 255, getPlayer().getHeldItem(), 0, 0, 0));
    				blocking = true;
    			}
    		}
    	}
    }
    
	private boolean holdingSword() {
		if (getPlayer().getCurrentEquippedItem() != null && getPlayer().inventory.getCurrentItem().getItem() instanceof ItemSword) {
			return true;
		}
		return false;
	}
}
