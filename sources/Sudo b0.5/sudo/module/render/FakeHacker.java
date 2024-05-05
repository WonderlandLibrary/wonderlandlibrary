package sudo.module.render;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import sudo.module.Mod;

public class FakeHacker extends Mod{

	PlayerEntity target = null;
	
	public FakeHacker() {
		super("FakeHacker", "Makes a player look like they are cheating", Category.RENDER, 0);
	}
	
    @Override
    public void onTick() {
    	HitResult hit = mc.crosshairTarget;
    	if (mc.player != null && (hit != null && hit.getType() == HitResult.Type.ENTITY)) {
    		if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
    			target = player;
    		}
    	}
        if (target != null) {
            Iterable<Entity> entities = mc.world.getEntities();
            List<Entity> entities1 = new ArrayList<>(StreamSupport.stream(entities.spliterator(), false).toList());
            Collections.shuffle(entities1);
            for (Entity entity : entities1) {
                if (entity.equals(target)) {
                    continue;
                }
                if (entity.isAttackable() && entity.distanceTo(target) < 4) {
                    target.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getPos().add(0, entity.getHeight() / 2, 0));
                    target.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
//  HitResult hit = mc.crosshairTarget;
//	int sWidth = mc.getWindow().getScaledWidth();
//	int sHeight = mc.getWindow().getScaledHeight();
//	
//	if (mc.player != null) {
//		if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
//		    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
//		        target = player;
//		    }
    @Override
    public void onEnable() {
    	target=null;
    	super.onEnable();
    }
    @Override
    public void onDisable() {
    	target=null;
    	super.onDisable();
    }
}
