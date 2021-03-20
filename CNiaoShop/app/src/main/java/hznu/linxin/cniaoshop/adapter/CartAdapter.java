package hznu.linxin.cniaoshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import hznu.linxin.cniaoshop.R;
import hznu.linxin.cniaoshop.bean.ShoppingCart;
import hznu.linxin.cniaoshop.utils.CartProvider;
import hznu.linxin.cniaoshop.widget.NumberAddSubView;

/**
 * @author: BacSon
 * data: 2021/3/13
 */

/**
 *  购物车的Adapter
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {


    public static final String TAG="CartAdapter";
    private CheckBox checkBox;
    private TextView textView;

    private CartProvider cartProvider;


    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView tv) {
        super(context, R.layout.template_cart, datas);

        setCheckBox(checkBox);
        setTextView(tv);

        cartProvider = new CartProvider(context);

        setOnItemClickListener(this);

        showTotalPrice();
    }


    /**
     *  数据初始化
     * @param viewHoder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder viewHoder, final ShoppingCart item) {

        // 加载购物车页面时将每一个商品的数据加载
        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        // 判断复选框是否选中
        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHoder.getView(R.id.num_control);

        numberAddSubView.setValue(item.getCount());

        // 点击加减按钮时
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {

                // 重新更新数量
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();

            }

            @Override
            public void onButtonSubClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });


    }


    /**
     *  统计总额
     * @return
     */
    private  float getTotalPrice(){

        float sum=0;
        if(!isNull())
            return sum;

        // 统计所有的单价 * 数量
        for (ShoppingCart cart:
                datas) {
            if(cart.isChecked())
                sum += cart.getCount()*cart.getPrice();
        }

        return sum;
    }


    /**
     *  显示总额
     */
    public void showTotalPrice(){

        float total = getTotalPrice();

        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }



    private boolean isNull(){

        return (datas !=null && datas.size()>0);
    }


    /**
     *  点击Item事件
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {

        ShoppingCart cart =  getItem(position);
        // 改变商品是否选中
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);

        checkListen();
        showTotalPrice();
    }


    /**
     *  全选按钮是否成立 如果有部分商品没选中选中按钮就不被选中
     *   只有所有商品选中才能使全选选中
     */
    private void checkListen() {

        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();

            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }

            if (count == checkNum) {
                checkBox.setChecked(true);
            }

        }
    }


    /**
     *  实现是否全选
     * @param isChecked
     */
    public void checkAll_None(boolean isChecked){

        if(!isNull())
            return ;

        int i=0;
        for (ShoppingCart cart :datas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }



    public void delCart(){

        if(!isNull())
            return ;

//        for (ShoppingCart cart : datas){
//
//            if(cart.isChecked()){
//                int position = datas.indexOf(cart);
//                cartProvider.delete(cart);
//                datas.remove(cart);
//                notifyItemRemoved(position);
//            }
//        }


        for(Iterator iterator = datas.iterator(); iterator.hasNext();){

            ShoppingCart cart = (ShoppingCart) iterator.next();
            if(cart.isChecked()){
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }

        }
    }



    public void setTextView(TextView textview){
        this.textView = textview;
    }

    public void setCheckBox(CheckBox ck){
        this.checkBox = ck;

        // 监听全选按钮是否选中
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();

            }
        });
    }

}
