package com.chenhao.musicplayer.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.chenhao.musicplayer.bean.ArtistSection;
import com.chenhao.musicplayer.bean.BannerSection;
import com.chenhao.musicplayer.bean.BillboardSection;
import com.chenhao.musicplayer.bean.ClassifySection;
import com.chenhao.musicplayer.bean.CommentInfo;
import com.chenhao.musicplayer.bean.CommentSection;
import com.chenhao.musicplayer.bean.ItemViewType;
import com.chenhao.musicplayer.bean.KlistSection;
import com.chenhao.musicplayer.bean.KsquareSection;
import com.chenhao.musicplayer.bean.ListSection;
import com.chenhao.musicplayer.bean.MusicSection;
import com.chenhao.musicplayer.bean.MvSection;
import com.chenhao.musicplayer.bean.MvSquareSection;
import com.chenhao.musicplayer.bean.OnlineInfo;
import com.chenhao.musicplayer.bean.RootInfo;
import com.chenhao.musicplayer.bean.Section;
import com.chenhao.musicplayer.bean.SquareSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 2016/11/16.
 */

public class MultiAdapter extends RecyclerAdapterFactory<RootInfo> {

    public MultiAdapter(Context context, RootInfo rootInfo, Handler handler) {
        super(context, rootInfo, handler);
    }

    public void setData(RootInfo rootInfo, ArrayList<OnlineInfo> infos) {
        loadMore(rootInfo, infos);
    }

    @Override
    protected void buildAdapters(RootInfo rootInfo) {
        List<Section> sections = rootInfo.getSections();
        for (Section section : sections) {
            if (section instanceof BannerSection) {
                addAdapter(new BannerAdapter(getContext(), (BannerSection) section, section.getItemViewType(), getHandler()));
            } else if (section instanceof SquareSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                ArrayList<String> labelInfos = new ArrayList<>();
                labelInfos.add(section.getLabel());
                if (section.getMdigest() > 0) {
                    labelInfos.add("更多");
                } else if ("今日精华".equals(section.getLabel()) || "热门电台".equals(section.getLabel())) {
                    labelInfos.add("更多");
                }
                if (!TextUtils.isEmpty(section.getLabel())) {
                    addAdapter(new LabelHeadAdapter(getContext(), labelInfos, ItemViewType.TYPE_HEAD.ordinal(), getHandler()));
                }
                ArrayList<OnlineInfo> infos = null;
                for (int i = 0; i < onlineInfos.size(); i++) {
                    if (i % 3 == 0) {
                        infos = new ArrayList<>();
                        ;
                    }
                    if (infos != null) {
                        infos.add(onlineInfos.get(i));
                    }
                    if (i % 3 == 2) {
                        addAdapter(new SquareAdapter(getContext(), infos, section.getItemViewType(), getHandler()));
                    }
                }
            } else if (section instanceof MvSquareSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                ArrayList<String> labelInfos = new ArrayList<>();
                labelInfos.add(section.getLabel());
                if (section.getMdigest() > 0) {
                    labelInfos.add("更多");
                } else if ("最潮视频".equals(section.getLabel())) {
                    labelInfos.add("更多");
                }
                addAdapter(new LabelHeadAdapter(getContext(), labelInfos, ItemViewType.TYPE_HEAD.ordinal(), getHandler()));
                ArrayList<OnlineInfo> infos = null;
                for (int i = 0; i < onlineInfos.size(); i++) {
                    if (i % 2 == 0) {
                        infos = new ArrayList<>();
                    }
                    if (infos != null) {
                        infos.add(onlineInfos.get(i));
                    }
                    if (i % 2 == 1) {
                        addAdapter(new MvSquareAdapter(getContext(), infos, section.getItemViewType(), getHandler()));
                    }
                }
            } else if (section instanceof KsquareSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                ArrayList<String> labelInfos = new ArrayList<>();
                labelInfos.add(section.getLabel());
                labelInfos.add("去翻唱");
                addAdapter(new LabelHeadAdapter(getContext(), labelInfos, ItemViewType.TYPE_HEAD.ordinal(), getHandler()));
                addAdapter(new KSquareAdapter(getContext(), onlineInfos, section.getItemViewType(), getHandler()));
            } else if (section instanceof KlistSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                ArrayList<String> labelInfos = new ArrayList<>();
                labelInfos.add(section.getLabel());
                labelInfos.add("去k歌");
                addAdapter(new LabelHeadAdapter(getContext(), labelInfos, ItemViewType.TYPE_HEAD.ordinal(), getHandler()));
                for (OnlineInfo infos : onlineInfos) {
                    addAdapter(new KListItemAdapter(getContext(), infos, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof ListSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                ArrayList<String> labelInfos = new ArrayList<>();
                labelInfos.add(section.getLabel());
                if ("哔哔社区".equals(section.getLabel())) {
                    labelInfos.add("更多");
                } else if (section.getMdigest() > 0) {
                    labelInfos.add("更多");
                }
                if (!TextUtils.isEmpty(section.getLabel())) {
                    addAdapter(new LabelHeadAdapter(getContext(), labelInfos, ItemViewType.TYPE_HEAD.ordinal(), getHandler()));
                }
                for (OnlineInfo infos : onlineInfos) {
                    addAdapter(new ListItemAdapter(getContext(), infos, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof BillboardSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo onineinfo : onlineInfos) {
                    addAdapter(new BillboardAdapter(getContext(), onineinfo, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof ArtistSection) {
                addAdapter(new ArtistHeadAdapter(getContext(), null, ItemViewType.TYPE_ARTIST_HEAD.ordinal(), getHandler()));
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                ArrayList<String> labelInfos = new ArrayList<>();
                labelInfos.add("热门歌手");
                addAdapter(new LabelHeadAdapter(getContext(), labelInfos, ItemViewType.TYPE_HEAD.ordinal(), getHandler()));
                for (OnlineInfo onlineInfo : onlineInfos) {
                    addAdapter(new ArtistAdapter(getContext(), onlineInfo, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof ClassifySection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                if (onlineInfos.size() > 0) {
                    addAdapter(new ClassifyMainAdapter(getContext(), (ClassifySection) section, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof MusicSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo info : onlineInfos) {
                    addAdapter(new MusicAdapter(getContext(), onlineInfos, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof CommentSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo onlineInfo : onlineInfos) {
                    addAdapter(new CommentAdapter(getContext(), (CommentInfo) onlineInfo, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof MvSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo onlineInfo : onlineInfos) {
                    addAdapter(new MvAdapter(getContext(), onlineInfo, section.getItemViewType(), getHandler()));
                }
            }
        }
    }

    private void loadMore(RootInfo rootInfo, ArrayList<OnlineInfo> infos) {
        List<Section> sections = rootInfo.getSections();
        for (Section section : sections) {
            if (section instanceof ArtistSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo onlineInfo : onlineInfos) {
                    addAdapter(new ArtistAdapter(getContext(), onlineInfo, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof MusicSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo info : onlineInfos) {
                    addAdapter(new MusicAdapter(getContext(), infos, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof ListSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo info : onlineInfos) {
                    addAdapter(new ListItemAdapter(getContext(), info, section.getItemViewType(), getHandler()));
                }
            } else if (section instanceof MvSection) {
                ArrayList<OnlineInfo> onlineInfos = section.getOnlineInfos();
                for (OnlineInfo onlineInfo : onlineInfos) {
                    addAdapter(new MvAdapter(getContext(), onlineInfo, section.getItemViewType(), getHandler()));
                }
            }
        }
    }

}
