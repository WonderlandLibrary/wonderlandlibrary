package today.flux.addon.api;

import com.soterdev.SoterObfuscator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import today.flux.addon.api.entities.AddonEntity;
import today.flux.addon.api.entities.AddonEntityLivingBase;
import today.flux.addon.api.entities.AddonEntityPlayer;
import today.flux.addon.api.utils.BlockPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * 世界信息获取
 */
public class AddonWorldClient {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static AddonWorldClient getWorldClient() {
        return new AddonWorldClient();
    }

    /**
     * 使用实体id获取实体对象
     *
     * @param id 实体id
     * @return 实体对象
     */

    public AddonEntity getEntityByID(int id) {
        Entity entity = mc.theWorld.getEntityByID(id);
        if (entity instanceof EntityPlayer) {
            return new AddonEntityPlayer((EntityPlayer) entity);
        } else if (entity instanceof EntityLivingBase) {
            return new AddonEntityLivingBase((EntityLivingBase) entity);
        } else if (entity != null) {
            return new AddonEntity(entity);
        }
        return null;
    }

    /**
     * 获取世界中所有已加载实体
     *
     * @return 实体列表
     */

    public List<AddonEntity> getLoadedEntities() {
        ArrayList<AddonEntity> list = new ArrayList<>();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            list.add(new AddonEntity(entity));
        }
        return list;
    }

    /**
     * 获取世界中所有已加载的玩家实体
     *
     * @return 玩家实体列表
     */

    public List<AddonEntityPlayer> getLoadedPlayerEntities() {
        ArrayList<AddonEntityPlayer> list = new ArrayList<>();
        for (EntityPlayer entity : mc.theWorld.playerEntities) {
            list.add(new AddonEntityPlayer(entity));
        }
        return list;
    }

    /**
     * 删除实体
     *
     * @param entity 实体对象
     */

    public void removeEntity(AddonEntity entity) {
        mc.theWorld.removeEntity(entity.getEntity());
    }

    /**
     * 判断某一坐标是否为指定方块
     *
     * @param position 实例化的BlockPosition(方块坐标信息)
     * @param id       方块ID
     * @return 判断结果
     */

    public boolean matchesBlockId(BlockPosition position, int id) {
        Block block = mc.theWorld.getBlock(position.x, position.y, position.z);
        return Block.getIdFromBlock(block) == id;
    }

    /**
     * 判断某一坐标是否为液体
     *
     * @param position 实例化的BlockPosition(方块坐标信息)
     * @return 判断结果
     */

    public boolean isLiquid(BlockPosition position) {
        Block block = mc.theWorld.getBlock(position.x, position.y, position.z);
        return block.getMaterial().isLiquid();
    }

    /**
     * 判断某一坐标是否为实体方块
     *
     * @param position 实例化的BlockPosition(方块坐标信息)
     * @return 判断结果
     */

    public boolean isSolid(BlockPosition position) {
        Block block = mc.theWorld.getBlock(position.x, position.y, position.z);
        return block.getMaterial().isSolid();
    }

    /**
     * 判断某一坐标的方块材质是否透明
     *
     * @param position 实例化的BlockPosition(方块坐标信息)
     * @return 是否透明
     */

    public boolean isOpaque(BlockPosition position) {
        Block block = mc.theWorld.getBlock(position.x, position.y, position.z);
        return block.getMaterial().isOpaque();
    }

    /**
     * 判断某一坐标的方块材质是否不透明
     *
     * @param position 实例化的BlockPosition(方块坐标信息)
     * @return 是否不透明
     */

    public boolean isTranslucent(BlockPosition position) {
        Block block = mc.theWorld.getBlock(position.x, position.y, position.z);
        return block.isTranslucent();
    }
}
