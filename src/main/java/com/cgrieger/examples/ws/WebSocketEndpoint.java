package com.cgrieger.examples.ws;

import com.cgrieger.examples.core.SessionHolder;
import com.google.inject.Inject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

@ServerEndpoint("/")
class WebSocketEndpoint {
  private final SessionHolder sessionHolder;
  private final Logger logger = Logger.getLogger(WebSocketEndpoint.class.getName());

  @Inject
  public WebSocketEndpoint(SessionHolder sessionHolder) {
    this.sessionHolder = sessionHolder;
  }

  @OnOpen
  public void connect(Session session) {
    String sessionId = session.getId();
    logger.info("New connection: " + sessionId);
    sessionHolder.put(session);

    sessionHolder.send("Welcome " + sessionId + "!");
  }

  @OnMessage
  public void message(String message, Session session) {
    // We ignore all messages.
  }

  @OnClose
  public void close(CloseReason closeReason, Session session) {
    String sessionId = session.getId();
    logger.info("Closed connection: " + sessionId + "(" + closeReason.getReasonPhrase() + ")");
    sessionHolder.remove(sessionId);
  }
}
