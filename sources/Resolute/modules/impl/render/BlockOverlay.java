// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MovingObjectPosition;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class BlockOverlay extends Module
{
    public static ModeSetting mode;
    public static ColorSetting color;
    public static boolean enabled;
    
    public BlockOverlay() {
        super("BlockOverlay", 0, "Highlights what you are looking at", Category.RENDER);
        this.addSettings(BlockOverlay.color);
    }
    
    @Override
    public void onEnable() {
        BlockOverlay.enabled = true;
    }
    
    @Override
    public void onDisable() {
        BlockOverlay.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D && BlockOverlay.mode.is("Filled")) {
            if (BlockOverlay.mc.objectMouseOver != null && BlockOverlay.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos pos = BlockOverlay.mc.objectMouseOver.getBlockPos();
                final Block block = BlockOverlay.mc.theWorld.getBlockState(pos).getBlock();
                final RenderManager renderManager = BlockOverlay.mc.getRenderManager();
                final String s = block.getLocalizedName();
                BlockOverlay.mc.getRenderManager();
                final double x = pos.getX() - renderManager.getRenderPosX();
                BlockOverlay.mc.getRenderManager();
                final double y = pos.getY() - renderManager.getRenderPosY();
                BlockOverlay.mc.getRenderManager();
                final double z = pos.getZ() - renderManager.getRenderPosZ();
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                final Color c = BlockOverlay.color.getValue();
                final int r = c.getRed();
                final int g = c.getGreen();
                final int b = c.getBlue();
                RenderUtils.glColor(new Color(r, g, b, 50).getRGB());
                final double minX = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinX();
                final double minY = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinY();
                final double minZ = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinZ();
                RenderUtils.drawBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
                RenderUtils.glColor(new Color(r, g, b).getRGB());
                GL11.glLineWidth(0.5f);
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    public static float getOutlineAlpha() {
        final int combinedColor = BlockOverlay.color.getValue().getRGB();
        return (combinedColor >> 25 & 0xFF) / 255.0f;
    }
    
    public static int getOutlineColor() {
        final int combinedColor = BlockOverlay.color.getValue().getRGB();
        return combinedColor;
    }
    
    static {
        BlockOverlay.mode = new ModeSetting("Mode", "Filled", new String[] { "Filled" });
        BlockOverlay.color = new ColorSetting("Color", new Color(0, 90, 255));
        BlockOverlay.enabled = false;
    }
}
