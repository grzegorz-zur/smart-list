/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.dialog;

import com.gzapps.shopping.R;

public class LoadCreatedErrorDialog extends ErrorDialog {

    @Override
    int getMessageId() {
        return R.string.error_loading_created;
    }
}
