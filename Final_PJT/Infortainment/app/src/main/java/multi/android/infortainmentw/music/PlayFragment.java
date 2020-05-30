package multi.android.infortainmentw.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import multi.android.infortainmentw.R;

public class PlayFragment extends Fragment implements View.OnClickListener{
    private ArrayList<MusicDTO> list;
    private MediaPlayer mediaPlayer;
    private TextView title;
    private ImageView album,previous,play,pause,next;
    private SeekBar seekBar;
    boolean isPlaying=true;
    private ContentResolver res;
    private ProgressUpdate progressUpdate;
    private int position;
    private Button musicback_btn;

    private TextView playingTime;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_music,container,false);

        mediaPlayer=new MediaPlayer();
        title=view.findViewById(R.id.title);
        album=view.findViewById(R.id.album);
        seekBar=view.findViewById(R.id.seekbar);

        musicback_btn=view.findViewById(R.id.musicback_btn);

/*
        position=intent.getIntExtra("position",0);
        list= (ArrayList<MusicDTO>) intent.getSerializableExtra("playlist");
        res=getContext().getContentResolver();*/

        position=getArguments().getInt("position",100);
        Log.d("확인중",position+"::::position값이야");



        list= (ArrayList<MusicDTO>) getArguments().getSerializable("playlist");

        final MusicFragment musicFragment=new MusicFragment();

        previous=view.findViewById(R.id.pre);
        play=view.findViewById(R.id.play);
        pause=view.findViewById(R.id.pause);
        next=view.findViewById(R.id.next);

        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        next.setOnClickListener(this);

        playMusic(list.get(position));
        progressUpdate=new ProgressUpdate();
        progressUpdate.start();

        musicback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager;
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction;
                transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fragment_music,musicFragment);
                transaction.commit();
            }
        });

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


        return view;
    }

    public void playMusic(MusicDTO musicDTO){
        try{
            seekBar.setProgress(0);
            title.setText(musicDTO.getArtist()+"-"+musicDTO.getTitle());
            Uri musicURI=Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,""+musicDTO.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getActivity(),musicURI);
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

            Bitmap bitmap= BitmapFactory.decodeFile(getCoverArtPath(Long.parseLong(musicDTO.getAlbumId()),getContext()));
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
    public void onDestroy() {
        super.onDestroy();
        isPlaying=false;
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}
