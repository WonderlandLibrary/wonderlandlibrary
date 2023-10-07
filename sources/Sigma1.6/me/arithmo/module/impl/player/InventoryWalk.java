/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class InventoryWalk
extends Module {
    private String CARRY = "CARRY";

    public InventoryWalk(ModuleData data) {
        super(data);
        this.settings.put(this.CARRY, new Setting<Boolean>(this.CARRY, false, "Carry items in crafting slots."));
    }

    @RegisterEvent(events={EventPacket.class, EventTick.class})
    public void onEvent(Event event) {
        EventPacket ep;
        if (InventoryWalk.mc.currentScreen instanceof GuiChat) {
            return;
        }
        if (event instanceof EventTick && InventoryWalk.mc.currentScreen != null) {
            if (Keyboard.isKeyDown((int)200)) {
                InventoryWalk.mc.thePlayer.rotationPitch -= 1.0f;
            }
            if (Keyboard.isKeyDown((int)208)) {
                InventoryWalk.mc.thePlayer.rotationPitch += 1.0f;
            }
            if (Keyboard.isKeyDown((int)203)) {
                InventoryWalk.mc.thePlayer.rotationYaw -= 3.0f;
            }
            if (Keyboard.isKeyDown((int)205)) {
                InventoryWalk.mc.thePlayer.rotationYaw += 3.0f;
            }
        }
        if (event instanceof EventPacket && ((Boolean)((Setting)this.settings.get(this.CARRY)).getValue()).booleanValue() && (ep = (EventPacket)event).isOutgoing() && ep.getPacket() instanceof C0DPacketCloseWindow) {
            ep.setCancelled(true);
        }
    }
}

