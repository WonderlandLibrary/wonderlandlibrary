// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.util.NonNullList;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Map;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.inventory.IInventory;
import com.google.common.collect.Lists;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.init.Items;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.item.Item;
import java.io.IOException;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.IContainerListener;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.ClickType;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.InventoryEffectRenderer;

public class GuiContainerCreative extends InventoryEffectRenderer
{
    private static final ResourceLocation CREATIVE_INVENTORY_TABS;
    private static final InventoryBasic basicInventory;
    private static int selectedTabIndex;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> originalSlots;
    private Slot destroyItemSlot;
    private boolean clearSearch;
    private CreativeCrafting listener;
    
    public GuiContainerCreative(final EntityPlayer player) {
        super(new ContainerCreative(player));
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }
    
    @Override
    public void updateScreen() {
        if (!GuiContainerCreative.mc.playerController.isInCreativeMode()) {
            final Minecraft mc = GuiContainerCreative.mc;
            final Minecraft mc2 = GuiContainerCreative.mc;
            mc.displayGuiScreen(new GuiInventory(Minecraft.player));
        }
    }
    
    @Override
    protected void handleMouseClick(@Nullable final Slot slotIn, final int slotId, final int mouseButton, ClickType type) {
        this.clearSearch = true;
        final boolean flag = type == ClickType.QUICK_MOVE;
        type = ((slotId == -999 && type == ClickType.PICKUP) ? ClickType.THROW : type);
        if (slotIn == null && GuiContainerCreative.selectedTabIndex != CreativeTabs.INVENTORY.getIndex() && type != ClickType.QUICK_CRAFT) {
            final Minecraft mc = GuiContainerCreative.mc;
            final InventoryPlayer inventoryplayer1 = Minecraft.player.inventory;
            if (!inventoryplayer1.getItemStack().isEmpty()) {
                if (mouseButton == 0) {
                    final Minecraft mc2 = GuiContainerCreative.mc;
                    Minecraft.player.dropItem(inventoryplayer1.getItemStack(), true);
                    GuiContainerCreative.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(ItemStack.EMPTY);
                }
                if (mouseButton == 1) {
                    final ItemStack itemstack6 = inventoryplayer1.getItemStack().splitStack(1);
                    final Minecraft mc3 = GuiContainerCreative.mc;
                    Minecraft.player.dropItem(itemstack6, true);
                    GuiContainerCreative.mc.playerController.sendPacketDropItem(itemstack6);
                }
            }
        }
        else {
            if (slotIn != null) {
                final Minecraft mc4 = GuiContainerCreative.mc;
                if (!slotIn.canTakeStack(Minecraft.player)) {
                    return;
                }
            }
            if (slotIn == this.destroyItemSlot && flag) {
                int j = 0;
                while (true) {
                    final int n = j;
                    final Minecraft mc5 = GuiContainerCreative.mc;
                    if (n >= Minecraft.player.inventoryContainer.getInventory().size()) {
                        break;
                    }
                    GuiContainerCreative.mc.playerController.sendSlotPacket(ItemStack.EMPTY, j);
                    ++j;
                }
            }
            else if (GuiContainerCreative.selectedTabIndex == CreativeTabs.INVENTORY.getIndex()) {
                if (slotIn == this.destroyItemSlot) {
                    final Minecraft mc6 = GuiContainerCreative.mc;
                    Minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
                }
                else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
                    final ItemStack itemstack7 = slotIn.decrStackSize((mouseButton == 0) ? 1 : slotIn.getStack().getMaxStackSize());
                    final ItemStack itemstack8 = slotIn.getStack();
                    final Minecraft mc7 = GuiContainerCreative.mc;
                    Minecraft.player.dropItem(itemstack7, true);
                    GuiContainerCreative.mc.playerController.sendPacketDropItem(itemstack7);
                    GuiContainerCreative.mc.playerController.sendSlotPacket(itemstack8, ((CreativeSlot)slotIn).slot.slotNumber);
                }
                else {
                    if (type == ClickType.THROW) {
                        final Minecraft mc8 = GuiContainerCreative.mc;
                        if (!Minecraft.player.inventory.getItemStack().isEmpty()) {
                            final Minecraft mc9 = GuiContainerCreative.mc;
                            final EntityPlayerSP player = Minecraft.player;
                            final Minecraft mc10 = GuiContainerCreative.mc;
                            player.dropItem(Minecraft.player.inventory.getItemStack(), true);
                            final PlayerControllerMP playerController = GuiContainerCreative.mc.playerController;
                            final Minecraft mc11 = GuiContainerCreative.mc;
                            playerController.sendPacketDropItem(Minecraft.player.inventory.getItemStack());
                            final Minecraft mc12 = GuiContainerCreative.mc;
                            Minecraft.player.inventory.setItemStack(ItemStack.EMPTY);
                            return;
                        }
                    }
                    final Minecraft mc13 = GuiContainerCreative.mc;
                    final Container inventoryContainer = Minecraft.player.inventoryContainer;
                    final int slotId2 = (slotIn == null) ? slotId : ((CreativeSlot)slotIn).slot.slotNumber;
                    final ClickType clickTypeIn = type;
                    final Minecraft mc14 = GuiContainerCreative.mc;
                    inventoryContainer.slotClick(slotId2, mouseButton, clickTypeIn, Minecraft.player);
                    final Minecraft mc15 = GuiContainerCreative.mc;
                    Minecraft.player.inventoryContainer.detectAndSendChanges();
                }
            }
            else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == GuiContainerCreative.basicInventory) {
                final Minecraft mc16 = GuiContainerCreative.mc;
                final InventoryPlayer inventoryplayer2 = Minecraft.player.inventory;
                ItemStack itemstack9 = inventoryplayer2.getItemStack();
                final ItemStack itemstack10 = slotIn.getStack();
                if (type == ClickType.SWAP) {
                    if (!itemstack10.isEmpty() && mouseButton >= 0 && mouseButton < 9) {
                        final ItemStack itemstack11 = itemstack10.copy();
                        itemstack11.setCount(itemstack11.getMaxStackSize());
                        final Minecraft mc17 = GuiContainerCreative.mc;
                        Minecraft.player.inventory.setInventorySlotContents(mouseButton, itemstack11);
                        final Minecraft mc18 = GuiContainerCreative.mc;
                        Minecraft.player.inventoryContainer.detectAndSendChanges();
                    }
                    return;
                }
                if (type == ClickType.CLONE) {
                    if (inventoryplayer2.getItemStack().isEmpty() && slotIn.getHasStack()) {
                        final ItemStack itemstack12 = slotIn.getStack().copy();
                        itemstack12.setCount(itemstack12.getMaxStackSize());
                        inventoryplayer2.setItemStack(itemstack12);
                    }
                    return;
                }
                if (type == ClickType.THROW) {
                    if (!itemstack10.isEmpty()) {
                        final ItemStack itemstack13 = itemstack10.copy();
                        itemstack13.setCount((mouseButton == 0) ? 1 : itemstack13.getMaxStackSize());
                        final Minecraft mc19 = GuiContainerCreative.mc;
                        Minecraft.player.dropItem(itemstack13, true);
                        GuiContainerCreative.mc.playerController.sendPacketDropItem(itemstack13);
                    }
                    return;
                }
                if (!itemstack9.isEmpty() && !itemstack10.isEmpty() && itemstack9.isItemEqual(itemstack10) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemstack9.setCount(itemstack9.getMaxStackSize());
                        }
                        else if (itemstack9.getCount() < itemstack9.getMaxStackSize()) {
                            itemstack9.grow(1);
                        }
                    }
                    else {
                        itemstack9.shrink(1);
                    }
                }
                else if (!itemstack10.isEmpty() && itemstack9.isEmpty()) {
                    inventoryplayer2.setItemStack(itemstack10.copy());
                    itemstack9 = inventoryplayer2.getItemStack();
                    if (flag) {
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                    }
                }
                else if (mouseButton == 0) {
                    inventoryplayer2.setItemStack(ItemStack.EMPTY);
                }
                else {
                    inventoryplayer2.getItemStack().shrink(1);
                }
            }
            else if (this.inventorySlots != null) {
                final ItemStack itemstack14 = (slotIn == null) ? ItemStack.EMPTY : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                final Container inventorySlots = this.inventorySlots;
                final int slotId3 = (slotIn == null) ? slotId : slotIn.slotNumber;
                final ClickType clickTypeIn2 = type;
                final Minecraft mc20 = GuiContainerCreative.mc;
                inventorySlots.slotClick(slotId3, mouseButton, clickTypeIn2, Minecraft.player);
                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; ++k) {
                        GuiContainerCreative.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + k).getStack(), 36 + k);
                    }
                }
                else if (slotIn != null) {
                    final ItemStack itemstack15 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                    GuiContainerCreative.mc.playerController.sendSlotPacket(itemstack15, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    final int i = 45 + mouseButton;
                    if (type == ClickType.SWAP) {
                        GuiContainerCreative.mc.playerController.sendSlotPacket(itemstack14, i - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                    else if (type == ClickType.THROW && !itemstack14.isEmpty()) {
                        final ItemStack itemstack16 = itemstack14.copy();
                        itemstack16.setCount((mouseButton == 0) ? 1 : itemstack16.getMaxStackSize());
                        final Minecraft mc21 = GuiContainerCreative.mc;
                        Minecraft.player.dropItem(itemstack16, true);
                        GuiContainerCreative.mc.playerController.sendPacketDropItem(itemstack16);
                    }
                    final Minecraft mc22 = GuiContainerCreative.mc;
                    Minecraft.player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }
    
    @Override
    protected void updateActivePotionEffects() {
        final int i = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != i) {
            this.searchField.x = this.guiLeft + 82;
        }
    }
    
    @Override
    public void initGui() {
        if (GuiContainerCreative.mc.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            (this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRenderer.FONT_HEIGHT)).setMaxStringLength(50);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            final int i = GuiContainerCreative.selectedTabIndex;
            GuiContainerCreative.selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
            this.listener = new CreativeCrafting(GuiContainerCreative.mc);
            final Minecraft mc = GuiContainerCreative.mc;
            Minecraft.player.inventoryContainer.addListener(this.listener);
        }
        else {
            final Minecraft mc2 = GuiContainerCreative.mc;
            final Minecraft mc3 = GuiContainerCreative.mc;
            mc2.displayGuiScreen(new GuiInventory(Minecraft.player));
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        final Minecraft mc = GuiContainerCreative.mc;
        if (Minecraft.player != null) {
            final Minecraft mc2 = GuiContainerCreative.mc;
            if (Minecraft.player.inventory != null) {
                final Minecraft mc3 = GuiContainerCreative.mc;
                Minecraft.player.inventoryContainer.removeListener(this.listener);
            }
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (GuiContainerCreative.selectedTabIndex != CreativeTabs.SEARCH.getIndex()) {
            if (GameSettings.isKeyDown(GuiContainerCreative.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            if (this.clearSearch) {
                this.clearSearch = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                }
                else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }
    
    private void updateCreativeSearch() {
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        guicontainercreative$containercreative.itemList.clear();
        if (this.searchField.getText().isEmpty()) {
            for (final Item item : Item.REGISTRY) {
                item.getSubItems(CreativeTabs.SEARCH, guicontainercreative$containercreative.itemList);
            }
        }
        else {
            guicontainercreative$containercreative.itemList.addAll((Collection<?>)GuiContainerCreative.mc.getSearchTree(SearchTreeManager.ITEMS).search(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        if (creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRenderer.drawString(I18n.format(creativetabs.getTranslationKey(), new Object[0]), 8, 6, 4210752);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            for (final CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                if (this.isMouseOverTab(creativetabs, i, j)) {
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (state == 0) {
            final int i = mouseX - this.guiLeft;
            final int j = mouseY - this.guiTop;
            for (final CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                if (this.isMouseOverTab(creativetabs, i, j)) {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private boolean needsScrollBars() {
        return GuiContainerCreative.selectedTabIndex != CreativeTabs.INVENTORY.getIndex() && CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex].hasScrollbar() && ((ContainerCreative)this.inventorySlots).canScroll();
    }
    
    private void setCurrentCreativeTab(final CreativeTabs tab) {
        final int i = GuiContainerCreative.selectedTabIndex;
        GuiContainerCreative.selectedTabIndex = tab.getIndex();
        final ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        if (tab == CreativeTabs.HOTBAR) {
            for (int j = 0; j < 9; ++j) {
                final HotbarSnapshot hotbarsnapshot = GuiContainerCreative.mc.creativeSettings.getHotbarSnapshot(j);
                if (hotbarsnapshot.isEmpty()) {
                    for (int k = 0; k < 9; ++k) {
                        if (k == j) {
                            final ItemStack itemstack = new ItemStack(Items.PAPER);
                            itemstack.getOrCreateSubCompound("CustomCreativeLock");
                            final String s = GameSettings.getKeyDisplayString(GuiContainerCreative.mc.gameSettings.keyBindsHotbar[j].getKeyCode());
                            final String s2 = GameSettings.getKeyDisplayString(GuiContainerCreative.mc.gameSettings.keyBindSaveToolbar.getKeyCode());
                            itemstack.setStackDisplayName(new TextComponentTranslation("inventory.hotbarInfo", new Object[] { s2, s }).getUnformattedText());
                            guicontainercreative$containercreative.itemList.add(itemstack);
                        }
                        else {
                            guicontainercreative$containercreative.itemList.add(ItemStack.EMPTY);
                        }
                    }
                }
                else {
                    guicontainercreative$containercreative.itemList.addAll((Collection<?>)hotbarsnapshot);
                }
            }
        }
        else if (tab != CreativeTabs.SEARCH) {
            tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
        }
        if (tab == CreativeTabs.INVENTORY) {
            final Minecraft mc = GuiContainerCreative.mc;
            final Container container = Minecraft.player.inventoryContainer;
            if (this.originalSlots == null) {
                this.originalSlots = guicontainercreative$containercreative.inventorySlots;
            }
            guicontainercreative$containercreative.inventorySlots = (List<Slot>)Lists.newArrayList();
            for (int l = 0; l < container.inventorySlots.size(); ++l) {
                final Slot slot = new CreativeSlot(container.inventorySlots.get(l), l);
                guicontainercreative$containercreative.inventorySlots.add(slot);
                if (l >= 5 && l < 9) {
                    final int j2 = l - 5;
                    final int l2 = j2 / 2;
                    final int j3 = j2 % 2;
                    slot.xPos = 54 + l2 * 54;
                    slot.yPos = 6 + j3 * 27;
                }
                else if (l >= 0 && l < 5) {
                    slot.xPos = -2000;
                    slot.yPos = -2000;
                }
                else if (l == 45) {
                    slot.xPos = 35;
                    slot.yPos = 20;
                }
                else if (l < container.inventorySlots.size()) {
                    final int i2 = l - 9;
                    final int k2 = i2 % 9;
                    final int i3 = i2 / 9;
                    slot.xPos = 9 + k2 * 18;
                    if (l >= 36) {
                        slot.yPos = 112;
                    }
                    else {
                        slot.yPos = 54 + i3 * 18;
                    }
                }
            }
            this.destroyItemSlot = new Slot(GuiContainerCreative.basicInventory, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
        }
        else if (i == CreativeTabs.INVENTORY.getIndex()) {
            guicontainercreative$containercreative.inventorySlots = this.originalSlots;
            this.originalSlots = null;
        }
        if (this.searchField != null) {
            if (tab == CreativeTabs.SEARCH) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            }
            else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }
        guicontainercreative$containercreative.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars()) {
            final int j = (((ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll -= (float)(i / (double)j);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final boolean flag = Mouse.isButtonDown(0);
        final int i = this.guiLeft;
        final int j = this.guiTop;
        final int k = i + 175;
        final int l = j + 18;
        final int i2 = k + 14;
        final int j2 = l + 112;
        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i2 && mouseY < j2) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!flag) {
            this.isScrolling = false;
        }
        this.wasClicking = flag;
        if (this.isScrolling) {
            this.currentScroll = (mouseY - l - 7.5f) / (j2 - l - 15.0f);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (final CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) {
                break;
            }
        }
        if (this.destroyItemSlot != null && GuiContainerCreative.selectedTabIndex == CreativeTabs.INVENTORY.getIndex() && this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, mouseX, mouseY)) {
            this.drawHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void renderToolTip(final ItemStack stack, final int x, final int y) {
        if (GuiContainerCreative.selectedTabIndex == CreativeTabs.SEARCH.getIndex()) {
            final Minecraft mc = GuiContainerCreative.mc;
            final List<String> list = stack.getTooltip(Minecraft.player, GuiContainerCreative.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();
            if (creativetabs == null && stack.getItem() == Items.ENCHANTED_BOOK) {
                final Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                if (map.size() == 1) {
                    final Enchantment enchantment = map.keySet().iterator().next();
                    for (final CreativeTabs creativetabs2 : CreativeTabs.CREATIVE_TAB_ARRAY) {
                        if (creativetabs2.hasRelevantEnchantmentType(enchantment.type)) {
                            creativetabs = creativetabs2;
                            break;
                        }
                    }
                }
            }
            if (creativetabs != null) {
                list.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(creativetabs.getTranslationKey(), new Object[0]));
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, stack.getRarity().color + list.get(i));
                }
                else {
                    list.set(i, TextFormatting.GRAY + list.get(i));
                }
            }
            this.drawHoveringText(list, x, y);
        }
        else {
            super.renderToolTip(stack, x, y);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        final CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[GuiContainerCreative.selectedTabIndex];
        for (final CreativeTabs creativetabs2 : CreativeTabs.CREATIVE_TAB_ARRAY) {
            GuiContainerCreative.mc.getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
            if (creativetabs2.getIndex() != GuiContainerCreative.selectedTabIndex) {
                this.drawTab(creativetabs2);
            }
        }
        GuiContainerCreative.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i = this.guiLeft + 175;
        final int j = this.guiTop + 18;
        final int k = j + 112;
        GuiContainerCreative.mc.getTextureManager().bindTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
        if (creativetabs.hasScrollbar()) {
            this.drawTexturedModalRect(i, j + (int)((k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.drawTab(creativetabs);
        if (creativetabs == CreativeTabs.INVENTORY) {
            final int posX = this.guiLeft + 88;
            final int posY = this.guiTop + 45;
            final int scale = 20;
            final float mouseX2 = (float)(this.guiLeft + 88 - mouseX);
            final float mouseY2 = (float)(this.guiTop + 45 - 30 - mouseY);
            final Minecraft mc = GuiContainerCreative.mc;
            GuiInventory.drawEntityOnScreen(posX, posY, scale, mouseX2, mouseY2, Minecraft.player);
        }
    }
    
    protected boolean isMouseOverTab(final CreativeTabs tab, final int mouseX, final int mouseY) {
        final int i = tab.getColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (tab.isOnTopRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }
    
    protected boolean renderCreativeInventoryHoveringText(final CreativeTabs tab, final int mouseX, final int mouseY) {
        final int i = tab.getColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.isAlignedRight()) {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0) {
            j += i;
        }
        if (tab.isOnTopRow()) {
            k -= 32;
        }
        else {
            k += this.ySize;
        }
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
            this.drawHoveringText(I18n.format(tab.getTranslationKey(), new Object[0]), mouseX, mouseY);
            return true;
        }
        return false;
    }
    
    protected void drawTab(final CreativeTabs tab) {
        final boolean flag = tab.getIndex() == GuiContainerCreative.selectedTabIndex;
        final boolean flag2 = tab.isOnTopRow();
        final int i = tab.getColumn();
        final int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i2 = this.guiTop;
        final int j2 = 32;
        if (flag) {
            k += 32;
        }
        if (tab.isAlignedRight()) {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        }
        else if (i > 0) {
            l += i;
        }
        if (flag2) {
            i2 -= 28;
        }
        else {
            k += 64;
            i2 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(l, i2, j, k, 28, 32);
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        l += 6;
        i2 = i2 + 8 + (flag2 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        final ItemStack itemstack = tab.getIcon();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i2);
        this.itemRender.renderItemOverlays(this.fontRenderer, itemstack, l, i2);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            final Minecraft mc = GuiContainerCreative.mc;
            final Minecraft mc2 = GuiContainerCreative.mc;
            mc.displayGuiScreen(new GuiStats(this, Minecraft.player.getStatFileWriter()));
        }
    }
    
    public int getSelectedTabIndex() {
        return GuiContainerCreative.selectedTabIndex;
    }
    
    public static void handleHotbarSnapshots(final Minecraft client, final int index, final boolean load, final boolean save) {
        final EntityPlayerSP entityplayersp = Minecraft.player;
        final CreativeSettings creativesettings = client.creativeSettings;
        final HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(index);
        if (load) {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
                final ItemStack itemstack = hotbarsnapshot.get(i).copy();
                entityplayersp.inventory.setInventorySlotContents(i, itemstack);
                client.playerController.sendSlotPacket(itemstack, 36 + i);
            }
            entityplayersp.inventoryContainer.detectAndSendChanges();
        }
        else if (save) {
            for (int j = 0; j < InventoryPlayer.getHotbarSize(); ++j) {
                hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
            }
            final String s = GameSettings.getKeyDisplayString(client.gameSettings.keyBindsHotbar[index].getKeyCode());
            final String s2 = GameSettings.getKeyDisplayString(client.gameSettings.keyBindLoadToolbar.getKeyCode());
            client.ingameGUI.setOverlayMessage(new TextComponentTranslation("inventory.hotbarSaved", new Object[] { s2, s }), false);
            creativesettings.write();
        }
    }
    
    static {
        CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
        basicInventory = new InventoryBasic("tmp", true, 45);
        GuiContainerCreative.selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getIndex();
    }
    
    public static class ContainerCreative extends Container
    {
        public NonNullList<ItemStack> itemList;
        
        public ContainerCreative(final EntityPlayer player) {
            this.itemList = NonNullList.create();
            final InventoryPlayer inventoryplayer = player.inventory;
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new LockedSlot(GuiContainerCreative.basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
            }
            this.scrollTo(0.0f);
        }
        
        @Override
        public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
        }
        
        public void scrollTo(final float pos) {
            final int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)(pos * i + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    final int i2 = l + (k + j) * 9;
                    if (i2 >= 0 && i2 < this.itemList.size()) {
                        GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, this.itemList.get(i2));
                    }
                    else {
                        GuiContainerCreative.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
                    }
                }
            }
        }
        
        public boolean canScroll() {
            return this.itemList.size() > 45;
        }
        
        @Override
        public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
                final Slot slot = this.inventorySlots.get(index);
                if (slot != null && slot.getHasStack()) {
                    slot.putStack(ItemStack.EMPTY);
                }
            }
            return ItemStack.EMPTY;
        }
        
        @Override
        public boolean canMergeSlot(final ItemStack stack, final Slot slotIn) {
            return slotIn.yPos > 90;
        }
        
        @Override
        public boolean canDragIntoSlot(final Slot slotIn) {
            return slotIn.inventory instanceof InventoryPlayer || (slotIn.yPos > 90 && slotIn.xPos <= 162);
        }
    }
    
    class CreativeSlot extends Slot
    {
        private final Slot slot;
        
        public CreativeSlot(final Slot p_i46313_2_, final int index) {
            super(p_i46313_2_.inventory, index, 0, 0);
            this.slot = p_i46313_2_;
        }
        
        @Override
        public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
            this.slot.onTake(thePlayer, stack);
            return stack;
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return this.slot.isItemValid(stack);
        }
        
        @Override
        public ItemStack getStack() {
            return this.slot.getStack();
        }
        
        @Override
        public boolean getHasStack() {
            return this.slot.getHasStack();
        }
        
        @Override
        public void putStack(final ItemStack stack) {
            this.slot.putStack(stack);
        }
        
        @Override
        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }
        
        @Override
        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }
        
        @Override
        public int getItemStackLimit(final ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }
        
        @Nullable
        @Override
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }
        
        @Override
        public ItemStack decrStackSize(final int amount) {
            return this.slot.decrStackSize(amount);
        }
        
        @Override
        public boolean isHere(final IInventory inv, final int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }
        
        @Override
        public boolean isEnabled() {
            return this.slot.isEnabled();
        }
        
        @Override
        public boolean canTakeStack(final EntityPlayer playerIn) {
            return this.slot.canTakeStack(playerIn);
        }
    }
    
    static class LockedSlot extends Slot
    {
        public LockedSlot(final IInventory p_i47453_1_, final int p_i47453_2_, final int p_i47453_3_, final int p_i47453_4_) {
            super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
        }
        
        @Override
        public boolean canTakeStack(final EntityPlayer playerIn) {
            if (super.canTakeStack(playerIn) && this.getHasStack()) {
                return this.getStack().getSubCompound("CustomCreativeLock") == null;
            }
            return !this.getHasStack();
        }
    }
}
