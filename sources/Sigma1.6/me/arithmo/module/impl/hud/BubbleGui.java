package me.arithmo.module.impl.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventKeyPress;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.ModuleManager;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.ModuleData.Type;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.MathUtils;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.RotationUtils;
import me.arithmo.util.StringConversions;
import me.arithmo.util.Timer;
import me.arithmo.util.misc.ChatUtil;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;

public class BubbleGui
  extends Module
{
  public int typeRot = 65356;
  public int setRot = 65356;
  public int modRot = 65356;
  public int targetTypeRot = 65356;
  public int targetSetRot = 65356;
  public int targetModRot = 65356;
  Timer timer = new Timer();
  int opacity = 45;
  int targetOpacity = 45;
  public int currentSetting;
  public int currentType;
  public int currentMod;
  public int hiddenX;
  public int targetX;
  public int targetModX;
  public int modX;
  public int targetModSetX;
  public int setModX;
  public int targetSetX;
  public int setX;
  public boolean inModules;
  public boolean inModSet;
  public boolean inSet;
  public Module selectedModule;
  boolean isActive;
  
  public BubbleGui(ModuleData data)
  {
    super(data);
  }
  
  public void onEnable()
  {
    this.inModules = false;
    this.inModSet = false;
    this.inSet = false;
    this.targetX = 0;
    this.typeRot = 65356;
    this.targetTypeRot = 65356;
  }
  
  @RegisterEvent(events={EventRenderGui.class, EventTick.class, EventKeyPress.class})
  public void onEvent(Event event)
  {
    if (mc.gameSettings.showDebugInfo) {
      return;
    }
    if ((event instanceof EventRenderGui))
    {
      EventRenderGui er = (EventRenderGui)event;
      if (this.timer.delay(4500.0F))
      {
        this.targetX = 65386;
        this.targetSetX = 65386;
        this.targetModX = 65386;
        this.inModules = false;
        this.inModSet = false;
        this.inSet = false;
        this.isActive = false;
      }
      animate();
      int rot = this.setRot;
      Color[] color;
      int currentModVal;
      if (this.inModSet)
      {
        color = new Color[getSettings(this.selectedModule).size()];
        currentModVal = 0;
        for (Setting mod : getSettings(this.selectedModule))
        {
          int posX = 93;
          int posZ = 0;
          float cos = (float)Math.cos(Math.toRadians(rot));
          float sin = (float)Math.sin(Math.toRadians(rot));
          float rotY = -(posZ * cos - posX * sin);
          float rotX = -(posX * cos + posZ * sin);
          int posXT = 110;
          int posYT = -mc.fontRendererObj.FONT_HEIGHT / 2;
          float rotY1 = -(posYT * cos - posXT * sin);
          float rotX1 = -(posXT * cos + posYT * sin);
          color[currentModVal] = new Color((int)(Math.sin(currentModVal) * 127.0D + 128.0D), 
            (int)(Math.sin(currentModVal + 1.5707963267948966D) * 127.0D + 128.0D), 
            (int)(Math.sin(currentModVal + 3.141592653589793D) * 127.0D + 128.0D));
          
          RenderingUtil.drawBorderedCircle(this.setX + (int)rotX, (int)rotY + er
            .getResolution().getScaledHeight() / 2, 10.0D, 1.0D, 
            Colors.getColor(color[currentModVal].getRed(), color[currentModVal].getGreen(), color[currentModVal]
            .getBlue(), getAlpha(rot, 2.35F)), 
            Colors.getColor(20, 20, 20, getAlpha(rot, 2.35F)));
          String xd = mod.getName().charAt(0) + mod.getName().toLowerCase().substring(1);
          GlStateManager.pushMatrix();
          GlStateManager.enableBlend();
          RenderingUtil.drawOutlinedString(xd + " " + (
            getSettings(this.selectedModule).get(this.currentSetting) == mod ? mod.getValue() : ""), this.setX + (int)rotX1, (int)rotY1 + er
            .getResolution().getScaledHeight() / 2, 
            Colors.getColor(255, 255, 255, getAlpha(rot, 2.35F)));
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
          rot -= 19;
          currentModVal++;
        }
      }
      rot = this.modRot;
      Color[] color2;
      int currentModVal2;
      if (this.inModules)
      {
        color = new Color[getModules(ModuleData.Type.values()[this.currentType]).size()];
        currentModVal = 0;
        for (Module mod : getModules(ModuleData.Type.values()[this.currentType]))
        {
          int posX = 66;
          int posZ = 0;
          float cos = (float)Math.cos(Math.toRadians(rot));
          float sin = (float)Math.sin(Math.toRadians(rot));
          float rotY = -(posZ * cos - posX * sin);
          float rotX = -(posX * cos + posZ * sin);
          int posXT = 83;
          int posYT = -mc.fontRendererObj.FONT_HEIGHT / 2;
          float rotY1 = -(posYT * cos - posXT * sin);
          float rotX1 = -(posXT * cos + posYT * sin);
          color[currentModVal] = new Color((int)(Math.sin(currentModVal) * 127.0D + 128.0D), 
            (int)(Math.sin(currentModVal + 1.5707963267948966D) * 127.0D + 128.0D), 
            (int)(Math.sin(currentModVal + 3.141592653589793D) * 127.0D + 128.0D));
          
          RenderingUtil.drawBorderedCircle(this.modX + (int)rotX, (int)rotY + er
            .getResolution().getScaledHeight() / 2, 10.0D, 1.0D, 
            Colors.getColor(color[currentModVal].getRed(), color[currentModVal].getGreen(), color[currentModVal]
            .getBlue(), getAlpha(rot, 2.25F)), 
            Colors.getColor(20, 20, 20, getAlpha(rot, 2.25F)));
          if (!this.inModSet)
          {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            RenderingUtil.drawOutlinedString(mod.getName(), this.modX + (int)rotX1, (int)rotY1 + er
              .getResolution().getScaledHeight() / 2, mod
              .isEnabled() ? Colors.getColor(255, 255, 255, getAlpha(rot, 2.25F)) : 
              Colors.getColor(175, 175, 175, getAlpha(rot, 2.25F)));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
          }
          rot -= 27;
          currentModVal++;
        }
      }
      rot = this.typeRot;
      for (ModuleData.Type type : ModuleData.Type.values())
      {
        int posX = 40;
        int posZ = 0;
        float cos = (float)Math.cos(Math.toRadians(rot));
        float sin = (float)Math.sin(Math.toRadians(rot));
        float rotY = -(posZ * cos - posX * sin);
        float rotX = -(posX * cos + posZ * sin);
        int posXT = 58;
        int posYT = -mc.fontRendererObj.FONT_HEIGHT / 2;
        float rotY1 = -(posYT * cos - posXT * sin);
        float rotX1 = -(posXT * cos + posYT * sin);
        Color color3 = new Color(Module.getColor(type));
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        RenderingUtil.drawBorderedCircle(this.hiddenX + (int)rotX, (int)rotY + er
          .getResolution().getScaledHeight() / 2, 10.0D, 1.0D, 
          Colors.getColor(color3.getRed(), color3.getGreen(), color3.getBlue(), getAlpha(rot, 2.2F)), 
          Colors.getColor(20, 20, 20, getAlpha(rot, 2.2F)));
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (!this.inModules)
        {
          GlStateManager.pushMatrix();
          GlStateManager.enableBlend();
          RenderingUtil.drawOutlinedString(type.name(), this.hiddenX + (int)rotX1 + 5, (int)rotY1 + er
            .getResolution().getScaledHeight() / 2, 
            Colors.getColor(255, 255, 255, getAlpha(rot, 2.2F)));
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
        }
        rot -= 45;
      }
      RenderingUtil.drawBorderedCircle(this.hiddenX / 8, er.getResolution().getScaledHeight() / 2, 24.0D, 1.0D, 
        Colors.getColor(50, 255, 163), Colors.getColor(20, 20, 20));
    }
    if ((event instanceof EventKeyPress))
    {
      EventKeyPress ek = (EventKeyPress)event;
      if ((this.isActive) && (keyCheck(ek.getKey()))) {
        this.timer.reset();
      }
      if ((!this.isActive) && (keyCheck(ek.getKey())))
      {
        this.isActive = true;
        this.inModules = false;
        this.targetX = 0;
        this.targetModX = 65386;
        this.timer.reset();
        return;
      }
      if ((!this.inModules) && (this.isActive))
      {
        if (ek.getKey() == 208)
        {
          this.targetTypeRot += 45;
          this.currentType += 1;
          if (this.currentType > ModuleData.Type.values().length - 1)
          {
            this.targetTypeRot = 65356;
            this.currentType = 0;
          }
        }
        else if (ek.getKey() == 200)
        {
          this.targetTypeRot -= 45;
          this.currentType -= 1;
          if (this.currentType < 0)
          {
            this.currentType = (ModuleData.Type.values().length - 1);
            this.targetTypeRot = (65356 + (ModuleData.Type.values().length - 1) * 45);
          }
        }
        else if (ek.getKey() == 205)
        {
          this.inModules = true;
          this.targetModRot = 65356;
          this.modRot = 65356;
          this.currentMod = 0;
          this.targetModX = 0;
        }
        else if (ek.getKey() == 203)
        {
          this.isActive = false;
          this.targetX = 65386;
          this.modX = 65361;
        }
      }
      else if ((this.inModules) && (!this.inModSet) && (this.isActive) && (!this.inSet))
      {
        if (ek.getKey() == 203)
        {
          Thread thread = new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                BubbleGui.this.targetModX = 65366;
                Thread.sleep(150L);
              }
              catch (InterruptedException localInterruptedException) {}
              BubbleGui.this.inModules = false;
            }
          });
          thread.start();
        }
        if (ek.getKey() == 208)
        {
          this.targetModRot += 27;
          this.currentMod += 1;
          if (this.currentMod > getModules(ModuleData.Type.values()[this.currentType]).size() - 1)
          {
            this.targetModRot = 65356;
            this.currentMod = 0;
          }
        }
        else if (ek.getKey() == 200)
        {
          this.targetModRot -= 27;
          this.currentMod -= 1;
          if (this.currentMod < 0)
          {
            this.targetModRot = (65356 + (getModules(ModuleData.Type.values()[this.currentType]).size() - 1) * 27);
            this.currentMod = (getModules(ModuleData.Type.values()[this.currentType]).size() - 1);
          }
        }
        else if (ek.getKey() == 28)
        {
          try
          {
            Module mod = (Module)getModules(ModuleData.Type.values()[this.currentType]).get(this.currentMod);
            if ((mod != this) || (mod != Client.getModuleManager().get(TabGUI.class))) {
              mod.toggle();
            }
          }
          catch (Exception e)
          {
            ChatUtil.printChat(
              getModules(ModuleData.Type.values()[this.currentType]).size() + ", " + this.currentMod + ", ");
          }
        }
        else if (ek.getKey() == 205)
        {
          this.selectedModule = ((Module)getModules(ModuleData.Type.values()[this.currentType]).get(this.currentMod));
          if (getSettings(this.selectedModule) != null)
          {
            this.inModSet = true;
            this.targetSetX = 0;
            this.currentSetting = 0;
            this.setRot = 65356;
            this.targetSetRot = 65356;
          }
          else if ((this.selectedModule != Client.getModuleManager().get(BubbleGui.class)) || (this.selectedModule != Client.getModuleManager().get(TabGUI.class)))
          {
            this.selectedModule.toggle();
          }
        }
      }
      else if ((this.inModSet) && (!this.inSet))
      {
        if (ek.getKey() == 203)
        {
          Thread thread = new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                BubbleGui.this.targetSetX = 65336;
                Thread.sleep(100L);
              }
              catch (InterruptedException localInterruptedException) {}
              BubbleGui.this.inModSet = false;
            }
          });
          thread.start();
        }
        else if (ek.getKey() == 208)
        {
          this.targetSetRot += 19;
          this.currentSetting += 1;
          if (this.currentSetting > getSettings(this.selectedModule).size() - 1)
          {
            this.targetSetRot = 65356;
            this.currentSetting = 0;
          }
        }
        else if (ek.getKey() == 200)
        {
          this.targetSetRot -= 19;
          this.currentSetting -= 1;
          if (this.currentSetting < 0)
          {
            this.targetSetRot = (65356 + (getSettings(this.selectedModule).size() - 1) * 19);
            this.currentSetting = (getSettings(this.selectedModule).size() - 1);
          }
        }
        else if (ek.getKey() == 205)
        {
          Setting set = (Setting)getSettings(this.selectedModule).get(this.currentSetting);
          if ((set.getValue() instanceof Number))
          {
            this.inSet = true;
          }
          else if (set.getValue().getClass().equals(Boolean.class))
          {
            boolean xd = ((Boolean)set.getValue()).booleanValue();
            set.setValue(Boolean.valueOf(!xd));
            Module.saveSettings();
          }
        }
      }
      else if (this.inSet)
      {
        if (ek.getKey() == 203)
        {
          this.inSet = false;
        }
        else if (ek.getKey() == 200)
        {
          Setting set = (Setting)getSettings(this.selectedModule).get(this.currentSetting);
          if ((set.getValue() instanceof Number))
          {
            double increment = set.getInc() != 0.0D ? set.getInc() : 0.5D;
            String str = MathUtils.roundToPlace(((Number)set.getValue()).doubleValue() + increment, 1) + "";
            if ((Double.parseDouble(str) > set.getMax()) && (set.getInc() != 0.0D)) {
              return;
            }
            Object newValue = StringConversions.castNumber(str, set.getValue());
            if (newValue != null)
            {
              set.setValue(newValue);
              Module.saveSettings();
              return;
            }
          }
        }
        else if (ek.getKey() == 208)
        {
          Setting set = (Setting)getSettings(this.selectedModule).get(this.currentSetting);
          if ((set.getValue() instanceof Number))
          {
            double increment = set.getInc() != 0.0D ? set.getInc() : 0.5D;
            String str = MathUtils.roundToPlace(((Number)set.getValue()).doubleValue() - increment, 1) + "";
            if ((Double.parseDouble(str) < set.getMin()) && (set.getInc() != 0.0D)) {
              return;
            }
            Object newValue = StringConversions.castNumber(str, set.getValue());
            if (newValue != null)
            {
              set.setValue(newValue);
              Module.saveSettings();
              return;
            }
          }
        }
      }
    }
  }
  
  private void animate()
  {
    int diffType = this.targetTypeRot - this.typeRot;
    this.typeRot += (int)(diffType * 0.1D);
    if (diffType > 0) {
      this.typeRot += 1;
    } else if (diffType < 0) {
      this.typeRot -= 1;
    }
    int diffMod = this.targetModRot - this.modRot;
    this.modRot += (int)(diffMod * 0.15D);
    if (diffMod > 0) {
      this.modRot += 1;
    } else if (diffMod < 0) {
      this.modRot -= 1;
    }
    int diffSetMod = this.targetSetRot - this.setRot;
    this.setRot += (int)(diffSetMod * 0.2D);
    if (diffSetMod > 0) {
      this.setRot += 1;
    } else if (diffSetMod < 0) {
      this.setRot -= 1;
    }
    int diffHidden = this.targetX - this.hiddenX;
    this.hiddenX += diffHidden / 9;
    if (diffHidden > 0) {
      this.hiddenX += 1;
    } else if (diffHidden < 0) {
      this.hiddenX -= 1;
    }
    int diffModX = this.targetModX - this.modX;
    this.modX += diffModX / 7;
    if (diffModX > 0) {
      this.modX += 1;
    } else if (diffModX < 0) {
      this.modX -= 1;
    }
    int diffSetModX = this.targetModSetX - this.setModX;
    this.setModX += diffSetModX / 7;
    if (diffSetModX > 0) {
      this.setModX += 1;
    } else if (diffSetModX < 0) {
      this.setModX -= 1;
    }
    int diffSetX = this.targetSetX - this.setX;
    this.setX += diffSetX / 7;
    if (diffSetX > 0) {
      this.setX += 1;
    } else if (diffSetX < 0) {
      this.setX -= 1;
    }
    int opacityDiff = this.targetOpacity - this.opacity;
    this.opacity = ((int)(this.opacity + opacityDiff * 0.25D));
  }
  
  private boolean keyCheck(int key)
  {
    boolean active = false;
    switch (key)
    {
    case 208: 
      active = true;
      break;
    case 200: 
      active = true;
      break;
    case 28: 
      active = true;
      break;
    case 203: 
      if (!this.inModules) {
        active = true;
      }
      break;
    case 205: 
      active = true;
      break;
    }
    return active;
  }
  
  private List<Setting> getSettings(Module mod)
  {
    List<Setting> settings = new ArrayList();
    for (Setting set : mod.getSettings().values()) {
      settings.add(set);
    }
    if (settings.isEmpty()) {
      return null;
    }
    return settings;
  }
  
  private int getAlpha(int rotation, float multiplier)
  {
    float dist = RotationUtils.getDistanceBetweenAngles(-180.0F, rotation);
    int alpha = (int)Math.abs(dist * multiplier / 2.5D * 0.009999999776482582D * 0.0D + (1.0D - dist * multiplier / 2.5D * 0.009999999776482582D) * 255.0D);
    if (dist > 90.0F) {
      alpha = 20;
    }
    return alpha;
  }
  
  private List<Module> getModules(ModuleData.Type type) {
      ArrayList<Module> modulesInType = new ArrayList<Module>();
      for (Module mod : Client.getModuleManager().getArray()) {
          if (mod.getType() != type) continue;
          modulesInType.add(mod);
          int width = 0;
          if (BubbleGui.mc.fontRendererObj.getStringWidth(mod.getName()) <= width) continue;
          width = BubbleGui.mc.fontRendererObj.getStringWidth(mod.getName());
      }
      if (modulesInType.isEmpty()) {
          return null;
      }
      modulesInType.sort(new Comparator<Module>(){

          @Override
          public int compare(Module m1, Module m2) {
              return m1.getName().compareTo(m2.getName());
          }
      });
      return modulesInType;
  }

}

