/*     */ package org.yaml.snakeyaml.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayUtils
/*     */ {
/*     */   public static <E> List<E> toUnmodifiableList(E[] elements) {
/*  35 */     return (elements.length == 0) ? Collections.<E>emptyList() : new UnmodifiableArrayList<>(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> List<E> toUnmodifiableCompositeList(E[] array1, E[] array2) {
/*     */     List<E> result;
/*  49 */     if (array1.length == 0) {
/*  50 */       result = toUnmodifiableList(array2);
/*  51 */     } else if (array2.length == 0) {
/*  52 */       result = toUnmodifiableList(array1);
/*     */     } else {
/*  54 */       result = new CompositeUnmodifiableArrayList<>(array1, array2);
/*     */     } 
/*  56 */     return result;
/*     */   }
/*     */   
/*     */   private static class UnmodifiableArrayList<E>
/*     */     extends AbstractList<E> {
/*     */     private final E[] array;
/*     */     
/*     */     UnmodifiableArrayList(E[] array) {
/*  64 */       this.array = array;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/*  69 */       if (index >= this.array.length) {
/*  70 */         throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
/*     */       }
/*  72 */       return this.array[index];
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/*  77 */       return this.array.length;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositeUnmodifiableArrayList<E>
/*     */     extends AbstractList<E> {
/*     */     private final E[] array1;
/*     */     private final E[] array2;
/*     */     
/*     */     CompositeUnmodifiableArrayList(E[] array1, E[] array2) {
/*  87 */       this.array1 = array1;
/*  88 */       this.array2 = array2;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/*     */       E element;
/*  94 */       if (index < this.array1.length) {
/*  95 */         element = this.array1[index];
/*  96 */       } else if (index - this.array1.length < this.array2.length) {
/*  97 */         element = this.array2[index - this.array1.length];
/*     */       } else {
/*  99 */         throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
/*     */       } 
/* 101 */       return element;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 106 */       return this.array1.length + this.array2.length;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyam\\util\ArrayUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */