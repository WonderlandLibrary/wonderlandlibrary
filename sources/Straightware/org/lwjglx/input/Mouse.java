package org.lwjglx.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;
import org.lwjglx.LWJGLException;
import org.lwjglx.Sys;
import org.lwjglx.opengl.Display;

public class Mouse {

	private static Cursor currentCursor = null;

	private static boolean grabbed = false;

	private static int lastX = 0;
	private static int lastY = 0;

	private static int latestX = 0;
	private static int latestY = 0;
	public static final int	EVENT_SIZE									= 1 + 1 + 4 + 4 + 4 + 8;

	private static int x = 0;
	private static int y = 0;

	private static double lastDw = 0;
	public static double dw = 0;

	private static EventQueue queue = new EventQueue(9999999);

	private static int[] buttonEvents = new int[queue.getMaxEvents()];
	private static boolean[] buttonEventStates = new boolean[queue.getMaxEvents()];
	private static int[] xEvents = new int[queue.getMaxEvents()];
	private static int[] yEvents = new int[queue.getMaxEvents()];
	private static int[] lastxEvents = new int[queue.getMaxEvents()];
	private static int[] lastyEvents = new int[queue.getMaxEvents()];
	private static long[] nanoTimeEvents = new long[queue.getMaxEvents()];
	private static double[] scrollEvents = new double[queue.getMaxEvents()];

	private static boolean clipPostionToDisplay = true;

	public static void addMoveEvent(double mouseX, double mouseY) {
		latestX = (int)mouseX;
		latestY = Display.getHeight() - (int)mouseY;

		lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
		lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];

		xEvents[queue.getNextPos()] = latestX;
		yEvents[queue.getNextPos()] = latestY;

		buttonEvents[queue.getNextPos()] = -1;
		buttonEventStates[queue.getNextPos()] = false;

		nanoTimeEvents[queue.getNextPos()] = Sys.getNanoTime();

		queue.add();
	}

	public static void addButtonEvent(int button, boolean pressed) {
		lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
		lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];

		xEvents[queue.getNextPos()] = latestX;
		yEvents[queue.getNextPos()] = latestY;

		buttonEvents[queue.getNextPos()] = button;
		buttonEventStates[queue.getNextPos()] = pressed;

		nanoTimeEvents[queue.getNextPos()] = Sys.getNanoTime();

		queue.add();
	}

	public static void addScrollEvent(double scroll) {
		scrollEvents[queue.getNextPos()] = scroll;
	}

	public static void poll() {
		lastX = x;
		lastY = y;

		if (!grabbed && clipPostionToDisplay) {
			if (latestX < 0) latestX = 0;
			if (latestY < 0) latestY = 0;
			if (latestX > Display.getWidth()-1) latestX = Display.getWidth() - 1;
			if (latestY > Display.getHeight()-1) latestY = Display.getHeight() - 1;
		}

		x = latestX;
		y = latestY;

		lastDw = dw;
	}

	public static void create() throws LWJGLException {

	}

	public static boolean isCreated() {
		return Display.isCreated();
	}

	public static void setGrabbed(boolean grab) {
		GLFW.glfwSetInputMode(Display.getWindow(),
				GLFW.GLFW_CURSOR,
				grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL
		);
		if (grab != grabbed) {
			setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			x = lastX = latestX = Display.getWidth() / 2;
			y = lastY = latestY = Display.getHeight() / 2;
		}
		grabbed = grab;
	}

	public static boolean isGrabbed() {
		return grabbed;
	}

	public static boolean isButtonDown(int button) {
		return GLFW.glfwGetMouseButton(Display.getWindow(), button) == GLFW.GLFW_PRESS;
	}

	public static boolean next() {
		return queue.next();
	}

	public static int getEventX() {
		return xEvents[queue.getCurrentPos()];
	}

	public static int getEventY() {
		return yEvents[queue.getCurrentPos()];
	}

	public static int getEventDX() {
		return xEvents[queue.getCurrentPos()] - lastxEvents[queue.getCurrentPos()];
	}

	public static int getEventDY() {
		return yEvents[queue.getCurrentPos()] - lastyEvents[queue.getCurrentPos()];
	}

	public static long getEventNanoseconds() {
		return nanoTimeEvents[queue.getCurrentPos()];
	}

	public static int getEventButton() {
		return buttonEvents[queue.getCurrentPos()];
	}

	public static boolean getEventButtonState() {
		return buttonEventStates[queue.getCurrentPos()];
	}

	public static int getEventDWheel() {
		return (int) scrollEvents[queue.getCurrentPos()]; // TODO
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public static int getDX() {
		return x - lastX;
	}

	public static int getDY() {
		return y - lastY;
	}

	public static int getDWheel() {
		// TODO
		return (int) (dw - lastDw);
	}

	public static int getButtonCount() {
		return 8; // max mouse buttons supported by GLFW
	}

	public static void setClipMouseCoordinatesToWindow(boolean clip) {
		clipPostionToDisplay = clip;
	}

	public static void setCursorPosition(int new_x, int new_y) {
		GLFW.glfwSetCursorPos(Display.getWindow(), new_x, new_y);
	}

	public static Cursor setNativeCursor(Cursor cursor) throws LWJGLException {
		if (cursor == null) {
			GLFW.glfwSetCursor(Display.getWindow(), MemoryUtil.NULL);
			return null;
		}

		GLFW.glfwSetCursor(Display.getWindow(), Display.Window.handle);
		currentCursor = cursor;
		return cursor;
	}

	public static Cursor getCurrentCursor() {
		return currentCursor;
	}

	public static Cursor getNativeCursor() {
		return currentCursor;
	}

	public static void destroy() {

	}

}