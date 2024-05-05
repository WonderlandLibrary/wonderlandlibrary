package optifine;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiPerformanceSettingsOF
  extends GuiScreen
{
  private GuiScreen prevScreen;
  protected String title;
  private GameSettings settings;
  private static GameSettings.Options[] enumOptions = { GameSettings.Options.SMOOTH_FPS, GameSettings.Options.SMOOTH_WORLD, GameSettings.Options.FAST_RENDER, GameSettings.Options.FAST_MATH, GameSettings.Options.CHUNK_UPDATES, GameSettings.Options.CHUNK_UPDATES_DYNAMIC, GameSettings.Options.LAZY_CHUNK_LOADING };
  private TooltipManager tooltipManager = new TooltipManager(this);
  
  public GuiPerformanceSettingsOF(GuiScreen guiscreen, GameSettings gamesettings)
  {
    this.prevScreen = guiscreen;
    this.settings = gamesettings;
  }
  
  public void initGui()
  {
    this.title = I18n.format("of.options.performanceTitle", new Object[0]);
    this.buttonList.clear();
    for (int i = 0; i < enumOptions.length; i++)
    {
      GameSettings.Options enumoptions = enumOptions[i];
      int x = width / 2 - 155 + i % 2 * 160;
      int y = height / 6 + 21 * (i / 2) - 12;
      if (!enumoptions.getEnumFloat()) {
        this.buttonList.add(new GuiOptionButtonOF(enumoptions.returnEnumOrdinal(), x, y, enumoptions, this.settings.getKeyBinding(enumoptions)));
      } else {
        this.buttonList.add(new GuiOptionSliderOF(enumoptions.returnEnumOrdinal(), x, y, enumoptions));
      }
    }
    this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
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
    }
  }
  
  public void drawScreen(int x, int y, float f)
  {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, this.title, width / 2, 15, 16777215);
    super.drawScreen(x, y, f);
    this.tooltipManager.drawTooltips(x, y, this.buttonList);
  }
}
