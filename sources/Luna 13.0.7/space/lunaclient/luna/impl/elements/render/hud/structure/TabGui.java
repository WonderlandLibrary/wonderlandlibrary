package space.lunaclient.luna.impl.elements.render.hud.structure;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.render.hud.HUD;
import space.lunaclient.luna.impl.events.EventKeyBoard;
import space.lunaclient.luna.impl.events.EventRender2D;
import space.lunaclient.luna.impl.managers.ElementManager;
import space.lunaclient.luna.impl.managers.SettingManager;
import space.lunaclient.luna.util.ColorUtils;
import space.lunaclient.luna.util.FontUtils;
import space.lunaclient.luna.util.MathUtils;

public class TabGui
{
  private ArrayList<Category> categoryArrayList;
  private int currentCategorySelection;
  private int currentModSelection;
  private int currentSettingSelection;
  private int screen;
  private boolean editMode;
  private int transition;
  
  public TabGui()
  {
    this.categoryArrayList = new ArrayList();
    this.currentCategorySelection = 0;
    this.currentModSelection = 0;
    this.currentSettingSelection = 0;
    this.editMode = false;
    this.categoryArrayList.addAll(Arrays.asList(Category.values()));
  }
  
  private int getTransition()
  {
    return this.transition;
  }
  
  private void setTransition(int transition)
  {
    this.transition = transition;
  }
  
  @EventRegister
  public void onRender2D(EventRender2D e)
  {
    if (!HUD.tabGui.getValBoolean()) {
      return;
    }
    GL11.glPushMatrix();
    
    drawName(2, 1);
    int startX = 2;
    int startY = 11;
    
    Gui.drawRect(startX - 1, startY - 1, startX + getWidestCategory() + 4, startY + this.categoryArrayList.size() * 11, Integer.MIN_VALUE);
    for (Category c : this.categoryArrayList)
    {
      if (getTransition() > 0) {
        setTransition(getTransition() - 1);
      }
      if (getCurrentCategory().equals(c)) {
        Gui.drawRect(startX, startY, startX + getWidestCategory() + 4 - 1, startY + 9 + 2 - 1, ColorUtils.getMainColor());
      }
      FontUtils.drawStringWithShadow(c.name().substring(0, 1).toUpperCase() + c.name().substring(1, c.name().length()).toLowerCase(), getCurrentCategory().equals(c) ? startX + 3 : startX + 1, startY + 1, -1);
      
      startY += 11;
    }
    int modX;
    int modY;
    Iterator localIterator2;
    if ((this.screen == 1) || (this.screen == 2))
    {
      modX = startX + getWidestCategory() + 4 + 3;
      modY = 11 + 11 * this.currentCategorySelection;
      
      Gui.drawRect(modX - 1, modY - 1, modX + getWidestMod() + 12, modY + getModsForCurrentCategory().size() * 11, Integer.MIN_VALUE);
      for (localIterator2 = getModsForCurrentCategory().iterator(); localIterator2.hasNext();)
      {
        m = (Element)localIterator2.next();
        if (getCurrentModule().equals(m)) {
          Gui.drawRect(modX, modY, modX + getWidestMod() + 12 - 1, modY + 9 + 2 - 1, ColorUtils.getMainColor());
        }
        if (Luna.INSTANCE.SETTING_MANAGER.getSettingsByMod(m) != null) {
          FontUtils.drawStringWithShadow("≡", modX + getWidestMod() + 12 - FontUtils.getStringWidth("≡") - 2, modY + 1, new Color(128, 131, 152).getRGB());
        }
        FontUtils.drawStringWithShadow(m.getName(), modX + (getCurrentModule().equals(m) ? 3 : 1), modY + 1, m.isToggled() ? -1 : new Color(128, 131, 152).getRGB());
        
        modY += 11;
      }
    }
    Element m;
    int setX;
    int setY;
    if (this.screen == 2)
    {
      setX = startX + getWidestCategory() + 4 + 3 + getWidestMod() + 12 + 3;
      int modY = 11 + 11 * this.currentCategorySelection;
      setY = modY + 11 * this.currentModSelection;
      
      Gui.drawRect(setX - 1, setY - 1, setX + getWidestSetting() + 8, setY + getSettingsForCurrentModule().size() * 11, Integer.MIN_VALUE);
      for (Setting s : getSettingsForCurrentModule())
      {
        if (getCurrentSetting().equals(s)) {
          Gui.drawRect(setX, setY, setX + getWidestSetting() + 8 - 1, setY + 9 + 2 - 1, ColorUtils.getMainColor());
        }
        if (s.isSlider()) {
          FontUtils.drawStringWithShadow(s.getName() + ": " + MathUtils.roundToPlace(s.getValDouble(), 2), setX + (getCurrentSetting().equals(s) ? 3 : 1), setY + 1, (this.editMode) && (getCurrentSetting().equals(s)) ? -1 : new Color(128, 131, 152).getRGB());
        } else if (s.isCheck()) {
          FontUtils.drawStringWithShadow(s.getName(), setX + (getCurrentSetting().equals(s) ? 3 : 1), setY + 1, s.getValBoolean() ? -1 : new Color(128, 131, 152).getRGB());
        } else if (s.isCombo()) {
          FontUtils.drawStringWithShadow(s.getName() + ": " + s.getValString(), setX + (getCurrentSetting().equals(s) ? 3 : 1), setY + 1, (this.editMode) && (getCurrentSetting().equals(s)) ? -1 : new Color(128, 131, 152).getRGB());
        }
        setY += 11;
      }
    }
    GL11.glPopMatrix();
  }
  
  private void drawName(int x, int y)
  {
    FontUtils.drawStringWithShadow(ChatFormatting.WHITE + Luna.INSTANCE.NAME + ChatFormatting.RESET + " " + Luna.INSTANCE.CURRENT_FORMAT + Luna.INSTANCE.BUILD, x, y, 
      ColorUtils.getMainColor());
  }
  
  private void up()
  {
    if (this.screen == 0)
    {
      if (this.currentCategorySelection > 0) {
        this.currentCategorySelection -= 1;
      } else {
        this.currentCategorySelection = (this.categoryArrayList.size() - 1);
      }
    }
    else if (this.screen == 1)
    {
      if (this.currentModSelection > 0) {
        this.currentModSelection -= 1;
      } else {
        this.currentModSelection = (getModsForCurrentCategory().size() - 1);
      }
    }
    else if ((this.screen == 2) && (!this.editMode))
    {
      if (this.currentSettingSelection > 0) {
        this.currentSettingSelection -= 1;
      } else {
        this.currentSettingSelection = (getSettingsForCurrentModule().size() - 1);
      }
    }
    else if (this.screen == 2)
    {
      Setting s = getCurrentSetting();
      if (s.isSlider())
      {
        if (s.onlyInt()) {
          s.setValDouble(s.getValDouble() + 1.0D);
        } else {
          s.setValDouble(s.getValDouble() + 0.1D);
        }
        if (s.getValDouble() > s.getMax()) {
          s.setValDouble(s.getMax());
        }
      }
      else if (s.isCombo())
      {
        try
        {
          s.setValString((String)s.getOptions().get(s.currentIndex() - 1));
        }
        catch (Exception e)
        {
          s.setValString((String)s.getOptions().get(s.getOptions().size() - 1));
        }
      }
      else if (s.isCheck())
      {
        s.setValBoolean(!s.getValBoolean());
      }
    }
  }
  
  private void down()
  {
    if (this.screen == 0)
    {
      if (this.currentCategorySelection < this.categoryArrayList.size() - 1) {
        this.currentCategorySelection += 1;
      } else {
        this.currentCategorySelection = 0;
      }
    }
    else if (this.screen == 1)
    {
      if (this.currentModSelection < getModsForCurrentCategory().size() - 1) {
        this.currentModSelection += 1;
      } else {
        this.currentModSelection = 0;
      }
    }
    else if ((this.screen == 2) && (!this.editMode))
    {
      if (this.currentSettingSelection < getSettingsForCurrentModule().size() - 1) {
        this.currentSettingSelection += 1;
      } else {
        this.currentSettingSelection = 0;
      }
    }
    else if (this.screen == 2)
    {
      Setting s = getCurrentSetting();
      if (s.isSlider())
      {
        if (s.onlyInt()) {
          s.setValDouble(s.getValDouble() - 1.0D);
        } else {
          s.setValDouble(s.getValDouble() - 0.1D);
        }
        if (s.getValDouble() < s.getMin()) {
          s.setValDouble(s.getMin());
        }
      }
      else if (s.isCombo())
      {
        try
        {
          s.setValString((String)s.getOptions().get(s.currentIndex() + 1));
        }
        catch (Exception e)
        {
          s.setValString((String)s.getOptions().get(0));
        }
      }
      else if (s.isCheck())
      {
        s.setValBoolean(!s.getValBoolean());
      }
    }
  }
  
  private void left()
  {
    if (this.screen == 1)
    {
      this.screen = 0;
      this.currentModSelection = 0;
    }
    else if (this.screen == 2)
    {
      this.screen = 1;
      this.currentSettingSelection = 0;
      this.editMode = false;
    }
  }
  
  private void right()
  {
    if (this.screen == 0) {
      this.screen = 1;
    } else if ((this.screen == 1) && (getCurrentModule() != null) && (getSettingsForCurrentModule() == null)) {
      getCurrentModule().toggle();
    } else if ((this.screen == 1) && (getSettingsForCurrentModule() != null) && (getCurrentModule() != null)) {
      this.screen = 2;
    }
  }
  
  private void enter()
  {
    if ((this.screen == 1) && (getCurrentModule() != null))
    {
      getCurrentModule().toggle();
    }
    else if ((this.screen == 2) && (getCurrentSetting().isCheck()))
    {
      Setting s = getCurrentSetting();
      s.setValBoolean(!s.getValBoolean());
    }
    else if (this.screen == 2)
    {
      this.editMode = (!this.editMode);
    }
  }
  
  @EventRegister
  public void onKey(EventKeyBoard e)
  {
    switch (e.getKey())
    {
    case 200: 
      up();
      break;
    case 208: 
      down();
      break;
    case 205: 
      right();
      break;
    case 203: 
      left();
      break;
    case 28: 
      enter();
    }
  }
  
  private Category getCurrentCategory()
  {
    return (Category)this.categoryArrayList.get(this.currentCategorySelection);
  }
  
  private ArrayList<Element> getModsForCurrentCategory()
  {
    return Luna.INSTANCE.ELEMENT_MANAGER.getElementsForCategory(getCurrentCategory());
  }
  
  private Element getCurrentModule()
  {
    return (Element)getModsForCurrentCategory().get(this.currentModSelection);
  }
  
  private ArrayList<Setting> getSettingsForCurrentModule()
  {
    return Luna.INSTANCE.SETTING_MANAGER.getSettingsByMod(getCurrentModule());
  }
  
  private Setting getCurrentSetting()
  {
    return (Setting)getSettingsForCurrentModule().get(this.currentSettingSelection);
  }
  
  private int getWidestCategory()
  {
    int width = 0;
    for (Category c : this.categoryArrayList) {
      if (FontUtils.getStringWidth(c.name()) >= width) {
        width = FontUtils.getStringWidth(c.name());
      }
    }
    return width;
  }
  
  private int getWidestMod()
  {
    int width = 0;
    for (Element m : getModsForCurrentCategory()) {
      if (FontUtils.getStringWidth(m.getName()) >= width) {
        width = FontUtils.getStringWidth(m.getName() + " ");
      }
    }
    return width;
  }
  
  private int getWidestSetting()
  {
    int width = 0;
    for (Setting s : getSettingsForCurrentModule()) {
      if (s.isCombo())
      {
        if (FontUtils.getStringWidth(s.getName() + ": " + s.getValString()) > width) {
          width = FontUtils.getStringWidth(s.getName() + ": " + s.getValString());
        }
      }
      else if (s.isSlider())
      {
        if (FontUtils.getStringWidth(s.getName() + ": " + MathUtils.roundToPlace(s.getValDouble(), 7)) > width) {
          width = FontUtils.getStringWidth(s.getName() + ": " + MathUtils.roundToPlace(s.getValDouble(), 7));
        }
      }
      else if ((s.isCheck()) && 
        (FontUtils.getStringWidth(s.getName() + ": " + s.getValBoolean()) > width)) {
        width = FontUtils.getStringWidth(s.getName() + s.getValBoolean());
      }
    }
    return width;
  }
}
