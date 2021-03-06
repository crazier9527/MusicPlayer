package com.chenhao.musicplayer.utils;

import android.content.Intent;

import com.chenhao.musicplayer.activity.MVActivity;
import com.chenhao.musicplayer.activity.MainActivity;
import com.chenhao.musicplayer.bean.AdArInfo;
import com.chenhao.musicplayer.bean.AdInfo;
import com.chenhao.musicplayer.bean.AlbumInfo;
import com.chenhao.musicplayer.bean.ArtistInfo;
import com.chenhao.musicplayer.bean.BillboardInfo;
import com.chenhao.musicplayer.bean.HitbillboardInfo;
import com.chenhao.musicplayer.bean.KubillboardInfo;
import com.chenhao.musicplayer.bean.ListInfo;
import com.chenhao.musicplayer.bean.MvInfo;
import com.chenhao.musicplayer.bean.MvplInfo;
import com.chenhao.musicplayer.bean.OnlineInfo;
import com.chenhao.musicplayer.bean.QzListInfo;
import com.chenhao.musicplayer.bean.SongListInfo;
import com.chenhao.musicplayer.bean.TabInfo;
import com.chenhao.musicplayer.mod.FragmentControl;
import com.chenhao.musicplayer.online.AlbumTabFragment;
import com.chenhao.musicplayer.online.ArtistTabFragment;
import com.chenhao.musicplayer.online.BillboardFragment;
import com.chenhao.musicplayer.online.KubillboardFragment;
import com.chenhao.musicplayer.online.ListFragment;
import com.chenhao.musicplayer.online.QzListFragment;
import com.chenhao.musicplayer.online.SongListTabFragment;
import com.chenhao.musicplayer.online.WebFragment;

/**
 * Created by chenhao on 2016/11/26.
 */

public class JumpUtils {
    public static void jumpFragment( OnlineInfo info){
        if(info instanceof SongListInfo){
            SongListTabFragment f = SongListTabFragment.newInstance(info.getId(), info.getName(), info.getImg(),info.getDigest());
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,SongListTabFragment.class.getSimpleName());
        }else if(info instanceof AdArInfo){
            WebFragment f = WebFragment.newInstance(((AdArInfo) info).getUrl(),info.getName());
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,WebFragment.class.getSimpleName());
        }else if(info instanceof AdInfo){
            WebFragment f = WebFragment.newInstance(((AdInfo) info).getUrl(),info.getName());
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,WebFragment.class.getSimpleName());
        }else if(info instanceof QzListInfo){
            QzListFragment f = QzListFragment.newInstance(info.getId(),info.getDigest(),info.getName());
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,QzListFragment.class.getSimpleName());
        }else if(info instanceof KubillboardInfo){
            KubillboardFragment f = KubillboardFragment.newInstance(info);
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,KubillboardFragment.class.getSimpleName());
        }else if(info instanceof BillboardInfo){
            BillboardFragment f = BillboardFragment.newInstance(info);
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,BillboardFragment.class.getSimpleName());
        }else if(info instanceof TabInfo){
            BillboardFragment f = BillboardFragment.newInstance(info);
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,BillboardFragment.class.getSimpleName());
        }else if(info instanceof HitbillboardInfo){
            WebFragment f = WebFragment.newInstance(((HitbillboardInfo) info).getUrl(),info.getName());
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,WebFragment.class.getSimpleName());
        }else if(info instanceof ArtistInfo){
            ArtistTabFragment f = ArtistTabFragment.newInstance(info);
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,ArtistTabFragment.class.getSimpleName());
        }else if(info instanceof AlbumInfo){
            AlbumTabFragment f = AlbumTabFragment.newInstance(info);
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,AlbumTabFragment.class.getSimpleName());
        }else if(info instanceof ListInfo){
            ListFragment f = ListFragment.newInstance(info.getId(), info.getDigest(),info.getName(), "sub_list");
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,ListFragment.class.getSimpleName());
        }else if(info instanceof MvInfo){
            Intent intent = new Intent();
            intent.putExtra("rid",((MvInfo) info).getRid());
            intent.setClass(MainActivity.getInstance(), MVActivity.class);
            MainActivity.getInstance().startActivity(intent);
        }else if(info instanceof MvplInfo){
            ListFragment f = ListFragment.newInstance(info.getId(), info.getDigest(),info.getName(), "sub_list");
            FragmentControl.getInstance().showWithPlayBarSubFrag(f,ListFragment.class.getSimpleName());
        }
    }
}
