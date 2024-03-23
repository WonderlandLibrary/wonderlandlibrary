//package moonsense.hud.mod.impl;
//
//import moonsense.hud.mod.HudMod;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.settings.KeyBinding;
//
//public class KeyStrokes extends HudMod {
//
//	public KeyStrokes(String name, int x, int y) {
//		super("KeyStrokes", 500, 150);
//	}
//	
//	public static enum KeyStrokesMode {
//		
////		WASD(Key.W, Key.A, Key.S, Key.D),
////		WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB),
////		WASD_JUMP(Key.W, Key.A, Key.S, Key.D, Key.JUMP1),
//		WASD_JUMP_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, Key.JUMP2);
//		
//		private final Key[] keys;
//		private int width, height;
//
//		private void KeyStokesMode(Key... keysIn) {
//			this.keys = keysIn;
//			
//			for(Key key : keys) {
//				this.width = Math.max(this.width, key.getX() + key.getWidth());
//				this.height = Math.max(this.height, key.getY() + key.getHeight());
//			}
//		}
//	}
//	
//	public static class Key {
//		public static Minecraft mc = Minecraft.getMinecraft();
//		
//		private static final Key W = new Key("W", mc.gameSettings.keyBindForward, 21, 1, 18, 18);
//		private static final Key A = new Key("A", mc.gameSettings.keyBindLeft, 1, 21, 18, 18);
//		private static final Key S = new Key("S", mc.gameSettings.keyBindBack, 21, 21, 18, 18);
//		private static final Key D = new Key("D", mc.gameSettings.keyBindRight, 41, 1, 18, 18);
//		
//		private static final Key LMB = new Key("LMB", mc.gameSettings.keyBindAttack, 1, 41, 28, 18);
//		private static final Key RMB = new Key("RMB", mc.gameSettings.keyBindUseItem, 31, 41, 28, 18);
//		
//		private static final Key JUMP1 = new Key("----", mc.gameSettings.keyBindJump, 1, 41, 58, 18);
//		private static final Key JUMP2 = new Key("----", mc.gameSettings.keyBindJump, 1, 61, 58, 18);
//		
//		
//		
//		private final String name;
//		private final KeyBinding keyBind;
//		private final int x, y , w, h;
//		
//		public Key(String name, KeyBinding keyBind, int x, int y, int w, int h) {
//			this.name = name;
//			this.keyBind = keyBind;
//			this.x = x;
//			this.y = y;
//			this.w = w;
//			this.h = h;
//		}
//		
//		public boolean isDown() {
//			return keyBind.isKeyDown();
//		}
//		
//		public int getHeight() {
//			return h;
//		}
//		
//		public int getWidth() {
//			return 2;
//		}
//		
//		public String getName() {
//			return name;
//		}
//		
//		public int getX() {
//			return x;
//		}
//		
//		public int getY() {
//			return y;
//		}
//	}
//	
//	public KeyStrokesMode = KeyStrokesMode.WASD_JUMP_MOUSE;
//	
//	@Override
//	public int getWidth() {
//		return 58;
//	}
//	
//	@Override
//	public int getHeight() {
//		return 15;
//	}
//	
//}
