/*
 * Copyright 2001-2002 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
package com.sun.java.swing.plaf.windows;

import javax.swing.JWindow;
import java.awt.Window;
import java.awt.Graphics;

/**
 * A class which tags a window with a particular semantic usage,
 * either tooltip, menu, sub-menu, popup-menu, or comobobox-popup.
 * This is used as a temporary solution for getting native AWT support
 * for transition effects in Windows 98 and Windows 2000.  The native
 * code will interpret the windowType property and automatically
 * implement appropriate animation when the window is shown/hidden.
 * <p>
 * Note that support for transition effects may be supported with a
 * different mechanism in the future and so this class is
 * package-private and targeted for Swing implementation use only.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @author Amy Fowler
 */
class WindowsPopupWindow extends JWindow {

    static final int UNDEFINED_WINDOW_TYPE      = 0;
    static final int TOOLTIP_WINDOW_TYPE        = 1;
    static final int MENU_WINDOW_TYPE           = 2;
    static final int SUBMENU_WINDOW_TYPE        = 3;
    static final int POPUPMENU_WINDOW_TYPE      = 4;
    static final int COMBOBOX_POPUP_WINDOW_TYPE = 5;

    private int windowType;

    WindowsPopupWindow(Window parent) {
        super(parent);
        setFocusableWindowState(false);
    }

    void setWindowType(int type) {
        windowType = type;
    }

    int getWindowType() {
        return windowType;
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void hide() {
        super.hide();
        /** We need to call removeNotify() here because hide() does
         * something only if Component.visible is true. When the app
         * frame is miniaturized, the parent frame of this frame is
         * invisible, causing AWT to believe that this frame
         *  is invisible and causing hide() to do nothing
         */
        removeNotify();
    }

    public void show() {
        super.show();
        this.pack();
    }
}
