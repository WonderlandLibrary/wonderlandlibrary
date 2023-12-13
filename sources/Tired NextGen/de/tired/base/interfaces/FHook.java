package de.tired.base.interfaces;

import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;

import java.awt.*;

public interface FHook {

    CustomFont fontRenderer = FontManager.SFUIMedium20;

    CustomFont big = new CustomFont(new Font("Arial", Font.BOLD, 40), true, true);
    CustomFont login = new CustomFont(new Font("Arial", Font.BOLD, 33), true, true);
    CustomFont fontRenderer2 = new CustomFont(new Font("Arial", Font.BOLD, 20), true, true);
    CustomFont fontRenderer4 = new CustomFont(new Font("Arial", Font.PLAIN, 7), true, true);
}
