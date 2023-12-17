// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.item.ItemFishFood;
import net.minecraft.init.Items;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockWall;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Blocks;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.optifine.CustomColors;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import java.awt.Color;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import ru.tuskevich.modules.impl.RENDER.EnchantmentColor;
import net.optifine.shaders.ShadersRender;
import net.optifine.shaders.Shaders;
import net.optifine.CustomItems;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class RenderItem implements IResourceManagerReloadListener
{
    private static final ResourceLocation RES_ITEM_GLINT;
    private boolean notRenderingEffectsInGUI;
    public float zLevel;
    private final ItemModelMesher itemModelMesher;
    private final TextureManager textureManager;
    private final ItemColors itemColors;
    private ResourceLocation modelLocation;
    private boolean renderItemGui;
    public ModelManager modelManager;
    private boolean renderModelHasEmissive;
    private boolean renderModelEmissive;
    private boolean forgeAllowEmissiveItems;
    
    public RenderItem(final TextureManager p_i46552_1_, final ModelManager p_i46552_2_, final ItemColors p_i46552_3_) {
        this.notRenderingEffectsInGUI = true;
        this.modelLocation = null;
        this.renderItemGui = false;
        this.modelManager = null;
        this.renderModelHasEmissive = false;
        this.renderModelEmissive = false;
        this.forgeAllowEmissiveItems = Reflector.getFieldValueBoolean(Reflector.ForgeModContainer_allowEmissiveItems, false);
        this.textureManager = p_i46552_1_;
        this.modelManager = p_i46552_2_;
        if (Reflector.ItemModelMesherForge_Constructor.exists()) {
            this.itemModelMesher = (ItemModelMesher)Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, p_i46552_2_);
        }
        else {
            this.itemModelMesher = new ItemModelMesher(p_i46552_2_);
        }
        this.registerItems();
        this.itemColors = p_i46552_3_;
    }
    
    public ItemModelMesher getItemModelMesher() {
        return this.itemModelMesher;
    }
    
    protected void registerItem(final Item itm, final int subType, final String identifier) {
        this.itemModelMesher.register(itm, subType, new ModelResourceLocation(identifier, "inventory"));
    }
    
    protected void registerBlock(final Block blk, final int subType, final String identifier) {
        this.registerItem(Item.getItemFromBlock(blk), subType, identifier);
    }
    
    private void registerBlock(final Block blk, final String identifier) {
        this.registerBlock(blk, 0, identifier);
    }
    
    private void registerItem(final Item itm, final String identifier) {
        this.registerItem(itm, 0, identifier);
    }
    
    private void renderModel(final IBakedModel model, final ItemStack stack) {
        this.renderModel(model, -1, stack);
    }
    
    public void renderModel(final IBakedModel model, final int color) {
        this.renderModel(model, color, ItemStack.EMPTY);
    }
    
    private void renderModel(final IBakedModel model, final int color, final ItemStack stack) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final boolean flag = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
        final boolean flag2 = Config.isMultiTexture() && flag;
        if (flag2) {
            bufferbuilder.setBlockLayer(BlockRenderLayer.SOLID);
        }
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
        for (final EnumFacing enumfacing : EnumFacing.VALUES) {
            this.renderQuads(bufferbuilder, model.getQuads(null, enumfacing, 0L), color, stack);
        }
        this.renderQuads(bufferbuilder, model.getQuads(null, null, 0L), color, stack);
        tessellator.draw();
        if (flag2) {
            bufferbuilder.setBlockLayer(null);
            GlStateManager.bindCurrentTexture();
        }
    }
    
    public void renderItem(final ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5f, -0.5f, -0.5f);
            if (model.isBuiltInRenderer()) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableRescaleNormal();
                if (Reflector.ForgeItem_getTileEntityItemStackRenderer.exists()) {
                    final TileEntityItemStackRenderer tileentityitemstackrenderer = (TileEntityItemStackRenderer)Reflector.call(stack.getItem(), Reflector.ForgeItem_getTileEntityItemStackRenderer, new Object[0]);
                    tileentityitemstackrenderer.renderByItem(stack);
                }
                else {
                    TileEntityItemStackRenderer.instance.renderByItem(stack);
                }
            }
            else {
                if (Config.isCustomItems()) {
                    model = CustomItems.getCustomItemModel(stack, model, this.modelLocation, false);
                    this.modelLocation = null;
                }
                this.renderModelHasEmissive = false;
                this.renderModel(model, stack);
                if (this.renderModelHasEmissive) {
                    final float f = OpenGlHelper.lastBrightnessX;
                    final float f2 = OpenGlHelper.lastBrightnessY;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, f2);
                    this.renderModelEmissive = true;
                    this.renderModel(model, stack);
                    this.renderModelEmissive = false;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
                }
                if (stack.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, stack, model))) {
                    this.renderEffect(model);
                }
            }
            GlStateManager.popMatrix();
        }
    }
    
    private void renderEffect(final IBakedModel model) {
        if ((!Config.isCustomItems() || CustomItems.isUseGlint()) && (!Config.isShaders() || !Shaders.isShadowPass)) {
            GlStateManager.depthMask(false);
            GlStateManager.depthFunc(514);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            this.textureManager.bindTexture(RenderItem.RES_ITEM_GLINT);
            if (Config.isShaders() && !this.renderItemGui) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0f, 8.0f, 8.0f);
            final float f = Minecraft.getSystemTime() % 3000L / 3000.0f / 8.0f;
            GlStateManager.translate(f, 0.0f, 0.0f);
            GlStateManager.rotate(-50.0f, 0.0f, 0.0f, 1.0f);
            final EnchantmentColor c = (EnchantmentColor)Minced.getInstance().manager.getModule(EnchantmentColor.class);
            if (c.state) {
                this.renderModel(model, c.enchantColor.getColorValue());
            }
            else {
                this.renderModel(model, -8372020);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0f, 8.0f, 8.0f);
            final float f2 = Minecraft.getSystemTime() % 4873L / 4873.0f / 8.0f;
            GlStateManager.translate(-f2, 0.0f, 0.0f);
            GlStateManager.rotate(10.0f, 0.0f, 0.0f, 1.0f);
            if (c.state) {
                final Color cc = new Color(c.enchantColor.getColorValue());
                GlStateManager.color(cc.getRed() / 255.0f, cc.getGreen() / 255.0f, cc.getBlue() / 255.0f, cc.getAlpha() / 255.0f);
            }
            else {
                GlStateManager.color(0.38f, 0.19f, 0.608f, 1.0f);
            }
            if (c.state) {
                this.renderModel(model, c.enchantColor.getColorValue());
            }
            else {
                this.renderModel(model, -8372020);
            }
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (Config.isShaders() && !this.renderItemGui) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }
    
    private void putQuadNormal(final BufferBuilder renderer, final BakedQuad quad) {
        final Vec3i vec3i = quad.getFace().getDirectionVec();
        renderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
    }
    
    private void renderQuad(final BufferBuilder renderer, BakedQuad quad, final int color) {
        if (this.renderModelEmissive) {
            if (quad.getQuadEmissive() == null) {
                return;
            }
            quad = quad.getQuadEmissive();
        }
        else if (quad.getQuadEmissive() != null) {
            this.renderModelHasEmissive = true;
        }
        if (renderer.isMultiTexture()) {
            renderer.addVertexData(quad.getVertexDataSingle());
        }
        else {
            renderer.addVertexData(quad.getVertexData());
        }
        renderer.putSprite(quad.getSprite());
        if (Reflector.ForgeHooksClient_putQuadColor.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_putQuadColor, renderer, quad, color);
        }
        else {
            renderer.putColor4(color);
        }
        this.putQuadNormal(renderer, quad);
    }
    
    private void renderQuads(final BufferBuilder renderer, final List<BakedQuad> quads, final int color, final ItemStack stack) {
        final boolean flag = color == -1 && !stack.isEmpty();
        for (int i = 0, j = quads.size(); i < j; ++i) {
            final BakedQuad bakedquad = quads.get(i);
            int k = color;
            if (flag && bakedquad.hasTintIndex()) {
                k = this.itemColors.colorMultiplier(stack, bakedquad.getTintIndex());
                if (Config.isCustomColors()) {
                    k = CustomColors.getColorFromItemStack(stack, bakedquad.getTintIndex(), k);
                }
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }
                k |= 0xFF000000;
            }
            this.renderQuad(renderer, bakedquad, k);
        }
    }
    
    public boolean shouldRenderItemIn3D(final ItemStack stack) {
        final IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        return ibakedmodel != null && ibakedmodel.isGui3d();
    }
    
    public void renderItem(final ItemStack stack, final ItemCameraTransforms.TransformType cameraTransformType) {
        if (!stack.isEmpty()) {
            final IBakedModel ibakedmodel = this.getItemModelWithOverrides(stack, null, null);
            this.renderItemModel(stack, ibakedmodel, cameraTransformType, false);
        }
    }
    
    public IBakedModel getItemModelWithOverrides(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entitylivingbaseIn) {
        final IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        final Item item = stack.getItem();
        if (Config.isCustomItems()) {
            if (item != null && item.hasCustomProperties()) {
                this.modelLocation = ibakedmodel.getOverrides().applyOverride(stack, worldIn, entitylivingbaseIn);
            }
            final IBakedModel ibakedmodel2 = CustomItems.getCustomItemModel(stack, ibakedmodel, this.modelLocation, true);
            if (ibakedmodel2 != ibakedmodel) {
                return ibakedmodel2;
            }
        }
        if (Reflector.ModelLoader_getInventoryVariant.exists()) {
            return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, worldIn, entitylivingbaseIn);
        }
        if (item != null && item.hasCustomProperties()) {
            final ResourceLocation resourcelocation = ibakedmodel.getOverrides().applyOverride(stack, worldIn, entitylivingbaseIn);
            return (resourcelocation == null) ? ibakedmodel : this.itemModelMesher.getModelManager().getModel(new ModelResourceLocation(resourcelocation, "inventory"));
        }
        return ibakedmodel;
    }
    
    public void renderItem(final ItemStack stack, final EntityLivingBase entitylivingbaseIn, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
        if (!stack.isEmpty() && entitylivingbaseIn != null) {
            final IBakedModel ibakedmodel = this.getItemModelWithOverrides(stack, entitylivingbaseIn.world, entitylivingbaseIn);
            this.renderItemModel(stack, ibakedmodel, transform, leftHanded);
        }
    }
    
    protected void renderItemModel(final ItemStack stack, IBakedModel bakedmodel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
        if (!stack.isEmpty()) {
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
                bakedmodel = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, bakedmodel, transform, leftHanded);
            }
            else {
                final ItemCameraTransforms itemcameratransforms = bakedmodel.getItemCameraTransforms();
                ItemCameraTransforms.applyTransformSide(itemcameratransforms.getTransform(transform), leftHanded);
                if (this.isThereOneNegativeScale(itemcameratransforms.getTransform(transform))) {
                    GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
                }
            }
            CustomItems.setRenderOffHand(leftHanded);
            this.renderItem(stack, bakedmodel);
            CustomItems.setRenderOffHand(false);
            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        }
    }
    
    private boolean isThereOneNegativeScale(final ItemTransformVec3f itemTranformVec) {
        return itemTranformVec.scale.x < 0.0f ^ itemTranformVec.scale.y < 0.0f ^ itemTranformVec.scale.z < 0.0f;
    }
    
    public void renderItemIntoGUI(final ItemStack stack, final double x, final double y) {
        this.renderItemModelIntoGUI(stack, x, y, this.getItemModelWithOverrides(stack, null, null));
    }
    
    protected void renderItemModelIntoGUI(final ItemStack stack, final double x, final double y, IBakedModel bakedmodel) {
        this.renderItemGui = true;
        GlStateManager.pushMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.setupGuiTransform(x, y, bakedmodel.isGui3d());
        if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
            bakedmodel = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        }
        else {
            bakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
        }
        this.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        this.renderItemGui = false;
    }
    
    private void setupGuiTransform(final double xPosition, final double yPosition, final boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0f + this.zLevel);
        GlStateManager.translate(8.0f, 8.0f, 0.0f);
        GlStateManager.scale(1.0f, -1.0f, 1.0f);
        GlStateManager.scale(16.0f, 16.0f, 16.0f);
        if (isGui3d) {
            GlStateManager.enableLighting();
        }
        else {
            GlStateManager.disableLighting();
        }
    }
    
    public void renderItemAndEffectIntoGUI(final ItemStack stack, final int xPosition, final int yPosition) {
        Minecraft.getMinecraft();
        this.renderItemAndEffectIntoGUI(Minecraft.player, stack, xPosition, yPosition);
    }
    
    public void renderItemAndEffectIntoGUI(@Nullable final EntityLivingBase p_184391_1_, final ItemStack p_184391_2_, final int p_184391_3_, final int p_184391_4_) {
        if (!p_184391_2_.isEmpty()) {
            this.zLevel += 50.0f;
            try {
                this.renderItemModelIntoGUI(p_184391_2_, p_184391_3_, p_184391_4_, this.getItemModelWithOverrides(p_184391_2_, null, p_184391_1_));
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addDetail("Item Type", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.getItem());
                    }
                });
                if (Reflector.IForgeRegistryEntry_Impl_getRegistryName.exists()) {
                    crashreportcategory.addDetail("Registry Name", ReflectorForge.getDetailItemRegistryName(p_184391_2_.getItem()));
                }
                crashreportcategory.addDetail("Item Aux", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.getMetadata());
                    }
                });
                crashreportcategory.addDetail("Item NBT", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.getTagCompound());
                    }
                });
                crashreportcategory.addDetail("Item Foil", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(p_184391_2_.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }
            this.zLevel -= 50.0f;
        }
    }
    
    public void renderItemOverlays(final FontRenderer fr, final ItemStack stack, final double xPosition, final double yPosition) {
        this.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, null);
    }
    
    public void renderItemOverlayIntoGUI(final FontRenderer fr, final ItemStack stack, final double xPosition, final double yPosition, @Nullable final String text) {
        if (!stack.isEmpty()) {
            if (stack.getCount() != 1 || text != null) {
                final String s = (text == null) ? String.valueOf(stack.getCount()) : text;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                fr.drawStringWithShadow(s, (float)(xPosition + 19.0 - 2.0 - fr.getStringWidth(s)), (float)(yPosition + 6.0 + 3.0), 16777215);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
            }
            if (ReflectorForge.isItemDamaged(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                final Tessellator tessellator = Tessellator.getInstance();
                final BufferBuilder bufferbuilder = tessellator.getBuffer();
                final float f = (float)stack.getItemDamage();
                final float f2 = (float)stack.getMaxDamage();
                final float f3 = Math.max(0.0f, (f2 - f) / f2);
                int i = Math.round(13.0f - f * 13.0f / f2);
                int j = MathHelper.hsvToRGB(f3 / 3.0f, 1.0f, 1.0f);
                if (Reflector.ForgeItem_getDurabilityForDisplay.exists() && Reflector.ForgeItem_getRGBDurabilityForDisplay.exists()) {
                    final double d0 = Reflector.callDouble(stack.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, stack);
                    final int k = Reflector.callInt(stack.getItem(), Reflector.ForgeItem_getRGBDurabilityForDisplay, stack);
                    i = Math.round(13.0f - (float)d0 * 13.0f);
                    j = k;
                }
                if (Config.isCustomColors()) {
                    j = CustomColors.getDurabilityColor(f3, j);
                }
                if (Reflector.ForgeItem_getDurabilityForDisplay.exists() && Reflector.ForgeItem_getRGBDurabilityForDisplay.exists()) {
                    final double d2 = Reflector.callDouble(stack.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, stack);
                    final int l = Reflector.callInt(stack.getItem(), Reflector.ForgeItem_getRGBDurabilityForDisplay, stack);
                    i = Math.round(13.0f - (float)d2 * 13.0f);
                    j = l;
                }
                if (Config.isCustomColors()) {
                    j = CustomColors.getDurabilityColor(f3, j);
                }
                this.draw(bufferbuilder, xPosition + 2.0, yPosition + 13.0, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, xPosition + 2.0, yPosition + 13.0, i, 1, j >> 16 & 0xFF, j >> 8 & 0xFF, j & 0xFF, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
            Minecraft.getMinecraft();
            final EntityPlayerSP entityplayersp = Minecraft.player;
            final float f4 = (entityplayersp == null) ? 0.0f : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
            if (f4 > 0.0f) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                final Tessellator tessellator2 = Tessellator.getInstance();
                final BufferBuilder bufferbuilder2 = tessellator2.getBuffer();
                this.draw(bufferbuilder2, xPosition, yPosition + MathHelper.floor(16.0f * (1.0f - f4)), 16, MathHelper.ceil(16.0f * f4), 255, 255, 255, 127);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }
    
    private void draw(final BufferBuilder renderer, final double x, final double y, final int width, final int height, final int red, final int green, final int blue, final int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos(x + 0.0, y + 0.0, 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos(x + 0.0, y + height, 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos(x + width, y + height, 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos(x + width, y + 0.0, 0.0).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
    
    private void registerItems() {
        this.registerBlock(Blocks.ANVIL, "anvil_intact");
        this.registerBlock(Blocks.ANVIL, 1, "anvil_slightly_damaged");
        this.registerBlock(Blocks.ANVIL, 2, "anvil_very_damaged");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.RED.getMetadata(), "red_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
        this.registerBlock(Blocks.CARPET, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
        this.registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
        this.registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
        this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
        this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
        this.registerBlock(Blocks.DIRT, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
        this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
        this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
        this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
        this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
        this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
        this.registerBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
        this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
        this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
        this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
        this.registerBlock(Blocks.LEAVES, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
        this.registerBlock(Blocks.LEAVES2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
        this.registerBlock(Blocks.LEAVES2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
        this.registerBlock(Blocks.LOG, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
        this.registerBlock(Blocks.LOG2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
        this.registerBlock(Blocks.LOG2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
        this.registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
        this.registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
        this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
        this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
        this.registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
        this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
        this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
        this.registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
        this.registerBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
        this.registerBlock(Blocks.SAND, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
        this.registerBlock(Blocks.SAND, BlockSand.EnumType.SAND.getMetadata(), "sand");
        this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
        this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
        this.registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
        this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
        this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
        this.registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
        this.registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
        this.registerBlock(Blocks.SPONGE, 0, "sponge");
        this.registerBlock(Blocks.SPONGE, 1, "sponge_wet");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
        this.registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
        this.registerBlock(Blocks.STONE, BlockStone.EnumType.STONE.getMetadata(), "stone");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
        this.registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
        this.registerBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
        this.registerBlock(Blocks.STONE_SLAB2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
        this.registerBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
        this.registerBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
        this.registerBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
        this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
        this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
        this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
        this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
        this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
        this.registerBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.BLACK.getMetadata(), "black_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.GREEN.getMetadata(), "green_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.LIME.getMetadata(), "lime_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.PINK.getMetadata(), "pink_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.RED.getMetadata(), "red_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.WHITE.getMetadata(), "white_wool");
        this.registerBlock(Blocks.WOOL, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
        this.registerBlock(Blocks.FARMLAND, "farmland");
        this.registerBlock(Blocks.ACACIA_STAIRS, "acacia_stairs");
        this.registerBlock(Blocks.ACTIVATOR_RAIL, "activator_rail");
        this.registerBlock(Blocks.BEACON, "beacon");
        this.registerBlock(Blocks.BEDROCK, "bedrock");
        this.registerBlock(Blocks.BIRCH_STAIRS, "birch_stairs");
        this.registerBlock(Blocks.BOOKSHELF, "bookshelf");
        this.registerBlock(Blocks.BRICK_BLOCK, "brick_block");
        this.registerBlock(Blocks.BRICK_BLOCK, "brick_block");
        this.registerBlock(Blocks.BRICK_STAIRS, "brick_stairs");
        this.registerBlock(Blocks.BROWN_MUSHROOM, "brown_mushroom");
        this.registerBlock(Blocks.CACTUS, "cactus");
        this.registerBlock(Blocks.CLAY, "clay");
        this.registerBlock(Blocks.COAL_BLOCK, "coal_block");
        this.registerBlock(Blocks.COAL_ORE, "coal_ore");
        this.registerBlock(Blocks.COBBLESTONE, "cobblestone");
        this.registerBlock(Blocks.CRAFTING_TABLE, "crafting_table");
        this.registerBlock(Blocks.DARK_OAK_STAIRS, "dark_oak_stairs");
        this.registerBlock(Blocks.DAYLIGHT_DETECTOR, "daylight_detector");
        this.registerBlock(Blocks.DEADBUSH, "dead_bush");
        this.registerBlock(Blocks.DETECTOR_RAIL, "detector_rail");
        this.registerBlock(Blocks.DIAMOND_BLOCK, "diamond_block");
        this.registerBlock(Blocks.DIAMOND_ORE, "diamond_ore");
        this.registerBlock(Blocks.DISPENSER, "dispenser");
        this.registerBlock(Blocks.DROPPER, "dropper");
        this.registerBlock(Blocks.EMERALD_BLOCK, "emerald_block");
        this.registerBlock(Blocks.EMERALD_ORE, "emerald_ore");
        this.registerBlock(Blocks.ENCHANTING_TABLE, "enchanting_table");
        this.registerBlock(Blocks.END_PORTAL_FRAME, "end_portal_frame");
        this.registerBlock(Blocks.END_STONE, "end_stone");
        this.registerBlock(Blocks.OAK_FENCE, "oak_fence");
        this.registerBlock(Blocks.SPRUCE_FENCE, "spruce_fence");
        this.registerBlock(Blocks.BIRCH_FENCE, "birch_fence");
        this.registerBlock(Blocks.JUNGLE_FENCE, "jungle_fence");
        this.registerBlock(Blocks.DARK_OAK_FENCE, "dark_oak_fence");
        this.registerBlock(Blocks.ACACIA_FENCE, "acacia_fence");
        this.registerBlock(Blocks.OAK_FENCE_GATE, "oak_fence_gate");
        this.registerBlock(Blocks.SPRUCE_FENCE_GATE, "spruce_fence_gate");
        this.registerBlock(Blocks.BIRCH_FENCE_GATE, "birch_fence_gate");
        this.registerBlock(Blocks.JUNGLE_FENCE_GATE, "jungle_fence_gate");
        this.registerBlock(Blocks.DARK_OAK_FENCE_GATE, "dark_oak_fence_gate");
        this.registerBlock(Blocks.ACACIA_FENCE_GATE, "acacia_fence_gate");
        this.registerBlock(Blocks.FURNACE, "furnace");
        this.registerBlock(Blocks.GLASS, "glass");
        this.registerBlock(Blocks.GLASS_PANE, "glass_pane");
        this.registerBlock(Blocks.GLOWSTONE, "glowstone");
        this.registerBlock(Blocks.GOLDEN_RAIL, "golden_rail");
        this.registerBlock(Blocks.GOLD_BLOCK, "gold_block");
        this.registerBlock(Blocks.GOLD_ORE, "gold_ore");
        this.registerBlock(Blocks.GRASS, "grass");
        this.registerBlock(Blocks.GRASS_PATH, "grass_path");
        this.registerBlock(Blocks.GRAVEL, "gravel");
        this.registerBlock(Blocks.HARDENED_CLAY, "hardened_clay");
        this.registerBlock(Blocks.HAY_BLOCK, "hay_block");
        this.registerBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "heavy_weighted_pressure_plate");
        this.registerBlock(Blocks.HOPPER, "hopper");
        this.registerBlock(Blocks.ICE, "ice");
        this.registerBlock(Blocks.IRON_BARS, "iron_bars");
        this.registerBlock(Blocks.IRON_BLOCK, "iron_block");
        this.registerBlock(Blocks.IRON_ORE, "iron_ore");
        this.registerBlock(Blocks.IRON_TRAPDOOR, "iron_trapdoor");
        this.registerBlock(Blocks.JUKEBOX, "jukebox");
        this.registerBlock(Blocks.JUNGLE_STAIRS, "jungle_stairs");
        this.registerBlock(Blocks.LADDER, "ladder");
        this.registerBlock(Blocks.LAPIS_BLOCK, "lapis_block");
        this.registerBlock(Blocks.LAPIS_ORE, "lapis_ore");
        this.registerBlock(Blocks.LEVER, "lever");
        this.registerBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "light_weighted_pressure_plate");
        this.registerBlock(Blocks.LIT_PUMPKIN, "lit_pumpkin");
        this.registerBlock(Blocks.MELON_BLOCK, "melon_block");
        this.registerBlock(Blocks.MOSSY_COBBLESTONE, "mossy_cobblestone");
        this.registerBlock(Blocks.MYCELIUM, "mycelium");
        this.registerBlock(Blocks.NETHERRACK, "netherrack");
        this.registerBlock(Blocks.NETHER_BRICK, "nether_brick");
        this.registerBlock(Blocks.NETHER_BRICK_FENCE, "nether_brick_fence");
        this.registerBlock(Blocks.NETHER_BRICK_STAIRS, "nether_brick_stairs");
        this.registerBlock(Blocks.NOTEBLOCK, "noteblock");
        this.registerBlock(Blocks.OAK_STAIRS, "oak_stairs");
        this.registerBlock(Blocks.OBSIDIAN, "obsidian");
        this.registerBlock(Blocks.PACKED_ICE, "packed_ice");
        this.registerBlock(Blocks.PISTON, "piston");
        this.registerBlock(Blocks.PUMPKIN, "pumpkin");
        this.registerBlock(Blocks.QUARTZ_ORE, "quartz_ore");
        this.registerBlock(Blocks.QUARTZ_STAIRS, "quartz_stairs");
        this.registerBlock(Blocks.RAIL, "rail");
        this.registerBlock(Blocks.REDSTONE_BLOCK, "redstone_block");
        this.registerBlock(Blocks.REDSTONE_LAMP, "redstone_lamp");
        this.registerBlock(Blocks.REDSTONE_ORE, "redstone_ore");
        this.registerBlock(Blocks.REDSTONE_TORCH, "redstone_torch");
        this.registerBlock(Blocks.RED_MUSHROOM, "red_mushroom");
        this.registerBlock(Blocks.SANDSTONE_STAIRS, "sandstone_stairs");
        this.registerBlock(Blocks.RED_SANDSTONE_STAIRS, "red_sandstone_stairs");
        this.registerBlock(Blocks.SEA_LANTERN, "sea_lantern");
        this.registerBlock(Blocks.SLIME_BLOCK, "slime");
        this.registerBlock(Blocks.SNOW, "snow");
        this.registerBlock(Blocks.SNOW_LAYER, "snow_layer");
        this.registerBlock(Blocks.SOUL_SAND, "soul_sand");
        this.registerBlock(Blocks.SPRUCE_STAIRS, "spruce_stairs");
        this.registerBlock(Blocks.STICKY_PISTON, "sticky_piston");
        this.registerBlock(Blocks.STONE_BRICK_STAIRS, "stone_brick_stairs");
        this.registerBlock(Blocks.STONE_BUTTON, "stone_button");
        this.registerBlock(Blocks.STONE_PRESSURE_PLATE, "stone_pressure_plate");
        this.registerBlock(Blocks.STONE_STAIRS, "stone_stairs");
        this.registerBlock(Blocks.TNT, "tnt");
        this.registerBlock(Blocks.TORCH, "torch");
        this.registerBlock(Blocks.TRAPDOOR, "trapdoor");
        this.registerBlock(Blocks.TRIPWIRE_HOOK, "tripwire_hook");
        this.registerBlock(Blocks.VINE, "vine");
        this.registerBlock(Blocks.WATERLILY, "waterlily");
        this.registerBlock(Blocks.WEB, "web");
        this.registerBlock(Blocks.WOODEN_BUTTON, "wooden_button");
        this.registerBlock(Blocks.WOODEN_PRESSURE_PLATE, "wooden_pressure_plate");
        this.registerBlock(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
        this.registerBlock(Blocks.END_ROD, "end_rod");
        this.registerBlock(Blocks.CHORUS_PLANT, "chorus_plant");
        this.registerBlock(Blocks.CHORUS_FLOWER, "chorus_flower");
        this.registerBlock(Blocks.PURPUR_BLOCK, "purpur_block");
        this.registerBlock(Blocks.PURPUR_PILLAR, "purpur_pillar");
        this.registerBlock(Blocks.PURPUR_STAIRS, "purpur_stairs");
        this.registerBlock(Blocks.PURPUR_SLAB, "purpur_slab");
        this.registerBlock(Blocks.PURPUR_DOUBLE_SLAB, "purpur_double_slab");
        this.registerBlock(Blocks.END_BRICKS, "end_bricks");
        this.registerBlock(Blocks.MAGMA, "magma");
        this.registerBlock(Blocks.NETHER_WART_BLOCK, "nether_wart_block");
        this.registerBlock(Blocks.RED_NETHER_BRICK, "red_nether_brick");
        this.registerBlock(Blocks.BONE_BLOCK, "bone_block");
        this.registerBlock(Blocks.STRUCTURE_VOID, "structure_void");
        this.registerBlock(Blocks.OBSERVER, "observer");
        this.registerBlock(Blocks.WHITE_SHULKER_BOX, "white_shulker_box");
        this.registerBlock(Blocks.ORANGE_SHULKER_BOX, "orange_shulker_box");
        this.registerBlock(Blocks.MAGENTA_SHULKER_BOX, "magenta_shulker_box");
        this.registerBlock(Blocks.LIGHT_BLUE_SHULKER_BOX, "light_blue_shulker_box");
        this.registerBlock(Blocks.YELLOW_SHULKER_BOX, "yellow_shulker_box");
        this.registerBlock(Blocks.LIME_SHULKER_BOX, "lime_shulker_box");
        this.registerBlock(Blocks.PINK_SHULKER_BOX, "pink_shulker_box");
        this.registerBlock(Blocks.GRAY_SHULKER_BOX, "gray_shulker_box");
        this.registerBlock(Blocks.SILVER_SHULKER_BOX, "silver_shulker_box");
        this.registerBlock(Blocks.CYAN_SHULKER_BOX, "cyan_shulker_box");
        this.registerBlock(Blocks.PURPLE_SHULKER_BOX, "purple_shulker_box");
        this.registerBlock(Blocks.BLUE_SHULKER_BOX, "blue_shulker_box");
        this.registerBlock(Blocks.BROWN_SHULKER_BOX, "brown_shulker_box");
        this.registerBlock(Blocks.GREEN_SHULKER_BOX, "green_shulker_box");
        this.registerBlock(Blocks.RED_SHULKER_BOX, "red_shulker_box");
        this.registerBlock(Blocks.BLACK_SHULKER_BOX, "black_shulker_box");
        this.registerBlock(Blocks.WHITE_GLAZED_TERRACOTTA, "white_glazed_terracotta");
        this.registerBlock(Blocks.ORANGE_GLAZED_TERRACOTTA, "orange_glazed_terracotta");
        this.registerBlock(Blocks.MAGENTA_GLAZED_TERRACOTTA, "magenta_glazed_terracotta");
        this.registerBlock(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, "light_blue_glazed_terracotta");
        this.registerBlock(Blocks.YELLOW_GLAZED_TERRACOTTA, "yellow_glazed_terracotta");
        this.registerBlock(Blocks.LIME_GLAZED_TERRACOTTA, "lime_glazed_terracotta");
        this.registerBlock(Blocks.PINK_GLAZED_TERRACOTTA, "pink_glazed_terracotta");
        this.registerBlock(Blocks.GRAY_GLAZED_TERRACOTTA, "gray_glazed_terracotta");
        this.registerBlock(Blocks.SILVER_GLAZED_TERRACOTTA, "silver_glazed_terracotta");
        this.registerBlock(Blocks.CYAN_GLAZED_TERRACOTTA, "cyan_glazed_terracotta");
        this.registerBlock(Blocks.PURPLE_GLAZED_TERRACOTTA, "purple_glazed_terracotta");
        this.registerBlock(Blocks.BLUE_GLAZED_TERRACOTTA, "blue_glazed_terracotta");
        this.registerBlock(Blocks.BROWN_GLAZED_TERRACOTTA, "brown_glazed_terracotta");
        this.registerBlock(Blocks.GREEN_GLAZED_TERRACOTTA, "green_glazed_terracotta");
        this.registerBlock(Blocks.RED_GLAZED_TERRACOTTA, "red_glazed_terracotta");
        this.registerBlock(Blocks.BLACK_GLAZED_TERRACOTTA, "black_glazed_terracotta");
        for (final EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            this.registerBlock(Blocks.CONCRETE, enumdyecolor.getMetadata(), enumdyecolor.getDyeColorName() + "_concrete");
            this.registerBlock(Blocks.CONCRETE_POWDER, enumdyecolor.getMetadata(), enumdyecolor.getDyeColorName() + "_concrete_powder");
        }
        this.registerBlock(Blocks.CHEST, "chest");
        this.registerBlock(Blocks.TRAPPED_CHEST, "trapped_chest");
        this.registerBlock(Blocks.ENDER_CHEST, "ender_chest");
        this.registerItem(Items.IRON_SHOVEL, "iron_shovel");
        this.registerItem(Items.IRON_PICKAXE, "iron_pickaxe");
        this.registerItem(Items.IRON_AXE, "iron_axe");
        this.registerItem(Items.FLINT_AND_STEEL, "flint_and_steel");
        this.registerItem(Items.APPLE, "apple");
        this.registerItem(Items.BOW, "bow");
        this.registerItem(Items.ARROW, "arrow");
        this.registerItem(Items.SPECTRAL_ARROW, "spectral_arrow");
        this.registerItem(Items.TIPPED_ARROW, "tipped_arrow");
        this.registerItem(Items.COAL, 0, "coal");
        this.registerItem(Items.COAL, 1, "charcoal");
        this.registerItem(Items.DIAMOND, "diamond");
        this.registerItem(Items.IRON_INGOT, "iron_ingot");
        this.registerItem(Items.GOLD_INGOT, "gold_ingot");
        this.registerItem(Items.IRON_SWORD, "iron_sword");
        this.registerItem(Items.WOODEN_SWORD, "wooden_sword");
        this.registerItem(Items.WOODEN_SHOVEL, "wooden_shovel");
        this.registerItem(Items.WOODEN_PICKAXE, "wooden_pickaxe");
        this.registerItem(Items.WOODEN_AXE, "wooden_axe");
        this.registerItem(Items.STONE_SWORD, "stone_sword");
        this.registerItem(Items.STONE_SHOVEL, "stone_shovel");
        this.registerItem(Items.STONE_PICKAXE, "stone_pickaxe");
        this.registerItem(Items.STONE_AXE, "stone_axe");
        this.registerItem(Items.DIAMOND_SWORD, "diamond_sword");
        this.registerItem(Items.DIAMOND_SHOVEL, "diamond_shovel");
        this.registerItem(Items.DIAMOND_PICKAXE, "diamond_pickaxe");
        this.registerItem(Items.DIAMOND_AXE, "diamond_axe");
        this.registerItem(Items.STICK, "stick");
        this.registerItem(Items.BOWL, "bowl");
        this.registerItem(Items.MUSHROOM_STEW, "mushroom_stew");
        this.registerItem(Items.GOLDEN_SWORD, "golden_sword");
        this.registerItem(Items.GOLDEN_SHOVEL, "golden_shovel");
        this.registerItem(Items.GOLDEN_PICKAXE, "golden_pickaxe");
        this.registerItem(Items.GOLDEN_AXE, "golden_axe");
        this.registerItem(Items.STRING, "string");
        this.registerItem(Items.FEATHER, "feather");
        this.registerItem(Items.GUNPOWDER, "gunpowder");
        this.registerItem(Items.WOODEN_HOE, "wooden_hoe");
        this.registerItem(Items.STONE_HOE, "stone_hoe");
        this.registerItem(Items.IRON_HOE, "iron_hoe");
        this.registerItem(Items.DIAMOND_HOE, "diamond_hoe");
        this.registerItem(Items.GOLDEN_HOE, "golden_hoe");
        this.registerItem(Items.WHEAT_SEEDS, "wheat_seeds");
        this.registerItem(Items.WHEAT, "wheat");
        this.registerItem(Items.BREAD, "bread");
        this.registerItem(Items.LEATHER_HELMET, "leather_helmet");
        this.registerItem(Items.LEATHER_CHESTPLATE, "leather_chestplate");
        this.registerItem(Items.LEATHER_LEGGINGS, "leather_leggings");
        this.registerItem(Items.LEATHER_BOOTS, "leather_boots");
        this.registerItem(Items.CHAINMAIL_HELMET, "chainmail_helmet");
        this.registerItem(Items.CHAINMAIL_CHESTPLATE, "chainmail_chestplate");
        this.registerItem(Items.CHAINMAIL_LEGGINGS, "chainmail_leggings");
        this.registerItem(Items.CHAINMAIL_BOOTS, "chainmail_boots");
        this.registerItem(Items.IRON_HELMET, "iron_helmet");
        this.registerItem(Items.IRON_CHESTPLATE, "iron_chestplate");
        this.registerItem(Items.IRON_LEGGINGS, "iron_leggings");
        this.registerItem(Items.IRON_BOOTS, "iron_boots");
        this.registerItem(Items.DIAMOND_HELMET, "diamond_helmet");
        this.registerItem(Items.DIAMOND_CHESTPLATE, "diamond_chestplate");
        this.registerItem(Items.DIAMOND_LEGGINGS, "diamond_leggings");
        this.registerItem(Items.DIAMOND_BOOTS, "diamond_boots");
        this.registerItem(Items.GOLDEN_HELMET, "golden_helmet");
        this.registerItem(Items.GOLDEN_CHESTPLATE, "golden_chestplate");
        this.registerItem(Items.GOLDEN_LEGGINGS, "golden_leggings");
        this.registerItem(Items.GOLDEN_BOOTS, "golden_boots");
        this.registerItem(Items.FLINT, "flint");
        this.registerItem(Items.PORKCHOP, "porkchop");
        this.registerItem(Items.COOKED_PORKCHOP, "cooked_porkchop");
        this.registerItem(Items.PAINTING, "painting");
        this.registerItem(Items.GOLDEN_APPLE, "golden_apple");
        this.registerItem(Items.GOLDEN_APPLE, 1, "golden_apple");
        this.registerItem(Items.SIGN, "sign");
        this.registerItem(Items.OAK_DOOR, "oak_door");
        this.registerItem(Items.SPRUCE_DOOR, "spruce_door");
        this.registerItem(Items.BIRCH_DOOR, "birch_door");
        this.registerItem(Items.JUNGLE_DOOR, "jungle_door");
        this.registerItem(Items.ACACIA_DOOR, "acacia_door");
        this.registerItem(Items.DARK_OAK_DOOR, "dark_oak_door");
        this.registerItem(Items.BUCKET, "bucket");
        this.registerItem(Items.WATER_BUCKET, "water_bucket");
        this.registerItem(Items.LAVA_BUCKET, "lava_bucket");
        this.registerItem(Items.MINECART, "minecart");
        this.registerItem(Items.SADDLE, "saddle");
        this.registerItem(Items.IRON_DOOR, "iron_door");
        this.registerItem(Items.REDSTONE, "redstone");
        this.registerItem(Items.SNOWBALL, "snowball");
        this.registerItem(Items.BOAT, "oak_boat");
        this.registerItem(Items.SPRUCE_BOAT, "spruce_boat");
        this.registerItem(Items.BIRCH_BOAT, "birch_boat");
        this.registerItem(Items.JUNGLE_BOAT, "jungle_boat");
        this.registerItem(Items.ACACIA_BOAT, "acacia_boat");
        this.registerItem(Items.DARK_OAK_BOAT, "dark_oak_boat");
        this.registerItem(Items.LEATHER, "leather");
        this.registerItem(Items.MILK_BUCKET, "milk_bucket");
        this.registerItem(Items.BRICK, "brick");
        this.registerItem(Items.CLAY_BALL, "clay_ball");
        this.registerItem(Items.REEDS, "reeds");
        this.registerItem(Items.PAPER, "paper");
        this.registerItem(Items.BOOK, "book");
        this.registerItem(Items.SLIME_BALL, "slime_ball");
        this.registerItem(Items.CHEST_MINECART, "chest_minecart");
        this.registerItem(Items.FURNACE_MINECART, "furnace_minecart");
        this.registerItem(Items.EGG, "egg");
        this.registerItem(Items.COMPASS, "compass");
        this.registerItem(Items.FISHING_ROD, "fishing_rod");
        this.registerItem(Items.CLOCK, "clock");
        this.registerItem(Items.GLOWSTONE_DUST, "glowstone_dust");
        this.registerItem(Items.FISH, ItemFishFood.FishType.COD.getMetadata(), "cod");
        this.registerItem(Items.FISH, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
        this.registerItem(Items.FISH, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
        this.registerItem(Items.FISH, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
        this.registerItem(Items.COOKED_FISH, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
        this.registerItem(Items.COOKED_FISH, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
        this.registerItem(Items.DYE, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
        this.registerItem(Items.DYE, EnumDyeColor.RED.getDyeDamage(), "dye_red");
        this.registerItem(Items.DYE, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
        this.registerItem(Items.DYE, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
        this.registerItem(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
        this.registerItem(Items.DYE, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
        this.registerItem(Items.DYE, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
        this.registerItem(Items.DYE, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
        this.registerItem(Items.DYE, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
        this.registerItem(Items.DYE, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
        this.registerItem(Items.DYE, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
        this.registerItem(Items.DYE, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
        this.registerItem(Items.DYE, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
        this.registerItem(Items.DYE, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
        this.registerItem(Items.DYE, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
        this.registerItem(Items.DYE, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
        this.registerItem(Items.BONE, "bone");
        this.registerItem(Items.SUGAR, "sugar");
        this.registerItem(Items.CAKE, "cake");
        this.registerItem(Items.REPEATER, "repeater");
        this.registerItem(Items.COOKIE, "cookie");
        this.registerItem(Items.SHEARS, "shears");
        this.registerItem(Items.MELON, "melon");
        this.registerItem(Items.PUMPKIN_SEEDS, "pumpkin_seeds");
        this.registerItem(Items.MELON_SEEDS, "melon_seeds");
        this.registerItem(Items.BEEF, "beef");
        this.registerItem(Items.COOKED_BEEF, "cooked_beef");
        this.registerItem(Items.CHICKEN, "chicken");
        this.registerItem(Items.COOKED_CHICKEN, "cooked_chicken");
        this.registerItem(Items.RABBIT, "rabbit");
        this.registerItem(Items.COOKED_RABBIT, "cooked_rabbit");
        this.registerItem(Items.MUTTON, "mutton");
        this.registerItem(Items.COOKED_MUTTON, "cooked_mutton");
        this.registerItem(Items.RABBIT_FOOT, "rabbit_foot");
        this.registerItem(Items.RABBIT_HIDE, "rabbit_hide");
        this.registerItem(Items.RABBIT_STEW, "rabbit_stew");
        this.registerItem(Items.ROTTEN_FLESH, "rotten_flesh");
        this.registerItem(Items.ENDER_PEARL, "ender_pearl");
        this.registerItem(Items.BLAZE_ROD, "blaze_rod");
        this.registerItem(Items.GHAST_TEAR, "ghast_tear");
        this.registerItem(Items.GOLD_NUGGET, "gold_nugget");
        this.registerItem(Items.NETHER_WART, "nether_wart");
        this.registerItem(Items.BEETROOT, "beetroot");
        this.registerItem(Items.BEETROOT_SEEDS, "beetroot_seeds");
        this.registerItem(Items.BEETROOT_SOUP, "beetroot_soup");
        this.registerItem(Items.TOTEM_OF_UNDYING, "totem");
        this.registerItem(Items.POTIONITEM, "bottle_drinkable");
        this.registerItem(Items.SPLASH_POTION, "bottle_splash");
        this.registerItem(Items.LINGERING_POTION, "bottle_lingering");
        this.registerItem(Items.GLASS_BOTTLE, "glass_bottle");
        this.registerItem(Items.DRAGON_BREATH, "dragon_breath");
        this.registerItem(Items.SPIDER_EYE, "spider_eye");
        this.registerItem(Items.FERMENTED_SPIDER_EYE, "fermented_spider_eye");
        this.registerItem(Items.BLAZE_POWDER, "blaze_powder");
        this.registerItem(Items.MAGMA_CREAM, "magma_cream");
        this.registerItem(Items.BREWING_STAND, "brewing_stand");
        this.registerItem(Items.CAULDRON, "cauldron");
        this.registerItem(Items.ENDER_EYE, "ender_eye");
        this.registerItem(Items.SPECKLED_MELON, "speckled_melon");
        this.itemModelMesher.register(Items.SPAWN_EGG, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(final ItemStack stack) {
                return new ModelResourceLocation("spawn_egg", "inventory");
            }
        });
        this.registerItem(Items.EXPERIENCE_BOTTLE, "experience_bottle");
        this.registerItem(Items.FIRE_CHARGE, "fire_charge");
        this.registerItem(Items.WRITABLE_BOOK, "writable_book");
        this.registerItem(Items.EMERALD, "emerald");
        this.registerItem(Items.ITEM_FRAME, "item_frame");
        this.registerItem(Items.FLOWER_POT, "flower_pot");
        this.registerItem(Items.CARROT, "carrot");
        this.registerItem(Items.POTATO, "potato");
        this.registerItem(Items.BAKED_POTATO, "baked_potato");
        this.registerItem(Items.POISONOUS_POTATO, "poisonous_potato");
        this.registerItem(Items.MAP, "map");
        this.registerItem(Items.GOLDEN_CARROT, "golden_carrot");
        this.registerItem(Items.SKULL, 0, "skull_skeleton");
        this.registerItem(Items.SKULL, 1, "skull_wither");
        this.registerItem(Items.SKULL, 2, "skull_zombie");
        this.registerItem(Items.SKULL, 3, "skull_char");
        this.registerItem(Items.SKULL, 4, "skull_creeper");
        this.registerItem(Items.SKULL, 5, "skull_dragon");
        this.registerItem(Items.CARROT_ON_A_STICK, "carrot_on_a_stick");
        this.registerItem(Items.NETHER_STAR, "nether_star");
        this.registerItem(Items.END_CRYSTAL, "end_crystal");
        this.registerItem(Items.PUMPKIN_PIE, "pumpkin_pie");
        this.registerItem(Items.FIREWORK_CHARGE, "firework_charge");
        this.registerItem(Items.COMPARATOR, "comparator");
        this.registerItem(Items.NETHERBRICK, "netherbrick");
        this.registerItem(Items.QUARTZ, "quartz");
        this.registerItem(Items.TNT_MINECART, "tnt_minecart");
        this.registerItem(Items.HOPPER_MINECART, "hopper_minecart");
        this.registerItem(Items.ARMOR_STAND, "armor_stand");
        this.registerItem(Items.IRON_HORSE_ARMOR, "iron_horse_armor");
        this.registerItem(Items.GOLDEN_HORSE_ARMOR, "golden_horse_armor");
        this.registerItem(Items.DIAMOND_HORSE_ARMOR, "diamond_horse_armor");
        this.registerItem(Items.LEAD, "lead");
        this.registerItem(Items.NAME_TAG, "name_tag");
        this.itemModelMesher.register(Items.BANNER, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(final ItemStack stack) {
                return new ModelResourceLocation("banner", "inventory");
            }
        });
        this.itemModelMesher.register(Items.BED, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(final ItemStack stack) {
                return new ModelResourceLocation("bed", "inventory");
            }
        });
        this.itemModelMesher.register(Items.SHIELD, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(final ItemStack stack) {
                return new ModelResourceLocation("shield", "inventory");
            }
        });
        this.registerItem(Items.ELYTRA, "elytra");
        this.registerItem(Items.CHORUS_FRUIT, "chorus_fruit");
        this.registerItem(Items.CHORUS_FRUIT_POPPED, "chorus_fruit_popped");
        this.registerItem(Items.SHULKER_SHELL, "shulker_shell");
        this.registerItem(Items.IRON_NUGGET, "iron_nugget");
        this.registerItem(Items.RECORD_13, "record_13");
        this.registerItem(Items.RECORD_CAT, "record_cat");
        this.registerItem(Items.RECORD_BLOCKS, "record_blocks");
        this.registerItem(Items.RECORD_CHIRP, "record_chirp");
        this.registerItem(Items.RECORD_FAR, "record_far");
        this.registerItem(Items.RECORD_MALL, "record_mall");
        this.registerItem(Items.RECORD_MELLOHI, "record_mellohi");
        this.registerItem(Items.RECORD_STAL, "record_stal");
        this.registerItem(Items.RECORD_STRAD, "record_strad");
        this.registerItem(Items.RECORD_WARD, "record_ward");
        this.registerItem(Items.RECORD_11, "record_11");
        this.registerItem(Items.RECORD_WAIT, "record_wait");
        this.registerItem(Items.PRISMARINE_SHARD, "prismarine_shard");
        this.registerItem(Items.PRISMARINE_CRYSTALS, "prismarine_crystals");
        this.registerItem(Items.KNOWLEDGE_BOOK, "knowledge_book");
        this.itemModelMesher.register(Items.ENCHANTED_BOOK, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(final ItemStack stack) {
                return new ModelResourceLocation("enchanted_book", "inventory");
            }
        });
        this.itemModelMesher.register(Items.FILLED_MAP, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(final ItemStack stack) {
                return new ModelResourceLocation("filled_map", "inventory");
            }
        });
        this.registerBlock(Blocks.COMMAND_BLOCK, "command_block");
        this.registerItem(Items.FIREWORKS, "fireworks");
        this.registerItem(Items.COMMAND_BLOCK_MINECART, "command_block_minecart");
        this.registerBlock(Blocks.BARRIER, "barrier");
        this.registerBlock(Blocks.MOB_SPAWNER, "mob_spawner");
        this.registerItem(Items.WRITTEN_BOOK, "written_book");
        this.registerBlock(Blocks.BROWN_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
        this.registerBlock(Blocks.RED_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
        this.registerBlock(Blocks.DRAGON_EGG, "dragon_egg");
        this.registerBlock(Blocks.REPEATING_COMMAND_BLOCK, "repeating_command_block");
        this.registerBlock(Blocks.CHAIN_COMMAND_BLOCK, "chain_command_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.SAVE.getModeId(), "structure_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.LOAD.getModeId(), "structure_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.CORNER.getModeId(), "structure_block");
        this.registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.DATA.getModeId(), "structure_block");
        if (Reflector.ModelLoader_onRegisterItems.exists()) {
            Reflector.call(Reflector.ModelLoader_onRegisterItems, this.itemModelMesher);
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.itemModelMesher.rebuildCache();
    }
    
    static {
        RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    }
}
