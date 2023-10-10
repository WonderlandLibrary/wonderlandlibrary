package org.lwjgl;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

abstract class J2SESysImplementation extends DefaultSysImplementation {
   public long getTime() {
      return System.currentTimeMillis();
   }

   public void alert(String var1, String var2) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var4) {
         LWJGLUtil.log("Caught exception while setting LAF: " + var4);
      }

      JOptionPane.showMessageDialog((Component)null, var2, var1, 2);
   }

   public String getClipboard() {
      try {
         Clipboard var1 = Toolkit.getDefaultToolkit().getSystemClipboard();
         Transferable var2 = var1.getContents((Object)null);
         if (var2.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String)var2.getTransferData(DataFlavor.stringFlavor);
         }
      } catch (Exception var3) {
         LWJGLUtil.log("Exception while getting clipboard: " + var3);
      }

      return null;
   }
}
