package game;


import common.Constant;
import common.ImageConstant;
import utils.BoardUtil;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;

/**
 * @author Tao
 */
public class Chess extends JPanel {
    /**
     * 棋子状态
     */
    private byte chess;
    /**
     * 图标
     */
    private Image image;

    /**
     *  /设置棋子状态
     */
    public Chess(byte chess) {
        this.setChess(chess);
    }


    /**
     * //设置棋子状态
     * @param chess
     */
    public void setChess(byte chess) {
        Map<ImageConstant, ImageIcon> imageIconMap = GameContext.getResources();
        switch (chess){
            case Constant.WHITE:
                this.image = imageIconMap.get(ImageConstant.PLAYER_W).getImage();
                break;
            case Constant.BLACK:
                this.image = imageIconMap.get(ImageConstant.PLAYER_B).getImage();
                break;
            case Constant.DOT_W:
                this.image = imageIconMap.get(ImageConstant.CANMOVE_W).getImage();
                break;
            case Constant.DOT_B:
                this.image = imageIconMap.get(ImageConstant.CANMOVE_B).getImage();
                break;
            case Constant.EMPTY:
                this.image = imageIconMap.get(ImageConstant.EMPTY).getImage();
                break;
            default:break;
        }
        this.chess = chess;
    }

    /**
     * 设置新走棋状态
     * @param chess
     */
    public void setNewPlayer(byte chess){
        Map<ImageConstant, ImageIcon> imageIconMap = GameContext.getResources();
        if(chess == Constant.WHITE){
            this.image = imageIconMap.get(ImageConstant.NWHITE).getImage();
        }else if(chess == Constant.BLACK){
            this.image = imageIconMap.get(ImageConstant.NBLACK).getImage();
        }
        this.chess = chess;
    }

    public byte getChess() {
        return chess;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
        g.drawImage(image, 0, 0,Constant.ROW, Constant.COL, this);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Chess{" +
                "chess=" + chess +
                ", image=" + image +
                '}';
    }

    /**
     * //转变棋子动画
     * @param player
     */
    public void change(byte player) {
        BoardUtil.converSion(player,this);
        //修正图标
        this.setChess(player);
    }

    @Override
    public void repaint() {
        super.repaint();
    }
}
