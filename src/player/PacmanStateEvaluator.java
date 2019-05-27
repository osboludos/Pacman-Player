package player;

import pacman.Game;
import pacman.Location;
import pacman.State;

import java.util.Collection;

public class PacmanStateEvaluator {


    private static int MAX_DOTS_COUNT = 245;

    public static double evaluateState(State state){
        if (Game.isLosing(state))
            return -1000.0; // really bad to get eaten
        if (Game.isWinning(state))
            return 1000.0; // really good to win
        double score = 0.0;
        Location pacManLocation = state.getPacManLocation();

        double dotsProportion = (double)1 / state.getDotLocations().size() * MAX_DOTS_COUNT;

        score += Math.min(Math.pow(dotsProportion, 16), 10000); // more dots left on the board is bad
        score -= getMinDistance(pacManLocation, state.getDotLocations()) * Math.pow(dotsProportion, 16); // being far
        // from
        // nearest dot
        // is bad
        score += getMinDistance(pacManLocation, state.getGhostLocations()); // being far
        // from
        // nearest
        // ghost is
        // good
        return score;
    }

    private static double getMinDistance(Location sourceLocation, Collection<Location> targetLocations) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (Location dotLocation : targetLocations) {
            double distance = Location.manhattanDistance(sourceLocation, dotLocation); // one
            // way
            // to
            // measure
            // distance
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        if (Double.isInfinite(minDistance))
            throw new RuntimeException();
        return minDistance;
    }

}
