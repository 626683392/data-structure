package arithmetic.evaluation;

import bean.BoardChess;
import bean.WeightIndividual;
import common.GameStatus;
import common.WeightEnum;
import game.GameRule;

/**
 * @author ypt
 * @ClassName ReversiEvaluation
 * @Description 估值方法
 * @date 2019/5/8 13:58
 */
public class ReversiEvaluation {

    private static Evaltion evaltion = new CalculationEvaltion();

    private EvaluationWeight evaluationWeight;

    /**
     * 传入基因编码
     * @param individual
     */
    public ReversiEvaluation(WeightIndividual individual){
        this.evaluationWeight = new EvaluationWeight(individual);
    }

    /**
     * 估值函数
     *
     * @param data
     * @return
     */
    public int currentValue(BoardChess data) {
        // 更新棋局状态
        GameRule.valid_moves(data);
        // 更新状态
        data.updateStatus();
        // 计算内部子
        GameRule.sum_inners_frontiers(data);
        int score = 0;
        GameStatus status = data.getStatus();
        for (WeightEnum weightEnum : WeightEnum.values()) {
            float weight = this.evaluationWeight.getWeight(status, weightEnum);
            if (weight == 0){
                continue;
            }
            score += evaltion.weightScore(weightEnum,weight,data);
        }
        return score;
    }

}
