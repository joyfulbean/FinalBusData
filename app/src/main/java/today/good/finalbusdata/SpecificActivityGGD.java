package today.good.finalbusdata;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SpecificActivityGGD extends AppCompatActivity {

    TextView textdata;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_ggd);

        // Id값 연결
        textdata = findViewById(R.id.busdata_ggd);

        // 몇 분 뒤 도착하는지 저장해뒀던 분 두 개 split으로 나누기
        String[] data = ListViewActivity.busWhen[0].split(" ");

        // int형으로 바꿔주기 위해 int형 배열 선언
        int[] dataN = new int[20];

        // 공백이 아니라면 int형으로 바꿔줌
        for (int j = 0; j < data.length; j++) {
            if (!data[j].isEmpty()) {
                dataN[j] = Integer.parseInt(data[j]);

            }
            Arrays.sort(dataN);
        }

        // 0보다 크다면 textdata에 추가해주기
        for(int i = 0; i < dataN.length; i++) {
            if(dataN[i] > 0) {
                textdata.append(ListViewActivityGGD.busrealnumGGD.get(i) + "번 버스가" + "\n" + dataN[i] + "분 뒤에 도착합니다.\n\n");
            }
        }

        //https://ande226.tistory.com/141
        //ActionBar로 뒤로가기 버튼 추가
        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setTitle("뒤로가기");  //액션바 제목설정
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

    }
}