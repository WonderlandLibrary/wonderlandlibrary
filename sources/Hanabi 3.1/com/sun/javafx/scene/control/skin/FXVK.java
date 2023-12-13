package com.sun.javafx.scene.control.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class FXVK extends Control {
   private final ObjectProperty onAction = new SimpleObjectProperty(this, "onAction");
   static final String[] VK_TYPE_NAMES = new String[]{"text", "numeric", "url", "email"};
   public static final String VK_TYPE_PROP_KEY = "vkType";
   String[] chars;
   private ObjectProperty attachedNode;
   static FXVK vk;
   private static final String DEFAULT_STYLE_CLASS = "fxvk";

   public final void setOnAction(EventHandler var1) {
      this.onAction.set(var1);
   }

   public final EventHandler getOnAction() {
      return (EventHandler)this.onAction.get();
   }

   public final ObjectProperty onActionProperty() {
      return this.onAction;
   }

   public FXVK() {
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
      this.getStyleClass().add("fxvk");
   }

   final ObjectProperty attachedNodeProperty() {
      if (this.attachedNode == null) {
         this.attachedNode = new ObjectPropertyBase() {
            public Object getBean() {
               return FXVK.this;
            }

            public String getName() {
               return "attachedNode";
            }
         };
      }

      return this.attachedNode;
   }

   final void setAttachedNode(Node var1) {
      this.attachedNodeProperty().setValue(var1);
   }

   final Node getAttachedNode() {
      return this.attachedNode == null ? null : (Node)this.attachedNode.getValue();
   }

   public static void init(Node var0) {
      if (vk == null) {
         vk = new FXVK();
         FXVKSkin var1 = new FXVKSkin(vk);
         vk.setSkin(var1);
         var1.prerender(var0);
      }
   }

   public static void attach(Node var0) {
      if (vk == null) {
         vk = new FXVK();
         vk.setSkin(new FXVKSkin(vk));
      }

      vk.setAttachedNode(var0);
   }

   public static void detach() {
      if (vk != null) {
         vk.setAttachedNode((Node)null);
      }

   }

   protected Skin createDefaultSkin() {
      return new FXVKSkin(this);
   }

   public static enum Type {
      TEXT,
      NUMERIC,
      EMAIL;
   }
}
