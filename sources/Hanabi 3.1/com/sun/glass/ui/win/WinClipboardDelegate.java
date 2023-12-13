package com.sun.glass.ui.win;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;

final class WinClipboardDelegate implements ClipboardDelegate {
   public Clipboard createClipboard(String var1) {
      if ("SYSTEM".equals(var1)) {
         return new WinSystemClipboard(var1);
      } else {
         return "DND".equals(var1) ? new WinDnDClipboard(var1) : null;
      }
   }
}
