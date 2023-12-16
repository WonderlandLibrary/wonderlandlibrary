// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import moonsense.utils.Maps;
import net.minecraft.util.EnumChatFormatting;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import moonsense.config.utils.AnchorPoint;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCAbstractRenderModule;

public class HotbarModule extends SCAbstractRenderModule
{
    private final Setting textColor;
    private final Setting textShadow;
    private final Setting showAttackDamage;
    private final Setting showEnchantments;
    
    public HotbarModule() {
        super("Hotbar", "No description set!");
        new Setting(this, "General Options");
        this.textColor = new Setting(this, "Text Color").setDefault(13421772, 0);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(true);
        this.showAttackDamage = new Setting(this, "Show held item attack damage").setDefault(false);
        this.showEnchantments = new Setting(this, "Show held item enchantments").setDefault(false);
    }
    
    @Override
    public void render(final float x, final float y) {
        this.renderAttackDamage();
        this.renderEnchantments();
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    @Override
    public int getWidth() {
        return 0;
    }
    
    @Override
    public int getHeight() {
        return 0;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_CENTER;
    }
    
    private void renderAttackDamage() {
        if (!this.showAttackDamage.getBoolean()) {
            return;
        }
        final ItemStack heldItemStack = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        if (heldItemStack != null) {
            GlStateManager.pushMatrix();
            final float scale = 0.5f;
            GlStateManager.scale(0.5f, 0.5f, 1.0f);
            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            GuiUtils.drawCenteredString(this.getAttackDamageString(heldItemStack), sr.getScaledWidth() / 2.0f / 0.5f, (float)((sr.getScaledHeight() - 59 + (Minecraft.getMinecraft().playerController.shouldDrawHUD() ? -1 : 14) + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) * 2 + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT), this.textColor.getColor(), this.textShadow.getBoolean());
            GlStateManager.popMatrix();
        }
    }
    
    private void renderEnchantments() {
        if (!this.showEnchantments.getBoolean()) {
            return;
        }
        final ItemStack heldItemStack = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        if (heldItemStack != null) {
            String toDraw = "";
            toDraw = this.getEnchantmentString(heldItemStack);
            GlStateManager.pushMatrix();
            final float scale = 0.5f;
            GlStateManager.scale(0.5f, 0.5f, 1.0f);
            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            GuiUtils.drawString(toDraw, sr.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(toDraw) / 2 + 0.1f, (float)((sr.getScaledHeight() - 59 + (Minecraft.getMinecraft().playerController.shouldDrawHUD() ? -2 : 14) + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) * 2), this.textColor.getColor(), this.textShadow.getBoolean());
            GlStateManager.popMatrix();
        }
    }
    
    private String getAttackDamageString(final ItemStack stack) {
        for (final Object entry : stack.getTooltip(Minecraft.getMinecraft().thePlayer, true)) {
            if (((String)entry).endsWith("Attack Damage")) {
                return ((String)entry).split(" ", 2)[0].substring(2);
            }
        }
        return "";
    }
    
    private String getEnchantmentString(final ItemStack heldItemStack) {
        final StringBuilder enchantBuilder = new StringBuilder();
        final Map<Integer, Integer> en = (Map<Integer, Integer>)EnchantmentHelper.getEnchantments(heldItemStack);
        for (final Map.Entry<Integer, Integer> entry : en.entrySet()) {
            enchantBuilder.append(EnumChatFormatting.BOLD.toString());
            enchantBuilder.append(Maps.ENCHANTMENT_SHORT_NAME.get(entry.getKey()));
            enchantBuilder.append(" ");
            enchantBuilder.append(entry.getValue());
            enchantBuilder.append(" ");
        }
        return enchantBuilder.toString().trim();
    }
    
    @Override
    public boolean isNewModule() {
        return true;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
}
