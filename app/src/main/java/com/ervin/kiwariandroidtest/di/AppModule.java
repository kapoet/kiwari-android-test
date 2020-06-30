package com.ervin.kiwariandroidtest.di;

import com.ervin.kiwariandroidtest.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private App contextApp;

    public AppModule(App app) {
        this.contextApp = app;
    }

    @Provides
    @Singleton
    public App provideApp() {
        return this.contextApp;
    }


}
