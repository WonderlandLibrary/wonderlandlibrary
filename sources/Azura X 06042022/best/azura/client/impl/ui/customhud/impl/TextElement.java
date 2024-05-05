package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.ui.customhud.Element;
import best.azura.client.impl.ui.customhud.GuiEditHUD;
import best.azura.client.impl.module.impl.render.HUD;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.PlayTimeUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.impl.value.StringValue;
import best.azura.client.api.value.Value;
import com.google.gson.JsonObject;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import viamcp.ViaMCP;
import viamcp.protocols.ProtocolCollection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TextElement extends Element {
	
	private String text;
	
	public TextElement() {
		super("Text", 0, 0, 40, 40);
		setCanScaleBeModified(true);
	}
	
	@Override
	public List<Value<?>> getValues() {
		return createValuesArray(new StringValue("Text", "Text for the text element", (val) -> text = ((StringValue) val).getObject(), text));
	}
	
	@Override
	public void loadFromJson(JsonObject object) {
		super.loadFromJson(object);
		if (object.has("text") && !object.get("text").getAsString().isEmpty())
			text = object.get("text").getAsString();
	}
	
	@Override
	public JsonObject buildJson() {
		JsonObject object = super.buildJson();
		object.addProperty("text", text);
		return object;
	}
	
	@Override
	public void render() {
		fitInScreen(mc.displayWidth, mc.displayHeight);
		final double diffX = MathUtil.getDifference(mc.thePlayer.posX, mc.thePlayer.lastTickPosX),
				diffZ = MathUtil.getDifference(mc.thePlayer.posZ, mc.thePlayer.lastTickPosZ),
				diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		if (text == null) {
			text = "";
			return;
		}
		final String[] str = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
		final float[] rotations = {180, -135, -90, -45, 0, 45, 90, 135};
		String direction = "N";
		int index = 0;
		if (text.contains("%facing%")) {
			for (String s : str)
				if (MathUtil.getDifference(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw), rotations[index++]) < 22.5)
					direction = s;
		}
		final ProtocolVersion version = ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion());

		assert version != null;
		final String text = this.text
				.replace("%clientName%", Client.NAME)
				.replace("%clientVersion%", Client.VERSION)
				.replace("%clientRelease%", Client.RELEASE)
				.replace("%username%", Client.INSTANCE.getUsername())
				.replace("%date%", Client.INSTANCE.getCurrentDate())
				.replace("%bps%", String.format("%.2f", diffXZ * 20 * mc.timer.timerSpeed).replace(',', '.'))
				.replace("%facing%", direction)
				.replace("%time%", new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime()))
				.replace("%time1%", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()))
				.replace("%fps%", String.valueOf(Minecraft.getDebugFPS()))
				.replace("%playtime%", (this.text.contains("%playtime%") && ServerUtil.joinedTime != 0) ? PlayTimeUtil.format(System.currentTimeMillis() - ServerUtil.joinedTime) : "")
				.replace("%px%", String.valueOf((int) mc.thePlayer.posX))
				.replace("%py%", String.valueOf((int) mc.thePlayer.posY))
				.replace("%pz%", String.valueOf((int) mc.thePlayer.posZ))
				.replace("%version%", version.getName())
				.replace("%name%", mc.thePlayer.getName())
				.replace("%ip%", ServerUtil.lastIP)
				.replace("$nofade$", "");
		
		final String renderText = text.isEmpty() ? "Placeholder" : text, text1 = mc.currentScreen instanceof GuiEditHUD ? renderText : text;
		final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
		final FontRenderer fr = hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont : mc.fontRendererObj;
		final double scale = hud.useClientFont.getObject() ? 1 : 2;
		GlStateManager.scale(scale, scale, 1);
		if (hud.colorValue.getObject().equals("Static"))
			fr.drawStringWithShadow(text1, getX() / scale, getY() / scale, hud.arrayListColor.getObject().getRGB());
		else {
			int xOffset = 0, count = 0;
			boolean skip = false;
			if (this.text.contains("$nofade$")) {
				fr.drawStringWithShadow(renderText, getX() / scale + xOffset, getY() / scale,
						ColorUtil.getHudColor(count).getRGB());
			} else {
				final StringBuilder builder = new StringBuilder();
				for (char c : text1.toCharArray()) {
					if (skip) {
						skip = false;
						builder.append(c);
						continue;
					}
					if (c == 167) {
						builder.append(c);
						skip = true;
						continue;
					}
					fr.drawStringWithShadow(builder.toString() + c, getX() / scale + xOffset, getY() / scale,
							ColorUtil.getHudColor(count).getRGB());
					xOffset += fr.getStringWidth(Character.toString(c));
					count--;
				}
			}
		}
		setWidth(fr.getStringWidth(renderText) * scale);
		setHeight(fr.FONT_HEIGHT * scale);
		GlStateManager.scale(1 / scale, 1 / scale, 1);
	}
	
	@Override
	public Element copy() {
		return new TextElement();
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}