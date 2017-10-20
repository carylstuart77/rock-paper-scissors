package edu.cnm.deepdive.ca.rock_paper_scissors.models;
//
import edu.cnm.deepdive.ca.rock_paper_scissors.models.Breed;
import java.util.Random;

public class Terrain {

  public static final int DEFAULT_SIZE = 50;
  public static final int DEFAULT_ITERATIONS = 500;
  public static final Neighborhood DEFAULT_NEIGHBORHOOD = Neighborhood.VON_NEUMANN;

  private Breed[][] cells;
  private Random rng = new Random();
  private int size = DEFAULT_SIZE;
  private Neighborhood neighborhood = DEFAULT_NEIGHBORHOOD;
  private int iterations = DEFAULT_ITERATIONS;
  private int steps = 0;

  public Terrain() {}

  public synchronized void initialize() {
    Breed[] breeds = Breed.values();
    cells = new Breed[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        cells[i][j] = breeds[rng.nextInt(breeds.length)];
      }
    }
  }

  public synchronized void step() {
    for (int i = 0; i < iterations; i++) {
      combat();
    }
    steps++;
  }

  public synchronized Breed[][] getSnapshot() {
    Breed[][] snapshot = new Breed[cells.length][];
    for (int i = 0; i < cells.length; i++) {
      snapshot[i] = cells[i].clone();
    }
    return snapshot;
  }

  public synchronized int getSteps() {
    return steps;
  }

  public Random getRng() {
    return rng;
  }

  public synchronized void setRng(Random rng) {
    this.rng = rng;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Neighborhood getNeighborhood() {
    return neighborhood;
  }

  public synchronized void setNeighborhood(Neighborhood neighborhood) {
    this.neighborhood = neighborhood;
  }

  public int getIterations() {
    return iterations;
  }

  public synchronized void setIterations(int iterations) {
    this.iterations = iterations;
  }

  private void combat() {
    int[] attackerCell = randomCell();
    int[] defenderCell = randomNeighbor(attackerCell[0], attackerCell[1]);
    Breed attacker = cells[attackerCell[0]][attackerCell[1]];
    Breed defender = cells[defenderCell[0]][defenderCell[1]];
    int outcome = Breed.comparator.compare(attacker, defender);
    if (outcome > 0) {
      cells[defenderCell[0]][defenderCell[1]] = attacker;
    } else if (outcome < 0) {
      cells[attackerCell[0]][attackerCell[1]] = defender;
    }
  }

  private int[] randomCell() {
    int row = rng.nextInt(cells.length);
    int column = rng.nextInt(cells[row].length);
    return new int[] {row, column};
  }

  private int[] randomNeighbor(int row, int column) {
    int[] offset = neighborhood.randomNeighbor(rng);
    int neighborRow = (row + cells.length + offset[0]) % cells.length;
    int neighborColumn = (column + cells[neighborRow].length + offset[1])
        % cells[neighborRow].length;
    return new int[] {neighborRow, neighborColumn};
  }

  public enum Neighborhood {
    MOORE {
      {
        neighbors = new int[][]{
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
        };
      }
    },
    VON_NEUMANN {
      {
        neighbors = new int[][]{
            {-1, 0},
            {0, -1},          {0, 1},
            { 1, 0}
        };
      }
    };

    protected int[][] neighbors;

    public int[] randomNeighbor(Random rng) {
      return neighbors[rng.nextInt(neighbors.length)];
    }

  }

}
