package net.minecraft.block;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.movement.NoWeb;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import static info.sigmaclient.sigma.modules.Module.mc;

public class WebBlock extends Block
{
    public WebBlock(AbstractBlock.Properties properties)
    {
        super(properties);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if(!(entityIn instanceof ClientPlayerEntity) && !SigmaNG.getSigmaNG().moduleManager.getModule(NoWeb.class).enabled) {
            entityIn.setMotionMultiplier(state, new Vector3d(0.25D, 0.05F, 0.25D));
        }else{
            switch (((NoWeb)SigmaNG.getSigmaNG().moduleManager.getModule(NoWeb.class)).type.getValue()){
                case "Intave":
                    entityIn.setMotionMultiplier(state, new Vector3d(0.95f, 0.05F, 0.95f));
                    break;
                case "NCP":
                    entityIn.setMotionMultiplier(state, new Vector3d(0.98f, 0.05F, 0.98f));
                    break;
                case "Vanilla":
                    break;
            }
        }
    }
}
