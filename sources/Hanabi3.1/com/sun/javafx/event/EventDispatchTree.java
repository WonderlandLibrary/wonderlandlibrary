package com.sun.javafx.event;

import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public interface EventDispatchTree extends EventDispatchChain {
   EventDispatchTree createTree();

   EventDispatchTree mergeTree(EventDispatchTree var1);

   EventDispatchTree append(EventDispatcher var1);

   EventDispatchTree prepend(EventDispatcher var1);
}
