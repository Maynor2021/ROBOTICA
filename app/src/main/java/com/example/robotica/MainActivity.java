package com.example.robotica;
import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
public class MainActivity extends AppCompatActivity
{
    TabLayout tablayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        elementos();
        programa();
    }
    //Metodo donde se asignan los elementos de la interfaz de usuario a las variables definidas
    private void elementos()
    {
        tablayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.viewpager);

    }
    //Metodo para la ejecucion del programa
    private void programa()
    {
// Configuración del ViewPager2 (Proporciona animaciones y transiciones más suaves y configurables)
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
// Configuración del listener para la selección de pestañas en TabLayout
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab)
            {
// Cambia la página del ViewPager2 según la pestaña seleccionada

                viewPager2.setCurrentItem(tab.getPosition());

// Cambia la orientación de la pantalla según la pestaña seleccionada
                switch (tab.getPosition())

                {

                    case 0: // VERTICAL
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        break;

                    case 1: // HORIZONTAL
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                        break;
                    case 2: // VOZ
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        break;

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

// Configuración del callback para los cambios de página en ViewPager2
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
// Selecciona la pestaña correspondiente en TabLayout cuando cambia la página

                tablayout.getTabAt(position).select();

            }
        });
    }

}