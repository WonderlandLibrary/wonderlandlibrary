package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
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
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final ModelBook MODEL_BOOK = new ModelBook();
    private final InventoryPlayer playerInventory;
    private Random random = new Random();
    private ContainerEnchantment container;
    public int field_147073_u;
    public float field_147071_v;
    public float field_147069_w;
    public float field_147082_x;
    public float field_147081_y;
    public float field_147080_z;
    public float field_147076_A;
    ItemStack field_147077_B;
    private final IWorldNameable field_175380_I;

    public GuiEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable p_i45502_3_) {
        super((Container)new ContainerEnchantment(inventory, worldIn));
        this.playerInventory = inventory;
        this.container = (ContainerEnchantment)this.inventorySlots;
        this.field_175380_I = p_i45502_3_;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12, 5, 0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    public void updateScreen() {
        super.updateScreen();
        this.func_147068_g();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        for (int k = 0; k < 3; ++k) {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);
            if (l < 0 || i1 < 0 || l >= 108 || i1 >= 19 || !this.container.enchantItem((EntityPlayer)this.mc.thePlayer, k)) continue;
            this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (width - this.xSize) / 2;
        int j = (height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((int)((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor()), (int)((scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor()), (int)(320 * scaledresolution.getScaleFactor()), (int)(240 * scaledresolution.getScaleFactor()));
        GlStateManager.translate((float)-0.34f, (float)0.23f, (float)0.0f);
        Project.gluPerspective((float)90.0f, (float)1.3333334f, (float)9.0f, (float)80.0f);
        float f = 1.0f;
        GlStateManager.matrixMode((int)5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate((float)0.0f, (float)3.3f, (float)-16.0f);
        GlStateManager.scale((float)f, (float)f, (float)f);
        float f1 = 5.0f;
        GlStateManager.scale((float)f1, (float)f1, (float)f1);
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate((float)20.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        float f2 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * partialTicks;
        GlStateManager.translate((float)((1.0f - f2) * 0.2f), (float)((1.0f - f2) * 0.1f), (float)((1.0f - f2) * 0.25f));
        GlStateManager.rotate((float)(-(1.0f - f2) * 90.0f - 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        float f3 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.25f;
        float f4 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.75f;
        f3 = (f3 - (float)MathHelper.truncateDoubleToInt((double)f3)) * 1.6f - 0.3f;
        f4 = (f4 - (float)MathHelper.truncateDoubleToInt((double)f4)) * 1.6f - 0.3f;
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
        MODEL_BOOK.render((Entity)null, 0.0f, f3, f4, f2, 0.0f, 0.0625f);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        EnchantmentNameParts.getInstance().reseedRandomGenerator((long)this.container.xpSeed);
        int k = this.container.getLapisAmount();
        for (int l = 0; l < 3; ++l) {
            int i1 = i + 60;
            int j1 = i1 + 20;
            int k1 = 86;
            String s = EnchantmentNameParts.getInstance().generateNewRandomName();
            zLevel = 0.0f;
            this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int l1 = this.container.enchantLevels[l];
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            if (l1 == 0) {
                this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                continue;
            }
            String s1 = "" + l1;
            FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
            int i2 = 6839882;
            if (!(k >= l + 1 && this.mc.thePlayer.experienceLevel >= l1 || this.mc.thePlayer.capabilities.isCreativeMode)) {
                this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                fontrenderer.drawSplitString(s, j1, j + 16 + 19 * l, k1, (i2 & 0xFEFEFE) >> 1);
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
                fontrenderer.drawSplitString(s, j1, j + 16 + 19 * l, k1, i2);
                i2 = 8453920;
            }
            fontrenderer = this.mc.fontRendererObj;
            fontrenderer.drawStringWithShadow(s1, (float)(j1 + 86 - fontrenderer.getStringWidth(s1)), (float)(j + 16 + 19 * l + 7), i2);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean flag = this.mc.thePlayer.capabilities.isCreativeMode;
        int i = this.container.getLapisAmount();
        for (int j = 0; j < 3; ++j) {
            int k = this.container.enchantLevels[j];
            int l = this.container.enchantmentIds[j];
            int i1 = j + 1;
            if (!this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) || k <= 0 || l < 0) continue;
            ArrayList list = Lists.newArrayList();
            if (l >= 0 && Enchantment.getEnchantmentById((int)(l & 0xFF)) != null) {
                String s = Enchantment.getEnchantmentById((int)(l & 0xFF)).getTranslatedName((l & 0xFF00) >> 8);
                list.add((Object)(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC.toString() + I18n.format((String)"container.enchant.clue", (Object[])new Object[]{s})));
            }
            if (!flag) {
                if (l >= 0) {
                    list.add((Object)"");
                }
                if (this.mc.thePlayer.experienceLevel < k) {
                    list.add((Object)(EnumChatFormatting.RED.toString() + "Level Requirement: " + this.container.enchantLevels[j]));
                } else {
                    String s1 = "";
                    s1 = i1 == 1 ? I18n.format((String)"container.enchant.lapis.one", (Object[])new Object[0]) : I18n.format((String)"container.enchant.lapis.many", (Object[])new Object[]{i1});
                    if (i >= i1) {
                        list.add((Object)(EnumChatFormatting.GRAY.toString() + "" + s1));
                    } else {
                        list.add((Object)(EnumChatFormatting.RED.toString() + "" + s1));
                    }
                    s1 = i1 == 1 ? I18n.format((String)"container.enchant.level.one", (Object[])new Object[0]) : I18n.format((String)"container.enchant.level.many", (Object[])new Object[]{i1});
                    list.add((Object)(EnumChatFormatting.GRAY.toString() + "" + s1));
                }
            }
            this.drawHoveringText((List)list, mouseX, mouseY);
            break;
        }
    }

    public void func_147068_g() {
        ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual((ItemStack)itemstack, (ItemStack)this.field_147077_B)) {
            this.field_147077_B = itemstack;
            do {
                this.field_147082_x += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while (!(this.field_147071_v > this.field_147082_x + 1.0f) && !(this.field_147071_v < this.field_147082_x - 1.0f));
        }
        ++this.field_147073_u;
        this.field_147069_w = this.field_147071_v;
        this.field_147076_A = this.field_147080_z;
        boolean flag = false;
        for (int i = 0; i < 3; ++i) {
            if (this.container.enchantLevels[i] == 0) continue;
            flag = true;
        }
        this.field_147080_z = flag ? (this.field_147080_z += 0.2f) : (this.field_147080_z -= 0.2f);
        this.field_147080_z = MathHelper.clamp_float((float)this.field_147080_z, (float)0.0f, (float)1.0f);
        float f1 = (this.field_147082_x - this.field_147071_v) * 0.4f;
        float f = 0.2f;
        f1 = MathHelper.clamp_float((float)f1, (float)(-f), (float)f);
        this.field_147081_y += (f1 - this.field_147081_y) * 0.9f;
        this.field_147071_v += this.field_147081_y;
    }
}
