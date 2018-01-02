package soafinal.tourtest;

/**
 * Created by 杨 on 2017/12/28.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import soafinal.tourtest.User;

public class Detail extends AppCompatActivity{
    private TextView nickname;
    private TextView sex;
    private TextView birthday;
    private TextView phone;
    private TextView email;
    private TextView address;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        User myUser=BmobUser.getCurrentUser(User.class);
        nickname = (TextView) findViewById(R.id.nickname);
        nickname.setText(myUser.getNick());
        sex = (TextView) findViewById(R.id.sex);
        sex.setText(myUser.getSex());
        birthday = (TextView) findViewById(R.id.birthday);
        birthday.setText(myUser.getBirthday());
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(myUser.getMobilePhoneNumber());
        email = (TextView) findViewById(R.id.email);
        email.setText(myUser.getEmail());
        address = (TextView) findViewById(R.id.address);
        address.setText(myUser.getAddress());
    }
    public void bk(View view){
        Intent intent = new Intent();
        intent.setClass(Detail.this, MyInfo.class);
        startActivity(intent);
    }
    public void edit(View view){
        Intent intent = new Intent();
        intent.setClass(Detail.this, EditInfo.class);
        startActivity(intent);
    }
}
