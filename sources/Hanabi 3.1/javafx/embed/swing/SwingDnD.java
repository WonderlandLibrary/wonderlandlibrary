package javafx.embed.swing;

import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.tk.Toolkit;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.scene.input.TransferMode;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

final class SwingDnD {
   private final Transferable dndTransferable = new DnDTransferable();
   private final DragSource dragSource;
   private final DragSourceListener dragSourceListener;
   private SwingDragSource swingDragSource;
   private EmbeddedSceneDTInterface fxDropTarget;
   private EmbeddedSceneDSInterface fxDragSource;
   private MouseEvent me;

   SwingDnD(final JComponent var1, final EmbeddedSceneInterface var2) {
      var1.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            SwingDnD.this.storeMouseEvent(var1);
         }

         public void mouseDragged(MouseEvent var1) {
            SwingDnD.this.storeMouseEvent(var1);
         }

         public void mousePressed(MouseEvent var1) {
            SwingDnD.this.storeMouseEvent(var1);
         }

         public void mouseReleased(MouseEvent var1) {
            SwingDnD.this.storeMouseEvent(var1);
         }
      });
      this.dragSource = new DragSource();
      this.dragSourceListener = new DragSourceAdapter() {
         public void dragDropEnd(DragSourceDropEvent var1) {
            assert SwingDnD.this.fxDragSource != null;

            try {
               SwingDnD.this.fxDragSource.dragDropEnd(SwingDnD.dropActionToTransferMode(var1.getDropAction()));
            } finally {
               SwingDnD.this.fxDragSource = null;
            }

         }
      };
      DropTargetAdapter var3 = new DropTargetAdapter() {
         private TransferMode lastTransferMode;

         public void dragEnter(DropTargetDragEvent var1x) {
            if (SwingDnD.this.swingDragSource == null && SwingDnD.this.fxDropTarget == null) {
               assert SwingDnD.this.swingDragSource == null;

               SwingDnD.this.swingDragSource = new SwingDragSource();
               SwingDnD.this.swingDragSource.updateContents(var1x, false);

               assert SwingDnD.this.fxDropTarget == null;

               SwingDnD.this.fxDropTarget = var2.createDropTarget();
               Point var2x = var1x.getLocation();
               Point var3 = new Point(var2x);
               SwingUtilities.convertPointToScreen(var3, var1);
               this.lastTransferMode = SwingDnD.this.fxDropTarget.handleDragEnter(var2x.x, var2x.y, var3.x, var3.y, SwingDnD.dropActionToTransferMode(var1x.getDropAction()), SwingDnD.this.swingDragSource);
               SwingDnD.this.applyDragResult(this.lastTransferMode, var1x);
            }
         }

         public void dragExit(DropTargetEvent var1x) {
            assert SwingDnD.this.swingDragSource != null;

            assert SwingDnD.this.fxDropTarget != null;

            try {
               SwingDnD.this.fxDropTarget.handleDragLeave();
            } finally {
               SwingDnD.this.endDnD();
               this.lastTransferMode = null;
            }

         }

         public void dragOver(DropTargetDragEvent var1x) {
            assert SwingDnD.this.swingDragSource != null;

            SwingDnD.this.swingDragSource.updateContents(var1x, false);

            assert SwingDnD.this.fxDropTarget != null;

            Point var2x = var1x.getLocation();
            Point var3 = new Point(var2x);
            SwingUtilities.convertPointToScreen(var3, var1);
            this.lastTransferMode = SwingDnD.this.fxDropTarget.handleDragOver(var2x.x, var2x.y, var3.x, var3.y, SwingDnD.dropActionToTransferMode(var1x.getDropAction()));
            SwingDnD.this.applyDragResult(this.lastTransferMode, var1x);
         }

         public void drop(DropTargetDropEvent var1x) {
            assert SwingDnD.this.swingDragSource != null;

            SwingDnD.this.applyDropResult(this.lastTransferMode, var1x);
            SwingDnD.this.swingDragSource.updateContents(var1x, true);
            Point var2x = var1x.getLocation();
            Point var3 = new Point(var2x);
            SwingUtilities.convertPointToScreen(var3, var1);

            assert SwingDnD.this.fxDropTarget != null;

            try {
               this.lastTransferMode = SwingDnD.this.fxDropTarget.handleDragDrop(var2x.x, var2x.y, var3.x, var3.y, SwingDnD.dropActionToTransferMode(var1x.getDropAction()));

               try {
                  SwingDnD.this.applyDropResult(this.lastTransferMode, var1x);
               } catch (InvalidDnDOperationException var8) {
               }
            } finally {
               var1x.dropComplete(this.lastTransferMode != null);
               SwingDnD.this.endDnD();
               this.lastTransferMode = null;
            }

         }
      };
      var1.setDropTarget(new DropTarget(var1, 1073741827, var3));
   }

   void addNotify() {
      this.dragSource.addDragSourceListener(this.dragSourceListener);
   }

   void removeNotify() {
      this.dragSource.removeDragSourceListener(this.dragSourceListener);
   }

   HostDragStartListener getDragStartListener() {
      return (var1, var2) -> {
         assert Toolkit.getToolkit().isFxUserThread();

         assert var1 != null;

         SwingUtilities.invokeLater(() -> {
            assert this.fxDragSource == null;

            assert this.swingDragSource == null;

            assert this.fxDropTarget == null;

            this.fxDragSource = var1;
            this.startDrag(this.me, this.dndTransferable, var1.getSupportedActions(), var2);
         });
      };
   }

   private void startDrag(final MouseEvent var1, Transferable var2, final Set var3, TransferMode var4) {
      assert var3.contains(var4);

      Point var5 = new Point(var1.getX(), var1.getY());
      int var6 = transferModeToDropAction(var4);

      final class 1StubDragGestureRecognizer extends DragGestureRecognizer {
         _StubDragGestureRecognizer/* $FF was: 1StubDragGestureRecognizer*/(DragSource var2) {
            super(var2, var1.getComponent());
            this.setSourceActions(SwingDnD.transferModesToDropActions(var3));
            this.appendEvent(var1);
         }

         protected void registerListeners() {
         }

         protected void unregisterListeners() {
         }
      }

      1StubDragGestureRecognizer var7 = new 1StubDragGestureRecognizer(this.dragSource);
      List var8 = Arrays.asList(var7.getTriggerEvent());
      DragGestureEvent var9 = new DragGestureEvent(var7, var6, var5, var8);
      var9.startDrag((Cursor)null, var2);
   }

   private void endDnD() {
      assert this.swingDragSource != null;

      assert this.fxDropTarget != null;

      this.fxDropTarget = null;
      this.swingDragSource = null;
   }

   private void storeMouseEvent(MouseEvent var1) {
      this.me = var1;
   }

   private void applyDragResult(TransferMode var1, DropTargetDragEvent var2) {
      if (var1 == null) {
         var2.rejectDrag();
      } else {
         var2.acceptDrag(transferModeToDropAction(var1));
      }

   }

   private void applyDropResult(TransferMode var1, DropTargetDropEvent var2) {
      if (var1 == null) {
         var2.rejectDrop();
      } else {
         var2.acceptDrop(transferModeToDropAction(var1));
      }

   }

   static TransferMode dropActionToTransferMode(int var0) {
      switch (var0) {
         case 0:
            return null;
         case 1:
            return TransferMode.COPY;
         case 2:
            return TransferMode.MOVE;
         case 1073741824:
            return TransferMode.LINK;
         default:
            throw new IllegalArgumentException();
      }
   }

   static int transferModeToDropAction(TransferMode var0) {
      switch (var0) {
         case COPY:
            return 1;
         case MOVE:
            return 2;
         case LINK:
            return 1073741824;
         default:
            throw new IllegalArgumentException();
      }
   }

   static Set dropActionsToTransferModes(int var0) {
      EnumSet var1 = EnumSet.noneOf(TransferMode.class);
      if ((var0 & 1) != 0) {
         var1.add(TransferMode.COPY);
      }

      if ((var0 & 2) != 0) {
         var1.add(TransferMode.MOVE);
      }

      if ((var0 & 1073741824) != 0) {
         var1.add(TransferMode.LINK);
      }

      return Collections.unmodifiableSet(var1);
   }

   static int transferModesToDropActions(Set var0) {
      int var1 = 0;

      TransferMode var3;
      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1 |= transferModeToDropAction(var3)) {
         var3 = (TransferMode)var2.next();
      }

      return var1;
   }

   private final class DnDTransferable implements Transferable {
      private DnDTransferable() {
      }

      public Object getTransferData(DataFlavor var1) throws UnsupportedEncodingException {
         assert SwingDnD.this.fxDragSource != null;

         assert SwingUtilities.isEventDispatchThread();

         String var2 = DataFlavorUtils.getFxMimeType(var1);
         return DataFlavorUtils.adjustFxData(var1, SwingDnD.this.fxDragSource.getData(var2));
      }

      public DataFlavor[] getTransferDataFlavors() {
         assert SwingDnD.this.fxDragSource != null;

         assert SwingUtilities.isEventDispatchThread();

         String[] var1 = SwingDnD.this.fxDragSource.getMimeTypes();
         ArrayList var2 = new ArrayList(var1.length);
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            DataFlavor var7 = null;

            try {
               var7 = new DataFlavor(var6);
            } catch (ClassNotFoundException var9) {
               continue;
            }

            var2.add(var7);
         }

         return (DataFlavor[])var2.toArray(new DataFlavor[0]);
      }

      public boolean isDataFlavorSupported(DataFlavor var1) {
         assert SwingDnD.this.fxDragSource != null;

         assert SwingUtilities.isEventDispatchThread();

         return SwingDnD.this.fxDragSource.isMimeTypeAvailable(DataFlavorUtils.getFxMimeType(var1));
      }

      // $FF: synthetic method
      DnDTransferable(Object var2) {
         this();
      }
   }
}
