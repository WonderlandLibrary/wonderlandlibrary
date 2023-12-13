/*    */ package org.neverhook.client.friend;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class FriendManager
/*    */ {
/*  8 */   private final List<Friend> friends = new ArrayList<>();
/*    */   
/*    */   public void addFriend(Friend friend) {
/* 11 */     this.friends.add(friend);
/*    */   }
/*    */   
/*    */   public void addFriend(String name) {
/* 15 */     this.friends.add(new Friend(name));
/*    */   }
/*    */   
/*    */   public boolean isFriend(String friend) {
/* 19 */     return this.friends.stream().anyMatch(isFriend -> isFriend.getName().equals(friend));
/*    */   }
/*    */   
/*    */   public void removeFriend(String name) {
/* 23 */     this.friends.removeIf(friend -> friend.getName().equalsIgnoreCase(name));
/*    */   }
/*    */   
/*    */   public List<Friend> getFriends() {
/* 27 */     return this.friends;
/*    */   }
/*    */   
/*    */   public Friend getFriend(String friend) {
/* 31 */     return this.friends.stream().filter(isFriend -> isFriend.getName().equals(friend)).findFirst().get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\friend\FriendManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */