package me.aidanmees.trivia.client.alts.New;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.alts.Login;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.GuiPasswordField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.CustomButton.GuiButtonDark;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiAltManager
extends GuiScreen {
    private GuiScreen prevMenu;
    public static GuiTextField theName;
    public static GuiPasswordField thePassword;
    public static GuiTextField theCombo;
    public static String loginstats = "Waiting...";
   
    FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 23));

    public GuiAltManager(GuiScreen parent) {
        this.prevMenu = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        theName = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 100, height / 4 , 200, 20);
        theName.setMaxStringLength(254);
        thePassword = new GuiPasswordField(0, this.fontRendererObj, width / 2 - 100, height / 4 + 50, 200, 20);
        thePassword.setMaxStringLength(254);
        theCombo = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 100, height / 4 + 100, 200, 20);
        theCombo.setMaxStringLength(254);
        this.buttonList.add(new GuiButtonDark(0, width / 2 - 101, height / 4 + 156,202,20, "Login"));
        this.buttonList.add(new GuiButtonDark(2, width / 2 - 101, height / 4 + 176,202,20, "Clipboard login"));
        this.buttonList.add(new GuiButtonDark(1, width / 2 - 101, height / 4 + 196,202,20, I18n.format("Back", new Object[0])));
       
        
        Keyboard.enableRepeatEvents((boolean)true);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
        	if ((thePassword.getText() == "" || thePassword.getText() == " ")&& !theCombo.getText().contains(":") && theCombo.getText().equals("")) {
        		System.out.println(thePassword.getText() + " <--- Password");
        		try {
        		Login.changeName(theName.getText());
        		loginstats = "Successfully logged in!";
        		}
        		catch (Exception E){
        			loginstats = "Failed logging in!";
        		}
        		
        	}
        
        
            if (theCombo.getText().contains(":")) {
            	try{
                String s = theCombo.getText();
                String nm = s.split(":")[0];
                String pw = s.split(":")[1];
                AltHelper.login(nm, pw);
                loginstats = "Successfully logged in!";
            	}
               catch(Exception e){
            	   loginstats = "Failed logged in!"; 
               }
            }
          if (thePassword.getText() != "" && theName.getText() != "") {
            	try {
                AltHelper.login(theName.getText(), thePassword.getText());
            	}
            	catch (Exception e) {
            		loginstats = "Successfully logged in!";
            	}
            
          }
        }
    
    
        
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(prevMenu);
        }
        if (button.id == 2) {
        	try {
				String data = (String) Toolkit.getDefaultToolkit()
				        .getSystemClipboard().getData(DataFlavor.stringFlavor);
				String[] split = data.split(":");
				 AltHelper.login(split[0], split[1]);
				 loginstats = "Successfully logged in!";
			} catch (Exception e) {
				loginstats = "Failed logged in!";
				e.printStackTrace();
			
			}
        }
        }
    @Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		theName.textboxKeyTyped(typedChar, keyCode);
		thePassword.textboxKeyTyped(typedChar, keyCode);
		theCombo.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		theName.mouseClicked(mouseX, mouseY, mouseButton);
		thePassword.mouseClicked(mouseX, mouseY, mouseButton);
		theCombo.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		
		theName.updateCursorCounter();
		
		
		thePassword.updateCursorCounter();
		
		theCombo.updateCursorCounter();
		
		super.updateScreen();
	}
    
    
    
    
    
    
    

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	

        this.mc.getTextureManager().bindTexture(trivia.triviaImage2);
        this.drawTexturedModalRect(0, 0, 0, 0, width, height);
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GuiAltManager.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, width, height, width, height, width, height);
        GuiAltManager.drawRect(width / 2 - 120, 0.0, width / 2 + 120, height, Integer.MIN_VALUE);
       
        
            
        
        theName.drawTextBox();
        thePassword.drawTextBox();
        theCombo.drawTextBox();
        
        Minecraft.getMinecraft().fontRendererObj.drawString("Username", sr.getScaledWidth() / 2- 25, height / 4 - 17, 0x0ffffff);
        
        
        Minecraft.getMinecraft().fontRendererObj.drawString("Password", sr.getScaledWidth() / 2 - 25, height / 4 + 34, 0x0ffffff);
  
        Minecraft.getMinecraft().fontRendererObj.drawString("Email:Password", sr.getScaledWidth() / 2- 35, height / 4 + 84, 0x0ffffff);
    

        
        int var3 = this.height / 4 + 48;
        String STats = mc.session.getToken().equals("") ? "Cracked" : "Premium";
        String SessiongText = "" + this.mc.session.getUsername() + " (" + STats+")";
       
        fontRenderer.drawString(SessiongText, width / 500, height / 500, 0xFFFFFFFF);
        int meme;
        if (loginstats.equals("Successfully logged in!")) {
        	meme = Color.green.getRGB();
        }
        else {
        	meme = Color.red.getRGB();
        }
        fontRenderer.drawString(loginstats, width - fontRenderer.getStringWidth(loginstats) - 3, height / 500, meme);
      
      
    	     
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

