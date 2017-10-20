package edu.cnm.deepdive.ca.rock_paper_scissors.models;

import java.util.Random;

/**
 * This class models cyclic dominance in a simplified ecosystem as a form of the game
 * Rock-Paper-Scissors (RPS), played on a toroidal terrain. RPS interactions happen between pairs of
 * neighboring {@link Breed} instances located on square lattice points on a 2-dimensional torus. In
 * each iteration, one such pair is chosen at random, with the loser replaced by an instance of the
 * winner's breed.
 * <p>
 * This model may also be viewed as a 2-dimensional, 3-state stochastic cellular automaton (CA). The
 * state of each cell in generation (<i>n</i> + 1) is based on the state of that cell in generation
 * <i>n</i> and the random selection of a single cell and one of its neighbors. If a given cell is
 * note one of the 2 randomly selected cells, then its state is unchanged; otherwise, the new state
 * of that cell (and that of the other cell selected at random) is based on the outcome of the
 * conflict between the 2 cells &ndash; that is, on the RPS game played between them.
 *
 * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android+SalesForce Bootcamp Cohort 2
 * @version 1.0, 2017-10-20
 */
public class Terrain {

  /** Default height and width of square lattice. */
  public static final int DEFAULT_SIZE = 50;
  /** Default number of iterationsPerStep performed in each high-level step of the system. */
  public static final int DEFAULT_ITERATIONS_PER_STEP = 500;
  /** Default neighborhood type used in selecting pairs of adjacent {@link Breed} instances. */
  public static final Neighborhood DEFAULT_NEIGHBORHOOD = Neighborhood.VON_NEUMANN;

  private Breed[][] cells = null;
  private Random rng = new Random();
  private int size = DEFAULT_SIZE;
  private Neighborhood neighborhood = DEFAULT_NEIGHBORHOOD;
  private int iterationsPerStep = DEFAULT_ITERATIONS_PER_STEP;
  private int steps;
  private long totalIterations;

  /**
   * Create the lattice and initialize it by assigning a random instance of {@link Breed} to each
   * lattice point.
   */
  public synchronized void initialize() {
    Breed[] breeds = Breed.values();
    cells = new Breed[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        cells[i][j] = breeds[rng.nextInt(breeds.length)];
      }
    }
    steps = 0;
    totalIterations = 0;
  }

  /**
   * Update the state of the system by performing {@link #getSteps()} iterations of random pair
   * selections and resolution of RPS interactions between the members of the selected pairs.
   */
  public synchronized void step() {
    if (cells == null) {
      initialize();
    }
    for (int i = 0; i < iterationsPerStep; i++) {
      combat();
    }
    steps++;
    totalIterations += iterationsPerStep;
  }

  /**
   * Constructs and returns a non-volatile copy of the terrain lattice.
   *
   * @return lattice of {@link Breed} instance references
   */
  public synchronized Breed[][] getSnapshot() {
    Breed[][] snapshot = new Breed[cells.length][];
    for (int i = 0; i < cells.length; i++) {
      snapshot[i] = cells[i].clone();
    }
    return snapshot;
  }

  /**
   * Returns the number of high-level steps executed since the last invocation of {@link
   * #initialize()}.
   *
   * @return number of high-level steps executed
   */
  public synchronized int getSteps() {
    return steps;
  }

  /**
   * Returns the instance of {@link Random} currently assigned as the RPS system's source of
   * randomness. This instance is used for lattice population initialization and for selection of
   * random neighboring pairs in each iteration.
   *
   * @return source of randomness
   */
  public Random getRng() {
    return rng;
  }

  /**
   * Sets the instance of {@link Random} to be used as the RPS system's source of randomness, for
   * randomness. This instance is used for lattice population initialization and for selection of
   * random neighboring pairs in each iteration.
   *
   * @param rng source of randomness
   */
  public synchronized void setRng(Random rng) {
    this.rng = rng;
  }

  /**
   * Returns the size (height and width) set for the square lattice used as the RPS terrain. Note
   * that if {@link #setSize(int)} has been invoked after the most recent invocation (explicit or
   * implicit, via {@link #step()}) of {@link #initialize()}, the value returned may not be equal to
   * the current size of the array returned by {@link #getSnapshot()}.
   *
   * @return terrain lattice size (height and width)
   */
  public int getSize() {
    return size;
  }

  /**
   * Sets the size (height and width) that will be used for creation of the terrain lattice in the
   * next execution of {@link #initialize()}.
   *
   * @param size terrain lattice size (height and width)
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Returns the {@link Neighborhood} instance currently specified for use in selecting pairs of
   * adjacent inhabitants of the terrain.
   *
   * @return adjacency type
   */
  public Neighborhood getNeighborhood() {
    return neighborhood;
  }

  /**
   * Sets the {@link Neighborhood} instance to be used in selecting pairs of adjacent inhabitants of
   * the terrain.
   *
   * @param neighborhood adjacency type
   */
  public synchronized void setNeighborhood(Neighborhood neighborhood) {
    this.neighborhood = neighborhood;
  }

  /**
   * Returns the number of low-level iterations performed in each high-level execution step &ndash;
   * i.e. each invocation of the {@link #step()} method.
   *
   * @return iterations per step
   */
  public int getIterationsPerStep() {
    return iterationsPerStep;
  }

  /**
   * Sets the number of low-level iterations to be performed in each high-level execution step
   * &ndash; i.e. each invocation of the {@link #step()} method.
   *
   * @param iterationsPerStep iterations per step
   */
  public synchronized void setIterationsPerStep(int iterationsPerStep) {
    this.iterationsPerStep = iterationsPerStep;
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

  /**
   * This class enumerates 2 commonly used neighborhoods for determining adjacency in a number of
   * 2-dimensional cellular automata (CA) and similar models.
   *
   * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android+SalesForce Bootcamp Cohort 2
   */
  public enum Neighborhood {
    /** Directly and diagonally adjacent cells are considered neighbors. */
    MOORE {
      {
        neighbors = new int[][]{
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
        };
      }
    },
    /** Directly (but not diagonally) adjacent cells are considered neighbors. */
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

    /**
     * Select and return a neighbor at random, in the form of a 2-element <code>int</code> array,
     * where the elements are row and column offsets in the closed interval [-1, 1].
     *
     * @param rng source of randomness
     * @return <code>{row, column}</code> offsets
     */
    public int[] randomNeighbor(Random rng) {
      return neighbors[rng.nextInt(neighbors.length)];
    }

  }

}
