package me.rhys.client.module.movement;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MoveUtils;

public class CustomSpeed extends Module {
  @Name("Set Speed On Toggle")
  public boolean setSpeedOnToggle;
  
  @Name("Set MotionY On Ground")
  public boolean motionOnGround;
  
  @Name("Custom MotionY")
  public boolean customMotionY;
  
  @Name("Custom MotionY Jump")
  @Clamp(min = 0.0D, max = 1.0D)
  public double customMotionYOnJump;
  
  @Name("Custom Speed")
  @Clamp(min = 0.0D, max = 9.0D)
  public double customSpeed;
  
  @Name("Speed Up Type")
  public SpeedUpMode speedUpMode;
  
  @Name("Speed State")
  public SpeedState speedState;
  
  @Name("Fast Fall")
  public boolean fastFall;
  
  @Name("Fast Fall Below Motion")
  public boolean fastFallMotionBelow;
  
  @Name("Use Custom Fall Motion")
  public boolean useCustomFallMotion;
  
  @Name("Custom Fall Motion")
  @Clamp(min = 0.0D, max = 9.0D)
  public double customFallMotion;
  
  @Name("Set Speed On Move")
  public boolean setSpeedOnMove;
  
  @Name("Set Motion On Move")
  public boolean setMotionOnMove;
  
  @Name("Spoof On Ground")
  public boolean spoofOnGround;
  
  @Name("Spoof Off Ground")
  public boolean spoofOffGround;
  
  @Name("Be 1/64 On Ground")
  public boolean magicGround;
  
  @Name("Be 1/64 In Air")
  public boolean magicValueAir;
  
  @Name("Magic Value State")
  public MagicValueState magicValueState;
  
  private double lastDistance;
  
  private double movementSpeed;
  
  private int stage;
  
  private int voidIndex;
  
  public CustomSpeed(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.setSpeedOnToggle = true;
    this.motionOnGround = true;
    this.customMotionY = false;
    this.customMotionYOnJump = 0.42D;
    this.customSpeed = 1.5D;
    this.speedUpMode = SpeedUpMode.SET_SPEED;
    this.speedState = SpeedState.ALWAYS;
    this.fastFall = false;
    this.fastFallMotionBelow = true;
    this.useCustomFallMotion = false;
    this.customFallMotion = 0.42D;
    this.setSpeedOnMove = true;
    this.setMotionOnMove = true;
    this.spoofOnGround = false;
    this.spoofOffGround = false;
    this.magicGround = false;
    this.magicValueAir = false;
    this.magicValueState = MagicValueState.BLOCK;
    this.voidIndex = 0;
  }
  
  public void onEnable() {
    this.lastDistance = 0.0D;
    this.movementSpeed = 0.0D;
    this.stage = 0;
    this.voidIndex = 0;
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    double x = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
    double z = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
    this.lastDistance = Math.sqrt(x * x + z * z);
    if (event.getType() == Event.Type.PRE) {
      if (this.spoofOnGround && !this.mc.thePlayer.onGround)
        event.setOnGround(true); 
      if (this.spoofOffGround && this.mc.thePlayer.onGround)
        event.setOnGround(false); 
      if ((this.magicGround && (this.mc.thePlayer.onGround || this.mc.thePlayer.posY % 0.015625D == 0.0D)) || (this.magicValueAir && !this.mc.thePlayer.onGround && this.mc.thePlayer.posY % 0.015625D != 0.0D)) {
        double offset = 0.5D;
        if (this.magicValueState == MagicValueState.NORMAL_JUMP)
          offset = 0.41999998688697815D; 
        event.getPosition().setY(event.getPosition().getY() + offset);
      } 
    } 
  }
  
  @EventTarget
  public void onMove(PlayerMoveEvent event) {
    if (this.motionOnGround && this.mc.thePlayer.onGround) {
      handleJump(event);
    } else if (!this.motionOnGround) {
      handleJump(event);
    } 
    if (this.fastFall && !this.mc.thePlayer.onGround)
      if (this.fastFallMotionBelow) {
        if (this.mc.thePlayer.motionY < 0.0D)
          handleFall(event); 
      } else {
        handleFall(event);
      }  
    switch (this.speedState) {
      case SET_SPEED:
        if (this.mc.thePlayer.onGround)
          doSpeed(this.customSpeed, event); 
        break;
      case FRICTION:
        doSpeed(this.customSpeed, event);
      case ACCELERATION:
        if (this.mc.thePlayer.motionY < 0.0D && !this.mc.thePlayer.onGround)
          doSpeed(this.customSpeed, event); 
        break;
      case TELEPORT:
        if (this.mc.thePlayer.onGround || this.mc.thePlayer.motionY != 0.0D)
          doSpeed(this.customSpeed, event); 
        break;
    } 
  }
  
  private void handleJump(PlayerMoveEvent event) {
    if (this.setMotionOnMove && !isMoving())
      return; 
    event.motionY = this.mc.thePlayer.motionY = this.customMotionYOnJump;
    event.motionY = this.mc.thePlayer.motionY = 0.41999998688697815D;
  }
  
  private void handleFall(PlayerMoveEvent event) {
    if (this.setMotionOnMove && !isMoving())
      return; 
    event.motionY = this.mc.thePlayer.motionY -= this.customFallMotion;
    event.motionY = this.mc.thePlayer.motionY - 0.41999998688697815D;
  }
  
  private void doSpeed(double speed, PlayerMoveEvent playerMoveEvent) {
    if (this.setSpeedOnMove && !isMoving())
      return; 
    switch (this.speedUpMode) {
      case SET_SPEED:
        playerMoveEvent.setMovementSpeed(speed);
        break;
      case FRICTION:
        MoveUtils.setFriction(speed);
        break;
      case ACCELERATION:
        if (this.mc.thePlayer.onGround || this.stage == 0) {
          this.stage = 0;
          this.movementSpeed = this.mc.thePlayer.getMovementSpeed() * this.customSpeed;
        } else if (this.stage == 1) {
          this.movementSpeed = this.lastDistance - 0.66D * (this.lastDistance - this.mc.thePlayer.getMovementSpeed());
        } else {
          this.movementSpeed = this.lastDistance - this.mc.thePlayer.getMovementSpeed() / 33.1D;
        } 
        this.stage++;
        playerMoveEvent.setMovementSpeed(this.movementSpeed);
        break;
      case TELEPORT:
        MoveUtils.teleportForward(speed, MoveUtils.TeleportMode.SET_POSITION_UPDATE);
        break;
    } 
  }
  
  private boolean isMoving() {
    return (this.mc.thePlayer.isPlayerMoving() || this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F);
  }
  
  public enum VoidTPMode {
    PRE, POST, BOTH;
  }
  
  public enum VoidType {
    DOWN, UP, SWITCH, RANDOM;
  }
  
  public enum MagicValueState {
    BLOCK, NORMAL_JUMP;
  }
  
  public enum SpeedState {
    ON_GROUND, ALWAYS, FALLING, ON_GROUND_AND_FALLING;
  }
  
  public enum SpeedUpMode {
    SET_SPEED, FRICTION, ACCELERATION, TELEPORT;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\CustomSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */