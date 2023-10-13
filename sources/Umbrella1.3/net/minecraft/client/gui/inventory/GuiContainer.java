/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui.inventory;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public abstract class GuiContainer
extends GuiScreen {
    protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    protected int xSize = 176;
    protected int ySize = 166;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;
    private Slot theSlot;
    private Slot clickedSlot;
    private boolean isRightMouseClick;
    private ItemStack draggedStack;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    private ItemStack returningStack;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set dragSplittingSlots = Sets.newHashSet();
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot;
    private static final String __OBFID = "CL_00000737";

    public GuiContainer(Container p_i1072_1_) {
        this.inventorySlots = p_i1072_1_;
        this.ignoreMouseUp = true;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ItemStack var16;
        int var11;
        this.drawDefaultBackground();
        int var4 = this.guiLeft;
        int var5 = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(var4, var5, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        this.theSlot = null;
        int var6 = 240;
        int var7 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0f, (float)var7 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int var8 = 0; var8 < this.inventorySlots.inventorySlots.size(); ++var8) {
            Slot var9 = (Slot)this.inventorySlots.inventorySlots.get(var8);
            this.drawSlot(var9);
            if (!this.isMouseOverSlot(var9, mouseX, mouseY) || !var9.canBeHovered()) continue;
            this.theSlot = var9;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int var10 = var9.xDisplayPosition;
            var11 = var9.yDisplayPosition;
            GlStateManager.colorMask(true, true, true, false);
            this.drawGradientRect(var10, var11, var10 + 16, var11 + 16, -2130706433, -2130706433);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        InventoryPlayer var15 = this.mc.thePlayer.inventory;
        ItemStack itemStack = var16 = this.draggedStack == null ? var15.getItemStack() : this.draggedStack;
        if (var16 != null) {
            int var17 = 8;
            var11 = this.draggedStack == null ? 8 : 16;
            String var12 = null;
            if (this.draggedStack != null && this.isRightMouseClick) {
                var16 = var16.copy();
                var16.stackSize = MathHelper.ceiling_float_int((float)var16.stackSize / 2.0f);
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                var16 = var16.copy();
                var16.stackSize = this.dragSplittingRemnant;
                if (var16.stackSize == 0) {
                    var12 = (Object)((Object)EnumChatFormatting.YELLOW) + "0";
                }
            }
            this.drawItemStack(var16, mouseX - var4 - var17, mouseY - var5 - var11, var12);
        }
        if (this.returningStack != null) {
            float var18 = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0f;
            if (var18 >= 1.0f) {
                var18 = 1.0f;
                this.returningStack = null;
            }
            var11 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            int var20 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            int var13 = this.touchUpX + (int)((float)var11 * var18);
            int var14 = this.touchUpY + (int)((float)var20 * var18);
            this.drawItemStack(this.returningStack, var13, var14, null);
        }
        GlStateManager.popMatrix();
        if (var15.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
            ItemStack var19 = this.theSlot.getStack();
            this.renderToolTip(var19, mouseX, mouseY);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0f, 0.0f, 32.0f);
        this.zLevel = 200.0f;
        this.itemRender.zLevel = 200.0f;
        this.itemRender.func_180450_b(stack, x, y);
        this.itemRender.func_180453_a(this.fontRendererObj, stack, x, y - (this.draggedStack == null ? 0 : 8), altText);
        this.zLevel = 0.0f;
        this.itemRender.zLevel = 0.0f;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

    private void drawSlot(Slot slotIn) {
        String var9;
        int var2 = slotIn.xDisplayPosition;
        int var3 = slotIn.yDisplayPosition;
        ItemStack var4 = slotIn.getStack();
        boolean var5 = false;
        boolean var6 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
        ItemStack var7 = this.mc.thePlayer.inventory.getItemStack();
        String var8 = null;
        if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && var4 != null) {
            var4 = var4.copy();
            var4.stackSize /= 2;
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && var7 != null) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slotIn, var7, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                var4 = var7.copy();
                var5 = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, var4, slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);
                if (var4.stackSize > var4.getMaxStackSize()) {
                    var8 = "" + (Object)((Object)EnumChatFormatting.YELLOW) + var4.getMaxStackSize();
                    var4.stackSize = var4.getMaxStackSize();
                }
                if (var4.stackSize > slotIn.func_178170_b(var4)) {
                    var8 = "" + (Object)((Object)EnumChatFormatting.YELLOW) + slotIn.func_178170_b(var4);
                    var4.stackSize = slotIn.func_178170_b(var4);
                }
            } else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        if (var4 == null && (var9 = slotIn.func_178171_c()) != null) {
            TextureAtlasSprite var10 = this.mc.getTextureMapBlocks().getAtlasSprite(var9);
            GlStateManager.disableLighting();
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            this.func_175175_a(var2, var3, var10, 16, 16);
            GlStateManager.enableLighting();
            var6 = true;
        }
        if (!var6) {
            if (var5) {
                GuiContainer.drawRect(var2, var3, var2 + 16, var3 + 16, -2130706433);
            }
            GlStateManager.enableDepth();
            this.itemRender.func_180450_b(var4, var2, var3);
            this.itemRender.func_180453_a(this.fontRendererObj, var4, var2, var3, var8);
        }
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }

    private void updateDragSplitting() {
        ItemStack var1 = this.mc.thePlayer.inventory.getItemStack();
        if (var1 != null && this.dragSplitting) {
            this.dragSplittingRemnant = var1.stackSize;
            for (Slot var3 : this.dragSplittingSlots) {
                ItemStack var4 = var1.copy();
                int var5 = var3.getStack() == null ? 0 : var3.getStack().stackSize;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, var4, var5);
                if (var4.stackSize > var4.getMaxStackSize()) {
                    var4.stackSize = var4.getMaxStackSize();
                }
                if (var4.stackSize > var3.func_178170_b(var4)) {
                    var4.stackSize = var3.func_178170_b(var4);
                }
                this.dragSplittingRemnant -= var4.stackSize - var5;
            }
        }
    }

    private Slot getSlotAtPosition(int x, int y) {
        for (int var3 = 0; var3 < this.inventorySlots.inventorySlots.size(); ++var3) {
            Slot var4 = (Slot)this.inventorySlots.inventorySlots.get(var3);
            if (!this.isMouseOverSlot(var4, x, y)) continue;
            return var4;
        }
        return null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean var4 = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
        Slot var5 = this.getSlotAtPosition(mouseX, mouseY);
        long var6 = Minecraft.getSystemTime();
        this.doubleClick = this.lastClickSlot == var5 && var6 - this.lastClickTime < 250L && this.lastClickButton == mouseButton;
        this.ignoreMouseUp = false;
        if (mouseButton == 0 || mouseButton == 1 || var4) {
            int var8 = this.guiLeft;
            int var9 = this.guiTop;
            boolean var10 = mouseX < var8 || mouseY < var9 || mouseX >= var8 + this.xSize || mouseY >= var9 + this.ySize;
            int var11 = -1;
            if (var5 != null) {
                var11 = var5.slotNumber;
            }
            if (var10) {
                var11 = -999;
            }
            if (this.mc.gameSettings.touchscreen && var10 && this.mc.thePlayer.inventory.getItemStack() == null) {
                this.mc.displayGuiScreen(null);
                return;
            }
            if (var11 != -1) {
                if (this.mc.gameSettings.touchscreen) {
                    if (var5 != null && var5.getHasStack()) {
                        this.clickedSlot = var5;
                        this.draggedStack = null;
                        this.isRightMouseClick = mouseButton == 1;
                    } else {
                        this.clickedSlot = null;
                    }
                } else if (!this.dragSplitting) {
                    if (this.mc.thePlayer.inventory.getItemStack() == null) {
                        if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.handleMouseClick(var5, var11, mouseButton, 3);
                        } else {
                            boolean var12 = var11 != -999 && (Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54));
                            int var13 = 0;
                            if (var12) {
                                this.shiftClickedSlot = var5 != null && var5.getHasStack() ? var5.getStack() : null;
                                var13 = 1;
                            } else if (var11 == -999) {
                                var13 = 4;
                            }
                            this.handleMouseClick(var5, var11, mouseButton, var13);
                        }
                        this.ignoreMouseUp = true;
                    } else {
                        this.dragSplitting = true;
                        this.dragSplittingButton = mouseButton;
                        this.dragSplittingSlots.clear();
                        if (mouseButton == 0) {
                            this.dragSplittingLimit = 0;
                        } else if (mouseButton == 1) {
                            this.dragSplittingLimit = 1;
                        } else if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }
        this.lastClickSlot = var5;
        this.lastClickTime = var6;
        this.lastClickButton = mouseButton;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        Slot var6 = this.getSlotAtPosition(mouseX, mouseY);
        ItemStack var7 = this.mc.thePlayer.inventory.getItemStack();
        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (clickedMouseButton == 0 || clickedMouseButton == 1) {
                if (this.draggedStack == null) {
                    if (var6 != this.clickedSlot) {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                } else if (this.draggedStack.stackSize > 1 && var6 != null && Container.canAddItemToSlot(var6, this.draggedStack, false)) {
                    long var8 = Minecraft.getSystemTime();
                    if (this.currentDragTargetSlot == var6) {
                        if (var8 - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.handleMouseClick(var6, var6.slotNumber, 1, 0);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.dragItemDropDelay = var8 + 750L;
                            --this.draggedStack.stackSize;
                        }
                    } else {
                        this.currentDragTargetSlot = var6;
                        this.dragItemDropDelay = var8;
                    }
                }
            }
        } else if (this.dragSplitting && var6 != null && var7 != null && var7.stackSize > this.dragSplittingSlots.size() && Container.canAddItemToSlot(var6, var7, true) && var6.isItemValid(var7) && this.inventorySlots.canDragIntoSlot(var6)) {
            this.dragSplittingSlots.add(var6);
            this.updateDragSplitting();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Slot var4 = this.getSlotAtPosition(mouseX, mouseY);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        boolean var7 = mouseX < var5 || mouseY < var6 || mouseX >= var5 + this.xSize || mouseY >= var6 + this.ySize;
        int var8 = -1;
        if (var4 != null) {
            var8 = var4.slotNumber;
        }
        if (var7) {
            var8 = -999;
        }
        if (this.doubleClick && var4 != null && state == 0 && this.inventorySlots.func_94530_a(null, var4)) {
            if (GuiContainer.isShiftKeyDown()) {
                if (var4 != null && var4.inventory != null && this.shiftClickedSlot != null) {
                    for (Slot var10 : this.inventorySlots.inventorySlots) {
                        if (var10 == null || !var10.canTakeStack(this.mc.thePlayer) || !var10.getHasStack() || var10.inventory != var4.inventory || !Container.canAddItemToSlot(var10, this.shiftClickedSlot, true)) continue;
                        this.handleMouseClick(var10, var10.slotNumber, state, 1);
                    }
                }
            } else {
                this.handleMouseClick(var4, var8, state, 6);
            }
            this.doubleClick = false;
            this.lastClickTime = 0L;
        } else {
            if (this.dragSplitting && this.dragSplittingButton != state) {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }
            if (this.ignoreMouseUp) {
                this.ignoreMouseUp = false;
                return;
            }
            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
                if (state == 0 || state == 1) {
                    if (this.draggedStack == null && var4 != this.clickedSlot) {
                        this.draggedStack = this.clickedSlot.getStack();
                    }
                    boolean var9 = Container.canAddItemToSlot(var4, this.draggedStack, false);
                    if (var8 != -1 && this.draggedStack != null && var9) {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
                        this.handleMouseClick(var4, var8, 0, 0);
                        if (this.mc.thePlayer.inventory.getItemStack() != null) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, 0);
                            this.touchUpX = mouseX - var5;
                            this.touchUpY = mouseY - var6;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        } else {
                            this.returningStack = null;
                        }
                    } else if (this.draggedStack != null) {
                        this.touchUpX = mouseX - var5;
                        this.touchUpY = mouseY - var6;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }
                    this.draggedStack = null;
                    this.clickedSlot = null;
                }
            } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
                this.handleMouseClick(null, -999, Container.func_94534_d(0, this.dragSplittingLimit), 5);
                for (Slot var10 : this.dragSplittingSlots) {
                    this.handleMouseClick(var10, var10.slotNumber, Container.func_94534_d(1, this.dragSplittingLimit), 5);
                }
                this.handleMouseClick(null, -999, Container.func_94534_d(2, this.dragSplittingLimit), 5);
            } else if (this.mc.thePlayer.inventory.getItemStack() != null) {
                if (state == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                    this.handleMouseClick(var4, var8, state, 3);
                } else {
                    boolean var9;
                    boolean bl = var9 = var8 != -999 && (Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54));
                    if (var9) {
                        this.shiftClickedSlot = var4 != null && var4.getHasStack() ? var4.getStack() : null;
                    }
                    this.handleMouseClick(var4, var8, state, var9 ? 1 : 0);
                }
            }
        }
        if (this.mc.thePlayer.inventory.getItemStack() == null) {
            this.lastClickTime = 0L;
        }
        this.dragSplitting = false;
    }

    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }

    protected boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
        int var7 = this.guiLeft;
        int var8 = this.guiTop;
        return (pointX -= var7) >= left - 1 && pointX < left + right + 1 && (pointY -= var8) >= top - 1 && pointY < top + bottom + 1;
    }

    protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
        if (slotIn != null) {
            slotId = slotIn.slotNumber;
        }
        this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, clickedButton, clickType, this.mc.thePlayer);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
        this.checkHotbarKeys(keyCode);
        if (this.theSlot != null && this.theSlot.getHasStack()) {
            if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
            } else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, GuiContainer.isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
    }

    protected boolean checkHotbarKeys(int keyCode) {
        if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
            for (int var2 = 0; var2 < 9; ++var2) {
                if (keyCode != this.mc.gameSettings.keyBindsHotbar[var2].getKeyCode()) continue;
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, var2, 2);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onGuiClosed() {
        if (this.mc.thePlayer != null) {
            this.inventorySlots.onContainerClosed(this.mc.thePlayer);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
            this.mc.thePlayer.closeScreen();
        }
    }
}

