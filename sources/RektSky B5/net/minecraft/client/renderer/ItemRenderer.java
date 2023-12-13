/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;
import tk.rektsky.event.EventManager;
import tk.rektsky.event.impl.ItemRenderInFirstPersonEvent;
import tk.rektsky.event.impl.PostItemRenderInFirstPersonEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.combat.KillAura;
import tk.rektsky.module.impl.render.SwordAnimations;

public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemToRender;
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private int equippedItemSlot = -1;
    int time = 0;
    float f = 1.0f;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        if (heldStack != null) {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
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

    private boolean isBlockTranslucent(Block blockIn) {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void func_178101_a(float angle, float p_178101_2_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(p_178101_2_, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void func_178109_a(AbstractClientPlayer clientPlayer) {
        int i2 = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + (double)clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        float f2 = i2 & 0xFFFF;
        float f1 = i2 >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f2, f1);
    }

    private void func_178110_a(EntityPlayerSP entityplayerspIn, float partialTicks) {
        float f2 = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((entityplayerspIn.rotationPitch - f2) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayerspIn.rotationYaw - f1) * 0.1f, 0.0f, 1.0f, 0.0f);
    }

    private float func_178100_c(float p_178100_1_) {
        float f2 = 1.0f - p_178100_1_ / 45.0f + 0.1f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f2 = -MathHelper.cos(f2 * (float)Math.PI) * 0.5f + 0.5f;
        return f2;
    }

    private void renderRightArm(RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(64.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-62.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.25f, -0.85f, 0.75f);
        renderPlayerIn.renderRightArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderLeftArm(RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.3f, -1.1f, 0.45f);
        renderPlayerIn.renderLeftArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderPlayerArms(AbstractClientPlayer clientPlayer) {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Render render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;
        if (!clientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }

    private void renderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_) {
        float f2 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        float f1 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI * 2.0f);
        float f22 = -0.2f * MathHelper.sin(p_178097_4_ * (float)Math.PI);
        GlStateManager.translate(f2, f1, f22);
        float f3 = this.func_178100_c(p_178097_2_);
        GlStateManager.translate(0.0f, 0.04f, -0.72f);
        GlStateManager.translate(0.0f, p_178097_3_ * -1.2f, 0.0f);
        GlStateManager.translate(0.0f, f3 * -0.5f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * -85.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        this.renderPlayerArms(clientPlayer);
        float f4 = MathHelper.sin(p_178097_4_ * p_178097_4_ * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        GlStateManager.rotate(f4 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f5 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f5 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-1.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.015625f, 0.015625f, 0.015625f);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void func_178095_a(AbstractClientPlayer clientPlayer, float p_178095_2_, float p_178095_3_) {
        float f2 = -0.3f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        float f1 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI * 2.0f);
        float f22 = -0.4f * MathHelper.sin(p_178095_3_ * (float)Math.PI);
        GlStateManager.translate(f2, f1, f22);
        GlStateManager.translate(0.64000005f, -0.6f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178095_2_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f3 = MathHelper.sin(p_178095_3_ * p_178095_3_ * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        GlStateManager.rotate(f4 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * -20.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        GlStateManager.translate(-1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(5.6f, 0.0f, 0.0f);
        Render render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(this.mc.thePlayer);
        GlStateManager.enableCull();
    }

    private void func_178105_d(float p_178105_1_) {
        float f2 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI);
        float f1 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI * 2.0f);
        float f22 = -0.2f * MathHelper.sin(p_178105_1_ * (float)Math.PI);
        GlStateManager.translate(f2, f1, f22);
    }

    private void func_178104_a(AbstractClientPlayer clientPlayer, float p_178104_2_) {
        float f2 = (float)clientPlayer.getItemInUseCount() - p_178104_2_ + 1.0f;
        float f1 = f2 / (float)this.itemToRender.getMaxItemUseDuration();
        float f22 = MathHelper.abs(MathHelper.cos(f2 / 4.0f * (float)Math.PI) * 0.1f);
        if (f1 >= 0.8f) {
            f22 = 0.0f;
        }
        GlStateManager.translate(0.0f, f22, 0.0f);
        float f3 = 1.0f - (float)Math.pow(f1, 27.0);
        GlStateManager.translate(f3 * 0.6f, f3 * -0.5f, f3 * 0.0f);
        GlStateManager.rotate(f3 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f3 * 30.0f, 0.0f, 0.0f, 1.0f);
    }

    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f2 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f1 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f1 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void func_178098_a(float p_178098_1_, AbstractClientPlayer clientPlayer) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        float f2 = (float)this.itemToRender.getMaxItemUseDuration() - ((float)clientPlayer.getItemInUseCount() - p_178098_1_ + 1.0f);
        float f1 = f2 / 20.0f;
        f1 = (f1 * f1 + f1 * 2.0f) / 3.0f;
        if (f1 > 1.0f) {
            f1 = 1.0f;
        }
        if (f1 > 0.1f) {
            float f22 = MathHelper.sin((f2 - 0.1f) * 1.3f);
            float f3 = f1 - 0.1f;
            float f4 = f22 * f3;
            GlStateManager.translate(f4 * 0.0f, f4 * 0.01f, f4 * 0.0f);
        }
        GlStateManager.translate(f1 * 0.0f, f1 * 0.0f, f1 * 0.1f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f + f1 * 0.2f);
    }

    public void renderBlockAnimation() {
        ++this.time;
        this.f += 0.1f;
        GlStateManager.translate(-0.5f, 0.6f, 0.0f);
        GlStateManager.rotate(1.0f * (float)(Minecraft.getSystemTime() % 360L), 0.3f, 0.0f, 0.1f);
    }

    public void renderItemInFirstPerson(float partialTicks) {
        float f2 = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f22 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.func_178101_a(f22, f3);
        this.func_178109_a(abstractclientplayer);
        this.func_178110_a(abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null) {
            boolean auraBlocking;
            KillAura killAura = ModulesManager.getModuleByClass(KillAura.class);
            boolean bl = auraBlocking = killAura != null && killAura.isAttacking && killAura.blockAnimationSetting.getValue() != false;
            if (this.itemToRender.getItem() == Items.filled_map) {
                this.renderItemMap(abstractclientplayer, f22, f2, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0 || auraBlocking) {
                EnumAction enumaction = this.itemToRender.getItemUseAction();
                float swingProgress = this.mc.thePlayer.getSwingProgress(partialTicks);
                if (auraBlocking) {
                    enumaction = EnumAction.BLOCK;
                }
                block0 : switch (enumaction) {
                    case NONE: {
                        this.transformFirstPersonItem(f2, f1);
                        break;
                    }
                    case EAT: 
                    case DRINK: {
                        this.func_178104_a(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f2, f1);
                        break;
                    }
                    case BLOCK: {
                        switch (SwordAnimations.mode.getValue()) {
                            case "Vanilla": {
                                this.transformFirstPersonItem(f2, 0.0f);
                                GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                break block0;
                            }
                            case "1.7": {
                                GlStateManager.translate(-0.15f, 0.15f, -0.2f);
                                this.transformFirstPersonItem(f2, swingProgress);
                                GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                                GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                                break block0;
                            }
                            case "animashion": {
                                GlStateManager.translate(-0.15f, 0.15f, -0.2f);
                                this.transformFirstPersonItem(f2, swingProgress);
                                GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-30.0f, 1.0f, 0.0f, 1.0f);
                                GlStateManager.rotate(60.0f, 0.0f, 1.0f, 1.0f);
                                break block0;
                            }
                            case "Swoop": {
                                GlStateManager.translate(-0.15f, 0.15f, -0.2f);
                                this.transformFirstPersonItem(f2, swingProgress);
                                GlStateManager.translate(-0.2f, 0.0f, 0.0f);
                                GlStateManager.rotate(80.0f, 0.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-100.0f, 1.0f, 0.0f, 0.0f);
                                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                                break block0;
                            }
                            case "AllahSwing": {
                                GlStateManager.translate(-0.0f, 0.15f, -0.1f);
                                this.transformFirstPersonItem(f2, swingProgress);
                                GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                GlStateManager.rotate(-10.0f, 3.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 1.0f);
                                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 1.0f);
                                break block0;
                            }
                            case "Swosh": {
                                GlStateManager.translate(-0.0f, 0.15f, -0.1f);
                                this.transformFirstPersonItem(f2, swingProgress / 20.0f);
                                GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                GlStateManager.rotate(30.0f, 1.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-110.0f, 1.0f, 0.0f, 0.0f);
                                GlStateManager.rotate(50.0f, 0.0f, 1.0f, 0.0f);
                            }
                        }
                        break;
                    }
                    case BOW: {
                        this.transformFirstPersonItem(f2, f1);
                        this.func_178098_a(partialTicks, abstractclientplayer);
                    }
                }
            } else {
                this.func_178105_d(f1);
                this.transformFirstPersonItem(f2, f1);
            }
            if (SwordAnimations.mode.getValue().equals("RektSky") && (this.mc.gameSettings.keyBindUseItem.isKeyDown() || auraBlocking) && this.isSword()) {
                this.transformFirstPersonItem(f2, 0.0f);
                this.renderBlockAnimation();
            }
            if (SwordAnimations.mode.getValue().equals("Fan") && (this.mc.gameSettings.keyBindUseItem.isKeyDown() || auraBlocking) && this.isSword()) {
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 2.0f);
                GlStateManager.translate(-0.1f, 0.7f, 0.0f);
                this.transformFirstPersonItem(f2, 0.0f);
                this.renderBlockAnimation();
            }
            ItemRenderInFirstPersonEvent event = new ItemRenderInFirstPersonEvent(this.itemToRender, partialTicks);
            EventManager.callEvent(event);
            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            PostItemRenderInFirstPersonEvent event2 = new PostItemRenderInFirstPersonEvent(this.itemToRender, partialTicks);
            EventManager.callEvent(event2);
        } else if (!abstractclientplayer.isInvisible()) {
            this.func_178095_a(abstractclientplayer, f2, f1);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private boolean isSword() {
        Item item = this.itemToRender.getItem();
        return item == Items.golden_sword || item == Items.wooden_sword || item == Items.stone_sword || item == Items.iron_sword || item == Items.diamond_sword;
    }

    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
            EntityPlayerSP entityplayer = this.mc.thePlayer;
            for (int i2 = 0; i2 < 8; ++i2) {
                double d0 = entityplayer.posX + (double)(((float)((i2 >> 0) % 2) - 0.5f) * entityplayer.width * 0.8f);
                double d1 = entityplayer.posY + (double)(((float)((i2 >> 1) % 2) - 0.5f) * 0.1f);
                double d2 = entityplayer.posZ + (double)(((float)((i2 >> 2) % 2) - 0.5f) * entityplayer.width * 0.8f);
                BlockPos blockpos = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.theWorld.getBlockState(blockpos);
                if (!iblockstate1.getBlock().isVisuallyOpaque()) continue;
                iblockstate = iblockstate1;
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

    private void func_178108_a(float p_178108_1_, TextureAtlasSprite p_178108_2_) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f2 = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        float f1 = -1.0f;
        float f22 = 1.0f;
        float f3 = -1.0f;
        float f4 = 1.0f;
        float f5 = -0.5f;
        float f6 = p_178108_2_.getMinU();
        float f7 = p_178108_2_.getMaxU();
        float f8 = p_178108_2_.getMinV();
        float f9 = p_178108_2_.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(f7, f9).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(f6, f9).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(f6, f8).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(f7, f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderWaterOverlayTexture(float p_78448_1_) {
        this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f2 = this.mc.thePlayer.getBrightness(p_78448_1_);
        GlStateManager.color(f2, f2, f2, 0.5f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        float f1 = 4.0f;
        float f22 = -1.0f;
        float f3 = 1.0f;
        float f4 = -1.0f;
        float f5 = 1.0f;
        float f6 = -0.5f;
        float f7 = -this.mc.thePlayer.rotationYaw / 64.0f;
        float f8 = this.mc.thePlayer.rotationPitch / 64.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(4.0f + f7, 4.0f + f8).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(0.0f + f7, 4.0f + f8).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(0.0f + f7, 0.0f + f8).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(4.0f + f7, 0.0f + f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
    }

    private void renderFireInFirstPerson(float p_78442_1_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        float f2 = 1.0f;
        for (int i2 = 0; i2 < 2; ++i2) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f1 = textureatlassprite.getMinU();
            float f22 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = (0.0f - f2) / 2.0f;
            float f6 = f5 + f2;
            float f7 = 0.0f - f2 / 2.0f;
            float f8 = f7 + f2;
            float f9 = -0.5f;
            GlStateManager.translate((float)(-(i2 * 2 - 1)) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((float)(i2 * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(f5, f7, f9).tex(f22, f4).endVertex();
            worldrenderer.pos(f6, f7, f9).tex(f1, f4).endVertex();
            worldrenderer.pos(f6, f8, f9).tex(f1, f3).endVertex();
            worldrenderer.pos(f5, f8, f9).tex(f22, f3).endVertex();
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
        EntityPlayerSP entityplayer = this.mc.thePlayer;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;
        if (this.itemToRender != null && itemstack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
                flag = true;
            }
        } else {
            flag = this.itemToRender != null || itemstack != null;
        }
        float f2 = 0.4f;
        float f1 = flag ? 0.0f : 1.0f;
        float f22 = MathHelper.clamp_float(f1 - this.equippedProgress, -f2, f2);
        this.equippedProgress += f22;
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

