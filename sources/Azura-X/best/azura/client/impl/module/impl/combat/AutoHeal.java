package best.azura.client.impl.module.impl.combat;


import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.combat.autoheal.GoldenAppleHealSub;
import best.azura.client.impl.module.impl.combat.autoheal.HealSub;
import best.azura.client.impl.module.impl.combat.autoheal.PotHealSub;
import best.azura.client.impl.module.impl.combat.autoheal.SoupHealSub;
import best.azura.client.impl.value.CategoryValue;
import best.azura.client.util.modes.ModeUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "Auto Heal", description = "Automatically heals you", category = Category.COMBAT)
public class AutoHeal extends Module {

    private final List<HealSub> subs;
    private static final List<Value<?>> values = new ArrayList<>();

    public AutoHeal() {
        super();
        subs = Arrays.asList(new GoldenAppleHealSub(), new PotHealSub(), new SoupHealSub());
        Client.INSTANCE.getValueManager().register(this);
        for (HealSub s : subs) {
            final CategoryValue categoryValue = new CategoryValue(s.getName());
            Client.INSTANCE.getValueManager().register(this, categoryValue);
            values.add(categoryValue);
            Client.INSTANCE.getValueManager().register(this, s.enabledValue);
            values.add(s.enabledValue);
            if (s.getValues() == null || s.getValues().isEmpty()) continue;
            ModeUtil.registerValues(this, s.getValues(), s.enabledValue);
            values.addAll(Client.INSTANCE.getValueManager().getValues(this));
        }
    }

    @SuppressWarnings("unused")
    public static Value<?> findValue(String name, Class<?> type) {
        return values.stream().filter(value -> value.getName().equalsIgnoreCase(name) && value.getClass().equals(type)).findFirst().orElse(null);
    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    public void handle(Event event) {
        for (HealSub s : subs) {
            if (!s.enabledValue.getObject()) continue;
            s.handle(event);
        }
    }
}