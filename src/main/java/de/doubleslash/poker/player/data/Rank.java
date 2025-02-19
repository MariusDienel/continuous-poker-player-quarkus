package de.doubleslash.poker.player.data;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Rank {
   ACE("A", 8, 13),
   KING("K", 7, 12),
   QUEEN("Q", 6, 11),
   JACK("J", 5, 10),
   TEN("10", 4, 9),
   NINE("9", 2, 8),
   EIGHT("8", 2, 7),
   SEVEN("7", 2, 6),
   SIX("6", 2, 5),
   FIVE("5",2, 4),
   FOUR("4",1, 3),
   THREE("3",1, 2),
   TWO("2",1, 1);

   private final String token;
   private final int value;
   private final int order;

   Rank(final String token, int value, int order) {
      this.token = token;
      this.value = value;
      this.order = order;
   }

   public String getToken() {
      return token;
   }

   public int getOrder() {
      return order;
   }


   @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
   public static Rank forToken(final String token) {
      return Arrays.stream(Rank.values()).filter(r -> r.getToken().equals(token)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Token not allowed: " + token));
   }


   public int getValue() {
      return value;
   }
}
