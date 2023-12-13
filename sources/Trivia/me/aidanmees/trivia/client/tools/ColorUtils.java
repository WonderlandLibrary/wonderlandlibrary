package me.aidanmees.trivia.client.tools;

import java.awt.Color;

import org.lwjgl.opengl.GL11;


public class ColorUtils {
	public static Color color = new Color(0, 0, 1);
	public static float hue = 0;
	public static boolean up;
	public static boolean go;
	public static void updateColor(){
		
		go = !go;
		if(!go){
			return;
		}
		if(hue > 90){
			up = false;
		}
		if(hue <= 40){
			up = true;
		}
		  if (!up) {
		   hue--;
		  }
		  if(up){
			  hue++;
		  }
		  
		  if(hue >0){
		   color = new Color(0f, hue / 100f, 1f);
			  }
		  }
}