package com.lekwacious.employee_app.data.repository;

import com.lekwacious.employee_app.data.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = """
            select * from employee em
             """
            , countQuery = """
             select count(em.id) from employee em
             where 1=1
             """
            , nativeQuery = true)
    List<Employee> findList();
//    Page<Employee> findList(Pageable pageable);

}
