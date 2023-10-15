/*     */ package org.neverhook.client.ui.clickgui.component.impl;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ColorSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.clickgui.ClickGuiScreen;
/*     */ import org.neverhook.client.ui.clickgui.Panel;
/*     */ import org.neverhook.client.ui.clickgui.component.AnimationState;
/*     */ import org.neverhook.client.ui.clickgui.component.Component;
/*     */ import org.neverhook.client.ui.clickgui.component.ExpandableComponent;
/*     */ 
/*     */ public final class ModuleComponent
/*     */   extends ExpandableComponent
/*     */   implements Helper {
/*     */   private final Feature module;
/*     */   private final AnimationState state;
/*     */   private boolean binding;
/*     */   private int buttonLeft;
/*     */   private int buttonTop;
/*     */   private int buttonRight;
/*     */   private int buttonBottom;
/*     */   
/*     */   public ModuleComponent(Component parent, Feature module, int x, int y, int width, int height) {
/*  35 */     super(parent, module.getLabel(), x, y, width, height);
/*  36 */     this.module = module;
/*  37 */     this.state = AnimationState.STATIC;
/*  38 */     int propertyX = 1;
/*  39 */     for (Setting setting : module.getSettings()) {
/*  40 */       if (setting instanceof BooleanSetting) {
/*  41 */         this.components.add(new BooleanSettingComponent((Component)this, (BooleanSetting)setting, propertyX, height, width - 2, 21)); continue;
/*  42 */       }  if (setting instanceof ColorSetting) {
/*  43 */         this.components.add(new ColorPickerComponent((Component)this, (ColorSetting)setting, propertyX, height, width - 2, 15)); continue;
/*  44 */       }  if (setting instanceof NumberSetting) {
/*  45 */         this.components.add(new NumberSettingComponent((Component)this, (NumberSetting)setting, propertyX, height, width - 2, 20)); continue;
/*  46 */       }  if (setting instanceof ListSetting) {
/*  47 */         this.components.add(new ListSettingComponent((Component)this, (ListSetting)setting, propertyX, height, width - 2, 22));
/*     */       }
/*     */     } 
/*  50 */     this.components.add(new VisibleComponent(module, (Component)this, propertyX, height, width - 2, 15));
/*     */   }
/*     */   
/*     */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/*     */     Panel panel;
/*  55 */     float x = getX();
/*  56 */     float y = (getY() - 2);
/*  57 */     int width = getWidth();
/*  58 */     int height = getHeight();
/*  59 */     if (isExpanded()) {
/*  60 */       int childY = 15;
/*  61 */       for (Component child : this.components) {
/*  62 */         int cHeight = child.getHeight();
/*  63 */         if (child instanceof BooleanSettingComponent) {
/*  64 */           BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent)child;
/*  65 */           if (!booleanSettingComponent.booleanSetting.isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/*  69 */         if (child instanceof NumberSettingComponent) {
/*  70 */           NumberSettingComponent numberSettingComponent = (NumberSettingComponent)child;
/*  71 */           if (!numberSettingComponent.numberSetting.isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/*  75 */         if (child instanceof ColorPickerComponent) {
/*  76 */           ColorPickerComponent colorPickerComponent = (ColorPickerComponent)child;
/*  77 */           if (!colorPickerComponent.getSetting().isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/*  81 */         if (child instanceof ListSettingComponent) {
/*  82 */           ListSettingComponent listSettingComponent = (ListSettingComponent)child;
/*  83 */           if (!listSettingComponent.getSetting().isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/*  87 */         if (child instanceof ExpandableComponent) {
/*  88 */           ExpandableComponent expandableComponent = (ExpandableComponent)child;
/*  89 */           if (expandableComponent.isExpanded())
/*  90 */             cHeight = expandableComponent.getHeightWithExpand(); 
/*     */         } 
/*  92 */         child.setY(childY);
/*  93 */         child.drawComponent(scaledResolution, mouseX, mouseY);
/*  94 */         childY += cHeight;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     if (!ClickGuiScreen.search.getText().isEmpty() && !this.module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     int color = 0;
/* 103 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/* 104 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/* 105 */     double speed = ClickGui.speed.getNumberValue();
/* 106 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/* 108 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/* 111 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/* 114 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6.0F / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/* 117 */         color = PaletteHelper.astolfo(true, (int)y).getRGB();
/*     */         break;
/*     */       case "Rainbow":
/* 120 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */       case "Category":
/* 123 */         panel = (Panel)this.parent;
/* 124 */         color = panel.type.getColor();
/*     */         break;
/*     */     } 
/*     */     
/* 128 */     boolean hovered = isHovered(mouseX, mouseY);
/*     */     
/* 130 */     if (hovered) {
/* 131 */       RectHelper.drawBorderedRect(x + width + 18.0F, y + height / 1.5F + 3.5F, x + width + 25.0F + mc.fontRendererObj.getStringWidth(this.module.getDesc()), y + 3.5F, 0.5F, (new Color(30, 30, 30, 255)).getRGB(), color, true);
/* 132 */       mc.fontRendererObj.drawStringWithShadow(this.module.getDesc(), x + width + 22.0F, y + height / 1.35F - 6.0F, -1);
/*     */     } 
/*     */     
/* 135 */     if (this.components.size() > 1) {
/* 136 */       mc.buttonFontRender.drawStringWithShadow(isExpanded() ? "-" : "+", (x + width - 10.0F), (y + height / 2.0F - 8.0F), Color.GRAY.getRGB());
/*     */     }
/*     */     
/* 139 */     int middleHeight = getHeight() / 2;
/* 140 */     int btnRight = (int)(x + 3.0F + middleHeight);
/*     */     
/* 142 */     RectHelper.drawRect((x - 1.0F), (y + height / 1.5F + 5.0F), (x + 20.0F), y, (new Color(20, 20, 20, 220)).getRGB());
/*     */     
/* 144 */     gui.drawGradientRect((this.buttonLeft = (int)(x + 5.0F)), (this.buttonTop = (int)(y + middleHeight - (middleHeight / 2 + 1))), (this.buttonRight = btnRight + 3), (this.buttonBottom = (int)(y + middleHeight + (middleHeight / 2) + 1.0F)), -9737365, (new Color(-9737365)).darker().darker().getRGB());
/*     */     
/* 146 */     RectHelper.drawRect(this.buttonLeft + 0.5D, this.buttonTop + 0.5D, this.buttonRight - 0.5D, this.buttonBottom - 0.5D, -12828863);
/*     */     
/* 148 */     if (this.module.getState()) {
/* 149 */       gui.drawGradientRect((this.buttonLeft = (int)(x + 6.5D)), (this.buttonTop = (int)(y + middleHeight - (middleHeight / 2))), (this.buttonRight = (int)(btnRight + 2.5D)), (this.buttonBottom = (int)(y + middleHeight + (middleHeight / 2))), color, (new Color(color)).darker().darker().getRGB());
/*     */     }
/*     */     
/* 152 */     mc.montserratRegular.drawStringWithShadow(this.binding ? ("Press a key... Key: " + Keyboard.getKeyName(this.module.getBind())) : getName(), (x + 25.0F), (y + height / 2.0F - 3.0F), hovered ? Color.LIGHT_GRAY.getRGB() : (this.module.getState() ? Color.LIGHT_GRAY.getRGB() : Color.GRAY.getRGB()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canExpand() {
/* 157 */     return !this.components.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPress(int mouseX, int mouseY, int button) {
/* 162 */     switch (button) {
/*     */       case 0:
/* 164 */         this.module.state();
/*     */         break;
/*     */       case 2:
/* 167 */         this.binding = !this.binding;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyPress(int keyCode) {
/* 174 */     if (this.binding) {
/* 175 */       ClickGuiScreen.escapeKeyInUse = true;
/* 176 */       this.module.setBind((keyCode == 1) ? 0 : keyCode);
/* 177 */       this.binding = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightWithExpand() {
/* 183 */     int height = getHeight();
/* 184 */     if (isExpanded()) {
/* 185 */       for (Component child : this.components) {
/* 186 */         int cHeight = child.getHeight();
/* 187 */         if (child instanceof BooleanSettingComponent) {
/* 188 */           BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent)child;
/* 189 */           if (!booleanSettingComponent.booleanSetting.isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/* 193 */         if (child instanceof NumberSettingComponent) {
/* 194 */           NumberSettingComponent numberSettingComponent = (NumberSettingComponent)child;
/* 195 */           if (!numberSettingComponent.numberSetting.isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/* 199 */         if (child instanceof ColorPickerComponent) {
/* 200 */           ColorPickerComponent colorPickerComponent = (ColorPickerComponent)child;
/* 201 */           if (!colorPickerComponent.getSetting().isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/* 205 */         if (child instanceof ListSettingComponent) {
/* 206 */           ListSettingComponent listSettingComponent = (ListSettingComponent)child;
/* 207 */           if (!listSettingComponent.getSetting().isVisible()) {
/*     */             continue;
/*     */           }
/*     */         } 
/* 211 */         if (child instanceof ExpandableComponent) {
/* 212 */           ExpandableComponent expandableComponent = (ExpandableComponent)child;
/* 213 */           if (expandableComponent.isExpanded())
/* 214 */             cHeight = expandableComponent.getHeightWithExpand(); 
/*     */         } 
/* 216 */         height += cHeight;
/*     */       } 
/*     */     }
/* 219 */     return height;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\impl\ModuleComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */