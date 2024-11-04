About the Project

The Hospital Management System is built to automate hospital tasks, such as registering patients, managing doctor details, and scheduling appointments. The project uses Java for its core logic and JDBC for database interactions.

Features

Patient: Add, view, and check patient records.
Doctor: View and check doctor records.
HospitalManagementSystem: Coordinate hospital operations and manage appointments.
Project Structure
Patient.java: Handles patient data, including registration and updating patient details.
Doctor.java: Manages doctor information and related operations.
HospitalManagementSystem.java: The main class that ties together patient and doctor management, acts as the entry point for the application, and includes the core logic for managing the system.
Technologies Used
Java: Programming language for the application.
JDBC (Java Database Connectivity): To connect and interact with the database.
MySQL (or any RDBMS): Backend database for storing records.

Database Creation Steps

Create the database: Start by creating a new database named Hospital:

CREATE DATABASE Hospital;

USE Hospital;

Create the Patients table:

CREATE TABLE Patients (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Age INT NOT NULL,
    Gender ENUM('Male', 'Female', 'Other') NOT NULL
);

Create the Doctors table:

CREATE TABLE Doctors (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Specialization VARCHAR(100) NOT NULL
);

Create the Appointments table:

CREATE TABLE Appointments (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    PatID INT NOT NULL,
    DocID INT NOT NULL,
    AppointmentDate DATE NOT NULL,
    FOREIGN KEY (PatID) REFERENCES Patients(ID),
    FOREIGN KEY (DocID) REFERENCES Doctors(ID)
);

Next Steps

Connect Java to MySQL: Ensure your HospitalManagementSystem.java file has the JDBC URL and credentials to connect to Hospital database.
Test Database Operations: Implement Java methods for CRUD operations in Patient.java, Doctor.java, and HospitalManagementSystem.java to interact with these tables.
This setup will give you a foundational database schema to manage patients, doctors, and appointments in your hospital management system.
