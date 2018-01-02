package soafinal.tourtest;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Main extends AppCompatActivity {
    private EditText keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

public void search(View view){
    keyword= (EditText) findViewById(R.id.key);
    String key = keyword.getText().toString().trim();
    Intent intent = new Intent(Main.this,Attaction.class);
    intent.putExtra("key",key);
    startActivity(intent);
}

    public void myInfo(View view) {
        Intent intent = new Intent();
        intent.setClass(Main.this, MyInfo.class);
        startActivity(intent);
    }
    public void map(View view){
        Intent intent = new Intent();
        intent.setClass(Main.this,Map.class);
        startActivity(intent);
    }
    public void navi(View view){
        Intent intent = new Intent();
        intent.setClass(Main.this,Navigation.class);
        startActivity(intent);
    }
}