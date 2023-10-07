package koks.mainmenu.interfaces;

import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.utils.RenderUtil;

import java.awt.*;

public interface Element {
    DirtyFontRenderer closeFont = Fonts.ralewayRegular17, titleFont = Fonts.segoeUIVF25, infoFont = Fonts.arial18, optionFont = Fonts.ralewayRegular25;;
    Color outlineColor = new Color(0x393739), insideColor = new Color(0x141214);
    RenderUtil renderUtil = RenderUtil.getInstance();

    default void draw() {}
}
