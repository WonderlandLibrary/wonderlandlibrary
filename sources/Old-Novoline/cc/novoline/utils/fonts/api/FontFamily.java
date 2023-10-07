package cc.novoline.utils.fonts.api;

/**
 * @author Artyom Popov
 * @since June 30, 2020
 */
public interface FontFamily {

	FontRenderer ofSize(int size);

	FontType font();
}
