/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KeyEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.ScreenEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

@ModuleInfo(name="KeepContainer", description="Allows you to open a formerly closed inventory container everywhere. (Press INSERT Key to open)", category=ModuleCategory.FUN)
public class KeepContainer
extends Module {
    private GuiContainer container;

    @Override
    public void onDisable() {
        if (this.container != null) {
            mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow(this.container.field_147002_h.field_75152_c));
        }
        this.container = null;
    }

    @EventTarget
    public void onGui(ScreenEvent event) {
        if (event.getGuiScreen() instanceof GuiContainer && !(event.getGuiScreen() instanceof GuiInventory)) {
            this.container = (GuiContainer)event.getGuiScreen();
        }
    }

    @EventTarget
    public void onKey(KeyEvent event) {
        if (event.getKey() == 210) {
            if (this.container == null) {
                return;
            }
            mc.func_147108_a((GuiScreen)this.container);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow) {
            event.cancelEvent();
        }
        if (event.getPacket() instanceof S2EPacketCloseWindow) {
            S2EPacketCloseWindow packetCloseWindow = (S2EPacketCloseWindow)event.getPacket();
            if (this.container != null && this.container.field_147002_h != null && packetCloseWindow.field_148896_a == this.container.field_147002_h.field_75152_c) {
                this.container = null;
            }
        }
    }
}

