/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.ContainerBlur;
import org.celestial.client.helpers.render.RenderHelper;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment
extends GuiContainer {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final ModelBook MODEL_BOOK = new ModelBook();
    private final InventoryPlayer playerInventory;
    private final Random random = new Random();
    private final ContainerEnchantment container;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.field_190927_a;
    private final IWorldNameable nameable;
    private int blur;

    public GuiEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable nameable) {
        super(new ContainerEnchantment(inventory, worldIn));
        this.playerInventory = inventory;
        this.container = (ContainerEnchantment)this.inventorySlots;
        this.nameable = nameable;
    }

    @Override
    public void initGui() {
        this.blur = 0;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.nameable.getDisplayName().getUnformattedText(), 12.0f, 5.0f, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.tickBook();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        for (int k = 0; k < 3; ++k) {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);
            if (l < 0 || i1 < 0 || l >= 108 || i1 >= 19 || !this.container.enchantItem(this.mc.player, k)) continue;
            this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34f, 0.23f, 0.0f);
        Project.gluPerspective(90.0f, 1.3333334f, 9.0f, 80.0f);
        float f = 1.0f;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0f, 3.3f, -16.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        float f1 = 5.0f;
        GlStateManager.scale(5.0f, 5.0f, 5.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
        float f2 = this.oOpen + (this.open - this.oOpen) * partialTicks;
        GlStateManager.translate((1.0f - f2) * 0.2f, (1.0f - f2) * 0.1f, (1.0f - f2) * 0.25f);
        GlStateManager.rotate(-(1.0f - f2) * 90.0f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        float f3 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.25f;
        float f4 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.75f;
        f3 = (f3 - (float)MathHelper.fastFloor(f3)) * 1.6f - 0.3f;
        f4 = (f4 - (float)MathHelper.fastFloor(f4)) * 1.6f - 0.3f;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render(null, 0.0f, f3, f4, f2, 0.0f, 0.0625f);
        GlStateManager.disableRescaleNormal();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
        int k = this.container.getLapisAmount();
        for (int l = 0; l < 3; ++l) {
            int i1 = i + 60;
            int j1 = i1 + 20;
            this.zLevel = 0.0f;
            this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int k1 = this.container.enchantLevels[l];
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (k1 == 0) {
                this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                continue;
            }
            String s = "" + k1;
            int l1 = 86 - this.fontRendererObj.getStringWidth(s);
            String s1 = EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRendererObj, l1);
            FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
            int i2 = 6839882;
            if (!(k >= l + 1 && this.mc.player.experienceLevel >= k1 || this.mc.player.capabilities.isCreativeMode)) {
                this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, (i2 & 0xFEFEFE) >> 1);
                i2 = 4226832;
            } else {
                int j2 = mouseX - (i + 60);
                int k2 = mouseY - (j + 14 + 19 * l);
                if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19) {
                    this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 204, 108, 19);
                    i2 = 0xFFFF80;
                } else {
                    this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 166, 108, 19);
                }
                this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, i2);
                i2 = 8453920;
            }
            fontrenderer = this.mc.fontRendererObj;
            fontrenderer.drawStringWithShadow(s, j1 + 86 - fontrenderer.getStringWidth(s), j + 16 + 19 * l + 7, i2);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.mc.func_193989_ak();
        this.drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(this.mc);
        ++this.blur;
        this.blur = MathHelper.clamp(this.blur, 0, ContainerBlur.blurStrength.getCurrentValueInt());
        if (Celestial.instance.featureManager.getFeatureByClass(ContainerBlur.class).getState()) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), this.blur / 2);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
        boolean flag = this.mc.player.capabilities.isCreativeMode;
        int i = this.container.getLapisAmount();
        for (int j = 0; j < 3; ++j) {
            int k = this.container.enchantLevels[j];
            Enchantment enchantment = Enchantment.getEnchantmentByID(this.container.enchantClue[j]);
            int l = this.container.worldClue[j];
            int i1 = j + 1;
            if (!this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) || k <= 0 || l < 0 || enchantment == null) continue;
            ArrayList<String> list = Lists.newArrayList();
            list.add("" + (Object)((Object)TextFormatting.WHITE) + (Object)((Object)TextFormatting.ITALIC) + I18n.format("container.enchant.clue", enchantment.getTranslatedName(l)));
            if (!flag) {
                list.add("");
                if (this.mc.player.experienceLevel < k) {
                    list.add((Object)((Object)TextFormatting.RED) + I18n.format("container.enchant.level.requirement", this.container.enchantLevels[j]));
                } else {
                    String s = i1 == 1 ? I18n.format("container.enchant.lapis.one", new Object[0]) : I18n.format("container.enchant.lapis.many", i1);
                    TextFormatting textformatting = i >= i1 ? TextFormatting.GRAY : TextFormatting.RED;
                    list.add((Object)((Object)textformatting) + "" + s);
                    s = i1 == 1 ? I18n.format("container.enchant.level.one", new Object[0]) : I18n.format("container.enchant.level.many", i1);
                    list.add((Object)((Object)TextFormatting.GRAY) + "" + s);
                }
            }
            this.drawHoveringText(list, mouseX, mouseY);
            break;
        }
    }

    public void tickBook() {
        ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual(itemstack, this.last)) {
            this.last = itemstack;
            do {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while (!(this.flip > this.flipT + 1.0f) && !(this.flip < this.flipT - 1.0f));
        }
        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;
        for (int i = 0; i < 3; ++i) {
            if (this.container.enchantLevels[i] == 0) continue;
            flag = true;
        }
        this.open = flag ? (this.open += 0.2f) : (this.open -= 0.2f);
        this.open = MathHelper.clamp(this.open, 0.0f, 1.0f);
        float f1 = (this.flipT - this.flip) * 0.4f;
        float f = 0.2f;
        f1 = MathHelper.clamp(f1, -0.2f, 0.2f);
        this.flipA += (f1 - this.flipA) * 0.9f;
        this.flip += this.flipA;
    }
}

