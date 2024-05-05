package optifine;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiAnimationSettingsOF
  extends GuiScreen
{
  private GuiScreen prevScreen;
  protected String title;
  private GameSettings settings;
  private static GameSettings.Options[] enumOptions = { GameSettings.Options.ANIMATED_WATER, GameSettings.Options.ANIMATED_LAVA, GameSettings.Options.ANIMATED_FIRE, GameSettings.Options.ANIMATED_PORTAL, GameSettings.Options.ANIMATED_REDSTONE, GameSettings.Options.ANIMATED_EXPLOSION, GameSettings.Options.ANIMATED_FLAME, GameSettings.Options.ANIMATED_SMOKE, GameSettings.Options.VOID_PARTICLES, GameSettings.Options.WATER_PARTICLES, GameSettings.Options.RAIN_SPLASH, GameSettings.Options.PORTAL_PARTICLES, GameSettings.Options.POTION_PARTICLES, GameSettings.Options.DRIPPING_WATER_LAVA, GameSettings.Options.ANIMATED_TERRAIN, GameSettings.Options.ANIMATED_TEXTURES, GameSettings.Options.FIREWORK_PARTICLES, GameSettings.Options.PARTICLES };
  
  public GuiAnimationSettingsOF(GuiScreen guiscreen, GameSettings gamesettings)
  {
    this.prevScreen = guiscreen;
    this.settings = gamesettings;
  }
  
  public void initGui()
  {
    this.title = I18n.format("of.options.animationsTitle", new Object[0]);
    this.buttonList.clear();
    for (int i = 0; i < enumOptions.length; i++)
    {
      GameSettings.Options opt = enumOptions[i];
      int x = width / 2 - 155 + i % 2 * 160;
      int y = height / 6 + 21 * (i / 2) - 12;
      if (!opt.getEnumFloat()) {
        this.buttonList.add(new GuiOptionButtonOF(opt.returnEnumOrdinal(), x, y, opt, this.settings.getKeyBinding(opt)));
      } else {
        this.buttonList.add(new GuiOptionSliderOF(opt.returnEnumOrdinal(), x, y, opt));
      }
    }
    this.buttonList.add(new GuiButton(210, width / 2 - 155, height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOn")));
    this.buttonList.add(new GuiButton(211, width / 2 - 155 + 80, height / 6 + 168 + 11, 70, 20, Lang.get("of.options.animation.allOff")));
    this.buttonList.add(new GuiOptionButton(200, width / 2 + 5, height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
  }
  
  protected void actionPerformed(GuiButton guibutton)
  {
    if (guibutton.enabled)
    {
      if ((guibutton.id < 200) && ((guibutton instanceof GuiOptionButton)))
      {
        this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
        guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
      }
      if (guibutton.id == 200)
      {
        this.mc.gameSettings.saveOptions();
        this.mc.displayGuiScreen(this.prevScreen);
      }
      if (guibutton.id == 210) {
        this.mc.gameSettings.setAllAnimations(true);
      }
      if (guibutton.id == 211) {
        this.mc.gameSettings.setAllAnimations(false);
      }
      ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      setWorldAndResolution(this.mc, sr.getScaledWidth(), sr.getScaledHeight());
    }
  }
  
  public void drawScreen(int x, int y, float f)
  {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, this.title, width / 2, 15, 16777215);
    super.drawScreen(x, y, f);
  }
}
