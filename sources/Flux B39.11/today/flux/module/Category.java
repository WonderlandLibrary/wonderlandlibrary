package today.flux.module;

public enum Category {
	
	Combat("a"), Movement("b"), Render("c"), Player("d"), World("e"), Ghost("f"), Misc("g"), Global("m");

	public String icon;
	
	Category(String string) {
		icon = string;
	}
}
