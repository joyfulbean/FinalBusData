package today.good.finalbusdata;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SelectcityActivity extends AppCompatActivity{


    //TTS 변수
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 1. 레이아웃을 정의한 레이아웃 리소스(R.layout)을 사용하여 현재 액티비티의 화면을 구성하도록 합니다.
        setContentView(R.layout.activity_selectcity);



        // 2. 레이아웃 파일에 정의된 ListView를 자바 코드에서 사용할 수 있도록 합니다.
        // findViewById 메소드는 레이아웃 파일의 android:id 속성을 이용하여 뷰 객체를 찾아 리턴합니다.
        ListView listview = (ListView)findViewById(R.id.listview9);


        // 3. 실제로 문자열 데이터를 저장하는데 사용할 ArrayList 객체를 생성합니다.
        final ArrayList<String> list = new ArrayList<>();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                    //tts.setLanguage(Locale.ENGLISH);
                }
            }
        });


        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void run() {
                //Log.d("디버깅2", stationKey.get(0) + "" + stationName.get(0));

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //http://stackoverflow.com/a/29777304 참고
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ttsGreater21("지역을 선택해주세요.");
                        } else {
                            ttsUnder20("지역을 선택해주세요.");
                        }
                    }
                });
            }
        }).start();


        // 4. ArrayList 객체에 데이터를 집어넣습니다.
        list.add("서울특별시");
        list.add("경기도");
        list.add("포항시");


        // 5. ArrayList 객체와 ListView 객체를 연결하기 위해 ArrayAdapter객체를 사용합니다.
        // 우선 ArrayList 객체를 ArrayAdapter 객체에 연결합니다.
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, //context(액티비티 인스턴스)
                R.layout.single_list_item_1, // 한 줄에 하나의 텍스트 아이템만 보여주는 레이아웃 파일
                // 한 줄에 보여지는 아이템 갯수나 구성을 변경하려면 여기에 새로만든 레이아웃을 지정하면 됩니다.
                list  // 데이터가 저장되어 있는 ArrayList 객체
        );



        // 6. ListView 객체에 adapter 객체를 연결합니다.
        listview.setAdapter(adapter);

        tts.stop();


        // 7. ListView 객체의 특정 아이템 클릭시 처리를 추가합니다.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // 8. 클릭한 아이템의 문자열을 가져와서
                String selected_item = (String) adapterView.getItemAtPosition(position);

                if (selected_item.equals(list.get(0))) {
                    Intent intent = new Intent(getApplicationContext(), FindSeoulActivity.class);
                    startActivity(intent);
                }
                else if(selected_item.equals(list.get(1))){
                    Log.d("경기도", selected_item);

                    Intent intent2 = new Intent(getApplicationContext(), FindBusStationGGD.class);
                    startActivity(intent2);
                }
                else if(selected_item.equals(list.get(2))){
                    Intent intent2 = new Intent(getApplicationContext(), FindBusStation.class);
                    startActivity(intent2);

                }
            }
        });

    }


    //API 버전 20 아래용
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        //해쉬맵 만들기
        HashMap<String, String> map = new HashMap<>();
        //그냥 음성인식기술 해쉬맵에 넣어주는 코드
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        //text 파라미터로 전달받은 인자를 해쉬맵이랑 연결해서 음성으로 바꿔서 말해줌
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    //API버전 21 이상용
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        //text 파라미터로 전달받은 인자를 음성으로 바꿔서 말해줌
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }


}