package javafx.scene.control;

import com.sun.javafx.scene.control.FormatterAccessor;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.util.StringConverter;

public class TextFormatter {
   private final StringConverter valueConverter;
   private final UnaryOperator filter;
   private Consumer textUpdater;
   public static final StringConverter IDENTITY_STRING_CONVERTER = new StringConverter() {
      public String toString(String var1) {
         return var1 == null ? "" : var1;
      }

      public String fromString(String var1) {
         return var1;
      }
   };
   private final ObjectProperty value;

   public TextFormatter(@NamedArg("filter") UnaryOperator var1) {
      this((StringConverter)null, (Object)null, var1);
   }

   public TextFormatter(@NamedArg("valueConverter") StringConverter var1, @NamedArg("defaultValue") Object var2, @NamedArg("filter") UnaryOperator var3) {
      this.value = new ObjectPropertyBase() {
         public Object getBean() {
            return TextFormatter.this;
         }

         public String getName() {
            return "value";
         }

         protected void invalidated() {
            if (TextFormatter.this.valueConverter == null && this.get() != null) {
               if (this.isBound()) {
                  this.unbind();
               }

               throw new IllegalStateException("Value changes are not supported when valueConverter is not set");
            } else {
               TextFormatter.this.updateText();
            }
         }
      };
      this.filter = var3;
      this.valueConverter = var1;
      this.setValue(var2);
   }

   public TextFormatter(@NamedArg("valueConverter") StringConverter var1, @NamedArg("defaultValue") Object var2) {
      this(var1, var2, (UnaryOperator)null);
   }

   public TextFormatter(@NamedArg("valueConverter") StringConverter var1) {
      this(var1, (Object)null, (UnaryOperator)null);
   }

   public final StringConverter getValueConverter() {
      return this.valueConverter;
   }

   public final UnaryOperator getFilter() {
      return this.filter;
   }

   public final ObjectProperty valueProperty() {
      return this.value;
   }

   public final void setValue(Object var1) {
      if (this.valueConverter == null && var1 != null) {
         throw new IllegalStateException("Value changes are not supported when valueConverter is not set");
      } else {
         this.value.set(var1);
      }
   }

   public final Object getValue() {
      return this.value.get();
   }

   private void updateText() {
      if (this.textUpdater != null) {
         this.textUpdater.accept(this);
      }

   }

   void bindToControl(Consumer var1) {
      if (this.textUpdater != null) {
         throw new IllegalStateException("Formatter is already used in other control");
      } else {
         this.textUpdater = var1;
      }
   }

   void unbindFromControl() {
      this.textUpdater = null;
   }

   void updateValue(String var1) {
      if (!this.value.isBound()) {
         try {
            Object var2 = this.valueConverter.fromString(var1);
            this.setValue(var2);
         } catch (Exception var3) {
            this.updateText();
         }
      }

   }

   public static final class Change implements Cloneable {
      private final FormatterAccessor accessor;
      private Control control;
      int start;
      int end;
      String text;
      int anchor;
      int caret;

      Change(Control var1, FormatterAccessor var2, int var3, int var4) {
         this(var1, var2, var4, var4, "", var3, var4);
      }

      Change(Control var1, FormatterAccessor var2, int var3, int var4, String var5) {
         this(var1, var2, var3, var4, var5, var3 + var5.length(), var3 + var5.length());
      }

      Change(Control var1, FormatterAccessor var2, int var3, int var4, String var5, int var6, int var7) {
         this.control = var1;
         this.accessor = var2;
         this.start = var3;
         this.end = var4;
         this.text = var5;
         this.anchor = var6;
         this.caret = var7;
      }

      public final Control getControl() {
         return this.control;
      }

      public final int getRangeStart() {
         return this.start;
      }

      public final int getRangeEnd() {
         return this.end;
      }

      public final void setRange(int var1, int var2) {
         int var3 = this.accessor.getTextLength();
         if (var1 >= 0 && var1 <= var3 && var2 >= 0 && var2 <= var3) {
            this.start = var1;
            this.end = var2;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public final int getCaretPosition() {
         return this.caret;
      }

      public final int getAnchor() {
         return this.anchor;
      }

      public final int getControlCaretPosition() {
         return this.accessor.getCaret();
      }

      public final int getControlAnchor() {
         return this.accessor.getAnchor();
      }

      public final void selectRange(int var1, int var2) {
         if (var1 >= 0 && var1 <= this.accessor.getTextLength() - (this.end - this.start) + this.text.length() && var2 >= 0 && var2 <= this.accessor.getTextLength() - (this.end - this.start) + this.text.length()) {
            this.anchor = var1;
            this.caret = var2;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public final IndexRange getSelection() {
         return IndexRange.normalize(this.anchor, this.caret);
      }

      public final void setAnchor(int var1) {
         if (var1 >= 0 && var1 <= this.accessor.getTextLength() - (this.end - this.start) + this.text.length()) {
            this.anchor = var1;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public final void setCaretPosition(int var1) {
         if (var1 >= 0 && var1 <= this.accessor.getTextLength() - (this.end - this.start) + this.text.length()) {
            this.caret = var1;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public final String getText() {
         return this.text;
      }

      public final void setText(String var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this.text = var1;
         }
      }

      public final String getControlText() {
         return this.accessor.getText(0, this.accessor.getTextLength());
      }

      public final String getControlNewText() {
         return this.accessor.getText(0, this.start) + this.text + this.accessor.getText(this.end, this.accessor.getTextLength());
      }

      public final boolean isAdded() {
         return !this.text.isEmpty();
      }

      public final boolean isDeleted() {
         return this.start != this.end;
      }

      public final boolean isReplaced() {
         return this.isAdded() && this.isDeleted();
      }

      public final boolean isContentChange() {
         return this.isAdded() || this.isDeleted();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("TextInputControl.Change [");
         if (this.isReplaced()) {
            var1.append(" replaced \"").append(this.accessor.getText(this.start, this.end)).append("\" with \"").append(this.text).append("\" at (").append(this.start).append(", ").append(this.end).append(")");
         } else if (this.isDeleted()) {
            var1.append(" deleted \"").append(this.accessor.getText(this.start, this.end)).append("\" at (").append(this.start).append(", ").append(this.end).append(")");
         } else if (this.isAdded()) {
            var1.append(" added \"").append(this.text).append("\" at ").append(this.start);
         }

         if (!this.isAdded() && !this.isDeleted()) {
            var1.append(" ");
         } else {
            var1.append("; ");
         }

         var1.append("new selection (anchor, caret): [").append(this.anchor).append(", ").append(this.caret).append("]");
         var1.append(" ]");
         return var1.toString();
      }

      public Change clone() {
         try {
            return (Change)super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
         }
      }
   }
}
