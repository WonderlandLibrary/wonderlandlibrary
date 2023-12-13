package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MobOwner
        extends Module {
    private final Map<Entity, String> owners = new HashMap<Entity, String>();
    private final Map<Entity, UUID> toLookUp = new ConcurrentHashMap<Entity, UUID>();
    private final List<Entity> lookedUp = new ArrayList<Entity>();

    public MobOwner() {
        super("MobOwner", "Shows you who owns mobs.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (PlayerUtil.timer.passedS(5.0)) {
            for (final Map.Entry<Entity, UUID> entry : this.toLookUp.entrySet()) {
                final Entity entity = entry.getKey();
                final UUID uuid = entry.getValue();
                if (uuid != null) {
                    final EntityPlayer owner = MobOwner.mc.world.getPlayerEntityByUUID(uuid);
                    if (owner == null) {
                        try {
                            final String name = PlayerUtil.getNameFromUUID(uuid);
                            if (name != null) {
                                this.owners.put(entity, name);
                                this.lookedUp.add(entity);
                            }
                        } catch (Exception e) {
                            this.lookedUp.add(entity);
                            this.toLookUp.remove(entry);
                        }
                        PlayerUtil.timer.reset();
                        break;
                    }
                    this.owners.put(entity, owner.getName());
                    this.lookedUp.add(entity);
                } else {
                    this.lookedUp.add(entity);
                    this.toLookUp.remove(entry);
                }
            }
        }
        for (final Entity entity2 : MobOwner.mc.world.getLoadedEntityList()) {
            if (!entity2.getAlwaysRenderNameTag()) {
                if (entity2 instanceof EntityTameable) {
                    final EntityTameable tameableEntity = (EntityTameable) entity2;
                    if (!tameableEntity.isTamed() || tameableEntity.getOwnerId() == null) {
                        continue;
                    }
                    if (this.owners.get(tameableEntity) != null) {
                        tameableEntity.setAlwaysRenderNameTag(true);
                        tameableEntity.setCustomNameTag(this.owners.get(tameableEntity));
                    } else {
                        if (this.lookedUp.contains(entity2)) {
                            continue;
                        }
                        this.toLookUp.put(tameableEntity, tameableEntity.getOwnerId());
                    }
                } else {
                    if (!(entity2 instanceof AbstractHorse)) {
                        continue;
                    }
                    final AbstractHorse tameableEntity2 = (AbstractHorse) entity2;
                    if (!tameableEntity2.isTame() || tameableEntity2.getOwnerUniqueId() == null) {
                        continue;
                    }
                    if (this.owners.get(tameableEntity2) != null) {
                        tameableEntity2.setAlwaysRenderNameTag(true);
                        tameableEntity2.setCustomNameTag(this.owners.get(tameableEntity2));
                    } else {
                        if (this.lookedUp.contains(entity2)) {
                            continue;
                        }
                        this.toLookUp.put(tameableEntity2, tameableEntity2.getOwnerUniqueId());
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (final Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityTameable)) {
                if (!(entity instanceof AbstractHorse)) {
                    continue;
                }
            }
            try {
                entity.setAlwaysRenderNameTag(false);
            } catch (Exception ex) {
            }
        }
    }
}
