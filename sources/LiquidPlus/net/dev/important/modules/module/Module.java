/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.Listenable;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.modules.misc.AutoDisable;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.Value;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\n\b\u0017\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010J\u001a\u00020K2\u0006\u0010L\u001a\u00020\"H\u0004J\u0016\u0010M\u001a\b\u0012\u0002\b\u0003\u0018\u00010H2\u0006\u0010N\u001a\u00020\"H\u0016J\b\u0010O\u001a\u00020\u000bH\u0016J\b\u0010P\u001a\u00020KH\u0016J\b\u0010Q\u001a\u00020KH\u0016J\b\u0010R\u001a\u00020KH\u0016J\u0010\u0010S\u001a\u00020K2\u0006\u0010A\u001a\u00020\u000bH\u0016J\u0006\u0010T\u001a\u00020KR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\n\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0007\"\u0004\b\u0012\u0010\tR \u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R$\u0010#\u001a\u00020\"2\u0006\u0010!\u001a\u00020\"@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001a\u0010(\u001a\u00020\"X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010%\"\u0004\b*\u0010'R\u0011\u0010+\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0007R\u001a\u0010!\u001a\u00020-X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R\u000e\u00102\u001a\u000203X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u00104\u001a\u00020\"X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010%\"\u0004\b6\u0010'R\u001a\u00107\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0007\"\u0004\b9\u0010\tR\u001a\u0010:\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010\u0007\"\u0004\b<\u0010\tR\u001a\u0010=\u001a\u00020\"X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b>\u0010%\"\u0004\b?\u0010'R$\u0010A\u001a\u00020\u000b2\u0006\u0010@\u001a\u00020\u000b@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bB\u0010\r\"\u0004\bC\u0010\u000fR\u0016\u0010D\u001a\u0004\u0018\u00010\"8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bE\u0010%R\u001e\u0010F\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030H0G8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bI\u0010\u0017\u00a8\u0006U"}, d2={"Lnet/dev/important/modules/module/Module;", "Lnet/dev/important/utils/MinecraftInstance;", "Lnet/dev/important/event/Listenable;", "()V", "animation", "", "getAnimation", "()F", "setAnimation", "(F)V", "array", "", "getArray", "()Z", "setArray", "(Z)V", "arrayY", "getArrayY", "setArrayY", "autoDisables", "", "Lnet/dev/important/modules/module/modules/misc/AutoDisable$DisableEvent;", "getAutoDisables", "()Ljava/util/List;", "setAutoDisables", "(Ljava/util/List;)V", "canEnable", "category", "Lnet/dev/important/modules/module/Category;", "getCategory", "()Lnet/dev/important/modules/module/Category;", "setCategory", "(Lnet/dev/important/modules/module/Category;)V", "keyBind", "", "cnName", "getCnName", "()Ljava/lang/String;", "setCnName", "(Ljava/lang/String;)V", "description", "getDescription", "setDescription", "hue", "getHue", "", "getKeyBind", "()I", "setKeyBind", "(I)V", "moduleInfo", "Lnet/dev/important/modules/module/Info;", "name", "getName", "setName", "slide", "getSlide", "setSlide", "slideStep", "getSlideStep", "setSlideStep", "spacedName", "getSpacedName", "setSpacedName", "value", "state", "getState", "setState", "tag", "getTag", "values", "", "Lnet/dev/important/value/Value;", "getValues", "chat", "", "msg", "getValue", "valueName", "handleEvents", "onDisable", "onEnable", "onInitialize", "onToggle", "toggle", "LiquidBounce"})
public class Module
extends MinecraftInstance
implements Listenable {
    @NotNull
    private final Info moduleInfo;
    @NotNull
    private String name;
    @NotNull
    private String spacedName;
    @NotNull
    private String description;
    @NotNull
    private Category category;
    private int keyBind;
    @NotNull
    private String cnName;
    private boolean array;
    private final boolean canEnable;
    private float slideStep;
    private float animation;
    @NotNull
    private List<AutoDisable.DisableEvent> autoDisables;
    private boolean state;
    private final float hue;
    private float slide;
    private float arrayY;

    public Module() {
        Info info = this.getClass().getAnnotation(Info.class);
        Intrinsics.checkNotNull(info);
        this.moduleInfo = info;
        this.cnName = this.moduleInfo.cnName();
        this.array = true;
        this.autoDisables = new ArrayList();
        Info info2 = this.getClass().getAnnotation(Info.class);
        Intrinsics.checkNotNull(info2);
        Info moduleInfo = info2;
        this.name = moduleInfo.name();
        this.spacedName = Intrinsics.areEqual(moduleInfo.spacedName(), "") ? this.name : moduleInfo.spacedName();
        this.description = moduleInfo.description();
        this.category = moduleInfo.category();
        this.keyBind = moduleInfo.keyBind();
        this.setArray(moduleInfo.array());
        this.canEnable = moduleInfo.canEnable();
        this.setCnName(moduleInfo.cnName());
        this.hue = (float)Math.random();
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.name = string;
    }

    @NotNull
    public final String getSpacedName() {
        return this.spacedName;
    }

    public final void setSpacedName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.spacedName = string;
    }

    @NotNull
    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.description = string;
    }

    @NotNull
    public final Category getCategory() {
        return this.category;
    }

    public final void setCategory(@NotNull Category category) {
        Intrinsics.checkNotNullParameter((Object)category, "<set-?>");
        this.category = category;
    }

    public final int getKeyBind() {
        return this.keyBind;
    }

    public final void setKeyBind(int n) {
        this.keyBind = n;
    }

    @NotNull
    public final String getCnName() {
        return this.cnName;
    }

    public final void setCnName(@NotNull String keyBind) {
        Intrinsics.checkNotNullParameter(keyBind, "keyBind");
        this.cnName = keyBind;
        if (!Client.INSTANCE.isStarting()) {
            Client.INSTANCE.getFileManager().saveConfig(Client.INSTANCE.getFileManager().modulesConfig);
        }
    }

    public final boolean getArray() {
        return this.array;
    }

    public final void setArray(boolean array) {
        this.array = array;
        if (!Client.INSTANCE.isStarting()) {
            Client.INSTANCE.getFileManager().saveConfig(Client.INSTANCE.getFileManager().modulesConfig);
        }
    }

    public final float getSlideStep() {
        return this.slideStep;
    }

    public final void setSlideStep(float f) {
        this.slideStep = f;
    }

    public final float getAnimation() {
        return this.animation;
    }

    public final void setAnimation(float f) {
        this.animation = f;
    }

    @NotNull
    public final List<AutoDisable.DisableEvent> getAutoDisables() {
        return this.autoDisables;
    }

    public final void setAutoDisables(@NotNull List<AutoDisable.DisableEvent> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.autoDisables = list;
    }

    public final boolean getState() {
        return this.state;
    }

    public final void setState(boolean value) {
        if (this.state == value || !this.canEnable) {
            return;
        }
        this.onToggle(value);
        if (!Client.INSTANCE.isStarting()) {
            switch (Client.INSTANCE.getModuleManager().getToggleSoundMode()) {
                case 1: {
                    MinecraftInstance.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.click"), (float)1.0f));
                    break;
                }
                case 2: {
                    (value ? Client.INSTANCE.getTipSoundManager().getEnableSound() : Client.INSTANCE.getTipSoundManager().getDisableSound()).asyncPlay(Client.INSTANCE.getModuleManager().getToggleVolume());
                }
            }
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
        Client.INSTANCE.getFileManager().saveConfig(Client.INSTANCE.getFileManager().modulesConfig);
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

    public final float getArrayY() {
        return this.arrayY;
    }

    public final void setArrayY(float f) {
        this.arrayY = f;
    }

    @Nullable
    public String getTag() {
        return null;
    }

    public final void toggle() {
        this.setState(!this.state);
    }

    protected final void chat(@NotNull String msg) {
        Intrinsics.checkNotNullParameter(msg, "msg");
        ClientUtils.displayChatMessage(Intrinsics.stringPlus("\u00a78[\u00a79\u00a7lLiquidPlus\u00a78] \u00a73", msg));
    }

    public void onToggle(boolean state) {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onInitialize() {
    }

    @Nullable
    public Value<?> getValue(@NotNull String valueName) {
        Object v0;
        block1: {
            Intrinsics.checkNotNullParameter(valueName, "valueName");
            for (Object t : (Iterable)this.getValues()) {
                Value it = (Value)t;
                boolean bl = false;
                if (!StringsKt.equals(it.getName(), valueName, true)) continue;
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
    @NotNull
    public List<Value<?>> getValues() {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Field[] fieldArray = this.getClass().getDeclaredFields();
        Intrinsics.checkNotNullExpressionValue(fieldArray, "javaClass.declaredFields");
        Object[] $this$map$iv = fieldArray;
        boolean $i$f$map = false;
        Object[] objectArray = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        for (void item$iv$iv : $this$mapTo$iv$iv) {
            void valueField;
            Field field = (Field)item$iv$iv;
            Collection collection = destination$iv$iv;
            boolean bl = false;
            valueField.setAccessible(true);
            collection.add(valueField.get(this));
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
}

