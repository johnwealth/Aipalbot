package net.aipalbot.aipalbot;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import net.aipalbot.aipalbot.Adapter.CustomAdapter;
import net.aipalbot.aipalbot.Helper.HttpDataHandler;
import net.aipalbot.aipalbot.Models.ChatModel;
import net.aipalbot.aipalbot.Models.SimsimiModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private final List<ChatModel> list_chat = new ArrayList<>();
    private FloatingActionButton btn_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_of_message);
        editText = findViewById(R.id.user_message);
        btn_send_message = findViewById(R.id.fab);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View view){
                String text = editText.getText().toString();
                ChatModel model = new ChatModel(text, true);
                list_chat.add(model);
                new SimsimiAPI().execute(list_chat);

                //remove user message

                editText.setText("");

            }

        });
    }


    @SuppressLint("StaticFieldLeak")
    private class SimsimiAPI extends AsyncTask<List<ChatModel>,Void,String> {
        String stream = null;
        List<ChatModel> models;
        final String text = editText.getText().toString();

        @SuppressWarnings("MalformedFormatString")
        @SafeVarargs
        @Override
        protected final String doInBackground(List<ChatModel>... params) {
            String url = String.format("http://sandbox.api.simsimi.com/request.p?key=0a10934b-af70-4e25-8ca5-60d03180e1db&lc=en&ft=1.0&text=%s", getString(R.string.simsimi_api),text);
            models = params[0];
            HttpDataHandler HttpDataHandler = new HttpDataHandler();
            stream = net.aipalbot.aipalbot.Helper.HttpDataHandler.GetHTTPData(url);
            return  stream;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            SimsimiModel response = gson.fromJson(s, SimsimiModel.class);
            ChatModel chatModel = new ChatModel(response.getResponse(),false);
            models.add(chatModel);
            CustomAdapter adapter = new CustomAdapter(models,getApplicationContext());
            listView.setAdapter(adapter);
        }
    }
}
