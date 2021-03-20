package hznu.linxin.cniaoshop.adapter;

/**
 * @author: BacSon
 * data: 2021/3/11
 */

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import hznu.linxin.cniaoshop.R;
import hznu.linxin.cniaoshop.bean.ShoppingCart;
import hznu.linxin.cniaoshop.bean.Wares;
import hznu.linxin.cniaoshop.utils.CartProvider;
import hznu.linxin.cniaoshop.utils.ToastUtils;

/**
 *  使用封装后的Adapter
 */
public class HWAdatper extends SimpleAdapter<Wares> {


    CartProvider provider ;

    public HWAdatper(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);

        provider = new CartProvider(context);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥ "+wares.getPrice());

        Button button =viewHolder.getButton(R.id.add_to_card);
        if(button !=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    provider.put(wares);

                    ToastUtils.show(context, "已添加到购物车");
                }
            });
        }

    }




    public void  resetLayout(int layoutId){


        this.layoutResId  = layoutId;

        notifyItemRangeChanged(0,getDatas().size());


    }

}