package me.rhys.client.module.player.scaffold;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.SafeWalkEvent;
import me.rhys.base.event.impl.render.RenderGameOverlayEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.base.util.vec.Vec3f;
import me.rhys.client.module.player.scaffold.modes.Expand;
import me.rhys.client.module.player.scaffold.modes.MineLand;
import me.rhys.client.module.player.scaffold.modes.Morgan;
import me.rhys.client.module.player.scaffold.modes.NCP;
import me.rhys.client.module.player.scaffold.modes.Verus;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Scaffold extends Module {
  @Name("Swing")
  public boolean swing;
  
  @Name("Sprint")
  public boolean sprint;
  
  @Name("Tower")
  public boolean tower;
  
  @Name("Show Amount")
  public boolean showAmount;
  
  @Name("SafeWalk")
  public boolean safeWalk;
  
  @Name("Delay")
  @Clamp(min = 0.0D, max = 9000.0D)
  public int delay;
  
  public final Timer delayTimer;
  
  public Scaffold(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.swing = false;
    this.sprint = true;
    this.tower = true;
    this.showAmount = true;
    this.safeWalk = true;
    this.delay = 0;
    this.delayTimer = new Timer();
    add((Object[])new ModuleMode[] { (ModuleMode)new NCP("NCP", this), (ModuleMode)new Verus("Verus", this), (ModuleMode)new Morgan("Morgan", this), (ModuleMode)new MineLand("MineLand", this), (ModuleMode)new Expand("Expand", this) });
  }
  
  public void onEnable() {
    this.delayTimer.reset();
  }
  
  @EventTarget
  void onRender(RenderGameOverlayEvent event) {
    if (this.showAmount) {
      int amount = getBlockCount();
      String str = EnumChatFormatting.GRAY + "Blocks: " + ((amount > 60) ? (String)EnumChatFormatting.GREEN : (String)EnumChatFormatting.RED) + amount;
      FontUtil.drawStringWithShadow(str, new Vec2f((event
            .getWidth() - FontUtil.typeToFont().getStringWidth(str)) / 2.0F, (event
            .getHeight() - FontUtil.typeToFont().getHeight()) / 2.0F - 15.0F), -1);
    } 
  }
  
  @EventTarget
  void onSafeWalk(SafeWalkEvent event) {
    event.setCancelled(this.safeWalk);
  }
  
  public BlockEntry findExpand(Vec3 offset3, int expand) {
    EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
    BlockPos position = (new BlockPos(this.mc.thePlayer.getPositionVector().add(offset3))).offset(EnumFacing.DOWN);
    if (!(this.mc.theWorld.getBlockState(position).getBlock() instanceof net.minecraft.block.BlockAir))
      return null; 
    EnumFacing[] arrayOfEnumFacing1;
    int j;
    byte b;
    for (arrayOfEnumFacing1 = EnumFacing.values(), j = arrayOfEnumFacing1.length, b = 0; b < j; ) {
      EnumFacing facing = arrayOfEnumFacing1[b];
      BlockPos offset = position.offset(facing);
      if (this.mc.theWorld.getBlockState(offset).getBlock() instanceof net.minecraft.block.BlockAir || 
        !rayTrace(this.mc.thePlayer.getLook(0.0F), 
          getPositionByFace(offset, invert[facing.ordinal()]))) {
        b++;
        continue;
      } 
      return new BlockEntry(offset, invert[facing.ordinal()]);
    } 
    for (int i = 0; i < expand; i++) {
      BlockPos[] offsets = { new BlockPos(-i, 0, 0), new BlockPos(i, 0, 0), new BlockPos(0, 0, -i), new BlockPos(0, 0, i) };
      for (BlockPos offset : offsets) {
        BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
        if (this.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof net.minecraft.block.BlockAir) {
          EnumFacing[] arrayOfEnumFacing;
          int k;
          byte b1;
          for (arrayOfEnumFacing = EnumFacing.values(), k = arrayOfEnumFacing.length, b1 = 0; b1 < k; ) {
            EnumFacing facing = arrayOfEnumFacing[b1];
            BlockPos offset2 = offsetPos.offset(facing);
            if (this.mc.theWorld.getBlockState(offset2).getBlock() instanceof net.minecraft.block.BlockAir || 
              !rayTrace(this.mc.thePlayer.getLook(0.0F), 
                getPositionByFace(offset, invert[facing.ordinal()]))) {
              b1++;
              continue;
            } 
            return new BlockEntry(offset2, invert[facing.ordinal()]);
          } 
        } 
      } 
    } 
    return null;
  }
  
  public boolean placeBlock(BlockPos blockPos, EnumFacing facing, int slot, boolean swing) {
    if (this.delayTimer.hasReached(this.delay)) {
      this.delayTimer.reset();
      BlockPos offset = blockPos.offset(facing);
      EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
      if (rayTrace(this.mc.thePlayer.getLook(0.0F), getPositionByFace(offset, invert[facing
              .ordinal()]))) {
        ItemStack stack = this.mc.thePlayer.inventory.getStackInSlot(slot);
        Vec3f hitVec = (new Vec3f(blockPos)).add(0.5F, 0.5F, 0.5F).add((new Vec3f(facing.getDirectionVec())).scale(0.5F));
        if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, stack, blockPos, facing, new Vec3(hitVec.x, hitVec.y, hitVec.z))) {
          if (swing) {
            this.mc.thePlayer.swingItem();
          } else {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
          } 
          return true;
        } 
      } 
    } 
    return false;
  }
  
  private boolean isPlaceable(ItemStack itemStack) {
    if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
      Block block = ((ItemBlock)itemStack.getItem()).getBlock();
      return (!(block instanceof net.minecraft.block.BlockNote) && !(block instanceof net.minecraft.block.BlockFurnace) && 
        !block.getLocalizedName().equalsIgnoreCase("Crafting Table") && !(block instanceof net.minecraft.block.BlockWeb) && !(block instanceof net.minecraft.block.BlockFence) && !(block instanceof net.minecraft.block.BlockFenceGate) && !(block instanceof net.minecraft.block.BlockSlab) && !(block instanceof net.minecraft.block.BlockStairs));
    } 
    return true;
  }
  
  public int getSlotWithBlock() {
    for (int i = 0; i < 9; i++) {
      ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
      if (isPlaceable(itemStack) && (
        itemStack == null || (!(itemStack.getItem() instanceof net.minecraft.item.ItemAnvilBlock) && (
        !(itemStack.getItem() instanceof ItemBlock) || 
        !(((ItemBlock)itemStack.getItem()).getBlock() instanceof net.minecraft.block.BlockSand)))))
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock && (
          (((ItemBlock)itemStack.getItem()).getBlock()).maxY - 
          (((ItemBlock)itemStack.getItem()).getBlock()).minY == 1.0D || itemStack.getItem() instanceof net.minecraft.item.ItemAnvilBlock))
          return i;  
    } 
    return -1;
  }
  
  public int getBlockCount() {
    AtomicInteger count = new AtomicInteger(0);
    (player()).inventoryContainer.getInventory().stream()
      .filter(Objects::nonNull).filter(itemStack -> 
        (itemStack.getItem() instanceof ItemBlock && isPlaceable(itemStack)))
      .forEach(itemStack -> count.addAndGet(itemStack.stackSize));
    return count.get();
  }
  
  public boolean placeBlockVerus(BlockPos blockPos, EnumFacing facing, int slot, boolean swing) {
    if (this.delayTimer.hasReached(this.delay)) {
      this.delayTimer.reset();
      BlockPos offset = blockPos.offset(facing);
      EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
      if (rayTrace(this.mc.thePlayer.getLook(0.0F), getPositionByFace(offset, invert[facing
              .ordinal()]))) {
        ItemStack stack = this.mc.thePlayer.inventory.getStackInSlot(slot);
        float f = MathUtil.randFloat(0.3F, 0.5F);
        Vec3f hitVec = (new Vec3f(blockPos)).add(f, f, f).add((new Vec3f(facing.getDirectionVec())).scale(f));
        if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, stack, blockPos, facing, new Vec3(hitVec.x, hitVec.y, hitVec.z))) {
          if (swing) {
            this.mc.thePlayer.swingItem();
          } else {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
          } 
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
    Vec3 offset = new Vec3(facing.getDirectionVec().getX() / 2.0D, facing.getDirectionVec().getY() / 2.0D, facing.getDirectionVec().getZ() / 2.0D);
    Vec3 point = new Vec3(position.getX() + 0.5D, position.getY() + 0.75D, position.getZ() + 0.5D);
    return point.add(offset);
  }
  
  public BlockEntry find(Vec3 offset3) {
    EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
    BlockPos position = (new BlockPos(this.mc.thePlayer.getPositionVector().add(offset3))).offset(EnumFacing.DOWN);
    EnumFacing[] arrayOfEnumFacing1;
    int i;
    for (arrayOfEnumFacing1 = EnumFacing.values(), i = arrayOfEnumFacing1.length, null = 0; null < i; ) {
      EnumFacing facing = arrayOfEnumFacing1[null];
      BlockPos offset = position.offset(facing);
      if (this.mc.theWorld.getBlockState(offset).getBlock() instanceof net.minecraft.block.BlockAir || 
        !rayTrace(this.mc.thePlayer.getLook(0.0F), 
          getPositionByFace(offset, invert[facing.ordinal()]))) {
        null++;
        continue;
      } 
      return new BlockEntry(offset, invert[facing.ordinal()]);
    } 
    BlockPos[] offsets = { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
    for (BlockPos offset : offsets) {
      BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
      if (this.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof net.minecraft.block.BlockAir) {
        EnumFacing[] arrayOfEnumFacing;
        int j;
        byte b;
        for (arrayOfEnumFacing = EnumFacing.values(), j = arrayOfEnumFacing.length, b = 0; b < j; ) {
          EnumFacing facing = arrayOfEnumFacing[b];
          BlockPos offset2 = offsetPos.offset(facing);
          if (this.mc.theWorld.getBlockState(offset2).getBlock() instanceof net.minecraft.block.BlockAir || 
            !rayTrace(this.mc.thePlayer.getLook(0.0F), 
              getPositionByFace(offset, invert[facing.ordinal()]))) {
            b++;
            continue;
          } 
          return new BlockEntry(offset2, invert[facing.ordinal()]);
        } 
      } 
    } 
    return null;
  }
  
  private boolean rayTrace(Vec3 origin, Vec3 position) {
    Vec3 difference = position.subtract(origin);
    int steps = 10;
    double x = difference.xCoord / steps;
    double y = difference.yCoord / steps;
    double z = difference.zCoord / steps;
    Vec3 point = origin;
    for (int i = 0; i < steps; i++) {
      BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
      IBlockState blockState = this.mc.theWorld.getBlockState(blockPosition);
      if (!(blockState.getBlock() instanceof net.minecraft.block.BlockLiquid) && !(blockState.getBlock() instanceof net.minecraft.block.BlockAir)) {
        AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox((World)this.mc.theWorld, blockPosition, blockState);
        if (boundingBox == null)
          boundingBox = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D); 
        if (boundingBox.offset(blockPosition).isVecInside(point))
          return false; 
      } 
    } 
    return true;
  }
  
  public class BlockEntry {
    private final BlockPos position;
    
    private final EnumFacing facing;
    
    BlockEntry(BlockPos position, EnumFacing facing) {
      this.position = position;
      this.facing = facing;
    }
    
    public BlockPos getPosition() {
      return this.position;
    }
    
    public EnumFacing getFacing() {
      return this.facing;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\scaffold\Scaffold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */