package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat extends GuiScreen {
   private String defaultInputFieldText = "";
   private int sentHistoryCursor = -1;
   protected GuiTextField inputField;
   private List foundPlayerNames = Lists.newArrayList();
   private int autocompleteIndex;
   private boolean playerNamesFound;
   private String historyBuffer = "";
   private static final Logger logger = LogManager.getLogger();
   private boolean waitingOnAutocomplete;

   private void sendAutocompleteRequest(String var1, String var2) {
      if (var1.length() >= 1) {
         BlockPos var3 = null;
         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            var3 = this.mc.objectMouseOver.getBlockPos();
         }

         Minecraft.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(var1, var3));
         this.waitingOnAutocomplete = true;
      }

   }

   protected void setText(String var1, boolean var2) {
      if (var2) {
         this.inputField.setText(var1);
      } else {
         this.inputField.writeText(var1);
      }

   }

   public void autocompletePlayerNames() {
      String var2;
      if (this.playerNamesFound) {
         this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
         if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
            this.autocompleteIndex = 0;
         }
      } else {
         int var1 = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
         this.foundPlayerNames.clear();
         this.autocompleteIndex = 0;
         var2 = this.inputField.getText().substring(var1).toLowerCase();
         String var3 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
         this.sendAutocompleteRequest(var3, var2);
         if (this.foundPlayerNames.isEmpty()) {
            return;
         }

         this.playerNamesFound = true;
         this.inputField.deleteFromCursor(var1 - this.inputField.getCursorPosition());
      }

      if (this.foundPlayerNames.size() > 1) {
         StringBuilder var4 = new StringBuilder();

         for(Iterator var5 = this.foundPlayerNames.iterator(); var5.hasNext(); var4.append(var2)) {
            var2 = (String)var5.next();
            if (var4.length() > 0) {
               var4.append(", ");
            }
         }

         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(String.valueOf(var4)), 1);
      }

      this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
   }

   public void onAutocompleteResponse(String[] var1) {
      if (this.waitingOnAutocomplete) {
         this.playerNamesFound = false;
         this.foundPlayerNames.clear();
         String[] var5 = var1;
         int var4 = var1.length;

         String var2;
         for(int var3 = 0; var3 < var4; ++var3) {
            var2 = var5[var3];
            if (var2.length() > 0) {
               this.foundPlayerNames.add(var2);
            }
         }

         var2 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
         String var6 = StringUtils.getCommonPrefix(var1);
         if (var6.length() > 0 && !var2.equalsIgnoreCase(var6)) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
            this.inputField.writeText(var6);
         } else if (this.foundPlayerNames.size() > 0) {
            this.playerNamesFound = true;
            this.autocompletePlayerNames();
         }
      }

   }

   public GuiChat() {
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      this.inputField = new GuiTextField(0, this.fontRendererObj, 4, height - 12, width - 4, 12);
      this.inputField.setMaxStringLength(100);
      this.inputField.setEnableBackgroundDrawing(false);
      this.inputField.setFocused(true);
      this.inputField.setText(this.defaultInputFieldText);
      this.inputField.setCanLoseFocus(false);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.getChatGUI().resetScroll();
   }

   public void updateScreen() {
      this.inputField.updateCursorCounter();
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      if (var3 == 0) {
         IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
         if (this.handleComponentClick(var4)) {
            return;
         }
      }

      this.inputField.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }

   public void getSentHistory(int var1) {
      int var2 = this.sentHistoryCursor + var1;
      int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      var2 = MathHelper.clamp_int(var2, 0, var3);
      if (var2 != this.sentHistoryCursor) {
         if (var2 == var3) {
            this.sentHistoryCursor = var3;
            this.inputField.setText(this.historyBuffer);
         } else {
            if (this.sentHistoryCursor == var3) {
               this.historyBuffer = this.inputField.getText();
            }

            this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
            this.sentHistoryCursor = var2;
         }
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      drawRect(2.0D, (double)(height - 14), (double)(width - 2), (double)(height - 2), Integer.MIN_VALUE);
      this.inputField.drawTextBox();
      IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
      if (var4 != null && var4.getChatStyle().getChatHoverEvent() != null) {
         this.handleComponentHover(var4, var1, var2);
      }

      super.drawScreen(var1, var2, var3);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int var1 = Mouse.getEventDWheel();
      if (var1 != 0) {
         if (var1 > 1) {
            var1 = 1;
         }

         if (var1 < -1) {
            var1 = -1;
         }

         if (!isShiftKeyDown()) {
            var1 *= 7;
         }

         this.mc.ingameGUI.getChatGUI().scroll(var1);
      }

   }

   public GuiChat(String var1) {
      this.defaultInputFieldText = var1;
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      this.waitingOnAutocomplete = false;
      if (var2 == 15) {
         this.autocompletePlayerNames();
      } else {
         this.playerNamesFound = false;
      }

      if (var2 == 1) {
         this.mc.displayGuiScreen((GuiScreen)null);
      } else if (var2 != 28 && var2 != 156) {
         if (var2 == 200) {
            this.getSentHistory(-1);
         } else if (var2 == 208) {
            this.getSentHistory(1);
         } else if (var2 == 201) {
            this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
         } else if (var2 == 209) {
            this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
         } else {
            this.inputField.textboxKeyTyped(var1, var2);
         }
      } else {
         String var3 = this.inputField.getText().trim();
         if (var3.length() > 0) {
            this.sendChatMessage(var3);
         }

         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }
}
