package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import me.AquaVit.liquidSense.utils.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ElementInfo(name = "PlayerList")
public class PlayerList extends Element {

    private BoolValue fadeSpeed = new BoolValue("Teams", false);

    @Override
    public void Render2DElement() {

    }

    @Nullable
    @Override
    public Border drawElement() {
        AntiBot ab = (AntiBot) LiquidBounce.moduleManager.getModule(AntiBot.class);
        ArrayList<EntityLivingBase> playername = new ArrayList<>();
        ArrayList<EntityLivingBase> teamplayer = new ArrayList<>();
        for (EntityLivingBase player : mc.theWorld.playerEntities) {
            assert ab != null;
            if (!player.getDisplayName().getFormattedText().toLowerCase().contains("npc") && !(player instanceof EntityPlayerSP)) {
                playername.add(player);

            }
            if (!player.getDisplayName().getFormattedText().toLowerCase().contains("npc") && player instanceof EntityPlayerSP) {
                teamplayer.add(player);
            }
        }
        String name = this.getLongestPlayerName(playername);
        float longestNameWidth = Fonts.csgo40.getStringWidth("F" ) + Fonts.font40.getStringWidth(name)+ 10;
        float borderedRectWidth = Fonts.csgo40.getStringWidth("F") + Fonts.font40.getStringWidth("PlayerList") + 60;
        float playerListWidth = Math.max(longestNameWidth, borderedRectWidth);


        //Fonts.font40.drawString("width: " + playerListWidth, 100, 100, Color.WHITE.getRGB(), false);
        //Fonts.font40.drawString("longestName: " + name + " | " + longestNameWidth, 100, 130, Color.WHITE.getRGB(), false);


        /*
        int y2 = 1;
        Fonts.minecraftFont.drawStringWithShadow("Other",123,31,new Color(200,50,50,255).getRGB());
        for (EntityLivingBase m : playername) {
            Fonts.minecraftFont.drawStringWithShadow((int) m.getHealth() + "\2477 " + m.getDisplayName().getFormattedText()+"\2477["+(int)mc.thePlayer.getDistanceToEntity(m)+"]", 123, y2 + 40, Color.red.getRGB());
            y2 += 9;
        }

         */
        int y = 1;
        if (playername.size() != 0) {
            BlurBuffer.blurArea((int) ((-4.5F + this.getRenderX()) * this.getScale()),
                    (int) ((this.getRenderY() + Fonts.csgo40.FONT_HEIGHT - 2) * this.getScale()),
                    (playerListWidth + 4.5F) * this.getScale(),
                    (8 + playername.size() * 14) * this.getScale(),
                    true);
            if (!this.getInfo().disableScale())
                GL11.glScalef(this.getScale(), this.getScale(), this.getScale());

            GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);

            for (EntityLivingBase m : playername) {
                NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(m.getUniqueID());
                if (playerInfo != null) {
                    ResourceLocation locationSkin = playerInfo.getLocationSkin();
                    RenderUtils.drawHead(locationSkin, (int) -1.1F, y + 16, 9, 9);
                }
                Fonts.font40.drawString(m.getDisplayName().getFormattedText(), Fonts.csgo40.getStringWidth("F") + 3, y + 17, Color.WHITE.getRGB(), false);
                y += 14;
            }
        }

        RenderUtils.drawBorderedRect(-5.5F, -5.5F, playerListWidth, Fonts.csgo40.FONT_HEIGHT + 0.5F, 3F, new Color(16, 25, 32, 200).getRGB(), new Color(16, 25, 32, 200).getRGB());
        Fonts.csgo40.drawString("F", -0.8F, -0.4F, new Color(0, 131, 193).getRGB(), false);
        Fonts.font40.drawString("PlayerList", Fonts.csgo40.getStringWidth("F") + 3, -1F, Color.WHITE.getRGB(), false);




        return new Border(20, 20, 120, 14 * playername.size());
    }

    private String getLongestPlayerName(List<EntityLivingBase> list) {
        String name = "";
        for (EntityLivingBase player : list) {
            if (player.getDisplayName().getUnformattedText().length() > name.length()) {
                name = player.getDisplayName().getUnformattedText();
            }
        }
        return name;
    }


}
