package arithmetic;

import bean.BoardChess;
import common.Constant;
import game.GameRule;
import utils.BoardUtil;


import java.util.LinkedList;
import java.util.List;

import static common.Constant.MAX;
import static common.Constant.MIN;
import static common.Constant.MODEL;

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
    private static final int MIDDLE = 16;

    /**
     * 行动力权重
     */
    private static final int mobilityWeight = 10;
    /**
     * 估值权重
     */
    private static final int posValueWeight = 2;
    /**
     * 棋子权重
     */
    private static final int countWeight = 8;

    /**
     * 稳定子权重
     */
    private static final int stabistorWeight = 10;
    /**
     * 终局权重
     */
    private static final int endWeight = 10000;
    /**
     * 计算次数
     */
    private static int count;

    /**
     * 终局估值 返回最终估值
     * @param data
     * @return
     */
    public static int endValue(BoardChess data) {
        count++;
        int score;
        byte[] chess = data.getChess();
        byte player = data.getCurrMove(), other = BoardUtil.change(player);
        // 只考虑棋子数
        int count = player_counters(chess, player) - player_counters(chess, other);
        score = count ==  0 ? 0 : (count >  0 ? MAX : MIN);
        return score;
    }
    /**
     * 估值函数
     *
     * @param data
     * @return
     */
    public static int currentValue(BoardChess data) {
        GameRule.valid_moves(data);
        int score = 0;
        byte player = data.getCurrMove(), other = player == Constant.WHITE ? Constant.BLACK : Constant.WHITE;
        byte[] chess = data.getChess();
        // 空位链表
        List<Byte> empty = data.getEmpty();
        if (empty.size() == 0 || GameRule.isShutDown(data)){
            return endValue(data);
        }
        count++;
        int emptyCount = empty.size();
        // 初盘只考虑行动力
        if (emptyCount >= OPENING){
            score += mobilityWeight * (data.getNextMobility() - data.getOtherMobility());
            return score;
        }else if (emptyCount > MIDDLE){
            // 行动力和权重
            score += mobilityWeight * (data.getNextMobility() - data.getOtherMobility());
            score += posValueWeight * (evaluation(chess,player) - evaluation(chess,other));
            return score;
        }else {
            // 行动力 棋子
            score += mobilityWeight * (data.getNextMobility() - data.getOtherMobility());
            score += posValueWeight * (evaluation(chess, player) - evaluation(chess, other));
            score += (player == Constant.WHITE ? countWeight : -countWeight) * (data.getwCount() - data.getbCount());
            // 稳定子
//            score += stabistorWeight * (stabistor(data, player) - stabistor(data, other));
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

//    /**
//     * 基于行动力的估值
//     *  head 空位链表
//     * @return
//     */
//    private static int countMobility(byte[] chess, Bag<Byte> empty, byte player){
//        int mobility = 0;
//        Iterator<Byte> em = empty.iterator();
//        while (em.hasNext()){
//            Byte cell = em.next();
//            if (GameRule.canFlips(chess,cell,player)){
//                mobility++;
//            }
//        }
//        return mobility;
//    }

    /**
     * 计算稳定子
     * @param data
     * @param player
     */
    private static int stabistor(BoardChess data, byte player) {
        byte[] chess = data.getChess();
        int count = 0;
        boolean flag;
        for (byte i = 0; i < MODEL; i++) {
            if (chess[i] == player){
                flag = false;
                // 角点为稳定子
                if (Constant.includeStabistor(i)){
                    flag = true;
                }else{
                    // 需判断八个方向上不能被翻转
//                    for (int dir = 0; dir < dirInc.length; dir++) {
//                        if (!singDirFlips(data,dirInc[dir],i)){
//                            flag = false;
//                            break;
//                        }
//                    }
                }
                if (flag) {
                    data.getOurStators().addFirst(i);
                    count++;
                }
            }
        }
        return count;
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

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        ReversiEvaluation.count = count;
    }
}
