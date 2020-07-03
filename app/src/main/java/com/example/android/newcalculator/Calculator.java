package com.example.android.newcalculator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class Calculator extends Activity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener{
    // EditText et;
    EditText tv;
    TextView tv1;

    Button bEquals,bClear, bDel;
    Button bsin,bcos,btan,blog,bpow,bsqr,bcube,bpi,bLeftBracket,bRightBracket;
    ImageButton imageButton;
    DecimalFormat df = new DecimalFormat("###.#");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);
        tv=findViewById(R.id.tv);
        tv.setShowSoftInputOnFocus(false);
        tv1=findViewById(R.id.tv1);

        //Buttons
        bClear=findViewById(R.id.clear);
        bEquals=findViewById(R.id.equals);
        bDel=findViewById(R.id.del);
        imageButton=findViewById(R.id.dot);
        bsin=findViewById(R.id.sin);
        bcos=findViewById(R.id.cos);
        btan=findViewById(R.id.tan);
        blog=findViewById(R.id.log);
        bpi=findViewById(R.id.pi);
        bpow=findViewById(R.id.power);
        bsqr=findViewById(R.id.square);
        bcube=findViewById(R.id.cube);
        bLeftBracket=findViewById(R.id.left_bracket);
        bRightBracket=findViewById(R.id.right_bracket);

        //OnClick Listeners
        tv.setOnClickListener(this);
        bClear.setOnClickListener(this);
        bEquals.setOnClickListener(this);


        //Delete last character
        bDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                tv.setText(tv.getText().delete(tv.getText().length()-1,tv.getText().length()));
                setTextNum(view);
            }catch(Exception e){}

            }
        });
    }
    //String num = tv.getText().toString();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //Menu for changing theme
    public void showPopup(View v){
        PopupMenu pop = new PopupMenu(this,v);
        MenuInflater m =  getMenuInflater();
        pop.setOnMenuItemClickListener(this);
        m.inflate(R.menu.menu,pop.getMenu());
        pop.show();
    }
    public boolean onMenuItemClick(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.black:

              break;
            case R.id.white:
                this.setTheme(R.style.AppTheme);
                break;
        }
        return true;
    }

    public void setTextNum (View v) {
        try {
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(15);
            Button btnPressed = (Button) v;
            String str = tv.getText().toString();
            Log.i("numbers","string is ["+str+"]");
            if((btnPressed.getId()==R.id.add||btnPressed.getId()==R.id.div||btnPressed.getId()==R.id.mult||btnPressed.getId()==R.id.percent)&&str.equalsIgnoreCase("")) {
                Log.i("numbers", "h");
                tv.setText("");
            }
            //trigonometry
            else if(btnPressed.getId()==(R.id.sin)||btnPressed.getId()==R.id.cos||btnPressed.getId()==R.id.tan||btnPressed.getId()==R.id.log)
            {
                tv.setText(str+btnPressed.getText()+"(");
            }
            else if(btnPressed.getId()==R.id.pi){
                tv.setText(str+Math.PI);
            }
            else if(btnPressed.getId()==R.id.square){
                String s= tv.getText().toString();
                Double d=Math.pow(Double.parseDouble(s),2);
                tv.setText(d.toString());
            }
            else if(btnPressed.getId()==R.id.cube){
                String s= tv.getText().toString();
                Double d=Math.pow(Double.parseDouble(s),3);
                tv.setText(str+d.toString());
            }
            else if(btnPressed.getId()==R.id.power){

                tv.setText(str+"^");
            }
            else if(btnPressed.getId() != R.id.del)
            {
                tv.setText(str + btnPressed.getText().toString());
            }

            String[] numArray = tv.getText().toString().split("(?<=[-+*/])|(?=[-+*/])");

            //Debugging
            for (int i = 0; i < numArray.length; i++) {
                Log.i("numbers", "index " + i + " " + numArray[i]);
                if(numArray[i].equals("+")||numArray[i].equals("-")||numArray[i].equals("/")||numArray[i].equals("*")){
                    try {
                        if (numArray[i + 1].equals("+") || numArray[i + 1].equals("-") || numArray[i + 1].equals("*") || numArray[i + 1].equals("/")) {
                            tv.getText().delete(tv.getText().length() - 2, tv.getText().length()-1);
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
            //percent
            for(int i=0;i<numArray.length;i++) {
                if(numArray[i].contains("%")) {
                    Double b=1.0;
                    Double a = Double.parseDouble(numArray[i].substring(0,numArray[i].indexOf('%')));
                    Double result = a/100;
                    b=result;
                    if(numArray[i-1].equalsIgnoreCase("+")||numArray[i-1].equalsIgnoreCase("-")||numArray[i-1].equalsIgnoreCase("/")){
                        result = b*Double.parseDouble(numArray[i-2]);
                        numArray[i]=result.toString();
                        Log.i("numbers", "hello "+result);
                    }

                }

                }
            //Square root
            for (int i = 0; i < numArray.length; i++) {
                if (numArray[i].contains("√")) {

                    Double b=1.0;
                    Double a = Double.parseDouble(numArray[i].substring(numArray[i].indexOf('√')+1));
                    if(numArray[i].charAt(0)!='√') {
                        b = Double.parseDouble(numArray[i].substring(0, numArray[i].indexOf('√')));

                    }
                    Double result = b*Math.sqrt(a);
                    numArray[i]=result.toString();
                    if(result==Math.floor(result)){
                        tv1.setText(df.format(result));
                    }
                    else{
                        tv1.setText(""+result);
                    }
                }
            }
            Double result = Double.parseDouble(numArray[0]);
            //Addition
            for (int i = 0; i < numArray.length - 1; i++) {
                if (numArray[i].equalsIgnoreCase("+")) {
                    result = result + Double.parseDouble(numArray[i + 1]);
                    if (result == Math.floor(result)) {
                        tv1.setText(df.format(result));
                    } else {
                        tv1.setText("" + result);
                    }
                }
            }
            //Subtraction
            for (int i = 0; i < numArray.length - 1; i++) {
                if (numArray[i].equalsIgnoreCase("-")) {
                    result = result - Double.parseDouble(numArray[i + 1]);
                    if (result == Math.floor(result)) {
                        tv1.setText(df.format(result));
                    } else {
                        tv1.setText("" + result);
                    }
                }
            }
            //Multiplication
            for (int i = 0; i < numArray.length - 1; i++) {
                if (numArray[i].equalsIgnoreCase("*")) {
                    result = result * Double.parseDouble(numArray[i + 1]);
                    if (result == Math.floor(result)) {
                        tv1.setText(df.format(result));
                    } else {
                        tv1.setText("" + result);
                    }
                }
            }
            //Division
            for (int i = 0; i < numArray.length - 1; i++) {
                if (numArray[i].equalsIgnoreCase("/")) { Log.i("numbers", "hello");
                    result = result / Double.parseDouble(numArray[i + 1]);
                    if (result == Math.floor(result)) {
                        tv1.setText(df.format(result));
                    } else {
                        tv1.setText("" + result);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void onClick(View v) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(30);
        //Cursor
        if(v.getId()==R.id.tv) {
            tv.requestFocus();
        }
        tv.setCursorVisible(true);
        //ClearAll
        if (v.getId() == R.id.clear) {
            tv.setText(null);
            tv1.setText(null);
        }
        try {
            String[] numArray = tv.getText().toString().split("(?<=[-+*/%])|(?=[-+*/%])|(?=(%))");
            //Equals
            if (v.getId() == R.id.equals) {
                tv1.setText(null);
                    //sin
                     for (int i = 0; i < numArray.length; i++) {
                    if (numArray[i].contains("sin")) {
                        Log.i("numbers", "I am in sin");
                        String s = numArray[i].substring(numArray[i].indexOf("(") + 1, numArray[i].indexOf(")"));
                        Log.i("numbers", "I am in sin and number is" + s);
                        Double a = Math.sin(Math.toRadians(Double.parseDouble(s)));
                        Log.i("numbers", "I am in sin and number is" + a);
                        numArray[i] = a.toString();
                        tv.setText(numArray[i]);
                    }
                }
                    //cos
                     for (int i = 0; i < numArray.length; i++) {
                        if (numArray[i].contains("cos")) {
                            Log.i("numbers", "I am in cos");
                            String s = numArray[i].substring(numArray[i].indexOf("(") + 1, numArray[i].indexOf(")"));
                            Log.i("numbers", "I am in cos and number is" + s);
                            Double a = Math.cos(Math.toRadians(Double.parseDouble(s)));
                            Log.i("numbers", "I am in cos and number is" + a);
                            numArray[i] = a.toString();
                            tv.setText(numArray[i]);
                        }
                    }
                   //tan
                     for (int i = 0; i < numArray.length; i++){
                    if(numArray[i].contains("tan")){
                        Log.i("numbers","I am in tan");
                        String s =numArray[i].substring(numArray[i].indexOf("(")+1,numArray[i].indexOf(")"));
                        Log.i("numbers","I am in tan and number is"+s);
                        Double a =Math.tan(Math.toRadians(Double.parseDouble(s)));
                        Log.i("numbers","I am in tan and number is"+a);
                        numArray[i]=a.toString();
                        tv.setText(numArray[i]);
                    }
                }
                     //log
                  for (int i = 0; i < numArray.length; i++){
                    if(numArray[i].contains("log")){
                        Log.i("numbers","I am in log");
                        String s =numArray[i].substring(numArray[i].indexOf("(")+1,numArray[i].indexOf(")"));
                        Log.i("numbers","I am in log and number is"+s);
                        Double a =Math.log10(Double.parseDouble(s));
                        Log.i("numbers","I am in log and number is"+a);
                        numArray[i]=a.toString();
                        tv.setText(numArray[i]);
                    }
                }
                  //power
                for (int i = 0; i < numArray.length; i++){
                    if(numArray[i].contains("^")){
                        Log.i("numbers","hi");
                        Log.i("numbers",""+numArray[i].indexOf("^"));
                        Double d = Double.parseDouble(numArray[i].substring(0,numArray[i].indexOf("^")));
                        Log.i("numbers",""+d.toString());
                        Double d2 =Double.parseDouble(numArray[i].substring(numArray[i].indexOf("^")+1));
                        Log.i("numbers",""+d2.toString());
                        Double res=Math.pow(d,d2);
                        numArray[i]=res.toString();
                        tv.setText(res.toString());
                    }
                }


                //Debugging
                for (int i = 0; i < numArray.length; i++) {
                    Log.i("numbers", numArray[i]);
                }
                //Percent
                for (int j = 0; j <numArray.length; j++) {
                    if (numArray[j].equals("%")) {
                        Double a = Double.parseDouble(numArray[j - 1]);
                        a = a / 100;
                        if(numArray[1].equals("%"))
                        {
                            tv.setText(String.valueOf(a));
                        }
                        Log.i("numbers", "hello at" +a);
                        numArray[j-1]=a.toString();
                        if(numArray[j-2].equals("-")||numArray[j-2].equals("+")||numArray[j-2].equals("/"))
                        {
                            Double d =  Double.parseDouble(numArray[j-3])*Double.parseDouble(numArray[j-1]);
                            numArray[j-1]=d.toString();
                        }
                    }
                }
                //sqrt
                for (int i = 0; i < numArray.length; i++) {
                    if (numArray[i].contains("√")) {
                        Double b=1.0;
                        Double a = Double.parseDouble(numArray[i].substring(numArray[i].indexOf("√")+1));
                        if(numArray[i].charAt(0)!='√'){
                            b = Double.parseDouble(numArray[i].substring(0,numArray[i].indexOf("√")));
                            Log.i("numbers", "hello2"+b);
                        }
                        Double result =b*Math.sqrt(a);
                        numArray[i]=result.toString();
                        if(result==Math.floor(result)){
                            tv.setText(df.format(result));
                        }
                        else{
                            tv.setText(""+result);
                        }
                    }
                }
                    Double result = Double.parseDouble(numArray[0]);
                    //Basic operations
                    //Addition
                    for (int i = 0; i < numArray.length; i++) {
                        if (numArray[i].equals("+"))
                        {
                            result = result + Double.parseDouble(numArray[i + 1]);
                            if (result == Math.floor(result))
                            {
                                tv.setText(df.format(result));
                                Log.i("numbers", "hello"+ result);
                            }
                            else
                            {
                                tv.setText(""+result);
                            }
                        }
                    }
                    //Subtraction
                    for (int i = 0; i < numArray.length; i++) {

                        if (numArray[i].equals("-")) {
                            result = result - Double.parseDouble(numArray[i + 1]);
                            if (result == Math.floor(result)) {
                                tv.setText(df.format(result));
                                Log.i("numbers", String.valueOf(result));
                            } else {
                                tv.setText(""+result);
                            }
                        }
                    }
                    //multiplication
                    for (int i = 0; i < numArray.length; i++) {
                        if (numArray[i].equals("*")) {
                            result = result * Double.parseDouble(numArray[i + 1]);

                            if (result == Math.floor(result)) {
                                tv.setText(df.format(result));
                                Log.i("numbers", String.valueOf(result));
                            } else {
                                tv.setText(""+result);
                            }
                        }
                    }
                    //division
                    for (int i = 0; i < numArray.length; i++) {
                        if (numArray[i].equals("/"))
                        {
                            result = result / Double.parseDouble(numArray[i + 1]);
                            if (result == Math.floor(result)) {
                                tv.setText(df.format(result));
                                Log.i("numbers", String.valueOf(result));
                            } else {
                                tv.setText(""+result);
                            }

                        }
                    }
                }

        }
        catch (Exception e) {
            Toast.makeText(this,"Bad expression"+e,Toast.LENGTH_SHORT).show();
        }
    }
}
