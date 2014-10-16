package com.cgrieger.examples.core;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentSessionHolder implements SessionHolder {
  private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

  @Override
  public void put(Session session) {
    sessions.put(session.getId(), session);
  }

  @Override
  public void remove(String sessionId) {
    sessions.remove(sessionId);
  }

  @Override
  public void send(String data) {
    sessions.forEachValue(10, (session) -> {
      if(session.isOpen()) {
        try {
          session.getBasicRemote().sendText(data);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
