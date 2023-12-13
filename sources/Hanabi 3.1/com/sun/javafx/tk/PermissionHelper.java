package com.sun.javafx.tk;

import java.awt.AWTPermission;
import java.security.Permission;

public class PermissionHelper {
   private static final Permission accessClipboardPermission = new AWTPermission("accessClipboard");

   public static Permission getAccessClipboardPermission() {
      return accessClipboardPermission;
   }

   private PermissionHelper() {
   }
}
