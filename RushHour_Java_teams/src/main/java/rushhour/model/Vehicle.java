package rushhour.model;

/**
 * 该类表示Rush Hour游戏板上的车辆。
 * @states symbol（char），back（Position）和front（Position）
 *
 * 作者：Daphne
 */
public class Vehicle {
    // 用于特定Vehicle对象的符号标识符
    private char symbol;
    // 保存车辆在板上后方的位置
    private Position back;
    // 保存车辆在板上前方的位置
    private Position front;

    /**
     * 类的构造函数
     * @param @车辆符号（char）
     * @param @车辆后方位置（Position）
     * @param @车辆前方位置（Position）
     *
     * 已测试
     */
    public Vehicle(char symbol, Position back, Position front) {
        this.symbol = symbol;
        this.back = back;
        this.front = front;
    }

    /** 类的拷贝构造函数
     * 该方法从另一个实例中初始化一个新的车辆（复制）
     * @param other：要复制的车辆
     *
     * 重构：Daphne - 没有实际复制对象
     * 已测试
     */
    public Vehicle(Vehicle other) {
        this.symbol = other.getSymbol();
        this.back = new Position(other.getBack().getRow(), other.getBack().getCol());
        this.front = new Position(other.getFront().getRow(), other.getFront().getCol());
    }

    /**
     * 该方法将车辆向给定方向移动1格，如果是有效的移动。
     * @MoveValidity：
     *      >> 水平车辆只能左右移动。
     *      >> 垂直车辆只能上下移动。
     *      >> 新位置上没有另一辆车（根据Rush Hour类）
     *      >> 车辆不会移动到网格之外（根据Rush Hour类）
     *      >> 提供了无效的车辆符号（根据Rush Hour类）
     * @param dir（Direction）- 尝试移动的方向
     * @throws RushHourException
     *
     * 作者：Daphne
     * 已测试
     */
    public void move(Direction dir) throws RushHourException {
        // 在Rush Hour主类中检查移动的有效性
        // 将车辆移动1格
        switch (dir) {
            case UP:
                this.back = new Position(this.back.getRow() - 1, this.back.getCol());
                this.front = new Position(this.front.getRow() - 1, this.front.getCol());
                break;
            case DOWN:
                this.back = new Position(this.back.getRow() + 1, this.back.getCol());
                this.front = new Position(this.front.getRow() + 1, this.front.getCol());
                break;
            case RIGHT:
                this.back = new Position(this.back.getRow(), this.back.getCol() + 1);
                this.front = new Position(this.front.getRow(), this.front.getCol() + 1);
                break;
            case LEFT:
                this.back = new Position(this.back.getRow(), this.back.getCol() - 1);
                this.front = new Position(this.front.getRow(), this.front.getCol() - 1);
        }
    }


    /**
     * 识别车辆在游戏板上是否水平。如果车辆的后方位置（back Position）更靠近板的左侧，则车辆是水平的。
     * @return 如果水平则为true，否则为false
     *
     * 作者：Daphne
     * 已测试
     */
    public boolean isHorizontal() {
        return (this.back.getRow() == this.front.getRow() && this.back.getCol() < this.front.getCol());
    }

    /**
     * 识别车辆在游戏板上是否垂直。如果车辆的后方位置（back Position）更靠近板的顶部，则车辆是垂直的。
     * @return 如果垂直则为true，否则为false
     *
     * 作者：Daphne
     * 已测试
     */
    public boolean isVertical() {
        return (this.back.getRow() < this.front.getRow() && this.back.getCol() == this.front.getCol());
    }

    /**
     * 返回车辆前方的潜在空间
     * @return 具有车辆前方空间坐标的Position
     *
     * 作者：Daphne
     * 已测试
     */
    public Position spaceInFront() {
        Position spaceInFront;
        if (isHorizontal()) {
            // 车辆朝右 - 前方空间 = (行, 列+1)
            spaceInFront = new Position(this.front.getRow(), this.front.getCol() + 1);
        } else {
            // 车辆垂直，朝下 - 前方空间 = (行+1, 列)
            spaceInFront = new Position(this.front.getRow() + 1, this.front.getCol());
        }
        return spaceInFront;
    }

    /**
     * 返回车辆后方的潜在空间
     * @return 具有车辆后方空间坐标的Position
     *
     * 作者：Daphne
     * 已测试
     */
    public Position spaceInBack() {
        Position spaceInBack;
        if (isHorizontal()) {
            // 车辆朝右 - 后方空间 = (行, 列-1)
            spaceInBack = new Position(this.back.getRow(), this.back.getCol() - 1);
        } else {
            // 车辆垂直，朝下 - 后方空间 = (行-1, 列)
            spaceInBack = new Position(this.back.getRow() - 1, this.back.getCol());
        }
        return spaceInBack;
    }

// 类字段的访问器
    /** 已使用构造函数测试 */
    public char getSymbol() {
        return symbol;
    }
    public Position getBack() {
        return back;
    }
    public Position getFront() {
        return front;
    }

    /**
     * 返回车辆对象的字符串表示
     * @return 车辆对象的字符串表示
     *
     * 作者：Lennard
     * 已测试
     */
    @Override
    public String toString() {
        return "[Vehicle: " + this.symbol + "]";
    }
}
