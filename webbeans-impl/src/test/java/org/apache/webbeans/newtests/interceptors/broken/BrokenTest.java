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
package org.apache.webbeans.newtests.interceptors.broken;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.webbeans.newtests.AbstractUnitTest;
import org.junit.Test;

public class BrokenTest extends AbstractUnitTest
{

    public static final String PACKAGE_NAME = BrokenTest.class.getPackage().getName();

    @Test
    public void testWarnings()
    {
        Collection<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(BrokenBean.class);
        classes.add(BrokenScope.class);
        classes.add(BrokenAlternative.class);
        classes.add(BrokenName.class);
        
        Collection<URL> xmls = new ArrayList<URL>();
        xmls.add(getXMLUrl(PACKAGE_NAME, "BrokenTest"));
        
        startContainer(classes,xmls);
                
        shutDownContainer();
        
    }

}