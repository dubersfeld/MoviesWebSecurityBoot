package com.dub.spring.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class NotTooOldValidator implements ConstraintValidator<NotTooOld, Date> {
	
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");	 
	private Date limDate; 

	
	@Override
	public void initialize(NotTooOld arg0) {
		System.out.println("custom valid init");
		try {
			limDate = formatter.parse("1920-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isValid(Date date, ConstraintValidatorContext context) {
		
		if (date == null) {
			return true;// no validation if null
		}	
		
		if (date.before(limDate)) {
			return false;
		} else { 				
			return true; 
		}
	}
		
}// class
