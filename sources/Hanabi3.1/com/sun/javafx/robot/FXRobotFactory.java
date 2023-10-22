package com.sun.javafx.robot;

import com.sun.javafx.robot.impl.BaseFXRobot;
import javafx.scene.Scene;

public class FXRobotFactory {
   public static FXRobot createRobot(Scene var0) {
      return new BaseFXRobot(var0);
   }
}
