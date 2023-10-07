package best.azura.client.impl.ui.gui.impl.buttons;

import best.azura.client.api.account.AccountData;
import best.azura.client.api.ui.buttons.Button;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.storage.SaveFormatComparator;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class ButtonImpl implements Button {
	public int x, y, width, height;
	public double roundness = 0, animation = 0;
	public Color normalColor = new Color(70, 70, 70, 50),
			hoverColor = new Color(100, 100, 100, 70),
			electedColor = new Color(120, 120, 120, 50);
	public boolean hovered = false, selected = false;
	public String text = "", description = "";
	public ServerData serverData = null;
	public SaveFormatComparator levelData = null;
	public AccountData accountData = null;
	private final DateFormat dateFormat = new SimpleDateFormat();

	public ButtonImpl(String text, int x, int y, int width, int height) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public ButtonImpl(String text, int x, int y, int width, int height, double roundness) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.roundness = roundness;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		this.hovered = RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0);
		Color color = hovered ? hoverColor : normalColor;
		boolean other = description.equals("") && serverData == null && levelData == null;
		RenderUtil.INSTANCE.drawRoundedRect(x, y, other ? width : width * animation, other ? height * animation : height, roundness * animation, selected ? RenderUtil.INSTANCE.modifiedAlpha(color, 100) : color);
		boolean desc = !description.equals("");
		if (levelData != null) {
			Fonts.INSTANCE.arial15bold.drawStringWithOutline(levelData.getDisplayName(), x + 20, y + 5, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			Fonts.INSTANCE.arial15.drawStringWithOutline(dateFormat.format(new Date(levelData.getLastTimePlayed())) + "", x + 25, y + 26, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			Fonts.INSTANCE.arial15.drawStringWithOutline(levelData.getEnumGameType().getName().substring(0, 1).toUpperCase() + levelData.getEnumGameType().getName().substring(1)
					+ (levelData.getCheatsEnabled() ? ", Cheats" : ""), x + 25, y + 45, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

		} else if (serverData != null) {
			Fonts.INSTANCE.arial15bold.drawStringWithOutline(serverData.serverName + " (" + serverData.pingToServer + "ms)", x + 90, y + 5, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			if (serverData.serverMOTD != null) {
				List<String> list = Minecraft.getMinecraft().fontRendererObj.listFormattedStringToWidth(serverData.serverMOTD, 2000);
				int count = 0;
				for (String s : list) {
					Fonts.INSTANCE.arial15.drawStringWithOutline(s.substring(0, (int) (s.length() * animation)), x + 80, y + 25 + count * 20, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
					count++;
				}
			} else
				Fonts.INSTANCE.arial15.drawStringWithOutline("Pinging..", x + 70, y + 25, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			if (serverData.populationInfo != null) {
				Fonts.INSTANCE.arial20.drawStringWithOutline(serverData.populationInfo.substring(0, (int) (serverData.populationInfo.length() * animation)), x + width - Fonts.INSTANCE.arial20.getStringWidth(serverData.populationInfo) - 20, y + height / 2.0 - Fonts.INSTANCE.arial20.FONT_HEIGHT / 2.0, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			}


		} else if (accountData != null) {

			Fonts.INSTANCE.arial15bold.drawStringWithOutline(accountData.getUsername() == null || accountData.getUsername().isEmpty() ?
							accountData.getData().contains(":") ? accountData.getData().split(":")[0] : accountData.getData() : accountData.getUsername(),
					x + 70, y + height / 2.0 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0 - Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2,
					new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			Fonts.INSTANCE.arial12.drawStringWithOutline(accountData.getType().toString(), x + 70, y + height / 2.0 + Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2, new Color(255, 255, 255, (int) (255 * animation)).getRGB());

		} else if (desc) {

			Fonts.INSTANCE.arial15bold.drawStringWithOutline(text, x + 70, y + height / 2.0 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0 - Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2,
					new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			Fonts.INSTANCE.arial12.drawStringWithOutline(description, x + 70, y + height / 2.0 + Fonts.INSTANCE.arial12.FONT_HEIGHT / 3.0 - 2, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
			glDisable(GL_BLEND);
			glPushMatrix();
			glEnable(GL_LINE_SMOOTH);
			RenderUtil.INSTANCE.color(new Color(255, 255, 255, (int) (255 * animation)));
			glLineWidth(3.0f);
			glBegin(GL_LINE_LOOP);
			glVertex2d(x + width - 30 - (5 * animation), y + height / 2F - 5);
			glVertex2d(x + width - 30, y + height / 2F);
			glEnd();
			glBegin(GL_LINE_LOOP);
			glVertex2d(x + width - 30, y + height / 2F);
			glVertex2d(x + width - 29, y + height / 2F);
			glEnd();
			glBegin(GL_LINE_LOOP);
			glVertex2d(x + width - 30, y + height / 2F);
			glVertex2d(x + width - 30 - (5 * animation), y + height / 2F + 5);
			glEnd();
			glDisable(GL_LINE_SMOOTH);
			glPopMatrix();
			glEnable(GL_BLEND);
		} else
			Fonts.INSTANCE.arial20.drawStringWithOutline(text, x + width / 2f - Fonts.INSTANCE.arial20.getStringWidth(text) / 2f, y + height / 2f - Fonts.INSTANCE.arial20.FONT_HEIGHT / 2f + 2, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
	}

	@Override
	public void keyTyped(char typed, int keyCode) {

	}

	@Override
	public void mouseClicked() {

	}
}
