package wtf.expensive.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.Item;
import wtf.expensive.events.Event;

/**
 * @author dedinside
 * @since 09.06.2023
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventCalculateCooldown extends Event {

    public Item itemStack;
    public float cooldown;

    public EventCalculateCooldown(Item item) {
        this.itemStack = item;
    }
}

