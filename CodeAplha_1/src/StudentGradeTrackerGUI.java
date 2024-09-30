import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class StudentGradeTrackerGUI extends JFrame {
    private final ArrayList<Student> students;
    private final JTextPane displayArea;
    private final JTextField nameField;
    private final JTextField gradeField;

    // Inner class to represent a student
    static class Student {
        String name;
        double grade;

        Student(String name, double grade) {
            this.name = name;
            this.grade = grade;
        }
    }

    public StudentGradeTrackerGUI() {
        students = new ArrayList<>();
        setTitle("Student Grade Tracker");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create UI components
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JLabel nameLabel = new JLabel("Student Name:");
        JLabel gradeLabel = new JLabel("Student Grade:");
        nameField = new JTextField();
        gradeField = new JTextField();
        JButton addButton = new JButton("Add Grade");
        JButton displayButton = new JButton("Display Grades");
        JButton clearButton = new JButton("Clear Results");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(gradeLabel);
        inputPanel.add(gradeField);
        inputPanel.add(addButton);
        inputPanel.add(displayButton);

        // Use JTextPane for rich text and color
        displayArea = new JTextPane();
        displayArea.setEditable(false);
        displayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Set to fixed-width font
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(clearButton, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addStudentGrade());
        displayButton.addActionListener(e -> displayStudentGrades());
        clearButton.addActionListener(e -> clearResults());

        setVisible(true);
    }

    // Function to add a student's grade
    private void addStudentGrade() {
        String name = nameField.getText();
        String gradeText = gradeField.getText();
        if (name.isEmpty() || gradeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both name and grade.");
            return;
        }

        try {
            double grade = Double.parseDouble(gradeText);
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Please enter a valid grade between 0 and 100.");
                return;
            }
            students.add(new Student(name, grade));
            JOptionPane.showMessageDialog(this, "Grade added successfully!");
            nameField.setText("");
            gradeField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a numeric grade.");
        }
    }

    // Function to display all student grades and statistics in a tabular form with color formatting
    private void displayStudentGrades() {
        if (students.isEmpty()) {
            displayArea.setText("No grades have been entered yet.");
            return;
        }

        // Use styled document to support text coloring
        StyledDocument doc = displayArea.getStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        // Define styles for different sections
        Style studentStyle = doc.addStyle("StudentStyle", defaultStyle);
        StyleConstants.setForeground(studentStyle, Color.BLUE);

        Style statStyle = doc.addStyle("StatStyle", defaultStyle);
        StyleConstants.setForeground(statStyle, Color.BLUE);

        Style headerStyle = doc.addStyle("HeaderStyle", defaultStyle);
        StyleConstants.setForeground(headerStyle, Color.RED);

        try {
            // Add header
            doc.insertString(doc.getLength(), String.format("%-20s %-10s\n", "Student Name", "Grade"), headerStyle);
            doc.insertString(doc.getLength(), "--------------------------------\n", headerStyle);

            double total = 0;
            double highest = students.get(0).grade;
            double lowest = students.get(0).grade;

            // Loop to append each student's name and grade with color
            for (Student student : students) {
                doc.insertString(doc.getLength(),
                        String.format("%-20s %-10.2f\n", student.name, student.grade), studentStyle);
                total += student.grade;
                if (student.grade > highest)
                    highest = student.grade;
                if (student.grade < lowest)
                    lowest = student.grade;
            }

            double average = total / students.size();

            // Display the results
            doc.insertString(doc.getLength(), "\n--------------------------------\n", headerStyle);
            doc.insertString(doc.getLength(),
                    String.format("%-20s %d\n", "Number of Students:", students.size()), statStyle);
            doc.insertString(doc.getLength(),
                    String.format("%-20s %.2f\n", "Average Grade:", average), statStyle);
            doc.insertString(doc.getLength(),
                    String.format("%-20s %.2f\n", "Highest Grade:", highest), statStyle);
            doc.insertString(doc.getLength(),
                    String.format("%-20s %.2f\n", "Lowest Grade:", lowest), statStyle);

        } catch (BadLocationException e) {
        }
    }

    // Function to clear the displayed results
    private void clearResults() {
        displayArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeTrackerGUI::new);
    }
}