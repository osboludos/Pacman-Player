package player.Equipe2;

import pacman.*;
import util.Counter;

import java.util.List;

public class GreedyPacManPlayer implements PacManPlayer, StateEvaluator {

    @Override
    public Move chooseMove(Game game) {
        State s = game.getCurrentState();

        List<Move> legalMoves = game.getLegalPacManMoves();

        Counter<Move> scores = new Counter<Move>();

        for (Move m : legalMoves){

            List<State> stateList = Game.getProjectedStates(s, m);

            State last = stateList.get(stateList.size() - 1);

            scores.setCount(m, evaluateState(last));
        }

        return scores.argmax();
    }

    @Override
    public double evaluateState(State s) {
        return PacmanStateEvaluator.evaluateState(s);
    }
}
