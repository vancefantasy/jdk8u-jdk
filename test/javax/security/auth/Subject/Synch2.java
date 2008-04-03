/*
 * Copyright 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/*
 * @test
 * @bug 4892913
 * @summary     Subject.getPrivateCredentials not thread-safe against changes to
 *              principals
 * @run main/othervm/policy=Synch2.policy Synch2
 */

import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

public class Synch2 {
    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        Subject subject = new Subject();
        final Set principals = subject.getPrincipals();
        principals.add(new X500Principal("CN=Alice"));
        final Set credentials = subject.getPrivateCredentials();
        credentials.add("Dummy credential");
        new Thread() {
            {
                setDaemon(true);
                start();
            }
            public void run() {
                X500Principal p = new X500Principal("CN=Bob");
                while (true) {
                    principals.add(p);
                    principals.remove(p);
                }
            }
        };
        for (int i = 0; i < 1000; i++) {
            synchronized (credentials) {
                for (Iterator it = credentials.iterator(); it.hasNext(); ) {
                    it.next();
                }
            }
        }
    }
}
