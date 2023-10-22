package javafx.application;

import com.sun.javafx.application.HostServicesDelegate;
import java.net.URI;
import netscape.javascript.JSObject;

public final class HostServices {
   private final HostServicesDelegate delegate;

   HostServices(Application var1) {
      this.delegate = HostServicesDelegate.getInstance(var1);
   }

   public final String getCodeBase() {
      return this.delegate.getCodeBase();
   }

   public final String getDocumentBase() {
      return this.delegate.getDocumentBase();
   }

   public final String resolveURI(String var1, String var2) {
      URI var3 = URI.create(var1).resolve(var2);
      return var3.toString();
   }

   public final void showDocument(String var1) {
      this.delegate.showDocument(var1);
   }

   public final JSObject getWebContext() {
      return this.delegate.getWebContext();
   }
}
