package com.better.amino.api.ws;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.better.amino.R;
import com.better.amino.activities.ChatActivity;
import com.better.amino.adapters.MessagesAdapter;
import com.better.amino.api.utils.ChatUtils;
import com.better.amino.utils.Headers;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MessageListener extends WebSocketListener {

    final ChatActivity activity;
    final MessagesAdapter adapter;
    final RecyclerView listView;
    private final int CHAT_MESSAGES = 1000;

    public MessageListener(ChatActivity activity, MessagesAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        listView = activity.findViewById(R.id.messageList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        // activity.runOnUiThread(() -> ToastManager.makeToast(activity, "Connected!"));
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull final String data) {
        Map<String, Object> json = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
        }.getType());

        activity.runOnUiThread(() -> {
            Map<String, Object> chatMessage = new HashMap<>();
            if (((Double) json.get("t")).intValue() == CHAT_MESSAGES) {
                chatMessage = ((Map<String, Object>) ((Map<String, Object>) json.get("o")).get("chatMessage"));
                String threadId = chatMessage.get("threadId").toString();

                if (threadId.equals(ChatUtils.chatId)) {
                }
            }
            Map<String, Object> finalChatMessage = chatMessage;

            //new Handler().postDelayed((Runnable) () -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) listView.getLayoutManager();
            int currentItem = layoutManager.findLastVisibleItemPosition();
            adapter.add(finalChatMessage);

            if (currentItem == adapter.getItemCount() - 2) {
                listView.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                View view = activity.findViewById(R.id.messageBox);
                TextInputLayout messageBox = activity.findViewById(R.id.messageBox);
                Snackbar.make(view, "New Messages", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Go To Message", view1 -> listView.scrollToPosition(adapter.getItemCount() - 1))
                        .setAnchorView(messageBox)
                        .show();
            }

            listView.setItemAnimator(null);
        });
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosed(webSocket, code, reason);
        new ChatActivity().instantiateWebSocket();
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull final Throwable t, @Nullable final Response response) {
        super.onFailure(webSocket, t, response);

        activity.runOnUiThread(() -> {
            String data = Headers.headers.get("NDCDEVICEID") + "|" + System.currentTimeMillis() * 1000 / 1000;

            okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();
            for (Map.Entry<String, String> entry : Headers.GetHeaders(data).entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("wss://ws1.narvii.com/?signbody=" + data.replace("|", "%7C")).headers(headerBuilder.build()).build();

            MessagesAdapter adapter = new MessagesAdapter(activity, this.adapter.getData());
            client.newWebSocket(request, new MessageListener(activity, adapter));

            listView.scrollToPosition(adapter.getItemCount() - 1);
            listView.setItemAnimator(null);
        });
    }
}