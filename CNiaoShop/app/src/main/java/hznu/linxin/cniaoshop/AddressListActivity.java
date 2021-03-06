package hznu.linxin.cniaoshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hznu.linxin.cniaoshop.adapter.AddressAdapter;
import hznu.linxin.cniaoshop.bean.Address;
import hznu.linxin.cniaoshop.http.OkHttpHelper;
import hznu.linxin.cniaoshop.http.SpotsCallBack;
import hznu.linxin.cniaoshop.msg.BaseRespMsg;
import hznu.linxin.cniaoshop.widget.CNiaoToolBar;

/**
 * @author: BacSon
 * data: 2021/3/20
 */

public class AddressListActivity extends BaseActivity {


    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private AddressAdapter mAdapter;


    private OkHttpHelper mHttpHelper= OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ViewUtils.inject(this);

        initToolbar();

        initAddress();


    }


    private void initToolbar(){

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddActivity();
            }
        });

    }


    private void toAddActivity() {

        Intent intent = new Intent(this,AddressAddActivity.class);
        startActivityForResult(intent,Contants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();

    }

    private void initAddress(){


        Map<String,Object> params = new HashMap<>(1);
        params.put("user_id",CniaoApplication.getInstance().getUser().getId());

        mHttpHelper.get(Contants.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {


            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    private void showAddress(List<Address> addresses) {

        Collections.sort(addresses);
        if(mAdapter ==null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressLisneter() {
                @Override
                public void setDefault(Address address) {

                    updateAddress(address);

                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
        else{
            mAdapter.refreshData(addresses);
            mRecyclerview.setAdapter(mAdapter);
        }

    }


    public void updateAddress(Address address){

        Map<String,Object> params = new HashMap<>(1);
        params.put("id",address.getId());
        params.put("consignee",address.getConsignee());
        params.put("phone",address.getPhone());
        params.put("addr",address.getAddr());
        params.put("zip_code",address.getZipCode());
        params.put("is_default",address.getIsDefault());

        mHttpHelper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){

                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

}

