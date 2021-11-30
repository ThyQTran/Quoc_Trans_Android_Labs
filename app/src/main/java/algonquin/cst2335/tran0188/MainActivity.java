package algonquin.cst2335.tran0188;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

    /** This variable holds the text that is entered in the city field where we would like to search for the forecast*/
    private EditText cityText = null;

    /** This variable holds the button that when clicked submits the city we want the forcast for*/
    private Button forecastBtn = null;

    private String stringURL;

    Bitmap image = null;
    String description = null;
    String iconName = null;
    String current = null;
    String min = null;
    String max = null;
    String humidity = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        forecastBtn = findViewById(R.id.forecastButton);
        cityText = findViewById(R.id.cityTextField);



        tv = findViewById(R.id.textView);
//w6        et = findViewById(R.id.editText);
//w6        btn = findViewById(R.id.button);

        forecastBtn.setOnClickListener(clk ->{

            String cityName = cityText.getText().toString();

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Getting forecast")
                    .setMessage("We're calling people in " + cityName + " to look outside their windows and tell us what's the weather like over there.")
                    .setView(new ProgressBar(MainActivity.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();

            newThread.execute( () -> {
                /* This runs in a separate thread */
                try {


                    stringURL = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName,"UTF-8") + "&appid=bdd1b06f072a3498fc5ca8d9a3e87c1d&units=metric&mode=xml";

                    URL url = new URL(stringURL); //Builds the server connection  as the URL passed here is the server you want to connect to
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //Connects to the server
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream()); //Waits for a response from the server

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( in  , "UTF-8");



                    while(xpp.next() != XmlPullParser.END_DOCUMENT){
                        switch(xpp.getEventType()){
                            case XmlPullParser.START_TAG:
                                if(xpp.getName().equals("temperature")){
                                     current = xpp.getAttributeValue(null, "value"); //Gets the current temperature
                                     min = xpp.getAttributeValue(null,"min"); //Gets the min temperature
                                     max = xpp.getAttributeValue(null, "max"); //Gets the max temperature
                                }else if(xpp.getName().equals("weather")){
                                     description = xpp.getAttributeValue(null, "value"); //Gets the weather description
                                     iconName = xpp.getAttributeValue(null, "icon"); //Gets the icon name
                                }else if(xpp.getName().equals("humidity")){
                                     humidity = xpp.getAttributeValue(null, "value");
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                break;
                        }
                    }

                    String text = (new BufferedReader( //Converts the inputStream from the server into Java String object to read JSON name/value pair data in Android
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    int i = 0; //Allows for the test variable to run fully and value of variable to display in debugging console

//                    JSONObject theDocument = new JSONObject(text);

//                   JSONArray weatherArray = theDocument.getJSONArray("weather");
//                    JSONObject position0 = weatherArray.getJSONObject(0);
//                     description = position0.getString("description");
//                     iconName = position0.getString("icon");

//                    JSONObject mainObject = theDocument.getJSONObject( "main" );

//                    double current = mainObject.getDouble("temp");
//                    double min = mainObject.getDouble("temp_min");
//                    double max = mainObject.getDouble("temp_max");
//                    int humidity = mainObject.getInt("humidity");

                    //Downloads URL picture and stores it as a bitmap
 // not new                  Bitmap image = null;


                    File file = new File(getFilesDir(), iconName + ".png");
                    if(file.exists()){
                        image = BitmapFactory.decodeFile(getFilesDir()+"/" + iconName + ".png");
                    }else {
                        URL imgUrl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                        HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {


                            image = BitmapFactory.decodeStream(connection.getInputStream());
                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput( iconName+".png", Activity.MODE_PRIVATE));
                        }
                    }

                    //ImageView iv = findViewById(R.id.icon);
                    //iv.setImageBitmap(image);



                    runOnUiThread(() -> {
                        TextView tv = findViewById(R.id.temp);
                        tv.setText("The current temperature is " + current);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.minTemp);
                        tv.setText("The min temperature is " + min);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.maxTemp);
                        tv.setText("The max temperature is " + max);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.humidity);
                        tv.setText("The humidity is " + humidity + "%");
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.description);
                        tv.setText(description);
                        tv.setVisibility(View.VISIBLE);

                        ImageView iv = findViewById(R.id.icon);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);

                        dialog.hide();
                    });



                        //Saves icon to the device so that next time it doesn't have to be downloaded again
                        FileOutputStream fOut = null;
                        try {
                            fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException ioe) {
                    Log.e("Connection error: ", ioe.getMessage());
//                } catch (JSONException e) {
//                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            } );





//w6            if(checkPasswordComplexity(password)){
//w6                tv.setText("Your password meets the requirements");
//w6            }else{
//w6                tv.setText("You shall not pass!");
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