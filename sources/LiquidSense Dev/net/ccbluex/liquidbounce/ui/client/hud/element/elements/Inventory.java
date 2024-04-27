package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ElementInfo(name = "Inventory")
public class Inventory extends Element {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    @Nullable
    @Override
    public Border drawElement() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);

        int value1 = (175 - 176) / 2;
        int value2 = (298 - 166) / 2;

        this.drawTexturedModalRect(0, 0, 0, 0, 176, 3 * 18 + 17);

        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        List<Slot> inventorySlots = mc.thePlayer.inventoryContainer.inventorySlots;
        for (Slot slot : inventorySlots) {
            if (slot != null && slot.getSlotIndex() > 8 && slot.getStack() != null && slot.getSlotIndex() < 36) {
                mc.getRenderItem().renderItemIntoGUI(slot.getStack(), slot.xDisplayPosition - value1, slot.yDisplayPosition - value2);
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, slot.getStack(), slot.xDisplayPosition - value1, slot.yDisplayPosition - value2);
            }
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        return new Border(0, 0, 176, 3 * 18 + 17);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0).tex((float) (textureX) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0).tex((float) (textureX + width) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0).tex((float) (textureX + width) * f, (float) (textureY) * f1).endVertex();
        worldrenderer.pos(x, y, 0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
        tessellator.draw();
    }
}
