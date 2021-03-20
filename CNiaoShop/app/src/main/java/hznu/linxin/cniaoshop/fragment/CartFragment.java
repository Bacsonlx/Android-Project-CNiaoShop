package hznu.linxin.cniaoshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import hznu.linxin.cniaoshop.MainActivity;
import hznu.linxin.cniaoshop.NewOrderActivity;
import hznu.linxin.cniaoshop.R;
import hznu.linxin.cniaoshop.adapter.CartAdapter;
import hznu.linxin.cniaoshop.bean.ShoppingCart;
import hznu.linxin.cniaoshop.utils.CartProvider;
import hznu.linxin.cniaoshop.widget.CNiaoToolBar;


/**
 * @author BacSon
 */

/**
 *  展示购物车数据
 */
public class CartFragment extends BaseFragment implements View.OnClickListener{

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;
    private static final String TAG = "CartFragment";

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private CNiaoToolBar mToolbar;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }


    @Override
    public void init() {

        cartProvider = new CartProvider(getActivity());

        changeToolbar();
        showData();
    }

    /**
     *  点击删除按钮
     * @param view
     */
    @OnClick(R.id.btn_del)
    public void delCart(View view){

        mAdapter.delCart();
    }

    /**
     *  点击去结算按钮
     * @param view
     */
    @OnClick(R.id.btn_order)
    public void toOrder(View view){

        Intent intent = new Intent(getActivity(), NewOrderActivity.class);

        startActivity(intent, true);
    }

    /**
     *  展示数据
     */
    private void showData(){

        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getActivity(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));


    }

    /**
     *  刷新数据
     */
    public void refData(){

        mAdapter.clearData();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    /**
     *  找到 ToolBar
     * @param context
     */
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if(context instanceof MainActivity){

            MainActivity activity = (MainActivity) context;

            mToolbar = (CNiaoToolBar) activity.findViewById(R.id.toolbar);
            changeToolbar();
        }

    }

    /**
     *  进入购物车页面改变ToolBar
     */
    public void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);

    }


    /**
     *  显示删除
     */
    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    /**
     *  隐藏删除
     */
    private void  hideDelControl(){

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }


    /**
     *  点击编辑按钮事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        int action = (int) v.getTag();
        if(ACTION_EDIT == action){

            showDelControl();
        }
        else if(ACTION_CAMPLATE == action){

            hideDelControl();
        }

    }


}
