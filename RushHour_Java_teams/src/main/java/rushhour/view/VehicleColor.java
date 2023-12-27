package rushhour.view;

import javafx.scene.paint.Color;

/**
 * 枚举类，根据 RushHour 游戏中与车辆关联的字符串符号设置车辆的颜色。
 * 作者：Daphne
 */
public enum VehicleColor {
    // 根据 CSV 文件中的车辆字符
    R(Color.RED, "红色"), // 红色车辆
    A(Color.AQUA, "青色"),
    B(Color.BLUE, "蓝色"),
    C(Color.CHOCOLATE, "棕色"),
    D(Color.PLUM, "紫红色"),
    E(Color.BLUEVIOLET, "蓝紫色"),
    F(Color.FORESTGREEN, "森林绿色"),
    G(Color.GREENYELLOW, "绿黄色"),
    H(Color.HOTPINK, "粉红色"),
    I(Color.IVORY, "象牙白色"),
    O(Color.ORANGE, "橙色"),
    P(Color.PURPLE, "紫色"),
    Q(Color.YELLOW, "黄色");

    // 枚举属性
    private final Color vehicleColor;
    private final String colorString;

    // 构造函数
    private VehicleColor(Color color, String colorString) {
        this.vehicleColor = color;
        this.colorString = colorString;
    }

    // 获取枚举属性的访问器
    public Color getColor() {
        return this.vehicleColor;
    }

    public String getColorString() {
        return colorString;
    }

    // 枚举的字符串表示
    @Override
    public String toString() {
        return String.format("(%1$s) %2$s", name(), colorString);
    }
}
