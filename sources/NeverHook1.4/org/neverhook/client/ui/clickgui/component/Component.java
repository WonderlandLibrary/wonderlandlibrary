/*     */ package org.neverhook.client.ui.clickgui.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ 
/*     */ public class Component
/*     */   implements Helper
/*     */ {
/*     */   public final Component parent;
/*  12 */   protected final List<Component> components = new ArrayList<>();
/*     */   private final String name;
/*     */   private int x;
/*     */   private int y;
/*     */   private int width;
/*     */   private int height;
/*     */   
/*     */   public Component(Component parent, String name, int x, int y, int width, int height) {
/*  20 */     this.parent = parent;
/*  21 */     this.name = name;
/*  22 */     this.x = x;
/*  23 */     this.y = y;
/*  24 */     this.width = width;
/*  25 */     this.height = height;
/*     */   }
/*     */   
/*     */   public Component getParent() {
/*  29 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/*  33 */     for (Component child : this.components) {
/*  34 */       child.drawComponent(scaledResolution, mouseX, mouseY);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMouseClick(int mouseX, int mouseY, int button) {
/*  39 */     for (Component child : this.components) {
/*  40 */       child.onMouseClick(mouseX, mouseY, button);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMouseRelease(int button) {
/*  45 */     for (Component child : this.components) {
/*  46 */       child.onMouseRelease(button);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onKeyPress(int keyCode) {
/*  51 */     for (Component child : this.components) {
/*  52 */       child.onKeyPress(keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getX() {
/*  61 */     Component familyMember = this.parent;
/*  62 */     int familyTreeX = this.x;
/*     */     
/*  64 */     while (familyMember != null) {
/*  65 */       familyTreeX += familyMember.x;
/*  66 */       familyMember = familyMember.parent;
/*     */     } 
/*     */     
/*  69 */     return familyTreeX;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  73 */     this.x = x;
/*     */   }
/*     */   
/*     */   protected boolean isHovered(int mouseX, int mouseY) {
/*     */     int x;
/*     */     int y;
/*  79 */     return (mouseX >= (x = getX()) && mouseY >= (y = getY()) && mouseX < x + getWidth() && mouseY < y + getHeight());
/*     */   }
/*     */   
/*     */   public int getY() {
/*  83 */     Component familyMember = this.parent;
/*  84 */     int familyTreeY = this.y;
/*     */     
/*  86 */     while (familyMember != null) {
/*  87 */       familyTreeY += familyMember.y;
/*  88 */       familyMember = familyMember.parent;
/*     */     } 
/*     */     
/*  91 */     return familyTreeY;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/*  95 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  99 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 103 */     this.width = width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 107 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 111 */     this.height = height;
/*     */   }
/*     */   
/*     */   public List<Component> getComponents() {
/* 115 */     return this.components;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */