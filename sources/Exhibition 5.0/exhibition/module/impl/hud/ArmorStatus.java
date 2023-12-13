// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.hud;

import exhibition.event.RegisterEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class ArmorStatus extends Module
{
    public ArmorStatus(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventRenderGui.class })
    @Override
    public void onEvent(final Event e) {
        final EventRenderGui event = (EventRenderGui)e;
        final boolean currentItem = true;
        GL11.glPushMatrix();
        final List<ItemStack> stuff = new ArrayList<ItemStack>();
        final boolean onwater = ArmorStatus.mc.thePlayer.isEntityAlive() && ArmorStatus.mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armer = ArmorStatus.mc.thePlayer.inventory.armorInventory[index];
            if (armer != null) {
                stuff.add(armer);
            }
        }
        if (ArmorStatus.mc.thePlayer.getCurrentEquippedItem() != null) {
            stuff.add(ArmorStatus.mc.thePlayer.getCurrentEquippedItem());
        }
        for (final ItemStack errything : stuff) {
            if (ArmorStatus.mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            ArmorStatus.mc.getRenderItem().zLevel = -150.0f;
            ArmorStatus.mc.getRenderItem().func_180450_b(errything, split + event.getResolution().getScaledWidth() / 2 - 4, event.getResolution().getScaledHeight() - (onwater ? 65 : 55));
            ArmorStatus.mc.getRenderItem().renderItemOverlays(ArmorStatus.mc.fontRendererObj, errything, split + event.getResolution().getScaledWidth() / 2 - 4, event.getResolution().getScaledHeight() - (onwater ? 65 : 55));
            ArmorStatus.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
}
