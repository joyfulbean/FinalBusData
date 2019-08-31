package today.good.finalbusdata;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SpecificSeoulActivity extends AppCompatActivity {

    TextView textdata;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);

        // Id값 연결
        textdata = findViewById(R.id.busdata);

        Intent intent = getIntent();

        int i = intent.getIntExtra("list_position",0);

        // 0보다 크다면 textdata에 추가해주기
        textdata.append(ListViewSeoulActivity.busnum.get(i) + "번 버스가" + "\n" + ListViewSeoulActivity.busmin.get(i)+ "\n" + ListViewSeoulActivity.busnum.get(i) + " 버스가" + "\n" + ListViewSeoulActivity.busmin2.get(i) + "\n\n");

        //https://ande226.tistory.com/141
        //ActionBar로 뒤로가기 버튼 추가
        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("뒤로가기");  //액션바 제목설정
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

    }
}