package org.lwjgl;

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
  
  public void alert(String title, String message) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      LWJGLUtil.log("Caught exception while setting LAF: " + e);
    } 
    JOptionPane.showMessageDialog(null, message, title, 2);
  }
  
  public String getClipboard() {
    try {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable transferable = clipboard.getContents(null);
      if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
        return (String)transferable.getTransferData(DataFlavor.stringFlavor); 
    } catch (Exception e) {
      LWJGLUtil.log("Exception while getting clipboard: " + e);
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\J2SESysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */