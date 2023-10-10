package client.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.multiplayer.ServerData;

@UtilityClass
public class ServerUtil {
    @Getter
    @Setter
    private ServerData lastServerData;
}