package sudo.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import sudo.module.settings.ISimpleOption;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> implements ISimpleOption<T> {

    @Shadow
    T value;
    @Shadow @Final
    private Consumer<T> changeCallback;


    public void setValueUnrestricted(T object) {
        if (!MinecraftClient.getInstance().isRunning()) {
            this.value = object;
            return;
        }
        if (!Objects.equals(this.value, object)) {
            this.value = object;
            this.changeCallback.accept(this.value);
        }
    }
}