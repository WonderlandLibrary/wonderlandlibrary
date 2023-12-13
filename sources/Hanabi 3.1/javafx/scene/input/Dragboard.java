package javafx.scene.input;

import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import java.security.Permission;
import java.util.Set;
import javafx.scene.image.Image;

public final class Dragboard extends Clipboard {
   private boolean dataAccessRestricted = true;

   Dragboard(TKClipboard var1) {
      super(var1);
   }

   Object getContentImpl(DataFormat var1) {
      if (this.dataAccessRestricted) {
         SecurityManager var2 = System.getSecurityManager();
         if (var2 != null) {
            Permission var3 = PermissionHelper.getAccessClipboardPermission();
            var2.checkPermission(var3);
         }
      }

      return super.getContentImpl(var1);
   }

   public final Set getTransferModes() {
      return this.peer.getTransferModes();
   }

   /** @deprecated */
   @Deprecated
   public TKClipboard impl_getPeer() {
      return this.peer;
   }

   /** @deprecated */
   @Deprecated
   public static Dragboard impl_createDragboard(TKClipboard var0) {
      return new Dragboard(var0);
   }

   public void setDragView(Image var1, double var2, double var4) {
      this.peer.setDragView(var1);
      this.peer.setDragViewOffsetX(var2);
      this.peer.setDragViewOffsetY(var4);
   }

   public void setDragView(Image var1) {
      this.peer.setDragView(var1);
   }

   public void setDragViewOffsetX(double var1) {
      this.peer.setDragViewOffsetX(var1);
   }

   public void setDragViewOffsetY(double var1) {
      this.peer.setDragViewOffsetY(var1);
   }

   public Image getDragView() {
      return this.peer.getDragView();
   }

   public double getDragViewOffsetX() {
      return this.peer.getDragViewOffsetX();
   }

   public double getDragViewOffsetY() {
      return this.peer.getDragViewOffsetY();
   }

   static {
      DragboardHelper.setDragboardAccessor((var0, var1) -> {
         var0.dataAccessRestricted = var1;
      });
   }
}
