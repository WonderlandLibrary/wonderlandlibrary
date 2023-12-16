// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud.bedwars;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.SCModule;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import moonsense.features.SCAbstractRenderModule;

public class BedwarsUtilitiesOverlayChild extends SCAbstractRenderModule
{
    public static BedwarsUtilitiesOverlayChild INSTANCE;
    private final ItemStack[] items;
    
    public BedwarsUtilitiesOverlayChild() {
        super("Bedwars Overlay - Utilities", "");
        this.items = new ItemStack[] { new ItemStack(Items.golden_apple), new ItemStack(Items.snowball), new ItemStack(Items.spawn_egg), new ItemStack(Items.fire_charge), new ItemStack(Blocks.tnt), new ItemStack(Items.ender_pearl), new ItemStack(Items.egg), new ItemStack(Items.water_bucket) };
        this.setParentModule(BedwarsOverlayModule.INSTANCE);
        BedwarsUtilitiesOverlayChild.INSTANCE = this;
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
        return 128;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (BedwarsOverlayModule.INSTANCE.utilitiesBackground.getBoolean()) {
            GuiUtils.drawRoundedRect(x, y, x + this.getWidth(), y + this.getHeight(), BedwarsOverlayModule.INSTANCE.utilitiesBackgroundRadius.getFloat(), BedwarsOverlayModule.INSTANCE.utilitiesBackgroundColor.getColor());
        }
        if (BedwarsOverlayModule.INSTANCE.utilitiesBorder.getBoolean()) {
            GuiUtils.drawRoundedOutline(x, y, x + this.getWidth(), y + this.getHeight(), BedwarsOverlayModule.INSTANCE.utilitiesBackgroundRadius.getFloat(), BedwarsOverlayModule.INSTANCE.utilitiesBorderThickness.getFloat(), BedwarsOverlayModule.INSTANCE.utilitiesBorderColor.getColor());
        }
        for (int i = 0; i < this.items.length; ++i) {
            BedwarsOverlayModule.INSTANCE.renderItemStack(x + 1.0f, y, i, this.items[i]);
            GuiUtils.drawString("x" + BedwarsOverlayModule.INSTANCE.getItemCount(this.items[i]), x + 19.0f, y + i * 16 + 5.0f, BedwarsOverlayModule.INSTANCE.utilitiesTextColor.getColor(), BedwarsOverlayModule.INSTANCE.utilitiesTextShadow.getBoolean());
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
