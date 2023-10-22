package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;

public class TextArea extends TextInputControl {
   public static final int DEFAULT_PREF_COLUMN_COUNT = 40;
   public static final int DEFAULT_PREF_ROW_COUNT = 10;
   /** @deprecated */
   public static final int DEFAULT_PARAGRAPH_CAPACITY = 32;
   private BooleanProperty wrapText;
   private IntegerProperty prefColumnCount;
   private IntegerProperty prefRowCount;
   private DoubleProperty scrollTop;
   private DoubleProperty scrollLeft;

   public TextArea() {
      this("");
   }

   public TextArea(String var1) {
      super(new TextAreaContent());
      this.wrapText = new StyleableBooleanProperty(false) {
         public Object getBean() {
            return TextArea.this;
         }

         public String getName() {
            return "wrapText";
         }

         public CssMetaData getCssMetaData() {
            return TextArea.StyleableProperties.WRAP_TEXT;
         }
      };
      this.prefColumnCount = new StyleableIntegerProperty(40) {
         private int oldValue = this.get();

         protected void invalidated() {
            int var1 = this.get();
            if (var1 < 0) {
               if (this.isBound()) {
                  this.unbind();
               }

               this.set(this.oldValue);
               throw new IllegalArgumentException("value cannot be negative.");
            } else {
               this.oldValue = var1;
            }
         }

         public CssMetaData getCssMetaData() {
            return TextArea.StyleableProperties.PREF_COLUMN_COUNT;
         }

         public Object getBean() {
            return TextArea.this;
         }

         public String getName() {
            return "prefColumnCount";
         }
      };
      this.prefRowCount = new StyleableIntegerProperty(10) {
         private int oldValue = this.get();

         protected void invalidated() {
            int var1 = this.get();
            if (var1 < 0) {
               if (this.isBound()) {
                  this.unbind();
               }

               this.set(this.oldValue);
               throw new IllegalArgumentException("value cannot be negative.");
            } else {
               this.oldValue = var1;
            }
         }

         public CssMetaData getCssMetaData() {
            return TextArea.StyleableProperties.PREF_ROW_COUNT;
         }

         public Object getBean() {
            return TextArea.this;
         }

         public String getName() {
            return "prefRowCount";
         }
      };
      this.scrollTop = new SimpleDoubleProperty(this, "scrollTop", 0.0);
      this.scrollLeft = new SimpleDoubleProperty(this, "scrollLeft", 0.0);
      this.getStyleClass().add("text-area");
      this.setAccessibleRole(AccessibleRole.TEXT_AREA);
      this.setText(var1);
   }

   final void textUpdated() {
      this.setScrollTop(0.0);
      this.setScrollLeft(0.0);
   }

   public ObservableList getParagraphs() {
      return ((TextAreaContent)this.getContent()).paragraphList;
   }

   public final BooleanProperty wrapTextProperty() {
      return this.wrapText;
   }

   public final boolean isWrapText() {
      return this.wrapText.getValue();
   }

   public final void setWrapText(boolean var1) {
      this.wrapText.setValue(var1);
   }

   public final IntegerProperty prefColumnCountProperty() {
      return this.prefColumnCount;
   }

   public final int getPrefColumnCount() {
      return this.prefColumnCount.getValue();
   }

   public final void setPrefColumnCount(int var1) {
      this.prefColumnCount.setValue((Number)var1);
   }

   public final IntegerProperty prefRowCountProperty() {
      return this.prefRowCount;
   }

   public final int getPrefRowCount() {
      return this.prefRowCount.getValue();
   }

   public final void setPrefRowCount(int var1) {
      this.prefRowCount.setValue((Number)var1);
   }

   public final DoubleProperty scrollTopProperty() {
      return this.scrollTop;
   }

   public final double getScrollTop() {
      return this.scrollTop.getValue();
   }

   public final void setScrollTop(double var1) {
      this.scrollTop.setValue((Number)var1);
   }

   public final DoubleProperty scrollLeftProperty() {
      return this.scrollLeft;
   }

   public final double getScrollLeft() {
      return this.scrollLeft.getValue();
   }

   public final void setScrollLeft(double var1) {
      this.scrollLeft.setValue((Number)var1);
   }

   protected Skin createDefaultSkin() {
      return new TextAreaSkin(this);
   }

   public static List getClassCssMetaData() {
      return TextArea.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData PREF_COLUMN_COUNT = new CssMetaData("-fx-pref-column-count", SizeConverter.getInstance(), 40) {
         public boolean isSettable(TextArea var1) {
            return !var1.prefColumnCount.isBound();
         }

         public StyleableProperty getStyleableProperty(TextArea var1) {
            return (StyleableProperty)var1.prefColumnCountProperty();
         }
      };
      private static final CssMetaData PREF_ROW_COUNT = new CssMetaData("-fx-pref-row-count", SizeConverter.getInstance(), 10) {
         public boolean isSettable(TextArea var1) {
            return !var1.prefRowCount.isBound();
         }

         public StyleableProperty getStyleableProperty(TextArea var1) {
            return (StyleableProperty)var1.prefRowCountProperty();
         }
      };
      private static final CssMetaData WRAP_TEXT = new CssMetaData("-fx-wrap-text", StyleConverter.getBooleanConverter(), false) {
         public boolean isSettable(TextArea var1) {
            return !var1.wrapText.isBound();
         }

         public StyleableProperty getStyleableProperty(TextArea var1) {
            return (StyleableProperty)var1.wrapTextProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(TextInputControl.getClassCssMetaData());
         var0.add(PREF_COLUMN_COUNT);
         var0.add(PREF_ROW_COUNT);
         var0.add(WRAP_TEXT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private static final class ParagraphListChange extends NonIterableChange {
      private List removed;

      protected ParagraphListChange(ObservableList var1, int var2, int var3, List var4) {
         super(var2, var3, var1);
         this.removed = var4;
      }

      public List getRemoved() {
         return this.removed;
      }

      protected int[] getPermutation() {
         return new int[0];
      }
   }

   private static final class ParagraphList extends AbstractList implements ObservableList {
      private TextAreaContent content;

      private ParagraphList() {
      }

      public CharSequence get(int var1) {
         return (CharSequence)this.content.paragraphs.get(var1);
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(CharSequence... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(CharSequence... var1) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return this.content.paragraphs.size();
      }

      public void addListener(ListChangeListener var1) {
         this.content.listenerHelper = ListListenerHelper.addListener(this.content.listenerHelper, var1);
      }

      public void removeListener(ListChangeListener var1) {
         this.content.listenerHelper = ListListenerHelper.removeListener(this.content.listenerHelper, var1);
      }

      public boolean removeAll(CharSequence... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(CharSequence... var1) {
         throw new UnsupportedOperationException();
      }

      public void remove(int var1, int var2) {
         throw new UnsupportedOperationException();
      }

      public void addListener(InvalidationListener var1) {
         this.content.listenerHelper = ListListenerHelper.addListener(this.content.listenerHelper, var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.content.listenerHelper = ListListenerHelper.removeListener(this.content.listenerHelper, var1);
      }

      // $FF: synthetic method
      ParagraphList(Object var1) {
         this();
      }
   }

   private static final class TextAreaContent implements TextInputControl.Content {
      private ExpressionHelper helper;
      private ArrayList paragraphs;
      private int contentLength;
      private ParagraphList paragraphList;
      private ListListenerHelper listenerHelper;

      private TextAreaContent() {
         this.helper = null;
         this.paragraphs = new ArrayList();
         this.contentLength = 0;
         this.paragraphList = new ParagraphList();
         this.paragraphs.add(new StringBuilder(32));
         this.paragraphList.content = this;
      }

      public String get(int var1, int var2) {
         int var3 = var2 - var1;
         StringBuilder var4 = new StringBuilder(var3);
         int var5 = this.paragraphs.size();
         int var6 = 0;

         int var7;
         StringBuilder var8;
         int var9;
         for(var7 = var1; var6 < var5; ++var6) {
            var8 = (StringBuilder)this.paragraphs.get(var6);
            var9 = var8.length() + 1;
            if (var7 < var9) {
               break;
            }

            var7 -= var9;
         }

         var8 = (StringBuilder)this.paragraphs.get(var6);

         for(var9 = 0; var9 < var3; ++var9) {
            if (var7 == var8.length() && var9 < this.contentLength) {
               var4.append('\n');
               ++var6;
               var8 = (StringBuilder)this.paragraphs.get(var6);
               var7 = 0;
            } else {
               var4.append(var8.charAt(var7++));
            }
         }

         return var4.toString();
      }

      public void insert(int var1, String var2, boolean var3) {
         if (var1 >= 0 && var1 <= this.contentLength) {
            if (var2 == null) {
               throw new IllegalArgumentException();
            } else {
               var2 = TextInputControl.filterInput(var2, false, false);
               int var4 = var2.length();
               if (var4 > 0) {
                  ArrayList var5 = new ArrayList();
                  StringBuilder var6 = new StringBuilder(32);

                  int var7;
                  int var8;
                  for(var7 = 0; var7 < var4; ++var7) {
                     var8 = var2.charAt(var7);
                     if (var8 == 10) {
                        var5.add(var6);
                        var6 = new StringBuilder(32);
                     } else {
                        var6.append((char)var8);
                     }
                  }

                  var5.add(var6);
                  var7 = this.paragraphs.size();
                  var8 = this.contentLength + 1;
                  StringBuilder var9 = null;

                  do {
                     --var7;
                     var9 = (StringBuilder)this.paragraphs.get(var7);
                     var8 -= var9.length() + 1;
                  } while(var1 < var8);

                  int var10 = var1 - var8;
                  int var11 = var5.size();
                  if (var11 == 1) {
                     var9.insert(var10, var6);
                     this.fireParagraphListChangeEvent(var7, var7 + 1, Collections.singletonList(var9));
                  } else {
                     int var12 = var9.length();
                     CharSequence var13 = var9.subSequence(var10, var12);
                     var9.delete(var10, var12);
                     StringBuilder var14 = (StringBuilder)var5.get(0);
                     var9.insert(var10, var14);
                     var6.append(var13);
                     this.fireParagraphListChangeEvent(var7, var7 + 1, Collections.singletonList(var9));
                     this.paragraphs.addAll(var7 + 1, var5.subList(1, var11));
                     this.fireParagraphListChangeEvent(var7 + 1, var7 + var11, Collections.EMPTY_LIST);
                  }

                  this.contentLength += var4;
                  if (var3) {
                     ExpressionHelper.fireValueChangedEvent(this.helper);
                  }
               }

            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public void delete(int var1, int var2, boolean var3) {
         if (var1 > var2) {
            throw new IllegalArgumentException();
         } else if (var1 >= 0 && var2 <= this.contentLength) {
            int var4 = var2 - var1;
            if (var4 > 0) {
               int var5 = this.paragraphs.size();
               int var6 = this.contentLength + 1;
               StringBuilder var7 = null;

               do {
                  --var5;
                  var7 = (StringBuilder)this.paragraphs.get(var5);
                  var6 -= var7.length() + 1;
               } while(var2 < var6);

               int var8 = var5;
               int var9 = var6;
               StringBuilder var10 = var7;
               ++var5;
               var6 += var7.length() + 1;

               do {
                  --var5;
                  var7 = (StringBuilder)this.paragraphs.get(var5);
                  var6 -= var7.length() + 1;
               } while(var1 < var6);

               if (var5 == var8) {
                  var7.delete(var1 - var6, var2 - var6);
                  this.fireParagraphListChangeEvent(var5, var5 + 1, Collections.singletonList(var7));
               } else {
                  CharSequence var14 = var7.subSequence(0, var1 - var6);
                  int var15 = var1 + var4 - var9;
                  var10.delete(0, var15);
                  this.fireParagraphListChangeEvent(var8, var8 + 1, Collections.singletonList(var10));
                  if (var8 - var5 > 0) {
                     ArrayList var16 = new ArrayList(this.paragraphs.subList(var5, var8));
                     this.paragraphs.subList(var5, var8).clear();
                     this.fireParagraphListChangeEvent(var5, var5, var16);
                  }

                  var10.insert(0, var14);
                  this.fireParagraphListChangeEvent(var5, var5 + 1, Collections.singletonList(var7));
               }

               this.contentLength -= var4;
               if (var3) {
                  ExpressionHelper.fireValueChangedEvent(this.helper);
               }
            }

         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public int length() {
         return this.contentLength;
      }

      public String get() {
         return this.get(0, this.length());
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public String getValue() {
         return this.get();
      }

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      private void fireParagraphListChangeEvent(int var1, int var2, List var3) {
         ParagraphListChange var4 = new ParagraphListChange(this.paragraphList, var1, var2, var3);
         ListListenerHelper.fireValueChangedEvent(this.listenerHelper, var4);
      }

      // $FF: synthetic method
      TextAreaContent(Object var1) {
         this();
      }
   }
}
