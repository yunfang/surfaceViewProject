package com.surface.surfaceviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.surface.surfaceviewproject.animation.AnimationView;
import com.surface.surfaceviewproject.notify.DataChangeNotification;
import com.surface.surfaceviewproject.notify.IssueKey;
import com.surface.surfaceviewproject.notify.ObserverGroup;

public class MainActivity extends AppCompatActivity {

    private ObserverGroup mObserverGroup;
    private AnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DisplayUtils.init(this);

        mObserverGroup = ObserverGroup.createConfirmPayDetailGroup();

        Button id_bu = (Button) findViewById(R.id.id_bu);
        animationView = (AnimationView) findViewById(R.id.anim_view);

        id_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.MESSAGE_PARSE_GIFT_NOTIFY,true);
                DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CHAT_WINDOW_INTEGRATED_MESSAGE,true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationView.surfaceDestroyed(null);
        animationView.destroyActivity();
        animationView = null;
    }
}
