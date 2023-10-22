package com.sun.javafx.scene.control.skin;

import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;

public class TooltipSkin implements Skin {
   private Label tipLabel;
   private Tooltip tooltip;

   public TooltipSkin(Tooltip var1) {
      this.tooltip = var1;
      this.tipLabel = new Label();
      this.tipLabel.contentDisplayProperty().bind(var1.contentDisplayProperty());
      this.tipLabel.fontProperty().bind(var1.fontProperty());
      this.tipLabel.graphicProperty().bind(var1.graphicProperty());
      this.tipLabel.graphicTextGapProperty().bind(var1.graphicTextGapProperty());
      this.tipLabel.textAlignmentProperty().bind(var1.textAlignmentProperty());
      this.tipLabel.textOverrunProperty().bind(var1.textOverrunProperty());
      this.tipLabel.textProperty().bind(var1.textProperty());
      this.tipLabel.wrapTextProperty().bind(var1.wrapTextProperty());
      this.tipLabel.minWidthProperty().bind(var1.minWidthProperty());
      this.tipLabel.prefWidthProperty().bind(var1.prefWidthProperty());
      this.tipLabel.maxWidthProperty().bind(var1.maxWidthProperty());
      this.tipLabel.minHeightProperty().bind(var1.minHeightProperty());
      this.tipLabel.prefHeightProperty().bind(var1.prefHeightProperty());
      this.tipLabel.maxHeightProperty().bind(var1.maxHeightProperty());
      this.tipLabel.getStyleClass().setAll((Collection)var1.getStyleClass());
      this.tipLabel.setStyle(var1.getStyle());
      this.tipLabel.setId(var1.getId());
   }

   public Tooltip getSkinnable() {
      return this.tooltip;
   }

   public Node getNode() {
      return this.tipLabel;
   }

   public void dispose() {
      this.tooltip = null;
      this.tipLabel = null;
   }
}
