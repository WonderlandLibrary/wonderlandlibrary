package me.earth.earthhack.impl.core.mixins.minecraft;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.core.ducks.IMinecraft;
import me.earth.earthhack.impl.event.events.client.ShutDownEvent;
import me.earth.earthhack.impl.event.events.keyboard.ClickMiddleEvent;
import me.earth.earthhack.impl.event.events.keyboard.KeyboardEvent;
import me.earth.earthhack.impl.event.events.misc.GameLoopEvent;
import me.earth.earthhack.impl.event.events.misc.TickEvent;
import me.earth.earthhack.impl.services.config.ConfigManager;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMinecraft
{
    @Shadow
    private int leftClickCounter;

    @Shadow
    private int rightClickDelayTimer;

    @Shadow
    @Final
    private Queue<FutureTask<?>> scheduledTasks;

    @Override
    public ListenableFuture<Object> scheduleNext(Runnable runnable)
    {
        Validate.notNull(runnable);
        return this.scheduleFuture(Executors.callable(runnable));
    }

    private <V> ListenableFuture<V> scheduleFuture(Callable<V> callable)
    {
        Validate.notNull(callable);
        ListenableFutureTask<V> listenableFutureTask = ListenableFutureTask.create(callable);
        //noinspection SynchronizeOnNonFinalField
        synchronized (this.scheduledTasks)
        {
            this.scheduledTasks.add(listenableFutureTask);
            return listenableFutureTask;
        }
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", ordinal = 0, shift = At.Shift.AFTER))
    private void post_ScheduledTasks(CallbackInfo callbackInfo)
    {
        Earthhack.EVENT_BUS.post(new GameLoopEvent());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void runTickHook(CallbackInfo info)
    {
        Earthhack.EVENT_BUS.post(new TickEvent());
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Keyboard.getEventKeyState()Z", remap = false))
    private void runTickKeyboardHook(CallbackInfo callbackInfo)
    {
        Earthhack.EVENT_BUS.post(new KeyboardEvent(Keyboard.getEventKeyState(), Keyboard.getEventKey()));
    }

    //meh could also inject after runTickKeyboard but that might not get called all the time.
    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;world:Lnet/minecraft/client/multiplayer/WorldClient;", ordinal = 4, shift = At.Shift.BEFORE))
    public void post_keyboardTickHook(CallbackInfo info)
    {
        Earthhack.EVENT_BUS.post(new KeyboardEvent.Post());
    }

    @Inject(method = "middleClickMouse", at = @At(value = "HEAD"), cancellable = true)
    private void middleClickMouseHook(CallbackInfo callbackInfo)
    {
        ClickMiddleEvent event = new ClickMiddleEvent();
        Earthhack.EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "HEAD"), cancellable = true)
    private void shutdownMinecraftAppletHook(CallbackInfo callbackInfo)
    {
        Bus.EVENT_BUS.post(new ShutDownEvent());
        ConfigManager.getInstance().save();
        Earthhack.running = false;
    }

}
