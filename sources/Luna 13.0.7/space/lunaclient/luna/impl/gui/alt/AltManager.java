package space.lunaclient.luna.impl.gui.alt;

import java.util.ArrayList;
import java.util.List;

public class AltManager
{
  private List<Alt> alts;
  private Alt lastAlt;
  
  public AltManager() {}
  
  public Alt getLastAlt()
  {
    return this.lastAlt;
  }
  
  public void setLastAlt(Alt alt)
  {
    this.lastAlt = alt;
  }
  
  public void setupAlts()
  {
    this.alts = new ArrayList();
  }
  
  public List<Alt> getAlts()
  {
    return this.alts;
  }
}
