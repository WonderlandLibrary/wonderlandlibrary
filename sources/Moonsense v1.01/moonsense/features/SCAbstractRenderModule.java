// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features;

import moonsense.config.utils.AnchorPoint;
import moonsense.settings.Setting;
import net.minecraft.client.Minecraft;

public abstract class SCAbstractRenderModule extends SCModule
{
    protected final Minecraft mc;
    public final Setting scale;
    public final Setting showWhileTyping;
    
    public SCAbstractRenderModule(final String displayName, final String description) {
        this(displayName, description, -1);
    }
    
    public SCAbstractRenderModule(final String displayName, final String description, final int textureIndex) {
        super(displayName, description, textureIndex);
        this.scale = new Setting(this, "Scale").setDefault(1.0f).setRange(0.5f, 2.0f, 0.01f);
        new Setting(this, "General Options");
        this.showWhileTyping = new Setting(this, "Show While Typing").setDefault(true);
        this.mc = Minecraft.getMinecraft();
    }
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public abstract void render(final float p0, final float p1);
    
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    public abstract AnchorPoint getDefaultPosition();
    
    public final float getScale() {
        return this.scale.getFloat();
    }
}
