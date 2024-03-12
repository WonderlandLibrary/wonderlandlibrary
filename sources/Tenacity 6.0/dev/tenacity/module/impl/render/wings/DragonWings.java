// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.wings;

import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.event.impl.render.RendererLivingEntityEvent;
import dev.tenacity.module.settings.Setting;
import java.awt.Color;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class DragonWings extends Module
{
    private final NumberSetting scale;
    private final ColorSetting color;
    private final WingModel model;
    
    public DragonWings() {
        super("DragonWings", "Dragon Wings", Category.RENDER, "gives you dragon wings");
        this.scale = new NumberSetting("Scale", 1.0, 1.25, 0.75, 0.25);
        this.color = new ColorSetting("Color", Color.WHITE);
        this.model = new WingModel();
        this.addSettings(this.scale, this.color);
    }
    
    @Override
    public void onRendererLivingEntityEvent(final RendererLivingEntityEvent event) {
        if (event.isPost() && event.getEntity() == DragonWings.mc.thePlayer && !DragonWings.mc.thePlayer.isInvisible()) {
            this.model.renderWings(DragonWings.mc.thePlayer, event.getPartialTicks(), this.scale.getValue(), this.color.getColor());
        }
    }
}
