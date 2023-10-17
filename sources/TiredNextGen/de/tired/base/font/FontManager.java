package de.tired.base.font;

import de.tired.base.interfaces.IHook;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager implements IHook {

    public static String interLightLocation = "Inter-Light.ttf", confortaaLocation = "confortaa.ttf", SFMediumLocation = "SFUIMedium.ttf", raleWay = "Raleway.ttf";

    public static CustomFont entypo;
    public static CustomFont entypoSmall;
    public static CustomFont bebasF;
    public static CustomFont bebasFBig;
    public static CustomFont notosansF;

   public static final Map<String, Font> locationMap = new HashMap<>();

    public static CustomFont notosansFBig;

    public static CustomFont spotifyButtons;

    public static CustomFont logo40;

    public static CustomFont raleWay30, raleWay40, raleWay20, raleWay15, raleWay10;

    public static CustomFont futuraBig, futuraNormal, futuraSmall;

    public static CustomFont IBMPlexSans;
    public static CustomFont SFPRO;

    public static CustomFont SFUIMedium20;

    public static CustomFont SFPROBig;

    public static CustomFont interSemiBold24;

    public static CustomFont interSemiBold20;

    public static CustomFont interMedium14;

    public static CustomFont interMedium10;

    public static CustomFont interLight20;
    public static CustomFont interLight16;

    public static CustomFont robotoF;
    public static CustomFont confortaa;
    public static CustomFont confortaaBig;
    public static CustomFont inter;
    public static CustomFont robotoT;

    public static CustomFont moonF;

    public static CustomFont iconFont, iconFontSmall, iconFontHuge;

    @Getter
    @Setter
    public double size;

    public static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = FontManager.class.getResourceAsStream("/assets/minecraft/client/fonts/" + location);
                assert is != null;
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font " + location);
            font = new Font("default", Font.PLAIN, +10);
        }

        return font;
    }

    public void bootstrap() {

        final int SIZE = getSize() == 0 ? 20 : (int) getSize();

        SFPRO = new CustomFont(getFont(locationMap, "SFPro.ttf", getSize() == 0 ? 21 : (int) getSize()), true, true);

        SFPROBig = new CustomFont(getFont(locationMap, "SFPro.ttf", 34), true, true);

        futuraBig = new CustomFont(getFont(locationMap, "Futura.ttf", 29), true, true);

        SFUIMedium20 = new CustomFont(getFont(locationMap, "SFUIMedium.ttf", 19), true, true);

        futuraNormal = new CustomFont(getFont(locationMap, "Futura.ttf", 19), true, true);

        futuraSmall = new CustomFont(getFont(locationMap, "Futura.ttf", 14), true, true);

        raleWay20 = new CustomFont(getFont(locationMap, "Raleway.ttf", 20), true, true);

        raleWay15 = new CustomFont(getFont(locationMap, "Raleway.ttf", 15), true, true);

        interSemiBold24 = new CustomFont(getFont(locationMap, "Inter-SemiBold.ttf", 24), true, true);

        interSemiBold20 = new CustomFont(getFont(locationMap, "Inter-SemiBold.ttf", 20), true, true);

        interMedium14 = new CustomFont(getFont(locationMap, "Inter-Medium.ttf", 14), true, true);
        interMedium10 = new CustomFont(getFont(locationMap, "Inter-Medium.ttf", 10), true, true);

        interLight20 = new CustomFont(getFont(locationMap, "Inter-Light.ttf", 20), true, true);
        interLight16 = new CustomFont(getFont(locationMap, "Inter-Light.ttf", 16), true, true);

        raleWay10 = new CustomFont(getFont(locationMap, "Raleway.ttf", 10), true, true);

        iconFont = new CustomFont(getFont(locationMap, "SearchIcon.ttf", 20), true, true);

        iconFontHuge = new CustomFont(getFont(locationMap, "SearchIcon.ttf", 70), true, true);

        iconFontSmall = new CustomFont(getFont(locationMap, "SearchIcon.ttf", 15), true, true);

        logo40 = new CustomFont(getFont(locationMap, "Logo.ttf", 80), true, true);

        raleWay30 = new CustomFont(getFont(locationMap, "Raleway.ttf", 30), true, true);

        raleWay40 = new CustomFont(getFont(locationMap, "Raleway.ttf", 40), true, true);

        IBMPlexSans = new CustomFont(getFont(locationMap, "IBMPlexSans.ttf", 21), true, true);

        confortaaBig = new CustomFont(getFont(locationMap, "confortaa.ttf", getSize() == 0 ? 28 : (int) getSize()), true, true);

        spotifyButtons = new CustomFont(getFont(locationMap, "SpotifyIcons.ttf", 30), true, true);

        inter = new CustomFont(getFont(locationMap, "inter.ttf", getSize() == 0 ? 23 : (int) getSize()), true, true);

        confortaa = new CustomFont(getFont(locationMap, "confortaa.ttf", getSize() == 0 ? 18 : (int) getSize()), true, true);

        bebasF = new CustomFont(getFont(locationMap, "bebas.ttf", getSize() == 0 ? 25 : (int) getSize()), true, true);

        bebasFBig = new CustomFont(getFont(locationMap, "bebas.ttf", getSize() == 0 ? 45 : (int) getSize()), true, true);

        notosansFBig = new CustomFont(getFont(locationMap, "notosans.ttf", getSize() == 0 ? 33 : (int) getSize()), true, true);

        notosansF = new CustomFont(getFont(locationMap, "notosans.ttf", SIZE), true, true);

        robotoF = new CustomFont(getFont(locationMap, "roboto.ttf", SIZE), true, true);

        entypo = new CustomFont(getFont(locationMap, "entypo.otf", getSize() == 0 ? 42 : (int) getSize()), true, true);

        entypoSmall = new CustomFont(getFont(locationMap, "entypo.otf", getSize() == 0 ? 32 : (int) getSize()), true, true);

        moonF = new CustomFont(getFont(locationMap, "moon.ttf", getSize() == 0 ? 21 : (int) getSize()), true, true);

        robotoT = new CustomFont(getFont(locationMap, "robotoT.ttf", 20), true, true);

    }

}
