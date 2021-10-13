package algonquin.cst2335.tran0188;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class ChatRoom extends AppCompatActivity {

    RecyclerView chatList;

    @Override
    public void onCreate(Bundle previousInstance) {

        super.onCreate(previousInstance);

        setContentView(R.layout.chatlayout);
        chatList = findViewById(R.id.myrecycler);
        chatList.setAdapter(new RecyclerView.Adapter());
    }

    private class MyChatAdapter extends RecyclerView.Adapter{

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });

    }

    private class chatMessage{

        String message;
        int sendOrReceive;
        Date timeSent;
    }
}
