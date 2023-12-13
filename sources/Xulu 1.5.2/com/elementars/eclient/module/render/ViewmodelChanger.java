// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ViewmodelChanger extends Module
{
    public final Value<HandMode> mode;
    public final Value<Boolean> pause;
    public final Value<Boolean> hand;
    public final Value<Boolean> item;
    public final Value<Float> sizex;
    public final Value<Float> sizey;
    public final Value<Float> sizez;
    public final Value<Float> x;
    public final Value<Float> y;
    public final Value<Float> z;
    public final Value<Float> posX;
    public final Value<Float> posY;
    public final Value<Float> posZ;
    public final Value<Float> alpha;
    
    public ViewmodelChanger() {
        super("ViewmodelChanger", "Tweaks the players hand", 0, Category.RENDER, true);
        this.mode = this.register(new Value<HandMode>("Mode", this, HandMode.MAINHAND, HandMode.values()));
        this.pause = this.register(new Value<Boolean>("Pause On Eat", this, false));
        this.hand = this.register(new Value<Boolean>("Hand", this, true));
        this.item = this.register(new Value<Boolean>("Item", this, true));
        this.sizex = this.register(new Value<Float>("Size X", this, 1.0f, 0.0f, 2.0f));
        this.sizey = this.register(new Value<Float>("Size Y", this, 1.0f, 0.0f, 2.0f));
        this.sizez = this.register(new Value<Float>("Size Z", this, 1.0f, 0.0f, 2.0f));
        this.x = this.register(new Value<Float>("x", this, 1.0f, 0.0f, 1.0f));
        this.y = this.register(new Value<Float>("y", this, 1.0f, 0.0f, 1.0f));
        this.z = this.register(new Value<Float>("z", this, 1.0f, 0.0f, 1.0f));
        this.posX = this.register(new Value<Float>("X offset", this, 0.0f, -2.0f, 2.0f));
        this.posY = this.register(new Value<Float>("Y offset", this, 0.0f, -2.0f, 2.0f));
        this.posZ = this.register(new Value<Float>("Z offset", this, 0.0f, -2.0f, 2.0f));
        this.alpha = this.register(new Value<Float>("Alpha", this, 1.0f, 0.0f, 1.0f));
    }
    
    @Override
    public void onEnable() {
        ViewmodelChanger.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        ViewmodelChanger.EVENT_BUS.unregister((Object)this);
    }
    
    public enum HandMode
    {
        MAINHAND, 
        OFFHAND, 
        BOTH;
        
        public boolean isOK(final boolean isOffhand) {
            switch (this) {
                case MAINHAND: {
                    return !isOffhand;
                }
                case OFFHAND: {
                    return isOffhand;
                }
                case BOTH: {
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
    }
}
