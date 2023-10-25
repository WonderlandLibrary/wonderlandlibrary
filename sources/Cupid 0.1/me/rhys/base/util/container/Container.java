package me.rhys.base.util.container;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Container<T> {
  private List<T> items = new CopyOnWriteArrayList<>();
  
  public List<T> getItems() {
    return this.items;
  }
  
  public void setItems(List<T> items) {
    this.items = items;
  }
  
  public void add(T item) {
    this.items.add(item);
  }
  
  @SafeVarargs
  public final void add(T... items) {
    Arrays.<T>stream(items).forEach(this::add);
  }
  
  public void remove(T item) {
    this.items.remove(item);
  }
  
  public T get(int index) {
    try {
      return this.items.get(index);
    } catch (Exception e) {
      return this.items.get(0);
    } 
  }
  
  public int indexOf(T item) {
    return this.items.indexOf(item);
  }
  
  public boolean isEmpty() {
    return this.items.isEmpty();
  }
  
  public void forEach(Consumer<? super T> action) {
    getItems().forEach(action);
  }
  
  public Stream<T> stream() {
    return getItems().stream();
  }
  
  public Stream<T> filter(Predicate<? super T> predicate) {
    return stream().filter(predicate);
  }
  
  public T find(Predicate<? super T> predicate) {
    return filter(predicate).findFirst().orElse(null);
  }
  
  public T findByClass(Class<? extends T> aClass) {
    return stream().filter(item -> item.getClass().equals(aClass)).findFirst().orElse(null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\container\Container.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */