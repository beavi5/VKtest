package com.example.beavi5.vktest.MODEL.models;



       // import net.octobersoft.android.caucasiancuisinefree.common.Constants;
        import android.app.*;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.*;
        import android.webkit.*;
        import android.widget.*;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.NodeList;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;

        import java.io.IOException;
        import java.io.StringReader;
        import java.util.ArrayList;
        import java.util.List;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

public class VkDialog extends Dialog{
    public static final float[] DIMENSIONS_LANDSCAPE = {20, 60};
    public static final float[] DIMENSIONS_PORTRAIT = {40, 60};
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;

    private String mUrl;
    private VkApp.VkDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;

    public VkDialog(Context context, String url , VkApp.VkDialogListener listener) {
        super(context);
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner = new ProgressDialog(getContext());

        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        mContent = new LinearLayout(getContext());

        mContent.setOrientation(LinearLayout.VERTICAL);

        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;

        addContentView(mContent, new FrameLayout.LayoutParams(
                display.getWidth() - (int)(dimensions[0] * scale + 0.5f),
                display.getHeight() - (int)(dimensions[1] * scale + 0.5f)));
    }

    private void setUpWebView() {
        mWebView = new WebView(getContext());

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);

        mContent.addView(mWebView);
    }

    private class VkWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("ERRORRRRRRRR", "Redirecting URL " + url);

            if (url.startsWith(VkApp.CALLBACK_URL) & ( !url.contains("error") )) {
                Log.d("ERRORRRRRRRR","url contains callback url");

                mListener.onComplete(url);
                VkDialog.this.dismiss();

                return true;
            }
            else if(url.contains("error")){
                VkDialog.this.dismiss();
                return false;
            }
            else {
                Log.d("ERRORRRRRRRR","url not contains callback url");
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d("ERRORRRRRRRR", "Page error: " + description);
            super.onReceivedError(view, errorCode, description, failingUrl);

            mListener.onError(description);

            VkDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
            Log.d("UUU", "Loading URL: " + url);
            super.onPageStarted(view, url, favicon);

            if( url.contains("error") ) {
                VkDialog.this.dismiss();
                return;
            }
            else if( url.contains("access_token")) {
                VkDialog.this.dismiss();


             //   mListener.onComplete(url);


                ////
                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                   String[] query = url.split("#");
                final String[] params = query[1].split("&");
                String feedUrl= "https://api.vk.com/method/friends.get.xml?"+params[2]+"&fields=photo_medium,uid,first_name,last_name,nickname";
                Log.d("UUU", "feedUrl: "+feedUrl);
                final StringRequest request=new StringRequest(Request.Method.GET, feedUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<FriendModel> friendsList= new ArrayList<>();


                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = null;
                        try {
                            builder = factory.newDocumentBuilder();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        }
                        InputSource is = new InputSource(new StringReader(response));

                        Document document= null;
                        try {
                            document = builder.parse(is);
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Element element = document.getDocumentElement();
                        NodeList nodeList = element.getElementsByTagName("user");

                        if (nodeList.getLength()>0)
                        {

                            int count = nodeList.getLength();
                            for (int i = 0; i <nodeList.getLength() ; i++) {
                                Log.d("arr", "" + nodeList.getLength());

                                Element entry = (Element) nodeList.item(i);

                                Element _uid = (Element) entry.getElementsByTagName("uid").item(0);
                                Element _photo = (Element) entry.getElementsByTagName("photo_medium").item(0);
                                Element _firstname = (Element) entry.getElementsByTagName("first_name").item(0);
                                Element _lastname = (Element) entry.getElementsByTagName("last_name").item(0);
                                String img = "";

                                String firstname = _firstname.getFirstChild().getNodeValue();
                                String lastname = _lastname.getFirstChild().getNodeValue();
                                String uid = _uid.getFirstChild().getNodeValue();
                                String photo = _photo.getFirstChild().getNodeValue();




                                friendsList.add( new FriendModel(photo, firstname, lastname, uid));
                                   mListener.onGetFriends(friendsList,params[0]);
                        //       rvNewsAdapter.notifyDataSetChanged();

                            }}



                       Log.d("UUU", "onResponse: "+response);
//


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
                requestQueue.add(request);
                ////
                return;
            }
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();
        }
    }
}
