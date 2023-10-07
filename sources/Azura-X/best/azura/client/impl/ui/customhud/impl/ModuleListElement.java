package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.impl.module.impl.other.ClientModule;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.module.impl.render.HUD;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.ui.customhud.corner.Side;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ColorValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleListElement extends Element {

	public ModuleListElement() {
		super("Array List", 3, 3, 40, 40);
		sorted = new ArrayList<>(Client.INSTANCE.getModuleManager().getRegistered());
	}

	private final NumberValue<Float> padding = new NumberValue<>("Padding", "Change the padding of the arraylist elements.", 3.0F, 0.5F, 1.0F, 20.0F);
	private final NumberValue<Float> lineWidth = new NumberValue<>("Line width", "Change the line width of all lines.", 1.0F, 0.5F, 1.0F, 5.0F);

	private final BooleanValue topLine = new BooleanValue("Top Line", "Render a line at the top of the array list", false);
	private final BooleanValue leftLine = new BooleanValue("Left Line", "Render a line on the left of the array list", false);
	private final BooleanValue rightLine = new BooleanValue("Right Line", "Render a line on the right of the array list", false);
	private final BooleanValue outLine = new BooleanValue("Out Line", "Render an outline on the array list", false);
	//Color stepping

	//Background of the ArrayList
	public final BooleanValue backgroundValue = new BooleanValue("Render background", "Renders a background behind the modules", false);


	//Background color
	public final ColorValue backgroundColor = new ColorValue("Background Color",
			"Change the color of the Array List", backgroundValue::getObject,
			HSBColor.fromColor(new Color(0xFF171616)));

	private ModuleComparator moduleComparator;

	@Override
	public void onAdd() {
		moduleComparator = new ModuleComparator(true, true);
	}

	//Background Alpha
	public final NumberValue<Integer> backgroundAlpha = new NumberValue<>("Background alpha",
			"Change the step of the Color when using Fade or Pulsate", () -> backgroundValue.getObject() && backgroundColor.collapsed, 166, 1, 1, 255);

	public final BooleanValue sort = new BooleanValue("Sort", "Sort the ArrayList NOTE: Must reload after disabling this feature", true);

	private boolean hasSorted;

	private boolean vertexEasterEgg;

	public static final ModeValue suffixMode = new ModeValue("Suffix", "Type of suffix", "Normal", "Normal", "Dash", "Brackets", "None");

	@Override
	public List<Value<?>> getValues() {
		return createValuesArray(
				backgroundValue,
				backgroundColor,
				backgroundAlpha,
				topLine,
				leftLine,
				rightLine,
				outLine,
				lineWidth,
				sort,
				padding,
				suffixMode
		);
	}

	@Override
	public void loadFromJson(JsonObject object) {
		super.loadFromJson(object);
		if (object.has("vertexEasterEgg"))
			this.vertexEasterEgg = true;
	}

	@Override
	public JsonObject buildJson() {
		final JsonObject json = super.buildJson();
		if (vertexEasterEgg) json.addProperty("vertexEasterEgg", true);
		return json;
	}

	public static ArrayList<Module> sorted;

	final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);

	@Override
	public void onFastTick() {

		if(sort.getObject()) {
			final ArrayList<Module> copy = new ArrayList<>(Client.INSTANCE.getModuleManager().getRegistered());
			copy.removeAll(sorted);
			sorted.addAll(copy);
			sorted.sort(moduleComparator);
			hasSorted = true;
		} else if(!sort.getObject() && hasSorted) {
			sorted = new ArrayList<>(Client.INSTANCE.getModuleManager().getRegistered());
			hasSorted = false;
		}
	}

	@Override
	public void render() {
		if (moduleComparator == null)
			moduleComparator = new ModuleComparator(true, true);
		fitInScreen(mc.displayWidth, mc.displayHeight);

		final Side side = getSide();
		final FontRenderer fr = hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont : mc.fontRendererObj;

		final int hudScale = hud.useClientFont.getObject() ? 1 : 2;

		final int constantOffset = fr.FONT_HEIGHT - (hud.useClientFont.getObject() && padding.getObject() == 1 ? 4 : 0) + padding.getObject().intValue() / 2;

		int offset = (padding.getObject().intValue() / 2 + ((topLine.getObject() || outLine.getObject()) ? lineWidth.getObject().intValue() : 0)) * hudScale * hudScale;
		int count = 0;
		int maxWidth = 0;

		boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurArray.getObject();

		// Calculate side offset
		int sideOffset = side == Side.LEFT ? 1 : -1;
		int defaultXOffset = 0;

		if (((rightLine.getObject() || outLine.getObject()) && side == Side.LEFT) || ((leftLine.getObject() || outLine.getObject()) && side == Side.RIGHT)) {
			defaultXOffset = lineWidth.getObject().intValue() * sideOffset;
		}

		if (!hud.useClientFont.getObject())
			GlStateManager.scale(hudScale, hudScale, 1.0);

		int lastColor;
		Module lastModule = null;

		ArrayList<Module> render = new ArrayList<>(sorted);
		render.removeIf(module -> {
			if (module.getName().equals("HUD")) return true;
			if (module.getName().equals("Client") || module.getName().equals("ClickGUI")) return true;
			if (ClientModule.hideRender.getObject() && module.getCategory().equals(Category.RENDER)) return true;
			module.animation = module.isEnabled() ? 1 : 0;
			if (module.animation == 0 && !module.isEnabled()) return true;
			return Client.INSTANCE.getValueManager().isRegistered(module) && Client.INSTANCE.getValueManager().getValue(module, "Hide") != null &&
					((BooleanValue) Client.INSTANCE.getValueManager().getValue(module, "Hide")).getObject();
		});

		for (Module module : render) {
			final Module module1 = render.get(count == 0 ? count : count - 1);

			module.animation = module.isEnabled() ? 1 : 0;
			if (module.animation == 0) {
				continue;
			}

			// Get module text
			String text = module.getName() + (module.getSuffix().equals("") ? "" : " ");
			String text1 = module1.getName() + (module1.getSuffix().equals("") ? "" : " ");

			// Get font width
			int width = fr.getStringWidth(side == Side.LEFT ? text : text + module.getSuffix());
			int width1 = fr.getStringWidth(side == Side.LEFT ? text1 : text1 + module1.getSuffix());
			int bgWidth = fr.getStringWidth(text + module.getSuffix());

			// Calculate x and y
			int x = defaultXOffset + (int) (side == Side.LEFT ? getX() / hudScale : getX() / hudScale + getWidth() / hudScale - width) + padding.getObject().intValue() * sideOffset * hudScale;
			int x1 = defaultXOffset + (int) (side == Side.LEFT ? getX() / hudScale : getX() / hudScale + getWidth() / hudScale - width1) + padding.getObject().intValue() * sideOffset * hudScale;
			int y = (int) (getY() + offset) / hudScale;

			// Calculate color
			int color = ColorUtil.getHudColor(count).getRGB();
			lastColor = ColorUtil.getHudColor(count - 1).getRGB();

			if (vertexEasterEgg) {
				final String name = module.getName().replace(" ", "").replace("Flight", "Fly");
				color = new Color(name.hashCode() * name.length() * 1337).getRGB();
				if (lastModule == null) lastColor = color;
				else {
					final String prevName = lastModule.getName().replace(" ", "").replace("Flight", "Fly");
					lastColor = new Color(prevName.hashCode() * prevName.length() * 1337).getRGB();
				}
			}

			lastModule = module;

			// Set new max width
			if (width > maxWidth)
				maxWidth = width;


			// Draw top line if enabled
			if ((topLine.getObject() || outLine.getObject()) && count == 0) {
				int leftLineOffset = (rightLine.getObject() || outLine.getObject()) ? lineWidth.getObject().intValue() : 0;
				int rightLineOffset = (leftLine.getObject() || outLine.getObject()) ? lineWidth.getObject().intValue() : 0;
				RenderUtil.INSTANCE.drawRect(x - padding.getObject().intValue() - leftLineOffset,
						y - padding.getObject().intValue() / 2.0, x + bgWidth + padding.getObject().intValue() + rightLineOffset,
						y - padding.getObject().intValue() / 2.0 - lineWidth.getObject().intValue(), color);
			}

			// Draw background if background or blur is enabled
			if (backgroundValue.getObject()) {
				final Color bgColor = backgroundColor.getObject().getColor();
				if (blur) {
					// Draw blurred background
					BlurModule.blurTasks.add(() -> {
						if (hud.useClientFont.getObject()) RenderUtil.INSTANCE.scaleFix(1.0);
						RenderUtil.INSTANCE.drawRect(x - padding.getObject().intValue(),
								y - (padding.getObject() / 2 - (padding.getObject() % 2 == 0 ? 0 : 0.5)), x + bgWidth + padding.getObject().intValue(),
								y + constantOffset, new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), backgroundAlpha.getObject()));
						if (hud.useClientFont.getObject()) RenderUtil.INSTANCE.invertScaleFix(1.0);
					});
				} else {
					// Draw normal background
					RenderUtil.INSTANCE.drawRect(x - padding.getObject().intValue(),
							y - (padding.getObject() / 2 - (padding.getObject() % 2 == 0 ? 0 : 0.5)), x + bgWidth + padding.getObject().intValue(),
							y + constantOffset, new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), backgroundAlpha.getObject()));
				}
			}

			// Draw right line if enabled
			if (leftLine.getObject() || outLine.getObject()) {
				RenderUtil.INSTANCE.drawRect(x - padding.getObject().intValue() - lineWidth.getObject().intValue(),
						y - padding.getObject() / 2, x - padding.getObject().intValue(), y + constantOffset, color);
			}

			// Draw left line if enabled
			if (rightLine.getObject() || outLine.getObject()) {
				RenderUtil.INSTANCE.drawGradientRect(x + bgWidth + padding.getObject(), y - padding.getObject() / 2,
						x + bgWidth + padding.getObject() + lineWidth.getObject(),
						y + constantOffset, color, lastColor);
			}

			if (outLine.getObject()) {
				if (count == render.size() - 1) {
					RenderUtil.INSTANCE.drawRect(x - padding.getObject() * hudScale,
							y + constantOffset - padding.getObject() / 2,
							x + bgWidth + padding.getObject(), y + constantOffset - (padding.getObject() / 2 - lineWidth.getObject()) / hudScale, color);
					RenderUtil.INSTANCE.drawRect(x1 - padding.getObject(),
							y - padding.getObject() / 2,
							x - padding.getObject(), y - padding.getObject() / 2 - lineWidth.getObject(), color);
				} else {
					RenderUtil.INSTANCE.drawRect(x - padding.getObject(),
							y - padding.getObject() / 2,
							x1 - padding.getObject(), y - padding.getObject() / 2 - lineWidth.getObject(), color);
				}
			}

			double x2 = x;
			if (!hud.useClientFont.getObject() && side == Side.LEFT) {
				x2 += 0.5;
			}

			// Draw the text
			fr.drawStringWithShadow(text, x2, y - (hud.useClientFont.getObject() ? 2 : 0), color);

			// Draw the suffix
			final Color suffixColor = new Color(
					hud.suffixColorValue.getObject().getColor().getRed(),
					hud.suffixColorValue.getObject().getColor().getGreen(),
					hud.suffixColorValue.getObject().getColor().getBlue(), hud.suffixAlpha.getObject()
			);

			fr.drawStringWithShadow(module.getSuffix(), (side == Side.LEFT ? x2 + width : x2 + fr.getStringWidth(text)), y - (hud.useClientFont.getObject() ? 2 : 0), suffixColor.getRGB());

			// Add new offset
			offset += (constantOffset + padding.getObject().intValue() / 2) * hudScale;
			count++;
		}
		if (!hud.useClientFont.getObject())
			GlStateManager.scale(1.0 / hudScale, 1.0 / hudScale, 1.0);
		setWidth(200);
		this.setHeight(Math.max(offset, 50));
	}

	public void setSorted(ArrayList<Module> sorted) {
		ModuleListElement.sorted = sorted;
	}

	private static class ModuleComparator implements Comparator<Module> {

		private final boolean values, swap;

		public ModuleComparator(boolean values, boolean swap) {
			this.values = values;
			this.swap = swap;
		}

		@Override
		public int compare(Module mod1, Module mod2) {
			final FontRenderer fr = Fonts.INSTANCE.hudFont;
			HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
			final String name1 = values ? mod1.getDisplayName()
					: mod1.getName();
			final String name2 = values ? mod2.getDisplayName()
					: mod2.getName();
			final int width = !hud.useClientFont.getObject() ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(name1) :
					fr.getStringWidth(name1);
			final int width1 = !hud.useClientFont.getObject() ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(name2) :
					fr.getStringWidth(name2);
			return swap ? Integer.compare(width1, width) : Integer.compare(width, width1);
		}
	}

	@Override
	public Element copy() {
		return new ModuleListElement();
	}

}