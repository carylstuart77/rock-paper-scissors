/**
 * Classes that implement a Rock-Paper-Scissors (RPS) model of an ecosystem with cyclic dominance
 * between 3 species. In this modeling approach, each individual's play is not based on a conscious
 * choice in the moment, but on the individual's species (more often called <em>breed</em> in the
 * agent-based modeling context). In each conflict between individuals (which are generally chosen
 * based on some kind of spacial adjacency), the loser is replaced by an individual of the winner's
 * breed.
 * <p>
 * As noted in <a
 * href="http://guava.physics.uiuc.edu/~nigel/courses/569/Essays_Spring2006/files/kircher.pdf">
 * "Emergent Behavior of Rock-Paper-Scissors Game"</a> (K. Kircher, 2006), while such an approach
 * may seem overly simplistic, it not only models some ecosystems quite well, but is also
 * instructive in understanding many others with non-transitive predation or competition. (It's also
 * a useful learning model for students of cellular automata, ecosystem modeling,
 * model-view-controller design, etc.)
 * <p>
 * While this model is inspired and informed by agent-based models on the same topic, this is not
 * itself an agent-based model. Rather than each inhabitant of the terrain being a distinct instance
 * of a breed (or member of a breed set), each is a reference to one of the small group of instances
 * of the {@link edu.cnm.deepdive.ca.rps.models.Breed Breed} enumerated type. This means (for
 * example) that there is no distinction between a {@link edu.cnm.deepdive.ca.rps.models.Breed#ROCK
 * Breed.ROCK} at one point in the terrain and a {@link edu.cnm.deepdive.ca.rps.models.Breed#ROCK
 * Breed.ROCK} at another point: they are not just equal in terms of their states, but also in terms
 * of their identities.
 *
 * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android+SalesForce Bootcamp Cohort 2
 * @version 1.0, 2017-10-20
 */
package edu.cnm.deepdive.ca.rock_paper_scissors.models;