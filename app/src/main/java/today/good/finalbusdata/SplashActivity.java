package today.good.finalbusdata;



import android.content.Intent;

import android.os.Bundle;



// https://yongtech.tistory.com/100 참고

// 로고화면 띄우는 class

public class SplashActivity extends MainActivity {



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        // 0.7초동안 유지되는 첫 로고 화면

        try {

            Thread.sleep(700);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }



        startActivity(new Intent(this, SelectcityActivity.class));

        finish();



    }

}