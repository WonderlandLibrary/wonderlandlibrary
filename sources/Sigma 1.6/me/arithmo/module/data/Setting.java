package me.arithmo.module.data;

import com.google.gson.annotations.Expose;
import java.lang.reflect.Type;

public class Setting<E>
{
  private final String name;
  private final String desc;
  private final Type type;
  private final double inc;
  private String[] options;
  private String selectedOption;
  private final double min;
  private final double max;
  @Expose
  private E value;
  
  public String getDesc()
  {
    return this.desc;
  }
  
  public Setting(String name, E value, String desc)
  {
    this.name = name;
    this.value = value;
    this.type = value.getClass().getGenericSuperclass();
    this.desc = desc;
    if ((value instanceof Number))
    {
      this.inc = 0.5D;
      this.min = 1.0D;
      this.max = 20.0D;
    }
    else
    {
      this.inc = 0.0D;
      this.min = 0.0D;
      this.max = 0.0D;
    }
  }
  
  public Setting(String name, E value, String desc, double inc, double min, double max)
  {
    this.name = name;
    this.value = value;
    this.type = value.getClass().getGenericSuperclass();
    this.desc = desc;
    this.inc = inc;
    this.min = min;
    this.max = max;
  }
  
  public void setValue(E value)
  {
    this.value = value;
  }
  
  public E getValue()
  {
    return (E)this.value;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Type getType()
  {
    return this.type;
  }
  
  public double getInc()
  {
    return this.inc;
  }
  
  public double getMin()
  {
    return this.min;
  }
  
  public double getMax()
  {
    return this.max;
  }
  
  public void update(Setting setting)
  {
    this.value = (E) setting.value;
  }
}
