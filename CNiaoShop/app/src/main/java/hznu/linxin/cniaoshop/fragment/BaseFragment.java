package hznu.linxin.cniaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lidroid.xutils.ViewUtils;

import hznu.linxin.cniaoshop.CniaoApplication;
import hznu.linxin.cniaoshop.LoginActivity;
import hznu.linxin.cniaoshop.bean.User;

/**
 * @author: BacSon
 * data: 2021/3/19
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater,container,savedInstanceState);
        ViewUtils.inject(this, view);
        // 初始化ToolBar
        initToolBar();
        // 初始化信息
        init();

        return view;

    }

    public void  initToolBar(){

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

    /**
     * 重载startActivity方法实现页面拦截
     * @param intent
     * @param isNeedLogin 是否登录
     */
    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = CniaoApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }

        }
        else{
            super.startActivity(intent);
        }

    }

}