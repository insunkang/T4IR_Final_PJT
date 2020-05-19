package multi.android.infortainmentw.music;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import multi.android.infortainmentw.R;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<MusicDTO> list;
    private MediaPlayer mediaPlayer;
    private TextView title;
    private ImageView album,previous,play,pause,next;
    private SeekBar seekBar;
    boolean isPlaying=true;
    private ContentResolver res;
    private ProgressUpdate progressUpdate;
    private int position;

    private TextView playingTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        Intent intent=getIntent();
        mediaPlayer=new MediaPlayer();
        title=findViewById(R.id.title);
        album=findViewById(R.id.album);
        seekBar=findViewById(R.id.seekbar);

        position=intent.getIntExtra("position",0);
        Log.d("확인하는중",position+"::::::position값이야");
        list= (ArrayList<MusicDTO>) intent.getSerializableExtra("playlist");
        res=getContentResolver();

        previous=findViewById(R.id.pre);
        play=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        next=findViewById(R.id.next);

        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        next.setOnClickListener(this);

        playMusic(list.get(position));
        progressUpdate=new ProgressUpdate();
        progressUpdate.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                if (seekBar.getProgress()>0 && play.getVisibility()==View.GONE){
                    mediaPlayer.start();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(position+1<list.size()){
                    position++;
                    playMusic(list.get(position));
                }
            }
        });

    }


    public void playMusic(MusicDTO musicDTO){
        try{
            seekBar.setProgress(0);
            title.setText(musicDTO.getArtist()+"-"+musicDTO.getTitle());
            Uri musicURI=Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,""+musicDTO.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this,musicURI);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            if(mediaPlayer.isPlaying()){
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }else{
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }

            Bitmap bitmap= BitmapFactory.decodeFile(getCoverArtPath(Long.parseLong(musicDTO.getAlbumId()),getApplication()));
            album.setImageBitmap(bitmap);
        }catch (Exception e){
            Log.e("SimplePlayer",e.getMessage());

        }
    }

    //앨범이 저장되어 있는 경로를 리턴함
    private static String getCoverArtPath(long albumId, Context context){
        Cursor albumCursor=context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Audio.Albums.ALBUM_ART}
                                                                                                                ,MediaStore.Audio.Albums._ID+"=?",
                                                                                                                new String[]{Long.toString(albumId)},null);
        boolean queryResult=albumCursor.moveToFirst();
        String result=null;
        if(queryResult){
            result=albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();
                break;
            case R.id.pause:
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                break;
            case R.id.pre:
                if(position-1>=0){
                    position--;
                    playMusic(list.get(position));
                    seekBar.setProgress(0);
                }
                break;
            case R.id.next:
                if(position+1<list.size()){
                    position++;
                    playMusic(list.get(position));
                    seekBar.setProgress(0);
                }
                break;
        }
    }

    class ProgressUpdate extends Thread{
        @Override
        public void run() {
            while (isPlaying){

                try{
                    String time="";
                Thread.sleep(500);

                if(mediaPlayer!=null){

                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    time = mediaPlayer.getCurrentPosition()+"";

                }
                System.out.println(time);
                playingTime.setText(time);

            }catch (Exception e){
                    Log.e("ProgressUpdate",e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying=false;
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}
