import javax.swing.*;

public class PWB extends InterfaceFrame {

    public static void main(String[] args){
        //GUI module
        //This is main function used to build the user interface
        InterfaceFrame frm = new InterfaceFrame();
        //next is used to save wines arraylist in to InterfaceFrame object to let their button listener use.
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }
}