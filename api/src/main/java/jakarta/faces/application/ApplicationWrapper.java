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
package jakarta.faces.application;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import jakarta.el.ELContextListener;
import jakarta.el.ELException;
import jakarta.el.ELResolver;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.FacesWrapper;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.Behavior;
import jakarta.faces.component.search.SearchExpressionHandler;
import jakarta.faces.component.search.SearchKeywordResolver;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.event.ActionListener;
import jakarta.faces.event.SystemEvent;
import jakarta.faces.event.SystemEventListener;
import jakarta.faces.flow.FlowHandler;
import jakarta.faces.validator.Validator;

/**
 * @since 2.0
 */
@SuppressWarnings("deprecation")
public abstract class ApplicationWrapper extends Application implements FacesWrapper<Application>
{
    private Application delegate;

    @Deprecated
    public ApplicationWrapper()
    {
        
    }
    
    public ApplicationWrapper(Application delegate)
    {
        this.delegate = delegate;
    }
    
    @Override
    public void addBehavior(String behaviorId, String behaviorClass)
    {
        getWrapped().addBehavior(behaviorId, behaviorClass);
    }

    @Override
    public void addComponent(String componentType, String componentClass)
    {
        getWrapped().addComponent(componentType, componentClass);
    }

    @Override
    public void addConverter(Class<?> targetClass, String converterClass)
    {
        getWrapped().addConverter(targetClass, converterClass);
    }

    @Override
    public void addConverter(String converterId, String converterClass)
    {
        getWrapped().addConverter(converterId, converterClass);
    }

    @Override
    public void addDefaultValidatorId(String validatorId)
    {
        getWrapped().addDefaultValidatorId(validatorId);
    }

    @Override
    public void addELContextListener(ELContextListener listener)
    {
        getWrapped().addELContextListener(listener);
    }

    @Override
    public void addELResolver(ELResolver resolver)
    {
        getWrapped().addELResolver(resolver);
    }

    @Override
    public void addValidator(String validatorId, String validatorClass)
    {
        getWrapped().addValidator(validatorId, validatorClass);
    }

    @Override
    public Behavior createBehavior(String behaviorId) throws FacesException
    {
        return getWrapped().createBehavior(behaviorId);
    }

    @Override
    public UIComponent createComponent(FacesContext context, Resource componentResource)
    {
        return getWrapped().createComponent(context, componentResource);
    }

    @Override
    public UIComponent createComponent(FacesContext context, String componentType, String rendererType)
    {
        return getWrapped().createComponent(context, componentType, rendererType);
    }

    @Override
    public UIComponent createComponent(String componentType) throws FacesException
    {
        return getWrapped().createComponent(componentType);
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression, FacesContext context, String componentType,
                                       String rendererType)
    {
        return getWrapped().createComponent(componentExpression, context, componentType, rendererType);
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression, FacesContext contexte, String componentType)
            throws FacesException
    {
        return getWrapped().createComponent(componentExpression, contexte, componentType);
    }

    @Override
    public <T> Converter<T> createConverter(Class<T> targetClass)
    {
        return getWrapped().createConverter(targetClass);
    }

    @Override
    public Converter<?> createConverter(String converterId)
    {
        return getWrapped().createConverter(converterId);
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException
    {
        return getWrapped().createValidator(validatorId);
    }

    @Override
    public <T> T evaluateExpressionGet(FacesContext context, String expression, Class<? extends T> expectedType)
            throws ELException
    {
        return getWrapped().evaluateExpressionGet(context, expression, expectedType);
    }

    @Override
    public ActionListener getActionListener()
    {
        return getWrapped().getActionListener();
    }

    @Override
    public Iterator<String> getBehaviorIds()
    {
        return getWrapped().getBehaviorIds();
    }

    @Override
    public Iterator<String> getComponentTypes()
    {
        return getWrapped().getComponentTypes();
    }

    @Override
    public Iterator<String> getConverterIds()
    {
        return getWrapped().getConverterIds();
    }

    @Override
    public Iterator<Class<?>> getConverterTypes()
    {
        return getWrapped().getConverterTypes();
    }

    @Override
    public Locale getDefaultLocale()
    {
        return getWrapped().getDefaultLocale();
    }

    @Override
    public String getDefaultRenderKitId()
    {
        return getWrapped().getDefaultRenderKitId();
    }

    @Override
    public Map<String, String> getDefaultValidatorInfo()
    {
        return getWrapped().getDefaultValidatorInfo();
    }

    @Override
    public ELContextListener[] getELContextListeners()
    {
        return getWrapped().getELContextListeners();
    }

    @Override
    public ELResolver getELResolver()
    {
        return getWrapped().getELResolver();
    }

    @Override
    public ExpressionFactory getExpressionFactory()
    {
        return getWrapped().getExpressionFactory();
    }

    @Override
    public String getMessageBundle()
    {
        return getWrapped().getMessageBundle();
    }

    @Override
    public NavigationHandler getNavigationHandler()
    {
        return getWrapped().getNavigationHandler();
    }

    @Override
    public ProjectStage getProjectStage()
    {
        return getWrapped().getProjectStage();
    }

    @Override
    public ResourceBundle getResourceBundle(FacesContext ctx, String name)
    {
        return getWrapped().getResourceBundle(ctx, name);
    }

    @Override
    public ResourceHandler getResourceHandler()
    {
        return getWrapped().getResourceHandler();
    }

    @Override
    public StateManager getStateManager()
    {
        return getWrapped().getStateManager();
    }

    @Override
    public Iterator<Locale> getSupportedLocales()
    {
        return getWrapped().getSupportedLocales();
    }

    @Override
    public Iterator<String> getValidatorIds()
    {
        return getWrapped().getValidatorIds();
    }

    @Override
    public ViewHandler getViewHandler()
    {
        return getWrapped().getViewHandler();
    }

    @Override
    public Application getWrapped()
    {
        return delegate;
    }

    @Override
    public void publishEvent(FacesContext facesContext, Class<? extends SystemEvent> systemEventClass,
                             Class<?> sourceBaseType, Object source)
    {
        getWrapped().publishEvent(facesContext, systemEventClass, sourceBaseType, source);
    }

    @Override
    public void publishEvent(FacesContext facesContext, Class<? extends SystemEvent> systemEventClass, Object source)
    {
        getWrapped().publishEvent(facesContext, systemEventClass, source);
    }

    @Override
    public void removeELContextListener(ELContextListener listener)
    {
        getWrapped().removeELContextListener(listener);
    }

    @Override
    public void setActionListener(ActionListener listener)
    {
        getWrapped().setActionListener(listener);
    }

    @Override
    public void setDefaultLocale(Locale locale)
    {
        getWrapped().setDefaultLocale(locale);
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId)
    {
        getWrapped().setDefaultRenderKitId(renderKitId);
    }

    @Override
    public void setMessageBundle(String bundle)
    {
        getWrapped().setMessageBundle(bundle);
    }

    @Override
    public void setNavigationHandler(NavigationHandler handler)
    {
        getWrapped().setNavigationHandler(handler);
    }

    @Override
    public void setResourceHandler(ResourceHandler resourceHandler)
    {
        getWrapped().setResourceHandler(resourceHandler);
    }

    @Override
    public void setStateManager(StateManager manager)
    {
        getWrapped().setStateManager(manager);
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales)
    {
        getWrapped().setSupportedLocales(locales);
    }

    @Override
    public void setViewHandler(ViewHandler handler)
    {
        getWrapped().setViewHandler(handler);
    }

    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass, Class<?> sourceClass,
                                 SystemEventListener listener)
    {
        getWrapped().subscribeToEvent(systemEventClass, sourceClass, listener);
    }

    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass, SystemEventListener listener)
    {
        getWrapped().subscribeToEvent(systemEventClass, listener);
    }

    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass, Class<?> sourceClass,
                                     SystemEventListener listener)
    {
        getWrapped().unsubscribeFromEvent(systemEventClass, sourceClass, listener);
    }

    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass, SystemEventListener listener)
    {
        getWrapped().unsubscribeFromEvent(systemEventClass, listener);
    }

    @Override
    public void setFlowHandler(FlowHandler flowHandler)
    {
        getWrapped().setFlowHandler(flowHandler);
    }

    @Override
    public FlowHandler getFlowHandler()
    {
        return getWrapped().getFlowHandler();
    }

    @Override
    public void setSearchExpressionHandler(SearchExpressionHandler searchExpressionHandler)
    {
        getWrapped().setSearchExpressionHandler(searchExpressionHandler);
    }

    @Override
    public SearchExpressionHandler getSearchExpressionHandler()
    {
        return getWrapped().getSearchExpressionHandler();
    }

    @Override
    public SearchKeywordResolver getSearchKeywordResolver()
    {
        return getWrapped().getSearchKeywordResolver();
    }

    @Override
    public void addSearchKeywordResolver(SearchKeywordResolver resolver)
    {
        getWrapped().addSearchKeywordResolver(resolver);
    }

}
