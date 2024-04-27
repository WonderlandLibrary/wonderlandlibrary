package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.JumpEvent;
import me.aquavit.liquidsense.event.events.MoveEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.block.BlockUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.block.BlockPane;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "HighJump", description = "Allows you to jump higher.", category = ModuleCategory.MOVEMENT)
public class HighJump extends Module {

    private final FloatValue heightValue = new FloatValue("Height", 2F, 1.1F, 5F);
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Vanilla", "Damage", "AACv3", "DAC", "Mineplex"}, "Vanilla");
    private final BoolValue glassValue = new BoolValue("OnlyGlassPane", false);

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if(glassValue.get() && !(BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) instanceof BlockPane))
            return;

        switch(modeValue.get().toLowerCase()) {
            case "damage":
                if(mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround)
                    mc.thePlayer.motionY += 0.42F * heightValue.get();
                break;
            case "aacv3":
                if(!mc.thePlayer.onGround) mc.thePlayer.motionY += 0.059D;
                break;
            case "dac":
                if(!mc.thePlayer.onGround) mc.thePlayer.motionY += 0.049999;
                break;
            case "mineplex":
                if(!mc.thePlayer.onGround) MovementUtils.strafe(0.35F);
                break;
        }
    }

    @EventTarget
    public void onMove(final MoveEvent event) {
        if(glassValue.get() && !(BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) instanceof BlockPane))
            return;

        if(!mc.thePlayer.onGround) {
            if ("mineplex".equals(modeValue.get().toLowerCase())) {
                mc.thePlayer.motionY += mc.thePlayer.fallDistance == 0 ? 0.0499D : 0.05D;
            }
        }
    }

    @EventTarget
    public void onJump(final JumpEvent event) {
        if(glassValue.get() && !(BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) instanceof BlockPane))
            return;

        switch(modeValue.get().toLowerCase()) {
            case "vanilla":
                event.setMotion(event.getMotion() * heightValue.get());
                break;
            case "mineplex":
                event.setMotion(0.47F);
                break;
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
