package com.dalingge.search;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appbar = (AppBarLayout) findViewById(R.id.app_bar);
        appbar.addOnOffsetChangedListener(this);
        searchView = (SearchView) findViewById(R.id.search_view);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float alpha = 1.0f - Math.abs((float) verticalOffset / appBarLayout.getTotalScrollRange());
        toolbar.setAlpha(1.0f - alpha);
        if(alpha==1){
            searchView.resetAnim();
        }
        if(alpha==0){
            searchView.startAnim();
        }
    }
}
