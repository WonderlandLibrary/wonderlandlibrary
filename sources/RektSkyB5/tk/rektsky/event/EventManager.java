/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event;

import org.greenrobot.eventbus.EventBus;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.commands.CommandsManager;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.ChatEvent;
import tk.rektsky.event.impl.HUDRenderEvent;
import tk.rektsky.event.impl.KeyPressedEvent;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.utils.combat.RotationUtil;
import tk.rektsky.utils.file.FileManager;

public class EventManager {
    static int ticks = 0;
    public static final EventBus EVENT_BUS = EventBus.builder().logNoSubscriberMessages(false).throwSubscriberException(false).build();

    private static void onEvent(Event e2) {
        ++ticks;
        if (e2 instanceof MotionUpdateEvent && !RotationUtil.doReset()) {
            ((MotionUpdateEvent)e2).setYaw(RotationUtil.getYaw());
            ((MotionUpdateEvent)e2).setPitch(RotationUtil.getPitch());
        }
        if (e2 instanceof WorldTickEvent) {
            // empty if block
        }
        if (ticks % 200 == 0 && e2 instanceof WorldTickEvent) {
            new Thread(){

                @Override
                public void run() {
                    FileManager.replaceAndSaveSettings();
                }
            }.start();
        }
        if (e2 instanceof RenderEvent && RotationUtil.doReset()) {
            RotationUtil.reset();
        }
        for (Module module : ModulesManager.getModules()) {
            KeyPressedEvent kpe;
            if (module.isToggled()) {
                module.onEvent(e2);
                if (e2 instanceof WorldTickEvent) {
                    ++module.enabledTicks;
                }
            }
            if (e2 instanceof HUDRenderEvent) {
                Client.hud.draw(((HUDRenderEvent)e2).getGui());
            }
            if (!(e2 instanceof KeyPressedEvent) || (kpe = (KeyPressedEvent)e2).getKey() != module.keyCode) continue;
            module.toggle();
        }
        for (Command command : CommandsManager.COMMANDS) {
            command.onEvent(e2);
        }
        if (e2 instanceof ChatEvent) {
            CommandsManager.processCommand((ChatEvent)e2);
        }
    }

    public static void callEvent(Event event) {
        EventManager.onEvent(event);
        EVENT_BUS.post(event);
    }
}

