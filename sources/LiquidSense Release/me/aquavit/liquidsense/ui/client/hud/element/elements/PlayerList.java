package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.module.modules.misc.AntiBot;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.AlphaData;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@ElementInfo(name = "PlayerList")
public class PlayerList extends Element {

	public final IntegerValue speedValue = new IntegerValue("Speed", 5, 5, 20);

	private final HashMap<UUID, AlphaData> alphaMap = new HashMap<>();

	@Override
	public Border drawElement() {

		ArrayList<EntityLivingBase> playername = new ArrayList<>();
		for (EntityLivingBase player : mc.theWorld.playerEntities) {
			if (!AntiBot.isBot(player) && !(player instanceof EntityPlayerSP)) {
				playername.add(player);
				if (!alphaMap.containsKey(player.getUniqueID())) {
					alphaMap.put(player.getUniqueID(), new AlphaData(player.getName()));
				}
			}
		}

		HashMap<UUID, AlphaData> tempMap = new HashMap<>(alphaMap);

		for (UUID uuid : tempMap.keySet()) {

			boolean flag = false;
			for (EntityLivingBase entityLivingBase : playername) {
				if (entityLivingBase.getUniqueID().equals(uuid)) {
					flag = true;
					break;
				}
			}

			AlphaData alphaData = alphaMap.get(uuid);
			if (flag) {
				alphaData.translate.translate(255f , 15f );
				alphaMap.put(uuid, alphaData);
			} else {
				if (alphaData.translate.getX()  < 30) {
					alphaMap.remove(uuid);
				} else {
					alphaData.translate.translate(0f , 0f);
					alphaMap.put(uuid, alphaData);
				}
			}
		}

		String name = this.getLongestPlayerName();
		float longestNameWidth = Fonts.csgo40.getStringWidth("F") + Fonts.font20.getStringWidth(name) + 10;
		float borderedRectWidth = Fonts.csgo40.getStringWidth("F") + Fonts.font20.getStringWidth("PlayerList") + 60;
		float playerListWidth = Math.max(longestNameWidth, borderedRectWidth);
		int y = 1;

		if (alphaMap.size() > 0) {
			BlurBuffer.blurArea((int) ((-4.5F + this.getRenderX()) * this.getScale()),
				(int) ((this.getRenderY() + Fonts.csgo40.FONT_HEIGHT - 2) * this.getScale()),
				(playerListWidth + 4.5F) * this.getScale(),
				(8 + alphaMap.size() * 14) * this.getScale(),
				true);
			if (!this.getInfo().disableScale())
				GL11.glScalef(this.getScale(), this.getScale(), this.getScale());

			GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);

			for (UUID uuid : alphaMap.keySet()) {
				NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(uuid);
				AlphaData alphaData = alphaMap.get(uuid);
				if (playerInfo != null && alphaData.translate.getX() > 30 &&  alphaData.translate.getX() <= 255) {
					ResourceLocation locationSkin = playerInfo.getLocationSkin();
					RenderUtils.drawHead(locationSkin, (int) -1.1F, y + 16, 9, 9);
					Fonts.font20.drawString(alphaData.playername, Fonts.csgo40.getStringWidth("F") + 3, y + 17, new Color(255, 255, 255, (int) alphaData.translate.getX()).getRGB(), false);
				}
				y += alphaData.translate.getY();
			}
		}

		RenderUtils.drawRoundedRect(-5.2F, -5.5F, playerListWidth + 5.2F, Fonts.csgo40.FONT_HEIGHT + 6F, 1.5F,
			new Color(16, 25, 32, 200).getRGB(), 1F, new Color(16, 25, 32, 200).getRGB());
		Fonts.csgo40.drawString("F", -1.5F, -0.4F, new Color(0, 131, 193).getRGB(), false);
		Fonts.font20.drawString("PlayerList", Fonts.csgo40.getStringWidth("F") + 3, -1F, Color.WHITE.getRGB(), false);
		return new Border(20, 20, 120, 14 * alphaMap.size());
	}

	private String getLongestPlayerName() {
		String name = "";
		for (UUID uuid : alphaMap.keySet()) {
			if (alphaMap.get(uuid).playername.length() > name.length()) {
				name = alphaMap.get(uuid).playername;
			}
		}
		return name;
	}
}