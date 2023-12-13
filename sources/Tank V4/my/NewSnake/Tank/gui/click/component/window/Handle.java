package my.NewSnake.Tank.gui.click.component.window;

import java.util.Iterator;
import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.gui.click.component.Component;
import my.NewSnake.Tank.module.Renders;
import my.NewSnake.utils.ClientUtils;

public class Handle extends Component {
   public static final double TITLE_SCALE = 1.1D;
   private static final float BORDER_WIDTH = 1.5F;
   private static final int SEPERATOR_COLOR = -14803426;
   private String name;
   private static final double SEPERATOR_HEIGHT = 2.0D;
   private static final int BACKGROUND_COLOR = -13290187;

   public void keyPress(int var1, char var2) {
   }

   public void release(int var1, int var2, int var3) {
      if (var3 == 0) {
         Iterator var5 = ClickGui.getInstance().getWindows().iterator();

         while(var5.hasNext()) {
            Window var4 = (Window)var5.next();
            var4.setDragging(false);
         }
      }

   }

   public void draw(int var1, int var2) {
   }

   public Handle(String var1, double var2, double var4, double var6, double var8) {
      super((Object)null, var2, var4, var6, var8);
      this.name = var1;
   }

   public void draw(int var1, int var2, boolean var3) {
      int[] var4 = new int[]{-14540254, -14540254, Renders.blend(-14540254, -16777216, 0.95F), Renders.blend(-14540254, -16777216, 0.95F)};
      int[] var5 = new int[]{Renders.blend(-15658735, -16777216, 0.95F), Renders.blend(-15658735, -16777216, 0.95F), -15658735, -15658735};
      Renders.rectangleBorderedGradient(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), var4, var5, 0.5D);
      Renders.rectangle(this.getX() + 1.0D, this.getY() + 0.5D, this.getX() + this.getWidth() - 1.0D, this.getY() + 1.0D, 553648127);
      ClientUtils.clientFont().drawScaledString(this.name, this.getX() + this.getWidth() / 2.0D, this.getY() + this.getHeight() / 2.0D + 1.5D, -1, 1.1D);
   }

   public void drag(int var1, int var2, int var3) {
      if (var3 == 0) {
         ((Window)this.getParent()).drag(var1, var2, var3);
      }

   }

   public void click(int var1, int var2, int var3) {
      Iterator var5 = ClickGui.getInstance().getWindows().iterator();

      while(var5.hasNext()) {
         Window var4 = (Window)var5.next();
         var4.setDragging(false);
      }

      if (var3 == 0) {
         ((Window)this.getParent()).setStartOffset(new double[]{(double)var1 - ((Window)this.getParent()).getX(), (double)var2 - ((Window)this.getParent()).getY()});
         ((Window)this.getParent()).setDragging(true);
      } else if (var3 == 1) {
         ((Window)this.getParent()).setExtended(!((Window)this.getParent()).isExtended());
      }

   }
}
