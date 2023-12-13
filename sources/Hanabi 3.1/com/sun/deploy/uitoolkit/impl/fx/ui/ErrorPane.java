package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.applet2.Applet2Context;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.ToolkitStore;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.deploy.uitoolkit.ui.UIFactory;
import java.lang.reflect.Method;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ErrorPane extends Pane {
   Applet2Context a2c;
   Label label;

   public ErrorPane(Applet2Context var1, Throwable var2, final boolean var3) {
      this.a2c = var1;
      this.setStyle("-fx-background-color: white; -fx-padding: 4; -fx-border-color: lightgray;");
      ImageView var4 = ResourceManager.getIcon("error.pane.icon");
      this.label = new Label(ResourceManager.getMessage("error.pane.message"), var4);
      this.setOnMouseClicked(new EventHandler() {
         public void handle(MouseEvent var1) {
            boolean var2 = true;
            UIFactory var10000;
            AppInfo var10002;
            int var6;
            if (var3) {
               var10000 = ToolkitStore.getUI();
               var10002 = new AppInfo();
               ToolkitStore.getUI();
               var6 = var10000.showMessageDialog((Object)null, var10002, 0, (String)null, ResourceManager.getMessage("applet.error.generic.masthead"), ResourceManager.getMessage("applet.error.generic.body"), (String)null, "applet.error.details.btn", "applet.error.ignore.btn", "applet.error.reload.btn");
            } else {
               var10000 = ToolkitStore.getUI();
               var10002 = new AppInfo();
               ToolkitStore.getUI();
               var6 = var10000.showMessageDialog((Object)null, var10002, 0, (String)null, ResourceManager.getMessage("applet.error.generic.masthead"), ResourceManager.getMessage("applet.error.generic.body"), (String)null, "applet.error.details.btn", "applet.error.ignore.btn", (String)null);
            }

            ToolkitStore.getUI();
            if (var6 == 0) {
               try {
                  Class var3x = Class.forName("sun.plugin.JavaRunTime");
                  Method var4 = var3x.getDeclaredMethod("showJavaConsole", Boolean.TYPE);
                  var4.invoke((Object)null, true);
               } catch (Exception var5) {
                  Trace.ignoredException(var5);
               }
            } else {
               ToolkitStore.getUI();
               if (var6 != 1) {
                  ToolkitStore.getUI();
                  if (var6 == 3) {
                     ErrorPane.this.reloadApplet();
                  }
               }
            }

         }
      });
      this.getChildren().add(this.label);
   }

   public void layoutChildren() {
      super.layoutChildren();
      Insets var1 = this.getInsets();
      this.label.relocate(var1.getLeft(), var1.getTop());
   }

   private void reloadApplet() {
      if (this.a2c != null && this.a2c.getHost() != null) {
         this.a2c.getHost().reloadAppletPage();
      }

   }
}
