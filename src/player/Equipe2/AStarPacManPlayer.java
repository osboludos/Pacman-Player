package player.Equipe2;

import pacman.*;
import util.Counter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AStarPacManPlayer implements StateEvaluator, PacManPlayer {

    @Override
    public Move chooseMove(Game game) {
        return AStar(game.getCurrentState(), new Location(0, 29), 20);
    }

    private class LocationNode{

        public LocationNode(LocationNode parent, State state, Location location, Move move){
            Reset(parent, state, location, move);
        }

        public void Reset(LocationNode newParent, State state, Location location, Move move){
            m_parent = newParent;
            m_location = location;
            m_move = move;
            m_state = state;
        }

        public Location GetLocation() { return new Location(m_location.getX(), m_location.getY()); }

        public Move GetMove() {return m_move;}

        public State GetState() {return m_state;}

        private State m_state;
        private Location m_location;
        private Move m_move;
        private LocationNode m_parent;
    }

    public Move AStar(State state, Location target, int depth){

        Location from = state.getPacManLocation();
        MyCounter<LocationNode> closed = new MyCounter<>();
        MyCounter<LocationNode> open = new MyCounter<>();

        LocationNode initial = new LocationNode(null, state, from, Move.NONE);
        closed.setCount(initial, CalculateCost(from, target, initial.GetLocation()));

        List<Move> legalMoves = Game.getLegalPacManMoves(state);

        for (Move m : legalMoves){
            State next = Game.getNextState(state, m);
            LocationNode neighbour = new LocationNode(initial, next, next.getPacManLocation(), m);
            open.setCount(neighbour, CalculateCost(from, target, neighbour.GetLocation()));
        }

        while(true){
            depth--;
            LocationNode current = open.argmin();
            open.remove(current);
            closed.setCount(current, CalculateCost(from, target, current.GetLocation()));

            if (current.GetLocation().equals(target) || depth < 0){
                while (!current.m_parent.equals(initial)){
                    current = current.m_parent;
                }
                return current.GetMove();
            }

            if (Game.isFinal(current.GetState())){
                continue;
            }
            legalMoves = Game.getLegalPacManMoves(current.GetState());

            legalMoves.remove(current.GetMove().getOpposite());

            for(Move m : legalMoves){

                State next = Game.getNextState(current.GetState(), m);
                LocationNode neighbour = new LocationNode(current, next, next.getPacManLocation(), m);

                if (closed.containsKey(neighbour)){
                    continue;
                }

                double cost = CalculateCost(from, target, neighbour.GetLocation());
                if (!open.containsKey(neighbour) || open.getCount(neighbour) > cost){
                    neighbour.Reset(current, neighbour.GetState(), neighbour.GetLocation(), m);
                    open.setCount(neighbour, cost);
                }
            }
        }
    }


    public double CalculateCost(Location from, Location to, Location current){
        double f = Location.euclideanDistance(current, from);
        double h = Location.euclideanDistance(current, to);
        return f + h;
    }

    @Override
    public double evaluateState(State s) {
        if (Game.isLosing(s))
            return -1000.0;
        if (Game.isWinning(s))
            return 1000.0;
        double score = 0.0;
        Location pacManLocation = s.getPacManLocation();

        double dotsProportion = (double)1 / s.getDotLocations().size() * PacmanStateEvaluator.MAX_DOTS_COUNT;

//        score += Math.pow(dotsProportion, 16);
//        score += getMinDistance(pacManLocation, state.getDotLocations()) * Math.pow(dotsProportion, 16);
//        score += getMinDistance(pacManLocation, state.getGhostLocations());

        return score;
    }
}
