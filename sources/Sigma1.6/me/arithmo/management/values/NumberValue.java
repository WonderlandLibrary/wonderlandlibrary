package me.arithmo.management.values;

import java.lang.reflect.Field;
import me.arithmo.module.Module;

public class NumberValue
  extends Value<Number>
{
  private double increment;
  private double min;
  private double max;
  
  public NumberValue(String name, Number value, Module module)
  {
    super(name, value, module);
  }
  
  public void setValue(String value)
  {
    Field[] arrayOfField;
    int j = (arrayOfField = getModule().getClass().getDeclaredFields()).length;
    for (int i = 0; i < j; i++)
    {
      Field field = arrayOfField[i];
      
      field.setAccessible(true);
      if ((field.isAnnotationPresent(Value.Val.class)) && (field.getName().equalsIgnoreCase(getValueName()))) {
        try
        {
          if (field.getType().isAssignableFrom(Float.TYPE))
          {
            super.setValue(Float.valueOf(Float.parseFloat(value)));
            field.setFloat(getModule(), Float.parseFloat(value));
          }
          else if (field.getType().isAssignableFrom(Double.TYPE))
          {
            super.setValue(Double.valueOf(Double.parseDouble(value)));
            field.setDouble(getModule(), Double.parseDouble(value));
          }
          else if (field.getType().isAssignableFrom(Long.TYPE))
          {
            super.setValue(Long.valueOf(Math.round(Double.parseDouble(value))));
            field.setLong(getModule(), Math.round(Double.parseDouble(value)));
          }
          else if (field.getType().isAssignableFrom(Integer.TYPE))
          {
            super.setValue(Integer.valueOf((int)Math.round(Double.parseDouble(value))));
            field.setInt(getModule(), (int)Math.round(Double.parseDouble(value)));
          }
          else if (field.getType().isAssignableFrom(Short.TYPE))
          {
            super.setValue(Short.valueOf((short)(int)Math.round(Double.parseDouble(value))));
            field.setShort(getModule(), (short)(int)Math.round(Double.parseDouble(value)));
          }
          else if (field.getType().isAssignableFrom(Byte.TYPE))
          {
            super.setValue(Byte.valueOf((byte)(int)Math.round(Double.parseDouble(value))));
            field.setByte(getModule(), (byte)(int)Math.round(Double.parseDouble(value)));
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  
  public void setValue(Number value)
  {
    super.setValue(value);
    Field[] arrayOfField;
    int j = (arrayOfField = getModule().getClass().getDeclaredFields()).length;
    for (int i = 0; i < j; i++)
    {
      Field field = arrayOfField[i];
      
      field.setAccessible(true);
      if ((field.isAnnotationPresent(Value.Val.class)) && (field.getName().equalsIgnoreCase(getValueName()))) {
        try
        {
          if (field.getType().isAssignableFrom(Float.TYPE)) {
            field.setFloat(getModule(), value.floatValue());
          } else if (field.getType().isAssignableFrom(Double.TYPE)) {
            field.setDouble(getModule(), value.doubleValue());
          } else if (field.getType().isAssignableFrom(Long.TYPE)) {
            field.setLong(getModule(), value.longValue());
          } else if (field.getType().isAssignableFrom(Integer.TYPE)) {
            field.setLong(getModule(), value.intValue());
          } else if (field.getType().isAssignableFrom(Short.TYPE)) {
            field.setLong(getModule(), value.shortValue());
          } else if (field.getType().isAssignableFrom(Byte.TYPE)) {
            field.setLong(getModule(), value.byteValue());
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  
  public double getMin()
  {
    return this.min;
  }
  
  public double getMax()
  {
    return this.max;
  }
  
  public double getIncrement()
  {
    return this.increment;
  }
  
  public void setMin(double min)
  {
    this.min = min;
  }
  
  public void setMax(double max)
  {
    this.max = max;
  }
  
  public void setIncrement(double increment)
  {
    this.increment = increment;
  }
  
  public void increment()
  {
    setValue(Double.valueOf(Math.min(((Number)getValue()).doubleValue() + this.increment, this.max)));
  }
  
  public void deincrement()
  {
    setValue(Double.valueOf(Math.max(((Number)getValue()).doubleValue() - this.increment, this.min)));
  }
}
