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
package org.apache.myfaces.view.facelets.tag.jsf.html;

import jakarta.el.ExpressionFactory;
import jakarta.faces.application.StateManager;
import org.apache.myfaces.test.core.AbstractMyFacesCDIRequestTestCase;

import org.apache.myfaces.config.MyfacesConfig;
import org.junit.Assert;
import org.junit.Test;

public class SelectOneItemsTest extends AbstractMyFacesCDIRequestTestCase
{
    @Override
    protected boolean isScanAnnotations()
    {
        return true;
    }

    @Override
    protected void setUpWebConfigParams() throws Exception
    {
        super.setUpWebConfigParams();
        servletContext.addInitParameter("org.apache.myfaces.annotation.SCAN_PACKAGES","org.apache.myfaces.view.facelets.tag.jsf.html");
        servletContext.addInitParameter(StateManager.STATE_SAVING_METHOD_PARAM_NAME, StateManager.STATE_SAVING_METHOD_CLIENT);
        servletContext.addInitParameter("jakarta.faces.PARTIAL_STATE_SAVING", "true");
        servletContext.addInitParameter(MyfacesConfig.REFRESH_TRANSIENT_BUILD_ON_PSS, "auto");
    }
    
    @Override
    protected ExpressionFactory createExpressionFactory()
    {
        return new org.apache.el.ExpressionFactoryImpl();
    }    
    
    @Test
    public void testIndex() throws Exception
    {
        startViewRequest("/selectOneItems.xhtml");
        processLifecycleExecuteAndRender();
        
        String content = getRenderedContent();
        System.err.println(content);
        Assert.assertTrue(content.contains("Cars"));
        Assert.assertTrue(content.contains("Animals"));
        Assert.assertTrue(content.contains("Audi"));
        Assert.assertTrue(content.contains("Fish"));

        endRequest();
    }
}