package me.offeex.bloomware.mixin;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.client.StreamerMode;
import me.offeex.bloomware.event.events.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.InputSupplier;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.nio.file.Path;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Shadow @Final private Window window;

    @ModifyConstant(method = "getFramerateLimit", constant = @Constant(intValue = 60))
    private int getFramerateLimit(int original) {
        return window.getFramerateLimit();
    }

    @ModifyArg(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;setPerspective(Lnet/minecraft/client/option/Perspective;)V"))
    Perspective setPerspective(Perspective perspective) {
        EventUpdate.Perspective event = new EventUpdate.Perspective(perspective).post();
        return event.getPerspective();
    }

    @Inject(method = "getWindowTitle", at = @At("TAIL"), cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        StreamerMode module = StreamerMode.INSTANCE;
        cir.setReturnValue(module.getEnabled() ? Bloomware.NAME + " v" + Bloomware.INSTANCE.getVERSION() + " | Streamer Mode" : Bloomware.NAME + " v" + Bloomware.INSTANCE.getVERSION());
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenPre(Screen screen, CallbackInfo ci) {
        new EventScreen.Open(screen).post(ci);
    }

    @ModifyVariable(method = "setScreen", at = @At("STORE"), argsOnly = true)
    private Screen onDeathScreen(Screen screen) {
        EventScreen.Update event = new EventScreen.Update(screen).post();
        return event.getScreen();
    }

    @Redirect(method = "setScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", opcode = Opcodes.PUTFIELD))
    private void onSetScreen(MinecraftClient instance, Screen value) {
        EventScreen.Set event = new EventScreen.Set(value).post();
        instance.currentScreen = event.getScreen();
    }

    @Inject(at = @At("HEAD"), method = "scheduleStop")
    private void stop(CallbackInfo ci) {
        new EventEntrypoint.Stop().post(ci);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"))
    private void init(RunArgs args, CallbackInfo ci) {
        new EventEntrypoint.SetOverlay().post(ci);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Lnet/minecraft/resource/InputSupplier;Lnet/minecraft/resource/InputSupplier;)V"))
    private void setAlternativeWindowIcon(Window instance, InputSupplier<InputStream> smallIconSupplier, InputSupplier<InputStream> bigIconSupplier) {
        instance.setIcon(InputSupplier.create(Path.of("/assets/bloomware/elements/tray/icon16x16.png")), InputSupplier.create(Path.of("/assets/bloomware/elements/tray/icon32x32.png")));
    }

    @Inject(method = "joinWorld", at = @At("HEAD"), cancellable = true)
    private void joinWorldPre(ClientWorld clientWorld, CallbackInfo ci) {
        new EventWorld.Join(clientWorld).post(ci);
    }

    @Inject(method = "joinWorld", at = @At("TAIL"))
    private void joinWorldPost(ClientWorld clientWorld, CallbackInfo ci) {
        new EventWorld.Join(clientWorld).shift().post();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;clearWorld()V"), cancellable = true)
    private void disconnect(Screen screen, CallbackInfo ci) {
        new EventWorld.Leave(screen).post(ci);
    }

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    public void doItemUse(CallbackInfo ci) {
        new EventItemUse().post();
    }
}
