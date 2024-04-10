package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.sigma5.killaura.NCPRotation;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.sigma5.utils.Sigma5DrawText;
import info.sigmaclient.sigma.sigma5.utils.SigmaRenderUtils;
import info.sigmaclient.sigma.sigma5.utils.핇댠䂷呓贞;
import info.sigmaclient.sigma.utils.RandomUtil;
import info.sigmaclient.sigma.utils.UnusedRtation;
import info.sigmaclient.sigma.utils.VecUtils;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import info.sigmaclient.sigma.utils.player.Rotation;
import info.sigmaclient.sigma.utils.player.ScaffoldUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CAnimateHandPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static info.sigmaclient.sigma.gui.Sigma5LoadProgressGui.霥瀳놣㠠釒;
import static info.sigmaclient.sigma.utils.player.MovementUtils.setSpeed;
import static info.sigmaclient.sigma.utils.player.RotationUtils.*;
import static info.sigmaclient.sigma.utils.player.ScaffoldUtils.isOkBlock;
import static net.optifine.reflect.Reflector.Minecraft;

public class BlockFly extends Module {
    public ModeValue type = new ModeValue("Type", "Hypixel", new String[]{
            "Custom", "Intave", "Hypixel", "Legit", "NCP"
    });
    public NumberValue yawSpeed = new NumberValue("YawSpeed", 40, 0, 180, NumberValue.NUMBER_TYPE.FLOAT) {@Override public boolean isHidden() {return (type.is("Hypixel") || type.is("NCP"));}};
    public NumberValue pitchSpeed = new NumberValue("PitchSpeed", 60, 0, 180, NumberValue.NUMBER_TYPE.FLOAT) {@Override public boolean isHidden() {return (type.is("Hypixel") || type.is("NCP"));}};
    public BooleanValue speedSlowDowbn = new BooleanValue("SpeedSlowDown", false) {@Override public boolean isHidden() {return type.is("Intave");}};
    public NumberValue speedSlow = new NumberValue("SpeedSlow", 0.8, 0, 1, NumberValue.NUMBER_TYPE.FLOAT) {@Override public boolean isHidden() {return type.is("Intave");}};
    public NumberValue expand = new NumberValue("Expand", 0, 0, 5, NumberValue.NUMBER_TYPE.INT) {@Override public boolean isHidden() {return !type.is("Custom");}};
    public NumberValue search = new NumberValue("Range", 1, 1, 4, NumberValue.NUMBER_TYPE.INT);
//    public BooleanValue searchXZ = new BooleanValue("XZSearcher", false);
//    public BooleanValue searchOnly = new BooleanValue("OnlyOneSearch", false);
    public BooleanValue addStrafe = new BooleanValue("AdStrafe", false) {@Override public boolean isHidden() {return (type.is("Hypixel") || type.is("NCP"));}};
    public BooleanValue raytrace = new BooleanValue("Raytrace", false) {@Override public boolean isHidden() {return (type.is("Hypixel") || type.is("NCP"));}};
    public BooleanValue movementFix = new BooleanValue("MovementFix", false) {@Override public boolean isHidden() {return (type.is("Hypixel") || type.is("NCP"));}};
    public BooleanValue noSwing = new BooleanValue("NoSwing", false);
    public BooleanValue bcounter = new BooleanValue("Show Block Amount", true);
    public BooleanValue towerMove = new BooleanValue("Tower", false);
    public BooleanValue towerWhileMove = new BooleanValue("Tower while move", false){@Override public boolean isHidden() {return !towerMove.isEnable();}};
    public ModeValue towerMode = new ModeValue("TowerMode", "Vanilla", new String[]{"Vanilla", "NewNCP", "NewNCP2", "NCP", "Test", "Hypixel", "Hypixel2"}){@Override public boolean isHidden() {return !towerMove.isEnable();}};
    public ModeValue place = new ModeValue("PlaceTiming", "Legit", new String[]{"Legit", "Prev", "Post"});
    public ModeValue itemSpoofMode = new ModeValue("ItemSpoofMode", "Switch", new String[]{"Switch", "Spoof", "LiteSpoof"});
    public ModeValue customRotationTime = new ModeValue("RotationTime", "Air", new String[]{"Air", "MM", "AirTower", "MMTower", "Always"}){@Override public boolean isHidden() {return customRotation.isHidden();}};
    public ModeValue customRotation = new ModeValue("Rotation", "Advanced",
            new String[]{"Basic", "Nearest", "Nearest2", "Advanced", "Static", "Smart", "Smart2", "Intave"
    }){@Override public boolean isHidden() {return !type.is("Custom");}};
    public NumberValue staticYaw = new NumberValue("StaticYaw", 180, 0, 180, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !customRotation.is("Static") || customRotation.isHidden();}};
    public NumberValue staticPitch = new NumberValue("StaticPitch", 80.4, 0, 90, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {return !customRotation.is("Static") || customRotation.isHidden();}};
    public ModeValue customRotationAddons = new ModeValue("RotationAddons", "None", new String[]{"None", "Random", "SmallRand", "OnlyPitch", "OnlyYaw"}) {@Override public boolean isHidden() {return customRotation.isHidden();}};
    public ModeValue placeTime = new ModeValue("PlaceMode", "Normal", new String[]{"Normal", "Telly"}){@Override public boolean isHidden() {return false;}};
    public BooleanValue keepY = new BooleanValue("KeepY", false);
    public BooleanValue jump = new BooleanValue("AutoJump", false){@Override public boolean isHidden() {return !keepY.isEnable();}};
    public BooleanValue sprint = new BooleanValue("NoSprint", false);
    public BooleanValue sneakWhenRotating = new BooleanValue("Sneak When Rotating", false);
    public BooleanValue spam = new BooleanValue("SpamClick", false){
        @Override
        public boolean isHidden() {
            return (type.is("Hypixel") || type.is("NCP"));
        }
    };
    public NumberValue placeDelay = new NumberValue("Spam Delay", 0.1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT){@Override public boolean isHidden() {
            return !spam.isEnable() || (type.is("Hypixel") || type.is("NCP"));
        }};
    public BooleanValue eagle = new BooleanValue("Eagle", false);
    public NumberValue eagleTicks = new NumberValue("EagleTicks", 1, 1, 10, NumberValue.NUMBER_TYPE.INT){@Override public boolean isHidden() {
            return !eagle.isEnable();
        }};
    int stage = 0;

    public BlockPos expand(BlockPos now, double max){
        double dist = 0;
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float YAW = mc.player.rotationYaw;
        BlockPos underPos = now;
        while(isOkBlock(underPos)){
            double xCalc = now.getX();
            double zCalc = now.getZ();
            dist += 0.5;
            if(dist > max){
                break;
            }
            double cos = Math.cos(Math.toRadians(YAW + 90.0f));
            double sin = Math.sin(Math.toRadians(YAW + 90.0f));
            xCalc += (forward * cos + strafe * sin) * dist;
            zCalc += (forward * sin - strafe * cos) * dist;
            underPos = new BlockPos(xCalc, mc.player.getPosY() - 0.5, zCalc);
        }
        return underPos;
    }
    public BlockFly() {
        super("BlockFly", Category.Movement, "Flying under block");
     registerValue(type);
     registerValue(yawSpeed);
     registerValue(pitchSpeed);
     registerValue(speedSlowDowbn);
     registerValue(speedSlow);
     registerValue(expand);
     registerValue(search);
     registerValue(addStrafe);
     registerValue(raytrace);
     registerValue(movementFix);
     registerValue(noSwing);
     registerValue(bcounter);
     registerValue(towerMove);
     registerValue(towerWhileMove);
     registerValue(towerMode);
//     registerValue(place);
     registerValue(itemSpoofMode);
     registerValue(customRotationTime);
     registerValue(customRotation);
     registerValue(staticYaw);
     registerValue(staticPitch);
     registerValue(customRotationAddons);
     registerValue(placeTime);
     registerValue(keepY);
     registerValue(jump);
     registerValue(sprint);
     registerValue(sneakWhenRotating);
     registerValue(spam);
     registerValue(placeDelay);
     registerValue(eagle);
     registerValue(eagleTicks);
    }
    ScaffoldUtils.BlockCache blockPos = null;
    boolean idk = false;
    int idkTick = 0;
    int towerTick = 0;
    boolean towering = false;
    int airTicks = 0;
    int groundTime = 0;
    int slot = -1;
    int act_slot = -1;
    int rslot = -1;
    boolean NCPRotUpdate = false;
    public static boolean backward = false;
    double y = 0;
    int sneakAfterTick = 0;
    boolean doneRotating = false;
    PartialTicksAnim blockCounterAnim = new PartialTicksAnim(0);
    public static boolean isBlockValid(final Block block) {
        return block != Blocks.GLASS &&
                block != Blocks.SAND &&
                block != Blocks.GRAVEL &&
                block != Blocks.DISPENSER &&
                block != Blocks.CHEST &&
                block != Blocks.ENDER_CHEST &&
                block != Blocks.COMMAND_BLOCK &&
                block != Blocks.NOTE_BLOCK &&
                block != Blocks.FURNACE &&
                block != Blocks.CRAFTING_TABLE &&
                block != Blocks.TNT &&
                block != Blocks.DROPPER &&
                block != Blocks.BEACON &&
                block != Blocks.ENCHANTING_TABLE &&
                block != Blocks.CARROTS &&
                block != Blocks.WHEAT &&
                block != Blocks.LADDER &&
                block != Blocks.COBWEB &&
                block != Blocks.TORCH;
    }
    float[] rots = new float[]{NO_ROTATION, NO_ROTATION};
    public int getSlot(){
        for(int i = 0; i < 9 ; i++){
            final ItemStack itemStack = mc.player.inventory.mainInventory.get(i);
            if (itemStack.getItem() instanceof BlockItem) {
                final BlockItem BlockItem = (BlockItem) itemStack.getItem();
                if (isBlockValid(BlockItem.getBlock())) {
                    return i;
                }
            }
        }
        return -1;
    }
    public int getBlocksAmount(){
        int i2 = 0;
        for(int i = 0; i < 9;i++){
            final ItemStack itemStack = mc.player.inventory.mainInventory.get(i);
            if (itemStack.getItem() instanceof BlockItem) {
                final BlockItem BlockItem = (BlockItem) itemStack.getItem();
                if (isBlockValid(BlockItem.getBlock())) {
                    i2 += itemStack.getStackSize();
                }
            }
        }
        return Math.min(99, i2);
    }
    boolean adreset = false;
    Vector3d ray = null;
    public ScaffoldUtils.BlockCache canPlace(float[] yawPitch) {
        RayTraceResult m4 = mc.player.customPick(4.5f, 1.0f, yawPitch[0], yawPitch[1]);
        if (m4.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) m4;
            if (rayTraceResult.getFace() != Direction.DOWN && (towering || rayTraceResult.getFace() != Direction.UP)) {
                ray = m4.getHitVec();
                return new ScaffoldUtils.BlockCache(rayTraceResult.getPos(), rayTraceResult.getFace());
            }
        }
        return null;
    }
    int delay = 0;
    public void click() {
        if(blockPos == null) return;
        if(placeTime.is("Telly")){
            if(!isTellying()) return;
        }
        ScaffoldUtils.BlockCache facing = blockPos;
        boolean placed = false;
        if(delay >= placeDelay.getValue().floatValue() * 10){
            delay = 0;
            placed = true;
        }else{

        }
        boolean ex = expand.getValue().intValue() != 0 && type.is("Custom");
        boolean r = raytrace.isEnable() && !(type.is("Hypixel") || type.is("NCP"));
        if (r) {
            facing = canPlace(rots);
            if (facing == null || ray == null
                  || mc.player.getDistanceSq(new Vector3d(blockPos.getPosition()).add(0.5,0.5,0.5)) > 2f * 2f
                    || !doneRotating
            ) {
                return;
            }
            blockPos = facing;
        }
        // itemspoof
        int lastSlot = mc.player.inventory.currentItem;
        if(!itemSpoofMode.getValue().equals("Switch")){
            mc.player.inventory.currentItem = slot;
        }
        BlockPos bp = blockPos.getPosition().add(blockPos.getFacing().getXOffset(), blockPos.getFacing().getYOffset(), blockPos.getFacing().getZOffset());
        if(mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(
                bp.getX(), bp.getY(), bp.getZ(),
                bp.getX() + 1, bp.getY() + 1, bp.getZ() + 1
        )).isEmpty()
                && !mc.world.getBlockState(bp).isSolid()) {
            if (mc.playerController.processRightClickBlock(
                    mc.player,
                    mc.world,
                    r ? facing.getPosition() : blockPos.getPosition(),
                    facing.getFacing(),
                    r ? ray : VecUtils.blockPosRedirection(ex ? new BlockPos(mc.player.getPositionVec()) : blockPos.getPosition(), facing.getFacing()),
                    Hand.MAIN_HAND) == ActionResultType.SUCCESS) {
                placed = false;
            }
            if (noSwing.isEnable()) {
                mc.getConnection().sendPacket(new CAnimateHandPacket(Hand.MAIN_HAND));
            } else {
                mc.player.swingArm(Hand.MAIN_HAND);
            }
        }
        if (placed && this.spam.isEnable() && !(type.is("Hypixel") || type.is("NCP"))) {
            if(mc.playerController.
                    processRightClick(mc.player, mc.world, Hand.MAIN_HAND) == ActionResultType.SUCCESS){
                if(noSwing.isEnable()){
                    mc.getConnection().sendPacket(new CAnimateHandPacket(Hand.MAIN_HAND));
                }else{
                    mc.player.swingArm(Hand.MAIN_HAND);
                }
            }
        }
        placed = true;
        if(!itemSpoofMode.getValue().equals("Switch")){
            mc.player.inventory.currentItem = lastSlot;
        }
        mc.sendClickBlockToController(false);
    }

    @Override
    public void onEnable() {
        idkTick = 5;
        progress = 0;
        doneRotating = false;
        adreset = false;
        rots = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};
        rslot = mc.player.inventory.currentItem;
        act_slot = mc.player.inventory.currentItem;
        y = mc.player.getPosY();
        placed = false;
        rotated = false;
        blockPos = null;
        blockCounterAnim.setValue(0);
        super.onEnable();
    }
    float progress = 0;
    public void tickForAnim(){
        blockCounterAnim.interpolate(this.enabled ? 10 : 0, 8);
    }
    public Sigma5AnimationUtil asff = new Sigma5AnimationUtil(114, 114, Sigma5AnimationUtil.AnimState.SLEEPING);
    public void renderBlockCounter(){
        if(!bcounter.isEnable()) return;
        this.asff.animTo(enabled ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        if (this.asff.getAnim() == 0.0f) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        if(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA)
            this.bmc(sr.getScaledWidth() / 2, (int) (sr.getScaledHeight() - 138 / 2f - (int)(25.0f / 2f * abc(this.asff.getAnim(), 0.0f, 1.0f, 1.0f))), this.asff.getAnim());
        else
            this.bmok(sr.getScaledWidth() / 2, (int) (sr.getScaledHeight() - 138 / 2f - (int)(25.0f / 2f)), this.asff.getAnim());
    }
    public float abc(float n, final float n2, final float n3, final float n4) {
        n /= n4;
        --n;
        return n3 * (n * n * n + 1.0f) + n2;
    }
    public void bmc(int n, int n2, final float n3) {
        final int n4 = 0;
        int count = getBlocksAmount();
        final float n5 = (float) (JelloFontUtil.jelloFont18.getStringWidth(count + "") + 3 / 2f);
        final float n6 = (float) (n4 + n5 + JelloFontUtil.jelloFont14.getStringWidth("Blocks") + 20 / 2);
        final int n7 = 32 / 2;
        n -= n6 / 2;
        GL11.glPushMatrix();
        SigmaRenderUtils.汌ꪕ蒕姮Ⱋ樽(n, n2, n6, n7, 霥瀳놣㠠釒(-15461356, 0.8f * n3));
        Sigma5DrawText.drawString(JelloFontUtil.jelloFont18, (float)(n + 10 / 2), (float)(n2 + 5), count + "", 霥瀳놣㠠釒(핇댠䂷呓贞.white.哺卫콗鱀ಽ, n3));
        Sigma5DrawText.drawString(JelloFontUtil.jelloFont14, (float)(n + 10 / 2+ n5), (float)(n2 + 7), "Blocks", 霥瀳놣㠠釒(핇댠䂷呓贞.white.哺卫콗鱀ಽ, 0.6f * n3));
        n += 11 / 2f + n6 / 2;
        n2 += n7;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)n, (float)n2, 0.0f);
        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n), (float)(-n2), 0.0f);
        RenderUtils.drawTextureLocation((float)n, (float)n2, 9.0f / 2, 23.0f / 2, "alt/select", new Color(霥瀳놣㠠釒(-15461356, 0.8f * n3), true));
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
    public void bmok(int n, int n2, final float n3) {
        final int n4 = 0;
        int count = getBlocksAmount();
        final float n5 = (float) (JelloFontUtil.jelloFont18.getStringWidth(count + "") + 3 / 2f);
        final float n6 = (float) (n4 + n5 + JelloFontUtil.jelloFont14.getStringWidth("Blocks") + 20 / 2);
        final int n7 = 32 / 2;
        n -= n6 / 2;
        float yy = abc(this.asff.getAnim(), 0.0f, 1.0f, 1.0f); // 0 - 1

        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(n - 5, n2 - 5, n - 5 + (n6 + 10) * yy, n2 - 5 + n7 + 10, -1);
        StencilUtil.readStencilBuffer(1);

        GL11.glPushMatrix();
        SigmaRenderUtils.汌ꪕ蒕姮Ⱋ樽(n, n2, n6, n7, 霥瀳놣㠠釒(new Color(36,36,36).getRGB(), 0.8f * n3));
        Sigma5DrawText.drawString(JelloFontUtil.jelloFont18, (float)(n + 10 / 2), (float)(n2 + 5), count + "", 霥瀳놣㠠釒(핇댠䂷呓贞.white.哺卫콗鱀ಽ, n3));
        Sigma5DrawText.drawString(JelloFontUtil.jelloFont14, (float)(n + 10 / 2+ n5), (float)(n2 + 7), "Blocks", 霥瀳놣㠠釒(핇댠䂷呓贞.white.哺卫콗鱀ಽ, 0.6f * n3));
        GL11.glPopMatrix();

        StencilUtil.uninitStencilBuffer();
    }
    boolean placed = false;
    boolean rotated = false;
    @Override
    public void onMoveEvent(MoveEvent event) {
        if(type.is("Hypixel")) {
            if (blockPos != null && !sprint.isEnable()) {
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.player.setSprinting(false);

                if (mc.player.onGround && (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.setSprinting(true);
                    if (mc.player.ticksExisted % 2 == 0) {
                        if (placed) {
                            MovementUtils.strafing(event, 5.71 / 20);
                        }
                    } else {
                        MovementUtils.strafing(event, 5.6000000000009 / 20);
                    }
                }
            }
        }
        super.onMoveEvent(event);
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        placed = false;
//        if(event.isPost()) {
            delay ++;
            if(!placeTime.is("Telly")) {
                if (sprint.isEnable()) {
                    mc.gameSettings.keyBindSprint.pressed = false;
                    mc.player.setSprinting(false);
                }else{
                    if(type.is("Hypixel")){
//                        if(mc.player.ticksExisted % 20 <= 14){
//                        }
                    }
                }
            }else{
                mc.gameSettings.keyBindSprint.pressed = false;
//                if(isTellying() || mc.player.lastReportedYaw != mc.player.rotationYaw){
                mc.player.setSprinting(!(airTicks >= 1));
//                }
            }
//            if(place.is("Post")) {
//                click();
//            }
//            return;
//        }
        if (this.idkTick > 0) {
            --this.idkTick;
        }
        if (this.towerTick > 0) {
            ++this.towerTick;
//            if (this.towerTick > 6) {
//                idk1(MovementUtils.getSpeed() * ((double) (100 - 50) / 100.0));
//            }
            if (this.towerTick > 16) {
                this.towerTick = 0;
            }
        }
        if(type.is("Intave") && rots != null){
//            mc.gameSettings.keyBindLeft.pressed = mc.gameSettings.keyBindForward.pressed;
        }
        if(!mc.player.onGround){
            airTicks ++;
            groundTime = 0;
        }else{
            airTicks = 0;
            groundTime ++;
        }
        if(speedSlowDowbn.isEnable() && !type.is("Intave")){
            mc.player.getMotion().x *= speedSlow.getValue().floatValue();
            mc.player.getMotion().z *= speedSlow.getValue().floatValue();
        }
        if(addStrafe.isEnable() && !(type.is("Hypixel") || type.is("NCP"))){
            if(mc.gameSettings.keyBindForward.pressed){
                adreset = true;
                int tick = mc.player.ticksExisted % 6;
                if(tick < 3){
                    mc.gameSettings.keyBindRight.pressed = true;
                    mc.gameSettings.keyBindLeft.pressed = false;
                }else{
                    mc.gameSettings.keyBindRight.pressed = false;
                    mc.gameSettings.keyBindLeft.pressed = true;
                }
            }else{
                if(adreset) {
                    adreset = false;
                    mc.gameSettings.keyBindRight.pressed = false;
                    mc.gameSettings.keyBindLeft.pressed = false;
                }
            }
        }
        //
        y = mc.player.onGround ? mc.player.getPosY() : (towering ? mc.player.getPosY() : y);
        if (sneakAfterTick > 0) {
            sneakAfterTick--;
            if (sneakAfterTick == 0) {
                mc.gameSettings.keyBindSneak.pressed = false;
            }
        }
        if(sneakWhenRotating.isEnable() && rotated){
            rotated = false;
            boolean block = mc.world.getBlockState(
                    new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 0.5, mc.player.getPosZ())
            ).getBlock() instanceof AirBlock;
            if (!mc.player.onGround) {
                mc.gameSettings.keyBindSneak.pressed = false;
            } else {
//                if (block) {
                    mc.gameSettings.keyBindSneak.pressed = true;
                    sneakAfterTick = 2;
//                } else {
//                }
            }
        }
        // todo eagle
        if(eagle.isEnable()) {
            boolean block = mc.world.getBlockState(
                    new BlockPos(mc.player.getPosX(), mc.player.getPosY() - 0.5, mc.player.getPosZ())
            ).getBlock() instanceof AirBlock;
            if (!mc.player.onGround) {
                mc.gameSettings.keyBindSneak.pressed = false;
            } else {
                if (block) {
                    mc.gameSettings.keyBindSneak.pressed = true;
                    sneakAfterTick = eagleTicks.getValue().intValue();
                }
            }
        }
        if(jump.isEnable() && keepY.isEnable()){
            if(placeTime.is("Telly")){
                if(!InputMappings.isKeyDown(mc.gameSettings.keyBindJump))
                    mc.gameSettings.keyBindJump.pressed = (MovementUtils.isMoving() && mc.player.isSprinting());
            }else
                mc.gameSettings.keyBindJump.pressed = MovementUtils.isMoving();
        }
        if(placeTime.is("Telly")) {
            if(!isTellying())
                return;
        }
        towering = false;
        slot = getSlot();
        BlockPos downPos = new BlockPos(mc.player.getPositionVector().add(0, -0.5, 0));
        if(keepY.isEnable())
            downPos = new BlockPos(downPos.getX(), y - 0.5, downPos.getZ());
        blockPos = ScaffoldUtils.getBlockCache(downPos, search.getValue().intValue(), false, false);
        boolean ex = expand.getValue().intValue() != 0 && type.is("Custom");
        if(blockPos != null && ex){
            blockPos = new ScaffoldUtils.BlockCache(
                    expand(new BlockPos(
                            mc.player.getPositionVector().add(0, -0.5, 0)
                    ), expand.getValue().floatValue()),
                    blockPos.getFacing()
            );
        }
        if(blockPos == null || slot == -1) {
            this.rots = mouseSens(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.lastReportedYaw, mc.player.lastReportedPitch);
            backward = false;
            return;
        }
        switch (itemSpoofMode.getValue()){
            case "Switch":
                mc.player.inventory.currentItem = slot;
                break;
        }
        mc.timer.setTimerSpeed(1f);
        if(InputMappings.isKeyDown(mc.gameSettings.keyBindJump)){
            towering = true;
            if(towerMove.isEnable() && (towerWhileMove.isEnable() || !MovementUtils.isMoving())) {
                mc.gameSettings.keyBindJump.pressed = false;
                switch (towerMode.getValue()) {
                    case "Vanilla":
                        mc.player.getMotion().y = 0.5;
                        mc.player.addStat(Stats.JUMP);
                        mc.player.isAirBorne = true;
                        break;
                    case "Test":
                        if (mc.player.onGround) {
                            mc.player.addStat(Stats.JUMP);
                            mc.player.isAirBorne = true;
                            mc.player.getMotion().y = 0.3682;
                        }
                        break;
                    case "VulcanFast":
                        mc.player.setPosition(mc.player.getPosX(), mc.player.getPosY() + 0.5, mc.player.getPosZ());
                        mc.player.getMotion().y = 0;
                        mc.player.addStat(Stats.JUMP);
                        mc.player.isAirBorne = true;
//                        event.onGround = true;
//                        event.y = event.y - (event.y % 0.015625);
                        break;
                    case "NewNCP":
                        if (mc.player.getPosY() - blockPos.getPosition().getY() <= 3) {
                            if (mc.player.onGround) {
                                stage = 0;
                                mc.player.getMotion().y = 0.41999998688697815;
                            } else {
                                stage++;
                                if (mc.player.getMotion().y <= 0.2) {
                                    mc.player.setPosition(mc.player.getPosX(), (int) mc.player.getPosY(), mc.player.getPosZ());
                                    mc.player.getMotion().y = 0.41999998688697815;
                                }
                            }
                            NCPRotUpdate = true;
                        }
                        break;
                    case "NewNCP2":
                        if (mc.player.getPosY() - blockPos.getPosition().getY() <= 3) {
                            if (mc.player.getPosY() % 0.125 == 0) {
                                stage = 0;
                                mc.player.getMotion().y = 0.41999998688697815;
                            } else {
                                stage++;
                                if (mc.player.getMotion().y <= 0.2) {
                                    mc.player.setPosition(mc.player.getPosX(), (int) mc.player.getPosY(), mc.player.getPosZ());
                                    mc.player.getMotion().y = 0;
                                }
                            }
                            NCPRotUpdate = true;
                        }
                        break;
                    case "Hypixel":
                        if (this.idkTick != 0) {
                            this.towerTick = 0;
                            return;
                        }

                        if (mc.player.onGround) {
                            if (this.towerTick == 0 || this.towerTick == 5) {
                                float f = mc.player.rotationYaw * ((float) Math.PI / 180);
                                mc.player.getMotion().x -= (double) (MathHelper.sin(f) * 0.2f) * 50 / 100.0;
                                mc.player.getMotion().y = 0.41999998688697815;
                                mc.player.getMotion().z += (double) (MathHelper.cos(f) * 0.2f) * 50 / 100.0;
                                this.towerTick = 1;
                            }
                        } else if (mc.player.getMotion().y > -0.0784000015258789) {
                            int n = (int) Math.round(mc.player.getPosY() % 1.0 * 100.0);
                            switch (n) {
                                case 42: {
                                    mc.player.getMotion().y = 0.33;
                                    break;
                                }
                                case 75: {
                                    mc.player.getMotion().y = 1.0 - mc.player.getPosY() % 1.0;
                                    this.idk = true;
                                    break;
                                }
                                case 0: {
                                    mc.player.getMotion().y = -0.0784000015258789;
                                }
                            }
                        }
                        break;
                    case "Hypixel2":
                        if (this.idkTick != 0) {
                            this.towerTick = 0;
                            return;
                        }

                        if (mc.player.onGround) {
                            if (this.towerTick == 0 || this.towerTick == 5) {
                                float f = mc.player.rotationYaw * ((float) Math.PI / 180);
                                mc.player.getMotion().x -= (double) (MathHelper.sin(f) * 0.2f) * 50 / 100.0;
                                mc.player.getMotion().y = 0.41965;
                                mc.player.getMotion().z += (double) (MathHelper.cos(f) * 0.2f) * 50 / 100.0;
                                this.towerTick = 1;
                            }
                            if (towerTick == 1) {
                                mc.player.getMotion().y = 0.33;
                            }
                            if (towerTick == 2) {
                                mc.player.getMotion().y = 1;
                            }
                            if (towerTick == 3) {
                                towerTick = 0;
                            }
                        }
                        if (towerTick >= 3) {
                            towerTick = 0;
                        }
                        mc = Minecraft.getInstance();
                        mc.gameSettings.keyBindJump.isPressed();
                        KeyBinding keyBindJump = null;
                        if (keyBindJump.isPressed()) {
                            setSpeed(0);
                            towerTick = 0;
                        }
                        break;
                case "NCP":
                    if (mc.player.onGround) {
                        mc.player.getMotion().y = 0.41999998688697815;
                        mc.player.addStat(Stats.JUMP);
                    } else {
                        if (mc.player.getMotion().y <= 0 && mc.player.getMotion().y >= -0.2) {
                            mc.player.getMotion().y -= 0.08;
                        }
                    }
                    break;
                }
            }
        }else {
            if (NCPRotUpdate) {
//                mc.player.setPosition(mc.player.getPosX(), (int) mc.player.getPosY(), mc.player.getPosZ());
                NCPRotUpdate = false;
            }
        }
        float[] calcRotation = scaffoldRots(
                blockPos.getPosition().getX() + 0.5 + blockPos.getFacing().getXOffset() / 2.0f,
                blockPos.getPosition().getY() + 0.5 + blockPos.getFacing().getYOffset() / 2.0f,
                blockPos.getPosition().getZ() + 0.5 + blockPos.getFacing().getZOffset() / 2.0f,
                mc.player.lastReportedYaw,
                mc.player.lastReportedPitch,
                this.yawSpeed.getValue().floatValue(),
                this.pitchSpeed.getValue().floatValue(),
                false);
        boolean rotationChange = shouldBuild(), underAir = mc.world.getBlockState(downPos).getBlock() instanceof AirBlock;
        if(type.is("Custom")) {
            if (rotationChange) {
                doneRotating = true;
                switch (customRotation.getValue()) {
                    case "Basic":
                        this.rots = calcRotation;
                        break;
                    case "Nearest2":
                        this.rots[0] = mc.player.rotationYaw - 180f;
                        getYawBasedPitch(this.rots[0], true);
                        break;
                    case "Static":
                        this.rots[0] = staticYaw.getValue().floatValue() + mc.player.rotationYaw;
                        this.rots[0] = MathHelper.wrapAngleTo180_float(this.rots[0]);
                        this.rots[1] = staticPitch.getValue().floatValue();
                        break;
                    case "Nearest":
                        this.rots[0] = mc.player.rotationYaw - 181f;
                        getYawBasedPitch(this.rots[0], false);
                        break;
                    case "Smart":
                        Rotation r = NCPRotation.NCPRotation(new AxisAlignedBB(
                                blockPos.getPosition().getX(),
                                blockPos.getPosition().getY(),
                                blockPos.getPosition().getZ(),
                                blockPos.getPosition().getX() + 1,
                                blockPos.getPosition().getY() + 1,
                                blockPos.getPosition().getZ() + 1
                        ));
                        float yaw2 = (float) (MathHelper.wrapAngleTo180_float((float) (MovementUtils.directionNoRadians() - 180f)));
                        float my = Math.round(yaw2 / 45.0f) * 45f;
                        if(mc.player.onGround && (Math.abs(getAngleDifference(my, r.getYaw())) < 36)) {
                            this.rots[0] = my;
                            getYawBasedPitch(this.rots[0], false);
                        }else{
                            this.rots[0] = r.getYaw();
                            this.rots[1] = r.getPitch() + 3;
                        }
                        break;
                    case "Smart2":
                        Rotation r2 = NCPRotation.NCPRotation(new AxisAlignedBB(
                                blockPos.getPosition().getX(),
                                blockPos.getPosition().getY(),
                                blockPos.getPosition().getZ(),
                                blockPos.getPosition().getX() + 1,
                                blockPos.getPosition().getY() + 1,
                                blockPos.getPosition().getZ() + 1
                        ));
                        float yaw = (float) (MathHelper.wrapAngleTo180_float((float) (MovementUtils.directionNoRadians() - 180f)));
                        if(underAir) {
                            this.rots[0] = r2.getYaw() + getAngleDifference(yaw,r2.getYaw()) * 0.7f;
                        }else{
                            this.rots[0] = mc.player.rotationYaw - 180f;
                        }
                        getYawBasedPitch(this.rots[0], false);
                        break;
                    case "Intave":
                        RayTraceResult m4 = mc.player.customPick(4.5f, 1.0f, mc.player.lastReportedYaw, mc.player.lastReportedPitch);
                        boolean refind = false;
                        if (m4.getType() == RayTraceResult.Type.BLOCK) {
                            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) m4;
                            if((!rayTraceResult.getFace().equals(blockPos.getFacing()) || !rayTraceResult.getPos().equals(blockPos.getPosition()))) {
                                refind = true;

                            }
                        }else{
                            refind = true;
                        }
                        if(refind){
                            float move = (float) MovementUtils.directionNoRadians() - 180;
//                            if(move % 45 >= 45 - 15 && move % 45 <= 15){
//                                float sb = this.rots[0] / 90;
//                                float ok = 0;
//                                if(sb % 1f >= 0.25 && sb % 1f <= 0.75){
//                                    ok = 0.5f;
//                                }else if(sb % 1f <= 0.25){
//                                    ok = 0;
//                                }else if(sb % 1f >= 0.75){
//                                    ok = 1;
//                                }
//                                ok = (int)sb + ok * 90;
//                                this.rots[0] = ok;
//                            }else{
                                this.rots[0] = (float) (Math.floor((move + 22.5f) / 45) * 45);
                            this.rots[0] += 1;
//                            }
                            float oldP = mc.player.lastReportedPitch;
                            getYawBasedPitch(this.rots[0], false);
                            if(oldP == this.rots[1] && underAir){
                            this.rots[0] = this.rots[0] + getAngleDifference(this.rots[0], calcRotation[0]) * 0.1f;
                                getYawBasedPitch(this.rots[0], false);
                            }
                        }
//                        }else{
//                            mc.gameSettings.keyBindLeft.pressed = false;
//                            mc.gameSettings.keyBindRight.pressed = false;
//                            this.rots[0] = r3.getYaw() + RotationUtils.getAngleDifference(r3.getYaw(), calcRotation[0]) * 0.1f;
//                            this.rots[0] = this.rots[0] + RotationUtils.getAngleDifference(yaw21,this.rots[0]) * 0.8f;
//                            getYawBasedPitch(this.rots[0], false);
//                        }
                        if(underAir) {
//                            this.rots[0] = r3.getYaw() + RotationUtils.getAngleDifference(r3.getYaw(), yaw21) * 0.7f;
                        }else{
//                            this.rots[0] = mc.player.rotationYaw - 180f;
                        }
                        break;
                    case "Advanced":
                        this.rots[0] = mc.player.rotationYaw - 181.3876f + RandomUtil.nextFloat(-0.111, 0.222);
                        this.rots[0] = MathHelper.wrapAngleTo180_float(this.rots[0]);
                        this.rots[1] = calcRotation[1];
                        this.rots[1] += RandomUtil.nextFloat(0, 0.0011);
                        if (mc.player.hurtTime > 0) {
                            this.rots = calcRotation;
                            this.rots[0] += RandomUtil.nextFloat(0, 0.11);
                            this.rots[1] += RandomUtil.nextFloat(0, 0.11);
                        }
                        break;
                }
                switch (customRotationAddons.getValue()) {
                    case "Random":
                        this.rots[0] += RandomUtil.nextFloat(0, 0.011);
                        this.rots[1] += RandomUtil.nextFloat(0, 0.011);
                        break;
                    case "OnlyPitch":
                        this.rots[1] += RandomUtil.nextFloat(0, 0.0011);
                        break;
                    case "OnlyYaw":
                        this.rots[0] += RandomUtil.nextFloat(0, 0.0011);
                        break;
                    case "SmallRand":
                        this.rots[1] += RandomUtil.nextFloat(0, 0.0011);
                        this.rots[0] += RandomUtil.nextFloat(0, 0.0011);
                        break;
                }
            } else if (!doneRotating) {
                this.rots[0] = mc.player.rotationYaw - 180f + RandomUtil.nextFloat(-0.111, 0.111);
                this.rots[1] = 80.4F;
            }
        }else{
            if((type.is("Hypixel"))){
                if(!underAir){
                    doneRotating = true;
                    this.rots[0] = calcRotation[0];
                    this.rots[0] = MathHelper.wrapAngleTo180_float(this.rots[0]);
                    this.rots[1] = 80.4f;
                    if(Math.abs(getAngleDifference(calcRotation[0], mc.player.lastReportedYaw)) <= 15 && Math.abs(getAngleDifference(calcRotation[1], mc.player.lastReportedPitch)) <= 10){
                        this.rots[0] = mc.player.lastReportedYaw;
                        this.rots[1] = mc.player.lastReportedPitch;
                    }
                    Rotation limitM = limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch), new Rotation(rots[0], rots[1]), (float) (50 + Math.random() * 10), (float) (20 + Math.random()));
                    rots = new float[]{limitM.getYaw(), limitM.getPitch()};
                    rots = mouseSens(rots[0], rots[1], mc.player.lastReportedYaw, mc.player.lastReportedPitch);
                    getYawBasedPitch(this.rots[0], false);
                }else if (!doneRotating) {
                    this.rots[0] = mc.player.rotationYaw - 180f + RandomUtil.nextFloat(-0.111, 0.111);
                    this.rots[1] = 80.4F;
                }
            }else if(type.is("Intave")){
                doneRotating = true;
                this.rots[0] = MathHelper.wrapAngleTo180_float(mc.player.rotationYaw - 181f);
                getYawBasedPitch(this.rots[0], false);
                if(mc.player.fallDistance != 0){
                    this.rots = calcRotation;
                }
            }else if(type.is("Legit")){
                this.rots[0] = mc.player.rotationYaw - 180f + RandomUtil.nextFloat(-0.111, 0.111);
                this.rots[1] = 80.4F;
            }else if(type.is("NCP")){
                if(underAir) {
                    this.rots = calcRotation;
                }
            }
        }
        float[] oldRots = new float[]{rots[0] + 0.00001f, rots[1] + 0.00001f};
        // 处理
        if(!(type.is("Hypixel") || type.is("NCP"))) {
            Rotation limit = limitAngleChange(new Rotation(mc.player.lastReportedYaw, mc.player.lastReportedPitch), new Rotation(rots[0], rots[1]),
                    this.yawSpeed.getValue().floatValue() + RandomUtil.nextFloat(0, 16), this.pitchSpeed.getValue().floatValue() + RandomUtil.nextFloat(0, 16));
            rots = new float[]{limit.getYaw(), limit.getPitch()};
        }
        if(Math.abs(getAngleDifference(oldRots[0], rots[0])) > 10 ||
                Math.abs(getAngleDifference(oldRots[1], rots[1])) > 7){
            rotated = true;
        }

        rots = mouseSens(this.rots[0], this.rots[1], mc.player.lastReportedYaw, mc.player.lastReportedPitch);

        if(slot != -1 && act_slot != slot && itemSpoofMode.is("LiteSpoof")) {
            act_slot = slot;
            mc.getConnection().sendPacketNOEvent(new CHeldItemChangePacket(slot));
        }
        // todo HERE click mouse
//        if(place.is("Prev")) {
//            click();
//        }
        // do movement fix
            movementFixYaw = rots[0];
            movementFixPitch = rots[1];
            fixing = movementFix.isEnable() && !(type.is("Hypixel") || type.is("NCP"));
//            RotationUtils.slient = placeTime.is("Telly");
            backward = (!placeTime.is("Telly") || isTellying()) && fixing;
        if(slot == -1) return;
        if(blockPos != null) {
//            if(place.is("Legit")){
                click();
//            }
        }
        super.onClickEvent(event);
    }
    public boolean shouldBuild(){
        BlockPos downPos = new BlockPos(mc.player.getPositionVector().add(0, -0.5, 0));
        boolean rotationChange = false, underAir = mc.world.getBlockState(downPos).getBlock() instanceof AirBlock;
        switch (customRotationTime.getValue()){
            case "Air":
                rotationChange = underAir;
                break;
            case "AirTower":
                rotationChange = underAir;
                if(towering){
                    rotationChange = true;
                }
                break;
            case "MM":
                rotationChange = UnusedRtation.shouldBuild(blockPos.getPosition());
                break;
            case "MMTower":
                rotationChange = UnusedRtation.shouldBuild(blockPos.getPosition());
                if(towering){
                    rotationChange = true;
                }
                break;
            case "Always":
                rotationChange = true;
                break;
        }
        return rotationChange;
    }
    public void idk1(double d) {
        float f = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(mc.player.getMotion().z, mc.player.getMotion().x)) - 90.0f);
        MovementUtils.strafing(d, f);
    }
    public boolean isTellying(){
        return airTicks >= 2;
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        super.onUpdateEvent(event);
    }

    public void getYawBasedPitch(float yaw, boolean t) {
        float[] curentRot = null;
        double lastDist = 0;
        double diff = 0.17f;
        for(float y = 40;y <= 90;y += diff){
            RayTraceResult rayTraceResult = mc.player.customPick(4.5f, 1.0f, yaw, y);
            if(rayTraceResult.getType() == RayTraceResult.Type.BLOCK){
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                 if((!blockRayTraceResult.getFace().equals(blockPos.getFacing()) || !blockRayTraceResult.getPos().equals(blockPos.getPosition()))) continue;
                double d = mc.player.getEyePosition(1.0f).squareDistanceTo(blockRayTraceResult.getHitVec());
                if(curentRot == null || d <= lastDist) {
                    curentRot = scaffoldRots(blockRayTraceResult.getHitVec().x, blockRayTraceResult.getHitVec().y, blockRayTraceResult.getHitVec().z, mc.player.lastReportedYaw, mc.player.lastReportedPitch, 999, 999, false);
                    lastDist = d;
                }
            }
            if(y > 70){
                diff = 0.05;
            }
        }
        if(curentRot != null)
            rots[1] = curentRot[1];
        else if(t){
            rots[1] = 80.41f;
        }
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof CHeldItemChangePacket){
            event.cancelable = true;
        }
        if (event.getPacket() instanceof CPlayerPacket) {
            CPlayerPacket packet = (CPlayerPacket) event.getPacket();
            if (this.idk) {
                packet.onGround = (true);
                this.idk = false;
            }
        }
        super.onPacketEvent(event);
    }

    @Override
    public void onDisable() {
//        if(type.is("Intave"))
//            mc.gameSettings.keyBindLeft.pressed = false;
        this.asff.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
        if(adreset) {
            adreset = false;
            mc.gameSettings.keyBindRight.pressed = false;
            mc.gameSettings.keyBindLeft.pressed = false;
        }
        mc.gameSettings.keyBindJump.pressed = false;
        mc.gameSettings.keyBindSneak.pressed = false;
        switch (itemSpoofMode.getValue()){
            case "Switch":
                mc.player.inventory.currentItem = rslot;
                break;
            case "LiteSpoof":
                if(act_slot != rslot && rslot != -1)
                    NEXT_SLOT = rslot;
                break;
        }
        super.onDisable();
    }
}
