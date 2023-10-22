package com.sun.javafx.fxml;

import java.net.URL;

public class ParseTraceElement {
   private URL location;
   private int lineNumber;

   public ParseTraceElement(URL var1, int var2) {
      this.location = var1;
      this.lineNumber = var2;
   }

   public URL getLocation() {
      return this.location;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public String toString() {
      return (this.location == null ? "?" : this.location.getPath()) + ": " + this.lineNumber;
   }
}
