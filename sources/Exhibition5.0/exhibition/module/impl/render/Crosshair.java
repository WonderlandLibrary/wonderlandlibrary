// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.RegisterEvent;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.management.ColorManager;
import net.minecraft.client.gui.ScaledResolution;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Crosshair extends Module
{
    private String GAP;
    private String WIDTH;
    private String SIZE;
    private String DYNAMIC;
    
    public Crosshair(final ModuleData data) {
        super(data);
        this.GAP = "GAP";
        this.WIDTH = "WIDTH";
        this.SIZE = "SIZE";
        this.DYNAMIC = "DYNAMIC";
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.DYNAMIC, new Setting<Boolean>(this.DYNAMIC, true, "Expands when moving."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.GAP, new Setting<Integer>(this.GAP, 5, "Crosshair Gap", 0.25, 0.25, 15.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.WIDTH, new Setting<Integer>(this.WIDTH, 2, "Crosshair Width", 0.25, 0.25, 10.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.SIZE, new Setting<Integer>(this.SIZE, 7, "Crosshair Size/Length", 0.25, 0.25, 15.0));
    }
    
    @RegisterEvent(events = { EventRenderGui.class })
    @Override
    public void onEvent(final Event event) {
        final double gap = ((HashMap<K, Setting<Number>>)this.settings).get(this.GAP).getValue().doubleValue();
        final double width = ((HashMap<K, Setting<Number>>)this.settings).get(this.WIDTH).getValue().doubleValue();
        final double size = ((HashMap<K, Setting<Number>>)this.settings).get(this.SIZE).getValue().doubleValue();
        final ScaledResolution scaledRes = new ScaledResolution(Crosshair.mc, Crosshair.mc.displayWidth, Crosshair.mc.displayHeight);
        RenderingUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 - gap - size - (this.isMoving() ? 2 : 0), scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 - gap - (this.isMoving() ? 2 : 0), 0.5, ColorManager.xhair.getColorInt(), Colors.getColor(0, 0, 0, ColorManager.xhair.alpha));
        RenderingUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 + gap + 1.0 + (this.isMoving() ? 2 : 0) - 0.15, scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 + 1 + gap + size + (this.isMoving() ? 2 : 0) - 0.15, 0.5, ColorManager.xhair.getColorInt(), Colors.getColor(0, 0, 0, ColorManager.xhair.alpha));
        RenderingUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 - gap - (this.isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, ColorManager.xhair.getColorInt(), Colors.getColor(0, 0, 0, ColorManager.xhair.alpha));
        RenderingUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (this.isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, ColorManager.xhair.getColorInt(), Colors.getColor(0, 0, 0, ColorManager.xhair.alpha));
    }
    
    public boolean isMoving() {
        return ((HashMap<K, Setting<Boolean>>)this.settings).get(this.DYNAMIC).getValue() && !Crosshair.mc.thePlayer.isCollidedHorizontally && !Crosshair.mc.thePlayer.isSneaking() && (Crosshair.mc.thePlayer.movementInput.moveForward != 0.0f || Crosshair.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
}
