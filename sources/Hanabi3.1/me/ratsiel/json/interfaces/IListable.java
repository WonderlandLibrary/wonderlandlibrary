package me.ratsiel.json.interfaces;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IListable {
   int size();

   void add(Object var1);

   void add(int var1, Object var2);

   Object get(int var1, Class var2);

   Object get(int var1);

   void loop(Consumer var1);

   void loop(BiConsumer var1);
}
