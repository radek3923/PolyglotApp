package pl.potocki.polyglotapp.database;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public WordDao provideWordDao(AppDatabase database) {
        return database.wordDao();
    }

    @Provides
    @Singleton
    public AppDatabase providesDatabase(Application application) {
        return AppDatabase.getInstance(application.getApplicationContext());
    }
}
