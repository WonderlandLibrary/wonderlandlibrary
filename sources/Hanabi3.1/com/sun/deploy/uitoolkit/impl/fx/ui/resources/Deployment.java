package com.sun.deploy.uitoolkit.impl.fx.ui.resources;

import java.util.ListResourceBundle;

public final class Deployment extends ListResourceBundle {
   static final Object[][] contents = new Object[][]{{"about.java.image", "image/aboutjava.png"}, {"about.java6.image", "image/aboutjava6.png"}, {"security.alert.high.image", "image/security_high.png"}, {"security.alert.low.image", "image/security_low.png"}, {"warning16.image", "image/icon-warning16.png"}, {"info16.image", "image/icon-info16.png"}, {"security.dialog.caption.run.question", "Do you want to run the application?"}, {"security.dialog.caption.continue.question", "Do you want to continue?"}, {"preloader.loading", "Loading..."}, {"error.pane.message", "Runtime error. Click for details."}, {"error.pane.icon", "image/graybox_error.png"}};

   public Object[][] getContents() {
      return contents;
   }
}
