package soafinal.tourtest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.BmobUser;
import soafinal.tourtest.User;

/**
 * Created by 杨 on 2017/12/17.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText userid;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this, "eab7161f314e6d1d5e57fa006c98a15b");
        userid = (EditText) findViewById(R.id.userId);
        password = (EditText) findViewById(R.id.Password);
    }

    public void register(View view) {
        String id = userid.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass)) {
            Toast.makeText(RegisterActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }else{
            BmobQuery<BmobUser>query=new BmobQuery<BmobUser>();
            query.addWhereEqualTo("username",id);
            query.findObjects(new FindListener<BmobUser>() {
                @Override
                public void done(List<BmobUser> list, BmobException e) {
               if(e==null){
                   if(list.size()==0){
                       String id = userid.getText().toString().trim();
                       String pass = password.getText().toString().trim();
                       //BmobUser myUser=new BmobUser();
                       User myUser=new User();
                       myUser.setPassword(pass);
                       myUser.setUsername(id);
                       myUser.signUp(new SaveListener<User>() {
                           @Override
                           public void done(User s,BmobException e){
                               if(e==null){
                                   Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent();
                                   intent.setClass(RegisterActivity.this,LoginActivity.class);
                                   startActivity(intent);
                               }
                               else{

                                   Toast.makeText(RegisterActivity.this, "注册失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }
                   else{
                       Toast.makeText(RegisterActivity.this, "已存在该用户！", Toast.LENGTH_SHORT).show();
                   }
               }
                    else{
                   Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
               }
                }
            });
        }
    }
    public void bkToLg(View view){
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}