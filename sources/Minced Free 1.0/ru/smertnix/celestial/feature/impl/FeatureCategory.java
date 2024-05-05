package ru.smertnix.celestial.feature.impl;

public enum FeatureCategory {
    Combat("Combat"),
    Movement("Movement"),
    Render("Render"),
    Player("Player"),
    Util("Util"),
	Configs("Configs");
    private final String displayName;

    FeatureCategory(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
