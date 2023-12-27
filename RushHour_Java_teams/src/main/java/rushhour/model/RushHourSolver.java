package rushhour.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import backtracker.Backtracker;
import backtracker.Configuration;

/**
 * 该类表示Rush Hour游戏板的配置，用于使用Backtracking算法尝试解决游戏。
 *
 * @states board（RushHour），configurationsMade（Collection）和movesMade（List）
 *
 * 作者：Emma - 为构造函数、getSuccessors、isGoal和toString设置了基础代码
 * 重构作为团队合作完成
 */
public class RushHourSolver implements Configuration<RushHourSolver> {
    /* 用于此配置的当前板（及其状态） */
    protected RushHour board;
    /* 一个包含先前此当前配置的配置的集合 */
    protected Set<RushHourSolver> configurationsMade;
    /* 先前配置所做的移动列表 */
    protected List<Move> movesMade;

    public RushHourSolver(RushHour board, Set<RushHourSolver> configurationsMade, List<Move> movesMade) {
        this.board = board;
        this.configurationsMade = configurationsMade;
        this.movesMade = movesMade;
    }

    /**
     * 重载的类构造函数
     * 从给定的RushHour对象创建RushHourSolver（Configuration）的实例
     *
     * @param board（RushHour）
     */
    public RushHourSolver(RushHour board) {
        this.board = board;
        this.configurationsMade = new HashSet<>();
        this.movesMade = new ArrayList<>();
    }

    /**
     * 此方法生成当前配置的后继配置。每个后继包含一个有效的可能下一步。
     *
     * @refactored by Daphne - 添加了movesMade列表的深拷贝，否则它会将一个旧的移动列表传递给每个新的后继
     * @refactored group effort - 搞定了！
     */
    @Override
    public Collection<RushHourSolver> getSuccessors() {
        HashSet<RushHourSolver> successors = new HashSet<>();

        for (Move possibleMove : this.board.getPossibleMoves()) {
            RushHour possible = new RushHour(this.board);
            try {
                possible.moveVehicle(possibleMove);
            } catch (RushHourException e) {
                // 捕获实际上不应该需要，因为getPossibleMoves应该只提供有效的移动
                continue;
            }

            List<Move> successorMoves = new ArrayList<>();
            successorMoves.addAll(this.movesMade);
            successorMoves.add(possibleMove);
            this.configurationsMade.add(this);
            RushHourSolver successorConfiguration = new RushHourSolver(possible, this.configurationsMade, successorMoves);
            successors.add(successorConfiguration);
        }
        return successors;
    }


    /**
     * 确定当前配置是否为有效配置。
     * 有效配置：
     * >> 不在与红色车辆相同行的位置上有另一辆水平车辆
     * >> 不重复之前看过的配置
     *
     * @return 如果有效则为true，否则为false
     * <p>
     * 作者：团队合作 - 搞定了！
     */
    @Override
    public boolean isValid() {
        return !configurationsMade.contains(this);
    }

    /**
     * 辅助方法，检查游戏板是否可解，应在调用 solve() 方法之前在 CLI 和 GUI 中调用。参见 CLI 中的第 107 行示例。
     *
     * @param board
     * @return 布尔值
     * <p>
     * 作者：Daphne
     * 重构：Lennard
     */
    public static boolean solvable(RushHour board) {
        // 如果与红色车辆相同行的位置上有另一辆水平车辆，则返回false
        HashMap<Character, Vehicle> checkBoard = board.getVehiclesOnBoard();
        Vehicle REDvehicle = checkBoard.get('R');
        int RedVehicleRow = REDvehicle.getBack().getRow();
        int RedVehicleFrontCol = REDvehicle.getFront().getCol();
        for (Object key : checkBoard.keySet()) {
            if ((Character) key != 'R') {
                Vehicle checkVehicle = checkBoard.get(key);
                if ((checkVehicle.isHorizontal())
                        && (checkVehicle.getBack().getRow() == RedVehicleRow)
                        && (checkVehicle.getBack().getCol() > RedVehicleFrontCol)
                ) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 确定当前配置是否为目标解决方案。
     *
     * @return 如果是目标解决方案则为true，否则为false
     * <p>
     * 作者：Lennard
     */
    @Override
    public boolean isGoal() {
        return board.isGameOver();
    }

    /**
     * RushHour配置的字符串表示，包括配置中到目前为止的所有移动以及当前游戏板的当前状态
     * 作者：Emma
     * 重构：Daphne
     */
    @Override
    public String toString() {
        String print = String.format("Moves: %1$s\n Board:\n%2$s", this.movesMade.toString(), this.board.toString());
        return print;
    }

    // 配置状态的访问器
    public Collection<RushHourSolver> getConfigurationsMade() {
        return configurationsMade;
    }

    public List<Move> getMovesMade() {
        return movesMade;
    }

    public RushHour getBoard() {
        return this.board;
    }

    /**
     * 给定RushHour游戏板，返回一个获胜的配置，或者返回null
     *
     * @param game
     * @return 获胜配置的RushHourConifguration，或者null
     * 作者：Lennard
     */
    public static RushHourSolver solve(RushHour game) {
        RushHourSolver configuration = new RushHourSolver(new RushHour(game));
        Backtracker<RushHourSolver> backtracker = new Backtracker<>(false);
        RushHourSolver winningConfig = backtracker.solve(configuration);
        return winningConfig;
    }

    /**
     * 根据其板状态的字符串表示比较两个RushHourConfigurations实例的相等性。
     * （即：检查板上的车辆是否处于相同的位置。）
     *
     * @return 如果板的状态匹配则为true，否则为false
     * 作者：团队合作
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof RushHourSolver) {
            RushHourSolver other = (RushHourSolver) o;
            return this.board.toString().equals(other.getBoard().toString());
        } else {
            return false;
        }
    }

    /**
     * 生成由configurationsMade（HashSet）使用的hashCode
     * 作者：Daphne
     * 重构：Lennard
     */
    @Override
    public int hashCode() {
        return this.board.toString().hashCode();
    }
}
