package space.lunaclient.luna.impl.elements.world;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.Event.Type;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.DoubleSetting;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventMotion;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.PlayerUtils.PacketUtils;
import space.lunaclient.luna.util.scaffold.BlockUtils;

@ElementInfo(name="Scaffold", category=Category.WORLD, description="Places blocks below your feats while walking.")
public class Scaffold
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Normal", options={"Normal", "CubeCraft"}, locked=false)
  public static Setting mode;
  @DoubleSetting(name="Distance", currentValue=0.4D, minValue=0.01D, maxValue=1.2D, onlyInt=false, locked=false)
  public static Setting distance;
  private List<Block> invalid;
  private space.lunaclient.luna.util.Timer timer;
  private BlockData blockData;
  private int slot;
  private int original;
  private int clock;
  private int yOffset;
  
  public Scaffold()
  {
    this.invalid = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever });
    this.timer = new space.lunaclient.luna.util.Timer();
    this.slot = -1;
  }
  
  private static float[] getAngles(Entity e)
  {
    return new float[] { getYawChangeToEntity(e) + Minecraft.thePlayer.rotationYaw, getPitchChangeToEntity(e) + Minecraft.thePlayer.rotationPitch };
  }
  
  private static float getYawChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double yawToEntity;
    double yawToEntity;
    if ((deltaZ < 0.0D) && (deltaX < 0.0D))
    {
      yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
    }
    else
    {
      double yawToEntity;
      if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
        yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else {
        yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }
    }
    return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)yawToEntity));
  }
  
  private static float getPitchChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4D - Minecraft.thePlayer.posY;
    double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
    return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
  }
  
  private void swap(int slot, int hotBarNumber)
  {
    Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotBarNumber, 2, Minecraft.thePlayer);
  }
  
  private boolean grabBlock()
  {
    for (int i = 0; i < 36; i++)
    {
      ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
      if ((stack != null) && ((stack.getItem() instanceof ItemBlock)))
      {
        for (int x = 36; x < 45; x++) {
          try
          {
            Item localItem = Minecraft.thePlayer.inventoryContainer.getSlot(x).getStack().getItem();
          }
          catch (NullPointerException var5)
          {
            System.out.println(x - 36);
            swap(i, x - 36);
            return true;
          }
        }
        swap(i, 1);
        return true;
      }
    }
    return false;
  }
  
  private int isBlockInHotBar()
  {
    for (int i = 36; i < 45; i++)
    {
      ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
      if ((stack != null) && ((stack.getItem() instanceof ItemBlock))) {
        return i;
      }
    }
    return 0;
  }
  
  private static float[] getBlockRotations(int x, int y, int z, EnumFacing facing)
  {
    Entity temp = new EntitySnowball(Minecraft.theWorld);
    temp.posX = (x + 0.5D);
    temp.posY = (y + 0.5D);
    temp.posZ = (z + 0.5D);
    return getAngles(temp);
  }
  
  private BlockData getBlockData(BlockPos pos)
  {
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
      return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
      return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
      return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
      return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
      return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH, null);
    }
    BlockPos add = pos.add(-1, 0, 0);
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
      return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
      return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
      return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
      return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, null);
    }
    BlockPos add2 = pos.add(1, 0, 0);
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
      return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
      return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
      return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
      return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, null);
    }
    BlockPos add3 = pos.add(0, 0, -1);
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
      return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
      return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
      return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
      return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, null);
    }
    BlockPos add4 = pos.add(0, 0, 1);
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
      return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
      return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
      return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, null);
    }
    if (!this.invalid.contains(Minecraft.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
      return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, null);
    }
    return null;
  }
  
  public static Block getBlock(int x, int y, int z)
  {
    return Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
  }
  
  private static float[] getRotationsForBlock(BlockPos pos, EnumFacing facing)
  {
    double d0 = pos.getX() - Minecraft.thePlayer.posX;
    double d1 = pos.getY() - (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
    double d2 = pos.getZ() - Minecraft.thePlayer.posZ;
    double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
    float f = (float)(Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
    float f1 = (float)-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D);
    return new float[] { f, f1 };
  }
  
  private class BlockData
  {
    public BlockPos position;
    public EnumFacing face;
    
    private BlockData(BlockPos position, EnumFacing face)
    {
      this.position = position;
      this.face = face;
    }
  }
  
  public void onDisable()
  {
    mc.timer.resetTimer();
    mc.gameSettings.ofDynamicFov = true;
    PlayerUtils.PacketUtils.stopSneaking();
    mc.gameSettings.keyBindSneak.pressed = false;
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
  }
  
  private int getBlockSlot()
  {
    for (int i = 36; i < 45; i++)
    {
      ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
      if ((itemStack != null) && ((itemStack.getItem() instanceof ItemBlock))) {
        return i - 36;
      }
    }
    return -1;
  }
  
  @EventRegister
  public void onUpdate(EventMotion event)
  {
    if (isToggled())
    {
      setMode(mode.getValString());
      if (event.getType() == Event.Type.PRE)
      {
        for (int block = 0; block < 45; block++) {
          ItemStack localItemStack = Minecraft.thePlayer.inventoryContainer.getSlot(block).getStack();
        }
        block = isBlockInHotBar();
        if ((block == 0) && (grabBlock()))
        {
          block = isBlockInHotBar();
          if (block == 0) {
            return;
          }
        }
        if (event.getType() == Event.Type.PRE)
        {
          int tempSlot = getBlockSlot();
          this.blockData = null;
          boolean hasRotated = false;
          this.original = Minecraft.thePlayer.inventory.currentItem;
          if ((Minecraft.thePlayer.isMoving()) && (mode.getValString().equalsIgnoreCase("CubeCraft")) && (Minecraft.thePlayer.onGround)) {
            mc.gameSettings.keyBindSneak.pressed = hasRotated;
          }
          this.slot = -1;
          if (tempSlot != -1)
          {
            double x2 = Math.cos(Math.toRadians(Minecraft.thePlayer.rotationYaw + 90.0F));
            double z2 = Math.sin(Math.toRadians(Minecraft.thePlayer.rotationYaw + 90.0F));
            double xOffset = MovementInput.moveForward * distance.getValDouble() * x2 + MovementInput.moveStrafe * 0.4D * z2;
            double zOffset = MovementInput.moveForward * distance.getValDouble() * z2 + MovementInput.moveStrafe * 0.4D * x2;
            double x = Minecraft.thePlayer.posX + xOffset;
            double y = Minecraft.thePlayer.posY - 1.0D;
            double z = Minecraft.thePlayer.posZ + zOffset;
            BlockPos blockBelow1 = new BlockPos(x, y, z);
            new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
            if (Minecraft.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air)
            {
              if ((mc.getCurrentServerData().serverIP.contains("hypixel") & !mc.isSingleplayer()))
              {
                int oldSlot = Minecraft.thePlayer.inventory.currentItem;
                Minecraft.thePlayer.inventory.currentItem = getBlockSlot();
                net.minecraft.util.Timer.timerSpeed = Minecraft.thePlayer.ticksExisted % 2 == 0 ? 1.0F : 1.085F;
                BlockUtils.placeBlockScaffold(blockBelow1);
                Minecraft.thePlayer.inventory.currentItem = oldSlot;
              }
              else
              {
                this.blockData = getBlockData(blockBelow1);
                getBlockData(blockBelow1);
              }
              this.slot = tempSlot;
              if (this.blockData != null) {
                if (mode.getValString().equalsIgnoreCase("CubeCraft"))
                {
                  int newSlot = getBlockSlot();
                  int oldSlot = Minecraft.thePlayer.inventory.currentItem;
                  Minecraft.thePlayer.inventory.currentItem = newSlot;
                  this.original += 1;
                  BlockUtils.getFacingRotations(blockBelow1.getX(), blockBelow1.getY(), blockBelow1.getZ(), EnumFacing.UP);
                  event.setYaw(Minecraft.thePlayer.rotationYawHead += (Minecraft.thePlayer.ticksExisted % 2 == 0 ? 80.0F : 79.0F));
                  event.setPitch(Minecraft.thePlayer.ticksExisted % 2 == 0 ? getBlockRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), EnumFacing.DOWN)[1] : getRotationsForBlock(blockBelow1, EnumFacing.DOWN)[1]);
                  PlayerUtils.rayTraceBlock(new BlockPos(blockBelow1.getX(), blockBelow1.getY(), blockBelow1.getZ()), event.getYaw(), event.getPitch());
                  if (!mc.getCurrentServerData().serverIP.contains("cubecraft")) {
                    Minecraft.thePlayer.inventory.currentItem = oldSlot;
                  }
                }
                else
                {
                  event.setYaw(getBlockRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face)[0]);
                  event.setPitch(getBlockRotations(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face)[1]);
                }
              }
            }
          }
        }
      }
      else if ((event.getType() == Event.Type.POST) && (this.blockData != null) && (this.timer.hasReached(75L)) && (this.slot != -1))
      {
        mc.rightClickDelayTimer = 3;
        boolean doHax = Minecraft.thePlayer.inventory.currentItem != this.slot;
        if (doHax) {
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
        }
        if (Minecraft.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()))) {
          Minecraft.thePlayer.swingItem();
        }
        if ((mc.gameSettings.keyBindJump.pressed) && (!mc.gameSettings.keyBindForward.pressed))
        {
          if ((this.timer.hasReached(1000L) & !mode.getValString().equalsIgnoreCase("CubeCraft")))
          {
            Minecraft.thePlayer.motionY = 0.0D;
            this.timer.reset();
          }
          if (!mode.getValString().equalsIgnoreCase("CubeCraft")) {
            Minecraft.thePlayer.motionY = 0.4D;
          }
        }
      }
    }
  }
}
