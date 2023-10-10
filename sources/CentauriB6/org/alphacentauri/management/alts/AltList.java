package org.alphacentauri.management.alts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.alphacentauri.AC;
import org.alphacentauri.management.alts.Account;
import org.alphacentauri.management.io.ConfigFile;
import org.alphacentauri.management.util.StringUtils;

public class AltList {
   private ConfigFile alts = new ConfigFile("alts");
   public ArrayList accounts = new ArrayList();

   public AltList() {
      this.load();
   }

   public boolean contains(String email) {
      for(Account account : this.accounts) {
         if(account.username.equalsIgnoreCase(email)) {
            return true;
         }
      }

      return false;
   }

   public void load() {
      for(Entry<String, String> entry : this.alts.all()) {
         if(((String)entry.getKey()).contains(":")) {
            Account toAdd = new Account(((String)entry.getKey()).split(":")[0], (String)entry.getValue());
            toAdd.ign = ((String)entry.getKey()).split(":")[1];
            this.accounts.add(toAdd);
         } else {
            this.accounts.add(new Account((String)entry.getKey(), (String)entry.getValue()));
         }
      }

   }

   public void save() {
      this.alts.clear();

      for(Account acc : this.accounts) {
         this.alts.set(acc.username + ":" + acc.ign, acc.password.isEmpty()?"$CRACKED$":acc.password);
      }

      this.alts.save();
   }

   public List getAllPremiumAccounts() {
      return (List)this.accounts.stream().filter((account) -> {
         return !account.isCracked();
      }).collect(Collectors.toCollection(ArrayList::<init>));
   }

   public Account getRandomPremium() {
      List<Account> accounts = this.getAllPremiumAccounts();
      return accounts.size() == 0?null:(Account)accounts.get(AC.getRandom().nextInt(accounts.size()));
   }

   public Account getRandomCracked() {
      return new Account(StringUtils.getRandomString(16), "$CRACKED$");
   }
}
