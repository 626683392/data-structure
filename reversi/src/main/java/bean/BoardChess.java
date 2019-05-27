package bean;

import arithmetic.subsidiary.TranspositionTable;
import common.Constant;
import common.GameStatus;
import game.Chess;
import lombok.Data;
import utils.BoardUtil;

import java.util.LinkedList;

import static common.Constant.MIDDLE;
import static common.Constant.MODEL;
import static common.Constant.OPENING;

/**
 * 计算用棋盘数据
 * @author Root
 */
@Data
public class BoardChess {
    /**
     * 棋盘共享数据
     */
    private byte[] chess;

    /**
     * 下一步棋子:白棋优先
     *  WHITE
     *  BLACK
     */
    private byte currMove;

    /**
     * 空位链表
     */
    private LinkedList<Byte> empty;
    /**
     * 双方子数
     */
    private byte wCount;
    private byte bCount;
    /**
     * 局面的zobrist值(哈希) 每次改变状态更新
     */
    private long zobrist;
    /**
     * 当前选手行动力
     */
    private int ourMobility = 0;
    /**
     * 对手行动力
     */
    private int oppMobility = 0;
    /**
     * 我方稳定子
     */
    private LinkedList<Byte> ourStators = new LinkedList<>();
    /**
     * 对方稳定子
     */
    private LinkedList<Byte> oppStators = new LinkedList<>();
    /**
     * 每一步 步数信息
     */
    private LinkedList<ChessStep> steps = new LinkedList<>();
    /**
     * 游戏状态
     */
    private GameStatus status;

    public BoardChess(byte player){
        this.chess = new byte[MODEL];
        this.empty = new LinkedList<>();
        this.wCount = 2;
        this.bCount = 2;
        this.currMove = player;
        // 初始化哨兵
        this.initChess();
        // 初始化状态
        this.status = GameStatus.OPENING;
    }

    public BoardChess(byte[] chess,byte player) {
        this(player);
        System.arraycopy(chess,0,this.chess,0,MODEL);
        // 初始化空位链表
        this.initEmpty(chess);
        // 初始化zobrist的值
        this.zobrist = TranspositionTable.initZobrist(this);
    }

    public BoardChess(Chess[][] chess,byte player) {
        this(player);
        // 转换棋盘数据
        this.copyChess(chess);
        // 初始化空位链表
        this.initEmpty(this.chess);
        // 初始化zobrist的值
        this.zobrist = TranspositionTable.initZobrist(this);
    }

    /**
     * 初始化空位链表
     */
    private void initEmpty(byte[] chess) {
        for (byte i = 0; i < MODEL; i++) {
            if (chess[i] == Constant.EMPTY || chess[i] == Constant.DOT_W || chess[i] == Constant.DOT_B){
                this.empty.add(i);
            }
        }
    }

    /**
     * 初始化棋盘数据
     *  d为边界 x为棋子
     *     ddddddddd    8
     *     dxxxxxxxx    17
     *     dxxxxxxxx    26
     *     dxxxxxxxx    35
     *     dxxxxxxxx    44
     *     dxxxxxxxx    53
     *     dxxxxxxxx    62
     *     dxxxxxxxx    71
     *     dxxxxxxxx    80
     *     dddddddddd
     */
    public void initChess() {
        for (byte i = 0; i < MODEL; i++) {
            // 前10个和后10个均为边界哨兵 // 中间71个位置 8x8 7个哨兵位
            if (i < 10 || i > 80 || i % 9 == 0){
                this.chess[i] = Constant.BOUNDARY;
            }
        }
    }

    /**
     * 初始化棋盘数据
     */
    public void copyChess(Chess[][] chess) {
        // 转换棋盘数据
        BoardUtil.convert(chess,this.chess);
    }

    public int getOurMobility() {
        return ourMobility;
    }

    public void setOurMobility(int ourMobility) {
        this.ourMobility = ourMobility;
    }

    public int getOppMobility() {
        return oppMobility;
    }

    public void setOppMobility(int oppMobility) {
        this.oppMobility = oppMobility;
    }

    public LinkedList<Byte> getOurStators() {
        return ourStators;
    }

    public void setOurStators(LinkedList<Byte> ourStators) {
        this.ourStators = ourStators;
    }

    public LinkedList<Byte> getOppStators() {
        return oppStators;
    }

    public void setOppStators(LinkedList<Byte> oppStators) {
        this.oppStators = oppStators;
    }

    public BoardChess incrementCount(byte player,int count){
        if (player == Constant.WHITE){
            wCount += count;
        }else {
            bCount += count;
        }
        return this;
    }

    public byte getOther() {
        return BoardUtil.change(currMove);
    }

    public byte getwCount() {
        return wCount;
    }

    public void setwCount(byte wCount) {
        this.wCount = wCount;
    }

    public byte getbCount() {
        return bCount;
    }

    public void setbCount(byte bCount) {
        this.bCount = bCount;
    }

    public long getZobrist() {
        return zobrist;
    }

    public void setZobrist(long zobrist) {
        this.zobrist = zobrist;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    /**
     * 更新棋局状态 非实时
     */
    public void updateStatus() {
        if (ourMobility == 0 && oppMobility == 0){
            status = GameStatus.END;
        }else {
            int empty = this.empty.size();
            if (empty == 0) {
                status = GameStatus.END;
            }else if (empty > OPENING) {
                status = GameStatus.OPENING;
            }else if (empty > MIDDLE){
                status = GameStatus.MIDDLE;
            }else {
                status = GameStatus.OUTCOME;
            }
        }
    }
}