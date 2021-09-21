/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ws.common.injection;

import java.io.Serializable;
import java.security.Principal;

import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.handler.MessageContext;

import org.w3c.dom.Element;

/**
 * Web service context implementation that is thread local aware as required by JAX-WS spec.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
public final class ThreadLocalAwareWebServiceContext implements WebServiceContext, Serializable
{

   private static final long serialVersionUID = 126557512266764152L;

   private static final ThreadLocalAwareWebServiceContext SINGLETON = new ThreadLocalAwareWebServiceContext();

   private final transient ThreadLocal<WebServiceContext> contexts = new InheritableThreadLocal<WebServiceContext>();

   public static ThreadLocalAwareWebServiceContext getInstance()
   {
      return SINGLETON;
   }

   public void setMessageContext(final WebServiceContext ctx)
   {
      this.contexts.set(ctx);
   }

   public EndpointReference getEndpointReference(final Element... referenceParameters)
   {
      return getWebServiceContext().getEndpointReference(referenceParameters);
   }

   public <T extends EndpointReference> T getEndpointReference(final Class<T> clazz, final Element... referenceParameters)
   {
      return getWebServiceContext().getEndpointReference(clazz, referenceParameters);
   }

   public MessageContext getMessageContext()
   {
      return getWebServiceContext().getMessageContext();
   }

   public Principal getUserPrincipal()
   {
      return getWebServiceContext().getUserPrincipal();
   }

   public boolean isUserInRole(String role)
   {
      return getWebServiceContext().isUserInRole(role);
   }
   
   private WebServiceContext getWebServiceContext()
   {
       final WebServiceContext delegate = contexts.get();

       if (delegate == null)
       {
          throw new IllegalStateException();
       }
       
       return delegate;
   }

   protected Object readResolve()
   {
       return SINGLETON;
   }

}
