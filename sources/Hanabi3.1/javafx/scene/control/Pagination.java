package javafx.scene.control;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.PaginationSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.util.Callback;

@DefaultProperty("pages")
public class Pagination extends Control {
   private static final int DEFAULT_MAX_PAGE_INDICATOR_COUNT = 10;
   public static final String STYLE_CLASS_BULLET = "bullet";
   public static final int INDETERMINATE = Integer.MAX_VALUE;
   private int oldMaxPageIndicatorCount;
   private IntegerProperty maxPageIndicatorCount;
   private int oldPageCount;
   private IntegerProperty pageCount;
   private final IntegerProperty currentPageIndex;
   private ObjectProperty pageFactory;
   private static final String DEFAULT_STYLE_CLASS = "pagination";

   public Pagination(int var1, int var2) {
      this.oldMaxPageIndicatorCount = 10;
      this.oldPageCount = Integer.MAX_VALUE;
      this.pageCount = new SimpleIntegerProperty(this, "pageCount", Integer.MAX_VALUE) {
         protected void invalidated() {
            if (!Pagination.this.pageCount.isBound()) {
               if (Pagination.this.getPageCount() < 1) {
                  Pagination.this.setPageCount(Pagination.this.oldPageCount);
               }

               Pagination.this.oldPageCount = Pagination.this.getPageCount();
            }

         }
      };
      this.currentPageIndex = new SimpleIntegerProperty(this, "currentPageIndex", 0) {
         protected void invalidated() {
            if (!Pagination.this.currentPageIndex.isBound()) {
               if (Pagination.this.getCurrentPageIndex() < 0) {
                  Pagination.this.setCurrentPageIndex(0);
               } else if (Pagination.this.getCurrentPageIndex() > Pagination.this.getPageCount() - 1) {
                  Pagination.this.setCurrentPageIndex(Pagination.this.getPageCount() - 1);
               }
            }

         }

         public void bind(ObservableValue var1) {
            throw new UnsupportedOperationException("currentPageIndex supports only bidirectional binding");
         }
      };
      this.pageFactory = new SimpleObjectProperty(this, "pageFactory");
      this.getStyleClass().setAll((Object[])("pagination"));
      this.setAccessibleRole(AccessibleRole.PAGINATION);
      this.setPageCount(var1);
      this.setCurrentPageIndex(var2);
   }

   public Pagination(int var1) {
      this(var1, 0);
   }

   public Pagination() {
      this(Integer.MAX_VALUE, 0);
   }

   public final void setMaxPageIndicatorCount(int var1) {
      this.maxPageIndicatorCountProperty().set(var1);
   }

   public final int getMaxPageIndicatorCount() {
      return this.maxPageIndicatorCount == null ? 10 : this.maxPageIndicatorCount.get();
   }

   public final IntegerProperty maxPageIndicatorCountProperty() {
      if (this.maxPageIndicatorCount == null) {
         this.maxPageIndicatorCount = new StyleableIntegerProperty(10) {
            protected void invalidated() {
               if (!Pagination.this.maxPageIndicatorCount.isBound()) {
                  if (Pagination.this.getMaxPageIndicatorCount() < 1 || Pagination.this.getMaxPageIndicatorCount() > Pagination.this.getPageCount()) {
                     Pagination.this.setMaxPageIndicatorCount(Pagination.this.oldMaxPageIndicatorCount);
                  }

                  Pagination.this.oldMaxPageIndicatorCount = Pagination.this.getMaxPageIndicatorCount();
               }

            }

            public CssMetaData getCssMetaData() {
               return Pagination.StyleableProperties.MAX_PAGE_INDICATOR_COUNT;
            }

            public Object getBean() {
               return Pagination.this;
            }

            public String getName() {
               return "maxPageIndicatorCount";
            }
         };
      }

      return this.maxPageIndicatorCount;
   }

   public final void setPageCount(int var1) {
      this.pageCount.set(var1);
   }

   public final int getPageCount() {
      return this.pageCount.get();
   }

   public final IntegerProperty pageCountProperty() {
      return this.pageCount;
   }

   public final void setCurrentPageIndex(int var1) {
      this.currentPageIndex.set(var1);
   }

   public final int getCurrentPageIndex() {
      return this.currentPageIndex.get();
   }

   public final IntegerProperty currentPageIndexProperty() {
      return this.currentPageIndex;
   }

   public final void setPageFactory(Callback var1) {
      this.pageFactory.set(var1);
   }

   public final Callback getPageFactory() {
      return (Callback)this.pageFactory.get();
   }

   public final ObjectProperty pageFactoryProperty() {
      return this.pageFactory;
   }

   protected Skin createDefaultSkin() {
      return new PaginationSkin(this);
   }

   public static List getClassCssMetaData() {
      return Pagination.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData MAX_PAGE_INDICATOR_COUNT = new CssMetaData("-fx-max-page-indicator-count", SizeConverter.getInstance(), 10) {
         public boolean isSettable(Pagination var1) {
            return var1.maxPageIndicatorCount == null || !var1.maxPageIndicatorCount.isBound();
         }

         public StyleableProperty getStyleableProperty(Pagination var1) {
            return (StyleableProperty)var1.maxPageIndicatorCountProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(MAX_PAGE_INDICATOR_COUNT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
