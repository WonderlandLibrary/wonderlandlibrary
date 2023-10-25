/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.ContainerBlur;
import org.celestial.client.helpers.render.RenderHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative
extends InventoryEffectRenderer {
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final InventoryBasic basicInventory = new InventoryBasic("tmp", true, 45);
    private static int selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getTabIndex();
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> originalSlots;
    private Slot destroyItemSlot;
    private boolean clearSearch;
    private CreativeCrafting listener;
    private int blur;

    public GuiContainerCreative(EntityPlayer player) {
        super(new ContainerCreative(player));
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }

    @Override
    public void updateScreen() {
        if (!this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
        }
    }

    @Override
    public void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type) {
        this.clearSearch = true;
        boolean flag = type == ClickType.QUICK_MOVE;
        ClickType clickType = type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;
        if (slotIn == null && selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && type != ClickType.QUICK_CRAFT) {
            InventoryPlayer inventoryplayer1 = this.mc.player.inventory;
            if (!inventoryplayer1.getItemStack().isEmpty()) {
                if (mouseButton == 0) {
                    this.mc.player.dropItem(inventoryplayer1.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(ItemStack.field_190927_a);
                }
                if (mouseButton == 1) {
                    ItemStack itemstack6 = inventoryplayer1.getItemStack().splitStack(1);
                    this.mc.player.dropItem(itemstack6, true);
                    this.mc.playerController.sendPacketDropItem(itemstack6);
                }
            }
        } else {
            if (slotIn != null && !slotIn.canTakeStack(this.mc.player)) {
                return;
            }
            if (slotIn == this.destroyItemSlot && flag) {
                for (int j = 0; j < this.mc.player.inventoryContainer.getInventory().size(); ++j) {
                    this.mc.playerController.sendSlotPacket(ItemStack.field_190927_a, j);
                }
            } else if (selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex()) {
                if (slotIn == this.destroyItemSlot) {
                    this.mc.player.inventory.setItemStack(ItemStack.field_190927_a);
                } else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack()) {
                    ItemStack itemstack = slotIn.decrStackSize(mouseButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
                    ItemStack itemstack1 = slotIn.getStack();
                    this.mc.player.dropItem(itemstack, true);
                    this.mc.playerController.sendPacketDropItem(itemstack);
                    this.mc.playerController.sendSlotPacket(itemstack1, ((CreativeSlot)((CreativeSlot)slotIn)).slot.slotNumber);
                } else if (type == ClickType.THROW && !this.mc.player.inventory.getItemStack().isEmpty()) {
                    this.mc.player.dropItem(this.mc.player.inventory.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(this.mc.player.inventory.getItemStack());
                    this.mc.player.inventory.setItemStack(ItemStack.field_190927_a);
                } else {
                    this.mc.player.inventoryContainer.slotClick(slotIn == null ? slotId : ((CreativeSlot)((CreativeSlot)slotIn)).slot.slotNumber, mouseButton, type, this.mc.player);
                    this.mc.player.inventoryContainer.detectAndSendChanges();
                }
            } else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == basicInventory) {
                InventoryPlayer inventoryplayer = this.mc.player.inventory;
                ItemStack itemstack5 = inventoryplayer.getItemStack();
                ItemStack itemstack7 = slotIn.getStack();
                if (type == ClickType.SWAP) {
                    if (!itemstack7.isEmpty() && mouseButton >= 0 && mouseButton < 9) {
                        ItemStack itemstack10 = itemstack7.copy();
                        itemstack10.func_190920_e(itemstack10.getMaxStackSize());
                        this.mc.player.inventory.setInventorySlotContents(mouseButton, itemstack10);
                        this.mc.player.inventoryContainer.detectAndSendChanges();
                    }
                    return;
                }
                if (type == ClickType.CLONE) {
                    if (inventoryplayer.getItemStack().isEmpty() && slotIn.getHasStack()) {
                        ItemStack itemstack9 = slotIn.getStack().copy();
                        itemstack9.func_190920_e(itemstack9.getMaxStackSize());
                        inventoryplayer.setItemStack(itemstack9);
                    }
                    return;
                }
                if (type == ClickType.THROW) {
                    if (!itemstack7.isEmpty()) {
                        ItemStack itemstack8 = itemstack7.copy();
                        itemstack8.func_190920_e(mouseButton == 0 ? 1 : itemstack8.getMaxStackSize());
                        this.mc.player.dropItem(itemstack8, true);
                        this.mc.playerController.sendPacketDropItem(itemstack8);
                    }
                    return;
                }
                if (!itemstack5.isEmpty() && !itemstack7.isEmpty() && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7)) {
                    if (mouseButton == 0) {
                        if (flag) {
                            itemstack5.func_190920_e(itemstack5.getMaxStackSize());
                        } else if (itemstack5.getCount() < itemstack5.getMaxStackSize()) {
                            itemstack5.func_190917_f(1);
                        }
                    } else {
                        itemstack5.func_190918_g(1);
                    }
                } else if (!itemstack7.isEmpty() && itemstack5.isEmpty()) {
                    inventoryplayer.setItemStack(itemstack7.copy());
                    itemstack5 = inventoryplayer.getItemStack();
                    if (flag) {
                        itemstack5.func_190920_e(itemstack5.getMaxStackSize());
                    }
                } else if (mouseButton == 0) {
                    inventoryplayer.setItemStack(ItemStack.field_190927_a);
                } else {
                    inventoryplayer.getItemStack().func_190918_g(1);
                }
            } else if (this.inventorySlots != null) {
                ItemStack itemstack3 = slotIn == null ? ItemStack.field_190927_a : this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.mc.player);
                if (Container.getDragEvent(mouseButton) == 2) {
                    for (int k = 0; k < 9; ++k) {
                        this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + k).getStack(), 36 + k);
                    }
                } else if (slotIn != null) {
                    ItemStack itemstack4 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                    this.mc.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    int i = 45 + mouseButton;
                    if (type == ClickType.SWAP) {
                        this.mc.playerController.sendSlotPacket(itemstack3, i - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    } else if (type == ClickType.THROW && !itemstack3.isEmpty()) {
                        ItemStack itemstack2 = itemstack3.copy();
                        itemstack2.func_190920_e(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
                        this.mc.player.dropItem(itemstack2, true);
                        this.mc.playerController.sendPacketDropItem(itemstack2);
                    }
                    this.mc.player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }

    @Override
    protected void updateActivePotionEffects() {
        int i = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != i) {
            this.searchField.xPosition = this.guiLeft + 82;
        }
    }

    @Override
    public void initGui() {
        if (this.mc.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRendererObj.FONT_HEIGHT);
            this.searchField.setMaxStringLength(50);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(0xFFFFFF);
            int i = selectedTabIndex;
            selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
            this.listener = new CreativeCrafting(this.mc);
            this.mc.player.inventoryContainer.addListener(this.listener);
        } else {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
        }
        this.blur = 0;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.mc.player != null && this.mc.player.inventory != null) {
            this.mc.player.inventoryContainer.removeListener(this.listener);
        }
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (selectedTabIndex != CreativeTabs.SEARCH.getTabIndex()) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        } else {
            if (this.clearSearch) {
                this.clearSearch = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                } else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    private void updateCreativeSearch() {
        ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        guicontainercreative$containercreative.itemList.clear();
        if (this.searchField.getText().isEmpty()) {
            for (Item item : Item.REGISTRY) {
                item.getSubItems(CreativeTabs.SEARCH, guicontainercreative$containercreative.itemList);
            }
        } else {
            guicontainercreative$containercreative.itemList.addAll(this.mc.func_193987_a(SearchTreeManager.field_194011_a).func_194038_a(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }
        this.currentScroll = 0.0f;
        guicontainercreative$containercreative.scrollTo(0.0f);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
        if (creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8.0f, 6.0f, 0x404040);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;
            for (CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                if (!this.isMouseOverTab(creativetabs, i, j)) continue;
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;
            for (CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
                if (!this.isMouseOverTab(creativetabs, i, j)) continue;
                this.setCurrentCreativeTab(creativetabs);
                return;
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    private boolean needsScrollBars() {
        return selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).canScroll();
    }

    private void setCurrentCreativeTab(CreativeTabs tab) {
        int i = selectedTabIndex;
        selectedTabIndex = tab.getTabIndex();
        ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        if (tab == CreativeTabs.field_192395_m) {
            for (int j = 0; j < 9; ++j) {
                HotbarSnapshot hotbarsnapshot = this.mc.field_191950_u.func_192563_a(j);
                if (hotbarsnapshot.isEmpty()) {
                    for (int k = 0; k < 9; ++k) {
                        if (k == j) {
                            ItemStack itemstack = new ItemStack(Items.PAPER);
                            itemstack.func_190925_c("CustomCreativeLock");
                            String s = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[j].getKeyCode());
                            String s1 = GameSettings.getKeyDisplayString(this.mc.gameSettings.field_193629_ap.getKeyCode());
                            itemstack.setStackDisplayName(new TextComponentTranslation("inventory.hotbarInfo", s1, s).getUnformattedText());
                            guicontainercreative$containercreative.itemList.add(itemstack);
                            continue;
                        }
                        guicontainercreative$containercreative.itemList.add(ItemStack.field_190927_a);
                    }
                    continue;
                }
                guicontainercreative$containercreative.itemList.addAll(hotbarsnapshot);
            }
        } else if (tab != CreativeTabs.SEARCH) {
            tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
        }
        if (tab == CreativeTabs.INVENTORY) {
            Container container = this.mc.player.inventoryContainer;
            if (this.originalSlots == null) {
                this.originalSlots = guicontainercreative$containercreative.inventorySlots;
            }
            guicontainercreative$containercreative.inventorySlots = Lists.newArrayList();
            for (int l = 0; l < container.inventorySlots.size(); ++l) {
                CreativeSlot slot = new CreativeSlot(container.inventorySlots.get(l), l);
                guicontainercreative$containercreative.inventorySlots.add(slot);
                if (l >= 5 && l < 9) {
                    int j1 = l - 5;
                    int l1 = j1 / 2;
                    int j2 = j1 % 2;
                    slot.xDisplayPosition = 54 + l1 * 54;
                    slot.yDisplayPosition = 6 + j2 * 27;
                    continue;
                }
                if (l >= 0 && l < 5) {
                    slot.xDisplayPosition = -2000;
                    slot.yDisplayPosition = -2000;
                    continue;
                }
                if (l == 45) {
                    slot.xDisplayPosition = 35;
                    slot.yDisplayPosition = 20;
                    continue;
                }
                if (l >= container.inventorySlots.size()) continue;
                int i1 = l - 9;
                int k1 = i1 % 9;
                int i2 = i1 / 9;
                slot.xDisplayPosition = 9 + k1 * 18;
                slot.yDisplayPosition = l >= 36 ? 112 : 54 + i2 * 18;
            }
            this.destroyItemSlot = new Slot(basicInventory, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
        } else if (i == CreativeTabs.INVENTORY.getTabIndex()) {
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
            } else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }
        this.currentScroll = 0.0f;
        guicontainercreative$containercreative.scrollTo(0.0f);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars()) {
            int j = (((ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
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
        boolean flag = Mouse.isButtonDown(0);
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;
        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!flag) {
            this.isScrolling = false;
        }
        this.wasClicking = flag;
        if (this.isScrolling) {
            this.currentScroll = ((float)(mouseY - l) - 7.5f) / ((float)(j1 - l) - 15.0f);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) break;
        }
        if (this.destroyItemSlot != null && selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex() && this.isPointInRegion(this.destroyItemSlot.xDisplayPosition, this.destroyItemSlot.yDisplayPosition, 16, 16, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        this.func_191948_b(mouseX, mouseY);
    }

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y) {
        if (selectedTabIndex == CreativeTabs.SEARCH.getTabIndex()) {
            Map<Enchantment, Integer> map;
            List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();
            if (creativetabs == null && stack.getItem() == Items.ENCHANTED_BOOK && (map = EnchantmentHelper.getEnchantments(stack)).size() == 1) {
                Enchantment enchantment = map.keySet().iterator().next();
                for (CreativeTabs creativetabs1 : CreativeTabs.CREATIVE_TAB_ARRAY) {
                    if (!creativetabs1.hasRelevantEnchantmentType(enchantment.type)) continue;
                    creativetabs = creativetabs1;
                    break;
                }
            }
            if (creativetabs != null) {
                list.add(1, "" + (Object)((Object)TextFormatting.BOLD) + (Object)((Object)TextFormatting.BLUE) + I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, (Object)((Object)stack.getRarity().rarityColor) + list.get(i));
                    continue;
                }
                list.set(i, (Object)((Object)TextFormatting.GRAY) + list.get(i));
            }
            this.drawHoveringText(list, x, y);
        } else {
            super.renderToolTip(stack, x, y);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
        for (CreativeTabs creativetabs1 : CreativeTabs.CREATIVE_TAB_ARRAY) {
            this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
            if (creativetabs1.getTabIndex() == selectedTabIndex) continue;
            this.drawTab(creativetabs1);
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int i = this.guiLeft + 175;
        int j = this.guiTop + 18;
        int k = j + 112;
        this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
        if (creativetabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.drawTab(creativetabs);
        if (creativetabs == CreativeTabs.INVENTORY) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, this.guiLeft + 88 - mouseX, this.guiTop + 45 - 30 - mouseY, this.mc.player);
        }
    }

    protected boolean isMouseOverTab(CreativeTabs tab, int mouseX, int mouseY) {
        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.func_192394_m()) {
            j = this.xSize - 28 * (6 - i) + 2;
        } else if (i > 0) {
            j += i;
        }
        k = tab.isTabInFirstRow() ? (k -= 32) : (k += this.ySize);
        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mouseX, int mouseY) {
        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (tab.func_192394_m()) {
            j = this.xSize - 28 * (6 - i) + 2;
        } else if (i > 0) {
            j += i;
        }
        k = tab.isTabInFirstRow() ? (k -= 32) : (k += this.ySize);
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format(tab.getTranslatedTabLabel(), new Object[0]), mouseX, mouseY);
            return true;
        }
        return false;
    }

    protected void drawTab(CreativeTabs tab) {
        boolean flag = tab.getTabIndex() == selectedTabIndex;
        boolean flag1 = tab.isTabInFirstRow();
        int i = tab.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;
        if (flag) {
            k += 32;
        }
        if (tab.func_192394_m()) {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        } else if (i > 0) {
            l += i;
        }
        if (flag1) {
            i1 -= 28;
        } else {
            k += 64;
            i1 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(l, i1, j, k, 28, 32);
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemstack = tab.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l += 6, i1);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
        }
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public static void func_192044_a(Minecraft p_192044_0_, int p_192044_1_, boolean p_192044_2_, boolean p_192044_3_) {
        EntityPlayerSP entityplayersp = p_192044_0_.player;
        CreativeSettings creativesettings = p_192044_0_.field_191950_u;
        HotbarSnapshot hotbarsnapshot = creativesettings.func_192563_a(p_192044_1_);
        if (p_192044_2_) {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
                ItemStack itemstack = ((ItemStack)hotbarsnapshot.get(i)).copy();
                entityplayersp.inventory.setInventorySlotContents(i, itemstack);
                p_192044_0_.playerController.sendSlotPacket(itemstack, 36 + i);
            }
            entityplayersp.inventoryContainer.detectAndSendChanges();
        } else if (p_192044_3_) {
            for (int j = 0; j < InventoryPlayer.getHotbarSize(); ++j) {
                hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
            }
            String s = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.keyBindsHotbar[p_192044_1_].getKeyCode());
            String s1 = GameSettings.getKeyDisplayString(p_192044_0_.gameSettings.field_193630_aq.getKeyCode());
            p_192044_0_.ingameGUI.setRecordPlaying(new TextComponentTranslation("inventory.hotbarSaved", s1, s), false);
            creativesettings.func_192564_b();
        }
    }

    static class LockedSlot
    extends Slot {
        public LockedSlot(IInventory p_i47453_1_, int p_i47453_2_, int p_i47453_3_, int p_i47453_4_) {
            super(p_i47453_1_, p_i47453_2_, p_i47453_3_, p_i47453_4_);
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            if (super.canTakeStack(playerIn) && this.getHasStack()) {
                return this.getStack().getSubCompound("CustomCreativeLock") == null;
            }
            return !this.getHasStack();
        }
    }

    class CreativeSlot
    extends Slot {
        private final Slot slot;

        public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_) {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.slot = p_i46313_2_;
        }

        @Override
        public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_) {
            this.slot.func_190901_a(p_190901_1_, p_190901_2_);
            return p_190901_2_;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
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
        public void putStack(ItemStack stack) {
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
        public int getItemStackLimit(ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }

        @Override
        @Nullable
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            return this.slot.decrStackSize(amount);
        }

        @Override
        public boolean isHere(IInventory inv, int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }

        @Override
        public boolean canBeHovered() {
            return this.slot.canBeHovered();
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            return this.slot.canTakeStack(playerIn);
        }
    }

    public static class ContainerCreative
    extends Container {
        public NonNullList<ItemStack> itemList = NonNullList.func_191196_a();

        public ContainerCreative(EntityPlayer player) {
            InventoryPlayer inventoryplayer = player.inventory;
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new LockedSlot(basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
            }
            this.scrollTo(0.0f);
        }

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return true;
        }

        public void scrollTo(float p_148329_1_) {
            int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)((double)(p_148329_1_ * (float)i) + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    int i1 = l + (k + j) * 9;
                    if (i1 >= 0 && i1 < this.itemList.size()) {
                        basicInventory.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
                        continue;
                    }
                    basicInventory.setInventorySlotContents(l + k * 9, ItemStack.field_190927_a);
                }
            }
        }

        public boolean canScroll() {
            return this.itemList.size() > 45;
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
            Slot slot;
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size() && (slot = (Slot)this.inventorySlots.get(index)) != null && slot.getHasStack()) {
                slot.putStack(ItemStack.field_190927_a);
            }
            return ItemStack.field_190927_a;
        }

        @Override
        public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
            return slotIn.yDisplayPosition > 90;
        }

        @Override
        public boolean canDragIntoSlot(Slot slotIn) {
            return slotIn.inventory instanceof InventoryPlayer || slotIn.yDisplayPosition > 90 && slotIn.xDisplayPosition <= 162;
        }
    }
}

