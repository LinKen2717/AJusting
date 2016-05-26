package com.eva.httpdemo.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Class description
 *
 * @author test
 * @date 2015-09-22 15:23
 */
public class HttpRequest {

    public static final String GET = "get";
    public static final String POST = "post";
    public static final String HTTPS = "https";

    /**
     * 'UTF-8' charset name
     */
    public static final String CHARSET_UTF8               = "UTF-8";

    /**
     * 'application/x-www-form-urlencoded' content type header value
     */
    public static final String CONTENT_TYPE_FORM          = "application/x-www-form-urlencoded";

    /**
     * 'application/json' content type header value
     */
    public static final String CONTENT_TYPE_JSON          = "application/json";

    /**
     * 'gzip' encoding header value
     */
    public static final String ENCODING_GZIP              = "gzip";

    /**
     * 'Accept' header name
     */
    public static final String HEADER_ACCEPT              = "Accept";

    /**
     * 'Accept-Charset' header name
     */
    public static final String HEADER_ACCEPT_CHARSET      = "Accept-Charset";

    /**
     * 'Accept-Encoding' header name
     */
    public static final String HEADER_ACCEPT_ENCODING     = "Accept-Encoding";

    /**
     * 'Authorization' header name
     */
    public static final String HEADER_AUTHORIZATION       = "Authorization";

    /**
     * 'Cache-Control' header name
     */
    public static final String HEADER_CACHE_CONTROL       = "Cache-Control";

    /**
     * 'Content-Encoding' header name
     */
    public static final String HEADER_CONTENT_ENCODING    = "Content-Encoding";

    /**
     * 'Content-Length' header name
     */
    public static final String HEADER_CONTENT_LENGTH      = "Content-Length";

    /**
     * 'Content-Type' header name
     */
    public static final String HEADER_CONTENT_TYPE        = "Content-Type";

    /**
     * 'Date' header name
     */
    public static final String HEADER_DATE                = "Date";

    /**
     * 'ETag' header name
     */
    public static final String HEADER_ETAG                = "ETag";

    /**
     * 'Expires' header name
     */
    public static final String HEADER_EXPIRES             = "Expires";

    /**
     * 'If-None-Match' header name
     */
    public static final String HEADER_IF_NONE_MATCH       = "If-None-Match";

    /**
     * 'Last-Modified' header name
     */
    public static final String HEADER_LAST_MODIFIED       = "Last-Modified";

    /**
     * 'Location' header name
     */
    public static final String HEADER_LOCATION            = "Location";

    /**
     * 'Proxy-Authorization' header name
     */
    public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";

    /**
     * 'Referer' header name
     */
    public static final String HEADER_REFERER             = "Referer";

    /**
     * 'Server' header name
     */
    public static final String HEADER_SERVER              = "Server";

    /**
     * 'User-Agent' header name
     */
    public static final String HEADER_USER_AGENT          = "User-Agent";

    /**
     * 'charset' header value parameter
     */
    public static final String PARAM_CHARSET              = "charset";

    private static final String BOUNDARY                   = "00content0boundary00";

    private static final String CONTENT_TYPE_MULTIPART     = "multipart/form-data; boundary=" + BOUNDARY;

    private static final String CRLF                       = "\r\n";

    private static OkHttpClient sClient;
    private static HttpRequest sInstance;
    private static Object sTag;

    /** 默认连接超时时间 单位：毫秒 */
    final static long                CONNECT_TIME_OUT            = 10 * 1000;
    /** 默认读取超时时间 单位：毫秒 */
    final static long                READ_TIME_OUT               = 20 * 1000;

    final static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    final static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");


    public HttpRequest(Context context) {
        sClient = new OkHttpClient();
        sClient.setConnectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        sClient.setReadTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        sTag = context.getPackageName();
    }

    public static HttpRequest getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HttpRequest(context);
        }
        return sInstance;
    }

    /**
     * 取消所有call， 默认tag为包名
     */
    public void cancel() {
        cancel(sTag);
    }

    /**
     * 取消所有带有tag的call
     *
     * @param tag
     */
    public void cancel(Object tag) {
        sClient.cancel(tag);
    }

    /**
     * Get请求
     *
     * @param url
     *          请求地址
     * @param encode
     *          url是否encode
     * @param headers
     *          请求头
     * @param params
     *          请求参数
     * @return
     * @throws IOException
     */
    public Response get(String url, boolean encode, Object tag, ArrayMap<?, ?> headers, ArrayMap<?, ?> params) throws IOException {
        String requestUrl = append(url, params);
        Request.Builder builder = new Request.Builder();
        builder.url(encode ? encode(requestUrl) : requestUrl);
        if (headers != null && !headers.isEmpty()) {
            for (Entry<?, ?> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        if (tag == null) {
            builder.tag(sTag);
        } else {
            builder.tag(tag);
        }
        Request request = builder.build();
        return sClient.newCall(request).execute();
    }

    /**
     * Post请求提交键值对参数
     *
     * @param url
     *          请求地址
     * @param encode
     *          地址是否encode
     * @param headers
     *          请求头
     * @param params
     *          请求参数
     * @return
     * @throws IOException
     */
    public Response post(String url, boolean encode, Object tag, ArrayMap<?, ?> headers, ArrayMap<?, ?> params) throws IOException {
        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        if (params != null || !params.isEmpty()) {
            Log.v("Eva", "POST params ================");
            for (Entry<?, ?> entry : params.entrySet()) {
                Log.v("Eva", entry.getKey().toString() + " = " + entry.getValue().toString());
                formBuilder.add(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        return post(url, encode, tag, headers, formBuilder.build());
    }

    /**
     * Post请求提交Json
     *
     * @param url
     *          请求地址
     * @param encode
     *          请求地址是否encode
     * @param headers
     *          请求头
     * @param json
     *          提交的JSON
     * @return
     * @throws IOException
     */
    public Response post(String url, boolean encode, Object tag, ArrayMap<?, ?> headers, JSONObject json) throws IOException {
        RequestBody requestBody = null;
        if (json != null) {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, json.toString());
        }
        return post(url, encode, tag, headers, requestBody);
    }

    /**
     * Post请求上传文件
     *
     * @param url
     *          请求地址
     * @param encode
     *          请求地址是否encode
     * @param headers
     *          请求头
     * @param file
     *          上传文件
     * @return
     * @throws IOException
     */
    public Response post(String url, boolean encode, Object tag, ArrayMap<?, ?> headers, File file) throws IOException {
        RequestBody requestBody = null;
        if (file.exists()) {
            requestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
        }

        return post(url, encode, tag, headers, requestBody);
    }

    /**
     * Post提交分块请求
     *
     * @param url
     *          请求地址
     * @param encode
     *          请求地址是否encode
     * @param headers
     *          请求头
     * @param params
     *          请求参数
     * @param file
     *          上传文件
     * @return
     * @throws IOException
     */
    public Response post(String url, boolean encode, Object tag, ArrayMap<?, ?> headers, ArrayMap<?, ?> params, File file) throws IOException {
        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        if (params != null && !headers.isEmpty()) {
            for (Entry<?, ?> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        if (file.exists()) {
            builder.addPart(RequestBody.create(MEDIA_TYPE_STREAM, file));
        }

        return post(url, encode, tag, headers, builder.build());
    }

    private Response post(String url, boolean encode, Object tag, ArrayMap<?, ?> headers, RequestBody requestBody) throws IOException {
        Request.Builder builder = new Request.Builder();
        builder.url(encode ? encode(url) : url);
        if (headers != null && !headers.isEmpty()) {
            for (Entry<?, ?> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        if (requestBody != null) {
            builder.post(requestBody);
        }

        if (tag == null) {
            builder.tag(sTag);
        } else {
            builder.tag(tag);
        }
        Request request = builder.build();
        return sClient.newCall(request).execute();
    }

    /**
     * 设置Https请求的SSL证书
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = certificates[i];
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

                if (certificate != null) {
                    certificate.close();
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            sClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append given map as query parameters to the base URL
     * <p>
     * Each map entry's key will be a parameter name and the value's {@link Object#toString()} will be the parameter value.
     *
     * @param url
     * @param params
     * @return URL with appended query params
     */
    private String append(final String url, final Map<?, ?> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        final StringBuilder result = new StringBuilder(url);

        addPathSeparator(url, result);
        addParamPrefix(url, result);

        Object value;
        for (Entry<?, ?> entry : params.entrySet()) {
            result.append(entry.getKey().toString());
            result.append('=');
            value = entry.getValue();
            if (value != null) {
                result.append(value);
            }
            result.append('&');
        }

        // delete last '&'
        final int lastChar = result.length() - 1;
        if (result.charAt(lastChar) == '&') {
            result.deleteCharAt(lastChar);
        }

        return result.toString();
    }

    private StringBuilder addPathSeparator(final String url, final StringBuilder result) {
        // Add trailing slash if the base URL doesn't have any path segments.
        //
        // The following test is checking for the last slash not being part of
        // the protocol to host separator: '://'.
        if (url.indexOf(':' + 2) == url.lastIndexOf('/')) {
            result.append('/');
        }
        return result;
    }

    private StringBuilder addParamPrefix(final String baseUrl, final StringBuilder result) {
        // Add '?' if missing and add '&' if params already exist in base url
        final int queryStart = baseUrl.indexOf('?');
        final int lastChar = result.length() - 1;
        if (queryStart == -1)
            result.append('?');
        else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&')
            result.append('&');
        return result;
    }

    /**
     * Encode the given URL as an ASCII {@link String}
     * <p>
     * This method ensures the path and query segments of the URL are properly encoded such as ' ' characters being encoded to '%20' or any UTF-8 characters
     * that are non-ASCII. No encoding of URLs is done by default by the {@link HttpRequest} constructors and so if URL encoding is needed this method should be
     * called before calling the {@link HttpRequest} constructor.
     *
     * @param url
     * @return encoded URL
     * @throws HttpRequestException
     */
    public String encode(final CharSequence url) throws HttpRequestException {
        URL parsed = null;
        try {
            parsed = new URL(url.toString());
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }

        String host = parsed.getHost();
        int port = parsed.getPort();
        if (port != -1)
            host = host + ':' + Integer.toString(port);

        try {
            String encoded = new URI(parsed.getProtocol(), host, parsed.getPath(), parsed.getQuery(), null)
                    .toASCIIString();
            int paramsStart = encoded.indexOf('?');
            if (paramsStart > 0 && paramsStart + 1 < encoded.length())
                encoded = encoded.substring(0, paramsStart + 1)
                        + encoded.substring(paramsStart + 1).replace("+", "%2B");
            return encoded;
        } catch (URISyntaxException e) {
            IOException io = new IOException("Parsing URI failed");
            io.initCause(e);
            throw new HttpRequestException(io);
        }
    }

    /**
     * HTTP request exception whose cause is always an {@link IOException}
     */
    public static class HttpRequestException extends RuntimeException {

        private static final long serialVersionUID = -1170466989781746231L;

        /**
         * Create a new HttpRequestException with the given cause
         *
         * @param cause
         */
        public HttpRequestException(final IOException cause) {
            super(cause);
        }

        /**
         * Get {@link IOException} that triggered this request exception
         *
         * @return {@link IOException} cause
         */
        @Override
        public IOException getCause() {
            return (IOException) super.getCause();
        }
    }
}
