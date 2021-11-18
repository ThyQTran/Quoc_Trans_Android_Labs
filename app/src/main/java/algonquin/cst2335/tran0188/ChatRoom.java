package algonquin.cst2335.tran0188;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    boolean isTablet = false;
    MessageListFragment chatFragment;

    @Override
    public void onCreate(Bundle previousInstance) {
        super.onCreate(previousInstance);

        setContentView(R.layout.empty_layout);

        isTablet = findViewById(R.id.detailsRoom) != null;

        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        chatFragment = new MessageListFragment();

        tx.add(R.id.fragmentRoom, chatFragment);

        tx.commit();//Loads the fragment into the specified FrameLayout

    }

    public void userClickedMessage(MessageListFragment.ChatMessage chatMessage, int position) {

        MessageDetailsFragment mdFragment = new MessageDetailsFragment(chatMessage, position);

        if(isTablet){

            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom, mdFragment).commit();

        }else { //on a phone

            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, mdFragment).commit();

        }

    }

    public void notifyMessageDeleted(MessageListFragment.ChatMessage chosenMessage, int chosenPosition) {
        chatFragment.notifyMessageDeleted(chosenMessage, chosenPosition);
    }
}




