package sudo.ui.screens.clickgui.setting;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.module.settings.KeybindSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;
import sudo.utils.text.KeyUtils;

public class Keybind extends Component {

	private KeybindSetting binding = (KeybindSetting)setting;
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	public boolean isBinding = false;

	public Keybind(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0) {
			binding.toggle();
			isBinding = true;
		}
		
		if (isHovered(mouseX, mouseY) && isBinding==true && button == 1) {
			binding.toggle();
			isBinding=false;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void keyPressed(int key) {
		if (isBinding == true) {
			parent.module.setKey(key);
			binding.setKey(key);
			isBinding = false;
		}
		if ((binding.getKey() == 256)) {
			parent.module.setKey(0);
			binding.setKey(0);
			isBinding = false;
		}
		if ((binding.getKey() == 259)) {
			parent.module.setKey(0);
			binding.setKey(0);
			isBinding = false;
		}
		super.keyPressed(key);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y+parent.offset+offset+parent.parent.height, 0xff1f1f1f);
		DrawableHelper.fill(matrices, parent.parent.x+2, parent.parent.y + parent.offset + offset, parent.parent.x+4, parent.parent.y+parent.offset+offset+parent.parent.height, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB());
		
		if (isBinding==false) {
			textRend.drawString(matrices, "Bind: ", parent.parent.x + 5, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);
			parent.parent.updateButton();
			textRend.drawString(matrices, KeyUtils.NumToKey(binding.getKey()), parent.parent.x+parent.parent.width-textRend.getStringWidth(KeyUtils.NumToKey(binding.getKey()))-2, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);
		}
		if (isBinding==true) textRend.drawString(matrices, "Binding...", parent.parent.x + 5, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);

		
		super.render(matrices, mouseX, mouseY, delta);
	}
}