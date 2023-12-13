// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import java.awt.Color;
import com.elementars.eclient.Xulu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class AntiFog extends Module
{
    private ArrayList<String> options;
    private final Value<Boolean> rainbow;
    private final Value<Float> r;
    private final Value<Float> g;
    private final Value<Float> b;
    private final Value<Float> r1;
    private final Value<Float> g1;
    private final Value<Float> b1;
    private final Value<Float> r2;
    private final Value<Float> g2;
    private final Value<Float> b2;
    private final Value<Boolean> clear;
    private final Value<Boolean> color;
    
    public AntiFog() {
        super("AntiFog", "Prevents fog", 0, Category.RENDER, true);
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.r = this.register(new Value<Float>("Red", this, 1.0f, 0.0f, 1.0f));
        this.g = this.register(new Value<Float>("Green", this, 1.0f, 0.0f, 1.0f));
        this.b = this.register(new Value<Float>("Blue", this, 1.0f, 0.0f, 1.0f));
        this.r1 = this.register(new Value<Float>("Nether Red", this, 1.0f, 0.0f, 1.0f));
        this.g1 = this.register(new Value<Float>("Nether Green", this, 1.0f, 0.0f, 1.0f));
        this.b1 = this.register(new Value<Float>("Nether Blue", this, 1.0f, 0.0f, 1.0f));
        this.r2 = this.register(new Value<Float>("End Red", this, 1.0f, 0.0f, 1.0f));
        this.g2 = this.register(new Value<Float>("End Green", this, 1.0f, 0.0f, 1.0f));
        this.b2 = this.register(new Value<Float>("End Blue", this, 1.0f, 0.0f, 1.0f));
        this.clear = this.register(new Value<Boolean>("Remove fog", this, true));
        this.color = this.register(new Value<Boolean>("Color fog", this, true));
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onFogDensity(final EntityViewRenderEvent.FogDensity event) {
        if (this.clear.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onFogColor(final EntityViewRenderEvent.FogColors event) {
        if (this.color.getValue()) {
            if (this.rainbow.getValue()) {
                event.setRed(new Color(Xulu.rgb).getRed() / 255.0f);
                event.setGreen(new Color(Xulu.rgb).getGreen() / 255.0f);
                event.setBlue(new Color(Xulu.rgb).getBlue() / 255.0f);
            }
            else if (AntiFog.mc.player.dimension == 0) {
                event.setRed((float)this.r.getValue());
                event.setGreen((float)this.g.getValue());
                event.setBlue((float)this.b.getValue());
            }
            else if (AntiFog.mc.player.dimension == -1) {
                event.setRed((float)this.r1.getValue());
                event.setGreen((float)this.g1.getValue());
                event.setBlue((float)this.b1.getValue());
            }
            else if (AntiFog.mc.player.dimension == 1) {
                event.setRed((float)this.r2.getValue());
                event.setGreen((float)this.g2.getValue());
                event.setBlue((float)this.b2.getValue());
            }
        }
    }
}
