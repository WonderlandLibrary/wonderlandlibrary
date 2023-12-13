// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.utils.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.settings.ColorSetting;
import net.minecraft.tileentity.TileEntity;
import java.util.ArrayList;
import net.augustus.utils.RainbowUtil;
import net.augustus.modules.Module;

public class StorageESP extends Module
{
    private final RainbowUtil rainbowUtil;
    private final ArrayList<TileEntity> tileEntities;
    private final ArrayList<TileEntity> notRender;
    public ColorSetting color;
    public StringValue mode;
    public BooleanValue rainbow;
    public DoubleValue rainbowSpeed;
    public DoubleValue rainbowAlpha;
    public DoubleValue lineWidth;
    public DoubleValue renderDistance;
    
    public StorageESP() {
        super("StorageESP", new Color(100, 166, 148, 255), Categorys.RENDER);
        this.rainbowUtil = new RainbowUtil();
        this.tileEntities = new ArrayList<TileEntity>();
        this.notRender = new ArrayList<TileEntity>();
        this.color = new ColorSetting(2, "Color", this, new Color(21, 121, 230, 65));
        this.mode = new StringValue(3, "Mode", this, "Box", new String[] { "Box", "OtherBox", "FakeCorner", "Fake2D" });
        this.rainbow = new BooleanValue(11, "Rainbow", this, false);
        this.rainbowSpeed = new DoubleValue(12, "RainbowSpeed", this, 55.0, 0.0, 1000.0, 0);
        this.rainbowAlpha = new DoubleValue(13, "RainbowAlpha", this, 80.0, 0.0, 255.0, 0);
        this.lineWidth = new DoubleValue(1, "LineWidth", this, 6.0, 0.0, 15.0, 0);
        this.renderDistance = new DoubleValue(4, "MaxDistance", this, 6.0, 0.0, 32.0, 0);
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (StorageESP.mc.theWorld == null) {
            return;
        }
        this.setDisplayName(this.getName() + " §8" + this.mode.getSelected());
        this.tileEntities.clear();
        this.notRender.clear();
        for (final TileEntity tileEntity : StorageESP.mc.theWorld.loadedTileEntityList) {
            if ((tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) && StorageESP.mc.thePlayer.getDistance(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < this.renderDistance.getValue() * 16.0) {
                if (tileEntity instanceof TileEntityChest) {
                    final TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
                    if (tileEntityChest.adjacentChestXNeg != null && !this.tileEntities.contains(tileEntityChest.adjacentChestXNeg)) {
                        this.notRender.add(tileEntityChest.adjacentChestXNeg);
                    }
                    else if (tileEntityChest.adjacentChestZNeg != null && !this.tileEntities.contains(tileEntityChest.adjacentChestZNeg)) {
                        this.notRender.add(tileEntityChest.adjacentChestZNeg);
                    }
                    else if (tileEntityChest.adjacentChestZPos != null && !this.tileEntities.contains(tileEntityChest.adjacentChestZPos)) {
                        this.notRender.add(tileEntityChest.adjacentChestZPos);
                    }
                    else if (tileEntityChest.adjacentChestXPos != null && !this.tileEntities.contains(tileEntityChest.adjacentChestXPos)) {
                        this.notRender.add(tileEntityChest.adjacentChestXPos);
                    }
                }
                this.tileEntities.add(tileEntity);
            }
        }
        this.tileEntities.removeIf(this.notRender::contains);
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
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
        for (final TileEntity blockEntity : this.tileEntities) {
            this.render(blockEntity, red, green, blue, alpha);
        }
        GL11.glDepthMask(true);
        GlStateManager.enableCull();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2848);
    }
    
    private void render(final TileEntity tileEntity, final float red, final float green, final float blue, final float alpha) {
        float lineWidth = (float)(this.lineWidth.getValue() / 2.0);
        if (StorageESP.mc.thePlayer.getDistance(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) > 1.0) {
            double d0 = 1.0 - StorageESP.mc.thePlayer.getDistance(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) / 20.0;
            if (d0 < 0.3) {
                d0 = 0.3;
            }
            lineWidth *= (float)d0;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Box": {
                RenderUtil.drawBlockESP(tileEntity, red, green, blue, alpha, 1.0f, lineWidth, false);
                break;
            }
            case "OtherBox": {
                RenderUtil.drawBlockESP(tileEntity, red, green, blue, alpha, 1.0f, lineWidth, true);
                break;
            }
            case "FakeCorner": {
                RenderUtil.drawCornerESP(tileEntity, red, green, blue);
                break;
            }
            case "Fake2D": {
                RenderUtil.drawFake2DESP(tileEntity, red, green, blue);
                break;
            }
        }
    }
}
