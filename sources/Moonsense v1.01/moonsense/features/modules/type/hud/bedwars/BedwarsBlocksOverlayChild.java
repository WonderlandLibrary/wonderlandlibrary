// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud.bedwars;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.SCModule;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import moonsense.features.SCAbstractRenderModule;

public class BedwarsBlocksOverlayChild extends SCAbstractRenderModule
{
    public static BedwarsBlocksOverlayChild INSTANCE;
    private final ItemStack[] items;
    
    public BedwarsBlocksOverlayChild() {
        super("Bedwars Overlay - Blocks", "");
        this.items = new ItemStack[] { new ItemStack(Blocks.wool), new ItemStack(Blocks.hardened_clay), new ItemStack(Blocks.glass), new ItemStack(Blocks.end_stone), new ItemStack(Blocks.ladder), new ItemStack(Blocks.planks), new ItemStack(Blocks.obsidian) };
        this.setParentModule(BedwarsOverlayModule.INSTANCE);
        BedwarsBlocksOverlayChild.INSTANCE = this;
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
        return 112;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (BedwarsOverlayModule.INSTANCE.blocksBackground.getBoolean()) {
            GuiUtils.drawRoundedRect(x, y, x + this.getWidth(), y + this.getHeight(), BedwarsOverlayModule.INSTANCE.blocksBackgroundRadius.getFloat(), BedwarsOverlayModule.INSTANCE.blocksBackgroundColor.getColor());
        }
        if (BedwarsOverlayModule.INSTANCE.blocksBorder.getBoolean()) {
            GuiUtils.drawRoundedOutline(x, y, x + this.getWidth(), y + this.getHeight(), BedwarsOverlayModule.INSTANCE.blocksBackgroundRadius.getFloat(), BedwarsOverlayModule.INSTANCE.blocksBorderThickness.getFloat(), BedwarsOverlayModule.INSTANCE.blocksBorderColor.getColor());
        }
        for (int i = 0; i < this.items.length; ++i) {
            BedwarsOverlayModule.INSTANCE.renderItemStack(x + 1.0f, y, i, this.items[i]);
            GuiUtils.drawString("x" + BedwarsOverlayModule.INSTANCE.getItemCount(this.items[i]), x + 19.0f, y + i * 16 + 5.0f, BedwarsOverlayModule.INSTANCE.blocksTextColor.getColor(), BedwarsOverlayModule.INSTANCE.blocksTextShadow.getBoolean());
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
