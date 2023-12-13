/*     */ package org.neverhook.client.settings.impl;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ 
/*     */ public class NumberSetting
/*     */   extends Setting
/*     */ {
/*     */   private final NumberType type;
/*     */   private float current;
/*     */   private float minimum;
/*     */   
/*     */   public NumberSetting(String name, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible) {
/*  14 */     this.name = name;
/*  15 */     this.minimum = minimum;
/*  16 */     this.current = current;
/*  17 */     this.maximum = maximum;
/*  18 */     this.increment = increment;
/*  19 */     this.type = NumberType.DEFAULT;
/*  20 */     setVisible(visible);
/*     */   }
/*     */   private float maximum; private float increment; private String desc;
/*     */   public NumberSetting(String name, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible, NumberType type) {
/*  24 */     this.name = name;
/*  25 */     this.minimum = minimum;
/*  26 */     this.current = current;
/*  27 */     this.maximum = maximum;
/*  28 */     this.increment = increment;
/*  29 */     this.type = type;
/*  30 */     setVisible(visible);
/*     */   }
/*     */   
/*     */   public NumberSetting(String name, String desc, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible) {
/*  34 */     this.name = name;
/*  35 */     this.desc = desc;
/*  36 */     this.minimum = minimum;
/*  37 */     this.current = current;
/*  38 */     this.maximum = maximum;
/*  39 */     this.increment = increment;
/*  40 */     this.type = NumberType.DEFAULT;
/*  41 */     setVisible(visible);
/*     */   }
/*     */   
/*     */   public NumberSetting(String name, String desc, float current, float minimum, float maximum, float increment, Supplier<Boolean> visible, NumberType type) {
/*  45 */     this.name = name;
/*  46 */     this.desc = desc;
/*  47 */     this.minimum = minimum;
/*  48 */     this.current = current;
/*  49 */     this.maximum = maximum;
/*  50 */     this.increment = increment;
/*  51 */     this.type = type;
/*  52 */     setVisible(visible);
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  56 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/*  60 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public float getMinValue() {
/*  64 */     return this.minimum;
/*     */   }
/*     */   
/*     */   public void setMinValue(float minimum) {
/*  68 */     this.minimum = minimum;
/*     */   }
/*     */   
/*     */   public float getMaxValue() {
/*  72 */     return this.maximum;
/*     */   }
/*     */   
/*     */   public void setMaxValue(float maximum) {
/*  76 */     this.maximum = maximum;
/*     */   }
/*     */   
/*     */   public float getNumberValue() {
/*  80 */     return this.current;
/*     */   }
/*     */   
/*     */   public void setValueNumber(float current) {
/*  84 */     this.current = current;
/*     */   }
/*     */   
/*     */   public float getIncrement() {
/*  88 */     return this.increment;
/*     */   }
/*     */   
/*     */   public void setIncrement(float increment) {
/*  92 */     this.increment = increment;
/*     */   }
/*     */   
/*     */   public NumberType getType() {
/*  96 */     return this.type;
/*     */   }
/*     */   
/*     */   public enum NumberType
/*     */   {
/* 101 */     MS("Ms"), APS("Aps"), SIZE("Size"), PERCENTAGE("Percentage"), DISTANCE("Distance"), DEFAULT("");
/*     */     
/*     */     String name;
/*     */     
/*     */     NumberType(String name) {
/* 106 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 110 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\settings\impl\NumberSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */