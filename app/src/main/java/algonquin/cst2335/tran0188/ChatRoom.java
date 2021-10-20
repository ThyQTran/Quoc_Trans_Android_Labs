package algonquin.cst2335.tran0188;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt = new MyChatAdapter();


    @Override
    public void onCreate(Bundle previousInstance) {
        super.onCreate(previousInstance);

        setContentView(R.layout.chatlayout);

        Button sendButton = findViewById(R.id.sendButton);
        Button receiveButton = findViewById(R.id.receiveButton);

        EditText edit = findViewById(R.id.editText);

        RecyclerView chatList = findViewById(R.id.myrecycler);

        sendButton.setOnClickListener(click -> {

            String whatIsTyped = edit.getText().toString();

            Date timeNow = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");

            String currentDateAndTime = sdf.format(timeNow);

            ChatMessage thisMessage = new ChatMessage(whatIsTyped, 1,currentDateAndTime);

            messages.add(thisMessage);
            edit.setText("");

            adt.notifyItemInserted(messages.size() - 1);
        });
  //      chatList.setAdapter(adt);
  //      chatList.setLayoutManager(new LinearLayoutManager(this));  //App crashes when this line is added and getItemCount() changes # (Mandatory though)

        receiveButton.setOnClickListener(click -> {

            String whatIsTyped = edit.getText().toString();

            Date timeNow = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");

            String currentDateAndTime = sdf.format(timeNow);

            ChatMessage thisMessage = new ChatMessage(whatIsTyped, 2, currentDateAndTime);

            messages.add(thisMessage);
            edit.setText("");

            adt.notifyItemInserted(messages.size() - 1);

        });
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));  //App crashes when this line is added and getItemCount() changes # (Mandatory though)
    }

        public class MyRowViews extends RecyclerView.ViewHolder {


            TextView messageText;
            TextView timeText;

            public MyRowViews(View itemView) {
                super(itemView);

                itemView.setOnClickListener((click -> {

                    int position = getAbsoluteAdapterPosition();

                    AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );

                    builder.setMessage("Do you want to delete the message: " + messageText.getText())

                           .setTitle("Question:")

                           .setNegativeButton("No", (dialog, cl) -> {})
                           .setPositiveButton("Yes", (dialog, cl) -> {

                               ChatMessage removedMessage = messages.get(position);
                               messages.remove(position);
                               adt.notifyItemRemoved(position);

                               Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                       .setAction("Undo", clk -> {

                                           messages.add(position, removedMessage);
                                           adt.notifyItemInserted(position);
                                       })
                                       .show();
                    })
                            .create().show();
                }));

                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);

            }
        }

        public class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {


            @Override
            public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

                //loads a new row from the layout file:
                LayoutInflater inflater = getLayoutInflater();

                int layoutID;

                if (viewType == 1) {
                    layoutID = R.layout.sent_message;
                } else {
                    layoutID = R.layout.receive_layout;
                }

                //import layout for row:
                View loadedRow = inflater.inflate(layoutID, parent, false);

                MyRowViews initRow = new MyRowViews(loadedRow);

                return initRow;
            }

            //initializes Row at position
            @Override
            public void onBindViewHolder(MyRowViews holder, int position) {

                holder.messageText.setText(messages.get(position).getMessage());
                holder.timeText.setText(messages.get(position).getTimeSent());
            }

            public int getItemViewType(int position) {

                return messages.get(position).getSendOrReceive();
            }

            @Override
            public int getItemCount() {

                return messages.size(); //Changing this number causes program to crash
            }
        }

        public class ChatMessage {

            String message;
            int sendOrReceive;
            String timeSent;

            public ChatMessage(String message, int sendOrReceive, String timeSent) {
                this.message = message;
                this.sendOrReceive = sendOrReceive;
                this.timeSent = timeSent;
            }

            public String getMessage() {

                return message;
            }

            public int getSendOrReceive() {

                return sendOrReceive;
            }

            public String getTimeSent() {

                return timeSent;
            }
        }


}




