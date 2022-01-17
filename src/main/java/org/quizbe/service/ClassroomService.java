package org.quizbe.service;

import com.google.common.collect.Lists;
import org.quizbe.dao.ClassroomRepository;
import org.quizbe.model.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {
    ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository){
        this.classroomRepository = classroomRepository;
    }

    public List<Classroom> getAllClassrooms(){
        return Lists.newArrayList(classroomRepository.findAll());
    }

}
