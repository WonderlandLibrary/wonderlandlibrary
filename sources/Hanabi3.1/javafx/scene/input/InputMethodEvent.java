package javafx.scene.input;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class InputMethodEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType INPUT_METHOD_TEXT_CHANGED;
   public static final EventType ANY;
   private transient ObservableList composed;
   private final String committed;
   private final int caretPosition;

   public InputMethodEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("composed") List var4, @NamedArg("committed") String var5, @NamedArg("caretPosition") int var6) {
      super(var1, var2, var3);
      this.composed = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Collection)var4));
      this.committed = var5;
      this.caretPosition = var6;
   }

   public InputMethodEvent(@NamedArg("eventType") EventType var1, @NamedArg("composed") List var2, @NamedArg("committed") String var3, @NamedArg("caretPosition") int var4) {
      this((Object)null, (EventTarget)null, var1, var2, var3, var4);
   }

   public final ObservableList getComposed() {
      return this.composed;
   }

   public final String getCommitted() {
      return this.committed;
   }

   public final int getCaretPosition() {
      return this.caretPosition;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("InputMethodEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", composed = ").append(this.getComposed());
      var1.append(", committed = ").append(this.getCommitted());
      var1.append(", caretPosition = ").append(this.getCaretPosition());
      return var1.append("]").toString();
   }

   public InputMethodEvent copyFor(Object var1, EventTarget var2) {
      return (InputMethodEvent)super.copyFor(var1, var2);
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(new ArrayList(this.composed));
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      ArrayList var2 = (ArrayList)var1.readObject();
      this.composed = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Collection)var2));
   }

   static {
      INPUT_METHOD_TEXT_CHANGED = new EventType(InputEvent.ANY, "INPUT_METHOD_TEXT_CHANGED");
      ANY = INPUT_METHOD_TEXT_CHANGED;
   }
}
