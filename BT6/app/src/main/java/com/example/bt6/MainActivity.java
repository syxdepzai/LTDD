package com.example.bt6;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bt6.adapter.ViewPager2Adapter;
import com.example.bt6.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPager2Adapter viewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolBar);

        // Setup ViewPager2 Adapter
        viewPager2Adapter = new ViewPager2Adapter(this);
        binding.viewPager2.setAdapter(viewPager2Adapter);

        // Setup TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Xác nhận");
                            break;
                        case 1:
                            tab.setText("Lấy hàng");
                            break;
                        case 2:
                            tab.setText("Đang giao");
                            break;
                        case 3:
                            tab.setText("Đánh giá");
                            break;
                        case 4:
                            tab.setText("Hủy");
                            break;
                    }
                }).attach();

        // Handle Tab Selection
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager2.setCurrentItem(tab.getPosition());
                changeFabIcon(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Handle ViewPager2 Page Change
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }

    private void changeFabIcon(final int index) {
        new Handler().postDelayed(() -> {
            switch (index) {
                case 0:
                    binding.fabAction.setImageResource(android.R.drawable.ic_input_add);
                    break;
                case 1:
                    binding.fabAction.setImageResource(android.R.drawable.ic_menu_edit);
                    break;
                case 2:
                    binding.fabAction.setImageResource(android.R.drawable.ic_menu_send);
                    break;
                case 3:
                    binding.fabAction.setImageResource(android.R.drawable.ic_menu_view);
                    break;
                case 4:
                    binding.fabAction.setImageResource(android.R.drawable.ic_menu_delete);
                    break;
            }
        }, 200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.menuSearch) {
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuNewGroup) {
            Toast.makeText(this, "New Group clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuBroadcast) {
            Toast.makeText(this, "Broadcast clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuWeb) {
            Toast.makeText(this, "Web clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuMessage) {
            Toast.makeText(this, "Message clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuSetting) {
            Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}