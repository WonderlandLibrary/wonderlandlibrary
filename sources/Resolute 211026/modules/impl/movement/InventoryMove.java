// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.modules.Module;

public class InventoryMove extends Module
{
    public BooleanSetting cancel;
    public static boolean enabled;
    
    public InventoryMove() {
        super("InventoryMove", 0, "Allows you to move while in inventory", Category.MOVEMENT);
        this.cancel = new BooleanSetting("Cancel Packet", false);
        this.addSettings(this.cancel);
    }
    
    @Override
    public void onEnable() {
        InventoryMove.enabled = true;
    }
    
    @Override
    public void onDisable() {
        InventoryMove.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("");
        if (e instanceof EventPacket && this.cancel.isEnabled() && (((EventPacket)e).getPacket() instanceof C16PacketClientStatus || ((EventPacket)e).getPacket() instanceof C0DPacketCloseWindow)) {
            e.setCancelled(true);
        }
    }
    
    static {
        InventoryMove.enabled = false;
    }
}
