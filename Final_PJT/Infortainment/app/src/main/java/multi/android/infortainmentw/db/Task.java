package multi.android.infortainmentw.db;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import multi.android.infortainmentw.Variable;

public class Task extends AsyncTask<Map<String, String>, Integer, String> {
    private String result = "";
    private String method;

    public static String ip = Variable.springIP; // spring 서버의 ip.
    @Override
    protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터
        method = maps[0].get("method");
        String result = "";

        switch (method) {
            case "login":
                result = login(maps[0]);
                setResult(result);
                break;
            case "stateSelect":
                result = stateSelect(maps[0]);
                setResult(result);
                break;
            case "stateUpdate":
                result = stateUpdate(maps[0]);
                setResult(result);
                break;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }



    public String login(Map<String, String> maps) {
        HttpClient.Builder http = new HttpClient.Builder("GET", "http://" + ip + ":8088/miri/member/select");
        http.addAllParameters(maps);
        HttpClient post = http.create();
        post.request();
        int statusCode = post.getHttpStatusCode();
        String body = post.getBody();
        return body;
    }

    public String stateSelect(Map<String, String> maps) {
        HttpClient.Builder http = new HttpClient.Builder("GET", "http://" + ip + ":8088/miri/state/select");
        http.addAllParameters(maps);
        HttpClient post = http.create();
        post.request();
        int statusCode = post.getHttpStatusCode();
        String body = post.getBody();
        return body;
    }
    public String stateUpdate(Map<String, String> maps) {
        HttpClient.Builder http = new HttpClient.Builder("GET", "http://" + ip + ":8088/miri/state/update");
        http.addAllParameters(maps);
        HttpClient post = http.create();
        post.request();
        int statusCode = post.getHttpStatusCode();
        String body = post.getBody();
        return body;
    }
}