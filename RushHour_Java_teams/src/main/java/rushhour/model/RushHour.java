package rushhour.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rushhour.view.VehicleColor;

/**
 * 该类表示Rush Hour游戏。游戏的目标是通过移动棋盘上的车辆，尝试让红色车辆通过唯一的出口离开。
 *
 * 车辆可以水平或垂直排列，只能沿着它们的朝向移动（即：垂直=上/下移动，水平=左/右移动）。
 *
 * @ModelClass 实现游戏规则并维护游戏状态的主要类。
 * @FinalStaticStates BOARD_DIM（int），RED_SYMBOL（char），EMPTY_SYMBOL（char），EXIT_POS（Position）
 *
 * 作者：Daphne
 * 作者：Lennard
 * 作者：Emma
 */
public class RushHour {
    /* 根据UML：用于Rush Hour游戏的静态和最终状态 */
    // 保存棋盘的尺寸（例如：6行x6列）
    public static final int BOARD_DIM = 6;
    // 保存红色车辆的符号标识符
    public static final char RED_SYMBOL = 'R';
    // 保存用于标识棋盘上空格的符号
    public static final char EMPTY_SYMBOL = '-';
    // 保存唯一出口位置的坐标
    public static final Position EXIT_POS = new Position(2, 5);

    /* 在构建类时添加的状态 */
    // 记录游戏中所做移动的次数
    private int moveCount;
    // 保存游戏板的数据结构（2D数组）
    private Object[][] gameBoard;
    // 保存在棋盘上创建的车辆及其位置的数据结构（Map）
    private HashMap<Character, Vehicle> vehiclesOnBoard;

    // 用于JavaFX GUI的观察者
    private RushHourObserver observer;

    /** RushHour对象的构造函数（即：游戏板）
     * 解析CSV文件以构造RushHour对象（2D数组游戏板），其中各种车辆位于给定CSV文件中的模式位置。
     * @param filename CSV游戏板文件的文件名（字符串）
     * @throws IOException
     *
     * 作者：Lennard
     * 已测试
     */
    public RushHour(String filename) throws IOException {
        // 创建2D数组 'board' - 6x6网格
        gameBoard = new Object[6][6];

        // 创建存储车辆对象及其位置的映射
        vehiclesOnBoard = new HashMap<>();

        // 设置文件IO
        FileReader fileReader = new FileReader(filename);
        BufferedReader reader = new BufferedReader(fileReader);

        // 遍历文件中的每一行
        String line;
        while ((line = reader.readLine()) != null) {
            // >>line格式：vehicle_symbol,back_row,back_col,front_row,front_col<<

            // 在','上标记行
            String[] lineBundle = line.split(",");
            // tokens[0] = 车辆符号 <- 使用 token[0].charAt(0) 转换为字符
            Character vehicleSymbol = lineBundle[0].charAt(0);
            // tokens[1] = 后排 <- 使用 Integer.valueOf(tokens[1])
            int backRow = Integer.valueOf(lineBundle[1]);
            // tokens[2] = 后列
            int backCol = Integer.valueOf(lineBundle[2]);
            // tokens[3] = 前排
            int frontRow = Integer.valueOf(lineBundle[3]);
            // tokens[4] = 前列
            int frontCol = Integer.valueOf(lineBundle[4]);

            // 创建前后Position对象
            Position frontPosition = new Position(frontRow, frontCol);
            Position backPosition = new Position(backRow, backCol);
            // 为每一行创建车辆对象：符号、后位置、前位置
            Vehicle vehicle = new Vehicle(vehicleSymbol, backPosition, frontPosition);
            // 将车辆添加到映射（字符/车辆对象对）
            vehiclesOnBoard.put(vehicleSymbol, vehicle);
        }
        // 将车辆符号（字符）添加到板上适当的位置
        updateBoard(vehiclesOnBoard);

        reader.close();
        // 结束：所有车辆已创建，映射包含车辆信息，2D数组有车辆字符和空格为null
    }




    /**
     * RushHourConfiguration的深拷贝构造函数
     * @param previous 先前的RushHour对象
     *
     * 作者：Emma
     * 由Daphne重构 - 没有对对象进行深层复制
     * 已测试
     */
    public RushHour(RushHour previous) {
        this.gameBoard = Arrays.copyOf(previous.gameBoard, 6);
        this.moveCount = previous.moveCount;
        this.vehiclesOnBoard = new HashMap<Character, Vehicle>();
        for (Character key : previous.getVehiclesOnBoard().keySet()) {
            Vehicle addVehicle = new Vehicle(previous.getVehiclesOnBoard().get(key));
            vehiclesOnBoard.put(key, addVehicle);
        }
    }

    /**
     * 用于在每次移动车辆时更新2D 'board'的辅助方法
     * @param vehicleMap (HashMap)
     *
     * 作者：Lennard
     * 已测试
     */
    public void updateBoard(HashMap<Character, Vehicle> vehicleMap) {
        // 清空地图 >:]
        gameBoard = new Object[6][6];

        // 遍历vehicleMap中的每个条目
        for (Map.Entry<Character, Vehicle> entry : vehicleMap.entrySet()) {
            // 从映射中提取车辆
            Vehicle vehicle = entry.getValue();
            // 填充前后位置
            gameBoard[vehicle.getFront().getRow()][vehicle.getFront().getCol()] = vehicle.getSymbol();
            gameBoard[vehicle.getBack().getRow()][vehicle.getBack().getCol()] = vehicle.getSymbol();

            if (vehicle.isVertical()) {
                // 如果车辆长度大于2
                if (vehicle.getBack().getRow() < vehicle.getFront().getRow()-1) {
                    // 填充前排位置 - 1
                    gameBoard[vehicle.getFront().getRow()-1][vehicle.getFront().getCol()] = vehicle.getSymbol();
                }
            } else { // 车辆是水平的:
                // 如果车辆长度大于2
                if (vehicle.getBack().getCol() < vehicle.getFront().getCol()-1) {
                    // 填充前列位置 - 1
                    gameBoard[vehicle.getFront().getRow()][vehicle.getFront().getCol()-1] = vehicle.getSymbol();
                }
            }
        }
    }

    /** 尝试根据给定的Move信息移动车辆1格。
     * @MoveValidity:
     *      >> 给定了无效的车辆符号
     *      >> 车辆将移出游戏板
     *      >> 新位置上已经有另一辆车辆
     *      >> 水平车辆只能向左或向右移动（在Vehicle类中检查）
     *      >> 垂直车辆只能向上或向下移动（在Vehicle类中检查）
     * 注意：如果车辆方向是垂直的（上/下）=向下；水平的（L/R）=向右
     * @param move (Move对象)，提供了移动车辆的标识符（char）和方向（枚举）
     * @throws RushHourException 如果给定的Move无效
     *
     * 作者：Daphne
     * 已测试
     */
    public void moveVehicle(Move move) throws RushHourException {
        // 从move中提取：车辆符号和方向
        char vehicleSymbol = move.getSymbol();
        Direction moveDirection = move.getDir();

        // 获取用于在打印异常消息时使用的车辆颜色字符串
        String vehicleColorString = VehicleColor.valueOf(String.format("%c", vehicleSymbol)).toString();

        // 从映射中获取车辆对象
        Vehicle vehicleToMove = vehiclesOnBoard.get(vehicleSymbol);
        if (vehicleToMove == null) {
            // 车辆符号（char）不在映射中 >> 抛出异常 - "车辆不在板上"
            throw new RushHourException("无效的移动：车辆 " + vehicleColorString + " 不在板上。\n");
        }

        // 检查车辆是否具有给定移动方向的正确方向
        // 如果车辆方向是垂直的（上/下）=向下；水平的（L/R）=向右
        if ((moveDirection == Direction.UP || moveDirection == Direction.DOWN) && vehicleToMove.isHorizontal()) {
            // 无效的方向 >> 抛出异常 - "车辆是水平的，不能上下移动"
            throw new RushHourException("无效的移动：车辆是水平的，不能移动 " + moveDirection + "。\n");
        }
        else if ((moveDirection == Direction.RIGHT || moveDirection == Direction.LEFT) && vehicleToMove.isVertical()) {
            // 无效的方向 >> 抛出异常 - "车辆是垂直的，不能左右移动"
            throw new RushHourException("无效的移动：车辆是垂直的，不能移动 " + moveDirection + "。\n");
        }

        /*根据提供的方向获取潜在移动的坐标
            如果向上 >> 评估车辆后面的空间
            如果向下 >> 评估车辆前面的空间
            如果向左 >> 评估车辆后面的空间
            如果向右 >> 评估车辆前面的空间*/
        Position futurePosition = null;
        switch (moveDirection) {
            case UP:
                futurePosition = vehicleToMove.spaceInBack();
                break;
            case DOWN:
                futurePosition = vehicleToMove.spaceInFront();
                break;
            case LEFT:
                futurePosition = vehicleToMove.spaceInBack();
                break;
            case RIGHT:
                futurePosition = vehicleToMove.spaceInFront();
                break;
        }
        int newRow = futurePosition.getRow();
        int newCol = futurePosition.getCol();

        // 检查车辆是否将移出板外（即：如果newRow或newCol在0-5索引之外）
        if (newRow < 0 || 5 < newRow || newCol < 0 || 5 < newCol) {
            // 无效的索引 >> 抛出异常 - "车辆在板的边缘"
            throw new RushHourException("无效的移动：车辆 " + vehicleColorString + " 处于板的边缘。\n");
        }

        // 检查是否有空格：获取板上空间的值，空 = null
        Object moveSpace = gameBoard[newRow][newCol];
        if (moveSpace != null) {
            // 空间不为空（即：已经有车辆了） >> 抛出异常 - "那里已经有一辆车了"
            // 获取在路上的车辆的字符串，将在打印异常消息时使用
            String obstructingVehicle = VehicleColor.valueOf(String.format("%c", moveSpace)).toString();
            throw new RushHourException("无效的移动：另一辆车辆，" + obstructingVehicle + "，挡住了去路！\n");
        }

        // 初步检查通过 >> 调用Vehicle.move() 进行最终检查和移动车辆
        // Vehicle.move()检查车辆是否是正确方向，并在有效时进行移动
        vehicleToMove.move(moveDirection);
        // 再次检查车辆的位置，看是否与计算的未来位置相匹配（如果相等，表示移动已完成）
        if (vehicleToMove.getBack().equals(futurePosition) || vehicleToMove.getFront().equals(futurePosition)) {
            // 移动已完成，增加moveCount
            this.moveCount += 1;
            updateBoard(vehiclesOnBoard);

            // DEBUG修复：如果观察者为null，无法调用观察者（即：仅从CLI播放）
            if (observer != null) {
                // 通知观察者车辆已移动
                notifyObserver(vehicleToMove);
            }
        } else {
            throw new RushHourException("无效的移动：移动逻辑失败");
        }
    }



    /**
     * 此类用于判断游戏是否结束。当红色车辆的前部占据EXIT_POSITION时，游戏结束。
     * @return 如果红色车辆在EXIT_POSITION上，则返回true，否则返回false
     *
     * 作者：Daphne
     * 已测试
     */
    public boolean isGameOver() {
        // 如果不是EMPTY_SYMBOL，获取位于游戏板EXIT_POS位置的车辆
        if (gameBoard[2][5] != null) {
            char exitSpaceVehicle = (char) gameBoard[2][5];
            // 如果空间中的符号是'R' = 红色车辆应该在那个空间
            if (exitSpaceVehicle == RED_SYMBOL) {
                Vehicle exitVehicle = vehiclesOnBoard.get(exitSpaceVehicle);
                // 确认红色车辆的前部是否在EXIT_POSITION上，返回true
                return (exitVehicle.getFront().equals(EXIT_POS));
            } else {
                // 位于出口空间的车辆不是红色车辆 - 游戏未结束，返回false
                return false;
            }
        } else {
            // EXIT_POS为空 - 游戏未结束，返回false
            return false;
        }
    }

    /**
     * 此方法为玩家提供在调用时任何车辆都可以进行的有效移动。
     * @return possibleMoves (Collection<Move>) 可以进行的有效移动的集合
     *
     * 作者：Daphne
     * //Emma 完成 - Daphne 完成
     * 已测试
     */
    public List<Move> getPossibleMoves() {
        // 创建一个possibleMoves Map<Character, Collection<Move>>（以车辆符号为键，以有效Move对象列表为值）
        List<Move> possibleMoves = new LinkedList<>();

        // 遍历车辆映射，获取每辆车前后坐标中的所有坐标
        for (Object vehicleChar : vehiclesOnBoard.keySet().toArray()) {
            // 从映射中提取车辆
            Vehicle vehicle = vehiclesOnBoard.get(vehicleChar);

            // 检查车辆后面的空间：是否越界？或者车辆后面的空间是否为null？
            Position spaceBehind = vehicle.spaceInBack();
            if ((0 <= spaceBehind.getRow() && spaceBehind.getRow() < 6)
                    && (0 <= spaceBehind.getCol() && spaceBehind.getCol() < 6)
                    && gameBoard[spaceBehind.getRow()][spaceBehind.getCol()] == null) {
                // 找到一个有效的移动。检查车辆的方向并将Move添加到移动列表
                if (vehicle.isHorizontal()) {
                    // 车辆是水平的，向后移动 = 左
                    Move validMoveBack = new Move((char) vehicleChar, Direction.LEFT);
                    possibleMoves.add(validMoveBack);
                } else {
                    // 车辆是垂直的，向后移动 = 上
                    Move validMoveBack = new Move((char) vehicleChar, Direction.UP);
                    possibleMoves.add(validMoveBack);
                }
            }
            // 检查车辆前面的空间：是否越界？或者车辆前面的空间是否为null？
            Position spaceInFront = vehicle.spaceInFront();
            if ((0 <= spaceInFront.getRow() && spaceInFront.getRow() < 6)
                    && (0 <= spaceInFront.getCol() && spaceInFront.getCol() < 6)
                    && gameBoard[spaceInFront.getRow()][spaceInFront.getCol()] == null) {
                // 找到一个有效的移动。检查车辆的方向并将Move添加到移动列表
                if (vehicle.isHorizontal()) {
                    // 车辆是水平的，向前移动 = 右
                    Move validMoveForward = new Move((char) vehicleChar, Direction.RIGHT);
                    possibleMoves.add(validMoveForward);
                } else {
                    // 车辆是垂直的，向前移动 = 下
                    Move validMoveForward = new Move((char) vehicleChar, Direction.DOWN);
                    possibleMoves.add(validMoveForward);
                }
            }
        }
        return possibleMoves;
    }
    /**
     * 获取moveCount字段的访问器
     * @return moveCount（整数）到目前为止游戏的移动次数
     * 已测试
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * 获取vehiclesOnBoard字段的访问器
     * @return vehiclesOnBoard的映射
     * 已测试
     */
    public HashMap<Character, Vehicle> getVehiclesOnBoard() {
        return this.vehiclesOnBoard;
    }

    /**
     * 获取gameBoard 2D数组的访问器
     * @return Object[][]
     * 已测试
     */
    public Object[][] getGameBoard() {
        return this.gameBoard;
    }

    /**
     * 此方法为GUI模型中要进行的更改分配具体的观察者。
     * @param observer：观察游戏板的观察者接口
     *
     * 作者：Emma
     * 未测试
     */
    public void registerObserver(RushHourObserver observer) {
        this.observer = observer;
    }

    /**
     * 此方法调用具体的观察者，并启动在模型中进行的更改的处理，
     * 以在GUI中反映这些更改。
     * @param vehicle：在游戏板上移动的车辆
     *
     * 作者：Emma
     * 未测试
     */
    private void notifyObserver(Vehicle vehicle) {
        this.observer.vehicleMoved(new Vehicle(vehicle));
    }

    /**
     * 此方法创建当前游戏板上所有车辆的深拷贝
     * @return vehicles：包含车辆副本的集合
     *
     * 作者：Emma
     * //TODO Emma测试
     */
    public Collection<Vehicle> getVehicles() {
        HashSet<Vehicle> vehicles = new HashSet<>();
        for (Map.Entry<Character, Vehicle> entry : this.vehiclesOnBoard.entrySet()) {
            vehicles.add(new Vehicle(entry.getValue()));
        }
        return vehicles;
    }

    /**
     * 此方法将moveCount状态设置为0（在RushHourGUI中使用）
     *
     * 作者：Emma
     * //TODO Emma测试
     */
    public void resetMoveCount() {
        this.moveCount = 0;
    }

    /**
     * 提供RushHour游戏板的字符串表示
     * @return 2D数组 'board' 的字符串表示
     *
     * 作者：Lennard
     * 已测试
     */
    @Override
    public String toString() {
        // 板上的每个字符应该是RushHour.EMPTY_SPACE
        // 或表示车辆符号的字母，包括RED_SYMBOL。

        String boardRepr = "";
        int rowTracker = 0;
        // 遍历行
        for (Object[] row : gameBoard) {
            // 遍历行中的单元格
            for (Object cell : row) {
                if (cell == null) {
                    boardRepr += EMPTY_SYMBOL;
                } else {
                    boardRepr += String.valueOf(cell);
                }
            }
            if (rowTracker == 2) {
                boardRepr += "< EXIT";
            }
            // 每行结束后换行
            boardRepr += "\n";
            rowTracker++;
        }
        return boardRepr;
    }

    /**
     * 根据每个对象的字符串表示比较两个RushHour对象是否相等
     *
     * 作者：Lennard
     * 已由Daphne重新编写 - 比较字符串
     * 已测试
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof RushHour) {
            RushHour other = (RushHour) o;
            return this.toString().equals(other.toString());
        }
        return false;
    }

}

