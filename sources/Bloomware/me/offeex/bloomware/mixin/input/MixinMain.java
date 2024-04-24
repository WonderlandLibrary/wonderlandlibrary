package me.offeex.bloomware.mixin.input;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.event.events.EventEntrypoint;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MixinMain {
    @Inject(method = "main([Ljava/lang/String;)V", at = @At("HEAD"), remap = false)
    private static void mainMethod(String[] args, CallbackInfo ci) {
        Bloomware.INSTANCE.getEVENTBUS().register(Bloomware.INSTANCE);
        new EventEntrypoint.Main().post(ci);
    }
}