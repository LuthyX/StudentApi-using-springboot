package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents (){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional =  studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("Email Taken");
        }
        studentRepository.save(student);
    }
    public void deleteStudent(Long studentId){
       boolean exists = studentRepository.existsById(studentId);
       if (!exists){
           throw new IllegalStateException("student with ID" + studentId + "does not exist");
       }
       studentRepository.deleteById(studentId);

        }

//    @Transactional
//    public void updateStudent(
//            Long studentId,
//            String name,
//            String email
//    ){
//        Student student  = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException(
//                "student with id " + studentId + " does not exist"));
//        if (name != null &&
//        name.length() >0 &&
//                !Objects.equals(student.getName(), name)
//        ) {
//             student.setName(name);
//        };

//        if (
//                email != null &&
//                email.length() > 0 &&
//                !Objects.equals(student.getEmail(), email)
//         ){
//            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
//            if (studentOptional.isPresent()) {
//                throw new IllegalStateException("email taken");
//            }
//            student.setEmail(email);
//        }
//    }



    public Optional<Student> getStudentById(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with ID " + studentId +"does not exist");
        }
        return studentRepository.findById(studentId);
    }


    public Optional<Student> changeNewStudent(Long studentId, Student changeStudent) {
        return studentRepository.findById(studentId).map(student -> {
            if (changeStudent.getName() != null && changeStudent.getName().length() > 0)
            {
                student.setName(changeStudent.getName());
            }

            if (changeStudent.getEmail()!= null && changeStudent.getEmail().length() >0){
                Optional<Student> checkEmail = studentRepository.findStudentByEmail(changeStudent.getEmail());
                if (checkEmail.isPresent()) {
                    throw new IllegalStateException("Email is already taken");
                }
                student.setEmail(changeStudent.getEmail());
            }
            ;
            return studentRepository.save(student);
        });
    }
}
