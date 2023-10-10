package Reality.Realii.guis.clickgui.component.components.sub;

import java.awt.*;
import java.util.Arrays;

import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

//Your Imports
import Reality.Realii.guis.clickgui.component.Component;
import Reality.Realii.guis.clickgui.component.components.Button;
import Reality.Realii.mods.Module;

public class ModeButton extends Component {

	private boolean hovered;
	private Button parent;
	private Value set;
	private int offset;
	
	private int x;
	private int y;
	private Module mod;

	private int modeIndex;
	
	public ModeButton(Value set, Button button, Module mod, int offset) {
		this.set = set;
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {

			int i = 0;
			Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
			Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
			Pair<Color, Color> colors = Pair.of(startColor, endColor);
			Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
		//RenderUtil.drawBorderedRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12,1, c.getRGB(), this.hovered ? 0xFF222222 : 0xFF111111);
		Gui.drawRect(parent.parent.getX() + 0.5, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);

		//Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(set.getDisplayName() +": " + set.getValue(), (parent.parent.getX() + 7) * 2, (parent.parent.getY() + offset + 2) * 2 + 5, -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
	
		//	int maxIndex = set.getValue().toString().length();

		//	if(modeIndex > maxIndex)
		//		modeIndex = 0;
		//	else
			//	modeIndex++;

		//	set.setValue(set.getValue().toString().(maxIndex));
		
			
		//Mode<?> modeValue = (Mode<?>) set;
			//int index = Arrays.binarySearch(modeValue.getModes(), set.getValue());
		//	if (index < 0) {
			 //   index = -index - set.getValue().toString().length();
		//	}
			
			
	//	if (index < modeValue.getModes().length - 1) {
		//	    set.setValue(modeValue.getModes()[index + 1]);
		//	} else {
			  //  set.setValue(modeValue.getModes()[0]);
		//}

			int maxIndex = ((Mode) set).getModes().length;

			if (modeIndex < maxIndex - 1)
				modeIndex += 1;
			else
				modeIndex = 0;

			((Mode) set).setValue(((Mode) set).getModes() [modeIndex]);

			//set.setValue((modeValue.getModes()[0]));
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.open) {
			int maxIndex = ((Mode) set).getModes().length;

			if (modeIndex > 0)
				modeIndex += 1;
			else
				modeIndex = maxIndex - 1;

			((Mode) set).setValue(((Mode) set).getModes() [modeIndex]);

		}

	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
