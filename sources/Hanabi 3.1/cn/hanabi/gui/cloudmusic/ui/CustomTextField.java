package cn.hanabi.gui.cloudmusic.ui;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Hanabi;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

@ObfuscationClass
public class CustomTextField {
   public String textString;
   public float x;
   public float y;
   public boolean isFocused;
   public boolean isTyping;
   public boolean back;
   public int ticks = 0;
   public int selectedChar;
   public float offset;
   public float newTextWidth;
   public float oldTextWidth;
   public float charWidth;
   public String oldString;
   public StringBuilder stringBuilder;

   public CustomTextField(String text) {
      this.textString = text;
      this.selectedChar = this.textString.length();
   }

   public void draw(float x, float y) {
      this.x = x;
      this.y = y;
      if (this.selectedChar > this.textString.length()) {
         this.selectedChar = this.textString.length();
      } else if (this.selectedChar < 0) {
         this.selectedChar = 0;
      }

      int selectedChar = this.selectedChar;
      RenderUtil.drawRoundedRect(this.x, this.y + 3.0F, this.x + 115.0F, this.y + 15.0F, 1.0F, -13355204);
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)this.x + 1, (int)this.y + 3, 113, 11);
      Hanabi.INSTANCE.fontManager.wqy13.drawString(this.textString, this.x + 1.5F - this.offset, this.y + 4.0F, Color.GRAY.getRGB());
      if (this.isFocused) {
         float width = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString.substring(0, selectedChar)) + 4);
         float posX = this.x + width - this.offset;
         RenderUtil.drawRect(posX - 0.5F, this.y + 5.5F, posX, this.y + 12.5F, RenderUtil.reAlpha(Color.GRAY.getRGB(), this.ticks / 500 % 2 == 0 ? 1.0F : 0.0F));
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      this.tick();
   }

   public void tick() {
      if (this.isFocused) {
         ++this.ticks;
      } else {
         this.ticks = 0;
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseID) {
      boolean hovering = RenderUtil.isHovering(mouseX, mouseY, this.x, this.y + 3.0F, this.x + 115.0F, this.y + 15.0F);
      if (hovering && mouseID == 0 && !this.isFocused) {
         this.isFocused = true;
         this.selectedChar = this.textString.length();
      } else if (!hovering) {
         this.isFocused = false;
         this.isTyping = false;
      }

   }

   public void keyPressed(int key) {
      if (key == 1) {
         this.isFocused = false;
         this.isTyping = false;
      }

      if (this.isFocused) {
         if (GuiScreen.isKeyComboCtrlV(key)) {
            this.textString = GuiScreen.getClipboardString();
            return;
         }

         float width;
         float barOffset;
         switch (key) {
            case 14:
               try {
                  if (this.selectedChar > 0 && this.textString.length() != 0) {
                     this.oldString = this.textString;
                     this.stringBuilder = new StringBuilder(this.oldString);
                     this.stringBuilder.charAt(this.selectedChar - 1);
                     this.stringBuilder.deleteCharAt(this.selectedChar - 1);
                     this.textString = ChatAllowedCharacters.filterAllowedCharacters(this.stringBuilder.toString());
                     --this.selectedChar;
                     if ((float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.oldString) + 2) > 111.0F && this.offset > 0.0F) {
                        this.newTextWidth = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString) + 2);
                        this.oldTextWidth = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.oldString) + 2);
                        this.charWidth = this.newTextWidth - this.oldTextWidth;
                        if (this.newTextWidth <= 111.0F && this.oldTextWidth - 111.0F > this.charWidth) {
                           this.charWidth = 111.0F - this.oldTextWidth;
                        }

                        this.offset += this.charWidth;
                     }

                     if (this.selectedChar > this.textString.length()) {
                        this.selectedChar = this.textString.length();
                     }

                     this.ticks = 0;
                  }
               } catch (Exception var7) {
               }
               break;
            case 28:
               this.isFocused = false;
               this.isTyping = false;
               this.ticks = 0;
               break;
            case 199:
               this.selectedChar = 0;
               this.offset = 0.0F;
               this.ticks = 0;
               break;
            case 203:
               if (this.selectedChar > 0) {
                  --this.selectedChar;
               }

               width = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2);
               barOffset = width - this.offset;
               barOffset -= 2.0F;
               if (barOffset < 0.0F) {
                  this.offset += barOffset;
               }

               this.ticks = 0;
               break;
            case 205:
               if (this.selectedChar < this.textString.length()) {
                  ++this.selectedChar;
               }

               width = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2);
               barOffset = width - this.offset;
               if (barOffset > 111.0F) {
                  this.offset += barOffset - 111.0F;
               }

               this.ticks = 0;
               break;
            case 207:
               this.selectedChar = this.textString.length();
               width = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2);
               barOffset = width - this.offset;
               if (barOffset > 111.0F) {
                  this.offset += barOffset - 111.0F;
               }

               this.ticks = 0;
               break;
            case 210:
               Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
               Transferable clipTf = sysClip.getContents((Object)null);
               if (clipTf != null && clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                  try {
                     this.textString = (String)clipTf.getTransferData(DataFlavor.stringFlavor);
                  } catch (Exception var8) {
                     var8.printStackTrace();
                  }
               }

               this.selectedChar = this.textString.length();
               width = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2);
               barOffset = width - this.offset;
               if (barOffset > 111.0F) {
                  this.offset += barOffset - 111.0F;
               }
         }
      }

   }

   public void charTyped(char c) {
      if (this.isFocused && ChatAllowedCharacters.isAllowedCharacter(c)) {
         if (!this.isTyping) {
            this.isTyping = true;
         }

         this.oldString = this.textString;
         this.stringBuilder = new StringBuilder(this.oldString);
         this.stringBuilder.insert(this.selectedChar, c);
         this.textString = ChatAllowedCharacters.filterAllowedCharacters(this.stringBuilder.toString());
         if (this.selectedChar > this.textString.length()) {
            this.selectedChar = this.textString.length();
         } else if (this.selectedChar == this.oldString.length() && this.textString.startsWith(this.oldString)) {
            this.selectedChar += this.textString.length() - this.oldString.length();
         } else {
            ++this.selectedChar;
            float width = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2);
            this.newTextWidth = width - this.offset;
            if (this.newTextWidth > 111.0F) {
               this.offset += this.newTextWidth - 111.0F;
            }
         }

         this.newTextWidth = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.textString) + 2);
         this.oldTextWidth = (float)(Hanabi.INSTANCE.fontManager.wqy13.getStringWidth(this.oldString) + 2);
         if (this.newTextWidth > 111.0F) {
            if (this.oldTextWidth < 111.0F) {
               this.oldTextWidth = 111.0F;
            }

            this.charWidth = this.newTextWidth - this.oldTextWidth;
            if (this.selectedChar == this.textString.length()) {
               this.offset += this.charWidth;
            }
         }

         this.ticks = 0;
      }

   }
}
