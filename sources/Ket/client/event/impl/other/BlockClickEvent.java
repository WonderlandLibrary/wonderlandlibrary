package client.event.impl.other;

import client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.BlockPos;

@Getter
@AllArgsConstructor
public class BlockClickEvent implements Event {
    private final BlockPos pos;
}