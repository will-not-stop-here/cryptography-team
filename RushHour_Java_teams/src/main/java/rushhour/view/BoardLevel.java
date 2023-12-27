package rushhour.view;

/**
 * 游戏板难度级别的枚举
 * 作者：Daphne
 */
public enum BoardLevel {
    // RushHour 游戏板的难度级别
    L1(1, 3, "data/03_00.csv"),
    L2(2, 3, "data/03_01.csv"), // 此文件没有解决方案
    L3(3, 4, "data/04_00.csv"),
    L4(4, 5, "data/05_00.csv"),
    L5(5, 5, "data/05_01.csv"),
    L6(6, 6, "data/06_00.csv"),
    L7(7, 7, "data/07_00.csv"),
    L8(8, 7, "data/07_01.csv"),
    L9(9, 7, "data/07_02.csv"), // 此文件没有解决方案
    L10(10, 8, "data/08_00.csv"),
    L11(11, 9, "data/09_00.csv"),
    L12(12, 10, "data/10_00.csv"),
    L13(13, 11, "data/11_00.csv"),
    L14(14, 12, "data/12_00.csv"),
    L15(15, 13, "data/13_00.csv");

    // 枚举属性
    private final int level;
    private final int numOfCars;
    private final String filename;

    // 构造函数
    private BoardLevel(int level, int numOfCars, String filename) {
        this.level = level;
        this.numOfCars = numOfCars;
        this.filename = filename;
    }

    // 获取枚举属性的访问器
    public int getLevel() {
        return level;
    }

    public int getNumOfCars() {
        return numOfCars;
    }

    public String getFilename() {
        return filename;
    }

    // 枚举的字符串表示
    @Override
    public String toString() {
        return String.format("%1$s: %2$d 辆车在游戏板上", name(), numOfCars);
    }
}
