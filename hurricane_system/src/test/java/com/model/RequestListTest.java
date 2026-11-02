package com.model;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import com.model.location.Address;
/*
 * @Author: Logan
 * +-----------------------------------------------------------------+----------------------------------------------------------------------+
 * | Test                                                            | Reasoning                                                            |
 * +-----------------------------------------------------------------+----------------------------------------------------------------------+
 * |testMakeARequest                                                 | Tests that a request can be created.                                 |
 * |testMakeANullRequest                                             | Tests that a null request can be created.                            |
 * |testEditSeverityNegativeOne                                      | Tests that the severity of a request can be set to a negative value. |
 * |testEditSeverityFive                                             | Tests that the severity of a request can be set to a value of 5.     |
 * |testEditSeverityZero                                             | Tests that the severity of a request can be set to a value of 0.     |
 * |testEditSeveritySix                                              | Tests that the severity of a request can be set to a value of 6.     |
 * |testEditSeverityOne                                              | Tests that the severity of a request can be set to a value of 1.     |   
 * |testEditSeverityNull                                             | Tests that the severity of a request can be set to null.             |
 * |testMakeRequestList                                              | Tests that a request list can be created.                            |
 * |testMakeANullRequestList                                         | Tests that a null request list can be created.                       |
 * |testAddRequest                                                   | Tests that a request can be added to the request list.               |
 * |testAddNullRequest                                               | Tests that a null request can be added to the request list.          |
 * |testSaveRequestList                                              | Tests that the request list can be saved.                            |
 * |testSaveNullRequestList                                          | Tests that a null request list can be saved.                         |
 * |testGetRequestList                                               | Tests that the request list can be retrieved.                        |
 * |testGetNullRequestList                                           | Tests that a null request list can be retrieved.                     |
 * +-----------------------------------------------------------------+----------------------------------------------------------------------+
**/
public class RequestListTest {
    @BeforeClass
	public static void setup() {
        RequestList.getInstance();
        RequestList.getInstance().addRequest(new Request(

            java.util.UUID.randomUUID(), "john doe", (byte) 4, 
            new Address("123 Main St", "Cityville", "State", "12345"), ResponseStatus.IN_PROGRESS,
             ResponseType.MEDICAL, "None", "None"
        ));
        RequestList.getInstance().save();
    }
    //---------------------------------------------------------------------------Request Tests--------------------------------------------------------------------------//
    @Test
    public void testMakeARequest() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        assertTrue(request == requestListList.getRequest(request.getId()));
    }
    @Test
    public void testMakeANUllRequest() {
        Request request = null;
        assertFalse(request != null);
    }
    //---------------------------------------------------------------------------Request Severity Tests--------------------------------------------------------------------------//
    @Test
    public void testEditSeverityNegativeOne() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        request.setSeverity((byte) -1);
        assertTrue(request.getSeverity() == 0);
    }
    @Test
    public void testEditSeverityFive() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        request.setSeverity((byte) 5);
        assertTrue(request.getSeverity() == 5);
    }
    @Test
    public void testEditSeverityZero() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        request.setSeverity((byte) 0);
        assertTrue(request.getSeverity() == 0);
    }
    @Test
    public void testEditSeveritySix() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        request.setSeverity((byte) 6);
        assertTrue(request.getSeverity() == 6);
    }
    @Test
    public void testEditSeverityOne() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        request.setSeverity((byte) 1);
        assertTrue(request.getSeverity() == 1);
    }
    @Test
    public void testEditSeverityNull() {
        RequestList requestListList = RequestList.getInstance();
        Request request = requestListList.getRequest(requestListList.getRequestList().keySet().iterator().next());
        request.setSeverity((Byte) null);
        assertTrue(request.getSeverity() == 0);
    }
    //---------------------------------------------------------------------------Request List Tests--------------------------------------------------------------------------//
     @Test
    public void testMakeRequestList() {
    RequestList requestList = RequestList.getInstance();
        assertTrue(requestList != null);
    }
    @Test
    public void testMakeANullRequestList() {
        RequestList requestList = null;
        assertFalse(requestList != null);
    }
    @Test
    public void testAddRequest() {
        RequestList requestList = RequestList.getInstance();
        int initialSize = requestList.getRequestList().size();
        requestList.addRequest(new Request(
            java.util.UUID.randomUUID(), "jane doe", (byte) 3,
            new Address("456 Elm St", "Townsville", "State", "67890"), ResponseStatus.IN_PROGRESS,
             ResponseType.MEDICAL, "None", "None"));
        assertTrue(requestList.getRequestList().size() == initialSize + 1);
    }
    @Test
    public void testAddNullRequest() {
        RequestList requestList = RequestList.getInstance();
        int initialSize = requestList.getRequestList().size();
        requestList.addRequest(null);
        assertTrue(requestList.getRequestList().size() == initialSize + 1);
    }
    @Test
    public void testSaveRequestList() {
        RequestList requestList = RequestList.getInstance();
        assertTrue(requestList.save());
    }
    @Test
    public void testSaveNullRequestList() {
        RequestList requestList = null;
        assertFalse(requestList != null && requestList.save());
    }
    @Test
    public void testGetRequestList() {
        RequestList requestList = RequestList.getInstance();
        assertTrue(requestList.getRequestList() != null);
    }
    @Test
    public void testGetNullRequestList() {
        RequestList requestList = null;
        assertFalse(requestList != null && requestList.getRequestList() != null);
    }
    @AfterClass
    public static void teardown() {
        RequestList.getInstance().getRequestList().clear();
        RequestList.getInstance().save();
    }
}
