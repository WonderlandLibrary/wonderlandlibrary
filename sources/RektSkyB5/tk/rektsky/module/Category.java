/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module;

import net.minecraft.util.ResourceLocation;

public enum Category {
    COMBAT("Combat", "Combat Hacks, e.g. Killaura", new ResourceLocation("rektsky/clickgui/combat.png")),
    MOVEMENT("Movement", "Some modules about movement e.g. Fly or Speed", new ResourceLocation("rektsky/clickgui/movement.png")),
    RENDER("Render", "Render more info on your screen e.g. Tracers", new ResourceLocation("rektsky/clickgui/render.png")),
    PLAYER("Player", "Player Hacks e.g. ChestStealer", new ResourceLocation("rektsky/clickgui/player.png")),
    EXPLOIT("Exploits", "Exploit the server : )", new ResourceLocation("rektsky/clickgui/exploit.png")),
    REKTSKY("RektSky", "AKA Misc : )", new ResourceLocation("rektsky/clickgui/rektsky.png")),
    WORLD("World", "World Hacks e.g. BedRekter", new ResourceLocation("rektsky/clickgui/world.png"));

    private String name;
    private String description;
    private boolean enabled = true;
    private ResourceLocation icon;

    private Category(String name, String descripton, ResourceLocation icon) {
        this.name = name;
        this.description = this.description;
        this.icon = icon;
    }

    public ResourceLocation getIcon() {
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

