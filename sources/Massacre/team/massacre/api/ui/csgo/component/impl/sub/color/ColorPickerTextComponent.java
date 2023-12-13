package team.massacre.api.ui.csgo.component.impl.sub.color;

import java.util.function.Consumer;
import java.util.function.Supplier;
import team.massacre.api.property.ValueChangeListener;
import team.massacre.api.ui.csgo.component.impl.sub.text.TextComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;

public class ColorPickerTextComponent extends Component implements PredicateComponent, ExpandableComponent {
   private static final int COLOR_PICKER_HEIGHT = 5;
   private static final int COLOR_PICKER_WIDTH = 11;
   private static final int TEXT_MARGIN = 1;
   private final ColorPickerComponent colorPicker;
   private final TextComponent textComponent;

   public ColorPickerTextComponent(Component parent, String text, final Supplier<Integer> getColor, final Consumer<Integer> setColor, final Consumer<ValueChangeListener<Integer>> addValueChangeListener, final Supplier<Boolean> isVisible, float x, float y) {
      super(parent, x, y, 0.0F, 5.0F);
      this.textComponent = new TextComponent(this, text, 1.0F, 1.0F);
      this.colorPicker = new ColorPickerComponent(this, 29.166668F, 0.0F, 11.0F, 5.0F) {
         public int getColor() {
            return (Integer)getColor.get();
         }

         public void setColor(int color) {
            setColor.accept(color);
         }

         public void addValueChangeListener(ValueChangeListener<Integer> onValueChange) {
            addValueChangeListener.accept(onValueChange);
         }

         public boolean isVisible() {
            return (Boolean)isVisible.get();
         }
      };
      this.addChild(this.colorPicker);
      this.addChild(this.textComponent);
   }

   public ColorPickerTextComponent(Component parent, String text, Supplier<Integer> getColor, Consumer<Integer> setColor, Consumer<ValueChangeListener<Integer>> addValueChangeListener, Supplier<Boolean> isVisible) {
      this(parent, text, getColor, setColor, addValueChangeListener, isVisible, 0.0F, 0.0F);
   }

   public ColorPickerTextComponent(Component parent, String text, Supplier<Integer> getColor, Consumer<Integer> setColor, Consumer<ValueChangeListener<Integer>> addValueChangeListener) {
      this(parent, text, getColor, setColor, addValueChangeListener, () -> {
         return true;
      });
   }

   public float getWidth() {
      return 13.0F + this.textComponent.getWidth();
   }

   public boolean isVisible() {
      return this.colorPicker.isVisible();
   }

   public float getExpandedX() {
      return this.colorPicker.getExpandedX();
   }

   public float getExpandedY() {
      return this.colorPicker.getY() + this.colorPicker.getHeight();
   }

   public float getExpandedWidth() {
      return this.colorPicker.getExpandedWidth();
   }

   public float getExpandedHeight() {
      return this.colorPicker.getExpandedHeight();
   }

   public void setExpanded(boolean expanded) {
      this.colorPicker.setExpanded(expanded);
   }

   public boolean isExpanded() {
      return this.colorPicker.isExpanded();
   }
}
