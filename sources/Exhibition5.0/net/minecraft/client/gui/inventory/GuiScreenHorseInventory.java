// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer
{
    private static final ResourceLocation horseGuiTextures;
    private IInventory field_147030_v;
    private IInventory field_147029_w;
    private EntityHorse field_147034_x;
    private float field_147033_y;
    private float field_147032_z;
    private static final String __OBFID = "CL_00000760";
    
    public GuiScreenHorseInventory(final IInventory p_i1093_1_, final IInventory p_i1093_2_, final EntityHorse p_i1093_3_) {
        super(new ContainerHorseInventory(p_i1093_1_, p_i1093_2_, p_i1093_3_, Minecraft.getMinecraft().thePlayer));
        this.field_147030_v = p_i1093_1_;
        this.field_147029_w = p_i1093_2_;
        this.field_147034_x = p_i1093_3_;
        this.allowUserInput = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.field_147029_w.getDisplayName().getUnformattedText(), 8.0f, 6.0f, 4210752);
        this.fontRendererObj.drawString(this.field_147030_v.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiScreenHorseInventory.horseGuiTextures);
        final int var4 = (this.width - this.xSize) / 2;
        final int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        if (this.field_147034_x.isChested()) {
            this.drawTexturedModalRect(var4 + 79, var5 + 17, 0, this.ySize, 90, 54);
        }
        if (this.field_147034_x.canWearArmor()) {
            this.drawTexturedModalRect(var4 + 7, var5 + 35, 0, this.ySize + 54, 18, 18);
        }
        GuiInventory.drawEntityOnScreen(var4 + 51, var5 + 60, 17, var4 + 51 - this.field_147033_y, var5 + 75 - 50 - this.field_147032_z, this.field_147034_x);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.field_147033_y = mouseX;
        this.field_147032_z = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static {
        horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
    }
}
