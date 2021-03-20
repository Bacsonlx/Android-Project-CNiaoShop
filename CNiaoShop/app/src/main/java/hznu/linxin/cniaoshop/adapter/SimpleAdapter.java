package hznu.linxin.cniaoshop.adapter;

import android.content.Context;

import java.util.List;

/**
 * @author: BacSon
 * data: 2021/3/11
 */

/**
 *  给封装后的Adapetr提供构造函数的抽象类
 * @param <T>
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }
}
