package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownClickGuiString extends DropdownClickGuiSettings{
	
	private StringSettings setting;
	
	private GuiTextField field;

	public DropdownClickGuiString(ModuleSettings setting, PositionUtils position) {
		this.setting = (StringSettings) setting;
		this.position = position;
		field = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, 0, 0, 60, 10);
		field.setText(this.setting.getString());
		this.mainSetting =setting;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		field.xPosition = (int) this.position.getX2()-60;
		field.yPosition = (int) this.position.getY()+1;
		Fonts.getFont("rb-m-13").drawString(this.setting.getName(),this.position.getX()+3, field.yPosition+3, -1);
		RenderUtils.drawRoundedRectWithCorners(field.xPosition,field.yPosition, field.xPosition+field.width, field.yPosition+field.height, 0x60000000,2, true,true,true,true);
		this.setting.setString(this.field.getText());
		Fonts.getFont("rb-r-13").drawString(this.setting.getString(),field.xPosition, field.yPosition+3, -1);
		
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		field.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void onKey(char chr, int key) {
		if (chr == '\t')
        {
            if (!this.field.isFocused())
            {
                this.field.setFocused(true);
            }
        }
		field.textboxKeyTyped(chr, key);
	}

}
