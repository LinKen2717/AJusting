package com.eva.httpdemo.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Class description
 *
 * @author test
 * @date 2015-09-23 11:44
 */
public abstract class HttpRequestEngine {

    /**
     * 默认url是否encode
     */
    final static boolean IS_ENCODE = true;

    public static final int RESPONSE_IS_NULL = 0x100;

    protected Context context;

    public HttpRequestEngine(Context context) {
        this.context = context;
    }

    protected void get(Object tag, String hostUrl, String url, ArrayMap<?, ?> params, HttpResponseHandle handle) {
        request(HttpRequest.GET, tag, hostUrl, url, params, handle);
    }

    protected void post(Object tag, String hostUrl, String url, ArrayMap<?, ?> params, HttpResponseHandle handle) {
        request(HttpRequest.POST, tag, hostUrl, url, params, handle);
    }

    protected void request(String method, Object tag, String hostUrl, String url, ArrayMap<?, ?> params, HttpResponseHandle handle) {
        Response response = request(method, tag, hostUrl, url, params);

        try {
            if (response == null) {
                if (handle != null) {
                    handle.onFailure(RESPONSE_IS_NULL, "response is null");
                }
            } else if (response.isSuccessful()) {
                if (handle != null) {
                    handle.onSuccess(response.code(), response.body().string());
                }
            } else {
                if (handle != null) {
                    handle.onFailure(response.code(), response.body().string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response request(String method, Object tag, String hostUrl, String url, ArrayMap<?, ?> params) {
        ArrayMap<?, ?> headers = getHeaders();
        String requestUrl = getRequestUrl(hostUrl, url, params);

        Log.v("Eva", "request url = " + requestUrl);

        URI uri = URI.create(requestUrl);
        Response response = null;

        try {
            if (HttpRequest.HTTPS.equals(uri.getScheme())) {
                setCertificates();
            }

            if (HttpRequest.GET.equals(method)) {
                response = HttpRequest.getInstance(context).get(requestUrl, IS_ENCODE, tag, headers, params);
            } else if (HttpRequest.POST.equals(method)) {
                response = HttpRequest.getInstance(context).post(requestUrl, IS_ENCODE, tag, headers, params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * SSL证书
     *
     * @return
     */
    protected int[] getCertificates() {
        return null;
    }

    protected void setCertificates() {
        int[] certificateIds = getCertificates();
        if (certificateIds != null && certificateIds.length > 0) {
            InputStream[] certificates = new InputStream[certificateIds.length];
            for (int i = 0; i < certificateIds.length; i++) {
                certificates[i] = context.getResources().openRawResource(certificateIds[i]);
            }
            HttpRequest.getInstance(context).setCertificates(certificates);
        }
    }

    /**
     * Http Headers
     *
     * @return
     */
    protected abstract ArrayMap<?, ?> getHeaders();

    protected String getRequestUrl(String hostUrl, String url, ArrayMap<?, ?> params) {
        return hostUrl + url;
    }
}
