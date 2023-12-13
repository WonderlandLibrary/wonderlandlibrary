package my.NewSnake.Tank.gui.click.component.window;

import java.util.ArrayList;
import java.util.List;
import my.NewSnake.Tank.gui.click.component.Component;

public abstract class Window extends Component {
   private List slotList = new ArrayList();
   private boolean extended;
   public static final int OUTLINE_COLOR = -15658735;
   protected static final int BACKGROUND_COLOR = -13290187;
   private Handle handle;
   private boolean dragging;
   protected static final int HANDLE_HEIGHT = 18;
   public static final int FILL_COLOR = -14540254;
   private double[] startOffset;

   public void setStartOffset(double[] var1) {
      this.startOffset = var1;
   }

   public void setDragging(boolean var1) {
      this.dragging = var1;
   }

   public Object getParent() {
      return super.getParent();
   }

   public List getSlotList() {
      return this.slotList;
   }

   public boolean isExtended() {
      return this.extended;
   }

   public double[] getStartOffset() {
      return this.startOffset;
   }

   public Handle getHandle() {
      return this.handle;
   }

   public abstract void draw(int var1, int var2);

   public void setExtended(boolean var1) {
      this.extended = var1;
   }

   public boolean isDragging() {
      return this.dragging;
   }

   public Window(Object var1, double var2, double var4, double var6, double var8, Handle var10) {
      super(var1, var2, var4, var6, var8);
      this.handle = var10;
   }
}
