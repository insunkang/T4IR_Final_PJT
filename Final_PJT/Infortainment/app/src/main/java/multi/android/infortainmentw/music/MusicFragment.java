package multi.android.infortainmentw.music;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import multi.android.infortainmentw.R;

public class MusicFragment extends Fragment{
    private ListView listView;
    public static ArrayList<MusicDTO> list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_music_fragment,container,false);
        listView=view.findViewById(R.id.music_listview);
        //MusicAdapter adapter=new MusicAdapter(list,getActivity());
        MusicAdapter adapter=new MusicAdapter(list,getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Intent intent=new Intent(getActivity(),MusicActivity.class);

                PlayFragment playFragment=new PlayFragment();
                MusicFragment musicFragment=new MusicFragment();

                Bundle bundle=new Bundle();
                bundle.putInt("position",position);
                bundle.putString("posi","123");
                bundle.putSerializable("playlist",list);
                playFragment.setArguments(bundle);
                Log.d("확인하는중","보내는 position::"+position);

                FragmentManager fragmentManager;
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction;
                transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fragment_music,playFragment);
                transaction.commit();


                //Intent intent=new Intent(getActivity(),PlayFragment.class);
                /*intent.putExtra("position",position);
                intent.putExtra("playlist",list);
                startActivity(intent);*/
            }
        });


        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMusicList();

    }

    public void getMusicList(){
        list=new ArrayList<>();
        //카져오고 싶은 컬럼명을 나열(음악의 아이디,앰블럼 아이디, 제목, 아티스트 정보를 가져온다)
        String[] projection={MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST};

        Cursor cursor=getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);

        while (cursor.moveToNext()){
            MusicDTO musicDTO=new MusicDTO();
            musicDTO.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDTO.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDTO.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDTO.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            list.add(musicDTO);
        }
        cursor.close();
    }

}