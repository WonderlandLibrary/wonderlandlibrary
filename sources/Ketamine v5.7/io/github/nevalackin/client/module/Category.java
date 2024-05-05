package io.github.nevalackin.client.module;

public enum Category {
    COMBAT("Combat", SubCategory.COMBAT_RAGE, SubCategory.COMBAT_LEGIT, SubCategory.COMBAT_MINI_GAMES, SubCategory.COMBAT_HEALING),
    MOVEMENT("Movement", SubCategory.MOVEMENT_MAIN, SubCategory.MOVEMENT_EXTRAS),
    MISC("Miscellaneous", SubCategory.MISC_INVENTORY, SubCategory.MISC_WORLD, SubCategory.MISC_PLAYER),
    RENDER("Render", SubCategory.RENDER_MODEL, SubCategory.RENDER_ESP, SubCategory.RENDER_WORLD, SubCategory.RENDER_SELF, SubCategory.RENDER_OVERLAY);

    private final String name;
    private final SubCategory[] subCategories;

    Category(String name, SubCategory... subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }

    public SubCategory[] getSubCategories() {
        return subCategories;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public enum SubCategory {
        // Combat
        COMBAT_RAGE("Rage"),
        COMBAT_LEGIT("Legit"),
        COMBAT_MINI_GAMES("Mini Games"),
        COMBAT_HEALING("Healing"),

        // Movement
        MOVEMENT_MAIN("Main"),
        MOVEMENT_EXTRAS("Extras"),

        // Misc
        MISC_INVENTORY("Inventory"),
        MISC_WORLD("World"),
        MISC_PLAYER("Player"),

        // Render
        RENDER_MODEL("Model"),
        RENDER_ESP("ESP"),
        RENDER_WORLD("World"),
        RENDER_SELF("Self"),
        RENDER_OVERLAY("Overlay");

        private final String name;

        SubCategory(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
