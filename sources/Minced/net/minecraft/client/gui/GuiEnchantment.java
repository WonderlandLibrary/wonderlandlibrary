// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraft.world.IWorldNameable;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ContainerEnchantment;
import java.util.Random;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiEnchantment extends GuiContainer
{
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE;
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE;
    private static final ModelBook MODEL_BOOK;
    private final InventoryPlayer playerInventory;
    private final Random random;
    private final ContainerEnchantment container;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last;
    private final IWorldNameable nameable;
    
    public GuiEnchantment(final InventoryPlayer inventory, final World worldIn, final IWorldNameable nameable) {
        super(new ContainerEnchantment(inventory, worldIn));
        this.random = new Random();
        this.last = ItemStack.EMPTY;
        this.playerInventory = inventory;
        this.container = (ContainerEnchantment)this.inventorySlots;
        this.nameable = nameable;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRenderer.drawString(this.nameable.getDisplayName().getUnformattedText(), 12, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.tickBook();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        for (int k = 0; k < 3; ++k) {
            final int l = mouseX - (i + 60);
            final int i2 = mouseY - (j + 14 + 19 * k);
            if (l >= 0 && i2 >= 0 && l < 108 && i2 < 19) {
                final ContainerEnchantment container = this.container;
                final Minecraft mc = GuiEnchantment.mc;
                if (container.enchantItem(Minecraft.player, k)) {
                    GuiEnchantment.mc.playerController.sendEnchantPacket(this.container.windowId, k);
                }
            }
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiEnchantment.mc.getTextureManager().bindTexture(GuiEnchantment.ENCHANTMENT_TABLE_GUI_TEXTURE);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final ScaledResolution scaledresolution = new ScaledResolution(GuiEnchantment.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * ScaledResolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * ScaledResolution.getScaleFactor(), 320 * ScaledResolution.getScaleFactor(), 240 * ScaledResolution.getScaleFactor());
        GlStateManager.translate(-0.34f, 0.23f, 0.0f);
        Project.gluPerspective(90.0f, 1.3333334f, 9.0f, 80.0f);
        final float f = 1.0f;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0f, 3.3f, -16.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        final float f2 = 5.0f;
        GlStateManager.scale(5.0f, 5.0f, 5.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GuiEnchantment.mc.getTextureManager().bindTexture(GuiEnchantment.ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
        final float f3 = this.oOpen + (this.open - this.oOpen) * partialTicks;
        GlStateManager.translate((1.0f - f3) * 0.2f, (1.0f - f3) * 0.1f, (1.0f - f3) * 0.25f);
        GlStateManager.rotate(-(1.0f - f3) * 90.0f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        float f4 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.25f;
        float f5 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.75f;
        f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6f - 0.3f;
        f5 = (f5 - MathHelper.fastFloor(f5)) * 1.6f - 0.3f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        GlStateManager.enableRescaleNormal();
        GuiEnchantment.MODEL_BOOK.render(null, 0.0f, f4, f5, f3, 0.0f, 0.0625f);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, GuiEnchantment.mc.displayWidth, GuiEnchantment.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
        final int k = this.container.getLapisAmount();
        for (int l = 0; l < 3; ++l) {
            final int i2 = i + 60;
            final int j2 = i2 + 20;
            this.zLevel = 0.0f;
            GuiEnchantment.mc.getTextureManager().bindTexture(GuiEnchantment.ENCHANTMENT_TABLE_GUI_TEXTURE);
            final int k2 = this.container.enchantLevels[l];
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (k2 == 0) {
                this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 185, 108, 19);
            }
            else {
                final String s = "" + k2;
                final int l2 = 86 - this.fontRenderer.getStringWidth(s);
                final String s2 = EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l2);
                FontRenderer fontrenderer = GuiEnchantment.mc.standardGalacticFontRenderer;
                int i3 = 6839882;
                Label_0988: {
                    Label_0824: {
                        if (k >= l + 1) {
                            final Minecraft mc = GuiEnchantment.mc;
                            if (Minecraft.player.experienceLevel >= k2) {
                                break Label_0824;
                            }
                        }
                        final Minecraft mc2 = GuiEnchantment.mc;
                        if (!Minecraft.player.capabilities.isCreativeMode) {
                            this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 185, 108, 19);
                            this.drawTexturedModalRect(i2 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                            fontrenderer.drawSplitString(s2, j2, j + 16 + 19 * l, l2, (i3 & 0xFEFEFE) >> 1);
                            i3 = 4226832;
                            break Label_0988;
                        }
                    }
                    final int j3 = mouseX - (i + 60);
                    final int k3 = mouseY - (j + 14 + 19 * l);
                    if (j3 >= 0 && k3 >= 0 && j3 < 108 && k3 < 19) {
                        this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 204, 108, 19);
                        i3 = 16777088;
                    }
                    else {
                        this.drawTexturedModalRect(i2, j + 14 + 19 * l, 0, 166, 108, 19);
                    }
                    this.drawTexturedModalRect(i2 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                    fontrenderer.drawSplitString(s2, j2, j + 16 + 19 * l, l2, i3);
                    i3 = 8453920;
                }
                fontrenderer = GuiEnchantment.mc.fontRenderer;
                fontrenderer.drawStringWithShadow(s, (float)(j2 + 86 - fontrenderer.getStringWidth(s)), (float)(j + 16 + 19 * l + 7), i3);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, float partialTicks) {
        partialTicks = GuiEnchantment.mc.getTickLength();
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        final Minecraft mc = GuiEnchantment.mc;
        final boolean flag = Minecraft.player.capabilities.isCreativeMode;
        final int i = this.container.getLapisAmount();
        for (int j = 0; j < 3; ++j) {
            final int k = this.container.enchantLevels[j];
            final Enchantment enchantment = Enchantment.getEnchantmentByID(this.container.enchantClue[j]);
            final int l = this.container.worldClue[j];
            final int i2 = j + 1;
            if (this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0 && l >= 0 && enchantment != null) {
                final List<String> list = (List<String>)Lists.newArrayList();
                list.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format("container.enchant.clue", enchantment.getTranslatedName(l)));
                if (!flag) {
                    list.add("");
                    final Minecraft mc2 = GuiEnchantment.mc;
                    if (Minecraft.player.experienceLevel < k) {
                        list.add(TextFormatting.RED + I18n.format("container.enchant.level.requirement", this.container.enchantLevels[j]));
                    }
                    else {
                        String s;
                        if (i2 == 1) {
                            s = I18n.format("container.enchant.lapis.one", new Object[0]);
                        }
                        else {
                            s = I18n.format("container.enchant.lapis.many", i2);
                        }
                        final TextFormatting textformatting = (i >= i2) ? TextFormatting.GRAY : TextFormatting.RED;
                        list.add(textformatting + "" + s);
                        if (i2 == 1) {
                            s = I18n.format("container.enchant.level.one", new Object[0]);
                        }
                        else {
                            s = I18n.format("container.enchant.level.many", i2);
                        }
                        list.add(TextFormatting.GRAY + "" + s);
                    }
                }
                this.drawHoveringText(list, mouseX, mouseY);
                break;
            }
        }
    }
    
    public void tickBook() {
        final ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual(itemstack, this.last)) {
            this.last = itemstack;
            do {
                this.flipT += this.random.nextInt(4) - this.random.nextInt(4);
            } while (this.flip <= this.flipT + 1.0f && this.flip >= this.flipT - 1.0f);
        }
        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;
        for (int i = 0; i < 3; ++i) {
            if (this.container.enchantLevels[i] != 0) {
                flag = true;
            }
        }
        if (flag) {
            this.open += 0.2f;
        }
        else {
            this.open -= 0.2f;
        }
        this.open = MathHelper.clamp(this.open, 0.0f, 1.0f);
        float f1 = (this.flipT - this.flip) * 0.4f;
        final float f2 = 0.2f;
        f1 = MathHelper.clamp(f1, -0.2f, 0.2f);
        this.flipA += (f1 - this.flipA) * 0.9f;
        this.flip += this.flipA;
    }
    
    static {
        ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
        ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
        MODEL_BOOK = new ModelBook();
    }
}
