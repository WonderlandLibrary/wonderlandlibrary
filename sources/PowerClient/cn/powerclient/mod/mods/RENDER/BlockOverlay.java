/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.events.EventRender;
import me.AveReborn.events.EventRender2D;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

public class BlockOverlay
extends Mod {
    public Value<Double> r = new Value<Double>("BlockOverlay_Red", 255.0, 0.0, 255.0, 1.0);
    public Value<Double> g = new Value<Double>("BlockOverlay_Green", 255.0, 0.0, 255.0, 1.0);
    public Value<Double> b = new Value<Double>("BlockOverlay_Blue", 255.0, 0.0, 255.0, 1.0);
    public Value<Boolean> togg = new Value<Boolean>("BlockOverlay_RenderString", true);

    public BlockOverlay() {
        super("BlockOverlay", Category.RENDER);
    }

    public int getRed() {
        return this.r.getValueState().intValue();
    }

    public int getGreen() {
        return this.g.getValueState().intValue();
    }

    public int getBlue() {
        return this.b.getValueState().intValue();
    }

    public boolean getRender() {
        return this.togg.getValueState();
    }

    @EventTarget
    public void onRender(EventRender2D event) {
        if (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            FontRenderer fr2 = this.mc.fontRendererObj;
            BlockPos pos = this.mc.objectMouseOver.getBlockPos();
            Block block = this.mc.theWorld.getBlockState(pos).getBlock();
            int id2 = Block.getIdFromBlock(block);
            String s2 = String.valueOf(String.valueOf(block.getLocalizedName())) + " ID:" + id2;
            String s22 = block.getLocalizedName();
            String s3 = " ID:" + id2;
            if (this.mc.objectMouseOver != null && this.getRender()) {
                ScaledResolution res = new ScaledResolution(this.mc);
                int x2 = res.getScaledWidth() / 2 + 10;
                int y2 = res.getScaledHeight() / 2 + 2;
                Gui.drawRect((float)x2, (float)y2, (float)(x2 + Client.fontManager.tahoma17.getStringWidth(s2) + 3), (float)(y2 + fr2.FONT_HEIGHT) + 0.5f, ClientUtil.reAlpha(Colors.BLACK.c, 1.0f));
                Client.fontManager.tahoma17.drawStringWithShadow(s22, (float)x2 + 1.0f, y2, Colors.WHITE.c);
                Client.fontManager.tahoma17.drawStringWithShadow(s3, (float)(x2 + Client.fontManager.tahoma17.getStringWidth(s22)) + 1.0f, y2, Colors.GREY.c);
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender event) {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = this.mc.objectMouseOver.getBlockPos();
            Block block = this.mc.theWorld.getBlockState(pos).getBlock();
            String s2 = block.getLocalizedName();
            this.mc.getRenderManager();
            this.mc.getRenderManager();
            double x2 = (double)pos.getX() - RenderManager.renderPosX;
            this.mc.getRenderManager();
            this.mc.getRenderManager();
            double y2 = (double)pos.getY() - RenderManager.renderPosY;
            this.mc.getRenderManager();
            this.mc.getRenderManager();
            double z2 = (double)pos.getZ() - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glColor4f((float)this.getRed() / 255.0f, (float)this.getGreen() / 255.0f, (float)this.getBlue() / 255.0f, 0.15f);
            double minX = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinX();
            double minY = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinY();
            double minZ = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinZ();
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x2 + minX, y2 + minY, z2 + minZ, x2 + block.getBlockBoundsMaxX(), y2 + block.getBlockBoundsMaxY(), z2 + block.getBlockBoundsMaxZ()));
            GL11.glColor4f((float)this.getRed() / 255.0f, (float)this.getGreen() / 255.0f, (float)this.getBlue() / 255.0f, 1.0f);
            GL11.glLineWidth(0.5f);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x2 + minX, y2 + minY, z2 + minZ, x2 + block.getBlockBoundsMaxX(), y2 + block.getBlockBoundsMaxY(), z2 + block.getBlockBoundsMaxZ()));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}

