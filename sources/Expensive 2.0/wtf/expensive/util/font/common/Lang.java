package wtf.expensive.util.font.common;

public enum Lang {
	
	ENG(new int[] {31, 127, 0, 0}),
	ENG_RU(new int[] {31, 127, 1024, 1106});
	
	private int[] charCodes;
	
	Lang(int[] charCodes) {
		this.charCodes = charCodes;
	}
	
	public int[] getCharCodes() {
		return charCodes;
	}
	
}
