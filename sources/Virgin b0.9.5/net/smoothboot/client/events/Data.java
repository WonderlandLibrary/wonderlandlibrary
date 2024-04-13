package net.smoothboot.client.events;

import java.lang.reflect.Method;

public record Data(Object source, Method target, byte priority) {
}
