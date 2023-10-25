/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.util.glu.Project
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment
extends GuiContainer {
    public int field_147073_u;
    public float field_147081_y;
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE;
    private final InventoryPlayer playerInventory;
    public float field_147069_w;
    ItemStack field_147077_B;
    private ContainerEnchantment container;
    private Random random = new Random();
    public float field_147071_v;
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE;
    public float field_147080_z;
    private static final ModelBook MODEL_BOOK;
    public float field_147082_x;
    private final IWorldNameable field_175380_I;
    public float field_147076_A;

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int n3 = (width - this.xSize) / 2;
        int n4 = (height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledResolution.getScaledWidth() - 320) / 2 * scaledResolution.getScaleFactor(), (scaledResolution.getScaledHeight() - 240) / 2 * scaledResolution.getScaleFactor(), 320 * scaledResolution.getScaleFactor(), 240 * scaledResolution.getScaleFactor());
        GlStateManager.translate(-0.34f, 0.23f, 0.0f);
        Project.gluPerspective((float)90.0f, (float)1.3333334f, (float)9.0f, (float)80.0f);
        float f2 = 1.0f;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0f, 3.3f, -16.0f);
        GlStateManager.scale(f2, f2, f2);
        float f3 = 5.0f;
        GlStateManager.scale(f3, f3, f3);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
        float f4 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * f;
        GlStateManager.translate((1.0f - f4) * 0.2f, (1.0f - f4) * 0.1f, (1.0f - f4) * 0.25f);
        GlStateManager.rotate(-(1.0f - f4) * 90.0f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        float f5 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * f + 0.25f;
        float f6 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * f + 0.75f;
        f5 = (f5 - (float)MathHelper.truncateDoubleToInt(f5)) * 1.6f - 0.3f;
        f6 = (f6 - (float)MathHelper.truncateDoubleToInt(f6)) * 1.6f - 0.3f;
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        if (f6 > 1.0f) {
            f6 = 1.0f;
        }
        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render(null, 0.0f, f5, f6, f4, 0.0f, 0.0625f);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, Minecraft.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
        int n5 = this.container.getLapisAmount();
        int n6 = 0;
        while (n6 < 3) {
            int n7 = n3 + 60;
            int n8 = n7 + 20;
            int n9 = 86;
            String string = EnchantmentNameParts.getInstance().generateNewRandomName();
            zLevel = 0.0f;
            this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int n10 = this.container.enchantLevels[n6];
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (n10 == 0) {
                this.drawTexturedModalRect(n7, n4 + 14 + 19 * n6, 0, 185, 108, 19);
            } else {
                String string2 = "" + n10;
                FontRenderer fontRenderer = this.mc.standardGalacticFontRenderer;
                int n11 = 6839882;
                if (!(n5 >= n6 + 1 && Minecraft.thePlayer.experienceLevel >= n10 || Minecraft.thePlayer.capabilities.isCreativeMode)) {
                    this.drawTexturedModalRect(n7, n4 + 14 + 19 * n6, 0, 185, 108, 19);
                    this.drawTexturedModalRect(n7 + 1, n4 + 15 + 19 * n6, 16 * n6, 239, 16, 16);
                    fontRenderer.drawSplitString(string, n8, n4 + 16 + 19 * n6, n9, (n11 & 0xFEFEFE) >> 1);
                    n11 = 4226832;
                } else {
                    int n12 = n - (n3 + 60);
                    int n13 = n2 - (n4 + 14 + 19 * n6);
                    if (n12 >= 0 && n13 >= 0 && n12 < 108 && n13 < 19) {
                        this.drawTexturedModalRect(n7, n4 + 14 + 19 * n6, 0, 204, 108, 19);
                        n11 = 0xFFFF80;
                    } else {
                        this.drawTexturedModalRect(n7, n4 + 14 + 19 * n6, 0, 166, 108, 19);
                    }
                    this.drawTexturedModalRect(n7 + 1, n4 + 15 + 19 * n6, 16 * n6, 223, 16, 16);
                    fontRenderer.drawSplitString(string, n8, n4 + 16 + 19 * n6, n9, n11);
                    n11 = 8453920;
                }
                fontRenderer = Minecraft.fontRendererObj;
                fontRenderer.drawStringWithShadow(string2, n8 + 86 - fontRenderer.getStringWidth(string2), n4 + 16 + 19 * n6 + 7, n11);
            }
            ++n6;
        }
    }

    static {
        ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
        ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
        MODEL_BOOK = new ModelBook();
    }

    public GuiEnchantment(InventoryPlayer inventoryPlayer, World world, IWorldNameable iWorldNameable) {
        super(new ContainerEnchantment(inventoryPlayer, world));
        this.playerInventory = inventoryPlayer;
        this.container = (ContainerEnchantment)this.inventorySlots;
        this.field_175380_I = iWorldNameable;
    }

    public void func_147068_g() {
        ItemStack itemStack = this.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual(itemStack, this.field_147077_B)) {
            this.field_147077_B = itemStack;
            do {
                this.field_147082_x += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while (!(this.field_147071_v > this.field_147082_x + 1.0f) && !(this.field_147071_v < this.field_147082_x - 1.0f));
        }
        ++this.field_147073_u;
        this.field_147069_w = this.field_147071_v;
        this.field_147076_A = this.field_147080_z;
        boolean bl = false;
        int n = 0;
        while (n < 3) {
            if (this.container.enchantLevels[n] != 0) {
                bl = true;
            }
            ++n;
        }
        this.field_147080_z = bl ? (this.field_147080_z += 0.2f) : (this.field_147080_z -= 0.2f);
        this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0f, 1.0f);
        float f = (this.field_147082_x - this.field_147071_v) * 0.4f;
        float f2 = 0.2f;
        f = MathHelper.clamp_float(f, -f2, f2);
        this.field_147081_y += (f - this.field_147081_y) * 0.9f;
        this.field_147071_v += this.field_147081_y;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.func_147068_g();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        boolean bl = Minecraft.thePlayer.capabilities.isCreativeMode;
        int n3 = this.container.getLapisAmount();
        int n4 = 0;
        while (n4 < 3) {
            int n5 = this.container.enchantLevels[n4];
            int n6 = this.container.field_178151_h[n4];
            int n7 = n4 + 1;
            if (this.isPointInRegion(60, 14 + 19 * n4, 108, 17, n, n2) && n5 > 0 && n6 >= 0) {
                String string;
                ArrayList arrayList = Lists.newArrayList();
                if (n6 >= 0 && Enchantment.getEnchantmentById(n6 & 0xFF) != null) {
                    string = Enchantment.getEnchantmentById(n6 & 0xFF).getTranslatedName((n6 & 0xFF00) >> 8);
                    arrayList.add(String.valueOf(EnumChatFormatting.WHITE.toString()) + EnumChatFormatting.ITALIC.toString() + I18n.format("container.enchant.clue", string));
                }
                if (!bl) {
                    if (n6 >= 0) {
                        arrayList.add("");
                    }
                    if (Minecraft.thePlayer.experienceLevel < n5) {
                        arrayList.add(String.valueOf(EnumChatFormatting.RED.toString()) + "Level Requirement: " + this.container.enchantLevels[n4]);
                    } else {
                        string = "";
                        string = n7 == 1 ? I18n.format("container.enchant.lapis.one", new Object[0]) : I18n.format("container.enchant.lapis.many", n7);
                        if (n3 >= n7) {
                            arrayList.add(String.valueOf(EnumChatFormatting.GRAY.toString()) + string);
                        } else {
                            arrayList.add(String.valueOf(EnumChatFormatting.RED.toString()) + string);
                        }
                        string = n7 == 1 ? I18n.format("container.enchant.level.one", new Object[0]) : I18n.format("container.enchant.level.many", n7);
                        arrayList.add(String.valueOf(EnumChatFormatting.GRAY.toString()) + string);
                    }
                }
                this.drawHoveringText(arrayList, n, n2);
                break;
            }
            ++n4;
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        int n4 = (width - this.xSize) / 2;
        int n5 = (height - this.ySize) / 2;
        int n6 = 0;
        while (n6 < 3) {
            int n7 = n - (n4 + 60);
            int n8 = n2 - (n5 + 14 + 19 * n6);
            if (n7 >= 0 && n8 >= 0 && n7 < 108 && n8 < 19 && this.container.enchantItem(Minecraft.thePlayer, n6)) {
                Minecraft.playerController.sendEnchantPacket(this.container.windowId, n6);
            }
            ++n6;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12.0, 5.0, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 0x404040);
    }
}

