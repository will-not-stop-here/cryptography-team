package rushhour.model;

/**
 * 该类指定了哪辆车（通过符号）在何方向上移动。
 * 所有移动都是一格。
 */

public class Move {
    // 存储正在移动的车辆的符号
    private char symbol;
    // 存储车辆移动的方向
    private Direction dir;

    // 类的构造方法
    public Move(char symbol, Direction dir) {
        this.symbol = symbol;
        this.dir = dir;
    }

    // 字段的访问器方法
    public char getSymbol() {
        return symbol;
    }

    public Direction getDir() {
        return dir;
    }

    /**
     * 获取表示移动对象的字符串表示，包括符号和方向。
     * @return 移动对象的字符串表示
     */
    @Override
    public String toString() {
        return "[车辆: " + symbol + "，方向: " + dir + "]";
    }
}
