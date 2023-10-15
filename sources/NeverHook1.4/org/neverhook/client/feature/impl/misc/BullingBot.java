/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.apache.commons.lang3.RandomStringUtils;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventAttackClient;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class BullingBot
/*    */   extends Feature
/*    */ {
/*    */   private final NumberSetting delay;
/* 18 */   public String lastMessage = "";
/* 19 */   TimerHelper timer = new TimerHelper();
/*    */   
/*    */   public BullingBot() {
/* 22 */     super("BullingBot", "При ударе игрока отправляет буллинг сообщение в его адресс", Type.Misc);
/* 23 */     this.delay = new NumberSetting("Bulling Delay", 8.0F, 1.0F, 20.0F, 1.0F, () -> Boolean.valueOf(true));
/* 24 */     addSettings(new Setting[] { (Setting)this.delay });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPacket(EventAttackClient event) {
/* 29 */     if (event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
/* 30 */       Entity entity = event.getEntity();
/* 31 */       String[] messages = { "Я TBOЮ MATЬ БЛЯTЬ ПOДВEСИЛ НА КОЛ ОНА EБAHAЯ БЛЯДИHA", "МАМАШУ ТВОЮ АРМАТУРОЙ С ШИПАМИ ПО ХРЕБТУ ПИЗДИЛ", "Я ТВОЕЙ МАТЕРИ ПИЗДАК РАЗОРВАЛ СЫН БЛЯДИНЫ ТЫ ЕБАННОЙ", "ВГЕТАЙ НЕВЕРХУК СЫН ЕБАННОЙ ШЛЮХИ", "ТЫ ПСИНА БЕЗ БРЕЙНА ДАВАЙ ТЕРПИ ТЕРПИ", "я твою мать об стол xуяpил сын тупорылой овчарки мать продал чит на кубики купил?", "СКУЛИ СВИHЬЯ ЕБAHAЯ , Я ТВОЮ MATЬ ПОДBECИЛ НА ЦЕПЬ И С ОКНА СБРОСИЛ ОНА ФЕМИНИСТКА ЕБАHAЯ ОНА СВОИМ ВЕСОМ 180КГ ПРОБУРИЛАСЬ ДО ЯДРА ЗЕМЛИ И СГОРЕЛА HAXУЙ АХАХАХАХА ЕБATЬ ОНА ГОРИТ ПРИКОЛЬНО", "ты мейн сначало свой пукни потом чет овысирай, с основы пиши нищ", "БАБКА СДОХЛА ОТ СТАРОСТИ Т.К. КОГДА ТВОЮ МATЬ РОДИЛИ ЕЙ БЫЛО 99 ЛЕТ И ОТ НЕРВОВ РАДОСТИ ОНА СДОХЛА ОЙ БЛ9TЬ ОТ РАДОСТИ ДЕД ТОЖЕ ОТ РАДОСТИ СДОХ HAXУЙ ДOЛБAЁБ EБAHЫЙ ЧТОБЫ ВЫЖИТЬ НА ПОМОЙКА МATЬ ТВOЯ ПOКА НЕ СДОХЛА EБAЛAСЬ С МУЖИКАМИ ЗА 2 КОПЕЙКИ", "ТЫ ПОНИМАЕШЬ ЧТО Я ТВОЮ МАТЬ ОТПРАВИЛ СО СВОЕГО XУЯ В НЕБО, ЧТОБ ОНА СВОИМ ПИЗДAKOМ ПРИНИМАЛА МИТЕОРИТНУЮ АТАКУ?)", "ТЫ ПОНИМАЕШЬ ЧТО ТBОЯ МATЬ СИДИТ У МЕНЯ НА ЦЕПИ И КАК БУЛЬДОГ EБАHЫЙ НА МОЙ XУЙ СЛЮНИ БЛ9ДЬ ПУСКАЕТ?))", "В ДЕТДОМЕ ТЕБЯ ПИЗДUЛИ ВСЕ КТО МОГ В ИТОГЕ ТЫ СДОХ НА УЛИЦЕ В 13 ЛЕТ ОТ НЕДОСТАТКА ЕДЫ ВОДУ ТЫ ЖЕ БРАЛ ЭТИМ ФИЛЬТРОМ И МОЧОЙ ДOЛБAЁБ ЕБAHЫЙ СУКA БЕЗ МATEPHAЯ ХУETА.", "Чё как нищий, купи неверхук не позорься", "Your mom owned by NeverHook", "АЛО БОМЖАТИНА БЕЗ МАТЕРИ Я ТВОЮ МАТЬ ОБ СТОЛ УБИЛ ЧЕРЕП ЕЙ РАЗБИЛ НОГОЙ БАТЮ ТВОЕГО С ОКНА ВЫКИНУЛ СУКА ЧМО ЕБАННОЕ ОТВЕТЬ ЧМО ЕБЛАН ТВАРЬ ШАЛАВА" };
/* 32 */       String text = messages[(new Random()).nextInt(messages.length)];
/* 33 */       if ((this.timer.hasReached(this.delay.getNumberValue() * 1000.0F) && !this.lastMessage.equals(text)) || this.lastMessage == null) {
/* 34 */         mc.player.sendChatMessage("![" + RandomStringUtils.randomAlphanumeric(5) + "] " + entity.getName() + " " + text + " [" + RandomStringUtils.randomAlphanumeric(5) + "]");
/* 35 */         this.lastMessage = text;
/* 36 */         this.timer.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\BullingBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */