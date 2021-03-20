package hznu.linxin.cniaoshop.utils;

import android.content.Context;
import android.text.TextUtils;

import hznu.linxin.cniaoshop.Contants;
import hznu.linxin.cniaoshop.bean.User;

/**
 * @author: BacSon
 */
public class UserLocalData {


    public static void putUser(Context context, User user){

        String user_json =  JSONUtil.toJSON(user);
        PreferencesUtils.putString(context, Contants.USER_JSON,user_json);

    }

    public static void putToken(Context context,String token){

        PreferencesUtils.putString(context, Contants.TOKEN,token);
    }


    /**
     * 提供获取User对象的方法
     * @param context
     * @return
     */
    public static User getUser(Context context){

        String user_json= PreferencesUtils.getString(context,Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){
            return  JSONUtil.fromJson(user_json,User.class);
        }
        return  null;
    }

    public static  String getToken(Context context){

        return  PreferencesUtils.getString( context,Contants.TOKEN);

    }


    public static void clearUser(Context context){

        PreferencesUtils.putString(context, Contants.USER_JSON,"");

    }

    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Contants.TOKEN,"");
    }

}
