package vip.radium.module.impl.misc;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import org.apache.commons.lang3.RandomUtils;
import vip.radium.event.impl.player.SendMessageEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.EnumProperty;

@ModuleInfo(label = "Chat Bypass", category = ModuleCategory.MISCELLANEOUS)
public final class ChatBypass extends Module {

    private static final char[] INVIS_CHARS = {'\u2764'};
    
    private final EnumProperty<BypassMode> bypassModeProperty = new EnumProperty<>("Mode", BypassMode.INVIS);

    @EventLink
    public final Listener<SendMessageEvent> SendMessageEvent = event -> {
        if (event.getMessage().startsWith("/")) {
            return;
        }
        switch (bypassModeProperty.getValue()) {
            case INVIS:
                final StringBuilder stringBuilder = new StringBuilder();
                for (char character : event.getMessage().toCharArray()) {
                    stringBuilder.append(character)
                            .append(INVIS_CHARS[RandomUtils.nextInt(0, INVIS_CHARS.length)]);
                }
                event.setMessage(stringBuilder.toString());
                break;
            case FONT:
                // TODO: find a way to use fonts
                break;
        }
    };

    private enum BypassMode {
        INVIS, FONT
    }
}