package team.massacre.api.ui.csgo.component.impl.sub.key;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import team.massacre.Massacre;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.utils.TTFFontRenderer;

public final class KeyBindComponent extends ButtonComponent {
   private static final TTFFontRenderer FONT_RENDERER;
   private final Supplier<Integer> getBind;
   private final Consumer<Integer> onSetBind;
   private boolean binding;

   public KeyBindComponent(Component parent, Supplier<Integer> getBind, Consumer<Integer> onSetBind, float x, float y) {
      super(parent, x, y, FONT_RENDERER.getWidth("[") * 2.0F, FONT_RENDERER.getHeight("[]"));
      this.getBind = getBind;
      this.onSetBind = onSetBind;
   }

   public float getWidth() {
      return super.getWidth() + Massacre.INSTANCE.getFontManager().getLatoRegularSmall().getWidth(this.getBind());
   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      Massacre.INSTANCE.getFontManager().getLatoRegularSmall().drawStringWithShadow("[" + this.getBind() + "]", x + 40.166668F - width, y, SkeetUI.getColor(7895160));
   }

   public boolean isHovered(int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      return (float)mouseX >= x + 40.166668F - this.getWidth() && (float)mouseY >= y && (float)mouseX <= x + 40.166668F && (float)mouseY <= y + this.getHeight();
   }

   public void onKeyPress(int keyCode) {
      if (this.binding) {
         if (keyCode == 211) {
            keyCode = 0;
         }

         this.onChangeBind(keyCode);
         this.binding = false;
      }

   }

   private String getBind() {
      int bind = (Integer)this.getBind.get();
      return this.binding ? "..." : (bind == 0 ? "-" : Keyboard.getKeyName(bind));
   }

   private void onChangeBind(int bind) {
      this.onSetBind.accept(bind);
   }

   public void onPress(int mouseButton) {
      this.binding = !this.binding;
   }

   static {
      FONT_RENDERER = SkeetUI.KEYBIND_FONT_RENDERER;
   }
}
