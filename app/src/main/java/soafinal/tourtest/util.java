package soafinal.tourtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import soafinal.tourtest.Fastjson.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2017/12/14.
 */

public class util {
    public static pagebean handlePagebeanResponse(String response){
        try{
            JSONObject jo = JSONObject.parseObject(response).getJSONObject("showapi_res_body");
            JSONObject js = jo.getJSONObject("pagebean");
            pagebean p = new pagebean();
            p.allPages = js.getString("allPages");
            p.currentPage = js.getString("currentPage");
            p.maxResult = js.getString("maxResult");
            JSONArray ja = js.getJSONArray("contentlist");
            for(int i = 0; i < ja.size(); i++){
                String str = ja.get(i).toString();
                content con = JSON.parseObject(str, content.class);
                p.contentlist.add(i, con);
            }

            return p;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getFirstPic(pagebean p){
        try {
            List<String> strList = new ArrayList<String>();
            for(int i = 0; i < p.contentlist.size(); i++){
                String str = p.contentlist.get(i).picList[0];
                PicList pl = JSON.parseObject(str, PicList.class);
                strList.add(pl.picUrlSmall);
            }
            return strList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
