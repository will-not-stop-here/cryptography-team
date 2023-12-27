package rushhour.model;

/**
 * 为RushHour游戏创建的自定义异常。当抛出异常时，它提供一个自定义消息。
 *
 * 已测试 - 与RushHour（类）的无效移动测试一起使用
 */
public class RushHourException extends Exception {
    /**
     * RushHourException被抛出时的构造函数
     * @param message (String) - 抛出异常时显示的自定义消息
     */
    public RushHourException(String message) {
        super(message);
    }

}
