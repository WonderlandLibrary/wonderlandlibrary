package space.lunaclient.luna.api.setting;

import java.util.ArrayList;
import space.lunaclient.luna.api.element.Element;

public class Setting
{
  private String name;
  private Element parent;
  private String sval;
  private Mode mode;
  private ArrayList<String> options;
  private boolean bval;
  private boolean locked;
  private boolean doubleLocked;
  private double doubleVal;
  private double min;
  private double max;
  private boolean onlyInt = false;
  
  private static enum Mode
  {
    COMBO,  CHECK,  SLIDER;
    
    private Mode() {}
  }
  
  public Setting(String name, Element parent, String stringVal, ArrayList<String> options, Boolean locked)
  {
    this.name = name;
    this.parent = parent;
    this.sval = stringVal;
    this.options = options;
    this.locked = locked.booleanValue();
    this.mode = Mode.COMBO;
  }
  
  public Setting(String name, Element parent, boolean booleanVal)
  {
    this.name = name;
    this.parent = parent;
    this.bval = booleanVal;
    this.mode = Mode.CHECK;
  }
  
  public Setting(String name, Element parent, double doubleVal, double min, double max, boolean onlyInt, Boolean locked)
  {
    this.name = name;
    this.parent = parent;
    this.doubleVal = doubleVal;
    this.min = min;
    this.max = max;
    this.onlyInt = onlyInt;
    this.doubleLocked = locked.booleanValue();
    this.mode = Mode.SLIDER;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Element getParentMod()
  {
    return this.parent;
  }
  
  public ArrayList<String> getOptions()
  {
    return this.options;
  }
  
  public int currentIndex()
  {
    return this.options.indexOf(this.sval);
  }
  
  public String getValString()
  {
    return this.sval.substring(0, 1).toUpperCase() + this.sval.substring(1);
  }
  
  public String getValStringForSaving()
  {
    return this.sval;
  }
  
  public boolean getValBoolean()
  {
    return this.bval;
  }
  
  public void setValString(String in)
  {
    this.sval = in;
  }
  
  public void setValBoolean(boolean in)
  {
    this.bval = in;
  }
  
  public void setValDouble(double in)
  {
    this.doubleVal = in;
  }
  
  public double getValDouble()
  {
    if (this.onlyInt) {
      this.doubleVal = ((int)this.doubleVal);
    }
    return this.doubleVal;
  }
  
  public void setLockedMode(boolean lockedVal)
  {
    this.locked = lockedVal;
  }
  
  public boolean isLockedDouble()
  {
    return this.doubleLocked;
  }
  
  public void setLockedDouble(boolean lockedVal)
  {
    this.doubleLocked = lockedVal;
  }
  
  public boolean isLockedMode()
  {
    return this.locked;
  }
  
  public double getMin()
  {
    return this.min;
  }
  
  public double getMax()
  {
    return this.max;
  }
  
  public boolean isCombo()
  {
    return this.mode.equals(Mode.COMBO);
  }
  
  public boolean isCheck()
  {
    return this.mode.equals(Mode.CHECK);
  }
  
  public boolean isSlider()
  {
    return this.mode.equals(Mode.SLIDER);
  }
  
  public boolean onlyInt()
  {
    return this.onlyInt;
  }
}
