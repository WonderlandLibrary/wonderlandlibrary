package intent.AquaDev.aqua.modules.visual;

import events.Event;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.fr.lavache.anime.Easing;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.utils.ColorUtils;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CustomHotbar
extends Module {
    Animate anim = new Animate();
    private final RenderItem itemRenderer = mc.getRenderItem();

    public CustomHotbar() {
        super("CustomHotbar", "CustomHotbar", 0, Category.Visual);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventRender2D) {
            this.drawShaders();
        }
        if (e instanceof EventPostRender2D) {
            this.drawRects();
        }
    }

    private void drawShaders() {
        ScaledResolution sr = new ScaledResolution(mc);
        int i = sr.getScaledWidth() / 2;
        this.anim.setEase(Easing.LINEAR).setMin(11.0f).setMax(40.0f).setSpeed(15.0f).setReversed(!GuiNewChat.animatedChatOpen).update();
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(i - 91), (double)((float)sr.getScaledHeight() - this.anim.getValue()), (double)182.0, (double)22.0, (double)3.0, (int)new Color(0, 0, 0, 1).getRGB()), (boolean)false);
    }

    private void drawRects() {
        ScaledResolution sr = new ScaledResolution(mc);
        int i = sr.getScaledWidth() / 2;
        this.anim.setEase(Easing.LINEAR).setMin(11.0f).setMax(40.0f).setSpeed(GuiNewChat.animatedChatOpen ? 15.0f : 40.0f).setReversed(!GuiNewChat.animatedChatOpen).update();
        RenderUtil.drawRoundedRect2Alpha((double)(i - 91), (double)((float)sr.getScaledHeight() - this.anim.getValue()), (double)182.0, (double)22.0, (double)3.0, (Color)new Color(0, 0, 0, 50));
        RenderUtil.drawRoundedRect2Alpha((double)((float)sr.getScaledWidth() / 2.0f - 91.0f + (float)(CustomHotbar.mc.thePlayer.inventory.currentItem * 20)), (double)((float)sr.getScaledHeight() - this.anim.getValue()), (double)22.0, (double)22.0, (double)3.0, (Color)ColorUtils.getColorAlpha((Color)Arraylist.getGradientOffset((Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (Color)new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor()), (double)15.0), (int)100));
        for (int j = 0; j < 9; ++j) {
            int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
            int l = (int)((float)sr.getScaledHeight() - this.anim.getValue() + 2.0f);
            this.renderHotbarItem(j, k, l, CustomHotbar.mc.timer.renderPartialTicks, (EntityPlayer)CustomHotbar.mc.thePlayer);
        }
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player) {
        GlStateManager.resetColor();
        ItemStack itemstack = player.inventory.mainInventory[index];
        if (itemstack != null) {
            float f = (float)itemstack.animationsToGo - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                float f1 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), (float)0.0f);
                GlStateManager.scale((float)(1.0f / f1), (float)((f1 + 1.0f) / 2.0f), (float)1.0f);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), (float)0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(CustomHotbar.mc.fontRendererObj, itemstack, xPos, yPos);
            GlStateManager.resetColor();
        }
    }
}
