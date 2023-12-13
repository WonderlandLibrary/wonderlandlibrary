package today.flux.module.implement.Movement.speed;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;
import today.flux.event.*;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Movement.Speed;
import today.flux.module.value.BooleanValue;
import today.flux.utility.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Spartan extends SubModule {
    public Spartan() {
        super("Spartan", "Speed");
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        if (PlayerUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01)) {
                Speed.setMotion(null, Math.max(0.34, MoveUtils.defaultSpeed()));
                mc.thePlayer.jump();
            } else {
                Speed.setMotion(null, PlayerUtils.getSpeed());
                mc.timer.timerSpeed = 1.05f;
            }
        }
    }
    
}
