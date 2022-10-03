package com.better.amino.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.better.amino.R;
import com.better.amino.adapters.MessagesAdapter;
import com.better.amino.api.Community;
import com.better.amino.api.utils.AccountUtils;
import com.better.amino.api.ws.MessageListener;
import com.better.amino.ui.ToastManager;
import com.better.amino.utils.Headers;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class ChatActivity extends AppCompatActivity {

    RecyclerView listView;
    private NavigationRailView railNavView;
    private TextInputLayout messageBox;
    private TextInputEditText messageText;
    private Community community;
    private int msgType = 0;
    private Dialog sendCoinsDialog;
    private MessagesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageBox = findViewById(R.id.messageBox);
        messageText = findViewById(R.id.messageText);
        railNavView = findViewById(R.id.navigation_rail);
        listView = findViewById(R.id.messageList);
        community = new Community(this);
        listView.setAdapter(adapter);
        getChatMessages();
        buildShowSendCoinsDialog();
        instantiateWebSocket();

        railNavView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.fake_coins:
                    showSendCoinsDialog();
                    break;

                case R.id.msg_type:
                    showMessageTypesPopup();
                    break;
            }

            return true;
        });

        messageBox.setEndIconOnClickListener(view -> {
            String message = messageText.getText().toString();

            if (!message.equals("")) {
                if (community.SendMessage(message, msgType)) {
                    messageText.setText("");
                }
            }
        });
    }

    public void getChatMessages() {
        adapter = new MessagesAdapter(this, new ArrayList<>());
        ArrayList<Map<String, Object>> msgs = community.getMessages();
        Collections.reverse(msgs);
        for (Map<String, Object> message : msgs) {
            listView.setLayoutManager(new LinearLayoutManager(this));
            adapter.add(message);
            listView.scrollToPosition(adapter.getItemCount() - 1);
            listView.setItemAnimator(null);
        }
    }

    public void instantiateWebSocket() {

        String data = Headers.headers.get("NDCDEVICEID") + "|" + System.currentTimeMillis() * 1000 / 1000;

        okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();
        for (Map.Entry<String, String> entry : Headers.GetHeaders(data).entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("wss://ws1.narvii.com/?signbody=" + data.replace("|", "%7C")).headers(headerBuilder.build()).build();

        WebSocket webSocket = client.newWebSocket(request, new MessageListener(this, adapter));
    }

    private void showMessageTypesPopup() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.msg_type));

        popupMenu.getMenuInflater().inflate(R.menu.msg_types, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item1 -> {
            msgType = Integer.parseInt(item1.getTitle().toString());
            return false;
        });
        popupMenu.show();
    }

    private void buildShowSendCoinsDialog() {
        sendCoinsDialog = new BottomSheetDialog(this);
        sendCoinsDialog.setContentView(R.layout.create_new_project_dialog);
    }

    @WorkerThread
    private void showSendCoinsDialog() {
        if (!sendCoinsDialog.isShowing()) {
            sendCoinsDialog.show();
            TextView title = sendCoinsDialog.findViewById(android.R.id.title);
            EditText input = sendCoinsDialog.findViewById(android.R.id.text1);
            TextInputLayout inputLayout = sendCoinsDialog.findViewById(R.id.til_project_name);
            Button cancelBtn = sendCoinsDialog.findViewById(android.R.id.button2);
            Button createBtn = sendCoinsDialog.findViewById(android.R.id.button1);
            createBtn.setText("Send");
            title.setText("Send Coins");
            inputLayout.setHint("Coins Count");
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            cancelBtn.setOnClickListener(v -> sendCoinsDialog.dismiss());
            createBtn.setOnClickListener(
                    v -> {
                        if (community.SendCoins(1, AccountUtils.uid)) {
                            if (community.SendCoins(Integer.parseInt(input.getText().toString()), AccountUtils.uid)) {
                                ToastManager.makeToast(this, "Coins Sent Successfully");
                            }
                        }
                        sendCoinsDialog.dismiss();
                    });

            input.setText("");
        }
    }
}