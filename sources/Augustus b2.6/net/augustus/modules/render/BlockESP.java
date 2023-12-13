// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.utils.RenderUtil;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.augustus.events.EventRenderChunk;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.block.Block;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.RainbowUtil;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class BlockESP extends Module
{
    private static final ArrayList<BlockPos> POSITIONS;
    private final RainbowUtil rainbowUtil;
    public ColorSetting color;
    public BooleanValue rainbow;
    public DoubleValue rainbowSpeed;
    public DoubleValue rainbowAlpha;
    public DoubleValue lineWidth;
    public DoubleValue distance;
    public DoubleValue id;
    
    public BlockESP() {
        super("BlockESP", new Color(171, 9, 41), Categorys.RENDER);
        this.rainbowUtil = new RainbowUtil();
        this.color = new ColorSetting(2, "Color", this, new Color(21, 121, 230, 65));
        this.rainbow = new BooleanValue(11, "Rainbow", this, false);
        this.rainbowSpeed = new DoubleValue(12, "RainbowSpeed", this, 55.0, 0.0, 1000.0, 0);
        this.rainbowAlpha = new DoubleValue(13, "RainbowAlpha", this, 80.0, 0.0, 255.0, 0);
        this.lineWidth = new DoubleValue(1, "LineWidth", this, 6.0, 0.0, 15.0, 0);
        this.distance = new DoubleValue(3, "Distance", this, 50.0, 1.0, 300.0, 0);
        this.id = new DoubleValue(4, "ID", this, 26.0, 1.0, 400.0, 0);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        BlockESP.POSITIONS.clear();
        BlockESP.mc.renderGlobal.loadRenderers();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        for (int i = BlockESP.POSITIONS.size() - 1; i >= 0; --i) {
            final BlockPos position = BlockESP.POSITIONS.get(i);
            if (!BlockESP.mc.theWorld.getBlockState(position).getBlock().equals(Block.getBlockById((int)this.id.getValue()))) {
                BlockESP.POSITIONS.remove(i);
            }
        }
    }
    
    @EventTarget
    public void onEventRenderChunk(final EventRenderChunk event) {
        if (event.getiBlockState().getBlock().equals(Block.getBlockById((int)this.id.getValue())) && !BlockESP.POSITIONS.contains(event.getBlockPos())) {
            BlockESP.POSITIONS.add(event.getBlockPos());
        }
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (BlockESP.mc.thePlayer == null || BlockESP.mc.theWorld == null) {
            return;
        }
        if (this.rainbow.getBoolean()) {
            this.rainbowUtil.updateRainbow((this.rainbowSpeed.getValue() == 1000.0) ? ((float)(this.rainbowSpeed.getValue() * 9.999999747378752E-6)) : ((float)(this.rainbowSpeed.getValue() * 9.999999974752427E-7)), (int)this.rainbowAlpha.getValue());
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GlStateManager.disableCull();
        GL11.glDepthMask(false);
        final float red = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getRed() / 255.0f) : (this.color.getColor().getRed() / 225.0f);
        final float green = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getGreen() / 255.0f) : (this.color.getColor().getGreen() / 225.0f);
        final float blue = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getBlue() / 255.0f) : (this.color.getColor().getBlue() / 225.0f);
        final float alpha = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getAlpha() / 255.0f) : (this.color.getColor().getAlpha() / 225.0f);
        for (final BlockPos blockPos : BlockESP.POSITIONS) {
            this.render(blockPos, red, green, blue, alpha);
        }
        GL11.glDepthMask(true);
        GlStateManager.enableCull();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2848);
    }
    
    private void render(final BlockPos blockPos, final float red, final float green, final float blue, final float alpha) {
        float lineWidth = (float)(this.lineWidth.getValue() / 2.0);
        if (BlockESP.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > 1.0) {
            double d0 = 1.0 - BlockESP.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) / 20.0;
            if (d0 < 0.3) {
                d0 = 0.3;
            }
            lineWidth *= (float)d0;
        }
        RenderUtil.drawBlockESP(blockPos, red, green, blue, alpha, 1.0f, lineWidth);
    }
    
    static {
        POSITIONS = new ArrayList<BlockPos>();
    }
}
