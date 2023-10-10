package org.newdawn.slick.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public abstract class BasicGameState implements GameState {
   public void inputStarted() {
   }

   public boolean isAcceptingInput() {
      return true;
   }

   public void setInput(Input var1) {
   }

   public void inputEnded() {
   }

   public abstract int getID();

   public void controllerButtonPressed(int var1, int var2) {
   }

   public void controllerButtonReleased(int var1, int var2) {
   }

   public void controllerDownPressed(int var1) {
   }

   public void controllerDownReleased(int var1) {
   }

   public void controllerLeftPressed(int var1) {
   }

   public void controllerLeftReleased(int var1) {
   }

   public void controllerRightPressed(int var1) {
   }

   public void controllerRightReleased(int var1) {
   }

   public void controllerUpPressed(int var1) {
   }

   public void controllerUpReleased(int var1) {
   }

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

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public void enter(GameContainer var1, StateBasedGame var2) throws SlickException {
   }

   public void leave(GameContainer var1, StateBasedGame var2) throws SlickException {
   }

   public void mouseWheelMoved(int var1) {
   }
}
