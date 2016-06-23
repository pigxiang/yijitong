package org.hehe7xiao.yijitong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianXX on 2016/4/4.
 */
public class Data {
    static final public List<Geo> geos = new ArrayList<Geo>() {{
//        add(new Geo("西山赢府", "39.962536,116.229567", "中国北京市海淀区杏石口路99号"));
//        add(new Geo("集团", "39.917384,116.364749", "中国北京市西城区金融大街31号"));
//        add(new Geo("东四", "39.932435,116.439415", "中国北京市东城区朝阳门北大街21号"));
        add(new Geo("西山赢府", 39.962536, 116.229567, "中国北京市海淀区杏石口路99号"));
        add(new Geo("集团", 39.917384, 116.364749, "中国北京市西城区金融大街31号"));
        add(new Geo("东四", 39.932435, 116.439415, "中国北京市东城区朝阳门北大街21号"));
    }};

}
