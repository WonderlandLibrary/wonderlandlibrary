package my.NewSnake.utils.minecraft;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

public class GuiTextField extends Gui {
   private Predicate field_175209_y = Predicates.alwaysTrue();
   private int cursorPosition;
   private final int field_175208_g;
   public int xPosition;
   private boolean isEnabled = true;
   private String text = "";
   private int maxStringLength = Integer.MAX_VALUE;
   private final FontRenderer fontRendererInstance;
   private boolean isFocused;
   public int width;
   private int enabledColor = 14737632;
   private static final String __OBFID = "CL_00000670";
   private boolean enableBackgroundDrawing = true;
   private GuiPageButtonList.GuiResponder field_175210_x;
   private int cursorCounter;
   private int selectionEnd;
   private boolean canLoseFocus = true;
   private boolean visible = true;
   public int height;
   private int disabledColor = 7368816;
   public int yPosition;
   private int lineScrollOffset;

   public void func_175207_a(GuiPageButtonList.GuiResponder var1) {
      this.field_175210_x = var1;
   }

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
               var5 = String.valueOf(var5) + this.text.substring(var4);
            }

            this.text = var5;
            if (var2) {
               this.moveCursorBy(var1);
            }

            if (this.field_175210_x != null) {
               this.field_175210_x.func_175319_a(this.field_175208_g, this.text);
            }
         }
      }

   }

   public int getWidth() {
      return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
   }

   public void setSelectionPos(int var1) {
      int var2 = this.text.length();
      if (var1 > var2) {
         var1 = var2;
      }

      if (var1 < 0) {
         var1 = 0;
      }

      this.selectionEnd = var1;
      if (this.fontRendererInstance != null) {
         if (this.lineScrollOffset > var2) {
            this.lineScrollOffset = var2;
         }

         int var3 = this.getWidth();
         String var4 = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), var3);
         int var5 = var4.length() + this.lineScrollOffset;
         if (var1 == this.lineScrollOffset) {
            this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, var3, true).length();
         }

         if (var1 > var5) {
            this.lineScrollOffset += var1 - var5;
         } else if (var1 <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - var1;
         }

         this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, var2);
      }

   }

   public int getCursorPosition() {
      return this.cursorPosition;
   }

   public boolean getVisible() {
      return this.visible;
   }

   public void setTextColor(int var1) {
      this.enabledColor = var1;
   }

   public void setEnableBackgroundDrawing(boolean var1) {
      this.enableBackgroundDrawing = var1;
   }

   public void setCursorPositionZero() {
      this.setCursorPosition(0);
   }

   public boolean getEnableBackgroundDrawing() {
      return this.enableBackgroundDrawing;
   }

   public void writeText(String var1) {
      String var2 = "";
      String var3 = ChatAllowedCharacters.filterAllowedCharacters(var1);
      int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
      int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
      int var6 = this.maxStringLength - this.text.length() - (var4 - var5);
      boolean var7 = false;
      if (this.text.length() > 0) {
         var2 = String.valueOf(var2) + this.text.substring(0, var4);
      }

      int var8;
      if (var6 < var3.length()) {
         var2 = String.valueOf(var2) + var3.substring(0, var6);
         var8 = var6;
      } else {
         var2 = String.valueOf(var2) + var3;
         var8 = var3.length();
      }

      if (this.text.length() > 0 && var5 < this.text.length()) {
         var2 = String.valueOf(var2) + this.text.substring(var5);
      }

      if (this.field_175209_y.apply(var2)) {
         this.text = var2;
         this.moveCursorBy(var4 - this.selectionEnd + var8);
         if (this.field_175210_x != null) {
            this.field_175210_x.func_175319_a(this.field_175208_g, this.text);
         }
      }

   }

   public int getNthWordFromPos(int var1, int var2) {
      return this.func_146197_a(var1, var2, true);
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   public void updateCursorCounter() {
      ++this.cursorCounter;
   }

   public void deleteWords(int var1) {
      if (this.text.length() != 0) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            this.deleteFromCursor(this.getNthWordFromCursor(var1) - this.cursorPosition);
         }
      }

   }

   public int func_146197_a(int var1, int var2, boolean var3) {
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

   public void moveCursorBy(int var1) {
      this.setCursorPosition(this.selectionEnd + var1);
   }

   public void setMaxStringLength(int var1) {
      this.maxStringLength = var1;
      if (this.text.length() > var1) {
         this.text = this.text.substring(0, var1);
      }

   }

   public void drawTextBox() {
      if (this.getVisible()) {
         if (this.getEnableBackgroundDrawing()) {
            Gui.drawRect((double)(this.xPosition - 1), (double)(this.yPosition - 1), (double)(this.xPosition + this.width + 1), (double)(this.yPosition + this.height + 1), -6250336);
            Gui.drawRect((double)this.xPosition, (double)this.yPosition, (double)(this.xPosition + this.width), (double)(this.yPosition + this.height), -16777216);
         }

         int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
         int var2 = this.cursorPosition - this.lineScrollOffset;
         int var3 = this.selectionEnd - this.lineScrollOffset;
         String var4 = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
         boolean var5 = var2 >= 0 && var2 <= var4.length();
         boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
         int var7 = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
         int var8 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
         int var9 = var7;
         if (var3 > var4.length()) {
            var3 = var4.length();
         }

         if (var4.length() > 0) {
            String var10 = var5 ? var4.substring(0, var2) : var4;
            var9 = this.fontRendererInstance.drawString(var10, (double)((float)var7), (double)((float)var8), var1);
         }

         boolean var13 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
         int var11 = var9;
         if (!var5) {
            var11 = var2 > 0 ? var7 + this.width : var7;
         } else if (var13) {
            var11 = var9 - 1;
            --var9;
         }

         if (var4.length() > 0 && var5 && var2 < var4.length()) {
            var9 = this.fontRendererInstance.drawString(var4.substring(var2), (double)((float)var9), (double)((float)var8), var1);
         }

         if (var6) {
            if (var13) {
               Gui.drawRect((double)var11, (double)(var8 - 1), (double)(var11 + 1), (double)(var8 + 1 + this.fontRendererInstance.FONT_HEIGHT), -3092272);
            } else {
               this.fontRendererInstance.drawString("_", (double)((float)var11), (double)((float)var8), var1);
            }
         }

         if (var3 != var2) {
            int var12 = var7 + this.fontRendererInstance.getStringWidth(var4.substring(0, var3));
            this.drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRendererInstance.FONT_HEIGHT);
         }
      }

   }

   public boolean textboxKeyTyped(char var1, int var2) {
      if (!this.isFocused) {
         return false;
      } else if (GuiScreen.isKeyComboCtrlA(var2)) {
         this.setCursorPositionEnd();
         this.setSelectionPos(0);
         return true;
      } else if (GuiScreen.isKeyComboCtrlC(var2)) {
         GuiScreen.setClipboardString(this.getSelectedText());
         return true;
      } else if (GuiScreen.isKeyComboCtrlV(var2)) {
         if (this.isEnabled) {
            this.writeText(GuiScreen.getClipboardString());
         }

         return true;
      } else if (GuiScreen.isKeyComboCtrlX(var2)) {
         GuiScreen.setClipboardString(this.getSelectedText());
         if (this.isEnabled) {
            this.writeText("");
         }

         return true;
      } else {
         switch(var2) {
         case 14:
            if (GuiScreen.isCtrlKeyDown()) {
               if (this.isEnabled) {
                  this.deleteWords(-1);
               }
            } else if (this.isEnabled) {
               this.deleteFromCursor(-1);
            }

            return true;
         case 199:
            if (GuiScreen.isShiftKeyDown()) {
               this.setSelectionPos(0);
            } else {
               this.setCursorPositionZero();
            }

            return true;
         case 203:
            if (GuiScreen.isShiftKeyDown()) {
               if (GuiScreen.isCtrlKeyDown()) {
                  this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
               } else {
                  this.setSelectionPos(this.getSelectionEnd() - 1);
               }
            } else if (GuiScreen.isCtrlKeyDown()) {
               this.setCursorPosition(this.getNthWordFromCursor(-1));
            } else {
               this.moveCursorBy(-1);
            }

            return true;
         case 205:
            if (GuiScreen.isShiftKeyDown()) {
               if (GuiScreen.isCtrlKeyDown()) {
                  this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
               } else {
                  this.setSelectionPos(this.getSelectionEnd() + 1);
               }
            } else if (GuiScreen.isCtrlKeyDown()) {
               this.setCursorPosition(this.getNthWordFromCursor(1));
            } else {
               this.moveCursorBy(1);
            }

            return true;
         case 207:
            if (GuiScreen.isShiftKeyDown()) {
               this.setSelectionPos(this.text.length());
            } else {
               this.setCursorPositionEnd();
            }

            return true;
         case 211:
            if (GuiScreen.isCtrlKeyDown()) {
               if (this.isEnabled) {
                  this.deleteWords(1);
               }
            } else if (this.isEnabled) {
               this.deleteFromCursor(1);
            }

            return true;
         default:
            if (ChatAllowedCharacters.isAllowedCharacter(var1)) {
               if (this.isEnabled) {
                  this.writeText(Character.toString(var1));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void setCanLoseFocus(boolean var1) {
      this.canLoseFocus = var1;
   }

   public int getNthWordFromCursor(int var1) {
      return this.getNthWordFromPos(var1, this.getCursorPosition());
   }

   public void setDisabledTextColour(int var1) {
      this.disabledColor = var1;
   }

   public void mouseClicked(int var1, int var2, int var3) {
      boolean var4 = var1 >= this.xPosition && var1 < this.xPosition + this.width && var2 >= this.yPosition && var2 < this.yPosition + this.height;
      if (this.canLoseFocus) {
         this.setFocused(var4);
      }

      if (this.isFocused && var4 && var3 == 0) {
         int var5 = var1 - this.xPosition;
         if (this.enableBackgroundDrawing) {
            var5 -= 4;
         }

         String var6 = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
         this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(var6, var5).length() + this.lineScrollOffset);
      }

   }

   public void setEnabled(boolean var1) {
      this.isEnabled = var1;
   }

   public void setText(String var1) {
      if (this.field_175209_y.apply(var1)) {
         if (var1.length() > this.maxStringLength) {
            this.text = var1.substring(0, this.maxStringLength);
         } else {
            this.text = var1;
         }

         this.setCursorPositionEnd();
      }

   }

   public String getSelectedText() {
      int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
      int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
      return this.text.substring(var1, var2);
   }

   private void drawCursorVertical(int var1, int var2, int var3, int var4) {
      int var5;
      if (var1 < var3) {
         var5 = var1;
         var1 = var3;
         var3 = var5;
      }

      if (var2 < var4) {
         var5 = var2;
         var2 = var4;
         var4 = var5;
      }

      if (var3 > this.xPosition + this.width) {
         var3 = this.xPosition + this.width;
      }

      if (var1 > this.xPosition + this.width) {
         var1 = this.xPosition + this.width;
      }

      Tessellator var7 = Tessellator.getInstance();
      WorldRenderer var6 = var7.getWorldRenderer();
      GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.enableColorLogic();
      GlStateManager.colorLogicOp(5387);
      var6.begin(7, DefaultVertexFormats.POSITION);
      var6.pos((double)var1, (double)var4, 0.0D).endVertex();
      var6.pos((double)var3, (double)var4, 0.0D).endVertex();
      var6.pos((double)var3, (double)var2, 0.0D).endVertex();
      var6.pos((double)var1, (double)var2, 0.0D).endVertex();
      var7.draw();
      GlStateManager.disableColorLogic();
      GlStateManager.enableTexture2D();
   }

   public void setCursorPosition(int var1) {
      this.cursorPosition = var1;
      int var2 = this.text.length();
      this.setSelectionPos(this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, var2));
   }

   public int getMaxStringLength() {
      return this.maxStringLength;
   }

   public boolean isFocused() {
      return this.isFocused;
   }

   public int getSelectionEnd() {
      return this.selectionEnd;
   }

   public int func_175206_d() {
      return this.field_175208_g;
   }

   public void func_175205_a(Predicate var1) {
      this.field_175209_y = var1;
   }

   public void setFocused(boolean var1) {
      if (var1 && !this.isFocused) {
         this.cursorCounter = 0;
      }

      this.isFocused = var1;
   }

   public String getText() {
      return this.text;
   }

   public GuiTextField(int var1, FontRenderer var2, int var3, int var4, int var5, int var6) {
      this.field_175208_g = var1;
      this.fontRendererInstance = var2;
      this.xPosition = var3;
      this.yPosition = var4;
      this.width = var5;
      this.height = var6;
   }

   public void setCursorPositionEnd() {
      this.setCursorPosition(this.text.length());
   }
}
