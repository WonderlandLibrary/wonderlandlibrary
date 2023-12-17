// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Objects;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventOverlay;
import ru.tuskevich.modules.impl.HUD.NoOverlay;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.optifine.reflect.ReflectorMethod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumBlockRenderType;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.ItemMap;
import net.optifine.reflect.Reflector;
import net.minecraft.item.ItemBow;
import com.google.common.base.MoreObjects;
import net.minecraft.util.EnumHand;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.RENDER.HandTranslate;
import ru.tuskevich.Minced;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.optifine.DynamicLights;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ItemRenderer
{
    private static final ResourceLocation RES_MAP_BACKGROUND;
    private static final ResourceLocation RES_UNDERWATER_OVERLAY;
    private final Minecraft mc;
    private ItemStack itemStackMainHand;
    private ItemStack itemStackOffHand;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    
    public ItemRenderer(final Minecraft mcIn) {
        this.itemStackMainHand = ItemStack.EMPTY;
        this.itemStackOffHand = ItemStack.EMPTY;
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }
    
    public void renderItem(final EntityLivingBase entityIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform) {
        this.renderItemSide(entityIn, heldStack, transform, false);
    }
    
    public void renderItemSide(final EntityLivingBase entitylivingbaseIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
        if (!heldStack.isEmpty()) {
            final Item item = heldStack.getItem();
            final Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            final boolean flag = this.itemRenderer.shouldRenderItemIn3D(heldStack) && block.getRenderLayer() == BlockRenderLayer.TRANSLUCENT;
            if (flag && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
                GlStateManager.depthMask(false);
            }
            this.itemRenderer.renderItem(heldStack, entitylivingbaseIn, transform, leftHanded);
            if (flag) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private void rotateArroundXAndY(final float angle, final float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(angleY, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    private void setLightmap() {
        final Minecraft mc = this.mc;
        final AbstractClientPlayer abstractclientplayer = Minecraft.player;
        int i = this.mc.world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }
        final float f = (float)(i & 0xFFFF);
        final float f2 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
    }
    
    private void rotateArm(final float p_187458_1_) {
        final Minecraft mc = this.mc;
        final EntityPlayerSP entityplayersp = Minecraft.player;
        final float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        final float f2 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayersp.rotationYaw - f2) * 0.1f, 0.0f, 1.0f, 0.0f);
    }
    
    private float getMapAngleFromPitch(final float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * 3.1415927f) * 0.5f + 0.5f;
        return f;
    }
    
    private void renderArms() {
        final Minecraft mc = this.mc;
        if (!Minecraft.player.isInvisible()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }
    
    private void renderArm(final EnumHandSide p_187455_1_) {
        final TextureManager textureManager = this.mc.getTextureManager();
        final Minecraft mc = this.mc;
        textureManager.bindTexture(Minecraft.player.getLocationSkin());
        final RenderManager renderManager = this.renderManager;
        final Minecraft mc2 = this.mc;
        final Render<AbstractClientPlayer> render = (Render<AbstractClientPlayer>)renderManager.getEntityRenderObject(Minecraft.player);
        final RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        final float f = (p_187455_1_ == EnumHandSide.RIGHT) ? 1.0f : -1.0f;
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * -41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(f * 0.3f, -1.1f, 0.45f);
        if (p_187455_1_ == EnumHandSide.RIGHT) {
            final RenderPlayer renderPlayer = renderplayer;
            final Minecraft mc3 = this.mc;
            renderPlayer.renderRightArm(Minecraft.player);
        }
        else {
            final RenderPlayer renderPlayer2 = renderplayer;
            final Minecraft mc4 = this.mc;
            renderPlayer2.renderLeftArm(Minecraft.player);
        }
        GlStateManager.popMatrix();
    }
    
    private void renderMapFirstPersonSide(final float p_187465_1_, final EnumHandSide hand, final float p_187465_3_, final ItemStack stack) {
        final float f = (hand == EnumHandSide.RIGHT) ? 1.0f : -1.0f;
        GlStateManager.translate(f * 0.125f, -0.125f, 0.0f);
        final Minecraft mc = this.mc;
        if (!Minecraft.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0f, 0.0f, 0.0f, 1.0f);
            this.renderArmFirstPerson(p_187465_1_, p_187465_3_, hand);
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51f, -0.08f + p_187465_1_ * -1.2f, -0.75f);
        final float f2 = MathHelper.sqrt(p_187465_3_);
        final float f3 = MathHelper.sin(f2 * 3.1415927f);
        final float f4 = -0.5f * f3;
        final float f5 = 0.4f * MathHelper.sin(f2 * 6.2831855f);
        final float f6 = -0.3f * MathHelper.sin(p_187465_3_ * 3.1415927f);
        GlStateManager.translate(f * f4, f5 - 0.3f * f3, f6);
        GlStateManager.rotate(f3 * -45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * f3 * -30.0f, 0.0f, 1.0f, 0.0f);
        this.renderMapFirstPerson(stack);
        GlStateManager.popMatrix();
    }
    
    private void renderMapFirstPerson(final float p_187463_1_, final float p_187463_2_, final float p_187463_3_) {
        final float f = MathHelper.sqrt(p_187463_3_);
        final float f2 = -0.2f * MathHelper.sin(p_187463_3_ * 3.1415927f);
        final float f3 = -0.4f * MathHelper.sin(f * 3.1415927f);
        GlStateManager.translate(0.0f, -f2 / 2.0f, f3);
        final float f4 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate(0.0f, 0.04f + p_187463_2_ * -1.2f + f4 * -0.5f, -0.72f);
        GlStateManager.rotate(f4 * -85.0f, 1.0f, 0.0f, 0.0f);
        this.renderArms();
        final float f5 = MathHelper.sin(f * 3.1415927f);
        GlStateManager.rotate(f5 * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        this.renderMapFirstPerson(this.itemStackMainHand);
    }
    
    private void renderMapFirstPerson(final ItemStack stack) {
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(ItemRenderer.RES_MAP_BACKGROUND);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5f, -0.5f, 0.0f);
        GlStateManager.scale(0.0078125f, 0.0078125f, 0.0078125f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        bufferbuilder.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        bufferbuilder.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        final MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, stack, this.mc.world);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
        GlStateManager.enableLighting();
    }
    
    private void renderArmFirstPerson(final float p_187456_1_, final float p_187456_2_, final EnumHandSide p_187456_3_) {
        final boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        final float f = flag ? 1.0f : -1.0f;
        final float f2 = MathHelper.sqrt(p_187456_2_);
        final float f3 = -0.3f * MathHelper.sin(f2 * 3.1415927f);
        final float f4 = 0.4f * MathHelper.sin(f2 * 6.2831855f);
        final float f5 = -0.4f * MathHelper.sin(p_187456_2_ * 3.1415927f);
        GlStateManager.translate(f * (f3 + 0.64000005f), f4 - 0.6f + p_187456_1_ * -0.6f, f5 - 0.71999997f);
        GlStateManager.rotate(f * 45.0f, 0.0f, 1.0f, 0.0f);
        final float f6 = MathHelper.sin(p_187456_2_ * p_187456_2_ * 3.1415927f);
        final float f7 = MathHelper.sin(f2 * 3.1415927f);
        GlStateManager.rotate(f * f7 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f * f6 * -20.0f, 0.0f, 0.0f, 1.0f);
        final Minecraft mc = this.mc;
        final AbstractClientPlayer abstractclientplayer = Minecraft.player;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(f * 120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f * -135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(f * 5.6f, 0.0f, 0.0f);
        final RenderPlayer renderplayer = (RenderPlayer)this.renderManager.getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();
        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);
        }
        else {
            renderplayer.renderLeftArm(abstractclientplayer);
        }
        GlStateManager.enableCull();
    }
    
    private void transformEatFirstPerson(final float p_187454_1_, final EnumHandSide hand, final ItemStack stack) {
        final Minecraft mc = this.mc;
        final float f = Minecraft.player.getItemInUseCount() - p_187454_1_ + 1.0f;
        final float f2 = f / stack.getMaxItemUseDuration();
        if (f2 < 0.8f) {
            final float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
            GlStateManager.translate(0.0f, f3, 0.0f);
        }
        final float f4 = 1.0f - (float)Math.pow(f2, 27.0);
        final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
        GlStateManager.translate(f4 * 0.6f * i, f4 * -0.5f, f4 * 0.0f);
        GlStateManager.rotate(i * f4 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(i * f4 * 30.0f, 0.0f, 0.0f, 1.0f);
    }
    
    private void transformFirstPerson(final EnumHandSide hand, final float p_187453_2_) {
        final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
        final float f = MathHelper.sin(p_187453_2_ * p_187453_2_ * 3.1415927f);
        GlStateManager.rotate(i * (45.0f + f * -20.0f), 0.0f, 1.0f, 0.0f);
        final float f2 = MathHelper.sin(MathHelper.sqrt(p_187453_2_) * 3.1415927f);
        GlStateManager.rotate(i * f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(i * -45.0f, 0.0f, 1.0f, 0.0f);
    }
    
    private void transformSideFirstPerson(final EnumHandSide p_187459_1_, final float p_187459_2_) {
        final int i = (p_187459_1_ == EnumHandSide.RIGHT) ? 1 : -1;
        GlStateManager.translate(i * 0.56f, -0.52f + p_187459_2_ * (Minced.getInstance().manager.getModule(HandTranslate.class).state ? -0.0f : -0.6f), -0.72f);
    }
    
    public void renderItemInFirstPerson(final float partialTicks) {
        final Minecraft mc = this.mc;
        final AbstractClientPlayer abstractclientplayer = Minecraft.player;
        final float f = abstractclientplayer.getSwingProgress(partialTicks);
        final EnumHand enumhand = (EnumHand)MoreObjects.firstNonNull((Object)abstractclientplayer.swingingHand, (Object)EnumHand.MAIN_HAND);
        final float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag2 = true;
        if (abstractclientplayer.isHandActive()) {
            final ItemStack itemstack = abstractclientplayer.getActiveItemStack();
            if (itemstack.getItem() instanceof ItemBow) {
                final EnumHand enumhand2 = abstractclientplayer.getActiveHand();
                flag = (enumhand2 == EnumHand.MAIN_HAND);
                flag2 = !flag;
            }
        }
        this.rotateArroundXAndY(f2, f3);
        this.setLightmap();
        this.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();
        if (flag) {
            final float f4 = (enumhand == EnumHand.MAIN_HAND) ? f : 0.0f;
            final float f5 = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks);
            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, EnumHand.MAIN_HAND, partialTicks, f2, f4, f5, this.itemStackMainHand)) {
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f2, EnumHand.MAIN_HAND, f4, this.itemStackMainHand, f5);
            }
        }
        if (flag2) {
            final float f6 = (enumhand == EnumHand.OFF_HAND) ? f : 0.0f;
            final float f7 = 1.0f - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks);
            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, EnumHand.OFF_HAND, partialTicks, f2, f6, f7, this.itemStackOffHand)) {
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f2, EnumHand.OFF_HAND, f6, this.itemStackOffHand, f7);
            }
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    public void renderItemInFirstPerson(final AbstractClientPlayer p_187457_1_, final float p_187457_2_, final float p_187457_3_, final EnumHand p_187457_4_, final float p_187457_5_, final ItemStack p_187457_6_, final float p_187457_7_) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand(p_187457_4_)) {
            final boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
            final EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
            GlStateManager.pushMatrix();
            if (p_187457_6_.isEmpty()) {
                if (flag && !p_187457_1_.isInvisible()) {
                    this.renderArmFirstPerson(p_187457_7_, p_187457_5_, enumhandside);
                }
            }
            else if (p_187457_6_.getItem() instanceof ItemMap) {
                if (flag && this.itemStackOffHand.isEmpty()) {
                    this.renderMapFirstPerson(p_187457_3_, p_187457_7_, p_187457_5_);
                }
                else {
                    this.renderMapFirstPersonSide(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
                }
            }
            else {
                final boolean flag2 = enumhandside == EnumHandSide.RIGHT;
                if (Minced.getInstance().manager.getModule(HandTranslate.class).state) {
                    if (flag2) {
                        GL11.glTranslatef(HandTranslate.right_x.getFloatValue(), HandTranslate.right_y.getFloatValue(), HandTranslate.right_z.getFloatValue());
                    }
                    else {
                        GL11.glTranslatef(HandTranslate.left_x.getFloatValue(), HandTranslate.left_y.getFloatValue(), HandTranslate.left_z.getFloatValue());
                    }
                }
                if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_) {
                    final int j = flag2 ? 1 : -1;
                    switch (p_187457_6_.getItemUseAction()) {
                        case NONE: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case EAT:
                        case DRINK: {
                            this.transformEatFirstPerson(p_187457_2_, enumhandside, p_187457_6_);
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case BLOCK: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            break;
                        }
                        case BOW: {
                            this.transformSideFirstPerson(enumhandside, p_187457_7_);
                            GlStateManager.translate(j * -0.2785682f, 0.18344387f, 0.15731531f);
                            GlStateManager.rotate(-13.935f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(j * 35.3f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(j * -9.785f, 0.0f, 0.0f, 1.0f);
                            final float n = (float)p_187457_6_.getMaxItemUseDuration();
                            final Minecraft mc = this.mc;
                            final float f5 = n - (Minecraft.player.getItemInUseCount() - p_187457_2_ + 1.0f);
                            float f6 = f5 / 20.0f;
                            f6 = (f6 * f6 + f6 * 2.0f) / 3.0f;
                            if (f6 > 1.0f) {
                                f6 = 1.0f;
                            }
                            if (f6 > 0.1f) {
                                final float f7 = MathHelper.sin((f5 - 0.1f) * 1.3f);
                                final float f8 = f6 - 0.1f;
                                final float f9 = f7 * f8;
                                GlStateManager.translate(f9 * 0.0f, f9 * 0.004f, f9 * 0.0f);
                            }
                            GlStateManager.translate(f6 * 0.0f, f6 * 0.0f, f6 * 0.04f);
                            GlStateManager.scale(1.0f, 1.0f, 1.0f + f6 * 0.2f);
                            GlStateManager.rotate(j * 45.0f, 0.0f, -1.0f, 0.0f);
                            break;
                        }
                    }
                }
                else {
                    this.transformSideFirstPerson(enumhandside, p_187457_7_);
                    final float equipProgress = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * p_187457_2_);
                    final Minecraft mc2 = this.mc;
                    final float swingprogress = Minecraft.player.getSwingProgress(p_187457_2_);
                    if (enumhandside == EnumHandSide.RIGHT) {
                        final float anim = (float)Math.sin(p_187457_5_ * 1.5707963267948966 * 2.0);
                        if (HandTranslate.swordAnim.getCurrentMode().equalsIgnoreCase("Type 1") && Minced.getInstance().manager.getModule(HandTranslate.class).state) {
                            GL11.glTranslated(0.20000000298023224, 0.20000000298023224, (double)(-0.15f * anim - 0.15f));
                            GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(-70.0f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(anim * -20.0f, 0.0f, 0.0f, 2.0f);
                            GlStateManager.rotate(anim * -75.0f, 1.0f, 0.0f, 0.0f);
                        }
                        else if (HandTranslate.swordAnim.getCurrentMode().equalsIgnoreCase("Type 2") && Minced.getInstance().manager.getModule(HandTranslate.class).state) {
                            GL11.glRotated(90.0, 0.0, 1.0, 0.0);
                            GL11.glRotated(-60.0, 0.0, 0.0, 1.0);
                            GL11.glRotated((double)(-80.0f - HandTranslate.swipePower.getFloatValue() * 10.0f * anim), 1.0, 0.0, 0.0);
                        }
                        else if (HandTranslate.swordAnim.getCurrentMode().equalsIgnoreCase("Type 3") && Minced.getInstance().manager.getModule(HandTranslate.class).state) {
                            GlStateManager.translate(0.0f, -0.0f, 0.0f);
                            GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                            final float var3 = MathHelper.sin(0.0f);
                            final float var4 = MathHelper.sin(MathHelper.sqrt(0.0f) * 3.1415927f);
                            GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
                            GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
                            GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
                            final int alpha = (int)Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 1L);
                            final float f10 = (anim > 0.5) ? (1.0f - anim) : anim;
                            GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                            GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                            GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                            GlStateManager.rotate(-f10 * 10.0f, 10.0f, 10.0f, -9.0f);
                            GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate((anim > 0.0f) ? ((float)(-alpha / 10)) : 1.0f, 1.0f, -0.0f, 1.0f);
                        }
                        else {
                            this.transformFirstPerson(enumhandside, p_187457_5_);
                        }
                    }
                    else {
                        this.transformFirstPerson(enumhandside, p_187457_5_);
                    }
                }
                this.renderItemSide(p_187457_1_, p_187457_6_, flag2 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag2);
            }
            GlStateManager.popMatrix();
        }
    }
    
    public void renderOverlays(final float partialTicks) {
        GlStateManager.disableAlpha();
        final Minecraft mc = this.mc;
        if (Minecraft.player.isEntityInsideOpaqueBlock()) {
            final WorldClient world = this.mc.world;
            final Minecraft mc2 = this.mc;
            IBlockState iblockstate = world.getBlockState(new BlockPos(Minecraft.player));
            final Minecraft mc3 = this.mc;
            BlockPos blockpos = new BlockPos(Minecraft.player);
            final Minecraft mc4 = this.mc;
            final EntityPlayer entityplayer = Minecraft.player;
            for (int i = 0; i < 8; ++i) {
                final double d0 = entityplayer.posX + ((i >> 0) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final double d2 = entityplayer.posY + ((i >> 1) % 2 - 0.5f) * 0.1f;
                final double d3 = entityplayer.posZ + ((i >> 2) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final BlockPos blockpos2 = new BlockPos(d0, d2 + entityplayer.getEyeHeight(), d3);
                final IBlockState iblockstate2 = this.mc.world.getBlockState(blockpos2);
                if (iblockstate2.causesSuffocation()) {
                    iblockstate = iblockstate2;
                    blockpos = blockpos2;
                }
            }
            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                final Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                final ReflectorMethod forgeEventFactory_renderBlockOverlay = Reflector.ForgeEventFactory_renderBlockOverlay;
                final Object[] params = new Object[5];
                final int n = 0;
                final Minecraft mc5 = this.mc;
                params[n] = Minecraft.player;
                params[1] = partialTicks;
                params[2] = object;
                params[3] = iblockstate;
                params[4] = blockpos;
                if (!Reflector.callBoolean(forgeEventFactory_renderBlockOverlay, params)) {
                    this.renderSuffocationOverlay(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }
        final Minecraft mc6 = this.mc;
        if (!Minecraft.player.isSpectator()) {
            final Minecraft mc7 = this.mc;
            if (Minecraft.player.isInsideOfMaterial(Material.WATER)) {
                final ReflectorMethod forgeEventFactory_renderWaterOverlay = Reflector.ForgeEventFactory_renderWaterOverlay;
                final Object[] params2 = new Object[2];
                final int n2 = 0;
                final Minecraft mc8 = this.mc;
                params2[n2] = Minecraft.player;
                params2[1] = partialTicks;
                if (!Reflector.callBoolean(forgeEventFactory_renderWaterOverlay, params2)) {
                    this.renderWaterOverlayTexture(partialTicks);
                }
            }
            final Minecraft mc9 = this.mc;
            if (Minecraft.player.isBurning()) {
                final ReflectorMethod forgeEventFactory_renderFireOverlay = Reflector.ForgeEventFactory_renderFireOverlay;
                final Object[] params3 = new Object[2];
                final int n3 = 0;
                final Minecraft mc10 = this.mc;
                params3[n3] = Minecraft.player;
                params3[1] = partialTicks;
                if (!Reflector.callBoolean(forgeEventFactory_renderFireOverlay, params3)) {
                    this.renderFireInFirstPerson();
                }
            }
        }
        GlStateManager.enableAlpha();
    }
    
    private void renderSuffocationOverlay(final TextureAtlasSprite sprite) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final float f = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        final float f2 = -1.0f;
        final float f3 = 1.0f;
        final float f4 = -1.0f;
        final float f5 = 1.0f;
        final float f6 = -0.5f;
        final float f7 = sprite.getMinU();
        final float f8 = sprite.getMaxU();
        final float f9 = sprite.getMinV();
        final float f10 = sprite.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0, -1.0, -0.5).tex(f8, f10).endVertex();
        bufferbuilder.pos(1.0, -1.0, -0.5).tex(f7, f10).endVertex();
        bufferbuilder.pos(1.0, 1.0, -0.5).tex(f7, f9).endVertex();
        bufferbuilder.pos(-1.0, 1.0, -0.5).tex(f8, f9).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderWaterOverlayTexture(final float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(ItemRenderer.RES_UNDERWATER_OVERLAY);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final Minecraft mc = this.mc;
            final float f = Minecraft.player.getBrightness();
            GlStateManager.color(f, f, f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            final float f2 = 4.0f;
            final float f3 = -1.0f;
            final float f4 = 1.0f;
            final float f5 = -1.0f;
            final float f6 = 1.0f;
            final float f7 = -0.5f;
            final Minecraft mc2 = this.mc;
            final float f8 = -Minecraft.player.rotationYaw / 64.0f;
            final Minecraft mc3 = this.mc;
            final float f9 = Minecraft.player.rotationPitch / 64.0f;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-1.0, -1.0, -0.5).tex(4.0f + f8, 4.0f + f9).endVertex();
            bufferbuilder.pos(1.0, -1.0, -0.5).tex(0.0f + f8, 4.0f + f9).endVertex();
            bufferbuilder.pos(1.0, 1.0, -0.5).tex(0.0f + f8, 0.0f + f9).endVertex();
            bufferbuilder.pos(-1.0, 1.0, -0.5).tex(4.0f + f8, 0.0f + f9).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
        }
    }
    
    private void renderFireInFirstPerson() {
        if (Minced.getInstance().manager.getModule(NoOverlay.class).state) {
            final EventOverlay eventOverlay = new EventOverlay(EventOverlay.OverlayType.Fire);
            EventManager.call(eventOverlay);
            if (eventOverlay.isCanceled()) {
                return;
            }
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            final float f2 = textureatlassprite.getMinU();
            final float f3 = textureatlassprite.getMaxU();
            final float f4 = textureatlassprite.getMinV();
            final float f5 = textureatlassprite.getMaxV();
            final float f6 = -0.5f;
            final float f7 = 0.5f;
            final float f8 = -0.5f;
            final float f9 = 0.5f;
            final float f10 = -0.5f;
            GlStateManager.translate(-(i * 2 - 1) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((i * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.setSprite(textureatlassprite);
            bufferbuilder.pos(-0.5, -0.5, -0.5).tex(f3, f5).endVertex();
            bufferbuilder.pos(0.5, -0.5, -0.5).tex(f2, f5).endVertex();
            bufferbuilder.pos(0.5, 0.5, -0.5).tex(f2, f4).endVertex();
            bufferbuilder.pos(-0.5, 0.5, -0.5).tex(f3, f4).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }
    
    public void updateEquippedItem() {
        this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
        this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
        final Minecraft mc = this.mc;
        final EntityPlayerSP entityplayersp = Minecraft.player;
        final ItemStack itemstack = entityplayersp.getHeldItemMainhand();
        final ItemStack itemstack2 = entityplayersp.getHeldItemOffhand();
        if (entityplayersp.isRowingBoat()) {
            this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4f, 0.0f, 1.0f);
            this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4f, 0.0f, 1.0f);
        }
        else {
            final float f = entityplayersp.getCooledAttackStrength(1.0f);
            if (Reflector.ForgeHooksClient_shouldCauseReequipAnimation.exists()) {
                final boolean flag = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, this.itemStackMainHand, itemstack, entityplayersp.inventory.currentItem);
                final boolean flag2 = Reflector.callBoolean(Reflector.ForgeHooksClient_shouldCauseReequipAnimation, this.itemStackOffHand, itemstack2, -1);
                if (!flag && !Objects.equals(this.itemStackMainHand, itemstack)) {
                    this.itemStackMainHand = itemstack;
                }
                if (!flag && !Objects.equals(this.itemStackOffHand, itemstack2)) {
                    this.itemStackOffHand = itemstack2;
                }
                this.equippedProgressMainHand += MathHelper.clamp((flag ? 0.0f : (f * f * f)) - this.equippedProgressMainHand, -0.4f, 0.4f);
                this.equippedProgressOffHand += MathHelper.clamp((float)(flag2 ? 0 : 1) - this.equippedProgressOffHand, -0.4f, 0.4f);
            }
            else {
                this.equippedProgressMainHand += MathHelper.clamp((Objects.equals(this.itemStackMainHand, itemstack) ? (f * f * f) : 0.0f) - this.equippedProgressMainHand, -0.4f, 0.4f);
                this.equippedProgressOffHand += MathHelper.clamp((float)(Objects.equals(this.itemStackOffHand, itemstack2) ? 1 : 0) - this.equippedProgressOffHand, -0.4f, 0.4f);
            }
        }
        if (this.equippedProgressMainHand < 0.1f) {
            this.itemStackMainHand = itemstack;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(this.itemStackMainHand);
            }
        }
        if (this.equippedProgressOffHand < 0.1f) {
            this.itemStackOffHand = itemstack2;
            if (Config.isShaders()) {
                Shaders.setItemToRenderOff(this.itemStackOffHand);
            }
        }
    }
    
    public void resetEquippedProgress(final EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            this.equippedProgressMainHand = 0.0f;
        }
        else {
            this.equippedProgressOffHand = 0.0f;
        }
    }
    
    static {
        RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
        RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    }
}
