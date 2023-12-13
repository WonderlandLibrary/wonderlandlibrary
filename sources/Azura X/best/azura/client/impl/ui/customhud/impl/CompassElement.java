package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.api.ui.customhud.Element;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CompassElement extends Element {

    /**
     * Constructor for the Elements
     *
     * Name for the corresponding element
     * Default x position for the element
     * Default y height for the element
     */
    public CompassElement() {
        super("Compass", 3, 16, 32, 32);
    }

    @Override
    public void render() {
        fitInScreen(mc.displayWidth, mc.displayHeight);
        ItemStack itemStack = new ItemStack(Items.compass);
        final double scale = 2.;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.scale(scale, scale, 1.);
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) (getX() / scale), (int) (getY() / scale));
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, (int) (getX() / scale), (int) (getY() / scale));
        GlStateManager.scale(1. / scale, 1. / scale, 1.);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }

    @Override
    public Element copy() {
        return new CompassElement();
    }
}