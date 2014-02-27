/*
 * Copyright 2010 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bensoft.apljax.discover;

/**
 * Basic implementation to skip well-known packages and allow only *.class files
 *
 * @author dbennett455@gmail.com
 */
public class AJFilter implements com.impetus.annovention.Filter {
    public static final String[] IGNORED_PACKAGES = {
        "java", "javax",
        "sun", "com.sun",
        "apple", "com.apple",
        "scalaj", "scala.tools.jline", "org.scala_tools.time",
        "javassist", "com.impetus.annovention"
    };

    private String[] ignoredPackages;

    public AJFilter()                         { this.ignoredPackages = IGNORED_PACKAGES; }
    public AJFilter(String[] ignoredPackages) { this.ignoredPackages = ignoredPackages;  }

    /* @see com.impetus.annovention.Filter#accepts(java.lang.String) */
    public final boolean accepts(String filename) {
        if (filename.endsWith(".class")) {
            if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }
            if (!ignoreScan(filename.replace('/', '.'))) {
                return true;
            }
        }
        return false;
    }

    private boolean ignoreScan(String intf) {
        for (String ignored : ignoredPackages) {
            if (intf.startsWith(ignored + ".")) {
                return true;
            }
        }
        return false;
    }
}
