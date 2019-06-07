package player.Equipe2;

import pacman.Game;
import pacman.Location;
import pacman.State;

import java.util.Collection;

public class PacmanStateEvaluator {


    public static int MAX_DOTS_COUNT = 245;

    public static double evaluateState(State state){
        if (Game.isLosing(state))
            return -1000.0;
        if (Game.isWinning(state))
            return 1000.0;
        double score = 0.0;
        Location pacManLocation = state.getPacManLocation();

        double dotsProportion = (double)1 / state.getDotLocations().size() * MAX_DOTS_COUNT;

        score += Math.pow(dotsProportion, 16);
        score += getMinDistance(pacManLocation, state.getDotLocations()) * Math.pow(dotsProportion, 16);
        score += getMinDistance(pacManLocation, state.getGhostLocations());
        return score;
    }

    private static double getMinDistance(Location sourceLocation, Collection<Location> targetLocations) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (Location dotLocation : targetLocations) {
            double distance = Location.manhattanDistance(sourceLocation, dotLocation);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        if (Double.isInfinite(minDistance))
            throw new RuntimeException();
        return minDistance;
    }

}
