package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.SessionInfoUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ElementInfo(name = "SessionInfo")
public class SessionInfo extends Element {
    private final IntegerValue backgroundAlphaValue = new IntegerValue("BackgroundAlpha",65,0,255);
    private final BoolValue shadowValue = new BoolValue("Shadow",true);

    private final IntegerValue titleRValue = new IntegerValue("TitleR",65,0,255);
    private final IntegerValue titleGValue = new IntegerValue("TitleG",215,0,255);
    private final IntegerValue titleBValue = new IntegerValue("TitleB",255,0,255);

    private final IntegerValue keyRValue = new IntegerValue("KeyR",175,0,255);
    private final IntegerValue keyGValue = new IntegerValue("KeyG",175,0,255);
    private final IntegerValue keyBValue = new IntegerValue("KeyB",175,0,255);

    private final IntegerValue textRValue = new IntegerValue("TextR",255,0,255);
    private final IntegerValue textGValue = new IntegerValue("TextG",255,0,255);
    private final IntegerValue textBValue = new IntegerValue("TextB",255,0,255);

    @Nullable
    @Override
    public Border drawElement() {
        RenderUtils.drawRect(0,0,150,(Fonts.font35.FONT_HEIGHT + 2) * 4 + Fonts.font40.FONT_HEIGHT + 10,new Color(0,0,0,backgroundAlphaValue.get()));

        Fonts.font40.drawCenteredString("SessionInfo",75,5,new Color(titleRValue.get(),titleGValue.get(),titleBValue.get()).getRGB(),shadowValue.get());

        Fonts.font35.drawString("Player",5,5 + Fonts.font40.FONT_HEIGHT + 5,new Color(keyRValue.get(),keyGValue.get(),keyBValue.get()).getRGB(),shadowValue.get());
        Fonts.font35.drawString(mc.session.getUsername(),145 - Fonts.font35.getStringWidth(mc.session.getUsername()),5 + Fonts.font40.FONT_HEIGHT + 5,new Color(textRValue.get(),textGValue.get(),textBValue.get()).getRGB(),shadowValue.get());
        Fonts.font35.drawString("Kills/Deaths",5,5 + Fonts.font40.FONT_HEIGHT + 5 + Fonts.font35.FONT_HEIGHT + 2,new Color(keyRValue.get(),keyGValue.get(),keyBValue.get()).getRGB(),shadowValue.get());
        Fonts.font35.drawString(SessionInfoUtils.getKills() + "/" + SessionInfoUtils.getDeaths(),145 - Fonts.font35.getStringWidth(SessionInfoUtils.getKills() + "/" + SessionInfoUtils.getDeaths()),5 + Fonts.font40.FONT_HEIGHT + 5 + Fonts.font35.FONT_HEIGHT + 2,new Color(textRValue.get(),textGValue.get(),textBValue.get()).getRGB(),shadowValue.get());
        Fonts.font35.drawString("Win/Lose",5,5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 2,new Color(keyRValue.get(),keyGValue.get(),keyBValue.get()).getRGB(),shadowValue.get());
        if (SessionInfoUtils.isInSupportedServer()) {
            Fonts.font35.drawString(SessionInfoUtils.getWins() + "/" + SessionInfoUtils.getLoses(),145 - Fonts.font35.getStringWidth(SessionInfoUtils.getWins() + "/" + SessionInfoUtils.getLoses()),5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 2,new Color(textRValue.get(),textGValue.get(),textBValue.get()).getRGB(),shadowValue.get());
        } else {
            Fonts.font35.drawString("Not supported server",145 - Fonts.font35.getStringWidth("Not supported server"),5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 2,new Color(255,80,80).getRGB(),shadowValue.get());
        }
        Fonts.font35.drawString("PlayTime",5,
                5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) * 3
                ,new Color(keyRValue.get(),keyGValue.get(),keyBValue.get()).getRGB(),shadowValue.get());
        Fonts.font35.drawString(SessionInfoUtils.getPlayTimeH() + "h " + SessionInfoUtils.getPlayTimeM() + "m " + SessionInfoUtils.getPlayTimeS() + "s",145 - Fonts.font35.getStringWidth(
                SessionInfoUtils.getPlayTimeH() + "h " + SessionInfoUtils.getPlayTimeM() + "m " + SessionInfoUtils.getPlayTimeS() + "s"
        ),5 + Fonts.font40.FONT_HEIGHT + 5 + (Fonts.font35.FONT_HEIGHT + 2) *
                3,new Color(textRValue.get(),textGValue.get(),textBValue.get()).getRGB(),shadowValue.get());

        return new Border(0,0,150,(Fonts.font35.FONT_HEIGHT + 2) * 4 + Fonts.font40.FONT_HEIGHT + 10);
    }
}
