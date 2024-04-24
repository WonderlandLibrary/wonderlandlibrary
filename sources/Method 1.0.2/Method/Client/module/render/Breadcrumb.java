package Method.Client.module.render;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static Method.Client.Main.setmgr;


public class Breadcrumb extends Module {

    Setting tickdelay = setmgr.add(new Setting("tickdelay", this, 2, 1, 20, true));
    Setting Color = setmgr.add(new Setting("Color", this, .4, .8, .9, 1));
    Setting OtherColor = setmgr.add(new Setting("OtherColor", this, .8, .8, .9, 1));
    Setting Width = setmgr.add(new Setting("Width", this, 2.5, 1, 5, false));
    Setting BlockSnap = setmgr.add(new Setting("BlockSnap", this, false));
    Setting OtherPlayers = setmgr.add(new Setting("OtherPlayers", this, false));

    ArrayList<Vec3d> doubles = new ArrayList<>();
    List<OtherPos> OtherPos = new ArrayList<>();

    public Breadcrumb() {
        super("Breadcrumb", Keyboard.KEY_NONE, Category.RENDER, "Breadcrumbs");
    }


    @Override
    public void onEnable() {
        doubles.clear();
        if (mc.player.getDistance(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ) < 200) {
            doubles.add(new Vec3d(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ));
            doubles.add(new Vec3d(mc.player.getPosition()));
        }
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.player.ticksExisted % (int) tickdelay.getValDouble() == 0) {
            doubles.add(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ));
            if (OtherPlayers.getValBoolean()) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityOtherPlayerMP) {
                        EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP) entity;
                        boolean newplayer = true;
                        for (Breadcrumb.OtherPos otherPo : OtherPos) {
                            if (otherPo.getName().equals(otherPlayerMP.getName())) {
                                otherPo.doubles.add(new Vec3d(otherPlayerMP.posX, otherPlayerMP.posY, otherPlayerMP.posZ));
                                newplayer = false;
                            }
                        }
                        if (newplayer) {
                            OtherPos NewPla = new OtherPos(otherPlayerMP.getName());
                            OtherPos.add(NewPla);
                            NewPla.doubles.add(new Vec3d(otherPlayerMP.posX, otherPlayerMP.posY, otherPlayerMP.posZ));
                        }
                    }
                }

            }
        }
        RenderUtils.RenderLine(doubles, Color.getcolor(), Width.getValDouble(), BlockSnap.getValBoolean());
        if (OtherPlayers.getValBoolean()) {
            for (OtherPos otherPo : OtherPos) {
                RenderUtils.RenderLine(otherPo.doubles, OtherColor.getcolor(), Width.getValDouble(), BlockSnap.getValBoolean());
            }
        }
    }

    static class OtherPos {
        public String getName() {
            return name;
        }

        private final String name;
        ArrayList<Vec3d> doubles = new ArrayList<>();

        public OtherPos(final String name) {
            this.name = name;
        }
    }
}

