package backtracker;

import java.util.Collection;

/**
 * 表示谜题的单个配置。
 * backtracker.Backtracker 依赖于这些例程以解决谜题。因此，所有谜题都必须实现这个接口。
 *
 * 作者：GCCIS Faculty
 */
public interface Configuration<C extends Configuration<C>> {
    /**
     * 获取从当前配置开始的所有后继。
     *
     * @return 所有后继，包括有效和无效的
     */
    public Collection<C> getSuccessors();

    /**
     * 当前配置是否有效？
     *
     * @return 如果有效则为true；否则为false
     */
    public boolean isValid();

    /**
     * 当前配置是否为目标？
     * @return 如果为目标则为true；否则为false
     */
    public boolean isGoal();
}
