package xyz.cucumber.base.module.feat.other;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.events.ext.EventBlur;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.addons.Dragable;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.math.PositionHandler;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

@ModuleInfo(category = Category.OTHER, description = "Displays watermark on screen", name = "Watermark", priority = ArrayPriority.LOW)
public class WaterMarkModule extends Mod implements Dragable {

	private PositionUtils accounts = new PositionUtils(0, 0, 150, 15, 1);

	private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 1000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 1000, 1);

	private BooleanSettings blur = new BooleanSettings("Blur", true);
	private BooleanSettings bloom = new BooleanSettings("Bloom", true);
	public ColorSettings bloomColor = new ColorSettings("Bloom color", "Static", 0xff000000, -1, 40);

	private ModeSettings mode = new ModeSettings("Mode", new String[] { "Modern", "Simple", "Old" });
	public ColorSettings logoColor = new ColorSettings("Mark color", "Mix", 0xff44bcfc, 0xff3275f0, 100);
	public ColorSettings backgroundColor = new ColorSettings("Background color", "Static", 0xff000000, -1, 40);

	public WaterMarkModule() {
		this.addSettings(positionX, positionY, blur, bloom, bloomColor, logoColor, backgroundColor);
	}

	@EventListener
	public void onBlur(EventBlur e) {
		if (!blur.isEnabled())
			return;
		e.setCancelled(true);
		if (e.getType() == EventType.POST)
			RenderUtils.drawRoundedRect(accounts.getX(), accounts.getY(), accounts.getX2(), accounts.getY2(),
					ColorUtils.getColor(backgroundColor, System.nanoTime() / 1000000, 0, 5), 1.5);
	}

	@EventListener
	public void onBloom(EventBloom e) {
		if (!bloom.isEnabled())
			return;
		e.setCancelled(true);

		if (e.getType() == EventType.POST) {
			ColorSettings fixedColor = new ColorSettings(bloomColor.getMode(), bloomColor.getMode(),
					bloomColor.getMainColor(), bloomColor.getSecondaryColor(), 255);
			RenderUtils.drawRoundedRect(accounts.getX(), accounts.getY(), accounts.getX2(), accounts.getY2(),
					ColorUtils.getColor(fixedColor, System.nanoTime() / 1000000, 0, 5), 1.5);
		}
	}

	@EventListener
	public void onRender2D(EventRenderGui e) {

		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		accounts.setX(pos[0]);
		accounts.setY(pos[1]);
		accounts.setWidth(2 + 11 + 4 + Fonts.getFont("rb-b").getWidth("Gothaj") + 5
				+ Fonts.getFont("rb-r-13").getWidth("" + Client.INSTANCE.version) + 5
				+ Fonts.getFont("rb-m-13").getWidth(mc.thePlayer.getName()) + 5
				+ Fonts.getFont("rb-m-13").getWidth("Fps: ") + Fonts.getFont("rb-r-13").getWidth("" + mc.debugFPS) + 2);

		RenderUtils.drawRoundedRect(accounts.getX(), accounts.getY(), accounts.getX2(), accounts.getY2(),
				ColorUtils.getColor(backgroundColor, System.nanoTime() / 1000000, 0, 5), 1.5);

		StencilUtils.initStencil();
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		StencilUtils.bindWriteStencilBuffer();
		RenderUtils.drawRoundedRect(accounts.getX() + 2, accounts.getY() + 2, accounts.getX() + 13,
				accounts.getY() + 13, 0xffffffff, 2D);

		StencilUtils.bindReadStencilBuffer(1);
		if (mc.getNetHandler() != null && mc.thePlayer.getUniqueID() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getLocationSkin() != null)
			Minecraft.getMinecraft().getTextureManager()
					.bindTexture(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getLocationSkin());
		Gui.drawScaledCustomSizeModalRect(accounts.getX() + 2, accounts.getY() + 2, 8, 8, 8, 8, 11, 11, 64F, 64F);

		StencilUtils.uninitStencilBuffer();
		String[] s = "Gothaj".split("");
		double w = 0;

		for (String t : s) {
			Fonts.getFont("rb-b").drawStringWithShadow(t, accounts.getX() + 2 + 11 + 4 + w,
					accounts.getY() + accounts.getHeight() / 2 - 3 / 2,
					ColorUtils.getColor(logoColor, System.nanoTime() / 1000000, w * 2, 5), 0x90000000);
			w += Fonts.getFont("rb-b").getWidth(t);
		}
		Fonts.getFont("rb-r-13").drawStringWithShadow("" + Client.INSTANCE.version,
				accounts.getX() + 2 + 11 + 4 + Fonts.getFont("rb-b").getWidth("Gothaj") + 5,
				accounts.getY() + accounts.getHeight() / 2 - 1.5 / 2, -1, 0x90000000);
		Fonts.getFont("rb-m-13").drawStringWithShadow(mc.thePlayer.getName(),
				accounts.getX() + 2 + 11 + 4 + Fonts.getFont("rb-b").getWidth("Gothaj") + 5
						+ Fonts.getFont("rb-r-13").getWidth("" + Client.INSTANCE.version) + 5,
				accounts.getY() + accounts.getHeight() / 2 - 1.5 / 2, -1, 0x90000000);
		Fonts.getFont("rb-m-13").drawStringWithShadow("FPS:",
				accounts.getX() + 2 + 11 + 4 + Fonts.getFont("rb-b").getWidth("Gothaj") + 5
						+ Fonts.getFont("rb-r-13").getWidth("" + Client.INSTANCE.version) + 5
						+ Fonts.getFont("rb-m-13").getWidth(mc.thePlayer.getName()) + 5,
				accounts.getY() + accounts.getHeight() / 2 - 1.5 / 2, -1, 0x90000000);
		Fonts.getFont("rb-r-13").drawStringWithShadow("" + mc.debugFPS,
				accounts.getX() + 2 + 11 + 4 + Fonts.getFont("rb-b").getWidth("Gothaj") + 5
						+ Fonts.getFont("rb-r-13").getWidth("" + Client.INSTANCE.version) + 5
						+ Fonts.getFont("rb-m-13").getWidth(mc.thePlayer.getName()) + 5
						+ Fonts.getFont("rb-m-13").getWidth("Fps: "),
				accounts.getY() + accounts.getHeight() / 2 - 1.5 / 2, -1, 0x90000000);
	}

	@Override
	public PositionUtils getPosition() {
		return accounts;
	}

	@Override
	public void setXYPosition(double x, double y) {
		this.positionX.setValue(x);
		this.positionY.setValue(y);
	}
}
