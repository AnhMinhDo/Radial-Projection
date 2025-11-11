package schneiderlab.tools.radialprojection;

import org.scijava.Context;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import javax.swing.*;

import schneiderlab.tools.radialprojection.controllers.controllers.MainController;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.Radical_Projection_Tool;

@Plugin(type = Command.class, menuPath = "Plugins > Radial Projection")
public class Main implements Command {

    @Parameter
    private Context context; // get context from current Fiji session

    @Override
    public void run() {
        SwingUtilities.invokeLater(()-> launchUI());
    }

    public void launchUI(){
            CurrentOSSystem currentOSSystem = CurrentOSSystem.getCurrent();
            try {
                if (currentOSSystem==CurrentOSSystem.MAC) {
                    com.formdev.flatlaf.themes.FlatMacLightLaf.setup();
                } else {
                    com.formdev.flatlaf.FlatLightLaf.setup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JFrame frame = new JFrame("Radical Projection Tool");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            RadialProjectionModel radialProjectionModel = new RadialProjectionModel();
            Radical_Projection_Tool form = new Radical_Projection_Tool(context, frame);
            MainController mainController = new MainController(form, context, currentOSSystem);
            frame.setContentPane(form.getContentPane());
            frame.pack();
            frame.setVisible(true);
    }
}
