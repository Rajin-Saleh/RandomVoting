import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;

public class VotingSystem extends JFrame {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton signInButton;

    // A map to store candidates' names and their votes
    private static HashMap<String, Integer> voteCounts = new HashMap<>();
    // A set to track users who have already voted
    private static HashSet<String> votedUsers = new HashSet<>();

    public VotingSystem() {
        // Initialize vote counts only once
        if (voteCounts.isEmpty()) {
            voteCounts.put("Urmi", 0);
            voteCounts.put("Ifram", 0);
            voteCounts.put("Diya", 0);
        }

        // Set up the JFrame
        setTitle("Login Page");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        signInButton = new JButton("Sign In");

        // Set layout and add components
        JPanel panel = new JPanel();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signInButton);

        // Add action listener to the button
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("BasedSir") && password.equals("987654321")) {
                    showVotingPage(username); // Allow "BasedSir" to access voting page
                } else if (votedUsers.contains(username)) {
                    JOptionPane.showMessageDialog(null, "You have already voted.");
                } else {
                    showVotingPage(username); // Allow other users to access voting page
                }
            }
        });

        // Add the panel to the frame
        add(panel);
        setVisible(true);
    }

    // Voting Page
    private void showVotingPage(String username) {
        JFrame votingFrame = new JFrame("Voting Page");
        votingFrame.setSize(400, 300);
        votingFrame.setLocationRelativeTo(null);

        // Create components
        JLabel welcomeLabel = new JLabel("Welcome to CR Voting Campaign");
        JLabel voteLabel = new JLabel("Select your candidate:");
        JRadioButton urmi = new JRadioButton("Urmi");
        JRadioButton ifram = new JRadioButton("Ifram");
        JRadioButton diya = new JRadioButton("Diya");

        ButtonGroup candidates = new ButtonGroup();
        candidates.add(urmi);
        candidates.add(ifram);
        candidates.add(diya);

        JButton voteButton = new JButton("Submit Vote");
        JButton endButton = new JButton("End Voting");

        // Set layout and add components
        JPanel panel = new JPanel();
        panel.add(welcomeLabel);
        panel.add(voteLabel);
        panel.add(urmi);
        panel.add(ifram);
        panel.add(diya);
        panel.add(voteButton);

        if (username.equals("BasedSir")) {
            panel.add(endButton);
        }

        // Action listener for voting
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (urmi.isSelected()) {
                    voteCounts.put("Urmi", voteCounts.get("Urmi") + 1);
                } else if (ifram.isSelected()) {
                    voteCounts.put("Ifram", voteCounts.get("Ifram") + 1);
                } else if (diya.isSelected()) {
                    voteCounts.put("Diya", voteCounts.get("Diya") + 1);
                }

                if (!username.equals("BasedSir")) {
                    // Add the user to the set of voters
                    votedUsers.add(username);
                }

                showSuccessMessage();
                votingFrame.dispose(); // Close voting page after voting
                new VotingSystem(); // Return to login page
            }
        });

        // Action listener for ending the voting process
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                votingFrame.dispose(); // Close voting page
                showEndPage(); // Show the new End Voting page
            }
        });

        votingFrame.add(panel);
        votingFrame.setVisible(true);
    }

    // Success Message Pop-up
    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Voting is Successful. Thank you!");
    }

    // End Voting Page
    private void showEndPage() {
        JFrame endFrame = new JFrame("Voting Ended");
        endFrame.setSize(300, 200);
        endFrame.setLocationRelativeTo(null);

        JLabel messageLabel = new JLabel("Voting has ended. Check the result:");
        JButton resultButton = new JButton("Result");

        JPanel panel = new JPanel();
        panel.add(messageLabel);
        panel.add(resultButton);

        // Action listener for result button
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if user is authorized to see the results
                String username = JOptionPane.showInputDialog(endFrame, "Enter your username:");
                String password = JOptionPane.showInputDialog(endFrame, "Enter your password:");
                if ("BasedSir".equals(username) && "987654321".equals(password)) {
                    showResultPage(); // Show the result page if credentials are correct
                } else {
                    JOptionPane.showMessageDialog(endFrame, "Access Denied!");
                }
            }
        });

        endFrame.add(panel);
        endFrame.setVisible(true);
    }

    // Result Page
    private void showResultPage() {
        JFrame resultFrame = new JFrame("Voting Results");
        resultFrame.setSize(300, 300);
        resultFrame.setLocationRelativeTo(null);

        // Determine the candidate with the most votes
        String topCandidate = "";
        int maxVotes = 0;
        for (String candidate : voteCounts.keySet()) {
            int votes = voteCounts.get(candidate);
            if (votes > maxVotes) {
                maxVotes = votes;
                topCandidate = candidate;
            }
        }

        // Create components to display results
        JLabel resultLabel = new JLabel("<html>Results:<br>Urmi: " + voteCounts.get("Urmi") +
                                        " votes<br>Ifram: " + voteCounts.get("Ifram") +
                                        " votes<br>Diya: " + voteCounts.get("Diya") +
                                        " votes<br><br><b>CANDIDATE " + topCandidate.toUpperCase() + " HAS BEEN CHOSEN AS CR</b></html>");
        resultLabel.setFont(new Font("Serif", Font.BOLD, 14));

        JPanel panel = new JPanel();
        panel.add(resultLabel);

        resultFrame.add(panel);
        resultFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new VotingSystem();
    }
}
