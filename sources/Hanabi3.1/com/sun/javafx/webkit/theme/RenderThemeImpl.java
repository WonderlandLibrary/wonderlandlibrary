package com.sun.javafx.webkit.theme;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public final class RenderThemeImpl extends RenderTheme {
   private static final Logger log = Logger.getLogger(RenderThemeImpl.class.getName());
   private Accessor accessor;
   private boolean isDefault;
   private Pool pool;

   public RenderThemeImpl(Accessor var1) {
      this.accessor = var1;
      this.pool = new Pool((var1x) -> {
         var1.removeChild(var1x.asControl());
      }, FormControl.class);
      var1.addViewListener(new ViewListener(this.pool, var1));
   }

   public RenderThemeImpl() {
      this.isDefault = true;
   }

   private void ensureNotDefault() {
      if (this.isDefault) {
         throw new IllegalStateException("the method should not be called in this context");
      }
   }

   protected Ref createWidget(long var1, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8) {
      this.ensureNotDefault();
      Object var9 = (FormControl)this.pool.get(var1);
      WidgetType var10 = RenderThemeImpl.WidgetType.convert(var3);
      if (var9 == null || ((FormControl)var9).getType() != var10) {
         if (var9 != null) {
            this.accessor.removeChild(((FormControl)var9).asControl());
         }

         switch (var10) {
            case TEXTFIELD:
               var9 = new FormTextField();
               break;
            case BUTTON:
               var9 = new FormButton();
               break;
            case CHECKBOX:
               var9 = new FormCheckBox();
               break;
            case RADIOBUTTON:
               var9 = new FormRadioButton();
               break;
            case MENULIST:
               var9 = new FormMenuList();
               break;
            case MENULISTBUTTON:
               var9 = new FormMenuListButton();
               break;
            case SLIDER:
               var9 = new FormSlider();
               break;
            case PROGRESSBAR:
               var9 = new FormProgressBar(RenderThemeImpl.WidgetType.PROGRESSBAR);
               break;
            case METER:
               var9 = new FormProgressBar(RenderThemeImpl.WidgetType.METER);
               break;
            default:
               log.log(Level.ALL, "unknown widget index: {0}", var3);
               return null;
         }

         ((FormControl)var9).asControl().setFocusTraversable(false);
         this.pool.put(var1, (Widget)var9, this.accessor.getPage().getUpdateContentCycleID());
         this.accessor.addChild(((FormControl)var9).asControl());
      }

      ((FormControl)var9).setState(var4);
      Control var11 = ((FormControl)var9).asControl();
      if (var11.getWidth() != (double)var5 || var11.getHeight() != (double)var6) {
         var11.resize((double)var5, (double)var6);
      }

      if (var11.isManaged()) {
         var11.setManaged(false);
      }

      if (var8 != null) {
         if (var10 == RenderThemeImpl.WidgetType.SLIDER) {
            Slider var12 = (Slider)var11;
            var8.order(ByteOrder.nativeOrder());
            var12.setOrientation(var8.getInt() == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL);
            var12.setMax((double)var8.getFloat());
            var12.setMin((double)var8.getFloat());
            var12.setValue((double)var8.getFloat());
         } else {
            ProgressBar var13;
            if (var10 == RenderThemeImpl.WidgetType.PROGRESSBAR) {
               var13 = (ProgressBar)var11;
               var8.order(ByteOrder.nativeOrder());
               var13.setProgress(var8.getInt() == 1 ? (double)var8.getFloat() : -1.0);
            } else if (var10 == RenderThemeImpl.WidgetType.METER) {
               var13 = (ProgressBar)var11;
               var8.order(ByteOrder.nativeOrder());
               var13.setProgress((double)var8.getFloat());
               var13.setStyle(this.getMeterStyle(var8.getInt()));
            }
         }
      }

      return new FormControlRef((FormControl)var9);
   }

   private String getMeterStyle(int var1) {
      switch (var1) {
         case 1:
            return "-fx-accent: yellow";
         case 2:
            return "-fx-accent: red";
         default:
            return "-fx-accent: green";
      }
   }

   public void drawWidget(WCGraphicsContext var1, Ref var2, int var3, int var4) {
      this.ensureNotDefault();
      FormControl var5 = ((FormControlRef)var2).asFormControl();
      if (var5 != null) {
         Control var6 = var5.asControl();
         if (var6 != null) {
            var1.saveState();
            var1.translate((float)var3, (float)var4);
            Renderer.getRenderer().render(var6, var1);
            var1.restoreState();
         }
      }

   }

   public WCSize getWidgetSize(Ref var1) {
      this.ensureNotDefault();
      FormControl var2 = ((FormControlRef)var1).asFormControl();
      if (var2 != null) {
         Control var3 = var2.asControl();
         return new WCSize((float)var3.getWidth(), (float)var3.getHeight());
      } else {
         return new WCSize(0.0F, 0.0F);
      }
   }

   protected int getRadioButtonSize() {
      String var1 = Application.getUserAgentStylesheet();
      if ("MODENA".equalsIgnoreCase(var1)) {
         return 20;
      } else {
         return "CASPIAN".equalsIgnoreCase(var1) ? 19 : 20;
      }
   }

   protected int getSelectionColor(int var1) {
      switch (var1) {
         case 0:
            return -16739329;
         case 1:
            return -1;
         default:
            return 0;
      }
   }

   private static boolean hasState(int var0, int var1) {
      return (var0 & var1) != 0;
   }

   private static final class FormMenuListButton extends Button implements FormControl {
      private static final int MAX_WIDTH = 20;
      private static final int MIN_WIDTH = 16;

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setHover(RenderThemeImpl.hasState(var1, 32));
         this.setPressed(RenderThemeImpl.hasState(var1, 16));
         if (this.isPressed()) {
            this.arm();
         } else {
            this.disarm();
         }

      }

      private FormMenuListButton() {
         this.setSkin(new Skin());
         this.setFocusTraversable(false);
         this.getStyleClass().add("form-select-button");
      }

      public void resize(double var1, double var3) {
         var1 = var3 > 20.0 ? 20.0 : (var3 < 16.0 ? 16.0 : var3);
         super.resize(var1, var3);
         this.setTranslateX(-var1);
      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.MENULISTBUTTON;
      }

      // $FF: synthetic method
      FormMenuListButton(Object var1) {
         this();
      }

      private final class Skin extends BehaviorSkinBase {
         Skin() {
            super(FormMenuListButton.this, new BehaviorBase(FormMenuListButton.this, Collections.EMPTY_LIST));
            Region var2 = new Region();
            var2.getStyleClass().add("arrow");
            var2.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            BorderPane var3 = new BorderPane();
            var3.setCenter(var2);
            this.getChildren().add(var3);
         }
      }
   }

   private static final class FormMenuList extends ChoiceBox implements FormControl {
      private FormMenuList() {
         ArrayList var1 = new ArrayList();
         var1.add("");
         this.setItems(FXCollections.observableList(var1));
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.MENULIST;
      }

      // $FF: synthetic method
      FormMenuList(Object var1) {
         this();
      }
   }

   private static final class FormProgressBar extends ProgressBar implements FormControl {
      private final WidgetType type;

      private FormProgressBar(WidgetType var1) {
         this.type = var1;
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
      }

      public WidgetType getType() {
         return this.type;
      }

      // $FF: synthetic method
      FormProgressBar(WidgetType var1, Object var2) {
         this(var1);
      }
   }

   private static final class FormSlider extends Slider implements FormControl {
      private FormSlider() {
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.SLIDER;
      }

      // $FF: synthetic method
      FormSlider(Object var1) {
         this();
      }
   }

   private static final class FormRadioButton extends RadioButton implements FormControl {
      private FormRadioButton() {
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
         this.setSelected(RenderThemeImpl.hasState(var1, 1));
      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.RADIOBUTTON;
      }

      // $FF: synthetic method
      FormRadioButton(Object var1) {
         this();
      }
   }

   private static final class FormCheckBox extends CheckBox implements FormControl {
      private FormCheckBox() {
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
         this.setSelected(RenderThemeImpl.hasState(var1, 1));
      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.CHECKBOX;
      }

      // $FF: synthetic method
      FormCheckBox(Object var1) {
         this();
      }
   }

   private static final class FormTextField extends TextField implements FormControl {
      private FormTextField() {
         this.setStyle("-fx-display-caret: false");
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setEditable(RenderThemeImpl.hasState(var1, 64));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.TEXTFIELD;
      }

      // $FF: synthetic method
      FormTextField(Object var1) {
         this();
      }
   }

   private static final class FormButton extends Button implements FormControl {
      private FormButton() {
      }

      public Control asControl() {
         return this;
      }

      public void setState(int var1) {
         this.setDisabled(!RenderThemeImpl.hasState(var1, 4));
         this.setFocused(RenderThemeImpl.hasState(var1, 8));
         this.setHover(RenderThemeImpl.hasState(var1, 32) && !this.isDisabled());
         this.setPressed(RenderThemeImpl.hasState(var1, 16));
         if (this.isPressed()) {
            this.arm();
         } else {
            this.disarm();
         }

      }

      public WidgetType getType() {
         return RenderThemeImpl.WidgetType.BUTTON;
      }

      // $FF: synthetic method
      FormButton(Object var1) {
         this();
      }
   }

   private interface FormControl extends Widget {
      Control asControl();

      void setState(int var1);
   }

   interface Widget {
      WidgetType getType();
   }

   private static final class FormControlRef extends Ref {
      private final WeakReference fcRef;

      private FormControlRef(FormControl var1) {
         this.fcRef = new WeakReference(var1);
      }

      private FormControl asFormControl() {
         return (FormControl)this.fcRef.get();
      }

      // $FF: synthetic method
      FormControlRef(FormControl var1, Object var2) {
         this(var1);
      }
   }

   static class ViewListener implements InvalidationListener {
      private final Pool pool;
      private final Accessor accessor;
      private LoadListenerClient loadListener;

      ViewListener(Pool var1, Accessor var2) {
         this.pool = var1;
         this.accessor = var2;
      }

      public void invalidated(Observable var1) {
         this.pool.clear();
         if (this.accessor.getPage() != null && this.loadListener == null) {
            this.loadListener = new LoadListenerClient() {
               public void dispatchLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
                  if (var3 == 0) {
                     ViewListener.this.pool.clear();
                  }

               }

               public void dispatchResourceLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
               }
            };
            this.accessor.getPage().addLoadListenerClient(this.loadListener);
         }

      }
   }

   static final class Pool {
      private static final int INITIAL_CAPACITY = 100;
      private int capacity = 100;
      private final LinkedHashMap ids = new LinkedHashMap();
      private final Map pool = new HashMap();
      private final Notifier notifier;
      private final String type;

      Pool(Notifier var1, Class var2) {
         this.notifier = var1;
         this.type = var2.getSimpleName();
      }

      Widget get(long var1) {
         if (RenderThemeImpl.log.isLoggable(Level.FINE)) {
            RenderThemeImpl.log.log(Level.FINE, "type: {0}, size: {1}, id: 0x{2}", new Object[]{this.type, this.pool.size(), Long.toHexString(var1)});
         }

         assert this.ids.size() == this.pool.size();

         WeakReference var3 = (WeakReference)this.pool.get(var1);
         if (var3 == null) {
            return null;
         } else {
            Widget var4 = (Widget)var3.get();
            if (var4 == null) {
               return null;
            } else {
               Integer var5 = (Integer)this.ids.remove(var1);
               this.ids.put(var1, var5);
               return var4;
            }
         }
      }

      void put(long var1, Widget var3, int var4) {
         if (RenderThemeImpl.log.isLoggable(Level.FINEST)) {
            RenderThemeImpl.log.log(Level.FINEST, "size: {0}, id: 0x{1}, control: {2}", new Object[]{this.pool.size(), Long.toHexString(var1), var3.getType()});
         }

         if (this.ids.size() >= this.capacity) {
            Long var5 = (Long)this.ids.keySet().iterator().next();
            Integer var6 = (Integer)this.ids.get(var5);
            if (var6 != var4) {
               this.ids.remove(var5);
               Widget var7 = (Widget)((WeakReference)this.pool.remove(var5)).get();
               if (var7 != null) {
                  this.notifier.notifyRemoved(var7);
               }
            } else {
               this.capacity = Math.min(this.capacity, (int)Math.ceil(1.073741823E9)) * 2;
            }
         }

         this.ids.put(var1, var4);
         this.pool.put(var1, new WeakReference(var3));
      }

      void clear() {
         if (RenderThemeImpl.log.isLoggable(Level.FINE)) {
            RenderThemeImpl.log.fine("size: " + this.pool.size() + ", controls: " + this.pool.values());
         }

         if (this.pool.size() != 0) {
            this.ids.clear();
            Iterator var1 = this.pool.values().iterator();

            while(var1.hasNext()) {
               WeakReference var2 = (WeakReference)var1.next();
               Widget var3 = (Widget)var2.get();
               if (var3 != null) {
                  this.notifier.notifyRemoved(var3);
               }
            }

            this.pool.clear();
            this.capacity = 100;
         }
      }

      interface Notifier {
         void notifyRemoved(Object var1);
      }
   }

   static enum WidgetType {
      TEXTFIELD(0),
      BUTTON(1),
      CHECKBOX(2),
      RADIOBUTTON(3),
      MENULIST(4),
      MENULISTBUTTON(5),
      SLIDER(6),
      PROGRESSBAR(7),
      METER(8),
      SCROLLBAR(9);

      private static final HashMap map = new HashMap();
      private final int value;

      private WidgetType(int var3) {
         this.value = var3;
      }

      private static WidgetType convert(int var0) {
         return (WidgetType)map.get(var0);
      }

      static {
         WidgetType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            WidgetType var3 = var0[var2];
            map.put(var3.value, var3);
         }

      }
   }
}
