// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.inventory.ClickType;
import ru.tuskevich.util.math.MathUtility;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.PLAYER.ItemScroller;
import ru.tuskevich.Minced;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Sets;
import ru.tuskevich.util.math.TimerUtility;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiContainer extends GuiScreen
{
    public static final ResourceLocation INVENTORY_BACKGROUND;
    protected int xSize;
    protected int ySize;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;
    private Slot hoveredSlot;
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
    protected final Set<Slot> dragSplittingSlots;
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
    private final TimerUtility timerHelper;
    
    public GuiContainer(final Container inventorySlotsIn) {
        this.xSize = 176;
        this.ySize = 166;
        this.draggedStack = ItemStack.EMPTY;
        this.returningStack = ItemStack.EMPTY;
        this.dragSplittingSlots = (Set<Slot>)Sets.newHashSet();
        this.shiftClickedSlot = ItemStack.EMPTY;
        this.timerHelper = new TimerUtility();
        this.inventorySlots = inventorySlotsIn;
        this.ignoreMouseUp = true;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final Minecraft mc = GuiContainer.mc;
        Minecraft.player.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderUtility.sizeAnimation(this.width, this.height, GuiContainer.animation.getDrop());
        final int i = this.guiLeft;
        final int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        this.hoveredSlot = null;
        final int k = 240;
        final int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i2 = 0; i2 < this.inventorySlots.inventorySlots.size(); ++i2) {
            final Slot slot = this.inventorySlots.inventorySlots.get(i2);
            if (slot.isEnabled()) {
                this.drawSlot(slot);
            }
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                if (Minced.getInstance().manager.getModule(ItemScroller.class).state && Mouse.isButtonDown(0) && Keyboard.isKeyDown(GuiContainer.mc.gameSettings.keyBindSneak.getKeyCode()) && GuiContainer.mc.currentScreen != null && this.timerHelper.hasTimeElapsed(Math.max(80, MathUtility.intRandom(120, 50)))) {
                    this.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
                    this.timerHelper.reset();
                }
                this.hoveredSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final int j2 = slot.xPos;
                final int k2 = slot.yPos;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j2, k2, j2 + 16, k2 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        final Minecraft mc = GuiContainer.mc;
        final InventoryPlayer inventoryplayer = Minecraft.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;
        if (!itemstack.isEmpty()) {
            final int j3 = 8;
            final int k3 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;
            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil(itemstack.getCount() / 2.0f));
            }
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);
                if (itemstack.isEmpty()) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }
            this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k3, s);
        }
        if (!this.returningStack.isEmpty()) {
            float f = (Minecraft.getSystemTime() - this.returningStackTime) / 100.0f;
            if (f >= 1.0f) {
                f = 1.0f;
                this.returningStack = ItemStack.EMPTY;
            }
            final int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            final int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            final int l3 = this.touchUpX + (int)(l2 * f);
            final int i4 = this.touchUpY + (int)(i3 * f);
            this.drawItemStack(this.returningStack, l3, i4, null);
        }
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }
    
    protected void renderHoveredToolTip(final int p_191948_1_, final int p_191948_2_) {
        final Minecraft mc = GuiContainer.mc;
        if (Minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
        }
    }
    
    private void drawItemStack(final ItemStack stack, final int x, final int y, final String altText) {
        GlStateManager.translate(0.0f, 0.0f, 32.0f);
        this.zLevel = 200.0f;
        this.itemRender.zLevel = 200.0f;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0f;
        this.itemRender.zLevel = 0.0f;
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    protected abstract void drawGuiContainerBackgroundLayer(final float p0, final int p1, final int p2);
    
    private void drawSlot(final Slot slotIn) {
        final int i = slotIn.xPos;
        final int j = slotIn.yPos;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag2 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        final Minecraft mc = GuiContainer.mc;
        final ItemStack itemstack2 = Minecraft.player.inventory.getItemStack();
        String s = null;
        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        }
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack2.isEmpty()) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slotIn, itemstack2, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack2.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                final int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            }
            else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        if (itemstack.isEmpty() && slotIn.isEnabled()) {
            final String s2 = slotIn.getSlotTexture();
            if (s2 != null) {
                final TextureAtlasSprite textureatlassprite = GuiContainer.mc.getTextureMapBlocks().getAtlasSprite(s2);
                GlStateManager.disableLighting();
                GuiContainer.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag2 = true;
            }
        }
        if (!flag2) {
            if (flag) {
                Gui.drawRect((float)i, (float)j, (float)(i + 16), (float)(j + 16), -2130706433);
            }
            GlStateManager.enableDepth();
            final RenderItem itemRender = this.itemRender;
            final Minecraft mc2 = GuiContainer.mc;
            itemRender.renderItemAndEffectIntoGUI(Minecraft.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }
    
    private void updateDragSplitting() {
        final Minecraft mc = GuiContainer.mc;
        final ItemStack itemstack = Minecraft.player.inventory.getItemStack();
        if (!itemstack.isEmpty() && this.dragSplitting) {
            if (this.dragSplittingLimit == 2) {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            }
            else {
                this.dragSplittingRemnant = itemstack.getCount();
                for (final Slot slot : this.dragSplittingSlots) {
                    final ItemStack itemstack2 = itemstack.copy();
                    final ItemStack itemstack3 = slot.getStack();
                    final int i = itemstack3.isEmpty() ? 0 : itemstack3.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack2, i);
                    final int j = Math.min(itemstack2.getMaxStackSize(), slot.getItemStackLimit(itemstack2));
                    if (itemstack2.getCount() > j) {
                        itemstack2.setCount(j);
                    }
                    this.dragSplittingRemnant -= itemstack2.getCount() - i;
                }
            }
        }
    }
    
    private Slot getSlotAtPosition(final int x, final int y) {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
            final Slot slot = this.inventorySlots.inventorySlots.get(i);
            if (this.isMouseOverSlot(slot, x, y) && slot.isEnabled()) {
                return slot;
            }
        }
        return null;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean flag = mouseButton == GuiContainer.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final long i = Minecraft.getSystemTime();
        this.doubleClick = (this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton);
        this.ignoreMouseUp = false;
        if (mouseButton == 0 || mouseButton == 1 || flag) {
            final int j = this.guiLeft;
            final int k = this.guiTop;
            final boolean flag2 = this.hasClickedOutside(mouseX, mouseY, j, k);
            int l = -1;
            if (slot != null) {
                l = slot.slotNumber;
            }
            if (flag2) {
                l = -999;
            }
            if (GuiContainer.mc.gameSettings.touchscreen && flag2) {
                final Minecraft mc = GuiContainer.mc;
                if (Minecraft.player.inventory.getItemStack().isEmpty()) {
                    GuiContainer.mc.displayGuiScreen(null);
                    return;
                }
            }
            if (l != -1) {
                if (GuiContainer.mc.gameSettings.touchscreen) {
                    if (slot != null && slot.getHasStack()) {
                        this.clickedSlot = slot;
                        this.draggedStack = ItemStack.EMPTY;
                        this.isRightMouseClick = (mouseButton == 1);
                    }
                    else {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.dragSplitting) {
                    final Minecraft mc2 = GuiContainer.mc;
                    if (Minecraft.player.inventory.getItemStack().isEmpty()) {
                        if (mouseButton == GuiContainer.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
                        }
                        else {
                            final boolean flag3 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;
                            if (flag3) {
                                this.shiftClickedSlot = ((slot != null && slot.getHasStack()) ? slot.getStack().copy() : ItemStack.EMPTY);
                                clicktype = ClickType.QUICK_MOVE;
                            }
                            else if (l == -999) {
                                clicktype = ClickType.THROW;
                            }
                            this.handleMouseClick(slot, l, mouseButton, clicktype);
                        }
                        this.ignoreMouseUp = true;
                    }
                    else {
                        this.dragSplitting = true;
                        this.dragSplittingButton = mouseButton;
                        this.dragSplittingSlots.clear();
                        if (mouseButton == 0) {
                            this.dragSplittingLimit = 0;
                        }
                        else if (mouseButton == 1) {
                            this.dragSplittingLimit = 1;
                        }
                        else if (mouseButton == GuiContainer.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }
        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = mouseButton;
    }
    
    protected boolean hasClickedOutside(final int p_193983_1_, final int p_193983_2_, final int p_193983_3_, final int p_193983_4_) {
        return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final Minecraft mc = GuiContainer.mc;
        final ItemStack itemstack = Minecraft.player.inventory.getItemStack();
        if (this.clickedSlot != null && GuiContainer.mc.gameSettings.touchscreen) {
            if (clickedMouseButton == 0 || clickedMouseButton == 1) {
                if (this.draggedStack.isEmpty()) {
                    if (slot != this.clickedSlot && !this.clickedSlot.getStack().isEmpty()) {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                }
                else if (this.draggedStack.getCount() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
                    final long i = Minecraft.getSystemTime();
                    if (this.currentDragTargetSlot == slot) {
                        if (i - this.dragItemDropDelay > 500L) {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.dragItemDropDelay = i + 750L;
                            this.draggedStack.shrink(1);
                        }
                    }
                    else {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        }
        else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);
        int k = -1;
        if (slot != null) {
            k = slot.slotNumber;
        }
        if (flag) {
            k = -999;
        }
        if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, slot)) {
            if (isShiftKeyDown()) {
                if (!this.shiftClickedSlot.isEmpty()) {
                    for (final Slot slot2 : this.inventorySlots.inventorySlots) {
                        if (slot2 != null) {
                            final Slot slot4 = slot2;
                            final Minecraft mc = GuiContainer.mc;
                            if (!slot4.canTakeStack(Minecraft.player) || !slot2.getHasStack() || slot2.inventory != slot.inventory || !Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
                                continue;
                            }
                            this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            else {
                this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
            }
            this.doubleClick = false;
            this.lastClickTime = 0L;
        }
        else {
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
            if (this.clickedSlot != null && GuiContainer.mc.gameSettings.touchscreen) {
                if (state == 0 || state == 1) {
                    if (this.draggedStack.isEmpty() && slot != this.clickedSlot) {
                        this.draggedStack = this.clickedSlot.getStack();
                    }
                    final boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);
                    if (k != -1 && !this.draggedStack.isEmpty() && flag2) {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);
                        final Minecraft mc2 = GuiContainer.mc;
                        if (Minecraft.player.inventory.getItemStack().isEmpty()) {
                            this.returningStack = ItemStack.EMPTY;
                        }
                        else {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                            this.touchUpX = mouseX - i;
                            this.touchUpY = mouseY - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                    }
                    else if (!this.draggedStack.isEmpty()) {
                        this.touchUpX = mouseX - i;
                        this.touchUpY = mouseY - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }
                    this.draggedStack = ItemStack.EMPTY;
                    this.clickedSlot = null;
                }
            }
            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
                this.handleMouseClick(null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                for (final Slot slot3 : this.dragSplittingSlots) {
                    this.handleMouseClick(slot3, slot3.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                }
                this.handleMouseClick(null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }
            else {
                final Minecraft mc3 = GuiContainer.mc;
                if (!Minecraft.player.inventory.getItemStack().isEmpty()) {
                    if (state == GuiContainer.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                        this.handleMouseClick(slot, k, state, ClickType.CLONE);
                    }
                    else {
                        final boolean flag3 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                        if (flag3) {
                            this.shiftClickedSlot = ((slot != null && slot.getHasStack()) ? slot.getStack().copy() : ItemStack.EMPTY);
                        }
                        this.handleMouseClick(slot, k, state, flag3 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                    }
                }
            }
        }
        final Minecraft mc4 = GuiContainer.mc;
        if (Minecraft.player.inventory.getItemStack().isEmpty()) {
            this.lastClickTime = 0L;
        }
        this.dragSplitting = false;
    }
    
    private boolean isMouseOverSlot(final Slot slotIn, final int mouseX, final int mouseY) {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }
    
    protected boolean isPointInRegion(final int rectX, final int rectY, final int rectWidth, final int rectHeight, int pointX, int pointY) {
        final int i = this.guiLeft;
        final int j = this.guiTop;
        pointX -= i;
        pointY -= j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
    
    protected void handleMouseClick(final Slot slotIn, int slotId, final int mouseButton, final ClickType type) {
        if (slotIn != null) {
            slotId = slotIn.slotNumber;
        }
        final PlayerControllerMP playerController = GuiContainer.mc.playerController;
        final int windowId = this.inventorySlots.windowId;
        final int slotId2 = slotId;
        final Minecraft mc = GuiContainer.mc;
        playerController.windowClick(windowId, slotId2, mouseButton, type, Minecraft.player);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == GuiContainer.mc.gameSettings.keyBindInventory.getKeyCode()) {
            final Minecraft mc = GuiContainer.mc;
            Minecraft.player.closeScreen();
        }
        this.checkHotbarKeys(keyCode);
        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            if (keyCode == GuiContainer.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            }
            else if (keyCode == GuiContainer.mc.gameSettings.keyBindDrop.getKeyCode()) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, GuiScreen.isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
    }
    
    protected boolean checkHotbarKeys(final int keyCode) {
        final Minecraft mc = GuiContainer.mc;
        if (Minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
            for (int i = 0; i < 9; ++i) {
                if (keyCode == GuiContainer.mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void onGuiClosed() {
        final Minecraft mc = GuiContainer.mc;
        if (Minecraft.player != null) {
            final Container inventorySlots = this.inventorySlots;
            final Minecraft mc2 = GuiContainer.mc;
            inventorySlots.onContainerClosed(Minecraft.player);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final Minecraft mc = GuiContainer.mc;
        if (Minecraft.player.isEntityAlive()) {
            final Minecraft mc2 = GuiContainer.mc;
            if (!Minecraft.player.isDead) {
                return;
            }
        }
        final Minecraft mc3 = GuiContainer.mc;
        Minecraft.player.closeScreen();
    }
    
    static {
        INVENTORY_BACKGROUND = new ResourceLocation("textures/gui/container/inventory.png");
    }
}
