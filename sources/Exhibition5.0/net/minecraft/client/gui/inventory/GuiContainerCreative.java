// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import net.minecraft.inventory.IInventory;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import java.io.IOException;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.ICrafting;
import org.lwjgl.input.Keyboard;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.gui.GuiScreen;
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
    private static final ResourceLocation creativeInventoryTabs;
    private static InventoryBasic field_147060_v;
    private static int selectedTabIndex;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List field_147063_B;
    private Slot field_147064_C;
    private boolean field_147057_D;
    private CreativeCrafting field_147059_E;
    private static final String __OBFID = "CL_00000752";
    
    public GuiContainerCreative(final EntityPlayer p_i1088_1_) {
        super(new ContainerCreative(p_i1088_1_));
        p_i1088_1_.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }
    
    @Override
    public void updateScreen() {
        if (!this.mc.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }
        this.func_175378_g();
    }
    
    @Override
    protected void handleMouseClick(final Slot slotIn, final int slotId, final int clickedButton, int clickType) {
        this.field_147057_D = true;
        final boolean var5 = clickType == 1;
        clickType = ((slotId == -999 && clickType == 0) ? 4 : clickType);
        if (slotIn == null && GuiContainerCreative.selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && clickType != 5) {
            final InventoryPlayer var6 = this.mc.thePlayer.inventory;
            if (var6.getItemStack() != null) {
                if (clickedButton == 0) {
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(var6.getItemStack(), true);
                    this.mc.playerController.sendPacketDropItem(var6.getItemStack());
                    var6.setItemStack(null);
                }
                if (clickedButton == 1) {
                    final ItemStack var7 = var6.getItemStack().splitStack(1);
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(var7, true);
                    this.mc.playerController.sendPacketDropItem(var7);
                    if (var6.getItemStack().stackSize == 0) {
                        var6.setItemStack(null);
                    }
                }
            }
        }
        else if (slotIn == this.field_147064_C && var5) {
            for (int var8 = 0; var8 < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++var8) {
                this.mc.playerController.sendSlotPacket(null, var8);
            }
        }
        else if (GuiContainerCreative.selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
            if (slotIn == this.field_147064_C) {
                this.mc.thePlayer.inventory.setItemStack(null);
            }
            else if (clickType == 4 && slotIn != null && slotIn.getHasStack()) {
                final ItemStack var9 = slotIn.decrStackSize((clickedButton == 0) ? 1 : slotIn.getStack().getMaxStackSize());
                this.mc.thePlayer.dropPlayerItemWithRandomChoice(var9, true);
                this.mc.playerController.sendPacketDropItem(var9);
            }
            else if (clickType == 4 && this.mc.thePlayer.inventory.getItemStack() != null) {
                this.mc.thePlayer.dropPlayerItemWithRandomChoice(this.mc.thePlayer.inventory.getItemStack(), true);
                this.mc.playerController.sendPacketDropItem(this.mc.thePlayer.inventory.getItemStack());
                this.mc.thePlayer.inventory.setItemStack(null);
            }
            else {
                this.mc.thePlayer.inventoryContainer.slotClick((slotIn == null) ? slotId : ((CreativeSlot)slotIn).field_148332_b.slotNumber, clickedButton, clickType, this.mc.thePlayer);
                this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
            }
        }
        else if (clickType != 5 && slotIn.inventory == GuiContainerCreative.field_147060_v) {
            final InventoryPlayer var6 = this.mc.thePlayer.inventory;
            ItemStack var7 = var6.getItemStack();
            final ItemStack var10 = slotIn.getStack();
            if (clickType == 2) {
                if (var10 != null && clickedButton >= 0 && clickedButton < 9) {
                    final ItemStack var11 = var10.copy();
                    var11.stackSize = var11.getMaxStackSize();
                    this.mc.thePlayer.inventory.setInventorySlotContents(clickedButton, var11);
                    this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
                }
                return;
            }
            if (clickType == 3) {
                if (var6.getItemStack() == null && slotIn.getHasStack()) {
                    final ItemStack var11 = slotIn.getStack().copy();
                    var11.stackSize = var11.getMaxStackSize();
                    var6.setItemStack(var11);
                }
                return;
            }
            if (clickType == 4) {
                if (var10 != null) {
                    final ItemStack var11 = var10.copy();
                    var11.stackSize = ((clickedButton == 0) ? 1 : var11.getMaxStackSize());
                    this.mc.thePlayer.dropPlayerItemWithRandomChoice(var11, true);
                    this.mc.playerController.sendPacketDropItem(var11);
                }
                return;
            }
            if (var7 != null && var10 != null && var7.isItemEqual(var10)) {
                if (clickedButton == 0) {
                    if (var5) {
                        var7.stackSize = var7.getMaxStackSize();
                    }
                    else if (var7.stackSize < var7.getMaxStackSize()) {
                        final ItemStack itemStack = var7;
                        ++itemStack.stackSize;
                    }
                }
                else if (var7.stackSize <= 1) {
                    var6.setItemStack(null);
                }
                else {
                    final ItemStack itemStack2 = var7;
                    --itemStack2.stackSize;
                }
            }
            else if (var10 != null && var7 == null) {
                var6.setItemStack(ItemStack.copyItemStack(var10));
                var7 = var6.getItemStack();
                if (var5) {
                    var7.stackSize = var7.getMaxStackSize();
                }
            }
            else {
                var6.setItemStack(null);
            }
        }
        else {
            this.inventorySlots.slotClick((slotIn == null) ? slotId : slotIn.slotNumber, clickedButton, clickType, this.mc.thePlayer);
            if (Container.getDragEvent(clickedButton) == 2) {
                for (int var8 = 0; var8 < 9; ++var8) {
                    this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var8).getStack(), 36 + var8);
                }
            }
            else if (slotIn != null) {
                final ItemStack var9 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
                this.mc.playerController.sendSlotPacket(var9, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
            }
        }
    }
    
    @Override
    public void initGui() {
        if (this.mc.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            (this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT)).setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            final int var1 = GuiContainerCreative.selectedTabIndex;
            GuiContainerCreative.selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
            this.field_147059_E = new CreativeCrafting(this.mc);
            this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
        }
        else {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
            this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (GuiContainerCreative.selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            }
            else {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            if (this.field_147057_D) {
                this.field_147057_D = false;
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
        final ContainerCreative var1 = (ContainerCreative)this.inventorySlots;
        var1.itemList.clear();
        for (final Item var3 : Item.itemRegistry) {
            if (var3 != null && var3.getCreativeTab() != null) {
                var3.getSubItems(var3, null, var1.itemList);
            }
        }
        for (final Enchantment var7 : Enchantment.enchantmentsList) {
            if (var7 != null && var7.type != null) {
                Items.enchanted_book.func_92113_a(var7, var1.itemList);
            }
        }
        final Iterator var2 = var1.itemList.iterator();
        final String var8 = this.searchField.getText().toLowerCase();
        while (var2.hasNext()) {
            final ItemStack var9 = var2.next();
            boolean var10 = false;
            for (final String var12 : var9.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips)) {
                if (!EnumChatFormatting.getTextWithoutFormattingCodes(var12).toLowerCase().contains(var8)) {
                    continue;
                }
                var10 = true;
                break;
            }
            if (!var10) {
                var2.remove();
            }
        }
        var1.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final CreativeTabs var3 = CreativeTabs.creativeTabArray[GuiContainerCreative.selectedTabIndex];
        if (var3.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(var3.getTranslatedTabLabel(), new Object[0]), 8.0f, 6.0f, 4210752);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int var4 = mouseX - this.guiLeft;
            final int var5 = mouseY - this.guiTop;
            for (final CreativeTabs var9 : CreativeTabs.creativeTabArray) {
                if (this.func_147049_a(var9, var4, var5)) {
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (state == 0) {
            final int var4 = mouseX - this.guiLeft;
            final int var5 = mouseY - this.guiTop;
            for (final CreativeTabs var9 : CreativeTabs.creativeTabArray) {
                if (this.func_147049_a(var9, var4, var5)) {
                    this.setCurrentCreativeTab(var9);
                    return;
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private boolean needsScrollBars() {
        return GuiContainerCreative.selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[GuiContainerCreative.selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).func_148328_e();
    }
    
    private void setCurrentCreativeTab(final CreativeTabs p_147050_1_) {
        final int var2 = GuiContainerCreative.selectedTabIndex;
        GuiContainerCreative.selectedTabIndex = p_147050_1_.getTabIndex();
        final ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        var3.itemList.clear();
        p_147050_1_.displayAllReleventItems(var3.itemList);
        if (p_147050_1_ == CreativeTabs.tabInventory) {
            final Container var4 = this.mc.thePlayer.inventoryContainer;
            if (this.field_147063_B == null) {
                this.field_147063_B = var3.inventorySlots;
            }
            var3.inventorySlots = Lists.newArrayList();
            for (int var5 = 0; var5 < var4.inventorySlots.size(); ++var5) {
                final CreativeSlot var6 = new CreativeSlot(var4.inventorySlots.get(var5), var5);
                var3.inventorySlots.add(var6);
                if (var5 >= 5 && var5 < 9) {
                    final int var7 = var5 - 5;
                    final int var8 = var7 / 2;
                    final int var9 = var7 % 2;
                    var6.xDisplayPosition = 9 + var8 * 54;
                    var6.yDisplayPosition = 6 + var9 * 27;
                }
                else if (var5 >= 0 && var5 < 5) {
                    var6.yDisplayPosition = -2000;
                    var6.xDisplayPosition = -2000;
                }
                else if (var5 < var4.inventorySlots.size()) {
                    final int var7 = var5 - 9;
                    final int var8 = var7 % 9;
                    final int var9 = var7 / 9;
                    var6.xDisplayPosition = 9 + var8 * 18;
                    if (var5 >= 36) {
                        var6.yDisplayPosition = 112;
                    }
                    else {
                        var6.yDisplayPosition = 54 + var9 * 18;
                    }
                }
            }
            this.field_147064_C = new Slot(GuiContainerCreative.field_147060_v, 0, 173, 112);
            var3.inventorySlots.add(this.field_147064_C);
        }
        else if (var2 == CreativeTabs.tabInventory.getTabIndex()) {
            var3.inventorySlots = this.field_147063_B;
            this.field_147063_B = null;
        }
        if (this.searchField != null) {
            if (p_147050_1_ == CreativeTabs.tabAllSearch) {
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
        var3.scrollTo(this.currentScroll = 0.0f);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();
        if (var1 != 0 && this.needsScrollBars()) {
            final int var2 = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;
            if (var1 > 0) {
                var1 = 1;
            }
            if (var1 < 0) {
                var1 = -1;
            }
            this.currentScroll -= var1 / var2;
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final boolean var4 = Mouse.isButtonDown(0);
        final int var5 = this.guiLeft;
        final int var6 = this.guiTop;
        final int var7 = var5 + 175;
        final int var8 = var6 + 18;
        final int var9 = var7 + 14;
        final int var10 = var8 + 112;
        if (!this.wasClicking && var4 && mouseX >= var7 && mouseY >= var8 && mouseX < var9 && mouseY < var10) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!var4) {
            this.isScrolling = false;
        }
        this.wasClicking = var4;
        if (this.isScrolling) {
            this.currentScroll = (mouseY - var8 - 7.5f) / (var10 - var8 - 15.0f);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (final CreativeTabs var14 : CreativeTabs.creativeTabArray) {
            if (this.renderCreativeInventoryHoveringText(var14, mouseX, mouseY)) {
                break;
            }
        }
        if (this.field_147064_C != null && GuiContainerCreative.selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
    }
    
    @Override
    protected void renderToolTip(final ItemStack itemIn, final int x, final int y) {
        if (GuiContainerCreative.selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
            final List var4 = itemIn.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs var5 = itemIn.getItem().getCreativeTab();
            if (var5 == null && itemIn.getItem() == Items.enchanted_book) {
                final Map var6 = EnchantmentHelper.getEnchantments(itemIn);
                if (var6.size() == 1) {
                    final Enchantment var7 = Enchantment.func_180306_c(var6.keySet().iterator().next());
                    for (final CreativeTabs var11 : CreativeTabs.creativeTabArray) {
                        if (var11.hasRelevantEnchantmentType(var7.type)) {
                            var5 = var11;
                            break;
                        }
                    }
                }
            }
            if (var5 != null) {
                var4.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(var5.getTranslatedTabLabel(), new Object[0]));
            }
            for (int var12 = 0; var12 < var4.size(); ++var12) {
                if (var12 == 0) {
                    var4.set(var12, itemIn.getRarity().rarityColor + var4.get(var12));
                }
                else {
                    var4.set(var12, EnumChatFormatting.GRAY + var4.get(var12));
                }
            }
            this.drawHoveringText(var4, x, y);
        }
        else {
            super.renderToolTip(itemIn, x, y);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        final CreativeTabs var4 = CreativeTabs.creativeTabArray[GuiContainerCreative.selectedTabIndex];
        for (final CreativeTabs var8 : CreativeTabs.creativeTabArray) {
            this.mc.getTextureManager().bindTexture(GuiContainerCreative.creativeInventoryTabs);
            if (var8.getTabIndex() != GuiContainerCreative.selectedTabIndex) {
                this.func_147051_a(var8);
            }
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + var4.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int var9 = this.guiLeft + 175;
        final int var6 = this.guiTop + 18;
        final int var7 = var6 + 112;
        this.mc.getTextureManager().bindTexture(GuiContainerCreative.creativeInventoryTabs);
        if (var4.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(var9, var6 + (int)((var7 - var6 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.func_147051_a(var4);
        if (var4 == CreativeTabs.tabInventory) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - mouseX, this.guiTop + 45 - 30 - mouseY, this.mc.thePlayer);
        }
    }
    
    protected boolean func_147049_a(final CreativeTabs p_147049_1_, final int p_147049_2_, final int p_147049_3_) {
        final int var4 = p_147049_1_.getTabColumn();
        int var5 = 28 * var4;
        final byte var6 = 0;
        if (var4 == 5) {
            var5 = this.xSize - 28 + 2;
        }
        else if (var4 > 0) {
            var5 += var4;
        }
        int var7;
        if (p_147049_1_.isTabInFirstRow()) {
            var7 = var6 - 32;
        }
        else {
            var7 = var6 + this.ySize;
        }
        return p_147049_2_ >= var5 && p_147049_2_ <= var5 + 28 && p_147049_3_ >= var7 && p_147049_3_ <= var7 + 32;
    }
    
    protected boolean renderCreativeInventoryHoveringText(final CreativeTabs p_147052_1_, final int p_147052_2_, final int p_147052_3_) {
        final int var4 = p_147052_1_.getTabColumn();
        int var5 = 28 * var4;
        final byte var6 = 0;
        if (var4 == 5) {
            var5 = this.xSize - 28 + 2;
        }
        else if (var4 > 0) {
            var5 += var4;
        }
        int var7;
        if (p_147052_1_.isTabInFirstRow()) {
            var7 = var6 - 32;
        }
        else {
            var7 = var6 + this.ySize;
        }
        if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, p_147052_2_, p_147052_3_)) {
            this.drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
            return true;
        }
        return false;
    }
    
    protected void func_147051_a(final CreativeTabs p_147051_1_) {
        final boolean var2 = p_147051_1_.getTabIndex() == GuiContainerCreative.selectedTabIndex;
        final boolean var3 = p_147051_1_.isTabInFirstRow();
        final int var4 = p_147051_1_.getTabColumn();
        final int var5 = var4 * 28;
        int var6 = 0;
        int var7 = this.guiLeft + 28 * var4;
        int var8 = this.guiTop;
        final byte var9 = 32;
        if (var2) {
            var6 += 32;
        }
        if (var4 == 5) {
            var7 = this.guiLeft + this.xSize - 28;
        }
        else if (var4 > 0) {
            var7 += var4;
        }
        if (var3) {
            var8 -= 28;
        }
        else {
            var6 += 64;
            var8 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
        this.zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        var7 += 6;
        var8 += 8 + (var3 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        final ItemStack var10 = p_147051_1_.getIconItemStack();
        this.itemRender.func_180450_b(var10, var7, var8);
        this.itemRender.renderItemOverlays(this.fontRendererObj, var10, var7, var8);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        this.zLevel = 0.0f;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
    }
    
    public int func_147056_g() {
        return GuiContainerCreative.selectedTabIndex;
    }
    
    static {
        creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
        GuiContainerCreative.field_147060_v = new InventoryBasic("tmp", true, 45);
        GuiContainerCreative.selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
    }
    
    static class ContainerCreative extends Container
    {
        public List itemList;
        private static final String __OBFID = "CL_00000753";
        
        public ContainerCreative(final EntityPlayer p_i1086_1_) {
            this.itemList = Lists.newArrayList();
            final InventoryPlayer var2 = p_i1086_1_.inventory;
            for (int var3 = 0; var3 < 5; ++var3) {
                for (int var4 = 0; var4 < 9; ++var4) {
                    this.addSlotToContainer(new Slot(GuiContainerCreative.field_147060_v, var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
                }
            }
            for (int var3 = 0; var3 < 9; ++var3) {
                this.addSlotToContainer(new Slot(var2, var3, 9 + var3 * 18, 112));
            }
            this.scrollTo(0.0f);
        }
        
        @Override
        public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
        }
        
        public void scrollTo(final float p_148329_1_) {
            final int var2 = (this.itemList.size() + 8) / 9 - 5;
            int var3 = (int)(p_148329_1_ * var2 + 0.5);
            if (var3 < 0) {
                var3 = 0;
            }
            for (int var4 = 0; var4 < 5; ++var4) {
                for (int var5 = 0; var5 < 9; ++var5) {
                    final int var6 = var5 + (var4 + var3) * 9;
                    if (var6 >= 0 && var6 < this.itemList.size()) {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(var5 + var4 * 9, this.itemList.get(var6));
                    }
                    else {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(var5 + var4 * 9, null);
                    }
                }
            }
        }
        
        public boolean func_148328_e() {
            return this.itemList.size() > 45;
        }
        
        @Override
        protected void retrySlotClick(final int p_75133_1_, final int p_75133_2_, final boolean p_75133_3_, final EntityPlayer p_75133_4_) {
        }
        
        @Override
        public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
                final Slot var3 = this.inventorySlots.get(index);
                if (var3 != null && var3.getHasStack()) {
                    var3.putStack(null);
                }
            }
            return null;
        }
        
        @Override
        public boolean func_94530_a(final ItemStack p_94530_1_, final Slot p_94530_2_) {
            return p_94530_2_.yDisplayPosition > 90;
        }
        
        @Override
        public boolean canDragIntoSlot(final Slot p_94531_1_) {
            return p_94531_1_.inventory instanceof InventoryPlayer || (p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162);
        }
    }
    
    class CreativeSlot extends Slot
    {
        private final Slot field_148332_b;
        private static final String __OBFID = "CL_00000754";
        
        public CreativeSlot(final Slot p_i46313_2_, final int p_i46313_3_) {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.field_148332_b = p_i46313_2_;
        }
        
        @Override
        public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
            this.field_148332_b.onPickupFromSlot(playerIn, stack);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return this.field_148332_b.isItemValid(stack);
        }
        
        @Override
        public ItemStack getStack() {
            return this.field_148332_b.getStack();
        }
        
        @Override
        public boolean getHasStack() {
            return this.field_148332_b.getHasStack();
        }
        
        @Override
        public void putStack(final ItemStack p_75215_1_) {
            this.field_148332_b.putStack(p_75215_1_);
        }
        
        @Override
        public void onSlotChanged() {
            this.field_148332_b.onSlotChanged();
        }
        
        @Override
        public int getSlotStackLimit() {
            return this.field_148332_b.getSlotStackLimit();
        }
        
        @Override
        public int func_178170_b(final ItemStack p_178170_1_) {
            return this.field_148332_b.func_178170_b(p_178170_1_);
        }
        
        @Override
        public String func_178171_c() {
            return this.field_148332_b.func_178171_c();
        }
        
        @Override
        public ItemStack decrStackSize(final int p_75209_1_) {
            return this.field_148332_b.decrStackSize(p_75209_1_);
        }
        
        @Override
        public boolean isHere(final IInventory p_75217_1_, final int p_75217_2_) {
            return this.field_148332_b.isHere(p_75217_1_, p_75217_2_);
        }
    }
}
