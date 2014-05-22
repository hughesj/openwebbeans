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
package org.apache.webbeans.newtests.interceptors.factory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.exception.WebBeansException;
import org.apache.webbeans.newtests.AbstractUnitTest;
import org.apache.webbeans.newtests.interceptors.factory.beans.ClassInterceptedClass;
import org.apache.webbeans.newtests.interceptors.factory.beans.TonsOfMethodsInterceptedClass;
import org.apache.webbeans.proxy.InterceptorDecoratorProxyFactory;

import org.apache.webbeans.proxy.InterceptorHandler;
import org.apache.webbeans.proxy.OwbInterceptorProxy;
import org.apache.webbeans.test.component.intercept.webbeans.TransactionalInterceptor;
import org.apache.webbeans.util.ClassUtil;
import org.apache.webbeans.util.CustomBaseType;
import org.apache.webbeans.util.CustomType;
import org.apache.webbeans.util.ExtendedSpecificClass;
import org.apache.webbeans.util.GenericInterface;
import org.apache.webbeans.util.SpecificClass;
import org.junit.Assert;
import org.junit.Test;


/**
 * Test the {@link org.apache.webbeans.proxy.InterceptorDecoratorProxyFactory}
 */
public class InterceptorDecoratorProxyFactoryTest extends AbstractUnitTest
{

    @Test
    public void testSimpleProxyCreation() throws Exception
    {
        InterceptorDecoratorProxyFactory pf = new InterceptorDecoratorProxyFactory(new WebBeansContext());

        // we take a fresh URLClassLoader to not blur the test classpath with synthetic classes.
        ClassLoader classLoader = new URLClassLoader(new URL[0]);

        List<Method> methods = ClassUtil.getNonPrivateMethods(ClassInterceptedClass.class, true);

        Method[] interceptedMethods = methods.toArray(new Method[methods.size()]);
        Method[] nonInterceptedMethods = null;

        Bean dummyBean = new DummyBean();

        Class<ClassInterceptedClass> proxyClass = pf.createProxyClass(dummyBean, classLoader, ClassInterceptedClass.class, interceptedMethods, nonInterceptedMethods);
        Assert.assertNotNull(proxyClass);

        ClassInterceptedClass internalInstance = new ClassInterceptedClass();
        internalInstance.init();

        TestInterceptorHandler testInvocationHandler = new TestInterceptorHandler(internalInstance);

        ClassInterceptedClass proxy = pf.createProxyInstance(proxyClass, internalInstance, testInvocationHandler);
        Assert.assertNotNull(proxy);

        Assert.assertTrue(proxy instanceof OwbInterceptorProxy);

        proxy.setMeaningOfLife(42);

        Assert.assertEquals(42, proxy.getMeaningOfLife());
        Assert.assertEquals(internalInstance.getFloat(), proxy.getFloat(), 0f);
        Assert.assertEquals('c', proxy.getChar());
        Assert.assertEquals(internalInstance, proxy.getSelf());

        Assert.assertEquals(5, testInvocationHandler.invokedMethodNames.size());
    }

    @Test
    public void testGenericProxyGeneration()
    {
        InterceptorDecoratorProxyFactory pf = new InterceptorDecoratorProxyFactory(new WebBeansContext());

        // we take a fresh URLClassLoader to not blur the test classpath with synthetic classes.
        ClassLoader classLoader = new URLClassLoader(new URL[0]);

        List<Method> methods = ClassUtil.getNonPrivateMethods(ExtendedSpecificClass.class, true);
        for (Iterator<Method> i = methods.iterator(); i.hasNext();)
        {
            if (i.next().isBridge())
            {
                i.remove();
            }
        }

        Method[] interceptedMethods = methods.toArray(new Method[methods.size()]);
        Method[] nonInterceptedMethods = null;

        Bean dummyBean = new DummyBean();

        Class<ExtendedSpecificClass> proxyClass = pf.createProxyClass(dummyBean, classLoader, ExtendedSpecificClass.class, interceptedMethods, nonInterceptedMethods);
        Assert.assertNotNull(proxyClass);

        ExtendedSpecificClass internalInstance = new ExtendedSpecificClass();
        internalInstance.init();

        TestInterceptorHandler testInvocationHandler = new TestInterceptorHandler(internalInstance);

        ExtendedSpecificClass extendedSpecificProxyInstance = pf.createProxyInstance(proxyClass, internalInstance, testInvocationHandler);
        SpecificClass<CustomType> specificProxyInstance = extendedSpecificProxyInstance;
        GenericInterface<CustomBaseType> interfaceProxyInstance = extendedSpecificProxyInstance;
        Assert.assertNotNull(extendedSpecificProxyInstance.newInstance());
        Assert.assertNotNull(specificProxyInstance.newInstance());
        Assert.assertNotNull(interfaceProxyInstance.newInstance());

        Assert.assertTrue(extendedSpecificProxyInstance instanceof OwbInterceptorProxy);
        Assert.assertNotNull(internalInstance.newInstance()); 
    }

    /**
     * We originally did have a bug in our proxy code which
     * blew up if we did have > 127 methods in an intercepted class.
     */
    @Test
    public void testManyMethodsInterceptor() throws Exception
    {
        addInterceptor(TransactionalInterceptor.class);
        startContainer(TonsOfMethodsInterceptedClass.class);

        TonsOfMethodsInterceptedClass instance = getInstance(TonsOfMethodsInterceptedClass.class);

        for (int i = 0; i < 130; i++)
        {
            String methodName = "method" + i;
            Method m = instance.getClass().getDeclaredMethod(methodName);
            m.invoke(instance);
        }
    }

    public static class DummyBean implements Bean {
        @Override
        public Object create(CreationalContext context)
        {
            return null;
        }

        @Override
        public Set<Type> getTypes()
        {
            return null;
        }

        @Override
        public Set<Annotation> getQualifiers()
        {
            return null;
        }

        @Override
        public Class<? extends Annotation> getScope()
        {
            return null;
        }

        @Override
        public String getName()
        {
            return null;
        }

        @Override
        public boolean isNullable()
        {
            return false;
        }

        @Override
        public Set<InjectionPoint> getInjectionPoints()
        {
            return null;
        }

        @Override
        public Class<?> getBeanClass()
        {
            return null;
        }

        @Override
        public Set<Class<? extends Annotation>> getStereotypes()
        {
            return null;
        }

        @Override
        public boolean isAlternative()
        {
            return false;
        }

        @Override
        public void destroy(Object instance, CreationalContext context)
        {
        }
    }

    public static class TestInterceptorHandler implements InterceptorHandler
    {
        public List<String> invokedMethodNames = new ArrayList<String>();

        private Object instance;

        public TestInterceptorHandler(Object instance)
        {
            this.instance = instance;
        }

        @Override
        public Object invoke(Method method, Object[] args)
        {
            Assert.assertFalse(method.isBridge());
            if (!method.getName().equals("toString"))
            {
                invokedMethodNames.add(method.getName());
            }

            System.out.println("TestInvocationHandler got properly invoked for method " + method.getName());

            try
            {
                return method.invoke(instance, args);
            }
            catch (IllegalAccessException e)
            {
                throw new WebBeansException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new WebBeansException(e);
            }
        }
    }
}
