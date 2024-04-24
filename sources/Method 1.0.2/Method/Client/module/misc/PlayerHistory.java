package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.OtherPlayerManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;

public class PlayerHistory extends Module {

    public Setting TotemPopLogs = setmgr.add(new Setting("TotemPopLogs", this, false));

    public Setting Teleport = setmgr.add(new Setting("EntityTeleport", this, true));
    public Setting GlobalSound = setmgr.add(new Setting("GlobalSound", this, true));

    public Setting MoveDistance = setmgr.add(new Setting("MoveDistance", this, 32, 2, 100, true));
    public Setting TeleportDis = setmgr.add(new Setting("TeleportDis", this, 256, 10, 1000, true));

    public Setting playSound = setmgr.add(new Setting("playSound", this, false));
    public Setting Box = setmgr.add(new Setting("Box", this, true));
    public Setting color = setmgr.add(new Setting("Logoff", this, 1, 1, 1, 1));
    public Setting Mode = setmgr.add(new Setting("Mode", this, "Outline", BlockEspOptions()));
    public Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));
    public Setting LeaveJoin = setmgr.add(new Setting("ChatJoins", this, false));
    public Setting ShowCoords = setmgr.add(new Setting("ShowCoords", this, false));

    public PlayerHistory() {
        super("PlayerHistory", Keyboard.KEY_NONE, Category.MISC, "PlayerHistory");
    }

    @Override
    public void setup() {
        this.setToggled(true);
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        Main.PlayerManager.onPacket(packet, this);
        return true;
    }

    @Subscribe
    public void onWorldLoad(WorldEvent.Load event) {
        Main.PlayerManager.onWorldLoad(event, this);
    }

    @Subscribe
    public void LivingDeathEvent(LivingDeathEvent event) {
        Main.PlayerManager.LivingDeathEvent(event, this);
    }

    @Subscribe
    public void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        Main.PlayerManager.LivingUpdateEvent(event, this);
    }

    @Subscribe
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Main.PlayerManager.onEntityJoinWorld(event, this);
    }

    @Subscribe
    public void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        Main.PlayerManager.PlayerLoggedOutEvent(event, this);
    }


    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Main.PlayerManager.onRenderWorldLast(event, this);
    }


}
