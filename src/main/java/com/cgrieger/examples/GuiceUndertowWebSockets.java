package com.cgrieger.examples;

import com.cgrieger.examples.core.Service;
import com.cgrieger.examples.ws.WebSocketService;
import com.cgrieger.examples.ws.WebSocketServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class GuiceUndertowWebSockets {
  public static void main(String [] args) {
    // Wire the injector
    Injector injector = Guice.createInjector(
        new WebSocketServiceModule()
    );

    Service<WebSocketService> service = injector.getInstance(Key.get(new TypeLiteral<Service<WebSocketService>>(){}));
    service.start();
    //service.stop();
  }
}
