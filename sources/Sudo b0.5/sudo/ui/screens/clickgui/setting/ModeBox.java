package sudo.ui.screens.clickgui.setting;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class ModeBox extends Component{
	
	private ModeSetting modeSet = (ModeSetting)setting;
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;

	public ModeBox(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.modeSet = (ModeSetting)setting;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y+parent.offset+offset+parent.parent.height, 0xff1f1f1f );
		DrawableHelper.fill(matrices, parent.parent.x+2, parent.parent.y + parent.offset + offset, parent.parent.x+4, parent.parent.y+parent.offset+offset+parent.parent.height, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB() );

		textRend.drawString(matrices, modeSet.getName() + ": " + modeSet.getMode(), parent.parent.x + 5, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);
//		textRend.drawString(matrices,
//				modeSet.getMode(),
//				parent.parent.x + parent.parent.width - mc.textRenderer.getWidth(modeSet.getMode())-4,
//				parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3,
//				0xff8B8B8B,1);
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY) && button == 0) modeSet.cycle();
		super.mouseClicked(mouseX, mouseY, button);
	}
}
