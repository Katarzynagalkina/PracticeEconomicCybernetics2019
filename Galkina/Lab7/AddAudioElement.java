import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAudioElement extends JFrame {
    private AudioCatalog catalog;
    private JLabel model = new JLabel("Тип(CD,виниловый диск,аудио-плеер,акустика)");
    private JLabel vendorCode = new JLabel("Артикул");
    private JLabel year = new JLabel("Год выпуска");
    private JLabel name = new JLabel("Название");
    private JTextField textModel = new JTextField();
    private JTextField textVendorCode = new JTextField();
    private JTextField textYear = new JTextField();
    private JTextField textName = new JTextField();
    private JButton okButton = new JButton("OK");

    public AddAudioElement(AudioCatalog catalog) throws HeadlessException, ClassNotFoundException {
        Container c = getContentPane();
        this.catalog = catalog;
        this.setTitle("Добавление элемента");
        this.setBounds(400, 200, 400, 150);
        this.setSize(350, 250);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(model);
        panel.add(textModel);
        panel.add(vendorCode);
        panel.add(textVendorCode);
        panel.add(name);
        panel.add(textName);
        panel.add(year);
        panel.add(textYear);
        panel.add(okButton);
        c.add(panel);


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textModel.getText().equals("") || textVendorCode.getText().equals("") || textYear.getText().equals("") || textName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Нужно заполнить все поля!", "Внимание", JOptionPane.WARNING_MESSAGE);
                } else {
                    if ((textModel.getText().equals("виниловый диск") || textName.getText().equals("аудио-плеер") || textModel.getText().equals("CD") || textModel.getText().equals("акустика"))) {
                        try {
                            Laptop lp = new Laptop(Integer.parseInt(textYear.getText()), Integer.parseInt(textVendorCode.getText()), textModel.getText(), textName.getText());
                            AudioCatalog.addResult = lp;
                            catalog.addNewItem();
                        } catch (NumberFormatException x) {
                            JOptionPane.showMessageDialog(null, "Артикул и год - числовые значения!", "Внимание", JOptionPane.WARNING_MESSAGE);
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Неверные данные в поле тип!", "Внимание", JOptionPane.WARNING_MESSAGE);
                }
                dispose();
            }
        });
    }

}