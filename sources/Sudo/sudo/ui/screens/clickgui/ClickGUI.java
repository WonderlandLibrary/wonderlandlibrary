package sudo.ui.screens.clickgui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import sudo.module.Mod.Category;
import sudo.module.client.ClickGuiMod;
import sudo.module.ModuleManager;
import sudo.utils.render.RenderUtils;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class ClickGUI extends Screen {

	public static final ClickGUI INSTANCE = new ClickGUI();
	private static MinecraftClient mc = MinecraftClient.getInstance();
	public static GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	private List<Frame> frames;
	
	private ClickGUI() {
		super(Text.literal("Click GUI"));

		frames = new ArrayList<>();
		
		int offset = 70;
		for (Category category : Category.values()) {
			frames.add(new Frame(category, offset, 20, 100, 15));
			offset += 110;
		}
	}
	
	@Override
	public boolean shouldPause() {
		return ModuleManager.INSTANCE.getModule(ClickGuiMod.class).pause.isEnabled();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (ModuleManager.INSTANCE.getModule(ClickGuiMod.class).background.isEnabled()) DrawableHelper.fillGradient(matrices, 0, 0, mc.getWindow().getWidth(), mc.getWindow().getHeight(), 0x35f803ff, 0x60ff03af, 0);
		if (ModuleManager.INSTANCE.getModule(ClickGuiMod.class).blur.isEnabled()) {
			RenderUtils.blur(matrices, 0, 0, mc.getWindow().getScaledWidth()*4, mc.getWindow().getScaledHeight()*4, (float) ModuleManager.INSTANCE.getModule(ClickGuiMod.class).blurIntensity.getValueFloat());
		}
		
		textRend.drawString(matrices, "Sudo client", 5, 5, -1, 1);

		for (Frame frame : frames) {
			frame.render(matrices, mouseX, mouseY, delta);
			frame.updatePosition(mouseX, mouseY);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Frame frame : frames) {
			frame.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (Frame frame : frames) {
			frame.mouseReleased(mouseX, mouseY, button);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
	    for (Frame frame : frames) {
	        frame.keyPressed(keyCode);
	    }
	    return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for (Frame frame : frames) {
			if (amount > 0) frame.setY((int) (frame.getY() + 5));
			else if (amount < 0) frame.setY((int) (frame.getY() - 5));
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
}