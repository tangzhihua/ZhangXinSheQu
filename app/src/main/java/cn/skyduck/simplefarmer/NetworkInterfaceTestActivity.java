package cn.skyduck.simplefarmer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import core_lib.domainbean_model.login.LoginNetRequestBean;
import core_lib.domainbean_model.login.LoginNetRespondBean;
import core_lib.simple_network_engine.domain_net_layer.SimpleNetworkEngineSingleton;
import core_lib.simple_network_engine.domain_net_layer.domainbean.IRespondBeanAsyncResponseListener;
import core_lib.simple_network_engine.error_bean.ErrorBean;
import core_lib.toolutils.DebugLog;

public class NetworkInterfaceTestActivity extends Activity {
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_interface_test);


        // ButterKnife.inject(this) should be called after setContentView()
        //ButterKnife.inject(this);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.button1)
    void onButtonClick1(View view) {
        LoginNetRequestBean netRequestBean = new LoginNetRequestBean("3252475@qq.com", "123456Hh");
        SimpleNetworkEngineSingleton.getInstance.requestDomainBean(netRequestBean,
                new IRespondBeanAsyncResponseListener<LoginNetRespondBean>() {

                    @Override
                    public void onSuccess(LoginNetRespondBean respondBean) {
                        DebugLog.e(TAG, respondBean.toString());
                        Toast.makeText(NetworkInterfaceTestActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(ErrorBean errorBean) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onBegin() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onEnd(boolean isSuccess) {
                        // TODO Auto-generated method stub

                    }
                });
    }


    @OnClick(R.id.button2)
    void onButtonClick2(View view) {


    }

    @OnClick(R.id.button3)
    void onButtonClick3(View view) {

    }

    @OnClick(R.id.button4)
    void onButtonClick4(View view) {

    }
}
