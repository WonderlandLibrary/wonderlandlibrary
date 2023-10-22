package javafx.scene.control;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

@DefaultProperty("buttonTypes")
public class DialogPane extends Pane {
   private final GridPane headerTextPanel;
   private final Label contentLabel;
   private final StackPane graphicContainer;
   private final Node buttonBar;
   private final ObservableList buttons = FXCollections.observableArrayList();
   private final Map buttonNodes = new WeakHashMap();
   private Node detailsButton;
   private Dialog dialog;
   private final ObjectProperty graphicProperty = new StyleableObjectProperty() {
      WeakReference graphicRef = new WeakReference((Object)null);

      public CssMetaData getCssMetaData() {
         return DialogPane.StyleableProperties.GRAPHIC;
      }

      public Object getBean() {
         return DialogPane.this;
      }

      public String getName() {
         return "graphic";
      }

      protected void invalidated() {
         Node var1 = (Node)this.graphicRef.get();
         if (var1 != null) {
            DialogPane.this.getChildren().remove(var1);
         }

         Node var2 = DialogPane.this.getGraphic();
         this.graphicRef = new WeakReference(var2);
         DialogPane.this.updateHeaderArea();
      }
   };
   private StyleableStringProperty imageUrl = null;
   private final ObjectProperty header = new SimpleObjectProperty((Node)null) {
      WeakReference headerRef = new WeakReference((Object)null);

      protected void invalidated() {
         Node var1 = (Node)this.headerRef.get();
         if (var1 != null) {
            DialogPane.this.getChildren().remove(var1);
         }

         Node var2 = DialogPane.this.getHeader();
         this.headerRef = new WeakReference(var2);
         DialogPane.this.updateHeaderArea();
      }
   };
   private final StringProperty headerText = new SimpleStringProperty(this, "headerText") {
      protected void invalidated() {
         DialogPane.this.updateHeaderArea();
         DialogPane.this.requestLayout();
      }
   };
   private final ObjectProperty content = new SimpleObjectProperty((Node)null) {
      WeakReference contentRef = new WeakReference((Object)null);

      protected void invalidated() {
         Node var1 = (Node)this.contentRef.get();
         if (var1 != null) {
            DialogPane.this.getChildren().remove(var1);
         }

         Node var2 = DialogPane.this.getContent();
         this.contentRef = new WeakReference(var2);
         DialogPane.this.updateContentArea();
      }
   };
   private final StringProperty contentText = new SimpleStringProperty(this, "contentText") {
      protected void invalidated() {
         DialogPane.this.updateContentArea();
         DialogPane.this.requestLayout();
      }
   };
   private final ObjectProperty expandableContentProperty = new SimpleObjectProperty((Node)null) {
      WeakReference expandableContentRef = new WeakReference((Object)null);

      protected void invalidated() {
         Node var1 = (Node)this.expandableContentRef.get();
         if (var1 != null) {
            DialogPane.this.getChildren().remove(var1);
         }

         Node var2 = DialogPane.this.getExpandableContent();
         this.expandableContentRef = new WeakReference(var2);
         if (var2 != null) {
            var2.setVisible(DialogPane.this.isExpanded());
            var2.setManaged(DialogPane.this.isExpanded());
            if (!var2.getStyleClass().contains("expandable-content")) {
               var2.getStyleClass().add("expandable-content");
            }

            DialogPane.this.getChildren().add(var2);
         }

      }
   };
   private final BooleanProperty expandedProperty = new SimpleBooleanProperty(this, "expanded", false) {
      protected void invalidated() {
         Node var1 = DialogPane.this.getExpandableContent();
         if (var1 != null) {
            var1.setVisible(DialogPane.this.isExpanded());
         }

         DialogPane.this.requestLayout();
      }
   };
   private double oldHeight = -1.0;

   static Label createContentLabel(String var0) {
      Label var1 = new Label(var0);
      var1.setMaxWidth(Double.MAX_VALUE);
      var1.setMaxHeight(Double.MAX_VALUE);
      var1.getStyleClass().add("content");
      var1.setWrapText(true);
      var1.setPrefWidth(360.0);
      return var1;
   }

   public DialogPane() {
      this.getStyleClass().add("dialog-pane");
      this.headerTextPanel = new GridPane();
      this.getChildren().add(this.headerTextPanel);
      this.graphicContainer = new StackPane();
      this.contentLabel = createContentLabel("");
      this.getChildren().add(this.contentLabel);
      this.buttonBar = this.createButtonBar();
      if (this.buttonBar != null) {
         this.getChildren().add(this.buttonBar);
      }

      this.buttons.addListener((var1) -> {
         while(var1.next()) {
            Iterator var2;
            ButtonType var3;
            if (var1.wasRemoved()) {
               var2 = var1.getRemoved().iterator();

               while(var2.hasNext()) {
                  var3 = (ButtonType)var2.next();
                  this.buttonNodes.remove(var3);
               }
            }

            if (var1.wasAdded()) {
               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (ButtonType)var2.next();
                  if (!this.buttonNodes.containsKey(var3)) {
                     this.buttonNodes.put(var3, this.createButton(var3));
                  }
               }
            }
         }

      });
   }

   public final ObjectProperty graphicProperty() {
      return this.graphicProperty;
   }

   public final Node getGraphic() {
      return (Node)this.graphicProperty.get();
   }

   public final void setGraphic(Node var1) {
      this.graphicProperty.set(var1);
   }

   private StyleableStringProperty imageUrlProperty() {
      if (this.imageUrl == null) {
         this.imageUrl = new StyleableStringProperty() {
            StyleOrigin origin;

            {
               this.origin = StyleOrigin.USER;
            }

            public void applyStyle(StyleOrigin var1, String var2) {
               this.origin = var1;
               if (DialogPane.this.graphicProperty == null || !DialogPane.this.graphicProperty.isBound()) {
                  super.applyStyle(var1, var2);
               }

               this.origin = StyleOrigin.USER;
            }

            protected void invalidated() {
               String var1 = super.get();
               if (var1 == null) {
                  ((StyleableProperty)DialogPane.this.graphicProperty()).applyStyle(this.origin, (Object)null);
               } else {
                  Node var2 = DialogPane.this.getGraphic();
                  if (var2 instanceof ImageView) {
                     ImageView var3 = (ImageView)var2;
                     Image var4 = var3.getImage();
                     if (var4 != null) {
                        String var5 = var4.impl_getUrl();
                        if (var1.equals(var5)) {
                           return;
                        }
                     }
                  }

                  Image var6 = StyleManager.getInstance().getCachedImage(var1);
                  if (var6 != null) {
                     ((StyleableProperty)DialogPane.this.graphicProperty()).applyStyle(this.origin, new ImageView(var6));
                  }
               }

            }

            public String get() {
               Node var1 = DialogPane.this.getGraphic();
               if (var1 instanceof ImageView) {
                  Image var2 = ((ImageView)var1).getImage();
                  if (var2 != null) {
                     return var2.impl_getUrl();
                  }
               }

               return null;
            }

            public StyleOrigin getStyleOrigin() {
               return DialogPane.this.graphicProperty != null ? ((StyleableProperty)DialogPane.this.graphicProperty).getStyleOrigin() : null;
            }

            public Object getBean() {
               return DialogPane.this;
            }

            public String getName() {
               return "imageUrl";
            }

            public CssMetaData getCssMetaData() {
               return DialogPane.StyleableProperties.GRAPHIC;
            }
         };
      }

      return this.imageUrl;
   }

   public final Node getHeader() {
      return (Node)this.header.get();
   }

   public final void setHeader(Node var1) {
      this.header.setValue(var1);
   }

   public final ObjectProperty headerProperty() {
      return this.header;
   }

   public final void setHeaderText(String var1) {
      this.headerText.set(var1);
   }

   public final String getHeaderText() {
      return (String)this.headerText.get();
   }

   public final StringProperty headerTextProperty() {
      return this.headerText;
   }

   public final Node getContent() {
      return (Node)this.content.get();
   }

   public final void setContent(Node var1) {
      this.content.setValue(var1);
   }

   public final ObjectProperty contentProperty() {
      return this.content;
   }

   public final void setContentText(String var1) {
      this.contentText.set(var1);
   }

   public final String getContentText() {
      return (String)this.contentText.get();
   }

   public final StringProperty contentTextProperty() {
      return this.contentText;
   }

   public final ObjectProperty expandableContentProperty() {
      return this.expandableContentProperty;
   }

   public final Node getExpandableContent() {
      return (Node)this.expandableContentProperty.get();
   }

   public final void setExpandableContent(Node var1) {
      this.expandableContentProperty.set(var1);
   }

   public final BooleanProperty expandedProperty() {
      return this.expandedProperty;
   }

   public final boolean isExpanded() {
      return this.expandedProperty().get();
   }

   public final void setExpanded(boolean var1) {
      this.expandedProperty().set(var1);
   }

   public final ObservableList getButtonTypes() {
      return this.buttons;
   }

   public final Node lookupButton(ButtonType var1) {
      return (Node)this.buttonNodes.get(var1);
   }

   protected Node createButtonBar() {
      ButtonBar var1 = new ButtonBar();
      var1.setMaxWidth(Double.MAX_VALUE);
      this.updateButtons(var1);
      this.getButtonTypes().addListener((var2) -> {
         this.updateButtons(var1);
      });
      this.expandableContentProperty().addListener((var2) -> {
         this.updateButtons(var1);
      });
      return var1;
   }

   protected Node createButton(ButtonType var1) {
      Button var2 = new Button(var1.getText());
      ButtonBar.ButtonData var3 = var1.getButtonData();
      ButtonBar.setButtonData(var2, var3);
      var2.setDefaultButton(var1 != null && var3.isDefaultButton());
      var2.setCancelButton(var1 != null && var3.isCancelButton());
      var2.addEventHandler(ActionEvent.ACTION, (var2x) -> {
         if (!var2x.isConsumed()) {
            if (this.dialog != null) {
               this.dialog.impl_setResultAndClose(var1, true);
            }

         }
      });
      return var2;
   }

   protected Node createDetailsButton() {
      Hyperlink var1 = new Hyperlink();
      String var2 = ControlResources.getString("Dialog.detail.button.more");
      String var3 = ControlResources.getString("Dialog.detail.button.less");
      InvalidationListener var4 = (var4x) -> {
         boolean var5 = this.isExpanded();
         var1.setText(var5 ? var3 : var2);
         var1.getStyleClass().setAll((Object[])("details-button", var5 ? "less" : "more"));
      };
      var4.invalidated((Observable)null);
      this.expandedProperty().addListener(var4);
      var1.setOnAction((var1x) -> {
         this.setExpanded(!this.isExpanded());
      });
      return var1;
   }

   protected void layoutChildren() {
      boolean var1 = this.hasHeader();
      double var2 = Math.max(this.minWidth(-1.0), this.getWidth());
      double var4 = this.minHeight(var2);
      double var6 = this.prefHeight(var2);
      double var8 = this.maxHeight(var2);
      double var10 = this.getHeight();
      double var12 = this.dialog == null ? 0.0 : this.dialog.dialog.getSceneHeight();
      double var14;
      if (!(var6 > var10) || !(var6 > var4) || !(var6 <= var12) && var12 != 0.0) {
         boolean var16 = var10 > this.oldHeight;
         if (var16) {
            double var17 = var10 < var6 ? Math.min(var6, var10) : Math.max(var6, var12);
            var14 = Utils.boundedSize(var17, var4, var8);
         } else {
            var14 = Utils.boundedSize(Math.min(var10, var12), var4, var8);
         }

         this.resize(var2, var14);
      } else {
         var14 = var6;
         this.resize(var2, var6);
      }

      var14 -= this.snappedTopInset() + this.snappedBottomInset();
      this.oldHeight = var14;
      double var48 = this.snappedLeftInset();
      double var18 = this.snappedTopInset();
      double var20 = this.snappedRightInset();
      double var22 = this.snappedBottomInset();
      Node var24 = this.getActualHeader();
      Node var25 = this.getActualContent();
      Node var26 = this.getActualGraphic();
      Node var27 = this.getExpandableContent();
      double var28 = !var1 && var26 != null ? var26.prefWidth(-1.0) : 0.0;
      double var30 = var1 ? var24.prefHeight(var2) : 0.0;
      double var32 = this.buttonBar == null ? 0.0 : this.buttonBar.prefHeight(var2);
      double var34 = !var1 && var26 != null ? var26.prefHeight(-1.0) : 0.0;
      double var42 = var2 - var28 - var48 - var20;
      double var36;
      double var38;
      double var40;
      if (this.isExpanded()) {
         var38 = this.isExpanded() ? var25.prefHeight(var42) : 0.0;
         var40 = var1 ? var38 : Math.max(var34, var38);
         var36 = var14 - (var30 + var40 + var32);
      } else {
         var36 = this.isExpanded() ? var27.prefHeight(var2) : 0.0;
         var38 = var14 - (var30 + var36 + var32);
         var40 = var1 ? var38 : Math.max(var34, var38);
      }

      double var44 = var48;
      double var46 = var18;
      if (!var1) {
         if (var26 != null) {
            var26.resizeRelocate(var48, var18, var28, var34);
            var44 = var48 + var28;
         }
      } else {
         var24.resizeRelocate(var48, var18, var2 - (var48 + var20), var30);
         var46 = var18 + var30;
      }

      var25.resizeRelocate(var44, var46, var42, var38);
      var46 += var1 ? var38 : var40;
      if (var27 != null) {
         var27.resizeRelocate(var48, var46, var2 - var20, var36);
         var46 += var36;
      }

      if (this.buttonBar != null) {
         this.buttonBar.resizeRelocate(var48, var46, var2 - (var48 + var20), var32);
      }

   }

   protected double computeMinWidth(double var1) {
      double var3 = this.hasHeader() ? this.getActualHeader().minWidth(var1) + 10.0 : 0.0;
      double var5 = this.getActualContent().minWidth(var1);
      double var7 = this.buttonBar == null ? 0.0 : this.buttonBar.minWidth(var1);
      double var9 = this.getActualGraphic().minWidth(var1);
      double var11 = 0.0;
      Node var13 = this.getExpandableContent();
      if (this.isExpanded() && var13 != null) {
         var11 = var13.minWidth(var1);
      }

      double var14 = this.snappedLeftInset() + (this.hasHeader() ? 0.0 : var9) + Math.max(Math.max(var3, var11), Math.max(var5, var7)) + this.snappedRightInset();
      return this.snapSize(var14);
   }

   protected double computeMinHeight(double var1) {
      boolean var3 = this.hasHeader();
      double var4 = var3 ? this.getActualHeader().minHeight(var1) : 0.0;
      double var6 = this.buttonBar == null ? 0.0 : this.buttonBar.minHeight(var1);
      Node var8 = this.getActualGraphic();
      double var9 = var3 ? 0.0 : var8.minWidth(-1.0);
      double var11 = var3 ? 0.0 : var8.minHeight(var1);
      Node var13 = this.getActualContent();
      double var14 = var1 == -1.0 ? -1.0 : (var3 ? var1 : var1 - var9);
      double var16 = var13.minHeight(var14);
      double var18 = 0.0;
      Node var20 = this.getExpandableContent();
      if (this.isExpanded() && var20 != null) {
         var18 = var20.minHeight(var1);
      }

      double var21 = this.snappedTopInset() + var4 + Math.max(var11, var16) + var18 + var6 + this.snappedBottomInset();
      return this.snapSize(var21);
   }

   protected double computePrefWidth(double var1) {
      double var3 = this.hasHeader() ? this.getActualHeader().prefWidth(var1) + 10.0 : 0.0;
      double var5 = this.getActualContent().prefWidth(var1);
      double var7 = this.buttonBar == null ? 0.0 : this.buttonBar.prefWidth(var1);
      double var9 = this.getActualGraphic().prefWidth(var1);
      double var11 = 0.0;
      Node var13 = this.getExpandableContent();
      if (this.isExpanded() && var13 != null) {
         var11 = var13.prefWidth(var1);
      }

      double var14 = this.snappedLeftInset() + (this.hasHeader() ? 0.0 : var9) + Math.max(Math.max(var3, var11), Math.max(var5, var7)) + this.snappedRightInset();
      return this.snapSize(var14);
   }

   protected double computePrefHeight(double var1) {
      boolean var3 = this.hasHeader();
      double var4 = var3 ? this.getActualHeader().prefHeight(var1) : 0.0;
      double var6 = this.buttonBar == null ? 0.0 : this.buttonBar.prefHeight(var1);
      Node var8 = this.getActualGraphic();
      double var9 = var3 ? 0.0 : var8.prefWidth(-1.0);
      double var11 = var3 ? 0.0 : var8.prefHeight(var1);
      Node var13 = this.getActualContent();
      double var14 = var1 == -1.0 ? -1.0 : (var3 ? var1 : var1 - var9);
      double var16 = var13.prefHeight(var14);
      double var18 = 0.0;
      Node var20 = this.getExpandableContent();
      if (this.isExpanded() && var20 != null) {
         var18 = var20.prefHeight(var1);
      }

      double var21 = this.snappedTopInset() + var4 + Math.max(var11, var16) + var18 + var6 + this.snappedBottomInset();
      return this.snapSize(var21);
   }

   private void updateButtons(ButtonBar var1) {
      var1.getButtons().clear();
      if (this.hasExpandableContent()) {
         if (this.detailsButton == null) {
            this.detailsButton = this.createDetailsButton();
         }

         ButtonBar.setButtonData(this.detailsButton, ButtonBar.ButtonData.HELP_2);
         var1.getButtons().add(this.detailsButton);
         ButtonBar.setButtonUniformSize(this.detailsButton, false);
      }

      boolean var2 = false;

      Node var5;
      for(Iterator var3 = this.getButtonTypes().iterator(); var3.hasNext(); var1.getButtons().add(var5)) {
         ButtonType var4 = (ButtonType)var3.next();
         var5 = (Node)this.buttonNodes.computeIfAbsent(var4, (var2x) -> {
            return this.createButton(var4);
         });
         if (var5 instanceof Button) {
            ButtonBar.ButtonData var6 = var4.getButtonData();
            ((Button)var5).setDefaultButton(!var2 && var6 != null && var6.isDefaultButton());
            ((Button)var5).setCancelButton(var6 != null && var6.isCancelButton());
            var2 |= var6 != null && var6.isDefaultButton();
         }
      }

   }

   private Node getActualContent() {
      Node var1 = this.getContent();
      return (Node)(var1 == null ? this.contentLabel : var1);
   }

   private Node getActualHeader() {
      Node var1 = this.getHeader();
      return (Node)(var1 == null ? this.headerTextPanel : var1);
   }

   private Node getActualGraphic() {
      return this.headerTextPanel;
   }

   private void updateHeaderArea() {
      Node var1 = this.getHeader();
      if (var1 != null) {
         if (!this.getChildren().contains(var1)) {
            this.getChildren().add(var1);
         }

         this.headerTextPanel.setVisible(false);
         this.headerTextPanel.setManaged(false);
      } else {
         String var2 = this.getHeaderText();
         this.headerTextPanel.getChildren().clear();
         this.headerTextPanel.getStyleClass().clear();
         this.headerTextPanel.setMaxWidth(Double.MAX_VALUE);
         if (var2 != null && !var2.isEmpty()) {
            this.headerTextPanel.getStyleClass().add("header-panel");
         }

         Label var3 = new Label(var2);
         var3.setWrapText(true);
         var3.setAlignment(Pos.CENTER_LEFT);
         var3.setMaxWidth(Double.MAX_VALUE);
         var3.setMaxHeight(Double.MAX_VALUE);
         this.headerTextPanel.add(var3, 0, 0);
         this.graphicContainer.getChildren().clear();
         if (!this.graphicContainer.getStyleClass().contains("graphic-container")) {
            this.graphicContainer.getStyleClass().add("graphic-container");
         }

         Node var4 = this.getGraphic();
         if (var4 != null) {
            this.graphicContainer.getChildren().add(var4);
         }

         this.headerTextPanel.add(this.graphicContainer, 1, 0);
         ColumnConstraints var5 = new ColumnConstraints();
         var5.setFillWidth(true);
         var5.setHgrow(Priority.ALWAYS);
         ColumnConstraints var6 = new ColumnConstraints();
         var6.setFillWidth(false);
         var6.setHgrow(Priority.NEVER);
         this.headerTextPanel.getColumnConstraints().setAll((Object[])(var5, var6));
         this.headerTextPanel.setVisible(true);
         this.headerTextPanel.setManaged(true);
      }

   }

   private void updateContentArea() {
      Node var1 = this.getContent();
      if (var1 != null) {
         if (!this.getChildren().contains(var1)) {
            this.getChildren().add(var1);
         }

         if (!var1.getStyleClass().contains("content")) {
            var1.getStyleClass().add("content");
         }

         this.contentLabel.setVisible(false);
         this.contentLabel.setManaged(false);
      } else {
         String var2 = this.getContentText();
         boolean var3 = var2 != null && !var2.isEmpty();
         this.contentLabel.setText(var3 ? var2 : "");
         this.contentLabel.setVisible(var3);
         this.contentLabel.setManaged(var3);
      }

   }

   boolean hasHeader() {
      return this.getHeader() != null || this.isTextHeader();
   }

   private boolean isTextHeader() {
      String var1 = this.getHeaderText();
      return var1 != null && !var1.isEmpty();
   }

   boolean hasExpandableContent() {
      return this.getExpandableContent() != null;
   }

   void setDialog(Dialog var1) {
      this.dialog = var1;
   }

   public static List getClassCssMetaData() {
      return DialogPane.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData GRAPHIC = new CssMetaData("-fx-graphic", StringConverter.getInstance()) {
         public boolean isSettable(DialogPane var1) {
            return var1.graphicProperty == null || !var1.graphicProperty.isBound();
         }

         public StyleableProperty getStyleableProperty(DialogPane var1) {
            return var1.imageUrlProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         Collections.addAll(var0, new CssMetaData[]{GRAPHIC});
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
