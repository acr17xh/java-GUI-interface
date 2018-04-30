import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {

    private ArrayList<String> queries_array;

    private String scope;
    private ArrayList<String> conditions;


    public Query() { }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public ArrayList<String> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<String> conditions) {
        this.conditions = conditions;
    }

    public ArrayList<String> getQueries_array() {
        return queries_array;
    }

    public void setQueries_array(ArrayList<String> queries_array) {
        this.queries_array = queries_array;
    }


    //The method used for fixing the searching queries problem
    public ArrayList<String> ConvertFormat(){
        ArrayList<String> arrayList = this.getQueries_array();

        for (int i=0;i<arrayList.size();){
            int j = i+1;

            Pattern pattern1 = Pattern.compile("[0-9]$");
            Matcher matcher1 = pattern1.matcher(arrayList.get(i));
            Boolean HasNumber = false;
            if (matcher1.find()){
                HasNumber = true;
            }
            //RegEX used to detect number data

            Boolean isSelect = false;
            if (arrayList.size() != j) {
                Pattern pattern2 = Pattern.compile("ele|sele|elec|elet");
                Matcher matcher2 = pattern2.matcher(arrayList.get(j));
                if (matcher2.find()) {
                    isSelect = true;
                }
            }
            //RegEX used to detect select keyword


            if (arrayList.get(i).contains("select") && arrayList.get(i).contains("where") && arrayList.size() != j){
                //Currently line have "select" and "where" and not the last line

                if (arrayList.get(j).contains("select") && HasNumber){
                    //Right format, do not change anything
                }
                else if (arrayList.get(j).contains("select") && !HasNumber){
                    //Short of a available number data
                    System.out.println("*** Warning: The searching query \"" +arrayList.get(j)+ "\" has short of available **NUMBER** data" +
                            " and it has been removed, please check and correct it");
                    arrayList.remove(j);
                }
                else if (isSelect && !arrayList.get(j).contains("select")){
                    System.out.println("*** Warning: The searching query \"" +arrayList.get(j)+ "\" has wrong in **SELECT** keyword and " +
                            "it has been removed, please check and correct it");
                    arrayList.remove(j);
                }
                else if (!arrayList.get(j).contains("select") && !arrayList.get(j).equals(null)){
                    //Next line doesn't contains select and not the last line
                    String a = arrayList.get(i) + " " + arrayList.get(j);
                    arrayList.set(i,a);
                    arrayList.remove(j);
                    //Adding currently line and next line together and delete next line
                    this.setQueries_array(arrayList);
                    //Set currently arraylist as newest Queries_array
                    this.ConvertFormat();
                    //Recursive it, re-execute it
                    break;
                    //break after Recursive to stop execute next programme after it
                }
                else{}
            }

            else if (arrayList.get(i).contains("select") && !arrayList.get(i).contains("where") && arrayList.size() != j){
                //Currently line have "select" but not "where" and not the last line

                String c = arrayList.get(i) + " " + arrayList.get(j);
                arrayList.set(i,c);
                arrayList.remove(j);
                //Adding currently line and next line together and delete next line
                this.setQueries_array(arrayList);
                //Set currently arraylist as newest Queries_array
                this.ConvertFormat();
                //Recursive it, re-execute it
                break;
                //break after Recursive to stop execute next programme after it
            }

            else {
                if (arrayList.size() == j){
                    //Judge the last line searching query.
                    if (arrayList.get(i).contains("select") && arrayList.get(i).contains("where") && HasNumber){
                        //Right format
                    }
                    else if (arrayList.get(i).contains("select") && arrayList.get(i).contains("where") && !HasNumber){
                        System.out.println("*** Warning: The searching query \"" + arrayList.get(i) + "\" is short of **NUMBER** data and " +
                                "it has been removed, please check and correct it");
                        arrayList.remove(i);
                        break;
                    }
                    else if (arrayList.get(i).contains("select") && !arrayList.get(i).contains("where") && HasNumber){
                        System.out.println("*** Warning: The searching query \"" + arrayList.get(i) + "\" is short of **WHERE** keyword and " +
                                "it has been removed, please check and correct it");
                        arrayList.remove(i);
                        break;
                    }
                    else if (!arrayList.get(i).contains("select") && arrayList.get(i).contains("where") && HasNumber){
                        System.out.println("*** Warning: The searching query \"" + arrayList.get(i) + "\" is short of **SELECT** keyword and " +
                                "it has been removed, please check and correct it");
                        arrayList.remove(i);
                        break;
                    }
                }
                else {
                    //Solve the other short elements problem
                    System.out.println("*** Warning: The searching query \"" + arrayList.get(i) + "\" is short of some elements and " +
                            "it has been removed, please check and correct it");
                    arrayList.remove(i);
                    break;
                }
            }
            i++;
        }
        return arrayList;
    }

    //Used for recognize the scope of wine samples
    public String ConvertScope(String originalText){

        if (originalText.toLowerCase().contains("red") && originalText.toLowerCase().contains("white")){
            setScope("red or white");
        }

        else if (originalText.toLowerCase().contains("red") && !originalText.toLowerCase().contains("white")){
            setScope("red");
        }

        else if (originalText.toLowerCase().contains("white") && !originalText.toLowerCase().contains("red")){
            setScope("white");
        }

        return scope;
    }

    //Used for take conditions in a searching qurey
    public ArrayList<String> ConvertConditions(String Original){
        ArrayList<String> conditions = new ArrayList<>();

        if (!Original.contains("and")) {
            //If only have one condition
            String StartCon = Original.substring(Original.indexOf("where") + 6);
            //the reason why plus 6 is fillet the where and the following space
            conditions.add(StartCon);
        }
        //Save the first condition in ArrayList

        else if (Original.contains("and")) {
            //If have more than one conditions
            String[] ands = Original.split("and");
            String StartCon = ands[0].substring(ands[0].indexOf("where") + 6);
            conditions.add(StartCon);
            //split via "and" then adding first condition in ArrayList

            int NumOfCon = ands.length;
            for (int i = 1; i < NumOfCon; ) {
                String condition = ands[i].trim();
                condition = condition.trim();
                conditions.add(condition);
                i++;
            }
            //Eliminate spaces on both side of the String
            //Then adding conditions in ArrayList
        }
        return conditions;
    }

}
