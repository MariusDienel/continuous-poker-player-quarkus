package de.doubleslash.poker.player.logic;

import de.doubleslash.poker.player.data.Card;
import de.doubleslash.poker.player.data.Rank;
import de.doubleslash.poker.player.data.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Strategy {

   public int decide(final Table table) {
      System.out.println(table);
      List<Card> cards = table.getOwnPlayer().getCards();
      List<Card> communityCards = table.getCommunityCards();

      List<Card> combined = new ArrayList<>(cards);
      combined.addAll(communityCards);

      List<Rank> ranks = combined.stream().map(Card::getRank).collect(Collectors.toList());

      int bet = 0;
      for (Rank rank : Rank.values()) {
         int sum = ranks.stream().filter(r -> r.equals(rank)).mapToInt(r -> r.getValue()).sum();
         bet += sum * rank.getValue();
      }

      return bet;
   }

}
