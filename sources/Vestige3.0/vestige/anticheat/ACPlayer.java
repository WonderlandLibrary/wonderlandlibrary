package vestige.anticheat;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

@Getter
public class ACPlayer {

    private final EntityPlayer entity;
    private final PlayerData data;
    private final ArrayList<AnticheatCheck> checks = new ArrayList<>();

    @Setter
    private boolean bot;

    public ACPlayer(EntityPlayer entity) {
        this.entity = entity;
        this.data = new PlayerData(entity);
    }

}
