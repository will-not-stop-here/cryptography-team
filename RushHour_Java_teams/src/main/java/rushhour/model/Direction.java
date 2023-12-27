package rushhour.model;

/**
 * 枚举类，包含不同方向类型，用于车辆移动。
 * 水平车辆只能左右移动。
 * 垂直车辆只能上下移动。
 *
 * @作者 Lennard
 */
public enum Direction {
    UP("上"),
    RIGHT("右"),
    DOWN("下"),
    LEFT("左");

    private String name;

    private Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
