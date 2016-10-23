/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InputOutput {

    private DataInputStream input;

    private final DataOutputStream output;

    private final ByteArrayOutputStream byteOutput;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayInputStream byteInput;

    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public InputOutput() {
        byteOutput = new ByteArrayOutputStream();
        output = new DataOutputStream(byteOutput);
    }

    public DataOutputStream output() {
        return output;
    }

    public DataInputStream input() throws IOException {
        rewrite();
        return input;
    }

    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    protected void rewrite() throws IOException {
        output.flush();
        byte[] buffer = byteOutput.toByteArray();
        byteInput = new ByteArrayInputStream(buffer);
        input = new DataInputStream(byteInput);
    }
}
