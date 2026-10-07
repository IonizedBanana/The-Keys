package com.model;

import java.util.HashMap;
import java.util.UUID;

public class RequestList {
  private static RequestList requestList;
  private HashMap<UUID, Request> requests;

  private RequestList() {
    requests = DataReader.getRequests();
  }

  public static RequestList getInstance() {
    if (requestList == null) {
      requestList = new RequestList();
      return requestList;
    }
    return requestList;
  }

  public Request getRequest(UUID id) {
    return requests.get(id);
  }

  public void addRequest(Request request) {
    requests.put(request.getId(), request);
  }

  public boolean save() {
    return DataWriter.saveRequests(requests);
  }
}
