package com.canon.majik.impl.modules.impl.hud;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.Render2DEvent;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.modules.impl.combat.KillAura;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

public class TargetHud extends Module {
    public TargetHud(String name) {
        super(name);
    }

    @EventListener
    public void onRender(Render2DEvent event){
        if(nullCheck()) return;
        float x1 = x.getValue().floatValue();
        float y1 = y.getValue().floatValue();
        if(KillAura.instance.target != null){
            RenderUtils.rect(x1,y1,130,5, new Color(255, 0, 0, 255).getRGB());
            RenderUtils.rect(x1,y1,130,70, new Color(15,15,15,100).getRGB());
            drawHead(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(KillAura.instance.target.getUniqueID()).getLocationSkin(), (int) (x1+10), (int) (y1+10));
            Initializer.CFont.drawStringWithShadow(KillAura.instance.target.getName(), x1+2,y1+7,new Color(0).getRGB());
        }
    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8, 8, 8, 8, 37, 37, 64, 64);
    }
}
