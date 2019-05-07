package interactive;

import arithmetic.AlphaBeta;
import bean.BoardData;
import bean.MinimaxResult;
import bean.Move;
import common.Constant;
import game.Board;
import game.GameContext;
import game.GameRule;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ForkJoinTask;

/**
 * @author ypt
 * @ClassName AlphaBetaListener
 * @Description 观察者
 * @date 2019/5/7 10:14
 */
public class AlphaBetaListener implements Observer {

    private Observable ob;

    public AlphaBetaListener(Observable ob) {
        this.ob = ob;
        ob.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        MouseListener mouseListener = (MouseListener)o;
        // 模拟棋盘
        BoardData copyBoard = mouseListener.getCopyBoard();
        // 显示棋盘
        BoardData boardChess = mouseListener.getBoardChess();
        // 棋盘UI
        Board board = mouseListener.getBoard();
        MinimaxResult result = AlphaBeta.alpha_Beta(copyBoard);
        if (copyBoard.getNextmove() == Constant.BLACK){
            System.out.println(result);
            // 走这步棋
            GameRule.MakeMoveRun makeMove = GameRule.getMakeMove(boardChess, result.getMove());
            ForkJoinTask<List<Move>> task = GameContext.submit(makeMove);
            GameContext.getCall(task);
            GameRule.valid_moves(boardChess,boardChess.getNextmove());
            board.upshow();
        }
    }
}
