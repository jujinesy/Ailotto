package com.lotto.ai.ailotto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    int AI_Point;

    Button[] btn;
    Button btn1, btn2, btn3;
    //랜덤 seed 변수 선언
    long rndSeed;

    @Override
    protected void onStart() {
        //Toast.makeText(getApplicationContext(),"onStart() Call.",Toast.LENGTH_LONG).show();
        if( init() == 0 )
        {
            AI_Point = 10;
            SavePoint(AI_Point);
        } else {
            AI_Point = LoadPoint();
        }
        TextView textFruit = (TextView) findViewById(R.id.numbers);
        textFruit.setText(String.valueOf(AI_Point));
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        TextView textFruit = (TextView) findViewById(R.id.numbers);
        textFruit.setText(String.valueOf(AI_Point));
//        Button button1;
//
//
//        button1=(Button)findViewById(R.id.button);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView textFruit = (TextView) findViewById(R.id.numbers);
//                textFruit.setText(AI_Point);
//            }
//        });

        rndSeed =  System.currentTimeMillis();
        //--- 로또 번호가 담길 버튼 객체 얻기 --------//
        btn = new Button[6];
        btn[0] = (Button)findViewById(R.id.Button01);
        btn[1] = (Button)findViewById(R.id.Button02);
        btn[2] = (Button)findViewById(R.id.Button03);
        btn[3] = (Button)findViewById(R.id.Button04);
        btn[4] = (Button)findViewById(R.id.Button05);
        btn[5] = (Button)findViewById(R.id.Button06);
        btn1 = (Button)findViewById(R.id.Button07);
        btn2 = (Button)findViewById(R.id.Button08);
        btn3 = (Button)findViewById(R.id.Button09);
        //생성과, 초기화 버튼 리스너 등록
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

        if(v == btn2){
            TextView textFruit = (TextView) findViewById(R.id.numbers);

            if(Integer.parseInt((String) textFruit.getText()) == 0 ) {
                Toast.makeText(getApplicationContext(),"AI포인트를 충전해 주세요.",Toast.LENGTH_LONG).show();
//                new AlertDialog.Builder(this)
//                        .setTitle("AI 포인트 부족")
//                        .setMessage("AI 포인트를 충전해주세요.")
//                        .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dlg, int sumthin) {
//// 기본적으로 창은 닫히고 추가 작업은 없다(닫히면서 행해지는 것)
//                            }
//                        })
//                        .show();

            } else {
                //버튼 객체 배열 사이즈 만큼 루프를 돌면서
                //랜덤값을 뽑아내고 각각의 버튼에 숫자를 넣어준다.
                for(int i = 0;  i < btn.length; i++){
                    int number = getRand(0, 46);
                    //number을 문자열로 캐스팅~
                    btn[i].setText(String.valueOf(number));
                }
                textFruit.setText(String.valueOf(--AI_Point));
                SavePoint(AI_Point);
            }
        }else if(v == btn1){
            System.exit(0);
        }else if(v == btn3){
            showIAP();
//            if (requestPayment()) {
//                Toast.makeText(getApplicationContext(), "Request Success", Toast.LENGTH_LONG)
//                        .show();
//            } else {
//                Toast.makeText(getApplicationContext(), "Request Failure", Toast.LENGTH_LONG)
//                        .show();
//            }
        }
    }
    //---랜덤난수~ 원하는 숫자에서~ 숫자만큼의 사이에서 발생 시키는 method!---//
    public int setRandSeed(){
        rndSeed = (rndSeed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int)((rndSeed >> (48 - 32)) & 2147483647);
    }
    public int getRand(int a, int b){
        return ((setRandSeed() % (b - a)) + a);
    }

    private void showIAP() {
        this.startActivityForResult(new Intent(((Context)this), BuyActivity.class), getResources().getInteger(R.integer.BuyActivity));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == getResources().getInteger(R.integer.BuyActivity) && resultCode == 200) {
            this.AI_Point += data.getIntExtra("AI", 0);
            TextView textFruit = (TextView) findViewById(R.id.numbers);
            textFruit.setText(String.valueOf(AI_Point));
            SavePoint(AI_Point);
        }
    }

    private int SavePoint(int point){
        SharedPreferences pref = getSharedPreferences("AI", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("point", point).commit();
        return point;
    }
    private int LoadPoint(){
        SharedPreferences pref = getSharedPreferences("AI",Activity.MODE_PRIVATE);
        return pref.getInt("point",0);
        //Toast.makeText(getApplicationContext(),"Load Score : "+score,Toast.LENGTH_LONG).show();
    }

    private int init(){
        SharedPreferences pref = getSharedPreferences("AI",Activity.MODE_PRIVATE);
        if( pref.getInt("init",0) == 0 )
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("init", 1).commit();
            return  0;
        }
        return 1;
        //Toast.makeText(getApplicationContext(),"Load Score : "+score,Toast.LENGTH_LONG).show();
    }
}
