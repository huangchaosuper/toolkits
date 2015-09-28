package cn.huangchaosuper.toolkits;

/**
 * Created by I311579 on 9/28/2015.
 */
public interface IStep {
    public boolean run(Context context);
    public void finalized();
}
