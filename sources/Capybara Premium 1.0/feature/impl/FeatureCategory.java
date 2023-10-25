package fun.expensive.client.feature.impl;

public enum FeatureCategory {
    Combat("Combat"),
    Movement("Movement"),
    Visuals("Visual"),
    Player("Player"),
    Misc("Misc"),
    Hud("Hud");
    private final String displayName;

    FeatureCategory(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
