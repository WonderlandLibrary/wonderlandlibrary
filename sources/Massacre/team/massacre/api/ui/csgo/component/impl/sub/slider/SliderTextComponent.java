package team.massacre.api.ui.csgo.component.impl.sub.slider;

import java.util.function.Consumer;
import java.util.function.Supplier;
import team.massacre.api.property.Representation;
import team.massacre.api.ui.csgo.component.impl.sub.text.TextComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.PredicateComponent;

public final class SliderTextComponent extends Component implements PredicateComponent {
   private static final float SLIDER_THICKNESS = 4.0F;
   private static final int SLIDER_Y_OFFSET = 1;
   private final SliderComponent sliderComponent;

   public SliderTextComponent(Component parent, String text, final Supplier<Double> getValue, final Consumer<Double> setValue, final Supplier<Double> getMin, final Supplier<Double> getMax, final Supplier<Double> getIncrement, final Supplier<Representation> getRepresentation, final Supplier<Boolean> isVisible, float x, float y) {
      super(parent, x, y, 40.166668F, 4.0F);
      this.sliderComponent = new SliderComponent(this, 0.0F, 6.0F, this.getWidth(), 4.0F) {
         public double getValue() {
            return (Double)getValue.get();
         }

         public void setValue(double value) {
            setValue.accept(value);
         }

         public Representation getRepresentation() {
            return (Representation)getRepresentation.get();
         }

         public double getMin() {
            return (Double)getMin.get();
         }

         public double getMax() {
            return (Double)getMax.get();
         }

         public double getIncrement() {
            return (Double)getIncrement.get();
         }

         public boolean isVisible() {
            return (Boolean)isVisible.get();
         }
      };
      this.addChild(this.sliderComponent);
      this.addChild(new TextComponent(this, text, 1.0F, 0.0F));
   }

   public SliderTextComponent(Component parent, String text, Supplier<Double> getValue, Consumer<Double> setValue, Supplier<Double> getMin, Supplier<Double> getMax, Supplier<Double> getIncrement, Supplier<Representation> getRepresentation, Supplier<Boolean> isVisible) {
      this(parent, text, getValue, setValue, getMin, getMax, getIncrement, getRepresentation, isVisible, 0.0F, 0.0F);
   }

   public SliderTextComponent(Component parent, String text, Supplier<Double> getValue, Consumer<Double> setValue, Supplier<Double> getMin, Supplier<Double> getMax, Supplier<Double> getIncrement, Supplier<Representation> getRepresentation) {
      this(parent, text, getValue, setValue, getMin, getMax, getIncrement, getRepresentation, () -> {
         return true;
      });
   }

   public float getHeight() {
      return 6.0F + super.getHeight();
   }

   public boolean isVisible() {
      return this.sliderComponent.isVisible();
   }
}
