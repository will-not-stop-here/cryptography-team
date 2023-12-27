package rushhour.model;

/**
 * 该类表示游戏板上的行和列空间。
 */
public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * 比较两个Position对象的行和列。
     * @returns 如果行和列相同则返回true
     *
     * @author Lennard
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position other = (Position)o;
            return (this.row == other.row &&
                    this.col == other.col);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[行: " + this.row + " 列: " + this.col + "]";
    }
}
