import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class whatsapp1 {

    private static WebDriver driver;

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver",
                "C:\\AEC_PROJECTS\\web_testing\\src\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Whatsapp Bot Automation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        placeComponents(panel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel messagesLabel = new JLabel("Number of Messages:");
        messagesLabel.setBounds(10, 20, 150, 25);
        panel.add(messagesLabel);

        JTextField messagesText = new JTextField(20);
        messagesText.setBounds(180, 20, 165, 25);
        panel.add(messagesText);

        JLabel contentLabel = new JLabel("Message Content:");
        contentLabel.setBounds(10, 50, 150, 25);
        panel.add(contentLabel);

        JTextField contentText = new JTextField(20);
        contentText.setBounds(180, 50, 165, 25);
        panel.add(contentText);

        JLabel recipientLabel = new JLabel("Recipient Phone Number:");
        recipientLabel.setBounds(10, 80, 180, 25);
        panel.add(recipientLabel);

        JTextField recipientText = new JTextField(20);
        recipientText.setBounds(180, 80, 165, 25);
        panel.add(recipientText);

        JButton loginButton = new JButton("Login and Send Messages");
        loginButton.setBounds(10, 120, 250, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberOfMessages = Integer.parseInt(messagesText.getText());
                String messageContent = contentText.getText();
                String recipientPhoneNumber = recipientText.getText();

                // Call your function to perform the automation after login
                loginAndSendMessage(numberOfMessages, messageContent, recipientPhoneNumber);
            }
        });
        panel.add(loginButton);
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
            } catch (org.openqa.selenium.NoSuchElementException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}