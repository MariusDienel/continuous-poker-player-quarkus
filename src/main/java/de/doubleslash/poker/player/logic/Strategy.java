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
      bet = getBetByPairs(ranks, bet);

      if(street(combined)) {

      }

      return bet;
   }

   private boolean street(List<Card> combined) {
      for (int i = 1; i <= 13; i++) {

      }
      return false;
   }

   private boolean containsCard(List<Card> combined, Rank searchedRank) {
      return combined.stream().map(c -> c.getRank()).anyMatch(e -> e.equals(searchedRank));
   }

   private int getBetByPairs(List<Rank> ranks, int bet) {
      for (Rank rank : Rank.values()) {
         int sum = ranks.stream().filter(r -> r.equals(rank)).mapToInt(Rank::getValue).sum();
         bet += sum * rank.getValue();
      }
      return bet;
   }

}
