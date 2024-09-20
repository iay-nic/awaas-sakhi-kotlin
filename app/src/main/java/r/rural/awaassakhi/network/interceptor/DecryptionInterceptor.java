package r.rural.awaassakhi.network.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import r.rural.awaassakhi.network.crypto.CryptoStrategy;
import r.rural.awaassakhi.utils.Utility;

import java.io.IOException;

public class DecryptionInterceptor implements Interceptor {

    private final CryptoStrategy mDecryptionStrategy;

    public DecryptionInterceptor(CryptoStrategy mDecryptionStrategy) {
        this.mDecryptionStrategy = mDecryptionStrategy;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.isSuccessful()) {
            Response.Builder newResponse = response.newBuilder();
            String contentType = response.header("Content-Type");
            if (TextUtils.isEmpty(contentType)) contentType = "application/json";
//            InputStream cryptedStream = response.body().byteStream();
            String responseStr = response.body().string();


            String decryptedString = null;

            if (!isJSONValid(responseStr)) { //if its a json object or jsonArray don't decrypt it
                if (mDecryptionStrategy != null) {
                    try {
                        decryptedString = mDecryptionStrategy.decrypt(responseStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    throw new IllegalArgumentException("No decryption strategy!");
                }
            }

            if (!TextUtils.isEmpty(decryptedString))//if not empty
                newResponse.body(ResponseBody.create(MediaType.parse(contentType), decryptedString));
            else
                newResponse.body(ResponseBody.create(MediaType.parse(contentType), responseStr));//no decryption

            return newResponse.build();
        }
        return response;
    }

    private boolean isJSONValid(String json) {
        try {
            new JSONObject(json); // Try to parse as JSONObject
            return true;
        } catch (JSONException e1) {
            try {
                new JSONArray(json); // Try to parse as JSONArray
                return true;
            } catch (JSONException e2) {
                return false;
            }
        }
    }
}