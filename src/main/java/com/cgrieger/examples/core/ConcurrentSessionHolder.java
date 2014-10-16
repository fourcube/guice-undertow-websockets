package com.cgrieger.examples.core;

import javax.websocket.Session;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class ConcurrentSessionHolder implements SessionHolder {
  private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

  // Our users wan't to know the current time!
  public ConcurrentSessionHolder() {
    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    Executors.newCachedThreadPool().submit((Runnable) () -> {
      while (true) {
        send("Server time: " + df.format(new Date()));

        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

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
