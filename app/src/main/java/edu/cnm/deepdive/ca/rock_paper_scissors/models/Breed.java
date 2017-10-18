package edu.cnm.deepdive.ca.rock_paper_scissors.models;

import java.util.Comparator;

public enum Breed {
  ROCK, PAPER, SCISSORS;

  public static final Comparator<Breed> comparator = new Comparator<Breed>() {
    @Override
    public int compare(Breed breed1, Breed breed2) {
      switch (breed1){
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
            case PAPER:
              return -1;
            case SCISSORS:
              return 1;
          }
      }
      return 0;
    }
  };

    }

