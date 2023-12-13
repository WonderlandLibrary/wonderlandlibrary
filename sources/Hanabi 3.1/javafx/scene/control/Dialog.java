package javafx.scene.control;

import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Optional;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;

public class Dialog implements EventTarget {
   final FXDialog dialog = new HeavyweightDialog(this);
   private boolean isClosing;
   private ObjectProperty dialogPane = new SimpleObjectProperty(this, "dialogPane", new DialogPane()) {
      final InvalidationListener expandedListener = (var1x) -> {
         DialogPane var2 = Dialog.this.getDialogPane();
         if (var2 != null) {
            Node var3 = var2.getExpandableContent();
            boolean var4 = var3 == null ? false : var3.isVisible();
            Dialog.this.setResizable(var4);
            Dialog.this.dialog.sizeToScene();
         }
      };
      final InvalidationListener headerListener = (var1x) -> {
         Dialog.this.updatePseudoClassState();
      };
      WeakReference dialogPaneRef = new WeakReference((Object)null);

      protected void invalidated() {
         DialogPane var1 = (DialogPane)this.dialogPaneRef.get();
         if (var1 != null) {
            var1.expandedProperty().removeListener(this.expandedListener);
            var1.headerProperty().removeListener(this.headerListener);
            var1.headerTextProperty().removeListener(this.headerListener);
            var1.setDialog((Dialog)null);
         }

         DialogPane var2 = Dialog.this.getDialogPane();
         if (var2 != null) {
            var2.setDialog(Dialog.this);
            var2.getButtonTypes().addListener((var1x) -> {
               var2.requestLayout();
            });
            var2.expandedProperty().addListener(this.expandedListener);
            var2.headerProperty().addListener(this.headerListener);
            var2.headerTextProperty().addListener(this.headerListener);
            Dialog.this.updatePseudoClassState();
            var2.requestLayout();
         }

         Dialog.this.dialog.setDialogPane(var2);
         this.dialogPaneRef = new WeakReference(var2);
      }
   };
   private final ObjectProperty resultProperty = new SimpleObjectProperty() {
      protected void invalidated() {
         Dialog.this.close();
      }
   };
   private final ObjectProperty resultConverterProperty = new SimpleObjectProperty(this, "resultConverter");
   private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
   private ObjectProperty onShowing;
   private ObjectProperty onShown;
   private ObjectProperty onHiding;
   private ObjectProperty onHidden;
   private ObjectProperty onCloseRequest;
   private static final PseudoClass HEADER_PSEUDO_CLASS = PseudoClass.getPseudoClass("header");
   private static final PseudoClass NO_HEADER_PSEUDO_CLASS = PseudoClass.getPseudoClass("no-header");

   public Dialog() {
      this.setDialogPane(new DialogPane());
      this.initModality(Modality.APPLICATION_MODAL);
   }

   public final void show() {
      Toolkit.getToolkit().checkFxUserThread();
      Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWING));
      if (this.getWidth() == Double.NaN && this.getHeight() == Double.NaN) {
         this.dialog.sizeToScene();
      }

      this.dialog.show();
      Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWN));
   }

   public final Optional showAndWait() {
      Toolkit.getToolkit().checkFxUserThread();
      if (!Toolkit.getToolkit().canStartNestedEventLoop()) {
         throw new IllegalStateException("showAndWait is not allowed during animation or layout processing");
      } else {
         Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWING));
         if (this.getWidth() == Double.NaN && this.getHeight() == Double.NaN) {
            this.dialog.sizeToScene();
         }

         Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_SHOWN));
         this.dialog.showAndWait();
         return Optional.ofNullable(this.getResult());
      }
   }

   public final void close() {
      if (!this.isClosing) {
         this.isClosing = true;
         Object var1 = this.getResult();
         if (var1 == null && !this.dialog.requestPermissionToClose(this)) {
            this.isClosing = false;
         } else {
            if (var1 == null) {
               ButtonType var2 = null;
               Iterator var3 = this.getDialogPane().getButtonTypes().iterator();

               while(var3.hasNext()) {
                  ButtonType var4 = (ButtonType)var3.next();
                  ButtonBar.ButtonData var5 = var4.getButtonData();
                  if (var5 != null) {
                     if (var5 == ButtonBar.ButtonData.CANCEL_CLOSE) {
                        var2 = var4;
                        break;
                     }

                     if (var5.isCancelButton()) {
                        var2 = var4;
                     }
                  }
               }

               this.impl_setResultAndClose(var2, false);
            }

            Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_HIDING));
            DialogEvent var6 = new DialogEvent(this, DialogEvent.DIALOG_CLOSE_REQUEST);
            Event.fireEvent(this, var6);
            if (var6.isConsumed()) {
               this.isClosing = false;
            } else {
               this.dialog.close();
               Event.fireEvent(this, new DialogEvent(this, DialogEvent.DIALOG_HIDDEN));
               this.isClosing = false;
            }
         }
      }
   }

   public final void hide() {
      this.close();
   }

   public final void initModality(Modality var1) {
      this.dialog.initModality(var1);
   }

   public final Modality getModality() {
      return this.dialog.getModality();
   }

   public final void initStyle(StageStyle var1) {
      this.dialog.initStyle(var1);
   }

   public final void initOwner(Window var1) {
      this.dialog.initOwner(var1);
   }

   public final Window getOwner() {
      return this.dialog.getOwner();
   }

   public final ObjectProperty dialogPaneProperty() {
      return this.dialogPane;
   }

   public final DialogPane getDialogPane() {
      return (DialogPane)this.dialogPane.get();
   }

   public final void setDialogPane(DialogPane var1) {
      this.dialogPane.set(var1);
   }

   public final StringProperty contentTextProperty() {
      return this.getDialogPane().contentTextProperty();
   }

   public final String getContentText() {
      return this.getDialogPane().getContentText();
   }

   public final void setContentText(String var1) {
      this.getDialogPane().setContentText(var1);
   }

   public final StringProperty headerTextProperty() {
      return this.getDialogPane().headerTextProperty();
   }

   public final String getHeaderText() {
      return this.getDialogPane().getHeaderText();
   }

   public final void setHeaderText(String var1) {
      this.getDialogPane().setHeaderText(var1);
   }

   public final ObjectProperty graphicProperty() {
      return this.getDialogPane().graphicProperty();
   }

   public final Node getGraphic() {
      return this.getDialogPane().getGraphic();
   }

   public final void setGraphic(Node var1) {
      this.getDialogPane().setGraphic(var1);
   }

   public final ObjectProperty resultProperty() {
      return this.resultProperty;
   }

   public final Object getResult() {
      return this.resultProperty().get();
   }

   public final void setResult(Object var1) {
      this.resultProperty().set(var1);
   }

   public final ObjectProperty resultConverterProperty() {
      return this.resultConverterProperty;
   }

   public final Callback getResultConverter() {
      return (Callback)this.resultConverterProperty().get();
   }

   public final void setResultConverter(Callback var1) {
      this.resultConverterProperty().set(var1);
   }

   public final ReadOnlyBooleanProperty showingProperty() {
      return this.dialog.showingProperty();
   }

   public final boolean isShowing() {
      return this.showingProperty().get();
   }

   public final BooleanProperty resizableProperty() {
      return this.dialog.resizableProperty();
   }

   public final boolean isResizable() {
      return this.resizableProperty().get();
   }

   public final void setResizable(boolean var1) {
      this.resizableProperty().set(var1);
   }

   public final ReadOnlyDoubleProperty widthProperty() {
      return this.dialog.widthProperty();
   }

   public final double getWidth() {
      return this.widthProperty().get();
   }

   public final void setWidth(double var1) {
      this.dialog.setWidth(var1);
   }

   public final ReadOnlyDoubleProperty heightProperty() {
      return this.dialog.heightProperty();
   }

   public final double getHeight() {
      return this.heightProperty().get();
   }

   public final void setHeight(double var1) {
      this.dialog.setHeight(var1);
   }

   public final StringProperty titleProperty() {
      return this.dialog.titleProperty();
   }

   public final String getTitle() {
      return (String)this.dialog.titleProperty().get();
   }

   public final void setTitle(String var1) {
      this.dialog.titleProperty().set(var1);
   }

   public final double getX() {
      return this.dialog.getX();
   }

   public final void setX(double var1) {
      this.dialog.setX(var1);
   }

   public final ReadOnlyDoubleProperty xProperty() {
      return this.dialog.xProperty();
   }

   public final double getY() {
      return this.dialog.getY();
   }

   public final void setY(double var1) {
      this.dialog.setY(var1);
   }

   public final ReadOnlyDoubleProperty yProperty() {
      return this.dialog.yProperty();
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      return var1.prepend(this.eventHandlerManager);
   }

   public final void setOnShowing(EventHandler var1) {
      this.onShowingProperty().set(var1);
   }

   public final EventHandler getOnShowing() {
      return this.onShowing == null ? null : (EventHandler)this.onShowing.get();
   }

   public final ObjectProperty onShowingProperty() {
      if (this.onShowing == null) {
         this.onShowing = new SimpleObjectProperty(this, "onShowing") {
            protected void invalidated() {
               Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_SHOWING, (EventHandler)this.get());
            }
         };
      }

      return this.onShowing;
   }

   public final void setOnShown(EventHandler var1) {
      this.onShownProperty().set(var1);
   }

   public final EventHandler getOnShown() {
      return this.onShown == null ? null : (EventHandler)this.onShown.get();
   }

   public final ObjectProperty onShownProperty() {
      if (this.onShown == null) {
         this.onShown = new SimpleObjectProperty(this, "onShown") {
            protected void invalidated() {
               Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_SHOWN, (EventHandler)this.get());
            }
         };
      }

      return this.onShown;
   }

   public final void setOnHiding(EventHandler var1) {
      this.onHidingProperty().set(var1);
   }

   public final EventHandler getOnHiding() {
      return this.onHiding == null ? null : (EventHandler)this.onHiding.get();
   }

   public final ObjectProperty onHidingProperty() {
      if (this.onHiding == null) {
         this.onHiding = new SimpleObjectProperty(this, "onHiding") {
            protected void invalidated() {
               Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_HIDING, (EventHandler)this.get());
            }
         };
      }

      return this.onHiding;
   }

   public final void setOnHidden(EventHandler var1) {
      this.onHiddenProperty().set(var1);
   }

   public final EventHandler getOnHidden() {
      return this.onHidden == null ? null : (EventHandler)this.onHidden.get();
   }

   public final ObjectProperty onHiddenProperty() {
      if (this.onHidden == null) {
         this.onHidden = new SimpleObjectProperty(this, "onHidden") {
            protected void invalidated() {
               Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_HIDDEN, (EventHandler)this.get());
            }
         };
      }

      return this.onHidden;
   }

   public final void setOnCloseRequest(EventHandler var1) {
      this.onCloseRequestProperty().set(var1);
   }

   public final EventHandler getOnCloseRequest() {
      return this.onCloseRequest != null ? (EventHandler)this.onCloseRequest.get() : null;
   }

   public final ObjectProperty onCloseRequestProperty() {
      if (this.onCloseRequest == null) {
         this.onCloseRequest = new SimpleObjectProperty(this, "onCloseRequest") {
            protected void invalidated() {
               Dialog.this.eventHandlerManager.setEventHandler(DialogEvent.DIALOG_CLOSE_REQUEST, (EventHandler)this.get());
            }
         };
      }

      return this.onCloseRequest;
   }

   void impl_setResultAndClose(ButtonType var1, boolean var2) {
      Callback var3 = this.getResultConverter();
      Object var4 = this.getResult();
      Object var5 = null;
      if (var3 == null) {
         var5 = var1;
      } else {
         var5 = var3.call(var1);
      }

      this.setResult(var5);
      if (var2 && var4 == var5) {
         this.close();
      }

   }

   private void updatePseudoClassState() {
      DialogPane var1 = this.getDialogPane();
      if (var1 != null) {
         boolean var2 = this.getDialogPane().hasHeader();
         var1.pseudoClassStateChanged(HEADER_PSEUDO_CLASS, var2);
         var1.pseudoClassStateChanged(NO_HEADER_PSEUDO_CLASS, !var2);
      }

   }
}
