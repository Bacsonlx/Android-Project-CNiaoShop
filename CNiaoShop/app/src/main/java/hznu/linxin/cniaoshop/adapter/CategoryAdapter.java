package hznu.linxin.cniaoshop.adapter;

import android.content.Context;

import java.util.List;

import hznu.linxin.cniaoshop.R;
import hznu.linxin.cniaoshop.bean.Category;

/**
 * @author: BacSon
 * data: 2021/3/12
 */

/**
 *  分类页面 左边侧边栏的Adapter
 */
public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {

        // 数据绑定
        viewHoder.getTextView(R.id.template_textView).setText(item.getName());
    }
}
