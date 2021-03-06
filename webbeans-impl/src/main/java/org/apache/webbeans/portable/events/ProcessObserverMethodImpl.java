/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.portable.events;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessObserverMethod;

import org.apache.webbeans.config.WebBeansContext;

/**
 * Implementation of  {@link ProcessObserverMethod}.
 * 
 * @version $Rev$ $Date$
 *
 * @param <X> declared bean class
 * @param <T> event type
 */
public class ProcessObserverMethodImpl<T,X> extends EventBase implements ProcessObserverMethod<T, X>
{
    /**Observer annotated method*/
    private final AnnotatedMethod<X> annotatedMethod;
    
    /**ObserverMethod instance*/
    private final ObserverMethod<T> observerMethod;

    public ProcessObserverMethodImpl(AnnotatedMethod<X> annotatedMethod,ObserverMethod<T> observerMethod)
    {
        this.annotatedMethod = annotatedMethod;
        this.observerMethod = observerMethod;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefinitionError(Throwable t)
    {
        checkState();
        WebBeansContext.getInstance().getBeanManagerImpl().getErrorStack().pushError(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotatedMethod<X> getAnnotatedMethod()
    {
        checkState();
        return annotatedMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObserverMethod<T> getObserverMethod()
    {
        checkState();
        return observerMethod;
    }

}
