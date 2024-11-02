package com.example.robotica;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.robotica.fragments.control_horizontal;
import com.example.robotica.fragments.control_vertical;
 import com.example.robotica.fragments.control_voz;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
// Retorna el fragmento correspondiente a la posición
        switch (position) {
            case 0:
                return new control_vertical();
            case 1:
                return new control_horizontal();

            case 2:
                return new control_voz();
            default:
                return new control_vertical();
        }
    }

    @Override
    public int getItemCount() {
// Retorna el número total de fragmentos
        return 3;


    }
}

