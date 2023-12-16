// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud.bedwars;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.SCModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import moonsense.features.SCAbstractRenderModule;

public class BedwarsResourcesOverlayChild extends SCAbstractRenderModule
{
    public static BedwarsResourcesOverlayChild INSTANCE;
    private final ItemStack[] items;
    
    public BedwarsResourcesOverlayChild() {
        super("Bedwars Overlay - Resources", "");
        this.items = new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond), new ItemStack(Items.emerald) };
        this.setParentModule(BedwarsOverlayModule.INSTANCE);
        BedwarsResourcesOverlayChild.INSTANCE = this;
    }
    
    @Override
    public int getWidth() {
        int startingWidth = 40;
        ItemStack[] items;
        for (int length = (items = this.items).length, i = 0; i < length; ++i) {
            final ItemStack is = items[i];
            final int width = 21 + this.mc.fontRendererObj.getStringWidth("x" + BedwarsOverlayModule.INSTANCE.getItemCount(is));
            if (width > startingWidth) {
                startingWidth = width;
            }
        }
        return startingWidth;
    }
    
    @Override
    public int getHeight() {
        return 64;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (BedwarsOverlayModule.INSTANCE.resourcesBackground.getBoolean()) {
            GuiUtils.drawRoundedRect(x, y, x + this.getWidth(), y + this.getHeight(), BedwarsOverlayModule.INSTANCE.resourcesBackgroundRadius.getFloat(), BedwarsOverlayModule.INSTANCE.resourcesBackgroundColor.getColor());
        }
        if (BedwarsOverlayModule.INSTANCE.resourcesBorder.getBoolean()) {
            GuiUtils.drawRoundedOutline(x, y, x + this.getWidth(), y + this.getHeight(), BedwarsOverlayModule.INSTANCE.resourcesBackgroundRadius.getFloat(), BedwarsOverlayModule.INSTANCE.resourcesBorderThickness.getFloat(), BedwarsOverlayModule.INSTANCE.resourcesBorderColor.getColor());
        }
        for (int i = 0; i < this.items.length; ++i) {
            BedwarsOverlayModule.INSTANCE.renderItemStack(x + 1.0f, y, i, this.items[i]);
            GuiUtils.drawString("x" + BedwarsOverlayModule.INSTANCE.getItemCount(this.items[i]), x + 19.0f, y + i * 16 + 5.0f, BedwarsOverlayModule.INSTANCE.resourcesTextColor.getColor(), BedwarsOverlayModule.INSTANCE.resourcesTextShadow.getBoolean());
        }
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.CENTER_RIGHT;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.SERVER;
    }
}
