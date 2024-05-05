package team.massacre.api.ui.csgo.component.impl.sub.text;

import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.utils.TTFFontRenderer;

public final class TextComponent extends Component {
   private static final TTFFontRenderer FONT_RENDERER;
   private final String text;

   public TextComponent(Component parent, String text, float x, float y) {
      super(parent, x, y, FONT_RENDERER.getWidth(text), FONT_RENDERER.getHeight(text));
      this.text = text;
   }

   public void drawComponent(ScaledResolution resolution, int mouseX, int mouseY) {
      if (SkeetUI.shouldRenderText()) {
         FONT_RENDERER.drawString(this.text, this.getX(), this.getY(), SkeetUI.getColor(15132390));
      }

   }

   static {
      FONT_RENDERER = SkeetUI.FONT_RENDERER;
   }
}
