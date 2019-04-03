package com.booktrade.kangere.validators;

import com.vaadin.data.validator.StringLengthValidator;

public class NotEmptyValidator extends StringLengthValidator {

    public NotEmptyValidator(){
        super("Field must not be empty",1,20);
    }

    public NotEmptyValidator(int max){
        super("Field must not be empty",1,max);
    }

}
