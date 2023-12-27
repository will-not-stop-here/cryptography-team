package rushhour.view;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import backtracker.Backtracker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourSolver;
import rushhour.model.RushHourException;
import rushhour.model.RushHourObserver;
import rushhour.model.Vehicle;

/**
 RushHour 小组项目
 @author Daphne 构建了GUI的基本结构以及解决方案的实现
 @author Emma 和 Lennard 实现了提供GUI功能和变更的方法
 @GUI 该GUI允许用户玩RushHour游戏
 * 
 */
@SuppressWarnings("unused")
public class RushHourGUI extends Application implements RushHourObserver {
    private BoardLevel gameLevel;
    private int gameLevelNum;
    private String levelFilename;
    // 创建 RushHour 游戏板对象
    private RushHour board;

    // 设置通用字体大小
    private int fontsize = 15;
    // 设置游戏板方块的最小大小
    private int squareMinSize = 110;

    // 创建重置按钮 - 需要在 GUI 外部访问以进行更新
    Button resetButton;
    // 创建解决按钮 - 需要在 GUI 外部访问以进行更新
    Button solveButton;
    // 创建游戏难度级别按钮 - 需要在 GUI 外部访问以进行更新
    Button gameLevelButton;
    // 创建提示按钮 - 需要在 GUI 外部访问以进行更新
    Button hintButton;
    // 创建退出按钮 - 需要在 GUI 外部访问以进行更新
    Button quitButton;
    // 创建游戏状态标签 - 需要在 GUI 外部访问以进行更新
    private Label gameStatus;
    // 创建移动计数标签 - 需要在 GUI 外部访问以进行更新
    private Label moveCountLabel;
    // 创建用作游戏板的节点网格及其访问器
    private GridPane gameBoard;
    public GridPane getGameBoard() { return gameBoard; }
    // 跟踪在 GUI 中创建的车辆对象 - 需要在 GUI 外部访问以进行更新
    private HashMap<Character, Node> vehicleGUIs;

    // 图像常量，用于在车辆前/后按钮中使用
    public static final Image UP_ARROW =
            new Image("file:data/upArrow.png");
    public static final Image DOWN_ARROW =
            new Image("file:data/downArrow.png");
    public static final Image LEFT_ARROW =
            new Image("file:data/leftArrow.png");
    public static final Image RIGHT_ARROW =
            new Image("file:data/rightArrow.png");


    /**********************************************************************/
    /* 常用 GUI 节点的工厂方法 */

    /** 工厂方法：创建用于以下用途的按钮：
     *          -- '显示提示' 按钮，用于显示随机选择的有效移动。
     *          -- '退出' 按钮，用于结束游戏。
     *          -- '重置' 按钮，用于重置整个游戏板。
     * @param buttonLabel（String）：显示在标签上的文本
     * @param @eventHandler 点击按钮时要启动的事件操作
     *
     * 作者：Daphne
     */

    private Button buttonFactory(String buttonLabel, EventHandler<ActionEvent> handler) {
        Button newButton = new Button();
        newButton.setText(buttonLabel);
        newButton.setAlignment(Pos.CENTER);
        newButton.setFont(new Font("ARIAL", fontsize));
        newButton.setPadding(new Insets(5));
        newButton.setTextFill(Color.BLACK);
        newButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // 调用 EventHandler 并在每个按钮上注册具体的观察者
        newButton.setOnAction(handler);
        board.registerObserver(this);
        return newButton;
    }

    /** 工厂方法：显示标签
     * 该方法创建一个标签，用于在游戏进行过程中显示信息
     * @param labelString：在标签中显示的起始文本
     * @return 游戏板显示标签（Label）
     *
     * 作者：Daphne
     */
    private Label makeDisplayLabel(String labelString) {
        Label newLabel = new Label();
        newLabel.setText(labelString);
        newLabel.setAlignment(Pos.CENTER);
        newLabel.setTextAlignment(TextAlignment.CENTER);
        newLabel.setFont(new Font("Arial", fontsize));
        newLabel.setPadding(new Insets(5));
        newLabel.setTextFill(Color.BLACK);
        newLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        newLabel.setBorder(new Border(new BorderStroke(Color.DARKGRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderStroke.THIN)));
        newLabel.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        return newLabel;
    }

    /** 工厂方法：为游戏板 GRID 创建标签
     * 该方法创建一个标签，用于填充游戏板网格
     * @param labelString（String）：在标签上显示的文本
     * @return 游戏板网格标签（Label）
     *
     * 作者：Daphne
     */
    private Label makeGridLabel(String labelString) {
        Label newLabel = new Label();
        newLabel.setText(labelString);
        newLabel.setAlignment(Pos.CENTER);
        newLabel.setTextAlignment(TextAlignment.CENTER);
        newLabel.setFont(new Font("IMPACT", 24));
        newLabel.setPadding(new Insets(5));
        newLabel.setTextFill(Color.WHITE);
        newLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        newLabel.setMinSize(squareMinSize, squareMinSize);
        newLabel.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderStroke.MEDIUM)));
        newLabel.setBackground(new Background(new BackgroundFill(
                Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        return newLabel;
    }

    /**
     * 重载的标签工厂：网格标签工厂方法，创建一个游戏板网格标签，不带文本
     * @return 游戏板网格标签（Label）
     *
     * 作者：Daphne
     */
    private Label makeGridLabel() {
        return makeGridLabel("");
    }

    /** 工厂方法：车辆前方按钮
     * 该方法创建用作车辆前方的按钮。车辆的“前方”为视觉效果而圆形化，并将车辆向下或向右移动。
     * @param isHorizontal：如果车辆是水平的，则为 true；如果是垂直的，则为 false
     * @param vehicle（Vehicle）：与创建的按钮相关联的车辆对象
     * @return 用作车辆前方的按钮
     *
     * 作者：Daphne
     */
    private Button vehicleFrontButton(Boolean isHorizontal, Vehicle vehicle) {
        // 根据车辆的方向确定变量的设置
        ImageView graphic;
        CornerRadii roundedEdge;
        // 确定车辆的方向（V 或 H）以及使用的箭头图像（UP/DOWN）
        if (isHorizontal) {
            graphic = new ImageView(RIGHT_ARROW);
            roundedEdge = new CornerRadii(0.0, 40.0, 40.0, 0.0, false);
        } else {
            graphic = new ImageView(DOWN_ARROW);
            roundedEdge = new CornerRadii(0.0, 0.0, 40.0, 40.0, false);
        }
        // 调用辅助方法创建按钮
        return makeVehicleButton(graphic, vehicle, roundedEdge, (e) -> { moveVehicleForwards(vehicle); });
    }


    /** 工厂方法：车辆后方按钮
     * 该方法创建用作车辆后方的按钮。车辆的“后方”为视觉效果而平坦，并将车辆向上或向左移动。
     * @param isHorizontal：如果车辆是水平的，则为 true；如果是垂直的，则为 false
     * @param vehicle（Vehicle）：与创建的按钮相关联的车辆对象
     * @return 用作车辆前方的按钮
     *
     * 作者：Daphne
     */
    private Button vehicleBackButton(Boolean isHorizontal, Vehicle vehicle) {
        // 根据车辆的方向确定变量的设置
        CornerRadii notRounded = CornerRadii.EMPTY;
        ImageView graphic;
        // 确定车辆的方向（V 或 H）以及使用的箭头图像（UP/DOWN）
        if (isHorizontal) {
            graphic = new ImageView(LEFT_ARROW);
        } else {
            graphic = new ImageView(UP_ARROW);
        }
        // 调用辅助方法创建按钮
        return makeVehicleButton(graphic, vehicle, notRounded, (e) -> { moveVehicleBackwards(vehicle); });
    }

    /**
     * 该辅助方法创建用作车辆前方或后方的按钮。
     * @param graphic（ImageView）：要在按钮上显示的箭头图像，指示按钮将使车辆移动的方向
     * @param vehicle（Vehicle）：与创建的按钮相关联的车辆对象
     * @param cornerEdges（CornderRadii）：按钮上角落将被圆角化的程度，如果有的话
     * @param @eventHandler 点击按钮时要启动的事件操作
     * @return 用作车辆前方的按钮
     *
     * 作者：Daphne
     */
    private Button makeVehicleButton(
            ImageView graphic, Vehicle vehicle, CornerRadii cornerEdges, EventHandler<ActionEvent> handler) {
        // 获取车辆的颜色
        Color vehicleColor = VehicleColor.valueOf(String.format("%c", vehicle.getSymbol())).getColor();
        // 创建按钮
        Button newButton = new Button("", graphic);
        newButton.setAlignment(Pos.CENTER);
        newButton.setPadding(new Insets(5));
        newButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        newButton.setMinSize(squareMinSize, squareMinSize);
        newButton.setBorder(new Border(new BorderStroke(vehicleColor,
                BorderStrokeStyle.SOLID, cornerEdges,
                BorderWidths.EMPTY)));
        newButton.setBackground(new Background(new BackgroundFill(
                vehicleColor, cornerEdges, Insets.EMPTY)));
        // 调用 EventHandler 并在每个按钮上注册具体的观察者
        newButton.setOnAction(handler);
        board.registerObserver((this));
        return newButton;
    }


    /*###########################################################################################################*/
    /* 创建 GUI - 用户将与之交互的界面 */
    /** 作者：Daphne 构建了 GUI 的基本结构 */

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* 加载新游戏文件： */
        // 创建一个新的游戏对象，用于提取游戏状态
        // board = new RushHour(level1Filename); //重构 - 原始代码 - 1级别播放

        // 游戏始终从级别 1 开始
        gameLevelNum = 1;
        // 获取新游戏级别的文件名
        gameLevel = BoardLevel.valueOf(String.format("L%d", gameLevelNum));
        levelFilename = gameLevel.getFilename();
        board = new RushHour(levelFilename);
        /**************************************************************/
        /* 底部状态栏：'游戏状态' 和 '移动计数' 标签以及重置、退出、解决和提示按钮 */
        // 按钮：为 StatusBar 创建 "重置" 按钮
        resetButton = buttonFactory("重置", (e) -> {
            try {
                resetGame();
            } catch (IOException ef) {
                System.out.println(ef);
            }
        });
        // 按钮：为 StatusBar 创建 "解决" 按钮
        solveButton = buttonFactory("解决", (e) -> {solveGame();});
        // 按钮：为 StatusBar 创建 "级别" 按钮
        gameLevelButton = buttonFactory("级别", (e) -> {
            try {
                levelUp();
            } catch (IOException ef) {
                System.out.println(ef);
            }
        });
        // 按钮：为 StatusBar 创建 "显示提示" 按钮
        hintButton = buttonFactory("显示提示", (e) -> {getHint();});
        // 按钮：为 StatusBar 创建 "退出" 按钮
        quitButton = buttonFactory("退出", (e) -> {quit();});

        // 显示标签：为 StatusBar 创建 "移动计数" 标签
        int moves = board.getMoveCount();
        moveCountLabel = makeDisplayLabel(String.format("移动次数：%d", moves));

        // 显示标签：为 StatusBar 创建用于显示游戏/移动更新消息的标签
        gameStatus = makeDisplayLabel("让我们玩 RushHour！（1级别）");

        // HBOX：底部状态栏的容器
        HBox statusBar = new HBox();
        statusBar.getChildren().addAll(resetButton, solveButton, gameLevelButton, gameStatus,
                moveCountLabel, hintButton, quitButton);
        HBox.setHgrow(gameStatus, Priority.ALWAYS);


        /****************************************************************/
        /** 游戏板网格 */
        // 为整个游戏板创建 GridPane
        gameBoard = new GridPane();

        // 在网格的其余部分添加标签
        int col = 6;
        int row = 6;
        // 循环以在 GUI 中添加一个 6x6 的标签网格
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 2 && j == 5) {
                    Label backgroundLabel = makeGridLabel("出口");
                    gameBoard.add(backgroundLabel, j, i);
                    GridPane.setHgrow(backgroundLabel, Priority.ALWAYS);
                    GridPane.setVgrow(backgroundLabel, Priority.ALWAYS);
                } else {
                    Label backgroundLabel = makeGridLabel();
                    gameBoard.add(backgroundLabel, j, i);
                    GridPane.setHgrow(backgroundLabel, Priority.ALWAYS);
                    GridPane.setVgrow(backgroundLabel, Priority.ALWAYS);
                }
            }
        }

        // 创建车辆节点并将它们添加到游戏板上
        addVehiclesToBoard();

        /****************************************************************/
        /** 游戏窗口：将所有布局组合在一起以构建游戏 */
        /** 使用 BorderPane 将 RushHour 游戏窗口的组件结合在一起。
         *
         * Center Pane = 游戏板（GridPane）
         * Bottom Pane = 状态栏 - 显示已进行的移动的状态和重置按钮
         */
        BorderPane gameWindow = new BorderPane();
        gameWindow.setBottom(statusBar);
        gameWindow.setCenter(gameBoard);
        gameWindow.setBorder(new Border(new BorderStroke(Color.DARKBLUE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderStroke.THICK)));
        gameWindow.setBackground(new Background(new BackgroundFill(
                Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));


        /****************************************************************/
        /** 显示 GUI */
        primaryStage.setTitle("RushHour");
        primaryStage.setScene(new Scene(gameWindow));
        primaryStage.show();
    }


    /*#####################################################################################################*/
/** 为事件处理/更新 GUI 需要的方法设置 */

    /** 此方法通过以下步骤在游戏板（GUI）上创建并添加车辆（节点）：
     * 1) 从 RushHour 模型获取车辆并循环遍历它们
     * 2) 为车辆的前方和后方分别创建按钮（使用每个按钮的工厂方法）
     *      >> 按钮标签包含箭头图像（上、下、左、右），取决于是前方还是后方，以及车辆的方向（垂直/水平）
     *      >> 图像设置为带有透明背景的常量
     *      >> 为按钮设置背景颜色为车辆的颜色（VehicleColor 枚举）
     * 3) 创建 HBox 或 VBox 以表示基于车辆方向的车辆。将两个按钮都添加到 Box 中。设置 V/H-Grow 的参数。
     * 4) 将 BOX 添加到网格，跨越行/列，根据车辆前方/后方的位置来确定
     *      （例如：前方行/列 = 2,1，后方行/列 = 2,3；然后 BOX 跨足 1 行（＃2）和 3 列（＃1-3））。
     *
     * 作者：Daphne Neale
     */
    private void addVehiclesToBoard() {
        // 获取 RushHour（模型）板上车辆的深度复制
        Collection<Vehicle> vehicles = this.board.getVehicles();
        vehicleGUIs = new HashMap<>();
        for (Vehicle vehicle : vehicles) {
            // 获取用于车辆位置和创建的规格
            int frontRow = vehicle.getFront().getRow();
            int frontCol = vehicle.getFront().getCol();
            int backRow = vehicle.getBack().getRow();
            int backCol = vehicle.getBack().getCol();
            char vehicleChar = vehicle.getSymbol();
            // 如果水平为 true，垂直为 false
            boolean isHorizontal = vehicle.isHorizontal();
            // 创建前方和后方标签
            Button frontLabel = vehicleFrontButton(isHorizontal, board.getVehiclesOnBoard().get(vehicle.getSymbol()));
            Button backLabel = vehicleBackButton(isHorizontal, board.getVehiclesOnBoard().get(vehicle.getSymbol()));
            // 将标签添加到适当的布局
            if (isHorizontal) {
                HBox horizontalVehicle = new HBox();
                horizontalVehicle.getChildren().addAll(backLabel, frontLabel);
                HBox.setHgrow(backLabel, Priority.ALWAYS);
                HBox.setHgrow(frontLabel, Priority.ALWAYS);
                gameBoard.add(horizontalVehicle, backCol, backRow, (frontCol - backCol) + 1, (frontRow - backRow) + 1);
                vehicleGUIs.put(vehicleChar, horizontalVehicle);
            } else {
                VBox verticalVehicle = new VBox();
                verticalVehicle.getChildren().addAll(backLabel, frontLabel);
                VBox.setVgrow(backLabel, Priority.ALWAYS);
                VBox.setVgrow(frontLabel, Priority.ALWAYS);
                gameBoard.add(verticalVehicle, backCol, backRow, (frontCol - backCol) + 1, (frontRow - backRow) + 1);
                vehicleGUIs.put(vehicleChar, verticalVehicle);
            }
        }
    }

    /**
     * 当按下车辆前方的箭头时，此方法为车辆在其尝试移动的方向（DOWN 或 RIGHT）上创建 Move 对象。
     * 然后调用 moveVehicle() 处理 RushHour 模型中的移动。
     *
     * 注意！此实现正在访问模型中车辆对象的原始副本，而不是深度复制。
     *
     * 作者：Lennard
     * 重构：Daphne
     */
    public void moveVehicleForwards(Vehicle vehicle) {
        // 创建方向变量以捕获车辆的移动方向
        Direction direction;

        // 确定要移动的方向
        if (vehicle.isHorizontal()) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.DOWN;
        }
        // 创建要传递给模型的移动对象
        Move move = new Move(vehicle.getSymbol(), direction);
        moveVehicle(move);
    }


    /**
     * 当车辆的后方箭头被按下时，此方法为车辆在其尝试移动的方向（UP 或 LEFT）上创建 Move 对象。
     * 然后调用 moveVehicle() 处理 RushHour 模型中的移动。
     *
     * 注意！此实现正在访问模型中车辆对象的原始副本，而不是深度复制。
     *
     * 作者：Lennard
     * 重构：Daphne
     */
    public void moveVehicleBackwards(Vehicle vehicle) {
        // 创建方向变量以捕获车辆的移动方向
        Direction direction;

        // 确定要移动的方向
        if (vehicle.isHorizontal()) {
            direction = Direction.LEFT;
        } else {
            direction = Direction.UP;
        }
        // 创建要传递给模型的移动对象
        Move move = new Move(vehicle.getSymbol(), direction);
        moveVehicle(move);
    }

    /**
     * 此 HELPER 方法调用 RushHour 模型中的 moveVehicle 方法，以启动尝试在模型中移动车辆。
     * 如果移动无效，则捕获错误消息并显示给用户。
     * @param move: 包含尝试的车辆移动信息的 Move 对象
     *
     * 作者：Daphne
     */
    private void moveVehicle(Move move) {
        try {
            board.moveVehicle(move);
        } catch (RushHourException e) {
            // 在状态区域显示任何错误消息
            gameStatus.setText(e.getMessage());
        }
    }

    /**
     * 当按下提示按钮时，在游戏状态区域显示一个随机提示。
     *
     * 作者：Lennard
     */
    public void getHint() {
        // 从可能移动的列表中返回一个随机移动
        Random rand = new Random();
        List<Move> possibleMoves = board.getPossibleMoves();
        // 获取随机索引和随机可能的 Move
        int randomIndex = rand.nextInt(possibleMoves.size());
        Move randomMove = possibleMoves.get(randomIndex);
        // 获取用于打印提示的信息（车辆颜色和方向）
        String colorSymbol = String.format("%c", randomMove.getSymbol());
        String color = VehicleColor.valueOf(colorSymbol).getColorString();
        // 字符符号 = randomMove.getSymbol();
        // String color = colorMap.get(symbol);
        Direction direction = randomMove.getDir();
        // 在 gameStatus 标签上显示提示
        gameStatus.setText("尝试 " + color + " " + direction);
    }

    /**
     * 此方法将 gameBoard 重置为给定文件的配置
     * @param levelFilename（String）：游戏配置文件的文件路径名
     * @throws IOException 如果找不到/读取不了 gameboard 文件
     *
     * 作者：Emma
     * 重构：Daphne - 升级游戏
     */
    public boolean resetGame(String levelFilename) throws IOException {
        // 创建新的重置板
        this.board = new RushHour(levelFilename);

        // 确保所有按钮都是启用的
        this.hintButton.setDisable(false);
        this.solveButton.setDisable(false);
        this.resetButton.setDisable(false);
        this.gameBoard.setDisable(false);

        // 重置移动计数器并更新 moveCountLabel
        this.board.resetMoveCount();
        this.moveCountLabel.setText("移动次数：" + this.board.getMoveCount());

        // 从 gameBoard（GUI）中删除所有当前车辆
        for (Node vgui : this.vehicleGUIs.values()) {
            gameBoard.getChildren().remove(vgui);
        }

        // 将来自重置板的车辆添加到 gameBoard（GUI）中
        addVehiclesToBoard();

        return true;
    }


    /** 重载的重置游戏的方法
     * 此方法将 gameBoard 重置为当前正在播放的级别的原始配置，并在成功重置时更新 gameStatus Label。
     * @throws IOException 如果找不到/读取不了 gameboard 文件
     *
     * 作者：Daphne
     */
    public void resetGame() throws IOException {
        // 使用当前游戏级别文件重置板
        if (resetGame(this.levelFilename)) {
            // 更新 gameStatus label，指示游戏已被重置
            gameStatus.setText("游戏重置。让我们再玩一次！");
        }
    }


    /**
     * 此方法加载下一个级别，从当前正在播放的级别升级，并在成功加载时更新 gameStatus Label。
     * @param @levelFilename（String）：游戏配置文件的文件路径名
     * @throws IOException 如果找不到/读取不了 gameboard 文件
     *
     * 作者：Daphne
     */
    private void levelUp() throws IOException {
        // 将级别增加 1
        BoardLevel[] maxLevels = BoardLevel.values();
        if (gameLevelNum < maxLevels.length){
            gameLevelNum += 1;
            // 获取新游戏级别的文件名
            gameLevel = BoardLevel.valueOf(String.format("L%d",gameLevelNum));
            levelFilename = gameLevel.getFilename();
            if (resetGame(levelFilename)) {
                gameStatus.setText(String.format("开始第 %1$d 级 - %2$d 辆车。让我们开始吧！", gameLevel.getLevel() ,gameLevel.getNumOfCars()));
            }
        }
        // 玩家已达到最高级别 - 循环回到第一级别
        else{
            gameLevelNum = 1;
            // 获取新游戏级别的文件名
            gameLevel = BoardLevel.valueOf(String.format("L%d",gameLevelNum));
            levelFilename = gameLevel.getFilename();
            if (resetGame(levelFilename)) {
                gameStatus.setText(String.format("回到第 %1$d 级 - %2$d 辆车。让我们开始吧！", gameLevel.getLevel() ,gameLevel.getNumOfCars()));
            }
        }

    }


    /**
     * 此方法充当 RushHour（模型）类的具体观察者。当 RushHour 类中的“board”上的车辆发生更新时，
     * 为了更新 GUI 的状态，调用此方法。
     * @ConcreteObserver 在模型中的车辆移动后更新 board
     * @param vehicle：在 board 上移动的车辆对象
     *
     * 作者：Emma
     * 作者：Daphne - 在新位置上从 board 中删除和添加 vehicleGUI
     */
    @Override
    public void vehicleMoved(Vehicle vehicle) {
        // 获取移动的车辆的字符符号
        Character vehicleSymbol = vehicle.getSymbol();
        // 获取代表车辆的 vehicleGUI（对象）
        Node vehicleGUI = vehicleGUIs.get(vehicleSymbol);
        // 从 gameBoard（GridPane）中删除 vehicleGUI
        gameBoard.getChildren().remove(vehicleGUI);

        /* 遵循将车辆添加到 GUI 时的相同过程
         * （大致在第 338-370 行之间），获取移动车辆的新位置信息，
         * 并将 vehicleGUI 添加回 board。
         *
         * 位置由车辆的后部位置设置（列，行，跨越列，跨越行）
         */
        // 获取车辆前后位置的新行和列（重复的第 340-343 行）
        int frontRow = vehicle.getFront().getRow();
        int frontCol = vehicle.getFront().getCol();
        int backRow = vehicle.getBack().getRow();
        int backCol = vehicle.getBack().getCol();
        // 将 vehicleGUI 添加回 gameBoard 的新位置
        gameBoard.add(vehicleGUI, backCol, backRow, (frontCol-backCol)+1,(frontRow-backRow)+1);

        // 使用新的移动计数更新 moveCountLabel
        int moveCount = board.getMoveCount();
        String messageUpdate = String.format("Moves: %d", moveCount);
        moveCountLabel.setText(messageUpdate);

        if (this.board.isGameOver()) {
            this.gameStatus.setText("你赢了，干得好！");
            System.out.println("游戏赢了"); // 作为检查从 CLI 确保移动完成的一部分添加的输出
            this.gameBoard.setDisable(true);
            this.hintButton.setDisable(true);
        } else {
            System.out.println("出色的移动！");  // 作为检查从 CLI 确保移动完成的一部分添加的输出
            this.gameStatus.setText("出色的移动！");
        }
    }


    /**
     * 此方法禁用 gameWindow 上的所有按钮
     * @作者 Daphne
     */
    private void quit() {
        this.gameBoard.setDisable(true);
        this.resetButton.setDisable(true);
        this.solveButton.setDisable(true);
        this.gameLevelButton.setDisable(true);
        this.hintButton.setDisable(true);
        this.quitButton.setDisable(true);
        this.gameStatus.setText("再见！");
    }

    /**
     * 此方法尝试从当前的 board 状态解决 RushHour 游戏。
     *   >> 如果找到解决方案，board 将逐一更新车辆的移动，直到游戏解决，并更新 gameStatus Label，表示找到解决方案。
     *   >> 如果找不到解决方案，gameStatus Label 将更新，指示未找到解决方案。
     *
     * 作者：Daphne
     */
    private void solveGame() {
        // 启动回溯以解决游戏
        RushHourSolver solutionConfig;
        // 在尝试解决 board 之前检查当前 board 是否可解
        if (RushHourSolver.solvable(this.board)) {
            solutionConfig = RushHourSolver.solve(this.board);
        } else {
            solutionConfig = null;
        }
        // 如果 configuration == null，在 StatusLabel 中显示失败消息
        if (solutionConfig == null) {
            this.gameStatus.setText("未找到解决方案。请重试。");
        } else {
            // 否则（solution 不为 null），使用解决方案配置更新 board
            // 获取移动列表，并逐一处理/显示每个移动直到解决
            List<Move> winningMoves = solutionConfig.getMovesMade();
            // 使用线程逐一进行并显示移动以解决游戏
            new Thread(() -> {
                // 对于每个移动
                for (Move move : winningMoves) {
                    System.out.println("platform.runLater"); // 用于测试添加的输出
                    Platform.runLater(() -> {
                        // 进行移动
                        System.out.println(move); // 用于测试添加的输出
                        this.moveVehicle(move);
                        System.out.println("thread"); // 用于测试添加的输出
                        // 更新 StatusLabel 以指示找到有效解决方案
                        this.gameStatus.setText("找到解决方案！（移动次数：" + solutionConfig.getBoard().getMoveCount() + "）");
                    });
                    // 休眠一小段时间（~250ms）
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // 无需进行任何操作
                    }
                }
            }).start();
        }
        // 游戏板应该已解决
    }

    public static void main(String[] args) {
        launch(args);
    }
}

