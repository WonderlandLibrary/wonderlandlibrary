package viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import viaversion.viabackwards.api.rewriters.Rewriter;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.AdvancementTranslations;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.rewriters.ComponentRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;

public class ChatPackets1_12 extends Rewriter<Protocol1_11_1To1_12> {

    private final ComponentRewriter componentRewriter = new ComponentRewriter() {
        @Override
        protected void handleTranslate(JsonObject object, String translate) {
            String text = AdvancementTranslations.get(translate);
            if (text != null) {
                object.addProperty("translate", text);
            }
        }
    };

    public ChatPackets1_12(Protocol1_11_1To1_12 protocol) {
        super(protocol);
    }

    @Override
    protected void registerPackets() {
        protocol.registerOutgoing(ClientboundPackets1_12.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    JsonElement element = wrapper.passthrough(Type.COMPONENT);
                    componentRewriter.processText(element);
                });
            }
        });
    }
}
