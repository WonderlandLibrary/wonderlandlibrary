package koks.module.player;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.KeyPressEvent;
import koks.event.PacketEvent;
import koks.event.UpdateEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.event.KeyEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "ShopSaver", description = "You can open the shop everywhere", category = Module.Category.PLAYER)
public class ShopSaver extends Module {

    @Value(name = "Spoof")
    boolean spoof = false;

    @Value(name = "Silent Sprint")
    boolean silentSprint = false;

    GuiScreen guiScreen;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<? extends INetHandler> packet = packetEvent.getPacket();
            if (packet instanceof final C16PacketClientStatus clientStatus) {
                if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                    if (spoof)
                        packetEvent.setCanceled(true);
            }
            if (packet instanceof C0BPacketEntityAction entityAction) {
                if (silentSprint)
                    switch (entityAction.getAction()) {
                        case START_SPRINTING, STOP_SPRINTING -> {
                            packetEvent.setCanceled(true);
                        }
                    }
            }
            if (packet instanceof C0DPacketCloseWindow) {
                packetEvent.setCanceled(true);
            }
            if (packet instanceof S2EPacketCloseWindow) {
                if (guiScreen != null) {
                    sendMessage("§cShop closed!");
                    guiScreen = null;
                }
            }
        }

        if (event instanceof final KeyPressEvent keyPressEvent) {
            if (keyPressEvent.getKey() == getGameSettings().keyBindInventory.getKeyCode() && guiScreen != null) {
                keyPressEvent.setCanceled(true);
            }
        }

        if (event instanceof UpdateEvent) {
            final GuiScreen currentScreen = mc.currentScreen;

            if (getGameSettings().keyBindInventory.pressed && mc.currentScreen == null && guiScreen != null) {
                getGameSettings().keyBindInventory.pressed = false;
                mc.displayGuiScreen(guiScreen);
            }

            if (currentScreen != null)
                if (currentScreen instanceof GuiContainer) {
                    if (guiScreen != currentScreen) {
                        sendMessage("§aShop Saved");
                    }
                    guiScreen = currentScreen;
                }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
