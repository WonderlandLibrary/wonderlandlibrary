package net.minecraft.util;

public abstract class LazyLoadBase
{
  private Object value;
  private boolean isLoaded = false;
  private static final String __OBFID = "CL_00002263";
  
  public LazyLoadBase() {}
  
  public Object getValue()
  {
    if (!this.isLoaded)
    {
      this.isLoaded = true;
      this.value = load();
    }
    return this.value;
  }
  
  protected abstract Object load();
}
