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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.R;
import com.example.easychatwebviewsdkmodule.WebViewClient.MyWebViewClient;
import com.example.easychatwebviewsdkmodule.utils.SpeechToText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class ChatDialog extends DialogFragment {
    private WebView webView;
    private SpeechToText speechToText;
    private static final Integer RecordAudioRequestCode = 1;
    private static final Integer CameraRequestCode = 2;
    private static final Integer StorageRequestCode = 3;
    private boolean isRecorderPermissionGranted = false;
    private boolean  is_text_to_speech_initialized = false;
    private ImageButton backButton;

    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;

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
        backButton = (ImageButton) view.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToWebView();
            }
        });
        if(GlobalParams.isDarkTheme()){
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
            }
        }else {
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
            }
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
//                GlobalParams.setBot_minimized(true);
//                if(GlobalParams.getChatDialog().getDialog().isShowing())
//                GlobalParams.getChatDialog().getDialog().hide();

            }

            @JavascriptInterface           // For API 17+
            public void close()
            {
                Log.i("TAG", "close: clicked");
                GlobalParams.setBot_minimized(false);
                dismiss();

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
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
//                    File photoFile = null;
//                    try{
//                        photoFile = createImageFile();
//                        takePictureIntent.putExtra("PhotoPath", mCM);
//                    }catch(IOException ex){
//                        Log.e("TAG", "Image file creation failed", ex);
//                    }
//                    if(photoFile != null){
//                        mCM = "file:" + photoFile.getAbsolutePath();
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                    }else{
//                        takePictureIntent = null;
//                    }
//                }
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

//        "/chat/index/?id=" + BOT_ID + '&name=' + BOT_NAME + '&theme=' +
//                BOT_THEME + '&easychat_window_location=' + easychat_window_location +
//                '&form_assist_id=' + easychat_form_assist_id + '&do_not_disturb=' + easychat_do_not_disturb +
//                '&is_lead_generation=' + is_lead_generation + '&page_category=' + easychat_page_category.toString() +
//                '&meta_data=' + meta_tags_information + '&easychat_intent_name=' + easychat_intent_name +
//                '&is_web_landing_allowed=' + is_web_landing_allowed + '&campaign_link_query_id=' + campaign_link_query_id +
//                '&selected_language=' + selected_language + '&is_initial_trigger_intent=' + is_initial_trigger_intent +
//                '&web_page_source=' + web_page_source;
//        String finalUrl = GlobalParams.domain_name+"/chat/bot/?id="+GlobalParams.bot_id+"&name=uat";
        String finalUrl = GlobalParams.getBase_url()+"/chat/index/?id="+GlobalParams.bot_id+"&channel=Android";
//        webView.loadUrl("https://easychat-dev.allincall.in/chat/bot/?id=216");
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
                // Directly request for required permissions, without explanation
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

}
