package me.rhys.base.ui.theme;

public class Theme {
  public WindowColors windowColors;
  
  public ButtonColors buttonColors;
  
  public LabelColors labelColors;
  
  public CheckBoxColors checkBoxColors;
  
  public SliderColors sliderColors;
  
  public Theme(WindowColors windowColors, ButtonColors buttonColors, LabelColors labelColors, CheckBoxColors checkBoxColors, SliderColors sliderColors) {
    this.windowColors = windowColors;
    this.buttonColors = buttonColors;
    this.labelColors = labelColors;
    this.checkBoxColors = checkBoxColors;
    this.sliderColors = sliderColors;
  }
  
  public static final class WindowColors {
    public int background;
    
    public int border;
    
    public WindowColors(int background, int border) {
      this.background = background;
      this.border = border;
    }
  }
  
  public static final class ButtonColors {
    public int background;
    
    public int text;
    
    public ButtonColors(int background, int text) {
      this.background = background;
      this.text = text;
    }
  }
  
  public static final class LabelColors {
    public int text;
    
    public LabelColors(int text) {
      this.text = text;
    }
  }
  
  public static final class CheckBoxColors {
    public int background;
    
    public int border;
    
    public int active;
    
    public CheckBoxColors(int background, int border, int active) {
      this.background = background;
      this.border = border;
      this.active = active;
    }
  }
  
  public static final class SliderColors {
    public int background;
    
    public int fill;
    
    public SliderColors(int background, int fill) {
      this.background = background;
      this.fill = fill;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\theme\Theme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */