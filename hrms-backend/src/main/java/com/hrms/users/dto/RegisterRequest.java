package com.hrms.users.dto;

import lombok.Data;
import com.hrms.users.entity.Role;

@Data
public class RegisterRequest {
	
	 private String email;
	 private String password;
	 private Role role;

}
