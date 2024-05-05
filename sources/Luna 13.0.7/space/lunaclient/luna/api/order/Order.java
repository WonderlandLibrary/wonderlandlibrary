package space.lunaclient.luna.api.order;

public abstract interface Order
{
  public abstract boolean run(String[] paramArrayOfString);
  
  public abstract String usage();
}
