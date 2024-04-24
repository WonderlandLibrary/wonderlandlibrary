package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.combat.AntiBot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;


public class GetClosestEntity extends Block {

    public GetClosestEntity() {
        super("GetClosestEntity", MainBlockType.Entity, Tabs.Utils, BlockObjects.Name, BlockObjects.Words,
                BlockObjects.BooleanTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));


        this.words[0] = "Players";
        this.words[1] = "Mobs";
        this.words[2] = "Animals";
        this.words[3] = "Antibot Checks";


        this.description = "Returns Closest Entity";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        EntityLivingBase closestEntity = null;
        for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
            if (o.equals(Minecraft.getMinecraft().player))
                continue;
            if (o instanceof EntityLivingBase && !(o instanceof EntityArmorStand)) {
                EntityLivingBase entity = (EntityLivingBase) o;
                if (entity instanceof EntityPlayer && !dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event))
                    continue;
                if (entity instanceof IMob && !dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event))
                    continue;
                if (entity instanceof IAnimals && !(entity instanceof IMob) && !dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event))
                    continue;
                if (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event))
                    if (AntiBot.isBot(entity))
                        continue;

                if (closestEntity == null || Minecraft.getMinecraft().player.getDistance(entity) < Minecraft.getMinecraft().player.getDistance(closestEntity)) {
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }


}
