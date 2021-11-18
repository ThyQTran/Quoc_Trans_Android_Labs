package algonquin.cst2335.tran0188;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageListFragment extends Fragment {

    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt = new MyChatAdapter();
    SQLiteDatabase db;
    Button sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);

        RecyclerView chatList = chatLayout.findViewById(R.id.myrecycler);
         sendButton = chatLayout.findViewById(R.id.sendButton);
        Button receiveButton = chatLayout.findViewById(R.id.receiveButton);
        EditText edit = chatLayout.findViewById(R.id.editText);



        MyOpenHelper opener = new MyOpenHelper(getContext());

        db = opener.getWritableDatabase();

        //loads messages from database
        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        while(results.moveToNext()){
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int sendOrReceive = results.getInt(sendCol);
            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }


        sendButton.setOnClickListener(click -> {

            String whatIsTyped = edit.getText().toString();

            Date timeNow = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");

            String currentDateAndTime = sdf.format(timeNow);

            ChatMessage thisMessage = new ChatMessage(whatIsTyped, 1,currentDateAndTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            thisMessage.setId(newId);

            messages.add(thisMessage);
            edit.setText("");

            adt.notifyItemInserted(messages.size() - 1);
        });

        receiveButton.setOnClickListener(click -> {

            String whatIsTyped = edit.getText().toString();

            Date timeNow = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");

            String currentDateAndTime = sdf.format(timeNow);

            ChatMessage thisMessage = new ChatMessage(whatIsTyped, 2, currentDateAndTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            thisMessage.setId(newId);

            messages.add(thisMessage);
            edit.setText("");

            adt.notifyItemInserted(messages.size() - 1);

        });
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        builder.setMessage("Do you want to delete the message: " + chosenMessage.getMessage())

                        .setTitle("Question:")

                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {

                            ChatMessage removedMessage = messages.get(chosenPosition);
                            messages.remove(chosenPosition);
                            adt.notifyItemRemoved(chosenPosition);

                            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] { Long.toString(removedMessage.getId())});

                            Snackbar.make(sendButton, "You deleted message #" + chosenPosition, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {

                                        messages.add(chosenPosition, removedMessage);
                                        adt.notifyItemInserted(chosenPosition);

                                        //Reinserting deleted item into database
                                        db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                                "','" + removedMessage.getMessage() +
                                                "','" + removedMessage.getSendOrReceive() +
                                                "','" + removedMessage.getTimeSent() + "');");
                                    })
                                    .show();
                        })
                        .create().show();
    }

    public class MyRowViews extends RecyclerView.ViewHolder {


        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener((click -> {

                ChatRoom parentActivity = (ChatRoom) getContext();
               int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);

/*                AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

                builder.setMessage("Do you want to delete the message: " + messageText.getText())

                        .setTitle("Question:")

                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {

                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            adt.notifyItemRemoved(position);

                            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] { Long.toString(removedMessage.getId())});

                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {

                                        messages.add(position, removedMessage);
                                        adt.notifyItemInserted(position);

                                        //Reinserting deleted item into database
                                        db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                                "','" + removedMessage.getMessage() +
                                                "','" + removedMessage.getSendOrReceive() +
                                                "','" + removedMessage.getTimeSent() + "');");
                                    })
                                    .show();
                        })
                        .create().show(); */
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
        long id;

        public void setId( long l) { id = l; }
        public long getId(){ return id;}

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public ChatMessage(String message, int sendOrReceive, String timeSent, long id){
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            setId(id);
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

