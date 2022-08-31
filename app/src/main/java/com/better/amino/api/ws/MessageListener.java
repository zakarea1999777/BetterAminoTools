package com.better.amino.api.ws;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.better.amino.R;
import com.better.amino.activities.ChatActivity;
import com.better.amino.adapters.MessagesAdapter;
import com.better.amino.api.utils.ChatUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MessageListener extends WebSocketListener {

    private final int CHAT_MESSAGES = 1000;

    final ChatActivity activity;
    final MessagesAdapter adapter;
    final RecyclerView listView;

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
        Map<String, Object> json = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());

        activity.runOnUiThread(() -> {
            if (((Double) json.get("t")).intValue() == CHAT_MESSAGES){
                Map<String, Object> chatMessage = ((Map<String, Object>) ((Map<String, Object>) json.get("o")).get("chatMessage"));
                String threadId = chatMessage.get("threadId").toString();

                if (threadId.equals(ChatUtils.chatId)){
                    listView.setLayoutManager(new LinearLayoutManager(activity));
                    adapter.add(chatMessage);
                    listView.scrollToPosition(adapter.getItemCount() - 1);
                    listView.setItemAnimator(null);
                }
            }
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
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull final Throwable t, @Nullable final Response response) {
        super.onFailure(webSocket, t, response);
    }
}