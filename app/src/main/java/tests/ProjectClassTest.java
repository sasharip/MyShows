package tests;

import com.myshows.studentapp.utils.EmailValidator;

import junit.framework.TestCase;

import org.junit.Test;


public class ProjectClassTest extends TestCase {
    @Test //testing valid email
    public void testEmailValidator() throws Exception{
        boolean ans = true;
        boolean val;
        String email = "vattghern777@gmail.com";
        val = EmailValidator.isValid(email);

        assertEquals(ans,val);

    }

    //@Mock
//    public void testConnectivityIsConnected() {
//        Connectivity ic = mock(Connectivity.class);
//        when(ic.isConnected().thenReturn;
//
//
//    }









}