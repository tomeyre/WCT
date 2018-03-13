package eyresapps.com.utils;

/**
 * Created by thomaseyre on 08/03/2018.
 */

public class CrimeTypeString {

    public String setString(String name){
        name = Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
        if(name.contains("-")){
            name = name.replace("-"," ");
        }
        if(name.split(" ").length > 1) {
            String[] split = name.split(" ");
            name = "";
            for (String word : split) {
                if(word.length() > 0) {
                    word = Character.toString(word.charAt(0)).toUpperCase() + word.substring(1);
                    name = name + " " + word;
                }
            }
        }

        return name.trim();
    }
}
