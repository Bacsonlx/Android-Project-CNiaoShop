package hznu.linxin.cniaoshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import hznu.linxin.cniaoshop.bean.User;
import hznu.linxin.cniaoshop.utils.UserLocalData;

/**
 * @author: BacSon
 * data: 2021/3/11
 */
public class CniaoApplication extends Application {

    // 储存当前登录用户
    private User user;

    private static  CniaoApplication mInstance;

    /**
     *  创建可以通过外部进行访问的公共静态方法
     * @return
     */
    public static  CniaoApplication getInstance(){
        return  mInstance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();
        Fresco.initialize(this);
    }

    /**
     *  初始化当前登录用户
     */
    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    /**
     *  返回User对象
     * @return
     */
    public User getUser(){
        return user;
    }


    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private  Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    /**
     * 页面跳转
     * @param context
     */
    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }

}
