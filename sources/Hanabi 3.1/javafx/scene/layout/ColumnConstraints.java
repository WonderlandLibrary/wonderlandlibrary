package javafx.scene.layout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.HPos;

public class ColumnConstraints extends ConstraintsBase {
   private DoubleProperty minWidth;
   private DoubleProperty prefWidth;
   private DoubleProperty maxWidth;
   private DoubleProperty percentWidth;
   private ObjectProperty hgrow;
   private ObjectProperty halignment;
   private BooleanProperty fillWidth;

   public ColumnConstraints() {
   }

   public ColumnConstraints(double var1) {
      this();
      this.setMinWidth(Double.NEGATIVE_INFINITY);
      this.setPrefWidth(var1);
      this.setMaxWidth(Double.NEGATIVE_INFINITY);
   }

   public ColumnConstraints(double var1, double var3, double var5) {
      this();
      this.setMinWidth(var1);
      this.setPrefWidth(var3);
      this.setMaxWidth(var5);
   }

   public ColumnConstraints(double var1, double var3, double var5, Priority var7, HPos var8, boolean var9) {
      this(var1, var3, var5);
      this.setHgrow(var7);
      this.setHalignment(var8);
      this.setFillWidth(var9);
   }

   public final void setMinWidth(double var1) {
      this.minWidthProperty().set(var1);
   }

   public final double getMinWidth() {
      return this.minWidth == null ? -1.0 : this.minWidth.get();
   }

   public final DoubleProperty minWidthProperty() {
      if (this.minWidth == null) {
         this.minWidth = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "minWidth";
            }
         };
      }

      return this.minWidth;
   }

   public final void setPrefWidth(double var1) {
      this.prefWidthProperty().set(var1);
   }

   public final double getPrefWidth() {
      return this.prefWidth == null ? -1.0 : this.prefWidth.get();
   }

   public final DoubleProperty prefWidthProperty() {
      if (this.prefWidth == null) {
         this.prefWidth = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "prefWidth";
            }
         };
      }

      return this.prefWidth;
   }

   public final void setMaxWidth(double var1) {
      this.maxWidthProperty().set(var1);
   }

   public final double getMaxWidth() {
      return this.maxWidth == null ? -1.0 : this.maxWidth.get();
   }

   public final DoubleProperty maxWidthProperty() {
      if (this.maxWidth == null) {
         this.maxWidth = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "maxWidth";
            }
         };
      }

      return this.maxWidth;
   }

   public final void setPercentWidth(double var1) {
      this.percentWidthProperty().set(var1);
   }

   public final double getPercentWidth() {
      return this.percentWidth == null ? -1.0 : this.percentWidth.get();
   }

   public final DoubleProperty percentWidthProperty() {
      if (this.percentWidth == null) {
         this.percentWidth = new DoublePropertyBase(-1.0) {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "percentWidth";
            }
         };
      }

      return this.percentWidth;
   }

   public final void setHgrow(Priority var1) {
      this.hgrowProperty().set(var1);
   }

   public final Priority getHgrow() {
      return this.hgrow == null ? null : (Priority)this.hgrow.get();
   }

   public final ObjectProperty hgrowProperty() {
      if (this.hgrow == null) {
         this.hgrow = new ObjectPropertyBase() {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "hgrow";
            }
         };
      }

      return this.hgrow;
   }

   public final void setHalignment(HPos var1) {
      this.halignmentProperty().set(var1);
   }

   public final HPos getHalignment() {
      return this.halignment == null ? null : (HPos)this.halignment.get();
   }

   public final ObjectProperty halignmentProperty() {
      if (this.halignment == null) {
         this.halignment = new ObjectPropertyBase() {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "halignment";
            }
         };
      }

      return this.halignment;
   }

   public final void setFillWidth(boolean var1) {
      this.fillWidthProperty().set(var1);
   }

   public final boolean isFillWidth() {
      return this.fillWidth == null ? true : this.fillWidth.get();
   }

   public final BooleanProperty fillWidthProperty() {
      if (this.fillWidth == null) {
         this.fillWidth = new BooleanPropertyBase(true) {
            protected void invalidated() {
               ColumnConstraints.this.requestLayout();
            }

            public Object getBean() {
               return ColumnConstraints.this;
            }

            public String getName() {
               return "fillWidth";
            }
         };
      }

      return this.fillWidth;
   }

   public String toString() {
      return "ColumnConstraints percentWidth=" + this.getPercentWidth() + " minWidth=" + this.getMinWidth() + " prefWidth=" + this.getPrefWidth() + " maxWidth=" + this.getMaxWidth() + " hgrow=" + this.getHgrow() + " fillWidth=" + this.isFillWidth() + " halignment=" + this.getHalignment();
   }
}
