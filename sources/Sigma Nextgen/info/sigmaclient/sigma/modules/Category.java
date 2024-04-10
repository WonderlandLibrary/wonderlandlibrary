package info.sigmaclient.sigma.modules;

import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;

import java.util.Locale;

public enum Category {
    Gui,
    Combat("Combat", "CombatCat.png", 2),
    Render("Render", "RenderCat.png", 4),
    World,
    Misc,
    Player("Player", "PlayerCat.png", 1),
    Item("Item", "ItemCat.png", 3),
    Movement("Movement", "MovementCat.png", 0);
    public float seenTrans;
    public PartialTicksAnim anim = new PartialTicksAnim(0);

    public String location;
    public int selectedIndex;
    public float selectedTrans;
    public float lastSelectedTrans;
    public PartialTicksAnim anim2 = new PartialTicksAnim(0);

    public String n;
    public int index;
    Category(String n, String name, final int index) {
        this.location = name.toLowerCase(Locale.ROOT);
        this.index = index;
        this.n=n;
    }
    Category(){

    }

}
