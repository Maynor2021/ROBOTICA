package com.example.robotica.fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.robotica.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class control_vertical extends Fragment implements View.OnClickListener, View.OnLongClickListener {


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

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice hc05Device;
    private OutputStream outputStream;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ArrayAdapter<String> deviceListAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSIONS_CODE = 1;
    // UUID para HC-05
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    PopupWindow popupWindow;
    control_horizontal control_horizontal;
    boolean arrowrightstate = false;
    boolean arrowleftstate = false;
    boolean arrowupstate = false;
    boolean arrowdownstate = false;
    boolean estado1 = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//Mostrar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_control_vertical, container, false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth no es compatible con este dispositivo", Toast.LENGTH_SHORT).show();
            return view;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        variables_botones(view);
        botones_acciones();
        velocidad();
        return view;
    }

    private void variables_botones(View view) {
        botonOn = view.findViewById(R.id.start_voz);
        botonOff = view.findViewById(R.id.stop_voz);
        up = view.findViewById(R.id.Upvoz);
        left = view.findViewById(R.id.leftvertical);
        right = view.findViewById(R.id.rigthvertical);
        down = view.findViewById(R.id.downvertical);
        stop = view.findViewById(R.id.stop_voz);
        emergency = view.findViewById(R.id.emergency_voz);
        bluetooth = view.findViewById(R.id.bluetooth_vertical);
        luces = view.findViewById(R.id.focovoz);
        robot = view.findViewById(R.id.robotvoz);
        velocidad = view.findViewById(R.id.velocidadvoz);


    }

    public void botones_acciones() {
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

        robot.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v==botonOn)
        {
            cambiar_imagen(botonOn);

        }
        if (v == luces) {
            estado1 = !estado1;
            cambiar_imagen(luces);
            vibrarCorto();

        }
        if (v == right) {
            cambiar_imagen(right);

            vibrarCorto();


        }
        if (v == left) {
            cambiar_imagen(left);
            vibrarCorto();
        }
        if (v == up) {
            cambiar_imagen(up);
            vibrarCorto();
        }
        if (v == down) {
            cambiar_imagen(down);
            vibrarCorto();
        }
        if (v == velocidad) {
            velocidad();
            vibrarCorto();
        }
        if (v == stop) {
            cambiar_imagen(stop);
            vibrarlargo();
        }
        if (v == emergency) {
            cambiar_imagen(emergency);
            vibrarlargo();
        }
        if (v == robot)
        {
            popup(robot);
            vibrarCorto();
        }



    }

    @Override
    public boolean onLongClick(View view) {

        if (view == robot) {

            popupV(robot);
            vibrarlargo();

        }
        if (view==bluetooth)
        {
            discoverDevices();
            vibrarlargo();
            return true;
        }
        return false;
    }


    public void cambiar_imagen(ImageButton boton) {
        if (boton == botonOn) {
            botonOn.setImageResource(R.drawable.go);
            robot.setImageResource(R.drawable.robot);
            up.setImageResource(R.drawable.arrowup);
            down.setImageResource(R.drawable.arrowdown);
            left.setImageResource(R.drawable.leftarrow);
            right.setImageResource(R.drawable.arrowright);

        }
        if (boton == botonOff) {
            botonOff.setImageResource(R.drawable.stop);
        }
        if (boton == luces) {
            if (!estado1) {
                luces.setImageResource(R.drawable.foco);
            }
            if (estado1) {
                luces.setImageResource(R.drawable.on);
            }

        }
        if (boton == stop) {

            robot.setImageResource(R.drawable.robotoff);
              vibrarCorto();
          //  luces.setImageResource(R.drawable.foco); solo el paro de emergencia paga el foco  y debe ser con click largo , no aparace iconos de las taps , deben de aparecer
            left.setImageResource(R.drawable.arroleftwhite);
            velocidad.setProgress(0);
            up.setImageResource(R.drawable.arrowwhiteup);
            right.setImageResource(R.drawable.arrowwhiteright);
            down.setImageResource(R.drawable.arrowwhitedown);


        }
        if (boton == right) {

            if (arrowrightstate) {

                right.setImageResource(R.drawable.arrowwhiteright);
                arrowrightstate = false;
            } else {

                right.setImageResource(R.drawable.arrowright);
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
        if (boton == up) {
            if (arrowupstate) {

                up.setImageResource(R.drawable.arrowwhiteup);
                arrowupstate = false;
            } else {

                up.setImageResource(R.drawable.arrowup);
                arrowupstate = true;
            }
        }
        if (boton == down) {
            if (arrowdownstate) {

                down.setImageResource(R.drawable.arrowwhitedown);
                arrowdownstate = false;
            } else {

                down.setImageResource(R.drawable.arrowdown);
                arrowdownstate = true;
            }
        }
        if (boton == emergency)
        {
            vibrarCorto();
            robot.setImageResource(R.drawable.robotoff);

            luces.setImageResource(R.drawable.foco);
            left.setImageResource(R.drawable.arroleftwhite);
            velocidad.setProgress(0);
            up.setImageResource(R.drawable.arrowwhiteup);
            right.setImageResource(R.drawable.arrowwhiteright);
            down.setImageResource(R.drawable.arrowwhitedown);

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


    public void popup(ImageButton posicion) {
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
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//Cierra la ventana emergente cuando se toca fuera de ella
                popupWindow.dismiss();
                return true;
            }
        });
//Muestra la ventana emergente en el centro de la pantalla
        popupWindow.showAtLocation(posicion, Gravity.CENTER, 0, 0);
    }

    public void popupV(ImageButton posicion) {
// Crea un objeto LayoutInflater para inflar el layout del popup
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// Infla el layout del popup
        View popupView = inflater.inflate(R.layout.logoemergente, null);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
// Inicializa el PopupWindow con el layout inflado

        popupWindow = new PopupWindow(popupView, width, height, focusable);
// Establece el listener de toque para el layout del popup
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                }
                if (progress >= 1 && progress < 25) {
                    velocidad.setBackgroundColor(Color.YELLOW);
                } else if (progress >= 25 && progress < 50) {
                    velocidad.setBackgroundColor(Color.parseColor("#FFA500")); // Orange
                } else if (progress >= 50 && progress < 75) {
                    velocidad.setBackgroundColor(Color.GREEN);
                } else if (progress >= 100) {
                    velocidad.setBackgroundColor(Color.BLUE);
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
    private boolean hasPermissions (Context context)
    {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions ()
    {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN},
                REQUEST_PERMISSIONS_CODE);
    }
    private void checkAndDiscoverDevices ()
    {
        if (hasPermissions(requireContext())) {
            discoverDevices();
        } else {
            requestPermissions();
        }
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
// TODO: Consider calling
// ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
// public void onRequestPermissionsResult(int requestCode, String[] permissions,
// int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
                    return;
                }
                deviceList.add(device.getName() + "\n" + device.getAddress());
                deviceListAdapter.notifyDataSetChanged();
            }
        }
    };



    private void discoverDevices ()
    {
        deviceList.clear();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
// TODO: Consider calling
// ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
// public void onRequestPermissionsResult(int requestCode, String[] permissions,
// int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        requireContext().registerReceiver(receiver, filter);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccionar el modulo HC-05");
        View customLayout = requireActivity().getLayoutInflater().inflate(R.layout.fragmente_dialog_device, null);
        builder.setView(customLayout);
        ListView listView = customLayout.findViewById(R.id.device_list);
        deviceListAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(deviceListAdapter);
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            String item = deviceList.get(position);
            String address = item.substring(item.length() - 17);
            connectToDevice(address);
//Actualizado para fragmento
            requireContext().unregisterReceiver(receiver);
        });
        builder.setNegativeButton("Cancel", (dialog, which) ->
        {
            bluetoothAdapter.cancelDiscovery();
//Actualizado para fragmento
            requireContext().unregisterReceiver(receiver);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void connectToDevice (String address)
    {
        hc05Device = bluetoothAdapter.getRemoteDevice(address);
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
// TODO: Consider calling
// ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
// public void onRequestPermissionsResult(int requestCode, String[] permissions,
// int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(requireContext(), "Connected to HC-05", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Connection failed", Toast.LENGTH_SHORT).show();
        }
    }

    ///aqui va sendata/////////////////////////////////////////////////////////
    public void sendData (String data)
    {
        if (outputStream != null) {
            try {
                outputStream.write(data.getBytes());
                Toast.makeText(requireContext(), "Data sent", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to send data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Not connected to HC-05", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                             int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
//Permisos concedidos, ejecutar el método discoverDevices
                discoverDevices();
            } else {
//Permisos no concedidos
                Toast.makeText(requireContext(), "Permissions required to use Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
