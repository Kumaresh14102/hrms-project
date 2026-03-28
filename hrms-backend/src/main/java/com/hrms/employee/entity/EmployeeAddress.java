package com.hrms.employee.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAddress {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "employee_id", nullable = false)
	    private Employee employee;
	    
	    @Column(nullable = false, length = 20)
	    private String type;
	    
	    @Column(name = "line1", nullable = false, length = 255)
	    private String line1;
	    
	    @Column(name = "line2", length = 255)
	    private String line2;
	    
	    @Column(nullable = false, length = 100)
	    private String city;
	    
	    @Column(nullable = false, length = 100)
	    private String state;
	    
	    @Column(nullable = false, length = 10)
	    private String pincode;
	    
	    @Column(nullable = false, length = 100)
	    private String country;

}
