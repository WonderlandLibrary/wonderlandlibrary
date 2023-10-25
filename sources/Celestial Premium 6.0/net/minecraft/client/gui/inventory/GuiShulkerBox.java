/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.ContainerBlur;
import org.celestial.client.helpers.render.RenderHelper;

public class GuiShulkerBox
extends GuiContainer {
    private static final ResourceLocation field_190778_u = new ResourceLocation("textures/gui/container/shulker_box.png");
    private final IInventory field_190779_v;
    private final InventoryPlayer field_190780_w;
    private int blur;

    public GuiShulkerBox(InventoryPlayer p_i47233_1_, IInventory p_i47233_2_) {
        super(new ContainerShulkerBox(p_i47233_1_, p_i47233_2_, Minecraft.getMinecraft().player));
        this.field_190780_w = p_i47233_1_;
        this.field_190779_v = p_i47233_2_;
        ++this.ySize;
    }

    @Override
    public void initGui() {
        this.blur = 0;
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.field_190779_v.getDisplayName().getUnformattedText(), 8.0f, 6.0f, 0x404040);
        this.fontRendererObj.drawString(this.field_190780_w.getDisplayName().getUnformattedText(), 8.0f, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(field_190778_u);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}

