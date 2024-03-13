import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataDriven {

    private static WebDriver driver;

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\AEC_PROJECTS\\web_testing\\src\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Whatsapp Bot Data-Driven Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        placeComponents(panel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel fileLabel = new JLabel("Enter CSV File Path:");
        fileLabel.setBounds(10, 20, 150, 25);
        panel.add(fileLabel);

        JTextField fileText = new JTextField(20);
        fileText.setBounds(180, 20, 165, 25);
        panel.add(fileText);

        JButton loginButton = new JButton("Start Data-Driven Testing");
        loginButton.setBounds(10, 60, 250, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = fileText.getText();
                performDataDrivenTesting(filePath);
            }
        });
        panel.add(loginButton);
    }

    private static void performDataDrivenTesting(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skipping header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int numberOfMessages = Integer.parseInt(data[0]);
                String messageContent = data[1];
                String recipientPhoneNumber = data[2];

                loginAndSendMessage(numberOfMessages, messageContent, recipientPhoneNumber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loginAndSendMessage(int numberOfMessages, String messageContent, String recipientPhoneNumber) {

        driver.get("https://web.whatsapp.com");

        driver.manage().timeouts().implicitlyWait(30, java.util.concurrent.TimeUnit.SECONDS);
        sendMessage(recipientPhoneNumber, messageContent, numberOfMessages);
    }

    private static void sendMessage(String phoneNumber, String message, int numberOfMessages) {
        driver.get("https://web.whatsapp.com/send?phone=" + phoneNumber + "&source=&data=#");

        boolean isInputBoxPresent = false;
        while (!isInputBoxPresent) {
            try {
                WebElement inputBox = driver
                        .findElement(By.xpath("//*[@id='main']/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div"));

                for (int i = 0; i < numberOfMessages; i++) {
                    inputBox.sendKeys(message);
                    inputBox.sendKeys("\n");
                    Thread.sleep(500);
                }

                isInputBoxPresent = true;
            } catch (org.openqa.selenium.NoSuchElementException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
