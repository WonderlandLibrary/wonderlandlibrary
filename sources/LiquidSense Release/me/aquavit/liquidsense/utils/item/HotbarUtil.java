package me.aquavit.liquidsense.utils.item;

import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraftforge.client.ForgeHooksClient;

import java.awt.*;
import java.util.concurrent.Callable;

public class HotbarUtil extends MinecraftInstance {
    public Translate translate = new Translate(0f , 0f);
    public float size = 1.0f;

    public void renderXHotbarItem(int index, float xPos, float yPos, float partialTicks){
        ItemStack itemStack = mc.thePlayer.inventory.mainInventory[index];
        if (itemStack != null) {
            float animantion = (float)itemStack.animationsToGo - partialTicks;
            if (animantion > 0.0F) {
                GlStateManager.pushMatrix();
                float scale = 1.0F + animantion / 5.0F;
                GlStateManager.translate(xPos + 8F, yPos + 12F, 0.0F);
                GlStateManager.scale(1.0F / scale, (scale + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate(-xPos - 8F, -yPos - 12F, 0.0F);
            }

            renderItemAndEffectIntoGUI(itemStack, xPos, yPos);
            if (animantion > 0.0F) {
                GlStateManager.popMatrix();
            }


            RenderUtils.drawTexturedRect(xPos - 7, yPos -7,30,30,"hotbar", new ScaledResolution(mc));
        } else {
            Fonts.font22.drawStringWithShadow(String.valueOf(index),
                    xPos + Fonts.font22.getStringWidth(String.valueOf(index)) / 2f,
                    yPos + Fonts.font22.getHeight() / 2f, Color.WHITE.getRGB());
        }
    }

	public void renderYHotbarItem(int index, float xPos, float yPos, float partialTicks){
		ItemStack itemStack = mc.thePlayer.inventory.mainInventory[index];
		if (itemStack != null) {
			float animantion = (float)itemStack.animationsToGo - partialTicks;
			if (animantion > 0.0F) {
				GlStateManager.pushMatrix();
				float scale = 1.0F + animantion / 5.0F;
				GlStateManager.translate(xPos + 8F, yPos + 12F, 0.0F);
				GlStateManager.scale(1.0F / scale, (scale + 1.0F) / 2.0F, 1.0F);
				GlStateManager.translate(-xPos - 8F, -yPos - 12F, 0.0F);
			}
			renderItemAndEffectIntoGUI(itemStack, xPos, yPos);
			if (animantion > 0.0F) {
				GlStateManager.popMatrix();
			}
		}
	}

    private void renderItemAndEffectIntoGUI(final ItemStack itemStack, float xPos, float yPos) {
        if (itemStack != null && itemStack.getItem() != null) {
            mc.getRenderItem().zLevel += 50.0F;

            try {
                renderItemIntoGUI(itemStack, xPos, yPos);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addCrashSectionCallable("Item Type", new Callable<String>() {
                    public String call() throws Exception {
                        return String.valueOf(itemStack.getItem());
                    }
                });
                crashreportcategory.addCrashSectionCallable("Item Aux", new Callable<String>() {
                    public String call() throws Exception {
                        return String.valueOf(itemStack.getMetadata());
                    }
                });
                crashreportcategory.addCrashSectionCallable("Item NBT", new Callable<String>() {
                    public String call() throws Exception {
                        return String.valueOf(itemStack.getTagCompound());
                    }
                });
                crashreportcategory.addCrashSectionCallable("Item Foil", new Callable<String>() {
                    public String call() throws Exception {
                        return String.valueOf(itemStack.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }

            mc.getRenderItem().zLevel -= 50.0F;
        }

    }

    private void renderItemIntoGUI(ItemStack itemStack, float xPos, float yPos) {
        IBakedModel ibakedmodel = mc.getRenderItem().getItemModelMesher().getItemModel(itemStack);
        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        setupGuiTransform(xPos, yPos, ibakedmodel.isGui3d());
        ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GUI);
        mc.getRenderItem().renderItem(itemStack, ibakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(float xPos, float yPos, boolean ibakedmodel) {
        GlStateManager.translate(xPos, yPos, 100.0F + mc.getRenderItem().zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, -1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        if (ibakedmodel) {
            GlStateManager.scale(40.0F, 40.0F, 40.0F);
            GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableLighting();
        } else {
            GlStateManager.scale(64.0F, 64.0F, 64.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
        }

    }
}
