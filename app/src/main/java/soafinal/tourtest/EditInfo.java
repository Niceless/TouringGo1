package soafinal.tourtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 杨 on 2017/12/28.
 */
public class EditInfo extends AppCompatActivity {
    private EditText upnick;
    private EditText upsex;
    private EditText upbirth;
    private EditText upphone;
    private EditText upemail;
    private EditText upaddress;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        upnick= (EditText) findViewById(R.id.upnick);
        upsex= (EditText) findViewById(R.id.upsex);
        upbirth= (EditText) findViewById(R.id.upbirth);
        upphone= (EditText) findViewById(R.id.upphone);
        upemail= (EditText) findViewById(R.id.upemail);
        upaddress= (EditText) findViewById(R.id.upaddress);
    }
    public void bktode(View view){
        Intent intent = new Intent();
        intent.setClass(EditInfo.this, Detail.class);
        startActivity(intent);
    }

    public void change(View view){
        String nick = upnick.getText().toString().trim();
        String sex = upsex.getText().toString().trim();
        String birth = upbirth.getText().toString().trim();
        String phone = upphone.getText().toString().trim();
        String email = upemail.getText().toString().trim();
        String address = upaddress.getText().toString().trim();
       ;
        User newUser=new User();
        if(!email.equals("")){
            newUser.setEmail(email);
        }
        if(!phone.equals("")){
            newUser.setMobilePhoneNumber(phone);
        }
        newUser.setNick(nick);
        newUser.setSex(sex);
        newUser.setBirthday(birth);
        newUser.setAddress(address);
        User myUser=BmobUser.getCurrentUser(User.class);
        newUser.update(myUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(EditInfo.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(EditInfo.this, Detail.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(EditInfo.this, "修改失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}