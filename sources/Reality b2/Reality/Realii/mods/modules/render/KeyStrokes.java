
package Reality.Realii.mods.modules.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import java.awt.Color;
import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import Reality.Realii.utils.cheats.world.TimerUtil;
import net.minecraft.util.ResourceLocation;
import Reality.Realii.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;




public class KeyStrokes
        extends Module {
	 
    public KeyStrokes() {
        super("KeyStrokes", ModuleType.Render);
       
    }

    
    @EventHandler
    public void renderKey(EventRender2D event) {
    	drawKeystrokes(10,200);
    	
    }

    public void drawKeystrokes(int x, int y) {
    	
    	 int width = 70;
         int height = 70;
         int padding = 5;
         int outlineWidth = 1;
         int alpha = 100;

       
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glDisable(GL11.GL_TEXTURE_2D);
         GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         RenderUtil.drawRect(x - outlineWidth, y - outlineWidth, x + width + outlineWidth, y + height + outlineWidth, new Color(0, 0, 0, alpha).getRGB());
         RenderUtil.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, alpha).getRGB());

        
       
         GL11.glEnable(GL11.GL_TEXTURE_2D);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glScalef(0.5f, 0.5f, 0.5f);
         mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + "W", (x + width / 2) * 2 - mc.fontRendererObj.getStringWidth("W") / 2, (y + padding) * 2, 0xFFFFFF);
         mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + "A", (x + padding) * 2, (y + height / 2) * 2 - mc.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
         mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + "S", (x + width / 2) * 2 - mc.fontRendererObj.getStringWidth("S") / 2, (y + height - padding - mc.fontRendererObj.FONT_HEIGHT) * 2, 0xFFFFFF);
         mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + "D", (x + width - padding - mc.fontRendererObj.getStringWidth("D")) * 2, (y + height / 2) * 2 - mc.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFF);
         GL11.glPopMatrix();
    }
}

