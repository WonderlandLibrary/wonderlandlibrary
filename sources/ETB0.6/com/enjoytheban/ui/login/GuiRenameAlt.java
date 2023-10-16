package com.enjoytheban.ui.login;

import java.io.IOException;

import com.enjoytheban.utils.Helper;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class GuiRenameAlt
        extends GuiScreen
{
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private String status = "§eWaiting...";
    private GuiPasswordField pwField;
    public GuiRenameAlt(GuiAltManager manager)
    {
        this.manager = manager;
    }

    public void actionPerformed(GuiButton button)
    {
      switch (button.id)
      {
      case 1: 
        mc.displayGuiScreen(manager);
        break;
      case 0: 
        manager.selectedAlt.setMask(nameField.getText());
        if(!pwField.getText().isEmpty())
          manager.selectedAlt.setPassword(pwField.getText());
        status = "§aEdited!";
      }
    }
    
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        Helper.mc.fontRendererObj.drawCenteredString("Edit Alt", width / 2, 10, -1);
		Helper.mc.fontRendererObj.drawCenteredString(status, width / 2, 20, -1);
      nameField.drawTextBox();
      pwField.drawTextBox();
      if (nameField.getText().isEmpty()) 
    	  Helper.mc.fontRendererObj.drawStringWithShadow("New E-Mail", width / 2 - 96, 66, -7829368);
      if (pwField.getText().isEmpty()) 
          Helper.mc.fontRendererObj.drawStringWithShadow("New Password", width / 2 - 96, 106, -7829368);
      super.drawScreen(par1, par2, par3);
    }
    
    public void initGui()
    {
      buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
      buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
      nameField = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
      pwField = new GuiPasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
    }
    
    protected void keyTyped(char par1, int par2)
    {
  	  nameField.textboxKeyTyped(par1, par2);
  	    pwField.textboxKeyTyped(par1, par2);
  	    if ((par1 == '\t') && ((nameField.isFocused()) || (pwField.isFocused())))
  	    {
  	    	nameField.setFocused(!nameField.isFocused());
  	    	pwField.setFocused(!pwField.isFocused());
  	    }
  	    if (par1 == '\r') {
  	      actionPerformed((GuiButton)buttonList.get(0));
  	    }
    }
    
    protected void mouseClicked(int par1, int par2, int par3)
    {
      try
      {
        super.mouseClicked(par1, par2, par3);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      nameField.mouseClicked(par1, par2, par3);
      pwField.mouseClicked(par1, par2, par3);
    }
  }