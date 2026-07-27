package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    AppCompatButton[]buttons;
    char[]response=new char[9];
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        buttons=new AppCompatButton[9];
        buttons[0]=findViewById(R.id.btn0);
        buttons[1]=findViewById(R.id.btn1);
        buttons[2]=findViewById(R.id.btn2);
        buttons[3]=findViewById(R.id.btn3);
        buttons[4]=findViewById(R.id.btn4);
        buttons[5]=findViewById(R.id.btn5);
        buttons[6]=findViewById(R.id.btn6);
        buttons[7]=findViewById(R.id.btn7);
        buttons[8]=findViewById(R.id.btn8);

    }

    public void check(View view){
        AppCompatButton currentButton=(AppCompatButton) view;
        if(flag%2==0){
            flag++;
            currentButton.setText("X");
        }else{
            flag++;
            currentButton.setText("A");
        }

        currentButton.setEnabled(false);



        if(flag>=5){
            response[0]=(!(buttons[0].getText().toString().isEmpty())?buttons[0].getText().charAt(0):'\0');
            response[1]=(!(buttons[1].getText().toString().isEmpty())?buttons[1].getText().charAt(0):'\0');
            response[2]=(!(buttons[2].getText().toString().isEmpty())?buttons[2].getText().charAt(0):'\0');
            response[3]=(!(buttons[3].getText().toString().isEmpty())?buttons[3].getText().charAt(0):'\0');
            response[4]=(!(buttons[4].getText().toString().isEmpty())?buttons[4].getText().charAt(0):'\0');
            response[5]=(!(buttons[5].getText().toString().isEmpty())?buttons[5].getText().charAt(0):'\0');
            response[6]=(!(buttons[6].getText().toString().isEmpty())?buttons[6].getText().charAt(0):'\0');
            response[7]=(!(buttons[7].getText().toString().isEmpty())?buttons[7].getText().charAt(0):'\0');
            response[8]=(!(buttons[8].getText().toString().isEmpty())?buttons[8].getText().charAt(0):'\0');


            /**
             * conditions
             */
            boolean winnerFound=false;
            char winner='\0';
            if(response[0]!='\0' && (response[0]==response[1] && response[1]==response[2])){
                Toast.makeText(this,"Winner is "+response[0],Toast.LENGTH_SHORT).show();
            }else if( response[3]!= '\0'&& response[3]==response[4]&& response[4]==response[5]){
                Toast.makeText(this,"Winner is "+response[3],Toast.LENGTH_SHORT).show();
            }else if( response[6]!= '\0'&&response[6]==response[7] && response[7]==response[8]){
                winnerFound=true;
                winner=response[6];
            }else if( response[3]!= '\0'&&response[0]==response[3] && response[3]== response[6]){
                winnerFound=true;
                winner=response[3];

            }else if( response[1]!= '\0'&&response[1]==response[4] && response[4]==response[7]){
                winnerFound=true;
                winner=response[1];
            }else if ( response[2]!= '\0'&&response[2]==response[5] && response[5]==response[8]){
                winnerFound=true;
                winner=response[2];

            }else if( response[0]!= '\0'&&response[0]==response[4] && response[4]==response[8]){
                winnerFound=true;
                winner=response[0];
            }else if( response[2]!= '\0'&&response[2]==response[4] && response[4]==response[8]){
                winnerFound=true;
                winner=response[2];
            }else if(isDraw()){
                Toast.makeText(this,"It's Draw!!",Toast.LENGTH_SHORT).show();
                newGame();
            }

            if(winnerFound){
                Toast.makeText(this,"Winner is "+winner,Toast.LENGTH_SHORT).show();
                try{

                    newGame();

                }catch (Exception t){
                    t.printStackTrace();
                }

            }
        }
    }

    private  void newGame(){
        for (AppCompatButton button : buttons) {
            button.setEnabled(true);
            button.setText("");
        }
        response = new char[9];
        flag = 0;
    }

    private  boolean isDraw(){
        for(char ch:response){
            if(ch=='\0')return  false;
        }
        return  true;
    }
}