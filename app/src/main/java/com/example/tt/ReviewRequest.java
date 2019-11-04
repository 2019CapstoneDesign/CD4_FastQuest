package com.example.tt;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewRequest extends StringRequest {
    final static private String URL = "http://10.0.2.2:8080/registration/UserRegister.php";
    private Map<String, String> parameters;

    public ReviewRequest(String review_title, String review_content, byte[] byteArray, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        String photo_byte = byteArray.toString();
        parameters = new HashMap<>();
        parameters.put("review_title", review_title);
        parameters.put("review_content", review_content);
        parameters.put("photo_byte", photo_byte);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
