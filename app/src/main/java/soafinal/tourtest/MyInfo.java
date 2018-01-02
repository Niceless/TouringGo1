package soafinal.tourtest;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptorFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.BmobWrapper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



public class MyInfo extends AppCompatActivity {
    private TextView myUser;

    User user;
    private ImageView img;
    private File mFile;
    private Bitmap mBitmap;
    String path = "";
    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;

    public static final int CUT_PHOTO = 3;

    protected String title() {
        return "个人资料";
    }

    // public final int SIZE=2*1024;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
       User bmobUser = BmobUser.getCurrentUser(User.class);
        myUser = (TextView) findViewById(R.id.user);
        myUser.setText(bmobUser.getUsername());

        iv = (ImageView) findViewById(R.id.img);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader loader=ImageLoader.getInstance();
        loader.init(config);
        loader.displayImage(bmobUser.getUrl(),iv);
    }
    public void Bkmain(View view) {
        Intent intent = new Intent();
        intent.setClass(MyInfo.this, Main.class);
        startActivity(intent);
    }

    public void detail(View view) {
        Intent intent = new Intent();
        intent.setClass(MyInfo.this, Detail.class);
        startActivity(intent);
    }

    public void logout(View view) {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            BmobUser.logOut();
            BmobUser currentUser = BmobUser.getCurrentUser();
            Intent intent = new Intent();
            intent.setClass(MyInfo.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "当前未登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(MyInfo.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void modify(View view) {
        String title = "选择获取图片方式";
        String[] items = new String[]{"拍照", "相册"};
        new AlertDialog.Builder(MyInfo.this)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                //选择拍照
                                pickImageFromCamera();
                                break;
                            case 1:
                                //选择相册
                                pickImageFromAlbum();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    public void pickImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            mFile = new File(file, System.currentTimeMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    //从相册获取图片
    public void pickImageFromAlbum() {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(Uri.fromFile(mFile), "image/*");
                    startActivityForResult(intent, CUT_PHOTO);
                    break;
                case CHOOSE_PHOTO:

                    if (data == null || data.getData() == null) {
                        return;
                    }
                    try {
                        Bitmap bm = null;
                        Uri originalUri = data.getData();        //获得图片的uri
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);        //显得到bitmap图片
                        //获取图片的路径：
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        path = cursor.getString(column_index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setPicToView(data);
                    break;
                case CUT_PHOTO:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
            }
        }
    }

    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            mBitmap = bundle.getParcelable("data");
            if (mFile != null) {
                path = mFile.getPath();
            }
            final BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                public void done(BmobException e) {
                    if (e == null) {
                        User p=BmobUser.getCurrentUser(User.class);
                        p.setUrl(bmobFile.getFileUrl());
                        p.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(MyInfo.this, "添加成功！", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MyInfo.this, "修改失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        MyTask task = new MyTask();
                        task.execute(bmobFile.getFileUrl());
                        Toast.makeText(MyInfo.this, "上传成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyInfo.this, "上传失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        URL myUrl;
        try {
            myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    class MyTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String url = arg0[0];
            Bitmap bm = getHttpBitmap(url);
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            iv.setImageBitmap(result);
        }
    }
}







