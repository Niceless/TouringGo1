package soafinal.tourtest;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 杨 on 2017/12/17.
 */

public class User extends BmobUser {
    private String sex;
    private String nick;
    private Integer age;
    private  String birthday;
    private String address;
    private String avator;
    private String url;
    public String getSex(){
        return this.sex;
    }

    public void setSex(String gender) {
        this.sex = gender;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nickname) {
        this.nick = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthday(String birth){this.birthday=birth;}

    public String getBirthday(){return this.birthday;}

    public void setAddress(String addr){this.address=addr;}

    public String getAddress(){return address;}

    public void setAvator(String url){this.avator=url;}

    public String getAvator(){return avator;}


    public void setUrl(String url){this.url=url;}

    public String getUrl(){return url;}
}

