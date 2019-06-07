package player.Equipe2;

import pacman.*;
import util.Counter;

import java.util.List;

public class BFSPacManPlayer implements PacManPlayer, StateEvaluator {


    Move m_lastMove = null;

    @Override
    public Move chooseMove(Game game) {

        System.out.println(game.getCurrentState().getDotLocations().size());

        State s = game.getCurrentState();

        List<Move> legalMoves = game.getLegalPacManMoves();

        Counter<Move> scores = new Counter<Move>();

        for (Move m : legalMoves){

            List<State> stateList = Game.getProjectedStates(s, m);

            State last = stateList.get(stateList.size() - 1);


            double score = bestMoveFromState(last, 4);

            if (m.getOpposite().equals(m_lastMove)){
                score -= 100;
            }

            scores.setCount(m, score);

        }

        m_lastMove = scores.argmax();

        return m_lastMove;
    }

    private double bestMoveFromState(State s, int depth){

        if (Game.isFinal(s)){
            if (Game.isWinning(s)){
                return 1000;
            }
            else{
                return -1250;
            }
        }

        List<Move> legalMoves = Game.getLegalPacManMoves(s);
        double totalScore = 0;

        for (Move m : legalMoves) {

            List<State> stateList = Game.getProjectedStates(s, m);

            State last = stateList.get(stateList.size() - 1);
            if (depth > 0){
                totalScore += bestMoveFromState(last, depth - 1);
            }
            totalScore += evaluateState(last);
        }
        return totalScore / legalMoves.size();
    }

    @Override
    public double evaluateState(State s) {
        return PacmanStateEvaluator.evaluateState(s);
    }
}
