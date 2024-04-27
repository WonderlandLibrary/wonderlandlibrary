package net.ccbluex.liquidbounce.ui.betterchat.handlers;

import net.ccbluex.liquidbounce.ui.betterchat.gui.GuiBetterChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InjectHandler {
    public static GuiBetterChat chatGUI;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        chatGUI = new GuiBetterChat(Minecraft.getMinecraft());
        ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, chatGUI, "field_73840_e");
    }
}

