package javafx.event;

import java.util.EventListener;

@FunctionalInterface
public interface EventHandler extends EventListener {
   void handle(Event var1);
}
