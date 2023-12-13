package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.javafx.stage.StageHelper;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

class FXDialog extends Stage {
   private BorderPane root;
   private StackPane decoratedRoot;
   private ToolBar toolBar;
   private HBox windowBtns;
   private Button minButton;
   private Button maxButton;
   private Rectangle resizeCorner;
   private double mouseDragOffsetX;
   private double mouseDragOffsetY;
   protected Label titleLabel;
   private static final int HEADER_HEIGHT = 28;
   private static final PseudoClass PSEUDO_CLASS_ACTIVE = PseudoClass.getPseudoClass("active");

   FXDialog(String var1) {
      this(var1, (Window)null, false);
   }

   FXDialog(String var1, Window var2, boolean var3) {
      this(var1, var2, var3, StageStyle.TRANSPARENT, true);
   }

   FXDialog(String var1, Window var2, boolean var3, StageStyle var4, boolean var5) {
      super(var4);
      this.mouseDragOffsetX = 0.0;
      this.mouseDragOffsetY = 0.0;
      StageHelper.initSecurityDialog(this, var5);
      this.setTitle(var1);
      if (var2 != null) {
         this.initOwner(var2);
      }

      if (var3) {
         this.initModality(Modality.WINDOW_MODAL);
      }

      this.resizableProperty().addListener(new InvalidationListener() {
         public void invalidated(Observable var1) {
            FXDialog.this.resizeCorner.setVisible(FXDialog.this.isResizable());
            FXDialog.this.maxButton.setVisible(FXDialog.this.isResizable());
         }
      });
      this.root = new BorderPane();
      Scene var6;
      if (var4 == StageStyle.DECORATED) {
         var6 = new Scene(this.root);
         var6.getStylesheets().addAll(FXDialog.class.getResource("deploydialogs.css").toExternalForm());
         this.setScene(var6);
      } else {
         this.decoratedRoot = new StackPane() {
            protected void layoutChildren() {
               super.layoutChildren();
               if (FXDialog.this.resizeCorner != null) {
                  FXDialog.this.resizeCorner.relocate(this.getWidth() - 20.0, this.getHeight() - 20.0);
               }

            }
         };
         this.decoratedRoot.getChildren().add(this.root);
         var6 = new Scene(this.decoratedRoot);
         String var7 = (String)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               URL var1 = FXDialog.class.getResource("deploydialogs.css");
               return var1.toExternalForm();
            }
         });
         var6.getStylesheets().addAll(var7);
         var6.setFill(Color.TRANSPARENT);
         this.setScene(var6);
         this.decoratedRoot.getStyleClass().add("decorated-root");
         this.focusedProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable var1) {
               boolean var2 = ((ReadOnlyBooleanProperty)var1).get();
               FXDialog.this.decoratedRoot.pseudoClassStateChanged(FXDialog.PSEUDO_CLASS_ACTIVE, var2);
            }
         });
         this.toolBar = new ToolBar();
         this.toolBar.setId("window-header");
         this.toolBar.setPrefHeight(28.0);
         this.toolBar.setMinHeight(28.0);
         this.toolBar.setMaxHeight(28.0);
         this.addDragHandlers(this.toolBar);
         this.titleLabel = new Label();
         this.titleLabel.setId("window-title");
         this.titleLabel.setText(this.getTitle());
         this.titleProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable var1) {
               FXDialog.this.titleLabel.setText(FXDialog.this.getTitle());
            }
         });
         Region var8 = new Region();
         HBox.setHgrow(var8, Priority.ALWAYS);
         WindowButton var9 = new WindowButton("close");
         var9.setOnAction(new EventHandler() {
            public void handle(Event var1) {
               FXDialog.this.hide();
            }
         });
         this.minButton = new WindowButton("minimize");
         this.minButton.setOnAction(new EventHandler() {
            public void handle(Event var1) {
               FXDialog.this.setIconified(!FXDialog.this.isIconified());
            }
         });
         this.maxButton = new WindowButton("maximize");
         this.maxButton.setOnAction(new EventHandler() {
            private double restoreX;
            private double restoreY;
            private double restoreW;
            private double restoreH;

            public void handle(Event var1) {
               Screen var2 = Screen.getPrimary();
               double var3 = var2.getVisualBounds().getMinX();
               double var5 = var2.getVisualBounds().getMinY();
               double var7 = var2.getVisualBounds().getWidth();
               double var9 = var2.getVisualBounds().getHeight();
               if (this.restoreW != 0.0 && FXDialog.this.getX() == var3 && FXDialog.this.getY() == var5 && FXDialog.this.getWidth() == var7 && FXDialog.this.getHeight() == var9) {
                  FXDialog.this.setX(this.restoreX);
                  FXDialog.this.setY(this.restoreY);
                  FXDialog.this.setWidth(this.restoreW);
                  FXDialog.this.setHeight(this.restoreH);
               } else {
                  this.restoreX = FXDialog.this.getX();
                  this.restoreY = FXDialog.this.getY();
                  this.restoreW = FXDialog.this.getWidth();
                  this.restoreH = FXDialog.this.getHeight();
                  FXDialog.this.setX(var3);
                  FXDialog.this.setY(var5);
                  FXDialog.this.setWidth(var7);
                  FXDialog.this.setHeight(var9);
               }

            }
         });
         this.windowBtns = new HBox(3.0);
         this.windowBtns.getChildren().addAll(this.minButton, this.maxButton, var9);
         this.toolBar.getItems().addAll(this.titleLabel, var8, this.windowBtns);
         this.root.setTop(this.toolBar);
         this.resizeCorner = new Rectangle(10.0, 10.0);
         this.resizeCorner.setId("window-resize-corner");
         EventHandler var10 = new EventHandler() {
            private double width;
            private double height;
            private Point2D dragAnchor;

            public void handle(MouseEvent var1) {
               EventType var2 = var1.getEventType();
               if (var2 == MouseEvent.MOUSE_PRESSED) {
                  this.width = FXDialog.this.getWidth();
                  this.height = FXDialog.this.getHeight();
                  this.dragAnchor = new Point2D(var1.getSceneX(), var1.getSceneY());
               } else if (var2 == MouseEvent.MOUSE_DRAGGED) {
                  FXDialog.this.setWidth(Math.max(FXDialog.this.decoratedRoot.minWidth(-1.0), this.width + (var1.getSceneX() - this.dragAnchor.getX())));
                  FXDialog.this.setHeight(Math.max(FXDialog.this.decoratedRoot.minHeight(-1.0), this.height + (var1.getSceneY() - this.dragAnchor.getY())));
               }

            }
         };
         this.resizeCorner.setOnMousePressed(var10);
         this.resizeCorner.setOnMouseDragged(var10);
         this.resizeCorner.setManaged(false);
         this.decoratedRoot.getChildren().add(this.resizeCorner);
      }
   }

   void setContentPane(Pane var1) {
      if (var1.getId() == null) {
         var1.setId("content-pane");
      }

      this.root.setCenter(var1);
   }

   public void setIconifiable(boolean var1) {
      this.minButton.setVisible(var1);
   }

   public void hideWindowTitle() {
      if (this.toolBar != null) {
         this.root.setTop((Node)null);
         this.sizeToScene();
         this.addDragHandlers(this.root);
      }

   }

   private void addDragHandlers(Node var1) {
      var1.setOnMousePressed(new EventHandler() {
         public void handle(MouseEvent var1) {
            FXDialog.this.mouseDragOffsetX = var1.getSceneX();
            FXDialog.this.mouseDragOffsetY = var1.getSceneY();
         }
      });
      var1.setOnMouseDragged(new EventHandler() {
         public void handle(MouseEvent var1) {
            FXDialog.this.setX(var1.getScreenX() - FXDialog.this.mouseDragOffsetX);
            FXDialog.this.setY(var1.getScreenY() - FXDialog.this.mouseDragOffsetY);
         }
      });
   }

   public static Button createCloseButton() {
      return new WindowButton("black-close");
   }

   private static class WindowButton extends Button {
      WindowButton(String var1) {
         this.getStyleClass().setAll((Object[])("window-button"));
         this.setId("window-" + var1 + "-button");
         StackPane var2 = new StackPane();
         var2.getStyleClass().setAll((Object[])("graphic"));
         this.setGraphic(var2);
         this.setMinSize(17.0, 17.0);
         this.setPrefSize(17.0, 17.0);
      }
   }
}
