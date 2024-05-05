package me.aidanmees.trivia.client.tools;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import me.aidanmees.trivia.client.events.Event;
import me.aidanmees.trivia.client.events.MouseEvent;

public class MouseUtil
{
    private static Field buttonStateField;
    private static Field buttonField;
    private static Field buttonsField;
    
    public MouseUtil() {
        super();
    }
    
    public static void sendClick(final int button, final boolean state) {
        MouseEvent mouseEvent = new MouseEvent();
        MouseUtil.buttonField.setAccessible(true);
        try {
            MouseUtil.buttonField.set(mouseEvent, button);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        MouseUtil.buttonField.setAccessible(false);
        MouseUtil.buttonStateField.setAccessible(true);
        try {
            MouseUtil.buttonStateField.set(mouseEvent, state);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        MouseUtil.buttonStateField.setAccessible(false);
       
        try {
            MouseUtil.buttonsField.setAccessible(true);
            final ByteBuffer buffer = (ByteBuffer)MouseUtil.buttonsField.get(null);
            MouseUtil.buttonsField.setAccessible(false);
            buffer.put(button, (byte)(state ? 1 : 0));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
}