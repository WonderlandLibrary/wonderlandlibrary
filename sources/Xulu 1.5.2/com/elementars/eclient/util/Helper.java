// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import dev.xulu.settings.FileManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;

public interface Helper
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final World world = Minecraft.getMinecraft().world;
    public static final EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
    public static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static final FileManager fileManager = Wrapper.getFileManager();
    public static final EventBus EVENT_BUS = MinecraftForge.EVENT_BUS;
}
