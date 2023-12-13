package javafx.scene.control;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.control.skin.LabelSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

public class Label extends Labeled {
   private ChangeListener mnemonicStateListener = (var1x, var2x, var3) -> {
      this.impl_showMnemonicsProperty().setValue(var3);
   };
   private ObjectProperty labelFor;

   public Label() {
      this.initialize();
   }

   public Label(String var1) {
      super(var1);
      this.initialize();
   }

   public Label(String var1, Node var2) {
      super(var1, var2);
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("label"));
      this.setAccessibleRole(AccessibleRole.TEXT);
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
   }

   public ObjectProperty labelForProperty() {
      if (this.labelFor == null) {
         this.labelFor = new ObjectPropertyBase() {
            Node oldValue = null;

            protected void invalidated() {
               if (this.oldValue != null) {
                  NodeHelper.getNodeAccessor().setLabeledBy(this.oldValue, (Node)null);
                  this.oldValue.impl_showMnemonicsProperty().removeListener(Label.this.mnemonicStateListener);
               }

               Node var1 = (Node)this.get();
               if (var1 != null) {
                  NodeHelper.getNodeAccessor().setLabeledBy(var1, Label.this);
                  var1.impl_showMnemonicsProperty().addListener(Label.this.mnemonicStateListener);
                  Label.this.impl_setShowMnemonics(var1.impl_isShowMnemonics());
               } else {
                  Label.this.impl_setShowMnemonics(false);
               }

               this.oldValue = var1;
            }

            public Object getBean() {
               return Label.this;
            }

            public String getName() {
               return "labelFor";
            }
         };
      }

      return this.labelFor;
   }

   public final void setLabelFor(Node var1) {
      this.labelForProperty().setValue(var1);
   }

   public final Node getLabelFor() {
      return this.labelFor == null ? null : (Node)this.labelFor.getValue();
   }

   protected Skin createDefaultSkin() {
      return new LabelSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }
}
