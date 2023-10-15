/*     */ package org.neverhook.client.ui.clickgui;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiPageButtonList;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiSearcher
/*     */   extends Gui
/*     */ {
/*     */   private final int id;
/*     */   private final FontRenderer fontRendererInstance;
/*     */   private final int width;
/*     */   private final int height;
/*     */   public int xPosition;
/*     */   public int yPosition;
/*  32 */   public int maxStringLength = 32;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFocused;
/*     */ 
/*     */ 
/*     */   
/*  40 */   private String text = "";
/*     */ 
/*     */   
/*     */   private int cursorCounter;
/*     */ 
/*     */   
/*     */   private boolean enableBackgroundDrawing = true;
/*     */ 
/*     */   
/*     */   private boolean canLoseFocus = true;
/*     */ 
/*     */   
/*     */   private boolean isEnabled = true;
/*     */ 
/*     */   
/*     */   private int lineScrollOffset;
/*     */ 
/*     */   
/*     */   private int cursorPosition;
/*     */   
/*     */   private int selectionEnd;
/*     */   
/*  62 */   private int enabledColor = 14737632;
/*  63 */   private int disabledColor = 7368816;
/*     */ 
/*     */   
/*     */   private boolean visible = true;
/*     */   
/*     */   private GuiPageButtonList.GuiResponder guiResponder;
/*     */   
/*  70 */   private Predicate<String> validator = Predicates.alwaysTrue();
/*     */   
/*     */   public GuiSearcher(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
/*  73 */     this.id = componentId;
/*  74 */     this.fontRendererInstance = fontrendererObj;
/*  75 */     this.xPosition = x;
/*  76 */     this.yPosition = y;
/*  77 */     this.width = par5Width;
/*  78 */     this.height = par6Height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGuiResponder(GuiPageButtonList.GuiResponder guiResponderIn) {
/*  85 */     this.guiResponder = guiResponderIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCursorCounter() {
/*  92 */     this.cursorCounter++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/*  99 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String textIn) {
/* 106 */     if (this.validator.apply(textIn)) {
/* 107 */       if (textIn.length() > this.maxStringLength) {
/* 108 */         this.text = textIn.substring(0, this.maxStringLength);
/*     */       } else {
/* 110 */         this.text = textIn;
/*     */       } 
/*     */       
/* 113 */       setCursorPositionEnd();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSelectedText() {
/* 121 */     int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
/* 122 */     int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
/* 123 */     return this.text.substring(i, j);
/*     */   }
/*     */   
/*     */   public void setValidator(Predicate<String> theValidator) {
/* 127 */     this.validator = theValidator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeText(String textToWrite) {
/*     */     int l;
/* 134 */     String s = "";
/* 135 */     String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
/* 136 */     int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
/* 137 */     int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
/* 138 */     int k = this.maxStringLength - this.text.length() - i - j;
/*     */     
/* 140 */     if (!this.text.isEmpty()) {
/* 141 */       s = s + this.text.substring(0, i);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (k < s1.length()) {
/* 147 */       s = s + s1.substring(0, k);
/* 148 */       l = k;
/*     */     } else {
/* 150 */       s = s + s1;
/* 151 */       l = s1.length();
/*     */     } 
/*     */     
/* 154 */     if (!this.text.isEmpty() && j < this.text.length()) {
/* 155 */       s = s + this.text.substring(j);
/*     */     }
/*     */     
/* 158 */     if (this.validator.apply(s)) {
/* 159 */       this.text = s;
/* 160 */       moveCursorBy(i - this.selectionEnd + l);
/* 161 */       func_190516_a(this.id, this.text);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void func_190516_a(int p_190516_1_, String p_190516_2_) {
/* 166 */     if (this.guiResponder != null) {
/* 167 */       this.guiResponder.setEntryValue(p_190516_1_, p_190516_2_);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteWords(int num) {
/* 176 */     if (!this.text.isEmpty()) {
/* 177 */       if (this.selectionEnd != this.cursorPosition) {
/* 178 */         writeText("");
/*     */       } else {
/* 180 */         deleteFromCursor(getNthWordFromCursor(num) - this.cursorPosition);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteFromCursor(int num) {
/* 190 */     if (!this.text.isEmpty()) {
/* 191 */       if (this.selectionEnd != this.cursorPosition) {
/* 192 */         writeText("");
/*     */       } else {
/* 194 */         boolean flag = (num < 0);
/* 195 */         int i = flag ? (this.cursorPosition + num) : this.cursorPosition;
/* 196 */         int j = flag ? this.cursorPosition : (this.cursorPosition + num);
/* 197 */         String s = "";
/*     */         
/* 199 */         if (i >= 0) {
/* 200 */           s = this.text.substring(0, i);
/*     */         }
/*     */         
/* 203 */         if (j < this.text.length()) {
/* 204 */           s = s + this.text.substring(j);
/*     */         }
/*     */         
/* 207 */         if (this.validator.apply(s)) {
/* 208 */           this.text = s;
/*     */           
/* 210 */           if (flag) {
/* 211 */             moveCursorBy(num);
/*     */           }
/*     */           
/* 214 */           func_190516_a(this.id, this.text);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public int getId() {
/* 221 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNthWordFromCursor(int numWords) {
/* 228 */     return getNthWordFromPos(numWords, getCursorPosition());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNthWordFromPos(int n, int pos) {
/* 235 */     return getNthWordFromPosWS(n, pos, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
/* 242 */     int i = pos;
/* 243 */     boolean flag = (n < 0);
/* 244 */     int j = Math.abs(n);
/*     */     
/* 246 */     for (int k = 0; k < j; k++) {
/* 247 */       if (!flag) {
/* 248 */         int l = this.text.length();
/* 249 */         i = this.text.indexOf(' ', i);
/*     */         
/* 251 */         if (i == -1) {
/* 252 */           i = l;
/*     */         } else {
/* 254 */           while (skipWs && i < l && this.text.charAt(i) == ' ') {
/* 255 */             i++;
/*     */           }
/*     */         } 
/*     */       } else {
/* 259 */         while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
/* 260 */           i--;
/*     */         }
/*     */         
/* 263 */         while (i > 0 && this.text.charAt(i - 1) != ' ') {
/* 264 */           i--;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 269 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void moveCursorBy(int num) {
/* 276 */     setCursorPosition(this.selectionEnd + num);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPositionZero() {
/* 283 */     setCursorPosition(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPositionEnd() {
/* 290 */     setCursorPosition(this.text.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean textboxKeyTyped(char typedChar, int keyCode) {
/* 297 */     if (!this.isFocused)
/* 298 */       return false; 
/* 299 */     if (GuiScreen.isKeyComboCtrlA(keyCode)) {
/* 300 */       setCursorPositionEnd();
/* 301 */       setSelectionPos(0);
/* 302 */       return true;
/* 303 */     }  if (GuiScreen.isKeyComboCtrlC(keyCode)) {
/* 304 */       GuiScreen.setClipboardString(getSelectedText());
/* 305 */       return true;
/* 306 */     }  if (GuiScreen.isKeyComboCtrlV(keyCode)) {
/* 307 */       if (this.isEnabled) {
/* 308 */         writeText(GuiScreen.getClipboardString());
/*     */       }
/*     */       
/* 311 */       return true;
/* 312 */     }  if (GuiScreen.isKeyComboCtrlX(keyCode)) {
/* 313 */       GuiScreen.setClipboardString(getSelectedText());
/*     */       
/* 315 */       if (this.isEnabled) {
/* 316 */         writeText("");
/*     */       }
/*     */       
/* 319 */       return true;
/*     */     } 
/* 321 */     switch (keyCode) {
/*     */       case 14:
/* 323 */         if (GuiScreen.isCtrlKeyDown()) {
/* 324 */           if (this.isEnabled) {
/* 325 */             deleteWords(-1);
/*     */           }
/* 327 */         } else if (this.isEnabled) {
/* 328 */           deleteFromCursor(-1);
/*     */         } 
/*     */         
/* 331 */         return true;
/*     */       
/*     */       case 199:
/* 334 */         if (GuiScreen.isShiftKeyDown()) {
/* 335 */           setSelectionPos(0);
/*     */         } else {
/* 337 */           setCursorPositionZero();
/*     */         } 
/*     */         
/* 340 */         return true;
/*     */       
/*     */       case 203:
/* 343 */         if (GuiScreen.isShiftKeyDown()) {
/* 344 */           if (GuiScreen.isCtrlKeyDown()) {
/* 345 */             setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
/*     */           } else {
/* 347 */             setSelectionPos(getSelectionEnd() - 1);
/*     */           } 
/* 349 */         } else if (GuiScreen.isCtrlKeyDown()) {
/* 350 */           setCursorPosition(getNthWordFromCursor(-1));
/*     */         } else {
/* 352 */           moveCursorBy(-1);
/*     */         } 
/*     */         
/* 355 */         return true;
/*     */       
/*     */       case 205:
/* 358 */         if (GuiScreen.isShiftKeyDown()) {
/* 359 */           if (GuiScreen.isCtrlKeyDown()) {
/* 360 */             setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
/*     */           } else {
/* 362 */             setSelectionPos(getSelectionEnd() + 1);
/*     */           } 
/* 364 */         } else if (GuiScreen.isCtrlKeyDown()) {
/* 365 */           setCursorPosition(getNthWordFromCursor(1));
/*     */         } else {
/* 367 */           moveCursorBy(1);
/*     */         } 
/*     */         
/* 370 */         return true;
/*     */       
/*     */       case 207:
/* 373 */         if (GuiScreen.isShiftKeyDown()) {
/* 374 */           setSelectionPos(this.text.length());
/*     */         } else {
/* 376 */           setCursorPositionEnd();
/*     */         } 
/*     */         
/* 379 */         return true;
/*     */       
/*     */       case 211:
/* 382 */         if (GuiScreen.isCtrlKeyDown()) {
/* 383 */           if (this.isEnabled) {
/* 384 */             deleteWords(1);
/*     */           }
/* 386 */         } else if (this.isEnabled) {
/* 387 */           deleteFromCursor(1);
/*     */         } 
/*     */         
/* 390 */         return true;
/*     */     } 
/*     */     
/* 393 */     if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
/* 394 */       if (this.isEnabled) {
/* 395 */         writeText(Character.toString(typedChar));
/*     */       }
/*     */       
/* 398 */       return true;
/*     */     } 
/* 400 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 410 */     boolean flag = (mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition && mouseY < this.yPosition + this.height);
/*     */     
/* 412 */     if (this.canLoseFocus) {
/* 413 */       setFocused(flag);
/*     */     }
/*     */     
/* 416 */     if (this.isFocused && flag && mouseButton == 0) {
/* 417 */       int i = mouseX - this.xPosition;
/*     */       
/* 419 */       if (this.enableBackgroundDrawing) {
/* 420 */         i -= 4;
/*     */       }
/*     */       
/* 423 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
/* 424 */       setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, i).length() + this.lineScrollOffset);
/* 425 */       return true;
/*     */     } 
/* 427 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawTextBox() {
/* 435 */     if (getVisible()) {
/* 436 */       if (getEnableBackgroundDrawing()) {
/* 437 */         RectHelper.drawRect(this.xPosition, this.yPosition, (this.xPosition + this.width), (this.yPosition + this.height), (new Color(30, 30, 30, 185)).getRGB());
/* 438 */         RectHelper.drawGradientRect(this.xPosition, this.yPosition, (this.xPosition + this.width), (this.yPosition + this.height), (new Color(30, 30, 30, 190)).getRGB(), (new Color(30, 30, 30, 190)).getRGB());
/*     */       } 
/* 440 */       int i = -1;
/* 441 */       int j = this.cursorPosition - this.lineScrollOffset;
/* 442 */       int k = this.selectionEnd - this.lineScrollOffset;
/* 443 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
/* 444 */       boolean flag = (j >= 0 && j <= s.length());
/* 445 */       boolean flag1 = (this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag);
/* 446 */       int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
/* 447 */       int i1 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
/* 448 */       int j1 = l;
/*     */       
/* 450 */       if (k > s.length()) {
/* 451 */         k = s.length();
/*     */       }
/*     */       
/* 454 */       if (!s.isEmpty()) {
/*     */         
/* 456 */         String s1 = flag ? s.substring(0, j) : s;
/*     */         
/* 458 */         if (NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.warpSpoof.getBoolValue() && 
/* 459 */           s1.startsWith("/warp")) {
/* 460 */           s1 = "/warp ";
/*     */         }
/*     */         
/* 463 */         if (NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.loginSpoof.getBoolValue() && 
/* 464 */           s1.startsWith("/l")) {
/* 465 */           s1 = "/l ";
/*     */         }
/*     */ 
/*     */         
/* 469 */         j1 = this.fontRendererInstance.drawStringWithShadow(s1, l, i1, i);
/*     */       } 
/*     */       
/* 472 */       boolean flag2 = (this.cursorPosition < this.text.length() || this.text.length() >= getMaxStringLength());
/* 473 */       int k1 = j1;
/*     */       
/* 475 */       if (!flag) {
/* 476 */         k1 = (j > 0) ? (l + this.width) : l;
/* 477 */       } else if (flag2) {
/* 478 */         k1 = j1 - 1;
/* 479 */         j1--;
/*     */       } 
/*     */       
/* 482 */       if (!s.isEmpty() && flag && j < s.length()) {
/* 483 */         j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), j1, i1, i);
/*     */       }
/*     */       
/* 486 */       if (flag1) {
/* 487 */         if (flag2) {
/* 488 */           Gui.drawRect(k1, (i1 - 1), (k1 + 1), (i1 + 1 + this.fontRendererInstance.FONT_HEIGHT), -3092272);
/*     */         } else {
/* 490 */           this.fontRendererInstance.drawStringWithShadow("_", k1, i1, i);
/*     */         } 
/*     */       }
/*     */       
/* 494 */       if (k != j) {
/* 495 */         int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
/* 496 */         drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawCursorVertical(int startX, int startY, int endX, int endY) {
/* 505 */     if (startX < endX) {
/* 506 */       int i = startX;
/* 507 */       startX = endX;
/* 508 */       endX = i;
/*     */     } 
/*     */     
/* 511 */     if (startY < endY) {
/* 512 */       int j = startY;
/* 513 */       startY = endY;
/* 514 */       endY = j;
/*     */     } 
/*     */     
/* 517 */     if (endX > this.xPosition + this.width) {
/* 518 */       endX = this.xPosition + this.width;
/*     */     }
/*     */     
/* 521 */     if (startX > this.xPosition + this.width) {
/* 522 */       startX = this.xPosition + this.width;
/*     */     }
/*     */     
/* 525 */     Tessellator tessellator = Tessellator.getInstance();
/* 526 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
/* 527 */     GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
/* 528 */     GlStateManager.disableTexture2D();
/* 529 */     GlStateManager.enableColorLogic();
/* 530 */     GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
/* 531 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
/* 532 */     bufferbuilder.pos(startX, endY, 0.0D).endVertex();
/* 533 */     bufferbuilder.pos(endX, endY, 0.0D).endVertex();
/* 534 */     bufferbuilder.pos(endX, startY, 0.0D).endVertex();
/* 535 */     bufferbuilder.pos(startX, startY, 0.0D).endVertex();
/* 536 */     tessellator.draw();
/* 537 */     GlStateManager.disableColorLogic();
/* 538 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxStringLength() {
/* 545 */     return this.maxStringLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxStringLength(int length) {
/* 553 */     this.maxStringLength = length;
/*     */     
/* 555 */     if (this.text.length() > length) {
/* 556 */       this.text = this.text.substring(0, length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCursorPosition() {
/* 564 */     return this.cursorPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorPosition(int pos) {
/* 571 */     this.cursorPosition = pos;
/* 572 */     int i = this.text.length();
/* 573 */     this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
/* 574 */     setSelectionPos(this.cursorPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getEnableBackgroundDrawing() {
/* 581 */     return this.enableBackgroundDrawing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
/* 588 */     this.enableBackgroundDrawing = enableBackgroundDrawingIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextColor(int color) {
/* 595 */     this.enabledColor = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisabledTextColour(int color) {
/* 602 */     this.disabledColor = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFocused() {
/* 609 */     return this.isFocused;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocused(boolean isFocusedIn) {
/* 616 */     if (isFocusedIn && !this.isFocused) {
/* 617 */       this.cursorCounter = 0;
/*     */     }
/*     */     
/* 620 */     this.isFocused = isFocusedIn;
/*     */     
/* 622 */     if ((Minecraft.getInstance()).currentScreen != null) {
/* 623 */       (Minecraft.getInstance()).currentScreen.func_193975_a(isFocusedIn);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 631 */     this.isEnabled = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSelectionEnd() {
/* 638 */     return this.selectionEnd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 645 */     return getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelectionPos(int position) {
/* 653 */     int i = this.text.length();
/*     */     
/* 655 */     if (position > i) {
/* 656 */       position = i;
/*     */     }
/*     */     
/* 659 */     if (position < 0) {
/* 660 */       position = 0;
/*     */     }
/*     */     
/* 663 */     this.selectionEnd = position;
/*     */     
/* 665 */     if (this.fontRendererInstance != null) {
/* 666 */       if (this.lineScrollOffset > i) {
/* 667 */         this.lineScrollOffset = i;
/*     */       }
/*     */       
/* 670 */       int j = getWidth();
/* 671 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
/* 672 */       int k = s.length() + this.lineScrollOffset;
/*     */       
/* 674 */       if (position == this.lineScrollOffset) {
/* 675 */         this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
/*     */       }
/*     */       
/* 678 */       if (position > k) {
/* 679 */         this.lineScrollOffset += position - k;
/* 680 */       } else if (position <= this.lineScrollOffset) {
/* 681 */         this.lineScrollOffset -= this.lineScrollOffset - position;
/*     */       } 
/*     */       
/* 684 */       this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanLoseFocus(boolean canLoseFocusIn) {
/* 692 */     this.canLoseFocus = canLoseFocusIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getVisible() {
/* 699 */     return this.visible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean isVisible) {
/* 706 */     this.visible = isVisible;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\GuiSearcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */