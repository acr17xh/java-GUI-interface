import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterfaceFrame extends CentreFrame implements ActionListener{
    JPanel panel = new JPanel();

    //创建中间件和组件
    //创建button
    private JButton input;
    private JButton main_info;
    private JButton check;
    private JButton validate;
    private JButton reset;
    
    //创建文本行
    private JTextField query_input;
    //创建显示框
    private JTextArea information;
    private JTextArea show;
    //创建FileChooser
    private JFileChooser chooser;


    //Used for saving the ArrayList<Wine>--wines in here and prepare be used
    public ArrayList<Wine> SavingWineSamples;

   


    //attributes getter and setter


    public JButton getInput() {
        return input;
    }
    public void setInput(JButton input) {
        this.input = input;
    }

    public JButton getMain_info() {
        return main_info;
    }
    public void setMain_info(JButton main_info) {
        this.main_info = main_info;
    }

    public JButton getCheck() {
        return check;
    }
    public void setCheck(JButton check) {
        this.check = check;
    }

    public JButton getValidate() {
        return validate;
    }
    public void setValidate(JButton validate) {
        this.validate = validate;
    }

    public JButton getReset() {
        return reset;
    }
    public void setReset(JButton reset) {
        this.reset = reset;
    }

    public JTextField getQuery_input() {
        return query_input;
    }
    public void setQuery_input(JTextField query_input) {
        this.query_input = query_input;
    }

    public JTextArea getInformation() {
        return information;
    }
    public void setInformation(JTextArea information) {
        this.information = information;
    }

    public JTextArea getShow() {
        return show;
    }
    public void setShow(JTextArea show) {
        this.show = show;
    }

    public ArrayList<Wine> getSavingWineSamples() {
        return SavingWineSamples;
    }
    public void setSavingWineSamples(ArrayList<Wine> savingWineSamples) {
        this.SavingWineSamples = savingWineSamples;
    }

    public JFileChooser getChooser() {
        return chooser;
    }
    public void setChooser(JFileChooser chooser) {
        this.chooser = chooser;
    }

    //构造方法InterfaceFrame()
    public InterfaceFrame(){
        //调用初始化函数
        initial();

        setTitle("Wine Samples Information Interface");
        Container container = getContentPane();

        //getContentPane()是用来获取JFrame内部ContentPane，因为只有它可以add组件(已加入变量中)进来
        container.add(panel);
        container.add(getQuery_input());

        container.add(getInput());
        container.add(getMain_info());
        container.add(new JScrollPane(getInformation()));
        container.add(new JScrollPane(getShow()));
        container.add(getCheck());
        container.add(getValidate());
        container.add(getReset());

        //给button和textfield设置监听器
        getInput().addActionListener(this);
        getMain_info().addActionListener(this);
        getCheck().addActionListener(this);
        getValidate().addActionListener(this);
        getReset().addActionListener(this);
    }

    //初始化函数 Initial function
    public void initial(){
        setLayout(new FlowLayout());

        //initial a Jfilechooser and give it to chooser
        chooser = new JFileChooser(".");
        //set multiply select in the alert interface become true
        chooser.setMultiSelectionEnabled(true);
        chooser.setSize(2,20);
        setChooser(chooser);

        //Creat input button and main_info button, then set to attribute
        input = new JButton("Choose csv file");
        setInput(input);
        main_info = new JButton("Main information");
        setMain_info(main_info);

        //Creat query_input field, then set to attribute
        add(new JLabel("Input your query"));
        query_input = new JTextField(40);
        setQuery_input(query_input);
        getQuery_input().setEditable(false);
        getQuery_input().setText("I can't being edit now, please input the target files first.");
        //Display some information in this area
        information = new JTextArea(4,50);
        //Set the default show area
        show = new JTextArea(11,50);
        setShow(show);
        getShow().setText(
                "Hi there! Please read using tutorial before manipulate."+"\r\n"+
                "1.You should click \'Choose csv file\' to get files first"+"\r\n"+
                "2.Then you should choose two csv files(red and white) in once time and click \'open\'."+"\r\n"+
                "3.You can click \'Main information\' button to get some key information, display in the middle textarea"+"\r\n"+
                "Thank you for cooperation O(∩_∩)O"
        );

        check = new JButton("query legality check");
        setCheck(check);
        validate = new JButton("Validate");
        setValidate(validate);
        reset = new JButton("Reset");
        setReset(reset);
    }

    //事件处理函数
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        //reset button
        if (source == reset){
            getQuery_input().setText("");
            getShow().setText(
                    "You already input the files, you can type searching query directly now, tutorial is below."
                            +"\r\n" +"1.Input searching query in the input field on the top."
                            +"\r\n" +"2.The syntax format is:\"select kind(red/white) where condition(qual > 5)\", you can use \"and\" to add more conditons"
                            +"\r\n" +"3.For example: select red where qual > 5 and ph < 5"
                            +"\r\n" +"4.Then click \"check legality\" button to check syntax is right or not."
                            +"\r\n" +"5.If no wrong, you can continue to click \"validate\" to get the result"
                            +"\r\n" +"\r\n"
                            +"\r\n" +"Remember1: You can click \"Main information\" button to get some key information."
                            +"\r\n" +"Remember2: If you click \"reset\", you don't need to choose csv file again, just search."

            );
            getQuery_input().setText("Warning: Don't input again, you can type query directly now.");
        }

        //input button
        else if (source == input){

            int result = getChooser().showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION){
                File[] arrayOfPath = chooser.getSelectedFiles();
                String path1 = arrayOfPath[0].getAbsolutePath();
                String path2 = arrayOfPath[1].getAbsolutePath();

                try {
                    BufferedReader br1 = new BufferedReader(new FileReader(path1));
                    BufferedReader br2 = new BufferedReader(new FileReader(path2));

                    ArrayList<Wine> wines = new ArrayList<>();
                    int id = 1;
                    String str1 = br1.readLine();

                    while (str1 != null){
                        str1 = br1.readLine();
                        if (str1 != null) {
                            //This if is used for prevent str1 become null when read to the end of the stream
                            String spl = str1;
                            String[] item;
                            item = spl.split(";");
                            Double[] item_double = new Double[item.length];
                            //Use split to divide every single line with ";"
                            //define item_double to store the Double type array

                            for (int i = 0; i < 12; ) {
                                double value = Double.valueOf(item[i]);
                                item_double[i] = value;
                                i++;
                            }
                            //Transform String type to Double type in item array, then store in a new array item_double
                            Wine wine = new Wine();
                            wine.setF_acid(item_double[0]);
                            wine.setV_acid(item_double[1]);
                            wine.setC_acid(item_double[2]);
                            wine.setR_sugar(item_double[3]);
                            wine.setChlorid(item_double[4]);
                            wine.setF_sulf(item_double[5]);
                            wine.setT_sulf(item_double[6]);
                            wine.setDens(item_double[7]);
                            wine.setpH(item_double[8]);
                            wine.setSulph(item_double[9]);
                            wine.setAlc(item_double[10]);
                            wine.setQual(item_double[11]);
                            wine.setID(id);
                            wine.setWineKind("red");

                            wines.add(wine);
                            id = id + 1;
                        }
                        else {
                            break;
                        }
                    }
                    String str2 = br2.readLine();
                    while (str2 != null){
                        str2 = br2.readLine();

                        if (str2 != null) {
                            String spl = str2;
                            String[] item;
                            item = spl.split(";");
                            Double[] item_double = new Double[item.length];

                            for (int i = 0; i < 12; ) {
                                double value = Double.valueOf(item[i]);
                                item_double[i] = value;
                                i++;
                            }

                            Wine wine = new Wine();
                            wine.setF_acid(item_double[0]);
                            wine.setV_acid(item_double[1]);
                            wine.setC_acid(item_double[2]);
                            wine.setR_sugar(item_double[3]);
                            wine.setChlorid(item_double[4]);
                            wine.setF_sulf(item_double[5]);
                            wine.setT_sulf(item_double[6]);
                            wine.setDens(item_double[7]);
                            wine.setpH(item_double[8]);
                            wine.setSulph(item_double[9]);
                            wine.setAlc(item_double[10]);
                            wine.setQual(item_double[11]);

                            wine.setID(id);
                            wine.setWineKind("white");

                            wines.add(wine);
                            id = id + 1;
                        }
                        else {
                            break;
                        }
                    }
                    setSavingWineSamples(wines);
                    getShow().setText("input success! All right, you can continue your searching work, congratulations!"
                            +"\r\n" +"1.Input searching query in the input field on the top."
                            +"\r\n" +"2.The syntax format is:\"select kind(red/white) where condition(qual > 5)\", you can use \"and\" to add more conditons"
                            +"\r\n" +"3.For example: select red where qual > 5 and ph < 5"
                            +"\r\n" +"4.Then click \"check legality\" button to check syntax is right or not."
                            +"\r\n" +"5.If no wrong, you can continue to click \"validate\" to get the result"
                            +"\r\n" +"\r\n"
                            +"\r\n" +"Remember1: You can click \"Main information\" button to get some key information."
                            +"\r\n" +"Remember2: If you click \"reset\", you don't need to choose csv file again, just search."
                    );
                    getQuery_input().setEditable(true);
                    getQuery_input().setText("Now you can edit me! Remember delete this word first!");
                }
                catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                    getShow().setText("File not found, please try again");
                }
                catch (IOException i1){
                    i1.printStackTrace();
                }
            }
        }
        //Main information button
        else if (source == main_info){

            //Display some information in this area
            int red = 0; int white = 0;int tot = 0;
            Iterator<Wine> iterator_RedOrWhite = getSavingWineSamples().iterator();
            while (iterator_RedOrWhite.hasNext()){
                Wine wine_iter = iterator_RedOrWhite.next();
                if (wine_iter.getWineKind().equals("red")){
                    red = red + 1;
                }
                else { white = white + 1; }

                tot = red + white;
            }
            getInformation().append("1. Number of wine samples : " + tot + "\r\n");
            getInformation().append("2. Number of red wine samples : " + red + " out of " + tot + "\r\n");
            getInformation().append("3. Number of white wine samples : " + white + " out of " + tot + "\r\n");

            double top = 0;
            Iterator<Wine> iterator_best_quality = getSavingWineSamples().iterator();
            while (iterator_best_quality.hasNext()){
                Wine wine_iter = iterator_best_quality.next();
                if (wine_iter.getQual() > top){
                    top = wine_iter.getQual();
                }
                else {}
            }
            getInformation().append("4. The best quality in wine samples is : quality " + top + "\r\n");

            double bot = 10;
            Iterator<Wine> iterator_worst_quality = getSavingWineSamples().iterator();
            while (iterator_worst_quality.hasNext()){
                Wine wine_iter = iterator_worst_quality.next();
                if (wine_iter.getQual() < bot){
                    bot = wine_iter.getQual();
                }
            }
            getInformation().append("5. The worst quality in wine samples is : quality " + bot + "\r\n");
        }

        //query check button
        else if (source == check){
            getShow().setText("");

            //获取textField值
            String intStr = getQuery_input().getText();

            //The code below is used to detect the problem of input
            //****************************************************************
            String intstr = intStr.toLowerCase();

            Pattern pattern = Pattern.compile("[0-9]$");
            Matcher matcher = pattern.matcher(intstr);
            Boolean HasNumber = false;
            if (matcher.find()){
                HasNumber = true;
            }
            //RegEX used to detect number data

            if (intstr.contains("select") && intstr.contains("where") && HasNumber){
                getShow().append("This searching query is great and it could be used" +"\r\n");
                getShow().append("Everything is ok, you can click validate button to get result." +"\r\n");
            }
            else if (!intstr.contains("select") && intstr.contains("where") && HasNumber){
                getShow().append("Error: This searching query is short of keyword \'select\'!" +"\r\n");
                getShow().append("Please fix it and try again." +"\r\n");
            }
            else if (intstr.contains("select") && !intstr.contains("where") && HasNumber){
                getShow().append("Error: This searching query is short of keyword \'where\'!" +"\r\n");
                getShow().append("Please fix it and try again." +"\r\n");
            }
            else if (intstr.contains("select") && intstr.contains("where") && !HasNumber){
                getShow().append("Error: This searching query is short of an available number !" +"\r\n");
                getShow().append("Please fix it and try again." +"\r\n");
            }
            else {
                getShow().append("Error: This searching query is not matches right format !" +"\r\n");
                getShow().append("Please fix it and try again." +"\r\n");
            }
        }

        else if (source == validate){
            getShow().setText("");

            //获取textField值
            String str = getQuery_input().getText();

            String intStr = str.toLowerCase();

            Query qry = new Query();
            //Pick up the scope and conditions via two function below
            qry.setScope(qry.ConvertScope(intStr));
            qry.setConditions(qry.ConvertConditions(intStr));

            //Set new temp used for save final result of searching
            ArrayList<Wine> temp = new ArrayList<>();
            //Set new RightScopeWine used for save wine samples which matches scope
            ArrayList<Wine> RightScopeWine = new ArrayList<>();
            for (int i=0;i<this.getSavingWineSamples().size();){
                if (qry.getScope().contains(getSavingWineSamples().get(i).getWineKind())){
                    RightScopeWine.add(getSavingWineSamples().get(i));
                }
                i++;
            }

            for (int j=0;j<RightScopeWine.size();){
                //This loop is used for searching wine sample with specific conditions

                ArrayList<ArrayList<Wine>> SamplesOfSamples = new ArrayList<>();
                //This ArrayList used for save SamplesMeetCondition;
                //There will be much number of SamplesMeetCondition, depends on the number of conditions
                //Then it will find wine samples which both exist in each single SamplesMeetCondition

                for (int n=0;n<qry.getConditions().size();){
                    Pattern pattern = Pattern.compile("\\d+\\.\\d+");
                    Matcher matcher = pattern.matcher(qry.getConditions().get(n));
                    String num_String ="";
                    if (matcher.find()) {
                        num_String = matcher.group(0);
                    }
                    //Use RegEX detect number is double number or not, then save it in num
                    else {
                        Pattern pattern1 = Pattern.compile("\\d+");
                        Matcher matcher1 = pattern1.matcher(qry.getConditions().get(n));
                        if (matcher1.find()){
                            num_String = matcher1.group(0);
                        }
                    }
                    //if not, detect number is integer or not, then save it in num
                    double num;
                    num = Double.valueOf(num_String);
                    //Transfer String to double

                    ArrayList<Wine> SamplesMeetCondition = new ArrayList<>();
                    //This ArrayList is used for save the samples meet the conditions from query
                    //Attention: need first search <=, then<; and first >=, then >.
                    //because first meeting < condition will not judge whether meeting <= or not.
                    if (qry.getConditions().get(n).contains("qual")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getQual() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getQual() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getQual() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getQual() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getQual() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getQual() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("alc")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getAlc() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getAlc() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getAlc() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getAlc() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getAlc() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getAlc() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("sulph")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getSulph() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getSulph() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getSulph() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getSulph() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getSulph() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getSulph() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("ph")){
                        //pay attention on the uppercase and lowercase of pH and ph
                        //This ph is after convert original to lowercase
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getpH() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getpH() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getpH() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getpH() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getpH() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getpH() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("dens")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getDens() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getDens() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getDens() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getDens() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getDens() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getDens() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("t_sulf")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getT_sulf() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getT_sulf() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getT_sulf() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getT_sulf() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getT_sulf() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getT_sulf() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("f_sulf")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getF_sulf() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getF_sulf() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getF_sulf() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getF_sulf() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getF_sulf() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getF_sulf() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("chlorid")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getChlorid() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getChlorid() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getChlorid() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getChlorid() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getChlorid() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getChlorid() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("r_sugar")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getR_sugar() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getR_sugar() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getR_sugar() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getR_sugar() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getR_sugar() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getR_sugar() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("c_acid")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getC_acid() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getC_acid() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getC_acid() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getC_acid() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getC_acid() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getC_acid() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("v_acid")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getV_acid() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getV_acid() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getV_acid() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getV_acid() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getV_acid() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getV_acid() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    else if (qry.getConditions().get(n).contains("f_acid")){
                        if (qry.getConditions().get(n).contains(">=")){
                            if (RightScopeWine.get(j).getF_acid() >= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains(">")){
                            if (RightScopeWine.get(j).getF_acid() > num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("!=")){
                            if (RightScopeWine.get(j).getF_acid() != num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("=")){
                            if (RightScopeWine.get(j).getF_acid() == num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<=")){
                            if (RightScopeWine.get(j).getF_acid() <= num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                        else if (qry.getConditions().get(n).contains("<")){
                            if (RightScopeWine.get(j).getF_acid() < num){
                                SamplesMeetCondition.add(RightScopeWine.get(j));
                            }
                        }
                    }

                    if (SamplesMeetCondition.size() != 0){
                        SamplesOfSamples.add(SamplesMeetCondition);
                    }
                    //If SamplesMeetCondition have member,save it
                    n++;
                }

                if (SamplesOfSamples.size() == qry.getConditions().size()){
                    temp.add(RightScopeWine.get(j));
                }
                //If SamplesOfSamples's size equals conditions number of query, save this sample
                j++;
            }

            //Active line changing service
            getShow().setLineWrap(true);
            getShow().setWrapStyleWord(true);
            //Area deny edit
            getShow().setEditable(false);

            //edit display area
            getShow().append("Query: "+intStr +"\r\n");
            getShow().append("Scope: "+qry.getScope()+"\r\n");
            getShow().append("--------------------------------------"+"\r\n");
            getShow().append("\n" + "Searching Result: "+"\r\n");
            if (temp.size()==0){
                getShow().append("No wine sample meeting this conditions"+"\r\n");
            }
            else {
                for (int m = 0; m < temp.size(); ) {
                    getShow().append(temp.get(m).getID() + " " + temp.get(m).getWineKind()
                            + " [Qual: " + temp.get(m).getQual() + "]"
                            + " [Alc: " + temp.get(m).getAlc() + "]"
                            + " [Sulph: " + temp.get(m).getSulph() + "]"
                            + " [pH: " + temp.get(m).getpH() + "]"
                            + " [Dens: " + temp.get(m).getDens() + "]"
                            + " [T_sulf: " + temp.get(m).getT_sulf() + "]"
                            + " [F_sulf: " + temp.get(m).getF_sulf() + "]"
                            + " [Chlorid: " + temp.get(m).getChlorid() + "]"
                            + " [R_sugar: " + temp.get(m).getR_sugar() + "]"
                            + " [C_acid: " + temp.get(m).getC_acid() + "]"
                            + " [V_acid: " + temp.get(m).getV_acid() + "]"
                            + " [F_acid: " + temp.get(m).getF_acid() + "]" +"\r\n");
                    m++;
                }
            }

            getShow().append("\n"+"Searching ended");


        }

    }



}
