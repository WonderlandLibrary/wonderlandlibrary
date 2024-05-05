package me.earth.earthhack.impl.modules.client.pingbypass.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * A button with a writable book on it.
 */
public class GuiButtonPingBypassOptions extends GuiButton
{
    private static final ItemStack STACK = new ItemStack(Items.WRITABLE_BOOK);

    public GuiButtonPingBypassOptions(int buttonID, int xPos, int yPos)
    {
        super(buttonID, xPos, yPos, 20, 20, "");
    }

    /**
     * Draws this button to the screen.
     */
    @SuppressWarnings("NullableProblems")
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            super.drawButton(mc, mouseX, mouseY, partialTicks);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getRenderItem().renderItemAndEffectIntoGUI(STACK, x + 2, y + 2);
        }
    }

}
