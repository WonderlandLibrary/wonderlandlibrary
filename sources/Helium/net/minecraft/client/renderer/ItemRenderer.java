package net.minecraft.client.renderer;

import net.minecraft.client.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.block.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.entity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.storage.*;
import rip.helium.*;
import rip.helium.cheat.impl.combat.aura.*;
import rip.helium.cheat.impl.visual.*;
import net.minecraft.item.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.texture.*;

public class ItemRenderer
{
    private static final ResourceLocation RES_MAP_BACKGROUND;
    private static final ResourceLocation RES_UNDERWATER_OVERLAY;
    private final Minecraft mc;
    private ItemStack itemToRender;
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private int equippedItemSlot;

    private float rotate;
    
    static {
        RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
        RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    }
    
    public ItemRenderer(final Minecraft mcIn) {
        this.equippedItemSlot = -1;
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }
    
    public void renderItem(final EntityLivingBase entityIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform) {
        if (heldStack != null) {
            final Item item = heldStack.getItem();
            final Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                if (this.isBlockTranslucent(block)) {
                    GlStateManager.depthMask(false);
                }
            }
            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
            if (this.isBlockTranslucent(block)) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private boolean isBlockTranslucent(final Block blockIn) {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    private void func_178101_a(final float angle, final float p_178101_2_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(p_178101_2_, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    private void func_178109_a(final AbstractClientPlayer clientPlayer) {
        final int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        final float f = (float)(i & 0xFFFF);
        final float f2 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
    }
    
    private void func_178110_a(final EntityPlayerSP entityplayerspIn, final float partialTicks) {
        final float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        final float f2 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayerspIn.rotationYaw - f2) * 0.1f, 0.0f, 1.0f, 0.0f);
    }
    
    private float func_178100_c(final float p_178100_1_) {
        float f = 1.0f - p_178100_1_ / 45.0f + 0.1f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * 3.1415927f) * 0.5f + 0.5f;
        return f;
    }
    
    private void renderRightArm(final RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(64.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-62.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.25f, -0.85f, 0.75f);
        renderPlayerIn.renderRightArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }
    
    private void renderLeftArm(final RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.3f, -1.1f, 0.45f);
        renderPlayerIn.renderLeftArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }
    
    private void renderPlayerArms(final AbstractClientPlayer clientPlayer) {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        final RenderPlayer renderplayer = (RenderPlayer)render;
        if (!clientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }
    
    private void renderItemMap(final AbstractClientPlayer clientPlayer, final float p_178097_2_, final float p_178097_3_, final float p_178097_4_) {
        final float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927f);
        final float f2 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927f * 2.0f);
        final float f3 = -0.2f * MathHelper.sin(p_178097_4_ * 3.1415927f);
        GlStateManager.translate(f, f2, f3);
        final float f4 = this.func_178100_c(p_178097_2_);
        GlStateManager.translate(0.0f, 0.04f, -0.72f);
        GlStateManager.translate(0.0f, p_178097_3_ * -1.2f, 0.0f);
        GlStateManager.translate(0.0f, f4 * -0.5f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * -85.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        this.renderPlayerArms(clientPlayer);
        final float f5 = MathHelper.sin(p_178097_4_ * p_178097_4_ * 3.1415927f);
        final float f6 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927f);
        GlStateManager.rotate(f5 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f6 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f6 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-1.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.015625f, 0.015625f, 0.015625f);
        this.mc.getTextureManager().bindTexture(ItemRenderer.RES_MAP_BACKGROUND);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        final MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }
    
    private void func_178095_a(final AbstractClientPlayer clientPlayer, final float p_178095_2_, final float p_178095_3_) {
        final float f = -0.3f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927f);
        final float f2 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927f * 2.0f);
        final float f3 = -0.4f * MathHelper.sin(p_178095_3_ * 3.1415927f);
        GlStateManager.translate(f, f2, f3);
        GlStateManager.translate(0.64000005f, -0.6f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178095_2_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f4 = MathHelper.sin(p_178095_3_ * p_178095_3_ * 3.1415927f);
        final float f5 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927f);
        GlStateManager.rotate(f5 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * -20.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        GlStateManager.translate(-1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(5.6f, 0.0f, 0.0f);
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        final RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(this.mc.thePlayer);
        GlStateManager.enableCull();
    }
    
    private void func_178105_d(final float p_178105_1_) {
        final float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927f);
        final float f2 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927f * 2.0f);
        final float f3 = -0.2f * MathHelper.sin(p_178105_1_ * 3.1415927f);
        GlStateManager.translate(f, f2, f3);
    }
    
    private void func_178104_a(final AbstractClientPlayer clientPlayer, final float p_178104_2_) {
        final float f = clientPlayer.getItemInUseCount() - p_178104_2_ + 1.0f;
        final float f2 = f / this.itemToRender.getMaxItemUseDuration();
        float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
        if (f2 >= 0.8f) {
            f3 = 0.0f;
        }
        GlStateManager.translate(0.0f, f3, 0.0f);
        final float f4 = 1.0f - (float)Math.pow(f2, 27.0);
        GlStateManager.translate(f4 * 0.6f, f4 * -0.5f, f4 * 0.0f);
        GlStateManager.rotate(f4 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f4 * 30.0f, 0.0f, 0.0f, 1.0f);
    }
    
    private void transformFirstPersonItem(final float equipProgress, final float swingProgress) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }
    
    private void func_178098_a(final float p_178098_1_, final AbstractClientPlayer clientPlayer) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        final float f = this.itemToRender.getMaxItemUseDuration() - (clientPlayer.getItemInUseCount() - p_178098_1_ + 1.0f);
        float f2 = f / 20.0f;
        f2 = (f2 * f2 + f2 * 2.0f) / 3.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        if (f2 > 0.1f) {
            final float f3 = MathHelper.sin((f - 0.1f) * 1.3f);
            final float f4 = f2 - 0.1f;
            final float f5 = f3 * f4;
            GlStateManager.translate(f5 * 0.0f, f5 * 0.01f, f5 * 0.0f);
        }
        GlStateManager.translate(f2 * 0.0f, f2 * 0.0f, f2 * 0.1f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f + f2 * 0.2f);
    }
    
    private void func_178103_d() {
        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }
    
    private void sigma2(final float var2, final float swingProgress) {
        final float smooth = swingProgress * 0.8f - swingProgress * swingProgress * 0.8f;
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, var2 * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 2.0f + smooth * 0.5f, smooth * 3.0f);
        final float var3 = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float var4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(0.37f, 0.37f, 0.37f);
    }
    
    public void renderItemInFirstPerson(final float partialTicks) {
        final float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        final AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        final float f2 = abstractclientplayer.getSwingProgress(partialTicks);
        final float f3 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f4 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.func_178101_a(f3, f4);
        this.func_178109_a(abstractclientplayer);
        this.func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null) {
            if (this.itemToRender.getItem() == Items.filled_map) {
                this.renderItemMap(abstractclientplayer, f3, f, f2);
            }
         else if (((Aura) Helium.instance.cheatManager.getCheatRegistry().get("KillAura")).shouldDoAutoblockAnim() && ((Aura) Helium.instance.cheatManager.getCheatRegistry().get("KillAura")).holdingSword()) {
            	if (Helium.instance.cheatManager.isCheatEnabled("Animations")) {
                    float f6 = MathHelper.sin((float) (MathHelper.sqrt_float(f2) * 2.1));
                    float var15 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
                    switch (((BlockAnimation) Helium.instance.cheatManager.getCheatRegistry().get("Animations")).mode.getSelectedStrings().get(0)) {
                        case "1.7": {
                            this.transformFirstPersonItem(f, f2);
                            this.func_178103_d();
                            GlStateManager.translate(0.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(5.0f, 50.0f, 100.0f, 50.0f);
                            break;
                        }
                        case "Matt": {
                            this.transformFirstPersonItem(f, 0.0f);
                            this.func_178103_d();
                            GlStateManager.translate(-0.0f, 0.4f, 0.1f);
                            GlStateManager.rotate(-var15 * 35.0f, -8.0f, -0.0f, 9.0f);
                            GlStateManager.rotate(-var15 * 10.0f, 1.0f, -0.4f, -0.5f);
                            break;
                        }
                        case "Slide": {
                            this.transformFirstPersonItem(f * 0.5f, 0.0f);
                            GlStateManager.rotate(-var15 * -74.0f / 4.0f, -8.0f, -0.0f, 9.0f);
                            GlStateManager.rotate(-var15 * 15.0f, 1.0f, var15 / 2.0f, -0.0f);
                            this.func_178103_d();
                            GL11.glTranslated(1.2, 0.3, 0.5);
                            GL11.glTranslatef(-1.0f, this.mc.thePlayer.isSneaking() ? -0.1f : -0.2f, 0.2f);
                            break;
                        }
                        case "Sigma": {
                            this.transformFirstPersonItem(f * 0.9f, 0.0f);
                            GlStateManager.rotate(-var15 * 15.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                            GlStateManager.rotate(-var15 * 15.0f, 1.0f, var15 / 2.0f, -0.0f);
                            this.func_178103_d();
                            GL11.glTranslated(1.2, 0.3, 0.5);
                            GL11.glTranslatef(-1.0f, this.mc.thePlayer.isSneaking() ? -0.1f : -0.2f, 0.2f);
                            break;
                        }
                        case "Helium": {
                            this.transformFirstPersonItem(f, 1.0f);
                            this.func_178103_d();
                            GL11.glTranslatef(0.4f, 0.1f, -0.01f);
                            final float var17 = MathHelper.sin(f2 * f2 * 5.1415925f);
                            GlStateManager.translate(-0.1f, 0.3f, 0.2f);
                            GlStateManager.rotate(var17 * 13.0f, -10.0f, -1.4f, -10.0f);
                            break;
                        }
                        case "Poke": {
                            this.transformFirstPersonItem(f, 1.0f);
                            this.func_178103_d();
                            GL11.glTranslatef(0.1f, 0.3f, 0.7f);
                            final float var16 = MathHelper.sin(f2 * f2 * 5.1415925f);
                            GlStateManager.translate(-0.4f, -0.1f, -0.2f);
                            GlStateManager.rotate(var16 * 33.0f, 85.0f, -0.4f, -5.0f);
                            break;
                        }
                        case "Dortware": {
                            final float var17 = MathHelper.sin((float) (f2 * f2 * Math.PI - 3));
                            this.transformFirstPersonItem(f, 1.0f);
                            // this.func_178103_d();
                             GlStateManager.rotate(-var15 * 10, 0.0f, 15.0f, 200.0f);
                             GlStateManager.rotate(-var15 * 10f, 300.0f, var15 / 2.0f, 1.0f);
                            this.func_178103_d();
                             GL11.glTranslated(2.4, 0.3, 0.5);
                            //this.func_178103_d();
                             GL11.glTranslatef(-2.10f, -0.2f, 0.1f);
                            GlStateManager.rotate(var17 * 13.0f, -10.0f, -1.4f, -10.0f);
                             break;
                        }
                        case "Swang": {
                            this.transformFirstPersonItem(f, f2);
                            this.func_178103_d();
                            final float var18 = MathHelper.sin(f2 * f2 * 5.1415925f);
                            GL11.glTranslatef(0.1f, -0.1f, 0.3f);
                            GlStateManager.translate(0.1f, -0.1f, 0.4f);
                            //GlStateManager.rotate(var18 * 223.0f, 115.0f, 1.4f, 325.0f);
                            break;
                        }
                        case "Vanilla": {
                            this.transformFirstPersonItem(f, f2);
                            this.func_178103_d();
                            GlStateManager.translate(0.0f, 0.0f, 0.0f);
                            break;
                        }
                        case "oHare": {
                            this.transformFirstPersonItem(f, 0.0F);
                            this.func_178103_d();
                            float var9 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927F);
                            GlStateManager.translate(-0.05F, 0.6F, 0.3F);
                            GlStateManager.rotate(-var9 * 70.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                            GlStateManager.rotate(-var9 * 70.0F, 1.5F, -0.4F, -0.0F);
                            break;
//                        	this.transformFirstPersonItem(f, f2);
//                            func_178103_d();
//                            GL11.glTranslated(1.2, 0.3, 0.5);
//                            GlStateManager.rotate(-var15 * 0.0F, -2.0F, 0.0F, 10.0F);
//                            GlStateManager.rotate(-var15 * 25.0F, 0.5F, 0F, 1F);
//                            break;
                        }
                        case "Exhibition": {
                            this.transformFirstPersonItem(f * 0.5f, -2.0f);
                            GlStateManager.rotate(-f6 * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                            GlStateManager.rotate(-f6 * 45.0f, 1.0f, f6 / 2.0f, -0.0f);
                            this.func_178103_d();
                            GL11.glTranslated(1.2, 0.3, 0.5);
                            GL11.glTranslatef(-1.0f, -0.1f, 0.2f);
                            GlStateManager.scale(1.5, 1.5, 1.5);
                            break;
                        }
                        case "Spin": {
                            this.transformFirstPersonItem(f, 0.0f);
                            this.func_178103_d();
                            GL11.glRotatef(rotate, rotate, 0, rotate);
                            GL11.glScalef(0.5f, 0.5f, 0.5F);
                            //GL11.glTranslatef(0, 5, 0);
                            rotate++;
                            break;
                        }
                        case "Astro": {
                            final float var = MathHelper.sin((float) (MathHelper.sqrt_float(f2) * Math.PI));
                            this.transformFirstPersonItem (f / 2, f2);
                            GL11.glRotatef(var * 50.0F / 9.0F, -var, -0.0F, 90.0F);
                            GL11.glRotatef(var * 50.0F, 200.0F, -var / 2.0F, -0.0F);
                            mc.getItemRenderer().func_178103_d();
                            break;
                        }

                        case "In": {
                            this.transformFirstPersonItem (f, f2);
                            GlStateManager.translate(0.05F, 0.2F, 0.05F);
                            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                            GlStateManager.rotate(50.0F, 1.0F, 0.0F, 0.0F);
                            break;
                        }

                        case "Remix": {
                            GL11.glTranslated(0.0, 0.0, 0.0);
                            this.transformFirstPersonItem(-0.1f, 1.0f);
                            GlStateManager.rotate(-f6 * 50.0f / 2.0f, f6 / 2.0f, -0.0f, 4.0f);
                            GlStateManager.rotate(-f6 * 30.0f, 1.0f, f6 / 2.0f, -0.0f);
                            this.func_178103_d();
                        }

                        case "Shotbowxd": {
                            this.transformFirstPersonItem(f, f2 / 40.0f);
                            this.func_178103_d();
                            GlStateManager.translate(-0.5f, 0.0f, 0.0f);
                            break;
                        }

                        default:
                            break;
                    }
                }
                else {
                    this.transformFirstPersonItem(f, 0);
                    this.func_178103_d();
                }
            }
            else if (abstractclientplayer.getItemInUseCount() > 0) {
                final EnumAction enumaction = this.itemToRender.getItemUseAction();
                switch (enumaction) {
                    case NONE: {
                        this.transformFirstPersonItem(f, 0.0f);
                        break;
                    }
                    case EAT:
                    case DRINK: {
                        this.func_178104_a(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, 0.0f);
                        break;
                    }
                    case BLOCK: {
                        this.transformFirstPersonItem(f, 0.0f);
                        this.func_178103_d();
                        break;
                    }
                    case BOW: {
                        this.transformFirstPersonItem(f, 0.0f);
                        this.func_178098_a(partialTicks, abstractclientplayer);
                        break;
                    }
                }
            }
            else {
                this.func_178105_d(f2);
                this.transformFirstPersonItem(f, f2);
            }
            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible()) {
            this.func_178095_a(abstractclientplayer, f, f2);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    public void renderOverlays(final float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
            final EntityPlayer entityplayer = this.mc.thePlayer;
            for (int i = 0; i < 8; ++i) {
                final double d0 = entityplayer.posX + ((i >> 0) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final double d2 = entityplayer.posY + ((i >> 1) % 2 - 0.5f) * 0.1f;
                final double d3 = entityplayer.posZ + ((i >> 2) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final BlockPos blockpos = new BlockPos(d0, d2 + entityplayer.getEyeHeight(), d3);
                final IBlockState iblockstate2 = this.mc.theWorld.getBlockState(blockpos);
                if (iblockstate2.getBlock().isVisuallyOpaque()) {
                    iblockstate = iblockstate2;
                }
            }
            if (iblockstate.getBlock().getRenderType() != -1) {
                this.func_178108_a(partialTicks, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
            }
        }
        if (!this.mc.thePlayer.isSpectator()) {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                this.renderWaterOverlayTexture(partialTicks);
            }
            if (this.mc.thePlayer.isBurning()) {
                this.renderFireInFirstPerson(partialTicks);
            }
        }
        GlStateManager.enableAlpha();
    }
    
    private void func_178108_a(final float p_178108_1_, final TextureAtlasSprite p_178108_2_) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        final float f2 = -1.0f;
        final float f3 = 1.0f;
        final float f4 = -1.0f;
        final float f5 = 1.0f;
        final float f6 = -0.5f;
        final float f7 = p_178108_2_.getMinU();
        final float f8 = p_178108_2_.getMaxU();
        final float f9 = p_178108_2_.getMinV();
        final float f10 = p_178108_2_.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(f8, f10).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(f7, f10).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(f7, f9).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(f8, f9).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderWaterOverlayTexture(final float p_78448_1_) {
        this.mc.getTextureManager().bindTexture(ItemRenderer.RES_UNDERWATER_OVERLAY);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f = this.mc.thePlayer.getBrightness(p_78448_1_);
        GlStateManager.color(f, f, f, 0.5f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        final float f2 = 4.0f;
        final float f3 = -1.0f;
        final float f4 = 1.0f;
        final float f5 = -1.0f;
        final float f6 = 1.0f;
        final float f7 = -0.5f;
        final float f8 = -this.mc.thePlayer.rotationYaw / 64.0f;
        final float f9 = this.mc.thePlayer.rotationPitch / 64.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(4.0f + f8, 4.0f + f9).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(0.0f + f8, 4.0f + f9).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(0.0f + f8, 0.0f + f9).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(4.0f + f8, 0.0f + f9).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
    }
    
    private void renderFireInFirstPerson(final float p_78442_1_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            final float f2 = textureatlassprite.getMinU();
            final float f3 = textureatlassprite.getMaxU();
            final float f4 = textureatlassprite.getMinV();
            final float f5 = textureatlassprite.getMaxV();
            final float f6 = (0.0f - f) / 2.0f;
            final float f7 = f6 + f;
            final float f8 = 0.0f - f / 2.0f;
            final float f9 = f8 + f;
            final float f10 = -0.5f;
            GlStateManager.translate(-(i * 2 - 1) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((i * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(f6, f8, f10).tex(f3, f5).endVertex();
            worldrenderer.pos(f7, f8, f10).tex(f2, f5).endVertex();
            worldrenderer.pos(f7, f9, f10).tex(f2, f4).endVertex();
            worldrenderer.pos(f6, f9, f10).tex(f3, f4).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }
    
    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        final EntityPlayer entityplayer = this.mc.thePlayer;
        final ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;
        if (this.itemToRender != null && itemstack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
                flag = true;
            }
        }
        else {
            flag = (this.itemToRender != null || itemstack != null);
        }
        final float f = 0.4f;
        final float f2 = flag ? 0.0f : 1.0f;
        final float f3 = MathHelper.clamp_float(f2 - this.equippedProgress, -f, f);
        this.equippedProgress += f3;
        if (this.equippedProgress < 0.1f) {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;
        }
    }
    
    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }
    
    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }
}



