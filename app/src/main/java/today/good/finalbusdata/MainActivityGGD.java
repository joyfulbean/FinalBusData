package today.good.finalbusdata;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivityGGD extends Activity {

    //기본 위젯들
    //EditText editBusStop;
    TextView text;
    Button button;
    /**TextView gpsdata;
     TextView stationdata;
     TextView internet;*/

    ArrayList<Integer> predictTimes = new ArrayList<Integer>();
    ArrayList<String> routeNames = new ArrayList<String>();
    ArrayList<String> routeIds = new ArrayList<String>();


    //공공데이터 API 사용을 위한 키값
    String key = "vrJZqsh%2BKv7FlarxXBLEYW4UQX1wPAkf%2F8dXw0RWsgFNNONsQqS%2Bc%2BigEfCod9rbDQpKAdaYTRfFuBwMtu3nBA%3D%3D";

    //공공데이터 API에서 가져오는 데이터
    String data;
    String data1;

    //버스정류장 id와 관련된 변수
    String stationName = "null";
    String stationRealName = "null";

    //TTS 변수
    TextToSpeech tts;

    //경도와 위도 변수 선언
    double longitude = 0.0;
    double latitude = 0.0;

    int minute = 0;
    // 5분 이내에 오는 버스만 읽고 출력하도록 check 라는 boolean 함수를 선언
    boolean check = true;

    XmlPullParser xpp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ggd);

        //변수 레이아웃 연결
        //editBusStop = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text_ggd);
        /**gpsdata = findViewById(R.id.gps);
         stationdata = findViewById(R.id.bus_station);
         internet = findViewById(R.id.internet);*/

        //button = findViewById(R.id.button);


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

//        바로 음성이 나와야 되니까 버튼 필요 없어서 주석으로 뺐습니다.
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
        //public void onClick(View v) {
//
//                switch( v.getId() ){
//                    case R.id.button2:

        tts.stop();
        Log.d("경기도", "ㄷㄹㅇ");

        getArrivalData();

        // http://blog.naver.com/PostView.nhn?blogId=ssarang8649&logNo=220947884163 참고
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                routeIds.clear();
                routeNames.clear();
                predictTimes.clear();
                getArrivalData();
            }
        };

        // 버스도착예정시간을 새로고침하기 위한 타이머 _ 1분 간격으로 업데이트되도록 함.
        Timer timer = new Timer();
        timer.schedule(tt, 60000, 60000);

        //editText창에 찾아낸 현재 버스정류장 이름 + 번호 띄워줌. --> 디버깅용
        //editBusStop.setText(FindBusStation.sName + " " + FindBusStation.sKey);

    }


    // TextView 에 도착예정 버스를 set 해주고 읽어주는 함수
    private void getArrivalData() {

        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void run() {
                //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                getXmlData();
                //버스번호를 알아오기 위해서 api사용
                getXmlData2();
                Log.d("배열 크기 비교",routeIds.size() + " " + routeNames.size() + " " + predictTimes.size());

                data = "";
                Collections.sort(predictTimes);
                for(int i=0;i<routeNames.size();i++) {
                    if(predictTimes.get(i) > 10){
                        predictTimes.remove(i);
                        routeNames.remove(i);
                        routeIds.remove(i);
                    }
                    else data += predictTimes.get(i) + "분 뒤에 " + routeNames.get(i) + "번 버스가 도착합니다.\n\n";
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //stationdata.setText(FindBusStation.sKey + " " + FindBusStation.sName);

                        /**if(longitude != 0.0 && latitude != 0.0) {
                         gpsdata.setText(longitude + "\n" + latitude + "");
                         } else {
                         gpsdata.setText("gps를 가져오지 못함");
                         }*/

                        /**if(xpp == null) {
                         internet.setText("인터넷 널값");
                         } else {
                         internet.setText("인터넷 연결 성공");
                         }*/



                        //data가 비었을 경우 도착 정보가 없음 표시
                        if(data.contentEquals("")) {
                            text.setText("10분 이내에 도착하는 버스가 없습니다.");
                        } else {
                            //TextView에 문자열 data 출력
                            text.setText(data);
                        }
                        //http://stackoverflow.com/a/29777304 참고
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if(data.contentEquals("")) {
                                ttsGreater21("10분 이내에 도착하는 버스가 없습니다.");
                            } else {
                                ttsGreater21(data);
                            }
                        } else {
                            if (data.contentEquals("")) {
                                ttsUnder20("10분 이내에 도착하는 버스가 없습니다.");
                            } else {
                                ttsUnder20(data);
                            }
                        }
                    }
                });
            }
        }).start();

    }

    //https://movie13.tistory.com/1 --> 공공기관 데이터 가져오는 법 레퍼런스
    private String getXmlData() {
        //버퍼 변수 선언, 스트링형으로 만들어져있음, 모든 정보 저장후 한번에 버퍼 출력 하는 형식
        StringBuffer buffer = new StringBuffer();

        //공공기관 데이터 가져오는 url형식, stationName에 nodeid를 GPS기능으로 찾아서 저장하면, 그 정보를 이용해서
        //도착정보 조회서비스의 정류소별 도착 예정 정보 목록 조회 API를 검색해서 버스 정보를 가져오기 위해, url만들기
        String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busarrivalservice/station?"//요청 URL
                + "serviceKey=" + key + "&stationId=" + FindBusStationGGD.sCode.substring(3);
        Log.d("경기도", FindBusStationGGD.sCode.substring(3));

        try {
            //문자열로 된 요청 url을 URL 객체로 생성.
            URL url = new URL(queryUrl);

            //url위치로 입력스트림 연결
            InputStream is = url.openStream();

            //XML형식의 API를 파싱하는 라이브러리
            //XML형식은 순차적으로 발생하기때문에 뒤로 못돌아가서 필요하면 변수에 미리 저장해둬야함.
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
            //inputstream 으로부터 xml 입력받기
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            //XML 안에 들어가는 태그
            String tag;

            //태그 하나 내려감
            xpp.next();
            int eventType = xpp.getEventType();

            //XML 끝이 아니라면
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //파싱 시작알리기, 이벤트가 문서의 시작
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    //태그의 시작
                    case XmlPullParser.START_TAG:
                        //태그 이름 얻어오기
                        tag = xpp.getName();
                        // 첫번째 검색결과
                        if (tag.equals("busArrivalList")) ;
                            //태그의 이름이 arrtime이면,
                        else if (tag.equals("predictTime1")) {
                            //buffer.append("도착예정버스 도착예상시간[초] : ");
                            //그 옆의
                            xpp.next();
                            //초를 가져와서 분으로 바꿈
                            minute = Integer.parseInt(xpp.getText());
                            Log.d("분",minute+"");

                            predictTimes.add(minute);

                            //다시 반복문 올라갔다가, item 태그의 이름별로 찾음.
                        }  else if (tag.equals("routeId")) {
                            //buffer.append("정류소명 :");
                            xpp.next();
                            routeIds.add(xpp.getText());
                            Log.d("아이디",xpp.getText());
                            //... 아런식으로 반복해서 API에서 필요한 정보 변수에 저장
                        }
                        /*else if (tag.equals("routeno")) {
                            if (check) {
                                //buffer.append("버스번호 :");
                                xpp.next();
                                buffer.append(xpp.getText() + "번 버스가 도착합니다");
                                buffer.append("\n");
                            }
                        }*/ /** else if (tag.equals("vehicletp")) {
                     buffer.append("도착예정버스 차량유형은 :");
                     xpp.next();
                     buffer.append(xpp.getText());//
                     buffer.append("\n");
                     } */

                        break;

                    //태그의 시작과 끝 사이에서 나타난다. 예: <data>여기서 텍스트 이벤트 발생</data>
                    case XmlPullParser.TEXT:
                        break;

                    //이벤트가 문서의 끝
                    case XmlPullParser.END_TAG:
                        //태그 이름 얻어오기
                        tag = xpp.getName();
                        // 첫번째 검색결과종료..줄바꿈
                        if (tag.equals("item") && check) buffer.append(" \n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            // Auto-generated catch blocke.printStackTrace();
        }
        //buffer.append("파싱 끝\n");
        //StringBuffer 문자열 객체 반환
        return buffer.toString();
    }

    private String getXmlData2() {
        //버퍼 변수 선언, 스트링형으로 만들어져있음, 모든 정보 저장후 한번에 버퍼 출력 하는 형식
        StringBuffer buffer = new StringBuffer();

        for(int i=0;i<routeIds.size();i++) {

            //공공기관 데이터 가져오는 url형식, stationName에 nodeid를 GPS기능으로 찾아서 저장하면, 그 정보를 이용해서
            //도착정보 조회서비스의 정류소별 도착 예정 정보 목록 조회 API를 검색해서 버스 정보를 가져오기 위해, url만들기
            String queryUrl = "http://openapi.gbis.go.kr/ws/rest/busrouteservice/info?"//요청 URL
                    + "serviceKey=" + key + "&routeId=" + routeIds.get(i);
            Log.d("url", queryUrl);

            try {
                //문자열로 된 요청 url을 URL 객체로 생성.
                URL url = new URL(queryUrl);

                //url위치로 입력스트림 연결
                InputStream is = url.openStream();

                //XML형식의 API를 파싱하는 라이브러리
                //XML형식은 순차적으로 발생하기때문에 뒤로 못돌아가서 필요하면 변수에 미리 저장해둬야함.
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                xpp = factory.newPullParser();
                //inputstream 으로부터 xml 입력받기
                xpp.setInput(new InputStreamReader(is, "UTF-8"));

                //XML 안에 들어가는 태그
                String tag;

                //태그 하나 내려감
                xpp.next();
                int eventType = xpp.getEventType();

                //XML 끝이 아니라면
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        //파싱 시작알리기, 이벤트가 문서의 시작
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;

                        //태그의 시작
                        case XmlPullParser.START_TAG:
                            //태그 이름 얻어오기
                            tag = xpp.getName();
                            // 첫번째 검색결과
                            if (tag.equals("busRouteInfoItem")) ;
                                //태그의 이름이 arrtime이면,
                            else if (tag.equals("routeName")) {
                                //buffer.append("도착예정버스 도착예상시간[초] : ");
                                //그 옆의
                                xpp.next();
                                //초를 가져와서 분으로 바꿈
                                Log.d("버스번호",xpp.getText());
                                routeNames.add(xpp.getText());

                            }
                            break;

                        //태그의 시작과 끝 사이에서 나타난다. 예: <data>여기서 텍스트 이벤트 발생</data>
                        case XmlPullParser.TEXT:
                            break;

                        //이벤트가 문서의 끝
                        case XmlPullParser.END_TAG:
                            //태그 이름 얻어오기
                            tag = xpp.getName();
                            // 첫번째 검색결과종료..줄바꿈
                            if (tag.equals("item") && check) buffer.append(" \n");
                            break;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
            }
        }
        return buffer.toString();
    }

    //https://webnautes.tistory.com/847 -->음성인식 기능 레퍼런스
    //무조건 tts를 위해서 onclick할때 override 되어야 오류가 뜨지 않음. 그냥 두면 됨.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            //음성인식 다하면 끝내기
            tts.stop();
            tts.shutdown();
        }
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