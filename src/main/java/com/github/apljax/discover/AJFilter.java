/**
 * This is the Annovention filter for finding an
 * applications JAX-RS annotations
 *
 * @author David Bennett - dbennett455@gmail.com
 *
 */

package com.github.apljax.discover;

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
