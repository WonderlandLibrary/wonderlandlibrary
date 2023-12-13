// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBrewingStand extends GuiContainer
{
    private static final ResourceLocation brewingStandGuiTextures;
    private final InventoryPlayer field_175384_v;
    private IInventory tileBrewingStand;
    private static final String __OBFID = "CL_00000746";
    
    public GuiBrewingStand(final InventoryPlayer p_i45506_1_, final IInventory p_i45506_2_) {
        super(new ContainerBrewingStand(p_i45506_1_, p_i45506_2_));
        this.field_175384_v = p_i45506_1_;
        this.tileBrewingStand = p_i45506_2_;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String var3 = this.tileBrewingStand.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6.0f, 4210752);
        this.fontRendererObj.drawString(this.field_175384_v.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiBrewingStand.brewingStandGuiTextures);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        final int var6 = this.tileBrewingStand.getField(0);
        if (var6 > 0) {
            int var7 = (int)(28.0f * (1.0f - var6 / 400.0f));
            if (var7 > 0) {
                this.drawTexturedModalRect(var4 + 97, var5 + 16, 176, 0, 9, var7);
            }
            final int var8 = var6 / 2 % 7;
            switch (var8) {
                case 0: {
                    var7 = 29;
                    break;
                }
                case 1: {
                    var7 = 24;
                    break;
                }
                case 2: {
                    var7 = 20;
                    break;
                }
                case 3: {
                    var7 = 16;
                    break;
                }
                case 4: {
                    var7 = 11;
                    break;
                }
                case 5: {
                    var7 = 6;
                    break;
                }
                case 6: {
                    var7 = 0;
                    break;
                }
            }
            if (var7 > 0) {
                this.drawTexturedModalRect(var4 + 65, var5 + 14 + 29 - var7, 185, 29 - var7, 12, var7);
            }
        }
    }
    
    static {
        brewingStandGuiTextures = new ResourceLocation("textures/gui/container/brewing_stand.png");
    }
}
