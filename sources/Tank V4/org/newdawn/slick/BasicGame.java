package org.newdawn.slick;

public abstract class BasicGame implements Game, InputListener {
   private static final int MAX_CONTROLLERS = 20;
   private static final int MAX_CONTROLLER_BUTTONS = 100;
   private String title;
   protected boolean[] controllerLeft = new boolean[20];
   protected boolean[] controllerRight = new boolean[20];
   protected boolean[] controllerUp = new boolean[20];
   protected boolean[] controllerDown = new boolean[20];
   protected boolean[][] controllerButton = new boolean[20][100];

   public BasicGame(String var1) {
      this.title = var1;
   }

   public void setInput(Input var1) {
   }

   public boolean closeRequested() {
      return true;
   }

   public String getTitle() {
      return this.title;
   }

   public abstract void init(GameContainer var1) throws SlickException;

   public void keyPressed(int var1, char var2) {
   }

   public void keyReleased(int var1, char var2) {
   }

   public void mouseMoved(int var1, int var2, int var3, int var4) {
   }

   public void mouseDragged(int var1, int var2, int var3, int var4) {
   }

   public void mouseClicked(int var1, int var2, int var3, int var4) {
   }

   public void mousePressed(int var1, int var2, int var3) {
   }

   public void controllerButtonPressed(int var1, int var2) {
      this.controllerButton[var1][var2] = true;
   }

   public void controllerButtonReleased(int var1, int var2) {
      this.controllerButton[var1][var2] = false;
   }

   public void controllerDownPressed(int var1) {
      this.controllerDown[var1] = true;
   }

   public void controllerDownReleased(int var1) {
      this.controllerDown[var1] = false;
   }

   public void controllerLeftPressed(int var1) {
      this.controllerLeft[var1] = true;
   }

   public void controllerLeftReleased(int var1) {
      this.controllerLeft[var1] = false;
   }

   public void controllerRightPressed(int var1) {
      this.controllerRight[var1] = true;
   }

   public void controllerRightReleased(int var1) {
      this.controllerRight[var1] = false;
   }

   public void controllerUpPressed(int var1) {
      this.controllerUp[var1] = true;
   }

   public void controllerUpReleased(int var1) {
      this.controllerUp[var1] = false;
   }

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public abstract void update(GameContainer var1, int var2) throws SlickException;

   public void mouseWheelMoved(int var1) {
   }

   public boolean isAcceptingInput() {
      return true;
   }

   public void inputEnded() {
   }

   public void inputStarted() {
   }
}
