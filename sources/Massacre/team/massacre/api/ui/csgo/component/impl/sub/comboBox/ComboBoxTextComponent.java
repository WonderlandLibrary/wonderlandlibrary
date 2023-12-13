package team.massacre.api.ui.csgo.component.impl.sub.comboBox;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import team.massacre.api.ui.csgo.component.impl.sub.text.TextComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;

public final class ComboBoxTextComponent extends Component implements PredicateComponent, ExpandableComponent {
   private static final int COMBO_BOX_HEIGHT = 10;
   private static final int COMBO_BOX_Y_OFFSET = 1;
   private final TextComponent textComponent;
   private final ComboBoxComponent comboBoxComponent;

   public ComboBoxTextComponent(Component parent, String name, final Supplier<Enum<?>[]> getValues, final Consumer<Integer> setValueByIndex, final Supplier<Enum<?>> getValue, final Supplier<List<Enum<?>>> getValueMultiSelect, final Supplier<Boolean> isVisible, final boolean multiSelect, float x, float y) {
      super(parent, x, y, 40.166668F, 16.0F);
      this.comboBoxComponent = new ComboBoxComponent(this, 0.0F, 6.0F, this.getWidth(), 10.0F) {
         public boolean isVisible() {
            return (Boolean)isVisible.get();
         }

         public Enum<?> getValue() {
            return (Enum)getValue.get();
         }

         public void setValue(int index) {
            setValueByIndex.accept(index);
         }

         public List<Enum<?>> getMultiSelectValues() {
            return (List)getValueMultiSelect.get();
         }

         public boolean isMultiSelectable() {
            return multiSelect;
         }

         public Enum<?>[] getValues() {
            return (Enum[])getValues.get();
         }
      };
      this.textComponent = new TextComponent(this, name, 1.0F, 0.0F);
      this.addChild(this.comboBoxComponent);
      this.addChild(this.textComponent);
   }

   public ComboBoxTextComponent(Component parent, String name, Supplier<Enum<?>[]> getValues, Consumer<Integer> setValueByIndex, Supplier<Enum<?>> getValue, Supplier<List<Enum<?>>> getValueMultiSelect, Supplier<Boolean> isVisible, boolean multiSelect) {
      this(parent, name, getValues, setValueByIndex, getValue, getValueMultiSelect, isVisible, multiSelect, 0.0F, 0.0F);
   }

   public ComboBoxTextComponent(Component parent, String name, Supplier<Enum<?>[]> getValues, Consumer<Integer> setValueByIndex, Supplier<Enum<?>> getValue, Supplier<List<Enum<?>>> getValueMultiSelect, boolean multiSelect) {
      this(parent, name, getValues, setValueByIndex, getValue, getValueMultiSelect, () -> {
         return true;
      }, multiSelect, 0.0F, 0.0F);
   }

   public boolean isVisible() {
      return this.comboBoxComponent.isVisible();
   }

   public float getExpandedX() {
      return this.comboBoxComponent.getExpandedX();
   }

   public float getExpandedY() {
      return this.getY() + this.textComponent.getHeight();
   }

   public float getExpandedWidth() {
      return this.comboBoxComponent.getExpandedWidth();
   }

   public float getExpandedHeight() {
      return this.comboBoxComponent.getExpandedHeight();
   }

   public void setExpanded(boolean expanded) {
      this.comboBoxComponent.setExpanded(expanded);
   }

   public boolean isExpanded() {
      return this.comboBoxComponent.isExpanded();
   }
}
