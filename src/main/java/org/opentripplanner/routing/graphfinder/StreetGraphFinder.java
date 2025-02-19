package org.opentripplanner.routing.graphfinder;

import org.opentripplanner.model.GenericLocation;
import org.opentripplanner.routing.algorithm.astar.AStar;
import org.opentripplanner.routing.algorithm.astar.TraverseVisitor;
import org.opentripplanner.routing.algorithm.astar.strategies.SearchTerminationStrategy;
import org.opentripplanner.routing.algorithm.astar.strategies.TrivialRemainingWeightHeuristic;
import org.opentripplanner.routing.api.request.RoutingRequest;
import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.spt.DominanceFunction;

import java.util.List;

/**
 * A GraphFinder which uses the street network to traverse the graph in order to find the nearest
 * stops and/or places from the origin.
 */
public class StreetGraphFinder implements GraphFinder {

  private final Graph graph;

  public StreetGraphFinder(Graph graph) {
    this.graph = graph;
  }

  @Override
  public List<StopAtDistance> findClosestStops(double lat, double lon, double radiusMeters) {
      StopFinderTraverseVisitor visitor = new StopFinderTraverseVisitor();
      findClosestUsingStreets(lat, lon, radiusMeters, visitor, null);
      return visitor.stopsFound;
  }

  private void findClosestUsingStreets(
      double lat, double lon, double radius, TraverseVisitor visitor, SearchTerminationStrategy terminationStrategy
  ) {
    // Make a normal OTP routing request so we can traverse edges and use GenericAStar
    // TODO make a function that builds normal routing requests from profile requests
    RoutingRequest rr = new RoutingRequest(TraverseMode.WALK);
    rr.from = new GenericLocation(null, null, lat, lon);
    rr.oneToMany = true;
    rr.setRoutingContext(graph);
    rr.walkSpeed = 1;
    rr.dominanceFunction = new DominanceFunction.LeastWalk();
    rr.rctx.remainingWeightHeuristic = new TrivialRemainingWeightHeuristic();
    // RR dateTime defaults to currentTime.
    // If elapsed time is not capped, searches are very slow.
    rr.worstTime = (rr.dateTime + (int) radius);
    AStar astar = new AStar();
    rr.setNumItineraries(1);
    astar.setTraverseVisitor(visitor);
    astar.getShortestPathTree(rr, 1, terminationStrategy); // timeout in seconds
    // Destroy the routing context, to clean up the temporary edges & vertices
    rr.rctx.destroy();
  }

}
