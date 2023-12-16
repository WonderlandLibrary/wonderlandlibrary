package wtf.expensive.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.util.movement.MoveUtil;

import java.util.UUID;

import static wtf.expensive.util.IMinecraft.mc;

public class FreeCamera extends ClientPlayerEntity implements IMinecraft {

    private static final ClientPlayNetHandler NETWORK_HANDLER = new ClientPlayNetHandler(IMinecraft.mc, IMinecraft.mc.currentScreen, IMinecraft.mc.getConnection().getNetworkManager(), new GameProfile(UUID.randomUUID(), "user001")) {
        @Override
        public void sendPacket(IPacket<?> packetIn) {
            super.sendPacket(packetIn);
        }
    };

    public FreeCamera(int i) {
        super(IMinecraft.mc, IMinecraft.mc.world, NETWORK_HANDLER, IMinecraft.mc.player.getStats(), IMinecraft.mc.player.getRecipeBook(), false, false);
        
        setEntityId(i);
        movementInput = new MovementInputFromOptions(IMinecraft.mc.gameSettings);
    }

    public void spawn() {
        if (world != null) {
            world.addEntity(this);
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
    }

    @Override
    public void rotateTowards(double yaw, double pitch) {
        super.rotateTowards(yaw, pitch);
    }
}