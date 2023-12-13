package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.BlockPos;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.utils.item.ItemUtil;

@Getter@Setter
public class AutoTool extends Module {
    public AutoTool(){
        super("AutoTool","Automatically change items based off of interaction", Category.PLAYER, ServerType.All);
    }

    @Subscribe
    public void onPacket(PacketEvent event){
        if (event.getType() == EventType.Outgoing && event.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packetUseEntity = (C02PacketUseEntity)event.getPacket();
            EntityLivingBase ent = (EntityLivingBase)packetUseEntity.getEntityFromWorld(mc.theWorld);
            if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                mc.thePlayer.inventory.currentItem = ItemUtil.getBestSword(ent);
                mc.playerController.updateController();
            }
        }

        if(mc.gameSettings.keyBindAttack.pressed) {
            if (mc.objectMouseOver != null) {
                BlockPos pos = mc.objectMouseOver.getBlockPos();
                if (pos != null) {
                    ItemUtil.updateTool(pos);
                }
            }
        }
    }
}
