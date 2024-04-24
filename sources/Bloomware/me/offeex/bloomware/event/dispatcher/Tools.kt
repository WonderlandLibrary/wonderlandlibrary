package me.offeex.bloomware.event.dispatcher

import java.lang.reflect.Method

/* Unwraps class hierarchy till base class, and then returns list of classes from base to current */
fun unwrapTillBase(c: Class<*>, baseClass: Class<*>): List<Class<*>> {
    if (c == baseClass) return listOf(c)
    if (c == Any::class.java) throw IllegalArgumentException("Base class $baseClass not found in hierarchy")
    return listOf(c) + unwrapTillBase(c.superclass, baseClass)
}

/* Collects all methods with Subscribe annotation from class hierarchy */
fun collectListeners(c: Class<*>): List<Method> {
    if (c == Any::class.java) return emptyList()
    val methods = c.declaredMethods.filter { it.isAnnotationPresent(Subscribe::class.java) }
    return methods + collectListeners(c.superclass)
}
