package Reality.Realii.guis.clickgui.component.components.sub;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import Reality.Realii.event.value.Numbers;
//Your Imports
import Reality.Realii.event.value.Value;
import Reality.Realii.guis.clickgui.component.Component;
import Reality.Realii.guis.clickgui.component.components.Button;


public class Slider extends Component {

	private boolean hovered;

	private Value set;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;
	
	public Slider(Value value, Button button, int offset) {
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
	//	 final double drag = set.getMin() / set.getMax() * this.parent.parent.getWidth();
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 12,hovered ? 0xFF555555 : 0xFF444444);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.set.getName() + ": " + this.set.getValue() , (parent.parent.getX()* 2 + 15), (parent.parent.getY() + offset + 2) * 2 + 5, -1);
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
		
		double diff = Math.min(88, Math.max(0, mouseX - this.x));

		double min = set.getMin();
		double max = set.getMax();
		
		renderWidth = (88) * (set.getMin() - min) / (max - min);
		
	
		  if (dragging) {
	            double reach = mouseX - (x + 5);
	            double percent = reach / 60f;
	            double val = (((Numbers<?>) set).getMaximum().doubleValue() - ((Numbers<?>) set).getMinimum().doubleValue()) * percent;
	            if (val > ((Numbers<?>) set).getMinimum().doubleValue() && val < ((Numbers<?>) set).getMaximum().doubleValue()) {
	                set.setValue((int)(val*10)/10F);
	            }
	            
	            if(val < ((Numbers<?>) set).getMinimum().doubleValue()){
	                set.setValue(((Numbers<?>) set).getMinimum());
	            }else if(val > ((Numbers<?>) set).getMaximum().doubleValue()){
	                set.setValue(((Numbers<?>) set).getMaximum());
	            }
	        }
	}
	
	
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
			
		}
		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		if(x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
	
	
	public boolean isMouseOnButtonI(int x, int y) {
		if(x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
