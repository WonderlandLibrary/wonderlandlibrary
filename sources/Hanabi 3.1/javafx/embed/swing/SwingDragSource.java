package javafx.embed.swing;

import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.tk.Toolkit;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Set;
import javafx.scene.input.TransferMode;

final class SwingDragSource extends CachingTransferable implements EmbeddedSceneDSInterface {
   private int sourceActions;

   void updateContents(DropTargetDragEvent var1, boolean var2) {
      this.sourceActions = var1.getSourceActions();
      this.updateData(var1.getTransferable(), var2);
   }

   void updateContents(DropTargetDropEvent var1, boolean var2) {
      this.sourceActions = var1.getSourceActions();
      this.updateData(var1.getTransferable(), var2);
   }

   public Set getSupportedActions() {
      assert Toolkit.getToolkit().isFxUserThread();

      return SwingDnD.dropActionsToTransferModes(this.sourceActions);
   }

   public void dragDropEnd(TransferMode var1) {
      throw new UnsupportedOperationException();
   }
}
