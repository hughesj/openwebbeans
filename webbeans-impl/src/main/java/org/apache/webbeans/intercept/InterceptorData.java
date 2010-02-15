/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.webbeans.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.spi.Interceptor;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;


/**
 * Web Beans general interceptor API contract. There are two types of
 * interceptor definition in the Web Beans Container. These are;
 * <p>
 * <ul>
 * <li>EJB related interceptors with {@link Interceptors} annotation</li>
 * <li>WebBeans specific interceptor definition with using
 * {@link javax.interceptor.InterceptorBinding} and {@link Interceptor}</li>
 * </ul>
 * </p>
 * <p>
 * If the web beans is an EJB component, EJB container is responsible for
 * calling the EJB related interceptors, otherwise Web Beans container takes the
 * responsibility. In the both cases, Web Beans Container is responsible for
 * calling web beans related inteceptors.
 * </p>
 * 
 * @version $Rev$ $Date$
 */
public interface InterceptorData
{
    /**
     * Gets {@link PostConstruct} annotated method if exist
     * return null otherwise. 
     * @return post-construct method
     */
    public Method getPostConstruct();

    /**
     * Gets the list of {@link PreDestroy} annotated method
     * if exist, returns null otherwise. 
     * @return pre-destroy method
     */
    public Method getPreDestroy();

    /**
     * Gets {@link AroundInvoke} annotated method
     * if exist, returns null otherwise. 
     * @return around invoke method
     */
    public Method getAroundInvoke();
    
    /**
     * Gets interceptor method that this
     * interceptor data contains. It is one of
     * the post construct, pre-destroy or around-invoke.
     * @return interceptor method.
     */
    public Method getInterceptorMethod();

    /**
     * Sets the interceptor method.
     * <p>
     * Annotation is in
     * <ul>
     * <li>{@link PostConstruct}</li>
     * <li>{@link PreDestroy}</li>
     * <li>{@link AroundInvoke}</li> 
     * </ul>
     * </p>
     * @param interceptorMethod interceptor method
     * @param interceptorType interceptor annotation class
     */
    public void setInterceptorMethod(Method interceptorMethod, Class<? extends Annotation> interceptorType);

    /**
     * Sets the source of the interceptor.
     * <p>
     * Interceptor is defined by the bean itself or defined
     * by the interceptor class.
     * </p>
     * @param definedInInterceptorClass defined in interceptor class
     */
    public void setDefinedInInterceptorClass(boolean definedInInterceptorClass);

    /**
     * Gets the interceptor instance. 
     * @return the interceptor instance
     */
    public Object getInterceptorInstance();

    /**
     * Sets the interceptor instance. 
     * @param instance interceptor instance
     */
    public void setInterceptorInstance(Object instance);

    /**
     * Checks the interceptor is defined at the method level. 
     * @return inteceptor defined in method
     */
    public boolean isDefinedInMethod();

    /**
     * Sets true if interceptor is defined at the method, false ow. 
     * @param definedInMethod defined in method flag
     */
    public void setDefinedInMethod(boolean definedInMethod);

    /**
     * Gets the interceptor annotated method.
     * 
     * @return the method
     */
    public Method getInterceptorBindingMethod();

    /**
     * Sets the interceptor annotated method. 
     * @param annotatedMethod interceptor annotated method.
     */
    public void setInterceptorBindingMethod(Method annotatedMethod);

    /**
     * Checks whether the interceptor is defined at the interceptor class. 
     * @return true if inteceptor is defined at the interceptor class
     */
    public boolean isDefinedInInterceptorClass();

    /**
     * Checks whether interceptor is configured with webbeans interceptor
     * definition or not. 
     * @return true if interceptor is configured with webbeans interceptor
     *         definition
     */
    public boolean isDefinedWithWebBeansInterceptor();
    
    /**
     * Sets JSR 299 style interceptor.
     * @param webBeansInterceptor JSR 299 style interceptor
     */
    public void setWebBeansInterceptor(Interceptor<?> webBeansInterceptor);
    
    /**
     * Gets JSR 299 style interceptor bean
     * that is responsible for producing interceptor
     * instance that defines interceptor method.
     * @return interceptor
     */
    public Interceptor<?> getWebBeansInterceptor();
    
    /**
     * Returns true if lifecycle interceptor method
     * false otherwise.
     * @return true if lifecycle interceptor method
     */
    public boolean isLifecycleInterceptor();
}
