/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.ArraysKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.Backend;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.Value;

public class Module
extends MinecraftInstance
implements Listenable {
    private boolean isSupported;
    private String name;
    private String description;
    private ModuleCategory category;
    private int keyBind;
    private boolean array = true;
    private final boolean canEnable;
    private float slideStep;
    private boolean state;
    private final float hue;
    private float slide;
    private boolean BreakName;
    private float higt;

    public final boolean isSupported() {
        return this.isSupported;
    }

    public final void setSupported(boolean bl) {
        this.isSupported = bl;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String string) {
        this.name = string;
    }

    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(String string) {
        this.description = string;
    }

    public final ModuleCategory getCategory() {
        return this.category;
    }

    public final void setCategory(ModuleCategory moduleCategory) {
        this.category = moduleCategory;
    }

    public final int getKeyBind() {
        return this.keyBind;
    }

    public final void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
        if (!LiquidBounce.INSTANCE.isStarting()) {
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
        }
    }

    public final boolean getArray() {
        return this.array;
    }

    public final void setArray(boolean array) {
        this.array = array;
        if (!LiquidBounce.INSTANCE.isStarting()) {
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
        }
    }

    public final float getSlideStep() {
        return this.slideStep;
    }

    public final void setSlideStep(float f) {
        this.slideStep = f;
    }

    public final boolean getState() {
        return this.state;
    }

    public final void setState(boolean value) {
        if (this.state == value) {
            return;
        }
        this.onToggle(value);
        if (!LiquidBounce.INSTANCE.isStarting()) {
            switch (LiquidBounce.INSTANCE.getModuleManager().getToggleSoundMode()) {
                case 2: {
                    (value ? LiquidBounce.INSTANCE.getTipSoundManager().getEnableSound() : LiquidBounce.INSTANCE.getTipSoundManager().getDisableSound()).asyncPlay();
                }
            }
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Module", (value ? "\u5f00\u542f\u6a21\u5757 " : "\u5173\u95ed\u6a21\u5757 ") + this.name, value ? NotifyType.SUCCESS : NotifyType.ERROR, 0, 0, 24, null));
        }
        if (value) {
            this.onEnable();
            if (this.canEnable) {
                this.state = true;
            }
        } else {
            this.onDisable();
            this.state = false;
        }
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
    }

    public final float getHue() {
        return this.hue;
    }

    public final float getSlide() {
        return this.slide;
    }

    public final void setSlide(float f) {
        this.slide = f;
    }

    public final boolean getBreakName() {
        return this.BreakName;
    }

    public final void setBreakName(boolean bl) {
        this.BreakName = bl;
    }

    public final float getHigt() {
        return this.higt;
    }

    public final void setHigt(float f) {
        this.higt = f;
    }

    public String getTag() {
        return null;
    }

    public final String getTagName() {
        return this.name + (this.getTag() == null ? "" : " \u00a77" + this.getTag());
    }

    public final String getColorlessTagName() {
        return this.name + (this.getTag() == null ? "" : " " + ColorUtils.stripColor(this.getTag()));
    }

    public final String breakname(boolean toggle) {
        String detName = this.name;
        if (toggle) {
            String string = detName;
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "autotool": {
                    return "Auto Tool";
                }
                case "noslow": {
                    return "No Slow";
                }
                case "chestaura": {
                    return "Chest Aura";
                }
                case "cheststealer": {
                    return "Chest Stealer";
                }
                case "invcleaner": {
                    return "Inv Cleaner";
                }
                case "invmove": {
                    return "Inv Move";
                }
                case "autopot": {
                    return "Auto Potion";
                }
                case "blockfly": {
                    return "Block Fly";
                }
                case "fastbreak": {
                    return "Fast Break";
                }
                case "fastplace": {
                    return "Fast Place";
                }
                case "noswing": {
                    return "No Swing";
                }
                case "nametags": {
                    return "Name Tags";
                }
                case "itemesp": {
                    return "Item ESP";
                }
                case "freecam": {
                    return "Free Cam";
                }
                case "blockesp": {
                    return "Block ESP";
                }
                case "antiblind": {
                    return "Anti Blind";
                }
                case "blockoverlay": {
                    return "Block Overlay";
                }
                case "nofall": {
                    return "No Fall";
                }
                case "fastuse": {
                    return "Fast Use";
                }
                case "autorespawn": {
                    return "Auto Respawn";
                }
                case "autofish": {
                    return "Auto Fish";
                }
                case "antiafk": {
                    return "Anti AFK";
                }
                case "anticactus": {
                    return "Anti Cactus";
                }
                case "potionsaver": {
                    return "Potion Saver";
                }
                case "safewalk": {
                    return "Safe Walk";
                }
                case "noweb": {
                    return "No Web";
                }
                case "noclip": {
                    return "No Clip";
                }
                case "longjump": {
                    return "Long Jump";
                }
                case "liquidwalk": {
                    return "Liquid Walk";
                }
                case "highjump": {
                    return "High Jump";
                }
                case "icespeed": {
                    return "Ice Speed";
                }
                case "faststairs": {
                    return "Fast Stairs";
                }
                case "fastclimb": {
                    return "Fast Climb";
                }
                case "antifall": {
                    return "Anti Fall";
                }
                case "bufferspeed": {
                    return "Buffer Speed";
                }
                case "blockwalk": {
                    return "Block Walk";
                }
                case "autowalk": {
                    return "Auto Walk";
                }
                case "midclick": {
                    return "Mid Click";
                }
                case "liquidchat": {
                    return "Liquid Chat";
                }
                case "antibot": {
                    return "Anti Bot";
                }
                case "skinderp": {
                    return "Skin Derp";
                }
                case "pingspoof": {
                    return "Ping Spoof";
                }
                case "consolespammer": {
                    return "Console Spammer";
                }
                case "keepcontainer": {
                    return "Keep Container";
                }
                case "abortbreaking": {
                    return "Abort Breaking";
                }
                case "nofriends": {
                    return "No Friends";
                }
                case "hitbox": {
                    return "Hit Box";
                }
                case "fastbow": {
                    return "Fast Bow";
                }
                case "bowaimbot": {
                    return "Bow Aimbot";
                }
                case "autoweapon": {
                    return "Auto Weapon";
                }
                case "autosoup": {
                    return "Auto Soup";
                }
                case "autoclicker": {
                    return "Auto Clicker";
                }
                case "autobow": {
                    return "Auto Bow";
                }
                case "autoarmor": {
                    return "Auto Armor";
                }
                case "speedmine": {
                    return "Speed Mine";
                }
                case "targethud": {
                    return "Target HUD";
                }
                case "pointeresp": {
                    return "Pointer ESP";
                }
                case "playerface": {
                    return "Player Face";
                }
                case "itemrotate": {
                    return "Item Rotate";
                }
                case "targetstrafe": {
                    return "Target Strafe";
                }
                case "hytrun": {
                    return "HYT Run";
                }
                case "autojump": {
                    return "Auto Jump";
                }
                case "memoryfixer": {
                    return "Memory Fixer";
                }
                case "lagback": {
                    return "Lag Back";
                }
                case "autohead": {
                    return "Auto Head";
                }
                case "autosword": {
                    return "Auto Sword";
                }
                case "keepalive": {
                    return "Keep Alive";
                }
                case "killaura": {
                    return "Kill Aura";
                }
            }
        }
        return detName;
    }

    public final void toggle() {
        this.setState(!this.state);
    }

    public void onToggle(boolean state) {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Value<?> getValue(String valueName) {
        Object v0;
        block1: {
            Iterable iterable = this.getValues();
            boolean bl = false;
            Iterable iterable2 = iterable;
            boolean bl2 = false;
            for (Object t : iterable2) {
                Value it = (Value)t;
                boolean bl3 = false;
                if (!StringsKt.equals((String)it.getName(), (String)valueName, (boolean)true)) continue;
                v0 = t;
                break block1;
            }
            v0 = null;
        }
        return v0;
    }

    /*
     * WARNING - void declaration
     */
    public List<Value<?>> getValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Field[] $this$map$iv = this.getClass().getDeclaredFields();
        boolean $i$f$map = false;
        Field[] fieldArray = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        Iterator iterator = $this$mapTo$iv$iv;
        int n = ((void)iterator).length;
        for (int i = 0; i < n; ++i) {
            void valueField;
            void item$iv$iv;
            void var10_11 = item$iv$iv = iterator[i];
            Collection collection = destination$iv$iv;
            boolean bl = false;
            valueField.setAccessible(true);
            Object object = valueField.get(this);
            collection.add(object);
        }
        Iterable $this$filterIsInstance$iv = (List)destination$iv$iv;
        boolean $i$f$filterIsInstance = false;
        $this$mapTo$iv$iv = $this$filterIsInstance$iv;
        destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof Value)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    @Override
    public boolean handleEvents() {
        return this.state;
    }

    public Module() {
        ModuleInfo moduleInfo = this.getClass().getAnnotation(ModuleInfo.class);
        if (moduleInfo == null) {
            Intrinsics.throwNpe();
        }
        ModuleInfo moduleInfo2 = moduleInfo;
        this.name = moduleInfo2.name();
        this.description = moduleInfo2.description();
        this.category = moduleInfo2.category();
        this.setKeyBind(moduleInfo2.keyBind());
        this.setArray(moduleInfo2.array());
        this.canEnable = moduleInfo2.canEnable();
        this.isSupported = ArraysKt.contains((Object[])moduleInfo2.supportedVersions(), (Object)((Object)Backend.INSTANCE.getREPRESENTED_BACKEND_VERSION()));
        this.hue = (float)Math.random();
    }
}

