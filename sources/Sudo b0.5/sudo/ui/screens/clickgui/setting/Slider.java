package sudo.ui.screens.clickgui.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.module.settings.NumberSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class Slider extends Component {
	
	public NumberSetting numSet = (NumberSetting)setting;
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	private boolean sliding = false;
	
	public Slider(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.numSet = (NumberSetting)setting;
	}
	
	int renderWidthAnimation=0;
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y+parent.offset+offset+parent.parent.height, 0xff1f1f1f);
		DrawableHelper.fill(matrices, parent.parent.x+2, parent.parent.y + parent.offset + offset, parent.parent.x+4, (int) (parent.parent.y+parent.offset+offset+parent.parent.height-3.5), ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB());

		double diff = Math.min(parent.parent.width, Math.max(0, mouseX - parent.parent.x));
		int renderWidth = (int) (parent.parent.width * (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()));
		
		if (renderWidthAnimation<renderWidth)renderWidthAnimation++;
		if (renderWidthAnimation<renderWidth)renderWidthAnimation++;	//my eyes
		if (renderWidthAnimation<renderWidth)renderWidthAnimation++;
		if (renderWidthAnimation>renderWidth)renderWidthAnimation--;
		if (renderWidthAnimation>renderWidth)renderWidthAnimation--;
		if (renderWidthAnimation>renderWidth)renderWidthAnimation--;
		
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset+parent.parent.height-3, parent.parent.x + parent.parent.width-2, parent.parent.y+parent.offset+offset+parent.parent.height-1, 0xff545454);
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset+parent.parent.height-3, parent.parent.x + renderWidthAnimation, /*replace renderWidthAnimation with renderWidth if animation bugs*/ parent.parent.y+parent.offset+offset+parent.parent.height-1, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor().getRGB());
		if (sliding) {
			if (diff==0) {
				numSet.setValue(numSet.getMin());
			} else {
				numSet.setValue(roundToPlace((diff / parent.parent.width) * (numSet.getMax() - numSet.getMin()) + numSet.getMin(), 2));
			}
		}

		textRend.drawString(matrices, numSet.getName(), parent.parent.x + 5, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);
		textRend.drawString(matrices, ""+roundToPlace(numSet.getValue(), 1), parent.parent.x + parent.parent.width-mc.textRenderer.getWidth(""+roundToPlace(numSet.getValue(), 1))-4, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset-3, 0xff8B8B8B,1);

		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public int getHeight(int len) {
		return len - len / 4 - 1;
	}
	public int getWidth(int len) {
		return len - len / 4 - 1;
	}
	
	double easeInOutQuad(double x) {
        return (x < 0.5) ? 2 * x * x : 1 - Math.pow((-2 * x + 2), 2) / 2;
    }
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY)) sliding = true;
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		sliding = false;
		super.mouseReleased(mouseX, mouseY, button);
	}
	 
	private double roundToPlace(double value, int place) {
		if (place < 0) {
			return value;
		}
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(place, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
