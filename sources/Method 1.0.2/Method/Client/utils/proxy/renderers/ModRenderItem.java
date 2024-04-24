package Method.Client.utils.proxy.renderers;

import Method.Client.Main;
import Method.Client.module.render.ArmorRender;
import Method.Client.utils.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static Method.Client.utils.proxy.Overrides.EntityrenderOverride.getColorForEnchantment;

@SideOnly(Side.CLIENT)
public class ModRenderItem extends RenderItem {
    private static final ResourceLocation RES_ITEM_GLINT_RUNE = new ResourceLocation(Main.MODID, "textures/misc/enchanted_item_glint_rune.png");
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(Main.MODID, "textures/misc/enchanted_item_glint.png");
    private final RenderItem originalRenderItem;

    public ModRenderItem(RenderItem parRenderItem, ModelManager modelManager) {
        super(Minecraft.getMinecraft().getTextureManager(), modelManager, Minecraft.getMinecraft().getItemColors());
        originalRenderItem = parRenderItem;
    }


    @Override
    public void renderItem(ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty()) {
            if (model.isBuiltInRenderer() || !ClientProxy.Gl.isToggled()) {
                originalRenderItem.renderItem(stack, model);
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);

                renderModel(model, stack);

                if (stack.hasEffect()) {
                    renderEffect(model, getColorForEnchantment(EnchantmentHelper.getEnchantments(stack)));
                }

                GlStateManager.popMatrix();
            }

        }
    }

    private void renderEffect(IBakedModel model, int color) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        if (ArmorRender.useRuneTexture.getValBoolean()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT_RUNE);
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
        }
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(16.0F, 16.0F, 16.0F);
        float f = Minecraft.getSystemTime() % 3000L / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        renderModel(model, color); // original was -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(16.0F, 16.0F, 16.0F);
        float f1 = Minecraft.getSystemTime() % 4873L / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        renderModel(model, color); // original was -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

    private void renderModel(IBakedModel model, ItemStack stack) {
        renderModel(model, -1, stack);
    }

    private void renderModel(IBakedModel model, int color) {
        renderModel(model, color, ItemStack.EMPTY);
    }

    private void renderModel(IBakedModel model, int color, ItemStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values()) {
            renderQuads(bufferbuilder, model.getQuads(null, enumfacing, 0L), color, stack);
        }

        renderQuads(bufferbuilder, model.getQuads(null, null, 0L), color, stack);
        tessellator.draw();
    }

    @Override
    public void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        boolean flag = color == -1 && !stack.isEmpty();
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex()) {
                k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }

    public static ResourceLocation getResItemGlint() {
        return RES_ITEM_GLINT;
    }
}
