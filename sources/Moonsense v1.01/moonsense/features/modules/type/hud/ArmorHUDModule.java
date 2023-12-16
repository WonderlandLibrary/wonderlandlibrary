// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import optifine.Reflector;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemBow;
import net.minecraft.client.gui.FontRenderer;
import moonsense.utils.ColorObject;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.utils.GuiUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import moonsense.ui.screen.settings.GuiHUDEditor;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class ArmorHUDModule extends SCAbstractRenderModule
{
    private final Setting itemName;
    private final Setting itemCount;
    private final Setting equippedItem;
    private final Setting showArrowCount;
    private final Setting textShadow;
    private final Setting damageOverlay;
    private final Setting showItemDamage;
    private final Setting showArmorDamage;
    private final Setting showMaxDamage;
    private final Setting damageDisplayType;
    private final Setting staticDamageColors;
    private final Setting nameColor;
    private final Setting defaultDamageColor;
    private final Setting highDamageColor;
    private final Setting mediumDamageColor;
    private final Setting mediumLowDamageColor;
    private final Setting lowDamageColor;
    private final Setting lowestDamageColor;
    
    public ArmorHUDModule() {
        super("Armor Status", "Displays equipped armor and the currently held item on the HUD.", 14);
        this.itemName = new Setting(this, "Item Name").setDefault(false);
        this.itemCount = new Setting(this, "Item Count").setDefault(true);
        this.equippedItem = new Setting(this, "Equipped Item").setDefault(true);
        this.showArrowCount = new Setting(this, "Arrow Count").setDefault(true);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(true);
        new Setting(this, "Damage Options");
        this.damageOverlay = new Setting(this, "Damage Overlay").setDefault(true);
        this.showItemDamage = new Setting(this, "Show Item Damage").setDefault(true);
        this.showArmorDamage = new Setting(this, "Show Armor Damage").setDefault(true);
        this.showMaxDamage = new Setting(this, "Show Max Damage").setDefault(false);
        new Setting(this, "Damage Display");
        this.damageDisplayType = new Setting(this, "Damage Display Type").setDefault(0).setRange("Value", "Percent", "None");
        new Setting(this, "Color Options");
        this.staticDamageColors = new Setting(this, "Static Damage Colors").setDefault(false);
        this.nameColor = new Setting(this, "Name Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.defaultDamageColor = new Setting(this, "Default Damage Color").setDefault(new Color(255, 255, 255, 255).getRGB(), 0);
        this.highDamageColor = new Setting(this, "High Damage Color").setDefault(new Color(85, 255, 85, 255).getRGB(), 0);
        this.mediumDamageColor = new Setting(this, "Medium Damage Color").setDefault(new Color(255, 255, 85, 255).getRGB(), 0);
        this.mediumLowDamageColor = new Setting(this, "Medium Low Damage Color").setDefault(new Color(255, 170, 0, 255).getRGB(), 0);
        this.lowDamageColor = new Setting(this, "Low Damage Color").setDefault(new Color(255, 85, 85, 255).getRGB(), 0);
        this.lowestDamageColor = new Setting(this, "Lowest Damage Color").setDefault(new Color(170, 0, 0, 255).getRGB(), 0);
    }
    
    @Override
    public int getWidth() {
        if (this.showArmorDamage.getBoolean() || this.showItemDamage.getBoolean()) {
            if (this.showArmorDamage.getBoolean() && this.showItemDamage.getBoolean()) {
                if (this.itemName.getBoolean()) {
                    return 20 + this.getWidthOfLargestItemName();
                }
                if (!this.showMaxDamage.getBoolean()) {
                    return 40;
                }
                if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Value")) {
                    return 62;
                }
                if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Percent")) {
                    return 46;
                }
            }
            return 56;
        }
        if (this.itemName.getBoolean()) {
            return 20 + this.getWidthOfLargestItemName();
        }
        return 16;
    }
    
    @Override
    public int getHeight() {
        int baseHeight = 64;
        if (this.equippedItem.getBoolean()) {
            baseHeight += 16;
        }
        if (this.itemName.getBoolean()) {
            baseHeight += 20;
        }
        return baseHeight;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (!(this.mc.currentScreen instanceof GuiHUDEditor)) {
            int j = 0;
            for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; ++i) {
                final ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
                this.renderItemStack(x, y, i, itemStack, true);
                ++j;
            }
            final int yAdd = this.getHeight() - 6;
            if (this.equippedItem.getBoolean()) {
                this.renderItemStack(x, y, -1, this.mc.thePlayer.getCurrentEquippedItem(), false);
            }
        }
        else {
            this.renderDummy(x, y);
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.renderItemStack(x, y, 3, new ItemStack(Items.diamond_helmet), true);
        this.renderItemStack(x, y, 2, new ItemStack(Items.diamond_chestplate), true);
        this.renderItemStack(x, y, 1, new ItemStack(Items.diamond_leggings), true);
        this.renderItemStack(x, y, 0, new ItemStack(Items.diamond_boots), true);
        if (this.equippedItem.getBoolean()) {
            this.renderItemStack(x, y, -1, new ItemStack(Items.iron_sword), false);
        }
    }
    
    private void renderItemStack(final float x, final float y, final int i, final ItemStack is, final boolean isArmor) {
        if (is == null) {
            return;
        }
        GL11.glPushMatrix();
        int yAdd;
        if (this.itemName.getBoolean()) {
            yAdd = -20 * i + 60;
        }
        else {
            yAdd = -16 * i + 48;
        }
        if (is.getItem().isDamageable()) {
            ColorObject damageColor;
            if (is.getMaxDamage() - is.getItemDamage() > 0.8 * is.getMaxDamage()) {
                damageColor = this.highDamageColor.getColorObject();
            }
            else if (is.getMaxDamage() - is.getItemDamage() > 0.6 * is.getMaxDamage()) {
                damageColor = this.mediumDamageColor.getColorObject();
            }
            else if (is.getMaxDamage() - is.getItemDamage() > 0.4 * is.getMaxDamage()) {
                damageColor = this.mediumLowDamageColor.getColorObject();
            }
            else if (is.getMaxDamage() - is.getItemDamage() > 0.2 * is.getMaxDamage()) {
                damageColor = this.lowDamageColor.getColorObject();
            }
            else {
                damageColor = this.lowestDamageColor.getColorObject();
            }
            if (this.staticDamageColors.getBoolean()) {
                damageColor = this.defaultDamageColor.getColorObject();
            }
            if (this.itemName.getBoolean()) {
                if (this.showArmorDamage.getBoolean() && isArmor) {
                    GuiUtils.drawString(is.getItem().getItemStackDisplayName(is), x + 20.0f, y + yAdd, SCModule.getColor(this.nameColor.getColorObject()), this.textShadow.getBoolean());
                }
                else if (this.showItemDamage.getBoolean() && !isArmor && is.isItemStackDamageable()) {
                    GuiUtils.drawString(is.getItem().getItemStackDisplayName(is), x + 20.0f, y + yAdd, SCModule.getColor(this.nameColor.getColorObject()), this.textShadow.getBoolean());
                }
                else {
                    GuiUtils.drawString(is.getItem().getItemStackDisplayName(is), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(this.nameColor.getColorObject()), this.textShadow.getBoolean());
                }
            }
            if (this.showArmorDamage.getBoolean() && isArmor) {
                if (this.showMaxDamage.getBoolean()) {
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Value")) {
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(is.getMaxDamage() - is.getItemDamage() + "/" + is.getMaxDamage(), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(is.getMaxDamage() - is.getItemDamage() + "/" + is.getMaxDamage(), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Percent")) {
                        final double damage = (is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0;
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                }
                else {
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Value")) {
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(new StringBuilder().append(is.getMaxDamage() - is.getItemDamage()).toString(), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(new StringBuilder().append(is.getMaxDamage() - is.getItemDamage()).toString(), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Percent")) {
                        final double damage = (is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0;
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                }
            }
            if (this.showItemDamage.getBoolean() && !isArmor && is.isItemStackDamageable()) {
                if (this.showMaxDamage.getBoolean()) {
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Value")) {
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(is.getMaxDamage() - is.getItemDamage() + "/" + is.getMaxDamage(), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(is.getMaxDamage() - is.getItemDamage() + "/" + is.getMaxDamage(), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Percent")) {
                        final double damage = (is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0;
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                }
                else {
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Value")) {
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(new StringBuilder().append(is.getMaxDamage() - is.getItemDamage()).toString(), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(new StringBuilder().append(is.getMaxDamage() - is.getItemDamage()).toString(), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                    if (this.damageDisplayType.getValue().get(this.damageDisplayType.getInt() + 1).equalsIgnoreCase("Percent")) {
                        final double damage = (is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0;
                        if (this.itemName.getBoolean()) {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 8.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                        else {
                            GuiUtils.drawString(String.format("%.0f%%", damage), x + 20.0f, y + yAdd + 4.0f, SCModule.getColor(damageColor), this.textShadow.getBoolean());
                        }
                    }
                }
            }
        }
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
    
    private int getWidthOfLargestItemName() {
        int width = 0;
        for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; ++i) {
            final ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
            if (itemStack != null) {
                final String name = itemStack.getItem().getItemStackDisplayName(itemStack);
                if (this.mc.fontRendererObj.getStringWidth(name) > width) {
                    width = this.mc.fontRendererObj.getStringWidth(name);
                }
            }
        }
        if (this.equippedItem.getBoolean()) {
            final ItemStack itemStack2 = this.mc.thePlayer.getCurrentEquippedItem();
            if (itemStack2 == null) {
                return width;
            }
            final String name2 = itemStack2.getItem().getItemStackDisplayName(itemStack2);
            if (this.mc.fontRendererObj.getStringWidth(name2) > width) {
                width = this.mc.fontRendererObj.getStringWidth(name2);
            }
        }
        return width;
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
        if (p_175030_2_.getItem() instanceof ItemBow) {
            this.renderItem3(p_175030_1_, p_175030_2_, p_175030_3_, p_175030_4_, new StringBuilder().append(this.getItemCount(new ItemStack(Items.arrow))).toString());
        }
        else {
            this.renderItem3(p_175030_1_, p_175030_2_, p_175030_3_, p_175030_4_, null);
        }
    }
    
    private void renderItem3(final FontRenderer fr, final ItemStack is, final int xPos, final int yPos, final String itemCount) {
        if (is != null) {
            if (is.stackSize != 1 || itemCount != null) {
                String itemDamaged = (itemCount == null) ? String.valueOf(is.stackSize) : itemCount;
                if (itemCount == null && is.stackSize < 1) {
                    itemDamaged = EnumChatFormatting.RED + String.valueOf(is.stackSize);
                }
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                if (this.itemCount.getBoolean()) {
                    fr.func_175063_a(itemDamaged, (float)(xPos + 19 - 2 - fr.getStringWidth(itemDamaged)), (float)(yPos + 6 + 3), 16777215);
                }
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
            boolean itemDamaged2 = is.isItemDamaged();
            if (Reflector.ForgeItem_showDurabilityBar.exists()) {
                itemDamaged2 = Reflector.callBoolean(is.getItem(), Reflector.ForgeItem_showDurabilityBar, is);
            }
            if (itemDamaged2) {
                int var12 = (int)Math.round(13.0 - is.getItemDamage() * 13.0 / is.getMaxDamage());
                int var13 = (int)Math.round(255.0 - is.getItemDamage() * 255.0 / is.getMaxDamage());
                if (Reflector.ForgeItem_getDurabilityForDisplay.exists()) {
                    final double var14 = Reflector.callDouble(is.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, is);
                    var12 = (int)Math.round(13.0 - var14 * 13.0);
                    var13 = (int)Math.round(255.0 - var14 * 255.0);
                }
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.func_179090_x();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                final Tessellator var15 = Tessellator.getInstance();
                final WorldRenderer var16 = var15.getWorldRenderer();
                final int var17 = 255 - var13 << 16 | var13 << 8;
                final int var18 = (255 - var13) / 4 << 16 | 0x3F00;
                if (this.damageOverlay.getBoolean()) {
                    this.mc.getRenderItem().func_175044_a(var16, xPos + 2, yPos + 13, 13, 2, 0);
                    this.mc.getRenderItem().func_175044_a(var16, xPos + 2, yPos + 13, 12, 1, var18);
                    this.mc.getRenderItem().func_175044_a(var16, xPos + 2, yPos + 13, var12, 1, var17);
                }
                if (!Reflector.ForgeHooksClient.exists()) {
                    GlStateManager.enableBlend();
                }
                GlStateManager.enableAlpha();
                GlStateManager.func_179098_w();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_RIGHT;
    }
}
