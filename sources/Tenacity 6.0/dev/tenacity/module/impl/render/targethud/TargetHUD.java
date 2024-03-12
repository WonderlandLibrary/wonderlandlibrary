// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render.targethud;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.HashMap;
import dev.tenacity.utils.objects.GradientColorWheel;
import dev.tenacity.utils.Utils;

public abstract class TargetHUD implements Utils
{
    protected GradientColorWheel colorWheel;
    private float width;
    private float height;
    private final String name;
    private static final HashMap<Class<? extends TargetHUD>, TargetHUD> targetHuds;
    
    public TargetHUD(final String name) {
        this.name = name;
    }
    
    public void setColorWheel(final GradientColorWheel colorWheel) {
        this.colorWheel = colorWheel;
    }
    
    protected void renderPlayer2D(final float x, final float y, final float width, final float height, final AbstractClientPlayer player) {
        GLUtil.startBlend();
        TargetHUD.mc.getTextureManager().bindTexture(player.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8.0f, 8.0f, width, height, 64.0f, 64.0f);
        GLUtil.endBlend();
    }
    
    public abstract void render(final float p0, final float p1, final float p2, final EntityLivingBase p3);
    
    public abstract void renderEffects(final float p0, final float p1, final float p2, final boolean p3);
    
    public static TargetHUD get(final String name) {
        return TargetHUD.targetHuds.values().stream().filter(hud -> hud.getName().equals(name)).findFirst().orElse(null);
    }
    
    public static <T extends TargetHUD> T get(final Class<T> clazz) {
        return (T)TargetHUD.targetHuds.get(clazz);
    }
    
    public static void init() {
        TargetHUD.targetHuds.put(AdaptTargetHUD.class, new AdaptTargetHUD());
        TargetHUD.targetHuds.put(AkrienTargetHUD.class, new AkrienTargetHUD());
        TargetHUD.targetHuds.put(AstolfoTargetHUD.class, new AstolfoTargetHUD());
        TargetHUD.targetHuds.put(AutoDoxTargetHUD.class, new AutoDoxTargetHUD());
        TargetHUD.targetHuds.put(AdaptOtherTargetHUD.class, new AdaptOtherTargetHUD());
        TargetHUD.targetHuds.put(ExhiTargetHUD.class, new ExhiTargetHUD());
        TargetHUD.targetHuds.put(ExireTargetHUD.class, new ExireTargetHUD());
        TargetHUD.targetHuds.put(NovolineTargetHUD.class, new NovolineTargetHUD());
        TargetHUD.targetHuds.put(OldTenacityTargetHUD.class, new OldTenacityTargetHUD());
        TargetHUD.targetHuds.put(RiseTargetHUD.class, new RiseTargetHUD());
        TargetHUD.targetHuds.put(VapeTargetHUD.class, new VapeTargetHUD());
        TargetHUD.targetHuds.put(TenacityTargetHUD.class, new TenacityTargetHUD());
    }
    
    public void setWidth(final float width) {
        this.width = width;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public String getName() {
        return this.name;
    }
    
    static {
        targetHuds = new HashMap<Class<? extends TargetHUD>, TargetHUD>();
    }
}
