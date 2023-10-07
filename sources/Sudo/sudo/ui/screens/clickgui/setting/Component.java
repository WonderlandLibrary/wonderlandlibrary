package sudo.ui.screens.clickgui.setting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;

public class Component {

	public Setting setting;
	public ModuleButton parent;
	public int offset;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();

	public Component(Setting setting, ModuleButton parent, int offset) {
		this.setting = setting;
		this.parent = parent;
		this.offset = offset;
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		this.offset = offset;
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.parent.x && mouseX < parent.parent.x + parent.parent.width && mouseY > parent.parent.y + parent.offset + offset && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
	}

	public void keyPressed(int key) {
	}
	
}