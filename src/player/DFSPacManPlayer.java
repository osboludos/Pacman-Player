package player;

import pacman.*;
import util.Counter;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this class for your basic DFS player implementation.
 * @author grenager
 *
 */
public class DFSPacManPlayer implements PacManPlayer, StateEvaluator {


  List<List<Location>> fourSquares;

  boolean firstFrame = true;

  public Move chooseMove(Game game) {

    if (firstFrame){
      game.getGhostPlayers().removeAll(game.getGhostPlayers());
      game.getCurrentState().getGhostLocations().removeAll(game.getCurrentState().getGhostLocations());

      fourSquares = new ArrayList<>();
      fourSquares.add(new ArrayList<>());
      fourSquares.add(new ArrayList<>());
      fourSquares.add(new ArrayList<>());
      fourSquares.add(new ArrayList<>());
      for(Location l : game.getAllLocationsCopy()){
        if (l.getX() < 13 && l.getY() > 14){
          fourSquares.get(0).add(l);
        }
        else if (l.getX() >= 13 && l.getY() > 14){
          fourSquares.get(1).add(l);
        }
        else if (l.getX() < 13 && l.getY() < 14){
          fourSquares.get(2).add(l);
        }
        else if (l.getX() >= 13 && l.getY() < 14){
          fourSquares.get(3).add(l);
        }
        else {
          System.out.println("Tinha uma location fora de tudo!" + l);
        }
      }
      firstFrame = false;
    }

    State s = game.getCurrentState();

    List<Move> legalMoves = game.getLegalPacManMoves();

    Counter<Move> scores = new Counter<Move>();

    for (Move m : legalMoves){

      List<State> stateList = Game.getProjectedStates(s, m);

      State last = stateList.get(stateList.size() - 1);

      scores.setCount(m, bestMoveFromState(last, 2));
    }

    return scores.argmax();
  }


  private double bestMoveFromState(State s, int depth){

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

  /**
   * Computes an estimate of the value of the State.
   * @params the State to evaluate.
   * @return an estimate of the value of the State.
   */
  public double evaluateState(State state) {
    if (Game.isLosing(state))
      return Double.NEGATIVE_INFINITY;

    double score = 0;

    int index = getFourSquareIndex(state.getDotLocations());






    return -state.getDotLocations().size();
  }

  private int getFourSquareIndex(LocationSet dots){

    int fs1 = 0, fs2 = 0, fs3 = 0, fs4 = 0;
    for(Location l : dots){
      if (l.getX() < 13 && l.getY() > 14){
        fs1++;
      }
      else if (l.getX() >= 13 && l.getY() > 14){
        fs2++;
      }
      else if (l.getX() < 13 && l.getY() < 14){
        fs3++;
      }
      else if (l.getX() >= 13 && l.getY() < 14){
        fs4++;
      }
      else {
        System.out.println("Tinha um dot fora de tudo!" + l);
      }
    }

    int best = -1;
    int bestIndex = -1;
    if (fs1 > best){
      best = fs1;
      bestIndex = 0;
    }
    if (fs2 > best){
      best = fs1;
      bestIndex = 1;
    }
    if (fs3 > best){
      best = fs1;
      bestIndex = 2;
    }
    if (fs4 > best){
      bestIndex = 3;
    }

    return bestIndex;
  }

}
