/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import tk.rektsky.event.Event;
import tk.rektsky.event.EventManager;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.module.specials.flys.ClanCraftFly;
import tk.rektsky.module.specials.flys.OldFly;
import tk.rektsky.module.specials.flys.VerusDamage;
import tk.rektsky.module.specials.flys.VerusFlat;
import tk.rektsky.module.specials.flys.VerusFly;
import tk.rektsky.module.specials.flys.VerusFunny;

public class Fly
extends Module {
    public BooleanSetting oldDisabler = new BooleanSetting("Old Disabler", false);
    public BooleanSetting damage = new BooleanSetting("Damage Delay", true);
    public DoubleSetting flySpeed = new DoubleSetting("Speed", 0.85, 9.69, 7.69);
    public DoubleSetting zoomDuration = new DoubleSetting("Zoom Duration", 1.0, 60.0, 8.0);
    public DoubleSetting timer = new DoubleSetting("Timer", 0.1, 5.0, 0.7);
    public BooleanSetting vanillaBypass = new BooleanSetting("Vanilla Bypass", false);
    public DoubleSetting stableFlySpeed = new DoubleSetting("Stable Speed", 0.5, 9.0, 0.8);
    public ListSetting mode = new ListSetting("Mode", new String[]{"Vanilla", "Stable", "Motion", "Verus", "Verus Damage", "Verus Flatten", "Verus FunnyFly", "ClanCraft"}, "Verus");
    public static ClanCraftFly clanCraftFly = new ClanCraftFly();
    public static OldFly oldFly = new OldFly();
    public static VerusDamage verusDamage = new VerusDamage();
    public static VerusFly verusFly = new VerusFly();
    public static VerusFlat verusFlat = new VerusFlat();
    private static VerusFunny verusFunny = new VerusFunny();

    public Fly() {
        super("Fly", "Makes you fly like a p2w player", 0, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        try {
            switch (this.mode.getValue()) {
                case "Verus": {
                    EventManager.EVENT_BUS.register(verusFly);
                    verusFly.onEnable();
                    break;
                }
                case "Verus Damage": {
                    EventManager.EVENT_BUS.register(verusDamage);
                    verusDamage.onEnable();
                    break;
                }
                case "Verus Flatten": {
                    EventManager.EVENT_BUS.register(verusFlat);
                    verusFlat.onEnable();
                    break;
                }
                case "Verus FunnyFly": {
                    EventManager.EVENT_BUS.register(verusFunny);
                    verusFunny.onEnable();
                    break;
                }
                case "ClanCraft": {
                    EventManager.EVENT_BUS.register(clanCraftFly);
                    clanCraftFly.onEnable();
                    break;
                }
                default: {
                    EventManager.EVENT_BUS.register(oldFly);
                    oldFly.onEnable();
                    break;
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public String getSuffix() {
        return this.mode.getValue();
    }

    @Override
    public void onDisable() {
        switch (this.mode.getValue()) {
            case "Verus": {
                EventManager.EVENT_BUS.unregister(verusFly);
                verusFly.onDisable();
                break;
            }
            case "Verus Damage": {
                EventManager.EVENT_BUS.unregister(verusDamage);
                verusDamage.onDisable();
                break;
            }
            case "Verus Flatten": {
                EventManager.EVENT_BUS.unregister(verusFlat);
                verusFlat.onDisable();
                break;
            }
            case "Verus FunnyFly": {
                EventManager.EVENT_BUS.unregister(verusFunny);
                verusFunny.onDisable();
                break;
            }
            case "ClanCraft": {
                EventManager.EVENT_BUS.unregister(clanCraftFly);
                clanCraftFly.onDisable();
            }
            default: {
                EventManager.EVENT_BUS.unregister(oldFly);
                oldFly.onDisable();
            }
        }
    }

    @Override
    public void unregisterListeners() {
        EventManager.EVENT_BUS.unregister(verusFly);
        verusFly.onDisable();
        EventManager.EVENT_BUS.unregister(verusDamage);
        verusDamage.onDisable();
        EventManager.EVENT_BUS.unregister(verusFlat);
        verusFlat.onDisable();
        EventManager.EVENT_BUS.unregister(clanCraftFly);
        clanCraftFly.onDisable();
        EventManager.EVENT_BUS.unregister(oldFly);
        oldFly.onDisable();
    }

    @Override
    public void onEvent(Event event) {
        switch (this.mode.getValue()) {
            case "Verus": {
                break;
            }
            case "Verus Damage": {
                verusDamage.onEvent(event);
                break;
            }
            case "Verus Flatten": {
                break;
            }
            case "Verus FunnyFly": {
                break;
            }
            case "ClanCraft": {
                break;
            }
        }
    }
}

