package utils;

import bean.BoardData;
import common.Constant;
import common.ImageConstant;
import game.Chess;
import game.GameContext;

import java.awt.Image;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static common.Constant.DELAY;
import static common.Constant.SIZE;

/**
 * @author Tao
 */
public class BoardUtil {

	/**
	 * 处理流
	 *  判断当前是否处理完成
	 */
	private static final Map chessQueue = new ConcurrentHashMap();


	/**
	 * 棋子动画
	 * 	//转变棋子动画
	 *
	 * 		1 --- 6
	 *     白     黑
	 * @param chess
	 * @param //repaint
	 */
	public static void converSion(byte chess, List<Chess> curr){
		Timer timer = new Timer();
		//根据传参的正负判断转变的棋子方向 6 -> 1 表示黑变白
		int tem = chess == Constant.WHITE ? 6 : 1;
		TimerRunTask task = new TimerRunTask(tem,chess,curr);
		timer.schedule(task,0,DELAY);
		chessQueue.put(curr,Constant.START);
	}

	/**
	 * 反转棋子任务
	 */
	public static class TimerRunTask extends TimerTask{
		private int count;
		private byte chess;
		private List<Chess> curr;

		public TimerRunTask(int count, byte chess, List<Chess> curr) {
			this.count = count;
			this.chess = chess;
			this.curr = curr;
		}

		@Override
		public void run() {
			if(count > 0 && count <= 6){
				updateImg(count,curr);
				if(chess == Constant.WHITE) count--;
				else count++;
			}else{
				//结束任务
				cancel();
				//修正图标
				fixImg(chess,curr);
				// 标识结束
				chessQueue.put(curr,Constant.END);
			}
		}
	}

	/**
	 * 是否正在执行转变
	 *
	 * 	如果是则阻塞
	 * @param curr
	 * @return
	 */
	public static boolean isRun(List<Chess> curr){
		while (Constant.START.equals(chessQueue.get(curr))){
			GameContext.sleep(10);
		}
		return Constant.END.equals(chessQueue.get(curr));
	}

	/**
	 * 设置翻转图片
	 * @param count
	 * @param chessList
	 */
	private static void updateImg(int count, List<Chess> chessList) {
		for (Chess curr : chessList) {
			String url = String.format(Constant.OVERTURN, count + "");
			Image image = GameContext.getResources().get(ImageConstant.valueOf(url)).getImage();
			curr.setImage(image);
			curr.repaint();
		}
	}
	/**
	 * 修正图标
	 * @param chess
	 * @param chessList
	 */
	private static void fixImg(byte chess, List<Chess> chessList) {
		for (Chess curr : chessList) {
			curr.setChess(chess);
			curr.repaint();
		}
	}

	/**
	 *  /控制台显示棋盘
	 */
	public static void display(BoardData data){
		System.out.println("===================chess==================");

		Chess[][] chess = data.getChess();
		boolean[][] moves = data.getMoves();
		char col_label = 'a';
		//打印第一行的a-z字母标识
		byte col = 0,row=0;
		System.out.print("  ");
		for(col = 0;col<SIZE;++col)
			System.out.printf("   %c",(char)(col_label+col));
		System.out.println();
		//打印棋盘
		for(row=0;row<SIZE;++row){
			System.out.printf("   +");
			col = (byte) SIZE;
			while(col>0){
				System.out.printf("---+");--col;
			}
			//打印第一列【1-SIZE】的值
			System.out.printf("\n%2d |",row+1);
			for(col = 0;col<SIZE;++col){
				if(moves[row][col] == false){
					System.out.printf(" %d |",chess[row][col].getChess());
				}else
					System.out.print(" . |");
			}
			System.out.println();
		}
		System.out.printf("   +");
		for(col=0;col<SIZE;++col)
			System.out.printf("---+");
		System.out.println();
	}

	/**
	 * 改变棋盘方向
	 * @param player
	 * @return
	 */
	public static byte change(byte player){
		if(player==Constant.WHITE){
			return Constant.BLACK;
		}else if(player==Constant.BLACK){
			return Constant.WHITE;
		}else if(player==Constant.DOT_W){
			return Constant.DOT_B;
		}else if(player==Constant.DOT_B){
			return Constant.DOT_W;
		}
		return Constant.EMPTY;
	}

	/**
	 * 深度拷贝棋盘
	 * @param data
	 * @return
	 */
	public static BoardData copyBoard(BoardData data) {
		return data.cloneData();
	}
}
