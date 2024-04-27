/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.ArraysKt
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module;

import dev.sakura_starring.util.sound.SoundFxPlayer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
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
    private boolean state;
    private boolean nameBreak;
    private float higt;
    private final boolean canEnable;
    private String name;
    private float keyBindY;
    @JvmField
    public float alpha;
    private ModuleCategory category;
    private boolean expanded;
    private int keyBind;
    private String description;
    private float slide;
    private float slideStep;
    private boolean array = true;
    private boolean isSupported;
    private final float hue;

    public final ModuleCategory getCategory() {
        return this.category;
    }

    public final void setState(boolean bl) {
        if (this.state == bl) {
            return;
        }
        this.onToggle(bl);
        if (!LiquidBounce.INSTANCE.isStarting()) {
            if (bl) {
                MinecraftInstance.mc.getSoundHandler().playSound("random.click", 1.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.name, "Enabled", NotifyType.SUCCESS, 0, 0, 24, null));
            } else {
                MinecraftInstance.mc.getSoundHandler().playSound("random.click", 1.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.name, "Disabled", NotifyType.ERROR, 0, 0, 24, null));
            }
        }
        if (bl) {
            this.onEnable();
            if (this.canEnable) {
                this.state = true;
            }
        } else {
            this.onDisable();
            this.state = false;
        }
        LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
    }

    public final void setCategory(ModuleCategory moduleCategory) {
        this.category = moduleCategory;
    }

    public final void setSupported(boolean bl) {
        this.isSupported = bl;
    }

    public final boolean getArray() {
        return this.array;
    }

    public final String getName() {
        return this.name;
    }

    public List getValues() {
        Object object = this.getClass().getDeclaredFields();
        boolean bl = false;
        Field[] fieldArray = object;
        Collection collection = new ArrayList(((Field[])object).length);
        boolean bl2 = false;
        Field[] fieldArray2 = fieldArray;
        int n = fieldArray2.length;
        for (int i = 0; i < n; ++i) {
            Field field;
            Field field2 = field = fieldArray2[i];
            Collection collection2 = collection;
            boolean bl3 = false;
            field2.setAccessible(true);
            Object object2 = field2.get(this);
            collection2.add(object2);
        }
        object = (List)collection;
        bl = false;
        fieldArray = object;
        collection = new ArrayList();
        bl2 = false;
        for (Object e : fieldArray) {
            if (!(e instanceof Value)) continue;
            collection.add(e);
        }
        return (List)collection;
    }

    public final void setSlideStep(float f) {
        this.slideStep = f;
    }

    public final String getTagName() {
        return this.name + (this.getTag() == null ? "" : " \u00a77" + this.getTag());
    }

    public final void setKeyBindY(float f) {
        this.keyBindY = f;
    }

    public final boolean getState() {
        return this.state;
    }

    public final String getColorlessTagName() {
        return this.name + (this.getTag() == null ? "" : " " + ColorUtils.stripColor(this.getTag()));
    }

    public final int getKeyBind() {
        return this.keyBind;
    }

    public final void setSlide(float f) {
        this.slide = f;
    }

    public final void setKeyBind(int n) {
        this.keyBind = n;
        if (!LiquidBounce.INSTANCE.isStarting()) {
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        }
    }

    @Override
    public boolean handleEvents() {
        return this.state;
    }

    public final void setHigt(float f) {
        this.higt = f;
    }

    public final boolean getExpanded() {
        return this.expanded;
    }

    public final float getHue() {
        return this.hue;
    }

    public final void setDescription(String string) {
        this.description = string;
    }

    public final void setName(String string) {
        this.name = string;
    }

    public final float getKeyBindY() {
        return this.keyBindY;
    }

    public final void setArray(boolean bl) {
        this.array = bl;
        if (!LiquidBounce.INSTANCE.isStarting()) {
            LiquidBounce.INSTANCE.getFileManager().saveConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        }
    }

    public String getTag() {
        return null;
    }

    public final void toggle() {
        this.setState(!this.state);
        if (this.state) {
            SoundFxPlayer.playSound(SoundFxPlayer.SoundType.Enable, 100.0f);
        } else {
            SoundFxPlayer.playSound(SoundFxPlayer.SoundType.Disable, 100.0f);
        }
    }

    public void onToggle(boolean bl) {
    }

    public final boolean isSupported() {
        return this.isSupported;
    }

    public final boolean getNameBreak() {
        return this.nameBreak;
    }

    public void onEnable() {
    }

    public Value getValue(String string) {
        Object v0;
        block1: {
            Iterable iterable = this.getValues();
            boolean bl = false;
            Iterable iterable2 = iterable;
            boolean bl2 = false;
            for (Object t : iterable2) {
                Value value = (Value)t;
                boolean bl3 = false;
                if (!StringsKt.equals((String)value.getName(), (String)string, (boolean)true)) continue;
                v0 = t;
                break block1;
            }
            v0 = null;
        }
        return v0;
    }

    public void onDisable() {
    }

    public final void setExpanded(boolean bl) {
        this.expanded = bl;
    }

    public final String getDescription() {
        return this.description;
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

    public final float getSlide() {
        return this.slide;
    }

    public final float getHigt() {
        return this.higt;
    }

    public final void setNameBreak(boolean bl) {
        this.nameBreak = bl;
    }

    public final float getSlideStep() {
        return this.slideStep;
    }
}

