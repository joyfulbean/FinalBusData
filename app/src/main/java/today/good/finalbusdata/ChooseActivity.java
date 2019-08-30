package today.good.finalbusdata;



import android.content.Intent;

import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;



public class ChooseActivity extends AppCompatActivity {



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose);



        //https://ande226.tistory.com/141

        //ActionBar로 뒤로가기 버튼 추가

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기

        actionBar.setTitle("뒤로가기");  //액션바 제목설정

        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기



    }



    // 5분 뒤에 도착하는 모든 버스 버튼을 눌렀을 때 실행됨

    public void mincall(View view) {

        // MainActivity 와 연결

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);

    }



    // 특정 버스의 정보만 듣기 버튼을 눌렀을 때 실행

    public void specificcall(View view) {

        // 버스 리스트뷰와 연결

        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);

        startActivity(intent);

    }

}