package com.cgrieger.examples.ws;

import com.cgrieger.examples.core.ConcurrentSessionHolder;
import com.cgrieger.examples.core.Service;
import com.cgrieger.examples.core.SessionHolder;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class WebSocketServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SessionHolder.class).to(ConcurrentSessionHolder.class).asEagerSingleton();
    bind(WebSocketEndpoint.class).asEagerSingleton();
    bind(new TypeLiteral<Service<WebSocketService>>(){}).to(WebSocketService.class);
  }
}
