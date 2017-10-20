package edu.cnm.deepdive.ca.rock_paper_scissors.models;

import java.util.Comparator;

/**
 * Enumerated type representing the 3 breeds &ndash; Rock, Paper, Scissors. A <code>static</code>
 * member field is provided for comparing the enumerated types based on the cyclic dominance rules
 * of RPS.
 *
 * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android+SalesForce Bootcamp Cohort 2
 * @version 1.0, 2017-10-20
 */
public enum Breed {
  ROCK, PAPER, SCISSORS;

  /** Comparator implementing cyclic dominance of RPS ecosystem */
  public static final Comparator<Breed> comparator = new Comparator<Breed>() {
    @Override
    public int compare(
        Breed breed1, Breed breed2) {
      switch (breed1) {
        case ROCK:
          switch (breed2) {
            case PAPER:
              return -1;
            case SCISSORS:
              return 1;
          }
        case PAPER:
          switch (breed2) {
            case SCISSORS:
              return -1;
            case ROCK:
              return 1;
          }
        case SCISSORS:
          switch (breed2) {
            case ROCK:
              return -1;
            case PAPER:
              return 1;
          }
      }
      return 0;
    }
  };

}
