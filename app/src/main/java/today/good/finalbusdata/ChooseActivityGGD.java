package today.good.finalbusdata;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ChooseActivityGGD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ggd);

        //https://ande226.tistory.com/141
        //ActionBar로 뒤로가기 버튼 추가
        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("뒤로가기");  //액션바 제목설정
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        Log.d("경기도", "경기도야");

    }

    // 5분 뒤에 도착하는 모든 버스 버튼을 눌렀을 때 실행됨
    public void totalbus(View view) {
        Log.d("경기도", "보낼준비");

        // MainActivity 와 연결
        Intent intent = new Intent(getApplicationContext(), MainActivityGGD.class);
        Log.d("경기도", "보냄");

        startActivity(intent);
    }

    // 특정 버스의 정보만 듣기 버튼을 눌렀을 때 실행
    public void specificbus(View view) {
        // 버스 리스트뷰와 연결
        Intent intent = new Intent(getApplicationContext(), ListViewActivityGGD.class);
        startActivity(intent);
    }
}