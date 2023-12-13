package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.value.Value;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ArmorHudElement extends Element {

    /**
     * Constructor for the Elements
     */
    public ArmorHudElement() {
        super("Armor HUD", 3, 3, 64, 64);
    }

    private final ModeValue directionValue = new ModeValue("Direction", "Direction of the armor hud", "Horizontal", "Horizontal", "Vertical");
    private final BooleanValue renderHeld = new BooleanValue("Render Held", "Render the held item",  false);
    private final BooleanValue renderDurability = new BooleanValue("Render Durability", "Render the durability",  true);

    @Override
    public List<Value<?>> getValues() {
        return createValuesArray(directionValue, renderHeld, renderDurability);
    }

    @Override
    public void render() {
        fitInScreen(mc.displayWidth, mc.displayHeight);
        int x = 0, y = 0;
        double scale = 1.75;
        GlStateManager.translate(getX(), getY(), 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        setWidth(32);
        setHeight(32);
        for (int i = 3; i >= -1; i--) {
            final ItemStack stack;
            if (i == -1 && renderHeld.getObject()) stack = mc.thePlayer.getHeldItem();
            else if (i >= 0 && i <= 3) stack = mc.thePlayer.getCurrentArmor(i);
            else stack = null;
            if (stack == null) continue;
            final int lastDamage = stack.getItemDamage();
            if (!renderDurability.getObject()) stack.setItemDamage(0);
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (x / scale), (int) (y / scale));
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (x / scale), (int) (y / scale), null);
            stack.setItemDamage(lastDamage);
            if (directionValue.getObject().equals("Horizontal")) x += 15 * scale;
            else y += 15 * scale;
            setWidth(Math.max(getWidth(), x));
            setHeight(Math.max(getHeight(), y));
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.scale(1 / scale, 1 / scale, 1);
        GlStateManager.translate(-getX(), -getY(), 0);
    }

    @Override
    public Element copy() {
        return new ArmorHudElement();
    }
}