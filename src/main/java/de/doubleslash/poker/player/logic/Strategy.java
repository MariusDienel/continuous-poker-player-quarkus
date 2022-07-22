package de.doubleslash.poker.player.logic;

import de.doubleslash.poker.player.data.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Strategy {

    public int decide(final Table table) {
        System.out.println(table);
        Player ownPlayer = table.getOwnPlayer();
        List<Card> cards = ownPlayer.getCards();
        List<Card> communityCards = table.getCommunityCards();

        List<Card> combined = new ArrayList<>(cards);
        combined.addAll(communityCards);

        List<Rank> ranks = combined.stream().map(Card::getRank).collect(Collectors.toList());

        int bet = 0;
        bet = getBetByPairs(ranks, bet);

        List<Integer> enemyBets = table.getPlayers().stream().map(Player::getBet).collect(Collectors.toList());
        if (enemyBets.stream().anyMatch(enemyBet -> enemyBet > 25)) {
            return 0;
        }

        int stack = ownPlayer.getStack();

        if (street(combined)) {
            return 50;
        }
        if(flush(combined)) {
            return 50;
        }
        double multiplier = stack / 100;

        long round = Math.round(bet * multiplier);
        return (int) round;
    }

    private boolean flush(List<Card> combined) {
        int hearts = 0;
        int spades = 0;
        int clubs = 0;
        int diamonds = 0;
        if (combined.size() >= 5) {
            for (Card card : combined) {
                Suit suit = card.getSuit();
                if(suit.equals(Suit.HEARTS)) {
                    hearts+=1;
                }
                if(suit.equals(Suit.CLUBS)) {
                    clubs+=1;
                }
                if(suit.equals(Suit.DIAMONDS)) {
                    diamonds+=1;
                }
                if(suit.equals(Suit.SPADES)) {
                    spades+=1;
                }
            }
        }
        if(hearts == 5 || spades == 5 ||clubs == 5 || diamonds == 5) {
            return true;
        }
        return false;
    }

    private boolean street(List<Card> combined) {
        if (combined.size() >= 5) {
            List<Integer> sortedOrders = combined.stream().map(c -> c.getRank().getValue()).sorted().collect(Collectors.toList());
            int current = sortedOrders.get(0);
            for (int i = 1; i < sortedOrders.size(); i++) {
                int newValue = sortedOrders.get(i);
                int expected = current += 1;
                if (newValue != expected) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
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
