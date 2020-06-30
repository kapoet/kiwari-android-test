package com.ervin.kiwariandroidtest;

import android.app.Application;

import com.ervin.kiwariandroidtest.di.AppComponent;
import com.ervin.kiwariandroidtest.di.AppModule;
import com.ervin.kiwariandroidtest.di.DaggerAppComponent;
import com.ervin.kiwariandroidtest.di.FirebaseModule;
import com.ervin.kiwariandroidtest.di.RoomModule;

public class App extends Application {
    public static AppComponent application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = initDagger(this);
    }

    private AppComponent initDagger(App application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .firebaseModule(new FirebaseModule())
                .roomModule(new RoomModule(application))
                .build();
    }
}
