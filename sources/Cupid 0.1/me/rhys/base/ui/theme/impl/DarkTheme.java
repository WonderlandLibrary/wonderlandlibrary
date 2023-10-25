package me.rhys.base.ui.theme.impl;

import me.rhys.base.ui.theme.Theme;
import me.rhys.base.util.render.ColorUtil;

public class DarkTheme extends Theme {
  public DarkTheme() {
    super(new Theme.WindowColors(
          
          ColorUtil.rgba(45, 52, 54, 1.0F), 
          ColorUtil.rgba(38, 43, 43, 1.0F)), new Theme.ButtonColors(
          
          ColorUtil.rgba(38, 43, 43, 1.0F), 
          ColorUtil.rgba(255, 255, 255, 1.0F)), new Theme.LabelColors(
          
          ColorUtil.rgba(255, 255, 255, 1.0F)), new Theme.CheckBoxColors(
          
          ColorUtil.rgba(38, 43, 43, 1.0F), 
          ColorUtil.rgba(28, 33, 33, 1.0F), 
          ColorUtil.rgba(46, 204, 113, 1.0F)), new Theme.SliderColors(
          
          ColorUtil.rgba(38, 43, 43, 1.0F), 
          ColorUtil.rgba(46, 204, 113, 1.0F)));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\theme\impl\DarkTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */