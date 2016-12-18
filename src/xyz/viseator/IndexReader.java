package xyz.viseator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lily on 2016/12/13.
 * Email: yifengtang@unique.com
 */
public class IndexReader {

    private Map<String, RowInfo> indexMap;
    private File index;

    public IndexReader(String path){
        indexMap = new HashMap<>();
        index = new File(path);
        initialIndexMap();
    }

    public RowInfo getRowInfo(String name){
        return indexMap.get(name);
    }

    public Set<String> getName(){
        return indexMap.keySet();
    }

    private void initialIndexMap(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(index), "UNICODE"));
            String s;
            while((s = bufferedReader.readLine()) != null){
                int count = 0;
                StringBuffer nameBuf;
                Character pos;
                Character type;
                nameBuf = new StringBuffer();
                for(; s.charAt(count) != ' '; count++){
                    nameBuf.append(s.charAt(count));
                }
                pos = s.charAt(count + 1);
                type = s.charAt(count + 3);
                indexMap.put(nameBuf.toString(), new RowInfo((type == 'N') ? RowInfo.IS_NUM : RowInfo.IS_STRING,
                        (pos - '0'), nameBuf.toString()));
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
