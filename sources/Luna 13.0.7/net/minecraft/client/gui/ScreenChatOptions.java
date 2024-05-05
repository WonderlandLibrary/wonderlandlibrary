package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;

public class ScreenChatOptions
  extends GuiScreen
{
  private static final GameSettings.Options[] field_146399_a = { GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO };
  private final GuiScreen field_146396_g;
  private final GameSettings game_settings;
  private String field_146401_i;
  private String field_146398_r;
  private int field_146397_s;
  private static final String __OBFID = "CL_00000681";
  
  public ScreenChatOptions(GuiScreen p_i1023_1_, GameSettings p_i1023_2_)
  {
    this.field_146396_g = p_i1023_1_;
    this.game_settings = p_i1023_2_;
  }
  
  public void initGui()
  {
    int var1 = 0;
    this.field_146401_i = I18n.format("options.chat.title", new Object[0]);
    this.field_146398_r = I18n.format("options.multiplayer.title", new Object[0]);
    GameSettings.Options[] var2 = field_146399_a;
    int var3 = var2.length;
    for (int var4 = 0; var4 < var3; var4++)
    {
      GameSettings.Options var5 = var2[var4];
      if (var5.getEnumFloat()) {
        this.buttonList.add(new GuiOptionSlider(var5.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var5));
      } else {
        this.buttonList.add(new GuiOptionButton(var5.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, height / 6 + 24 * (var1 >> 1), var5, this.game_settings.getKeyBinding(var5)));
      }
      var1++;
    }
    if (var1 % 2 == 1) {
      var1++;
    }
    this.field_146397_s = (height / 6 + 24 * (var1 >> 1));
    this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 120, I18n.format("gui.done", new Object[0])));
  }
  
  protected void actionPerformed(GuiButton button)
    throws IOException
  {
    if (button.enabled)
    {
      if ((button.id < 100) && ((button instanceof GuiOptionButton)))
      {
        this.game_settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
        button.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
      }
      if (button.id == 200)
      {
        this.mc.gameSettings.saveOptions();
        this.mc.displayGuiScreen(this.field_146396_g);
      }
    }
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, this.field_146401_i, width / 2, 20, 16777215);
    drawCenteredString(this.fontRendererObj, this.field_146398_r, width / 2, this.field_146397_s + 7, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}
