package my.NewSnake.Tank.me.tireman.hexa.alts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

public class PasswordField extends Gui {
   private final int height;
   private boolean field_73823_s = true;
   private int field_73816_n = 0;
   private boolean isEnabled = true;
   private boolean canLoseFocus = true;
   private final int xPos;
   private boolean enableBackgroundDrawing = true;
   private int enabledColor = 14737632;
   private final int yPos;
   public boolean isFocused = false;
   private int selectionEnd = 0;
   private String text = "";
   private final int width;
   private final FontRenderer fontRenderer;
   private int disabledColor = 7368816;
   private int maxStringLength = 50;
   private int cursorPosition = 0;
   private int cursorCounter;

   public void deleteFromCursor(int var1) {
      if (this.text.length() != 0) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            boolean var2 = var1 < 0;
            int var3 = var2 ? this.cursorPosition + var1 : this.cursorPosition;
            int var4 = var2 ? this.cursorPosition : this.cursorPosition + var1;
            String var5 = "";
            if (var3 >= 0) {
               var5 = this.text.substring(0, var3);
            }

            if (var4 < this.text.length()) {
               var5 = String.valueOf(String.valueOf(var5)) + this.text.substring(var4);
            }

            this.text = var5;
            if (var2) {
               this.func_73784_d(var1);
            }
         }
      }

   }

   public String getText() {
      String var1 = this.text.replaceAll(" ", "");
      return var1;
   }

   public void updateCursorCounter() {
      ++this.cursorCounter;
   }

   public int getNthWordFromCursor(int var1) {
      return this.getNthWordFromPos(var1, this.getCursorPosition());
   }

   public void drawTextBox() {
      if (this.func_73778_q()) {
         if (this.getEnableBackgroundDrawing()) {
            Gui.drawRect((double)(this.xPos - 1), (double)(this.yPos - 1), (double)(this.xPos + this.width + 1), (double)(this.yPos + this.height + 1), -6250336);
            Gui.drawRect((double)this.xPos, (double)this.yPos, (double)(this.xPos + this.width), (double)(this.yPos + this.height), -16777216);
         }

         int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
         int var2 = this.cursorPosition - this.field_73816_n;
         int var3 = this.selectionEnd - this.field_73816_n;
         String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), this.getWidth());
         boolean var5 = var2 >= 0 && var2 <= var4.length();
         boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
         int var7 = this.enableBackgroundDrawing ? this.xPos + 4 : this.xPos;
         int var8 = this.enableBackgroundDrawing ? this.yPos + (this.height - 8) / 2 : this.yPos;
         int var9 = var7;
         if (var3 > var4.length()) {
            var3 = var4.length();
         }

         if (var4.length() > 0) {
            if (var5) {
               var4.substring(0, var2);
            }

            Minecraft.getMinecraft();
            var9 = Minecraft.fontRendererObj.drawString(this.text.replaceAll("(?s).", "*"), (double)var7, (double)var8, var1);
         }

         boolean var10 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
         int var11 = var9;
         if (!var5) {
            var11 = var2 > 0 ? var7 + this.width : var7;
         } else if (var10) {
            var11 = var9 - 1;
            --var9;
         }

         if (var4.length() > 0 && var5 && var2 < var4.length()) {
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawString(var4.substring(var2), (double)var9, (double)var8, var1);
         }

         if (var6) {
            if (var10) {
               Gui.drawRect((double)var11, (double)(var8 - 1), (double)(var11 + 1), (double)(var8 + 1 + this.fontRenderer.FONT_HEIGHT), -3092272);
            } else {
               Minecraft.getMinecraft();
               Minecraft.fontRendererObj.drawString("_", (double)var11, (double)var8, var1);
            }
         }

         if (var3 != var2) {
            int var12 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
            this.drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
         }
      }

   }

   private void drawCursorVertical(int var1, int var2, int var3, int var4) {
      if (var1 < var3) {
         ;
      }

      if (var2 < var4) {
         ;
      }

      Tessellator var5 = Tessellator.getInstance();
      WorldRenderer var6 = var5.getWorldRenderer();
      GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
      GL11.glDisable(3553);
      GL11.glEnable(3058);
      GL11.glLogicOp(5387);
      GL11.glDisable(3058);
      GL11.glEnable(3553);
   }

   public boolean textboxKeyTyped(char var1, int var2) {
      if (this.isEnabled && this.isFocused) {
         switch(var1) {
         case '\u0001':
            this.setCursorPositionEnd();
            this.func_73800_i(0);
            return true;
         case '\u0003':
            GuiScreen.setClipboardString(this.getSelectedtext());
            return true;
         case '\u0016':
            this.writeText(GuiScreen.getClipboardString());
            return true;
         case '\u0018':
            GuiScreen.setClipboardString(this.getSelectedtext());
            this.writeText("");
            return true;
         default:
            switch(var2) {
            case 14:
               if (GuiScreen.isCtrlKeyDown()) {
                  this.func_73779_a(-1);
               } else {
                  this.deleteFromCursor(-1);
               }

               return true;
            case 199:
               if (GuiScreen.isShiftKeyDown()) {
                  this.func_73800_i(0);
               } else {
                  this.setCursorPositionZero();
               }

               return true;
            case 203:
               if (GuiScreen.isShiftKeyDown()) {
                  if (GuiScreen.isCtrlKeyDown()) {
                     this.func_73800_i(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                  } else {
                     this.func_73800_i(this.getSelectionEnd() - 1);
                  }
               } else if (GuiScreen.isCtrlKeyDown()) {
                  this.setCursorPosition(this.getNthWordFromCursor(-1));
               } else {
                  this.func_73784_d(-1);
               }

               return true;
            case 205:
               if (GuiScreen.isShiftKeyDown()) {
                  if (GuiScreen.isCtrlKeyDown()) {
                     this.func_73800_i(this.getNthWordFromPos(1, this.getSelectionEnd()));
                  } else {
                     this.func_73800_i(this.getSelectionEnd() + 1);
                  }
               } else if (GuiScreen.isCtrlKeyDown()) {
                  this.setCursorPosition(this.getNthWordFromCursor(1));
               } else {
                  this.func_73784_d(1);
               }

               return true;
            case 207:
               if (GuiScreen.isShiftKeyDown()) {
                  this.func_73800_i(this.text.length());
               } else {
                  this.setCursorPositionEnd();
               }

               return true;
            case 211:
               if (GuiScreen.isCtrlKeyDown()) {
                  this.func_73779_a(1);
               } else {
                  this.deleteFromCursor(1);
               }

               return true;
            default:
               if (ChatAllowedCharacters.isAllowedCharacter(var1)) {
                  this.writeText(Character.toString(var1));
                  return true;
               } else {
                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public void setFocused(boolean var1) {
      if (var1 && !this.isFocused) {
         this.cursorCounter = 0;
      }

      this.isFocused = var1;
   }

   public void func_73794_g(int var1) {
      this.enabledColor = var1;
   }

   public void func_73800_i(int var1) {
      int var2 = this.text.length();
      if (var1 > var2) {
         var1 = var2;
      }

      if (var1 < 0) {
         var1 = 0;
      }

      this.selectionEnd = var1;
      if (this.fontRenderer != null) {
         if (this.field_73816_n > var2) {
            this.field_73816_n = var2;
         }

         int var3 = this.getWidth();
         String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), var3);
         int var5 = var4.length() + this.field_73816_n;
         if (var1 == this.field_73816_n) {
            this.field_73816_n -= this.fontRenderer.trimStringToWidth(this.text, var3, true).length();
         }

         if (var1 > var5) {
            this.field_73816_n += var1 - var5;
         } else if (var1 <= this.field_73816_n) {
            this.field_73816_n -= this.field_73816_n - var1;
         }

         if (this.field_73816_n < 0) {
            this.field_73816_n = 0;
         }

         if (this.field_73816_n > var2) {
            this.field_73816_n = var2;
         }
      }

   }

   public boolean func_73778_q() {
      return this.field_73823_s;
   }

   public String getSelectedtext() {
      int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
      int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
      return this.text.substring(var1, var2);
   }

   public int getNthWordFromPos(int var1, int var2) {
      return this.func_73798_a(var1, this.getCursorPosition(), true);
   }

   public void setMaxStringLength(int var1) {
      this.maxStringLength = var1;
      if (this.text.length() > var1) {
         this.text = this.text.substring(0, var1);
      }

   }

   public void setCanLoseFocus(boolean var1) {
      this.canLoseFocus = var1;
   }

   public void mouseClicked(int var1, int var2, int var3) {
      boolean var4 = var1 >= this.xPos && var1 < this.xPos + this.width && var2 >= this.yPos && var2 < this.yPos + this.height;
      if (this.canLoseFocus) {
         this.setFocused(this.isEnabled && var4);
      }

      if (this.isFocused && var3 == 0) {
         int var5 = var1 - this.xPos;
         if (this.enableBackgroundDrawing) {
            var5 -= 4;
         }

         String var6 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), this.getWidth());
         this.setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.field_73816_n);
      }

   }

   public void writeText(String var1) {
      String var2 = "";
      String var3 = ChatAllowedCharacters.filterAllowedCharacters(var1);
      int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
      int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
      int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
      boolean var7 = false;
      if (this.text.length() > 0) {
         var2 = String.valueOf(String.valueOf(var2)) + this.text.substring(0, var4);
      }

      int var8;
      if (var6 < var3.length()) {
         var2 = String.valueOf(String.valueOf(var2)) + var3.substring(0, var6);
         var8 = var6;
      } else {
         var2 = String.valueOf(String.valueOf(var2)) + var3;
         var8 = var3.length();
      }

      if (this.text.length() > 0 && var5 < this.text.length()) {
         var2 = String.valueOf(String.valueOf(var2)) + this.text.substring(var5);
      }

      this.text = var2.replaceAll(" ", "");
      this.func_73784_d(var4 - this.selectionEnd + var8);
   }

   public void setText(String var1) {
      if (var1.length() > this.maxStringLength) {
         this.text = var1.substring(0, this.maxStringLength);
      } else {
         this.text = var1;
      }

      this.setCursorPositionEnd();
   }

   public int func_73798_a(int var1, int var2, boolean var3) {
      int var4 = var2;
      boolean var5 = var1 < 0;
      int var6 = Math.abs(var1);

      for(int var7 = 0; var7 < var6; ++var7) {
         if (!var5) {
            int var8 = this.text.length();
            var4 = this.text.indexOf(32, var4);
            if (var4 == -1) {
               var4 = var8;
            } else {
               while(var3 && var4 < var8 && this.text.charAt(var4) == ' ') {
                  ++var4;
               }
            }
         } else {
            while(var3 && var4 > 0 && this.text.charAt(var4 - 1) == ' ') {
               --var4;
            }

            while(var4 > 0 && this.text.charAt(var4 - 1) != ' ') {
               --var4;
            }
         }
      }

      return var4;
   }

   public int getSelectionEnd() {
      return this.selectionEnd;
   }

   public void setEnableBackgroundDrawing(boolean var1) {
      this.enableBackgroundDrawing = var1;
   }

   public PasswordField(FontRenderer var1, int var2, int var3, int var4, int var5) {
      this.fontRenderer = var1;
      this.xPos = var2;
      this.yPos = var3;
      this.width = var4;
      this.height = var5;
   }

   public int getCursorPosition() {
      return this.cursorPosition;
   }

   public void func_73784_d(int var1) {
      this.setCursorPosition(this.selectionEnd + var1);
   }

   public void func_73790_e(boolean var1) {
      this.field_73823_s = var1;
   }

   public void setCursorPositionZero() {
      this.setCursorPosition(0);
   }

   public int getMaxStringLength() {
      return this.maxStringLength;
   }

   public boolean isFocused() {
      return this.isFocused;
   }

   public void setCursorPosition(int var1) {
      this.cursorPosition = var1;
      int var2 = this.text.length();
      if (this.cursorPosition < 0) {
         this.cursorPosition = 0;
      }

      if (this.cursorPosition > var2) {
         this.cursorPosition = var2;
      }

      this.func_73800_i(this.cursorPosition);
   }

   public int getWidth() {
      return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
   }

   public void setCursorPositionEnd() {
      this.setCursorPosition(this.text.length());
   }

   public boolean getEnableBackgroundDrawing() {
      return this.enableBackgroundDrawing;
   }

   public void func_73779_a(int var1) {
      if (this.text.length() != 0) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            this.deleteFromCursor(this.getNthWordFromCursor(var1) - this.cursorPosition);
         }
      }

   }
}
