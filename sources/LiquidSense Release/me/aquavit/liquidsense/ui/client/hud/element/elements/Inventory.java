package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@ElementInfo(name = "Inventory")
public class Inventory extends Element {

    private ListValue mode = new ListValue("Mode",new String[]{"Minecraft","Rect"},"Minecraft");
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    @Override
    public Border drawElement() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (mode.get().equalsIgnoreCase("Minecraft")) mc.getTextureManager().bindTexture(TEXTURE);

        int value1 = (175 - 176) / 2;
        int value2 = (298 - 166) / 2;

        if (mode.get().equalsIgnoreCase("Minecraft")) this.drawTexturedModalRect(0, 0, 0, 0, 176, 3 * 18 + 17);

        if (mode.get().equalsIgnoreCase("Rect")) {
            BlurBuffer.blurArea((int)(getRenderX() * getScale()),(int)((getRenderY() + 17) * getScale()),176*getScale(),3 * 18 *getScale(),true);
            if (!this.getInfo().disableScale())
                GL11.glScalef(this.getScale(), this.getScale(), this.getScale());

            GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);
            RenderUtils.drawRect(0,17,176, 3 * 18 + 17, new Color(0,0,0,160));
            RenderUtils.drawRoundedRect(-0.5F, 0, 176.5F, 7F + Fonts.csgo40.FONT_HEIGHT,1.5F,
                    new Color(16, 25, 32, 200).getRGB(), 1F,new Color(16, 25, 32, 200).getRGB());
            Fonts.csgo40.drawString("A", 4.2F, (float) (Fonts.csgo40.FONT_HEIGHT / 2) + 1.2F, new Color(0, 131, 193).getRGB(), false);
            Fonts.font20.drawString("Inventory", Fonts.csgo40.getStringWidth("A") + 9.2F, (float) (Fonts.csgo40.FONT_HEIGHT / 2) + 0.2F, Color.WHITE.getRGB(), false);
        }

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        List<Slot> inventorySlots = mc.thePlayer.inventoryContainer.inventorySlots;
        for (Slot slot : inventorySlots) {
            if (slot != null && slot.getSlotIndex() > 8 && slot.getStack() != null && slot.getSlotIndex() < 36) {
                mc.getRenderItem().renderItemIntoGUI(slot.getStack(), slot.xDisplayPosition - value1, slot.yDisplayPosition - value2);
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, slot.getStack(), slot.xDisplayPosition - value1, slot.yDisplayPosition - value2);
            }
        }
        RenderHelper.disableStandardItemLighting();
        if (mode.get().equalsIgnoreCase("Rect") && getInventoryCount() == 0)
            Fonts.font20.drawString("Inventory Empty", 88 - (float)(Fonts.font20.getStringWidth("Inventory Empty") / 2),
                48 - Fonts.font20.getHeight(), Color.WHITE.getRGB(),true);
        GlStateManager.popMatrix();
        return new Border(0, 0, 176, 3 * 18 + 17);
    }

    private int getInventoryCount() {
        int amount = 0;

        for(int i = 9; i < 35; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if(itemStack != null)
                amount += itemStack.stackSize;
        }

        return amount;
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
