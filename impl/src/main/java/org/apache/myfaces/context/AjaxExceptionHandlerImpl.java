/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.el.ELException;
import jakarta.faces.FacesException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.PartialResponseWriter;
import jakarta.faces.context.PartialViewContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import jakarta.faces.event.SystemEvent;
import org.apache.myfaces.core.api.shared.lang.Assert;

/**
 * 
 * Specialized Ajax Handler, according to Faces 2.0 rev A section 13.3.7.
 */
public class AjaxExceptionHandlerImpl extends ExceptionHandler
{
    //The spec says this related to exception handling in ajax requests:
    //
    //"... JavaServer Faces Ajax frameworks must ensure exception information 
    // is written to the response in the format: 
    //
    //<partial-response>
    //  <error>
    //    <error-name>...</error-name>
    //    <error-message>...</error-message>
    //  </error>
    //</partial-response>
    //
    //Implementations must ensure that an ExceptionHandler suitable for writing 
    //exceptions to the partial response is installed if the current request 
    //required an Ajax response (PartialViewContext.isAjaxRequest() returns true)
    //
    //Implementations may choose to include a specialized ExceptionHandler for 
    //Ajax that extends from jakarta.faces.context.ExceptionHandlerWrapper, and 
    //have the jakarta.faces.context.ExceptionHandlerFactory implementation 
    //install it if the environment requires it. ..."
    
    private static final Logger log = Logger.getLogger(AjaxExceptionHandlerImpl.class.getName());
    
    private Queue<ExceptionQueuedEvent> handled;
    private Queue<ExceptionQueuedEvent> unhandled;
    private ExceptionQueuedEvent handledAndThrown;
    
    public AjaxExceptionHandlerImpl()
    {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ExceptionQueuedEvent getHandledExceptionQueuedEvent()
    {
        return handledAndThrown;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents()
    {
        return handled == null ? Collections.emptyList() : handled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Throwable getRootCause(Throwable throwable)
    {
        Assert.notNull(throwable, "throwable");
        
        while (throwable != null)
        {
            Class<?> clazz = throwable.getClass();
            if (!clazz.equals(FacesException.class) && !clazz.equals(ELException.class))
            {
                return throwable;
            }
            
            throwable = throwable.getCause();
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents()
    {
        return unhandled == null ? Collections.emptyList() : unhandled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() throws FacesException
    {
        if (unhandled != null && !unhandled.isEmpty())
        {
            if (handled == null)
            {
                handled = new LinkedList<>();
            }
            
            List<Throwable> throwableList = new ArrayList<>();
            FacesContext facesContext = null;
            
            do
            {
                // For each ExceptionEvent in the list
                
                // get the event to handle
                ExceptionQueuedEvent event = unhandled.peek();
                try
                {
                    // call its getContext() method
                    ExceptionQueuedEventContext context = event.getContext();

                    if (facesContext == null)
                    {
                        facesContext = event.getContext().getContext();
                    }
                    
                    // and call getException() on the returned result
                    Throwable exception = context.getException();

                    // Upon encountering the first such Exception that is not an instance of
                    // jakarta.faces.event.AbortProcessingException
                    if (!shouldSkip(exception))
                    {
                        Throwable rootCause = getRootCause(exception);

                        // set handledAndThrown so that getHandledExceptionQueuedEvent() returns this event
                        handledAndThrown = event;

                        throwableList.add(rootCause == null ? exception : rootCause);

                        ExceptionHandlerUtils.logException(rootCause == null ? exception : rootCause,
                                context.getComponent(), context.getContext(), log);
                    }
                }
                finally
                {
                    // if we will throw the Exception or if we just logged it,
                    // we handled it in either way --> add to handled
                    handled.add(event);
                    unhandled.remove(event);
                }
            } while (!unhandled.isEmpty());

            if (!throwableList.isEmpty())
            {
                PartialResponseWriter partialWriter = null;
                boolean responseComplete = false;

                //Retrieve the partial writer used to render the exception
                if (facesContext == null)
                {
                    facesContext = FacesContext.getCurrentInstance();
                }
                
                facesContext = (facesContext == null) ? FacesContext.getCurrentInstance() : facesContext;
                ExternalContext externalContext = facesContext.getExternalContext();
                
                //Clean up the response if by some reason something has been written before.
                if (!facesContext.getExternalContext().isResponseCommitted())
                {
                    facesContext.getExternalContext().responseReset();
                }
                
                PartialViewContext partialViewContext = facesContext.getPartialViewContext();
                partialWriter = partialViewContext.getPartialResponseWriter();

                // ajax request --> xml error page 
                externalContext.setResponseContentType("text/xml");
                externalContext.setResponseCharacterEncoding("UTF-8");
                externalContext.addResponseHeader("Cache-control", "no-cache");
                
                try
                {
                    partialWriter.startDocument();
    
                    for (Throwable t : throwableList)
                    {
                        renderAjaxError(partialWriter, t);
                    }
                    
                    responseComplete = true;
                }
                catch (IOException e)
                {
                    if (log.isLoggable(Level.SEVERE))
                    {
                        log.log(Level.SEVERE, "Cannot render exception on ajax request", e);
                    }
                }
                finally
                {
                    if (responseComplete)
                    {
                        try
                        {
                            partialWriter.endDocument();
                            facesContext.responseComplete();
                        }
                        catch (IOException e1)
                        {
                            if (log.isLoggable(Level.SEVERE))
                            {
                                log.log(Level.SEVERE, "Cannot render exception on ajax request", e1);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void renderAjaxError(PartialResponseWriter partialWriter, Throwable ex) throws IOException
    {
        partialWriter.startError(ex.getClass().getName());
        if (ex.getCause() != null)
        {
            partialWriter.write(ex.getCause().toString());
        }
        else if (ex.getMessage() != null)
        {
            partialWriter.write(ex.getMessage());
        }
        partialWriter.endError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isListenerForSource(Object source)
    {
        return source instanceof ExceptionQueuedEventContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processEvent(SystemEvent exceptionQueuedEvent) throws AbortProcessingException
    {
        if (unhandled == null)
        {
            unhandled = new LinkedList<ExceptionQueuedEvent>();
        }
        
        unhandled.add((ExceptionQueuedEvent)exceptionQueuedEvent);
    }
    
    protected Throwable getRethrownException(Throwable exception)
    {
        // Let toRethrow be either the result of calling getRootCause() on the Exception, 
        // or the Exception itself, whichever is non-null
        Throwable toRethrow = getRootCause(exception);
        if (toRethrow == null)
        {
            toRethrow = exception;
        }
        
        return toRethrow;
    }
    
    protected FacesException wrap(Throwable exception)
    {
        if (exception instanceof FacesException facesException)
        {
            return facesException;
        }
        return new FacesException(exception);
    }
    
    protected boolean shouldSkip(Throwable exception)
    {
        return exception instanceof AbortProcessingException;
    }

}
