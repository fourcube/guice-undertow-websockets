package com.cgrieger.examples.ws;

import com.cgrieger.examples.core.Service;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

import javax.servlet.ServletException;
import java.util.logging.Logger;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.websockets.jsr.WebSocketDeploymentInfo.ATTRIBUTE_NAME;

public class WebSocketService implements Service<WebSocketService> {
  private final Injector injector;
  private final Logger logger = Logger.getLogger(WebSocketService.class.getName());
  private Undertow server;

  @Inject
  public WebSocketService(Injector injector) {
    this.injector = injector;
  }

  @Override
  public void start() {
    final WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo()
        .addEndpoint(WebSocketEndpoint.class);

    DeploymentInfo websocketDeployment = deployment()
        .setContextPath("/ws")
        .addServletContextAttribute(ATTRIBUTE_NAME, webSocketDeploymentInfo)
        .setDeploymentName("websocket-deployment")
        .setClassLoader(WebSocketService.class.getClassLoader())
        .setClassIntrospecter(new GuiceClassIntrospector());

    DeploymentManager manager = Servlets.defaultContainer()
        .addDeployment(websocketDeployment);
    manager.deploy();

    try {
      server = Undertow.builder()
          .addHttpListener(8081, "localhost")
          .setHandler(path()
              .addPrefixPath("/ws", manager.start())
              .addPrefixPath("/", resource(new ClassPathResourceManager(WebSocketService.class.getClassLoader()))
                  .addWelcomeFiles("index.html")))


          .build();

      server.start();
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    if (server != null) {
      server.stop();
    }
  }

  private class GuiceClassIntrospector implements ClassIntrospecter {
    @Override
    public <T> InstanceFactory<T> createInstanceFactory(Class<T> clazz) throws NoSuchMethodException {
      logger.info("Creating instance of " + clazz.getName());
      return new ImmediateInstanceFactory<>(injector.getInstance(clazz));
    }
  }
}
