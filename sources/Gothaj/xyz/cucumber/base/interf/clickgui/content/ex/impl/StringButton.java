package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class StringButton extends SettingsButton{
	
	private StringSettings setting;
	
	private GuiTextField textField;
	
	private double animation;

	public StringButton(StringSettings setting, PositionUtils pos) {
		this.settingMain =setting;
		this.setting = setting;
		this.position = pos;
		textField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, 0, 0, 100, 12);
		textField.setText(setting.getString());
	}
	
	

	@Override
	public void draw(int mouseX, int mouseY) {

		Fonts.getFont("rb-r").drawString(setting.getName(), this.position.getX()+8, this.position.getY()+3, -1);
		
		textField.xPosition = (int) (this.position.getX2()-5-textField.width);
		textField.yPosition = (int) (this.position.getY()+1);
		
		RenderUtils.drawRoundedRect(textField.xPosition, textField.yPosition, textField.xPosition+textField.width, textField.yPosition+textField.height, 0xff191919, 1);
		if(this.textField.isFocused()) {
			animation = (animation * 9 + textField.width/2-3) /10;
			
		}else {
			animation = (animation * 9) /10;
		}
		RenderUtils.drawLine(textField.xPosition+textField.width/2-animation, textField.yPosition+textField.height-1, textField.xPosition+textField.width/2+animation, textField.yPosition+textField.height-1,  0xff4269f5, 1);
		String render = textField.getText();
		if(textField.getText().equals("") || textField.getText() == null) {
			render = "Add your text";
		}else {
			setting.setString(render);
		}
		StencilUtils.initStencil();
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        StencilUtils.bindWriteStencilBuffer();
        RenderUtils.drawRoundedRect(textField.xPosition, textField.yPosition, textField.xPosition+textField.width, textField.yPosition+textField.height, 0xff191919, 1);
		
		StencilUtils.bindReadStencilBuffer(1);
		Fonts.getFont("rb-r").drawString(render, textField.xPosition+textField.width/2-Fonts.getFont("rb-r").getWidth(render)/2, textField.yPosition+textField.height/2-Fonts.getFont("rb-r").getHeight(render)/2, 0xffaaaaaa);
        StencilUtils.uninitStencilBuffer();
		
	}



	@Override
	public void click(int mouseX, int mouseY, int button) {
		 this.textField.mouseClicked(mouseX, mouseY, button);
	}
	
	


	@Override
	public void key(char character, int key) {
		if (character == '\t')
        {
            if (!this.textField.isFocused())
            {
            	this.textField.setFocused(true);
            }
        }
        this.textField.textboxKeyTyped(character, key);
	}



	public StringSettings getSetting() {
		return setting;
	}

	public void setSetting(StringSettings setting) {
		this.setting = setting;
	}
	
}
