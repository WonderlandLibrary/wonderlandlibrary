package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.scene.control.FormatterAccessor;
import com.sun.javafx.util.Utils;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

@DefaultProperty("text")
public abstract class TextInputControl extends Control {
   private ObjectProperty font;
   private StringProperty promptText = new SimpleStringProperty(this, "promptText", "") {
      protected void invalidated() {
         String var1 = this.get();
         if (var1 != null && var1.contains("\n")) {
            var1 = var1.replace("\n", "");
            this.set(var1);
         }

      }
   };
   private final ObjectProperty textFormatter = new ObjectPropertyBase() {
      private TextFormatter oldFormatter = null;

      public Object getBean() {
         return TextInputControl.this;
      }

      public String getName() {
         return "textFormatter";
      }

      protected void invalidated() {
         TextFormatter var1 = (TextFormatter)this.get();

         try {
            if (var1 != null) {
               try {
                  var1.bindToControl((var1x) -> {
                     TextInputControl.this.updateText(var1x);
                  });
               } catch (IllegalStateException var6) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set((Object)null);
                  throw var6;
               }

               if (!TextInputControl.this.isFocused()) {
                  TextInputControl.this.updateText((TextFormatter)this.get());
               }
            }

            if (this.oldFormatter != null) {
               this.oldFormatter.unbindFromControl();
            }
         } finally {
            this.oldFormatter = var1;
         }

      }
   };
   private final Content content;
   private TextProperty text = new TextProperty();
   private ReadOnlyIntegerWrapper length = new ReadOnlyIntegerWrapper(this, "length");
   private BooleanProperty editable = new SimpleBooleanProperty(this, "editable", true) {
      protected void invalidated() {
         TextInputControl.this.pseudoClassStateChanged(TextInputControl.PSEUDO_CLASS_READONLY, !this.get());
      }
   };
   private ReadOnlyObjectWrapper selection = new ReadOnlyObjectWrapper(this, "selection", new IndexRange(0, 0));
   private ReadOnlyStringWrapper selectedText = new ReadOnlyStringWrapper(this, "selectedText");
   private ReadOnlyIntegerWrapper anchor = new ReadOnlyIntegerWrapper(this, "anchor", 0);
   private ReadOnlyIntegerWrapper caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition", 0);
   private UndoRedoChange undoChangeHead = new UndoRedoChange();
   private UndoRedoChange undoChange;
   private boolean createNewUndoRecord;
   private final ReadOnlyBooleanWrapper undoable;
   private final ReadOnlyBooleanWrapper redoable;
   private BreakIterator charIterator;
   private BreakIterator wordIterator;
   private FormatterAccessor accessor;
   private static final PseudoClass PSEUDO_CLASS_READONLY = PseudoClass.getPseudoClass("readonly");

   protected TextInputControl(Content var1) {
      this.undoChange = this.undoChangeHead;
      this.createNewUndoRecord = false;
      this.undoable = new ReadOnlyBooleanWrapper(this, "undoable", false);
      this.redoable = new ReadOnlyBooleanWrapper(this, "redoable", false);
      this.content = var1;
      var1.addListener((var2) -> {
         if (var1.length() > 0) {
            this.text.textIsNull = false;
         }

         this.text.controlContentHasChanged();
      });
      this.length.bind(new IntegerBinding() {
         {
            this.bind(new Observable[]{TextInputControl.this.text});
         }

         protected int computeValue() {
            String var1 = TextInputControl.this.text.get();
            return var1 == null ? 0 : var1.length();
         }
      });
      this.selectedText.bind(new StringBinding() {
         {
            this.bind(new Observable[]{TextInputControl.this.selection, TextInputControl.this.text});
         }

         protected String computeValue() {
            String var1 = TextInputControl.this.text.get();
            IndexRange var2 = (IndexRange)TextInputControl.this.selection.get();
            if (var1 != null && var2 != null) {
               int var3 = var2.getStart();
               int var4 = var2.getEnd();
               int var5 = var1.length();
               if (var4 > var3 + var5) {
                  var4 = var5;
               }

               if (var3 > var5 - 1) {
                  var4 = 0;
                  var3 = 0;
               }

               return var1.substring(var3, var4);
            } else {
               return "";
            }
         }
      });
      this.focusedProperty().addListener((var1x, var2, var3) -> {
         if (var3) {
            if (this.getTextFormatter() != null) {
               this.updateText(this.getTextFormatter());
            }
         } else {
            this.commitValue();
         }

      });
      this.getStyleClass().add("text-input");
   }

   public final ObjectProperty fontProperty() {
      if (this.font == null) {
         this.font = new StyleableObjectProperty(Font.getDefault()) {
            private boolean fontSetByCss = false;

            public void applyStyle(StyleOrigin var1, Font var2) {
               try {
                  this.fontSetByCss = true;
                  super.applyStyle(var1, var2);
               } catch (Exception var7) {
                  throw var7;
               } finally {
                  this.fontSetByCss = false;
               }

            }

            public void set(Font var1) {
               Font var2 = (Font)this.get();
               if (var1 == null) {
                  if (var2 == null) {
                     return;
                  }
               } else if (var1.equals(var2)) {
                  return;
               }

               super.set(var1);
            }

            protected void invalidated() {
               if (!this.fontSetByCss) {
                  TextInputControl.this.impl_reapplyCSS();
               }

            }

            public CssMetaData getCssMetaData() {
               return TextInputControl.StyleableProperties.FONT;
            }

            public Object getBean() {
               return TextInputControl.this;
            }

            public String getName() {
               return "font";
            }
         };
      }

      return this.font;
   }

   public final void setFont(Font var1) {
      this.fontProperty().setValue(var1);
   }

   public final Font getFont() {
      return this.font == null ? Font.getDefault() : (Font)this.font.getValue();
   }

   public final StringProperty promptTextProperty() {
      return this.promptText;
   }

   public final String getPromptText() {
      return (String)this.promptText.get();
   }

   public final void setPromptText(String var1) {
      this.promptText.set(var1);
   }

   public final ObjectProperty textFormatterProperty() {
      return this.textFormatter;
   }

   public final TextFormatter getTextFormatter() {
      return (TextFormatter)this.textFormatter.get();
   }

   public final void setTextFormatter(TextFormatter var1) {
      this.textFormatter.set(var1);
   }

   protected final Content getContent() {
      return this.content;
   }

   public final String getText() {
      return this.text.get();
   }

   public final void setText(String var1) {
      this.text.set(var1);
   }

   public final StringProperty textProperty() {
      return this.text;
   }

   public final int getLength() {
      return this.length.get();
   }

   public final ReadOnlyIntegerProperty lengthProperty() {
      return this.length.getReadOnlyProperty();
   }

   public final boolean isEditable() {
      return this.editable.getValue();
   }

   public final void setEditable(boolean var1) {
      this.editable.setValue(var1);
   }

   public final BooleanProperty editableProperty() {
      return this.editable;
   }

   public final IndexRange getSelection() {
      return (IndexRange)this.selection.getValue();
   }

   public final ReadOnlyObjectProperty selectionProperty() {
      return this.selection.getReadOnlyProperty();
   }

   public final String getSelectedText() {
      return this.selectedText.get();
   }

   public final ReadOnlyStringProperty selectedTextProperty() {
      return this.selectedText.getReadOnlyProperty();
   }

   public final int getAnchor() {
      return this.anchor.get();
   }

   public final ReadOnlyIntegerProperty anchorProperty() {
      return this.anchor.getReadOnlyProperty();
   }

   public final int getCaretPosition() {
      return this.caretPosition.get();
   }

   public final ReadOnlyIntegerProperty caretPositionProperty() {
      return this.caretPosition.getReadOnlyProperty();
   }

   public final boolean isUndoable() {
      return this.undoable.get();
   }

   public final ReadOnlyBooleanProperty undoableProperty() {
      return this.undoable.getReadOnlyProperty();
   }

   public final boolean isRedoable() {
      return this.redoable.get();
   }

   public final ReadOnlyBooleanProperty redoableProperty() {
      return this.redoable.getReadOnlyProperty();
   }

   public String getText(int var1, int var2) {
      if (var1 > var2) {
         throw new IllegalArgumentException("The start must be <= the end");
      } else if (var1 >= 0 && var2 <= this.getLength()) {
         return this.getContent().get(var1, var2);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void appendText(String var1) {
      this.insertText(this.getLength(), var1);
   }

   public void insertText(int var1, String var2) {
      this.replaceText(var1, var1, var2);
   }

   public void deleteText(IndexRange var1) {
      this.replaceText(var1, "");
   }

   public void deleteText(int var1, int var2) {
      this.replaceText(var1, var2, "");
   }

   public void replaceText(IndexRange var1, String var2) {
      int var3 = var1.getStart();
      int var4 = var3 + var1.getLength();
      this.replaceText(var3, var4, var2);
   }

   public void replaceText(int var1, int var2, String var3) {
      if (var1 > var2) {
         throw new IllegalArgumentException();
      } else if (var3 == null) {
         throw new NullPointerException();
      } else if (var1 >= 0 && var2 <= this.getLength()) {
         if (!this.text.isBound()) {
            int var4 = this.getLength();
            TextFormatter var5 = this.getTextFormatter();
            TextFormatter.Change var6 = new TextFormatter.Change(this, this.getFormatterAccessor(), var1, var2, var3);
            if (var5 != null && var5.getFilter() != null) {
               var6 = (TextFormatter.Change)var5.getFilter().apply(var6);
               if (var6 == null) {
                  return;
               }
            }

            this.updateContent(var6, var4 == 0);
         }

      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private void updateContent(TextFormatter.Change var1, boolean var2) {
      boolean var3 = this.getSelection().getLength() > 0;
      String var4 = this.getText(var1.start, var1.end);
      int var5 = this.replaceText(var1.start, var1.end, var1.text, var1.getAnchor(), var1.getCaretPosition());
      int var6 = this.undoChange == this.undoChangeHead ? -1 : this.undoChange.start + this.undoChange.newText.length();
      String var7 = this.getText(var1.start, var1.start + var1.text.length() - var5);
      if (!this.createNewUndoRecord && !var3 && var6 != -1 && !var2 && (var6 == var1.start || var6 == var1.end) && var1.start - var1.end <= 1) {
         StringBuilder var10000;
         UndoRedoChange var10002;
         if (var1.start != var1.end && var1.text.isEmpty()) {
            if (this.undoChange.newText.length() > 0) {
               this.undoChange.newText = this.undoChange.newText.substring(0, var1.start - this.undoChange.start);
               if (this.undoChange.newText.isEmpty()) {
                  this.undoChange = this.undoChange.discard();
               }
            } else if (var1.start == var6) {
               var10000 = new StringBuilder();
               var10002 = this.undoChange;
               var10002.oldText = var10000.append(var10002.oldText).append(var4).toString();
            } else {
               this.undoChange.oldText = var4 + this.undoChange.oldText;
               --this.undoChange.start;
            }
         } else {
            var10000 = new StringBuilder();
            var10002 = this.undoChange;
            var10002.newText = var10000.append(var10002.newText).append(var7).toString();
         }
      } else {
         this.undoChange = this.undoChange.add(var1.start, var4, var7);
      }

      this.updateUndoRedoState();
   }

   public void cut() {
      this.copy();
      IndexRange var1 = this.getSelection();
      this.deleteText(var1.getStart(), var1.getEnd());
   }

   public void copy() {
      String var1 = this.getSelectedText();
      if (var1.length() > 0) {
         ClipboardContent var2 = new ClipboardContent();
         var2.putString(var1);
         Clipboard.getSystemClipboard().setContent(var2);
      }

   }

   public void paste() {
      Clipboard var1 = Clipboard.getSystemClipboard();
      if (var1.hasString()) {
         String var2 = var1.getString();
         if (var2 != null) {
            this.createNewUndoRecord = true;

            try {
               this.replaceSelection(var2);
            } finally {
               this.createNewUndoRecord = false;
            }
         }
      }

   }

   public void selectBackward() {
      if (this.getCaretPosition() > 0 && this.getLength() > 0) {
         if (this.charIterator == null) {
            this.charIterator = BreakIterator.getCharacterInstance();
         }

         this.charIterator.setText(this.getText());
         this.selectRange(this.getAnchor(), this.charIterator.preceding(this.getCaretPosition()));
      }

   }

   public void selectForward() {
      int var1 = this.getLength();
      if (var1 > 0 && this.getCaretPosition() < var1) {
         if (this.charIterator == null) {
            this.charIterator = BreakIterator.getCharacterInstance();
         }

         this.charIterator.setText(this.getText());
         this.selectRange(this.getAnchor(), this.charIterator.following(this.getCaretPosition()));
      }

   }

   public void previousWord() {
      this.previousWord(false);
   }

   public void nextWord() {
      this.nextWord(false);
   }

   public void endOfNextWord() {
      this.endOfNextWord(false);
   }

   public void selectPreviousWord() {
      this.previousWord(true);
   }

   public void selectNextWord() {
      this.nextWord(true);
   }

   public void selectEndOfNextWord() {
      this.endOfNextWord(true);
   }

   private void previousWord(boolean var1) {
      int var2 = this.getLength();
      String var3 = this.getText();
      if (var2 > 0) {
         if (this.wordIterator == null) {
            this.wordIterator = BreakIterator.getWordInstance();
         }

         this.wordIterator.setText(var3);

         int var4;
         for(var4 = this.wordIterator.preceding(Utils.clamp(0, this.getCaretPosition(), var2)); var4 != -1 && !Character.isLetterOrDigit(var3.charAt(Utils.clamp(0, var4, var2 - 1))); var4 = this.wordIterator.preceding(Utils.clamp(0, var4, var2))) {
         }

         this.selectRange(var1 ? this.getAnchor() : var4, var4);
      }
   }

   private void nextWord(boolean var1) {
      int var2 = this.getLength();
      String var3 = this.getText();
      if (var2 > 0) {
         if (this.wordIterator == null) {
            this.wordIterator = BreakIterator.getWordInstance();
         }

         this.wordIterator.setText(var3);
         int var4 = this.wordIterator.following(Utils.clamp(0, this.getCaretPosition(), var2 - 1));

         for(int var5 = this.wordIterator.next(); var5 != -1; var5 = this.wordIterator.next()) {
            for(int var6 = var4; var6 <= var5; ++var6) {
               char var7 = var3.charAt(Utils.clamp(0, var6, var2 - 1));
               if (var7 != ' ' && var7 != '\t') {
                  if (var1) {
                     this.selectRange(this.getAnchor(), var6);
                  } else {
                     this.selectRange(var6, var6);
                  }

                  return;
               }
            }

            var4 = var5;
         }

         if (var1) {
            this.selectRange(this.getAnchor(), var2);
         } else {
            this.end();
         }

      }
   }

   private void endOfNextWord(boolean var1) {
      int var2 = this.getLength();
      String var3 = this.getText();
      if (var2 > 0) {
         if (this.wordIterator == null) {
            this.wordIterator = BreakIterator.getWordInstance();
         }

         this.wordIterator.setText(var3);
         int var4 = this.wordIterator.following(Utils.clamp(0, this.getCaretPosition(), var2));

         for(int var5 = this.wordIterator.next(); var5 != -1; var5 = this.wordIterator.next()) {
            for(int var6 = var4; var6 <= var5; ++var6) {
               if (!Character.isLetterOrDigit(var3.charAt(Utils.clamp(0, var6, var2 - 1)))) {
                  if (var1) {
                     this.selectRange(this.getAnchor(), var6);
                  } else {
                     this.selectRange(var6, var6);
                  }

                  return;
               }
            }

            var4 = var5;
         }

         if (var1) {
            this.selectRange(this.getAnchor(), var2);
         } else {
            this.end();
         }

      }
   }

   public void selectAll() {
      this.selectRange(0, this.getLength());
   }

   public void home() {
      this.selectRange(0, 0);
   }

   public void end() {
      int var1 = this.getLength();
      if (var1 > 0) {
         this.selectRange(var1, var1);
      }

   }

   public void selectHome() {
      this.selectRange(this.getAnchor(), 0);
   }

   public void selectEnd() {
      int var1 = this.getLength();
      if (var1 > 0) {
         this.selectRange(this.getAnchor(), var1);
      }

   }

   public boolean deletePreviousChar() {
      boolean var1 = true;
      if (this.isEditable() && !this.isDisabled()) {
         String var2 = this.getText();
         int var3 = this.getCaretPosition();
         int var4 = this.getAnchor();
         if (var3 != var4) {
            this.replaceSelection("");
            var1 = false;
         } else if (var3 > 0) {
            int var5 = Character.offsetByCodePoints(var2, var3, -1);
            this.deleteText(var5, var3);
            var1 = false;
         }
      }

      return !var1;
   }

   public boolean deleteNextChar() {
      boolean var1 = true;
      if (this.isEditable() && !this.isDisabled()) {
         int var2 = this.getLength();
         String var3 = this.getText();
         int var4 = this.getCaretPosition();
         int var5 = this.getAnchor();
         if (var4 != var5) {
            this.replaceSelection("");
            var1 = false;
         } else if (var2 > 0 && var4 < var2) {
            if (this.charIterator == null) {
               this.charIterator = BreakIterator.getCharacterInstance();
            }

            this.charIterator.setText(var3);
            int var6 = this.charIterator.following(var4);
            this.deleteText(var4, var6);
            var1 = false;
         }
      }

      return !var1;
   }

   public void forward() {
      int var1 = this.getLength();
      int var2 = this.getCaretPosition();
      int var3 = this.getAnchor();
      int var4;
      if (var2 != var3) {
         var4 = Math.max(var2, var3);
         this.selectRange(var4, var4);
      } else if (var2 < var1 && var1 > 0) {
         if (this.charIterator == null) {
            this.charIterator = BreakIterator.getCharacterInstance();
         }

         this.charIterator.setText(this.getText());
         var4 = this.charIterator.following(var2);
         this.selectRange(var4, var4);
      }

      this.deselect();
   }

   public void backward() {
      int var1 = this.getLength();
      int var2 = this.getCaretPosition();
      int var3 = this.getAnchor();
      int var4;
      if (var2 != var3) {
         var4 = Math.min(var2, var3);
         this.selectRange(var4, var4);
      } else if (var2 > 0 && var1 > 0) {
         if (this.charIterator == null) {
            this.charIterator = BreakIterator.getCharacterInstance();
         }

         this.charIterator.setText(this.getText());
         var4 = this.charIterator.preceding(var2);
         this.selectRange(var4, var4);
      }

      this.deselect();
   }

   public void positionCaret(int var1) {
      int var2 = Utils.clamp(0, var1, this.getLength());
      this.selectRange(var2, var2);
   }

   public void selectPositionCaret(int var1) {
      this.selectRange(this.getAnchor(), Utils.clamp(0, var1, this.getLength()));
   }

   public void selectRange(int var1, int var2) {
      var2 = Utils.clamp(0, var2, this.getLength());
      var1 = Utils.clamp(0, var1, this.getLength());
      TextFormatter.Change var3 = new TextFormatter.Change(this, this.getFormatterAccessor(), var1, var2);
      TextFormatter var4 = this.getTextFormatter();
      if (var4 != null && var4.getFilter() != null) {
         var3 = (TextFormatter.Change)var4.getFilter().apply(var3);
         if (var3 == null) {
            return;
         }
      }

      this.updateContent(var3, false);
   }

   private void doSelectRange(int var1, int var2) {
      this.caretPosition.set(Utils.clamp(0, var2, this.getLength()));
      this.anchor.set(Utils.clamp(0, var1, this.getLength()));
      this.selection.set(IndexRange.normalize(this.getAnchor(), this.getCaretPosition()));
      this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_START);
   }

   public void extendSelection(int var1) {
      int var2 = Utils.clamp(0, var1, this.getLength());
      int var3 = this.getCaretPosition();
      int var4 = this.getAnchor();
      int var5 = Math.min(var3, var4);
      int var6 = Math.max(var3, var4);
      if (var2 < var5) {
         this.selectRange(var6, var2);
      } else {
         this.selectRange(var5, var2);
      }

   }

   public void clear() {
      this.deselect();
      if (!this.text.isBound()) {
         this.setText("");
      }

   }

   public void deselect() {
      this.selectRange(this.getCaretPosition(), this.getCaretPosition());
   }

   public void replaceSelection(String var1) {
      this.replaceText(this.getSelection(), var1);
   }

   public final void undo() {
      if (this.isUndoable()) {
         int var1 = this.undoChange.start;
         String var2 = this.undoChange.newText;
         String var3 = this.undoChange.oldText;
         if (var2 != null) {
            this.getContent().delete(var1, var1 + var2.length(), var3.isEmpty());
         }

         if (var3 != null) {
            this.getContent().insert(var1, var3, true);
            this.doSelectRange(var1, var1 + var3.length());
         } else {
            this.doSelectRange(var1, var1 + var2.length());
         }

         this.undoChange = this.undoChange.prev;
      }

      this.updateUndoRedoState();
   }

   public final void redo() {
      if (this.isRedoable()) {
         this.undoChange = this.undoChange.next;
         int var1 = this.undoChange.start;
         String var2 = this.undoChange.newText;
         String var3 = this.undoChange.oldText;
         if (var3 != null) {
            this.getContent().delete(var1, var1 + var3.length(), var2.isEmpty());
         }

         if (var2 != null) {
            this.getContent().insert(var1, var2, true);
            this.doSelectRange(var1 + var2.length(), var1 + var2.length());
         } else {
            this.doSelectRange(var1, var1);
         }
      }

      this.updateUndoRedoState();
   }

   void textUpdated() {
   }

   private void resetUndoRedoState() {
      this.undoChange = this.undoChangeHead;
      this.undoChange.next = null;
      this.updateUndoRedoState();
   }

   private void updateUndoRedoState() {
      this.undoable.set(this.undoChange != this.undoChangeHead);
      this.redoable.set(this.undoChange.next != null);
   }

   private boolean filterAndSet(String var1) {
      TextFormatter var2 = this.getTextFormatter();
      int var3 = this.content.length();
      if (var2 != null && var2.getFilter() != null && !this.text.isBound()) {
         TextFormatter.Change var4 = new TextFormatter.Change(this, this.getFormatterAccessor(), 0, var3, var1, 0, 0);
         var4 = (TextFormatter.Change)var2.getFilter().apply(var4);
         if (var4 == null) {
            return false;
         }

         this.replaceText(var4.start, var4.end, var4.text, var4.getAnchor(), var4.getCaretPosition());
      } else {
         this.replaceText(0, var3, var1, 0, 0);
      }

      return true;
   }

   private int replaceText(int var1, int var2, String var3, int var4, int var5) {
      int var6 = this.getLength();
      int var7 = 0;
      if (var2 != var1) {
         this.getContent().delete(var1, var2, var3.isEmpty());
         var6 -= var2 - var1;
      }

      if (var3 != null) {
         this.getContent().insert(var1, var3, true);
         var7 = var3.length() - (this.getLength() - var6);
         var4 -= var7;
         var5 -= var7;
      }

      this.doSelectRange(var4, var5);
      return var7;
   }

   private void updateText(TextFormatter var1) {
      Object var2 = var1.getValue();
      StringConverter var3 = var1.getValueConverter();
      if (var3 != null) {
         String var4 = var3.toString(var2);
         this.replaceText(0, this.getLength(), var4, var4.length(), var4.length());
      }

   }

   public final void commitValue() {
      if (this.getTextFormatter() != null) {
         this.getTextFormatter().updateValue(this.getText());
      }

   }

   public final void cancelEdit() {
      if (this.getTextFormatter() != null) {
         this.updateText(this.getTextFormatter());
      }

   }

   private FormatterAccessor getFormatterAccessor() {
      if (this.accessor == null) {
         this.accessor = new TextInputControlFromatterAccessor();
      }

      return this.accessor;
   }

   static String filterInput(String var0, boolean var1, boolean var2) {
      if (containsInvalidCharacters(var0, var1, var2)) {
         StringBuilder var3 = new StringBuilder(var0.length());

         for(int var4 = 0; var4 < var0.length(); ++var4) {
            char var5 = var0.charAt(var4);
            if (!isInvalidCharacter(var5, var1, var2)) {
               var3.append(var5);
            }
         }

         var0 = var3.toString();
      }

      return var0;
   }

   static boolean containsInvalidCharacters(String var0, boolean var1, boolean var2) {
      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (isInvalidCharacter(var4, var1, var2)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isInvalidCharacter(char var0, boolean var1, boolean var2) {
      if (var0 == 127) {
         return true;
      } else if (var0 == '\n') {
         return var1;
      } else if (var0 == '\t') {
         return var2;
      } else {
         return var0 < ' ';
      }
   }

   public static List getClassCssMetaData() {
      return TextInputControl.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            String var3 = this.getAccessibleText();
            if (var3 != null && !var3.isEmpty()) {
               return var3;
            }

            String var4 = this.getText();
            if (var4 == null || var4.isEmpty()) {
               var4 = this.getPromptText();
            }

            return var4;
         case EDITABLE:
            return this.isEditable();
         case SELECTION_START:
            return this.getSelection().getStart();
         case SELECTION_END:
            return this.getSelection().getEnd();
         case CARET_OFFSET:
            return this.getCaretPosition();
         case FONT:
            return this.getFont();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case SET_TEXT:
            String var3 = (String)var2[0];
            if (var3 != null) {
               this.setText(var3);
            }
         case SET_TEXT_SELECTION:
            Integer var5 = (Integer)var2[0];
            Integer var4 = (Integer)var2[1];
            if (var5 != null && var4 != null) {
               this.selectRange(var5, var4);
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }

   private class TextInputControlFromatterAccessor implements FormatterAccessor {
      private TextInputControlFromatterAccessor() {
      }

      public int getTextLength() {
         return TextInputControl.this.getLength();
      }

      public String getText(int var1, int var2) {
         return TextInputControl.this.getText(var1, var2);
      }

      public int getCaret() {
         return TextInputControl.this.getCaretPosition();
      }

      public int getAnchor() {
         return TextInputControl.this.getAnchor();
      }

      // $FF: synthetic method
      TextInputControlFromatterAccessor(Object var2) {
         this();
      }
   }

   private static class StyleableProperties {
      private static final FontCssMetaData FONT = new FontCssMetaData("-fx-font", Font.getDefault()) {
         public boolean isSettable(TextInputControl var1) {
            return var1.font == null || !var1.font.isBound();
         }

         public StyleableProperty getStyleableProperty(TextInputControl var1) {
            return (StyleableProperty)var1.fontProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(FONT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   static class UndoRedoChange {
      int start;
      String oldText;
      String newText;
      UndoRedoChange prev;
      UndoRedoChange next;

      public UndoRedoChange add(int var1, String var2, String var3) {
         UndoRedoChange var4 = new UndoRedoChange();
         var4.start = var1;
         var4.oldText = var2;
         var4.newText = var3;
         var4.prev = this;
         this.next = var4;
         return var4;
      }

      public UndoRedoChange discard() {
         this.prev.next = this.next;
         return this.prev;
      }

      void debugPrint() {
         UndoRedoChange var1 = this;
         System.out.print("[");

         for(; var1 != null; var1 = var1.next) {
            System.out.print(var1.toString());
            if (var1.next != null) {
               System.out.print(", ");
            }
         }

         System.out.println("]");
      }

      public String toString() {
         if (this.oldText == null && this.newText == null) {
            return "head";
         } else if (this.oldText.isEmpty() && !this.newText.isEmpty()) {
            return "added '" + this.newText + "' at index " + this.start;
         } else {
            return !this.oldText.isEmpty() && !this.newText.isEmpty() ? "replaced '" + this.oldText + "' with '" + this.newText + "' at index " + this.start : "deleted '" + this.oldText + "' at index " + this.start;
         }
      }
   }

   private class TextProperty extends StringProperty {
      private ObservableValue observable;
      private InvalidationListener listener;
      private ExpressionHelper helper;
      private boolean textIsNull;

      private TextProperty() {
         this.observable = null;
         this.listener = null;
         this.helper = null;
         this.textIsNull = false;
      }

      public String get() {
         return this.textIsNull ? null : (String)TextInputControl.this.content.get();
      }

      public void set(String var1) {
         if (this.isBound()) {
            throw new RuntimeException("A bound value cannot be set.");
         } else {
            this.doSet(var1);
            this.markInvalid();
         }
      }

      private void controlContentHasChanged() {
         this.markInvalid();
         TextInputControl.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
      }

      public void bind(ObservableValue var1) {
         if (var1 == null) {
            throw new NullPointerException("Cannot bind to null");
         } else {
            if (!var1.equals(this.observable)) {
               this.unbind();
               this.observable = var1;
               if (this.listener == null) {
                  this.listener = new Listener();
               }

               this.observable.addListener(this.listener);
               this.markInvalid();
               this.doSet((String)var1.getValue());
            }

         }
      }

      public void unbind() {
         if (this.observable != null) {
            this.doSet((String)this.observable.getValue());
            this.observable.removeListener(this.listener);
            this.observable = null;
         }

      }

      public boolean isBound() {
         return this.observable != null;
      }

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public Object getBean() {
         return TextInputControl.this;
      }

      public String getName() {
         return "text";
      }

      private void fireValueChangedEvent() {
         ExpressionHelper.fireValueChangedEvent(this.helper);
      }

      private void markInvalid() {
         this.fireValueChangedEvent();
      }

      private void doSet(String var1) {
         this.textIsNull = var1 == null;
         if (var1 == null) {
            var1 = "";
         }

         if (TextInputControl.this.filterAndSet(var1)) {
            if (TextInputControl.this.getTextFormatter() != null) {
               TextInputControl.this.getTextFormatter().updateValue(TextInputControl.this.getText());
            }

            TextInputControl.this.textUpdated();
            TextInputControl.this.resetUndoRedoState();
         }
      }

      // $FF: synthetic method
      TextProperty(Object var2) {
         this();
      }

      private class Listener implements InvalidationListener {
         private Listener() {
         }

         public void invalidated(Observable var1) {
            TextProperty.this.doSet((String)TextProperty.this.observable.getValue());
         }

         // $FF: synthetic method
         Listener(Object var2) {
            this();
         }
      }
   }

   protected interface Content extends ObservableStringValue {
      String get(int var1, int var2);

      void insert(int var1, String var2, boolean var3);

      void delete(int var1, int var2, boolean var3);

      int length();
   }
}
