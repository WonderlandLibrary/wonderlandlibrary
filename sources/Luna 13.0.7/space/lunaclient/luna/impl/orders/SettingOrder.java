package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.managers.ElementManager;
import space.lunaclient.luna.impl.managers.SettingManager;
import space.lunaclient.luna.util.PlayerUtils;

public class SettingOrder
  implements Order
{
  public SettingOrder() {}
  
  public boolean run(String[] args)
  {
    if (args.length == 3)
    {
      Element module = Luna.INSTANCE.ELEMENT_MANAGER.getElement(args[0]);
      if (module != null)
      {
        Setting setting = Luna.INSTANCE.SETTING_MANAGER.getSettingByModByName(module, args[1]);
        if (setting != null)
        {
          if (setting.isCheck())
          {
            if (validSetting(setting, args[2]))
            {
              setting.setValBoolean(Boolean.parseBoolean(args[2]));
              PlayerUtils.tellPlayer(setting.getName() + " set to " + args[2] + ".", false);
            }
            else
            {
              PlayerUtils.tellPlayer(setting.getName() + ": [TRUE or FALSE].", false);
            }
            return true;
          }
          if (setting.isSlider())
          {
            if (validSetting(setting, args[2]))
            {
              setting.setValDouble(Double.parseDouble(args[2]));
              PlayerUtils.tellPlayer(setting.getName() + " set to " + args[2] + ".", false);
            }
            else
            {
              PlayerUtils.tellPlayer(setting.getName() + ": [min = " + setting.getMin() + " , max = " + setting.getMax() + "].", false);
            }
            return true;
          }
          if (setting.isCombo())
          {
            if (validSetting(setting, args[2]))
            {
              setting.setValString(args[2]);
              PlayerUtils.tellPlayer(setting.getName() + " set to " + args[2] + ".", false);
            }
            else
            {
              PlayerUtils.tellPlayer(setting.getName() + ": " + setting.getOptions().toString() + ".", false);
            }
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private boolean validSetting(Setting setting, String text)
  {
    try
    {
      if (setting.isSlider())
      {
        if (setting.onlyInt()) {
          return (Integer.parseInt(text) >= setting.getMin()) && (Integer.parseInt(text) <= setting.getMax());
        }
        return (Double.parseDouble(text) >= setting.getMin()) && (Double.parseDouble(text) <= setting.getMax());
      }
      if (setting.isCombo()) {
        return contains(setting.getOptions(), text);
      }
      if (setting.isCheck()) {
        return (text.equalsIgnoreCase("true")) || (text.equalsIgnoreCase("false"));
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return false;
  }
  
  private boolean contains(ArrayList<String> arrayList, String text)
  {
    for (String s : arrayList) {
      if (s.equalsIgnoreCase(text)) {
        return true;
      }
    }
    return false;
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "<element> <setting> <value>" + ChatFormatting.GRAY + " ]";
  }
}
