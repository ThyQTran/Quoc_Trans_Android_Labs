package algonquin.cst2335.tran0188;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Page that authenticates that a submitted password has an uppercase character, a lowercase
 * character, a number and a special symbol (#$%^&*!@?)
 *
 * @author Quoc Tran
 *
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This variable holds text at the centre of the screen*/
    private TextView tv = null;

    /** This variable holds the text that is entered in the password field*/
    private EditText et = null;

    /** This variable holds the button that when clicked submits the password*/
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(clk ->{
            String password = et.getText().toString();

            if(checkPasswordComplexity(password)){
                tv.setText("Your password meets the requirements");
            }else{
                tv.setText("You shall not pass!");
            };
        });

    }

    /** Checks to see if the String/password has an uppercase letter, a lower case letter, a number and a special symbol (#$%^&*!@?).
     *
     * @param pw The String object that is being checked
     *
     * @return Returns true if password is complex enough and false if password is not complex enough
     */
    private boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;



        for(int i=0; i < pw.length(); i++) {

            char c = pw.charAt(i);
            Log.i("Looking at char:", "" + c);

            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            }

            if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            }

            if (Character.isDigit(c)) {
                foundNumber = true;
            }

            if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

            if(!foundUpperCase){
                Toast.makeText(getApplicationContext(),"Password is missing an uppercase character", Toast.LENGTH_LONG).show();
                return false;
            }else if(!foundLowerCase){
                Toast.makeText(getApplicationContext(),"Password is missing a lowercase character", Toast.LENGTH_LONG).show();
                return false;
            }else if(!foundNumber){
                Toast.makeText(getApplicationContext(),"Password is missing an number character",Toast.LENGTH_LONG).show();
                return false;
            }else if(!foundSpecial) {
                Toast.makeText(getApplicationContext(), "Password is missing an uppercase character(#$%^&*!@?)", Toast.LENGTH_LONG).show();
                return false;
            }else{
                return true;
            }


        //return foundUpperCase && foundLowerCase && foundNumber && foundSpecial;

    }

    /** Checks to see if the character is a special symbol (#$%^&*!@?)
     *
     * @param c The character that is being checked
     *
     *  @return Returns true if the character is a special symbol (#$%^&*!@?) and false otherwise
     */
    private boolean isSpecialCharacter(char c){
        switch(c){
            case'#':
            case'$':
            case'%':
            case'^':
            case'&':
            case'*':
            case'!':
            case'@':
            case'?':
                return true;
            default:
                return false;
        }
    }

}