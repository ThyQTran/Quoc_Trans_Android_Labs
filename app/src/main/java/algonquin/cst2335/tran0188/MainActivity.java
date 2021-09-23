package algonquin.cst2335.tran0188;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //loads xml

        TextView topView = findViewById(R.id.textview);
        topView.setText("Java put this here");
        Button button = findViewById(R.id.mybutton); //uses the id declared in the xml file

        EditText myedit = findViewById(R.id.myedittext);
        String editString = myedit.getText().toString();

        myedit.setText("Your edit has: " + editString);


        button.setOnClickListener (vw -> topView.setText("Your edit text has: " + myedit.getText()));

        CheckBox cb = findViewById(R.id.mycheckbox);
        cb.setOnCheckedChangeListener((btn,isChecked) -> {
            if(isChecked)
                cb.setChecked(true);
            else
                cb.setChecked(false);

            Toast.makeText(MainActivity.this, "You clicked on the Checkbox and it is now: " + isChecked, Toast.LENGTH_LONG).show();

        });

        Switch sw = findViewById(R.id.myswitch);
        sw.setOnCheckedChangeListener((btn,isChecked) -> {

            sw.setChecked(isChecked);

            Toast.makeText(MainActivity.this, "You clicked on the Switch and it is now: " + isChecked, Toast.LENGTH_SHORT).show();

        });

        RadioButton rb = findViewById(R.id.myradio);
        rb.setOnCheckedChangeListener((btn,isChecked) -> {

            rb.setChecked(isChecked);

            Toast.makeText(MainActivity.this, "You clicked on the Radio Button and it is now: " + isChecked, Toast.LENGTH_SHORT).show();

        });

        ImageView myimage = findViewById(R.id.logo_algonquin);

        ImageButton imgbtn = findViewById(R.id.myimagebutton);
        imgbtn.setOnClickListener(btn -> {
            int width = imgbtn.getWidth();
            int height = imgbtn.getHeight();
        });
    }


}