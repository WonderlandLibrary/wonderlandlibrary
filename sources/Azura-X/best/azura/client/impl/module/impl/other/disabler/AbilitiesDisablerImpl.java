package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

import java.util.Arrays;
import java.util.List;

public class AbilitiesDisablerImpl implements ModeImpl<Disabler> {

    private final BooleanValue flying = new BooleanValue("Flying", "Is flying", false);
    private final BooleanValue allowFlying = new BooleanValue("Allow Flying", "Allow flying", false);
    private final BooleanValue creative = new BooleanValue("Creative", "Is creative", false);

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(flying, allowFlying, creative);
    }

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Abilities";
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        final PlayerCapabilities capabilities = new PlayerCapabilities();
        capabilities.setFlySpeed(mc.thePlayer.capabilities.getFlySpeed());
        capabilities.setPlayerWalkSpeed(mc.thePlayer.capabilities.getWalkSpeed());
        capabilities.isFlying = flying.getObject();
        capabilities.allowFlying = allowFlying.getObject();
        capabilities.isCreativeMode = creative.getObject();
        mc.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(capabilities));
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}