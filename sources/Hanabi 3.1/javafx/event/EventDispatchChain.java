package javafx.event;

public interface EventDispatchChain {
   EventDispatchChain append(EventDispatcher var1);

   EventDispatchChain prepend(EventDispatcher var1);

   Event dispatchEvent(Event var1);
}
