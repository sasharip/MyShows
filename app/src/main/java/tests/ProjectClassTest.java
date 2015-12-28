package tests;

import com.myshows.studentapp.utils.EmailValidator;

import junit.framework.TestCase;

import org.junit.Test;


public class ProjectClassTest extends TestCase {
    @Test
    public void testEmailValidator() throws Exception{
        EmailValidator emw1 =new EmailValidator();
        assertEquals("valid",emw1.isValid("vattghern777@gmail.com"));
        assertEquals("valid",emw1.isValid("demonOS@gadkasd.com"));
        assertEquals("not valid",emw1.isValid("dasdsaasda"));

    }






}