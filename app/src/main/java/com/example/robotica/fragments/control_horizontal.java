package com.example.robotica.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;


import com.example.robotica.R;



public class control_horizontal extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    ImageButton botonOn;
    ImageButton botonOff;
    ImageButton up;
    ImageButton left;
    ImageButton right;
    ImageButton down;
    ImageButton stop;
    ImageButton emergency;
    ImageButton bluetooth;
    ImageButton luces;
    ImageButton robot;
    SeekBar velocidad;

    PopupWindow popupWindow;
    control_horizontal control_horizontal;
    boolean arrowrightstate=false;
    boolean arrowleftstate=false;
    boolean arrowdownstate=false;
    boolean arrowupstatw=false;

    boolean estado1=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//Mostrar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_control_horizontal, container, false);


        variables_botones(view);
        botones_acciones();
        velocidad();


        return view;
    }

    private void variables_botones(View view) {
        botonOn = view.findViewById(R.id.start_voz);
        botonOff = view.findViewById(R.id.stop_voz);
        up = view.findViewById(R.id.up);
        left = view.findViewById(R.id.left);
        right = view.findViewById(R.id.right);
        down = view.findViewById(R.id.down);
        stop = view.findViewById(R.id.stop_voz);
        emergency = view.findViewById(R.id.emergency_voz);
        bluetooth = view.findViewById(R.id.bluetooth_vertical);
        luces=view.findViewById(R.id.focovoz);
        robot=view.findViewById(R.id.robotvoz);
        velocidad=view.findViewById(R.id.velocidadvoz);




    }
    public void botones_acciones()
    {
        botonOn.setOnClickListener(this);
        botonOff.setOnClickListener(this);
        up.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        down.setOnClickListener(this);
        stop.setOnClickListener(this);
        luces.setOnClickListener(this);
        emergency.setOnClickListener(this);
        bluetooth.setOnClickListener(this);
        robot.setOnClickListener(this);
        velocidad.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v==bluetooth)
        {
            cambiar_imagen(bluetooth);
            vibrarCorto();

        }
        if(v==luces)

        {
            estado1=!estado1;
            cambiar_imagen(luces);
            vibrarCorto();

        }
        if(v==right)
        {
            cambiar_imagen(right);

            vibrarCorto();


        }
        if(v==left)
        {
            cambiar_imagen(left);
            vibrarCorto();
        }
        if(v==up)
        {
            cambiar_imagen(up);
            vibrarCorto();
        }
        if(v==down)
        {
            cambiar_imagen(down);
            vibrarCorto();
        }
        if(v==velocidad)
        {
            velocidad();
            vibrarCorto();
        }

    }

    @Override
    public boolean onLongClick(View view) {

        if (view == robot) {
            popup(robot);
                vibrarlargo();
        }

        return false;
    }



    public void cambiar_imagen(ImageButton boton )
    {
        if(boton==botonOn)
        {
            botonOn.setImageResource(R.drawable.go);
        }
        if(boton==botonOff)
        {
            botonOff.setImageResource(R.drawable.stop);
        }
        if (boton==luces)
        {
            if(!estado1){
                luces.setImageResource(R.drawable.foco);
            }
            if(estado1){
                luces.setImageResource(R.drawable.on);
            }

        }
        if (boton == stop)
        {

            robot.setImageResource(R.drawable.robotoff);

            luces.setImageResource(R.drawable.off);
            velocidad.setProgress(0);
        }
        if (boton == right)
        {

            if (arrowrightstate) {

                right.setImageResource(R.drawable.arrowright);
                arrowrightstate = false;
            } else {

                right.setImageResource(R.drawable.arrowwhiteright);
                arrowrightstate = true;
            }


        }
        if (boton == left)
        {
            if (arrowleftstate) {

                left.setImageResource(R.drawable.arroleftwhite);
                arrowleftstate = false;
            } else {

                left.setImageResource(R.drawable.leftarrow);
                arrowleftstate = true;
            }
        }
        if (boton==up)
        {
            if (arrowupstatw) {

                up.setImageResource(R.drawable.arrowwhiteup);
                arrowupstatw = false;
            } else {

                up.setImageResource(R.drawable.arrowup);
                arrowupstatw = true;
            }
        }
        if (boton==down)
        {
            if (arrowdownstate) {

                down.setImageResource(R.drawable.arrowwhitedown);
                arrowupstatw = false;
            } else {

                down.setImageResource(R.drawable.arrowdown);
                arrowdownstate = true;
            }
        }


    }

    public void vibrarCorto() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            // Duración de la vibración en milisegundos
            final int vibracionDuracion = 80;
            // Hacer vibrar el dispositivo
            vibrator.vibrate(vibracionDuracion);
        }
    }

    public void vibrarlargo() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            // Duración de la vibración en milisegundos
            final int vibracionDuracion = 300;
            // Hacer vibrar el dispositivo
            vibrator.vibrate(vibracionDuracion);
        }
    }




    public void popup (ImageButton posicion)
    {
// Crea un objeto LayoutInflater para inflar el layout del popup
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// Infla el layout del popup
        View popupView = inflater.inflate(R.layout.fragment_pop_up2, null);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
// Inicializa el PopupWindow con el layout inflado

        popupWindow = new PopupWindow(popupView, width, height, focusable);
// Establece el listener de toque para el layout del popup
        popupView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
//Cierra la ventana emergente cuando se toca fuera de ella
                popupWindow.dismiss();
                return true;
            }
        });
//Muestra la ventana emergente en el centro de la pantalla
        popupWindow.showAtLocation(posicion, Gravity.CENTER, 0, 0);
    }

    public void velocidad() {
        // Set listener to track seek bar changes
        velocidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar velocidad, int progress, boolean fromUser) {
                // Update background color based on progress
                if (progress == 0) {
                    velocidad.setBackgroundColor(Color.RED);
                } else if (progress > 0 && progress < 25) {
                    velocidad.setBackgroundColor(Color.YELLOW);
                } else if (progress >= 25 && progress < 50) {
                    velocidad.setBackgroundColor(Color.parseColor("#FFA500")); // Orange
                } else if (progress >= 50 && progress < 75) {
                    velocidad.setBackgroundColor(Color.GREEN);
                } else if (progress >= 75 && progress < 100) {
                    velocidad.setBackgroundColor(Color.BLUE);
                } else if (progress == 100) {
                    velocidad.setBackgroundColor(Color.MAGENTA); // Puedes cambiar el color si lo prefieres
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar velocidad) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar velocidad) {
                // Not needed for this example
            }
        });
    }

}