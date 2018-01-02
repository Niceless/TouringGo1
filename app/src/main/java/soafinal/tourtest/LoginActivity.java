package soafinal.tourtest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
import android.content.Intent;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private EditText userid;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "eab7161f314e6d1d5e57fa006c98a15b");
        userid= (EditText) findViewById(R.id.userId);
        password= (EditText) findViewById(R.id.Password);

    }
    public void regsnow(View view){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String id = userid.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            User myUser = new User();
            myUser.setUsername(id);
            myUser.setPassword(pass);
            myUser.login(new SaveListener<User>() {
                public void done(User s, BmobException e){
                    if(e==null){
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, Main.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
    }