package javafx.scene.input;

import java.util.HashMap;
import java.util.Map;

public enum KeyCode {
   ENTER(10, "Enter", 128),
   BACK_SPACE(8, "Backspace"),
   TAB(9, "Tab", 128),
   CANCEL(3, "Cancel"),
   CLEAR(12, "Clear"),
   SHIFT(16, "Shift", 8),
   CONTROL(17, "Ctrl", 8),
   ALT(18, "Alt", 8),
   PAUSE(19, "Pause"),
   CAPS(20, "Caps Lock"),
   ESCAPE(27, "Esc"),
   SPACE(32, "Space", 128),
   PAGE_UP(33, "Page Up", 2),
   PAGE_DOWN(34, "Page Down", 2),
   END(35, "End", 2),
   HOME(36, "Home", 2),
   LEFT(37, "Left", 6),
   UP(38, "Up", 6),
   RIGHT(39, "Right", 6),
   DOWN(40, "Down", 6),
   COMMA(44, "Comma"),
   MINUS(45, "Minus"),
   PERIOD(46, "Period"),
   SLASH(47, "Slash"),
   DIGIT0(48, "0", 32),
   DIGIT1(49, "1", 32),
   DIGIT2(50, "2", 32),
   DIGIT3(51, "3", 32),
   DIGIT4(52, "4", 32),
   DIGIT5(53, "5", 32),
   DIGIT6(54, "6", 32),
   DIGIT7(55, "7", 32),
   DIGIT8(56, "8", 32),
   DIGIT9(57, "9", 32),
   SEMICOLON(59, "Semicolon"),
   EQUALS(61, "Equals"),
   A(65, "A", 16),
   B(66, "B", 16),
   C(67, "C", 16),
   D(68, "D", 16),
   E(69, "E", 16),
   F(70, "F", 16),
   G(71, "G", 16),
   H(72, "H", 16),
   I(73, "I", 16),
   J(74, "J", 16),
   K(75, "K", 16),
   L(76, "L", 16),
   M(77, "M", 16),
   N(78, "N", 16),
   O(79, "O", 16),
   P(80, "P", 16),
   Q(81, "Q", 16),
   R(82, "R", 16),
   S(83, "S", 16),
   T(84, "T", 16),
   U(85, "U", 16),
   V(86, "V", 16),
   W(87, "W", 16),
   X(88, "X", 16),
   Y(89, "Y", 16),
   Z(90, "Z", 16),
   OPEN_BRACKET(91, "Open Bracket"),
   BACK_SLASH(92, "Back Slash"),
   CLOSE_BRACKET(93, "Close Bracket"),
   NUMPAD0(96, "Numpad 0", 96),
   NUMPAD1(97, "Numpad 1", 96),
   NUMPAD2(98, "Numpad 2", 96),
   NUMPAD3(99, "Numpad 3", 96),
   NUMPAD4(100, "Numpad 4", 96),
   NUMPAD5(101, "Numpad 5", 96),
   NUMPAD6(102, "Numpad 6", 96),
   NUMPAD7(103, "Numpad 7", 96),
   NUMPAD8(104, "Numpad 8", 96),
   NUMPAD9(105, "Numpad 9", 96),
   MULTIPLY(106, "Multiply"),
   ADD(107, "Add"),
   SEPARATOR(108, "Separator"),
   SUBTRACT(109, "Subtract"),
   DECIMAL(110, "Decimal"),
   DIVIDE(111, "Divide"),
   DELETE(127, "Delete"),
   NUM_LOCK(144, "Num Lock"),
   SCROLL_LOCK(145, "Scroll Lock"),
   F1(112, "F1", 1),
   F2(113, "F2", 1),
   F3(114, "F3", 1),
   F4(115, "F4", 1),
   F5(116, "F5", 1),
   F6(117, "F6", 1),
   F7(118, "F7", 1),
   F8(119, "F8", 1),
   F9(120, "F9", 1),
   F10(121, "F10", 1),
   F11(122, "F11", 1),
   F12(123, "F12", 1),
   F13(61440, "F13", 1),
   F14(61441, "F14", 1),
   F15(61442, "F15", 1),
   F16(61443, "F16", 1),
   F17(61444, "F17", 1),
   F18(61445, "F18", 1),
   F19(61446, "F19", 1),
   F20(61447, "F20", 1),
   F21(61448, "F21", 1),
   F22(61449, "F22", 1),
   F23(61450, "F23", 1),
   F24(61451, "F24", 1),
   PRINTSCREEN(154, "Print Screen"),
   INSERT(155, "Insert"),
   HELP(156, "Help"),
   META(157, "Meta", 8),
   BACK_QUOTE(192, "Back Quote"),
   QUOTE(222, "Quote"),
   KP_UP(224, "Numpad Up", 70),
   KP_DOWN(225, "Numpad Down", 70),
   KP_LEFT(226, "Numpad Left", 70),
   KP_RIGHT(227, "Numpad Right", 70),
   DEAD_GRAVE(128, "Dead Grave"),
   DEAD_ACUTE(129, "Dead Acute"),
   DEAD_CIRCUMFLEX(130, "Dead Circumflex"),
   DEAD_TILDE(131, "Dead Tilde"),
   DEAD_MACRON(132, "Dead Macron"),
   DEAD_BREVE(133, "Dead Breve"),
   DEAD_ABOVEDOT(134, "Dead Abovedot"),
   DEAD_DIAERESIS(135, "Dead Diaeresis"),
   DEAD_ABOVERING(136, "Dead Abovering"),
   DEAD_DOUBLEACUTE(137, "Dead Doubleacute"),
   DEAD_CARON(138, "Dead Caron"),
   DEAD_CEDILLA(139, "Dead Cedilla"),
   DEAD_OGONEK(140, "Dead Ogonek"),
   DEAD_IOTA(141, "Dead Iota"),
   DEAD_VOICED_SOUND(142, "Dead Voiced Sound"),
   DEAD_SEMIVOICED_SOUND(143, "Dead Semivoiced Sound"),
   AMPERSAND(150, "Ampersand"),
   ASTERISK(151, "Asterisk"),
   QUOTEDBL(152, "Double Quote"),
   LESS(153, "Less"),
   GREATER(160, "Greater"),
   BRACELEFT(161, "Left Brace"),
   BRACERIGHT(162, "Right Brace"),
   AT(512, "At"),
   COLON(513, "Colon"),
   CIRCUMFLEX(514, "Circumflex"),
   DOLLAR(515, "Dollar"),
   EURO_SIGN(516, "Euro Sign"),
   EXCLAMATION_MARK(517, "Exclamation Mark"),
   INVERTED_EXCLAMATION_MARK(518, "Inverted Exclamation Mark"),
   LEFT_PARENTHESIS(519, "Left Parenthesis"),
   NUMBER_SIGN(520, "Number Sign"),
   PLUS(521, "Plus"),
   RIGHT_PARENTHESIS(522, "Right Parenthesis"),
   UNDERSCORE(523, "Underscore"),
   WINDOWS(524, "Windows", 8),
   CONTEXT_MENU(525, "Context Menu"),
   FINAL(24, "Final"),
   CONVERT(28, "Convert"),
   NONCONVERT(29, "Nonconvert"),
   ACCEPT(30, "Accept"),
   MODECHANGE(31, "Mode Change"),
   KANA(21, "Kana"),
   KANJI(25, "Kanji"),
   ALPHANUMERIC(240, "Alphanumeric"),
   KATAKANA(241, "Katakana"),
   HIRAGANA(242, "Hiragana"),
   FULL_WIDTH(243, "Full Width"),
   HALF_WIDTH(244, "Half Width"),
   ROMAN_CHARACTERS(245, "Roman Characters"),
   ALL_CANDIDATES(256, "All Candidates"),
   PREVIOUS_CANDIDATE(257, "Previous Candidate"),
   CODE_INPUT(258, "Code Input"),
   JAPANESE_KATAKANA(259, "Japanese Katakana"),
   JAPANESE_HIRAGANA(260, "Japanese Hiragana"),
   JAPANESE_ROMAN(261, "Japanese Roman"),
   KANA_LOCK(262, "Kana Lock"),
   INPUT_METHOD_ON_OFF(263, "Input Method On/Off"),
   CUT(65489, "Cut"),
   COPY(65485, "Copy"),
   PASTE(65487, "Paste"),
   UNDO(65483, "Undo"),
   AGAIN(65481, "Again"),
   FIND(65488, "Find"),
   PROPS(65482, "Properties"),
   STOP(65480, "Stop"),
   COMPOSE(65312, "Compose"),
   ALT_GRAPH(65406, "Alt Graph", 8),
   BEGIN(65368, "Begin"),
   UNDEFINED(0, "Undefined"),
   SOFTKEY_0(4096, "Softkey 0"),
   SOFTKEY_1(4097, "Softkey 1"),
   SOFTKEY_2(4098, "Softkey 2"),
   SOFTKEY_3(4099, "Softkey 3"),
   SOFTKEY_4(4100, "Softkey 4"),
   SOFTKEY_5(4101, "Softkey 5"),
   SOFTKEY_6(4102, "Softkey 6"),
   SOFTKEY_7(4103, "Softkey 7"),
   SOFTKEY_8(4104, "Softkey 8"),
   SOFTKEY_9(4105, "Softkey 9"),
   GAME_A(4106, "Game A"),
   GAME_B(4107, "Game B"),
   GAME_C(4108, "Game C"),
   GAME_D(4109, "Game D"),
   STAR(4110, "Star"),
   POUND(4111, "Pound"),
   POWER(409, "Power"),
   INFO(457, "Info"),
   COLORED_KEY_0(403, "Colored Key 0"),
   COLORED_KEY_1(404, "Colored Key 1"),
   COLORED_KEY_2(405, "Colored Key 2"),
   COLORED_KEY_3(406, "Colored Key 3"),
   EJECT_TOGGLE(414, "Eject", 256),
   PLAY(415, "Play", 256),
   RECORD(416, "Record", 256),
   FAST_FWD(417, "Fast Forward", 256),
   REWIND(412, "Rewind", 256),
   TRACK_PREV(424, "Previous Track", 256),
   TRACK_NEXT(425, "Next Track", 256),
   CHANNEL_UP(427, "Channel Up", 256),
   CHANNEL_DOWN(428, "Channel Down", 256),
   VOLUME_UP(447, "Volume Up", 256),
   VOLUME_DOWN(448, "Volume Down", 256),
   MUTE(449, "Mute", 256),
   COMMAND(768, "Command", 8),
   SHORTCUT(-1, "Shortcut");

   final int code;
   final String ch;
   final String name;
   private int mask;
   private static final Map nameMap = new HashMap(values().length);

   private KeyCode(int var3, String var4, int var5) {
      this.code = var3;
      this.name = var4;
      this.mask = var5;
      this.ch = String.valueOf((char)var3);
   }

   private KeyCode(int var3, String var4) {
      this(var3, var4, 0);
   }

   public final boolean isFunctionKey() {
      return (this.mask & 1) != 0;
   }

   public final boolean isNavigationKey() {
      return (this.mask & 2) != 0;
   }

   public final boolean isArrowKey() {
      return (this.mask & 4) != 0;
   }

   public final boolean isModifierKey() {
      return (this.mask & 8) != 0;
   }

   public final boolean isLetterKey() {
      return (this.mask & 16) != 0;
   }

   public final boolean isDigitKey() {
      return (this.mask & 32) != 0;
   }

   public final boolean isKeypadKey() {
      return (this.mask & 64) != 0;
   }

   public final boolean isWhitespaceKey() {
      return (this.mask & 128) != 0;
   }

   public final boolean isMediaKey() {
      return (this.mask & 256) != 0;
   }

   public final String getName() {
      return this.name;
   }

   /** @deprecated */
   @Deprecated
   public String impl_getChar() {
      return this.ch;
   }

   /** @deprecated */
   @Deprecated
   public int impl_getCode() {
      return this.code;
   }

   public static KeyCode getKeyCode(String var0) {
      return (KeyCode)nameMap.get(var0);
   }

   static {
      KeyCode[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         KeyCode var3 = var0[var2];
         nameMap.put(var3.name, var3);
      }

   }

   private static class KeyCodeClass {
      private static final int FUNCTION = 1;
      private static final int NAVIGATION = 2;
      private static final int ARROW = 4;
      private static final int MODIFIER = 8;
      private static final int LETTER = 16;
      private static final int DIGIT = 32;
      private static final int KEYPAD = 64;
      private static final int WHITESPACE = 128;
      private static final int MEDIA = 256;
   }
}
