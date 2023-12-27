package backtracker;

/**
 * 该类表示经典的递归回溯算法。
 * 它具有一个求解器，可以接受一个有效的配置并返回一个解决方案（如果存在）。
 *
 * 作者：GCCIS Faculty
 */
public class Backtracker<C extends Configuration<C>> {
    /*
     * 是否启用调试输出？
     */
    private boolean debug;

    /**
     * 初始化一个新的回溯器
     *
     * @param debug 是否启用调试输出？
     */
    public Backtracker(boolean debug) {
        this.debug = debug;
        if (this.debug) {
            System.out.println("backtracker.Backtracker 调试已启用...");
        }
    }

    /**
     * 用于打印各种调试消息的实用程序方法。
     *
     * @param msg 被查看的配置类型（当前，目标，后继等）
     * @param config 要显示的配置
     */
    private void debugPrint(String msg, C config) {
        if (this.debug) {
            System.out.println(msg + ":\n" + config);
        }
    }

    /**
     * 尝试找到给定配置的解决方案（如果存在）。
     *
     * @param config 一个有效的配置
     * @return 解决方案配置，如果没有解决方案则为null
     */
    public C solve(C config) {
        debugPrint("当前配置", config);
        if (config.isGoal()) {
            debugPrint("\t目标配置", config);
            return config;
        } else {
            for (C child : config.getSuccessors()) {
                if (child.isValid()) {
                    debugPrint("\t有效的后继", child);
                    C sol = solve(child);
                    if(sol != null) {
                        return sol;
                    }
                } else {
                    debugPrint("\t无效的后继", child);
                }
            }
            // 隐式回溯发生在这里
        }
        return null;
    }
}
