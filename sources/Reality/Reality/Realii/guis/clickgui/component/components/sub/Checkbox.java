package Reality.Realii.guis.clickgui.component.components.sub;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import Reality.Realii.event.value.Value;
import Reality.Realii.guis.clickgui.component.Component;
import Reality.Realii.guis.clickgui.component.components.Button;
import java.awt.Color;
import java.awt.*;


public class Checkbox extends Component {

	private boolean hovered;
	private Value op;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	
	public Checkbox(Value option, Button button, int offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent() {
		int i = 0;
		Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
		Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
		Pair<Color, Color> colors = Pair.of(startColor, endColor);
		Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.op.getName(), (parent.parent.getX() + 10 + 4) * 2 + 5, (parent.parent.getY() + offset + 2) * 2 + 4, -1);
		GL11.glPopMatrix();
	      if (((Boolean) op.getValue())) {
			  RenderUtil.drawBorderedRect(parent.parent.getX() + 4 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 8,0.5f, c.getRGB(),c.getRGB());
	      } else {
			  RenderUtil.drawBorderedRect(parent.parent.getX() + 4 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 8,0.5f, c.getRGB(),new Color(20,20,20).getRGB());

	      }
	      
		//Gui.drawRect(parent.parent.getX() + 3 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 9, 0xFF999999);
		//if(this.op.getName() != null)
			//
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
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
			//this.op.setValue(this.op.getName() != null);;
			   op.setValue(!(Boolean) op.getValue());
			
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
