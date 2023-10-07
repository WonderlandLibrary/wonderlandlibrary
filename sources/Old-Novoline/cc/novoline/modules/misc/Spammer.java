package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.StringProperty;
import cc.novoline.utils.Channels;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Timer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ThreadLocalRandom;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createInt;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createString;

public final class Spammer extends AbstractModule {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    /* fields */
    private final Timer timer = new Timer();

    /* properties @off */
    @Property("text")
    private final StringProperty text = createString("Buy novoline");
    @Property("delay")
    private final IntProperty delay = createInt(3_000).minimum(100).maximum(15_000);

    /* constructors @on */
    public Spammer(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Spammer", "Spammer", EnumModuleType.MISC, "Spams whatever text you set with whatever delay");
        put(new Setting("SPAMMER_TEXT", "Text", SettingType.TEXTBOX, this, "Spam text", text));
        put(new Setting("SPAMMER_DELAY", "Delay (in ms)", SettingType.SLIDER, this, delay, 100));
    }

    /* events */
    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (timer.check(delay.get())) {
                StringBuilder msg = new StringBuilder();
                char[] chars = text.get().toCharArray();

                for (char aChar : chars) {
                    msg.append(aChar);

                    for (int i = 0; i < ThreadLocalRandom.current().nextInt(0, 4); i++) {
                        msg.append('\u05fc');
                    }
                }

                if (text.get().startsWith("/")) {
                    sendPacket(new C01PacketChatMessage(text.get()));
                } else if (!ServerUtils.isHypixel() || !ServerUtils.channelIs(Channels.PM)) {
                    sendPacketNoEvent(new C01PacketChatMessage(msg.toString()));
                }

                timer.reset();
            }
        }
    }

}
