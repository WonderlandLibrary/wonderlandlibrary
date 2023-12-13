/*     */ package org.neverhook.client.ui.clickgui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.clickgui.component.AnimationState;
/*     */ import org.neverhook.client.ui.clickgui.component.Component;
/*     */ import org.neverhook.client.ui.clickgui.component.DraggablePanel;
/*     */ import org.neverhook.client.ui.clickgui.component.ExpandableComponent;
/*     */ import org.neverhook.client.ui.clickgui.component.impl.ModuleComponent;
/*     */ 
/*     */ public final class Panel
/*     */   extends DraggablePanel
/*     */   implements Helper {
/*     */   public static final int HEADER_WIDTH = 120;
/*     */   public static final int X_ITEM_OFFSET = 1;
/*     */   public static final int ITEM_HEIGHT = 15;
/*     */   public static final int HEADER_HEIGHT = 17;
/*     */   private final List<Feature> features;
/*     */   public Type type;
/*     */   public AnimationState state;
/*     */   private int prevX;
/*     */   private int prevY;
/*     */   private boolean dragging;
/*     */   
/*     */   public Panel(Type category, int x, int y) {
/*  37 */     super(null, category.name(), x, y, 120, 17);
/*  38 */     int moduleY = 17;
/*  39 */     this.state = AnimationState.STATIC;
/*  40 */     this.features = NeverHook.instance.featureManager.getFeaturesForCategory(category);
/*  41 */     for (Feature module : this.features) {
/*  42 */       this.components.add(new ModuleComponent((Component)this, module, 1, moduleY, 118, 15));
/*  43 */       moduleY += 15;
/*     */     } 
/*  45 */     this.type = category;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/*  50 */     this.components.sort(new SorterHelper());
/*  51 */     if (this.dragging) {
/*  52 */       setX(mouseX - this.prevX);
/*  53 */       setY(mouseY - this.prevY);
/*     */     } 
/*  55 */     int x = getX();
/*  56 */     int y = getY();
/*  57 */     int width = getWidth();
/*  58 */     int height = getHeight();
/*     */     
/*  60 */     int heightWithExpand = getHeightWithExpand();
/*  61 */     int headerHeight = isExpanded() ? heightWithExpand : height;
/*  62 */     int color = 0;
/*  63 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  64 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  65 */     double speed = ClickGui.speed.getNumberValue();
/*  66 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  68 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6L / 60L * 2L)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/*  71 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/*  74 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/*  77 */         color = PaletteHelper.astolfo(true, y).getRGB();
/*     */         break;
/*     */       case "Static":
/*  80 */         color = onecolor.getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  83 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/*  87 */     float extendedHeight = 2.0F;
/*  88 */     RectHelper.drawSmoothRect(x, (y - 4), (x + width), (y + headerHeight) - extendedHeight, (new Color(26, 26, 26)).getRGB());
/*  89 */     RenderHelper.drawImage(new ResourceLocation("neverhook/clickguiicons/" + this.type.getName() + ".png"), (x + 4), (y - 1), 14.0F, 14.0F, new Color(color));
/*     */     
/*  91 */     mc.montserratRegular.drawStringWithShadow(getName(), (x + 22), (y + 8.5F - 6.0F), Color.LIGHT_GRAY.getRGB());
/*     */     
/*  93 */     super.drawComponent(scaledResolution, mouseX, mouseY);
/*     */     
/*  95 */     if (isExpanded()) {
/*  96 */       for (Component component : this.components) {
/*  97 */         component.setY(height);
/*  98 */         component.drawComponent(scaledResolution, mouseX, mouseY);
/*  99 */         int cHeight = component.getHeight();
/* 100 */         if (component instanceof ExpandableComponent) {
/* 101 */           ExpandableComponent expandableComponent = (ExpandableComponent)component;
/* 102 */           if (expandableComponent.isExpanded()) {
/* 103 */             cHeight = expandableComponent.getHeightWithExpand() + 5;
/*     */           }
/*     */         } 
/* 106 */         height += cHeight;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPress(int mouseX, int mouseY, int button) {
/* 113 */     if (button == 0 && !this.dragging) {
/* 114 */       this.dragging = true;
/* 115 */       this.prevX = mouseX - getX();
/* 116 */       this.prevY = mouseY - getY();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMouseRelease(int button) {
/* 123 */     super.onMouseRelease(button);
/* 124 */     this.dragging = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canExpand() {
/* 129 */     return !this.features.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightWithExpand() {
/* 134 */     int height = getHeight();
/* 135 */     if (isExpanded()) {
/* 136 */       for (Component component : this.components) {
/* 137 */         int cHeight = component.getHeight();
/* 138 */         if (component instanceof ExpandableComponent) {
/* 139 */           ExpandableComponent expandableComponent = (ExpandableComponent)component;
/* 140 */           if (expandableComponent.isExpanded())
/* 141 */             cHeight = expandableComponent.getHeightWithExpand() + 5; 
/*     */         } 
/* 143 */         height += cHeight;
/*     */       } 
/*     */     }
/* 146 */     return height;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\Panel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */