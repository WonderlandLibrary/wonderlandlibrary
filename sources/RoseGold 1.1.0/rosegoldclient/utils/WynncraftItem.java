package rosegoldclient.utils;

import java.awt.*;

public class WynncraftItem {

    private String name;
    private String tier;
    private String type;
    private int level;

    public WynncraftItem(String name, String tier, String type, int level) {
        this.name = name;
        this.tier = tier;
        this.type = type;
        this.level = level;
    }

    public Color getColor() {
        switch (this.tier) {
            case "Legendary":
                return new Color(85,255,255);
            case "Fabled":
                return new Color(255,85,85);
            case "Mythic":
                return new Color(170,0,170);
            default:
                return new Color(255, 255, 255);
        }
    }

    public String getTextColor() {
        switch (this.tier) {
            case "Legendary":
                return "§b";
            case "Fabled":
                return "§c";
            case "Mythic":
                return "§5";
            default:
                return "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
