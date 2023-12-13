package me.yarukon.mainmenu;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.RenderUtil;
import me.yarukon.YRenderUtil;

public class GuiMainMenuButton {
   public GuiCustomMainMenu parent;
   public String icon;
   public String text;
   public Executor action;
   public int buttonID;
   public float x;
   public float y;
   public float textOffset;
   public float yAnimation = 0.0F;

   public GuiMainMenuButton(GuiCustomMainMenu parent, int id, String icon, String text, Executor action) {
      this.parent = parent;
      this.buttonID = id;
      this.icon = icon;
      this.text = text;
      this.action = action;
      this.textOffset = 0.0F;
   }

   public GuiMainMenuButton(GuiCustomMainMenu parent, int id, String icon, String text, Executor action, float yOffset) {
      this.parent = parent;
      this.buttonID = id;
      this.icon = icon;
      this.text = text;
      this.action = action;
      this.textOffset = yOffset;
   }

   public void draw(float x, float y, int mouseX, int mouseY) {
      this.x = x;
      this.y = y;
      Hanabi.INSTANCE.fontManager.sessionInfoIcon30.drawString(this.icon, x + 25.0F - (float)Hanabi.INSTANCE.fontManager.sessionInfoIcon30.getStringWidth(this.icon) / 2.0F - 2.0F, y + 15.0F, Colors.WHITE.c);
      this.yAnimation = RenderUtil.smoothAnimation(this.yAnimation, RenderUtil.isHovering(mouseX, mouseY, x, y, x + 50.0F, y + 30.0F) ? 2.0F : 0.0F, 50.0F, 0.3F);
      YRenderUtil.drawGradientRect(x, y + 30.0F - this.yAnimation * 3.0F, x + 50.0F, y + 30.0F, 3453695, 2016719615);
      RenderUtil.drawRect(x, y + 30.0F - this.yAnimation, x + 50.0F, y + 30.0F, -13323521);
   }

   public void mouseClick(int mouseX, int mouseY, int mouseButton) {
      if (RenderUtil.isHovering(mouseX, mouseY, this.x, this.y, this.x + 50.0F, this.y + 30.0F) && this.action != null && mouseButton == 0) {
         this.action.execute();
      }

   }

   interface Executor {
      void execute();
   }
}
