package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.events.impl.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import com.initial.utils.render.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import java.util.*;
import com.initial.events.*;

public class Radar extends Module
{
    public Radar() {
        super("Radar", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void onDraw(final EventRender2D e) {
        final double center = 50.0;
        final double toAddX = 5.0;
        final double toAddZ = 5.0;
        Gui.drawRect(0.0 + toAddX, 0.0 + toAddZ - 1.0, 100.0 + toAddX, 0.0 + toAddZ, new Color(10249983).getRGB());
        Render2DUtils.prepareScissorBox(0.0 + toAddX, 0.0 + toAddZ, 100.0 + toAddX, 100.0 + toAddZ);
        GL11.glEnable(3089);
        Gui.drawRect(0.0 + toAddX, 0.0 + toAddZ, 100.0 + toAddX, 100.0 + toAddZ, new Color(-15527149, true).getRGB());
        for (final Entity ent : this.mc.theWorld.getLoadedEntityList()) {
            if (ent != this.mc.thePlayer) {
                int color = 0;
                if (ent instanceof EntityMob) {
                    color = new Color(-1795162113, true).getRGB();
                }
                if (ent instanceof EntityAnimal) {
                    color = new Color(1795162111, true).getRGB();
                }
                if (ent instanceof EntityPlayer) {
                    color = new Color(-1, true).getRGB();
                }
                if (ent instanceof EntityItem) {
                    color = new Color(671088639, true).getRGB();
                }
                final double drawX = center + (Math.round(this.mc.thePlayer.posX) - Math.round(ent.posX));
                final double drawZ = center + (Math.round(this.mc.thePlayer.posZ) - Math.round(ent.posZ));
                Gui.drawRect(drawX + toAddX, drawZ + toAddZ, drawX + 1.0 + toAddX, drawZ + 1.0 + toAddZ, color);
            }
        }
        GL11.glDisable(3089);
        Gui.drawRect(toAddX + center - 1.0, 0.0 + toAddZ, toAddX + center, 100.0 + toAddZ, new Color(1459617791, true).getRGB());
        Gui.drawRect(0.0 + toAddX, toAddZ + center - 1.0, 100.0 + toAddX, toAddZ + center, new Color(1459617791, true).getRGB());
    }
}
