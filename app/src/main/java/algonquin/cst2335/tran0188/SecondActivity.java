package algonquin.cst2335.tran0188;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    EditText numberInput;
    String dialNumber;

    @Override
    protected void onPause() {
        super.onPause();

        Log.w( "SecondActivity", "onPause() - The application no longer responds to user input" );
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String phoneNumber = prefs.getString("PhoneNumber", null);

        numberInput = findViewById(R.id.editTextPhone);
        numberInput.setText(phoneNumber);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("PhoneNumber", dialNumber);

        editor.apply();
    }

    ImageView currentImage;

    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Bitmap thumbnail = data.getParcelableExtra("data");

                        try {
                            FileOutputStream file = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, file);

                            file.flush();

                            file.close();
                        }

                        catch (FileNotFoundException e) {
                                e.printStackTrace();
                        }

                        catch (IOException ioe){
                            Log.w("IOException", "Can't output PNG");
                        }


                        currentImage = findViewById(R.id.imageView2);

                        currentImage.setImageBitmap(thumbnail);


                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        TextView welcomeText = findViewById(R.id.topTextView);

        welcomeText.setText("Welcome Back " + emailAddress);

        Button callButton = findViewById(R.id.callButton);

        callButton.setOnClickListener( phone -> {

            EditText numberInput = findViewById(R.id.editTextPhone);

            dialNumber = numberInput.getText().toString();

            Intent call = new Intent(Intent.ACTION_DIAL);

            call.setData(Uri.parse("tel: " + dialNumber));

            startActivity(call);

        });

        Button camera = findViewById(R.id.cameraButton);

        camera.setOnClickListener( pic -> {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraResult.launch(cameraIntent);
        });

       File file = new File(getFilesDir(), "Picture.png");
        currentImage = findViewById(R.id.imageView2);

        if(file.exists()) {

        Bitmap savedImage = BitmapFactory.decodeFile(file.toString());

        currentImage.setImageBitmap(savedImage);
       }


    }


}