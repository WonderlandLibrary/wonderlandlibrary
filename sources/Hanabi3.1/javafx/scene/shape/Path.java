package javafx.scene.shape;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.shape.PathUtils;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPath;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Path extends Shape {
   private Path2D path2d = null;
   private ObjectProperty fillRule;
   private boolean isPathValid;
   private final ObservableList elements;

   public Path() {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.elements = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            ObservableList var2 = var1.getList();

            boolean var3;
            for(var3 = false; var1.next(); var3 |= var1.getFrom() == 0) {
               List var4 = var1.getRemoved();

               int var5;
               for(var5 = 0; var5 < var1.getRemovedSize(); ++var5) {
                  ((PathElement)var4.get(var5)).removeNode(Path.this);
               }

               for(var5 = var1.getFrom(); var5 < var1.getTo(); ++var5) {
                  ((PathElement)var2.get(var5)).addNode(Path.this);
               }
            }

            if (Path.this.path2d != null) {
               var1.reset();
               var1.next();
               if (var1.getFrom() == var1.getList().size() && !var1.wasRemoved() && var1.wasAdded()) {
                  for(int var6 = var1.getFrom(); var6 < var1.getTo(); ++var6) {
                     ((PathElement)var2.get(var6)).impl_addTo(Path.this.path2d);
                  }
               } else {
                  Path.this.path2d = null;
               }
            }

            if (var3) {
               Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
            }

            Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
            Path.this.impl_geomChanged();
         }
      };
   }

   public Path(PathElement... var1) {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.elements = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            ObservableList var2 = var1.getList();

            boolean var3;
            for(var3 = false; var1.next(); var3 |= var1.getFrom() == 0) {
               List var4 = var1.getRemoved();

               int var5;
               for(var5 = 0; var5 < var1.getRemovedSize(); ++var5) {
                  ((PathElement)var4.get(var5)).removeNode(Path.this);
               }

               for(var5 = var1.getFrom(); var5 < var1.getTo(); ++var5) {
                  ((PathElement)var2.get(var5)).addNode(Path.this);
               }
            }

            if (Path.this.path2d != null) {
               var1.reset();
               var1.next();
               if (var1.getFrom() == var1.getList().size() && !var1.wasRemoved() && var1.wasAdded()) {
                  for(int var6 = var1.getFrom(); var6 < var1.getTo(); ++var6) {
                     ((PathElement)var2.get(var6)).impl_addTo(Path.this.path2d);
                  }
               } else {
                  Path.this.path2d = null;
               }
            }

            if (var3) {
               Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
            }

            Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
            Path.this.impl_geomChanged();
         }
      };
      if (var1 != null) {
         this.elements.addAll(var1);
      }

   }

   public Path(Collection var1) {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.elements = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            ObservableList var2 = var1.getList();

            boolean var3;
            for(var3 = false; var1.next(); var3 |= var1.getFrom() == 0) {
               List var4 = var1.getRemoved();

               int var5;
               for(var5 = 0; var5 < var1.getRemovedSize(); ++var5) {
                  ((PathElement)var4.get(var5)).removeNode(Path.this);
               }

               for(var5 = var1.getFrom(); var5 < var1.getTo(); ++var5) {
                  ((PathElement)var2.get(var5)).addNode(Path.this);
               }
            }

            if (Path.this.path2d != null) {
               var1.reset();
               var1.next();
               if (var1.getFrom() == var1.getList().size() && !var1.wasRemoved() && var1.wasAdded()) {
                  for(int var6 = var1.getFrom(); var6 < var1.getTo(); ++var6) {
                     ((PathElement)var2.get(var6)).impl_addTo(Path.this.path2d);
                  }
               } else {
                  Path.this.path2d = null;
               }
            }

            if (var3) {
               Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
            }

            Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
            Path.this.impl_geomChanged();
         }
      };
      if (var1 != null) {
         this.elements.addAll(var1);
      }

   }

   void markPathDirty() {
      this.path2d = null;
      this.impl_markDirty(DirtyBits.NODE_CONTENTS);
      this.impl_geomChanged();
   }

   public final void setFillRule(FillRule var1) {
      if (this.fillRule != null || var1 != FillRule.NON_ZERO) {
         this.fillRuleProperty().set(var1);
      }

   }

   public final FillRule getFillRule() {
      return this.fillRule == null ? FillRule.NON_ZERO : (FillRule)this.fillRule.get();
   }

   public final ObjectProperty fillRuleProperty() {
      if (this.fillRule == null) {
         this.fillRule = new ObjectPropertyBase(FillRule.NON_ZERO) {
            public void invalidated() {
               Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
               Path.this.impl_geomChanged();
            }

            public Object getBean() {
               return Path.this;
            }

            public String getName() {
               return "fillRule";
            }
         };
      }

      return this.fillRule;
   }

   public final ObservableList getElements() {
      return this.elements;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGPath();
   }

   /** @deprecated */
   @Deprecated
   public Path2D impl_configShape() {
      if (this.isPathValid) {
         if (this.path2d == null) {
            this.path2d = PathUtils.configShape(this.getElements(), this.getFillRule() == FillRule.EVEN_ODD);
         } else {
            this.path2d.setWindingRule(this.getFillRule() == FillRule.NON_ZERO ? 1 : 0);
         }

         return this.path2d;
      } else {
         return new Path2D();
      }
   }

   /** @deprecated */
   @Deprecated
   protected Bounds impl_computeLayoutBounds() {
      return (Bounds)(this.isPathValid ? super.impl_computeLayoutBounds() : new BoundingBox(0.0, 0.0, -1.0, -1.0));
   }

   private boolean impl_isFirstPathElementValid() {
      ObservableList var1 = this.getElements();
      if (var1 != null && var1.size() > 0) {
         PathElement var2 = (PathElement)var1.get(0);
         if (!var2.isAbsolute()) {
            System.err.printf("First element of the path can not be relative. Path: %s\n", this);
            return false;
         } else if (var2 instanceof MoveTo) {
            return true;
         } else {
            System.err.printf("Missing initial moveto in path definition. Path: %s\n", this);
            return false;
         }
      } else {
         return true;
      }
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         NGPath var1 = (NGPath)this.impl_getPeer();
         if (var1.acceptsPath2dOnUpdate()) {
            var1.updateWithPath2d(this.impl_configShape());
         } else {
            var1.reset();
            if (this.isPathValid) {
               var1.setFillRule(this.getFillRule());
               Iterator var2 = this.getElements().iterator();

               while(var2.hasNext()) {
                  PathElement var3 = (PathElement)var2.next();
                  var3.addTo(var1);
               }

               var1.update();
            }
         }
      }

   }

   /** @deprecated */
   @Deprecated
   protected Paint impl_cssGetFillInitialValue() {
      return null;
   }

   /** @deprecated */
   @Deprecated
   protected Paint impl_cssGetStrokeInitialValue() {
      return Color.BLACK;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Path[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("elements=").append(this.getElements());
      var1.append(", fill=").append(this.getFill());
      var1.append(", fillRule=").append(this.getFillRule());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
