package com.better.amino.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.better.amino.R;
import com.better.amino.adapters.MessagesAdapter;
import com.better.amino.api.Community;
import com.better.amino.api.ws.MessageListener;
import com.better.amino.utils.Headers;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextInputLayout messageBox = findViewById(R.id.messageBox);
        TextInputEditText messageText = findViewById(R.id.messageText);

        Community community = new Community(this);

        instantiateWebSocket();

        messageBox.setEndIconOnClickListener(view -> {
            String message = messageText.getText().toString();

            if (community.SendMessage(message)){
                messageText.setText("");
            }

        });
    }

    private void instantiateWebSocket() {

        String data = Headers.headers.get("NDCDEVICEID") + "|" + System.currentTimeMillis() * 1000 / 1000;

        okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();
        for (Map.Entry<String, String> entry : Headers.GetHeaders(data).entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("wss://ws1.narvii.com/?signbody=" + data.replace("|", "%7C")).headers(headerBuilder.build()).build();

        MessagesAdapter adapter = new MessagesAdapter(this, new ArrayList<>());
        WebSocket webSocket = client.newWebSocket(request, new MessageListener(this, adapter));

    }
}