package vn.com.supertao.datting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Movie extends AppCompatActivity {
    private WebView wbMovie;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        btnBack=findViewById(R.id.btnbackMovie);
        wbMovie=findViewById(R.id.wbMovie);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            wbMovie.goBack();
            }
        });
        wbMovie.loadUrl("https://www.galaxycine.vn/");
        wbMovie.setWebViewClient(new WebViewClient());
    }
}
