package javafx.scene.layout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.VPos;

public class RowConstraints extends ConstraintsBase {
   private DoubleProperty minHeight;
   private DoubleProperty prefHeight;
   private DoubleProperty maxHeight;
   private DoubleProperty percentHeight;
   private ObjectProperty vgrow;
   private ObjectProperty valignment;
   private BooleanProperty fillHeight;

   public RowConstraints() {
   }

   public RowConstraints(double var1) {
      this();
      this.setMinHeight(Double.NEGATIVE_INFINITY);
      this.setPrefHeight(var1);
      this.setMaxHeight(Double.NEGATIVE_INFINITY);
   }

   public RowConstraints(double var1, double var3, double var5) {
      this();
      this.setMinHeight(var1);
      this.setPrefHeight(var3);
      this.setMaxHeight(var5);
   }

   public RowConstraints(double var1, double var3, double var5, Priority var7, VPos var8, boolean var9) {
      this(var1, var3, var5);
      this.setVgrow(var7);
      this.setValignment(var8);
      this.setFillHeight(var9);
   }

   public final void setMinHeight(double var1) {
      this.minHeightProperty().set(var1);
   }

   public final double getMinHeight() {
      return this.minHeight == null ? -1.0 : this.minHeight.get();
   }

   public final DoubleProperty minHeightProperty() {
      if (this.minHeight == null) {
         this.minHeight = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "minHeight";
            }
         };
      }

      return this.minHeight;
   }

   public final void setPrefHeight(double var1) {
      this.prefHeightProperty().set(var1);
   }

   public final double getPrefHeight() {
      return this.prefHeight == null ? -1.0 : this.prefHeight.get();
   }

   public final DoubleProperty prefHeightProperty() {
      if (this.prefHeight == null) {
         this.prefHeight = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "prefHeight";
            }
         };
      }

      return this.prefHeight;
   }

   public final void setMaxHeight(double var1) {
      this.maxHeightProperty().set(var1);
   }

   public final double getMaxHeight() {
      return this.maxHeight == null ? -1.0 : this.maxHeight.get();
   }

   public final DoubleProperty maxHeightProperty() {
      if (this.maxHeight == null) {
         this.maxHeight = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "maxHeight";
            }
         };
      }

      return this.maxHeight;
   }

   public final void setPercentHeight(double var1) {
      this.percentHeightProperty().set(var1);
   }

   public final double getPercentHeight() {
      return this.percentHeight == null ? -1.0 : this.percentHeight.get();
   }

   public final DoubleProperty percentHeightProperty() {
      if (this.percentHeight == null) {
         this.percentHeight = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "percentHeight";
            }
         };
      }

      return this.percentHeight;
   }

   public final void setVgrow(Priority var1) {
      this.vgrowProperty().set(var1);
   }

   public final Priority getVgrow() {
      return this.vgrow == null ? null : (Priority)this.vgrow.get();
   }

   public final ObjectProperty vgrowProperty() {
      if (this.vgrow == null) {
         this.vgrow = new ObjectPropertyBase() {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "vgrow";
            }
         };
      }

      return this.vgrow;
   }

   public final void setValignment(VPos var1) {
      this.valignmentProperty().set(var1);
   }

   public final VPos getValignment() {
      return this.valignment == null ? null : (VPos)this.valignment.get();
   }

   public final ObjectProperty valignmentProperty() {
      if (this.valignment == null) {
         this.valignment = new ObjectPropertyBase() {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "valignment";
            }
         };
      }

      return this.valignment;
   }

   public final void setFillHeight(boolean var1) {
      this.fillHeightProperty().set(var1);
   }

   public final boolean isFillHeight() {
      return this.fillHeight == null ? true : this.fillHeight.get();
   }

   public final BooleanProperty fillHeightProperty() {
      if (this.fillHeight == null) {
         this.fillHeight = new BooleanPropertyBase(true) {
            protected void invalidated() {
               RowConstraints.this.requestLayout();
            }

            public Object getBean() {
               return RowConstraints.this;
            }

            public String getName() {
               return "fillHeight";
            }
         };
      }

      return this.fillHeight;
   }

   public String toString() {
      return "RowConstraints percentHeight=" + this.getPercentHeight() + " minHeight=" + this.getMinHeight() + " prefHeight=" + this.getPrefHeight() + " maxHeight=" + this.getMaxHeight() + " vgrow=" + this.getVgrow() + " fillHeight=" + this.isFillHeight() + " valignment=" + this.getValignment();
   }
}
