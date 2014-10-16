package com.cgrieger.examples.core;

import javax.websocket.Session;

/**
 * Stores WebSocket Sessions.
 */
public interface SessionHolder {
  public void put(Session session);
  public void remove(String sessionId);
  public void send(String data);
}
