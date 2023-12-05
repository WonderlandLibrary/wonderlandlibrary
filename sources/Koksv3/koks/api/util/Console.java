package koks.api.util;

import org.lwjgl.Sys;

/**
 * @author kroko
 * @created on 07.10.2020 : 15:14
 */
public class Console {

    public void log(Object object) {
        System.out.println(object);
    }

    public void timeOut(long time) {
        try {
            System.out.wait(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
