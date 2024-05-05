// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.util.render.RenderUtils;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventCrosshair;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Crosshair extends Module
{
    public NumberSetting gapProp;
    public NumberSetting lengthProp;
    public NumberSetting widthProp;
    public BooleanSetting tShapeProp;
    public BooleanSetting dotProp;
    public BooleanSetting dynamicProp;
    public BooleanSetting outlineProp;
    public NumberSetting outlineWidthProp;
    public ColorSetting colorProp;
    private static double lastDist;
    private static double prevLastDist;
    private static double baseMoveSpeed;
    
    public Crosshair() {
        super("Crosshair", 0, "Renders a custom crosshair", Category.RENDER);
        this.gapProp = new NumberSetting("Gap", 1.0, 0.0, 10.0, 0.5);
        this.lengthProp = new NumberSetting("Length", 3.0, 0.0, 10.0, 0.5);
        this.widthProp = new NumberSetting("Width", 1.0, 0.0, 5.0, 0.5);
        this.tShapeProp = new BooleanSetting("T Shape", false);
        this.dotProp = new BooleanSetting("Dot", false);
        this.dynamicProp = new BooleanSetting("Dynamic", true);
        this.outlineProp = new BooleanSetting("Outline", true);
        this.outlineWidthProp = new NumberSetting("Outline Width", 0.5, this.outlineProp::isEnabled, 0.5, 5.0, 0.5);
        this.colorProp = new ColorSetting("Color", new Color(1668818));
        this.addSettings(this.gapProp, this.lengthProp, this.widthProp, this.tShapeProp, this.dynamicProp, this.outlineProp, this.outlineWidthProp, this.colorProp);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventCrosshair) {
            e.setCancelled(true);
        }
        if (e instanceof EventMotion) {
            final EventMotion event = (EventMotion)e;
            if (e.isPre()) {
                Crosshair.baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                final EntityPlayerSP player = Crosshair.mc.thePlayer;
                final double xDif = player.posX - player.lastTickPosX;
                final double zDif = player.posZ - player.lastTickPosZ;
                Crosshair.prevLastDist = Crosshair.lastDist;
                Crosshair.lastDist = StrictMath.sqrt(xDif * xDif + zDif * zDif);
            }
        }
        if (e instanceof EventRender2D) {
            final EventRender2D event2 = (EventRender2D)e;
            final double width = this.widthProp.getValue();
            final double halfWidth = width / 2.0;
            double gap = this.gapProp.getValue();
            if (this.dynamicProp.isEnabled()) {
                gap *= Math.max(Crosshair.mc.thePlayer.isSneaking() ? 0.5 : 1.0, RenderUtils.interpolate(getPrevLastDist(), getLastDist(), event2.getPartialTicks()) * 10.0);
            }
            final double length = this.lengthProp.getValue();
            final int color = this.colorProp.getColor();
            final double outlineWidth = this.outlineWidthProp.getValue();
            final boolean outline = this.outlineProp.isEnabled();
            final boolean tShape = this.tShapeProp.isEnabled();
            final ScaledResolution lr = new ScaledResolution(Crosshair.mc);
            final double middleX = lr.getScaledWidth() / 2.0;
            final double middleY = lr.getScaledHeight() / 2.0;
            if (outline) {
                Gui.drawRect(middleX - gap - length - outlineWidth, middleY - halfWidth - outlineWidth, middleX - gap + outlineWidth, middleY + halfWidth + outlineWidth, -1778384896);
                Gui.drawRect(middleX + gap - outlineWidth, middleY - halfWidth - outlineWidth, middleX + gap + length + outlineWidth, middleY + halfWidth + outlineWidth, -1778384896);
                Gui.drawRect(middleX - halfWidth - outlineWidth, middleY + gap - outlineWidth, middleX + halfWidth + outlineWidth, middleY + gap + length + outlineWidth, -1778384896);
                if (!tShape) {
                    Gui.drawRect(middleX - halfWidth - outlineWidth, middleY - gap - length - outlineWidth, middleX + halfWidth + outlineWidth, middleY - gap + outlineWidth, -1778384896);
                }
            }
            Gui.drawRect(middleX - gap - length, middleY - halfWidth, middleX - gap, middleY + halfWidth, color);
            Gui.drawRect(middleX + gap, middleY - halfWidth, middleX + gap + length, middleY + halfWidth, color);
            Gui.drawRect(middleX - halfWidth, middleY + gap, middleX + halfWidth, middleY + gap + length, color);
            if (!tShape) {
                Gui.drawRect(middleX - halfWidth, middleY - gap - length, middleX + halfWidth, middleY - gap, color);
            }
        }
    }
    
    public static double getPrevLastDist() {
        return Crosshair.prevLastDist;
    }
    
    public static double getLastDist() {
        return Crosshair.lastDist;
    }
    
    public static double getBaseMoveSpeed() {
        return Crosshair.baseMoveSpeed;
    }
}
