// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud.bedwars;

import moonsense.enums.ModuleCategory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import java.awt.Color;
import moonsense.config.ModuleConfig;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class BedwarsOverlayModule extends SCModule
{
    public static BedwarsOverlayModule INSTANCE;
    public final Setting resourcesGroup;
    public final Setting resourcesEnabled;
    public final Setting resourcesTextShadow;
    public final Setting resourcesTextColor;
    public final Setting resourcesBackground;
    public final Setting resourcesBackgroundColor;
    public final Setting resourcesBackgroundRadius;
    public final Setting resourcesBorder;
    public final Setting resourcesBorderColor;
    public final Setting resourcesBorderThickness;
    public final Setting blocksGroup;
    public final Setting blocksEnabled;
    public final Setting blocksTextShadow;
    public final Setting blocksTextColor;
    public final Setting blocksBackground;
    public final Setting blocksBackgroundColor;
    public final Setting blocksBackgroundRadius;
    public final Setting blocksBorder;
    public final Setting blocksBorderColor;
    public final Setting blocksBorderThickness;
    public final Setting utilitiesGroup;
    public final Setting utilitiesEnabled;
    public final Setting utilitiesTextShadow;
    public final Setting utilitiesTextColor;
    public final Setting utilitiesBackground;
    public final Setting utilitiesBackgroundColor;
    public final Setting utilitiesBackgroundRadius;
    public final Setting utilitiesBorder;
    public final Setting utilitiesBorderColor;
    public final Setting utilitiesBorderThickness;
    private boolean[] childrenEnabled;
    
    public BedwarsOverlayModule() {
        super("Bedwars Overlay", "Shows the amount of items you have in bedwars.");
        this.childrenEnabled = new boolean[3];
        BedwarsOverlayModule.INSTANCE = this;
        this.resourcesGroup = new Setting(this, "Bedwars Overlay - Resources").setDefault(new Setting.CompoundSettingGroup("Bedwars Overlay - Resources", new Setting[] { this.resourcesEnabled = new Setting(null, "Enabled", "bwoverlay.resources.enabled").setDefault(true).compound(true).onValueChanged(setting -> ModuleConfig.INSTANCE.setEnabled(BedwarsResourcesOverlayChild.INSTANCE, setting.getBoolean())), this.resourcesTextShadow = new Setting(null, "Text Shadow", "bwoverlay.resources.textshadow").setDefault(false).compound(true), this.resourcesTextColor = new Setting(null, "Text Color", "bwoverlay.resources.textcolor").setDefault(-1, 0).compound(true), this.resourcesBackground = new Setting(null, "Background", "bwoverlay.resources.background").setDefault(true).compound(true), this.resourcesBackgroundColor = new Setting(null, "Background Color", "bwoverlay.resources.bgcolor").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.resourcesBackgroundRadius = new Setting(null, "Background Radius", "bwoverlay.resources.bgradius").setDefault(0.0f).setRange(0.0f, 5.0f, 0.01f).compound(true), this.resourcesBorder = new Setting(null, "Border", "bwoverlay.resources.border").setDefault(false).compound(true), this.resourcesBorderColor = new Setting(null, "Border Color", "bwoverlay.resources.bordercolor").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.resourcesBorderThickness = new Setting(null, "Border Thickness", "bwoverlay.resources.borderthickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f).compound(true) }));
        this.blocksGroup = new Setting(this, "Bedwars Overlay - Blocks").setDefault(new Setting.CompoundSettingGroup("Bedwars Overlay - blocks", new Setting[] { this.blocksEnabled = new Setting(null, "Enabled", "bwoverlay.blocks.enabled").setDefault(true).compound(true).onValueChanged(setting -> ModuleConfig.INSTANCE.setEnabled(BedwarsBlocksOverlayChild.INSTANCE, setting.getBoolean())), this.blocksTextShadow = new Setting(null, "Text Shadow", "bwoverlay.blocks.textshadow").setDefault(false).compound(true), this.blocksTextColor = new Setting(null, "Text Color", "bwoverlay.blocks.textcolor").setDefault(-1, 0).compound(true), this.blocksBackground = new Setting(null, "Background", "bwoverlay.blocks.background").setDefault(true).compound(true), this.blocksBackgroundColor = new Setting(null, "Background Color", "bwoverlay.blocks.bgcolor").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.blocksBackgroundRadius = new Setting(null, "Background Radius", "bwoverlay.blocks.bgradius").setDefault(0.0f).setRange(0.0f, 5.0f, 0.01f).compound(true), this.blocksBorder = new Setting(null, "Border", "bwoverlay.blocks.border").setDefault(false).compound(true), this.blocksBorderColor = new Setting(null, "Border Color", "bwoverlay.blocks.bordercolor").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.blocksBorderThickness = new Setting(null, "Border Thickness", "bwoverlay.blocks.borderthickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f).compound(true) }));
        this.utilitiesGroup = new Setting(this, "Bedwars Overlay - Utilities").setDefault(new Setting.CompoundSettingGroup("Bedwars Overlay - utilities", new Setting[] { this.utilitiesEnabled = new Setting(null, "Enabled", "bwoverlay.utilities.enabled").setDefault(true).compound(true).onValueChanged(setting -> ModuleConfig.INSTANCE.setEnabled(BedwarsUtilitiesOverlayChild.INSTANCE, setting.getBoolean())), this.utilitiesTextShadow = new Setting(null, "Text Shadow", "bwoverlay.utilities.textshadow").setDefault(false).compound(true), this.utilitiesTextColor = new Setting(null, "Text Color", "bwoverlay.utilities.textcolor").setDefault(-1, 0).compound(true), this.utilitiesBackground = new Setting(null, "Background", "bwoverlay.utilities.background").setDefault(true).compound(true), this.utilitiesBackgroundColor = new Setting(null, "Background Color", "bwoverlay.utilities.bgcolor").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.utilitiesBackgroundRadius = new Setting(null, "Background Radius", "bwoverlay.utilities.bgradius").setDefault(0.0f).setRange(0.0f, 5.0f, 0.01f).compound(true), this.utilitiesBorder = new Setting(null, "Border", "bwoverlay.utilities.border").setDefault(false).compound(true), this.utilitiesBorderColor = new Setting(null, "Border Color", "bwoverlay.utilities.bordercolor").setDefault(new Color(0, 0, 0, 75).getRGB(), 0).compound(true), this.utilitiesBorderThickness = new Setting(null, "Border Thickness", "bwoverlay.utilities.borderthickness").setDefault(0.5f).setRange(0.5f, 3.0f, 0.1f).compound(true) }));
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.resourcesEnabled.setValue(this.childrenEnabled[0]);
        ModuleConfig.INSTANCE.setEnabled(BedwarsResourcesOverlayChild.INSTANCE, this.childrenEnabled[0]);
        this.blocksEnabled.setValue(this.childrenEnabled[1]);
        ModuleConfig.INSTANCE.setEnabled(BedwarsBlocksOverlayChild.INSTANCE, this.childrenEnabled[1]);
        this.utilitiesEnabled.setValue(this.childrenEnabled[2]);
        ModuleConfig.INSTANCE.setEnabled(BedwarsUtilitiesOverlayChild.INSTANCE, this.childrenEnabled[2]);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.childrenEnabled[0] = this.resourcesEnabled.getBoolean();
        ModuleConfig.INSTANCE.setEnabled(BedwarsResourcesOverlayChild.INSTANCE, false);
        this.childrenEnabled[1] = this.blocksEnabled.getBoolean();
        ModuleConfig.INSTANCE.setEnabled(BedwarsBlocksOverlayChild.INSTANCE, false);
        this.childrenEnabled[2] = this.utilitiesEnabled.getBoolean();
        ModuleConfig.INSTANCE.setEnabled(BedwarsUtilitiesOverlayChild.INSTANCE, false);
    }
    
    protected void renderItemStack(final float x, final float y, final int i, final ItemStack is) {
        if (is == null) {
            return;
        }
        GlStateManager.pushMatrix();
        final int yAdd = 16 * i;
        RenderHelper.enableGUIStandardItemLighting();
        this.mc.getRenderItem().func_180450_b(is, (int)x, (int)(y + yAdd));
        GlStateManager.popMatrix();
    }
    
    protected int getItemCount(final ItemStack is) {
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
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.SERVER;
    }
}
