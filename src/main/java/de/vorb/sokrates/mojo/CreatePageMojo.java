/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vorb.sokrates.mojo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Create a new page using a wizard.
 */
@Slf4j
@Mojo(name = "create", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
@RequiredArgsConstructor
public class CreatePageMojo extends AbstractMojo {

    private final InputStream in;
    private final PrintStream out;

    @Override
    public void execute() {
        final Scanner inputScanner = new Scanner(in);

        out.print("Title: ");

        final String title = inputScanner.nextLine();

        out.print(String.format("Path: [%s]", title));
    }
}
