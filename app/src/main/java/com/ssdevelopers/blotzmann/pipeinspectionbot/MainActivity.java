package com.ssdevelopers.blotzmann.pipeinspectionbot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    private Button start;
    private Button stop;
    private TextView status;
    private TextView data1;
    private TextView data2;
    private TextView data3;
    private TextView data4;
    private TextView data5;
    private TextView data6;
    private TextView data7;
    private TextView data8;
    private TextView data9;
    private Boolean check;

    private String csvFile = "myData.xls";
    private File directory;
    WritableWorkbook workbook;
    WritableSheet sheet;

    private BluetoothAdapter madapter;
    private static final int REQUEST_ENABLE_BT=1;
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");
    private static final String device_name = "HC-05";



    private static final int STATE_CONNECTING =2;
    private static final int STATE_CONNECTED =3;
    private static final int STATE_CONNECTION_FAILED =4;
    private static final int STATE_MESSAGE_RECEIVED =5;
    StringBuilder stringBuilder;
    ConnectedThread connectedThread;
    ConnectThread connectThread;
    ArrayList<Float> datalist1;
    ArrayList<Float> datalist2;
    ArrayList<Float> datalist3;
    ArrayList<Float> datalist4;
    ArrayList<Float> datalist5;
    ArrayList<Float> datalist6;
    ArrayList<Float> datalist7;
    ArrayList<Float> datalist8;
    ArrayList<Float> datalist9;
    ArrayList<Float> distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        stringBuilder = new StringBuilder();

        madapter = BluetoothAdapter.getDefaultAdapter();


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!madapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else{
                    Toast.makeText(MainActivity.this, "Device does not bluetooth",Toast.LENGTH_LONG).show();
                }

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectThread.cancel();
                connectedThread.cancel();
                File sd = Environment.getExternalStorageDirectory();
                directory= new File(sd.getAbsolutePath());
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }
                try{
                    File file = new File(directory,csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    sheet = workbook.createSheet("bot data", 0);
                    sheet.addCell(new Label(0, 0, "sensor1"));
                    sheet.addCell(new Label(1, 0, "sensor2"));
                    sheet.addCell(new Label(2, 0, "sensor3"));
                    sheet.addCell(new Label(3, 0, "sensor4"));
                    sheet.addCell(new Label(4, 0, "sensor5"));
                    sheet.addCell(new Label(5, 0, "sensor6"));
                    sheet.addCell(new Label(6, 0, "sensor7"));
                    sheet.addCell(new Label(7, 0, "sensor8"));
                    sheet.addCell(new Label(8, 0, "sensor9"));
                    sheet.addCell(new Label(9, 0, "distance travelled"));

                    for(int i=0;i<datalist1.size();i++){
                        sheet.addCell(new Label(0,i+1,datalist1.get(i).toString()));
                        sheet.addCell(new Label(1,i+1,datalist2.get(i).toString()));
                        sheet.addCell(new Label(2,i+1,datalist3.get(i).toString()));
                        sheet.addCell(new Label(3,i+1,datalist4.get(i).toString()));
                        sheet.addCell(new Label(4,i+1,datalist5.get(i).toString()));
                        sheet.addCell(new Label(5,i+1,datalist6.get(i).toString()));
                        sheet.addCell(new Label(6,i+1,datalist7.get(i).toString()));
                        sheet.addCell(new Label(7,i+1,datalist8.get(i).toString()));
                        sheet.addCell(new Label(8,i+1,datalist9.get(i).toString()));
                        sheet.addCell(new Label(9,i+1,distance.get(i).toString()));
                    }
                    workbook.write();
                    workbook.close();
                    Toast.makeText(MainActivity.this,"Data exported in a excel file",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    Log.i("insideListener","insideListener");
                    e.printStackTrace();
                }
                Intent i =new Intent(MainActivity.this,Results.class);
                Bundle b =new Bundle();
                b.putSerializable("DataList1",datalist1);
                b.putSerializable("DataList2",datalist2);
                b.putSerializable("DataList3",datalist3);
                b.putSerializable("DataList4",datalist4);
                b.putSerializable("DataList5",datalist5);
                b.putSerializable("DataList6",datalist6);
                b.putSerializable("DataList7",datalist7);
                b.putSerializable("DataList8",datalist8);
                b.putSerializable("DataList9",datalist9);
                b.putSerializable("distanceList",distance);
                Log.i("ResultsData",String.valueOf(datalist1.size()));
                i.putExtra("data",b);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        madapter.disable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                    // Start connect thread by giving device
                String address = null;
                for (BluetoothDevice d : madapter.getBondedDevices()){
                    if (d.getName().equals(device_name)){
                        address = d.getAddress();
                    }
                }
                if (address!=null){
                    BluetoothDevice device = madapter.getRemoteDevice(address);

                    // start Connect thread
                    connectThread = new ConnectThread(device);
                    connectThread.start();
                    status.setText("started connect thread");

                }else{
                    Toast.makeText(MainActivity.this,"Pair Bluetooth module to your device and try again..",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MainActivity.this,"Enable bluetooth for inspection",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initialize() {
        start =(Button) findViewById(R.id.start);
        status = (TextView) findViewById(R.id.status);
        data1 = (TextView) findViewById(R.id.data1);
        data2 = (TextView) findViewById(R.id.data2);
        data3 = (TextView) findViewById(R.id.data3);
        data4 = (TextView) findViewById(R.id.data4);
        data5 = (TextView) findViewById(R.id.data5);
        data6 = (TextView) findViewById(R.id.data6);
        data7 = (TextView) findViewById(R.id.data7);
        data8 = (TextView) findViewById(R.id.data8);
        stop = (Button) findViewById(R.id.stop);
        data9 = (TextView) findViewById(R.id.data9);
        datalist1=new ArrayList<>();
        datalist2=new ArrayList<>();
        datalist3=new ArrayList<>();
        datalist4=new ArrayList<>();
        datalist5=new ArrayList<>();
        datalist6=new ArrayList<>();
        datalist7=new ArrayList<>();
        datalist8=new ArrayList<>();
        datalist9=new ArrayList<>();
        distance=new ArrayList<>();
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    // set text of message text view
                    byte[] readBuff = (byte[]) msg.obj;
                    String Msg = new String(readBuff,0,msg.arg1);
                    Log.i(TAG,Msg);
                    stringBuilder.append(Msg);
                    Log.i("Total Line",stringBuilder.toString());
                    if (stringBuilder.toString().contains("\n")) {
                        String[] values = stringBuilder.toString().split("\\|");
                        stringBuilder = new StringBuilder();
                        if (values.length == 10) {

                            data1.setText(values[0]);
                            data2.setText(values[1]);
                            data3.setText(values[2]);
                            data4.setText(values[3]);
                            data5.setText(values[4]);
                            data6.setText(values[5]);
                            data7.setText(values[6]);
                            data8.setText(values[7]);
                            data9.setText(values[8]);

                            Float y;
                            try{
                                y=Float.parseFloat(values[0]);
                                y=Float.parseFloat(values[1]);
                                y=Float.parseFloat(values[2]);
                                y=Float.parseFloat(values[3]);
                                y=Float.parseFloat(values[4]);
                                y=Float.parseFloat(values[5]);
                                y=Float.parseFloat(values[6]);
                                y=Float.parseFloat(values[7]);
                                y=Float.parseFloat(values[8]);
                                y=Float.parseFloat(values[9]);
                                check=true;
                            }catch (NumberFormatException e){
                                check=false;
                            }
                            if(check){
                            datalist1.add(Float.parseFloat(values[0]));
                            datalist2.add(Float.parseFloat(values[1]));
                            datalist3.add(Float.parseFloat(values[2]));
                            datalist4.add(Float.parseFloat(values[3]));
                            datalist5.add(Float.parseFloat(values[4]));
                            datalist6.add(Float.parseFloat(values[5]));
                            datalist7.add(Float.parseFloat(values[6]));
                            datalist8.add(Float.parseFloat(values[7]));
                            datalist9.add(Float.parseFloat(values[8]));
                            distance.add(Float.parseFloat(values[9]));

                        }
                        }
                    }
                    break;
            }
            return true;
        }
    });

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            madapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                mHandler.sendMessage(message);


                connectedThread = new ConnectedThread(mmSocket);
                connectedThread.start();

            } catch (IOException connectException) {
                // Unable to connect; close the socket and return
                Log.e(TAG,"connect method exception",connectException);

                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                mHandler.sendMessage(message);

                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.


        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket){
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

        }

        @Override
        public void run() {
            byte[] mmBuffer = new byte[1024];
            // bytes returned from read()
            int bytes;

            while(true){
                try {
                        bytes=mmInStream.read(mmBuffer);
                        mHandler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,mmBuffer).sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }

    }


}
