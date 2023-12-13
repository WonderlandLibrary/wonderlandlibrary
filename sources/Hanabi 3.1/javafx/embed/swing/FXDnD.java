package javafx.embed.swing;

import com.sun.javafx.tk.Toolkit;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.SecondaryLoop;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import sun.awt.AWTAccessor;
import sun.awt.dnd.SunDragSourceContextPeer;
import sun.swing.JLightweightFrame;

final class FXDnD {
   private final SwingNode node;
   private static boolean fxAppThreadIsDispatchThread;
   private boolean isDragSourceListenerInstalled = false;
   private MouseEvent pressEvent = null;
   private long pressTime = 0L;
   private volatile SecondaryLoop loop;
   private final Map recognizers = new HashMap();
   private final EventHandler onMousePressHandler = (var1x) -> {
      this.pressEvent = var1x;
      this.pressTime = System.currentTimeMillis();
   };
   private volatile FXDragSourceContextPeer activeDSContextPeer;
   private final EventHandler onDragStartHandler = (var1x) -> {
      this.activeDSContextPeer = null;
      MouseEvent var2 = this.getInitialGestureEvent();
      SwingFXUtils.runOnEDTAndWait(this, () -> {
         this.fireEvent((int)var2.getX(), (int)var2.getY(), this.pressTime, SwingEvents.fxMouseModsToMouseMods(var2));
      });
      if (this.activeDSContextPeer != null) {
         var1x.consume();
         Dragboard var3 = this.getNode().startDragAndDrop((TransferMode[])SwingDnD.dropActionsToTransferModes(this.activeDSContextPeer.sourceActions).toArray(new TransferMode[1]));
         HashMap var4 = new HashMap();
         String[] var5 = this.activeDSContextPeer.transferable.getMimeTypes();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            DataFormat var9 = DataFormat.lookupMimeType(var8);
            if (var9 != null) {
               var4.put(var9, this.activeDSContextPeer.transferable.getData(var8));
            }
         }

         boolean var10 = var3.setContent(var4);
         if (!var10 && !fxAppThreadIsDispatchThread) {
            this.loop.exit();
         }

      }
   };
   private final EventHandler onDragDoneHandler = (var1x) -> {
      var1x.consume();
      if (!fxAppThreadIsDispatchThread) {
         this.loop.exit();
      }

      if (this.activeDSContextPeer != null) {
         TransferMode var2 = var1x.getTransferMode();
         this.activeDSContextPeer.dragDone(var2 == null ? 0 : SwingDnD.transferModeToDropAction(var2), (int)var1x.getX(), (int)var1x.getY());
      }

   };
   private boolean isDropTargetListenerInstalled = false;
   private volatile FXDropTargetContextPeer activeDTContextPeer = null;
   private final Map dropTargets = new HashMap();
   private final EventHandler onDragEnteredHandler = (var1x) -> {
      if (this.activeDTContextPeer == null) {
         this.activeDTContextPeer = new FXDropTargetContextPeer();
      }

      int var2 = this.activeDTContextPeer.postDropTargetEvent(var1x);
      if (var2 != 0) {
         var1x.consume();
      }

   };
   private final EventHandler onDragExitedHandler = (var1x) -> {
      if (this.activeDTContextPeer == null) {
         this.activeDTContextPeer = new FXDropTargetContextPeer();
      }

      this.activeDTContextPeer.postDropTargetEvent(var1x);
      this.activeDTContextPeer = null;
   };
   private final EventHandler onDragOverHandler = (var1x) -> {
      if (this.activeDTContextPeer == null) {
         this.activeDTContextPeer = new FXDropTargetContextPeer();
      }

      int var2 = this.activeDTContextPeer.postDropTargetEvent(var1x);
      if (var2 != 0) {
         var1x.acceptTransferModes((TransferMode[])SwingDnD.dropActionsToTransferModes(var2).toArray(new TransferMode[1]));
         var1x.consume();
      }

   };
   private final EventHandler onDragDroppedHandler = (var1x) -> {
      if (this.activeDTContextPeer == null) {
         this.activeDTContextPeer = new FXDropTargetContextPeer();
      }

      int var2 = this.activeDTContextPeer.postDropTargetEvent(var1x);
      if (var2 != 0) {
         var1x.setDropCompleted(this.activeDTContextPeer.success);
         var1x.consume();
      }

      this.activeDTContextPeer = null;
   };

   private SwingNode getNode() {
      return this.node;
   }

   FXDnD(SwingNode var1) {
      this.node = var1;
   }

   public ComponentMapper mapComponent(Map var1, int var2, int var3) {
      return new ComponentMapper(var1, var2, var3);
   }

   private void fireEvent(int var1, int var2, long var3, int var5) {
      ComponentMapper var6 = this.mapComponent(this.recognizers, var1, var2);
      FXDragGestureRecognizer var7 = (FXDragGestureRecognizer)var6.object;
      if (var7 != null) {
         var7.fireEvent(var6.x, var6.y, var3, var5);
      } else {
         SwingFXUtils.leaveFXNestedLoop(this);
      }

   }

   private MouseEvent getInitialGestureEvent() {
      return this.pressEvent;
   }

   public DragGestureRecognizer createDragGestureRecognizer(Class var1, DragSource var2, Component var3, int var4, DragGestureListener var5) {
      return new FXDragGestureRecognizer(var2, var3, var4, var5);
   }

   public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent var1) throws InvalidDnDOperationException {
      return new FXDragSourceContextPeer(var1);
   }

   public void addDropTarget(DropTarget var1) {
      this.dropTargets.put(var1.getComponent(), var1);
      Platform.runLater(() -> {
         if (!this.isDropTargetListenerInstalled) {
            this.node.addEventHandler(DragEvent.DRAG_ENTERED, this.onDragEnteredHandler);
            this.node.addEventHandler(DragEvent.DRAG_EXITED, this.onDragExitedHandler);
            this.node.addEventHandler(DragEvent.DRAG_OVER, this.onDragOverHandler);
            this.node.addEventHandler(DragEvent.DRAG_DROPPED, this.onDragDroppedHandler);
            this.isDropTargetListenerInstalled = true;
         }

      });
   }

   public void removeDropTarget(DropTarget var1) {
      this.dropTargets.remove(var1.getComponent());
      Platform.runLater(() -> {
         if (this.isDropTargetListenerInstalled && this.dropTargets.isEmpty()) {
            this.node.removeEventHandler(DragEvent.DRAG_ENTERED, this.onDragEnteredHandler);
            this.node.removeEventHandler(DragEvent.DRAG_EXITED, this.onDragExitedHandler);
            this.node.removeEventHandler(DragEvent.DRAG_OVER, this.onDragOverHandler);
            this.node.removeEventHandler(DragEvent.DRAG_DROPPED, this.onDragDroppedHandler);
            this.isDropTargetListenerInstalled = true;
         }

      });
   }

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            FXDnD.fxAppThreadIsDispatchThread = "true".equals(System.getProperty("javafx.embed.singleThread"));
            return null;
         }
      });
   }

   private final class FXDropTargetContextPeer implements DropTargetContextPeer {
      private int targetActions;
      private int currentAction;
      private DropTarget dt;
      private DropTargetContext ctx;
      private final CachingTransferable transferable;
      private boolean success;
      private int dropAction;

      private FXDropTargetContextPeer() {
         this.targetActions = 0;
         this.currentAction = 0;
         this.dt = null;
         this.ctx = null;
         this.transferable = new CachingTransferable();
         this.success = false;
         this.dropAction = 0;
      }

      public synchronized void setTargetActions(int var1) {
         this.targetActions = var1;
      }

      public synchronized int getTargetActions() {
         return this.targetActions;
      }

      public synchronized DropTarget getDropTarget() {
         return this.dt;
      }

      public synchronized boolean isTransferableJVMLocal() {
         return false;
      }

      public synchronized DataFlavor[] getTransferDataFlavors() {
         return this.transferable.getTransferDataFlavors();
      }

      public synchronized Transferable getTransferable() {
         return this.transferable;
      }

      public synchronized void acceptDrag(int var1) {
         this.currentAction = var1;
      }

      public synchronized void rejectDrag() {
         this.currentAction = 0;
      }

      public synchronized void acceptDrop(int var1) {
         this.dropAction = var1;
      }

      public synchronized void rejectDrop() {
         this.dropAction = 0;
      }

      public synchronized void dropComplete(boolean var1) {
         this.success = var1;
      }

      private int postDropTargetEvent(DragEvent var1) {
         ComponentMapper var2 = FXDnD.this.mapComponent(FXDnD.this.dropTargets, (int)var1.getX(), (int)var1.getY());
         EventType var3 = var1.getEventType();
         Dragboard var4 = var1.getDragboard();
         this.transferable.updateData((Clipboard)var4, DragEvent.DRAG_DROPPED.equals(var3));
         int var5 = SwingDnD.transferModesToDropActions(var4.getTransferModes());
         int var6 = var1.getTransferMode() == null ? 0 : SwingDnD.transferModeToDropAction(var1.getTransferMode());
         DropTarget var7 = var2.object != null ? (DropTarget)var2.object : this.dt;
         SwingFXUtils.runOnEDTAndWait(FXDnD.this, () -> {
            if (var7 != this.dt) {
               if (this.ctx != null) {
                  this.ctx.removeNotify();
               }

               this.ctx = null;
               this.currentAction = this.dropAction = 0;
            }

            if (var7 != null) {
               if (this.ctx == null) {
                  this.ctx = var7.getDropTargetContext();
                  this.ctx.addNotify(this);
               }

               if (DragEvent.DRAG_DROPPED.equals(var3)) {
                  DropTargetDropEvent var7x = new DropTargetDropEvent(this.ctx, new Point(var2.x, var2.y), var6, var5);
                  var7.drop(var7x);
               } else {
                  DropTargetDragEvent var8 = new DropTargetDragEvent(this.ctx, new Point(var2.x, var2.y), var6, var5);
                  if (DragEvent.DRAG_OVER.equals(var3)) {
                     var7.dragOver(var8);
                  } else if (DragEvent.DRAG_ENTERED.equals(var3)) {
                     var7.dragEnter(var8);
                  } else if (DragEvent.DRAG_EXITED.equals(var3)) {
                     var7.dragExit(var8);
                  }
               }
            }

            this.dt = (DropTarget)var2.object;
            if (this.dt == null) {
               if (this.ctx != null) {
                  this.ctx.removeNotify();
               }

               this.ctx = null;
               this.currentAction = this.dropAction = 0;
            }

            if (DragEvent.DRAG_DROPPED.equals(var3) || DragEvent.DRAG_EXITED.equals(var3)) {
               if (this.ctx != null) {
                  this.ctx.removeNotify();
               }

               this.ctx = null;
            }

            SwingFXUtils.leaveFXNestedLoop(FXDnD.this);
         });
         return DragEvent.DRAG_DROPPED.equals(var3) ? this.dropAction : this.currentAction;
      }

      // $FF: synthetic method
      FXDropTargetContextPeer(Object var2) {
         this();
      }
   }

   private final class FXDragSourceContextPeer extends SunDragSourceContextPeer {
      private volatile int sourceActions = 0;
      private final CachingTransferable transferable = new CachingTransferable();

      public void startSecondaryEventLoop() {
         Toolkit.getToolkit().enterNestedEventLoop(this);
      }

      public void quitSecondaryEventLoop() {
         assert !Platform.isFxApplicationThread();

         Platform.runLater(() -> {
            Toolkit.getToolkit().exitNestedEventLoop(this, (Object)null);
         });
      }

      protected void setNativeCursor(long var1, Cursor var3, int var4) {
      }

      private void dragDone(int var1, int var2, int var3) {
         this.dragDropFinished(var1 != 0, var1, var2, var3);
      }

      FXDragSourceContextPeer(DragGestureEvent var2) {
         super(var2);
      }

      protected void startDrag(Transferable var1, long[] var2, Map var3) {
         FXDnD.this.activeDSContextPeer = this;
         this.transferable.updateData(var1, true);
         this.sourceActions = this.getDragSourceContext().getSourceActions();
         if (!FXDnD.fxAppThreadIsDispatchThread) {
            FXDnD.this.loop = java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
            SwingFXUtils.leaveFXNestedLoop(FXDnD.this);
            if (!FXDnD.this.loop.enter()) {
            }
         }

      }
   }

   private class FXDragGestureRecognizer extends MouseDragGestureRecognizer {
      FXDragGestureRecognizer(DragSource var2, Component var3, int var4, DragGestureListener var5) {
         super(var2, var3, var4, var5);
         if (var3 != null) {
            FXDnD.this.recognizers.put(var3, this);
         }

      }

      public void setComponent(Component var1) {
         Component var2 = this.getComponent();
         if (var2 != null) {
            FXDnD.this.recognizers.remove(var2);
         }

         super.setComponent(var1);
         if (var1 != null) {
            FXDnD.this.recognizers.put(var1, this);
         }

      }

      protected void registerListeners() {
         SwingFXUtils.runOnFxThread(() -> {
            if (!FXDnD.this.isDragSourceListenerInstalled) {
               FXDnD.this.node.addEventHandler(MouseEvent.MOUSE_PRESSED, FXDnD.this.onMousePressHandler);
               FXDnD.this.node.addEventHandler(MouseEvent.DRAG_DETECTED, FXDnD.this.onDragStartHandler);
               FXDnD.this.node.addEventHandler(DragEvent.DRAG_DONE, FXDnD.this.onDragDoneHandler);
               FXDnD.this.isDragSourceListenerInstalled = true;
            }

         });
      }

      protected void unregisterListeners() {
         SwingFXUtils.runOnFxThread(() -> {
            if (FXDnD.this.isDragSourceListenerInstalled) {
               FXDnD.this.node.removeEventHandler(MouseEvent.MOUSE_PRESSED, FXDnD.this.onMousePressHandler);
               FXDnD.this.node.removeEventHandler(MouseEvent.DRAG_DETECTED, FXDnD.this.onDragStartHandler);
               FXDnD.this.node.removeEventHandler(DragEvent.DRAG_DONE, FXDnD.this.onDragDoneHandler);
               FXDnD.this.isDragSourceListenerInstalled = false;
            }

         });
      }

      private void fireEvent(int var1, int var2, long var3, int var5) {
         this.appendEvent(new java.awt.event.MouseEvent(this.getComponent(), 501, var3, var5, var1, var2, 0, false));
         int var6 = SunDragSourceContextPeer.convertModifiersToDropAction(var5, this.getSourceActions());
         this.fireDragGestureRecognized(var6, new Point(var1, var2));
      }
   }

   private class ComponentMapper {
      private int x;
      private int y;
      private Object object;

      private ComponentMapper(Map var2, int var3, int var4) {
         this.object = null;
         this.x = var3;
         this.y = var4;
         JLightweightFrame var5 = FXDnD.this.node.getLightweightFrame();
         Object var6 = AWTAccessor.getContainerAccessor().findComponentAt(var5, this.x, this.y, false);
         if (var6 != null) {
            synchronized(((Component)var6).getTreeLock()) {
               do {
                  this.object = var2.get(var6);
               } while(this.object == null && (var6 = ((Component)var6).getParent()) != null);

               if (this.object != null) {
                  while(var6 != var5 && var6 != null) {
                     this.x -= ((Component)var6).getX();
                     this.y -= ((Component)var6).getY();
                     var6 = ((Component)var6).getParent();
                  }
               }

            }
         }
      }

      // $FF: synthetic method
      ComponentMapper(Map var2, int var3, int var4, Object var5) {
         this(var2, var3, var4);
      }
   }
}
