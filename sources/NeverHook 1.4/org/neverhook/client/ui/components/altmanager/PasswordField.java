/*     */ package org.neverhook.client.ui.components.altmanager;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ 
/*     */ public class PasswordField
/*     */   extends Gui
/*     */ {
/*     */   private final FontRenderer fontRenderer;
/*     */   private final int xPos;
/*     */   private final int yPos;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final boolean isEnabled = true;
/*  24 */   private final int disabledColor = 7368816;
/*  25 */   public String text = "";
/*  26 */   public int maxStringLength = 50;
/*     */   public boolean isFocused = false;
/*     */   private int cursorCounter;
/*     */   private boolean enableBackgroundDrawing = true;
/*     */   private boolean canLoseFocus = true;
/*  31 */   private int field_73816_n = 0;
/*  32 */   private int cursorPosition = 0;
/*  33 */   private int selectionEnd = 0;
/*  34 */   private int enabledColor = 14737632;
/*     */   private boolean field_73823_s = true;
/*     */   
/*     */   public PasswordField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
/*  38 */     this.fontRenderer = par1FontRenderer;
/*  39 */     this.xPos = par2;
/*  40 */     this.yPos = par3;
/*  41 */     this.width = par4;
/*  42 */     this.height = par5;
/*     */   }
/*     */   
/*     */   public void updateCursorCounter() {
/*  46 */     this.cursorCounter++;
/*     */   }
/*     */   
/*     */   public String getText() {
/*  50 */     return this.text.replaceAll(" ", "");
/*     */   }
/*     */   
/*     */   public void setText(String par1Str) {
/*  54 */     if (par1Str.length() > this.maxStringLength) {
/*  55 */       this.text = par1Str.substring(0, this.maxStringLength);
/*     */     } else {
/*  57 */       this.text = par1Str;
/*     */     } 
/*     */     
/*  60 */     setCursorPositionEnd();
/*     */   }
/*     */   
/*     */   public String getSelectedtext() {
/*  64 */     int var1 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
/*  65 */     int var2 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
/*  66 */     return this.text.substring(var1, var2);
/*     */   }
/*     */   public void writeText(String par1Str) {
/*     */     int var8;
/*  70 */     String var2 = "";
/*  71 */     String var3 = ChatAllowedCharacters.filterAllowedCharacters(par1Str);
/*  72 */     int var4 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
/*  73 */     int var5 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
/*  74 */     int var6 = this.maxStringLength - this.text.length() - var4 - this.selectionEnd;
/*  75 */     boolean var7 = false;
/*  76 */     if (this.text.length() > 0) {
/*  77 */       var2 = var2 + this.text.substring(0, var4);
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (var6 < var3.length()) {
/*  82 */       var2 = var2 + var3.substring(0, var6);
/*  83 */       var8 = var6;
/*     */     } else {
/*  85 */       var2 = var2 + var3;
/*  86 */       var8 = var3.length();
/*     */     } 
/*     */     
/*  89 */     if (this.text.length() > 0 && var5 < this.text.length()) {
/*  90 */       var2 = var2 + this.text.substring(var5);
/*     */     }
/*     */     
/*  93 */     this.text = var2.replaceAll(" ", "");
/*  94 */     func_73784_d(var4 - this.selectionEnd + var8);
/*     */   }
/*     */   
/*     */   public void func_73779_a(int par1) {
/*  98 */     if (this.text.length() != 0) {
/*  99 */       if (this.selectionEnd != this.cursorPosition) {
/* 100 */         writeText("");
/*     */       } else {
/* 102 */         deleteFromCursor(getNthWordFromCursor(par1) - this.cursorPosition);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteFromCursor(int par1) {
/* 109 */     if (this.text.length() != 0) {
/* 110 */       if (this.selectionEnd != this.cursorPosition) {
/* 111 */         writeText("");
/*     */       } else {
/* 113 */         boolean var2 = (par1 < 0);
/* 114 */         int var3 = var2 ? (this.cursorPosition + par1) : this.cursorPosition;
/* 115 */         int var4 = var2 ? this.cursorPosition : (this.cursorPosition + par1);
/* 116 */         String var5 = "";
/* 117 */         if (var3 >= 0) {
/* 118 */           var5 = this.text.substring(0, var3);
/*     */         }
/*     */         
/* 121 */         if (var4 < this.text.length()) {
/* 122 */           var5 = var5 + this.text.substring(var4);
/*     */         }
/*     */         
/* 125 */         this.text = var5;
/* 126 */         if (var2) {
/* 127 */           func_73784_d(par1);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNthWordFromCursor(int par1) {
/* 135 */     return getNthWordFromPos(par1, getCursorPosition());
/*     */   }
/*     */   
/*     */   public int getNthWordFromPos(int par1, int par2) {
/* 139 */     return func_73798_a(par1, getCursorPosition(), true);
/*     */   }
/*     */   
/*     */   public int func_73798_a(int par1, int par2, boolean par3) {
/* 143 */     int var4 = par2;
/* 144 */     boolean var5 = (par1 < 0);
/* 145 */     int var6 = Math.abs(par1);
/*     */     
/* 147 */     for (int var7 = 0; var7 < var6; var7++) {
/* 148 */       if (!var5) {
/* 149 */         int var8 = this.text.length();
/* 150 */         var4 = this.text.indexOf(' ', var4);
/* 151 */         if (var4 == -1) {
/* 152 */           var4 = var8;
/*     */         } else {
/* 154 */           while (par3 && var4 < var8 && this.text.charAt(var4) == ' ') {
/* 155 */             var4++;
/*     */           }
/*     */         } 
/*     */       } else {
/* 159 */         while (par3 && var4 > 0 && this.text.charAt(var4 - 1) == ' ') {
/* 160 */           var4--;
/*     */         }
/*     */         
/* 163 */         while (var4 > 0 && this.text.charAt(var4 - 1) != ' ') {
/* 164 */           var4--;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     return var4;
/*     */   }
/*     */   
/*     */   public void func_73784_d(int par1) {
/* 173 */     setCursorPosition(this.selectionEnd + par1);
/*     */   }
/*     */   
/*     */   public void setCursorPositionZero() {
/* 177 */     setCursorPosition(0);
/*     */   }
/*     */   
/*     */   public void setCursorPositionEnd() {
/* 181 */     setCursorPosition(this.text.length());
/*     */   }
/*     */   
/*     */   public boolean textboxKeyTyped(char par1, int par2) {
/* 185 */     getClass(); if (this.isFocused) {
/* 186 */       switch (par1) {
/*     */         case '\001':
/* 188 */           setCursorPositionEnd();
/* 189 */           func_73800_i(0);
/* 190 */           return true;
/*     */         case '\003':
/* 192 */           GuiScreen.setClipboardString(getSelectedtext());
/* 193 */           return true;
/*     */         case '\026':
/* 195 */           writeText(GuiScreen.getClipboardString());
/* 196 */           return true;
/*     */         case '\030':
/* 198 */           GuiScreen.setClipboardString(getSelectedtext());
/* 199 */           writeText("");
/* 200 */           return true;
/*     */       } 
/* 202 */       switch (par2) {
/*     */         case 14:
/* 204 */           if (GuiScreen.isCtrlKeyDown()) {
/* 205 */             func_73779_a(-1);
/*     */           } else {
/* 207 */             deleteFromCursor(-1);
/*     */           } 
/*     */           
/* 210 */           return true;
/*     */         case 199:
/* 212 */           if (GuiScreen.isShiftKeyDown()) {
/* 213 */             func_73800_i(0);
/*     */           } else {
/* 215 */             setCursorPositionZero();
/*     */           } 
/*     */           
/* 218 */           return true;
/*     */         case 203:
/* 220 */           if (GuiScreen.isShiftKeyDown()) {
/* 221 */             if (GuiScreen.isCtrlKeyDown()) {
/* 222 */               func_73800_i(getNthWordFromPos(-1, getSelectionEnd()));
/*     */             } else {
/* 224 */               func_73800_i(getSelectionEnd() - 1);
/*     */             } 
/* 226 */           } else if (GuiScreen.isCtrlKeyDown()) {
/* 227 */             setCursorPosition(getNthWordFromCursor(-1));
/*     */           } else {
/* 229 */             func_73784_d(-1);
/*     */           } 
/*     */           
/* 232 */           return true;
/*     */         case 205:
/* 234 */           if (GuiScreen.isShiftKeyDown()) {
/* 235 */             if (GuiScreen.isCtrlKeyDown()) {
/* 236 */               func_73800_i(getNthWordFromPos(1, getSelectionEnd()));
/*     */             } else {
/* 238 */               func_73800_i(getSelectionEnd() + 1);
/*     */             } 
/* 240 */           } else if (GuiScreen.isCtrlKeyDown()) {
/* 241 */             setCursorPosition(getNthWordFromCursor(1));
/*     */           } else {
/* 243 */             func_73784_d(1);
/*     */           } 
/*     */           
/* 246 */           return true;
/*     */         case 207:
/* 248 */           if (GuiScreen.isShiftKeyDown()) {
/* 249 */             func_73800_i(this.text.length());
/*     */           } else {
/* 251 */             setCursorPositionEnd();
/*     */           } 
/*     */           
/* 254 */           return true;
/*     */         case 211:
/* 256 */           if (GuiScreen.isCtrlKeyDown()) {
/* 257 */             func_73779_a(1);
/*     */           } else {
/* 259 */             deleteFromCursor(1);
/*     */           } 
/*     */           
/* 262 */           return true;
/*     */       } 
/* 264 */       if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
/* 265 */         writeText(Character.toString(par1));
/* 266 */         return true;
/*     */       } 
/* 268 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 273 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int par1, int par2, int par3) {
/* 278 */     boolean var4 = (par1 >= this.xPos && par1 < this.xPos + this.width && par2 >= this.yPos && par2 < this.yPos + this.height);
/* 279 */     if (this.canLoseFocus) {
/* 280 */       getClass(); setFocused(var4);
/*     */     } 
/*     */     
/* 283 */     if (this.isFocused && par3 == 0) {
/* 284 */       int var5 = par1 - this.xPos;
/* 285 */       if (this.enableBackgroundDrawing) {
/* 286 */         var5 -= 4;
/*     */       }
/*     */       
/* 289 */       String var6 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), getWidth());
/* 290 */       setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.field_73816_n);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawTextBox() {
/* 296 */     if (func_73778_q()) {
/* 297 */       if (getEnableBackgroundDrawing()) {
/* 298 */         RectHelper.drawRect(this.xPos, this.yPos, (this.xPos + this.width), (this.yPos + this.height), (new Color(30, 30, 30, 185)).getRGB());
/* 299 */         RectHelper.drawGradientRect(this.xPos, this.yPos, (this.xPos + this.width), (this.yPos + this.height), (new Color(1, 1, 1, 255)).getRGB(), (new Color(5, 5, 5, 255)).getRGB());
/*     */       } 
/* 301 */       getClass(); int var1 = this.enabledColor;
/* 302 */       int var2 = this.cursorPosition - this.field_73816_n;
/* 303 */       int var3 = this.selectionEnd - this.field_73816_n;
/* 304 */       String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), getWidth());
/* 305 */       boolean var5 = (var2 >= 0 && var2 <= var4.length());
/* 306 */       boolean var6 = (this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5);
/* 307 */       int var7 = this.enableBackgroundDrawing ? (this.xPos + 4) : this.xPos;
/* 308 */       int var8 = this.enableBackgroundDrawing ? (this.yPos + (this.height - 8) / 2) : this.yPos;
/* 309 */       int var9 = var7;
/* 310 */       if (var3 > var4.length()) {
/* 311 */         var3 = var4.length();
/*     */       }
/*     */       
/* 314 */       if (var4.length() > 0) {
/* 315 */         if (var5) {
/* 316 */           var4.substring(0, var2);
/*     */         }
/*     */         
/* 319 */         var9 = (Minecraft.getInstance()).fontRendererObj.drawStringWithShadow(this.text.replaceAll("(?s).", "*"), var7, var8, var1);
/*     */       } 
/*     */       
/* 322 */       boolean var10 = (this.cursorPosition < this.text.length() || this.text.length() >= getMaxStringLength());
/* 323 */       int var11 = var9;
/* 324 */       if (!var5) {
/* 325 */         var11 = (var2 > 0) ? (var7 + this.width) : var7;
/* 326 */       } else if (var10) {
/* 327 */         var11 = var9 - 1;
/* 328 */         var9--;
/*     */       } 
/*     */       
/* 331 */       if (var4.length() > 0 && var5 && var2 < var4.length()) {
/* 332 */         (Minecraft.getInstance()).fontRendererObj.drawStringWithShadow(var4.substring(var2), var9, var8, var1);
/*     */       }
/*     */       
/* 335 */       if (var6) {
/* 336 */         if (var10) {
/* 337 */           Gui.drawRect(var11, (var8 - 1), (var11 + 1), (var8 + 1 + this.fontRenderer.FONT_HEIGHT), -3092272);
/*     */         } else {
/* 339 */           (Minecraft.getInstance()).fontRendererObj.drawStringWithShadow("_", var11, var8, var1);
/*     */         } 
/*     */       }
/*     */       
/* 343 */       if (var3 != var2) {
/* 344 */         int var12 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
/* 345 */         drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawCursorVertical(int par1, int par2, int par3, int par4) {
/* 352 */     if (par1 < par3) {
/* 353 */       int var5 = par1;
/* 354 */       par1 = par3;
/* 355 */       par3 = var5;
/*     */     } 
/*     */     
/* 358 */     if (par2 < par4) {
/* 359 */       int var5 = par2;
/* 360 */       par2 = par4;
/* 361 */       par4 = var5;
/*     */     } 
/*     */     
/* 364 */     Tessellator var6 = Tessellator.getInstance();
/* 365 */     BufferBuilder var7 = var6.getBuffer();
/* 366 */     GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
/* 367 */     GL11.glDisable(3553);
/* 368 */     GL11.glEnable(3058);
/* 369 */     GL11.glLogicOp(5387);
/* 370 */     var7.begin(7, DefaultVertexFormats.POSITION);
/* 371 */     var7.pos(par1, par4, 0.0D).endVertex();
/* 372 */     var7.pos(par3, par4, 0.0D).endVertex();
/* 373 */     var7.pos(par3, par2, 0.0D).endVertex();
/* 374 */     var7.pos(par1, par2, 0.0D).endVertex();
/* 375 */     var6.draw();
/* 376 */     GL11.glDisable(3058);
/* 377 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public int getMaxStringLength() {
/* 381 */     return this.maxStringLength;
/*     */   }
/*     */   
/*     */   public void setMaxStringLength(int par1) {
/* 385 */     this.maxStringLength = par1;
/* 386 */     if (this.text.length() > par1) {
/* 387 */       this.text = this.text.substring(0, par1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCursorPosition() {
/* 393 */     return this.cursorPosition;
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int par1) {
/* 397 */     this.cursorPosition = par1;
/* 398 */     int var2 = this.text.length();
/* 399 */     if (this.cursorPosition < 0) {
/* 400 */       this.cursorPosition = 0;
/*     */     }
/*     */     
/* 403 */     if (this.cursorPosition > var2) {
/* 404 */       this.cursorPosition = var2;
/*     */     }
/*     */     
/* 407 */     func_73800_i(this.cursorPosition);
/*     */   }
/*     */   
/*     */   public boolean getEnableBackgroundDrawing() {
/* 411 */     return this.enableBackgroundDrawing;
/*     */   }
/*     */   
/*     */   public void setEnableBackgroundDrawing(boolean par1) {
/* 415 */     this.enableBackgroundDrawing = par1;
/*     */   }
/*     */   
/*     */   public void func_73794_g(int par1) {
/* 419 */     this.enabledColor = par1;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 423 */     return this.isFocused;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean par1) {
/* 427 */     if (par1 && !this.isFocused) {
/* 428 */       this.cursorCounter = 0;
/*     */     }
/*     */     
/* 431 */     this.isFocused = par1;
/*     */   }
/*     */   
/*     */   public int getSelectionEnd() {
/* 435 */     return this.selectionEnd;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 439 */     return getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
/*     */   }
/*     */   
/*     */   public void func_73800_i(int par1) {
/* 443 */     int var2 = this.text.length();
/* 444 */     if (par1 > var2) {
/* 445 */       par1 = var2;
/*     */     }
/*     */     
/* 448 */     if (par1 < 0) {
/* 449 */       par1 = 0;
/*     */     }
/*     */     
/* 452 */     this.selectionEnd = par1;
/* 453 */     if (this.fontRenderer != null) {
/* 454 */       if (this.field_73816_n > var2) {
/* 455 */         this.field_73816_n = var2;
/*     */       }
/*     */       
/* 458 */       int var3 = getWidth();
/* 459 */       String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.field_73816_n), var3);
/* 460 */       int var5 = var4.length() + this.field_73816_n;
/* 461 */       if (par1 == this.field_73816_n) {
/* 462 */         this.field_73816_n -= this.fontRenderer.trimStringToWidth(this.text, var3, true).length();
/*     */       }
/*     */       
/* 465 */       if (par1 > var5) {
/* 466 */         this.field_73816_n += par1 - var5;
/* 467 */       } else if (par1 <= this.field_73816_n) {
/* 468 */         this.field_73816_n -= this.field_73816_n - par1;
/*     */       } 
/*     */       
/* 471 */       if (this.field_73816_n < 0) {
/* 472 */         this.field_73816_n = 0;
/*     */       }
/*     */       
/* 475 */       if (this.field_73816_n > var2) {
/* 476 */         this.field_73816_n = var2;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCanLoseFocus(boolean par1) {
/* 483 */     this.canLoseFocus = par1;
/*     */   }
/*     */   
/*     */   public boolean func_73778_q() {
/* 487 */     return this.field_73823_s;
/*     */   }
/*     */   
/*     */   public void func_73790_e(boolean par1) {
/* 491 */     this.field_73823_s = par1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\PasswordField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */