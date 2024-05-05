package space.lunaclient.luna.api.event;

public enum EventPriority
{
  LOW(0),  MEDIUM(1),  HIGH(2);
  
  private int value;
  
  private EventPriority(int value)
  {
    this.value = value;
  }
  
  public int getValue()
  {
    return this.value;
  }
}
