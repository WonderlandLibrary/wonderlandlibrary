package space.lunaclient.luna.api.manager;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Manager<T>
{
  private CopyOnWriteArrayList<T> contents = new CopyOnWriteArrayList();
  
  public Manager() {}
  
  public CopyOnWriteArrayList<T> getContents()
  {
    return this.contents;
  }
  
  public void setContents(CopyOnWriteArrayList<T> contents)
  {
    this.contents = contents;
  }
}
