package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class AccordionBuilder extends ControlBuilder implements Builder {
   private int __set;
   private TitledPane expandedPane;
   private Collection panes;

   protected AccordionBuilder() {
   }

   public static AccordionBuilder create() {
      return new AccordionBuilder();
   }

   public void applyTo(Accordion var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setExpandedPane(this.expandedPane);
      }

      if ((var2 & 2) != 0) {
         var1.getPanes().addAll(this.panes);
      }

   }

   public AccordionBuilder expandedPane(TitledPane var1) {
      this.expandedPane = var1;
      this.__set |= 1;
      return this;
   }

   public AccordionBuilder panes(Collection var1) {
      this.panes = var1;
      this.__set |= 2;
      return this;
   }

   public AccordionBuilder panes(TitledPane... var1) {
      return this.panes((Collection)Arrays.asList(var1));
   }

   public Accordion build() {
      Accordion var1 = new Accordion();
      this.applyTo(var1);
      return var1;
   }
}
