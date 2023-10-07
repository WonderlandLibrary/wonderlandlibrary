// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import exhibition.event.impl.EventPacket;
import org.lwjgl.input.Keyboard;
import exhibition.event.impl.EventTick;
import net.minecraft.client.gui.GuiChat;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class InventoryWalk extends Module
{
    private String CARRY;
    
    public InventoryWalk(final ModuleData data) {
        super(data);
        this.CARRY = "CARRY";
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.CARRY, new Setting<Boolean>(this.CARRY, false, "Carry items in crafting slots."));
    }
    
    @RegisterEvent(events = { EventPacket.class, EventTick.class })
    @Override
    public void onEvent(final Event event) {
        if (InventoryWalk.mc.currentScreen instanceof GuiChat) {
            return;
        }
        if (event instanceof EventTick && InventoryWalk.mc.currentScreen != null) {
            if (Keyboard.isKeyDown(200)) {
                InventoryWalk.mc.thePlayer.rotationPitch -= 3.0f;
            }
            if (Keyboard.isKeyDown(208)) {
                InventoryWalk.mc.thePlayer.rotationPitch += 3.0f;
            }
            if (Keyboard.isKeyDown(203)) {
                InventoryWalk.mc.thePlayer.rotationYaw -= 5.0f;
            }
            if (Keyboard.isKeyDown(205)) {
                InventoryWalk.mc.thePlayer.rotationYaw += 5.0f;
            }
        }
        if (event instanceof EventPacket && ((HashMap<K, Setting<Boolean>>)this.settings).get(this.CARRY).getValue()) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C0DPacketCloseWindow) {
                ep.setCancelled(true);
            }
        }
    }
}
