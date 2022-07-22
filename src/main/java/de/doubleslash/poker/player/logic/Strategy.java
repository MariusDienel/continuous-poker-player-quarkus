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

        int bet = table.getSmallBlind()+ 5;
        bet += getBetByPairs(ranks, bet) / 10;

        List<Integer> enemyBets = table.getPlayers().stream().map(Player::getBet).collect(Collectors.toList());
        if (enemyBets.stream().anyMatch(enemyBet -> enemyBet > 50)) {
            return 0;
        }

        int pairMultiplier = getPairMultiplier(combined);
        bet = bet * pairMultiplier;

        int stack = ownPlayer.getStack();

        int halfStack = stack / 2;
        if (bet > halfStack) {
            return halfStack;
        }

        if (street(combined)) {
            bet = 1000;
        }
        if (flush(combined)) {
            bet = 1000;
        }
//        double multiplier = stack / 100;
        double multiplier2 = table.getPot() / 10;

//        if (multiplier > 1) {
//            bet = (int) Math.round(bet * multiplier);
//        }
//        if (multiplier2 > 1) {
//            bet = (int) Math.round(bet * multiplier2);
//        }
        return bet;
    }

    private int getPairMultiplier(List<Card> combined) {
        List<Integer> values = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            long count = combined.stream().filter(c -> c.getRank().equals(rank)).count();
            values.add((int) count);
        }
        boolean isDouble = values.contains(2);
        boolean isDoubleDouble = values.stream().filter(e -> e == 2).count() >= 2;
        boolean isTriple = values.contains(3);
        boolean isQuad = values.contains(4);

        if (isDouble && isTriple) {
            return 20000;
        }
        if (isQuad) {
            return 100000000;
        }
        if (isDoubleDouble) {
            return 3;
        }
        if (isTriple) {
            return 3;
        }
        if (isDouble) {
            return 2;
        }
        return 1;
    }

    private boolean flush(List<Card> combined) {
        int hearts = 0;
        int spades = 0;
        int clubs = 0;
        int diamonds = 0;
        if (combined.size() >= 5) {
            for (Card card : combined) {
                Suit suit = card.getSuit();
                if (suit.equals(Suit.HEARTS)) {
                    hearts += 1;
                }
                if (suit.equals(Suit.CLUBS)) {
                    clubs += 1;
                }
                if (suit.equals(Suit.DIAMONDS)) {
                    diamonds += 1;
                }
                if (suit.equals(Suit.SPADES)) {
                    spades += 1;
                }
            }
        }
        if (hearts == 5 || spades == 5 || clubs == 5 || diamonds == 5) {
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
