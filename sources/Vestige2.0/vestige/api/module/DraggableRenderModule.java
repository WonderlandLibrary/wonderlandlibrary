package vestige.api.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

@Getter
@Setter
public abstract class DraggableRenderModule extends Module {
	
	protected float x, y, width, height;
	private boolean isMouseHolding;
	
	public DraggableRenderModule() {
		this.initialisePositions();
	}
	
	public abstract void initialisePositions();
	
	public void onChatRender(int mouseX, int mouseY, float partialTicks) {
		FontRenderer fr = mc.fontRendererObj;
		
		Gui.drawRect(x, y, x + width, y + height, 0x90000000);
		
		String name = this.getName();
		double length = fr.getStringWidth(name);
		
		fr.drawStringWithShadow(name, (float) (x + width / 2 - length / 2), (float) (y + height / 2 - fr.FONT_HEIGHT / 2), -1);
	}
	
}
