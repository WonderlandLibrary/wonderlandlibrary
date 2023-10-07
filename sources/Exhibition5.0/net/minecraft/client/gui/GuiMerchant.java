// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.IMerchant;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiMerchant extends GuiContainer
{
    private static final Logger logger;
    private static final ResourceLocation field_147038_v;
    private IMerchant field_147037_w;
    private MerchantButton field_147043_x;
    private MerchantButton field_147042_y;
    private int field_147041_z;
    private IChatComponent field_147040_A;
    private static final String __OBFID = "CL_00000762";
    
    public GuiMerchant(final InventoryPlayer p_i45500_1_, final IMerchant p_i45500_2_, final World worldIn) {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
        this.field_147037_w = p_i45500_2_;
        this.field_147040_A = p_i45500_2_.getDisplayName();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final int var1 = (this.width - this.xSize) / 2;
        final int var2 = (this.height - this.ySize) / 2;
        this.buttonList.add(this.field_147043_x = new MerchantButton(1, var1 + 120 + 27, var2 + 24 - 1, true));
        this.buttonList.add(this.field_147042_y = new MerchantButton(2, var1 + 36 - 19, var2 + 24 - 1, false));
        this.field_147043_x.enabled = false;
        this.field_147042_y.enabled = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String var3 = this.field_147040_A.getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6.0f, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final MerchantRecipeList var1 = this.field_147037_w.getRecipes(this.mc.thePlayer);
        if (var1 != null) {
            this.field_147043_x.enabled = (this.field_147041_z < var1.size() - 1);
            this.field_147042_y.enabled = (this.field_147041_z > 0);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        boolean var2 = false;
        if (button == this.field_147043_x) {
            ++this.field_147041_z;
            final MerchantRecipeList var3 = this.field_147037_w.getRecipes(this.mc.thePlayer);
            if (var3 != null && this.field_147041_z >= var3.size()) {
                this.field_147041_z = var3.size() - 1;
            }
            var2 = true;
        }
        else if (button == this.field_147042_y) {
            --this.field_147041_z;
            if (this.field_147041_z < 0) {
                this.field_147041_z = 0;
            }
            var2 = true;
        }
        if (var2) {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.field_147041_z);
            final PacketBuffer var4 = new PacketBuffer(Unpooled.buffer());
            var4.writeInt(this.field_147041_z);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", var4));
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiMerchant.field_147038_v);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        final MerchantRecipeList var6 = this.field_147037_w.getRecipes(this.mc.thePlayer);
        if (var6 != null && !var6.isEmpty()) {
            final int var7 = this.field_147041_z;
            if (var7 < 0 || var7 >= var6.size()) {
                return;
            }
            final MerchantRecipe var8 = var6.get(var7);
            if (var8.isRecipeDisabled()) {
                this.mc.getTextureManager().bindTexture(GuiMerchant.field_147038_v);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final MerchantRecipeList var4 = this.field_147037_w.getRecipes(this.mc.thePlayer);
        if (var4 != null && !var4.isEmpty()) {
            final int var5 = (this.width - this.xSize) / 2;
            final int var6 = (this.height - this.ySize) / 2;
            final int var7 = this.field_147041_z;
            final MerchantRecipe var8 = var4.get(var7);
            final ItemStack var9 = var8.getItemToBuy();
            final ItemStack var10 = var8.getSecondItemToBuy();
            final ItemStack var11 = var8.getItemToSell();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.itemRender.zLevel = 100.0f;
            this.itemRender.func_180450_b(var9, var5 + 36, var6 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, var9, var5 + 36, var6 + 24);
            if (var10 != null) {
                this.itemRender.func_180450_b(var10, var5 + 62, var6 + 24);
                this.itemRender.renderItemOverlays(this.fontRendererObj, var10, var5 + 62, var6 + 24);
            }
            this.itemRender.func_180450_b(var11, var5 + 120, var6 + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, var11, var5 + 120, var6 + 24);
            this.itemRender.zLevel = 0.0f;
            GlStateManager.disableLighting();
            if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && var9 != null) {
                this.renderToolTip(var9, mouseX, mouseY);
            }
            else if (var10 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && var10 != null) {
                this.renderToolTip(var10, mouseX, mouseY);
            }
            else if (var11 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && var11 != null) {
                this.renderToolTip(var11, mouseX, mouseY);
            }
            else if (var8.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY))) {
                this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    public IMerchant getMerchant() {
        return this.field_147037_w;
    }
    
    static {
        logger = LogManager.getLogger();
        field_147038_v = new ResourceLocation("textures/gui/container/villager.png");
    }
    
    static class MerchantButton extends GuiButton
    {
        private final boolean field_146157_o;
        private static final String __OBFID = "CL_00000763";
        
        public MerchantButton(final int p_i1095_1_, final int p_i1095_2_, final int p_i1095_3_, final boolean p_i1095_4_) {
            super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiMerchant.field_147038_v);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int var5 = 0;
                int var6 = 176;
                if (!this.enabled) {
                    var6 += this.width * 2;
                }
                else if (var4) {
                    var6 += this.width;
                }
                if (!this.field_146157_o) {
                    var5 += this.height;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, var6, var5, this.width, this.height);
            }
        }
    }
}
