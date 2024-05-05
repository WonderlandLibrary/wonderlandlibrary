package team.massacre.api.ui.main.components;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.main.components.tab.TabComponent;
import team.massacre.api.ui.main.components.tab.impl.AntiAimTabComponent;
import team.massacre.api.ui.main.components.tab.impl.LegitTabComponent;
import team.massacre.api.ui.main.components.tab.impl.PlayerTabComponent;
import team.massacre.api.ui.main.components.tab.impl.RageTabComponent;
import team.massacre.api.ui.main.components.tab.impl.SaveTabComponent;
import team.massacre.api.ui.main.components.tab.impl.SettingsTabComponent;
import team.massacre.api.ui.main.components.tab.impl.VisualsTabComponent;
import team.massacre.utils.OGLUtils;
import team.massacre.utils.RenderingUtils;

public final class UIComponent extends Component {
   private final TabSelectorComponent tabSelector;
   private final float minWidth;
   private final float minHeight;
   private final List<TabComponent> tabs;
   private float prevX;
   private float prevY;
   private float prevWidth;
   private float prevHeight;
   private boolean dragging;
   private boolean resizing;
   public static int uiColor = -65536;
   private final ResourceLocation backgroundTexture = new ResourceLocation("massacre/gui/skeetchainmail.png");

   public UIComponent(float width, float height) {
      super((Component)null, 5.0F, 5.0F, width, height);
      this.minWidth = width;
      this.minHeight = height;
      this.tabSelector = new TabSelectorComponent(this, 45.0F, height);
      this.addChild(this.tabSelector);
      float tabComponentsMarginX = 10.0F;
      float tabComponentsMarginY = 15.0F;
      float tabComponentsX = this.tabSelector.getWidth() + 10.0F;
      float tabComponentsHeight = height - 15.0F - 10.0F;
      float tabComponentsWidth = width - tabComponentsX - 10.0F;
      this.tabs = Arrays.asList(new RageTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight), new AntiAimTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight), new LegitTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight), new VisualsTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight), new SettingsTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight), new PlayerTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight), new SaveTabComponent(this, tabComponentsX, 15.0F, tabComponentsWidth, tabComponentsHeight));
   }

   private void onResize(float width, float height) {
      float tabComponentsMarginX = 10.0F;
      float tabComponentsMarginY = 15.0F;
      float tabComponentsX = this.tabSelector.getWidth() + 10.0F;
      float tabComponentsHeight = height - 15.0F - 10.0F;
      float tabComponentsWidth = width - tabComponentsX - 10.0F;
      Iterator var8 = this.tabs.iterator();

      while(var8.hasNext()) {
         TabComponent tabComponent = (TabComponent)var8.next();
         tabComponent.setWidth(tabComponentsWidth);
         tabComponent.setHeight(tabComponentsHeight);
      }

   }

   public void setHeight(float height) {
      super.setHeight(height);
      this.tabSelector.setHeight(height);
      this.onResize(this.getWidth(), height);
   }

   public void setWidth(float width) {
      super.setWidth(width);
      this.onResize(width, this.getHeight());
   }

   private TabComponent getSelectedTab() {
      return (TabComponent)this.tabs.get(this.tabSelector.getSelected().ordinal());
   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      if (this.dragging) {
         this.setX((float)mouseX - this.prevX);
         this.setY((float)mouseY - this.prevY);
      } else if (this.resizing) {
         this.setWidth(Math.min(650.0F, Math.max(this.minWidth, (float)mouseX - this.prevWidth)));
         this.setHeight(Math.min(550.0F, Math.max(this.minHeight, (float)mouseY - this.prevHeight)));
      }

      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float height = this.getHeight();
      float rainbowBarThickness = 1.0F;
      Gui.drawRect((double)(x - 3.0F), (double)(y - 3.0F - 1.0F), (double)(x + width + 3.0F), (double)(y + height + 3.0F), -15724274);
      Gui.drawRect((double)(x - 2.5F), (double)(y - 2.5F - 1.0F), (double)(x + width + 2.5F), (double)(y + height + 2.5F), -13157830);
      Gui.drawRect((double)(x - 2.0F), (double)(y - 2.0F - 1.0F), (double)(x + width + 2.0F), (double)(y + height + 2.0F), -14474461);
      Gui.drawRect((double)(x - 0.5F), (double)(y - 0.5F - 1.0F), (double)(x + width + 0.5F), (double)(y + height + 0.5F), -13684945);
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), -15395563);
      GL11.glEnable(3089);
      OGLUtils.startScissorBox(lockedResolution, (int)x, (int)y, (int)width, (int)Math.floor((double)height));
      Minecraft.getMinecraft().getTextureManager().bindTexture(this.backgroundTexture);
      RenderingUtils.drawImage(x, y, 325.0F, 275.0F, -1);
      boolean tooWide = width > 325.0F;
      boolean tooLong = height > 275.0F;
      if (tooWide) {
         RenderingUtils.drawImage(x + 325.0F, y, 325.0F, 275.0F, -1);
      }

      if (tooLong) {
         RenderingUtils.drawImage(x, y + 275.0F, 325.0F, 275.0F, -1);
      }

      if (tooWide && tooLong) {
         RenderingUtils.drawImage(x + 325.0F, y + 275.0F, 325.0F, 275.0F, -1);
      }

      GL11.glDisable(3089);
      RenderingUtils.drawGradientRect((double)x, (double)(y - 1.0F), (double)(x + width / 2.0F), (double)y, true, RenderingUtils.darker(-12819350, 1.5F), RenderingUtils.darker(-9424273, 1.5F));
      RenderingUtils.drawGradientRect((double)(x + width / 2.0F), (double)(y - 1.0F), (double)(x + width), (double)y, true, RenderingUtils.darker(-9424273, 1.5F), RenderingUtils.darker(-8682700, 1.5F));
      Gui.drawRect((double)x, (double)(y - 0.5F), (double)(x + width), (double)y, 1879048192);
      this.getSelectedTab().drawComponent(lockedResolution, mouseX, mouseY);
      super.drawComponent(lockedResolution, mouseX, mouseY);
   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      if (this.isHovered(mouseX, mouseY)) {
         Iterator var4 = this.getSelectedTab().getChildren().iterator();

         Component child;
         while(var4.hasNext()) {
            child = (Component)var4.next();
            if (child.isHovered(mouseX, mouseY)) {
               child.onMouseClick(mouseX, mouseY, button);
               return;
            }
         }

         var4 = this.getChildren().iterator();

         while(var4.hasNext()) {
            child = (Component)var4.next();
            if (child.isHovered(mouseX, mouseY)) {
               child.onMouseClick(mouseX, mouseY, button);
               return;
            }
         }

         float x = this.getX();
         float y = this.getY();
         float width = this.getWidth();
         float height = this.getHeight();
         if ((float)mouseX > x + width - 3.0F && (float)mouseY > y + height - 3.0F) {
            this.resizing = true;
            this.prevWidth = (float)mouseX - width;
            this.prevHeight = (float)mouseY - height;
         } else {
            this.dragging = true;
            this.prevX = (float)mouseX - x;
            this.prevY = (float)mouseY - y;
         }
      }

   }

   public void onMouseRelease(int button) {
      this.dragging = false;
      this.resizing = false;
      this.getSelectedTab().onMouseRelease(button);
      super.onMouseRelease(button);
   }

   public void onKeyPress(int keyCode) {
      this.getSelectedTab().onKeyPress(keyCode);
      super.onKeyPress(keyCode);
   }
}
