package com.sun.javafx.embed;

import java.util.Set;
import javafx.scene.input.TransferMode;

public interface EmbeddedSceneDSInterface {
   Set getSupportedActions();

   Object getData(String var1);

   String[] getMimeTypes();

   boolean isMimeTypeAvailable(String var1);

   void dragDropEnd(TransferMode var1);
}
