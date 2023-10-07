package cc.novoline.utils.fonts.impl;

import cc.novoline.Novoline;
import cc.novoline.utils.fonts.api.FontFamily;
import cc.novoline.utils.fonts.api.FontManager;
import cc.novoline.utils.fonts.api.FontRenderer;
import cc.novoline.utils.fonts.api.FontType;

/**
 * @author Artyom Popov
 * @since June 30, 2020
 */
@SuppressWarnings("SpellCheckingInspection")
public interface Fonts {

	FontManager FONT_MANAGER = Novoline.getInstance().getFontManager();

	interface OXIDE {
		FontFamily OXIDE = FONT_MANAGER.fontFamily(FontType.OXIDE);

		final class OXIDE_55 { public static final FontRenderer OXIDE_55 = OXIDE.ofSize(40); private OXIDE_55() {} }
		final class OXIDE_18 { public static final FontRenderer OXIDE_18 = OXIDE.ofSize(16); private OXIDE_18() {} }
	}

	interface ICONFONT {

		FontFamily ICONFONT = FONT_MANAGER.fontFamily(FontType.ICONFONT);

		final class ICONFONT_16 { public static final FontRenderer ICONFONT_16 = ICONFONT.ofSize(16); private ICONFONT_16() {} }
		final class ICONFONT_24 { public static final FontRenderer ICONFONT_24 = ICONFONT.ofSize(24); private ICONFONT_24() {} }
		final class ICONFONT_35 { public static final FontRenderer ICONFONT_35 = ICONFONT.ofSize(35); private ICONFONT_35() {} }
		final class ICONFONT_50 { public static final FontRenderer ICONFONT_50 = ICONFONT.ofSize(50); private ICONFONT_50() {} }
	}

	interface SF {

		FontFamily SF = FONT_MANAGER.fontFamily(FontType.SF);

		final class SF_14 { public static final FontRenderer SF_14 = SF.ofSize(14); private SF_14() {} }
		final class SF_15 { public static final FontRenderer SF_15 = SF.ofSize(15); private SF_15() {} }
		final class SF_16 { public static final FontRenderer SF_16 = SF.ofSize(16); private SF_16() {} }
		final class SF_17 { public static final FontRenderer SF_17 = SF.ofSize(17); private SF_17() {} }
		final class SF_18 { public static final FontRenderer SF_18 = SF.ofSize(18); private SF_18() {} }
		final class SF_19 { public static final FontRenderer SF_19 = SF.ofSize(19); private SF_19() {} }
		final class SF_20 { public static final FontRenderer SF_20 = SF.ofSize(20); private SF_20() {} }
		final class SF_21 { public static final FontRenderer SF_21 = SF.ofSize(21); private SF_21() {} }
		final class SF_22 { public static final FontRenderer SF_22 = SF.ofSize(22); private SF_22() {} }
		final class SF_23 { public static final FontRenderer SF_23 = SF.ofSize(23); private SF_23() {} }
		final class SF_24 { public static final FontRenderer SF_24 = SF.ofSize(24); private SF_24() {} }
		final class SF_25 { public static final FontRenderer SF_25 = SF.ofSize(25); private SF_25() {} }
		final class SF_26 { public static final FontRenderer SF_26 = SF.ofSize(26); private SF_26() {} }
		final class SF_27 { public static final FontRenderer SF_27 = SF.ofSize(27); private SF_27() {} }
		final class SF_28 { public static final FontRenderer SF_28 = SF.ofSize(28); private SF_28() {} }
		final class SF_29 { public static final FontRenderer SF_29 = SF.ofSize(29); private SF_29() {} }
		final class SF_30 { public static final FontRenderer SF_30 = SF.ofSize(30); private SF_30() {} }
		final class SF_31 { public static final FontRenderer SF_31 = SF.ofSize(31); private SF_31() {} }
		final class SF_50 { public static final FontRenderer SF_50 = SF.ofSize(45); private SF_50() {} }
	}

	interface SFTHIN {

		FontFamily SFTHIN = FONT_MANAGER.fontFamily(FontType.SFTHIN);

		final class SFTHIN_10 { public static final FontRenderer SFTHIN_10 = SFTHIN.ofSize(10); private SFTHIN_10() {} }
		final class SFTHIN_12 { public static final FontRenderer SFTHIN_12 = SFTHIN.ofSize(12); private SFTHIN_12() {} }
		final class SFTHIN_16 { public static final FontRenderer SFTHIN_16 = SFTHIN.ofSize(16); private SFTHIN_16() {} }
		final class SFTHIN_17 { public static final FontRenderer SFTHIN_17 = SFTHIN.ofSize(17); private SFTHIN_17() {} }
		final class SFTHIN_18 { public static final FontRenderer SFTHIN_18 = SFTHIN.ofSize(18); private SFTHIN_18() {} }
		final class SFTHIN_19 { public static final FontRenderer SFTHIN_19 = SFTHIN.ofSize(19); private SFTHIN_19() {} }
		final class SFTHIN_20 { public static final FontRenderer SFTHIN_20 = SFTHIN.ofSize(20); private SFTHIN_20() {} }
		final class SFTHIN_28 { public static final FontRenderer SFTHIN_28 = SFTHIN.ofSize(28); private SFTHIN_28() {} }
	}

	interface SFBOLD {

		FontFamily SFBOLD = FONT_MANAGER.fontFamily(FontType.SFBOLD);

		final class SFBOLD_12 { public static final FontRenderer SFBOLD_12 = SFBOLD.ofSize(12); private SFBOLD_12() {} }
		final class SFBOLD_16 { public static final FontRenderer SFBOLD_16 = SFBOLD.ofSize(16); private SFBOLD_16() {} }
		final class SFBOLD_18 { public static final FontRenderer SFBOLD_18 = SFBOLD.ofSize(18); private SFBOLD_18() {} }
		final class SFBOLD_20 { public static final FontRenderer SFBOLD_20 = SFBOLD.ofSize(20); private SFBOLD_20() {} }
		final class SFBOLD_22 { public static final FontRenderer SFBOLD_22 = SFBOLD.ofSize(22); private SFBOLD_22() {} }
		final class SFBOLD_26 { public static final FontRenderer SFBOLD_26 = SFBOLD.ofSize(26); private SFBOLD_26() {} }
		final class SFBOLD_28 { public static final FontRenderer SFBOLD_28 = SFBOLD.ofSize(28); private SFBOLD_28() {} }
		final class SFBOLD_35 { public static final FontRenderer SFBOLD_35 = SFBOLD.ofSize(35); private SFBOLD_35() {} }
	}
}
