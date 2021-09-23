package algonquin.cst2335.tran0188;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


        button.setOnClickListener (vw -> topView.setText("Your edit text has: " + editString));

    }
}