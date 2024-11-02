package com.example.robotica.fragments;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.robotica.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robotica.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class control_voz extends Fragment implements View.OnClickListener, View.OnLongClickListener
{
    //Atributos
    ImageButton robot;
    ImageButton icono;
    ImageButton bluetooth;
    ImageButton estop;
    ImageButton stop;
    ImageButton start;
    ImageButton voz;
    ImageButton luces;
    SeekBar velocidad;
    control_horizontal vibracio;
    boolean estado1 = false;
    PopupWindow popupWindow;

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



    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//Mostrar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_control_voz, container, false);

        variables_botones(view);
        botones_accion();
        velocidad();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth no es compatible con este dispositivo", Toast.LENGTH_SHORT).show();
            return view;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return view;
    }

    //Metodo donde se asignan los elementos de la interfaz de usuario a las variables definidas
    private void variables_botones(View view)
    {
        robot = view.findViewById(R.id.robotvoz);
        bluetooth = view.findViewById(R.id.bluetooth_vertical);
        estop = view.findViewById(R.id.stop_voz);
        stop = view.findViewById(R.id.stop_voz);
        start = view.findViewById(R.id.start_voz);
        voz = view.findViewById(R.id.mic);
        luces = view.findViewById(R.id.focovoz);
        velocidad = view.findViewById(R.id.velocidadvoz);
        icono=view.findViewById(R.id.Upvoz);
    }
    //Metodo para detectar los botones presionados
    private void botones_accion()
    {
//click corto
        bluetooth.setOnClickListener(this);
        stop.setOnClickListener(this);
        start.setOnClickListener(this);
        luces.setOnClickListener(this);
        voz.setOnClickListener(v -> startVoiceRecognition());

//click largo

        bluetooth.setOnLongClickListener(this);
        robot.setOnLongClickListener(this);
        estop.setOnLongClickListener(this);
    }
    //Metodo para saber que tipo de boton fue presionado
    @Override
    public void onClick(View v)
    {
        if (v == bluetooth)
        {

        }
        if (v == stop)
        {
            cambiar_imagenes(stop);
        }
        if (v == start)
        {
            cambiar_imagenes(start);
        }
        if (v == luces)
        {
            estado1 = !estado1;
            cambiar_imagenes(luces);
        }
        if(v==voz){
            startVoiceRecognition();
            vibracio.vibrarCorto();
        }
    }
    //Metodo para detectar los botones con click largo
    @Override
    public boolean onLongClick(View v)
    {
        if (v == bluetooth)
        {
            discoverDevices();
           vibracio.vibrarCorto();
        }

        if (v == estop)
        {

            cambiar_imagenes(estop);
        }
        if (v == robot)
        {
            cambiar_imagenes(robot);
            popup(robot);

        }

        return false;
    }
    //Metodo para cambiar las imagenes segun el estado y el evento
    private void cambiar_imagenes(ImageButton boton)
    {
        if (boton == stop)
        {
            robot.setImageResource(R.drawable.robotoff);
            velocidad.setProgress(0);
            luces.setImageResource(R.drawable.foco);
            velocidad.setProgress(0);

        }
        if (boton == start)
        {
            robot.setImageResource(R.drawable.robot);

        }
        if (boton == luces)
        {
            if (estado1 == false)
            {
                luces.setImageResource(R.drawable.off);
            }
            if (estado1 == true)
            {
                luces.setImageResource(R.drawable.on);
            }
        }
        if (boton == icono)

        {


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


    public void velocidad ()
    {
// Set listener to track seek bar changes
        velocidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar velocidad, int progress, boolean fromUser)
            {
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
                } else if (progress >= 100){
                    velocidad.setBackgroundColor(Color.BLUE);

                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar velocidad)
            {
            }
            @Override
            public void onStopTrackingTouch(SeekBar velocidad)
            {
            }
        });
    }
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES"); // Fuerza el uso del español (España)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES"); // Preferencia de idioma español (España)
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "es-ES"); // Forzar el idioma
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "CONTROL ROBOTICO ");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "El reconocimiento de voz no es compatible con este dispositivo.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT)

        {
            if (resultCode == RESULT_OK && data != null)
            {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && result.contains("encender luces"))
                {
                    estado1 = !estado1;
                    cambiar_imagenes(luces);
                }
                if (result != null && result.contains("apagar luces"))
                {
                    estado1 = !estado1;
                    cambiar_imagenes(luces);
                }
                if (result != null && result.contains("encender robot"))
                {
                    cambiar_imagenes(start);
                }
                if (result != null && result.contains("apagar robot")){
                    cambiar_imagenes(stop);
                }
                if (result != null && result.contains("derecha")){
                    icono.setImageResource(R.drawable.arrowright);

                }
                if (result != null && result.contains("izquierda")){
                    icono.setImageResource(R.drawable.leftarrow);

                }
                if (result != null && result.contains("arriba")){
                    icono.setImageResource(R.drawable.arrowup);

                }
                if (result != null && result.contains("abajo")){
                    icono.setImageResource(R.drawable.arrowdown);
                }

            }
        }
    }
    //barra de velocidad no vibra ni cambia de color , poner en el popup , las intrucciones, al presionar go y stop debe cambiar las flehcas .

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