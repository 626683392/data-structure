package arithmetic;

import bean.BoardChess;
import common.Bag;
import common.Constant;
import game.GameRule;

import java.util.Iterator;

import static common.Constant.MODEL;
import static common.Constant.SIZE;

/**
 * @author ypt
 * @ClassName ReversiEvaluation
 * @Description 估值方法
 * @date 2019/5/8 13:58
 */
public class ReversiEvaluation {

    /**
     * 开局 空闲位置40以上
     */
    private static final int OPENING = 40;
    /**
     * 中盘 空闲位置10 - 40
     */
    private static final int MIDDLE = 10;

    /**
     * 行动力权重
     */
    private static final int mobilityWeight = 7;
    /**
     * 估值权重
     */
    private static final int posValueWeight = 2;
    /**
     * 棋子权重
     */
    private static final int countWeight = 7;
    /**
     * 终局权重
     */
    private static final int endWeight = 10000;

    /**
     * 终局估值
     * @param data
     * @return
     */
    public static int endValue(BoardChess data) {
        int score = 0;
        byte[] chess = data.getChess();
        byte player = data.getCurrMove(), other = player == Constant.WHITE ? Constant.BLACK : Constant.WHITE;
        // 只考虑棋子数
        score += endWeight * (player_counters(chess, player) - player_counters(chess, other));
        return score;
    }
    /**
     * 估值函数
     *
     * @param data
     * @return
     */
    public static int currentValue(BoardChess data) {
        if (GameRule.isShutDown(data)){
            return endValue(data);
        }
        int score = 0;
        byte player = data.getCurrMove(), other = player == Constant.WHITE ? Constant.BLACK : Constant.WHITE;
        byte[] chess = data.getChess();
        // 空位链表
        Bag<Byte> empty = data.getEmpty();
        int emptyCount = empty.size();
        // 初盘只考虑行动力
        if (emptyCount >= OPENING){
            score += mobilityWeight * (countMobility(chess,empty,player) - countMobility(chess,empty,other));
            return score;
        }else if (emptyCount > MIDDLE){
            // 行动力和权重
            score += mobilityWeight * (countMobility(chess,empty,player) - countMobility(chess,empty,other));
            score += posValueWeight * (evaluation(chess,player) - evaluation(chess,other));
        }else {
            // 行动力 棋子
            score += posValueWeight * (evaluation(chess, player) - evaluation(chess, other));
            score += mobilityWeight * (countMobility(chess, empty, player) - countMobility(chess, empty, other));
            score += countWeight * (player_counters(chess, player) - player_counters(chess, other));
        }
        return score;
    }

    /**
     * //每一个棋子的权重
     */
    private final static int[] evaluation = {
            0,  0,    0,    0,    0,    0,   0,   0,     0,
            0,  100,  -8,   10,   5,    5,   10,  -8,    100,
            0,  -8,   -45,  1,    1,    1,   1,   -45,   -8,
            0,  10,   1,    3,    2,    2,   3,   1,     10,
            0,  5,    1,    2,    1,    1,   2,   1,     5,
            0,  5,    1,    2,    1,    1,   2,   1,     5,
            0,  10,   1,    3,    2,    2,   3,   1,     10,
            0,  -8,   -45,  1,    1,    1,   1,   -45,   -8,
            0,  100,  -8,   10,   5,    5,   10,  -8,    100,
            0,  0,    0,    0,    0,    0,   0,   0,     0,     0
    };

    /**
     * 位置估值
     * @param chess
     * @param player
     * @return
     */
    private static int evaluation(byte[] chess, byte player){
        int count = 0;
        for (byte i = 0; i < MODEL; i++) {
            if (chess[i] == player){
                count += evaluation[i];
            }
        }
        return count;
    }

    /**
     * 基于行动力的估值
     *  head 空位链表  
     * @return
     */
    private static int countMobility(byte[] chess, Bag<Byte> empty, byte player){
        int mobility = 0;
        Iterator<Byte> em = empty.iterator();
        while (em.hasNext()){
            Byte cell = em.next();
            if (GameRule.canFlips(chess,cell,player)){
                mobility++;
            }
        }
        return mobility;
    }

    /**
     * /棋子统计方法
     */
    public static int player_counters(byte[] chess, byte player){
        int count = 0;
        for (byte i = 0; i < MODEL; i++) {
            if (chess[i] == player){
                count ++;
            }
        }
        return count;
    }

    /**
     * /棋子统计方法
     */
    public static int player_counters(byte[][] chess, byte player){
        int count = 0,row,col;
        for(row=0;row<SIZE;++row)
            for(col=0;col<SIZE;++col)
                if(chess[row][col] == player)
                    ++count;
        return count;
    }

}
