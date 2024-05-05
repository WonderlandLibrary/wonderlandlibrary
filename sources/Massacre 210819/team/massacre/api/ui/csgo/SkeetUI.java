package team.massacre.api.ui.csgo;

import java.awt.Font;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import optifine.FontUtils;
import org.lwjgl.opengl.GL11;
import team.massacre.Massacre;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.api.property.impl.MultiSelectEnumProperty;
import team.massacre.api.ui.csgo.component.TabComponent;
import team.massacre.api.ui.csgo.component.impl.GroupBoxComponent;
import team.massacre.api.ui.csgo.component.impl.sub.button.ButtonComponentImpl;
import team.massacre.api.ui.csgo.component.impl.sub.checkBox.CheckBoxTextComponent;
import team.massacre.api.ui.csgo.component.impl.sub.color.ColorPickerTextComponent;
import team.massacre.api.ui.csgo.component.impl.sub.comboBox.ComboBoxTextComponent;
import team.massacre.api.ui.csgo.component.impl.sub.key.KeyBindComponent;
import team.massacre.api.ui.csgo.component.impl.sub.slider.SliderTextComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.utils.OGLUtils;
import team.massacre.utils.RenderingUtils;
import team.massacre.utils.TTFFontRenderer;

public final class SkeetUI extends GuiScreen {
   public static final int GROUP_BOX_MARGIN = 8;
   public static final TTFFontRenderer ICONS_RENDERER;
   public static final TTFFontRenderer GROUP_BOX_HEADER_RENDERER = new TTFFontRenderer(new Font("Tahoma", 1, 11));
   public static final TTFFontRenderer FONT_RENDERER;
   public static final TTFFontRenderer KEYBIND_FONT_RENDERER;
   private static final int WIDTH = 370;
   private static final int HEIGHT = 350;
   private static final float TOTAL_BORDER_WIDTH = 3.5F;
   private static final float RAINBOW_BAR_WIDTH = 1.5F;
   private static final int TAB_SELECTOR_WIDTH = 48;
   public static final float USABLE_AREA_WIDTH = 315.0F;
   public static final int GROUP_BOX_LEFT_MARGIN = 3;
   public static final int ENABLE_BUTTON_Y_OFFSET = 6;
   public static final int ENABLE_BUTTON_Y_GAP = 4;
   public static final float GROUP_BOX_WIDTH = 94.333336F;
   public static final float HALF_GROUP_BOX = 40.166668F;
   private static final SkeetUI INSTANCE;
   private static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("massacre/gui/skeetchainmail.png");
   private static final char[] ICONS = new char[]{'E', 'G', 'F', 'I', 'D', 'J', 'H'};
   private static final float USABLE_AREA_HEIGHT = 341.5F;
   private static final int TAB_SELECTOR_HEIGHT;
   private static final Property<Integer> GUI_COLOR;
   private static double alpha;
   private static boolean open;
   private final Component rootComponent = new Component((Component)null, 0.0F, 0.0F, 370.0F, 350.0F) {
      public boolean isHovered(int mouseX, int mouseY) {
         float x;
         float y;
         return (float)mouseX >= (x = this.getX() + 3.0F) && (float)mouseY >= (y = this.getY() + 3.0F) && (float)mouseX <= x + this.getWidth() - 6.0F && (float)mouseY <= y + this.getHeight() - 6.0F;
      }

      public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
         if (SkeetUI.this.dragging) {
            this.setX(Math.max(0.0F, Math.min((float)lockedResolution.getScaledWidth() - this.getWidth(), (float)mouseX - SkeetUI.this.prevX)));
            this.setY(Math.max(0.0F, Math.min((float)lockedResolution.getScaledHeight() - this.getHeight(), (float)mouseY - SkeetUI.this.prevY)));
         }

         float borderX = this.getX();
         float borderY = this.getY();
         float width = this.getWidth();
         float height = this.getHeight();
         Gui.drawRect((double)borderX, (double)borderY, (double)(borderX + width), (double)(borderY + height), SkeetUI.getColor(1052942));
         Gui.drawRect((double)(borderX + 0.5F), (double)(borderY + 0.5F), (double)(borderX + width - 0.5F), (double)(borderY + height - 0.5F), SkeetUI.getColor(3619386));
         Gui.drawRect((double)(borderX + 1.0F), (double)(borderY + 1.0F), (double)(borderX + width - 1.0F), (double)(borderY + height - 1.0F), SkeetUI.getColor(2302755));
         Gui.drawRect((double)(borderX + 3.0F), (double)(borderY + 3.0F), (double)(borderX + width - 3.0F), (double)(borderY + height - 3.0F), SkeetUI.getColor(3092271));
         float left = borderX + 3.5F;
         float top = borderY + 3.5F;
         float right = borderX + width - 3.5F;
         float bottom = borderY + height - 3.5F;
         Gui.drawRect((double)left, (double)top, (double)right, (double)bottom, SkeetUI.getColor(1381653));
         if (SkeetUI.alpha > 20.0D) {
            GL11.glEnable(3089);
            OGLUtils.startScissorBox(lockedResolution, (int)left, (int)top, (int)(right - left), (int)(bottom - top));
            Minecraft.getMinecraft().getTextureManager().bindTexture(SkeetUI.BACKGROUND_IMAGE);
            RenderingUtils.drawImage(left, top, 325.0F, 275.0F, -1);
            RenderingUtils.drawImage(left + 325.0F, top + 1.0F, 325.0F, 275.0F, -1);
            RenderingUtils.drawImage(left + 1.0F, top + 275.0F, 325.0F, 275.0F, -1);
            RenderingUtils.drawImage(left + 326.0F, top + 276.0F, 325.0F, 275.0F, -1);
            GL11.glDisable(3089);
         }

         float xDif = (right - left) / 2.0F;
         top += 0.5F;
         left += 0.5F;
         right -= 0.5F;
         RenderingUtils.drawGradientRect((double)left, (double)top, (double)(left + xDif), (double)(top + 1.5F - 0.5F), true, SkeetUI.getColor(RenderingUtils.darker(3957866, 1.5F)), SkeetUI.getColor(RenderingUtils.darker(7352943, 1.5F)));
         RenderingUtils.drawGradientRect((double)(left + xDif), (double)top, (double)right, (double)(top + 1.5F - 0.5F), true, SkeetUI.getColor(RenderingUtils.darker(7352943, 1.5F)), SkeetUI.getColor(RenderingUtils.darker(8094516, 1.5F)));
         if (SkeetUI.alpha >= 112.0D) {
            Gui.drawRect((double)left, (double)(top + 1.5F - 1.0F), (double)right, (double)(top + 1.5F - 0.5F), 1879048192);
         }

         Iterator var13 = this.children.iterator();

         while(true) {
            Component child;
            do {
               if (!var13.hasNext()) {
                  return;
               }

               child = (Component)var13.next();
            } while(child instanceof TabComponent && SkeetUI.this.selectedTab != child);

            child.drawComponent(lockedResolution, mouseX, mouseY);
         }
      }

      public void onKeyPress(int keyCode) {
         Iterator var2 = this.children.iterator();

         while(true) {
            Component child;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               child = (Component)var2.next();
            } while(child instanceof TabComponent && SkeetUI.this.selectedTab != child);

            child.onKeyPress(keyCode);
         }
      }

      public void onMouseClick(int mouseX, int mouseY, int button) {
         Iterator var4 = this.children.iterator();

         Component tabOrSideBar;
         while(var4.hasNext()) {
            tabOrSideBar = (Component)var4.next();
            if (tabOrSideBar instanceof TabComponent) {
               if (SkeetUI.this.selectedTab != tabOrSideBar) {
                  continue;
               }

               if (tabOrSideBar.isHovered(mouseX, mouseY)) {
                  tabOrSideBar.onMouseClick(mouseX, mouseY, button);
                  break;
               }
            }

            tabOrSideBar.onMouseClick(mouseX, mouseY, button);
         }

         if (button == 0 && this.isHovered(mouseX, mouseY)) {
            var4 = this.getChildren().iterator();

            while(true) {
               label44:
               do {
                  do {
                     if (!var4.hasNext()) {
                        SkeetUI.this.dragging = true;
                        SkeetUI.this.prevX = (float)mouseX - this.getX();
                        SkeetUI.this.prevY = (float)mouseY - this.getY();
                        return;
                     }

                     tabOrSideBar = (Component)var4.next();
                     if (tabOrSideBar instanceof TabComponent) {
                        continue label44;
                     }
                  } while(!tabOrSideBar.isHovered(mouseX, mouseY));

                  return;
               } while(SkeetUI.this.selectedTab != tabOrSideBar);

               Iterator var6 = tabOrSideBar.getChildren().iterator();

               while(var6.hasNext()) {
                  Component groupBox = (Component)var6.next();
                  if (groupBox instanceof GroupBoxComponent) {
                     GroupBoxComponent groupBoxComponent = (GroupBoxComponent)groupBox;
                     if (groupBoxComponent.isHoveredEntire(mouseX, mouseY)) {
                        return;
                     }
                  }
               }
            }
         }
      }

      public void onMouseRelease(int button) {
         super.onMouseRelease(button);
         SkeetUI.this.dragging = false;
      }
   };
   private final Component tabSelectorComponent;
   private double targetAlpha;
   private boolean closed;
   private boolean dragging;
   private float prevX;
   private float prevY;
   private int selectorIndex;
   private TabComponent selectedTab;

   private SkeetUI() {
      Category[] var1 = Category.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         final Category category = var1[var3];
         TabComponent categoryTab = new TabComponent(this.rootComponent, StringUtils.upperSnakeCaseToPascal(category.name()), 51.5F, 5.0F, 315.0F, 341.5F) {
            public void setupChildren() {
               List<Module> modulesInCategory = Massacre.INSTANCE.getModuleManager().getModulesInCategory(category);
               Iterator var2 = modulesInCategory.iterator();

               while(var2.hasNext()) {
                  Module module = (Module)var2.next();
                  GroupBoxComponent groupBoxComponent = new GroupBoxComponent(this, module.getName(), 0.0F, 0.0F, 94.333336F, 6.0F);
                  Supplier var10004 = module::getState;
                  module.getClass();
                  CheckBoxTextComponent enabledButton = new CheckBoxTextComponent(groupBoxComponent, "Enabled", var10004, module::setState);
                  enabledButton.addChild(new KeyBindComponent(enabledButton, module::getKey, module::setKey, 2.0F, 1.0F));
                  groupBoxComponent.addChild(enabledButton);
                  this.addChild(groupBoxComponent);
                  Iterator var6 = module.getValues().iterator();

                  while(var6.hasNext()) {
                     Property<?> property = (Property)var6.next();
                     Component component = null;
                     String var10003;
                     Consumer var10005;
                     if (property.getType() == Boolean.class) {
                        var10003 = property.getLabel();
                        var10004 = property::getValue;
                        var10005 = property::setValue;
                        property.getClass();
                        component = new CheckBoxTextComponent(groupBoxComponent, var10003, var10004, var10005, property::isAvailable);
                     } else if (property.getType() == Integer.class) {
                        var10003 = property.getLabel();
                        var10004 = property::getValue;
                        var10005 = property::setValue;
                        Consumer var10006 = property::addValueChangeListener;
                        property.getClass();
                        component = new ColorPickerTextComponent(groupBoxComponent, var10003, var10004, var10005, var10006, property::isAvailable);
                     } else if (property instanceof DoubleProperty) {
                        DoubleProperty doubleProperty = (DoubleProperty)property;
                        var10003 = property.getLabel();
                        var10004 = doubleProperty::getValue;
                        var10005 = doubleProperty::setValue;
                        Supplier var10 = doubleProperty::getMin;
                        Supplier var10007 = doubleProperty::getMax;
                        Supplier var10008 = doubleProperty::getIncrement;
                        Supplier var10009 = doubleProperty::getRepresentation;
                        doubleProperty.getClass();
                        component = new SliderTextComponent(groupBoxComponent, var10003, var10004, var10005, var10, var10007, var10008, var10009, doubleProperty::isAvailable);
                     } else if (property instanceof EnumProperty) {
                        EnumProperty<?> enumProperty = (EnumProperty)property;
                        component = new ComboBoxTextComponent(groupBoxComponent, property.getLabel(), enumProperty::getValues, enumProperty::setValue, enumProperty::getValue, () -> {
                           return null;
                        }, enumProperty::isAvailable, false);
                     } else if (property instanceof MultiSelectEnumProperty) {
                        MultiSelectEnumProperty<?> enumPropertyx = (MultiSelectEnumProperty)property;
                        component = new ComboBoxTextComponent(groupBoxComponent, property.getLabel(), enumPropertyx::getValues, enumPropertyx::setValue, () -> {
                           return null;
                        }, () -> {
                           return (List)enumPropertyx.getValue();
                        }, enumPropertyx::isAvailable, true);
                     }

                     if (component != null) {
                        groupBoxComponent.addChild((Component)component);
                     }
                  }
               }

               this.getChildren().sort(Comparator.comparingDouble(Component::getHeight).reversed());
            }
         };
         this.rootComponent.addChild(categoryTab);
      }

      TabComponent configTab = new TabComponent(this.rootComponent, "Settings", 51.5F, 5.0F, 315.0F, 341.5F) {
         public void setupChildren() {
            GroupBoxComponent configsGroupBox = new GroupBoxComponent(this, "Configs", 8.0F, 8.0F, 94.333336F, 140.0F);
            int buttonHeight = true;
            Consumer<Integer> onPress = (button) -> {
            };
            configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Load", onPress, 88.333336F, 15.0F));
            configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Save", onPress, 88.333336F, 15.0F));
            configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Refresh", onPress, 88.333336F, 15.0F));
            configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Delete", onPress, 88.333336F, 15.0F));
            this.addChild(configsGroupBox);
            GroupBoxComponent guiSettingsGroupBox = new GroupBoxComponent(this, "GUI Settings", 8.0F, 8.0F, 94.333336F, 100.0F);
            Supplier var10005 = SkeetUI::getColor;
            Consumer var10006 = SkeetUI::setColor;
            Property var10007 = SkeetUI.GUI_COLOR;
            var10007.getClass();
            guiSettingsGroupBox.addChild(new ColorPickerTextComponent(guiSettingsGroupBox, "Gui Color", var10005, var10006, var10007::addValueChangeListener));
            this.addChild(guiSettingsGroupBox);
         }
      };
      this.rootComponent.addChild(configTab);
      this.selectedTab = (TabComponent)this.rootComponent.getChildren().get(this.selectorIndex);
      this.tabSelectorComponent = new Component(this.rootComponent, 3.5F, 5.0F, 48.0F, 341.5F) {
         private double selectorY;

         public void onMouseClick(int mouseX, int mouseY, int button) {
            if (this.isHovered(mouseX, mouseY)) {
               float mouseYOffset = (float)mouseY - SkeetUI.this.tabSelectorComponent.getY() - 10.0F;
               if (mouseYOffset > 0.0F && mouseYOffset < SkeetUI.this.tabSelectorComponent.getHeight() - 10.0F) {
                  SkeetUI.this.selectorIndex = Math.min(SkeetUI.ICONS.length - 1, (int)(mouseYOffset / (float)SkeetUI.TAB_SELECTOR_HEIGHT));
                  SkeetUI.this.selectedTab = (TabComponent)SkeetUI.this.rootComponent.getChildren().get(SkeetUI.this.selectorIndex);
               }
            }

         }

         public void drawComponent(ScaledResolution resolution, int mouseX, int mouseY) {
            this.selectorY = RenderingUtils.progressiveAnimation(this.selectorY, (double)(SkeetUI.this.selectorIndex * SkeetUI.TAB_SELECTOR_HEIGHT + 10), 1.0D);
            float x = this.getX();
            float y = this.getY();
            float width = this.getWidth();
            float height = this.getHeight();
            int innerColor = SkeetUI.getColor(394758);
            int outerColor = SkeetUI.getColor(2105376);
            Gui.drawRect((double)x, (double)y, (double)(x + width), (double)y + this.selectorY, SkeetUI.getColor(789516));
            Gui.drawRect((double)(x + width - 1.0F), (double)y, (double)(x + width), (double)y + this.selectorY, innerColor);
            Gui.drawRect((double)(x + width - 0.5F), (double)y, (double)(x + width), (double)y + this.selectorY, outerColor);
            Gui.drawRect((double)x, (double)y + this.selectorY - 1.0D, (double)(x + width - 0.5F), (double)y + this.selectorY, innerColor);
            Gui.drawRect((double)x, (double)y + this.selectorY - 0.5D, (double)(x + width), (double)y + this.selectorY, outerColor);
            Gui.drawRect((double)x, (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT, (double)(x + width), (double)(y + height), SkeetUI.getColor(789516));
            Gui.drawRect((double)(x + width - 1.0F), (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT, (double)(x + width), (double)(y + height), innerColor);
            Gui.drawRect((double)(x + width - 0.5F), (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT, (double)(x + width), (double)(y + height), outerColor);
            Gui.drawRect((double)x, (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT, (double)(x + width - 0.5F), (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT + 1.0D, innerColor);
            Gui.drawRect((double)x, (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT, (double)(x + width), (double)y + this.selectorY + (double)SkeetUI.TAB_SELECTOR_HEIGHT + 0.5D, outerColor);
            if (SkeetUI.shouldRenderText()) {
               for(int i = 0; i < SkeetUI.ICONS.length; ++i) {
                  String c = String.valueOf(SkeetUI.ICONS[i]);
                  SkeetUI.ICONS_RENDERER.drawString(c, x + 24.0F - SkeetUI.ICONS_RENDERER.getWidth(c) / 2.0F - 1.0F, y + 10.0F + (float)(i * SkeetUI.TAB_SELECTOR_HEIGHT) + (float)SkeetUI.TAB_SELECTOR_HEIGHT / 2.0F - SkeetUI.ICONS_RENDERER.getHeight(c) / 2.0F, SkeetUI.getColor(i == SkeetUI.this.selectorIndex ? 16777215 : 8421504));
               }
            }

         }
      };
      this.rootComponent.addChild(this.tabSelectorComponent);
   }

   public static double getAlpha() {
      return alpha;
   }

   public static int getColor() {
      return getColor((Integer)GUI_COLOR.getValue());
   }

   public static void setColor(int color) {
      GUI_COLOR.setValue(color);
   }

   public static boolean shouldRenderText() {
      return alpha > 20.0D;
   }

   private static boolean isVisible() {
      return open || alpha > 0.0D;
   }

   public static int getColor(int color) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      int a = (int)alpha;
      return (r & 255) << 16 | (g & 255) << 8 | b & 255 | (a & 255) << 24;
   }

   public static void init() {
      INSTANCE.open();
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.close();
      } else {
         this.rootComponent.onKeyPress(keyCode);
      }

   }

   private void close() {
      if (open) {
         this.targetAlpha = 0.0D;
         open = false;
         this.dragging = false;
      }

   }

   private void open() {
      Minecraft.getMinecraft().displayGuiScreen(this);
      alpha = 0.0D;
      this.targetAlpha = 255.0D;
      open = true;
      this.closed = false;
   }

   private boolean finishedClosing() {
      return !open && alpha == 0.0D && !this.closed;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if (this.finishedClosing()) {
         Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
      } else {
         if (isVisible()) {
            alpha = RenderingUtils.linearAnimation(alpha, this.targetAlpha, 15.0D);
            this.rootComponent.drawComponent(new ScaledResolution(Minecraft.getMinecraft()), mouseX, mouseY);
         }

      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (isVisible()) {
         this.rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
      }

   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      if (isVisible()) {
         this.rootComponent.onMouseRelease(state);
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   static {
      TAB_SELECTOR_HEIGHT = 321 / ICONS.length;
      GUI_COLOR = new Property("Gui Color", -1);
      ICONS_RENDERER = new TTFFontRenderer(FontUtils.deriveFontFromAssetsTTF("icons.ttf", 40));
      FONT_RENDERER = new TTFFontRenderer(new Font("Tahoma", 0, 11));
      KEYBIND_FONT_RENDERER = new TTFFontRenderer(new Font("Tahoma", 0, 9));
      INSTANCE = new SkeetUI();
   }
}
