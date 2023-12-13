package slavikcodd3r.rainbow.module.modules.render;

import java.awt.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.FontUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

@Mod(displayName = "ArmorStatus")
public class ArmorStatus extends Module
{
	public static FontUtils font;
    public Minecraft mc;
    
    public ArmorStatus() {
        this.mc = Minecraft.getMinecraft();
    }
    
    @EventTarget
    private void onRender2D(final Render2DEvent e) {
        if (font == null) {
            font = new FontUtils("Verdana", 18.0f);
        }
        int itemX = e.getWidth() / 2 + 9;
        for (int i = 0; i < 5; ++i) {
            final ItemStack ia = this.mc.thePlayer.getEquipmentInSlot(i);
            if (ia != null) {
                final float oldZ = this.mc.getRenderItem().zLevel;
                GL11.glPushMatrix();
                GlStateManager.clear(256);
                GlStateManager.disableAlpha();
                RenderHelper.enableStandardItemLighting();
                this.mc.getRenderItem().zLevel = -100.0f;
                this.mc.getRenderItem().renderItemIntoGUI(ia, itemX, e.getHeight() - 55);
                this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, ia, itemX, e.getHeight() - 55);
                this.mc.getRenderItem().zLevel = oldZ;
                RenderHelper.disableStandardItemLighting();
                GlStateManager.enableAlpha();
                GL11.glPopMatrix();
                if (ia.getItem() instanceof ItemSword || ia.getItem() instanceof ItemTool || ia.getItem() instanceof ItemArmor || ia.getItem() instanceof ItemBow) {
                    GlStateManager.pushMatrix();
                    final int durability = ia.getMaxDamage() - ia.getItemDamage();
                    final int y = e.getHeight() - 60;
                    GlStateManager.scale(0.5, 0.5, 0.5);
                    GlStateManager.popMatrix();
                }
                itemX += 16;
            }
        }
    }
}
