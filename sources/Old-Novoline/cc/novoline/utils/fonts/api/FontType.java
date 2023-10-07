package cc.novoline.utils.fonts.api;

/**
 * @author Artyom Popov
 * @since June 30, 2020
 */
@SuppressWarnings("SpellCheckingInspection")
public enum FontType {

	DM("diramight.ttf"),
	FIXEDSYS("tahoma.ttf"),
	ICONFONT("stylesicons.ttf"),
	SF("SF.ttf"),
	SFBOLD("SFBOLD.ttf"),
	SFTHIN("SFREGULAR.ttf"),
	OXIDE("oxide.ttf");

	private final String fileName;

	FontType(String fileName) {
		this.fileName = fileName;
	}

	public String fileName() { return fileName; }
}
