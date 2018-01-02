package soafinal.tourtest;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import cn.bmob.v3.Bmob;
import android.widget.Toast;
import android.app.ListActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import soafinal.tourtest.Fastjson.pagebean;
import com.show.api.ShowApiRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 杨 on 2017/12/19.
 */

public class Attaction extends ListActivity {
    //Intent getIntent=getIntent();
    //String key=getIntent.getStringExtra("key");
    protected Handler mHandler =  new Handler();
    private pagebean data = null;
    String keyword ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent getIntent=getIntent();
        String key=getIntent.getStringExtra("key");
        keyword=key;
        //Toast.makeText(this,key,Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run(){
                String appid="52038";
                String secret="f52a21f0f81c44e4893022707d3a6cb1";
                final String res=new ShowApiRequest("http://route.showapi.com/268-1",appid,secret).addTextPara("keyword", keyword)
                        .addTextPara("proId", "")
                        .addTextPara("cityId", "")
                        .addTextPara("areaId", "")
                        .addTextPara("page", "")
                        .post();
                data = util.handlePagebeanResponse(res);

                final SimpleAdapter adapter = new SimpleAdapter(Attaction.this,getData(),R.layout.activity_attraction,
                        new String[]{"name","address","summary","img"},
                        new int[]{R.id.name,R.id.address,R.id.summary,R.id.img});

                mHandler.post(new Thread(){
                    @Override
                    public void run() {
                       setListAdapter(adapter);

                        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Object data, String textRepresentation) {
                                if(view instanceof ImageView && data instanceof Drawable){
                                    ImageView iv = (ImageView)view;
                                    iv.setImageDrawable((Drawable)data);
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final List<String> strList = util.getFirstPic(data);
        for(int i = 0; i < data.contentlist.size() ; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name",data.contentlist.get(i).name);
            map.put("address",data.contentlist.get(i).cityName
                    +data.contentlist.get(i).areaName
                    +data.contentlist.get(i).address);
            map.put("summary",data.contentlist.get(i).summary);
            String str = strList.get(i);
            map.put("img",loadImageFromNetwork(str));
            list.add(map);
        }
        return list;
    }

    private Drawable loadImageFromNetwork(String url) {
        Drawable drawable = null;
        try{
            drawable = Drawable.createFromStream(new URL(url).openStream(), "image.jpg");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return drawable;
    }
    }

