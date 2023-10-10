package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityList;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback {
   protected List labelList = Lists.newArrayList();
   private int touchValue;
   private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
   private GuiButton selectedButton;
   public int eventButton;
   private URI clickedLinkURI;
   private static final Logger LOGGER = LogManager.getLogger();
   private long lastMouseEvent;
   protected RenderItem itemRender;
   public static int height;
   protected Minecraft mc;
   public boolean allowUserInput;
   protected FontRenderer fontRendererObj;
   private static final Set PROTOCOLS = Sets.newHashSet((Object[])("http", "https"));
   protected List buttonList = Lists.newArrayList();
   public static int width;

   protected void setText(String var1, boolean var2) {
   }

   public void handleInput() throws IOException {
      if (Mouse.isCreated()) {
         while(Mouse.next()) {
            this.handleMouseInput();
         }
      }

      if (Keyboard.isCreated()) {
         while(Keyboard.next()) {
            this.handleKeyboardInput();
         }
      }

   }

   public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
      this.mc = var1;
      this.itemRender = var1.getRenderItem();
      this.fontRendererObj = Minecraft.fontRendererObj;
      width = var2;
      height = var3;
      this.buttonList.clear();
      this.initGui();
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (var2 == 1) {
         this.mc.displayGuiScreen((GuiScreen)null);
         if (this.mc.currentScreen == null) {
            this.mc.setIngameFocus();
         }
      }

   }

   public void onGuiClosed() {
   }

   public static String getClipboardString() {
      try {
         Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);
         if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String)var0.getTransferData(DataFlavor.stringFlavor);
         }
      } catch (Exception var1) {
      }

      return "";
   }

   public static void setClipboardString(String var0) {
      if (!StringUtils.isEmpty(var0)) {
         try {
            StringSelection var1 = new StringSelection(var0);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var1, (ClipboardOwner)null);
         } catch (Exception var2) {
         }
      }

   }

   public void onResize(Minecraft var1, int var2, int var3) {
      this.setWorldAndResolution(var1, var2, var3);
   }

   public static boolean isKeyComboCtrlX(int param0) {
      // $FF: Couldn't be decompiled
   }

   protected void drawHoveringText(List var1, int var2, int var3) {
      if (!var1.isEmpty()) {
         GlStateManager.disableRescaleNormal();
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableLighting();
         GlStateManager.disableDepth();
         int var4 = 0;
         Iterator var6 = var1.iterator();

         int var7;
         while(var6.hasNext()) {
            String var5 = (String)var6.next();
            var7 = this.fontRendererObj.getStringWidth(var5);
            if (var7 > var4) {
               var4 = var7;
            }
         }

         int var13 = var2 + 12;
         int var14 = var3 - 12;
         var7 = 8;
         if (var1.size() > 1) {
            var7 += 2 + (var1.size() - 1) * 10;
         }

         if (var13 + var4 > width) {
            var13 -= 28 + var4;
         }

         if (var14 + var7 + 6 > height) {
            var14 = height - var7 - 6;
         }

         zLevel = 300.0F;
         this.itemRender.zLevel = 300.0F;
         int var8 = -267386864;
         drawGradientRect((double)(var13 - 3), (double)(var14 - 4), (float)(var13 + var4 + 3), (float)(var14 - 3), var8, var8);
         drawGradientRect((double)(var13 - 3), (double)(var14 + var7 + 3), (float)(var13 + var4 + 3), (float)(var14 + var7 + 4), var8, var8);
         drawGradientRect((double)(var13 - 3), (double)(var14 - 3), (float)(var13 + var4 + 3), (float)(var14 + var7 + 3), var8, var8);
         drawGradientRect((double)(var13 - 4), (double)(var14 - 3), (float)(var13 - 3), (float)(var14 + var7 + 3), var8, var8);
         drawGradientRect((double)(var13 + var4 + 3), (double)(var14 - 3), (float)(var13 + var4 + 4), (float)(var14 + var7 + 3), var8, var8);
         int var9 = 1347420415;
         int var10 = (var9 & 16711422) >> 1 | var9 & -16777216;
         drawGradientRect((double)(var13 - 3), (double)(var14 - 3 + 1), (float)(var13 - 3 + 1), (float)(var14 + var7 + 3 - 1), var9, var10);
         drawGradientRect((double)(var13 + var4 + 2), (double)(var14 - 3 + 1), (float)(var13 + var4 + 3), (float)(var14 + var7 + 3 - 1), var9, var10);
         drawGradientRect((double)(var13 - 3), (double)(var14 - 3), (float)(var13 + var4 + 3), (float)(var14 - 3 + 1), var9, var9);
         drawGradientRect((double)(var13 - 3), (double)(var14 + var7 + 2), (float)(var13 + var4 + 3), (float)(var14 + var7 + 3), var10, var10);

         for(int var11 = 0; var11 < var1.size(); ++var11) {
            String var12 = (String)var1.get(var11);
            this.fontRendererObj.drawStringWithShadow(var12, (float)var13, (float)var14, -1);
            if (var11 == 0) {
               var14 += 2;
            }

            var14 += 10;
         }

         zLevel = 0.0F;
         this.itemRender.zLevel = 0.0F;
         GlStateManager.enableLighting();
         GlStateManager.enableDepth();
         RenderHelper.enableStandardItemLighting();
         GlStateManager.enableRescaleNormal();
      }

   }

   public boolean doesGuiPauseGame() {
      return true;
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      if (this.selectedButton != null && var3 == 0) {
         this.selectedButton.mouseReleased(var1, var2);
         this.selectedButton = null;
      }

   }

   protected void actionPerformed(GuiButton var1) throws IOException {
   }

   public void drawDefaultBackground() {
      this.drawWorldBackground(0);
   }

   public void updateScreen() {
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      if (var3 == 0) {
         for(int var4 = 0; var4 < this.buttonList.size(); ++var4) {
            GuiButton var5 = (GuiButton)this.buttonList.get(var4);
            if (var5.mousePressed(this.mc, var1, var2)) {
               this.selectedButton = var5;
               var5.playPressSound(this.mc.getSoundHandler());
               this.actionPerformed(var5);
            }
         }
      }

   }

   protected void drawCreativeTabHoveringText(String var1, int var2, int var3) {
      this.drawHoveringText(Arrays.asList(var1), var2, var3);
   }

   public void handleMouseInput() throws IOException {
      int var1 = Mouse.getEventX() * width / this.mc.displayWidth;
      int var2 = height - Mouse.getEventY() * height / this.mc.displayHeight - 1;
      int var3 = Mouse.getEventButton();
      if (Mouse.getEventButtonState()) {
         if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
            return;
         }

         this.eventButton = var3;
         this.lastMouseEvent = Minecraft.getSystemTime();
         this.mouseClicked(var1, var2, this.eventButton);
      } else if (var3 != -1) {
         if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
            return;
         }

         this.eventButton = -1;
         this.mouseReleased(var1, var2, var3);
      } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
         long var4 = Minecraft.getSystemTime() - this.lastMouseEvent;
         this.mouseClickMove(var1, var2, this.eventButton, var4);
      }

   }

   protected void handleComponentHover(IChatComponent var1, int var2, int var3) {
      if (var1 != null && var1.getChatStyle().getChatHoverEvent() != null) {
         HoverEvent var4 = var1.getChatStyle().getChatHoverEvent();
         if (var4.getAction() == HoverEvent.Action.SHOW_ITEM) {
            ItemStack var5 = null;

            try {
               NBTTagCompound var6 = JsonToNBT.getTagFromJson(var4.getValue().getUnformattedText());
               if (var6 instanceof NBTTagCompound) {
                  var5 = ItemStack.loadItemStackFromNBT((NBTTagCompound)var6);
               }
            } catch (NBTException var11) {
            }

            if (var5 != null) {
               this.renderToolTip(var5, var2, var3);
            } else {
               this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", var2, var3);
            }
         } else {
            String var8;
            if (var4.getAction() == HoverEvent.Action.SHOW_ENTITY) {
               if (this.mc.gameSettings.advancedItemTooltips) {
                  try {
                     NBTTagCompound var12 = JsonToNBT.getTagFromJson(var4.getValue().getUnformattedText());
                     if (var12 instanceof NBTTagCompound) {
                        ArrayList var14 = Lists.newArrayList();
                        NBTTagCompound var7 = (NBTTagCompound)var12;
                        var14.add(var7.getString("name"));
                        if (var7.hasKey("type", 8)) {
                           var8 = var7.getString("type");
                           var14.add("Type: " + var8 + " (" + EntityList.getIDFromString(var8) + ")");
                        }

                        var14.add(var7.getString("id"));
                        this.drawHoveringText(var14, var2, var3);
                     } else {
                        this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", var2, var3);
                     }
                  } catch (NBTException var10) {
                     this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", var2, var3);
                  }
               }
            } else if (var4.getAction() == HoverEvent.Action.SHOW_TEXT) {
               this.drawHoveringText(NEWLINE_SPLITTER.splitToList(var4.getValue().getFormattedText()), var2, var3);
            } else if (var4.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
               StatBase var13 = StatList.getOneShotStat(var4.getValue().getUnformattedText());
               if (var13 != null) {
                  IChatComponent var15 = var13.getStatName();
                  ChatComponentTranslation var16 = new ChatComponentTranslation("stats.tooltip.type." + (var13.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                  var16.getChatStyle().setItalic(true);
                  var8 = var13 instanceof Achievement ? ((Achievement)var13).getDescription() : null;
                  ArrayList var9 = Lists.newArrayList((Object[])(var15.getFormattedText(), var16.getFormattedText()));
                  if (var8 != null) {
                     var9.addAll(this.fontRendererObj.listFormattedStringToWidth(var8, 150));
                  }

                  this.drawHoveringText(var9, var2, var3);
               } else {
                  this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", var2, var3);
               }
            }
         }

         GlStateManager.disableLighting();
      }

   }

   public void sendChatMessage(String var1) {
      this.sendChatMessage(var1, true);
   }

   public void confirmClicked(boolean var1, int var2) {
      if (var2 == 31102009) {
         if (var1) {
            this.openWebLink(this.clickedLinkURI);
         }

         this.clickedLinkURI = null;
         this.mc.displayGuiScreen(this);
      }

   }

   public void drawBackground(int var1) {
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      Tessellator var2 = Tessellator.getInstance();
      WorldRenderer var3 = var2.getWorldRenderer();
      this.mc.getTextureManager().bindTexture(optionsBackground);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float var4 = 32.0F;
      var3.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var3.pos(0.0D, (double)height, 0.0D).tex(0.0D, (double)((float)height / 32.0F + (float)var1)).color(64, 64, 64, 255).endVertex();
      var3.pos((double)width, (double)height, 0.0D).tex((double)((float)width / 32.0F), (double)((float)height / 32.0F + (float)var1)).color(64, 64, 64, 255).endVertex();
      var3.pos((double)width, 0.0D, 0.0D).tex((double)((float)width / 32.0F), (double)var1).color(64, 64, 64, 255).endVertex();
      var3.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)var1).color(64, 64, 64, 255).endVertex();
      var2.draw();
   }

   public void drawScreen(int var1, int var2, float var3) {
      int var4;
      for(var4 = 0; var4 < this.buttonList.size(); ++var4) {
         ((GuiButton)this.buttonList.get(var4)).drawButton(this.mc, var1, var2);
      }

      for(var4 = 0; var4 < this.labelList.size(); ++var4) {
         ((GuiLabel)this.labelList.get(var4)).drawLabel(this.mc, var1, var2);
      }

   }

   public void sendChatMessage(String var1, boolean var2) {
      if (var2) {
         this.mc.ingameGUI.getChatGUI().addToSentMessages(var1);
      }

      Minecraft.thePlayer.sendChatMessage(var1);
   }

   protected void renderToolTip(ItemStack var1, int var2, int var3) {
      List var4 = var1.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips);

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         if (var5 == 0) {
            var4.set(var5, var1.getRarity().rarityColor + (String)var4.get(var5));
         } else {
            var4.set(var5, EnumChatFormatting.GRAY + (String)var4.get(var5));
         }
      }

      this.drawHoveringText(var4, var2, var3);
   }

   public static boolean isKeyComboCtrlC(int param0) {
      // $FF: Couldn't be decompiled
   }

   public void drawBackground2(int var1) {
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("client/BackDaSenha.jpg"));
      Gui.drawScaledCustomSizeModalRect(0.0D, 0.0D, 0.0F, 0.0F, width, height, width, height, (float)width, (float)height);
   }

   private void openWebLink(URI var1) {
      try {
         Class var2 = Class.forName("java.awt.Desktop");
         Object var3 = var2.getMethod("getDesktop").invoke((Object)null);
         var2.getMethod("browse", URI.class).invoke(var3, var1);
      } catch (Throwable var4) {
         LOGGER.error("Couldn't open link", var4);
      }

   }

   public void drawWorldBackground(int var1) {
      if (Minecraft.theWorld != null) {
         drawGradientRect(0.0D, 0.0D, (float)width, (float)height, -1072689136, -804253680);
      } else {
         this.drawBackground(var1);
      }

   }

   public static boolean isKeyComboCtrlA(int param0) {
      // $FF: Couldn't be decompiled
   }

   protected void mouseClickMove(int var1, int var2, int var3, long var4) {
   }

   public static boolean isKeyComboCtrlV(int param0) {
      // $FF: Couldn't be decompiled
   }

   public void initGui() {
   }

   public void handleKeyboardInput() throws IOException {
      if (Keyboard.getEventKeyState()) {
         this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
      }

      this.mc.dispatchKeypresses();
   }

   protected boolean handleComponentClick(IChatComponent param1) {
      // $FF: Couldn't be decompiled
   }
}
