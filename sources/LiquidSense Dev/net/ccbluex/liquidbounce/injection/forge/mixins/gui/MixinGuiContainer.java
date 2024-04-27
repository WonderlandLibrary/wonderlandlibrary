package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import com.google.common.collect.Sets;
import javafx.scene.transform.Translate;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SlientStealerEvent;
import net.ccbluex.liquidbounce.event.UpdateModelEvent;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Set;

@Mixin(GuiContainer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiContainer extends GuiScreen{

    @Shadow
    protected int guiLeft;

    @Shadow
    protected int guiTop;

    @Shadow
    private Slot theSlot;

    @Shadow
    private ItemStack draggedStack;

    @Shadow
    protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

    @Shadow
    public Container inventorySlots;

    @Shadow
    public abstract void drawSlot(Slot p_drawSlot_1_);

    @Shadow
    public abstract boolean isMouseOverSlot(Slot p_isMouseOverSlot_1_, int p_isMouseOverSlot_2_, int p_isMouseOverSlot_3_);

    @Shadow
    protected abstract void drawGuiContainerForegroundLayer(int p_drawGuiContainerForegroundLayer_1_, int p_drawGuiContainerForegroundLayer_2_);

    @Shadow
    private boolean isRightMouseClick;

    @Shadow
    protected boolean dragSplitting;

    @Shadow
    protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();

    @Shadow
    public abstract void drawItemStack(ItemStack p_drawItemStack_1_, int p_drawItemStack_2_, int p_drawItemStack_3_, String p_drawItemStack_4_);

    @Shadow
    private int dragSplittingRemnant;

    @Shadow
    private ItemStack returningStack;

    @Shadow
    private long returningStackTime;

    @Shadow
    private Slot returningStackDestSlot;

    @Shadow
    private int touchUpX;

    @Shadow
    private int touchUpY;

    @Shadow
    protected int xSize = 176;

    @Shadow
    protected int ySize = 166;

    @Overwrite
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        ChestStealer ch = (ChestStealer) LiquidBounce.moduleManager.getModule(ChestStealer.class);
        if (ch.getState() && ch.getTrue().get() && mc.currentScreen instanceof GuiChest){
            Minecraft.getMinecraft().inGameHasFocus = true;
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            if(true) return;
        }
        this.drawDefaultBackground();
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(p_drawScreen_3_, p_drawScreen_1_, p_drawScreen_2_);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        this.theSlot = null;
        int k = 240;
        int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int l2;
        for(int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
            this.drawSlot(slot);
            if (this.isMouseOverSlot(slot, p_drawScreen_1_, p_drawScreen_2_) && slot.canBeHovered()) {
                this.theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xDisplayPosition;
                l2 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j1, l2, j1 + 16, l2 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(p_drawScreen_1_, p_drawScreen_2_);
        RenderHelper.enableGUIStandardItemLighting();
        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;
        if (itemstack != null) {
            int j2 = 8;
            l2 = this.draggedStack == null ? 8 : 16;
            String s = null;
            if (this.draggedStack != null && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int((float)itemstack.stackSize / 2.0F);
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.stackSize = this.dragSplittingRemnant;
                if (itemstack.stackSize == 0) {
                    s = "" + EnumChatFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, p_drawScreen_1_ - i - j2, p_drawScreen_2_ - j - l2, s);
        }

        if (this.returningStack != null) {
            float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = null;
            }

            l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String)null);
        }

        GlStateManager.popMatrix();
        if (inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
            ItemStack itemstack1 = this.theSlot.getStack();
            this.renderToolTip(itemstack1, p_drawScreen_1_, p_drawScreen_2_);
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }
}
