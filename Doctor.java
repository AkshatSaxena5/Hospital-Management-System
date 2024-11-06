import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class HospitalManagementSystem extends JFrame {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Akshat@12345";

    private Connection connection;
    private Patient patient;
    private Doctor doctor;

    public HospitalManagementSystem() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            patient = new Patient(connection);
            doctor = new Doctor(connection);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Setup main frame properties
        setTitle("Hospital Management System");
        setSize(800, 300);  // Increased height for heading
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Heading
        JLabel headingLabel = new JLabel("Welcome to India's Best Hospital", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size and style
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add some padding

        // Main button panel (Horizontal layout)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Horizontal layout with spacing

        // Buttons
        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton viewDoctorsButton = new JButton("View Doctors");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton exitButton = new JButton("Exit");

        // Add buttons to panel
        buttonPanel.add(addPatientButton);
        buttonPanel.add(viewPatientsButton);
        buttonPanel.add(viewDoctorsButton);
        buttonPanel.add(bookAppointmentButton);
        buttonPanel.add(exitButton);

        // Add components to the frame
        add(headingLabel, BorderLayout.NORTH); // Add heading at the top
        add(buttonPanel, BorderLayout.CENTER); // Add buttons in the center

        // Button Listeners
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter Patient Name:");
                String ageStr = JOptionPane.showInputDialog("Enter Patient Age:");
                String gender = JOptionPane.showInputDialog("Enter Patient Gender:");
                int age = Integer.parseInt(ageStr);

                if (patient.addPatient(name, age, gender)) {
                    JOptionPane.showMessageDialog(null, "Patient added successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add patient.");
                }
            }
        });

        viewPatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object[]> patients = patient.viewPatients();
                String[] columnNames = {"ID", "Name", "Age", "Gender"};
                JTable table = new JTable(patients.toArray(new Object[0][0]), columnNames);
                JOptionPane.showMessageDialog(null, new JScrollPane(table), "View Patients", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewDoctorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object[]> doctors = doctor.viewDoctors();
                String[] columnNames = {"ID", "Name", "Specialization"};
                JTable table = new JTable(doctors.toArray(new Object[0][0]), columnNames);
                JOptionPane.showMessageDialog(null, new JScrollPane(table), "View Doctors", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        bookAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patientIdStr = JOptionPane.showInputDialog("Enter Patient ID:");
                String doctorIdStr = JOptionPane.showInputDialog("Enter Doctor ID:");
                String appointmentDate = JOptionPane.showInputDialog("Enter appointment date (YYYY-MM-DD):");
                int patientId = Integer.parseInt(patientIdStr);
                int doctorId = Integer.parseInt(doctorIdStr);

                if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
                    if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                        if (bookAppointment(patientId, doctorId, appointmentDate)) {
                            JOptionPane.showMessageDialog(null, "Appointment booked successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to book appointment.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Doctor not available on this date!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid patient or doctor ID.");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private boolean bookAppointment(int patientId, int doctorId, String appointmentDate) {
        String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, doctorId);
            preparedStatement.setString(3, appointmentDate);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalManagementSystem hms = new HospitalManagementSystem();
            hms.setVisible(true);
        });
    }
}
