package com.asaewing.healthimprover;

import android.content.Context;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Mock
    Context mMockContext;

    private String string = "++";
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1() throws Exception {
        //Assert.assertFalse("No",string.contains("*"));
        //Assert.assertTrue("Yes",string.contains("*"));
        if (string.contains("*")){
            System.out.print("True");
        } else {
            System.out.print("False");
        }
    }

}