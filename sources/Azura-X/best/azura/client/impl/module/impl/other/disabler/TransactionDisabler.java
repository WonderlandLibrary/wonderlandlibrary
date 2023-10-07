package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.api.value.Value;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

import java.util.Arrays;
import java.util.List;

public class TransactionDisabler implements ModeImpl<Disabler> {

    private final BooleanValue denyValue = new BooleanValue("Deny", "Deny transactions", false);
    private final BooleanValue negateId = new BooleanValue("Negate ID", "Negate transaction ids", false);

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(denyValue, negateId);
    }

    @Override
    public String getName() {
        return "Transaction";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
            final C0FPacketConfirmTransaction c0f = e.getPacket();
            e.setPacket(new C0FPacketConfirmTransaction(c0f.getWindowId(), negateId.getObject() ? (short) -c0f.getUid() : c0f.getUid(), !denyValue.getObject()));
        }
    };

}