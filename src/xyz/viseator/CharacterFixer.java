package xyz.viseator;

import java.io.*;
import java.util.*;

/**
 * Created by Lily on 2016/12/6.
 * Email: yifengtang@unique.com
 */
public class CharacterFixer {

    private Map<Character, ArrayList<String>> wordsList;
    private File dic;

    public CharacterFixer(String dicPath){
        this.dic = new File(dicPath);
        initialWordsList();
    }

    public String fixChars(String output){
        Set<Character> keySet = wordsList.keySet();
        StringBuffer buffer = new StringBuffer(output);
        for(int i = 0; i < buffer.length(); i++){
            if(keySet.contains(buffer.charAt(i))) {
                if ((i == 0 && wordsList.get(buffer.charAt(i)).get(1).contains(Character.toString(buffer.charAt(i + 1)))) ||
                        (i == buffer.length() - 1 &&
                                wordsList.get(buffer.charAt(i)).get(1).contains(Character.toString(buffer.charAt(i - 1)))) ||
                        (i != buffer.length() - 1 && i != 0 &&
                                wordsList.get(buffer.charAt(i)).get(1).contains(Character.toString(buffer.charAt(i - 1)))
                                || wordsList.get(buffer.charAt(i)).get(1).contains(Character.toString(buffer.charAt(i + 1))))) {
                    buffer.setCharAt(i, wordsList.get(buffer.charAt(i)).get(0).charAt(0));
                }
            }
        }
        return buffer.toString();
    }

    private void initialWordsList(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dic), "UNICODE"));
            String s;
            while((s = bufferedReader.readLine()) != null){
                addWordList(s.charAt(0), s.charAt(2), s.substring(4));
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addWordList(Character aim, Character result, String conditions){
        if(wordsList == null)
            wordsList = new HashMap<>();
        ArrayList<String> lists = new ArrayList<>();
        lists.add(result.toString());
        lists.add(conditions);
        wordsList.put(aim, lists);
    }
}
