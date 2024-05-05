package space.lunaclient.luna.api.event;

import java.lang.reflect.Method;

public class Data
{
  private Object source;
  private Method target;
  private EventPriority priority;
  
  public Data(Object source, Method target, EventPriority priority)
  {
    this.source = source;
    this.target = target;
    this.priority = priority;
  }
  
  public Object getSource()
  {
    return this.source;
  }
  
  public Method getTarget()
  {
    return this.target;
  }
  
  public EventPriority getPriority()
  {
    return this.priority;
  }
}
