/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative
extends InventoryEffectRenderer {
    private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private boolean wasClicking;
    private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
    private float currentScroll;
    private CreativeCrafting field_147059_E;
    private GuiTextField searchField;
    private List<Slot> field_147063_B;
    private boolean isScrolling;
    private Slot field_147064_C;
    private boolean field_147057_D;
    private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();

    private boolean needsScrollBars() {
        return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).func_148328_e();
    }

    public GuiContainerCreative(EntityPlayer entityPlayer) {
        super(new ContainerCreative(entityPlayer));
        entityPlayer.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }

    @Override
    protected void updateActivePotionEffects() {
        int n = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != n) {
            this.searchField.xPosition = this.guiLeft + 82;
        }
    }

    @Override
    protected void renderToolTip(ItemStack itemStack, int n, int n2) {
        if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
            Map<Integer, Integer> map;
            List<String> list = itemStack.getTooltip(Minecraft.thePlayer, Minecraft.gameSettings.advancedItemTooltips);
            CreativeTabs creativeTabs = itemStack.getItem().getCreativeTab();
            if (creativeTabs == null && itemStack.getItem() == Items.enchanted_book && (map = EnchantmentHelper.getEnchantments(itemStack)).size() == 1) {
                Enchantment enchantment = Enchantment.getEnchantmentById(map.keySet().iterator().next());
                CreativeTabs[] creativeTabsArray = CreativeTabs.creativeTabArray;
                int n3 = CreativeTabs.creativeTabArray.length;
                int n4 = 0;
                while (n4 < n3) {
                    CreativeTabs creativeTabs2 = creativeTabsArray[n4];
                    if (creativeTabs2.hasRelevantEnchantmentType(enchantment.type)) {
                        creativeTabs = creativeTabs2;
                        break;
                    }
                    ++n4;
                }
            }
            if (creativeTabs != null) {
                list.add(1, (Object)((Object)EnumChatFormatting.BOLD) + (Object)((Object)EnumChatFormatting.BLUE) + I18n.format(creativeTabs.getTranslatedTabLabel(), new Object[0]));
            }
            int n5 = 0;
            while (n5 < list.size()) {
                if (n5 == 0) {
                    list.set(n5, (Object)((Object)itemStack.getRarity().rarityColor) + list.get(n5));
                } else {
                    list.set(n5, (Object)((Object)EnumChatFormatting.GRAY) + list.get(n5));
                }
                ++n5;
            }
            this.drawHoveringText(list, n, n2);
        } else {
            super.renderToolTip(itemStack, n, n2);
        }
    }

    @Override
    public void initGui() {
        if (Minecraft.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents((boolean)true);
            this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
            this.searchField.setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(0xFFFFFF);
            int n = selectedTabIndex;
            selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[n]);
            this.field_147059_E = new CreativeCrafting(this.mc);
            Minecraft.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
        } else {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
        }
    }

    protected boolean func_147049_a(CreativeTabs creativeTabs, int n, int n2) {
        int n3 = creativeTabs.getTabColumn();
        int n4 = 28 * n3;
        int n5 = 0;
        if (n3 == 5) {
            n4 = this.xSize - 28 + 2;
        } else if (n3 > 0) {
            n4 += n3;
        }
        n5 = creativeTabs.isTabInFirstRow() ? (n5 -= 32) : (n5 += this.ySize);
        return n >= n4 && n <= n4 + 28 && n2 >= n5 && n2 <= n5 + 32;
    }

    protected void func_147051_a(CreativeTabs creativeTabs) {
        boolean bl = creativeTabs.getTabIndex() == selectedTabIndex;
        boolean bl2 = creativeTabs.isTabInFirstRow();
        int n = creativeTabs.getTabColumn();
        int n2 = n * 28;
        int n3 = 0;
        int n4 = this.guiLeft + 28 * n;
        int n5 = this.guiTop;
        int n6 = 32;
        if (bl) {
            n3 += 32;
        }
        if (n == 5) {
            n4 = this.guiLeft + this.xSize - 28;
        } else if (n > 0) {
            n4 += n;
        }
        if (bl2) {
            n5 -= 28;
        } else {
            n3 += 64;
            n5 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(n4, n5, n2, n3, 28, n6);
        zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        n5 = n5 + 8 + (bl2 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemStack = creativeTabs.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemStack, n4 += 6, n5);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemStack, n4, n5);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        zLevel = 0.0f;
    }

    @Override
    public void updateScreen() {
        if (!Minecraft.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
        }
        this.updateActivePotionEffects();
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        if (n3 == 0) {
            int n4 = n - this.guiLeft;
            int n5 = n2 - this.guiTop;
            CreativeTabs[] creativeTabsArray = CreativeTabs.creativeTabArray;
            int n6 = CreativeTabs.creativeTabArray.length;
            int n7 = 0;
            while (n7 < n6) {
                CreativeTabs creativeTabs = creativeTabsArray[n7];
                if (this.func_147049_a(creativeTabs, n4, n5)) {
                    return;
                }
                ++n7;
            }
        }
        super.mouseClicked(n, n2, n3);
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        if (n3 == 0) {
            int n4 = n - this.guiLeft;
            int n5 = n2 - this.guiTop;
            CreativeTabs[] creativeTabsArray = CreativeTabs.creativeTabArray;
            int n6 = CreativeTabs.creativeTabArray.length;
            int n7 = 0;
            while (n7 < n6) {
                CreativeTabs creativeTabs = creativeTabsArray[n7];
                if (this.func_147049_a(creativeTabs, n4, n5)) {
                    this.setCurrentCreativeTab(creativeTabs);
                    return;
                }
                ++n7;
            }
        }
        super.mouseReleased(n, n2, n3);
    }

    private void setCurrentCreativeTab(CreativeTabs creativeTabs) {
        int n = selectedTabIndex;
        selectedTabIndex = creativeTabs.getTabIndex();
        ContainerCreative containerCreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        containerCreative.itemList.clear();
        creativeTabs.displayAllReleventItems(containerCreative.itemList);
        if (creativeTabs == CreativeTabs.tabInventory) {
            Container container = Minecraft.thePlayer.inventoryContainer;
            if (this.field_147063_B == null) {
                this.field_147063_B = containerCreative.inventorySlots;
            }
            containerCreative.inventorySlots = Lists.newArrayList();
            int n2 = 0;
            while (n2 < container.inventorySlots.size()) {
                int n3;
                int n4;
                int n5;
                CreativeSlot creativeSlot = new CreativeSlot(container.inventorySlots.get(n2), n2);
                containerCreative.inventorySlots.add(creativeSlot);
                if (n2 >= 5 && n2 < 9) {
                    n5 = n2 - 5;
                    n4 = n5 / 2;
                    n3 = n5 % 2;
                    creativeSlot.xDisplayPosition = 9 + n4 * 54;
                    creativeSlot.yDisplayPosition = 6 + n3 * 27;
                } else if (n2 >= 0 && n2 < 5) {
                    creativeSlot.yDisplayPosition = -2000;
                    creativeSlot.xDisplayPosition = -2000;
                } else if (n2 < container.inventorySlots.size()) {
                    n5 = n2 - 9;
                    n4 = n5 % 9;
                    n3 = n5 / 9;
                    creativeSlot.xDisplayPosition = 9 + n4 * 18;
                    creativeSlot.yDisplayPosition = n2 >= 36 ? 112 : 54 + n3 * 18;
                }
                ++n2;
            }
            this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
            containerCreative.inventorySlots.add(this.field_147064_C);
        } else if (n == CreativeTabs.tabInventory.getTabIndex()) {
            containerCreative.inventorySlots = this.field_147063_B;
            this.field_147063_B = null;
        }
        if (this.searchField != null) {
            if (creativeTabs == CreativeTabs.tabAllSearch) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            } else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }
        this.currentScroll = 0.0f;
        containerCreative.scrollTo(0.0f);
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs creativeTabs, int n, int n2) {
        int n3 = creativeTabs.getTabColumn();
        int n4 = 28 * n3;
        int n5 = 0;
        if (n3 == 5) {
            n4 = this.xSize - 28 + 2;
        } else if (n3 > 0) {
            n4 += n3;
        }
        n5 = creativeTabs.isTabInFirstRow() ? (n5 -= 32) : (n5 += this.ySize);
        if (this.isPointInRegion(n4 + 3, n5 + 3, 23, 27, n, n2)) {
            this.drawCreativeTabHoveringText(I18n.format(creativeTabs.getTranslatedTabLabel(), new Object[0]), n, n2);
            return true;
        }
        return false;
    }

    private void updateCreativeSearch() {
        Object object2;
        ContainerCreative containerCreative = (ContainerCreative)this.inventorySlots;
        containerCreative.itemList.clear();
        for (Object object2 : Item.itemRegistry) {
            if (object2 == null || ((Item)object2).getCreativeTab() == null) continue;
            ((Item)object2).getSubItems((Item)object2, null, containerCreative.itemList);
        }
        Enchantment[] enchantmentArray = Enchantment.enchantmentsBookList;
        int n = Enchantment.enchantmentsBookList.length;
        int n2 = 0;
        while (n2 < n) {
            object2 = enchantmentArray[n2];
            if (object2 != null && ((Enchantment)object2).type != null) {
                Items.enchanted_book.getAll((Enchantment)object2, containerCreative.itemList);
            }
            ++n2;
        }
        object2 = containerCreative.itemList.iterator();
        String string = this.searchField.getText().toLowerCase();
        while (object2.hasNext()) {
            ItemStack itemStack = (ItemStack)object2.next();
            boolean bl = false;
            for (String string2 : itemStack.getTooltip(Minecraft.thePlayer, Minecraft.gameSettings.advancedItemTooltips)) {
                if (!EnumChatFormatting.getTextWithoutFormattingCodes(string2).toLowerCase().contains(string)) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            object2.remove();
        }
        this.currentScroll = 0.0f;
        containerCreative.scrollTo(0.0f);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        boolean bl = Mouse.isButtonDown((int)0);
        int n3 = this.guiLeft;
        int n4 = this.guiTop;
        int n5 = n3 + 175;
        int n6 = n4 + 18;
        int n7 = n5 + 14;
        int n8 = n6 + 112;
        if (!this.wasClicking && bl && n >= n5 && n2 >= n6 && n < n7 && n2 < n8) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!bl) {
            this.isScrolling = false;
        }
        this.wasClicking = bl;
        if (this.isScrolling) {
            this.currentScroll = ((float)(n2 - n6) - 7.5f) / ((float)(n8 - n6) - 15.0f);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(n, n2, f);
        CreativeTabs[] creativeTabsArray = CreativeTabs.creativeTabArray;
        int n9 = CreativeTabs.creativeTabArray.length;
        int n10 = 0;
        while (n10 < n9) {
            CreativeTabs creativeTabs = creativeTabsArray[n10];
            if (this.renderCreativeInventoryHoveringText(creativeTabs, n, n2)) break;
            ++n10;
        }
        if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, n, n2)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), n, n2);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int n = Mouse.getEventDWheel();
        if (n != 0 && this.needsScrollBars()) {
            int n2 = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5;
            if (n > 0) {
                n = 1;
            }
            if (n < 0) {
                n = -1;
            }
            this.currentScroll = (float)((double)this.currentScroll - (double)n / (double)n2);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    @Override
    protected void handleMouseClick(Slot slot, int n, int n2, int n3) {
        this.field_147057_D = true;
        boolean bl = n3 == 1;
        int n4 = n3 = n == -999 && n3 == 0 ? 4 : n3;
        if (slot == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && n3 != 5) {
            InventoryPlayer inventoryPlayer = Minecraft.thePlayer.inventory;
            if (inventoryPlayer.getItemStack() != null) {
                if (n2 == 0) {
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(inventoryPlayer.getItemStack(), true);
                    Minecraft.playerController.sendPacketDropItem(inventoryPlayer.getItemStack());
                    inventoryPlayer.setItemStack(null);
                }
                if (n2 == 1) {
                    ItemStack itemStack = inventoryPlayer.getItemStack().splitStack(1);
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemStack, true);
                    Minecraft.playerController.sendPacketDropItem(itemStack);
                    if (inventoryPlayer.getItemStack().stackSize == 0) {
                        inventoryPlayer.setItemStack(null);
                    }
                }
            }
        } else if (slot == this.field_147064_C && bl) {
            int n5 = 0;
            while (n5 < Minecraft.thePlayer.inventoryContainer.getInventory().size()) {
                Minecraft.playerController.sendSlotPacket(null, n5);
                ++n5;
            }
        } else if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
            if (slot == this.field_147064_C) {
                Minecraft.thePlayer.inventory.setItemStack(null);
            } else if (n3 == 4 && slot != null && slot.getHasStack()) {
                ItemStack itemStack = slot.decrStackSize(n2 == 0 ? 1 : slot.getStack().getMaxStackSize());
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemStack, true);
                Minecraft.playerController.sendPacketDropItem(itemStack);
            } else if (n3 == 4 && Minecraft.thePlayer.inventory.getItemStack() != null) {
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(Minecraft.thePlayer.inventory.getItemStack(), true);
                Minecraft.playerController.sendPacketDropItem(Minecraft.thePlayer.inventory.getItemStack());
                Minecraft.thePlayer.inventory.setItemStack(null);
            } else {
                Minecraft.thePlayer.inventoryContainer.slotClick(slot == null ? n : ((CreativeSlot)((CreativeSlot)slot)).slot.slotNumber, n2, n3, Minecraft.thePlayer);
                Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
            }
        } else if (n3 != 5 && slot.inventory == field_147060_v) {
            InventoryPlayer inventoryPlayer = Minecraft.thePlayer.inventory;
            ItemStack itemStack = inventoryPlayer.getItemStack();
            ItemStack itemStack2 = slot.getStack();
            if (n3 == 2) {
                if (itemStack2 != null && n2 >= 0 && n2 < 9) {
                    ItemStack itemStack3 = itemStack2.copy();
                    itemStack3.stackSize = itemStack3.getMaxStackSize();
                    Minecraft.thePlayer.inventory.setInventorySlotContents(n2, itemStack3);
                    Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
                }
                return;
            }
            if (n3 == 3) {
                if (inventoryPlayer.getItemStack() == null && slot.getHasStack()) {
                    ItemStack itemStack4 = slot.getStack().copy();
                    itemStack4.stackSize = itemStack4.getMaxStackSize();
                    inventoryPlayer.setItemStack(itemStack4);
                }
                return;
            }
            if (n3 == 4) {
                if (itemStack2 != null) {
                    ItemStack itemStack5 = itemStack2.copy();
                    itemStack5.stackSize = n2 == 0 ? 1 : itemStack5.getMaxStackSize();
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemStack5, true);
                    Minecraft.playerController.sendPacketDropItem(itemStack5);
                }
                return;
            }
            if (itemStack != null && itemStack2 != null && itemStack.isItemEqual(itemStack2)) {
                if (n2 == 0) {
                    if (bl) {
                        itemStack.stackSize = itemStack.getMaxStackSize();
                    } else if (itemStack.stackSize < itemStack.getMaxStackSize()) {
                        ++itemStack.stackSize;
                    }
                } else if (itemStack.stackSize <= 1) {
                    inventoryPlayer.setItemStack(null);
                } else {
                    --itemStack.stackSize;
                }
            } else if (itemStack2 != null && itemStack == null) {
                inventoryPlayer.setItemStack(ItemStack.copyItemStack(itemStack2));
                itemStack = inventoryPlayer.getItemStack();
                if (bl) {
                    itemStack.stackSize = itemStack.getMaxStackSize();
                }
            } else {
                inventoryPlayer.setItemStack(null);
            }
        } else {
            this.inventorySlots.slotClick(slot == null ? n : slot.slotNumber, n2, n3, Minecraft.thePlayer);
            if (Container.getDragEvent(n2) == 2) {
                int n6 = 0;
                while (n6 < 9) {
                    Minecraft.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + n6).getStack(), 36 + n6);
                    ++n6;
                }
            } else if (slot != null) {
                ItemStack itemStack = this.inventorySlots.getSlot(slot.slotNumber).getStack();
                Minecraft.playerController.sendSlotPacket(itemStack, slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (Minecraft.thePlayer != null && Minecraft.thePlayer.inventory != null) {
            Minecraft.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
        }
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 0) {
            this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
        }
        if (guiButton.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int n, int n2) {
        CreativeTabs creativeTabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        if (creativeTabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(creativeTabs.getTranslatedTabLabel(), new Object[0]), 8.0, 6.0, 0x404040);
        }
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
            if (GameSettings.isKeyDown(Minecraft.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            } else {
                super.keyTyped(c, n);
            }
        } else {
            if (this.field_147057_D) {
                this.field_147057_D = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(n)) {
                if (this.searchField.textboxKeyTyped(c, n)) {
                    this.updateCreativeSearch();
                } else {
                    super.keyTyped(c, n);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int n, int n2) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativeTabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        CreativeTabs[] creativeTabsArray = CreativeTabs.creativeTabArray;
        int n3 = CreativeTabs.creativeTabArray.length;
        int n4 = 0;
        while (n4 < n3) {
            CreativeTabs creativeTabs2 = creativeTabsArray[n4];
            this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
            if (creativeTabs2.getTabIndex() != selectedTabIndex) {
                this.func_147051_a(creativeTabs2);
            }
            ++n4;
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativeTabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int n5 = this.guiLeft + 175;
        n4 = this.guiTop + 18;
        n3 = n4 + 112;
        this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
        if (creativeTabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(n5, n4 + (int)((float)(n3 - n4 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.func_147051_a(creativeTabs);
        if (creativeTabs == CreativeTabs.tabInventory) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - n, this.guiTop + 45 - 30 - n2, Minecraft.thePlayer);
        }
    }

    static class ContainerCreative
    extends Container {
        public List<ItemStack> itemList = Lists.newArrayList();

        @Override
        public boolean canDragIntoSlot(Slot slot) {
            return slot.inventory instanceof InventoryPlayer || slot.yDisplayPosition > 90 && slot.xDisplayPosition <= 162;
        }

        public boolean func_148328_e() {
            return this.itemList.size() > 45;
        }

        @Override
        protected void retrySlotClick(int n, int n2, boolean bl, EntityPlayer entityPlayer) {
        }

        public ContainerCreative(EntityPlayer entityPlayer) {
            InventoryPlayer inventoryPlayer = entityPlayer.inventory;
            int n = 0;
            while (n < 5) {
                int n2 = 0;
                while (n2 < 9) {
                    this.addSlotToContainer(new Slot(field_147060_v, n * 9 + n2, 9 + n2 * 18, 18 + n * 18));
                    ++n2;
                }
                ++n;
            }
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(inventoryPlayer, n, 9 + n * 18, 112));
                ++n;
            }
            this.scrollTo(0.0f);
        }

        @Override
        public boolean canInteractWith(EntityPlayer entityPlayer) {
            return true;
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
            Slot slot;
            if (n >= this.inventorySlots.size() - 9 && n < this.inventorySlots.size() && (slot = (Slot)this.inventorySlots.get(n)) != null && slot.getHasStack()) {
                slot.putStack(null);
            }
            return null;
        }

        @Override
        public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
            return slot.yDisplayPosition > 90;
        }

        public void scrollTo(float f) {
            int n = (this.itemList.size() + 9 - 1) / 9 - 5;
            int n2 = (int)((double)(f * (float)n) + 0.5);
            if (n2 < 0) {
                n2 = 0;
            }
            int n3 = 0;
            while (n3 < 5) {
                int n4 = 0;
                while (n4 < 9) {
                    int n5 = n4 + (n3 + n2) * 9;
                    if (n5 >= 0 && n5 < this.itemList.size()) {
                        field_147060_v.setInventorySlotContents(n4 + n3 * 9, this.itemList.get(n5));
                    } else {
                        field_147060_v.setInventorySlotContents(n4 + n3 * 9, null);
                    }
                    ++n4;
                }
                ++n3;
            }
        }
    }

    class CreativeSlot
    extends Slot {
        private final Slot slot;

        @Override
        public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
            this.slot.onPickupFromSlot(entityPlayer, itemStack);
        }

        @Override
        public int getItemStackLimit(ItemStack itemStack) {
            return this.slot.getItemStackLimit(itemStack);
        }

        @Override
        public void putStack(ItemStack itemStack) {
            this.slot.putStack(itemStack);
        }

        public CreativeSlot(Slot slot, int n) {
            super(slot.inventory, n, 0, 0);
            this.slot = slot;
        }

        @Override
        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }

        @Override
        public ItemStack decrStackSize(int n) {
            return this.slot.decrStackSize(n);
        }

        @Override
        public boolean getHasStack() {
            return this.slot.getHasStack();
        }

        @Override
        public boolean isHere(IInventory iInventory, int n) {
            return this.slot.isHere(iInventory, n);
        }

        @Override
        public ItemStack getStack() {
            return this.slot.getStack();
        }

        @Override
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }

        @Override
        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return this.slot.isItemValid(itemStack);
        }
    }
}

