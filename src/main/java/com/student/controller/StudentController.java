package com.student.controller;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.student.model.Student;
import com.student.repository.StudentRepository;



@Controller
@RequestMapping("/students/")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;	
	
	@GetMapping("showForm")
	public String showStudentForm(Student student) {
		return "add";
	}
	
	@GetMapping("list")
	public String students(Model model) {
		model.addAttribute("students", this.studentRepository.findAll());
		return "index";
	}
	
	@PostMapping("add")
	public String addStudent(@Valid Student student, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "add";
		}
		
		this.studentRepository.save(student);
		return "redirect:list";
	}
	
	
	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable ("id") long id, Model model) {
		Student student = this.studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));
		
		model.addAttribute("student", student);
		return "update";
	}
	
	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, @Valid Student student, BindingResult result, Model model) {
		if(result.hasErrors()) {
			student.setId(id);
			return "update";
		}
		
		// update student
		studentRepository.save(student);
		
		// get all students ( with update)
		model.addAttribute("students", this.studentRepository.findAll());
		return "index";
	}
	
	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable ("id") long id, Model model) {
		
		Student student = this.studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));
		
		this.studentRepository.delete(student);
		model.addAttribute("students", this.studentRepository.findAll());
		return "index";
		
	}
}
