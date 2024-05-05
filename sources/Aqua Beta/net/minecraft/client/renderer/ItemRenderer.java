package net.minecraft.client.renderer;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.modules.visual.HeldItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.optifine.DynamicLights;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

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

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        if (heldStack != null) {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem((Item)item);
            GlStateManager.pushMatrix();
            if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
                GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
                if (!(!this.isBlockTranslucent(block) || Config.isShaders() && Shaders.renderItemKeepDepthMask)) {
                    GlStateManager.depthMask((boolean)false);
                }
            }
            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
            if (this.isBlockTranslucent(block)) {
                GlStateManager.depthMask((boolean)true);
            }
            GlStateManager.popMatrix();
        }
    }

    private boolean isBlockTranslucent(Block blockIn) {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void rotateArroundXAndY(float angle, float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float)angle, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)angleY, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightMapFromPlayer(AbstractClientPlayer clientPlayer) {
        int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + (double)clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight((Entity)this.mc.getRenderViewEntity(), (int)i);
        }
        float f = i & 0xFFFF;
        float f1 = i >> 16;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)f, (float)f1);
    }

    private void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks) {
        float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((float)((entityplayerspIn.rotationPitch - f) * 0.1f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((entityplayerspIn.rotationYaw - f1) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private float getMapAngleFromPitch(float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp_float((float)f, (float)0.0f, (float)1.0f);
        f = -MathHelper.cos((float)(f * (float)Math.PI)) * 0.5f + 0.5f;
        return f;
    }

    private void renderRightArm(RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float)54.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)64.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)-62.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.translate((float)0.25f, (float)-0.85f, (float)0.75f);
        renderPlayerIn.renderRightArm((AbstractClientPlayer)this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderLeftArm(RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float)92.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)41.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.translate((float)-0.3f, (float)-1.1f, (float)0.45f);
        renderPlayerIn.renderLeftArm((AbstractClientPlayer)this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderPlayerArms(AbstractClientPlayer clientPlayer) {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Render render = this.renderManager.getEntityRenderObject((Entity)this.mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;
        if (!clientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }

    private void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress) {
        float f = -0.4f * MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        float f1 = 0.2f * MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI * 2.0f));
        float f2 = -0.2f * MathHelper.sin((float)(swingProgress * (float)Math.PI));
        GlStateManager.translate((float)f, (float)f1, (float)f2);
        float f3 = this.getMapAngleFromPitch(pitch);
        GlStateManager.translate((float)0.0f, (float)0.04f, (float)-0.72f);
        GlStateManager.translate((float)0.0f, (float)(equipmentProgress * -1.2f), (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)(f3 * -0.5f), (float)0.0f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f3 * -85.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        this.renderPlayerArms(clientPlayer);
        float f4 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float f5 = MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(f4 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f5 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(f5 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)0.38f, (float)0.38f, (float)0.38f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.translate((float)-1.0f, (float)-1.0f, (float)0.0f);
        GlStateManager.scale((float)0.015625f, (float)0.015625f, (float)0.015625f);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)-1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.itemToRender, (World)this.mc.theWorld);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress) {
        float f = -0.3f * MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        float f1 = 0.4f * MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI * 2.0f));
        float f2 = -0.4f * MathHelper.sin((float)(swingProgress * (float)Math.PI));
        GlStateManager.translate((float)f, (float)f1, (float)f2);
        GlStateManager.translate((float)0.64000005f, (float)-0.6f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f3 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float f4 = MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(f4 * 70.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f3 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        GlStateManager.translate((float)-1.0f, (float)3.6f, (float)3.5f);
        GlStateManager.rotate((float)120.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)200.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.translate((float)5.6f, (float)0.0f, (float)0.0f);
        Render render = this.renderManager.getEntityRenderObject((Entity)this.mc.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm((AbstractClientPlayer)this.mc.thePlayer);
        GlStateManager.enableCull();
    }

    private void doItemUsedTransformations(float swingProgress) {
        HeldItem.x = (float)Aqua.setmgr.getSetting("HeldItemX").getCurrentNumber();
        HeldItem.y = (float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber();
        HeldItem.z = (float)Aqua.setmgr.getSetting("HeldItemZ").getCurrentNumber();
        float f = -0.4f * MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        float f1 = 0.2f * MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI * 2.0f));
        float f2 = -0.2f * MathHelper.sin((float)(swingProgress * (float)Math.PI));
        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
            GlStateManager.translate((float)HeldItem.x, (float)HeldItem.y, (float)HeldItem.z);
        } else {
            GlStateManager.translate((float)f, (float)f1, (float)f2);
        }
    }

    private void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks) {
        float f = (float)clientPlayer.getItemInUseCount() - partialTicks + 1.0f;
        float f1 = f / (float)this.itemToRender.getMaxItemUseDuration();
        float f2 = MathHelper.abs((float)(MathHelper.cos((float)(f / 4.0f * (float)Math.PI)) * 0.1f));
        if (f1 >= 0.8f) {
            f2 = 0.0f;
        }
        GlStateManager.translate((float)0.0f, (float)f2, (float)0.0f);
        float f3 = 1.0f - (float)Math.pow((double)f1, (double)27.0);
        GlStateManager.translate((float)(f3 * 0.6f), (float)(f3 * -0.5f), (float)(f3 * 0.0f));
        GlStateManager.rotate((float)(f3 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f3 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(f3 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
    }

    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float f1 = MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f1 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(f1 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        HeldItem.scale = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
            GlStateManager.scale((float)HeldItem.scale, (float)HeldItem.scale, (float)HeldItem.scale);
        } else {
            GlStateManager.scale((double)0.4, (double)0.4, (double)0.4);
        }
    }

    private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer) {
        GlStateManager.rotate((float)-18.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)-12.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)-8.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.translate((float)-0.9f, (float)0.2f, (float)0.0f);
        float f = (float)this.itemToRender.getMaxItemUseDuration() - ((float)clientPlayer.getItemInUseCount() - partialTicks + 1.0f);
        float f1 = f / 20.0f;
        f1 = (f1 * f1 + f1 * 2.0f) / 3.0f;
        if (f1 > 1.0f) {
            f1 = 1.0f;
        }
        if (f1 > 0.1f) {
            float f2 = MathHelper.sin((float)((f - 0.1f) * 1.3f));
            float f3 = f1 - 0.1f;
            float f4 = f2 * f3;
            GlStateManager.translate((float)(f4 * 0.0f), (float)(f4 * 0.01f), (float)(f4 * 0.0f));
        }
        GlStateManager.translate((float)(f1 * 0.0f), (float)(f1 * 0.0f), (float)(f1 * 0.1f));
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)(1.0f + f1 * 0.2f));
    }

    private void doBlockTransformations() {
        GlStateManager.translate((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.rotate((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void func_178103_d() {
        GlStateManager.translate((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.rotate((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void transformFirstPersonItem3(float equipProgress, float swingProgress) {
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float f1 = MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f1 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(f1 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
            float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
            GlStateManager.scale((float)size, (float)size, (float)size);
        } else {
            GlStateManager.scale((float)0.4f, (float)0.4f, (float)0.4f);
        }
    }

    public void renderItemInFirstPerson(float partialTicks) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand()) {
            float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
            float f1 = abstractclientplayer.getSwingProgress(partialTicks);
            float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
            this.rotateArroundXAndY(f2, f3);
            this.setLightMapFromPlayer((AbstractClientPlayer)abstractclientplayer);
            this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            if (this.itemToRender != null) {
                if (this.itemToRender.getItem() instanceof ItemMap) {
                    this.renderItemMap((AbstractClientPlayer)abstractclientplayer, f2, f, f1);
                } else if (abstractclientplayer.getItemInUseCount() > 0) {
                    EnumAction enumaction = this.itemToRender.getItemUseAction();
                    switch (1.$SwitchMap$net$minecraft$item$EnumAction[enumaction.ordinal()]) {
                        case 1: {
                            this.transformFirstPersonItem(f, 0.0f);
                            break;
                        }
                        case 2: 
                        case 3: {
                            this.performDrinking((AbstractClientPlayer)abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, 0.0f);
                            break;
                        }
                        case 4: {
                            if (Aqua.moduleManager.getModuleByName("Animations").isToggled()) {
                                float f12;
                                float f6;
                                float rot;
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("1.7")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    this.transformFirstPersonItem(f, f1 + f);
                                    this.func_178103_d();
                                    GlStateManager.translate((double)0.1, (double)0.0, (double)0.0);
                                }
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Own")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    this.transformFirstPersonItem(f, 0.0f);
                                    rot = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                    GlStateManager.rotate((float)(-rot * 36.0f), (float)2.09f, (float)0.0f, (float)0.0f);
                                    GlStateManager.rotate((float)(-rot * 25.0f), (float)2.09f, (float)0.0f, (float)15.0f);
                                    GlStateManager.rotate((float)(-rot * 16.0f), (float)12.09f, (float)3.0f, (float)0.0f);
                                    GlStateManager.rotate((float)5.0f, (float)0.0f, (float)-2.0f, (float)12.0f);
                                    this.func_178103_d();
                                }
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("High1.7")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    this.transformFirstPersonItem(-0.3f, f1);
                                    this.func_178103_d();
                                }
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Whack")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                                    float swing = MathHelper.sin((float)((float)((double)MathHelper.sqrt_float((float)f1) * Math.PI)));
                                    GL11.glRotatef((float)(-swing * 80.0f / 5.0f), (float)(swing / 3.0f), (float)-0.0f, (float)9.0f);
                                    GL11.glRotatef((float)(-swing * 40.0f), (float)8.0f, (float)(swing / 9.0f), (float)-0.1f);
                                    this.func_178103_d();
                                }
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Skidding")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    float funny = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI));
                                    GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    GlStateManager.translate((float)(0.56f - funny / 15.0f), (float)(-0.4f + funny / 15.0f), (float)-0.71999997f);
                                    GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                                    GlStateManager.rotate((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                                    f6 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                                    f12 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                    GlStateManager.rotate((float)(f6 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                                    GlStateManager.rotate((float)(f12 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                                    GlStateManager.rotate((float)(f12 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                                    float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
                                    GlStateManager.scale((float)size, (float)size, (float)size);
                                    this.func_178103_d();
                                }
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Exhibition")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    this.transformFirstPersonItem(f, 0.0f);
                                    GlStateManager.translate((double)0.0, (double)0.3, (double)0.0);
                                    rot = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                    GlStateManager.rotate((float)(-rot * 36.0f), (float)2.09f, (float)0.0f, (float)0.0f);
                                    GlStateManager.rotate((float)(-rot * 25.0f), (float)2.09f, (float)0.0f, (float)15.0f);
                                    this.func_178103_d();
                                }
                                if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Butter")) {
                                    if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                    }
                                    GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
                                    GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                                    GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                                    float f4 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                                    float f11 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                    GlStateManager.rotate((float)(f4 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                                    GlStateManager.rotate((float)(f11 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                                    GlStateManager.rotate((float)(f11 * -20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                                    float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
                                    GlStateManager.scale((float)size, (float)size, (float)size);
                                    this.func_178103_d();
                                }
                                if (!Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Aqua")) break;
                                if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                    GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                                }
                                float animation = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI));
                                GlStateManager.translate((float)(0.56f - animation / 15.0f), (float)(-0.4f + animation / 15.0f), (float)-0.71999997f);
                                GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                                GlStateManager.rotate((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                                f6 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                                f12 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                GlStateManager.rotate((float)(f6 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                                GlStateManager.rotate((float)(f12 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                                GlStateManager.rotate((float)(f12 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                                if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                    GlStateManager.scale((float)HeldItem.scale, (float)HeldItem.scale, (float)HeldItem.scale);
                                } else {
                                    GlStateManager.scale((double)0.4, (double)0.4, (double)0.4);
                                }
                                this.func_178103_d();
                                break;
                            }
                            this.transformFirstPersonItem(f, 0.0f);
                            this.doBlockTransformations();
                            break;
                        }
                        case 5: {
                            this.transformFirstPersonItem(f, 0.0f);
                            this.doBowTransformations(partialTicks, (AbstractClientPlayer)abstractclientplayer);
                        }
                    }
                } else if (Aqua.moduleManager.getModuleByName("FakeBlock").isToggled() && Killaura.target != null) {
                    float f12;
                    float f6;
                    float rot;
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("1.7")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        this.transformFirstPersonItem(f, f1 + f);
                        this.func_178103_d();
                        GlStateManager.translate((double)0.1, (double)0.0, (double)0.0);
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Own")) {
                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        this.transformFirstPersonItem(f, 0.0f);
                        GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        rot = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                        GlStateManager.rotate((float)(-rot * 36.0f), (float)2.09f, (float)0.0f, (float)0.0f);
                        GlStateManager.rotate((float)(-rot * 25.0f), (float)2.09f, (float)0.0f, (float)15.0f);
                        GlStateManager.rotate((float)(-rot * 16.0f), (float)12.09f, (float)3.0f, (float)0.0f);
                        GlStateManager.rotate((float)5.0f, (float)0.0f, (float)-2.0f, (float)12.0f);
                        this.func_178103_d();
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("High1.7")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        this.transformFirstPersonItem(-0.3f, f1);
                        this.func_178103_d();
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Whack")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                        float swing = MathHelper.sin((float)((float)((double)MathHelper.sqrt_float((float)f1) * Math.PI)));
                        GL11.glRotatef((float)(-swing * 80.0f / 5.0f), (float)(swing / 3.0f), (float)-0.0f, (float)9.0f);
                        GL11.glRotatef((float)(-swing * 40.0f), (float)8.0f, (float)(swing / 9.0f), (float)-0.1f);
                        this.func_178103_d();
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Skidding")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        float funny = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI));
                        GlStateManager.translate((float)(0.56f - funny / 15.0f), (float)(-0.4f + funny / 15.0f), (float)-0.71999997f);
                        GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                        GlStateManager.rotate((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                        f6 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                        f12 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                        GlStateManager.rotate((float)(f6 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.rotate((float)(f12 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                        GlStateManager.rotate((float)(f12 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                        float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
                        GlStateManager.scale((float)size, (float)size, (float)size);
                        this.func_178103_d();
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Exhibition")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        this.transformFirstPersonItem(f, 0.0f);
                        GlStateManager.translate((double)0.0, (double)0.3, (double)0.0);
                        rot = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                        GlStateManager.rotate((float)(-rot * 36.0f), (float)2.09f, (float)0.0f, (float)0.0f);
                        GlStateManager.rotate((float)(-rot * 25.0f), (float)2.09f, (float)0.0f, (float)15.0f);
                        this.func_178103_d();
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Butter")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
                        GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                        GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                        float f4 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                        float f11 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                        GlStateManager.rotate((float)(f4 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.rotate((float)(f11 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                        GlStateManager.rotate((float)(f11 * -20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                        float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
                        GlStateManager.scale((float)size, (float)size, (float)size);
                        this.func_178103_d();
                    }
                    if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Aqua")) {
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                        }
                        float animation = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI));
                        GlStateManager.translate((float)(0.56f - animation / 15.0f), (float)(-0.4f + animation / 15.0f), (float)-0.71999997f);
                        GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                        GlStateManager.rotate((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                        f6 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                        f12 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                        GlStateManager.rotate((float)(f6 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.rotate((float)(f12 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                        GlStateManager.rotate((float)(f12 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                        if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                            GlStateManager.scale((float)HeldItem.scale, (float)HeldItem.scale, (float)HeldItem.scale);
                        } else {
                            GlStateManager.scale((double)0.4, (double)0.4, (double)0.4);
                        }
                        this.func_178103_d();
                    }
                } else {
                    if (!this.mc.isSingleplayer() && this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("hypixel.net") && Killaura.target != null) {
                        float f12;
                        float f6;
                        float rot;
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("1.7")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            this.transformFirstPersonItem(f, f1 + f);
                            this.func_178103_d();
                            GlStateManager.translate((double)0.1, (double)0.0, (double)0.0);
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Own")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            this.transformFirstPersonItem(f, 0.0f);
                            rot = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                            GlStateManager.rotate((float)(-rot * 36.0f), (float)2.09f, (float)0.0f, (float)0.0f);
                            GlStateManager.rotate((float)(-rot * 25.0f), (float)2.09f, (float)0.0f, (float)15.0f);
                            GlStateManager.rotate((float)(-rot * 16.0f), (float)12.09f, (float)3.0f, (float)0.0f);
                            GlStateManager.rotate((float)5.0f, (float)0.0f, (float)-2.0f, (float)12.0f);
                            this.func_178103_d();
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("High1.7")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            this.transformFirstPersonItem(-0.3f, f1);
                            this.func_178103_d();
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Whack")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                            float swing = MathHelper.sin((float)((float)((double)MathHelper.sqrt_float((float)f1) * Math.PI)));
                            GL11.glRotatef((float)(-swing * 80.0f / 5.0f), (float)(swing / 3.0f), (float)-0.0f, (float)9.0f);
                            GL11.glRotatef((float)(-swing * 40.0f), (float)8.0f, (float)(swing / 9.0f), (float)-0.1f);
                            this.func_178103_d();
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Skidding")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            float funny = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI));
                            GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            GlStateManager.translate((float)(0.56f - funny / 15.0f), (float)(-0.4f + funny / 15.0f), (float)-0.71999997f);
                            GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                            GlStateManager.rotate((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                            f6 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                            f12 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                            GlStateManager.rotate((float)(f6 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                            GlStateManager.rotate((float)(f12 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                            GlStateManager.rotate((float)(f12 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                            float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
                            GlStateManager.scale((float)size, (float)size, (float)size);
                            this.func_178103_d();
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Exhibition")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            this.transformFirstPersonItem(f, 0.0f);
                            GlStateManager.translate((double)0.0, (double)0.3, (double)0.0);
                            rot = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                            GlStateManager.rotate((float)(-rot * 36.0f), (float)2.09f, (float)0.0f, (float)0.0f);
                            GlStateManager.rotate((float)(-rot * 25.0f), (float)2.09f, (float)0.0f, (float)15.0f);
                            this.func_178103_d();
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Butter")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
                            GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                            GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                            float f4 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                            float f11 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                            GlStateManager.rotate((float)(f4 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                            GlStateManager.rotate((float)(f11 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                            GlStateManager.rotate((float)(f11 * -20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                            float size = (float)Aqua.setmgr.getSetting("HeldItemScale").getCurrentNumber();
                            GlStateManager.scale((float)size, (float)size, (float)size);
                            this.func_178103_d();
                        }
                        if (Aqua.setmgr.getSetting("AnimationsMode").getCurrentMode().equalsIgnoreCase("Aqua")) {
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.translate((float)0.0f, (float)((float)Aqua.setmgr.getSetting("HeldItemY").getCurrentNumber()), (float)0.0f);
                            }
                            float animation = MathHelper.sin((float)(MathHelper.sqrt_float((float)f) * (float)Math.PI));
                            GlStateManager.translate((float)(0.56f - animation / 15.0f), (float)(-0.4f + animation / 15.0f), (float)-0.71999997f);
                            GlStateManager.translate((float)0.0f, (float)(f * -0.6f), (float)0.0f);
                            GlStateManager.rotate((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                            f6 = MathHelper.sin((float)(f1 * f1 * (float)Math.PI));
                            f12 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                            GlStateManager.rotate((float)(f6 * -30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                            GlStateManager.rotate((float)(f12 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
                            GlStateManager.rotate((float)(f12 * -85.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                            if (Aqua.moduleManager.getModuleByName("HeldItem").isToggled()) {
                                GlStateManager.scale((float)HeldItem.scale, (float)HeldItem.scale, (float)HeldItem.scale);
                            } else {
                                GlStateManager.scale((double)0.4, (double)0.4, (double)0.4);
                            }
                            this.func_178103_d();
                        } else {
                            this.doItemUsedTransformations(f1);
                            this.transformFirstPersonItem(f, f1);
                        }
                    }
                    this.doItemUsedTransformations(f1);
                    this.transformFirstPersonItem(f, f1);
                }
                this.renderItem((EntityLivingBase)abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            } else if (!abstractclientplayer.isInvisible()) {
                this.renderPlayerArm((AbstractClientPlayer)abstractclientplayer, f, f1);
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        }
    }

    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos((Entity)this.mc.thePlayer));
            BlockPos blockpos = new BlockPos((Entity)this.mc.thePlayer);
            EntityPlayerSP entityplayer = this.mc.thePlayer;
            for (int i = 0; i < 8; ++i) {
                double d0 = entityplayer.posX + (double)(((float)((i >> 0) % 2) - 0.5f) * entityplayer.width * 0.8f);
                double d1 = entityplayer.posY + (double)(((float)((i >> 1) % 2) - 0.5f) * 0.1f);
                double d2 = entityplayer.posZ + (double)(((float)((i >> 2) % 2) - 0.5f) * entityplayer.width * 0.8f);
                BlockPos blockpos1 = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.theWorld.getBlockState(blockpos1);
                if (!iblockstate1.getBlock().isVisuallyOpaque()) continue;
                iblockstate = iblockstate1;
                blockpos = blockpos1;
            }
            if (iblockstate.getBlock().getRenderType() != -1) {
                Object object = Reflector.getFieldValue((ReflectorField)Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                if (!Reflector.callBoolean((ReflectorMethod)Reflector.ForgeEventFactory_renderBlockOverlay, (Object[])new Object[]{this.mc.thePlayer, Float.valueOf((float)partialTicks), object, iblockstate, blockpos})) {
                    this.renderBlockInHand(partialTicks, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }
        if (!this.mc.thePlayer.isSpectator()) {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water) && !Reflector.callBoolean((ReflectorMethod)Reflector.ForgeEventFactory_renderWaterOverlay, (Object[])new Object[]{this.mc.thePlayer, Float.valueOf((float)partialTicks)})) {
                this.renderWaterOverlayTexture(partialTicks);
            }
            if (this.mc.thePlayer.isBurning() && !Reflector.callBoolean((ReflectorMethod)Reflector.ForgeEventFactory_renderFireOverlay, (Object[])new Object[]{this.mc.thePlayer, Float.valueOf((float)partialTicks)})) {
                this.renderFireInFirstPerson(partialTicks);
            }
        }
        GlStateManager.enableAlpha();
    }

    private void renderBlockInHand(float partialTicks, TextureAtlasSprite atlas) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = 0.1f;
        GlStateManager.color((float)0.1f, (float)0.1f, (float)0.1f, (float)0.5f);
        GlStateManager.pushMatrix();
        float f1 = -1.0f;
        float f2 = 1.0f;
        float f3 = -1.0f;
        float f4 = 1.0f;
        float f5 = -0.5f;
        float f6 = atlas.getMinU();
        float f7 = atlas.getMaxU();
        float f8 = atlas.getMinV();
        float f9 = atlas.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex((double)f7, (double)f9).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex((double)f6, (double)f9).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex((double)f6, (double)f8).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex((double)f7, (double)f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private void renderWaterOverlayTexture(float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            float f = this.mc.thePlayer.getBrightness(partialTicks);
            GlStateManager.color((float)f, (float)f, (float)f, (float)0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            GlStateManager.pushMatrix();
            float f1 = 4.0f;
            float f2 = -1.0f;
            float f3 = 1.0f;
            float f4 = -1.0f;
            float f5 = 1.0f;
            float f6 = -0.5f;
            float f7 = -this.mc.thePlayer.rotationYaw / 64.0f;
            float f8 = this.mc.thePlayer.rotationPitch / 64.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-1.0, -1.0, -0.5).tex((double)(4.0f + f7), (double)(4.0f + f8)).endVertex();
            worldrenderer.pos(1.0, -1.0, -0.5).tex((double)(0.0f + f7), (double)(4.0f + f8)).endVertex();
            worldrenderer.pos(1.0, 1.0, -0.5).tex((double)(0.0f + f7), (double)(0.0f + f8)).endVertex();
            worldrenderer.pos(-1.0, 1.0, -0.5).tex((double)(4.0f + f7), (double)(0.0f + f8)).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.disableBlend();
        }
    }

    private void renderFireInFirstPerson(float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
        GlStateManager.depthFunc((int)519);
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = -0.5f;
            float f6 = 0.5f;
            float f7 = -0.5f;
            float f8 = 0.5f;
            float f9 = -0.5f;
            GlStateManager.translate((float)((float)(-(i * 2 - 1)) * 0.24f), (float)-0.3f, (float)0.0f);
            GlStateManager.rotate((float)((float)(i * 2 - 1) * 10.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setSprite(textureatlassprite);
            worldrenderer.pos(-0.5, -0.5, -0.5).tex((double)f2, (double)f4).endVertex();
            worldrenderer.pos(0.5, -0.5, -0.5).tex((double)f1, (double)f4).endVertex();
            worldrenderer.pos(0.5, 0.5, -0.5).tex((double)f1, (double)f3).endVertex();
            worldrenderer.pos(-0.5, 0.5, -0.5).tex((double)f2, (double)f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.depthFunc((int)515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayerSP entityplayer = this.mc.thePlayer;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;
        if (this.itemToRender != null && itemstack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
                boolean flag1;
                if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists() && !(flag1 = Reflector.callBoolean((Object)this.itemToRender.getItem(), (ReflectorMethod)Reflector.ForgeItem_shouldCauseReequipAnimation, (Object[])new Object[]{this.itemToRender, itemstack, this.equippedItemSlot != entityplayer.inventory.currentItem}))) {
                    this.itemToRender = itemstack;
                    this.equippedItemSlot = entityplayer.inventory.currentItem;
                    return;
                }
                flag = true;
            }
        } else {
            flag = this.itemToRender != null || itemstack != null;
        }
        float f2 = 0.4f;
        float f = flag ? 0.0f : 1.0f;
        float f1 = MathHelper.clamp_float((float)(f - this.equippedProgress), (float)-0.4f, (float)0.4f);
        this.equippedProgress += f1;
        if (this.equippedProgress < 0.1f) {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain((ItemStack)itemstack);
            }
        }
    }

    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }

    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }
}
