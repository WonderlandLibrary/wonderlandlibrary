// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import vip.Resolute.util.render.OutlineUtils;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class ChestESP extends Module
{
    public static ModeSetting mode;
    public BooleanSetting outlineProp;
    public ColorSetting colorProp;
    public NumberSetting alphaProp;
    public static ColorSetting visibleColor;
    public static ColorSetting occludedColor;
    public static BooleanSetting visibleFlat;
    public static BooleanSetting occludedFlat;
    public static boolean enabled;
    
    public ChestESP() {
        super("ChestESP", 0, "Highlights any chest", Category.RENDER);
        this.outlineProp = new BooleanSetting("Outline", false, () -> ChestESP.mode.is("Box"));
        this.colorProp = new ColorSetting("Color", new Color(8158463), () -> ChestESP.mode.is("Box"));
        this.alphaProp = new NumberSetting("Alpha", 40.0, 1.0, 255.0, 1.0);
        this.addSettings(ChestESP.mode, this.outlineProp, this.colorProp, this.alphaProp, ChestESP.visibleColor, ChestESP.occludedColor, ChestESP.visibleFlat, ChestESP.occludedFlat);
    }
    
    @Override
    public void onEnable() {
        ChestESP.enabled = true;
    }
    
    @Override
    public void onDisable() {
        ChestESP.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            final EventRender3D event = (EventRender3D)e;
            if (ChestESP.mode.is("Box")) {
                for (final TileEntity entity : ChestESP.mc.theWorld.loadedTileEntityList) {
                    if (entity instanceof TileEntityChest) {
                        final BlockPos pos = entity.getPos();
                        final AxisAlignedBB bb = entity.getBlockType().getCollisionBoundingBox(ChestESP.mc.theWorld, pos, entity.getBlockType().getStateFromMeta(entity.getBlockMetadata()));
                        if (bb == null) {
                            continue;
                        }
                        GL11.glDisable(2929);
                        RenderUtils.enableBlending();
                        GL11.glDepthMask(false);
                        GL11.glDisable(3553);
                        RenderUtils.color(this.colorProp.getValue().getRed() / 255, this.colorProp.getValue().getGreen() / 255, this.colorProp.getValue().getBlue() / 255, this.alphaProp.getValue() / 255.0);
                        final boolean outline = this.outlineProp.isEnabled();
                        if (outline) {
                            GL11.glLineWidth(1.0f);
                            GL11.glEnable(2848);
                            GL11.glHint(3154, 4354);
                        }
                        final double rX = RenderManager.renderPosX;
                        final double rY = RenderManager.renderPosY;
                        final double rZ = RenderManager.renderPosZ;
                        GL11.glTranslated(-rX, -rY, -rZ);
                        RenderGlobal.func_181561_a(bb, outline, true);
                        GL11.glTranslated(rX, rY, rZ);
                        GL11.glEnable(2929);
                        RenderUtils.disableBlending();
                        GL11.glDepthMask(true);
                        if (outline) {
                            GL11.glDisable(2848);
                        }
                        GL11.glEnable(3553);
                    }
                }
            }
            if (ChestESP.mode.is("Filled")) {
                try {
                    for (final TileEntity entity2 : ChestESP.mc.theWorld.loadedTileEntityList) {
                        if (entity2 instanceof TileEntityChest || entity2 instanceof TileEntityEnderChest) {
                            OutlineUtils.renderOne(2.5f);
                            TileEntityRendererDispatcher.instance.renderTileEntity(entity2, event.getPartialTicks(), -1);
                            OutlineUtils.renderTwo();
                            TileEntityRendererDispatcher.instance.renderTileEntity(entity2, event.getPartialTicks(), -1);
                            OutlineUtils.renderThree();
                            TileEntityRendererDispatcher.instance.renderTileEntity(entity2, event.getPartialTicks(), -1);
                            OutlineUtils.renderFour(Color.cyan);
                            TileEntityRendererDispatcher.instance.renderTileEntity(entity2, event.getPartialTicks(), -1);
                            OutlineUtils.renderFive();
                            OutlineUtils.setColor(Color.white);
                        }
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    public static void preOccludedRender(final int occludedColor, final boolean occludedFlat) {
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
    
    public static void preVisibleRender(final int visibleColor, final boolean visibleFlat, final boolean occludedFlat) {
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
    
    static {
        ChestESP.mode = new ModeSetting("Mode", "Filled", new String[] { "Filled", "Chams", "Box" });
        ChestESP.visibleColor = new ColorSetting("Visible Color", new Color(12216520), () -> ChestESP.mode.is("Chams"));
        ChestESP.occludedColor = new ColorSetting("Occluded Color", new Color(-1753449217), () -> ChestESP.mode.is("Chams"));
        ChestESP.visibleFlat = new BooleanSetting("Visible Flat", false, () -> ChestESP.mode.is("Chams"));
        ChestESP.occludedFlat = new BooleanSetting("Occluded Flat", true, () -> ChestESP.mode.is("Chams"));
        ChestESP.enabled = false;
    }
}
