package com.example.healthapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.healthapp.database.AppDatabase;
import com.example.healthapp.entities.Option;
import com.example.healthapp.fragments.CryptoFragment;
import com.example.healthapp.fragments.NewsFragment;
import com.example.healthapp.fragments.NoteFragment;
import com.example.healthapp.fragments.StepFragment;
import com.example.healthapp.fragments.StockFragment;
import com.example.healthapp.fragments.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setFragment(new StepFragment());

        SharedPreferences settings = getSharedPreferences("RoomSettings", MODE_PRIVATE);
        boolean isFirstTime = settings.getBoolean("firstTime", true);

        db = AppDatabase.getInstance(this);

        if (isFirstTime) {
            // Set up six fragments, they can all be customized
            Option step = new Option(R.drawable.baseline_directions_walk_24, "Step", 1);
            Option weather = new Option(R.drawable.baseline_wb_sunny_24, "Weather", 2);
            Option stock = new Option(R.drawable.stock, "Stock", 3);
            Option note = new Option(R.drawable.note, "Note", 0);
            Option news = new Option(R.drawable.news, "News", 0);
            Option crypto = new Option(R.drawable.bitcoin, "Crypto", 0);

            new Thread(() -> {
                db.optionDao().insert(step);
                db.optionDao().insert(weather);
                db.optionDao().insert(stock);
                db.optionDao().insert(note);
                db.optionDao().insert(news);
                db.optionDao().insert(crypto);
            }).start();

            SharedPreferences.Editor update = settings.edit().putBoolean("firstTime", false);
            update.apply();
            ;
        }

        bottomNav = findViewById(R.id.bottomNavItem);
        Menu menu = bottomNav.getMenu();
        initApp(menu);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.pos1) {
                    db.optionDao().getSelectedNum1().observe(MainActivity.this, new Observer<Option>() {
                        @Override
                        public void onChanged(Option option) {
                            changeFragment(option);
                            item.setIcon(option.getImg());
                            item.setTitle(option.getName());
                        }
                    });
                } else if (item.getItemId() == R.id.pos2) {
                    db.optionDao().getSelectedNum2().observe(MainActivity.this, new Observer<Option>() {
                        @Override
                        public void onChanged(Option option) {
                            changeFragment(option);
                            item.setIcon(option.getImg());
                            item.setTitle(option.getName());
                        }
                    });
                } else if (item.getItemId() == R.id.pos3) {
                    db.optionDao().getSelectedNum3().observe(MainActivity.this, new Observer<Option>() {
                        @Override
                        public void onChanged(Option option) {
                            changeFragment(option);
                            item.setIcon(option.getImg());
                            item.setTitle(option.getName());
                        }
                    });
                }
                return true;
            }
        });
    }

    private void initApp(Menu menu){
        db.optionDao().getSelectedNum1().observe(MainActivity.this, new Observer<Option>() {
            @Override
            public void onChanged(Option option) {
                changeFragment(option);
                menu.findItem(R.id.pos1).setIcon(option.getImg());
                menu.findItem(R.id.pos1).setTitle(option.getName());
            }
        });
        db.optionDao().getSelectedNum2().observe(MainActivity.this, new Observer<Option>() {
            @Override
            public void onChanged(Option option) {
                menu.findItem(R.id.pos2).setIcon(option.getImg());
                menu.findItem(R.id.pos2).setTitle(option.getName());
            }
        });
        db.optionDao().getSelectedNum3().observe(MainActivity.this, new Observer<Option>() {
            @Override
            public void onChanged(Option option) {
                menu.findItem(R.id.pos3).setIcon(option.getImg());
                menu.findItem(R.id.pos3).setTitle(option.getName());
            }
        });
    }

    private void changeFragment(Option option) {
        if (option == null) {
            setFragment(new StepFragment());
        } else {
            if (option.getName().equals("Stock")) {
                setFragment(new StockFragment());
            } else if (option.getName().equals("Note")) {
                setFragment(new NoteFragment());
            } else if (option.getName().equals("News")) {
                setFragment(new NewsFragment());
            } else if (option.getName().equals("Crypto")) {
                setFragment(new CryptoFragment());
            } else if (option.getName().equals("Step")) {
                setFragment(new StepFragment());
            } else if (option.getName().equals("Weather")) {
                setFragment(new WeatherFragment());
            }
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}