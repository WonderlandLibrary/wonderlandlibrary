// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.enchantment.Enchantment;
import com.google.common.collect.Lists;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
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
    private static final ResourceLocation field_147078_C;
    private static final ResourceLocation field_147070_D;
    private static final ModelBook field_147072_E;
    private final InventoryPlayer field_175379_F;
    private Random field_147074_F;
    private ContainerEnchantment field_147075_G;
    public int field_147073_u;
    public float field_147071_v;
    public float field_147069_w;
    public float field_147082_x;
    public float field_147081_y;
    public float field_147080_z;
    public float field_147076_A;
    ItemStack field_147077_B;
    private final IWorldNameable field_175380_I;
    private static final String __OBFID = "CL_00000757";
    
    public GuiEnchantment(final InventoryPlayer p_i45502_1_, final World worldIn, final IWorldNameable p_i45502_3_) {
        super(new ContainerEnchantment(p_i45502_1_, worldIn));
        this.field_147074_F = new Random();
        this.field_175379_F = p_i45502_1_;
        this.field_147075_G = (ContainerEnchantment)this.inventorySlots;
        this.field_175380_I = p_i45502_3_;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12.0f, 5.0f, 4210752);
        this.fontRendererObj.drawString(this.field_175379_F.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.func_147068_g();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        for (int var6 = 0; var6 < 3; ++var6) {
            final int var7 = mouseX - (var4 + 60);
            final int var8 = mouseY - (var5 + 14 + 19 * var6);
            if (var7 >= 0 && var8 >= 0 && var7 < 108 && var8 < 19 && this.field_147075_G.enchantItem(this.mc.thePlayer, var6)) {
                this.mc.playerController.sendEnchantPacket(this.field_147075_G.windowId, var6);
            }
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiEnchantment.field_147078_C);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final ScaledResolution var6 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.viewport((var6.getScaledWidth() - 320) / 2 * var6.getScaleFactor(), (var6.getScaledHeight() - 240) / 2 * var6.getScaleFactor(), 320 * var6.getScaleFactor(), 240 * var6.getScaleFactor());
        GlStateManager.translate(-0.34f, 0.23f, 0.0f);
        Project.gluPerspective(90.0f, 1.3333334f, 9.0f, 80.0f);
        final float var7 = 1.0f;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0f, 3.3f, -16.0f);
        GlStateManager.scale(var7, var7, var7);
        final float var8 = 5.0f;
        GlStateManager.scale(var8, var8, var8);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiEnchantment.field_147070_D);
        GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
        final float var9 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * partialTicks;
        GlStateManager.translate((1.0f - var9) * 0.2f, (1.0f - var9) * 0.1f, (1.0f - var9) * 0.25f);
        GlStateManager.rotate(-(1.0f - var9) * 90.0f - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        float var10 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.25f;
        float var11 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.75f;
        var10 = (var10 - MathHelper.truncateDoubleToInt(var10)) * 1.6f - 0.3f;
        var11 = (var11 - MathHelper.truncateDoubleToInt(var11)) * 1.6f - 0.3f;
        if (var10 < 0.0f) {
            var10 = 0.0f;
        }
        if (var11 < 0.0f) {
            var11 = 0.0f;
        }
        if (var10 > 1.0f) {
            var10 = 1.0f;
        }
        if (var11 > 1.0f) {
            var11 = 1.0f;
        }
        GlStateManager.enableRescaleNormal();
        GuiEnchantment.field_147072_E.render(null, 0.0f, var10, var11, var9, 0.0f, 0.0625f);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        EnchantmentNameParts.func_178176_a().reseedRandomGenerator(this.field_147075_G.field_178149_f);
        final int var12 = this.field_147075_G.func_178147_e();
        for (int var13 = 0; var13 < 3; ++var13) {
            final int var14 = var4 + 60;
            final int var15 = var14 + 20;
            final byte var16 = 86;
            final String var17 = EnchantmentNameParts.func_178176_a().generateNewRandomName();
            this.zLevel = 0.0f;
            this.mc.getTextureManager().bindTexture(GuiEnchantment.field_147078_C);
            final int var18 = this.field_147075_G.enchantLevels[var13];
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (var18 == 0) {
                this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 185, 108, 19);
            }
            else {
                final String var19 = "" + var18;
                FontRenderer var20 = this.mc.standardGalacticFontRenderer;
                int var21 = 6839882;
                if ((var12 < var13 + 1 || this.mc.thePlayer.experienceLevel < var18) && !this.mc.thePlayer.capabilities.isCreativeMode) {
                    this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 185, 108, 19);
                    this.drawTexturedModalRect(var14 + 1, var5 + 15 + 19 * var13, 16 * var13, 239, 16, 16);
                    var20.drawSplitString(var17, var15, var5 + 16 + 19 * var13, var16, (var21 & 0xFEFEFE) >> 1);
                    var21 = 4226832;
                }
                else {
                    final int var22 = mouseX - (var4 + 60);
                    final int var23 = mouseY - (var5 + 14 + 19 * var13);
                    if (var22 >= 0 && var23 >= 0 && var22 < 108 && var23 < 19) {
                        this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 204, 108, 19);
                        var21 = 16777088;
                    }
                    else {
                        this.drawTexturedModalRect(var14, var5 + 14 + 19 * var13, 0, 166, 108, 19);
                    }
                    this.drawTexturedModalRect(var14 + 1, var5 + 15 + 19 * var13, 16 * var13, 223, 16, 16);
                    var20.drawSplitString(var17, var15, var5 + 16 + 19 * var13, var16, var21);
                    var21 = 8453920;
                }
                var20 = this.mc.fontRendererObj;
                var20.drawStringWithShadow(var19, var15 + 86 - var20.getStringWidth(var19), var5 + 16 + 19 * var13 + 7, var21);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final boolean var4 = this.mc.thePlayer.capabilities.isCreativeMode;
        final int var5 = this.field_147075_G.func_178147_e();
        for (int var6 = 0; var6 < 3; ++var6) {
            final int var7 = this.field_147075_G.enchantLevels[var6];
            final int var8 = this.field_147075_G.field_178151_h[var6];
            final int var9 = var6 + 1;
            if (this.isPointInRegion(60, 14 + 19 * var6, 108, 17, mouseX, mouseY) && var7 > 0 && var8 >= 0) {
                final ArrayList var10 = Lists.newArrayList();
                if (var8 >= 0 && Enchantment.func_180306_c(var8 & 0xFF) != null) {
                    final String var11 = Enchantment.func_180306_c(var8 & 0xFF).getTranslatedName((var8 & 0xFF00) >> 8);
                    var10.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC.toString() + I18n.format("container.enchant.clue", var11));
                }
                if (!var4) {
                    if (var8 >= 0) {
                        var10.add("");
                    }
                    if (this.mc.thePlayer.experienceLevel < var7) {
                        var10.add(EnumChatFormatting.RED.toString() + "Level Requirement: " + this.field_147075_G.enchantLevels[var6]);
                    }
                    else {
                        String var11 = "";
                        if (var9 == 1) {
                            var11 = I18n.format("container.enchant.lapis.one", new Object[0]);
                        }
                        else {
                            var11 = I18n.format("container.enchant.lapis.many", var9);
                        }
                        if (var5 >= var9) {
                            var10.add(EnumChatFormatting.GRAY.toString() + "" + var11);
                        }
                        else {
                            var10.add(EnumChatFormatting.RED.toString() + "" + var11);
                        }
                        if (var9 == 1) {
                            var11 = I18n.format("container.enchant.level.one", new Object[0]);
                        }
                        else {
                            var11 = I18n.format("container.enchant.level.many", var9);
                        }
                        var10.add(EnumChatFormatting.GRAY.toString() + "" + var11);
                    }
                }
                this.drawHoveringText(var10, mouseX, mouseY);
                break;
            }
        }
    }
    
    public void func_147068_g() {
        final ItemStack var1 = this.inventorySlots.getSlot(0).getStack();
        if (!ItemStack.areItemStacksEqual(var1, this.field_147077_B)) {
            this.field_147077_B = var1;
            do {
                this.field_147082_x += this.field_147074_F.nextInt(4) - this.field_147074_F.nextInt(4);
            } while (this.field_147071_v <= this.field_147082_x + 1.0f && this.field_147071_v >= this.field_147082_x - 1.0f);
        }
        ++this.field_147073_u;
        this.field_147069_w = this.field_147071_v;
        this.field_147076_A = this.field_147080_z;
        boolean var2 = false;
        for (int var3 = 0; var3 < 3; ++var3) {
            if (this.field_147075_G.enchantLevels[var3] != 0) {
                var2 = true;
            }
        }
        if (var2) {
            this.field_147080_z += 0.2f;
        }
        else {
            this.field_147080_z -= 0.2f;
        }
        this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0f, 1.0f);
        float var4 = (this.field_147082_x - this.field_147071_v) * 0.4f;
        final float var5 = 0.2f;
        var4 = MathHelper.clamp_float(var4, -var5, var5);
        this.field_147081_y += (var4 - this.field_147081_y) * 0.9f;
        this.field_147071_v += this.field_147081_y;
    }
    
    static {
        field_147078_C = new ResourceLocation("textures/gui/container/enchanting_table.png");
        field_147070_D = new ResourceLocation("textures/entity/enchanting_table_book.png");
        field_147072_E = new ModelBook();
    }
}
