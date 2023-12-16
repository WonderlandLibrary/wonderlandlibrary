// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class UHCOverlayModule extends SCAbstractRenderModule
{
    public static UHCOverlayModule INSTANCE;
    public final Setting itemSize;
    
    public UHCOverlayModule() {
        super("UHC Overlay", "Adds an item counter for important UHC items and resizes them for easier visibility.");
        UHCOverlayModule.INSTANCE = this;
        this.itemSize = new Setting(this, "Item Size").setDefault(1.0f).setRange(1.0f, 3.0f, 0.01f);
    }
    
    @Override
    public int getWidth() {
        return 16;
    }
    
    @Override
    public int getHeight() {
        return 96;
    }
    
    @Override
    public void render(final float x, final float y) {
        this.renderItemStack(x, y, 3, new ItemStack(Items.golden_apple), this.getItemCount(new ItemStack(Items.golden_apple)));
        this.renderItemStack(x, y, 2, new ItemStack(Items.golden_carrot), this.getItemCount(new ItemStack(Items.golden_carrot)));
        this.renderItemStack(x, y, 1, new ItemStack(Blocks.gold_block), this.getItemCount(new ItemStack(Blocks.gold_block)));
        this.renderItemStack(x, y, 0, new ItemStack(Blocks.gold_ore), this.getItemCount(new ItemStack(Blocks.gold_ore)));
        this.renderItemStack(x, y, -1, new ItemStack(Items.gold_ingot), this.getItemCount(new ItemStack(Items.gold_ingot)));
        this.renderItemStack(x, y, -2, new ItemStack(Items.gold_nugget), this.getItemCount(new ItemStack(Items.gold_nugget)));
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    private int getItemCount(final ItemStack is) {
        int itemCount = 0;
        ItemStack[] mainInventory;
        for (int length = (mainInventory = this.mc.thePlayer.inventory.mainInventory).length, i = 0; i < length; ++i) {
            final ItemStack stack = mainInventory[i];
            try {
                if (stack.getItem().equals(is.getItem())) {
                    itemCount += stack.stackSize;
                }
            }
            catch (NullPointerException ex) {}
        }
        return itemCount;
    }
    
    private void renderItemStack(final float x, final float y, final int i, final ItemStack is, final int count) {
        if (is == null) {
            return;
        }
        GL11.glPushMatrix();
        final int yAdd = -16 * i + 48;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        this.renderItem(is, (int)x, (int)(y + yAdd), 0.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    
    private void renderItem(final ItemStack is, final int p_175184_2_, final int p_175184_3_, final float p_175184_4_) {
        if (is != null) {
            final float var7 = is.animationsToGo - p_175184_4_;
            if (var7 > 0.0f) {
                GlStateManager.pushMatrix();
                final float var8 = 1.0f + var7 / 5.0f;
                GlStateManager.translate((float)(p_175184_2_ + 8), (float)(p_175184_3_ + 12), 0.0f);
                GlStateManager.scale(1.0f / var8, (var8 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(p_175184_2_ + 8)), (float)(-(p_175184_3_ + 12)), 0.0f);
            }
            this.mc.getRenderItem().func_180450_b(is, p_175184_2_, p_175184_3_);
            if (var7 > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.renderItem2(this.mc.fontRendererObj, is, p_175184_2_, p_175184_3_);
        }
    }
    
    private void renderItem2(final FontRenderer p_175030_1_, final ItemStack p_175030_2_, final int p_175030_3_, final int p_175030_4_) {
        this.renderItem3(p_175030_1_, p_175030_2_, p_175030_3_, p_175030_4_, null);
    }
    
    private void renderItem3(final FontRenderer fr, final ItemStack is, final int x, final int y, final String string) {
        if (is != null) {
            final int stackSize = this.getItemCount(is);
            final String itemDamaged = (string == null) ? String.valueOf(stackSize) : string;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            fr.func_175063_a(itemDamaged, (float)(x + 19 - 2 - fr.getStringWidth(itemDamaged)), (float)(y + 6 + 3), 16777215);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.CENTER_RIGHT;
    }
}
