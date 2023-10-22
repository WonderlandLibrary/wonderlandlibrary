package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class SplitPaneSkin extends BehaviorSkinBase {
   private ObservableList contentRegions;
   private ObservableList contentDividers;
   private boolean horizontal;
   private double previousSize = -1.0;
   private int lastDividerUpdate = 0;
   private boolean resize = false;
   private boolean checkDividerPos = true;

   public SplitPaneSkin(SplitPane var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.horizontal = ((SplitPane)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
      this.contentRegions = FXCollections.observableArrayList();
      this.contentDividers = FXCollections.observableArrayList();
      int var2 = 0;
      Iterator var3 = ((SplitPane)this.getSkinnable()).getItems().iterator();

      while(var3.hasNext()) {
         Node var4 = (Node)var3.next();
         this.addContent(var2++, var4);
      }

      this.initializeContentListener();
      var3 = ((SplitPane)this.getSkinnable()).getDividers().iterator();

      while(var3.hasNext()) {
         SplitPane.Divider var5 = (SplitPane.Divider)var3.next();
         this.addDivider(var5);
      }

      this.registerChangeListener(var1.orientationProperty(), "ORIENTATION");
      this.registerChangeListener(var1.widthProperty(), "WIDTH");
      this.registerChangeListener(var1.heightProperty(), "HEIGHT");
   }

   private void addContent(int var1, Node var2) {
      Content var3 = new Content(var2);
      this.contentRegions.add(var1, var3);
      this.getChildren().add(var1, var3);
   }

   private void removeContent(Node var1) {
      Iterator var2 = this.contentRegions.iterator();

      while(var2.hasNext()) {
         Content var3 = (Content)var2.next();
         if (var3.getContent().equals(var1)) {
            this.getChildren().remove(var3);
            this.contentRegions.remove(var3);
            break;
         }
      }

   }

   private void initializeContentListener() {
      ((SplitPane)this.getSkinnable()).getItems().addListener((var1) -> {
         label47:
         while(true) {
            Iterator var2;
            if (var1.next()) {
               Node var4;
               int var5;
               Iterator var6;
               if (!var1.wasPermutated() && !var1.wasUpdated()) {
                  var2 = var1.getRemoved().iterator();

                  while(var2.hasNext()) {
                     Node var7 = (Node)var2.next();
                     this.removeContent(var7);
                  }

                  var5 = var1.getFrom();
                  var6 = var1.getAddedSubList().iterator();

                  while(true) {
                     if (!var6.hasNext()) {
                        continue label47;
                     }

                     var4 = (Node)var6.next();
                     this.addContent(var5++, var4);
                  }
               }

               this.getChildren().clear();
               this.contentRegions.clear();
               var5 = 0;
               var6 = var1.getList().iterator();

               while(true) {
                  if (!var6.hasNext()) {
                     continue label47;
                  }

                  var4 = (Node)var6.next();
                  this.addContent(var5++, var4);
               }
            }

            this.removeAllDividers();
            var2 = ((SplitPane)this.getSkinnable()).getDividers().iterator();

            while(var2.hasNext()) {
               SplitPane.Divider var3 = (SplitPane.Divider)var2.next();
               this.addDivider(var3);
            }

            return;
         }
      });
   }

   private void checkDividerPosition(ContentDivider var1, double var2, double var4) {
      double var6 = var1.prefWidth(-1.0);
      Content var8 = this.getLeft(var1);
      Content var9 = this.getRight(var1);
      double var10 = var8 == null ? 0.0 : (this.horizontal ? var8.minWidth(-1.0) : var8.minHeight(-1.0));
      double var12 = var9 == null ? 0.0 : (this.horizontal ? var9.minWidth(-1.0) : var9.minHeight(-1.0));
      double var14 = var8 == null ? 0.0 : (var8.getContent() != null ? (this.horizontal ? var8.getContent().maxWidth(-1.0) : var8.getContent().maxHeight(-1.0)) : 0.0);
      double var16 = var9 == null ? 0.0 : (var9.getContent() != null ? (this.horizontal ? var9.getContent().maxWidth(-1.0) : var9.getContent().maxHeight(-1.0)) : 0.0);
      double var18 = 0.0;
      double var20 = this.getSize();
      int var22 = this.contentDividers.indexOf(var1);
      if (var22 - 1 >= 0) {
         var18 = ((ContentDivider)this.contentDividers.get(var22 - 1)).getDividerPos();
         if (var18 == -1.0) {
            var18 = this.getAbsoluteDividerPos((ContentDivider)this.contentDividers.get(var22 - 1));
         }
      }

      if (var22 + 1 < this.contentDividers.size()) {
         var20 = ((ContentDivider)this.contentDividers.get(var22 + 1)).getDividerPos();
         if (var20 == -1.0) {
            var20 = this.getAbsoluteDividerPos((ContentDivider)this.contentDividers.get(var22 + 1));
         }
      }

      this.checkDividerPos = false;
      double var23;
      double var25;
      double var27;
      double var29;
      if (var2 > var4) {
         var23 = var18 == 0.0 ? var14 : var18 + var6 + var14;
         var25 = var20 - var12 - var6;
         var27 = Math.min(var23, var25);
         if (var2 >= var27) {
            this.setAbsoluteDividerPos(var1, var27);
         } else {
            var29 = var20 - var16 - var6;
            if (var2 <= var29) {
               this.setAbsoluteDividerPos(var1, var29);
            } else {
               this.setAbsoluteDividerPos(var1, var2);
            }
         }
      } else {
         var23 = var20 - var16 - var6;
         var25 = var18 == 0.0 ? var10 : var18 + var10 + var6;
         var27 = Math.max(var23, var25);
         if (var2 <= var27) {
            this.setAbsoluteDividerPos(var1, var27);
         } else {
            var29 = var18 + var14 + var6;
            if (var2 >= var29) {
               this.setAbsoluteDividerPos(var1, var29);
            } else {
               this.setAbsoluteDividerPos(var1, var2);
            }
         }
      }

      this.checkDividerPos = true;
   }

   private void addDivider(SplitPane.Divider var1) {
      ContentDivider var2 = new ContentDivider(var1);
      var2.setInitialPos(var1.getPosition());
      var2.setDividerPos(-1.0);
      PosPropertyListener var3 = new PosPropertyListener(var2);
      var2.setPosPropertyListener(var3);
      var1.positionProperty().addListener(var3);
      this.initializeDivderEventHandlers(var2);
      this.contentDividers.add(var2);
      this.getChildren().add(var2);
   }

   private void removeAllDividers() {
      ListIterator var1 = this.contentDividers.listIterator();

      while(var1.hasNext()) {
         ContentDivider var2 = (ContentDivider)var1.next();
         this.getChildren().remove(var2);
         var2.getDivider().positionProperty().removeListener(var2.getPosPropertyListener());
         var1.remove();
      }

      this.lastDividerUpdate = 0;
   }

   private void initializeDivderEventHandlers(ContentDivider var1) {
      var1.addEventHandler(MouseEvent.ANY, (var0) -> {
         var0.consume();
      });
      var1.setOnMousePressed((var2) -> {
         if (this.horizontal) {
            var1.setInitialPos(var1.getDividerPos());
            var1.setPressPos(var2.getSceneX());
            var1.setPressPos(((SplitPane)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane)this.getSkinnable()).getWidth() - var2.getSceneX() : var2.getSceneX());
         } else {
            var1.setInitialPos(var1.getDividerPos());
            var1.setPressPos(var2.getSceneY());
         }

         var2.consume();
      });
      var1.setOnMouseDragged((var2) -> {
         double var3 = 0.0;
         if (this.horizontal) {
            var3 = ((SplitPane)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane)this.getSkinnable()).getWidth() - var2.getSceneX() : var2.getSceneX();
         } else {
            var3 = var2.getSceneY();
         }

         var3 -= var1.getPressPos();
         this.setAndCheckAbsoluteDividerPos(var1, Math.ceil(var1.getInitialPos() + var3));
         var2.consume();
      });
   }

   private Content getLeft(ContentDivider var1) {
      int var2 = this.contentDividers.indexOf(var1);
      return var2 != -1 ? (Content)this.contentRegions.get(var2) : null;
   }

   private Content getRight(ContentDivider var1) {
      int var2 = this.contentDividers.indexOf(var1);
      return var2 != -1 ? (Content)this.contentRegions.get(var2 + 1) : null;
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ORIENTATION".equals(var1)) {
         this.horizontal = ((SplitPane)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
         this.previousSize = -1.0;
         Iterator var2 = this.contentDividers.iterator();

         while(var2.hasNext()) {
            ContentDivider var3 = (ContentDivider)var2.next();
            var3.setGrabberStyle(this.horizontal);
         }

         ((SplitPane)this.getSkinnable()).requestLayout();
      } else if ("WIDTH".equals(var1) || "HEIGHT".equals(var1)) {
         ((SplitPane)this.getSkinnable()).requestLayout();
      }

   }

   private void setAbsoluteDividerPos(ContentDivider var1, double var2) {
      if (((SplitPane)this.getSkinnable()).getWidth() > 0.0 && ((SplitPane)this.getSkinnable()).getHeight() > 0.0 && var1 != null) {
         SplitPane.Divider var4 = var1.getDivider();
         var1.setDividerPos(var2);
         double var5 = this.getSize();
         if (var5 != 0.0) {
            double var7 = var2 + var1.prefWidth(-1.0) / 2.0;
            var4.setPosition(var7 / var5);
         } else {
            var4.setPosition(0.0);
         }
      }

   }

   private double getAbsoluteDividerPos(ContentDivider var1) {
      if (((SplitPane)this.getSkinnable()).getWidth() > 0.0 && ((SplitPane)this.getSkinnable()).getHeight() > 0.0 && var1 != null) {
         SplitPane.Divider var2 = var1.getDivider();
         double var3 = this.posToDividerPos(var1, var2.getPosition());
         var1.setDividerPos(var3);
         return var3;
      } else {
         return 0.0;
      }
   }

   private double posToDividerPos(ContentDivider var1, double var2) {
      double var4 = this.getSize() * var2;
      if (var2 == 1.0) {
         var4 -= var1.prefWidth(-1.0);
      } else {
         var4 -= var1.prefWidth(-1.0) / 2.0;
      }

      return (double)Math.round(var4);
   }

   private double totalMinSize() {
      double var1 = !this.contentDividers.isEmpty() ? (double)this.contentDividers.size() * ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0) : 0.0;
      double var3 = 0.0;
      Iterator var5 = this.contentRegions.iterator();

      while(var5.hasNext()) {
         Content var6 = (Content)var5.next();
         if (this.horizontal) {
            var3 += var6.minWidth(-1.0);
         } else {
            var3 += var6.minHeight(-1.0);
         }
      }

      return var3 + var1;
   }

   private double getSize() {
      SplitPane var1 = (SplitPane)this.getSkinnable();
      double var2 = this.totalMinSize();
      if (this.horizontal) {
         if (var1.getWidth() > var2) {
            var2 = var1.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
         }
      } else if (var1.getHeight() > var2) {
         var2 = var1.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
      }

      return var2;
   }

   private double distributeTo(List var1, double var2) {
      if (var1.isEmpty()) {
         return var2;
      } else {
         var2 = this.snapSize(var2);
         int var4 = (int)var2 / var1.size();

         while(var2 > 0.0 && !var1.isEmpty()) {
            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Content var7 = (Content)var6.next();
               double var8 = Math.min(this.horizontal ? var7.maxWidth(-1.0) : var7.maxHeight(-1.0), Double.MAX_VALUE);
               double var10 = this.horizontal ? var7.minWidth(-1.0) : var7.minHeight(-1.0);
               if (var7.getArea() >= var8) {
                  var7.setAvailable(var7.getArea() - var10);
                  var6.remove();
               } else {
                  if ((double)var4 >= var8 - var7.getArea()) {
                     var2 -= var8 - var7.getArea();
                     var7.setArea(var8);
                     var7.setAvailable(var8 - var10);
                     var6.remove();
                  } else {
                     var7.setArea(var7.getArea() + (double)var4);
                     var7.setAvailable(var7.getArea() - var10);
                     var2 -= (double)var4;
                  }

                  if ((int)var2 == 0) {
                     return var2;
                  }
               }
            }

            if (var1.isEmpty()) {
               return var2;
            }

            var4 = (int)var2 / var1.size();
            int var5 = (int)var2 % var1.size();
            if (var4 == 0 && var5 != 0) {
               var4 = var5;
               boolean var12 = false;
            }
         }

         return var2;
      }
   }

   private double distributeFrom(double var1, List var3) {
      if (var3.isEmpty()) {
         return var1;
      } else {
         var1 = this.snapSize(var1);
         int var4 = (int)var1 / var3.size();

         while(var1 > 0.0 && !var3.isEmpty()) {
            Iterator var6 = var3.iterator();

            while(var6.hasNext()) {
               Content var7 = (Content)var6.next();
               if ((double)var4 >= var7.getAvailable()) {
                  var7.setArea(var7.getArea() - var7.getAvailable());
                  var1 -= var7.getAvailable();
                  var7.setAvailable(0.0);
                  var6.remove();
               } else {
                  var7.setArea(var7.getArea() - (double)var4);
                  var7.setAvailable(var7.getAvailable() - (double)var4);
                  var1 -= (double)var4;
               }

               if ((int)var1 == 0) {
                  return var1;
               }
            }

            if (var3.isEmpty()) {
               return var1;
            }

            var4 = (int)var1 / var3.size();
            int var5 = (int)var1 % var3.size();
            if (var4 == 0 && var5 != 0) {
               var4 = var5;
               boolean var8 = false;
            }
         }

         return var1;
      }
   }

   private void setupContentAndDividerForLayout() {
      double var1 = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
      double var3 = 0.0;
      double var5 = 0.0;
      Iterator var7 = this.contentRegions.iterator();

      while(var7.hasNext()) {
         Content var8 = (Content)var7.next();
         if (this.resize && !var8.isResizableWithParent()) {
            var8.setArea(var8.getResizableWithParentArea());
         }

         var8.setX(var3);
         var8.setY(var5);
         if (this.horizontal) {
            var3 += var8.getArea() + var1;
         } else {
            var5 += var8.getArea() + var1;
         }
      }

      var3 = 0.0;
      var5 = 0.0;
      this.checkDividerPos = false;

      for(int var9 = 0; var9 < this.contentDividers.size(); ++var9) {
         ContentDivider var10 = (ContentDivider)this.contentDividers.get(var9);
         if (this.horizontal) {
            var3 += this.getLeft(var10).getArea() + (var9 == 0 ? 0.0 : var1);
         } else {
            var5 += this.getLeft(var10).getArea() + (var9 == 0 ? 0.0 : var1);
         }

         var10.setX(var3);
         var10.setY(var5);
         this.setAbsoluteDividerPos(var10, this.horizontal ? var10.getX() : var10.getY());
         var10.posExplicit = false;
      }

      this.checkDividerPos = true;
   }

   private void layoutDividersAndContent(double var1, double var3) {
      double var5 = this.snappedLeftInset();
      double var7 = this.snappedTopInset();
      double var9 = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
      Iterator var11 = this.contentRegions.iterator();

      while(var11.hasNext()) {
         Content var12 = (Content)var11.next();
         if (this.horizontal) {
            var12.setClipSize(var12.getArea(), var3);
            this.layoutInArea(var12, var12.getX() + var5, var12.getY() + var7, var12.getArea(), var3, 0.0, HPos.CENTER, VPos.CENTER);
         } else {
            var12.setClipSize(var1, var12.getArea());
            this.layoutInArea(var12, var12.getX() + var5, var12.getY() + var7, var1, var12.getArea(), 0.0, HPos.CENTER, VPos.CENTER);
         }
      }

      var11 = this.contentDividers.iterator();

      while(var11.hasNext()) {
         ContentDivider var13 = (ContentDivider)var11.next();
         if (this.horizontal) {
            var13.resize(var9, var3);
            this.positionInArea(var13, var13.getX() + var5, var13.getY() + var7, var9, var3, 0.0, HPos.CENTER, VPos.CENTER);
         } else {
            var13.resize(var1, var9);
            this.positionInArea(var13, var13.getX() + var5, var13.getY() + var7, var1, var9, 0.0, HPos.CENTER, VPos.CENTER);
         }
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      SplitPane var9 = (SplitPane)this.getSkinnable();
      double var10 = var9.getWidth();
      double var12 = var9.getHeight();
      if (var9.isVisible()) {
         if (this.horizontal) {
            if (var10 == 0.0) {
               return;
            }
         } else if (var12 == 0.0) {
            return;
         }

         if (!this.contentRegions.isEmpty()) {
            double var14 = this.contentDividers.isEmpty() ? 0.0 : ((ContentDivider)this.contentDividers.get(0)).prefWidth(-1.0);
            int var20;
            int var21;
            double var27;
            if (this.contentDividers.size() > 0 && this.previousSize != -1.0 && this.previousSize != (this.horizontal ? var10 : var12)) {
               ArrayList var16 = new ArrayList();
               Iterator var17 = this.contentRegions.iterator();

               while(var17.hasNext()) {
                  Content var18 = (Content)var17.next();
                  if (var18.isResizableWithParent()) {
                     var16.add(var18);
                  }
               }

               double var38 = (this.horizontal ? var9.getWidth() : var9.getHeight()) - this.previousSize;
               boolean var19 = var38 > 0.0;
               var38 = Math.abs(var38);
               if (var38 != 0.0 && !var16.isEmpty()) {
                  var20 = (int)var38 / var16.size();
                  var21 = (int)var38 % var16.size();
                  boolean var22 = false;
                  int var43;
                  if (var20 == 0) {
                     var20 = var21;
                     var43 = var21;
                     var21 = 0;
                  } else {
                     var43 = var20 * var16.size();
                  }

                  Content var24;
                  while(var43 > 0 && !var16.isEmpty()) {
                     if (var19) {
                        ++this.lastDividerUpdate;
                     } else {
                        --this.lastDividerUpdate;
                        if (this.lastDividerUpdate < 0) {
                           this.lastDividerUpdate = this.contentRegions.size() - 1;
                        }
                     }

                     int var23 = this.lastDividerUpdate % this.contentRegions.size();
                     var24 = (Content)this.contentRegions.get(var23);
                     if (var24.isResizableWithParent() && var16.contains(var24)) {
                        double var25 = var24.getArea();
                        if (var19) {
                           var27 = this.horizontal ? var24.maxWidth(-1.0) : var24.maxHeight(-1.0);
                           if (!(var25 + (double)var20 <= var27)) {
                              var16.remove(var24);
                              continue;
                           }

                           var25 += (double)var20;
                        } else {
                           var27 = this.horizontal ? var24.minWidth(-1.0) : var24.minHeight(-1.0);
                           if (!(var25 - (double)var20 >= var27)) {
                              var16.remove(var24);
                              continue;
                           }

                           var25 -= (double)var20;
                        }

                        var24.setArea(var25);
                        var43 -= var20;
                        if (var43 == 0 && var21 != 0) {
                           var20 = var21;
                           var43 = var21;
                           var21 = 0;
                        } else if (var43 == 0) {
                           break;
                        }
                     }
                  }

                  Iterator var45 = this.contentRegions.iterator();

                  while(var45.hasNext()) {
                     var24 = (Content)var45.next();
                     var24.setResizableWithParentArea(var24.getArea());
                     var24.setAvailable(0.0);
                  }

                  this.resize = true;
               }

               this.previousSize = this.horizontal ? var10 : var12;
            } else {
               this.previousSize = this.horizontal ? var10 : var12;
            }

            double var37 = this.totalMinSize();
            double var47;
            if (var37 > (this.horizontal ? var5 : var7)) {
               double var40 = 0.0;

               for(var20 = 0; var20 < this.contentRegions.size(); ++var20) {
                  Content var49 = (Content)this.contentRegions.get(var20);
                  var47 = this.horizontal ? var49.minWidth(-1.0) : var49.minHeight(-1.0);
                  var40 = var47 / var37;
                  var49.setArea(this.snapSpace(var40 * (this.horizontal ? var5 : var7)));
                  var49.setAvailable(0.0);
               }

               this.setupContentAndDividerForLayout();
               this.layoutDividersAndContent(var5, var7);
               this.resize = false;
               return;
            }

            for(int var39 = 0; var39 < 10; ++var39) {
               ContentDivider var41 = null;
               ContentDivider var42 = null;

               for(var21 = 0; var21 < this.contentRegions.size(); ++var21) {
                  var47 = 0.0;
                  if (var21 < this.contentDividers.size()) {
                     var42 = (ContentDivider)this.contentDividers.get(var21);
                     if (var42.posExplicit) {
                        this.checkDividerPosition(var42, this.posToDividerPos(var42, var42.d.getPosition()), var42.getDividerPos());
                     }

                     if (var21 == 0) {
                        var47 = this.getAbsoluteDividerPos(var42);
                     } else {
                        double var48 = this.getAbsoluteDividerPos(var41) + var14;
                        if (this.getAbsoluteDividerPos(var42) <= this.getAbsoluteDividerPos(var41)) {
                           this.setAndCheckAbsoluteDividerPos(var42, var48);
                        }

                        var47 = this.getAbsoluteDividerPos(var42) - var48;
                     }
                  } else if (var21 == this.contentDividers.size()) {
                     var47 = (this.horizontal ? var5 : var7) - (var41 != null ? this.getAbsoluteDividerPos(var41) + var14 : 0.0);
                  }

                  if (!this.resize || var42.posExplicit) {
                     ((Content)this.contentRegions.get(var21)).setArea(var47);
                  }

                  var41 = var42;
               }

               double var44 = 0.0;
               double var46 = 0.0;
               Iterator var50 = this.contentRegions.iterator();

               while(var50.hasNext()) {
                  Content var26 = (Content)var50.next();
                  var27 = 0.0;
                  double var29 = 0.0;
                  if (var26 != null) {
                     var27 = this.horizontal ? var26.maxWidth(-1.0) : var26.maxHeight(-1.0);
                     var29 = this.horizontal ? var26.minWidth(-1.0) : var26.minHeight(-1.0);
                  }

                  if (var26.getArea() >= var27) {
                     var46 += var26.getArea() - var27;
                     var26.setArea(var27);
                  }

                  var26.setAvailable(var26.getArea() - var29);
                  if (var26.getAvailable() < 0.0) {
                     var44 += var26.getAvailable();
                  }
               }

               var44 = Math.abs(var44);
               ArrayList var52 = new ArrayList();
               ArrayList var51 = new ArrayList();
               ArrayList var53 = new ArrayList();
               double var28 = 0.0;
               Iterator var30 = this.contentRegions.iterator();

               Content var31;
               while(var30.hasNext()) {
                  var31 = (Content)var30.next();
                  if (var31.getAvailable() >= 0.0) {
                     var28 += var31.getAvailable();
                     var52.add(var31);
                  }

                  if (this.resize && !var31.isResizableWithParent()) {
                     if (var31.getArea() >= var31.getResizableWithParentArea()) {
                        var46 += var31.getArea() - var31.getResizableWithParentArea();
                     } else {
                        var44 += var31.getResizableWithParentArea() - var31.getArea();
                     }

                     var31.setAvailable(0.0);
                  }

                  if (this.resize) {
                     if (var31.isResizableWithParent()) {
                        var51.add(var31);
                     }
                  } else {
                     var51.add(var31);
                  }

                  if (var31.getAvailable() < 0.0) {
                     var53.add(var31);
                  }
               }

               if (var46 > 0.0) {
                  var46 = this.distributeTo(var51, var46);
                  var44 = 0.0;
                  var53.clear();
                  var28 = 0.0;
                  var52.clear();
                  var30 = this.contentRegions.iterator();

                  while(var30.hasNext()) {
                     var31 = (Content)var30.next();
                     if (var31.getAvailable() < 0.0) {
                        var44 += var31.getAvailable();
                        var53.add(var31);
                     } else {
                        var28 += var31.getAvailable();
                        var52.add(var31);
                     }
                  }

                  var44 = Math.abs(var44);
               }

               if (var28 >= var44) {
                  var30 = var53.iterator();

                  while(var30.hasNext()) {
                     var31 = (Content)var30.next();
                     double var32 = this.horizontal ? var31.minWidth(-1.0) : var31.minHeight(-1.0);
                     var31.setArea(var32);
                     var31.setAvailable(0.0);
                  }

                  if (var44 > 0.0 && !var53.isEmpty()) {
                     this.distributeFrom(var44, var52);
                  }

                  if (this.resize) {
                     double var54 = 0.0;
                     Iterator var57 = this.contentRegions.iterator();

                     while(var57.hasNext()) {
                        Content var33 = (Content)var57.next();
                        if (var33.isResizableWithParent()) {
                           var54 += var33.getArea();
                        } else {
                           var54 += var33.getResizableWithParentArea();
                        }
                     }

                     var54 += var14 * (double)this.contentDividers.size();
                     if (var54 < (this.horizontal ? var5 : var7)) {
                        var46 += (this.horizontal ? var5 : var7) - var54;
                        this.distributeTo(var51, var46);
                     } else {
                        var44 += var54 - (this.horizontal ? var5 : var7);
                        this.distributeFrom(var44, var51);
                     }
                  }
               }

               this.setupContentAndDividerForLayout();
               boolean var56 = true;
               Iterator var55 = this.contentRegions.iterator();

               while(var55.hasNext()) {
                  Content var58 = (Content)var55.next();
                  double var59 = this.horizontal ? var58.maxWidth(-1.0) : var58.maxHeight(-1.0);
                  double var35 = this.horizontal ? var58.minWidth(-1.0) : var58.minHeight(-1.0);
                  if (var58.getArea() < var35 || var58.getArea() > var59) {
                     var56 = false;
                     break;
                  }
               }

               if (var56) {
                  break;
               }
            }

            this.layoutDividersAndContent(var5, var7);
            this.resize = false;
            return;
         }
      }

   }

   private void setAndCheckAbsoluteDividerPos(ContentDivider var1, double var2) {
      double var4 = var1.getDividerPos();
      this.setAbsoluteDividerPos(var1, var2);
      this.checkDividerPosition(var1, var2, var4);
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;

      Content var16;
      Iterator var15;
      for(var15 = this.contentRegions.iterator(); var15.hasNext(); var13 = Math.max(var13, var16.minWidth(-1.0))) {
         var16 = (Content)var15.next();
         var11 += var16.minWidth(-1.0);
      }

      ContentDivider var17;
      for(var15 = this.contentDividers.iterator(); var15.hasNext(); var11 += var17.prefWidth(-1.0)) {
         var17 = (ContentDivider)var15.next();
      }

      return this.horizontal ? var11 + var9 + var5 : var13 + var9 + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;

      Content var16;
      Iterator var15;
      for(var15 = this.contentRegions.iterator(); var15.hasNext(); var13 = Math.max(var13, var16.minHeight(-1.0))) {
         var16 = (Content)var15.next();
         var11 += var16.minHeight(-1.0);
      }

      ContentDivider var17;
      for(var15 = this.contentDividers.iterator(); var15.hasNext(); var11 += var17.prefWidth(-1.0)) {
         var17 = (ContentDivider)var15.next();
      }

      return this.horizontal ? var13 + var3 + var7 : var11 + var3 + var7;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;

      Content var16;
      Iterator var15;
      for(var15 = this.contentRegions.iterator(); var15.hasNext(); var13 = Math.max(var13, var16.prefWidth(-1.0))) {
         var16 = (Content)var15.next();
         var11 += var16.prefWidth(-1.0);
      }

      ContentDivider var17;
      for(var15 = this.contentDividers.iterator(); var15.hasNext(); var11 += var17.prefWidth(-1.0)) {
         var17 = (ContentDivider)var15.next();
      }

      return this.horizontal ? var11 + var9 + var5 : var13 + var9 + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;

      Content var16;
      Iterator var15;
      for(var15 = this.contentRegions.iterator(); var15.hasNext(); var13 = Math.max(var13, var16.prefHeight(-1.0))) {
         var16 = (Content)var15.next();
         var11 += var16.prefHeight(-1.0);
      }

      ContentDivider var17;
      for(var15 = this.contentDividers.iterator(); var15.hasNext(); var11 += var17.prefWidth(-1.0)) {
         var17 = (ContentDivider)var15.next();
      }

      return this.horizontal ? var13 + var3 + var7 : var11 + var3 + var7;
   }

   static class Content extends StackPane {
      private Node content;
      private Rectangle clipRect = new Rectangle();
      private double x;
      private double y;
      private double area;
      private double resizableWithParentArea;
      private double available;

      public Content(Node var1) {
         this.setClip(this.clipRect);
         this.content = var1;
         if (var1 != null) {
            this.getChildren().add(var1);
         }

         this.x = 0.0;
         this.y = 0.0;
      }

      public Node getContent() {
         return this.content;
      }

      public double getX() {
         return this.x;
      }

      public void setX(double var1) {
         this.x = var1;
      }

      public double getY() {
         return this.y;
      }

      public void setY(double var1) {
         this.y = var1;
      }

      public double getArea() {
         return this.area;
      }

      public void setArea(double var1) {
         this.area = var1;
      }

      public double getAvailable() {
         return this.available;
      }

      public void setAvailable(double var1) {
         this.available = var1;
      }

      public boolean isResizableWithParent() {
         return SplitPane.isResizableWithParent(this.content);
      }

      public double getResizableWithParentArea() {
         return this.resizableWithParentArea;
      }

      public void setResizableWithParentArea(double var1) {
         if (!this.isResizableWithParent()) {
            this.resizableWithParentArea = var1;
         } else {
            this.resizableWithParentArea = 0.0;
         }

      }

      protected void setClipSize(double var1, double var3) {
         this.clipRect.setWidth(var1);
         this.clipRect.setHeight(var3);
      }

      protected double computeMaxWidth(double var1) {
         return this.snapSize(this.content.maxWidth(var1));
      }

      protected double computeMaxHeight(double var1) {
         return this.snapSize(this.content.maxHeight(var1));
      }
   }

   class ContentDivider extends StackPane {
      private double initialPos;
      private double dividerPos;
      private double pressPos;
      private SplitPane.Divider d;
      private StackPane grabber;
      private double x;
      private double y;
      private boolean posExplicit;
      private ChangeListener listener;

      public ContentDivider(SplitPane.Divider var2) {
         this.getStyleClass().setAll((Object[])("split-pane-divider"));
         this.d = var2;
         this.initialPos = 0.0;
         this.dividerPos = 0.0;
         this.pressPos = 0.0;
         this.grabber = new StackPane() {
            protected double computeMinWidth(double var1) {
               return 0.0;
            }

            protected double computeMinHeight(double var1) {
               return 0.0;
            }

            protected double computePrefWidth(double var1) {
               return this.snappedLeftInset() + this.snappedRightInset();
            }

            protected double computePrefHeight(double var1) {
               return this.snappedTopInset() + this.snappedBottomInset();
            }

            protected double computeMaxWidth(double var1) {
               return this.computePrefWidth(-1.0);
            }

            protected double computeMaxHeight(double var1) {
               return this.computePrefHeight(-1.0);
            }
         };
         this.setGrabberStyle(SplitPaneSkin.this.horizontal);
         this.getChildren().add(this.grabber);
      }

      public SplitPane.Divider getDivider() {
         return this.d;
      }

      public final void setGrabberStyle(boolean var1) {
         this.grabber.getStyleClass().clear();
         this.grabber.getStyleClass().setAll((Object[])("vertical-grabber"));
         this.setCursor(Cursor.V_RESIZE);
         if (var1) {
            this.grabber.getStyleClass().setAll((Object[])("horizontal-grabber"));
            this.setCursor(Cursor.H_RESIZE);
         }

      }

      public double getInitialPos() {
         return this.initialPos;
      }

      public void setInitialPos(double var1) {
         this.initialPos = var1;
      }

      public double getDividerPos() {
         return this.dividerPos;
      }

      public void setDividerPos(double var1) {
         this.dividerPos = var1;
      }

      public double getPressPos() {
         return this.pressPos;
      }

      public void setPressPos(double var1) {
         this.pressPos = var1;
      }

      public double getX() {
         return this.x;
      }

      public void setX(double var1) {
         this.x = var1;
      }

      public double getY() {
         return this.y;
      }

      public void setY(double var1) {
         this.y = var1;
      }

      public ChangeListener getPosPropertyListener() {
         return this.listener;
      }

      public void setPosPropertyListener(ChangeListener var1) {
         this.listener = var1;
      }

      protected double computeMinWidth(double var1) {
         return this.computePrefWidth(var1);
      }

      protected double computeMinHeight(double var1) {
         return this.computePrefHeight(var1);
      }

      protected double computePrefWidth(double var1) {
         return this.snappedLeftInset() + this.snappedRightInset();
      }

      protected double computePrefHeight(double var1) {
         return this.snappedTopInset() + this.snappedBottomInset();
      }

      protected double computeMaxWidth(double var1) {
         return this.computePrefWidth(var1);
      }

      protected double computeMaxHeight(double var1) {
         return this.computePrefHeight(var1);
      }

      protected void layoutChildren() {
         double var1 = this.grabber.prefWidth(-1.0);
         double var3 = this.grabber.prefHeight(-1.0);
         double var5 = (this.getWidth() - var1) / 2.0;
         double var7 = (this.getHeight() - var3) / 2.0;
         this.grabber.resize(var1, var3);
         this.positionInArea(this.grabber, var5, var7, var1, var3, 0.0, HPos.CENTER, VPos.CENTER);
      }
   }

   class PosPropertyListener implements ChangeListener {
      ContentDivider divider;

      public PosPropertyListener(ContentDivider var2) {
         this.divider = var2;
      }

      public void changed(ObservableValue var1, Number var2, Number var3) {
         if (SplitPaneSkin.this.checkDividerPos) {
            this.divider.posExplicit = true;
         }

         ((SplitPane)SplitPaneSkin.this.getSkinnable()).requestLayout();
      }
   }
}
