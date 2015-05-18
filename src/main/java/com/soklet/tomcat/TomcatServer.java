/*
 * Copyright 2015 Transmogrify LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.soklet.tomcat;

import static java.util.Objects.requireNonNull;

import java.nio.file.Paths;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import com.soklet.util.InstanceProvider;
import com.soklet.web.routing.RoutingServlet;
import com.soklet.web.server.Server;
import com.soklet.web.server.ServerException;

/**
 * A <a href="http://tomcat.apache.org">Tomcat</a>-backed implementation of {@link Server}.
 * 
 * @author <a href="http://revetkn.com">Mark Allen</a>
 * @since 1.0.0
 */
public class TomcatServer implements Server {
  private final Tomcat tomcat;
  private final InstanceProvider instanceProvider;

  public TomcatServer(InstanceProvider instanceProvider) {
    this.instanceProvider = requireNonNull(instanceProvider);
    this.tomcat = new Tomcat();
  }

  public void start() throws ServerException {
    try {
      tomcat.setPort(8080);
      tomcat.setHostname("0.0.0.0");

      Context context = tomcat.addWebapp("", Paths.get(".").toAbsolutePath().toString());

      Tomcat.addServlet(context, "soklet-default", instanceProvider.provide(DefaultServlet.class));
      context.addServletMapping("/static/*", "soklet-default");

      Tomcat.addServlet(context, "routing", instanceProvider.provide(RoutingServlet.class));
      context.addServletMapping("/*", "routing");

      tomcat.start();
    } catch (Exception e) {
      throw new ServerException(e);
    }
  }

  public void stop() throws ServerException {
    try {
      tomcat.stop();
    } catch (LifecycleException e) {
      throw new ServerException(e);
    }
  }
}