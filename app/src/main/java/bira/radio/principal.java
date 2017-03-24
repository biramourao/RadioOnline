package bira.radio;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class principal extends AppCompatActivity {

    private ImageButton button_stop;
    private ImageButton button_play;
    private String STREAM_URL = "http://playsorriafm.netpub.com.br:8152/live";
    private MediaPlayer mePlayer;
    private boolean teste;
    private final static int MAX_VOLUME = 100;
    private SeekBar cVolume;
    private TextView texto_barra_volume;
    private boolean onOff =false;

    public boolean verificaConexao() {
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            teste = true;
        } else {
            teste = false;
        }
        return teste;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        button_stop = (ImageButton) findViewById(R.id.button_stop);
        button_play = (ImageButton) findViewById(R.id.button_play);
        cVolume = (SeekBar) findViewById(R.id.cVolume);
        texto_barra_volume = (TextView) findViewById(R.id.texto_barra_volume);


        button_play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (onOff == false) {

                    verificaConexao();
                    if (teste == true) {
                        Toast.makeText(
                                principal.this,
                                "Conectando... aguarde",
                                Toast.LENGTH_LONG
                        ).show();

                        try {

                            mePlayer = new MediaPlayer();
                            mePlayer.setDataSource(STREAM_URL);
                            mePlayer.prepareAsync();
                            mePlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });


                            final ProgressDialog pdialog = new ProgressDialog(principal.this);
                            pdialog.setCancelable(true);
                            pdialog.setMessage("Conectando ....");
                            pdialog.show();

                            new Thread(new Runnable() {
                                public void run() {
                                    try {

                                        Thread.sleep(10000);
                                        pdialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            onOff = true;

                        } catch (IOException e) {

                            e.printStackTrace();

                            Toast.makeText(
                                    principal.this,
                                    "Erro: " + e,
                                    Toast.LENGTH_LONG
                            ).show();

                            onOff =false;
                        }
                    } else {

                        Toast.makeText(
                                principal.this,
                                "Verifique sua conexão com a internet!!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });

        cVolume.setMax(MAX_VOLUME);
        cVolume.setProgress(50);
        texto_barra_volume.setText("Volume: "+cVolume.getProgress()+" / 100");

        cVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser){

                    texto_barra_volume.setText("Volume: " + cVolume.getProgress() + " / 100");

                    if( onOff == true){

                        float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                        mePlayer.setVolume(volume, volume);

                        /**Toast.makeText(
                         principal.this,
                         "Volume: "+cVolume.getProgress()+" / 100",
                         Toast.LENGTH_SHORT
                         ).show();**/
                    }
                }
                @Override
                public void onStartTrackingTouch (SeekBar seekBar){
                }

                @Override
                public void onStopTrackingTouch (SeekBar seekBar){
                }

        });

        /**SeekBar.OnSeekBarChangeListener mVolumeControlChangeListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                mePlayer.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };**/

        button_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onOff == true) {
                    mePlayer.stop();
                    mePlayer.release();
                    mePlayer = null;

                    Toast.makeText(
                            principal.this,
                            "Desconectando... ",
                            Toast.LENGTH_LONG
                    ).show();
                    onOff =false;
                }
                else{
                    Toast.makeText(
                            principal.this,
                            "Você ainda não deu 'Play' na Radio ",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

        });

    }

}
