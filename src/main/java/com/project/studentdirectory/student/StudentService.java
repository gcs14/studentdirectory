package com.project.studentdirectory.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Student profile already exists.");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exists.");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, Student updatedStudent) {
        Student currentStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + studentId + " does not exist"));

        String updatedStudentName = updatedStudent.getName();
        String updatedStudentEmail = updatedStudent.getEmail();
        LocalDate updatedStudentDob = updatedStudent.getDob();

        //name upgrade --> make functional
        if (updatedStudentName != null &&
                updatedStudentName.length() > 0 &&
                !Objects.equals(currentStudent.getName(), updatedStudentName)) {
            currentStudent.setName(updatedStudentName);
        }

        //email upgrade --> make functional
        if (updatedStudentEmail != null &&
                updatedStudentEmail.length() > 0 &&
                !Objects.equals(currentStudent.getEmail(), updatedStudentEmail)) {
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(updatedStudentEmail);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            currentStudent.setEmail(updatedStudentEmail);
        }

        //make a date of birth upgrade
        if (currentStudent.getDob() != updatedStudentDob){
            currentStudent.setDob(updatedStudentDob);
        }

    }
}
