// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.util.OutlineUtils2;
import java.awt.Color;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Timer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.AxisAlignedBB;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.RainbowUtils;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ItemESP extends Module
{
    public final Value<String> mode;
    private final Value<Integer> outlinewidth;
    private final Value<Boolean> rainbow;
    private final Value<Boolean> items;
    private final Value<Boolean> xpbottles;
    private final Value<Boolean> pearls;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    private final Value<Integer> alphaF;
    private final Value<Integer> pred;
    private final Value<Integer> pgreen;
    private final Value<Integer> pblue;
    private final Value<Integer> palpha;
    private final Value<RenderMode> outline;
    private final Value<Integer> width;
    public static ItemESP INSTANCE;
    
    public ItemESP() {
        super("ItemESP", "Highlights items", 0, Category.RENDER, true);
        this.mode = this.register(new Value<String>("Mode", this, "Box", new ArrayList<String>(Arrays.asList("Box", "Text", "Shader", "Chams"))));
        this.outlinewidth = this.register(new Value<Integer>("Shader Width", this, 1, 1, 10));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.items = this.register(new Value<Boolean>("Items", this, true));
        this.xpbottles = this.register(new Value<Boolean>("EXP Bottles", this, true));
        this.pearls = this.register(new Value<Boolean>("Pearls", this, true));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 80, 0, 255));
        this.alphaF = this.register(new Value<Integer>("Full Alpha", this, 80, 0, 255));
        this.pred = this.register(new Value<Integer>("Pearl Red", this, 255, 0, 255));
        this.pgreen = this.register(new Value<Integer>("Pearl Green", this, 255, 0, 255));
        this.pblue = this.register(new Value<Integer>("Pearl Blue", this, 255, 0, 255));
        this.palpha = this.register(new Value<Integer>("Pearl Alpha", this, 255, 0, 255));
        this.outline = this.register(new Value<RenderMode>("Render Mode", this, RenderMode.SOLID, RenderMode.values()));
        this.width = this.register(new Value<Integer>("Width", this, 1, 1, 10));
        ItemESP.INSTANCE = this;
    }
    
    @Override
    public void onDisable() {
        ItemESP.mc.world.loadedEntityList.stream().filter(entity -> (entity instanceof EntityItem && this.items.getValue()) || (entity instanceof EntityExpBottle && this.xpbottles.getValue()) || (entity instanceof EntityEnderPearl && this.pearls.getValue())).forEach(entity -> entity.setGlowing(false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.rainbow.getValue()) {
            final int r = RainbowUtils.r;
            final int g = RainbowUtils.g;
            final int b = RainbowUtils.b;
        }
        else {
            final int r = this.red.getValue();
            final int g = this.green.getValue();
            final int b = this.blue.getValue();
        }
        XuluTessellator.prepare(7);
        RenderManager renderManager;
        Timer timer;
        double x;
        double y;
        double z;
        AxisAlignedBB entityBox;
        AxisAlignedBB axisAlignedBB;
        final int red;
        final int green;
        final int blue;
        ItemESP.mc.world.loadedEntityList.stream().filter(entity -> (entity instanceof EntityItem && this.items.getValue()) || (entity instanceof EntityExpBottle && this.xpbottles.getValue()) || (entity instanceof EntityEnderPearl && this.pearls.getValue())).forEach(entity -> {
            if (this.mode.getValue().equalsIgnoreCase("text")) {
                this.drawText(entity);
                return;
            }
            else if (this.mode.getValue().equalsIgnoreCase("shader")) {
                entity.setGlowing(true);
                return;
            }
            else if (this.mode.getValue().equalsIgnoreCase("chams")) {
                return;
            }
            else {
                entity.setGlowing(false);
                renderManager = ItemESP.mc.renderManager;
                timer = ItemESP.mc.timer;
                x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
                y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
                z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;
                entityBox = entity.getEntityBoundingBox();
                axisAlignedBB = new AxisAlignedBB(entityBox.minX - entity.posX + x - 0.05, entityBox.minY - entity.posY + y, entityBox.minZ - entity.posZ + z - 0.05, entityBox.maxX - entity.posX + x + 0.05, entityBox.maxY - entity.posY + y + 0.15, entityBox.maxZ - entity.posZ + z + 0.05);
                if (entity instanceof EntityEnderPearl) {
                    if (this.rainbow.getValue()) {
                        switch (this.outline.getValue()) {
                            case SOLID: {
                                XuluTessellator.drawBox2(axisAlignedBB, RainbowUtils.r, RainbowUtils.g, RainbowUtils.b, this.palpha.getValue(), 63);
                                break;
                            }
                            case OUTLINE: {
                                XuluTessellator.drawBoundingBox(axisAlignedBB, this.width.getValue(), RainbowUtils.r, RainbowUtils.g, RainbowUtils.b, this.palpha.getValue());
                                break;
                            }
                            case FULL: {
                                XuluTessellator.drawFullBoxAA(axisAlignedBB, this.width.getValue(), RainbowUtils.r, RainbowUtils.g, RainbowUtils.b, this.palpha.getValue(), this.alphaF.getValue());
                                break;
                            }
                        }
                    }
                    else {
                        switch (this.outline.getValue()) {
                            case SOLID: {
                                XuluTessellator.drawBox2(axisAlignedBB, this.pred.getValue(), this.pgreen.getValue(), this.pblue.getValue(), this.palpha.getValue(), 63);
                                break;
                            }
                            case OUTLINE: {
                                XuluTessellator.drawBoundingBox(axisAlignedBB, this.width.getValue(), this.pred.getValue(), this.pgreen.getValue(), this.pblue.getValue(), this.palpha.getValue());
                                break;
                            }
                            case FULL: {
                                XuluTessellator.drawFullBoxAA(axisAlignedBB, this.width.getValue(), this.pred.getValue(), this.pgreen.getValue(), this.pblue.getValue(), this.palpha.getValue(), this.alphaF.getValue());
                                break;
                            }
                        }
                    }
                }
                else {
                    switch (this.outline.getValue()) {
                        case SOLID: {
                            XuluTessellator.drawBox2(axisAlignedBB, red, green, blue, this.alpha.getValue(), 63);
                            break;
                        }
                        case OUTLINE: {
                            XuluTessellator.drawBoundingBox(axisAlignedBB, this.width.getValue(), red, green, blue, this.alpha.getValue());
                            break;
                        }
                        case FULL: {
                            XuluTessellator.drawFullBoxAA(axisAlignedBB, this.width.getValue(), red, green, blue, this.alpha.getValue(), this.alphaF.getValue());
                            break;
                        }
                    }
                }
                return;
            }
        });
        XuluTessellator.release();
    }
    
    private void drawText(final Entity entityIn) {
        GlStateManager.pushMatrix();
        final double scale = 1.0;
        final String name = (entityIn instanceof EntityItem) ? ((EntityItem)entityIn).getItem().getDisplayName() : ((entityIn instanceof EntityEnderPearl) ? "Thrown Ender Pearl" : ((entityIn instanceof EntityExpBottle) ? "Thrown Exp Bottle" : "null"));
        final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entityIn, ItemESP.mc.getRenderPartialTicks());
        final float yAdd = entityIn.height / 2.0f + 0.5f;
        final double x = interp.x;
        final double y = interp.y + yAdd;
        final double z = interp.z;
        final float viewerYaw = ItemESP.mc.getRenderManager().playerViewY;
        final float viewerPitch = ItemESP.mc.getRenderManager().playerViewX;
        final boolean isThirdPersonFrontal = ItemESP.mc.getRenderManager().options.thirdPersonView == 2;
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        final float f = ItemESP.mc.player.getDistance(entityIn);
        final float m = f / 8.0f * (float)Math.pow(1.258925437927246, scale);
        GlStateManager.scale(m, m, m);
        final FontRenderer fontRendererIn = ItemESP.mc.fontRenderer;
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        final String str = name + ((entityIn instanceof EntityItem) ? (" x" + ((EntityItem)entityIn).getItem().getCount()) : "");
        final int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        if (Xulu.CustomFont) {
            Xulu.cFontRenderer.drawStringWithShadow(str, -i, 9.0, ColorUtils.Colors.WHITE);
        }
        else {
            GlStateManager.enableTexture2D();
            fontRendererIn.drawStringWithShadow(str, (float)(-i), 9.0f, ColorUtils.Colors.WHITE);
            GlStateManager.disableTexture2D();
        }
        GlStateManager.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.popMatrix();
    }
    
    public void render1(final float n) {
        RenderHelper.enableStandardItemLighting();
        for (final Entity entity : ItemESP.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                final EntityItem item = (EntityItem)entity;
                GL11.glPushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                ItemESP.mc.getRenderManager().renderShadow = false;
                ItemESP.mc.getRenderManager().renderEntityStatic((Entity)item, n, true);
                GL11.glPopMatrix();
            }
        }
    }
    
    public void render2(final float n) {
        RenderHelper.enableStandardItemLighting();
        int r;
        int g;
        int b;
        if (this.rainbow.getValue()) {
            r = RainbowUtils.r;
            g = RainbowUtils.g;
            b = RainbowUtils.b;
        }
        else {
            r = this.red.getValue();
            g = this.green.getValue();
            b = this.blue.getValue();
        }
        for (final Entity entity : ItemESP.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) {
                final EntityItem item = (EntityItem)entity;
                GlStateManager.pushMatrix();
                OutlineUtils2.setColor(new Color(r, g, b));
                ItemESP.mc.getRenderManager().renderShadow = false;
                ItemESP.mc.getRenderManager().renderEntityStatic((Entity)item, n, true);
                GlStateManager.popMatrix();
            }
        }
    }
    
    private enum RenderMode
    {
        SOLID, 
        OUTLINE, 
        FULL;
    }
}
