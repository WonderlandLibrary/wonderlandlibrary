package sudo.ui.screens.clickgui.setting;

import java.awt.Color;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;
import sudo.utils.render.RenderUtils;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class CheckBox extends Component {

	private BooleanSetting boolSet = (BooleanSetting)setting;
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;

	public CheckBox(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.boolSet = (BooleanSetting)setting;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y+parent.offset+offset+parent.parent.height, 0xff1f1f1f);
		DrawableHelper.fill(matrices, parent.parent.x+2, parent.parent.y + parent.offset + offset, parent.parent.x+4, parent.parent.y+parent.offset+offset+parent.parent.height, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB());
		int offsetY = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);

		if (boolSet.isEnabled()) {
			RenderUtils.renderRoundedQuad(matrices, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor(), (parent.parent.x + offsetY) + 88, (parent.parent.y + parent.offset + offset + offsetY) + 2, parent.parent.x + offsetY + 94, parent.parent.y + parent.offset + offset + offsetY + 8, 1, 100);
//			textRend.drawString(matrices, "x", parent.parent.width+0.5, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3.77, -1, 1);
		} else if (!boolSet.isEnabled()) {
			RenderUtils.renderRoundedQuad(matrices, new Color(84, 84, 84), (parent.parent.x + offsetY) + 88, (parent.parent.y + parent.offset + offset + offsetY) + 2, parent.parent.x + offsetY + 94, parent.parent.y + parent.offset + offset + offsetY + 8, 1, 100);
		}
		textRend.drawString(matrices, boolSet.getName(), parent.parent.x + 5, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0) {
			boolSet.toggle();
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
}