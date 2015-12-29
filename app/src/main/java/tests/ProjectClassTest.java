package tests;

import android.content.Context;

import com.myshows.studentapp.utils.Connectivity;
import com.myshows.studentapp.utils.EmailValidator;

import junit.framework.TestCase;

import org.junit.Test;

import static org.mockito.Mockito.mock;


public class ProjectClassTest extends TestCase {
    @Test //testing valid email
    public void testEmailValidator() throws Exception{
        boolean ans = true;
        boolean val;
        String email = "vattghern777@gmail.com";
        val = EmailValidator.isValid(email);

        assertEquals(ans,val);

    }

    @Test
    public void testConnectivityIsConnected() {
        Connectivity is = mock(Connectivity.class);
        Context context = Context.
        when();

    }









}