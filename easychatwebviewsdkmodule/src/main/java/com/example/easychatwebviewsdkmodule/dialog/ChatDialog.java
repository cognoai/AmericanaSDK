package com.example.easychatwebviewsdkmodule.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.R;
import com.example.easychatwebviewsdkmodule.ViewModal.AuthenticationViewModal;
import com.example.easychatwebviewsdkmodule.WebViewClient.MyWebViewClient;
import com.example.easychatwebviewsdkmodule.modal.Request.AccessTokenRequestPacket;
import com.example.easychatwebviewsdkmodule.modal.Request.LiveChatSessionExpiryRequestPacket;
import com.example.easychatwebviewsdkmodule.modal.Response.AccessTokenResponse;
import com.example.easychatwebviewsdkmodule.modal.Response.LiveChatSessionExpiryResponse;
import com.example.easychatwebviewsdkmodule.utils.LanguageUtils;
import com.example.easychatwebviewsdkmodule.utils.SpeechToText;
import com.example.easychatwebviewsdkmodule.utils.TextToSpeech;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF;
import static androidx.webkit.WebSettingsCompat.FORCE_DARK_ON;


public class ChatDialog extends DialogFragment {
    private WebView webView;
    private SpeechToText speechToText;
    private static final Integer RecordAudioRequestCode = 1;
    private static final Integer CameraRequestCode = 2;
    private static final Integer StorageRequestCode = 3;
    private boolean isRecorderPermissionGranted = false;
    private boolean  is_text_to_speech_initialized = false;
    private ImageButton backButton;
//    private RelativeLayout relativeLayout;
//    private TextView headerText;
    private TextToSpeech textToSpeech;
    private LanguageUtils languageUtils;

    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;
    private String category_name_param = "";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(data == null || data.getData() == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = data.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }


    public static DialogFragment newInstance(String title) {
        ChatDialog instance = new ChatDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.chat_dialog, container, false);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webview);
//        relativeLayout = (RelativeLayout) view.findViewById(R.id.layout);
//        headerText = (TextView)view.findViewById(R.id.title);
        languageUtils = new LanguageUtils();

        initializeTextToSpeech();

        if (!GlobalParams.getLiveChatSessionId().isEmpty()) {
            String query_value = GlobalParams.getLiveChatSessionId();
            verifyLiveChatSessionExpired(getActivity() ,query_value);
        }
        switch (GlobalParams.getTheme()) {
            case "Dark":
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
//                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.light_black));
//                    headerText.setTextColor(getResources().getColor(R.color.white));
                }
                break;
            case "Automatic":
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                        case Configuration.UI_MODE_NIGHT_YES:
                            WebSettingsCompat.setForceDark(webView.getSettings(), FORCE_DARK_ON);
//                            relativeLayout.setBackgroundColor(getResources().getColor(R.color.light_black));
//                            headerText.setTextColor(getResources().getColor(R.color.white));
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            WebSettingsCompat.setForceDark(webView.getSettings(), FORCE_DARK_OFF);
//                            relativeLayout.setBackgroundColor(getResources().getColor(R.color.light_white));
//                            headerTheaderTextext.setTextColor(getResources().getColor(R.color.black));
                            break;
                    }
                }
                break;
            case "Light":
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                    WebSettingsCompat.setForceDark(webView.getSettings(), FORCE_DARK_OFF);
//                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.light_white));
//                    headerText.setTextColor(getResources().getColor(R.color.black));
                }
                break;
        }
        webView.setScrollbarFadingEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().getUserAgentString();
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface           // For API 17+
            public void minimize()
            {
                Log.i("TAG", "minimize: clicked");
                GlobalParams.setBot_minimized(true);
                dismiss();

            }

            @JavascriptInterface           // For API 17+
            public void close()
            {
                Log.i("TAG", "close: clicked");
                String uuid = UUID.randomUUID().toString();
                GlobalParams.setRandom_uuid(uuid);
                GlobalParams.setSelected_language("en");
                GlobalParams.setLiveChatSessionId("");
//                GlobalParams.setBot_minimized(false);
                dismiss();

            }

            @JavascriptInterface           // For API 17+
            public void setLiveChatSessionId(String liveChatSessionId)
            {
                GlobalParams.setLiveChatSessionId(liveChatSessionId);
            }

            @JavascriptInterface           // For API 17+
            public void reload_chatbot() {
                String finalUrl = GlobalParams.getFinalUrl();
                String mobile_session_id = GlobalParams.getMobileSessionId();
                String uuid = UUID.randomUUID().toString();
                GlobalParams.setRandom_uuid(uuid);
                if (finalUrl.isEmpty()) {
                    finalUrl = GlobalParams.getBase_url()+"/chat/index/?id="+GlobalParams.bot_id+"&channel=Android" + category_name_param + "&mobile_session_id=" + mobile_session_id;
                    if (!GlobalParams.isStoreChatPermanently()) {
                        finalUrl += GlobalParams.getRandom_uuid();
                    }
                } else {
                    String query_value = mobile_session_id;
                    if (!GlobalParams.isStoreChatPermanently()) {
                        query_value += GlobalParams.getRandom_uuid();
                    }
                    finalUrl = appendQueryParameterToUri("mobile_session_id", query_value, finalUrl);

                }

                if (!GlobalParams.getSelected_language().isEmpty()) {
                    String query_value = GlobalParams.getSelected_language();
                    finalUrl = appendQueryParameterToUri("selected_language", query_value, finalUrl);
                }

                if (!GlobalParams.getLiveChatSessionId().isEmpty()) {
                    String query_value = GlobalParams.getLiveChatSessionId();
                    finalUrl = appendQueryParameterToUri("livechat_session_id", query_value, finalUrl);
                }

                final Timer t = new java.util.Timer();
                String finalUrl1 = finalUrl;
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        webView.loadUrl(finalUrl1);
                                    }
                                }
                                );
                                t.cancel();
                            }

                        },
                        300
                );


            }

            @JavascriptInterface           // For API 17+
            public  void  textToSpeech(String text, String languageCode) {
                Log.i("TAG", "textToSpeech: " + text);
                languageCode = languageUtils.getMappedLanguage(languageCode);
                textToSpeech.convertTextToSpeech(text, languageCode);
            }

            @JavascriptInterface           // For API 17+
            public  void  terminateTextToSpeech() {
                Log.i("TAG", "textToSpeech shutdow: ");

                textToSpeech.terminateTextToSpeech();
            }

            @JavascriptInterface
            public void start_recording()
            {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!is_text_to_speech_initialized) {
                                initializeSpeechToText();
                                is_text_to_speech_initialized = true;
                            }

                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                speechToText.getSpeechRecognizer().startListening(speechToText.getSpeechRecognizerIntent());
                            } else {
                                checkPermissionsForMic();
                                Toast.makeText(getContext(), "Please provide microphone permission.", Toast.LENGTH_SHORT).show();

                            }
                            }//public void run() {
                    });
                } catch (Exception e)
                {
                    Log.i("TAG", "start_recording: "+e.getMessage());
                    e.printStackTrace();

                }

            }
        }, "android");

        webView.setFocusable(true);

        webView.setFocusableInTouchMode(true);
        webView.setWebViewClient(new MyWebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    if (url.contains("&channel=Android")) {
                        Uri uri = Uri.parse(url);
                        String selected_language = uri.getQueryParameter("selected_language");
                        if (selected_language != null || !selected_language.isEmpty()) {
                            GlobalParams.setSelected_language(selected_language);
                        }
                        return super.shouldOverrideUrlLoading(view, url);
                    } else {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }

                }else if (url != null && url.contains("mailto:")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else if(url != null && url.contains("tel:")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                checkPermission();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("TAG", "onConsoleMessage: "+consoleMessage.message()+" From line "+consoleMessage.lineNumber() +" of "+consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }

            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams){

                if(mUMA != null){
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;

                    intentArray = new Intent[0];


                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        if (!GlobalParams.getCategoryName().isEmpty()) {
            category_name_param = "&category_name=" + GlobalParams.getCategoryName();
        }

        // code to handle session chats
        String finalUrl = GlobalParams.getFinalUrl();
        String mobile_session_id = GlobalParams.getMobileSessionId();
        if (GlobalParams.getRandom_uuid().isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            GlobalParams.setRandom_uuid(uuid);
        }
        if (finalUrl.isEmpty()) {
            finalUrl = GlobalParams.getBase_url()+"/chat/index/?id="+GlobalParams.bot_id+"&channel=Android" + category_name_param + "&mobile_session_id=" + mobile_session_id;
            if (!GlobalParams.isStoreChatPermanently()) {
                finalUrl += GlobalParams.getRandom_uuid();
            }
        } else {
            String query_value = mobile_session_id;
            if (!GlobalParams.isStoreChatPermanently()) {
                query_value += GlobalParams.getRandom_uuid();
            }
            finalUrl = appendQueryParameterToUri("mobile_session_id", query_value, finalUrl);

        }

        if (!GlobalParams.getSelected_language().isEmpty()) {
            String query_value = GlobalParams.getSelected_language();
            finalUrl = appendQueryParameterToUri("selected_language", query_value, finalUrl);
        }

        if (!GlobalParams.getLiveChatSessionId().isEmpty()) {
            String query_value = GlobalParams.getLiveChatSessionId();
            finalUrl = appendQueryParameterToUri("livechat_session_id", query_value, finalUrl);
        }

        webView.loadUrl(finalUrl);

        checkPermissionsForMic();
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {

            CookieManager cookieManager = CookieManager.getInstance();

            cookieManager.setAcceptThirdPartyCookies( webView, true );

        }


    }

    // Create an image file
    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView=null; // remove webView, prevent chromium to crash
    }

    private void initializeSpeechToText() {
        speechToText = new SpeechToText(getContext(), webView);
        speechToText.initializeSpeechRecognizer();
    }

    public void checkPermissionsForMic() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            isRecorderPermissionGranted =false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
            }

        } else {
            isRecorderPermissionGranted = true;
            initializeSpeechToText();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("results perm:", requestCode+" "+grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            Log.i("check perm",grantResults[0]+" "+PackageManager.PERMISSION_GRANTED);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isRecorderPermissionGranted = true;
                initializeSpeechToText();
            } else {
                isRecorderPermissionGranted = false;
            }
        }

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ) {
                    // Permissions are granted
                    Toast.makeText(getContext(), "Permissions granted.", Toast.LENGTH_SHORT).show();
                } else {
                    // Permissions are denied
                    Toast.makeText(getContext(), "Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void initializeTextToSpeech() {
        Log.d("TextToSpeech", "initializeTextToSpeech: " + textToSpeech);
        textToSpeech = new TextToSpeech(getContext());
        if (textToSpeech.getTextToSpeech() == null) {
            textToSpeech.initialiseTextToSpeech();
        }
    }

    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(),Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Camera and" +
                        " Storage access permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // convertTextToSpeechDirectly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            // Do something, when permissions are already granted
//            Toast.makeText(mContext,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                sendDataToWebView();
            }
        };
    }

    private void sendDataToWebView(){
        webView.evaluateJavascript(
                "javascript: " +"open_chat_termination_modal()",null);
    }

    public String appendQueryParameterToUri(String key, String value, String url) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter(key, value);
        url = builder.build().toString();
        return url;
    }

    private static void verifyLiveChatSessionExpired(FragmentActivity fragmentActivity, String livechat_session_id) {
        AuthenticationViewModal authenticationViewModal;
        authenticationViewModal = ViewModelProviders.of(fragmentActivity).get(AuthenticationViewModal.class);
        authenticationViewModal.init();
        LiveChatSessionExpiryRequestPacket liveChatExpiryRequestPacket = new LiveChatSessionExpiryRequestPacket(livechat_session_id);
        LiveChatSessionExpiryResponse resp = authenticationViewModal.liveChatSessionExpiry(liveChatExpiryRequestPacket);
        if (resp.getStatus() == 440) {
            GlobalParams.setLiveChatSessionId("");
        }
    }

}
