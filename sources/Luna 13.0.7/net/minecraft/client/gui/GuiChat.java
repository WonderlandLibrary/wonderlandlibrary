package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat
  extends GuiScreen
{
  private static final Logger logger = ;
  private String historyBuffer = "";
  private int sentHistoryCursor = -1;
  private boolean playerNamesFound;
  private boolean waitingOnAutocomplete;
  private int autocompleteIndex;
  private List foundPlayerNames = Lists.newArrayList();
  protected GuiTextField inputField;
  private String defaultInputFieldText = "";
  private static final String __OBFID = "CL_00000682";
  
  public GuiChat() {}
  
  public GuiChat(String p_i1024_1_)
  {
    this.defaultInputFieldText = p_i1024_1_;
  }
  
  public void initGui()
  {
    Keyboard.enableRepeatEvents(true);
    this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
    this.inputField = new GuiTextField(0, this.fontRendererObj, 4, height - 12, width - 4, 12);
    this.inputField.setMaxStringLength(100);
    this.inputField.setEnableBackgroundDrawing(false);
    this.inputField.setFocused(true);
    this.inputField.setText(this.defaultInputFieldText);
    this.inputField.setCanLoseFocus(false);
  }
  
  public void onGuiClosed()
  {
    Keyboard.enableRepeatEvents(false);
    this.mc.ingameGUI.getChatGUI().resetScroll();
  }
  
  public void updateScreen()
  {
    this.inputField.updateCursorCounter();
  }
  
  protected void keyTyped(char typedChar, int keyCode)
    throws IOException
  {
    this.waitingOnAutocomplete = false;
    if (keyCode == 15) {
      autocompletePlayerNames();
    } else {
      this.playerNamesFound = false;
    }
    if (keyCode == 1)
    {
      this.mc.displayGuiScreen(null);
    }
    else if ((keyCode != 28) && (keyCode != 156))
    {
      if (keyCode == 200) {
        getSentHistory(-1);
      } else if (keyCode == 208) {
        getSentHistory(1);
      } else if (keyCode == 201) {
        this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
      } else if (keyCode == 209) {
        this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
      } else {
        this.inputField.textboxKeyTyped(typedChar, keyCode);
      }
    }
    else
    {
      String var3 = this.inputField.getText().trim();
      if (var3.length() > 0) {
        func_175275_f(var3);
      }
      this.mc.displayGuiScreen(null);
    }
  }
  
  public void handleMouseInput()
    throws IOException
  {
    super.handleMouseInput();
    int var1 = Mouse.getEventDWheel();
    if (var1 != 0)
    {
      if (var1 > 1) {
        var1 = 1;
      }
      if (var1 < -1) {
        var1 = -1;
      }
      if (!isShiftKeyDown()) {
        var1 *= 7;
      }
      this.mc.ingameGUI.getChatGUI().scroll(var1);
    }
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    throws IOException
  {
    if (mouseButton == 0)
    {
      IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
      if (func_175276_a(var4)) {
        return;
      }
    }
    this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  protected void func_175274_a(String p_175274_1_, boolean p_175274_2_)
  {
    if (p_175274_2_) {
      this.inputField.setText(p_175274_1_);
    } else {
      this.inputField.writeText(p_175274_1_);
    }
  }
  
  public void autocompletePlayerNames()
  {
    if (this.playerNamesFound)
    {
      this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
      if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
        this.autocompleteIndex = 0;
      }
    }
    else
    {
      int var1 = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
      this.foundPlayerNames.clear();
      this.autocompleteIndex = 0;
      String var2 = this.inputField.getText().substring(var1).toLowerCase();
      String var3 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
      sendAutocompleteRequest(var3, var2);
      if (this.foundPlayerNames.isEmpty()) {
        return;
      }
      this.playerNamesFound = true;
      this.inputField.deleteFromCursor(var1 - this.inputField.getCursorPosition());
    }
    if (this.foundPlayerNames.size() > 1)
    {
      StringBuilder var4 = new StringBuilder();
      String var3;
      for (Iterator var5 = this.foundPlayerNames.iterator(); var5.hasNext(); var4.append(var3))
      {
        var3 = (String)var5.next();
        if (var4.length() > 0) {
          var4.append(", ");
        }
      }
      this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(var4.toString()), 1);
    }
    this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
  }
  
  private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_)
  {
    if (p_146405_1_.length() >= 1)
    {
      BlockPos var3 = null;
      if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)) {
        var3 = this.mc.objectMouseOver.func_178782_a();
      }
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, var3));
      this.waitingOnAutocomplete = true;
    }
  }
  
  public void getSentHistory(int p_146402_1_)
  {
    int var2 = this.sentHistoryCursor + p_146402_1_;
    int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
    var2 = MathHelper.clamp_int(var2, 0, var3);
    if (var2 != this.sentHistoryCursor) {
      if (var2 == var3)
      {
        this.sentHistoryCursor = var3;
        this.inputField.setText(this.historyBuffer);
      }
      else
      {
        if (this.sentHistoryCursor == var3) {
          this.historyBuffer = this.inputField.getText();
        }
        this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
        this.sentHistoryCursor = var2;
      }
    }
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    drawRect(2.0D, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
    this.inputField.drawTextBox();
    IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
    if ((var4 != null) && (var4.getChatStyle().getChatHoverEvent() != null)) {
      func_175272_a(var4, mouseX, mouseY);
    }
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public void onAutocompleteResponse(String[] p_146406_1_)
  {
    if (this.waitingOnAutocomplete)
    {
      this.playerNamesFound = false;
      this.foundPlayerNames.clear();
      int var3 = p_146406_1_.length;
      for (String var5 : p_146406_1_) {
        if (var5.length() > 0) {
          this.foundPlayerNames.add(var5);
        }
      }
      String var6 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
      String var7 = StringUtils.getCommonPrefix(p_146406_1_);
      if ((var7.length() > 0) && (!var6.equalsIgnoreCase(var7)))
      {
        this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
        this.inputField.writeText(var7);
      }
      else if (this.foundPlayerNames.size() > 0)
      {
        this.playerNamesFound = true;
        autocompletePlayerNames();
      }
    }
  }
  
  public boolean doesGuiPauseGame()
  {
    return false;
  }
}
