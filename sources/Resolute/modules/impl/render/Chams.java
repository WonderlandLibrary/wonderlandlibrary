// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Chams extends Module
{
    public static ModeSetting mode;
    public static BooleanSetting hurtEffect;
    public static ColorSetting hurtColor;
    public static NumberSetting hurtAlpha;
    public static BooleanSetting handsProp;
    public static ColorSetting handsColor;
    public static NumberSetting handsAlpha;
    public static BooleanSetting visibleFlat;
    public static BooleanSetting occludedFlat;
    public static ModeSetting visibleColorMode;
    public static ModeSetting occludedColorMode;
    public static NumberSetting visibleAlpha;
    public static NumberSetting occludedAlpha;
    public static ColorSetting visibleColor;
    public static ColorSetting obstructedColor;
    public static ColorSetting secondVisibleColor;
    public static ColorSetting secondObstructedColor;
    public static boolean enabled;
    
    public Chams() {
        super("Chams", 0, "Renders entities behind walls", Category.RENDER);
        this.addSettings(Chams.mode, Chams.hurtEffect, Chams.hurtColor, Chams.hurtAlpha, Chams.handsProp, Chams.handsColor, Chams.handsAlpha, Chams.visibleFlat, Chams.occludedFlat, Chams.visibleColorMode, Chams.occludedColorMode, Chams.visibleAlpha, Chams.occludedAlpha, Chams.visibleColor, Chams.obstructedColor, Chams.secondVisibleColor, Chams.secondObstructedColor);
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("Blend");
    }
    
    @Override
    public void onEnable() {
        Chams.enabled = true;
    }
    
    @Override
    public void onDisable() {
        Chams.enabled = false;
    }
    
    public static void preRenderOccluded(final int occludedColor, final boolean occludedFlat) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        if (occludedFlat) {
            GL11.glDisable(2896);
        }
        GL11.glEnable(32823);
        GL11.glPolygonOffset(0.0f, -1000000.0f);
        OpenGlHelper.setLightmapTextureCoords(1, 240.0f, 240.0f);
        GL11.glDepthMask(false);
        RenderUtils.color(occludedColor);
    }
    
    public static void preRenderVisible(final int visibleColor, final boolean visibleFlat, final boolean occludedFlat) {
        GL11.glDepthMask(true);
        if (occludedFlat && !visibleFlat) {
            GL11.glEnable(2896);
        }
        else if (!occludedFlat && visibleFlat) {
            GL11.glDisable(2896);
        }
        RenderUtils.color(visibleColor);
        GL11.glDisable(32823);
    }
    
    public static void postRender(final boolean visibleFlat) {
        if (visibleFlat) {
            GL11.glEnable(2896);
        }
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static boolean isValid(final EntityLivingBase entity) {
        return !entity.isInvisible() && entity.isEntityAlive() && entity instanceof EntityPlayer;
    }
    
    static {
        Chams.mode = new ModeSetting("Mode", "Blend", new String[] { "Blend" });
        Chams.hurtEffect = new BooleanSetting("Hurt Effect", true);
        Chams.hurtColor = new ColorSetting("Hurt Color", new Color(255, 0, 0), Chams.hurtEffect::isEnabled);
        Chams.hurtAlpha = new NumberSetting("Hurt Alpha", 1.0, Chams.hurtEffect::isEnabled, 0.0, 1.0, 0.1);
        Chams.handsProp = new BooleanSetting("Hands", true);
        Chams.handsColor = new ColorSetting("Hands Color", new Color(16007990), Chams.handsProp::isEnabled);
        Chams.handsAlpha = new NumberSetting("Hands Alpha", 0.3, Chams.handsProp::isEnabled, 0.1, 1.0, 0.1);
        Chams.visibleFlat = new BooleanSetting("Visible Flat", true);
        Chams.occludedFlat = new BooleanSetting("Occluded Flat", true);
        Chams.visibleColorMode = new ModeSetting("Visible Color", "Static", new String[] { "Static", "Rainbow", "Pulsing" });
        Chams.occludedColorMode = new ModeSetting("Occluded Color", "Static", new String[] { "Static", "Rainbow", "Pulsing" });
        Chams.visibleAlpha = new NumberSetting("Visible Alpha", 1.0, 0.0, 1.0, 0.1);
        Chams.occludedAlpha = new NumberSetting("Obstructed Alpha", 0.4, 0.0, 1.0, 0.1);
        Chams.visibleColor = new ColorSetting("Visible", new Color(0, 255, 133));
        Chams.obstructedColor = new ColorSetting("Obstructed", new Color(255, 191, 226));
        Chams.secondVisibleColor = new ColorSetting("Second Visible", new Color(16007990), () -> Chams.visibleColorMode.is("Pulsing"));
        Chams.secondObstructedColor = new ColorSetting("Second Obstructed", new Color(65350), () -> Chams.visibleColorMode.is("Pulsing"));
        Chams.enabled = false;
    }
}
