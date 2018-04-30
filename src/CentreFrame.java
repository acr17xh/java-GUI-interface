import javax.swing.*;
import java.awt.*;

public class CentreFrame extends JFrame {

    //构造方法CentreFrame()
    public CentreFrame() {
        setTitle("A centred frame");
        // use a toolkit to get system dependent info
        Toolkit tk = Toolkit.getDefaultToolkit();
        // Dimension encapsulates width and height
        Dimension dim = tk.getScreenSize();
        // same h:w ratio as screen and 1/4 the area
        setSize(dim.width/2,dim.height/2);
        // centre window on screen
        setLocation(new Point(dim.width/4,dim.height/4));
    }
}
