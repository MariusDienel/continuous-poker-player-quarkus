package de.doubleslash.poker.player.data;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Rank {
   ACE("A", 5),
   KING("K", 4),
   QUEEN("Q", 4),
   JACK("J", 3),
   TEN("10", 3),
   NINE("9", 2),
   EIGHT("8", 2),
   SEVEN("7", 2),
   SIX("6", 2),
   FIVE("5",1),
   FOUR("4",1),
   THREE("3",1),
   TWO("2",1);

   private final String token;
   private final int value;

   Rank(final String token, int value) {
      this.token = token;
      this.value = value;
   }

   public String getToken() {
      return token;
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
