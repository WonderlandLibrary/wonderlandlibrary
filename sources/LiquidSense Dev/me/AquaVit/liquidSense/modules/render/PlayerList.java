package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.List;

@ModuleInfo(name = "PlayerList", description = "PlayerList.", category = ModuleCategory.RENDER)
public class PlayerList extends Module {
    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    public IntegerValue red = new IntegerValue("Red", 255, 0,255);
    public IntegerValue green = new IntegerValue("Green", 255, 0,255);
    public IntegerValue blue = new IntegerValue("Blue", 255, 0,255);
    public IntegerValue alpha = new IntegerValue("Alpha", 255, 0,255);
    float ULX2 = 2;
    @EventTarget
    private void renderHud(Render2DEvent event) {
        float ULY2 = height() / 3;

        float last = 2;
        List<EntityPlayerSP> PlayerLists = (List)mc.theWorld.getLoadedEntityList();
        for(EntityPlayer Player : PlayerLists) {
            ULY2+=8;
            String Textx = (int) Player.getHealth() + "HP " + Player.getName();
            if(mc.fontRendererObj.getStringWidth(Textx) + 2 > ULX2) {
                ULX2=mc.fontRendererObj.getStringWidth(Textx) + 2;
            }
            mc.fontRendererObj.drawStringWithShadow(Textx, last, ULY2, new Color(red.get(),green.get(),blue.get(),alpha.get()).getRGB());
            if(height() / 3 + 25 * 8 == ULY2) {
                ULY2=height() / 3;
                last+=ULX2;
                ULX2=0;
            }
        }
    }
}

