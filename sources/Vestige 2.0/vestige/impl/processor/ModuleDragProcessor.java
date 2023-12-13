package vestige.impl.processor;

import vestige.Vestige;
import vestige.api.module.DraggableRenderModule;
import vestige.util.base.IMinecraft;

public class ModuleDragProcessor implements IMinecraft {
	
	private int lastMouseX, lastMouseY;
	private int mouseDiffX, mouseDiffY;
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {	
		mouseDiffX = mouseX - lastMouseX;
		mouseDiffY = mouseY - lastMouseY;
		
		for(DraggableRenderModule m : Vestige.getInstance().getModuleManager().getDraggableRenderModules()) {
			if(m.isMouseHolding()) {
				m.setX(m.getX() + mouseDiffX);
				m.setY(m.getY() + mouseDiffY);
			}
			
			m.onChatRender(mouseX, mouseY, partialTicks);
		}
		
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		//Vestige.addChatMessage("ok " + mouseX + " " + mouseY);
		for(DraggableRenderModule m : Vestige.getInstance().getModuleManager().getDraggableRenderModules()) {
			float startX = m.getX();
			float startY = m.getY();
			float endX = m.getX() + m.getWidth();
			float endY = m.getY() + m.getY();
			
			if(mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
				m.setMouseHolding(true);
				break;
			}
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		Vestige.getInstance().getModuleManager().getDraggableRenderModules().forEach(m -> m.setMouseHolding(false));
    }
	
	public void onChatClosed() {
		Vestige.getInstance().getModuleManager().getDraggableRenderModules().forEach(m -> m.setMouseHolding(false));
	}
	
}
