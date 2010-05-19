/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.el.el22;

import javax.el.ELContextListener;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;

import org.apache.webbeans.el.OwbElContextListener;
import org.apache.webbeans.el.WebBeansELResolver;
import org.apache.webbeans.el.WrappedExpressionFactory;
import org.apache.webbeans.spi.adaptor.ELAdaptor;

public class EL22Adaptor implements ELAdaptor
{
    public EL22Adaptor()
    {
        
    }

    @Override
    public ELContextListener getOwbELContextListener()
    {
        return new OwbElContextListener();
    }

    @Override
    public ELResolver getOwbELResolver()
    {        
        return new WebBeansELResolver();
    }

    @Override
    public ExpressionFactory getOwbWrappedExpressionFactory(ExpressionFactory expressionFactroy)
    {
        return new WrappedExpressionFactory(expressionFactroy);
    }

}