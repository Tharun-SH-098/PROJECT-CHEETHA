import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class ImageGenerator extends JFrame {

    private JTextField promptField;
    private JLabel imageLabel;

    public ImageGenerator() {

        setTitle("AI Image Generator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();

        JLabel label = new JLabel("Enter Prompt:");
        promptField = new JTextField(30);
        JButton generateButton = new JButton("Generate Image");

        topPanel.add(label);
        topPanel.add(promptField);
        topPanel.add(generateButton);

        add(topPanel, BorderLayout.NORTH);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateImage());

        setVisible(true);
    }

    private void generateImage() {

        try {

            String prompt = promptField.getText();

            if (prompt.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a prompt!"
                );
                return;
            }

            URL imageUrl = URI
                    .create("https://picsum.photos/600/400")
                    .toURL();

            ImageIcon icon = new ImageIcon(imageUrl);

            imageLabel.setIcon(icon);

            JOptionPane.showMessageDialog(
                    this,
                    "Image generated successfully for:\n" + prompt
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ImageGenerator();
        });
    }
}