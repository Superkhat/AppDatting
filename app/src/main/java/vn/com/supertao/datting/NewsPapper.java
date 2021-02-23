package vn.com.supertao.datting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class NewsPapper extends AppCompatActivity {
    private WebView wbNewsPapper;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_papper);
        btnBack=findViewById(R.id.btnbackNewsPapper);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wbNewsPapper.goBack();
            }
        });
        wbNewsPapper=findViewById(R.id.wbNewsPapper);
        wbNewsPapper.loadUrl("https://vnexpress.net/");
        wbNewsPapper.setWebViewClient(new WebViewClient());

    }
}
