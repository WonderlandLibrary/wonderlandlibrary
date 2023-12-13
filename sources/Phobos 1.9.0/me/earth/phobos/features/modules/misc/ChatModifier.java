package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Managers;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.TextUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatModifier
        extends Module {
    private static ChatModifier INSTANCE = new ChatModifier();
    private final Timer timer = new Timer();
    public Setting<Suffix> suffix = this.register(new Setting<Suffix>("Suffix", Suffix.NONE, "Your Suffix."));
    public Setting<Boolean> clean = this.register(new Setting<Boolean>("CleanChat", Boolean.valueOf(false), "Cleans your chat"));
    public Setting<Boolean> infinite = this.register(new Setting<Boolean>("Infinite", Boolean.valueOf(false), "Makes your chat infinite."));
    public Setting<Boolean> autoQMain = this.register(new Setting<Boolean>("AutoQMain", Boolean.valueOf(false), "Spams AutoQMain"));
    public Setting<Boolean> qNotification = this.register(new Setting<Object>("QNotification", Boolean.valueOf(false), v -> this.autoQMain.getValue()));
    public Setting<Integer> qDelay = this.register(new Setting<Object>("QDelay", Integer.valueOf(9), Integer.valueOf(1), Integer.valueOf(90), v -> this.autoQMain.getValue()));
    public Setting<TextUtil.Color> timeStamps = this.register(new Setting<TextUtil.Color>("Time", TextUtil.Color.NONE));
    public Setting<Boolean> rainbowTimeStamps = this.register(new Setting<Object>("RainbowTimeStamps", Boolean.valueOf(false), v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
    public Setting<TextUtil.Color> bracket = this.register(new Setting<Object>("Bracket", TextUtil.Color.WHITE, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
    public Setting<Boolean> space = this.register(new Setting<Object>("Space", Boolean.valueOf(true), v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
    public Setting<Boolean> all = this.register(new Setting<Object>("All", Boolean.valueOf(false), v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
    public Setting<Boolean> shrug = this.register(new Setting<Boolean>("Shrug", false));
    public Setting<Boolean> disability = this.register(new Setting<Boolean>("Disability", false));

    public ChatModifier() {
        super("Chat", "Modifies your chat", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.shrug.getValue().booleanValue()) {
            ChatModifier.mc.player.sendChatMessage(TextUtil.shrug);
            this.shrug.setValue(false);
        }
        if (this.autoQMain.getValue().booleanValue()) {
            if (!this.shouldSendMessage(ChatModifier.mc.player)) {
                return;
            }
            if (this.qNotification.getValue().booleanValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            ChatModifier.mc.player.sendChatMessage("/queue main");
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/")) {
                return;
            }
            switch (this.suffix.getValue()) {
                case EARTH: {
                    s = s + " \u23d0 3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b";
                    break;
                }
                case PHOBOS: {
                    s = s + " \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
                    break;
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }

    @SubscribeEvent
    public void onChatPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() != 0 || event.getPacket() instanceof SPacketChat) {
            // empty if block
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat) event.getPacket()).isSystem()) {
                return;
            }
            String originalMessage = ((SPacketChat) event.getPacket()).chatComponent.getFormattedText();
            String message = this.getTimeString(originalMessage) + originalMessage;
            ((SPacketChat) event.getPacket()).chatComponent = new TextComponentString(message);
        }
    }

    public String getTimeString(String message) {
        String date = new SimpleDateFormat("k:mm").format(new Date());
        if (this.rainbowTimeStamps.getValue().booleanValue()) {
            String timeString = "<" + date + ">" + (this.space.getValue() != false ? " " : "");
            StringBuilder builder = new StringBuilder(timeString);
            builder.insert(0, "\u00a7+");
            if (!message.contains(Managers.getInstance().getRainbowCommandMessage())) {
                builder.append("\u00a7r");
            }
            return builder.toString();
        }
        return (this.bracket.getValue() == TextUtil.Color.NONE ? "" : TextUtil.coloredString("<", this.bracket.getValue())) + TextUtil.coloredString(date, this.timeStamps.getValue()) + (this.bracket.getValue() == TextUtil.Color.NONE ? "" : TextUtil.coloredString(">", this.bracket.getValue())) + (this.space.getValue() != false ? " " : "") + "\u00a7r";
    }

    private boolean shouldSendMessage(EntityPlayer player) {
        if (player.dimension != 1) {
            return false;
        }
        if (!this.timer.passedS(this.qDelay.getValue().intValue())) {
            return false;
        }
        return player.getPosition().equals(new Vec3i(0, 240, 0));
    }

    public enum Suffix {
        NONE,
        PHOBOS,
        EARTH

    }
}

