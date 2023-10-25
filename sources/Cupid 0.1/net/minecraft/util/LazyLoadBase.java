package net.minecraft.util;

public abstract class LazyLoadBase<T> {
  private T value;
  
  private boolean isLoaded = false;
  
  public T getValue() {
    if (!this.isLoaded) {
      this.isLoaded = true;
      this.value = load();
    } 
    return this.value;
  }
  
  protected abstract T load();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\LazyLoadBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */