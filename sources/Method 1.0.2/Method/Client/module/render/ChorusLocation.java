package Method.Client.module.render;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Random;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.RenderUtils.RenderBlock;
import static Method.Client.utils.visual.RenderUtils.Standardbb;

public class ChorusLocation extends Module {

    Setting OverlayColor = setmgr.add(new Setting("OverlayColor", this, 0, 1, 1, .62));
    Setting Mode = setmgr.add(new Setting("Hole Mode", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));
    Setting Times = setmgr.add(new Setting("Times", this, 1, 0, 4, true));

    public ChorusLocation() {
        super("ChorusLocation", Keyboard.KEY_NONE, Category.RENDER, "ChorusLocation");
    }

    ArrayList<BlockPos> TpSpots = new ArrayList<>();

    @Override
    public void onToggle() {
        TpSpots = new ArrayList<>();
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.player.getHeldItemMainhand().item instanceof ItemChorusFruit || mc.player.getHeldItemOffhand().item instanceof ItemChorusFruit) {
            if (mc.player.ticksExisted % (20 / Times.getValDouble()) == 0)
                TpSpots = ChorusTP(mc.world, mc.player);
            for (BlockPos tpSpot : TpSpots) {
                RenderBlock(Mode.getValString(), Standardbb(tpSpot), OverlayColor.getcolor(), LineWidth.getValDouble());
            }
        }
    }

    public ArrayList<BlockPos> ChorusTP(World worldIn, EntityLivingBase entityLiving) {
        ArrayList<BlockPos> TpSpots = new ArrayList<>();
        EntityPlayer entityPlayer = Utils.createPlayer(mc.player, entityLiving.getName(), false);
        Random copy = entityLiving.getRNG();
        for (int i = 0; i < 16; ++i) {
            double x = entityPlayer.posX + (copy.nextDouble() - 0.5D) * 16.0D;
            double y = MathHelper.clamp(entityPlayer.posY + (double) (copy.nextInt(16) - 8), 0.0D, worldIn.getActualHeight() - 1);
            double z = entityPlayer.posZ + (copy.nextDouble() - 0.5D) * 16.0D;
            if (entityPlayer.attemptTeleport(x, y, z)) {
                TpSpots.add(new BlockPos(x, y, z));
            }
        }
        mc.world.removeEntity(entityPlayer);
        return TpSpots;
    }
}
