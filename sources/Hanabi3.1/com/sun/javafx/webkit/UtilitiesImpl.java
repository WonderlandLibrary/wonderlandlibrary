package com.sun.javafx.webkit;

import com.sun.javafx.webkit.theme.ContextMenuImpl;
import com.sun.javafx.webkit.theme.PopupMenuImpl;
import com.sun.webkit.ContextMenu;
import com.sun.webkit.Pasteboard;
import com.sun.webkit.PopupMenu;
import com.sun.webkit.Utilities;

public final class UtilitiesImpl extends Utilities {
   protected Pasteboard createPasteboard() {
      return new PasteboardImpl();
   }

   protected PopupMenu createPopupMenu() {
      return new PopupMenuImpl();
   }

   protected ContextMenu createContextMenu() {
      return new ContextMenuImpl();
   }
}
