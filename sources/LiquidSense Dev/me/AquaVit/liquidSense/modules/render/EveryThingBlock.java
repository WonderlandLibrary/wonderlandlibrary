package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@ModuleInfo(name = "EveryThingBlock", description = "EveryThingBlock", category = ModuleCategory.RENDER)
public class EveryThingBlock extends Module {

    private final FloatValue x = new FloatValue("X", 0.0f, -1.0f, 1.0f);
    private final FloatValue y = new FloatValue("Y", 0.0f, -1.0f, 1.0f);
    private final FloatValue z = new FloatValue("Z", 0.0f, -1.0f, 1.0f);

    public FloatValue getX() {
        return x;
    }
    public FloatValue getY() {
        return y;
    }
    public FloatValue getZ() {
        return z;
    }

}

