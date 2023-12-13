package com.sun.javafx.tk;

import java.security.AccessControlContext;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.util.Pair;

public interface TKClipboard {
   void setSecurityContext(AccessControlContext var1);

   Set getContentTypes();

   boolean putContent(Pair... var1);

   Object getContent(DataFormat var1);

   boolean hasContent(DataFormat var1);

   Set getTransferModes();

   void setDragView(Image var1);

   void setDragViewOffsetX(double var1);

   void setDragViewOffsetY(double var1);

   Image getDragView();

   double getDragViewOffsetX();

   double getDragViewOffsetY();
}
