package hznu.linxin.cniaoshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import hznu.linxin.cniaoshop.bean.User;
import hznu.linxin.cniaoshop.http.OkHttpHelper;
import hznu.linxin.cniaoshop.http.SpotsCallBack;
import hznu.linxin.cniaoshop.msg.LoginRespMsg;
import hznu.linxin.cniaoshop.utils.CountTimerView;
import hznu.linxin.cniaoshop.utils.DESUtil;
import hznu.linxin.cniaoshop.utils.ManifestUtil;
import hznu.linxin.cniaoshop.utils.ToastUtils;
import hznu.linxin.cniaoshop.widget.CNiaoToolBar;
import hznu.linxin.cniaoshop.widget.ClearEditText;

/**
 * @author: BacSon
 */
public class RegSecondActivity extends BaseActivity {



    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.btn_reSend)
    private Button mBtnResend;

    @ViewInject(R.id.edittxt_code)
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;


    private CountTimerView countTimerView;



    private SpotsDialog dialog;

    private OkHttpHelper okHttpHelper  = OkHttpHelper.getInstance();

    private  SMSEvenHanlder evenHanlder;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ViewUtils.inject(this);

        initToolBar();

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);



        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));



        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();




        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"));

        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this,"?????????????????????");




    }


    private void initToolBar(){


        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();

            }
        });


    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view){

        SMSSDK.getVerificationCode("+"+countryCode, phone);
        countTimerView = new CountTimerView(mBtnResend,R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog.setMessage("???????????????????????????");
        dialog.show();
    }

    /** ?????????????????? */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }





    private  void submitCode(){

        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode,phone,vCode);
        dialog.show();
    }


    private void doReg(){

        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        okHttpHelper.post(Contants.API.REG, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();

                if(userLoginRespMsg.getStatus()== LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(RegSecondActivity.this,"????????????:"+userLoginRespMsg.getMessage());
                    return;
                }
                CniaoApplication application =CniaoApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
//                 super.onTokenError(response, code);

            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    class SMSEvenHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(dialog !=null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {


//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");

//                            ToastUtils.show(RegSecondActivity.this,"???????????????"+phone+",country:"+country);


                            doReg();
                            dialog.setMessage("????????????????????????");
                            dialog.show();;
                        }
                    } else {

                        // ??????????????????????????????????????????toast??????
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }
}
