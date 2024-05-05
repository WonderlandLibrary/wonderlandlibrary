package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
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
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative
  extends InventoryEffectRenderer
{
  private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
  private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
  private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
  private float currentScroll;
  private boolean isScrolling;
  private boolean wasClicking;
  private GuiTextField searchField;
  private List field_147063_B;
  private Slot field_147064_C;
  private boolean field_147057_D;
  private CreativeCrafting field_147059_E;
  private static final String __OBFID = "CL_00000752";
  
  public GuiContainerCreative(EntityPlayer p_i1088_1_)
  {
    super(new ContainerCreative(p_i1088_1_));
    p_i1088_1_.openContainer = this.inventorySlots;
    this.allowUserInput = true;
    this.ySize = 136;
    this.xSize = 195;
  }
  
  public void updateScreen()
  {
    if (!Minecraft.playerController.isInCreativeMode()) {
      this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
    }
    func_175378_g();
  }
  
  protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType)
  {
    this.field_147057_D = true;
    boolean var5 = clickType == 1;
    clickType = (slotId == 64537) && (clickType == 0) ? 4 : clickType;
    int var10;
    if ((slotIn == null) && (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()) && (clickType != 5))
    {
      InventoryPlayer var11 = Minecraft.thePlayer.inventory;
      if (var11.getItemStack() != null)
      {
        if (clickedButton == 0)
        {
          Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var11.getItemStack(), true);
          Minecraft.playerController.sendPacketDropItem(var11.getItemStack());
          var11.setItemStack(null);
        }
        if (clickedButton == 1)
        {
          ItemStack var7 = var11.getItemStack().splitStack(1);
          Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var7, true);
          Minecraft.playerController.sendPacketDropItem(var7);
          if (var11.getItemStack().stackSize == 0) {
            var11.setItemStack(null);
          }
        }
      }
    }
    else if ((slotIn == this.field_147064_C) && (var5))
    {
      for (var10 = 0; var10 < Minecraft.thePlayer.inventoryContainer.getInventory().size();)
      {
        Minecraft.playerController.sendSlotPacket(null, var10);var10++; continue;
        if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
        {
          if (slotIn == this.field_147064_C)
          {
            Minecraft.thePlayer.inventory.setItemStack(null);
          }
          else if ((clickType == 4) && (slotIn != null) && (slotIn.getHasStack()))
          {
            ItemStack var6 = slotIn.decrStackSize(clickedButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
            Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var6, true);
            Minecraft.playerController.sendPacketDropItem(var6);
          }
          else if ((clickType == 4) && (Minecraft.thePlayer.inventory.getItemStack() != null))
          {
            Minecraft.thePlayer.dropPlayerItemWithRandomChoice(Minecraft.thePlayer.inventory.getItemStack(), true);
            Minecraft.playerController.sendPacketDropItem(Minecraft.thePlayer.inventory.getItemStack());
            Minecraft.thePlayer.inventory.setItemStack(null);
          }
          else
          {
            Minecraft.thePlayer.inventoryContainer.slotClick(slotIn == null ? slotId : ((CreativeSlot)slotIn).field_148332_b.slotNumber, clickedButton, clickType, Minecraft.thePlayer);
            Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
          }
        }
        else if ((clickType != 5) && (slotIn.inventory == field_147060_v))
        {
          InventoryPlayer var11 = Minecraft.thePlayer.inventory;
          ItemStack var7 = var11.getItemStack();
          ItemStack var8 = slotIn.getStack();
          if (clickType == 2)
          {
            if ((var8 != null) && (clickedButton >= 0) && (clickedButton < 9))
            {
              ItemStack var9 = var8.copy();
              var9.stackSize = var9.getMaxStackSize();
              Minecraft.thePlayer.inventory.setInventorySlotContents(clickedButton, var9);
              Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
            }
            return;
          }
          if (clickType == 3)
          {
            if ((var11.getItemStack() == null) && (slotIn.getHasStack()))
            {
              ItemStack var9 = slotIn.getStack().copy();
              var9.stackSize = var9.getMaxStackSize();
              var11.setItemStack(var9);
            }
            return;
          }
          if (clickType == 4)
          {
            if (var8 != null)
            {
              ItemStack var9 = var8.copy();
              var9.stackSize = (clickedButton == 0 ? 1 : var9.getMaxStackSize());
              Minecraft.thePlayer.dropPlayerItemWithRandomChoice(var9, true);
              Minecraft.playerController.sendPacketDropItem(var9);
            }
            return;
          }
          if ((var7 != null) && (var8 != null) && (var7.isItemEqual(var8)))
          {
            if (clickedButton == 0)
            {
              if (var5) {
                var7.stackSize = var7.getMaxStackSize();
              } else if (var7.stackSize < var7.getMaxStackSize()) {
                var7.stackSize += 1;
              }
            }
            else if (var7.stackSize <= 1) {
              var11.setItemStack(null);
            } else {
              var7.stackSize -= 1;
            }
          }
          else if ((var8 != null) && (var7 == null))
          {
            var11.setItemStack(ItemStack.copyItemStack(var8));
            var7 = var11.getItemStack();
            if (var5) {
              var7.stackSize = var7.getMaxStackSize();
            }
          }
          else
          {
            var11.setItemStack(null);
          }
        }
        else
        {
          this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, clickedButton, clickType, Minecraft.thePlayer);
          if (Container.getDragEvent(clickedButton) == 2) {
            for (int var10 = 0; var10 < 9; var10++) {
              Minecraft.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var10).getStack(), 36 + var10);
            }
          }
          if (slotIn != null)
          {
            ItemStack var6 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
            Minecraft.playerController.sendSlotPacket(var6, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
          }
        }
      }
    }
  }
  
  public void initGui()
  {
    if (Minecraft.playerController.isInCreativeMode())
    {
      super.initGui();
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
      this.searchField.setMaxStringLength(15);
      this.searchField.setEnableBackgroundDrawing(false);
      this.searchField.setVisible(false);
      this.searchField.setTextColor(16777215);
      int var1 = selectedTabIndex;
      selectedTabIndex = -1;
      setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
      this.field_147059_E = new CreativeCrafting(this.mc);
      Minecraft.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
    }
    else
    {
      this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
    }
  }
  
  public void onGuiClosed()
  {
    super.onGuiClosed();
    if ((Minecraft.thePlayer != null) && (Minecraft.thePlayer.inventory != null)) {
      Minecraft.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
    }
    Keyboard.enableRepeatEvents(false);
  }
  
  protected void keyTyped(char typedChar, int keyCode)
    throws IOException
  {
    if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
    {
      if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
        setCurrentCreativeTab(CreativeTabs.tabAllSearch);
      } else {
        super.keyTyped(typedChar, keyCode);
      }
    }
    else
    {
      if (this.field_147057_D)
      {
        this.field_147057_D = false;
        this.searchField.setText("");
      }
      if (!checkHotbarKeys(keyCode)) {
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
          updateCreativeSearch();
        } else {
          super.keyTyped(typedChar, keyCode);
        }
      }
    }
  }
  
  private void updateCreativeSearch()
  {
    ContainerCreative var1 = (ContainerCreative)this.inventorySlots;
    var1.itemList.clear();
    Iterator var2 = Item.itemRegistry.iterator();
    while (var2.hasNext())
    {
      Item var3 = (Item)var2.next();
      if ((var3 != null) && (var3.getCreativeTab() != null)) {
        var3.getSubItems(var3, null, var1.itemList);
      }
    }
    Enchantment[] var8 = Enchantment.enchantmentsList;
    int var9 = var8.length;
    for (int var4 = 0; var4 < var9; var4++)
    {
      Enchantment var5 = var8[var4];
      if ((var5 != null) && (var5.type != null)) {
        Items.enchanted_book.func_92113_a(var5, var1.itemList);
      }
    }
    var2 = var1.itemList.iterator();
    String var10 = this.searchField.getText().toLowerCase();
    while (var2.hasNext())
    {
      ItemStack var11 = (ItemStack)var2.next();
      boolean var12 = false;
      Iterator var6 = var11.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();
      while (var6.hasNext())
      {
        String var7 = (String)var6.next();
        if (EnumChatFormatting.getTextWithoutFormattingCodes(var7).toLowerCase().contains(var10)) {
          var12 = true;
        }
      }
      if (!var12) {
        var2.remove();
      }
    }
    this.currentScroll = 0.0F;
    var1.scrollTo(0.0F);
  }
  
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
  {
    CreativeTabs var3 = CreativeTabs.creativeTabArray[selectedTabIndex];
    if (var3.drawInForegroundOfTab())
    {
      GlStateManager.disableBlend();
      this.fontRendererObj.drawString(I18n.format(var3.getTranslatedTabLabel(), new Object[0]), 8, 6, 4210752);
    }
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    throws IOException
  {
    if (mouseButton == 0)
    {
      int var4 = mouseX - this.guiLeft;
      int var5 = mouseY - this.guiTop;
      CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
      int var7 = var6.length;
      for (int var8 = 0; var8 < var7; var8++)
      {
        CreativeTabs var9 = var6[var8];
        if (func_147049_a(var9, var4, var5)) {
          return;
        }
      }
    }
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  protected void mouseReleased(int mouseX, int mouseY, int state)
  {
    if (state == 0)
    {
      int var4 = mouseX - this.guiLeft;
      int var5 = mouseY - this.guiTop;
      CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
      int var7 = var6.length;
      for (int var8 = 0; var8 < var7; var8++)
      {
        CreativeTabs var9 = var6[var8];
        if (func_147049_a(var9, var4, var5))
        {
          setCurrentCreativeTab(var9);
          return;
        }
      }
    }
    super.mouseReleased(mouseX, mouseY, state);
  }
  
  private boolean needsScrollBars()
  {
    return (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()) && (CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()) && (((ContainerCreative)this.inventorySlots).func_148328_e());
  }
  
  private void setCurrentCreativeTab(CreativeTabs p_147050_1_)
  {
    int var2 = selectedTabIndex;
    selectedTabIndex = p_147050_1_.getTabIndex();
    ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
    this.dragSplittingSlots.clear();
    var3.itemList.clear();
    p_147050_1_.displayAllReleventItems(var3.itemList);
    if (p_147050_1_ == CreativeTabs.tabInventory)
    {
      Container var4 = Minecraft.thePlayer.inventoryContainer;
      if (this.field_147063_B == null) {
        this.field_147063_B = var3.inventorySlots;
      }
      var3.inventorySlots = Lists.newArrayList();
      for (int var5 = 0; var5 < var4.inventorySlots.size(); var5++)
      {
        CreativeSlot var6 = new CreativeSlot((Slot)var4.inventorySlots.get(var5), var5);
        var3.inventorySlots.add(var6);
        if ((var5 >= 5) && (var5 < 9))
        {
          int var7 = var5 - 5;
          int var8 = var7 / 2;
          int var9 = var7 % 2;
          var6.xDisplayPosition = (9 + var8 * 54);
          var6.yDisplayPosition = (6 + var9 * 27);
        }
        else if ((var5 >= 0) && (var5 < 5))
        {
          var6.yDisplayPosition = 63536;
          var6.xDisplayPosition = 63536;
        }
        else if (var5 < var4.inventorySlots.size())
        {
          int var7 = var5 - 9;
          int var8 = var7 % 9;
          int var9 = var7 / 9;
          var6.xDisplayPosition = (9 + var8 * 18);
          if (var5 >= 36) {
            var6.yDisplayPosition = 112;
          } else {
            var6.yDisplayPosition = (54 + var9 * 18);
          }
        }
      }
      this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
      var3.inventorySlots.add(this.field_147064_C);
    }
    else if (var2 == CreativeTabs.tabInventory.getTabIndex())
    {
      var3.inventorySlots = this.field_147063_B;
      this.field_147063_B = null;
    }
    if (this.searchField != null) {
      if (p_147050_1_ == CreativeTabs.tabAllSearch)
      {
        this.searchField.setVisible(true);
        this.searchField.setCanLoseFocus(false);
        this.searchField.setFocused(true);
        this.searchField.setText("");
        updateCreativeSearch();
      }
      else
      {
        this.searchField.setVisible(false);
        this.searchField.setCanLoseFocus(true);
        this.searchField.setFocused(false);
      }
    }
    this.currentScroll = 0.0F;
    var3.scrollTo(0.0F);
  }
  
  public void handleMouseInput()
    throws IOException
  {
    super.handleMouseInput();
    int var1 = Mouse.getEventDWheel();
    if ((var1 != 0) && (needsScrollBars()))
    {
      int var2 = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;
      if (var1 > 0) {
        var1 = 1;
      }
      if (var1 < 0) {
        var1 = -1;
      }
      this.currentScroll = ((float)(this.currentScroll - var1 / var2));
      this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
      ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
    }
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    boolean var4 = Mouse.isButtonDown(0);
    int var5 = this.guiLeft;
    int var6 = this.guiTop;
    int var7 = var5 + 175;
    int var8 = var6 + 18;
    int var9 = var7 + 14;
    int var10 = var8 + 112;
    if ((!this.wasClicking) && (var4) && (mouseX >= var7) && (mouseY >= var8) && (mouseX < var9) && (mouseY < var10)) {
      this.isScrolling = needsScrollBars();
    }
    if (!var4) {
      this.isScrolling = false;
    }
    this.wasClicking = var4;
    if (this.isScrolling)
    {
      this.currentScroll = ((mouseY - var8 - 7.5F) / (var10 - var8 - 15.0F));
      this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
      ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
    }
    super.drawScreen(mouseX, mouseY, partialTicks);
    CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
    int var12 = var11.length;
    for (int var13 = 0; var13 < var12; var13++)
    {
      CreativeTabs var14 = var11[var13];
      if (renderCreativeInventoryHoveringText(var14, mouseX, mouseY)) {
        break;
      }
    }
    if ((this.field_147064_C != null) && (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) && (isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY))) {
      drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
    }
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.disableLighting();
  }
  
  protected void renderToolTip(ItemStack itemIn, int x, int y)
  {
    if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
    {
      List var4 = itemIn.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips);
      CreativeTabs var5 = itemIn.getItem().getCreativeTab();
      if ((var5 == null) && (itemIn.getItem() == Items.enchanted_book))
      {
        Map var6 = EnchantmentHelper.getEnchantments(itemIn);
        if (var6.size() == 1)
        {
          Enchantment var7 = Enchantment.func_180306_c(((Integer)var6.keySet().iterator().next()).intValue());
          CreativeTabs[] var8 = CreativeTabs.creativeTabArray;
          int var9 = var8.length;
          for (int var10 = 0; var10 < var9; var10++)
          {
            CreativeTabs var11 = var8[var10];
            if (var11.hasRelevantEnchantmentType(var7.type))
            {
              var5 = var11;
              break;
            }
          }
        }
      }
      if (var5 != null) {
        var4.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE + I18n.format(var5.getTranslatedTabLabel(), new Object[0]));
      }
      for (int var12 = 0; var12 < var4.size(); var12++) {
        if (var12 == 0) {
          var4.set(var12, itemIn.getRarity().rarityColor + (String)var4.get(var12));
        } else {
          var4.set(var12, EnumChatFormatting.GRAY + (String)var4.get(var12));
        }
      }
      drawHoveringText(var4, x, y);
    }
    else
    {
      super.renderToolTip(itemIn, x, y);
    }
  }
  
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    RenderHelper.enableGUIStandardItemLighting();
    CreativeTabs var4 = CreativeTabs.creativeTabArray[selectedTabIndex];
    CreativeTabs[] var5 = CreativeTabs.creativeTabArray;
    int var6 = var5.length;
    for (int var7 = 0; var7 < var6; var7++)
    {
      CreativeTabs var8 = var5[var7];
      this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
      if (var8.getTabIndex() != selectedTabIndex) {
        func_147051_a(var8);
      }
    }
    this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + var4.getBackgroundImageName()));
    drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    this.searchField.drawTextBox();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    int var9 = this.guiLeft + 175;
    var6 = this.guiTop + 18;
    var7 = var6 + 112;
    this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
    if (var4.shouldHidePlayerInventory()) {
      drawTexturedModalRect(var9, var6 + (int)((var7 - var6 - 17) * this.currentScroll), 'è' + (needsScrollBars() ? 0 : 12), 0, 12, 15);
    }
    func_147051_a(var4);
    if (var4 == CreativeTabs.tabInventory) {
      GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - mouseX, this.guiTop + 45 - 30 - mouseY, Minecraft.thePlayer);
    }
  }
  
  protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_)
  {
    int var4 = p_147049_1_.getTabColumn();
    int var5 = 28 * var4;
    byte var6 = 0;
    if (var4 == 5) {
      var5 = this.xSize - 28 + 2;
    } else if (var4 > 0) {
      var5 += var4;
    }
    int var7;
    int var7;
    if (p_147049_1_.isTabInFirstRow()) {
      var7 = var6 - 32;
    } else {
      var7 = var6 + this.ySize;
    }
    return (p_147049_2_ >= var5) && (p_147049_2_ <= var5 + 28) && (p_147049_3_ >= var7) && (p_147049_3_ <= var7 + 32);
  }
  
  protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_)
  {
    int var4 = p_147052_1_.getTabColumn();
    int var5 = 28 * var4;
    byte var6 = 0;
    if (var4 == 5) {
      var5 = this.xSize - 28 + 2;
    } else if (var4 > 0) {
      var5 += var4;
    }
    int var7;
    int var7;
    if (p_147052_1_.isTabInFirstRow()) {
      var7 = var6 - 32;
    } else {
      var7 = var6 + this.ySize;
    }
    if (isPointInRegion(var5 + 3, var7 + 3, 23, 27, p_147052_2_, p_147052_3_))
    {
      drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
      return true;
    }
    return false;
  }
  
  protected void func_147051_a(CreativeTabs p_147051_1_)
  {
    boolean var2 = p_147051_1_.getTabIndex() == selectedTabIndex;
    boolean var3 = p_147051_1_.isTabInFirstRow();
    int var4 = p_147051_1_.getTabColumn();
    int var5 = var4 * 28;
    int var6 = 0;
    int var7 = this.guiLeft + 28 * var4;
    int var8 = this.guiTop;
    byte var9 = 32;
    if (var2) {
      var6 += 32;
    }
    if (var4 == 5) {
      var7 = this.guiLeft + this.xSize - 28;
    } else if (var4 > 0) {
      var7 += var4;
    }
    if (var3)
    {
      var8 -= 28;
    }
    else
    {
      var6 += 64;
      var8 += this.ySize - 4;
    }
    GlStateManager.disableLighting();
    drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
    this.zLevel = 100.0F;
    this.itemRender.zLevel = 100.0F;
    var7 += 6;
    var8 += 8 + (var3 ? 1 : -1);
    GlStateManager.enableLighting();
    GlStateManager.enableRescaleNormal();
    ItemStack var10 = p_147051_1_.getIconItemStack();
    this.itemRender.func_180450_b(var10, var7, var8);
    this.itemRender.func_175030_a(this.fontRendererObj, var10, var7, var8);
    GlStateManager.disableLighting();
    this.itemRender.zLevel = 0.0F;
    this.zLevel = 0.0F;
  }
  
  protected void actionPerformed(GuiButton button)
    throws IOException
  {
    if (button.id == 0) {
      this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
    }
    if (button.id == 1) {
      this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
    }
  }
  
  public int func_147056_g()
  {
    return selectedTabIndex;
  }
  
  static class ContainerCreative
    extends Container
  {
    public List itemList = Lists.newArrayList();
    private static final String __OBFID = "CL_00000753";
    
    public ContainerCreative(EntityPlayer p_i1086_1_)
    {
      InventoryPlayer var2 = p_i1086_1_.inventory;
      for (int var3 = 0; var3 < 5; var3++) {
        for (int var4 = 0; var4 < 9; var4++) {
          addSlotToContainer(new Slot(GuiContainerCreative.field_147060_v, var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
        }
      }
      for (var3 = 0; var3 < 9; var3++) {
        addSlotToContainer(new Slot(var2, var3, 9 + var3 * 18, 112));
      }
      scrollTo(0.0F);
    }
    
    public boolean canInteractWith(EntityPlayer playerIn)
    {
      return true;
    }
    
    public void scrollTo(float p_148329_1_)
    {
      int var2 = (this.itemList.size() + 8) / 9 - 5;
      int var3 = (int)(p_148329_1_ * var2 + 0.5D);
      if (var3 < 0) {
        var3 = 0;
      }
      for (int var4 = 0; var4 < 5; var4++) {
        for (int var5 = 0; var5 < 9; var5++)
        {
          int var6 = var5 + (var4 + var3) * 9;
          if ((var6 >= 0) && (var6 < this.itemList.size())) {
            GuiContainerCreative.field_147060_v.setInventorySlotContents(var5 + var4 * 9, (ItemStack)this.itemList.get(var6));
          } else {
            GuiContainerCreative.field_147060_v.setInventorySlotContents(var5 + var4 * 9, null);
          }
        }
      }
    }
    
    public boolean func_148328_e()
    {
      return this.itemList.size() > 45;
    }
    
    protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {}
    
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
      if ((index >= this.inventorySlots.size() - 9) && (index < this.inventorySlots.size()))
      {
        Slot var3 = (Slot)this.inventorySlots.get(index);
        if ((var3 != null) && (var3.getHasStack())) {
          var3.putStack(null);
        }
      }
      return null;
    }
    
    public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
    {
      return p_94530_2_.yDisplayPosition > 90;
    }
    
    public boolean canDragIntoSlot(Slot p_94531_1_)
    {
      return ((p_94531_1_.inventory instanceof InventoryPlayer)) || ((p_94531_1_.yDisplayPosition > 90) && (p_94531_1_.xDisplayPosition <= 162));
    }
  }
  
  class CreativeSlot
    extends Slot
  {
    private final Slot field_148332_b;
    private static final String __OBFID = "CL_00000754";
    
    public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_)
    {
      super(p_i46313_3_, 0, 0);
      this.field_148332_b = p_i46313_2_;
    }
    
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
    {
      this.field_148332_b.onPickupFromSlot(playerIn, stack);
    }
    
    public boolean isItemValid(ItemStack stack)
    {
      return this.field_148332_b.isItemValid(stack);
    }
    
    public ItemStack getStack()
    {
      return this.field_148332_b.getStack();
    }
    
    public boolean getHasStack()
    {
      return this.field_148332_b.getHasStack();
    }
    
    public void putStack(ItemStack p_75215_1_)
    {
      this.field_148332_b.putStack(p_75215_1_);
    }
    
    public void onSlotChanged()
    {
      this.field_148332_b.onSlotChanged();
    }
    
    public int getSlotStackLimit()
    {
      return this.field_148332_b.getSlotStackLimit();
    }
    
    public int func_178170_b(ItemStack p_178170_1_)
    {
      return this.field_148332_b.func_178170_b(p_178170_1_);
    }
    
    public String func_178171_c()
    {
      return this.field_148332_b.func_178171_c();
    }
    
    public ItemStack decrStackSize(int p_75209_1_)
    {
      return this.field_148332_b.decrStackSize(p_75209_1_);
    }
    
    public boolean isHere(IInventory p_75217_1_, int p_75217_2_)
    {
      return this.field_148332_b.isHere(p_75217_1_, p_75217_2_);
    }
  }
}
