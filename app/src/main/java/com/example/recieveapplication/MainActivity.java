package com.example.recieveapplication;

import static com.example.recieveapplication.MainActivity.logTag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
class Network implements Runnable
{

    public int count = 0;
    String sentence;

    @Override
    public void run()
    {

        try {


            DatagramSocket serverSocket = new DatagramSocket(9000);
            byte[] receiveData = new byte[8];
            String sendString = "polo";
            byte[] sendData = sendString.getBytes("UTF-8");

            System.out.printf("Listening on udp:%s:%d%n",
                    InetAddress.getLocalHost().getHostAddress(), 9000);
            Log.d(logTag, "here1");
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);

            while(true)
            {
                Log.d(logTag, "here2");
                serverSocket.receive(receivePacket);
                sentence = new String( receivePacket.getData(), 0,
                        receivePacket.getLength() );
                Log.d(logTag, "hereReceive");
                System.out.println("RECEIVED: " + sentence);

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
                Log.d(logTag, "here3");
            }
        } catch (IOException e) {
            System.out.println(e);
        }


    }

}
public class MainActivity extends AppCompatActivity
{
    DatagramSocket datagramSocket;
    DatagramPacket packet;
    TextView tv2;
    Button button1;
    static String logTag = "logTag123";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(logTag, "Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv2 = findViewById(R.id.tv1);
        button1 = findViewById(R.id.button123);


        Network network = new Network();
        Thread thread = new Thread(network);
        thread.start();
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tv2.setText(network.sentence);
            }
        });
    }
}